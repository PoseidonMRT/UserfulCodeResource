package com.mqunar.contacts.basis.upload;

import com.mqunar.contacts.basis.model.Contact;
import java.util.List;

public interface IUpload {
    void uploadContacts(List<Contact> list, String str, UploadListener uploadListener);
}
