package com.mqunar.hy.res.Listener;

public interface QpDownloadListener {
    void onMessageError();

    void onQpDownLoaded();

    void requestResultHasQp();

    void requestResultNoQp();
}
