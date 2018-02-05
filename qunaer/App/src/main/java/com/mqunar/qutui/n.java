package com.mqunar.qutui;

import android.content.SharedPreferences.Editor;

class n implements Runnable {
    final /* synthetic */ Editor a;
    final /* synthetic */ m b;

    n(m mVar, Editor editor) {
        this.b = mVar;
        this.a = editor;
    }

    public void run() {
        this.a.commit();
    }
}
