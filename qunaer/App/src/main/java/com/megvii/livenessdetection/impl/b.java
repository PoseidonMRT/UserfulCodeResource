package com.megvii.livenessdetection.impl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import com.megvii.livenessdeteciton.obf.d;
import com.megvii.livenessdetection.DetectionConfig;
import com.megvii.livenessdetection.DetectionFrame;
import com.megvii.livenessdetection.Detector.DetectionType;
import com.megvii.livenessdetection.bean.FaceInfo.FaceInfoFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class b extends DetectionFrame {
    private int a;
    private int b;
    private DetectionType c;
    private byte[] d;
    private int e;

    public b(byte[] bArr, int i, int i2, int i3, DetectionType detectionType) {
        this.a = i;
        this.b = i2;
        this.e = i3;
        System.currentTimeMillis();
        this.c = detectionType;
        this.d = bArr;
    }

    public final int getRotation() {
        return this.e;
    }

    public final DetectionType a() {
        return this.c;
    }

    public final int getImageWidth() {
        return this.a;
    }

    public final int getImageHeight() {
        return this.b;
    }

    public final byte[] getYUVData() {
        return this.d;
    }

    private synchronized byte[] a(int i, Rect rect) {
        return a(i, rect, 80);
    }

    private synchronized byte[] a(int i, Rect rect, int i2) {
        byte[] toByteArray;
        if (hasFace()) {
            float max;
            YuvImage yuvImage = new YuvImage(this.d, 17, this.a, this.b, null);
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (360 - this.e), 0.5f, 0.5f);
            RectF rectF = new RectF();
            matrix.mapRect(rectF, this.mFaceInfo.position);
            Rect rect2 = new Rect();
            rect2.left = (int) (rectF.left * ((float) this.a));
            rect2.right = (int) (rectF.right * ((float) this.a));
            rect2.top = (int) (rectF.top * ((float) this.b));
            rect2.bottom = (int) (rectF.bottom * ((float) this.b));
            matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f, (float) rect2.centerX(), (float) rect2.centerY());
            RectF rectF2 = new RectF();
            matrix.mapRect(rectF2, new RectF(rect2));
            rectF2.left = Math.max(0.0f, rectF2.left);
            rectF2.top = Math.max(0.0f, rectF2.top);
            rectF2.right = Math.min(rectF2.right, (float) this.a);
            rectF2.bottom = Math.min(rectF2.bottom, (float) this.b);
            yuvImage.compressToJpeg(new Rect((int) rectF2.left, (int) rectF2.top, (int) rectF2.right, (int) rectF2.bottom), i2, byteArrayOutputStream);
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                d.a(e.toString());
            }
            toByteArray = byteArrayOutputStream.toByteArray();
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
            Matrix matrix2 = new Matrix();
            matrix2.postRotate((float) this.e);
            if (i != -1) {
                max = ((float) Math.max(decodeByteArray.getWidth(), decodeByteArray.getHeight())) / ((float) i);
                if (max > 1.0f) {
                    matrix2.postScale(1.0f / max, 1.0f / max);
                }
            }
            decodeByteArray = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix2, true);
            max = ((float) rect2.centerX()) - rectF2.left;
            float centerY = ((float) rect2.centerY()) - rectF2.top;
            Matrix matrix3 = new Matrix();
            matrix3.setRotate((float) this.e, 0.5f, 0.5f);
            r4 = new float[2];
            matrix3.mapPoints(r4, new float[]{max / rectF2.width(), centerY / rectF2.height()});
            max = r4[0] * ((float) decodeByteArray.getWidth());
            centerY = r4[1] * ((float) decodeByteArray.getHeight());
            float max2 = ((float) Math.max(decodeByteArray.getWidth(), decodeByteArray.getHeight())) / 1.5f;
            if (rect == null) {
                rect = new Rect();
            }
            rect.left = (int) (max - (max2 / 2.0f));
            rect.top = (int) (centerY - (max2 / 2.0f));
            rect.right = (int) (max + (max2 / 2.0f));
            rect.bottom = (int) ((max2 / 2.0f) + centerY);
            String a = com.megvii.livenessdeteciton.obf.b.a(rect);
            OutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            decodeByteArray.compress(CompressFormat.JPEG, i2, byteArrayOutputStream2);
            try {
                byteArrayOutputStream2.close();
                toByteArray = EncodeImpl.a(byteArrayOutputStream2.toByteArray(), false, false, 1824058797, a);
            } catch (IOException e2) {
                toByteArray = null;
            }
        } else {
            toByteArray = null;
        }
        return toByteArray;
    }

    private synchronized byte[] b(int i, Rect rect, int i2) {
        byte[] toByteArray;
        YuvImage yuvImage = new YuvImage(this.d, 17, this.a, this.b, null);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, this.a, this.b), i2, byteArrayOutputStream);
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            d.a(e.toString());
        }
        toByteArray = byteArrayOutputStream.toByteArray();
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
        Matrix matrix = new Matrix();
        matrix.postRotate((float) this.e);
        if (i != -1) {
            float max = ((float) Math.max(decodeByteArray.getWidth(), decodeByteArray.getHeight())) / ((float) i);
            if (max > 1.0f) {
                matrix.postScale(1.0f / max, 1.0f / max);
            }
        }
        decodeByteArray = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
        if (rect == null) {
            rect = new Rect();
        }
        if (hasFace()) {
            rect.left = (int) (this.mFaceInfo.position.left * ((float) decodeByteArray.getWidth()));
            rect.top = (int) (this.mFaceInfo.position.top * ((float) decodeByteArray.getHeight()));
            rect.right = (int) (this.mFaceInfo.position.right * ((float) decodeByteArray.getWidth()));
            rect.bottom = (int) (this.mFaceInfo.position.bottom * ((float) decodeByteArray.getHeight()));
        }
        byteArrayOutputStream = new ByteArrayOutputStream();
        decodeByteArray.compress(CompressFormat.JPEG, i2, byteArrayOutputStream);
        try {
            byteArrayOutputStream.close();
            toByteArray = byteArrayOutputStream.toByteArray();
            if (hasFace()) {
                toByteArray = EncodeImpl.a(toByteArray, false, false, 1824058797, com.megvii.livenessdeteciton.obf.b.a(rect));
            }
        } catch (IOException e2) {
            toByteArray = null;
        }
        return toByteArray;
    }

    public final synchronized byte[] getCroppedFaceImageData() {
        return a(-1, null);
    }

    public final synchronized byte[] getCroppedFaceImageData(int i) {
        return a(i, null);
    }

    public final byte[] getCroppedFaceImageData(Rect rect) {
        return a(-1, rect);
    }

    public final byte[] getCroppedFaceImageData(int i, Rect rect) {
        return a(i, rect);
    }

    public final byte[] getEncodedFaceImageData(int i, int i2, Rect rect) {
        return EncodeImpl.a(getCroppedFaceImageData(i2, rect), true, true, i);
    }

    public final byte[] getImageData(Rect rect, boolean z, int i, int i2, boolean z2, boolean z3, int i3) {
        if ((z2 || z3) && i3 < 0) {
            return null;
        }
        if (i < 0 || i > 100) {
            return null;
        }
        if (z) {
            if (i2 <= 0) {
                i2 = -1;
            }
            return EncodeImpl.a(a(i2, rect, i), z2, z3, i3);
        }
        if (i2 <= 0) {
            i2 = -1;
        }
        return EncodeImpl.a(b(i2, rect, i), z2, z3, i3);
    }

    public final boolean a(DetectionFrame detectionFrame) {
        return detectionFrame == null || !detectionFrame.hasFace() || (this.mFaceInfo == null ? 0.0f : this.mFaceInfo.smoothQuality) > detectionFrame.getFaceInfo().smoothQuality;
    }

    public final void a(String str, DetectionConfig detectionConfig, com.megvii.livenessdeteciton.obf.b bVar) {
        this.mFaceInfo = FaceInfoFactory.createFaceInfo(str);
        bVar.a(this.mFaceInfo);
    }
}
