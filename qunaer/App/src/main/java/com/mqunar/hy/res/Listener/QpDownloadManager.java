package com.mqunar.hy.res.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class QpDownloadManager {
    private static QpDownloadManager loadUrlManager;
    private Map<String, QpDownloadListener> map = Collections.synchronizedMap(new HashMap());

    private QpDownloadManager() {
    }

    public static QpDownloadManager getInstance() {
        if (loadUrlManager == null) {
            loadUrlManager = new QpDownloadManager();
        }
        return loadUrlManager;
    }

    public void registerQpDownloadListener(String str, QpDownloadListener qpDownloadListener) {
    }

    public void unRegisterQpDownloadListener(String str) {
    }

    public Map<String, QpDownloadListener> getMap() {
        return this.map;
    }

    public void sendRequestResultNoQpl(String str) {
    }

    public void sendRequestResultHasQp(String str) {
    }

    public void sendMessageError(String str) {
    }

    public void sendQpDownLoaded(String str) {
    }
}
