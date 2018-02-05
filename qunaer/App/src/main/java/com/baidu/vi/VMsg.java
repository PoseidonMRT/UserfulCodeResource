package com.baidu.vi;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class VMsg {
    private static final String a = VMsg.class.getSimpleName();
    private static Handler b;
    private static HandlerThread c;

    class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            VMsg.OnUserCommand1(message.what, message.arg1, message.arg2, message.obj == null ? 0 : ((Long) message.obj).longValue());
        }
    }

    private static native void OnUserCommand1(int i, int i2, int i3, long j);

    public static void destroy() {
        c.quit();
        c = null;
        b.removeCallbacksAndMessages(null);
        b = null;
    }

    public static void init() {
        c = new HandlerThread("VIMsgThread");
        c.start();
        b = new a(c.getLooper());
    }

    private static void postMessage(int i, int i2, int i3, long j) {
        if (b != null) {
            Message.obtain(b, i, i2, i3, Long.valueOf(j)).sendToTarget();
        }
    }
}
