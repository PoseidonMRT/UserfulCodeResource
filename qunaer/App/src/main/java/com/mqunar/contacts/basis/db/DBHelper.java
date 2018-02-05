package com.mqunar.contacts.basis.db;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.utils.CrashUtils;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.DbUtils;
import com.mqunar.xutils.dbutils.DbUtils.DaoConfig;
import com.mqunar.xutils.dbutils.DbUtils.DbUpgradeListener;
import java.util.HashMap;
import java.util.List;

public enum DBHelper {
    INSTANCE;
    
    public static final String DB_IM_NAME = "qunar_contacts_%s.db";
    public static final int DB_VERSION = 1;
    public static final String TAG = "Contacts";
    private HashMap<String, DbUtils> mContactsDatabase;
    private DBContactsWriteTask mWriteTask;

    public synchronized DbUtils getContactsDatabaseByUser(Context context, String str) {
        DbUtils dbUtils;
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("userName is empty!");
        }
        dbUtils = (DbUtils) this.mContactsDatabase.get(str);
        if (dbUtils == null) {
            DaoConfig daoConfig = new DaoConfig(context);
            daoConfig.setDbName(String.format(DB_IM_NAME, new Object[]{str}));
            daoConfig.setDbVersion(1);
            daoConfig.setDbUpgradeListener(new DbUpgradeListener() {
                public void onUpgrade(DbUtils dbUtils, int i, int i2) {
                }
            });
            dbUtils = DbUtils.create(daoConfig);
            this.mContactsDatabase.put(str, dbUtils);
        }
        return dbUtils;
    }

    public void saveContacts(Context context, List<Contact> list, String str) {
        if (context == null || CheckUtils.isEmpty(list)) {
            QLog.e("Contacts", "saveContacts but context or contacts is empty! ", new Object[0]);
            return;
        }
        this.mWriteTask = new DBContactsWriteTask(context, list, str);
        this.mWriteTask.run();
    }

    public void getContacts(Context context, String str, DBContactsReadCallback dBContactsReadCallback) {
        new DBContactsReadTask(context, str, dBContactsReadCallback).run();
    }

    public List<Contact> getContacts(Context context, String str) {
        int i;
        long currentTimeMillis = System.currentTimeMillis();
        List<Contact> list = null;
        if (CheckUtils.isEmpty(str)) {
            QLog.e("Contacts", "uniqueKey is empty!", new Object[0]);
        } else {
            DbUtils contactsDatabaseByUser = INSTANCE.getContactsDatabaseByUser(context, str);
            if (contactsDatabaseByUser == null) {
                QLog.e("Contacts", "databases is empty!", new Object[0]);
            } else {
                try {
                    list = contactsDatabaseByUser.findAll(Contact.class);
                } catch (Throwable e) {
                    QLog.e("Contacts", CrashUtils.getStackTraceString(e), new Object[0]);
                }
            }
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        if (CheckUtils.isEmpty(list)) {
            i = 0;
        } else {
            i = list.size();
        }
        QLog.d("Contacts", "Database -> read contact (" + i + ") waste " + (currentTimeMillis2 - currentTimeMillis), new Object[0]);
        return list;
    }
}
