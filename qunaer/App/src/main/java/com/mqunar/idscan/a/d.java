package com.mqunar.idscan.a;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.WindowManager;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class d {
    public static boolean a = false;
    public String b = "支持NV21";
    private final Context c;
    private Point d;
    private Point e;

    d(Context context) {
        this.c = context;
    }

    private Point a(Parameters parameters, Point point) {
        Collection supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        if (supportedPreviewSizes == null) {
            QLog.w("CameraConfiguration", "Device returned no supported preview sizes; using default", new Object[0]);
            Size previewSize = parameters.getPreviewSize();
            return new Point(previewSize.width, previewSize.height);
        }
        List arrayList = new ArrayList(supportedPreviewSizes);
        Collections.sort(arrayList, new e(this));
        double d = ((double) point.x) / ((double) point.y);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Size size = (Size) it.next();
            int i = size.width;
            int i2 = size.height;
            if (i * i2 < 153600) {
                it.remove();
            } else {
                int i3 = i < i2 ? 1 : 0;
                int i4 = i3 != 0 ? i2 : i;
                i3 = i3 != 0 ? i : i2;
                if (Math.abs((((double) i4) / ((double) i3)) - d) > 0.15d) {
                    it.remove();
                } else if (i4 == point.x && i3 == point.y) {
                    Point point2 = new Point(i, i2);
                    QLog.i("CameraConfiguration", "Found preview size exactly matching screen size: " + point2, new Object[0]);
                    return point2;
                }
            }
        }
        if (arrayList.isEmpty()) {
            previewSize = parameters.getPreviewSize();
            point2 = new Point(previewSize.width, previewSize.height);
            QLog.i("CameraConfiguration", "No suitable preview sizes, using default: " + point2, new Object[0]);
            return point2;
        }
        size = (Size) arrayList.get(0);
        Point point3 = new Point(size.width, size.height);
        QLog.i("CameraConfiguration", "Using largest suitable preview size: " + point3, new Object[0]);
        return point3;
    }

    private static String a(Collection collection, String... strArr) {
        QLog.i("CameraConfiguration", "Supported values: " + collection, new Object[0]);
        if (collection != null) {
            for (String str : strArr) {
                if (collection.contains(str)) {
                    break;
                }
            }
        }
        String str2 = null;
        QLog.i("CameraConfiguration", "Settable value: " + str2, new Object[0]);
        return str2;
    }

    public final Point a() {
        return this.e;
    }

    final void a(Camera camera) {
        Parameters parameters = camera.getParameters();
        Display defaultDisplay = ((WindowManager) this.c.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.d = point;
        QLog.i("CameraConfiguration", "Screen resolution: " + this.d, new Object[0]);
        Point point2 = new Point();
        point2.x = this.d.x;
        point2.y = this.d.y;
        if (this.d.x < this.d.y) {
            point2.x = this.d.y;
            point2.y = this.d.x;
        }
        this.e = a(parameters, point2);
        QLog.i("CameraConfiguration", "Camera resolution: " + this.e, new Object[0]);
    }

    final void a(Camera camera, boolean z) {
        Parameters parameters = camera.getParameters();
        if (parameters == null) {
            QLog.w("CameraConfiguration", "Device error: no camera parameters are available. Proceeding without configuration.", new Object[0]);
            return;
        }
        QLog.i("CameraConfiguration", "Initial camera parameters: " + parameters.flatten(), new Object[0]);
        if (z) {
            QLog.w("CameraConfiguration", "In camera config safe mode -- most settings will not be honored", new Object[0]);
        }
        String a = a(parameters.getSupportedFocusModes(), "auto");
        if (!z && a == null) {
            a = a(parameters.getSupportedFocusModes(), "macro", "edof");
        }
        if (a != null) {
            parameters.setFocusMode(a);
        }
        a = false;
        List<Integer> supportedPreviewFormats = parameters.getSupportedPreviewFormats();
        if (!supportedPreviewFormats.contains(Integer.valueOf(17))) {
            StringBuilder stringBuilder = new StringBuilder("支持格式");
            for (Integer append : supportedPreviewFormats) {
                stringBuilder.append("__").append(append);
            }
            this.b = stringBuilder.toString();
        }
        parameters.setPreviewFormat(17);
        parameters.setPreviewSize(this.e.x, this.e.y);
        camera.setParameters(parameters);
        Size previewSize = camera.getParameters().getPreviewSize();
        if (previewSize == null) {
            return;
        }
        if (this.e.x != previewSize.width || this.e.y != previewSize.height) {
            QLog.w("CameraConfiguration", "Camera said it supported preview size " + this.e.x + 'x' + this.e.y + ", but after setting it, preview size is " + previewSize.width + 'x' + previewSize.height, new Object[0]);
            this.e.x = previewSize.width;
            this.e.y = previewSize.height;
        }
    }

    public final Point b() {
        return this.d;
    }
}
