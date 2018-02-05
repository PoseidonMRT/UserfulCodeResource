package com.mqunar.contacts.basis.impl.common;

import com.mqunar.contacts.basis.model.Contact;
import java.util.List;

public interface CollectContactsCallback {
    void onFailure(CollectContactsError collectContactsError);

    void onSuccess(List<Contact> list);
}
