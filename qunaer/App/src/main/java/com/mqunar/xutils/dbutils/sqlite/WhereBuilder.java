package com.mqunar.xutils.dbutils.sqlite;

import android.text.TextUtils;
import com.mqunar.xutils.dbutils.converter.ColumnConverterFactory;
import com.mqunar.xutils.dbutils.table.ColumnUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhereBuilder {
    private final List<String> whereItems = new ArrayList();

    private WhereBuilder() {
    }

    public static WhereBuilder b() {
        return new WhereBuilder();
    }

    public static WhereBuilder b(String str, String str2, Object obj) {
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.appendCondition(null, str, str2, obj);
        return whereBuilder;
    }

    public WhereBuilder and(String str, String str2, Object obj) {
        appendCondition(this.whereItems.size() == 0 ? null : "AND", str, str2, obj);
        return this;
    }

    public WhereBuilder or(String str, String str2, Object obj) {
        appendCondition(this.whereItems.size() == 0 ? null : "OR", str, str2, obj);
        return this;
    }

    public WhereBuilder expr(String str) {
        this.whereItems.add(" " + str);
        return this;
    }

    public WhereBuilder expr(String str, String str2, Object obj) {
        appendCondition(null, str, str2, obj);
        return this;
    }

    public int getWhereItemSize() {
        return this.whereItems.size();
    }

    public String toString() {
        if (this.whereItems.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : this.whereItems) {
            stringBuilder.append(append);
        }
        return stringBuilder.toString();
    }

    private void appendCondition(String str, String str2, String str3, Object obj) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.whereItems.size() > 0) {
            stringBuilder.append(" ");
        }
        if (!TextUtils.isEmpty(str)) {
            stringBuilder.append(str + " ");
        }
        stringBuilder.append(str2);
        if ("!=".equals(str3)) {
            str3 = "<>";
        } else if ("==".equals(str3)) {
            str3 = "=";
        }
        if (obj != null) {
            stringBuilder.append(" " + str3 + " ");
            ArrayList arrayList;
            int length;
            ArrayList arrayList2;
            Object convert2DbColumnValueIfNeeded;
            String obj2;
            if ("IN".equalsIgnoreCase(str3)) {
                if (obj instanceof Iterable) {
                    obj = (Iterable) obj;
                } else if (obj.getClass().isArray()) {
                    arrayList = new ArrayList();
                    length = Array.getLength(obj);
                    while (i < length) {
                        arrayList.add(Array.get(obj, i));
                        i++;
                    }
                    arrayList2 = arrayList;
                } else {
                    obj = null;
                }
                if (r12 != null) {
                    StringBuffer stringBuffer = new StringBuffer("(");
                    for (Object convert2DbColumnValueIfNeeded2 : r12) {
                        convert2DbColumnValueIfNeeded2 = ColumnUtils.convert2DbColumnValueIfNeeded(convert2DbColumnValueIfNeeded2);
                        if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(convert2DbColumnValueIfNeeded2.getClass()))) {
                            obj2 = convert2DbColumnValueIfNeeded2.toString();
                            if (obj2.indexOf(39) != -1) {
                                obj2 = obj2.replace("'", "''");
                            }
                            stringBuffer.append("'" + obj2 + "'");
                        } else {
                            stringBuffer.append(convert2DbColumnValueIfNeeded2);
                        }
                        stringBuffer.append(",");
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    stringBuffer.append(")");
                    stringBuilder.append(stringBuffer.toString());
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else if ("BETWEEN".equalsIgnoreCase(str3)) {
                if (obj instanceof Iterable) {
                    obj = (Iterable) obj;
                } else if (obj.getClass().isArray()) {
                    arrayList = new ArrayList();
                    length = Array.getLength(obj);
                    while (i < length) {
                        arrayList.add(Array.get(obj, i));
                        i++;
                    }
                    arrayList2 = arrayList;
                } else {
                    obj = null;
                }
                if (obj != null) {
                    Iterator it = obj.iterator();
                    if (it.hasNext()) {
                        Object next = it.next();
                        if (it.hasNext()) {
                            convert2DbColumnValueIfNeeded2 = it.next();
                            next = ColumnUtils.convert2DbColumnValueIfNeeded(next);
                            Object convert2DbColumnValueIfNeeded3 = ColumnUtils.convert2DbColumnValueIfNeeded(convert2DbColumnValueIfNeeded2);
                            if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(next.getClass()))) {
                                obj2 = next.toString();
                                if (obj2.indexOf(39) != -1) {
                                    obj2 = obj2.replace("'", "''");
                                }
                                String obj3 = convert2DbColumnValueIfNeeded3.toString();
                                if (obj3.indexOf(39) != -1) {
                                    obj3 = obj3.replace("'", "''");
                                }
                                stringBuilder.append("'" + obj2 + "'");
                                stringBuilder.append(" AND ");
                                stringBuilder.append("'" + obj3 + "'");
                            } else {
                                stringBuilder.append(next);
                                stringBuilder.append(" AND ");
                                stringBuilder.append(convert2DbColumnValueIfNeeded3);
                            }
                        } else {
                            throw new IllegalArgumentException("value must have tow items.");
                        }
                    }
                    throw new IllegalArgumentException("value must have tow items.");
                }
                throw new IllegalArgumentException("value must be an Array or an Iterable.");
            } else {
                convert2DbColumnValueIfNeeded2 = ColumnUtils.convert2DbColumnValueIfNeeded(obj);
                if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(convert2DbColumnValueIfNeeded2.getClass()))) {
                    obj2 = convert2DbColumnValueIfNeeded2.toString();
                    if (obj2.indexOf(39) != -1) {
                        obj2 = obj2.replace("'", "''");
                    }
                    stringBuilder.append("'" + obj2 + "'");
                } else {
                    stringBuilder.append(convert2DbColumnValueIfNeeded2);
                }
            }
        } else if ("=".equals(str3)) {
            stringBuilder.append(" IS NULL");
        } else if ("<>".equals(str3)) {
            stringBuilder.append(" IS NOT NULL");
        } else {
            stringBuilder.append(" " + str3 + " NULL");
        }
        this.whereItems.add(stringBuilder.toString());
    }
}
