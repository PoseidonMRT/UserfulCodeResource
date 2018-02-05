package qunar.sdk.mapapi.mapquest;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import com.mapquest.android.maps.MapView;
import qunar.sdk.mapapi.IMapView;
import qunar.sdk.mapapi.QunarMapInitOptions;
import qunar.sdk.mapapi.QunarMapType;

public class MapQuestView implements IMapView {
    public static String MAPQUEST_KEY = "";
    private QunarMapInitOptions initOptions;
    private MapView mapView;

    public MapQuestView(Context context, QunarMapInitOptions qunarMapInitOptions) {
        String str = null;
        this.mapView = str;
        this.initOptions = qunarMapInitOptions;
        try {
            str = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("com.mapquest.maps.API_KEY");
        } catch (NameNotFoundException e) {
        }
        if (TextUtils.isEmpty(str)) {
            this.mapView = new MapView(context);
        } else {
            MAPQUEST_KEY = str;
            this.mapView = new MapView(context, str);
        }
        this.mapView.getController().stopPanning();
        this.mapView.setBuiltInZoomControls(qunarMapInitOptions.zoomControlsEnabled);
    }

    public MapView getMapView() {
        return this.mapView;
    }

    public void onDestroy() {
        if (this.mapView != null) {
            this.mapView.destroy();
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
        if (this.mapView != null) {
            this.mapView.onSaveInstanceState(bundle);
        }
    }

    public void onStop() {
        if (this.mapView != null) {
            this.mapView.onStop();
        }
    }

    public QunarMapInitOptions getInitOptions() {
        if (this.initOptions == null) {
            this.initOptions = new QunarMapInitOptions();
            this.initOptions.mapType = QunarMapType.MAPQUEST;
        }
        return this.initOptions;
    }
}
