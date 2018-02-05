package qunar.sdk.location;

import qunar.sdk.PermissionsListener;

public interface QunarGPSLocationListener extends PermissionsListener {
    void onReceiveLocation(QLocation qLocation);
}
