package com.mapquest.android.maps;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class DefaultItemizedOverlay extends ItemizedOverlay<OverlayItem> {
    private Drawable defaultMarker;
    private List<OverlayItem> items = new ArrayList();

    public DefaultItemizedOverlay(Drawable drawable) {
        super(ItemizedOverlay.boundCenterBottom(drawable));
        this.defaultMarker = drawable;
    }

    protected OverlayItem createItem(int i) {
        return (OverlayItem) this.items.get(i);
    }

    public int size() {
        return this.items.size();
    }

    public void addItem(OverlayItem overlayItem) {
        if (overlayItem.getMarker(overlayItem.getState()) == null) {
            overlayItem.setMarker(this.defaultMarker);
        }
        this.items.add(overlayItem);
        populate();
    }

    public void removeItem(OverlayItem overlayItem) {
        this.items.remove(overlayItem);
        populate();
    }

    protected int getIndexToDraw(int i) {
        return i;
    }

    public void clear() {
        super.clear();
        this.items.clear();
    }

    public void destroy() {
        clear();
    }

    protected boolean onTap(int i) {
        return true;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (!super.onTap(geoPoint, mapView) || this.tapListener == null) {
            return false;
        }
        this.tapListener.onTap(geoPoint, mapView);
        return true;
    }
}
