package com.mqunar.cock.network;

import com.mqunar.cock.model.RequestHttpMessage;
import com.mqunar.cock.model.TcpResponseHeader;

public interface OnCockMessage {
    void onConnected();

    void onMessage(TcpResponseHeader tcpResponseHeader, byte[] bArr);

    void onNetEnd(boolean z);

    void onRequest(int i, RequestHttpMessage requestHttpMessage);
}
