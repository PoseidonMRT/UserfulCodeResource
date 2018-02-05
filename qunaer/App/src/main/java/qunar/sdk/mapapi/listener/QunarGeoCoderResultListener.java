package qunar.sdk.mapapi.listener;

import qunar.sdk.location.QLocation;

public interface QunarGeoCoderResultListener {
    void onGetReverseGeoCodeResult(boolean z, QLocation qLocation, String str);
}
