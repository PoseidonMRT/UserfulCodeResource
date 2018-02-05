package com.mqunar.xutils.dbutils.table;

import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.xutils.dbutils.DbUtils;
import java.util.HashMap;
import java.util.Map.Entry;

public class Table {
    private static final HashMap<String, Table> tableMap = new HashMap();
    private boolean checkedDatabase;
    public final HashMap<String, Column> columnMap;
    public final DbUtils db;
    public final HashMap<String, Finder> finderMap = new HashMap();
    public final Id id;
    public final String tableName;

    private Table(DbUtils dbUtils, Class<?> cls) {
        this.db = dbUtils;
        this.tableName = TableUtils.getTableName(cls);
        this.id = TableUtils.getId(cls);
        this.columnMap = TableUtils.getColumnMap(cls);
        for (Column column : this.columnMap.values()) {
            column.setTable(this);
            if (column instanceof Finder) {
                this.finderMap.put(column.getColumnName(), (Finder) column);
            }
        }
    }

    public static synchronized Table get(DbUtils dbUtils, Class<?> cls) {
        Table table;
        synchronized (Table.class) {
            String str = dbUtils.getDaoConfig().getDbName() + Contact.NUMBER + cls.getName();
            table = (Table) tableMap.get(str);
            if (table == null) {
                table = new Table(dbUtils, cls);
                tableMap.put(str, table);
            }
        }
        return table;
    }

    public static synchronized void remove(DbUtils dbUtils, Class<?> cls) {
        synchronized (Table.class) {
            tableMap.remove(dbUtils.getDaoConfig().getDbName() + Contact.NUMBER + cls.getName());
        }
    }

    public static synchronized void remove(DbUtils dbUtils, String str) {
        synchronized (Table.class) {
            if (tableMap.size() > 0) {
                String str2 = null;
                for (Entry entry : tableMap.entrySet()) {
                    String str3;
                    Table table = (Table) entry.getValue();
                    if (table == null || !table.tableName.equals(str)) {
                        str3 = str2;
                    } else {
                        str3 = (String) entry.getKey();
                        if (str3.startsWith(dbUtils.getDaoConfig().getDbName() + Contact.NUMBER)) {
                            break;
                        }
                    }
                    str2 = str3;
                }
                Object obj = str2;
                if (TextUtils.isEmpty(r0)) {
                    tableMap.remove(r0);
                }
            }
        }
    }

    public boolean isCheckedDatabase() {
        return this.checkedDatabase;
    }

    public void setCheckedDatabase(boolean z) {
        this.checkedDatabase = z;
    }
}
