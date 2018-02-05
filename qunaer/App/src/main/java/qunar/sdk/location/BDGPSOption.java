package qunar.sdk.location;

public class BDGPSOption extends QunarGPSOption {
    private BDGPSCoorType coorType;
    private BDGPSLocationMode locationMode;
    private boolean openGps = false;

    public BDGPSOption(boolean z, BDGPSCoorType bDGPSCoorType, BDGPSLocationMode bDGPSLocationMode, int i, int i2) {
        this.openGps = z;
        this.coorType = bDGPSCoorType;
        this.locationMode = bDGPSLocationMode;
        this.scanSpan = i;
        this.timeout = i2;
    }

    public boolean isOpenGps() {
        return this.openGps;
    }

    public BDGPSCoorType getCoorType() {
        return this.coorType;
    }

    public BDGPSLocationMode getLocationMode() {
        return this.locationMode;
    }
}
