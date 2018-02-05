package com.megvii.livenessdeteciton.obf;

import com.megvii.livenessdetection.Detector.DetectionFailedType;
import com.megvii.livenessdetection.Detector.DetectionType;
import org.apache.commons.io.IOUtils;

public final class a {
    private StringBuilder a = new StringBuilder();
    private long b = -1;

    public final synchronized void a(DetectionType detectionType) {
        if (detectionType != null) {
            b();
            StringBuilder stringBuilder = this.a;
            String str = "";
            switch (detectionType) {
                case NONE:
                    str = "N";
                    break;
                case DONE:
                    str = "O";
                    break;
                case BLINK:
                    str = "E";
                    break;
                case MOUTH:
                    str = "M";
                    break;
                case POS_YAW:
                    str = "Y";
                    break;
                case POS_YAW_LEFT:
                    str = "L";
                    break;
                case POS_YAW_RIGHT:
                    str = "R";
                    break;
                case POS_PITCH:
                    str = "P";
                    break;
                case POS_PITCH_UP:
                    str = "U";
                    break;
                case POS_PITCH_DOWN:
                    str = "D";
                    break;
                case AIMLESS:
                    str = "A";
                    break;
            }
            stringBuilder.append(str);
            this.a.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
    }

    public final synchronized void a(DetectionFailedType detectionFailedType) {
        if (detectionFailedType != null) {
            b();
            StringBuilder stringBuilder = this.a;
            String str = "";
            switch (detectionFailedType) {
                case NOTVIDEO:
                    str = "n";
                    break;
                case ACTIONBLEND:
                    str = "b";
                    break;
                case TIMEOUT:
                    str = "t";
                    break;
                case MASK:
                    str = MapViewConstants.UNITS_DEFAULT;
                    break;
                case TOOMANYFACELOST:
                    str = "o";
                    break;
                case FACELOSTNOTCONTINUOUS:
                    str = "l";
                    break;
                case FACENOTCONTINUOUS:
                    str = "c";
                    break;
            }
            stringBuilder.append(str);
            this.a.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
    }

    private void b() {
        if (this.b == -1) {
            this.b = System.currentTimeMillis();
            this.a.append(System.currentTimeMillis() / 1000);
            this.a.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        this.a.append(System.currentTimeMillis() - this.b);
        this.a.append(":");
    }

    public final synchronized void a() {
        this.a = new StringBuilder();
        this.b = -1;
    }

    public final synchronized String toString() {
        return this.a.toString();
    }
}
