package com.mapquest.android.maps;

class MapQuestTileFactory extends OSMTileFactory {
    private static final String LOG_TAG = MapQuestTileFactory.class.getSimpleName();
    protected static final boolean USE_TILE_CACHE_SERVER = false;
    protected MapConfiguration mapConfiguration;
    StringBuilder sb = new StringBuilder(256);

    public MapQuestTileFactory(MapView mapView, MapConfiguration mapConfiguration) {
        super(mapView);
        this.mapConfiguration = mapConfiguration;
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
        return MapProvider.MAPQUEST.getTileFallbackUrl();
    }

    protected String getURL(TileType tileType) {
        return this.mapConfiguration.getUrlPattern("commercial", tileType.value());
    }

    protected String getProvider() {
        return "mq";
    }

    public MapProvider getMapProvider() {
        return MapProvider.MAPQUEST;
    }

    public boolean isSupportedTileType(TileType tileType) {
        return tileType == TileType.MAP || tileType == TileType.HYB || tileType == TileType.SAT || tileType == TileType.SATHYB || tileType == TileType.TRAFFIC;
    }
}
