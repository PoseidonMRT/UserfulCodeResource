package com.mqunar.idscan.decode;

import android.os.Handler;
import android.os.Message;
import com.mqunar.idscan.a.f;
import com.mqunar.idscan.activity.a;
import com.mqunar.idscan.model.ScanPassportResultData;
import com.mqunar.tools.log.QLog;

public final class CaptureActivityHandler extends Handler {
    private static final String a = CaptureActivityHandler.class.getSimpleName();
    private final a b;
    private final c c;
    private State d = State.SUCCESS;
    private final f e;

    enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(a aVar, f fVar) {
        this.b = aVar;
        this.c = new c(aVar);
        this.c.start();
        this.e = fVar;
        fVar.c();
        b();
    }

    private void b() {
        if (this.d == State.SUCCESS) {
            this.d = State.PREVIEW;
            this.e.a(this.c.a());
            this.b.a();
        }
    }

    public final void a() {
        this.d = State.DONE;
        this.e.d();
        Message.obtain(this.c.a(), 21).sendToTarget();
        try {
            this.c.join(500);
        } catch (Throwable e) {
            QLog.e(e);
        }
        removeMessages(9);
        removeMessages(10);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 8:
                QLog.d(a, "Got restart preview message", new Object[0]);
                b();
                return;
            case 9:
                QLog.d(a, "Got decode succeeded message", new Object[0]);
                QLog.d(a, "message == " + message.obj.toString(), new Object[0]);
                this.d = State.SUCCESS;
                this.b.a((ScanPassportResultData) message.obj);
                this.b.b().setLineShouldMove(false);
                return;
            case 10:
                this.d = State.PREVIEW;
                this.e.a(this.c.a());
                return;
            default:
                return;
        }
    }
}
