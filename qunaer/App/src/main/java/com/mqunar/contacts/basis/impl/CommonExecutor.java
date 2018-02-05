package com.mqunar.contacts.basis.impl;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.contacts.basis.IContactsExecutor;
import com.mqunar.contacts.basis.db.ContactsMakeDiffCallback;
import com.mqunar.contacts.basis.db.ContactsMakeDiffTask;
import com.mqunar.contacts.basis.db.DBContactsReadCallback;
import com.mqunar.contacts.basis.db.DBHelper;
import com.mqunar.contacts.basis.db.ReadContactsError;
import com.mqunar.contacts.basis.impl.common.CollectContactsCallback;
import com.mqunar.contacts.basis.impl.common.CollectContactsError;
import com.mqunar.contacts.basis.impl.common.CollectContactsTask;
import com.mqunar.contacts.basis.impl.common.SyncContactsTask;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.upload.IUpload;
import com.mqunar.contacts.basis.upload.UploadListener;
import com.mqunar.contacts.basis.utils.ContactUtils;
import com.mqunar.contacts.basis.utils.CrashUtils;
import com.mqunar.contacts.business.net.UploadContactsCallback;
import com.mqunar.contacts.business.net.error.UploadError;
import com.mqunar.storage.Storage;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import java.util.List;

public class CommonExecutor implements IContactsExecutor {
    private static final String TAG = "Contacts";
    private static final String USERID_FORMAT = "user_id_%s";
    private List<Contact> mContacts;
    private Context mContext;
    private Storage mStorage;
    private SyncContactsTask mSyncTask;
    private IUpload uploadFunc;

    public CommonExecutor(Context context, IUpload iUpload) {
        this.mContext = context;
        this.mStorage = Storage.newStorage(context, "contacts");
        this.uploadFunc = iUpload;
    }

    public boolean hasPermission(String str) {
        return true;
    }

    public boolean hasLicenses(String str) {
        return this.mStorage.getBoolean(formatKey(str), false);
    }

    public boolean setLicenses(String str, boolean z) {
        return this.mStorage.putBoolean(formatKey(str), z);
    }

    public List<Contact> getLocalContactsSync() {
        try {
            this.mContacts = ContactUtils.getContacts(this.mContext);
            return this.mContacts;
        } catch (Throwable e) {
            QLog.e("Contacts", CrashUtils.getStackTraceString(e), new Object[0]);
            return null;
        }
    }

    public boolean getLocalContactsAsync(CollectContactsCallback collectContactsCallback) {
        new CollectContactsTask(this.mContext, collectContactsCallback).run();
        return true;
    }

    public boolean getLocalContactsFromDatabaseAsync(String str, DBContactsReadCallback dBContactsReadCallback) {
        DBHelper.INSTANCE.getContacts(this.mContext, str, dBContactsReadCallback);
        return true;
    }

    public boolean uploadContacts(final String str, final UploadContactsCallback uploadContactsCallback) {
        if (!hasLicenses(str)) {
            if (uploadContactsCallback != null) {
                uploadContactsCallback.onFailure(new UploadError(1, "licenses_failure"));
            }
            return false;
        } else if (!CheckUtils.isEmpty(this.mContacts)) {
            return uploadContacts(this.mContacts, str, uploadContactsCallback);
        } else {
            getLocalContactsAsync(new CollectContactsCallback() {
                public void onSuccess(List<Contact> list) {
                    CommonExecutor.this.uploadContacts(list, str, uploadContactsCallback);
                }

                public void onFailure(CollectContactsError collectContactsError) {
                    if (uploadContactsCallback != null) {
                        uploadContactsCallback.onFailure(new UploadError("upload failure..."));
                    }
                }
            });
            return true;
        }
    }

