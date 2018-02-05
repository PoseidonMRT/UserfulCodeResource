package qunar.sdk.location;

import android.content.Context;
import java.util.List;

abstract class QunarGPSLocationStrategy {
    public static QLocation newestGPS;
    protected Context context;
    protected float distance = 0.0f;
    protected QLocation eLoc;
    protected QunarGPSLocationListener locationListener;
    protected QunarGPSOption option;

    public abstract void forceStopLocation();

    public abstract List<QLocation> getCacheLocation(int i);

    public abstract QunarGPSLocationType getLocationType();

    protected abstract void pauseLocation();

    protected abstract void startGPSLoction();

    protected abstract void stopLocation();

    public QunarGPSLocationStrategy(Context context) {
        this.context = context;
    }

    public void setOption(QunarGPSOption qunarGPSOption) {
        this.option = qunarGPSOption;
    }

    public void setELocationListener(QunarGPSLocationListener qunarGPSLocationListener) {
        this.locationListener = qunarGPSLocationListener;
    }
}
