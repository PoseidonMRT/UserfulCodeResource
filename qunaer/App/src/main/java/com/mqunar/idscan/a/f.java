package com.mqunar.idscan.a;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.view.SurfaceHolder;
import com.mqunar.idscan.decode.a;
import com.mqunar.tools.log.QLog;
import java.io.IOException;

public final class f {
    public static byte[] a;
    public static byte[] b;
    public static int c = 0;
    private static final String d = f.class.getSimpleName();
    private final Context e;
    private final d f;
    private Camera g;
    private a h;
    private Rect i;
    private Rect j;
    private boolean k;
    private boolean l;
    private final g m = new g(this.f);

    public f(Context context) {
        this.e = context;
        this.f = new d(context);
    }

    public final synchronized void a(Handler handler) {
        Camera camera = this.g;
        if (camera != null && this.l) {
            Point a;
            this.m.a(handler, 20);
            if (a == null) {
                a = this.f.a();
                a = new byte[(((a.y * a.x) * ImageFormat.getBitsPerPixel(camera.getParameters().getPreviewFormat())) / 8)];
            }
            if (b == null) {
                a = this.f.a();
                b = new byte[(((a.y * a.x) * ImageFormat.getBitsPerPixel(camera.getParameters().getPreviewFormat())) / 8)];
            }
            if (a.a) {
                if (c == 0) {
                    camera.addCallbackBuffer(a);
                    c = 1;
                } else if (c == 1) {
                    camera.addCallbackBuffer(b);
                    c = 0;
                }
                a.a = false;
            } else if (c == 0) {
                camera.addCallbackBuffer(b);
            } else if (c == 1) {
                camera.addCallbackBuffer(a);
            }
            camera.setPreviewCallbackWithBuffer(this.m);
        }
    }

    public final synchronized void a(SurfaceHolder surfaceHolder) {
        Camera camera = this.g;
        if (camera == null) {
            int numberOfCameras = Camera.getNumberOfCameras();
            if (numberOfCameras == 0) {
                QLog.w(d, "No cameras!", new Object[0]);
                camera = null;
            } else {
                int i = 0;
                while (i < numberOfCameras) {
                    CameraInfo cameraInfo = new CameraInfo();
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == 0) {
                        break;
                    }
                    i++;
                }
                if (i < numberOfCameras) {
                    camera = Camera.open(i);
                } else {
                    QLog.i("camera_operate", "No camera facing back; returning camera #0", new Object[0]);
                    camera = Camera.open(0);
                }
            }
            if (camera == null) {
                throw new IOException();
            }
            this.g = camera;
        }
        Camera camera2 = camera;
        camera2.setPreviewDisplay(surfaceHolder);
        if (!this.k) {
            this.k = true;
            this.f.a(camera2);
        }
        Parameters parameters = camera2.getParameters();
        String flatten = parameters == null ? null : parameters.flatten();
        try {
            this.f.a(camera2, false);
        } catch (RuntimeException e) {
            QLog.w(d, "Camera rejected parameters. Setting only minimal safe-mode parameters", new Object[0]);
            QLog.i(d, "Resetting to saved camera params: " + flatten, new Object[0]);
            if (flatten != null) {
                Parameters parameters2 = camera2.getParameters();
                parameters2.unflatten(flatten);
                try {
                    camera2.setParameters(parameters2);
                    this.f.a(camera2, true);
                } catch (RuntimeException e2) {
                    QLog.w(d, "Camera rejected even safe-mode parameters! No configuration", new Object[0]);
                }
            }
        }
    }

    public final synchronized boolean a() {
        return this.g != null;
    }

    public final synchronized void b() {
        if (this.g != null) {
            QLog.i("camera_operate", "release camera #", new Object[0]);
            this.g.release();
            this.g = null;
            this.i = null;
        }
    }

    public final synchronized void c() {
        Camera camera = this.g;
        if (!(camera == null || this.l)) {
            camera.startPreview();
            if (!d.a) {
                this.h = new a(this.g);
            }
            this.l = true;
        }
    }

    public final synchronized void d() {
        if (this.h != null) {
            this.h.b();
            this.h = null;
        }
        if (this.g != null && this.l) {
            this.g.stopPreview();
            this.m.a(null, 0);
            this.l = false;
        }
    }

    public final synchronized Rect e() {
        Rect rect;
        int i = 0;
        synchronized (this) {
            if (this.j == null) {
                Point b = this.f.b();
                if (b == null) {
                    rect = null;
                } else {
                    int i2 = (b.y * 9) / 10;
                    int i3 = (b.x * 4) / 5;
                    double d = (double) (((float) i3) / ((float) i2));
                    if (d > 1.777d) {
                        i = (int) (((double) i2) * 1.777d);
                    } else if (d < 1.777d) {
                        i2 = (int) (((double) i3) / 1.777d);
                        i = i3;
                    } else {
                        i2 = 0;
                    }
                    String[] split = (i + "&" + i2).split("&");
                    i = Integer.parseInt(split[0]);
                    i2 = Integer.parseInt(split[1]);
                    i3 = (b.x - i) / 2;
                    int i4 = (b.y - i2) / 2;
                    this.j = new Rect(i3, i4, i + i3, i2 + i4);
                    QLog.d(d, "Calculated framing rect: " + this.i, new Object[0]);
                }
            }
            rect = this.j;
        }
        return rect;
    }

    public final synchronized Rect f() {
        if (this.i == null && this.j != null) {
            this.i = new Rect(this.j.left, this.j.bottom - ((int) (((double) this.j.width()) / 6.33d)), this.j.right, this.j.bottom);
            QLog.d(d, "Calculated framing rect: " + this.i, new Object[0]);
        }
        return this.i;
    }

    public final synchronized Rect g() {
        Rect rect = null;
        synchronized (this) {
            Rect f = f();
            if (f != null) {
                Rect rect2 = new Rect(f);
                Point a = this.f.a();
                Point b = this.f.b();
                if (!(a == null || b == null)) {
                    rect2.left = (rect2.left * a.x) / b.x;
                    rect2.right = (rect2.right * a.x) / b.x;
                    rect2.top = (rect2.top * a.y) / b.y;
                    rect2.bottom = (rect2.bottom * a.y) / b.y;
                    QLog.d(d, "Calculated framingRectInPreview rect: " + rect2, new Object[0]);
                    QLog.d(d, "cameraResolution: " + a, new Object[0]);
                    QLog.d(d, "screenResolution: " + b, new Object[0]);
                    rect = rect2;
                }
            }
        }
        return rect;
    }

    public final d h() {
        return this.f;
    }
}
