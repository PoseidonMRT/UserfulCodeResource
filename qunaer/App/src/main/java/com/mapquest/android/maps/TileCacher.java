package com.mapquest.android.maps;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

class TileCacher implements ITileCache {
    private static final String TAG = TileCacher.class.getSimpleName();
    private ITileCache db;
    private ITileCache memory;
    private int total = 0;

    public enum CacheType {
        DB,
        MEMORY,
        ALL
    }

    public TileCacher(Context context) {
        Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        checkCacheSize(defaultDisplay.getHeight(), defaultDisplay.getWidth());
        this.db = new FSTileCache(context, true);
    }

    public ITileCache getCache(CacheType cacheType) {
        if (CacheType.DB == cacheType) {
            return this.db;
        }
        if (CacheType.MEMORY == cacheType) {
            return this.memory;
        }
        return CacheType.ALL != cacheType ? null : this;
    }

    public void addTile(Tile tile) {
        this.memory.addTile(tile);
        this.db.addTile(tile);
    }

    public Tile getTile(Tile tile) {
        Tile tile2 = this.memory.getTile(tile);
        if (tile2 == null) {
            return this.db.getTile(tile);
        }
        return tile2;
    }

    public void removeTile(Tile tile) {
        getCache(CacheType.MEMORY).removeTile(tile);
        getCache(CacheType.DB).removeTile(tile);
        if (tile.getBitmap() != null && !tile.getBitmap().isRecycled()) {
            tile.getBitmap().recycle();
        }
    }

    public boolean contains(Tile tile) {
        return getCache(CacheType.MEMORY).contains(tile) ? true : getCache(CacheType.DB).contains(tile);
    }

    public void clear() {
        getCache(CacheType.MEMORY).clear();
        getCache(CacheType.DB).clear();
    }

    public int size() {
        return getCache(CacheType.DB).size();
    }

    public void destroy() {
        this.db.destroy();
        this.db = null;
        this.memory.destroy();
        this.memory = null;
    }

    public void checkCacheSize(int i, int i2) {
        float f = 2.0f;
        int i3 = ((i2 / 256) + 2) * ((i / 256) + 2);
        if (i3 > 25 && i3 < 50) {
            f = 1.5f;
        } else if (i3 > 50) {
            f = 1.1f;
        }
        int i4 = (int) (f * ((float) i3));
        if (i4 > this.total) {
            synchronized (this) {
                this.total = i4;
                if (this.memory != null) {
                    this.memory.destroy();
                }
                this.memory = new MemoryTileCache(this.total);
            }
        }
    }
}
