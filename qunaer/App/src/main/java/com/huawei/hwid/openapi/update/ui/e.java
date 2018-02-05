package com.huawei.hwid.openapi.update.ui;

class e implements Runnable {
    final /* synthetic */ OtaDownloadActivity a;

    e(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void run() {
        this.a.finish();
    }
}
