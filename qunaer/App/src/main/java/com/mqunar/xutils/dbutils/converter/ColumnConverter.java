package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public interface ColumnConverter<T> {
    Object fieldValue2ColumnValue(T t);

    ColumnDbType getColumnDbType();

    T getFieldValue(Cursor cursor, int i);

    T getFieldValue(String str);
}
