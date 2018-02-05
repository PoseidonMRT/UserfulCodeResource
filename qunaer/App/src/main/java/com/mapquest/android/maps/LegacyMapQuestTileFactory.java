package com.mapquest.android.maps;

import android.graphics.Point;
import android.graphics.Rect;

class LegacyMapQuestTileFactory implements TileFactory {
    private static final String BASE_URL = "http://vtiles01.mqcdn.com/tiles/std/vy/";
    private static final String LOG_TAG = "com.mapquest.android.maps.legacymapquesttilefactory";
    private static double PIXELSPERLATDEGREE = 3.15552459661917E8d;
    private static double PIXELSPERLNGDEGREE = 2.5034459790989706E8d;
    private static int[] SCALES = new int[]{88011773, 29337258, 9779086, 3520471, 1504475, 701289, 324767, 154950, 74999, 36000, 18000, 9000, 4700, 2500, 1500, 1000};
    private static final int TILE_SIZE = 256;
    private static final byte ZOOM_MAX = (byte) 16;
    private static final byte ZOOM_MIN = (byte) 1;
    private static final String provider = "mq";
    private MapView mapView;
    private MapQuestProjection projection;
    private String tileCacheVersion;
    private TileType type = TileType.MAP;

    public LegacyMapQuestTileFactory(MapView mapView) {
        this.projection = new MapQuestProjection(mapView);
        this.mapView = mapView;
    }

    public void setType(TileType tileType) {
        this.type = tileType;
    }

    public MapProvider getMapProvider() {
        return MapProvider.MAPQUEST;
    }

    private String getTileURL(Tile tile) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        stringBuilder.append(tile.getTileType().value());
        stringBuilder.append("?s=" + SCALES[tile.getZoomLevel() - 1]);
        stringBuilder.append("&x=" + tile.getX());
        stringBuilder.append("&y=" + tile.getY());
        return stringBuilder.toString();
    }

    public Tile buildTile(Tile tile, int i, TileType tileType) {
        Tile copy = tile.copy(tileType);
        copy.setUrl(getTileURL(copy));
        return copy;
    }

    public Tile buildTile(GeoPoint geoPoint, int i, TileType tileType) {
        int globalX = this.projection.getGlobalX(Util.from1E6(geoPoint.getLongitudeE6()), i) >> 8;
        int globalY = this.projection.getGlobalY(Util.from1E6(geoPoint.getLatitudeE6()), i) >> 8;
        Tile tile = new Tile(globalX, globalY, globalX * 256, (globalY * 256) + 256, i, provider, tileType);
        tile.setUrl(getTileURL(tile));
        return tile;
    }

    public Tile buildTile(int i, int i2, int i3, TileType tileType) {
        Point globalFromScreen = this.projection.getGlobalFromScreen(i, i2);
        if (globalFromScreen == null) {
            return null;
        }
        int i4 = globalFromScreen.x >> 8;
        int i5 = globalFromScreen.y >> 8;
        int i6 = i4 << 8;
        int i7 = (i5 << 8) + 256;
        int i8 = (i6 - globalFromScreen.x) + i;
        int i9 = (globalFromScreen.y - i7) + i2;
        Rect rect = new Rect(i8, i9, i8 + 256, i9 + 256);
        Tile tile = new Tile(i4, i5, i6, i7, i3, provider, tileType);
        tile.setUrl(getTileURL(tile));
        tile.setRect(rect);
        return tile;
    }

    public int getMaxZoomLevel() {
        return 16;
    }

    public int getMinZoomLevel() {
        return 1;
    }

    public Projection getProjection() {
        return this.projection;
    }

    public int getTileSize() {
        return 256;
    }

    public TileType getTileType() {
        return this.type;
    }

    public boolean isSupportedTileType(TileType tileType) {
        return tileType == TileType.MAP || tileType == TileType.HYB || tileType == TileType.SAT || tileType == TileType.TRAFFIC;
    }
}
