package qunar.sdk.mapapi.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.mapquest.android.maps.OverlayItem;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarMapType;
import qunar.sdk.mapapi.utils.MarkerParamerCase;

public class QMarker extends QOverlay {
    public static final String DATA = "myData";
    public static final String MARKER_HEIGHT = "height";
    private static final long serialVersionUID = 1;
    private float anchorX = 0.5f;
    private float anchorY = 1.0f;
    public Bitmap bitmap;
    private Object bitmapDescriptor;
    public String imagePath;
    public MarkerParamerCase paramerCase;
    private boolean perspective = true;
    public QLocation position;
    public int resourceId;
    private float rotate = 0.0f;
    private String title = "";
    public View view;

    public void recycle() {
        if (this.bitmapDescriptor != null) {
            if (this.mapType == QunarMapType.BAIDU) {
                if (this.bitmapDescriptor instanceof BitmapDescriptor) {
                    ((BitmapDescriptor) this.bitmapDescriptor).recycle();
                }
            } else if (this.mapType != QunarMapType.GAODE && this.mapType == QunarMapType.MAPQUEST && (this.bitmapDescriptor instanceof OverlayItem)) {
                ((OverlayItem) this.bitmapDescriptor).recycle();
            }
        }
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }

    public QMarker(QLocation qLocation, String str, MarkerParamerCase markerParamerCase, Context context) {
        this.position = qLocation;
        this.imagePath = str;
        this.paramerCase = markerParamerCase;
        switch (markerParamerCase) {
            case ASSETNAME_TYPE:
                InputStream inputStream = null;
                try {
                    InputStream open = context.getAssets().open(str);
                    if (open != null) {
                        try {
                            open.close();
                            return;
                        } catch (IOException e) {
                            throw new IllegalArgumentException("The path of the unreasonable");
                        }
                    }
                    return;
                } catch (IOException e2) {
                    throw new IllegalArgumentException("The path of the unreasonable");
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                            throw new IllegalArgumentException("The path of the unreasonable");
                        }
                    }
                }
            case FILENAME_TYPE:
                if (!new File(str).exists()) {
                    throw new IllegalArgumentException("The path of the unreasonable");
                }
                return;
            case ABSOLUTEPATH_TYPE:
                if (!new File(str).exists()) {
                    throw new IllegalArgumentException("The path of the unreasonable");
                }
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public QMarker(QLocation qLocation, Bitmap bitmap) {
        this.position = qLocation;
        this.bitmap = bitmap;
        this.paramerCase = MarkerParamerCase.IMAGE_TYPE;
    }

    public QMarker(QLocation qLocation, int i) {
        this.position = qLocation;
        this.resourceId = i;
        this.paramerCase = MarkerParamerCase.RESOURCEID_TYPE;
    }

    public QMarker(QLocation qLocation, View view) {
        this.position = qLocation;
        this.view = view;
        this.paramerCase = MarkerParamerCase.VIEW_TYPE;
    }

    public float getAnchorX() {
        return this.anchorX;
    }

    public void setAnchorX(float f) {
        this.anchorX = f;
    }

    public float getAnchorY() {
        return this.anchorY;
    }

    public void setAnchorY(float f) {
        this.anchorY = f;
    }

    public boolean isPerspective() {
        return this.perspective;
    }

    public void setPerspective(boolean z) {
        this.perspective = z;
    }

    public float getRotate() {
        return this.rotate;
    }

    public void setRotate(float f) {
        this.rotate = f;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public Object getBitmapDescriptor() {
        return this.bitmapDescriptor;
    }

    public void setBitmapDescriptor(Object obj) {
        this.bitmapDescriptor = obj;
    }

    public Serializable getData() {
        if (getExtraInfo() == null) {
            return null;
        }
        return getExtraInfo().getSerializable(DATA);
    }

    public void setData(Serializable serializable) {
        Bundle bundle = getExtraInfo() == null ? new Bundle() : getExtraInfo();
        bundle.putSerializable(DATA, serializable);
        setExtraInfo(bundle);
    }

    public int getMarkerHeight() {
        if (getExtraInfo() == null) {
            return 0;
        }
        return getExtraInfo().getInt(MARKER_HEIGHT);
    }

    public void setMarkerHeight(int i) {
        Bundle bundle = getExtraInfo() == null ? new Bundle() : getExtraInfo();
        bundle.putInt(MARKER_HEIGHT, i);
        setExtraInfo(bundle);
    }

    public String toString() {
        return "QMarker [position=" + this.position + ", imagePath=" + this.imagePath + ", bitmap=" + this.bitmap + ", " + "resourceId=" + this.resourceId + ", view=" + this.view + ", paramerCase=" + this.paramerCase + ", " + "anchorX=" + this.anchorX + ", anchorY=" + this.anchorY + ", perspective=" + this.perspective + ", " + "rotate=" + this.rotate + ", title=" + this.title + "]";
    }
}
