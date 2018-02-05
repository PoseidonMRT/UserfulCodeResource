package com.mapquest.android.maps;

class MapProvider {
    public static MapProvider MAPQUEST = new MapProvider("MAPQUEST");
    public static MapProvider OSM = new MapProvider("OSM");
    public static MapProvider TOMTOM = new MapProvider("TOMTOM");
    protected String geocodeUrl = null;
    protected String mapConfigUrl = null;
    protected String platformApiKey = null;
    protected String tileFallbackUrl = null;
    protected String trafficUrl = null;
    private final String value;

    static {
        OSM.tileFallbackUrl = "http://otile1.mqcdn.com/tiles/1.0.0/";
        OSM.trafficUrl = "";
        OSM.platformApiKey = "";
        MAPQUEST.tileFallbackUrl = "http://mtile01.mqcdn.com/tiles/1.0.0/vy/";
        MAPQUEST.trafficUrl = "http://www.mapquestapi.com/traffic/v1";
        MAPQUEST.platformApiKey = "Amjtd%7Cluu2216rn1%2C8w%3Do5-lz7l5";
        TOMTOM.tileFallbackUrl = "http://ttiles01.mqcdn.com/tiles/1.0.0/vy/";
        TOMTOM.trafficUrl = "http://traffic.tt.mapquest.com/traffic/v1";
        TOMTOM.platformApiKey = "Cmjtd%7Cluua2qu2nd%2Cb5%3Do5-gzb0";
    }

    MapProvider(String str) {
        this.value = str;
    }

    public String value() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        MapProvider mapProvider = (MapProvider) obj;
        if (this.value == null) {
            if (mapProvider.value != null) {
                return false;
            }
            return true;
        } else if (this.value.equals(mapProvider.value)) {
            return true;
        } else {
            return false;
        }
    }

    public String getTileFallbackUrl() {
        return this.tileFallbackUrl;
    }

    public String getGeocodeUrl() {
        return this.geocodeUrl;
    }

    public String getTrafficUrl() {
        return this.trafficUrl;
    }

    public String getPlatformApiKey() {
        return this.platformApiKey;
    }
}
