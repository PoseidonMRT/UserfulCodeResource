package com.mqunar.xutils.dbutils.table;

import android.text.TextUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.annotation.Id;
import com.mqunar.xutils.dbutils.annotation.Table;
import com.mqunar.xutils.dbutils.converter.ColumnConverterFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FilenameUtils;

public class TableUtils {
    private static ConcurrentHashMap<String, HashMap<String, Column>> entityColumnsMap = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, Id> entityIdMap = new ConcurrentHashMap();

    private TableUtils() {
    }

    public static String getTableName(Class<?> cls) {
        Table table = (Table) cls.getAnnotation(Table.class);
        if (table == null || TextUtils.isEmpty(table.name())) {
            return cls.getName().replace(FilenameUtils.EXTENSION_SEPARATOR, '_');
        }
        return table.name();
    }

    public static String getExecAfterTableCreated(Class<?> cls) {
        Table table = (Table) cls.getAnnotation(Table.class);
        if (table != null) {
            return table.execAfterTableCreated();
        }
        return null;
    }

    static synchronized HashMap<String, Column> getColumnMap(Class<?> cls) {
        HashMap<String, Column> hashMap;
        synchronized (TableUtils.class) {
            if (entityColumnsMap.containsKey(cls.getName())) {
                hashMap = (HashMap) entityColumnsMap.get(cls.getName());
            } else {
                hashMap = new HashMap();
                addColumns2Map(cls, getPrimaryKeyFieldName(cls), hashMap);
                entityColumnsMap.put(cls.getName(), hashMap);
            }
        }
        return hashMap;
    }

    private static void addColumns2Map(Class<?> cls, String str, HashMap<String, Column> hashMap) {
        if (!Object.class.equals(cls)) {
            try {
                for (Field field : cls.getDeclaredFields()) {
                    if (!(ColumnUtils.isTransient(field) || Modifier.isStatic(field.getModifiers()))) {
                        if (ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
                            if (!field.getName().equals(str)) {
                                Column column = new Column(cls, field);
                                if (!hashMap.containsKey(column.getColumnName())) {
                                    hashMap.put(column.getColumnName(), column);
                                }
                            }
                        } else if (ColumnUtils.isForeign(field)) {
                            Foreign foreign = new Foreign(cls, field);
                            if (!hashMap.containsKey(foreign.getColumnName())) {
                                hashMap.put(foreign.getColumnName(), foreign);
                            }
                        } else if (ColumnUtils.isFinder(field)) {
                            Finder finder = new Finder(cls, field);
                            if (!hashMap.containsKey(finder.getColumnName())) {
                                hashMap.put(finder.getColumnName(), finder);
                            }
                        }
                    }
                }
                if (!Object.class.equals(cls.getSuperclass())) {
                    addColumns2Map(cls.getSuperclass(), str, hashMap);
                }
            } catch (Throwable th) {
                QLog.e(th);
            }
        }
    }

    static Column getColumnOrId(Class<?> cls, String str) {
        if (getPrimaryKeyColumnName(cls).equals(str)) {
            return getId(cls);
        }
        return (Column) getColumnMap(cls).get(str);
    }

    static synchronized Id getId(Class<?> cls) {
        Id id;
        int i = 0;
        synchronized (TableUtils.class) {
            if (Object.class.equals(cls)) {
                throw new RuntimeException("field 'id' not found");
            }
            if (entityIdMap.containsKey(cls.getName())) {
                id = (Id) entityIdMap.get(cls.getName());
            } else {
                Field field;
                Field[] declaredFields = cls.getDeclaredFields();
                if (declaredFields != null) {
                    int length;
                    for (Field field2 : declaredFields) {
                        if (field2.getAnnotation(Id.class) != null) {
                            break;
                        }
                    }
                    field2 = null;
                    if (field2 == null) {
                        length = declaredFields.length;
                        while (i < length) {
                            Field field3 = declaredFields[i];
                            if ("id".equals(field3.getName()) || "_id".equals(field3.getName())) {
                                field2 = field3;
                                break;
                            }
                            i++;
                        }
                    }
                } else {
                    field2 = null;
                }
                if (field2 == null) {
                    id = getId(cls.getSuperclass());
                } else {
                    id = new Id(cls, field2);
                    entityIdMap.put(cls.getName(), id);
                }
            }
        }
        return id;
    }

    private static String getPrimaryKeyFieldName(Class<?> cls) {
        Id id = getId(cls);
        return id == null ? null : id.getColumnField().getName();
    }

    private static String getPrimaryKeyColumnName(Class<?> cls) {
        Id id = getId(cls);
        return id == null ? null : id.getColumnName();
    }
}
