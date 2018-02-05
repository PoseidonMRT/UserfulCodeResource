package com.mqunar.hy.res.libtask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ReflectUtils {
    ReflectUtils() {
    }

    private static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        Method method = null;
        while (cls != null) {
            try {
                method = cls.getDeclaredMethod(str, clsArr);
            } catch (Exception e) {
            }
            if (method != null) {
                method.setAccessible(true);
                break;
            }
            cls = cls.getSuperclass();
        }
        return method;
    }

    private static Field getField(Class<?> cls, String str) {
        Field field = null;
        while (cls != null) {
            try {
                field = cls.getDeclaredField(str);
            } catch (Exception e) {
            }
            if (field != null) {
                field.setAccessible(true);
                break;
            }
            cls = cls.getSuperclass();
        }
        return field;
    }

    public static Object invokeStaticMethod(String str, String str2, Class<?>[] clsArr, Object[] objArr) {
        Object obj = null;
        try {
            obj = getMethod(Class.forName(str), str2, clsArr).invoke(null, objArr);
        } catch (Exception e) {
        }
        return obj;
    }

    public static Object invokeMethod(String str, Object obj, Class<?>[] clsArr, Object[] objArr) {
        try {
            return getMethod(obj.getClass(), str, clsArr).invoke(obj, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getField(Object obj, String str) {
        try {
            Field field = getField(obj.getClass(), str);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean setField(String str, Object obj, Object obj2) {
        try {
            return setField(obj.getClass(), str, obj, obj2);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean setField(Class<?> cls, String str, Object obj, Object obj2) {
        try {
            Field field = getField((Class) cls, str);
            field.setAccessible(true);
            field.set(obj, obj2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
