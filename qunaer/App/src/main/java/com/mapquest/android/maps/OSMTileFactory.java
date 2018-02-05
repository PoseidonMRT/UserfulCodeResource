package com.mapquest.android.maps;

import android.graphics.Point;
import android.graphics.Rect;

class OSMTileFactory implements TileFactory {
    private static final String DEFAULT_URL = "http://c.tile.openstreetmap.org/";
    private static final int TILE_SIZE = 256;
    private static final byte ZOOM_MAX = (byte) 18;
    private static final byte ZOOM_MIN = (byte) 1;
    MapView mapView;
    MercatorProjection projection;
    Point reuse = new Point();
    private TileType type = TileType.MAP;

    public OSMTileFactory(MapView mapView) {
        this.projection = new MercatorProjection(mapView);
        this.mapView = mapView;
    }

    public MapProvider getMapProvider() {
        return MapProvider.OSM;
    }

    public int getTileSize() {
        return 256;
    }

    protected String getTileURL(Tile tile) {
        return getURL(tile.getTileType()) + tile.getZoomLevel() + "/" + tile.getX() + "/" + tile.getY() + ".png";
    }

    public Tile buildTile(Tile tile, int i, TileType tileType) {
        Tile copy = tile.copy(tileType);
        copy.setUrl(getTileURL(copy));
        return copy;
    }

    public Tile buildTile(int i, int i2, int i3, TileType tileType) {
        Point globalFromScreen = this.projection.getGlobalFromScreen(i, i2, this.reuse);
        if (globalFromScreen == null) {
            return null;
        }
        int i4 = globalFromScreen.x / 256;
        int i5 = globalFromScreen.y / 256;
        int i6 = i4 * 256;
        int i7 = i5 * 256;
        int i8 = (i6 - globalFromScreen.x) + i;
        int i9 = (i7 - globalFromScreen.y) + i2;
        Rect rect = new Rect(i8, i9, i8 + 256, i9 + 256);
        Tile tile = new Tile(i4, i5, i6, i7, i3, getProvider(), tileType);
        tile.setUrl(getTileURL(tile));
        tile.setRect(rect);
        return tile;
    }

    public Tile buildTile(GeoPoint geoPoint, int i, TileType tileType) {
        Point toGlobalPixels = this.projection.toGlobalPixels(geoPoint, (Point) null);
        int i2 = toGlobalPixels.x >> 8;
        int i3 = toGlobalPixels.y >> 8;
        Tile tile = new Tile(i2, i3, i2 * 256, i3 * 256, i, getProvider(), tileType);
        tile.setUrl(getTileURL(tile));
        return tile;
    }

    protected String getURL(TileType tileType) {
        return DEFAULT_URL;
    }

    protected String getProvider() {
        return "osm";
    }

    public Projection getProjection() {
        return this.projection;
    }

    public int getMaxZoomLevel() {
        return 18;
    }

    public int getMinZoomLevel() {
        return 1;
    }

    public TileType getTileType() {
        return this.type;
    }

    public void setType(TileType tileType) {
        this.type = tileType;
    }

    public boolean isSupportedTileType(TileType tileType) {
        return tileType == TileType.MAP;
    }
}
