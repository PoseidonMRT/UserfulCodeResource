package com.mqunar.contacts.basis.impl.common;

import android.content.Context;
import com.mqunar.contacts.basis.async.ITask;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.utils.ContactUtils;
import com.mqunar.libtask.AsyncTask;
import java.util.List;

public class CollectContactsTask extends AsyncTask<Void, Integer, List<Contact>> implements ITask {
    private CollectContactsCallback callback;
    private Context context;
    private Exception exception;

    public CollectContactsTask(Context context, CollectContactsCallback collectContactsCallback) {
        this.context = context;
        this.callback = collectContactsCallback;
    }

    protected List<Contact> doInBackground(Void... voidArr) {
        try {
            return ContactUtils.getContacts(this.context);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(List<Contact> list) {
        super.onPostExecute(list);
        if (this.callback != null) {
            if (this.exception != null) {
                this.callback.onFailure(new CollectContactsError(this.exception));
            } else {
                this.callback.onSuccess(list);
            }
        }
    }

    public void run() {
        executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }
}
