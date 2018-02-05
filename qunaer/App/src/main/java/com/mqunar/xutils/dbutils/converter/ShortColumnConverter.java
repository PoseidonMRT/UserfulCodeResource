package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class ShortColumnConverter implements ColumnConverter<Short> {
    public Short getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Short.valueOf(cursor.getShort(i));
    }

    public Short getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Short.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Short sh) {
        return sh;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
