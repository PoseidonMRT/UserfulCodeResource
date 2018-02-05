package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class LongColumnConverter implements ColumnConverter<Long> {
    public Long getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Long.valueOf(cursor.getLong(i));
    }

    public Long getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Long.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Long l) {
        return l;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
