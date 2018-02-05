package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class ByteColumnConverter implements ColumnConverter<Byte> {
    public Byte getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Byte.valueOf((byte) cursor.getInt(i));
    }

    public Byte getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Byte.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Byte b) {
        return b;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
