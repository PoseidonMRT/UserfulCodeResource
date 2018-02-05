package com.mqunar.storage;

import android.content.SharedPreferences.Editor;

class a implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ Editor b;
    final /* synthetic */ SpStorage c;

    a(SpStorage spStorage, String str, Editor editor) {
        this.c = spStorage;
        this.a = str;
        this.b = editor;
    }

    public void run() {
        if (this.c.d.containsKey(this.a)) {
            this.b.commit();
            synchronized (this.c) {
                this.c.d.remove(this.a);
            }
        }
    }
}
