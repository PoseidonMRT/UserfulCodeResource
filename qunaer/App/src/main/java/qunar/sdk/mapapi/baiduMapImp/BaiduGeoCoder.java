package qunar.sdk.mapapi.baiduMapImp;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarGeoCoder;
import qunar.sdk.mapapi.listener.QunarGeoCoderResultListener;

public class BaiduGeoCoder implements OnGetGeoCoderResultListener, QunarGeoCoder {
    private GeoCoder mSearch;
    private QunarGeoCoderResultListener qunarGeoCoderResultListener;

    public BaiduGeoCoder() {
        this.mSearch = null;
        this.mSearch = GeoCoder.newInstance();
        this.mSearch.setOnGetGeoCodeResultListener(this);
    }

    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (this.qunarGeoCoderResultListener != null && this.qunarGeoCoderResultListener != null) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != ERRORNO.NO_ERROR || reverseGeoCodeResult.getLocation() == null) {
                this.qunarGeoCoderResultListener.onGetReverseGeoCodeResult(false, null, "");
            } else {
                this.qunarGeoCoderResultListener.onGetReverseGeoCodeResult(true, new QLocation(reverseGeoCodeResult.getLocation().latitude, reverseGeoCodeResult.getLocation().longitude), reverseGeoCodeResult.getAddress());
            }
        }
    }

    public void setOnGetGeoCodeResultListener(QunarGeoCoderResultListener qunarGeoCoderResultListener) {
        this.qunarGeoCoderResultListener = qunarGeoCoderResultListener;
    }

    public boolean reverseGeoCode(QLocation qLocation) {
        if (qLocation == null || this.mSearch == null) {
            return false;
        }
        return this.mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(qLocation.getLatitude(), qLocation.getLongitude())));
    }

    public void destory() {
        if (this.mSearch != null) {
            this.mSearch.destroy();
        }
        this.mSearch = null;
    }
}
