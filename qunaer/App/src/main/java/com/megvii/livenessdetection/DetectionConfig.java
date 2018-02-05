package com.megvii.livenessdetection;

import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.SpeechConstant;
import org.json.JSONException;
import org.json.JSONObject;

public final class DetectionConfig {
    public final float eyeOpenThreshold;
    public final float gaussianBlur;
    public final float integrity;
    public final int maxBrightness;
    public final int minBrightness;
    public final float minFaceSize;
    public final float motionBlur;
    public final float mouthOpenThreshold;
    public final float pitchAngle;
    public final long timeout;
    public final float yawAngle;

    public final class Builder {
        private float a = 0.17f;
        private float b = 0.17f;
        private int c = 80;
        private int d = 170;
        private float e = 0.1f;
        private float f = 0.08f;
        private float g = 150.0f;
        private int h = LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL;
        private float i = 0.3f;
        private float j = 0.4f;
        private float k = 0.9f;

        public final Builder setMinFaceSize(int i) {
            this.g = (float) i;
            return this;
        }

        public final Builder setDetectionTimeout(int i) {
            this.h = i;
            return this;
        }

        public final Builder setMaxAngle(float f, float f2, float f3) {
            this.b = f;
            this.a = f2;
            return this;
        }

        public final Builder setBrightness(int i, int i2) {
            this.c = i;
            this.d = i2;
            return this;
        }

        public final Builder setMouthHwratio(float f) {
            this.j = f;
            return this;
        }

        public final Builder setEyeHwratio(float f) {
            this.i = f;
            return this;
        }

        public final Builder setBlur(float f, float f2) {
            this.f = f;
            this.e = f2;
            return this;
        }

        public final Builder setIntegrity(float f) {
            this.k = f;
            return this;
        }

        public final DetectionConfig build() {
            return new DetectionConfig();
        }
    }

    @Deprecated
    public final int getMinBrightness() {
        return this.minBrightness;
    }

    @Deprecated
    public final int getMaxBrightness() {
        return this.maxBrightness;
    }

    @Deprecated
    public final float getMotionBlur() {
        return this.motionBlur;
    }

    @Deprecated
    public final float getGaussianBlur() {
        return this.gaussianBlur;
    }

    @Deprecated
    public final long getTimeout() {
        return this.timeout;
    }

    @Deprecated
    public final float getYawAngle() {
        return this.yawAngle;
    }

    @Deprecated
    public final float getPitchAngle() {
        return this.pitchAngle;
    }

    @Deprecated
    public final float getMinFaceSize() {
        return this.minFaceSize;
    }

    @Deprecated
    public final float getEyeOpenThreshold() {
        return this.eyeOpenThreshold;
    }

    @Deprecated
    public final float getMouthOpenThreshold() {
        return this.mouthOpenThreshold;
    }

    private DetectionConfig(Builder builder) {
        this.gaussianBlur = builder.f;
        this.motionBlur = builder.e;
        this.pitchAngle = builder.b;
        this.yawAngle = builder.a;
        this.minBrightness = builder.c;
        this.maxBrightness = builder.d;
        this.minFaceSize = builder.g;
        this.timeout = (long) builder.h;
        this.eyeOpenThreshold = builder.i;
        this.mouthOpenThreshold = builder.j;
        this.integrity = builder.k;
    }

    public final String toJsonString() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("gaussianBlur", (double) this.gaussianBlur);
            jSONObject.put("motionBlur", (double) this.motionBlur);
            jSONObject.put("pitchAngle", (double) this.pitchAngle);
            jSONObject.put("yawAngle", (double) this.yawAngle);
            jSONObject.put("minBrightness", this.minBrightness);
            jSONObject.put("maxBrightness", this.maxBrightness);
            jSONObject.put("minFaceSize", (double) this.minFaceSize);
            jSONObject.put(SpeechConstant.NET_TIMEOUT, this.timeout);
            jSONObject.put("eyeOpenThreshold", (double) this.eyeOpenThreshold);
            jSONObject.put("mouthOpenThreshold", (double) this.mouthOpenThreshold);
            jSONObject.put("integrity", (double) this.integrity);
            return jSONObject.toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
