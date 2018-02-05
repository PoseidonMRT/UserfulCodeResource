package com.mqunar.tools;

import android.content.Context;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class CheckUtils {
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (!(obj instanceof CharSequence)) {
            if (obj.getClass().isArray()) {
                if (obj instanceof Object[]) {
                    if (((Object[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof int[]) {
                    if (((int[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof long[]) {
                    if (((long[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof short[]) {
                    if (((short[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof double[]) {
                    if (((double[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof float[]) {
                    if (((float[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof boolean[]) {
                    if (((boolean[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof char[]) {
                    if (((char[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof byte[]) {
                    if (((byte[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                }
            } else if (obj instanceof EmptyCheckable) {
                return ((EmptyCheckable) obj).isEmpty();
            }
            return false;
        } else if (((CharSequence) obj).length() != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isExist(Object obj) {
        return !isEmpty(obj);
    }

    public static <T> boolean isContainsEmpty(T... tArr) {
        if (isEmpty(tArr)) {
            return true;
        }
        for (Object isEmpty : tArr) {
            if (isEmpty(isEmpty)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentRuntimeValue() {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getMethod("get", new Class[]{String.class, String.class});
            if (method == null) {
                return "WTF?!";
            }
            try {
                String str = (String) method.invoke(cls, new Object[]{"persist.sys.dalvik.vm.lib", "Dalvik"});
                if ("libdvm.so".equals(str)) {
                    return "Dalvik";
                }
                if ("libart.so".equals(str)) {
                    return "ART";
                }
                if ("libartd.so".equals(str)) {
                    return "ART debug build";
                }
                return str;
            } catch (IllegalAccessException e) {
                return "IllegalAccessException";
            } catch (IllegalArgumentException e2) {
                return "IllegalArgumentException";
            } catch (InvocationTargetException e3) {
                return "InvocationTargetException";
            }
        } catch (NoSuchMethodException e4) {
            return "SystemProperties.get(String key, String def) method is not found";
        } catch (Throwable th) {
            return "SystemProperties class is not found";
        }
    }

    public static boolean isOdd(int i) {
        return i % 2 != 0;
    }

    public static boolean isEven(int i) {
        return i % 2 == 0;
    }

    public static boolean isContainsEnum(Enum<?>[] enumArr, Enum<?> enumR) {
        if (isEmpty(enumArr)) {
            return false;
        }
        for (Enum<?> enumR2 : enumArr) {
            if (enumR2 == enumR) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasRootAccess(Context context) {
        try {
            if (runScriptAsRoot(context, "exit 0") == 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static int runScriptAsRoot(Context context, String str) {
        a aVar = new a(new File(context.getCacheDir(), "secopt.sh"), str, new StringBuilder());
        aVar.start();
        try {
            aVar.join(40000);
            if (aVar.isAlive()) {
                aVar.interrupt();
                aVar.join(150);
                aVar.destroy();
                aVar.join(50);
            }
        } catch (InterruptedException e) {
        }
        return aVar.a;
    }
}
