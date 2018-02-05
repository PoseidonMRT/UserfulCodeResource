package com.mqunar.yacca;

public interface YaccaCallback {
    void onClose(YaccaBridge yaccaBridge);

    void onOpen(YaccaBridge yaccaBridge);
}
