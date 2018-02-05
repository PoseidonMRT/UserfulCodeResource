package com.mqunar.contacts.basis.upload;

import com.mqunar.contacts.basis.model.Contact;
import java.util.List;

public class MutiPartUploadListener implements UploadListener {
    public int curIndex = -1;
    private UploadListener listener;
    public int total = -1;

    public MutiPartUploadListener(int i, int i2, UploadListener uploadListener) {
        this.listener = uploadListener;
        this.curIndex = i;
        this.total = i2;
    }

    public void onSuccess(List<Contact> list, String str) {
        this.listener.onSuccess(list, str);
    }

    public void onFailure(String str) {
        this.listener.onFailure(str);
    }
}
