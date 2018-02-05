package com.mqunar.contacts.basis;

import com.mqunar.contacts.basis.db.DBContactsReadCallback;
import com.mqunar.contacts.basis.impl.common.CollectContactsCallback;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.business.net.UploadContactsCallback;
import java.util.List;

public interface IContactsExecutor {
    public static final String KEY_LICENSES = "__licenses";

    boolean getLocalContactsAsync(CollectContactsCallback collectContactsCallback);

    boolean getLocalContactsFromDatabaseAsync(String str, DBContactsReadCallback dBContactsReadCallback);

    List<Contact> getLocalContactsSync();

    boolean hasLicenses(String str);

    boolean hasPermission(String str);

    boolean setLicenses(String str, boolean z);

    boolean sync(String str);

    boolean uploadContacts(String str, UploadContactsCallback uploadContactsCallback);
}
