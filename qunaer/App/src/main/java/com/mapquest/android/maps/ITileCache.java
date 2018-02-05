package com.mapquest.android.maps;

interface ITileCache {
    void addTile(Tile tile);

    void clear();

    boolean contains(Tile tile);

    void destroy();

    Tile getTile(Tile tile);

    void removeTile(Tile tile);

    int size();
}
