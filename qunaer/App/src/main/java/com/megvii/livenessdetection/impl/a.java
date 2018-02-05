package com.megvii.livenessdetection.impl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.RectF;
import com.megvii.livenessdeteciton.obf.b;
import com.megvii.livenessdetection.DetectionConfig;
import com.megvii.livenessdetection.DetectionFrame;
import com.megvii.livenessdetection.bean.FaceInfo.FaceInfoFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class a extends DetectionFrame {
    private Bitmap a;
    private byte[] b;

    public a(Bitmap bitmap) {
        this.a = bitmap;
    }

    private boolean b() {
        return (this.a == null || this.a.isRecycled() || !Config.ARGB_8888.equals(this.a.getConfig())) ? false : true;
    }

    public final int getImageWidth() {
        return b() ? this.a.getWidth() : -1;
    }

    public final int getImageHeight() {
        return b() ? this.a.getHeight() : -1;
    }

    public final synchronized byte[] getYUVData() {
        return null;
    }

    private synchronized void c() {
        if (this.mFaceInfo != null) {
            RectF rectF = this.mFaceInfo.position;
            int width = this.a.getWidth();
            int height = this.a.getHeight();
            Bitmap createBitmap = Bitmap.createBitmap(this.a, (int) (rectF.left * ((float) width)), (int) (rectF.top * ((float) height)), (int) (((float) width) * rectF.width()), (int) (rectF.height() * ((float) height)));
            if (createBitmap != null) {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                createBitmap.compress(CompressFormat.JPEG, 80, byteArrayOutputStream);
                createBitmap.recycle();
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                }
                this.b = byteArrayOutputStream.toByteArray();
            }
        }
    }

    public final byte[] getCroppedFaceImageData() {
        if (this.b == null && b() && hasFace()) {
            c();
        }
        return this.b;
    }

    public final byte[] getCroppedFaceImageData(int i) {
        return null;
    }

    public final byte[] getCroppedFaceImageData(Rect rect) {
        return null;
    }

    public final byte[] getCroppedFaceImageData(int i, Rect rect) {
        return null;
    }

    public final byte[] getEncodedFaceImageData(int i, int i2, Rect rect) {
        return null;
    }

    public final byte[] getImageData(Rect rect, boolean z, int i, int i2, boolean z2, boolean z3, int i3) {
        return null;
    }

    public final byte[] a() {
        if (!b()) {
            return null;
        }
        Bitmap bitmap = this.a;
        byte[] bArr = new byte[(bitmap.getWidth() * bitmap.getHeight())];
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int i2 = 0; i2 < bitmap.getWidth(); i2++) {
                int pixel = bitmap.getPixel(i2, i);
                int i3 = (pixel >> 16) & 255;
                int i4 = (pixel >> 8) & 255;
                bArr[(bitmap.getWidth() * i) + i2] = (byte) ((((pixel & 255) * 114) + ((i3 * 299) + (i4 * 587))) / 1000);
            }
        }
        return bArr;
    }

    public final void a(String str, DetectionConfig detectionConfig, b bVar) {
        this.mFaceInfo = FaceInfoFactory.createFaceInfo(str);
        if (this.mFaceInfo != null) {
            bVar.a(this.mFaceInfo);
        }
    }

    public final int getRotation() {
        return 0;
    }
}
