package qunar.sdk.mapapi;

import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.listener.QunarGeoCoderResultListener;

public interface QunarGeoCoder {
    void destory();

    boolean reverseGeoCode(QLocation qLocation);

    void setOnGetGeoCodeResultListener(QunarGeoCoderResultListener qunarGeoCoderResultListener);
}
