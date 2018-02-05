package com.mqunar.dispatcher;

import android.net.Uri;
import org.acra.ACRA;

final class f implements Runnable {
    final /* synthetic */ Uri a;

    f(Uri uri) {
        this.a = uri;
    }

    public void run() {
        ACRA.getErrorReporter().handleSilentException(new RuntimeException(this.a.toString()));
    }
}
