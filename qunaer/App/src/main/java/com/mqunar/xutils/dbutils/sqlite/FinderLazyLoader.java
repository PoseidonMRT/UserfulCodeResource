package com.mqunar.xutils.dbutils.sqlite;

import com.mqunar.xutils.dbutils.table.ColumnUtils;
import com.mqunar.xutils.dbutils.table.Finder;
import com.mqunar.xutils.dbutils.table.Table;
import java.util.List;

public class FinderLazyLoader<T> {
    private final Finder finderColumn;
    private final Object finderValue;

    public FinderLazyLoader(Finder finder, Object obj) {
        this.finderColumn = finder;
        this.finderValue = ColumnUtils.convert2DbColumnValueIfNeeded(obj);
    }

    public List<T> getAllFromDb() {
        Table table = this.finderColumn.getTable();
        if (table != null) {
            return table.db.findAll(Selector.from(this.finderColumn.getTargetEntityType()).where(this.finderColumn.getTargetColumnName(), "=", this.finderValue));
        }
        return null;
    }

    public long count() {
        Table table = this.finderColumn.getTable();
        if (table != null) {
            return table.db.count(Selector.from(this.finderColumn.getTargetEntityType()).where(this.finderColumn.getTargetColumnName(), "=", this.finderValue));
        }
        return 0;
    }

    public T getFirstFromDb() {
        Table table = this.finderColumn.getTable();
        if (table != null) {
            return table.db.findFirst(Selector.from(this.finderColumn.getTargetEntityType()).where(this.finderColumn.getTargetColumnName(), "=", this.finderValue));
        }
        return null;
    }
}
