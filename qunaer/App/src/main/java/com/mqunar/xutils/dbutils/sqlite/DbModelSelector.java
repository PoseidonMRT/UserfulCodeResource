package com.mqunar.xutils.dbutils.sqlite;

import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.xutils.dbutils.sqlite.Selector.OrderBy;

public class DbModelSelector {
    private String[] columnExpressions;
    private String groupByColumnName;
    private WhereBuilder having;
    private Selector selector;

    private DbModelSelector(Class<?> cls) {
        this.selector = Selector.from(cls);
    }

    protected DbModelSelector(Selector selector, String str) {
        this.selector = selector;
        this.groupByColumnName = str;
    }

    protected DbModelSelector(Selector selector, String[] strArr) {
        this.selector = selector;
        this.columnExpressions = strArr;
    }

    public static DbModelSelector from(Class<?> cls) {
        return new DbModelSelector(cls);
    }

    public DbModelSelector where(WhereBuilder whereBuilder) {
        this.selector.where(whereBuilder);
        return this;
    }

    public DbModelSelector where(String str, String str2, Object obj) {
        this.selector.where(str, str2, obj);
        return this;
    }

    public DbModelSelector and(String str, String str2, Object obj) {
        this.selector.and(str, str2, obj);
        return this;
    }

    public DbModelSelector and(WhereBuilder whereBuilder) {
        this.selector.and(whereBuilder);
        return this;
    }

    public DbModelSelector or(String str, String str2, Object obj) {
        this.selector.or(str, str2, obj);
        return this;
    }

    public DbModelSelector or(WhereBuilder whereBuilder) {
        this.selector.or(whereBuilder);
        return this;
    }

    public DbModelSelector expr(String str) {
        this.selector.expr(str);
        return this;
    }

    public DbModelSelector expr(String str, String str2, Object obj) {
        this.selector.expr(str, str2, obj);
        return this;
    }

    public DbModelSelector groupBy(String str) {
        this.groupByColumnName = str;
        return this;
    }

    public DbModelSelector having(WhereBuilder whereBuilder) {
        this.having = whereBuilder;
        return this;
    }

    public DbModelSelector select(String... strArr) {
        this.columnExpressions = strArr;
        return this;
    }

    public DbModelSelector orderBy(String str) {
        this.selector.orderBy(str);
        return this;
    }

    public DbModelSelector orderBy(String str, boolean z) {
        this.selector.orderBy(str, z);
        return this;
    }

    public DbModelSelector limit(int i) {
        this.selector.limit(i);
        return this;
    }

    public DbModelSelector offset(int i) {
        this.selector.offset(i);
        return this;
    }

    public Class<?> getEntityType() {
        return this.selector.getEntityType();
    }

    public String toString() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT ");
        if (this.columnExpressions != null && this.columnExpressions.length > 0) {
            for (String append : this.columnExpressions) {
                stringBuffer.append(append);
                stringBuffer.append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } else if (TextUtils.isEmpty(this.groupByColumnName)) {
            stringBuffer.append(Contact.OTHER);
        } else {
            stringBuffer.append(this.groupByColumnName);
        }
        stringBuffer.append(" FROM ").append(this.selector.tableName);
        if (this.selector.whereBuilder != null && this.selector.whereBuilder.getWhereItemSize() > 0) {
            stringBuffer.append(" WHERE ").append(this.selector.whereBuilder.toString());
        }
        if (!TextUtils.isEmpty(this.groupByColumnName)) {
            stringBuffer.append(" GROUP BY ").append(this.groupByColumnName);
            if (this.having != null && this.having.getWhereItemSize() > 0) {
                stringBuffer.append(" HAVING ").append(this.having.toString());
            }
        }
        if (this.selector.orderByList != null) {
            while (i < this.selector.orderByList.size()) {
                stringBuffer.append(" ORDER BY ").append(((OrderBy) this.selector.orderByList.get(i)).toString());
                i++;
            }
        }
        if (this.selector.limit > 0) {
            stringBuffer.append(" LIMIT ").append(this.selector.limit);
            stringBuffer.append(" OFFSET ").append(this.selector.offset);
        }
        return stringBuffer.toString();
    }
}
