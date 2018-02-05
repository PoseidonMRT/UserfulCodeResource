package com.mqunar.cock.network;

public interface ImListener {
    void onClosed();

    void onConnected();

    void onMessage(byte[] bArr);
}
