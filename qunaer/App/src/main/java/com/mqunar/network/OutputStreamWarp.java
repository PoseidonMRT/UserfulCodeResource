package com.mqunar.network;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.OutputStream;

public class OutputStreamWarp extends OutputStream {
    private OutputStream a;
    private NetRequest b;
    private long c;

    public OutputStreamWarp(OutputStream outputStream, NetRequest netRequest) {
        this.a = outputStream;
        this.b = netRequest;
    }

    public OutputStreamWarp(NetRequest netRequest) {
        this(null, netRequest);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.a = outputStream;
    }

    public void write(int i) {
        this.a.write(i);
        NetRequest netRequest = this.b;
        netRequest.total++;
        if (this.b.total % PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM == 0) {
            checkNotify();
        }
    }

    public void close() {
        this.a.close();
        checkNotify();
    }

    public void flush() {
        this.a.flush();
        checkNotify();
    }

    public void checkNotify() {
        if (this.c < this.b.total) {
            this.c = this.b.total;
            NetRequestManager.getInstance().sendMessageWrite(this.b.handler, this.b);
        }
    }
}
