package qunar.sdk.mapapi;

import java.io.Serializable;

public class QunarMapInitOptions implements Serializable {
    private static final long serialVersionUID = 1;
    public boolean compassEnabled = false;
    public QunarMapType mapType = QunarMapType.BAIDU;
    public boolean overlookingGesturesEnabled = true;
    public boolean rotateGesturesEnabled = true;
    public boolean scaleControlEnabled = false;
    public boolean zoomControlsEnabled = false;
    public boolean zoomGesturesEnabled = true;
}
