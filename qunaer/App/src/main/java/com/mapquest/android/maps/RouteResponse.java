package com.mapquest.android.maps;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import qunar.sdk.mapapi.utils.MapConstant.QUNAR_GPS_FIELD;

public class RouteResponse extends ServiceResponse {
    public Collections collections;
    private JSONHelper helper;
    public Route route;

    public class Collections {
        public List<List<Location>> locationList = new ArrayList();

        public Collections(JSONArray jSONArray) {
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONArray jSONArray2 = RouteResponse.this.helper.getJSONArray(i, jSONArray);
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    JSONObject jSONObject = RouteResponse.this.helper.getJSONObject(i2, jSONArray2);
                    RouteResponse.this.getClass();
                    arrayList.add(new Location(jSONObject));
                }
                this.locationList.add(arrayList);
            }
        }
    }

    public class Location {
        public String adminArea1;
        public String adminArea1Type;
        public String adminArea2;
        public String adminArea2Type;
        public String adminArea3;
        public String adminArea3Type;
        public String adminArea4;
        public String adminArea4Type;
        public String adminArea5;
        public String adminArea5Type;
        public GeoPoint displayLatLng;
        public String geocodeQuality;
        public String geocodeQualityCode;
        public GeoPoint latLng;
        public int linkId;
        public JSONObject locationJSON;
        public String postalCode;
        public String sideOfStreet;
        public String street;
        public String type;

        public Location(JSONObject jSONObject) {
            this.locationJSON = jSONObject;
            this.latLng = RouteResponse.this.buildGeoPoint(RouteResponse.this.helper.getJSONObject("latLng", jSONObject));
            this.displayLatLng = RouteResponse.this.buildGeoPoint(RouteResponse.this.helper.getJSONObject("displayLatLng", jSONObject));
            this.adminArea1 = RouteResponse.this.helper.getString("adminArea1", jSONObject);
            this.adminArea1Type = RouteResponse.this.helper.getString("adminArea1Type", jSONObject);
            this.adminArea2 = RouteResponse.this.helper.getString("adminArea2", jSONObject);
            this.adminArea2Type = RouteResponse.this.helper.getString("adminArea2Type", jSONObject);
            this.adminArea3 = RouteResponse.this.helper.getString("adminArea3", jSONObject);
            this.adminArea3Type = RouteResponse.this.helper.getString("adminArea3Type", jSONObject);
            this.adminArea4 = RouteResponse.this.helper.getString("adminArea4", jSONObject);
            this.adminArea4Type = RouteResponse.this.helper.getString("adminArea4Type", jSONObject);
            this.adminArea5 = RouteResponse.this.helper.getString("adminArea5", jSONObject);
            this.adminArea5Type = RouteResponse.this.helper.getString("adminArea5Type", jSONObject);
            this.street = RouteResponse.this.helper.getString("street", jSONObject);
            this.type = RouteResponse.this.helper.getString("type", jSONObject);
            this.linkId = RouteResponse.this.helper.getInt("linkId", jSONObject);
            this.postalCode = RouteResponse.this.helper.getString("postalCode", jSONObject);
            this.sideOfStreet = RouteResponse.this.helper.getString("sideOfStreet", jSONObject);
            this.geocodeQuality = RouteResponse.this.helper.getString("geocodeQuality", jSONObject);
            this.geocodeQualityCode = RouteResponse.this.helper.getString("geocodeQualityCode", jSONObject);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            String str = this.geocodeQuality;
            if ("ADDRESS".equals(str) || "POINT".equals(str)) {
                stringBuilder.append(this.street).append(" ").append(this.adminArea5).append(", ").append(this.adminArea3).append(" ").append(this.postalCode);
            } else if ("STREET".equals(str)) {
                stringBuilder.append(this.street).append(" ").append(this.adminArea5).append(", ").append(this.adminArea3);
            } else if ("COUNTRY".equals(str)) {
                stringBuilder.append(this.adminArea1);
            } else if ("STATE".equals(str)) {
                stringBuilder.append(this.adminArea3);
            } else if ("COUNTY".equals(str)) {
                stringBuilder.append(this.adminArea4).append(" ").append(this.adminArea3);
            } else if ("ZIP".equals(str) || "ZIP_EXTENDED".equals(str)) {
                stringBuilder.append(this.postalCode).append(" ").append(this.adminArea3);
            } else if ("CITY".equals(str)) {
                stringBuilder.append(this.adminArea5).append(", ").append(this.adminArea3);
            } else if ("LATLNG".equals(str)) {
                if (this.displayLatLng != null) {
                    stringBuilder.append("Latitude: ").append(this.displayLatLng.getLatitude()).append(", Longitude: ").append(this.displayLatLng.getLongitude());
                } else {
                    stringBuilder.append("Latitude: ").append(this.latLng.getLatitude()).append(", Longitude: ").append(this.latLng.getLongitude());
                }
            }
            return stringBuilder.toString();
        }
    }

    class TimeDistance {
        public double distance;
        public String formattedTime;
        public int time;

        public TimeDistance(JSONObject jSONObject) {
            this.time = RouteResponse.this.helper.getInt(QUNAR_GPS_FIELD.TIME_FIELD, jSONObject);
            this.distance = RouteResponse.this.helper.getDouble("distance", jSONObject).doubleValue();
            this.formattedTime = RouteResponse.this.helper.getString("formattedTime", jSONObject);
        }
    }

    class RouteMeta extends TimeDistance {
        public boolean hasFerry;
        public boolean hasHighway;
        public boolean hasSeasonalClosure;
        public boolean hasTollRoad;
        public boolean hasUnpaved;

        public RouteMeta(JSONObject jSONObject) {
            super(jSONObject);
            this.hasSeasonalClosure = RouteResponse.this.helper.getBoolean("hasSeasonalClosure", jSONObject);
            this.hasUnpaved = RouteResponse.this.helper.getBoolean("hasUnpaved", jSONObject);
            this.hasHighway = RouteResponse.this.helper.getBoolean("hasHighway", jSONObject);
            this.hasFerry = RouteResponse.this.helper.getBoolean("hasFerry", jSONObject);
            this.hasTollRoad = RouteResponse.this.helper.getBoolean("hasTollRoad", jSONObject);
        }
    }

    public class Route extends RouteMeta {
        public BoundingBox boundingBox = new BoundingBox();
        public List<Leg> legs = new ArrayList();
        public List<Integer> locationSequence = new ArrayList();
        public List<Location> locations = new ArrayList();
        public Options options;
        public String sessionId;
        public Shape shape;
        final /* synthetic */ RouteResponse this$0;

        public class Leg extends RouteMeta {
            public int index;
            public List<Maneuver> maneuvers = new ArrayList();
            public Route route;

            public class Maneuver extends TimeDistance {
                public int attributes;
                public int direction;
                public String directionName;
                public String iconUrl;
                public int index;
                public String mapUrl;
                public String narrative;
                public List<Sign> signs = new ArrayList();
                public GeoPoint startPoint;
                public List<String> streets = new ArrayList();
                final /* synthetic */ Leg this$2;
                public String transportMode;
                public int turnType;

                public class Sign {
                    public int direction;
                    public String extraText;
                    public String text;
                    public int type;
                    public String url;

                    public Sign(com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver r3, org.json.JSONObject r4) {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
                        /*
                        r2 = this;
                        com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver.this = r3;
                        r2.<init>();
                        r0 = r3.this$2;
                        r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                        r0 = r0.this$0;
                        r0 = r0.helper;
                        r1 = "text";
                        r0 = r0.getString(r1, r4);
                        r2.text = r0;
                        r0 = r3.this$2;
                        r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                        r0 = r0.this$0;
                        r0 = r0.helper;
                        r1 = "extraText";
                        r0 = r0.getString(r1, r4);
                        r2.extraText = r0;
                        r0 = r3.this$2;
                        r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                        r0 = r0.this$0;
                        r0 = r0.helper;
                        r1 = "direction";
                        r0 = r0.getInt(r1, r4);
                        r2.direction = r0;
                        r0 = r3.this$2;
                        r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                        r0 = r0.this$0;
                        r0 = r0.helper;
                        r1 = "type";
                        r0 = r0.getInt(r1, r4);
                        r2.type = r0;
                        r0 = r3.this$2;
                        r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                        r0 = r0.this$0;
                        r0 = r0.helper;
                        r1 = "url";
                        r0 = r0.getString(r1, r4);
                        r2.url = r0;
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver.Sign.<init>(com.mapquest.android.maps.RouteResponse$Route$Leg$Maneuver, org.json.JSONObject):void");
                    }
                }

                public Maneuver(com.mapquest.android.maps.RouteResponse.Route.Leg r7, org.json.JSONObject r8) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
                    /*
                    r6 = this;
                    r1 = 0;
                    r6.this$2 = r7;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r6.<init>(r8);
                    r0 = new java.util.ArrayList;
                    r0.<init>();
                    r6.signs = r0;
                    r0 = new java.util.ArrayList;
                    r0.<init>();
                    r6.streets = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "signs";
                    r2 = r0.getJSONArray(r2, r8);
                    r0 = r1;
                L_0x0027:
                    r3 = r2.length();
                    if (r0 >= r3) goto L_0x0048;
                L_0x002d:
                    r3 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r3 = r3.this$0;
                    r3 = r3.helper;
                    r3 = r3.getJSONObject(r0, r2);
                    if (r3 == 0) goto L_0x0045;
                L_0x003b:
                    r4 = r6.signs;
                    r5 = new com.mapquest.android.maps.RouteResponse$Route$Leg$Maneuver$Sign;
                    r5.<init>(r3);
                    r4.add(r5);
                L_0x0045:
                    r0 = r0 + 1;
                    goto L_0x0027;
                L_0x0048:
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "index";
                    r0 = r0.getInt(r2, r8);
                    r6.index = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "direction";
                    r0 = r0.getInt(r2, r8);
                    r6.direction = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "narrative";
                    r0 = r0.getString(r2, r8);
                    r6.narrative = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "iconUrl";
                    r0 = r0.getString(r2, r8);
                    r6.iconUrl = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r2 = "streets";
                    r0 = r0.getJSONArray(r2, r8);
                L_0x0096:
                    r2 = r0.length();
                    if (r1 >= r2) goto L_0x00b2;
                L_0x009c:
                    r2 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r2 = r2.this$0;
                    r2 = r2.helper;
                    r2 = r2.getString(r1, r0);
                    if (r2 == 0) goto L_0x00af;
                L_0x00aa:
                    r3 = r6.streets;
                    r3.add(r2);
                L_0x00af:
                    r1 = r1 + 1;
                    goto L_0x0096;
                L_0x00b2:
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r1 = "attributes";
                    r0 = r0.getInt(r1, r8);
                    r6.attributes = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r1 = "transportMode";
                    r0 = r0.getString(r1, r8);
                    r6.transportMode = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r1 = "directionName";
                    r0 = r0.getString(r1, r8);
                    r6.directionName = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r1 = "mapUrl";
                    r0 = r0.getString(r1, r8);
                    r6.mapUrl = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r1 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r1 = r1.this$0;
                    r1 = r1.helper;
                    r2 = "startPoint";
                    r1 = r1.getJSONObject(r2, r8);
                    r0 = r0.buildGeoPoint(r1);
                    r6.startPoint = r0;
                    r0 = com.mapquest.android.maps.RouteResponse.Route.this;
                    r0 = r0.this$0;
                    r0 = r0.helper;
                    r1 = "turnType";
                    r0 = r0.getInt(r1, r8);
                    r6.turnType = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver.<init>(com.mapquest.android.maps.RouteResponse$Route$Leg, org.json.JSONObject):void");
                }
            }

            public Leg(com.mapquest.android.maps.RouteResponse.Route r6, org.json.JSONObject r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
                /*
                r5 = this;
                com.mapquest.android.maps.RouteResponse.Route.this = r6;
                r0 = r6.this$0;
                r5.<init>(r7);
                r0 = new java.util.ArrayList;
                r0.<init>();
                r5.maneuvers = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "index";
                r0 = r0.getInt(r1, r7);
                r5.index = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "maneuvers";
                r1 = r0.getJSONArray(r1, r7);
                r0 = 0;
            L_0x0029:
                r2 = r1.length();
                if (r0 >= r2) goto L_0x0048;
            L_0x002f:
                r2 = r6.this$0;
                r2 = r2.helper;
                r2 = r2.getJSONObject(r0, r1);
                if (r2 == 0) goto L_0x0045;
            L_0x003b:
                r3 = r5.maneuvers;
                r4 = new com.mapquest.android.maps.RouteResponse$Route$Leg$Maneuver;
                r4.<init>(r5, r2);
                r3.add(r4);
            L_0x0045:
                r0 = r0 + 1;
                goto L_0x0029;
            L_0x0048:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.Leg.<init>(com.mapquest.android.maps.RouteResponse$Route, org.json.JSONObject):void");
            }
        }

        public class Options {
            public boolean avoidTimedConditions;
            public List<Integer> avoidTripIds = new ArrayList();
            public boolean countryBoundaryDisplay;
            public int cyclingRoadFactor;
            public boolean destinationManeuverDisplay;
            public boolean enhancedNarrative;
            public int filterZoneFactor = -1;
            public int generalize;
            public String locale;
            public int maneuverPenalty;
            public boolean manmaps;
            public int maxLinkId;
            public int maxWalkingDistance;
            public List<Integer> mustAvoidLinkIds = new ArrayList();
            public String narrativeType;
            public String projection;
            public int routeNumber;
            public String routeType;
            public String shapeFormat;
            public boolean sideOfStreetDisplay;
            public boolean stateBoundaryDisplay;
            final /* synthetic */ Route this$1;
            public int timeType;
            public int transferPenalty;
            public List<Integer> tryAvoidLinkIds = new ArrayList();
            public String unit;
            public int urbanAvoidFactor;
            public int walkingSpeed;

            public Options(com.mapquest.android.maps.RouteResponse.Route r6, org.json.JSONObject r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
                /*
                r5 = this;
                r1 = 0;
                r5.this$1 = r6;
                r5.<init>();
                r0 = new java.util.ArrayList;
                r0.<init>();
                r5.mustAvoidLinkIds = r0;
                r0 = -1;
                r5.filterZoneFactor = r0;
                r0 = new java.util.ArrayList;
                r0.<init>();
                r5.tryAvoidLinkIds = r0;
                r0 = new java.util.ArrayList;
                r0.<init>();
                r5.avoidTripIds = r0;
                if (r7 == 0) goto L_0x01f0;
            L_0x0020:
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "mustAvoidLinkIds";
                r2 = r0.getJSONArray(r2, r7);
                if (r2 == 0) goto L_0x004b;
            L_0x002e:
                r0 = r1;
            L_0x002f:
                r3 = r2.length();
                if (r0 >= r3) goto L_0x004b;
            L_0x0035:
                r3 = r5.mustAvoidLinkIds;
                r4 = r6.this$0;
                r4 = r4.helper;
                r4 = r4.getInt(r0, r2);
                r4 = java.lang.Integer.valueOf(r4);
                r3.add(r4);
                r0 = r0 + 1;
                goto L_0x002f;
            L_0x004b:
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "countryBoundaryDisplay";
                r0 = r0.getBoolean(r2, r7);
                r5.countryBoundaryDisplay = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "generalize";
                r0 = r0.getInt(r2, r7);
                r5.generalize = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "narrativeType";
                r0 = r0.getString(r2, r7);
                r5.narrativeType = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "projection";
                r0 = r0.getString(r2, r7);
                r5.projection = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "locale";
                r0 = r0.getString(r2, r7);
                r5.locale = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "avoidTimedConditions";
                r0 = r0.getBoolean(r2, r7);
                r5.avoidTimedConditions = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "destinationManeuverDisplay";
                r0 = r0.getBoolean(r2, r7);
                r5.destinationManeuverDisplay = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "enhancedNarrative";
                r0 = r0.getBoolean(r2, r7);
                r5.enhancedNarrative = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "filterZoneFactor";
                r0 = r0.getInt(r2, r7);
                r5.filterZoneFactor = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "timeType";
                r0 = r0.getInt(r2, r7);
                r5.timeType = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "maxWalkingDistance";
                r0 = r0.getInt(r2, r7);
                r5.maxWalkingDistance = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "routeType";
                r0 = r0.getString(r2, r7);
                r5.routeType = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "transferPenalty";
                r0 = r0.getInt(r2, r7);
                r5.transferPenalty = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "stateBoundaryDisplay";
                r0 = r0.getBoolean(r2, r7);
                r5.stateBoundaryDisplay = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "walkingSpeed";
                r0 = r0.getInt(r2, r7);
                r5.walkingSpeed = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "maxLinkId";
                r0 = r0.getInt(r2, r7);
                r5.maxLinkId = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "tryAvoidLinkIds";
                r2 = r0.getJSONArray(r2, r7);
                if (r2 == 0) goto L_0x0156;
            L_0x0139:
                r0 = r1;
            L_0x013a:
                r3 = r2.length();
                if (r0 >= r3) goto L_0x0156;
            L_0x0140:
                r3 = r5.tryAvoidLinkIds;
                r4 = r6.this$0;
                r4 = r4.helper;
                r4 = r4.getInt(r0, r2);
                r4 = java.lang.Integer.valueOf(r4);
                r3.add(r4);
                r0 = r0 + 1;
                goto L_0x013a;
            L_0x0156:
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "unit";
                r0 = r0.getString(r2, r7);
                r5.unit = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "routeNumber";
                r0 = r0.getInt(r2, r7);
                r5.routeNumber = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "maneuverPenalty";
                r0 = r0.getInt(r2, r7);
                r5.maneuverPenalty = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r2 = "avoidTripIds";
                r0 = r0.getJSONArray(r2, r7);
                if (r0 == 0) goto L_0x01aa;
            L_0x018e:
                r2 = r0.length();
                if (r1 >= r2) goto L_0x01aa;
            L_0x0194:
                r2 = r5.avoidTripIds;
                r3 = r6.this$0;
                r3 = r3.helper;
                r3 = r3.getInt(r1, r0);
                r3 = java.lang.Integer.valueOf(r3);
                r2.add(r3);
                r1 = r1 + 1;
                goto L_0x018e;
            L_0x01aa:
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "manmaps";
                r0 = r0.getBoolean(r1, r7);
                r5.manmaps = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "sideOfStreetDisplay";
                r0 = r0.getBoolean(r1, r7);
                r5.sideOfStreetDisplay = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "cyclingRoadFactor";
                r0 = r0.getInt(r1, r7);
                r5.cyclingRoadFactor = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "urbanAvoidFactor";
                r0 = r0.getInt(r1, r7);
                r5.urbanAvoidFactor = r0;
                r0 = r6.this$0;
                r0 = r0.helper;
                r1 = "shapeFormat";
                r0 = r0.getString(r1, r7);
                r5.shapeFormat = r0;
            L_0x01f0:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.Options.<init>(com.mapquest.android.maps.RouteResponse$Route, org.json.JSONObject):void");
            }
        }

        public class Shape {
            public List<Integer> legIndexes = new ArrayList();
            public List<Integer> manueverIndexes = new ArrayList();
            public List<GeoPoint> shapePoints = new ArrayList();
            final /* synthetic */ Route this$1;

            public Shape(com.mapquest.android.maps.RouteResponse.Route r10, org.json.JSONObject r11, com.mapquest.android.maps.RouteResponse.Route.Options r12) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
                /*
                r9 = this;
                r6 = 6;
                r5 = 1;
                r1 = 0;
                r9.this$1 = r10;
                r9.<init>();
                r0 = new java.util.ArrayList;
                r0.<init>();
                r9.manueverIndexes = r0;
                r0 = new java.util.ArrayList;
                r0.<init>();
                r9.shapePoints = r0;
                r0 = new java.util.ArrayList;
                r0.<init>();
                r9.legIndexes = r0;
                r0 = r10.this$0;
                r0 = r0.helper;
                r2 = "maneuverIndexes";
                r2 = r0.getJSONArray(r2, r11);
                r0 = r1;
            L_0x002a:
                r3 = r2.length();
                if (r0 >= r3) goto L_0x0046;
            L_0x0030:
                r3 = r9.manueverIndexes;
                r4 = r10.this$0;
                r4 = r4.helper;
                r4 = r4.getInt(r0, r2);
                r4 = java.lang.Integer.valueOf(r4);
                r3.add(r4);
                r0 = r0 + 1;
                goto L_0x002a;
            L_0x0046:
                r0 = r12.shapeFormat;
                r2 = "raw";
                r2 = r2.equals(r0);
                if (r2 == 0) goto L_0x0086;
            L_0x0050:
                r0 = r10.this$0;
                r0 = r0.helper;
                r2 = "shapePoints";
                r2 = r0.getJSONArray(r2, r11);
                r0 = r1;
            L_0x005d:
                r3 = r2.length();
                if (r0 >= r3) goto L_0x00ab;
            L_0x0063:
                r3 = r10.this$0;
                r3 = r3.helper;
                r3 = r3.getDouble(r0, r2);
                r0 = r0 + 1;
                r5 = r10.this$0;
                r5 = r5.helper;
                r5 = r5.getDouble(r0, r2);
                r7 = r9.shapePoints;
                r8 = new com.mapquest.android.maps.GeoPoint;
                r8.<init>(r3, r5);
                r7.add(r8);
                r0 = r0 + 1;
                goto L_0x005d;
            L_0x0086:
                r2 = "cmp6";
                r2 = r2.equals(r0);
                if (r2 == 0) goto L_0x00d3;
            L_0x008e:
                r0 = new com.mapquest.android.util.ShapeTransform;
                r0.<init>(r6);
                r0.setOptimizeShape(r5);
                r2 = r10.this$0;
                r2 = r2.helper;
                r3 = "shapePoints";
                r2 = r2.getString(r3, r11);
                r3 = r9.shapePoints;
                r0 = r0.decodeCompressed(r2);
                r3.addAll(r0);
            L_0x00ab:
                r0 = r10.this$0;
                r0 = r0.helper;
                r2 = "legIndexes";
                r0 = r0.getJSONArray(r2, r11);
            L_0x00b7:
                r2 = r0.length();
                if (r1 >= r2) goto L_0x00f9;
            L_0x00bd:
                r2 = r9.legIndexes;
                r3 = r10.this$0;
                r3 = r3.helper;
                r3 = r3.getInt(r1, r0);
                r3 = java.lang.Integer.valueOf(r3);
                r2.add(r3);
                r1 = r1 + 1;
                goto L_0x00b7;
            L_0x00d3:
                r2 = "cmp5";
                r0 = r2.equals(r0);
                if (r0 == 0) goto L_0x00ab;
            L_0x00db:
                r0 = new com.mapquest.android.util.ShapeTransform;
                r0.<init>(r6);
                r0.setOptimizeShape(r5);
                r2 = r10.this$0;
                r2 = r2.helper;
                r3 = "shapePoints";
                r2 = r2.getString(r3, r11);
                r3 = r9.shapePoints;
                r0 = r0.decodeCompressed(r2);
                r3.addAll(r0);
                goto L_0x00ab;
            L_0x00f9:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.Shape.<init>(com.mapquest.android.maps.RouteResponse$Route, org.json.JSONObject, com.mapquest.android.maps.RouteResponse$Route$Options):void");
            }
        }

        public Route(com.mapquest.android.maps.RouteResponse r7, org.json.JSONObject r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r6 = this;
            r1 = 0;
            r6.this$0 = r7;
            r6.<init>(r8);
            r0 = new com.mapquest.android.maps.BoundingBox;
            r0.<init>();
            r6.boundingBox = r0;
            r0 = new java.util.ArrayList;
            r0.<init>();
            r6.locationSequence = r0;
            r0 = new java.util.ArrayList;
            r0.<init>();
            r6.locations = r0;
            r0 = new java.util.ArrayList;
            r0.<init>();
            r6.legs = r0;
            r0 = new com.mapquest.android.maps.RouteResponse$Route$Options;
            r2 = r7.helper;
            r3 = "options";
            r2 = r2.getJSONObject(r3, r8);
            r0.<init>(r6, r2);
            r6.options = r0;
            r0 = new com.mapquest.android.maps.RouteResponse$Route$Shape;
            r2 = r7.helper;
            r3 = "shape";
            r2 = r2.getJSONObject(r3, r8);
            r3 = r6.options;
            r0.<init>(r6, r2, r3);
            r6.shape = r0;
            r0 = r7.helper;
            r2 = "boundingBox";
            r0 = r0.getJSONObject(r2, r8);
            if (r0 == 0) goto L_0x0076;
        L_0x0052:
            r2 = r6.boundingBox;
            r3 = r7.helper;
            r4 = "ul";
            r3 = r3.getJSONObject(r4, r0);
            r3 = r7.buildGeoPoint(r3);
            r2.ul = r3;
            r2 = r6.boundingBox;
            r3 = r7.helper;
            r4 = "lr";
            r0 = r3.getJSONObject(r4, r0);
            r0 = r7.buildGeoPoint(r0);
            r2.lr = r0;
        L_0x0076:
            r0 = r7.helper;
            r2 = "locationSequence";
            r2 = r0.getJSONArray(r2, r8);
            r0 = r1;
        L_0x0081:
            r3 = r2.length();
            if (r0 >= r3) goto L_0x009b;
        L_0x0087:
            r3 = r6.locationSequence;
            r4 = r7.helper;
            r4 = r4.getInt(r0, r2);
            r4 = java.lang.Integer.valueOf(r4);
            r3.add(r4);
            r0 = r0 + 1;
            goto L_0x0081;
        L_0x009b:
            r0 = r7.helper;
            r2 = "sessionId";
            r0 = r0.getString(r2, r8);
            r6.sessionId = r0;
            r0 = r7.helper;
            r2 = "locations";
            r2 = r0.getJSONArray(r2, r8);
            r0 = r1;
        L_0x00b2:
            r3 = r2.length();
            if (r0 >= r3) goto L_0x00d2;
        L_0x00b8:
            r3 = r7.helper;
            r3 = r3.getJSONObject(r0, r2);
            if (r3 == 0) goto L_0x00cf;
        L_0x00c2:
            r4 = r6.locations;
            r5 = new com.mapquest.android.maps.RouteResponse$Location;
            r7.getClass();
            r5.<init>(r3);
            r4.add(r5);
        L_0x00cf:
            r0 = r0 + 1;
            goto L_0x00b2;
        L_0x00d2:
            r0 = r7.helper;
            r2 = "legs";
            r0 = r0.getJSONArray(r2, r8);
        L_0x00dc:
            r2 = r0.length();
            if (r1 >= r2) goto L_0x00f7;
        L_0x00e2:
            r2 = r7.helper;
            r2 = r2.getJSONObject(r1, r0);
            r3 = r6.legs;
            r4 = new com.mapquest.android.maps.RouteResponse$Route$Leg;
            r4.<init>(r2);
            r3.add(r4);
            r1 = r1 + 1;
            goto L_0x00dc;
        L_0x00f7:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.RouteResponse.Route.<init>(com.mapquest.android.maps.RouteResponse, org.json.JSONObject):void");
        }
    }

    public RouteResponse(JSONObject jSONObject) {
        super(jSONObject);
        this.helper = super.getHelper();
        JSONArray jSONArray = this.helper.getJSONArray("collections", jSONObject);
        if (jSONArray != null) {
            this.collections = new Collections(jSONArray);
        }
        this.route = new Route(this, this.helper.getJSONObject("route", jSONObject));
    }

    GeoPoint buildGeoPoint(JSONObject jSONObject) {
        return jSONObject != null ? new GeoPoint(this.helper.getDouble("lat", jSONObject).doubleValue(), this.helper.getDouble("lng", jSONObject).doubleValue()) : null;
    }
}
