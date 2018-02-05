package com.mapquest.android.maps;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

class MemoryTileCache implements ITileCache {
    private static final String TAG = MemoryTileCache.class.getSimpleName();
    private boolean bumped;
    RemoveHandler removeHandler = null;
    private Map<String, Bitmap> synTileCache;
    private Cache tileCache;

    class Cache extends LinkedHashMap<String, Bitmap> {
        private static final float load = 0.6f;
        private static final long serialVersionUID = 1;
        protected int capacity = 20;

        public Cache(int i) {
            super(i, load, true);
            this.capacity = i;
        }

        protected boolean removeEldestEntry(Entry<String, Bitmap> entry) {
            if (size() <= this.capacity) {
                return false;
            }
            MemoryTileCache.this.removeHandler.removeBitmap((Bitmap) entry.getValue());
            return true;
        }
    }

    class RemoveHandler extends Handler {
        static final int REMOVE_BITMAP = 0;
        ConcurrentLinkedQueue<Bitmap> removalQueue;

        public RemoveHandler(int i, Looper looper) {
            super(looper);
            this.removalQueue = null;
            this.removalQueue = new ConcurrentLinkedQueue();
        }

        public void handleMessage(Message message) {
            if (message.what == 0) {
                while (!this.removalQueue.isEmpty()) {
                    Bitmap bitmap = (Bitmap) this.removalQueue.poll();
                    if (!(bitmap == null || bitmap.isRecycled())) {
                        bitmap.recycle();
                    }
                }
            }
            super.handleMessage(message);
        }

        public void removeBitmap(Bitmap bitmap) {
            if (bitmap != null) {
                this.removalQueue.add(bitmap);
                sendEmptyMessage(0);
            }
        }
    }

    public MemoryTileCache(int i) {
        this.tileCache = new Cache(i);
        this.synTileCache = Collections.synchronizedMap(this.tileCache);
        this.removeHandler = new RemoveHandler(i, Looper.getMainLooper());
    }

    public Tile getTile(Tile tile) {
        if (tile == null) {
            return null;
        }
        String buildCacheKey = tile.buildCacheKey();
        if (buildCacheKey == null) {
            return null;
        }
        Cache cache = this.tileCache;
        synchronized (this.tileCache) {
            Bitmap bitmap = (Bitmap) this.tileCache.get(buildCacheKey);
            if (bitmap == null) {
                return null;
            }
            tile.setBitMap(bitmap);
            return tile;
        }
    }

    public void addTile(Tile tile) {
        if (tile.isValid()) {
            String buildCacheKey = tile.buildCacheKey();
            if (!this.tileCache.containsKey(buildCacheKey) && tile.getBitmap() != null) {
                Cache cache = this.tileCache;
                synchronized (this.tileCache) {
                    if (tile.getTileType() == TileType.HYB && !this.bumped) {
                        Cache cache2 = this.tileCache;
                        cache2.capacity *= 2;
                        this.bumped = true;
                    }
                    if (this.tileCache.containsKey(buildCacheKey)) {
                        return;
                    }
                    this.tileCache.put(buildCacheKey, tile.getBitmap());
                }
            }
        }
    }

    public boolean contains(Tile tile) {
        return this.tileCache.containsKey(tile.buildCacheKey());
    }

    public void removeTile(Tile tile) {
        Cache cache = this.tileCache;
        synchronized (this.tileCache) {
            this.removeHandler.removeBitmap((Bitmap) this.tileCache.remove(tile.buildCacheKey()));
        }
    }

    public void clear() {
        Cache cache = this.tileCache;
        synchronized (this.tileCache) {
            for (Bitmap bitmap : this.tileCache.values()) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            this.tileCache.clear();
        }
    }

    public int size() {
        return ((this.tileCache.size() * 256) * 256) * 2;
    }

    public void destroy() {
        clear();
    }
}
