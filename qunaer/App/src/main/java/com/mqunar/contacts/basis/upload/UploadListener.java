package com.mqunar.contacts.basis.upload;

import com.mqunar.contacts.basis.model.Contact;
import java.util.List;

public interface UploadListener {
    void onFailure(String str);

    void onSuccess(List<Contact> list, String str);
}
