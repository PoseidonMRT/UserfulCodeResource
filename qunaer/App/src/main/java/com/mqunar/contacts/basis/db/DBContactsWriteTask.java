package com.mqunar.contacts.basis.db;

import android.content.Context;
import com.mqunar.contacts.basis.async.ITask;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.utils.CrashUtils;
import com.mqunar.libtask.AsyncTask;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.xutils.dbutils.DbUtils;
import java.util.List;

public class DBContactsWriteTask extends AsyncTask<Void, Integer, Void> implements ITask {
    private static final String TAG = "Contacts";
    private List<Contact> contacts;
    private Context context;
    private boolean isRunningEnd = false;
    private String uniqueKey;

    public DBContactsWriteTask(Context context, List<Contact> list, String str) {
        this.context = context;
        this.contacts = list;
        this.uniqueKey = str;
    }

    protected Void doInBackground(Void... voidArr) {
        QLog.d("Contacts", Thread.currentThread().getName() + " Database save contact (" + (CheckUtils.isEmpty(this.contacts) ? 0 : this.contacts.size()) + ") start work ", new Object[0]);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            if (CheckUtils.isEmpty(this.contacts) || CheckUtils.isEmpty(this.uniqueKey)) {
                QLog.e("Contacts", "contacts or uniqueKey is empty!", new Object[0]);
                QLog.d("Contacts", "Database save contact (" + this.contacts.size() + ") waste " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
                return null;
            }
            DbUtils contactsDatabaseByUser = DBHelper.INSTANCE.getContactsDatabaseByUser(this.context, this.uniqueKey);
            if (contactsDatabaseByUser == null) {
                QLog.e("Contacts", "databases is empty!", new Object[0]);
            } else {
                try {
                    contactsDatabaseByUser.mergeAll(this.contacts);
                    QLog.d("Contacts", "contacts save database success!", new Object[0]);
                } catch (Throwable e) {
                    QLog.e("Contacts", CrashUtils.getStackTraceString(e), new Object[0]);
                }
            }
            QLog.d("Contacts", "Database save contact (" + this.contacts.size() + ") waste " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return null;
        } catch (Throwable e2) {
            QLog.e("Contacts", CrashUtils.getStackTraceString(e2), new Object[0]);
        }
    }

    protected void onPostExecute(Void voidR) {
        super.onPostExecute(voidR);
        this.isRunningEnd = true;
    }

    public void run() {
        executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    public boolean isOver() {
        return this.isRunningEnd;
    }
}
