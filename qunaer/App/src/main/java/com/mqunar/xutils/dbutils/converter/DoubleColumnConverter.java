package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class DoubleColumnConverter implements ColumnConverter<Double> {
    public Double getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Double.valueOf(cursor.getDouble(i));
    }

    public Double getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Double.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Double d) {
        return d;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
