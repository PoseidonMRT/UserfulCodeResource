package com.baidu.mapapi.map;

import android.util.Log;

class t implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ String d;
    final /* synthetic */ TileOverlay e;

    t(TileOverlay tileOverlay, int i, int i2, int i3, String str) {
        this.e = tileOverlay;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = str;
    }

    public void run() {
        Tile tile = ((FileTileProvider) this.e.g).getTile(this.a, this.b, this.c);
        if (tile == null) {
            Log.e(TileOverlay.b, "FileTile pic is null");
        } else if (tile.width == 256 && tile.height == 256) {
            this.e.a(this.a + "_" + this.b + "_" + this.c, tile);
        } else {
            Log.e(TileOverlay.b, "FileTile pic must be 256 * 256");
        }
        this.e.e.remove(this.d);
    }
}
