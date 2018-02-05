package com.qunar.qwifi;

import android.net.wifi.ScanResult;
import java.util.Comparator;

final class d implements Comparator<ScanResult> {
    d() {
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((ScanResult) obj, (ScanResult) obj2);
    }

    public int a(ScanResult scanResult, ScanResult scanResult2) {
        return scanResult2.level - scanResult.level;
    }
}
