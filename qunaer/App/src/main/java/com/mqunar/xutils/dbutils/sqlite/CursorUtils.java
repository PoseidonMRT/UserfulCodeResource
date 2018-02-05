package com.mqunar.xutils.dbutils.sqlite;

import android.database.Cursor;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.DbUtils;
import com.mqunar.xutils.dbutils.table.Column;
import com.mqunar.xutils.dbutils.table.DbModel;
import com.mqunar.xutils.dbutils.table.Finder;
import com.mqunar.xutils.dbutils.table.Id;
import com.mqunar.xutils.dbutils.table.Table;
import java.util.concurrent.ConcurrentHashMap;

public class CursorUtils {

    class EntityTempCache {
        private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap();
        private static long seq = 0;

        private EntityTempCache() {
        }

        public static <T> void put(Class<T> cls, Object obj, Object obj2) {
            cache.put(cls.getName() + Contact.NUMBER + obj, obj2);
        }

        public static <T> T get(Class<T> cls, Object obj) {
            return cache.get(cls.getName() + Contact.NUMBER + obj);
        }

        public static void setSeq(long j) {
            if (seq != j) {
                cache.clear();
                seq = j;
            }
        }
    }

    public class FindCacheSequence {
        private static final String FINDER_LAZY_LOADER_CLASS_NAME = FinderLazyLoader.class.getName();
        private static final String FOREIGN_LAZY_LOADER_CLASS_NAME = ForeignLazyLoader.class.getName();
        private static long seq = 0;

        private FindCacheSequence() {
        }

        public static long getSeq() {
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            if (!(className.equals(FOREIGN_LAZY_LOADER_CLASS_NAME) || className.equals(FINDER_LAZY_LOADER_CLASS_NAME))) {
                seq++;
            }
            return seq;
        }
    }

    public static <T> T getEntity(DbUtils dbUtils, Cursor cursor, Class<T> cls, long j) {
        if (dbUtils == null || cursor == null) {
            return null;
        }
        EntityTempCache.setSeq(j);
        try {
            int columnIndex;
            Table table = Table.get(dbUtils, cls);
            Id id = table.id;
            String columnName = id.getColumnName();
            int index = id.getIndex();
            if (index < 0) {
                columnIndex = cursor.getColumnIndex(columnName);
            } else {
                columnIndex = index;
            }
            Object fieldValue = id.getColumnConverter().getFieldValue(cursor, columnIndex);
            T t = EntityTempCache.get(cls, fieldValue);
            if (t != null) {
                return t;
            }
            T newInstance = cls.newInstance();
            id.setValue2Entity(newInstance, cursor, columnIndex);
            EntityTempCache.put(cls, fieldValue, newInstance);
            columnIndex = cursor.getColumnCount();
            for (int i = 0; i < columnIndex; i++) {
                Column column = (Column) table.columnMap.get(cursor.getColumnName(i));
                if (column != null) {
                    column.setValue2Entity(newInstance, cursor, i);
                }
            }
            for (Finder value2Entity : table.finderMap.values()) {
                value2Entity.setValue2Entity(newInstance, null, 0);
            }
            return newInstance;
        } catch (Throwable th) {
            QLog.e(th);
            return null;
        }
    }

    public static DbModel getDbModel(Cursor cursor) {
        DbModel dbModel = null;
        if (cursor != null) {
            dbModel = new DbModel();
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                dbModel.add(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return dbModel;
    }
}
