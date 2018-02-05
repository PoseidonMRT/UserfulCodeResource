package com.mqunar.hy.res.utils;

import java.io.Serializable;

public enum EnumUtils {
    ;

    public interface ITypeCode extends Serializable {
        int getCode();
    }

    public interface ITypeDesc extends Serializable {
        String getDesc();
    }

    public interface IType extends ITypeCode, ITypeDesc {
    }

    public static <T extends ITypeCode> T getTypeOf(Class<T> cls, int i) {
        try {
            for (T t : (ITypeCode[]) cls.getMethod("values", (Class[]) null).invoke(null, new Object[0])) {
                if (t.getCode() == i) {
                    return t;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static <T extends ITypeCode> T optTypeOf(Class<T> cls, int i, T t) {
        T typeOf = getTypeOf(cls, i);
        return typeOf == null ? t : typeOf;
    }

    public static <T extends ITypeDesc> T getFirstTypeOf(Class<T> cls, String str) {
        try {
            for (T t : (ITypeDesc[]) cls.getMethod("values", (Class[]) null).invoke(null, new Object[0])) {
                if (t.getDesc().equalsIgnoreCase(str)) {
                    return t;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }
}
