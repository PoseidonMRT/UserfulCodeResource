package com.mqunar.contacts.business.net.param;

import com.mqunar.contacts.basis.model.Contact;
import java.io.Serializable;
import java.util.List;

public class UploadParam implements Serializable {
    public List<Contact> phoneContacts;
    public String uuid;
}
