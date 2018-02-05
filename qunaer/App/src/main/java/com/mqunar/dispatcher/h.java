package com.mqunar.dispatcher;

import android.content.Intent;
import android.net.Uri;
import com.mqunar.tools.log.QLog;

final class h implements Runnable {
    final /* synthetic */ Object a;
    final /* synthetic */ String b;
    final /* synthetic */ Intent c;
    final /* synthetic */ Uri d;

    h(Object obj, String str, Intent intent, Uri uri) {
        this.a = obj;
        this.b = str;
        this.c = intent;
        this.d = uri;
    }

    public void run() {
        try {
            DispatcherLogic.b(this.a, this.b, this.c, this.d);
        } catch (Throwable e) {
            QLog.e(e);
        }
    }
}