    public boolean uploadContacts(final List<Contact> list, final String str, final UploadContactsCallback uploadContactsCallback) {
        if (CheckUtils.isEmpty(list)) {
            QLog.d("Contacts", "uploadContacts ,but contacts is empty ! ", new Object[0]);
            if (uploadContactsCallback == null) {
                return false;
            }
            uploadContactsCallback.onFailure(new UploadError("uploadContacts ,but contacts is empty ! "));
            return false;
        }
        if (this.uploadFunc != null) {
            QLog.d("Contacts", "make diff ,original size : " + list.size(), new Object[0]);
            new ContactsMakeDiffTask(this.mContext, str, list, new ContactsMakeDiffCallback() {
                public void onSuccess(List<Contact> list) {
                    QLog.d("Contacts", "make diff ,after size : " + (CheckUtils.isEmpty(list) ? 0 : list.size()), new Object[0]);
                    CommonExecutor.this.uploadContacts(CommonExecutor.this.uploadFunc, CommonExecutor.this.mContext, str, list, uploadContactsCallback);
                }

                public void onFailure(ReadContactsError readContactsError) {
                    QLog.d("Contacts", "make diff failure! upload original!", new Object[0]);
                    CommonExecutor.this.uploadContacts(CommonExecutor.this.uploadFunc, CommonExecutor.this.mContext, str, list, uploadContactsCallback);
                }
            }).run();
        } else if (uploadContactsCallback != null) {
            uploadContactsCallback.onFailure(new UploadError("uploadFunc is empty ! "));
        }
        return true;
    }

    public boolean sync(String str) {
        if (CheckUtils.isEmpty(str)) {
            QLog.e("Contacts", "sync but context or contacts is empty! ", new Object[0]);
            return false;
        } else if (this.mSyncTask == null || this.mSyncTask.isOver()) {
            this.mSyncTask = new SyncContactsTask(this.mContext, str, this.uploadFunc);
            this.mSyncTask.run();
            return true;
        } else {
            QLog.e("Contacts", "sync but already running! ", new Object[0]);
            return false;
        }
    }

    public String formatKey(String str) {
        return String.format(USERID_FORMAT, new Object[]{str}) + IContactsExecutor.KEY_LICENSES;
    }

    void uploadContacts(IUpload iUpload, Context context, String str, List<Contact> list, UploadContactsCallback uploadContactsCallback) {
        final Context context2 = context;
        final String str2 = str;
        final UploadContactsCallback uploadContactsCallback2 = uploadContactsCallback;
        final List<Contact> list2 = list;
        iUpload.uploadContacts(list, str, new UploadListener() {
            private boolean isAlreadyCallback = false;

            public synchronized void onSuccess(List<Contact> list, String str) {
                if (TextUtils.isEmpty(str)) {
                    QLog.d("Contacts", "upload hotdog success...", new Object[0]);
                } else {
                    QLog.d("Contacts", "upload hotdog success by tags " + str, new Object[0]);
                }
                DBHelper.INSTANCE.saveContacts(context2, list, str2);
                if (uploadContactsCallback2 != null) {
                    if (this.isAlreadyCallback) {
                        QLog.d("Contacts", "upload hotdog success...(already callback)", new Object[0]);
                    } else {
                        this.isAlreadyCallback = true;
                        uploadContactsCallback2.onSuccess(list2);
                    }
                }
            }

            public synchronized void onFailure(String str) {
                if (TextUtils.isEmpty(str)) {
                    QLog.d("Contacts", "upload hotdog failure...", new Object[0]);
                } else {
                    QLog.d("Contacts", "upload hotdog failure by tags " + str, new Object[0]);
                }
                if (uploadContactsCallback2 != null) {
                    if (this.isAlreadyCallback) {
                        QLog.d("Contacts", "upload hotdog failure...(already callback)", new Object[0]);
                    } else {
                        this.isAlreadyCallback = true;
                        uploadContactsCallback2.onFailure(new UploadError("upload failure..."));
                    }
                }
            }
        });
    }
}
