package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;

public class FloatColumnConverter implements ColumnConverter<Float> {
    public Float getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : Float.valueOf(cursor.getFloat(i));
    }

    public Float getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Float.valueOf(str);
    }

    public Object fieldValue2ColumnValue(Float f) {
        return f;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
