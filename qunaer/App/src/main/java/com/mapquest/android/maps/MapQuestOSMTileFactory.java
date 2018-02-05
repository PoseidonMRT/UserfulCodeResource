package com.mapquest.android.maps;

class MapQuestOSMTileFactory extends OSMTileFactory {
    private static final String LOG_TAG = "mq.maps.mapquestosmtilefactory";
    protected MapConfiguration mapConfiguration;
    private StringBuilder sb = new StringBuilder(256);

    public MapQuestOSMTileFactory(MapView mapView, MapConfiguration mapConfiguration) {
        super(mapView);
        this.mapConfiguration = mapConfiguration;
    }

    protected String getURL(TileType tileType) {
        return this.mapConfiguration.getUrlPattern("open", tileType.value());
    }

    protected String getTileURL(Tile tile) {
        String url = getURL(tile.getTileType());
        if (url == null) {
            return getFallbackTileUrl(tile);
        }
        try {
            return url.replace("{$z}", tile.getZoomLevel() + "").replace("{$x}", tile.getX() + "").replace("{$y}", tile.getY() + "").replace("{$ext}", "jpg");
        } catch (Exception e) {
            return getFallbackTileUrl(tile);
        }
    }

    private String getFallbackTileUrl(Tile tile) {
        String fallbackUrl = getFallbackUrl();
        this.sb.setLength(0);
        this.sb.append(fallbackUrl);
        this.sb.append(tile.getTileType()).append("/");
        this.sb.append(tile.getZoomLevel()).append("/");
        this.sb.append(tile.getX()).append("/");
        this.sb.append(tile.getY()).append(".jpg");
        return this.sb.toString();
    }

    protected String getFallbackUrl() {
        return MapProvider.OSM.getTileFallbackUrl();
    }

    protected String getProvider() {
        return "mqosm";
    }

    public MapProvider getMapProvider() {
        return MapProvider.OSM;
    }

    public boolean isSupportedTileType(TileType tileType) {
        return tileType == TileType.MAP || tileType == TileType.HYB || tileType == TileType.SAT || tileType == TileType.SATHYB || tileType == TileType.TRAFFIC;
    }
}
