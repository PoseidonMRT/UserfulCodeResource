package com.mapquest.android.maps;

interface TileFactory {
    Tile buildTile(int i, int i2, int i3, TileType tileType);

    Tile buildTile(GeoPoint geoPoint, int i, TileType tileType);

    Tile buildTile(Tile tile, int i, TileType tileType);

    MapProvider getMapProvider();

    int getMaxZoomLevel();

    int getMinZoomLevel();

    Projection getProjection();

    int getTileSize();

    TileType getTileType();

    boolean isSupportedTileType(TileType tileType);

    void setType(TileType tileType);
}
