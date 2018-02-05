package com.mapquest.android.maps;

import android.graphics.Bitmap;
import android.graphics.Rect;
import java.util.Date;

class Tile implements Comparable<Tile> {
    private Bitmap bitmap;
    private byte[] bytes;
    private Date createdOn;
    private long id;
    private String key = cacheKey();
    private final int pixelX;
    private final int pixelY;
    private int priority;
    private final String provider;
    private Rect rect;
    private TileType tileType;
    private String url;
    private final int x;
    private final int y;
    private final int zoomLevel;

    public Tile(int i, int i2, int i3, int i4, int i5, String str, TileType tileType) {
        this.x = i;
        this.y = i2;
        this.pixelX = i3;
        this.pixelY = i4;
        this.zoomLevel = i5;
        this.provider = str;
        this.tileType = tileType;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public int getPixelX() {
        return this.pixelX;
    }

    public int getPixelY() {
        return this.pixelY;
    }

    public int getZoomLevel() {
        return this.zoomLevel;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public TileType getTileType() {
        return this.tileType;
    }

    public int compareTo(Tile tile) {
        return this.priority - tile.priority;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setBytes(byte[] bArr) {
        this.bytes = bArr;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public Rect getRect() {
        return this.rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    private String cacheKey() {
        StringBuilder stringBuilder = new StringBuilder(24);
        stringBuilder.append(getProvider()).append("_");
        stringBuilder.append(getTileType()).append("_");
        stringBuilder.append(getZoomLevel()).append("_");
        stringBuilder.append(getX()).append("_");
        stringBuilder.append(getY());
        return stringBuilder.toString();
    }

    public String buildCacheKey() {
        return this.key;
    }

    public void setId(long j) {
        this.id = j;
    }

    public long getId() {
        return this.id;
    }

    public void setCreatedOn(Date date) {
        this.createdOn = date;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public boolean isValid() {
        if (getBitmap() == null || getBitmap().isRecycled()) {
            return getBytes() != null && getBytes().length > 0;
        } else {
            return true;
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitMap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Tile copy(TileType tileType) {
        return new Tile(this.x, this.y, this.pixelX, this.pixelY, this.zoomLevel, this.provider, tileType == null ? this.tileType : tileType);
    }

    public int hashCode() {
        return buildCacheKey().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return buildCacheKey().equals(((Tile) obj).buildCacheKey());
    }

    public String toString() {
        return "Tile [id=" + this.id + ", pixelX=" + this.pixelX + ", pixelY=" + this.pixelY + ", url=" + this.url + ", x=" + this.x + ", y=" + this.y + ", zoomLevel=" + this.zoomLevel + "]";
    }
}
