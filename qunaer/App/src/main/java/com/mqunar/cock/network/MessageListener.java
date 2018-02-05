package com.mqunar.cock.network;

import com.mqunar.cock.model.RequestHttpMessage;

public interface MessageListener {
    void onRequest(int i, RequestHttpMessage requestHttpMessage);
}
