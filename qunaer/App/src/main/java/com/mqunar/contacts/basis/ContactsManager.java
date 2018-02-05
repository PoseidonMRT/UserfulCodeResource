package com.mqunar.contacts.basis;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.contacts.basis.db.DBContactsReadCallback;
import com.mqunar.contacts.basis.db.ReadContactsError;
import com.mqunar.contacts.basis.impl.CommonExecutor;
import com.mqunar.contacts.basis.impl.common.CollectContactsCallback;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.business.net.Hotdog;
import com.mqunar.contacts.business.net.UploadContactsCallback;
import com.mqunar.contacts.business.net.error.UploadError;
import com.mqunar.patch.util.UCUtils;
import com.mqunar.tools.log.QLog;
import java.util.List;

public class ContactsManager {
    public static final String TAG = "Contacts";
    private static ContactsManager manager;
    private IContactsExecutor executor;
    private Context mContext;
    private String unique;

    public static synchronized ContactsManager getManager(Context context) {
        ContactsManager contactsManager;
        synchronized (ContactsManager.class) {
            if (manager == null) {
                manager = new ContactsManager(context);
            }
            contactsManager = manager;
        }
        return contactsManager;
    }

    private ContactsManager(Context context) {
        this.mContext = context;
        this.executor = getExecutor(context);
    }

    private IContactsExecutor getExecutor(Context context) {
        return new CommonExecutor(context, new Hotdog(context));
    }

    public boolean hasLicenses() {
        if (UCUtils.getInstance().userValidate()) {
            Object userid = UCUtils.getInstance().getUserid();
            if (!TextUtils.isEmpty(userid)) {
                return this.executor.hasLicenses(userid);
            }
            QLog.d("Contacts", " userId is empty !", new Object[0]);
            return false;
        }
        QLog.d("Contacts", "hasLicenses validate failure ! ", new Object[0]);
        return false;
    }

    public boolean setLicenses(boolean z) {
        if (UCUtils.getInstance().userValidate()) {
            Object userid = UCUtils.getInstance().getUserid();
            if (!TextUtils.isEmpty(userid)) {
                return this.executor.setLicenses(userid, z);
            }
            QLog.d("Contacts", " userId is empty !", new Object[0]);
            return false;
        }
        QLog.d("Contacts", " validate failure ! ", new Object[0]);
        return false;
    }

    public List<Contact> getLocalContactsSync() {
        return this.executor.getLocalContactsSync();
    }

    public boolean getLocalContactsAsync(CollectContactsCallback collectContactsCallback) {
        return this.executor.getLocalContactsAsync(collectContactsCallback);
    }

    public boolean getLocalContactsFromDatabaseAsync(DBContactsReadCallback dBContactsReadCallback) {
        if (UCUtils.getInstance().userValidate()) {
            Object userid = UCUtils.getInstance().getUserid();
            if (!TextUtils.isEmpty(userid)) {
                return this.executor.getLocalContactsFromDatabaseAsync(userid, dBContactsReadCallback);
            }
            QLog.d("Contacts", "getLocalContactsFromDatabaseAsync userId is empty !", new Object[0]);
            if (dBContactsReadCallback == null) {
                return false;
            }
            dBContactsReadCallback.onFailure(new ReadContactsError(1, "getLocalContactsFromDatabaseAsync userId is empty !"));
            return false;
        }
        QLog.d("Contacts", "getLocalContactsFromDatabaseAsync validate failure ! ", new Object[0]);
        if (dBContactsReadCallback == null) {
            return false;
        }
        dBContactsReadCallback.onFailure(new ReadContactsError(1, "getLocalContactsFromDatabaseAsync validate failure ! "));
        return false;
    }

    private boolean uploadContacts(String str, UploadContactsCallback uploadContactsCallback) {
        return this.executor.uploadContacts(str, uploadContactsCallback);
    }

    private boolean hasLicenses(String str) {
        return this.executor.hasLicenses(str);
    }

    public boolean setLicenses(String str, boolean z) {
        return this.executor.setLicenses(str, z);
    }

    public boolean uploadContactsWithUserID(UploadContactsCallback uploadContactsCallback) {
        if (UCUtils.getInstance().userValidate()) {
            Object userid = UCUtils.getInstance().getUserid();
            if (!TextUtils.isEmpty(userid)) {
                return this.executor.uploadContacts(userid, uploadContactsCallback);
            }
            QLog.d("Contacts", " userId is empty !", new Object[0]);
            if (uploadContactsCallback == null) {
                return false;
            }
            uploadContactsCallback.onFailure(new UploadError(3, "userId is empty!"));
            return false;
        }
        QLog.d("Contacts", " validate failure ! ", new Object[0]);
        if (uploadContactsCallback == null) {
            return false;
        }
        uploadContactsCallback.onFailure(new UploadError(2, "validate failure!"));
        return false;
    }

    public boolean syncWithUserId() {
        if (UCUtils.getInstance().userValidate()) {
            Object userid = UCUtils.getInstance().getUserid();
            if (!TextUtils.isEmpty(userid)) {
                return this.executor.sync(userid);
            }
            QLog.d("Contacts", "sync userId is empty !", new Object[0]);
            return false;
        }
        QLog.d("Contacts", "sync validate failure ! ", new Object[0]);
        return false;
    }
}
