package com.mqunar.cock.network.callback;

import com.mqunar.cock.model.TcpResponseHeader;

public interface TcpMessageCallBack {
    void onReceipt(TcpResponseHeader tcpResponseHeader, byte[] bArr);

    void onTimeOut(byte[] bArr);
}
