package com.mqunar.xutils.dbutils.sqlite;

import com.mqunar.xutils.dbutils.DbUtils;
import com.mqunar.xutils.dbutils.exception.DbException;
import com.mqunar.xutils.dbutils.table.Column;
import com.mqunar.xutils.dbutils.table.ColumnUtils;
import com.mqunar.xutils.dbutils.table.Finder;
import com.mqunar.xutils.dbutils.table.Id;
import com.mqunar.xutils.dbutils.table.KeyValue;
import com.mqunar.xutils.dbutils.table.Table;
import com.mqunar.xutils.dbutils.table.TableUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SqlInfoBuilder {
    private SqlInfoBuilder() {
    }

    public static SqlInfo buildInsertSqlInfo(DbUtils dbUtils, Object obj) {
        List<KeyValue> entity2KeyValueList = entity2KeyValueList(dbUtils, obj);
        if (entity2KeyValueList.size() == 0) {
            return null;
        }
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO ");
        stringBuffer.append(TableUtils.getTableName(obj.getClass()));
        stringBuffer.append(" (");
        for (KeyValue keyValue : entity2KeyValueList) {
            stringBuffer.append(keyValue.key).append(",");
            sqlInfo.addBindArgWithoutConverter(keyValue.value);
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(") VALUES (");
        int size = entity2KeyValueList.size();
        for (int i = 0; i < size; i++) {
            stringBuffer.append("?,");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(")");
        sqlInfo.setSql(stringBuffer.toString());
        return sqlInfo;
    }

    public static SqlInfo buildReplaceSqlInfo(DbUtils dbUtils, Object obj) {
        List<KeyValue> entity2KeyValueList = entity2KeyValueList(dbUtils, obj);
        if (entity2KeyValueList.size() == 0) {
            return null;
        }
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("REPLACE INTO ");
        stringBuffer.append(TableUtils.getTableName(obj.getClass()));
        stringBuffer.append(" (");
        for (KeyValue keyValue : entity2KeyValueList) {
            stringBuffer.append(keyValue.key).append(",");
            sqlInfo.addBindArgWithoutConverter(keyValue.value);
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(") VALUES (");
        int size = entity2KeyValueList.size();
        for (int i = 0; i < size; i++) {
            stringBuffer.append("?,");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(")");
        sqlInfo.setSql(stringBuffer.toString());
        return sqlInfo;
    }

    private static String buildDeleteSqlByTableName(String str) {
        return "DELETE FROM " + str;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils dbUtils, Object obj) {
        SqlInfo sqlInfo = new SqlInfo();
        Table table = Table.get(dbUtils, obj.getClass());
        Id id = table.id;
        Object columnValue = id.getColumnValue(obj);
        if (columnValue == null) {
            throw new DbException("this entity[" + obj.getClass() + "]'s id value is null");
        }
        StringBuilder stringBuilder = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
        stringBuilder.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", columnValue));
        sqlInfo.setSql(stringBuilder.toString());
        return sqlInfo;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils dbUtils, Class<?> cls, Object obj) {
        SqlInfo sqlInfo = new SqlInfo();
        Table table = Table.get(dbUtils, cls);
        Id id = table.id;
        if (obj == null) {
            throw new DbException("this entity[" + cls + "]'s id value is null");
        }
        StringBuilder stringBuilder = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
        stringBuilder.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", obj));
        sqlInfo.setSql(stringBuilder.toString());
        return sqlInfo;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils dbUtils, Class<?> cls, WhereBuilder whereBuilder) {
        StringBuilder stringBuilder = new StringBuilder(buildDeleteSqlByTableName(Table.get(dbUtils, cls).tableName));
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            stringBuilder.append(" WHERE ").append(whereBuilder.toString());
        }
        return new SqlInfo(stringBuilder.toString());
    }

    public static SqlInfo buildUpdateSqlInfo(DbUtils dbUtils, Object obj, String... strArr) {
        List<KeyValue> entity2KeyValueList = entity2KeyValueList(dbUtils, obj);
        if (entity2KeyValueList.size() == 0) {
            return null;
        }
        HashSet hashSet;
        if (strArr == null || strArr.length <= 0) {
            hashSet = null;
        } else {
            Collection hashSet2 = new HashSet(strArr.length);
            Collections.addAll(hashSet2, strArr);
            hashSet = hashSet2;
        }
        Table table = Table.get(dbUtils, obj.getClass());
        Id id = table.id;
        Object columnValue = id.getColumnValue(obj);
        if (columnValue == null) {
            throw new DbException("this entity[" + obj.getClass() + "]'s id value is null");
        }
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(table.tableName);
        stringBuffer.append(" SET ");
        for (KeyValue keyValue : entity2KeyValueList) {
            if (hashSet == null || hashSet.contains(keyValue.key)) {
                stringBuffer.append(keyValue.key).append("=?,");
                sqlInfo.addBindArgWithoutConverter(keyValue.value);
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", columnValue));
        sqlInfo.setSql(stringBuffer.toString());
        return sqlInfo;
    }

    public static SqlInfo buildUpdateSqlInfo(DbUtils dbUtils, Object obj, WhereBuilder whereBuilder, String... strArr) {
        List<KeyValue> entity2KeyValueList = entity2KeyValueList(dbUtils, obj);
        if (entity2KeyValueList.size() == 0) {
            return null;
        }
        HashSet hashSet;
        if (strArr == null || strArr.length <= 0) {
            hashSet = null;
        } else {
            Collection hashSet2 = new HashSet(strArr.length);
            Collections.addAll(hashSet2, strArr);
            hashSet = hashSet2;
        }
        String tableName = TableUtils.getTableName(obj.getClass());
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(tableName);
        stringBuffer.append(" SET ");
        for (KeyValue keyValue : entity2KeyValueList) {
            if (hashSet == null || hashSet.contains(keyValue.key)) {
                stringBuffer.append(keyValue.key).append("=?,");
                sqlInfo.addBindArgWithoutConverter(keyValue.value);
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            stringBuffer.append(" WHERE ").append(whereBuilder.toString());
        }
        sqlInfo.setSql(stringBuffer.toString());
        return sqlInfo;
    }

    public static SqlInfo buildCreateTableSqlInfo(DbUtils dbUtils, Class<?> cls) {
        Table table = Table.get(dbUtils, cls);
        Id id = table.id;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
        stringBuffer.append(table.tableName);
        stringBuffer.append(" ( ");
        if (id.isAutoIncrement()) {
            stringBuffer.append("\"").append(id.getColumnName()).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        } else {
            stringBuffer.append("\"").append(id.getColumnName()).append("\"  ").append(id.getColumnDbType()).append(" PRIMARY KEY,");
        }
        for (Column column : table.columnMap.values()) {
            if (!(column instanceof Finder)) {
                stringBuffer.append("\"").append(column.getColumnName()).append("\"  ");
                stringBuffer.append(column.getColumnDbType());
                if (ColumnUtils.isUnique(column.getColumnField())) {
                    stringBuffer.append(" UNIQUE");
                }
                if (ColumnUtils.isNotNull(column.getColumnField())) {
                    stringBuffer.append(" NOT NULL");
                }
                String check = ColumnUtils.getCheck(column.getColumnField());
                if (check != null) {
                    stringBuffer.append(" CHECK(").append(check).append(")");
                }
                stringBuffer.append(",");
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(" )");
        return new SqlInfo(stringBuffer.toString());
    }

    private static KeyValue column2KeyValue(Object obj, Column column) {
        String columnName = column.getColumnName();
        if (columnName == null) {
            return null;
        }
        Object columnValue = column.getColumnValue(obj);
        if (columnValue == null) {
            columnValue = column.getDefaultValue();
        }
        return new KeyValue(columnName, columnValue);
    }

    public static List<KeyValue> entity2KeyValueList(DbUtils dbUtils, Object obj) {
        List<KeyValue> arrayList = new ArrayList();
        Table table = Table.get(dbUtils, obj.getClass());
        Id id = table.id;
        if (!id.isAutoIncrement()) {
            arrayList.add(new KeyValue(id.getColumnName(), id.getColumnValue(obj)));
        }
        for (Column column : table.columnMap.values()) {
            if (!(column instanceof Finder)) {
                KeyValue column2KeyValue = column2KeyValue(obj, column);
                if (column2KeyValue != null) {
                    arrayList.add(column2KeyValue);
                }
            }
        }
        return arrayList;
    }
}
