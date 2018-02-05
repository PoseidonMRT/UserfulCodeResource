package qunar.sdk.mapapi;

import android.view.View;
import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.listener.QunarInfoWindowClickListener;

public class QunarInfoWindow {
    private Object callbackData;
    private QunarInfoWindowClickListener infoWindowClickListener;
    private QMarker marker;
    private int offsetPixelY = 0;
    private View view;

    public QunarInfoWindow(View view, QMarker qMarker, Object obj, int i, QunarInfoWindowClickListener qunarInfoWindowClickListener) {
        this.view = view;
        this.marker = qMarker;
        this.callbackData = obj;
        this.offsetPixelY = i;
        this.infoWindowClickListener = qunarInfoWindowClickListener;
    }

    public QunarInfoWindowClickListener getInfoWindowClickListener() {
        return this.infoWindowClickListener;
    }

    public View getView() {
        return this.view;
    }

    public QMarker getMarker() {
        return this.marker;
    }

    public Object getCallbackData() {
        return this.callbackData;
    }

    public int getOffsetPixelY() {
        return this.offsetPixelY;
    }
}
