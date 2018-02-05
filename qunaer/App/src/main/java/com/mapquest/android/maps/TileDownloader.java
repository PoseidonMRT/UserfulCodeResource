package com.mapquest.android.maps;

interface TileDownloader {
    void beginQueue();

    void clearQueue();

    void destroy();

    void endQueue();

    void finishedDownload(Tile tile);

    void queueTile(Tile tile);
}
