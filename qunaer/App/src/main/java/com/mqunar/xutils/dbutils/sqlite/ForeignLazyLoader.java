package com.mqunar.xutils.dbutils.sqlite;

import com.mqunar.xutils.dbutils.table.ColumnUtils;
import com.mqunar.xutils.dbutils.table.Foreign;
import com.mqunar.xutils.dbutils.table.Table;
import java.util.List;

public class ForeignLazyLoader<T> {
    private Object columnValue;
    private final Foreign foreignColumn;

    public ForeignLazyLoader(Foreign foreign, Object obj) {
        this.foreignColumn = foreign;
        this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(obj);
    }

    public List<T> getAllFromDb() {
        Table table = this.foreignColumn.getTable();
        if (table != null) {
            return table.db.findAll(Selector.from(this.foreignColumn.getForeignEntityType()).where(this.foreignColumn.getForeignColumnName(), "=", this.columnValue));
        }
        return null;
    }

    public T getFirstFromDb() {
        Table table = this.foreignColumn.getTable();
        if (table != null) {
            return table.db.findFirst(Selector.from(this.foreignColumn.getForeignEntityType()).where(this.foreignColumn.getForeignColumnName(), "=", this.columnValue));
        }
        return null;
    }

    public void setColumnValue(Object obj) {
        this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(obj);
    }

    public Object getColumnValue() {
        return this.columnValue;
    }
}
