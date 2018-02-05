package com.mqunar.xutils.dbutils.table;

import android.text.TextUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.annotation.Check;
import com.mqunar.xutils.dbutils.annotation.Column;
import com.mqunar.xutils.dbutils.annotation.Encrypt;
import com.mqunar.xutils.dbutils.annotation.Finder;
import com.mqunar.xutils.dbutils.annotation.Foreign;
import com.mqunar.xutils.dbutils.annotation.Id;
import com.mqunar.xutils.dbutils.annotation.NotNull;
import com.mqunar.xutils.dbutils.annotation.Transient;
import com.mqunar.xutils.dbutils.annotation.Unique;
import com.mqunar.xutils.dbutils.converter.ColumnConverter;
import com.mqunar.xutils.dbutils.converter.ColumnConverterFactory;
import com.mqunar.xutils.dbutils.sqlite.FinderLazyLoader;
import com.mqunar.xutils.dbutils.sqlite.ForeignLazyLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;

public class ColumnUtils {
    private static final HashSet<String> DB_PRIMITIVE_TYPES = new HashSet(14);

    private ColumnUtils() {
    }

    static {
        DB_PRIMITIVE_TYPES.add(Integer.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Long.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Short.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Byte.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Float.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Double.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Integer.class.getName());
        DB_PRIMITIVE_TYPES.add(Long.class.getName());
        DB_PRIMITIVE_TYPES.add(Short.class.getName());
        DB_PRIMITIVE_TYPES.add(Byte.class.getName());
        DB_PRIMITIVE_TYPES.add(Float.class.getName());
        DB_PRIMITIVE_TYPES.add(Double.class.getName());
        DB_PRIMITIVE_TYPES.add(String.class.getName());
        DB_PRIMITIVE_TYPES.add(byte[].class.getName());
    }

    public static boolean isDbPrimitiveType(Class<?> cls) {
        return DB_PRIMITIVE_TYPES.contains(cls.getName());
    }

    public static Method getColumnGetMethod(Class<?> cls, Field field) {
        String name = field.getName();
        Method method = null;
        if (field.getType() == Boolean.TYPE) {
            method = getBooleanColumnGetMethod(cls, name);
        }
        if (method == null) {
            name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                method = cls.getDeclaredMethod(name, new Class[0]);
            } catch (NoSuchMethodException e) {
                QLog.d(name + " not exist", new Object[0]);
            }
        }
        if (method != null || Object.class.equals(cls.getSuperclass())) {
            return method;
        }
        return getColumnGetMethod(cls.getSuperclass(), field);
    }

    public static Method getColumnSetMethod(Class<?> cls, Field field) {
        String name = field.getName();
        Method method = null;
        if (field.getType() == Boolean.TYPE) {
            method = getBooleanColumnSetMethod(cls, field);
        }
        if (method == null) {
            name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                method = cls.getDeclaredMethod(name, new Class[]{field.getType()});
            } catch (NoSuchMethodException e) {
                QLog.d(name + " not exist", new Object[0]);
            }
        }
        if (method != null || Object.class.equals(cls.getSuperclass())) {
            return method;
        }
        return getColumnSetMethod(cls.getSuperclass(), field);
    }

    public static String getColumnNameByField(Field field) {
        Column column = (Column) field.getAnnotation(Column.class);
        if (column != null && !TextUtils.isEmpty(column.column())) {
            return column.column();
        }
        Id id = (Id) field.getAnnotation(Id.class);
        if (id != null && !TextUtils.isEmpty(id.column())) {
            return id.column();
        }
        Foreign foreign = (Foreign) field.getAnnotation(Foreign.class);
        if (foreign != null && !TextUtils.isEmpty(foreign.column())) {
            return foreign.column();
        }
        if (((Finder) field.getAnnotation(Finder.class)) != null) {
            return field.getName();
        }
        return field.getName();
    }

    public static String getForeignColumnNameByField(Field field) {
        Foreign foreign = (Foreign) field.getAnnotation(Foreign.class);
        if (foreign != null) {
            return foreign.foreign();
        }
        return field.getName();
    }

    public static String getColumnDefaultValue(Field field) {
        Column column = (Column) field.getAnnotation(Column.class);
        if (column == null || TextUtils.isEmpty(column.defaultValue())) {
            return null;
        }
        return column.defaultValue();
    }

    public static boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    public static boolean isForeign(Field field) {
        return field.getAnnotation(Foreign.class) != null;
    }

    public static boolean isFinder(Field field) {
        return field.getAnnotation(Finder.class) != null;
    }

    public static boolean isUnique(Field field) {
        return field.getAnnotation(Unique.class) != null;
    }

    public static boolean isNotNull(Field field) {
        return field.getAnnotation(NotNull.class) != null;
    }

    public static String getCheck(Field field) {
        Check check = (Check) field.getAnnotation(Check.class);
        if (check != null) {
            return check.value();
        }
        return null;
    }

    public static Class<?> getForeignEntityType(Foreign foreign) {
        Class<?> type = foreign.getColumnField().getType();
        if (type.equals(ForeignLazyLoader.class) || type.equals(List.class)) {
            return (Class) ((ParameterizedType) foreign.getColumnField().getGenericType()).getActualTypeArguments()[0];
        }
        return type;
    }

    public static Class<?> getFinderTargetEntityType(Finder finder) {
        Class<?> type = finder.getColumnField().getType();
        if (type.equals(FinderLazyLoader.class) || type.equals(List.class)) {
            return (Class) ((ParameterizedType) finder.getColumnField().getGenericType()).getActualTypeArguments()[0];
        }
        return type;
    }

    public static Object convert2DbColumnValueIfNeeded(Object obj) {
        if (obj == null) {
            return obj;
        }
        Class cls = obj.getClass();
        if (isDbPrimitiveType(cls)) {
            return obj;
        }
        ColumnConverter columnConverter = ColumnConverterFactory.getColumnConverter(cls);
        if (columnConverter != null) {
            return columnConverter.fieldValue2ColumnValue(obj);
        }
        return obj;
    }

    private static boolean isStartWithIs(String str) {
        return str != null && str.startsWith("is");
    }

    private static Method getBooleanColumnGetMethod(Class<?> cls, String str) {
        String str2 = "is" + str.substring(0, 1).toUpperCase() + str.substring(1);
        if (!isStartWithIs(str)) {
            str = str2;
        }
        try {
            return cls.getDeclaredMethod(str, new Class[0]);
        } catch (NoSuchMethodException e) {
            QLog.d(str + " not exist", new Object[0]);
            return null;
        }
    }

    private static Method getBooleanColumnSetMethod(Class<?> cls, Field field) {
        String name = field.getName();
        if (isStartWithIs(field.getName())) {
            name = "set" + name.substring(2, 3).toUpperCase() + name.substring(3);
        } else {
            name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        try {
            return cls.getDeclaredMethod(name, new Class[]{field.getType()});
        } catch (NoSuchMethodException e) {
            QLog.d(name + " not exist", new Object[0]);
            return null;
        }
    }

    public static String getColumnEncryptType(Field field) {
        Encrypt encrypt = (Encrypt) field.getAnnotation(Encrypt.class);
        if (encrypt == null || TextUtils.isEmpty(encrypt.type())) {
            return field.getName();
        }
        return encrypt.type();
    }
}
