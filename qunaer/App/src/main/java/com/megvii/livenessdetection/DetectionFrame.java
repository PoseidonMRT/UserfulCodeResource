package com.megvii.livenessdetection;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import com.megvii.livenessdetection.bean.FaceInfo;

public abstract class DetectionFrame {
    private FrameType a = FrameType.NONE;
    protected FaceInfo mFaceInfo;

    public enum FrameType {
        NONE,
        WAITINGNORMAL
    }

    public abstract byte[] getCroppedFaceImageData();

    public abstract byte[] getCroppedFaceImageData(int i);

    public abstract byte[] getCroppedFaceImageData(int i, Rect rect);

    public abstract byte[] getCroppedFaceImageData(Rect rect);

    public abstract byte[] getEncodedFaceImageData(int i, int i2, Rect rect);

    public abstract byte[] getImageData(Rect rect, boolean z, int i, int i2, boolean z2, boolean z3, int i3);

    public abstract int getImageHeight();

    public abstract int getImageWidth();

    public abstract int getRotation();

    public abstract byte[] getYUVData();

    public FrameType getFrameType() {
        return this.a;
    }

    public void setFrameType(FrameType frameType) {
        this.a = frameType;
    }

    public FaceInfo getFaceInfo() {
        return this.mFaceInfo;
    }

    @Deprecated
    public float getWearGlass() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.wearGlass;
    }

    public byte[] getEncodedFaceImageData(int i, Rect rect) {
        return getEncodedFaceImageData(i, -1, rect);
    }

    public byte[] getEncodedFaceImageData(int i, int i2) {
        return getEncodedFaceImageData(i, i2, null);
    }

    public byte[] getEncodedFaceImageData(int i) {
        return getEncodedFaceImageData(i, -1, null);
    }

    @Deprecated
    public synchronized Rect getFaceSize() {
        return this.mFaceInfo == null ? null : this.mFaceInfo.faceSize;
    }

    @Deprecated
    public RectF getFacePos() {
        return this.mFaceInfo == null ? null : this.mFaceInfo.position;
    }

    @Deprecated
    public float getYawAngle() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.yaw;
    }

    @Deprecated
    public float getPitchAngle() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.pitch;
    }

    @Deprecated
    public float getGaussianBlur() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.gaussianBlur;
    }

    @Deprecated
    public float getMotionBlur() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.motionBlur;
    }

    @Deprecated
    public float getBrightness() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.brightness;
    }

    @Deprecated
    public float getFaceQuality() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.faceQuality;
    }

    public boolean hasFace() {
        return this.mFaceInfo != null;
    }

    @Deprecated
    public float getLeftEyeHwratio() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.leftEyeHWRatio;
    }

    @Deprecated
    public float getRightEyeHwratio() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.rightEyeHWRatio;
    }

    @Deprecated
    public float getMouthHwratio() {
        return this.mFaceInfo == null ? -1.0f : this.mFaceInfo.mouthHWRatio;
    }

    public static PointF get2DPoint(float f, float f2, float f3, float f4, float f5, float f6) {
        PointF pointF = new PointF();
        pointF.x = ((f6 < 0.0f ? f6 / f3 : (-f6) / f4) * 0.5f) + 0.5f;
        pointF.y = ((f5 < 0.0f ? (-f5) / f : f5 / f2) * 0.5f) + 0.5f;
        return pointF;
    }

    public PointF get2DPoint(float f, float f2, float f3, float f4) {
        if (!hasFace()) {
            return null;
        }
        return get2DPoint(f, f2, f3, f4, this.mFaceInfo.smoothPitch, this.mFaceInfo.smoothYaw);
    }

    public PointF get2DPoint() {
        return get2DPoint(-0.17f, 0.17f, -0.22f, 0.22f);
    }

    public static boolean isValid2DPoint(PointF pointF) {
        return pointF.x >= 0.0f && pointF.x <= 1.0f && pointF.y >= 0.0f && pointF.y <= 1.0f;
    }
}
