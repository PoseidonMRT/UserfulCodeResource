package qunar.sdk.mapapi.gaodeMapImp;

import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarGeoCoder;
import qunar.sdk.mapapi.listener.QunarGeoCoderResultListener;

public class GaodeGeoCoder implements QunarGeoCoder {
    public boolean reverseGeoCode(QLocation qLocation) {
        return false;
    }

    public void setOnGetGeoCodeResultListener(QunarGeoCoderResultListener qunarGeoCoderResultListener) {
    }

    public void destory() {
    }
}
