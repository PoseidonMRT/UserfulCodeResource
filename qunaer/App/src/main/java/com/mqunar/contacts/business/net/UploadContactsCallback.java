package com.mqunar.contacts.business.net;

import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.business.net.error.UploadError;
import java.util.List;

public interface UploadContactsCallback {
    void onFailure(UploadError uploadError);

    void onSuccess(List<Contact> list);
}
