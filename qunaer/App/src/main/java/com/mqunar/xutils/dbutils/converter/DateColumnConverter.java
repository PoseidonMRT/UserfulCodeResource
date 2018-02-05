package com.mqunar.xutils.dbutils.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;
import java.util.Date;

public class DateColumnConverter implements ColumnConverter<Date> {
    public Date getFieldValue(Cursor cursor, int i) {
        return cursor.isNull(i) ? null : new Date(cursor.getLong(i));
    }

    public Date getFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new Date(Long.valueOf(str).longValue());
    }

    public Object fieldValue2ColumnValue(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
