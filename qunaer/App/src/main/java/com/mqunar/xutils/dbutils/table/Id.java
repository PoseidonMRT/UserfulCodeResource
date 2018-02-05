package com.mqunar.xutils.dbutils.table;

import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.annotation.NoAutoIncrement;
import java.lang.reflect.Field;
import java.util.HashSet;

public class Id extends Column {
    private static final HashSet<String> AUTO_INCREMENT_TYPES = new HashSet(4);
    private static final HashSet<String> INTEGER_TYPES = new HashSet(2);
    private String columnFieldClassName = this.columnField.getType().getName();
    private boolean isAutoIncrement = false;
    private boolean isAutoIncrementChecked = false;

    Id(Class<?> cls, Field field) {
        super(cls, field);
    }

    public boolean isAutoIncrement() {
        boolean z = true;
        if (!this.isAutoIncrementChecked) {
            this.isAutoIncrementChecked = true;
            if (!(this.columnField.getAnnotation(NoAutoIncrement.class) == null && AUTO_INCREMENT_TYPES.contains(this.columnFieldClassName))) {
                z = false;
            }
            this.isAutoIncrement = z;
        }
        return this.isAutoIncrement;
    }

    public void setAutoIncrementId(Object obj, long j) {
        Object valueOf = Long.valueOf(j);
        if (INTEGER_TYPES.contains(this.columnFieldClassName)) {
            valueOf = Integer.valueOf((int) j);
        }
        if (this.setMethod != null) {
            try {
                this.setMethod.invoke(obj, new Object[]{valueOf});
                return;
            } catch (Throwable th) {
                QLog.e(th);
                return;
            }
        }
        try {
            this.columnField.setAccessible(true);
            this.columnField.set(obj, valueOf);
        } catch (Throwable th2) {
            QLog.e(th2);
        }
    }

    public Object getColumnValue(Object obj) {
        Object columnValue = super.getColumnValue(obj);
        if (columnValue == null) {
            return null;
        }
        if (isAutoIncrement() && (columnValue.equals(Integer.valueOf(0)) || columnValue.equals(Long.valueOf(0)))) {
            return null;
        }
        return columnValue;
    }

    static {
        INTEGER_TYPES.add(Integer.TYPE.getName());
        INTEGER_TYPES.add(Integer.class.getName());
        AUTO_INCREMENT_TYPES.addAll(INTEGER_TYPES);
        AUTO_INCREMENT_TYPES.add(Long.TYPE.getName());
        AUTO_INCREMENT_TYPES.add(Long.class.getName());
    }
}
