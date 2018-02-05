package com.mapquest.android.maps;

public class RouteRequest {
    private final String apiKey;
    private String apiVersion;
    private boolean ignoreAmbiguities;
    private final String openURL = "http://open.mapquestapi.com/directions/v2/route?";
    private final String requestJSON;

    public String getRequestJSON() {
        return this.requestJSON;
    }

    public RouteRequest(String str, String str2, boolean z) {
        this.requestJSON = str;
        if (str2 == null) {
            str2 = "";
        }
        this.apiKey = str2;
        this.ignoreAmbiguities = z;
        this.apiVersion = Util.getApiVersion();
    }

    public String getURL() {
        StringBuilder append = new StringBuilder("key=").append(this.apiKey);
        if (this.ignoreAmbiguities) {
            append.append("&ambiguities=ignore");
        }
        append.append("&sdk=").append(this.apiVersion);
        return "http://open.mapquestapi.com/directions/v2/route?" + append.toString();
    }
}
