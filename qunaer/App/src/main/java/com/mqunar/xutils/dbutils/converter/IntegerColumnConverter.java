package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class IntegerColumnConverter implements ColumnConverter<Integer> {
    public Integer getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Integer.valueOf(cursor.getInt(i));
    }

    public Integer getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Integer.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Integer num) {
        return num;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
