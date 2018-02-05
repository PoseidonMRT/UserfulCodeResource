package com.mqunar.hy.res.libtask;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.OutputStream;

class OutputStreamWarp extends OutputStream {
    private long lastNotifyLen;
    private OutputStream outputStream;
    private NetRequest req;

    public OutputStreamWarp(OutputStream outputStream, NetRequest netRequest) {
        this.outputStream = outputStream;
        this.req = netRequest;
    }

    public OutputStreamWarp(NetRequest netRequest) {
        this(null, netRequest);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(int i) {
        this.outputStream.write(i);
        NetRequest netRequest = this.req;
        netRequest.total++;
        if (this.req.total % PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM == 0) {
            checkNotify();
        }
    }

    public void close() {
        this.outputStream.close();
        checkNotify();
    }

    public void flush() {
        this.outputStream.flush();
        checkNotify();
    }

    public void checkNotify() {
        if (this.lastNotifyLen < this.req.total) {
            this.lastNotifyLen = this.req.total;
            NetRequestManager.getInstance().sendMessageWrite(this.req.handler, this.req);
        }
    }
}
