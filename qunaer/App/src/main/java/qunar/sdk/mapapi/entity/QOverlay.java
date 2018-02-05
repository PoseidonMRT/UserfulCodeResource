package qunar.sdk.mapapi.entity;

import android.os.Bundle;
import com.baidu.mapapi.map.Overlay;
import java.io.Serializable;
import qunar.sdk.mapapi.QunarMapType;

public class QOverlay implements Serializable {
    private static final long serialVersionUID = 1;
    private Bundle extraInfo;
    protected QunarMapType mapType;
    private Object overlay;
    private boolean visible = true;
    private int zIndex;

    public void remove() {
        if (this.overlay == null) {
            return;
        }
        if (this.mapType == QunarMapType.BAIDU) {
            if (this.overlay instanceof Overlay) {
                ((Overlay) this.overlay).remove();
            }
        } else if (this.mapType != QunarMapType.GAODE) {
        }
    }

    public void setOverlayOption(Object obj) {
        this.overlay = obj;
    }

    public Object getOverlayOption() {
        return this.overlay;
    }

    public int getzIndex() {
        return this.zIndex;
    }

    public void setzIndex(int i) {
        this.zIndex = i;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public Bundle getExtraInfo() {
        return this.extraInfo;
    }

    public void setExtraInfo(Bundle bundle) {
        this.extraInfo = bundle;
    }

    public void setMapType(QunarMapType qunarMapType) {
        this.mapType = qunarMapType;
    }
}
