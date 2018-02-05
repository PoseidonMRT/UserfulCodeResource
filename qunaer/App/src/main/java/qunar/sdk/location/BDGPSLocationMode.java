package qunar.sdk.location;

import com.baidu.location.LocationClientOption.LocationMode;

public enum BDGPSLocationMode {
    Battery_Saving,
    Device_Sensors,
    Hight_Accuracy;

    public static LocationMode convert2BDMode(BDGPSLocationMode bDGPSLocationMode) {
        switch (bDGPSLocationMode) {
            case Battery_Saving:
                return LocationMode.Battery_Saving;
            case Device_Sensors:
                return LocationMode.Device_Sensors;
            case Hight_Accuracy:
                return LocationMode.Hight_Accuracy;
            default:
                return null;
        }
    }
}
