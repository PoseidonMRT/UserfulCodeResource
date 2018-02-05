package com.mqunar.xutils.dbutils.converter;

import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ColumnConverterFactory {
    private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map = new ConcurrentHashMap();

    private ColumnConverterFactory() {
    }

    public static ColumnConverter getColumnConverter(Class cls) {
        if (columnType_columnConverter_map.containsKey(cls.getName())) {
            return (ColumnConverter) columnType_columnConverter_map.get(cls.getName());
        }
        if (ColumnConverter.class.isAssignableFrom(cls)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) cls.newInstance();
                if (columnConverter == null) {
                    return columnConverter;
                }
                columnType_columnConverter_map.put(cls.getName(), columnConverter);
                return columnConverter;
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public static ColumnDbType getDbColumnType(Class cls) {
        ColumnConverter columnConverter = getColumnConverter(cls);
        if (columnConverter != null) {
            return columnConverter.getColumnDbType();
        }
        return ColumnDbType.TEXT;
    }

    public static void registerColumnConverter(Class cls, ColumnConverter columnConverter) {
        columnType_columnConverter_map.put(cls.getName(), columnConverter);
    }

    public static boolean isSupportColumnConverter(Class cls) {
        if (columnType_columnConverter_map.containsKey(cls.getName())) {
            return true;
        }
        if (ColumnConverter.class.isAssignableFrom(cls)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) cls.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(cls.getName(), columnConverter);
                }
                return columnConverter == null;
            } catch (Throwable th) {
            }
        }
        return false;
    }

    static {
        BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
        columnType_columnConverter_map.put(Boolean.TYPE.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(byte[].class.getName(), new ByteArrayColumnConverter());
        ByteColumnConverter byteColumnConverter = new ByteColumnConverter();
        columnType_columnConverter_map.put(Byte.TYPE.getName(), byteColumnConverter);
        columnType_columnConverter_map.put(Byte.class.getName(), byteColumnConverter);
        CharColumnConverter charColumnConverter = new CharColumnConverter();
        columnType_columnConverter_map.put(Character.TYPE.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Date.class.getName(), new DateColumnConverter());
        DoubleColumnConverter doubleColumnConverter = new DoubleColumnConverter();
        columnType_columnConverter_map.put(Double.TYPE.getName(), doubleColumnConverter);
        columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);
        FloatColumnConverter floatColumnConverter = new FloatColumnConverter();
        columnType_columnConverter_map.put(Float.TYPE.getName(), floatColumnConverter);
        columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);
        IntegerColumnConverter integerColumnConverter = new IntegerColumnConverter();
        columnType_columnConverter_map.put(Integer.TYPE.getName(), integerColumnConverter);
        columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);
        LongColumnConverter longColumnConverter = new LongColumnConverter();
        columnType_columnConverter_map.put(Long.TYPE.getName(), longColumnConverter);
        columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);
        ShortColumnConverter shortColumnConverter = new ShortColumnConverter();
        columnType_columnConverter_map.put(Short.TYPE.getName(), shortColumnConverter);
        columnType_columnConverter_map.put(Short.class.getName(), shortColumnConverter);
        columnType_columnConverter_map.put(java.sql.Date.class.getName(), new SqlDateColumnConverter());
        columnType_columnConverter_map.put(String.class.getName(), new StringColumnConverter());
    }
}
