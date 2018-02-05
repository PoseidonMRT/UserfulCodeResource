package com.mapquest.android.maps;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class MapActivity extends Activity {
    private static final String TAG = "com.mapquest.android.maps.mapactivity";
    private ArrayList<MapView> mapViews = new ArrayList();

    protected abstract boolean isRouteDisplayed();

    protected boolean isLocationDisplayed() {
        Iterator it = copyMapViews().iterator();
        while (it.hasNext()) {
            for (Overlay overlay : ((MapView) it.next()).getOverlays()) {
                if (overlay instanceof MyLocationOverlay) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void onPause() {
        Iterator it = copyMapViews().iterator();
        while (it.hasNext()) {
            ((MapView) it.next()).onPause();
        }
        super.onPause();
    }

    protected void onResume() {
        Iterator it = copyMapViews().iterator();
        while (it.hasNext()) {
            ((MapView) it.next()).onResume();
        }
        super.onResume();
    }

    protected void onStop() {
        Iterator it = copyMapViews().iterator();
        while (it.hasNext()) {
            ((MapView) it.next()).onStop();
        }
        super.onStop();
    }

    protected void onDestroy() {
        Iterator it = copyMapViews().iterator();
        while (it.hasNext()) {
            MapView mapView = (MapView) it.next();
            if (mapView != null) {
                mapView.destroy();
            }
        }
        super.onDestroy();
    }

    private ArrayList<MapView> copyMapViews() {
        return (ArrayList) this.mapViews.clone();
    }

    void addMapView(MapView mapView) {
        this.mapViews.add(mapView);
    }

    void removeMapView(MapView mapView) {
        this.mapViews.remove(mapView);
    }

    String getKey() {
        Iterator it = this.mapViews.iterator();
        if (it.hasNext()) {
            return ((MapView) it.next()).getConfiguration().getApiKey();
        }
        return "";
    }
}
