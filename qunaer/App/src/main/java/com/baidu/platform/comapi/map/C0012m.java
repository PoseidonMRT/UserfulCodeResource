package com.baidu.platform.comapi.map;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.opengl.GLUtils;
import com.baidu.platform.comapi.map.m.a;
import java.lang.Thread.State;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

@SuppressLint({"NewApi"})
public class C0012m extends Thread {
    private AtomicBoolean a;
    private SurfaceTexture b;
    private a c;
    private EGL10 d;
    private EGLDisplay e = EGL10.EGL_NO_DISPLAY;
    private EGLContext f = EGL10.EGL_NO_CONTEXT;
    private EGLSurface g = EGL10.EGL_NO_SURFACE;
    private GL10 h;
    private int i = 1;
    private boolean j = false;
    private final F k;

    public interface a {
        int a();
    }

    public C0012m(SurfaceTexture surfaceTexture, a aVar, AtomicBoolean atomicBoolean, F f) {
        this.b = surfaceTexture;
        this.c = aVar;
        this.a = atomicBoolean;
        this.k = f;
    }

    private boolean a(int i, int i2, int i3, int i4, int i5, int i6) {
        this.d = (EGL10) EGLContext.getEGL();
        this.e = this.d.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.e == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
        }
        if (this.d.eglInitialize(this.e, new int[2])) {
            EGLConfig[] eGLConfigArr = new EGLConfig[100];
            int[] iArr = new int[1];
            if (!this.d.eglChooseConfig(this.e, new int[]{12352, 4, 12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344}, eGLConfigArr, 100, iArr) || iArr[0] <= 0) {
                return false;
            }
            this.f = this.d.eglCreateContext(this.e, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
            this.g = this.d.eglCreateWindowSurface(this.e, eGLConfigArr[0], this.b, null);
            if (this.g == EGL10.EGL_NO_SURFACE || this.f == EGL10.EGL_NO_CONTEXT) {
                if (this.d.eglGetError() == 12299) {
                    throw new RuntimeException("eglCreateWindowSurface returned EGL_BAD_NATIVE_WINDOW. ");
                }
                GLUtils.getEGLErrorString(this.d.eglGetError());
            }
            if (this.d.eglMakeCurrent(this.e, this.g, this.g, this.f)) {
                this.h = (GL10) this.f.getGL();
                return true;
            }
            throw new RuntimeException("eglMakeCurrent failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
        }
        throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
    }

    private static boolean b(int i, int i2, int i3, int i4, int i5, int i6) {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglGetDisplay, new int[2]);
        int[] iArr = new int[1];
        return egl10.eglChooseConfig(eglGetDisplay, new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344}, new EGLConfig[100], 100, iArr) && iArr[0] > 0;
    }

    private void d() {
        try {
            if (C0012m.b(5, 6, 5, 0, 24, 0)) {
                a(5, 6, 5, 0, 24, 0);
            } else {
                a(8, 8, 8, 0, 16, 0);
            }
        } catch (IllegalArgumentException e) {
            a(8, 8, 8, 0, 16, 0);
        }
        MapRenderer.nativeInit(this.k.b().h);
        MapRenderer.nativeResize(this.k.b().h, F.a, F.b);
    }

    private void e() {
        this.d.eglDestroyContext(this.e, this.f);
        this.d.eglDestroySurface(this.e, this.g);
        this.d.eglTerminate(this.e);
        this.f = EGL10.EGL_NO_CONTEXT;
        this.g = EGL10.EGL_NO_SURFACE;
    }

    public void a() {
        this.i = 1;
        synchronized (this) {
            if (getState() == State.WAITING) {
                notify();
            }
        }
    }

    public void b() {
        this.i = 0;
    }

    public void c() {
        this.j = true;
        synchronized (this) {
            if (getState() == State.WAITING) {
                notify();
            }
        }
    }

    public void run() {
        d();
        while (this.c != null) {
            if (this.i != 1) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (this.k.b() == null) {
                break;
            } else {
                synchronized (this.k.b()) {
                    this.i = this.c.a();
                    for (l lVar : this.k.b().f) {
                        E H = this.k.b().H();
                        if (this.h == null) {
                            return;
                        }
                        lVar.a(this.h, H);
                    }
                    this.d.eglSwapBuffers(this.e, this.g);
                }
            }
            if (this.j) {
                break;
            }
        }
        e();
    }
}
