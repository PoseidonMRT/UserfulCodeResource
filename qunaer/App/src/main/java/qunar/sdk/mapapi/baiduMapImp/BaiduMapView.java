package qunar.sdk.mapapi.baiduMapImp;

import android.content.Context;
import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapView;
import qunar.sdk.mapapi.IMapView;
import qunar.sdk.mapapi.QunarMapInitOptions;
import qunar.sdk.mapapi.QunarMapType;

public class BaiduMapView implements IMapView {
    private QunarMapInitOptions initOptions;
    private MapView mapView = null;

    public BaiduMapView(Context context) {
        this.mapView = new MapView(context);
    }

    public BaiduMapView(Context context, QunarMapInitOptions qunarMapInitOptions) {
        this.initOptions = qunarMapInitOptions;
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.compassEnabled(qunarMapInitOptions.compassEnabled);
        baiduMapOptions.overlookingGesturesEnabled(qunarMapInitOptions.overlookingGesturesEnabled);
        baiduMapOptions.rotateGesturesEnabled(qunarMapInitOptions.rotateGesturesEnabled);
        baiduMapOptions.scaleControlEnabled(qunarMapInitOptions.scaleControlEnabled);
        baiduMapOptions.zoomControlsEnabled(qunarMapInitOptions.zoomControlsEnabled);
        baiduMapOptions.zoomGesturesEnabled(qunarMapInitOptions.zoomGesturesEnabled);
        this.mapView = new MapView(context, baiduMapOptions);
    }

    public MapView getMapView() {
        return this.mapView;
    }

    public void onDestroy() {
        if (this.mapView != null) {
            this.mapView.onDestroy();
        }
    }

    public void onPause() {
        if (this.mapView != null) {
            this.mapView.onPause();
        }
    }

    public void onResume() {
        if (this.mapView != null) {
            this.mapView.onResume();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
    }

    public void onStop() {
    }

    public QunarMapInitOptions getInitOptions() {
        if (this.initOptions == null) {
            this.initOptions = new QunarMapInitOptions();
            this.initOptions.mapType = QunarMapType.BAIDU;
        }
        return this.initOptions;
    }
}
