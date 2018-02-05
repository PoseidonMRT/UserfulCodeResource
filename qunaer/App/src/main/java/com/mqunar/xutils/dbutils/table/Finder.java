package com.mqunar.xutils.dbutils.table;

import android.database.Cursor;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;
import com.mqunar.xutils.dbutils.sqlite.FinderLazyLoader;
import java.lang.reflect.Field;
import java.util.List;

public class Finder extends Column {
    private final String targetColumnName;
    private final String valueColumnName;

    Finder(Class<?> cls, Field field) {
        super(cls, field);
        com.mqunar.xutils.dbutils.annotation.Finder finder = (com.mqunar.xutils.dbutils.annotation.Finder) field.getAnnotation(com.mqunar.xutils.dbutils.annotation.Finder.class);
        this.valueColumnName = finder.valueColumn();
        this.targetColumnName = finder.targetColumn();
    }

    public Class<?> getTargetEntityType() {
        return ColumnUtils.getFinderTargetEntityType(this);
    }

    public String getTargetColumnName() {
        return this.targetColumnName;
    }

    public void setValue2Entity(Object obj, Cursor cursor, int i) {
        Object obj2 = null;
        Class type = this.columnField.getType();
        Object columnValue = TableUtils.getColumnOrId(obj.getClass(), this.valueColumnName).getColumnValue(obj);
        if (type.equals(FinderLazyLoader.class)) {
            obj2 = new FinderLazyLoader(this, columnValue);
        } else if (type.equals(List.class)) {
            try {
                obj2 = new FinderLazyLoader(this, columnValue).getAllFromDb();
            } catch (Throwable e) {
                QLog.e(e);
            }
        } else {
            try {
                obj2 = new FinderLazyLoader(this, columnValue).getFirstFromDb();
            } catch (Throwable e2) {
                QLog.e(e2);
            }
        }
        if (this.setMethod != null) {
            try {
                this.setMethod.invoke(obj, new Object[]{obj2});
                return;
            } catch (Throwable th) {
                QLog.e(th);
                return;
            }
        }
        try {
            this.columnField.setAccessible(true);
            this.columnField.set(obj, obj2);
        } catch (Throwable th2) {
            QLog.e(th2);
        }
    }

    public Object getColumnValue(Object obj) {
        return null;
    }

    public Object getDefaultValue() {
        return null;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
