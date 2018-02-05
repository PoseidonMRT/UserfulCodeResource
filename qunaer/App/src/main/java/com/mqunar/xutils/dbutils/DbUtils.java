package com.mqunar.xutils.dbutils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.atom.ICreateDatabase;
import com.mqunar.xutils.dbutils.atom.IEncrypt;
import com.mqunar.xutils.dbutils.exception.DbException;
import com.mqunar.xutils.dbutils.sqlite.CursorUtils;
import com.mqunar.xutils.dbutils.sqlite.CursorUtils.FindCacheSequence;
import com.mqunar.xutils.dbutils.sqlite.DbModelSelector;
import com.mqunar.xutils.dbutils.sqlite.Selector;
import com.mqunar.xutils.dbutils.sqlite.SqlInfo;
import com.mqunar.xutils.dbutils.sqlite.SqlInfoBuilder;
import com.mqunar.xutils.dbutils.sqlite.WhereBuilder;
import com.mqunar.xutils.dbutils.table.DbModel;
import com.mqunar.xutils.dbutils.table.Id;
import com.mqunar.xutils.dbutils.table.Table;
import com.mqunar.xutils.dbutils.table.TableUtils;
import com.mqunar.xutils.dbutils.util.IOUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DbUtils {
    private static final String TAG = "DbUtils";
    private static HashMap<String, DbUtils> daoMap = new HashMap();
    private static HashMap<String, IEncrypt> sEncrtpts = null;
    private boolean allowTransaction = true;
    private DaoConfig daoConfig;
    private SQLiteDatabase database;
    private boolean debug = false;
    private final FindTempCache findTempCache = new FindTempCache();
    private Lock writeLock = new ReentrantLock();
    private volatile boolean writeLocked = false;

    public interface DbUpgradeListener {
        void onUpgrade(DbUtils dbUtils, int i, int i2);
    }

    public class DaoConfig {
        private Context context;
        private ICreateDatabase createDatabase;
        private String dbDir;
        private String dbName = "xUtils.db";
        private DbUpgradeListener dbUpgradeListener;
        private int dbVersion = 1;

        public DaoConfig(Context context) {
            this.context = context.getApplicationContext();
        }

        public Context getContext() {
            return this.context;
        }

        public String getDbName() {
            return this.dbName;
        }

        public void setDbName(String str) {
            if (!TextUtils.isEmpty(str)) {
                this.dbName = str;
            }
        }

        public int getDbVersion() {
            return this.dbVersion;
        }

        public void setDbVersion(int i) {
            this.dbVersion = i;
        }

        public DbUpgradeListener getDbUpgradeListener() {
            return this.dbUpgradeListener;
        }

        public void setDbUpgradeListener(DbUpgradeListener dbUpgradeListener) {
            this.dbUpgradeListener = dbUpgradeListener;
        }

        public String getDbDir() {
            return this.dbDir;
        }

        public void setInterfaceCreateDatabase(ICreateDatabase iCreateDatabase) {
            this.createDatabase = iCreateDatabase;
        }

        public ICreateDatabase getInterfaceCreateDatabase() {
            return this.createDatabase;
        }

        public void setDbDir(String str) {
            this.dbDir = str;
        }
    }

    class FindTempCache {
        private final ConcurrentHashMap<String, Object> cache;
        private long seq;

        private FindTempCache() {
            this.cache = new ConcurrentHashMap();
            this.seq = 0;
        }

        public void put(String str, Object obj) {
            if (str != null && obj != null) {
                this.cache.put(str, obj);
            }
        }

        public Object get(String str) {
            return this.cache.get(str);
        }

        public void setSeq(long j) {
            if (this.seq != j) {
                this.cache.clear();
                this.seq = j;
            }
        }
    }

    private DbUtils(DaoConfig daoConfig) {
        if (daoConfig == null) {
            throw new IllegalArgumentException("daoConfig may not be null");
        }
        this.database = createDatabase(daoConfig);
        this.daoConfig = daoConfig;
    }

    public static IEncrypt addEncrypt(String str, IEncrypt iEncrypt) {
        if (sEncrtpts == null) {
            sEncrtpts = new HashMap();
        }
        return (IEncrypt) sEncrtpts.put(str, iEncrypt);
    }

    public static IEncrypt getEncrypt(String str) {
        if (sEncrtpts == null || sEncrtpts.size() == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        return (IEncrypt) sEncrtpts.get(str);
    }

    private static synchronized DbUtils getInstance(DaoConfig daoConfig) {
        DbUtils dbUtils;
        synchronized (DbUtils.class) {
            dbUtils = (DbUtils) daoMap.get(daoConfig.getDbName());
            if (dbUtils == null) {
                dbUtils = new DbUtils(daoConfig);
                daoMap.put(daoConfig.getDbName(), dbUtils);
            } else {
                dbUtils.daoConfig = daoConfig;
            }
            SQLiteDatabase sQLiteDatabase = dbUtils.database;
            int version = sQLiteDatabase.getVersion();
            int dbVersion = daoConfig.getDbVersion();
            if (version != dbVersion) {
                if (version != 0) {
                    DbUpgradeListener dbUpgradeListener = daoConfig.getDbUpgradeListener();
                    if (dbUpgradeListener != null) {
                        dbUpgradeListener.onUpgrade(dbUtils, version, dbVersion);
                    } else {
                        try {
                            dbUtils.dropDb();
                        } catch (Throwable e) {
                            QLog.e(e);
                        }
                    }
                }
                sQLiteDatabase.setVersion(dbVersion);
            }
        }
        return dbUtils;
    }

    public static DbUtils create(Context context) {
        return getInstance(new DaoConfig(context));
    }

    public static DbUtils create(Context context, String str) {
        DaoConfig daoConfig = new DaoConfig(context);
        daoConfig.setDbName(str);
        return getInstance(daoConfig);
    }

    public static DbUtils create(Context context, String str, String str2) {
        DaoConfig daoConfig = new DaoConfig(context);
        daoConfig.setDbDir(str);
        daoConfig.setDbName(str2);
        return getInstance(daoConfig);
    }

    public static DbUtils create(Context context, String str, int i, DbUpgradeListener dbUpgradeListener) {
        DaoConfig daoConfig = new DaoConfig(context);
        daoConfig.setDbName(str);
        daoConfig.setDbVersion(i);
        daoConfig.setDbUpgradeListener(dbUpgradeListener);
        return getInstance(daoConfig);
    }

    public static DbUtils create(Context context, String str, String str2, int i, DbUpgradeListener dbUpgradeListener) {
        DaoConfig daoConfig = new DaoConfig(context);
        daoConfig.setDbDir(str);
        daoConfig.setDbName(str2);
        daoConfig.setDbVersion(i);
        daoConfig.setDbUpgradeListener(dbUpgradeListener);
        return getInstance(daoConfig);
    }

    public static DbUtils create(DaoConfig daoConfig) {
        return getInstance(daoConfig);
    }

    public DbUtils configDebug(boolean z) {
        this.debug = z;
        return this;
    }

    public DbUtils configAllowTransaction(boolean z) {
        this.allowTransaction = z;
        return this;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public DaoConfig getDaoConfig() {
        return this.daoConfig;
    }

    public void saveOrUpdate(Object obj) {
        try {
            beginTransaction();
            createTableIfNotExist(obj.getClass());
            saveOrUpdateWithoutTransaction(obj);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void saveOrUpdateAll(List<?> list) {
        if (list != null && list.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(list.get(0).getClass());
                for (Object saveOrUpdateWithoutTransaction : list) {
                    saveOrUpdateWithoutTransaction(saveOrUpdateWithoutTransaction);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void replace(Object obj) {
        try {
            beginTransaction();
            createTableIfNotExist(obj.getClass());
            execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, obj));
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void replaceAll(List<?> list) {
        if (list != null && list.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(list.get(0).getClass());
                for (Object buildReplaceSqlInfo : list) {
                    execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, buildReplaceSqlInfo));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void save(Object obj) {
        try {
            beginTransaction();
            createTableIfNotExist(obj.getClass());
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, obj));
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void saveAll(List<?> list) {
        if (list != null && list.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(list.get(0).getClass());
                for (Object buildInsertSqlInfo : list) {
                    execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, buildInsertSqlInfo));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public boolean saveBindingId(Object obj) {
        try {
            beginTransaction();
            createTableIfNotExist(obj.getClass());
            boolean saveBindingIdWithoutTransaction = saveBindingIdWithoutTransaction(obj);
            setTransactionSuccessful();
            return saveBindingIdWithoutTransaction;
        } finally {
            endTransaction();
        }
    }

    public void saveBindingIdAll(List<?> list) {
        if (list != null && list.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(list.get(0).getClass());
                for (Object saveBindingIdWithoutTransaction : list) {
                    if (!saveBindingIdWithoutTransaction(saveBindingIdWithoutTransaction)) {
                        throw new DbException("saveBindingId error, transaction will not commit!");
                    }
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteById(Class<?> cls, Object obj) {
        if (tableIsExist(cls)) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, (Class) cls, obj));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void delete(Object obj) {
        if (tableIsExist(obj.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, obj));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void delete(Class<?> cls, WhereBuilder whereBuilder) {
        if (tableIsExist(cls)) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, (Class) cls, whereBuilder));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteAll(List<?> list) {
        if (list != null && list.size() != 0 && tableIsExist(list.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object buildDeleteSqlInfo : list) {
                    execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, buildDeleteSqlInfo));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteAll(Class<?> cls) {
        delete(cls, null);
    }

    public void update(Object obj, String... strArr) {
        if (tableIsExist(obj.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, obj, strArr));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void update(Object obj, WhereBuilder whereBuilder, String... strArr) {
        if (tableIsExist(obj.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, obj, whereBuilder, strArr));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void updateAll(List<?> list, String... strArr) {
        if (list != null && list.size() != 0 && tableIsExist(list.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object buildUpdateSqlInfo : list) {
                    execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, buildUpdateSqlInfo, strArr));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void updateAll(List<?> list, WhereBuilder whereBuilder, String... strArr) {
        if (list != null && list.size() != 0 && tableIsExist(list.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object buildUpdateSqlInfo : list) {
                    execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, buildUpdateSqlInfo, whereBuilder, strArr));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public <T> T findById(Class<T> cls, Object obj) {
        if (!tableIsExist(cls)) {
            return null;
        }
        String selector = Selector.from(cls).where(Table.get(this, cls).id.getColumnName(), "=", obj).limit(1).toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        T t = this.findTempCache.get(selector);
        if (t != null) {
            return t;
        }
        Cursor execQuery = execQuery(selector);
        if (execQuery == null) {
            return null;
        }
        try {
            if (execQuery.moveToNext()) {
                T entity = CursorUtils.getEntity(this, execQuery, cls, seq);
                this.findTempCache.put(selector, entity);
                IOUtils.closeQuietly(execQuery);
                return entity;
            }
            IOUtils.closeQuietly(execQuery);
            return null;
        } catch (Throwable th) {
            IOUtils.closeQuietly(execQuery);
        }
    }

    public <T> T findFirst(Selector selector) {
        if (!tableIsExist(selector.getEntityType())) {
            return null;
        }
        String selector2 = selector.limit(1).toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        T t = this.findTempCache.get(selector2);
        if (t != null) {
            return t;
        }
        Cursor execQuery = execQuery(selector2);
        if (execQuery == null) {
            return null;
        }
        try {
            if (execQuery.moveToNext()) {
                T entity = CursorUtils.getEntity(this, execQuery, selector.getEntityType(), seq);
                this.findTempCache.put(selector2, entity);
                IOUtils.closeQuietly(execQuery);
                return entity;
            }
            IOUtils.closeQuietly(execQuery);
            return null;
        } catch (Throwable th) {
            IOUtils.closeQuietly(execQuery);
        }
    }

    public <T> T findFirst(Class<T> cls) {
        return findFirst(Selector.from(cls));
    }

    public <T> List<T> findAll(Selector selector) {
        if (!tableIsExist(selector.getEntityType())) {
            return null;
        }
        String selector2 = selector.toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        Object obj = this.findTempCache.get(selector2);
        if (obj != null) {
            return (List) obj;
        }
        List<T> arrayList = new ArrayList();
        Cursor execQuery = execQuery(selector2);
        if (execQuery == null) {
            return arrayList;
        }
        while (execQuery.moveToNext()) {
            try {
                arrayList.add(CursorUtils.getEntity(this, execQuery, selector.getEntityType(), seq));
            } catch (Throwable th) {
                IOUtils.closeQuietly(execQuery);
            }
        }
        this.findTempCache.put(selector2, arrayList);
        IOUtils.closeQuietly(execQuery);
        return arrayList;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return findAll(Selector.from(cls));
    }

    public DbModel findDbModelFirst(SqlInfo sqlInfo) {
        Cursor execQuery = execQuery(sqlInfo);
        if (execQuery != null) {
            try {
                if (execQuery.moveToNext()) {
                    DbModel dbModel = CursorUtils.getDbModel(execQuery);
                    IOUtils.closeQuietly(execQuery);
                    return dbModel;
                }
                IOUtils.closeQuietly(execQuery);
            } catch (Throwable th) {
                IOUtils.closeQuietly(execQuery);
            }
        }
        return null;
    }

    public DbModel findDbModelFirst(DbModelSelector dbModelSelector) {
        DbModel dbModel = null;
        if (tableIsExist(dbModelSelector.getEntityType())) {
            Cursor execQuery = execQuery(dbModelSelector.limit(1).toString());
            if (execQuery != null) {
                try {
                    if (execQuery.moveToNext()) {
                        dbModel = CursorUtils.getDbModel(execQuery);
                        IOUtils.closeQuietly(execQuery);
                    } else {
                        IOUtils.closeQuietly(execQuery);
                    }
                } catch (Throwable th) {
                    IOUtils.closeQuietly(execQuery);
                }
            }
        }
        return dbModel;
    }

    public List<DbModel> findDbModelAll(SqlInfo sqlInfo) {
        List<DbModel> arrayList = new ArrayList();
        Cursor execQuery = execQuery(sqlInfo);
        if (execQuery != null) {
            while (execQuery.moveToNext()) {
                try {
                    arrayList.add(CursorUtils.getDbModel(execQuery));
                } catch (Throwable th) {
                    IOUtils.closeQuietly(execQuery);
                }
            }
            IOUtils.closeQuietly(execQuery);
        }
        return arrayList;
    }

    public List<DbModel> findDbModelAll(DbModelSelector dbModelSelector) {
        if (!tableIsExist(dbModelSelector.getEntityType())) {
            return null;
        }
        List<DbModel> arrayList = new ArrayList();
        Cursor execQuery = execQuery(dbModelSelector.toString());
        if (execQuery == null) {
            return arrayList;
        }
        while (execQuery.moveToNext()) {
            try {
                arrayList.add(CursorUtils.getDbModel(execQuery));
            } catch (Throwable th) {
                IOUtils.closeQuietly(execQuery);
            }
        }
        IOUtils.closeQuietly(execQuery);
        return arrayList;
    }

    public long count(Selector selector) {
        Class entityType = selector.getEntityType();
        if (!tableIsExist(entityType)) {
            return 0;
        }
        Table table = Table.get(this, entityType);
        return findDbModelFirst(selector.select("count(" + table.id.getColumnName() + ") as count")).getLong("count");
    }

    public long count(Class<?> cls) {
        return count(Selector.from(cls));
    }

    private SQLiteDatabase createDatabase(DaoConfig daoConfig) {
        if (daoConfig.getInterfaceCreateDatabase() != null) {
            return daoConfig.getInterfaceCreateDatabase().createDatabase(daoConfig);
        }
        String dbDir = daoConfig.getDbDir();
        if (TextUtils.isEmpty(dbDir)) {
            return daoConfig.getContext().openOrCreateDatabase(daoConfig.getDbName(), 0, null);
        }
        File file = new File(dbDir);
        if (file.exists() || file.mkdirs()) {
            return SQLiteDatabase.openOrCreateDatabase(new File(dbDir, daoConfig.getDbName()), null);
        }
        return null;
    }

    private void saveOrUpdateWithoutTransaction(Object obj) {
        Id id = Table.get(this, obj.getClass()).id;
        if (!id.isAutoIncrement()) {
            execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, obj));
        } else if (id.getColumnValue(obj) != null) {
            execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, obj, new String[0]));
        } else {
            saveBindingIdWithoutTransaction(obj);
        }
    }

    private boolean saveBindingIdWithoutTransaction(Object obj) {
        Table table = Table.get(this, obj.getClass());
        Id id = table.id;
        if (id.isAutoIncrement()) {
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, obj));
            long lastAutoIncrementId = getLastAutoIncrementId(table.tableName);
            if (lastAutoIncrementId == -1) {
                return false;
            }
            id.setAutoIncrementId(obj, lastAutoIncrementId);
            return true;
        }
        execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, obj));
        return true;
    }

    private long getLastAutoIncrementId(String str) {
        long j = -1;
        Cursor execQuery = execQuery("SELECT seq FROM sqlite_sequence WHERE name='" + str + "'");
        if (execQuery != null) {
            try {
                if (execQuery.moveToNext()) {
                    j = execQuery.getLong(0);
                }
                IOUtils.closeQuietly(execQuery);
            } catch (Throwable th) {
                IOUtils.closeQuietly(execQuery);
            }
        }
        return j;
    }

    public void createTableIfNotExist(Class<?> cls) {
        if (!tableIsExist(cls)) {
            execNonQuery(SqlInfoBuilder.buildCreateTableSqlInfo(this, cls));
            String execAfterTableCreated = TableUtils.getExecAfterTableCreated(cls);
            if (!TextUtils.isEmpty(execAfterTableCreated)) {
                execNonQuery(execAfterTableCreated);
            }
        }
    }

    public boolean tableIsExist(Class<?> cls) {
        Table table = Table.get(this, cls);
        if (table.isCheckedDatabase()) {
            return true;
        }
        Cursor execQuery = execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + table.tableName + "'");
        if (execQuery != null) {
            try {
                if (!execQuery.moveToNext() || execQuery.getInt(0) <= 0) {
                    IOUtils.closeQuietly(execQuery);
                } else {
                    table.setCheckedDatabase(true);
                    IOUtils.closeQuietly(execQuery);
                    return true;
                }
            } catch (Throwable th) {
                IOUtils.closeQuietly(execQuery);
            }
        }
        return false;
    }

    public void dropDb() {
        Cursor execQuery = execQuery("SELECT name FROM sqlite_master WHERE type='table' AND name<>'sqlite_sequence'");
        if (execQuery != null) {
            while (execQuery.moveToNext()) {
                try {
                    String string = execQuery.getString(0);
                    execNonQuery("DROP TABLE " + string);
                    Table.remove(this, string);
                } catch (Throwable th) {
                    try {
                        DbException dbException = new DbException(th);
                    } catch (Throwable th2) {
                        IOUtils.closeQuietly(execQuery);
                    }
                }
            }
            IOUtils.closeQuietly(execQuery);
        }
    }

    public void dropTable(Class<?> cls) {
        if (tableIsExist(cls)) {
            execNonQuery("DROP TABLE " + TableUtils.getTableName(cls));
            Table.remove(this, (Class) cls);
        }
    }

    public void close() {
        String dbName = this.daoConfig.getDbName();
        if (daoMap.containsKey(dbName)) {
            daoMap.remove(dbName);
            this.database.close();
        }
    }

    private void debugSql(String str) {
        if (this.debug) {
            QLog.d(TAG, str, new Object[0]);
        }
    }

    private void beginTransaction() {
        log("beginTransaction isLocked(" + this.writeLock + ") , allowTransaction(" + this.allowTransaction + ")");
        if (this.allowTransaction) {
            this.database.beginTransaction();
            return;
        }
        this.writeLock.lock();
        this.writeLocked = true;
    }

    private void setTransactionSuccessful() {
        log("setTransactionSuccessful isLocked(" + this.writeLock + ") , allowTransaction(" + this.allowTransaction + ")");
        if (this.allowTransaction) {
            this.database.setTransactionSuccessful();
        }
    }

    private void endTransaction() {
        log("endTransaction isLocked(" + this.writeLock + ") , allowTransaction(" + this.allowTransaction + ")");
        if (this.allowTransaction) {
            this.database.endTransaction();
        } else if (this.writeLocked) {
            try {
                this.writeLock.unlock();
                this.writeLocked = false;
            } catch (IllegalMonitorStateException e) {
                IllegalMonitorStateException illegalMonitorStateException = new IllegalMonitorStateException(e.getMessage() + String.format(" [writeLock %s , allowTransaction %s , dbName %s]", new Object[]{this.writeLock + "", this.allowTransaction + "", this.daoConfig.dbName}));
                illegalMonitorStateException.setStackTrace(e.getStackTrace());
                throw illegalMonitorStateException;
            } catch (Throwable th) {
                this.writeLocked = false;
            }
        }
    }

    public void execNonQuery(SqlInfo sqlInfo) {
        debugSql(sqlInfo.getSql());
        try {
            if (sqlInfo.getBindArgs() != null) {
                this.database.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
            } else {
                this.database.execSQL(sqlInfo.getSql());
            }
        } catch (Throwable th) {
            DbException dbException = new DbException(th);
        }
    }

    public void execNonQuery(String str) {
        debugSql(str);
        try {
            this.database.execSQL(str);
        } catch (Throwable th) {
            DbException dbException = new DbException(th);
        }
    }

    public Cursor execQuery(SqlInfo sqlInfo) {
        debugSql(sqlInfo.getSql());
        try {
            return this.database.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStrArray());
        } catch (Throwable th) {
            DbException dbException = new DbException(th);
        }
    }

    public Cursor execQuery(String str) {
        debugSql(str);
        try {
            return this.database.rawQuery(str, null);
        } catch (Throwable th) {
            DbException dbException = new DbException(th);
        }
    }

    public void merge(Object obj) {
        Class cls = obj.getClass();
        List findAll = findAll(cls);
        if (findAll == null || findAll.size() == 0 || !tableIsExist(cls)) {
            save(obj);
            return;
        }
        int indexOf = findAll.indexOf(obj);
        if (indexOf == -1) {
            save(obj);
        } else if (indexOf >= findAll.size()) {
            throw new DbException(new ArrayIndexOutOfBoundsException(indexOf));
        } else {
            try {
                Object obj2 = findAll.get(indexOf);
                Id id = Table.get(this, cls).id;
                id.setAutoIncrementId(obj, Long.parseLong(id.getColumnValue(obj2).toString()));
                update(obj, new String[0]);
            } catch (Throwable th) {
                DbException dbException = new DbException(th);
            }
        }
    }

    public void mergeByNONETransaction(Object obj) {
        Class cls = obj.getClass();
        List findAll = findAll(cls);
        if (findAll == null || findAll.size() == 0 || !tableIsExist(cls)) {
            createTableIfNotExist(obj.getClass());
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, obj));
            return;
        }
        int indexOf = findAll.indexOf(obj);
        if (indexOf == -1) {
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, obj));
        } else if (indexOf >= findAll.size()) {
            throw new DbException(new ArrayIndexOutOfBoundsException(indexOf));
        } else {
            try {
                Object obj2 = findAll.get(indexOf);
                Id id = Table.get(this, cls).id;
                id.setAutoIncrementId(obj, Long.parseLong(id.getColumnValue(obj2).toString()));
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, obj, new String[0]));
            } catch (Throwable th) {
                DbException dbException = new DbException(th);
            }
        }
    }

    public void mergeAll(List<?> list) {
        if (list != null && list.size() != 0) {
            if (tableIsExist(list.get(0).getClass())) {
                try {
                    beginTransaction();
                    for (Object mergeByNONETransaction : list) {
                        mergeByNONETransaction(mergeByNONETransaction);
                    }
                    setTransactionSuccessful();
                } finally {
                    endTransaction();
                }
            } else {
                saveAll(list);
            }
        }
    }

    public void log(String str) {
        if (this.debug) {
            String str2 = "";
            try {
                QLog.d(TAG, (Thread.currentThread().getName() + " : ") + str, new Object[0]);
            } catch (Exception e) {
            }
        }
    }
}
