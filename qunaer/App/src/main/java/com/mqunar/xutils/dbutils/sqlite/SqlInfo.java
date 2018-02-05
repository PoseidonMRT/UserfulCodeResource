package com.mqunar.xutils.dbutils.sqlite;

import com.mqunar.xutils.dbutils.table.ColumnUtils;
import java.util.LinkedList;

public class SqlInfo {
    private LinkedList<Object> bindArgs;
    private String sql;

    public SqlInfo(String str) {
        this.sql = str;
    }

    public SqlInfo(String str, Object... objArr) {
        this.sql = str;
        addBindArgs(objArr);
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String str) {
        this.sql = str;
    }

    public LinkedList<Object> getBindArgs() {
        return this.bindArgs;
    }

    public Object[] getBindArgsAsArray() {
        if (this.bindArgs != null) {
            return this.bindArgs.toArray();
        }
        return null;
    }

    public String[] getBindArgsAsStrArray() {
        if (this.bindArgs == null) {
            return null;
        }
        String[] strArr = new String[this.bindArgs.size()];
        for (int i = 0; i < this.bindArgs.size(); i++) {
            Object obj = this.bindArgs.get(i);
            strArr[i] = obj == null ? null : obj.toString();
        }
        return strArr;
    }

    public void addBindArg(Object obj) {
        if (this.bindArgs == null) {
            this.bindArgs = new LinkedList();
        }
        this.bindArgs.add(ColumnUtils.convert2DbColumnValueIfNeeded(obj));
    }

    void addBindArgWithoutConverter(Object obj) {
        if (this.bindArgs == null) {
            this.bindArgs = new LinkedList();
        }
        this.bindArgs.add(obj);
    }

    public void addBindArgs(Object... objArr) {
        if (objArr != null) {
            for (Object addBindArg : objArr) {
                addBindArg(addBindArg);
            }
        }
    }
}
