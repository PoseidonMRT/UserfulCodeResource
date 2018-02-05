package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class StringColumnConverter implements ColumnConverter<String> {
    public String getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : cursor.getString(i);
    }

    public String getFieldValue(String str) {
        return str;
    }

    public Object fieldValue2ColumnValue(String str) {
        return str;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
