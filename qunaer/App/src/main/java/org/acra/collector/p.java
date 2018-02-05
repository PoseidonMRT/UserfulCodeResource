package org.acra.collector;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;

final class p {
    @NonNull
    public static String a(@NonNull Class<?> cls, @Nullable String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : cls.getFields()) {
            if (!(str == null || str.length() == 0)) {
                stringBuilder.append(str).append(FilenameUtils.EXTENSION_SEPARATOR);
            }
            stringBuilder.append(field.getName()).append('=');
            try {
                Object obj = field.get(null);
                if (obj != null) {
                    if (field.getType().isArray()) {
                        stringBuilder.append(Arrays.toString((Object[]) obj));
                    } else {
                        stringBuilder.append(obj.toString());
                    }
                }
            } catch (IllegalArgumentException e) {
                stringBuilder.append("N/A");
            } catch (IllegalAccessException e2) {
                stringBuilder.append("N/A");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String a(@NonNull Class<?> cls) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Method method : cls.getMethods()) {
            if (method.getParameterTypes().length == 0 && ((method.getName().startsWith("get") || method.getName().startsWith("is")) && !"getClass".equals(method.getName()))) {
                try {
                    stringBuilder.append(method.getName());
                    stringBuilder.append('=');
                    stringBuilder.append(method.invoke(null, (Object[]) null));
                    stringBuilder.append('\n');
                } catch (IllegalArgumentException e) {
                } catch (InvocationTargetException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String b(@NonNull Class<?> cls) {
        return a(cls, "");
    }
}
