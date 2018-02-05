package com.mqunar.tools.log;

import android.content.Context;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class QLog {
    private static final List<Clerk> a = new CopyOnWriteArrayList();
    private static final Clerk b = new a();

    public interface Clerk {
        void crash(Throwable th, String str);

        void d(String str, Object... objArr);

        void e(String str, Object... objArr);

        void e(Throwable th);

        void e(Throwable th, String str, Object... objArr);

        Map<String, String> getCrashes();

        void i(String str, Object... objArr);

        void removeCrash(String str);

        void v(String str, Object... objArr);

        void w(String str, Object... objArr);
    }

    public interface Markable {
        void tag(String str);
    }

    public final class GlobalContext {
        private static Object a() {
            Object invoke;
            Object obj = null;
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
                declaredMethod.setAccessible(true);
                invoke = declaredMethod.invoke(null, new Object[0]);
                if (invoke == null || !cls.isAssignableFrom(invoke.getClass())) {
                    invoke = null;
                }
                obj = invoke;
            } catch (Throwable th) {
            }
            if (obj == null) {
                try {
                    cls = Class.forName("android.app.ActivityThread");
                    Field declaredField = cls.getDeclaredField("sMainThreadHandler");
                    declaredField.setAccessible(true);
                    Object obj2 = declaredField.get(null);
                    try {
                        declaredField = obj2.getClass().getDeclaredField("this$0");
                        declaredField.setAccessible(true);
                        invoke = declaredField.get(obj2);
                        if (invoke == null || !cls.isAssignableFrom(invoke.getClass())) {
                            invoke = obj;
                        }
                        obj = invoke;
                    } catch (Throwable th2) {
                    }
                    if (obj == null) {
                        Field[] declaredFields = obj2.getClass().getDeclaredFields();
                        if (declaredFields != null) {
                            for (int i = 0; i < declaredFields.length; i++) {
                                declaredFields[i].setAccessible(true);
                                invoke = declaredFields[i].get(obj2);
                                if (invoke != null && cls.isAssignableFrom(invoke.getClass())) {
                                    return invoke;
                                }
                            }
                        }
                    }
                } catch (Throwable th3) {
                    return obj;
                }
            }
            return obj;
        }

        public static Context getAppContext() {
            Context context = null;
            try {
                Method declaredMethod = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]);
                declaredMethod.setAccessible(true);
                context = (Context) declaredMethod.invoke(null, new Object[0]);
            } catch (Throwable th) {
            }
            if (context == null) {
                Object a = a();
                if (a != null) {
                    try {
                        Field declaredField = Class.forName("android.app.ActivityThread").getDeclaredField("mInitialApplication");
                        declaredField.setAccessible(true);
                        context = (Context) declaredField.get(a);
                    } catch (Throwable th2) {
                    }
                }
            }
            if (context == null) {
                try {
                    declaredMethod = Class.forName("com.mqunar.core.QunarApkLoader").getMethod("getAppContext", new Class[0]);
                    declaredMethod.setAccessible(true);
                    context = (Context) declaredMethod.invoke(null, new Object[0]);
                } catch (Throwable th3) {
                }
            }
            if (context != null) {
                return context;
            }
            try {
                declaredMethod = Class.forName("com.mqunar.core.basectx.application.QApplication").getMethod("getContext", new Class[0]);
                declaredMethod.setAccessible(true);
                return (Context) declaredMethod.invoke(null, new Object[0]);
            } catch (Throwable th4) {
                return context;
            }
        }
    }

    public static long printDuration(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        d("Performance", stackTraceElement.getFileName() + "_" + stackTraceElement.getLineNumber() + ":" + (currentTimeMillis - j), new Object[0]);
        return currentTimeMillis;
    }

    public static void printLogPos(String str) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        d(str, stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber() + "::" + stackTraceElement.getMethodName() + "()", new Object[0]);
    }

    public static void v(String str, String str2, Object... objArr) {
        a(str);
        b.v(str2, objArr);
    }

    public static void v(String str, Object... objArr) {
        b.v(str, objArr);
    }

    public static void d(String str, String str2, Object... objArr) {
        a(str);
        b.d(str2, objArr);
    }

    public static void d(String str, Object... objArr) {
        b.d(str, objArr);
    }

    public static void i(String str, String str2, Object... objArr) {
        a(str);
        b.i(str2, objArr);
    }

    public static void i(String str, Object... objArr) {
        b.i(str, objArr);
    }

    public static void w(String str, String str2, Object... objArr) {
        a(str);
        b.w(str2, objArr);
    }

    public static void w(String str, Object... objArr) {
        b.w(str, objArr);
    }

    public static void e(String str, String str2, Object... objArr) {
        a(str);
        b.e(str2, objArr);
    }

    public static void e(String str, Object... objArr) {
        b.e(str, objArr);
    }

    public static void e(Throwable th, String str, Object... objArr) {
        b.e(th, str, objArr);
    }

    public static void e(Throwable th) {
        b.e(th);
    }

    public static void crash(Throwable th, String str) {
        b.crash(th, str);
    }

    public static Map<String, String> getCrashes() {
        return b.getCrashes();
    }

    public static void removeCrash(String str) {
        b.removeCrash(str);
    }

    public static void open(Clerk clerk) {
        if (clerk != null && !a.contains(clerk)) {
            a.add(clerk);
        }
    }

    public static void close(Clerk clerk) {
        int size = a.size();
        for (int i = 0; i < size; i++) {
            if (a.get(i) == clerk) {
                a.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot close clerk which is not open: " + clerk);
    }

    public static void closeAll() {
        a.clear();
    }

    private static void a(String str) {
        for (Clerk clerk : a) {
            if (clerk instanceof Markable) {
                ((Markable) clerk).tag(str);
            }
        }
    }

    static {
        QLogInit qLogInit = new QLogInit();
    }

    private QLog() {
    }
}
