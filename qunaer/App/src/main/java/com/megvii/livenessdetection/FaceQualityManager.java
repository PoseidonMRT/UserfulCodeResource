package com.megvii.livenessdetection;

import android.graphics.RectF;
import com.megvii.livenessdetection.bean.FaceInfo;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

public class FaceQualityManager {
    private float a = 0.5f;
    private float b = 0.5f;
    private int c = 0;
    public float faceMaxSizeRatioThreshold = 0.4f;
    public float faceWidthThreshold = 150.0f;
    public float gaussianBlurThreshold = 0.15f;
    public float integrityThreshold = 0.99f;
    public float maxBrightnessThreshold = 230.0f;
    public float minBrightnessThreshold = 70.0f;
    public float motionBlurThreshold = 0.2f;
    public int needHolding = 3;
    public float pitchThreshold = 0.17f;
    public float yawThreshold = 0.17f;

    public enum FaceQualityErrorType {
        NONE,
        FRAME_NEED_HOLDING,
        FACE_NOT_FOUND,
        FACE_POS_DEVIATED,
        FACE_NONINTEGRITY,
        FACE_TOO_DARK,
        FACE_TOO_BRIGHT,
        FACE_TOO_SMALL,
        FACE_TOO_LARGE,
        FACE_TOO_BLURRY,
        FACE_OUT_OF_RECT,
        FACE_EYE_OCCLUSIVE,
        FACE_MOUTH_OCCLUSIVE
    }

    public FaceQualityManager(float f, float f2) {
        this.a = f;
        this.b = f2;
    }

    public synchronized List<FaceQualityErrorType> feedFrame(DetectionFrame detectionFrame) {
        LinkedList linkedList;
        linkedList = new LinkedList();
        if (detectionFrame == null) {
            throw new InvalidParameterException("detectionFrame could not be null");
        }
        int imageWidth;
        int imageHeight;
        if (detectionFrame.getRotation() % 180 == 0) {
            imageWidth = detectionFrame.getImageWidth();
        } else {
            imageWidth = detectionFrame.getImageHeight();
        }
        if (detectionFrame.getRotation() % 180 == 0) {
            imageHeight = detectionFrame.getImageHeight();
        } else {
            imageHeight = detectionFrame.getImageWidth();
        }
        FaceInfo faceInfo = detectionFrame.getFaceInfo();
        if (faceInfo == null) {
            linkedList.add(FaceQualityErrorType.FACE_NOT_FOUND);
        } else {
            RectF facePos = detectionFrame.getFacePos();
            float centerX = facePos.centerX();
            float centerY = facePos.centerY();
            if (((float) Math.sqrt(Math.pow((double) (((float) imageHeight) * (centerY - this.b)), 2.0d) + Math.pow((double) (((float) imageWidth) * (centerX - this.a)), 2.0d))) / ((float) faceInfo.faceSize.width()) > 0.5f) {
                linkedList.add(FaceQualityErrorType.FACE_OUT_OF_RECT);
            }
            if (Math.abs(faceInfo.yaw) > this.yawThreshold || Math.abs(faceInfo.pitch) > this.pitchThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_POS_DEVIATED);
            }
            if (faceInfo.integrity < this.integrityThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_NONINTEGRITY);
            }
            if (faceInfo.brightness < this.minBrightnessThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_TOO_DARK);
            }
            if (faceInfo.brightness > this.maxBrightnessThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_TOO_BRIGHT);
            }
            if (((float) faceInfo.faceSize.width()) < this.faceWidthThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_TOO_SMALL);
            }
            if (facePos.width() > this.faceMaxSizeRatioThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_TOO_LARGE);
            }
            if (faceInfo.motionBlur > this.motionBlurThreshold || faceInfo.gaussianBlur > this.gaussianBlurThreshold) {
                linkedList.add(FaceQualityErrorType.FACE_TOO_BLURRY);
            }
            if (faceInfo.eyeLeftOcclusion > 0.5f || faceInfo.eyeRightOcclusion > 0.5f) {
                linkedList.add(FaceQualityErrorType.FACE_EYE_OCCLUSIVE);
            }
            if (faceInfo.mouthOcclusion > 0.5f) {
                linkedList.add(FaceQualityErrorType.FACE_MOUTH_OCCLUSIVE);
            }
        }
        if (linkedList.size() == 0) {
            imageHeight = this.c;
            this.c = imageHeight + 1;
            if (imageHeight < this.needHolding) {
                linkedList.add(FaceQualityErrorType.FRAME_NEED_HOLDING);
            }
        } else {
            this.c = 0;
        }
        return new LinkedList(linkedList);
    }
}
