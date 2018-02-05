package com.mqunar.idscan.a;

import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import com.mqunar.tools.log.QLog;

final class g implements PreviewCallback {
    private static final String a = g.class.getSimpleName();
    private final d b;
    private Handler c;
    private int d;

    g(d dVar) {
        this.b = dVar;
    }

    final void a(Handler handler, int i) {
        this.c = handler;
        this.d = i;
    }

    public final void onPreviewFrame(byte[] bArr, Camera camera) {
        Point a = this.b.a();
        Handler handler = this.c;
        if (a == null || handler == null) {
            QLog.d(a, "Got preview callback, but no handler or resolution available", new Object[0]);
            return;
        }
        handler.obtainMessage(this.d, a.x, a.y, bArr).sendToTarget();
        this.c = null;
    }
}
