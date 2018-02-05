package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class ByteArrayColumnConverter implements ColumnConverter<byte[]> {
    public byte[] getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : cursor.getBlob(i);
    }

    public byte[] getFieldValue(String str) {
        return null;
    }

    public Object fieldValue2ColumnValue(byte[] bArr) {
        return bArr;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.BLOB;
    }
}
