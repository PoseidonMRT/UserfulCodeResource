package com.baidu.mapapi.map;

import android.util.Log;
import com.baidu.mapapi.common.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public final class TileOverlay {
    private static final String b = TileOverlay.class.getSimpleName();
    private static int f = 0;
    BaiduMap a;
    private ExecutorService c = Executors.newFixedThreadPool(1);
    private HashMap<String, Tile> d = new HashMap();
    private HashSet<String> e = new HashSet();
    private TileProvider g;

    public TileOverlay(BaiduMap baiduMap, TileProvider tileProvider) {
        this.a = baiduMap;
        this.g = tileProvider;
    }

    private synchronized Tile a(String str) {
        Tile tile;
        if (this.d.containsKey(str)) {
            tile = (Tile) this.d.get(str);
            this.d.remove(str);
        } else {
            tile = null;
        }
        return tile;
    }

    private synchronized void a(String str, Tile tile) {
        this.d.put(str, tile);
    }

    private synchronized boolean b(String str) {
        return this.e.contains(str);
    }

    private synchronized void c(String str) {
        this.e.add(str);
    }

    Tile a(int i, int i2, int i3) {
        String str = i + "_" + i2 + "_" + i3;
        Tile a = a(str);
        if (a != null) {
            return a;
        }
        if (this.a != null && f == 0) {
            MapStatus mapStatus = this.a.getMapStatus();
            f = (((mapStatus.a.j.bottom - mapStatus.a.j.top) / 256) + 2) * (((mapStatus.a.j.right - mapStatus.a.j.left) / 256) + 2);
        }
        if (this.d.size() > f) {
            a();
        }
        if (!(b(str) || this.c.isShutdown())) {
            try {
                c(str);
                this.c.execute(new t(this, i, i2, i3, str));
            } catch (RejectedExecutionException e) {
                Log.e(b, "ThreadPool excepiton");
            } catch (Exception e2) {
                Log.e(b, "fileDir is not legal");
            }
        }
        return null;
    }

    synchronized void a() {
        Logger.logE(b, "clearTaskSet");
        this.e.clear();
        this.d.clear();
    }

    void b() {
        this.c.shutdownNow();
    }

    public boolean clearTileCache() {
        return this.a.b();
    }

    public void removeTileOverlay() {
        if (this.a != null) {
            this.a.a(this);
        }
    }
}
