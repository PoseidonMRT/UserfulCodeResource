package qunar.sdk.mapapi.listener;

import qunar.sdk.location.QLocation;

public interface MapStatusChangeListener {
    void onMapStatusChange(QLocation qLocation, float f);

    void onMapStatusChangeFinish(QLocation qLocation, float f);

    void onMapStatusChangeStart(QLocation qLocation, float f);
}
