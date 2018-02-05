package com.mapquest.android.maps;

class TileType {
    public static TileType HYB = new TileType("hyb");
    public static TileType MAP = new TileType("map");
    public static TileType MOBMAP = new TileType("mobmap");
    public static TileType SAT = new TileType("sat");
    public static TileType SATHYB = new TileType("sathyb");
    public static TileType TRAFFIC = new TileType("traffic");
    final String value;

    public TileType(String str) {
        this.value = str;
    }

    public static TileType valueOf(String str) {
        if (MAP.value.equalsIgnoreCase(str)) {
            return MAP;
        }
        if (SAT.value.equalsIgnoreCase(str)) {
            return SAT;
        }
        if (HYB.value.equalsIgnoreCase(str)) {
            return HYB;
        }
        if (SATHYB.value.equalsIgnoreCase(str)) {
            return SATHYB;
        }
        if (TRAFFIC.value.equalsIgnoreCase(str)) {
            return TRAFFIC;
        }
        return MOBMAP.value.equalsIgnoreCase(str) ? MOBMAP : null;
    }

    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }
}
