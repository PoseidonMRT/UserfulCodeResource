package qunar.sdk.location;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.SpeechConstant;
import java.util.ArrayList;
import java.util.List;

public class BDLocationStrategy extends QunarGPSLocationStrategy implements BDLocationListener {
    private Context context;
    private boolean isInit = false;
    private ArrayList<QLocation> locations;
    private LocationClient mLocationClient;

    public /* bridge */ /* synthetic */ void setELocationListener(QunarGPSLocationListener qunarGPSLocationListener) {
        super.setELocationListener(qunarGPSLocationListener);
    }

    public /* bridge */ /* synthetic */ void setOption(QunarGPSOption qunarGPSOption) {
        super.setOption(qunarGPSOption);
    }

    public BDLocationStrategy(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        this.mLocationClient = new LocationClient(this.context);
        this.locations = new ArrayList();
    }

    public QunarGPSLocationType getLocationType() {
        return QunarGPSLocationType.BAIDU;
    }

    private void setBDOptions() {
        if (this.mLocationClient != null && this.option != null && (this.option instanceof BDGPSOption)) {
            BDGPSOption bDGPSOption = (BDGPSOption) this.option;
            LocationClientOption locationClientOption = new LocationClientOption();
            locationClientOption.setScanSpan(bDGPSOption.scanSpan);
            locationClientOption.setOpenGps(bDGPSOption.isOpenGps());
            locationClientOption.setCoorType(BDGPSCoorType.convert2BDCoorType(bDGPSOption.getCoorType()));
            locationClientOption.setLocationMode(BDGPSLocationMode.convert2BDMode(bDGPSOption.getLocationMode()));
            locationClientOption.setTimeOut(bDGPSOption.timeout);
            locationClientOption.setAddrType(SpeechConstant.PLUS_LOCAL_ALL);
            this.mLocationClient.setLocOption(locationClientOption);
            this.isInit = true;
        }
    }

    public void startGPSLoction() {
        if (this.mLocationClient != null) {
            if (!this.isInit) {
                setBDOptions();
            }
            this.mLocationClient.registerLocationListener(this);
            this.mLocationClient.start();
            this.mLocationClient.requestLocation();
        }
    }

    public void pauseLocation() {
        if (this.mLocationClient != null) {
            this.mLocationClient.stop();
        }
    }

    public void stopLocation() {
        GPSStorageUtils.cach5GPS(this.context, this.locations);
        if (this.mLocationClient != null) {
            this.mLocationClient.unRegisterLocationListener(this);
        }
        pauseLocation();
        this.isInit = false;
    }

    public void forceStopLocation() {
        if (this.mLocationClient != null) {
            try {
                LocationClientOption locOption = this.mLocationClient.getLocOption();
                locOption.setOpenGps(false);
                this.mLocationClient.setLocOption(locOption);
                this.mLocationClient.unRegisterLocationListener(this);
                this.mLocationClient.stop();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void onReceiveLocation(BDLocation bDLocation) {
        if (bDLocation == null || bDLocation.getLatitude() == 0.0d || bDLocation.getLongitude() == 0.0d) {
            this.locationListener.onReceiveLocation(this.eLoc);
            return;
        }
        int locType = bDLocation.getLocType();
        if (locType == 61 || locType == 65 || locType == 66 || locType == BDLocation.TypeNetWorkLocation) {
            if (this.eLoc == null) {
                this.eLoc = new QLocation(bDLocation);
            } else {
                synchronized (this.eLoc) {
                    this.eLoc.set(bDLocation);
                }
            }
            if (this.locationListener != null) {
                if (this.locations.size() >= 5) {
                    this.locations.remove(0);
                }
                this.locations.add(this.eLoc);
                this.locationListener.onReceiveLocation(this.eLoc);
            }
        }
    }

    public List<QLocation> getCacheLocation(int i) {
        return GPSStorageUtils.getFitGPSS(this.context, (long) i);
    }
}
