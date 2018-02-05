package com.mqunar.cock.network;

import com.mqunar.cock.model.TcpResponseHeader;
import com.mqunar.cock.model.TcpResult;
import com.mqunar.cock.network.callback.TcpMessageCallBack;
import java.util.concurrent.CountDownLatch;

class h implements TcpMessageCallBack {
    final /* synthetic */ TcpResult a;
    final /* synthetic */ CountDownLatch b;
    final /* synthetic */ OneKeyCremation c;

    h(OneKeyCremation oneKeyCremation, TcpResult tcpResult, CountDownLatch countDownLatch) {
        this.c = oneKeyCremation;
        this.a = tcpResult;
        this.b = countDownLatch;
    }

    public void onReceipt(TcpResponseHeader tcpResponseHeader, byte[] bArr) {
        this.a.resultcode = tcpResponseHeader.code;
        this.a.tcpResponseHeader = tcpResponseHeader;
        this.a.bytes = bArr;
        this.b.countDown();
    }

    public void onTimeOut(byte[] bArr) {
        this.a.resultcode = 300;
        this.b.countDown();
    }
}
