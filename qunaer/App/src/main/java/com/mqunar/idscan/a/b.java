package com.mqunar.idscan.a;

import android.os.AsyncTask;
import com.mqunar.tools.log.QLog;

final class b extends AsyncTask {
    final /* synthetic */ a a;

    private b(a aVar) {
        this.a = aVar;
    }

    protected final Object doInBackground(Object... objArr) {
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            QLog.e(e);
        }
        synchronized (this.a) {
            if (this.a.b) {
                this.a.a();
            }
        }
        return null;
    }
}
