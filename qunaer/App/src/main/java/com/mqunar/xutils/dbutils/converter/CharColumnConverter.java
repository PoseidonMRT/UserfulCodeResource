package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class CharColumnConverter implements ColumnConverter<Character> {
    public Character getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Character.valueOf((char) cursor.getInt(i));
    }

    public Character getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Character.valueOf(str.charAt(0));
    }

    public Object fieldValue2ColumnValue(Character ch) {
        if (ch == null) {
            return null;
        }
        return Integer.valueOf(ch.charValue());
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
