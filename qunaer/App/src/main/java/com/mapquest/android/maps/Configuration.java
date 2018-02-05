package com.mapquest.android.maps;

public class Configuration {
    private String apiKey = null;
    private MapView mapView;
    private String platformApiKey = null;
    private boolean satellite = false;
    private boolean satelliteLabeled = true;
    private int trafficMinimumZoomLevel = 5;
    private String trafficURL = "http://www.mapquestapi.com/traffic/v1";

    Configuration(MapView mapView) {
        this.mapView = mapView;
    }

    String getApiKey() {
        return this.apiKey;
    }

    void setApiKey(String str) {
        this.apiKey = str;
    }

    public boolean isSatelliteLabeled() {
        return this.satelliteLabeled;
    }

    public void setSatelliteLabeled(boolean z) {
        if (this.satelliteLabeled != z) {
            this.satelliteLabeled = z;
            this.mapView.setSatellite(this.satellite, this.satelliteLabeled);
        }
    }

    public boolean isSatellite() {
        return this.satellite;
    }

    public void setSatellite(boolean z) {
        this.satellite = z;
        this.mapView.setSatellite(this.satellite, this.satelliteLabeled);
    }

    int getTrafficMinimumZoomLevel() {
        return this.trafficMinimumZoomLevel;
    }

    void setTrafficMinimumZoomLevel(int i) {
        this.trafficMinimumZoomLevel = i;
    }

    String getTrafficURL() {
        return this.trafficURL;
    }

    void setTrafficURL(String str) {
        this.trafficURL = str;
    }

    String getPlatformApiKey() {
        return this.platformApiKey == null ? getApiKey() : this.platformApiKey;
    }

    void setPlatformApiKey(String str) {
        this.platformApiKey = str;
    }
}
