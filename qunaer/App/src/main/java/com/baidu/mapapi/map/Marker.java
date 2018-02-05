package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.ParcelItem;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0008h;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

public final class Marker extends Overlay {
    LatLng a;
    BitmapDescriptor b;
    float c;
    float d;
    boolean e;
    boolean f;
    float g;
    String h;
    int i;
    boolean j;
    boolean k;
    float l;
    int m;
    ArrayList<BitmapDescriptor> n;
    int o;

    Marker() {
        this.j = false;
        this.k = false;
        this.o = 20;
        this.type = C0008h.marker;
    }

    private void a(ArrayList<BitmapDescriptor> arrayList, Bundle bundle) {
        int i = 0;
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            BitmapDescriptor bitmapDescriptor = (BitmapDescriptor) it.next();
            ParcelItem parcelItem = new ParcelItem();
            Bundle bundle2 = new Bundle();
            Bitmap bitmap = bitmapDescriptor.a;
            Buffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            bundle2.putByteArray("image_data", array);
            bundle2.putInt("image_width", bitmap.getWidth());
            bundle2.putInt("image_height", bitmap.getHeight());
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            messageDigest.update(array, 0, array.length);
            byte[] digest = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder("");
            for (byte b : digest) {
                stringBuilder.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            bundle2.putString("image_hashcode", stringBuilder.toString());
            parcelItem.setBundle(bundle2);
            arrayList2.add(parcelItem);
        }
        if (arrayList2.size() > 0) {
            Parcelable[] parcelableArr = new ParcelItem[arrayList2.size()];
            while (i < arrayList2.size()) {
                parcelableArr[i] = (ParcelItem) arrayList2.get(i);
                i++;
            }
            bundle.putParcelableArray("icons", parcelableArr);
        }
    }

    Bundle a(Bundle bundle) {
        int i = 1;
        super.a(bundle);
        Bundle bundle2 = new Bundle();
        if (this.b != null) {
            bundle.putBundle("image_info", this.b.b());
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(this.a);
        bundle.putInt("animatetype", this.m);
        bundle.putDouble("location_x", ll2mc.getLongitudeE6());
        bundle.putDouble("location_y", ll2mc.getLatitudeE6());
        bundle.putInt("perspective", this.e ? 1 : 0);
        bundle.putFloat("anchor_x", this.c);
        bundle.putFloat("anchor_y", this.d);
        bundle.putFloat("rotate", this.g);
        bundle.putInt("y_offset", this.i);
        bundle.putInt("isflat", this.j ? 1 : 0);
        String str = "istop";
        if (!this.k) {
            i = 0;
        }
        bundle.putInt(str, i);
        bundle.putInt("period", this.o);
        bundle.putFloat("alpha", this.l);
        if (this.n != null && this.n.size() > 0) {
            a(this.n, bundle);
        }
        bundle2.putBundle("param", bundle);
        return bundle;
    }

    public float getAlpha() {
        return this.l;
    }

    public float getAnchorX() {
        return this.c;
    }

    public float getAnchorY() {
        return this.d;
    }

    public BitmapDescriptor getIcon() {
        return this.b;
    }

    public ArrayList<BitmapDescriptor> getIcons() {
        return this.n;
    }

    public int getPeriod() {
        return this.o;
    }

    public LatLng getPosition() {
        return this.a;
    }

    public float getRotate() {
        return this.g;
    }

    public String getTitle() {
        return this.h;
    }

    public boolean isDraggable() {
        return this.f;
    }

    public boolean isFlat() {
        return this.j;
    }

    public boolean isPerspective() {
        return this.e;
    }

    public void setAlpha(float f) {
        if (f < 0.0f || ((double) f) > WeightedLatLng.DEFAULT_INTENSITY) {
            this.l = 1.0f;
            return;
        }
        this.l = f;
        this.listener.b(this);
    }

    public void setAnchor(float f, float f2) {
        if (f >= 0.0f && f <= 1.0f && f2 >= 0.0f && f2 <= 1.0f) {
            this.c = f;
            this.d = f2;
            this.listener.b(this);
        }
    }

    public void setDraggable(boolean z) {
        this.f = z;
        this.listener.b(this);
    }

    public void setFlat(boolean z) {
        this.j = z;
        this.listener.b(this);
    }

    public void setIcon(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor == null) {
            throw new IllegalArgumentException("marker's icon can not be null");
        }
        this.b = bitmapDescriptor;
        this.listener.b(this);
    }

    public void setIcons(ArrayList<BitmapDescriptor> arrayList) {
        if (arrayList == null) {
            throw new IllegalArgumentException("marker's icons can not be null");
        } else if (arrayList.size() != 0) {
            int i = 0;
            while (i < arrayList.size()) {
                if (arrayList.get(i) != null && ((BitmapDescriptor) arrayList.get(i)).a != null) {
                    i++;
                } else {
                    return;
                }
            }
            this.n = arrayList;
            this.listener.b(this);
        }
    }

    public void setPeriod(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("marker's period must be greater than zero ");
        }
        this.o = i;
        this.listener.b(this);
    }

    public void setPerspective(boolean z) {
        this.e = z;
        this.listener.b(this);
    }

    public void setPosition(LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("marker's position can not be null");
        }
        this.a = latLng;
        this.listener.b(this);
    }

    public void setRotate(float f) {
        while (f < 0.0f) {
            f += 360.0f;
        }
        this.g = f % 360.0f;
        this.listener.b(this);
    }

    public void setTitle(String str) {
        this.h = str;
    }

    public void setToTop() {
        this.k = true;
        this.listener.b(this);
    }
}
