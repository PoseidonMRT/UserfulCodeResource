package com.mqunar.contacts.basis.impl;

import com.mqunar.contacts.basis.IContactsExecutor;
import com.mqunar.contacts.basis.db.DBContactsReadCallback;
import com.mqunar.contacts.basis.impl.common.CollectContactsCallback;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.business.net.UploadContactsCallback;
import java.util.List;

public class AndroidMExecutor implements IContactsExecutor {
    public boolean hasPermission(String str) {
        return false;
    }

    public boolean hasLicenses(String str) {
        return false;
    }

    public boolean setLicenses(String str, boolean z) {
        return false;
    }

    public List<Contact> getLocalContactsSync() {
        return null;
    }

    public boolean getLocalContactsAsync(CollectContactsCallback collectContactsCallback) {
        return false;
    }

    public boolean getLocalContactsFromDatabaseAsync(String str, DBContactsReadCallback dBContactsReadCallback) {
        return false;
    }

    public boolean uploadContacts(String str, UploadContactsCallback uploadContactsCallback) {
        return false;
    }

    public boolean sync(String str) {
        return false;
    }
}
