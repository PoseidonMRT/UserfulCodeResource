package com.mqunar.xutils.dbutils.table;

import android.database.Cursor;
import android.text.TextUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.DbUtils;
import com.mqunar.xutils.dbutils.atom.IEncrypt;
import com.mqunar.xutils.dbutils.converter.ColumnConverter;
import com.mqunar.xutils.dbutils.converter.ColumnConverterFactory;
import com.mqunar.xutils.dbutils.sqlite.ColumnDbType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Column {
    protected final ColumnConverter columnConverter;
    protected final Field columnField;
    protected final String columnName;
    private final Object defaultValue;
    protected final IEncrypt encrypt;
    protected final Method getMethod;
    private int index = -1;
    protected final Method setMethod;
    private Table table;

    Column(Class<?> cls, Field field) {
        this.columnField = field;
        this.columnConverter = ColumnConverterFactory.getColumnConverter(field.getType());
        this.columnName = ColumnUtils.getColumnNameByField(field);
        this.encrypt = getEncrypt(ColumnUtils.getColumnEncryptType(field));
        if (this.columnConverter != null) {
            this.defaultValue = this.columnConverter.getFieldValue(ColumnUtils.getColumnDefaultValue(field));
        } else {
            this.defaultValue = null;
        }
        this.getMethod = ColumnUtils.getColumnGetMethod(cls, field);
        this.setMethod = ColumnUtils.getColumnSetMethod(cls, field);
    }

    public void setValue2Entity(Object obj, Cursor cursor, int i) {
        this.index = i;
        Object fieldValue = this.columnConverter.getFieldValue(cursor, i);
        if (fieldValue != null || this.defaultValue != null) {
            if (this.encrypt != null) {
                fieldValue = this.encrypt.decrypt(fieldValue);
            }
            if (this.setMethod != null) {
                try {
                    Method method = this.setMethod;
                    Object[] objArr = new Object[1];
                    if (fieldValue == null) {
                        fieldValue = this.defaultValue;
                    }
                    objArr[0] = fieldValue;
                    method.invoke(obj, objArr);
                    return;
                } catch (Throwable th) {
                    QLog.e(th);
                    return;
                }
            }
            try {
                this.columnField.setAccessible(true);
                Field field = this.columnField;
                if (fieldValue == null) {
                    fieldValue = this.defaultValue;
                }
                field.set(obj, fieldValue);
            } catch (Throwable th2) {
                QLog.e(th2);
            }
        }
    }

    public Object getColumnValue(Object obj) {
        Object fieldValue2ColumnValue = this.columnConverter.fieldValue2ColumnValue(getFieldValue(obj));
        if (this.encrypt != null) {
            return this.encrypt.encrypt(fieldValue2ColumnValue);
        }
        return fieldValue2ColumnValue;
    }

    public Object getFieldValue(Object obj) {
        Object obj2 = null;
        if (obj != null) {
            if (this.getMethod != null) {
                try {
                    obj2 = this.getMethod.invoke(obj, new Object[0]);
                } catch (Throwable th) {
                    QLog.e(th);
                }
            } else {
                try {
                    this.columnField.setAccessible(true);
                    obj2 = this.columnField.get(obj);
                } catch (Throwable th2) {
                    QLog.e(th2);
                }
            }
        }
        return obj2;
    }

    public Table getTable() {
        return this.table;
    }

    void setTable(Table table) {
        this.table = table;
    }

    public int getIndex() {
        return this.index;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public Field getColumnField() {
        return this.columnField;
    }

    public ColumnConverter getColumnConverter() {
        return this.columnConverter;
    }

    public ColumnDbType getColumnDbType() {
        return this.columnConverter.getColumnDbType();
    }

    public IEncrypt getEncrypt(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return DbUtils.getEncrypt(str);
    }
}
