package com.mqunar.xutils.dbutils.table;

import android.database.Cursor;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.converter.ColumnConverter;
import com.mqunar.xutils.dbutils.converter.ColumnConverterFactory;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;
import com.mqunar.xutils.dbutils.sqlite.ForeignLazyLoader;
import java.lang.reflect.Field;
import java.util.List;

public class Foreign extends Column {
    private final ColumnConverter foreignColumnConverter = ColumnConverterFactory.getColumnConverter(TableUtils.getColumnOrId(getForeignEntityType(), this.foreignColumnName).columnField.getType());
    private final String foreignColumnName;

    Foreign(Class<?> cls, Field field) {
        super(cls, field);
        this.foreignColumnName = ColumnUtils.getForeignColumnNameByField(field);
    }

    public String getForeignColumnName() {
        return this.foreignColumnName;
    }

    public Class<?> getForeignEntityType() {
        return ColumnUtils.getForeignEntityType(this);
    }

    public void setValue2Entity(Object obj, Cursor cursor, int i) {
        Object fieldValue = this.foreignColumnConverter.getFieldValue(cursor, i);
        if (fieldValue != null) {
            Object obj2 = null;
            Class type = this.columnField.getType();
            if (type.equals(ForeignLazyLoader.class)) {
                obj2 = new ForeignLazyLoader(this, fieldValue);
            } else if (type.equals(List.class)) {
                try {
                    obj2 = new ForeignLazyLoader(this, fieldValue).getAllFromDb();
                } catch (Throwable e) {
                    QLog.e(e);
                }
            } else {
                try {
                    obj2 = new ForeignLazyLoader(this, fieldValue).getFirstFromDb();
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
    }

    public Object getColumnValue(Object obj) {
        Throwable th;
        Throwable th2;
        Object fieldValue = getFieldValue(obj);
        Object obj2 = null;
        if (fieldValue != null) {
            Class type = this.columnField.getType();
            if (type.equals(ForeignLazyLoader.class)) {
                return ((ForeignLazyLoader) fieldValue).getColumnValue();
            }
            Column columnOrId;
            Table table;
            if (type.equals(List.class)) {
                try {
                    List list = (List) fieldValue;
                    if (list.size() > 0) {
                        columnOrId = TableUtils.getColumnOrId(ColumnUtils.getForeignEntityType(this), this.foreignColumnName);
                        obj2 = columnOrId.getColumnValue(list.get(0));
                        try {
                            table = getTable();
                            if (table != null && (columnOrId instanceof Id)) {
                                for (Object next : list) {
                                    if (columnOrId.getColumnValue(next) == null) {
                                        table.db.saveOrUpdate(next);
                                    }
                                }
                            }
                            return columnOrId.getColumnValue(list.get(0));
                        } catch (Throwable th3) {
                            th = th3;
                            fieldValue = obj2;
                            th2 = th;
                            QLog.e(th2);
                            return fieldValue;
                        }
                    }
                } catch (Throwable th32) {
                    th = th32;
                    fieldValue = obj2;
                    th2 = th;
                    QLog.e(th2);
                    return fieldValue;
                }
            }
            try {
                columnOrId = TableUtils.getColumnOrId(type, this.foreignColumnName);
                obj2 = columnOrId.getColumnValue(fieldValue);
                try {
                    table = getTable();
                    if (table != null && obj2 == null && (columnOrId instanceof Id)) {
                        table.db.saveOrUpdate(fieldValue);
                    }
                    return columnOrId.getColumnValue(fieldValue);
                } catch (Throwable th322) {
                    th = th322;
                    fieldValue = obj2;
                    th2 = th;
                    QLog.e(th2);
                    return fieldValue;
                }
            } catch (Throwable th3222) {
                th = th3222;
                fieldValue = obj2;
                th2 = th;
                QLog.e(th2);
                return fieldValue;
            }
        }
        return obj2;
    }

    public ColumnDbType getColumnDbType() {
        return this.foreignColumnConverter.getColumnDbType();
    }

    public Object getDefaultValue() {
        return null;
    }
}
