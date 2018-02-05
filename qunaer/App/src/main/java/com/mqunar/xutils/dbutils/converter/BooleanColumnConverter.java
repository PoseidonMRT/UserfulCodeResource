package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class BooleanColumnConverter implements ColumnConverter<Boolean> {
    public Boolean getFieldValue(Cursor cursor, int i) {
        boolean z = true;
        if (cursor.isNull(i)) {
            return null;
        }
        if (cursor.getInt(i) != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public Boolean getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Boolean.valueOf(str.length() == 1 ? "1".equals(str) : Boolean.valueOf(str).booleanValue());
    }

    public Object fieldValue2ColumnValue(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return Integer.valueOf(bool.booleanValue() ? 1 : 0);
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
