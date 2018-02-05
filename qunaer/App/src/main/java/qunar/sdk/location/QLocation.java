package qunar.sdk.location;

import android.location.Location;
import com.baidu.location.BDLocation;
import java.io.Serializable;

public class QLocation extends Location implements Serializable {
    private static final long serialVersionUID = 1;
    private String country = "";
    private String countryCode = "";
    private boolean isAbroad = false;

    public QLocation(String str) {
        super(str);
    }

    public QLocation(BDLocation bDLocation) {
        super("bmap");
        set(bDLocation);
    }

    public QLocation(double d, double d2) {
        super("bmap");
        setLatitude(d);
        setLongitude(d2);
    }

    public void set(BDLocation bDLocation) {
        reset();
        setProvider("bmap");
        setLatitude(bDLocation.getLatitude());
        setLongitude(bDLocation.getLongitude());
        setAltitude(bDLocation.getAltitude());
        setAccuracy(bDLocation.getRadius());
        setBearing(bDLocation.getDirection());
        setTime(System.currentTimeMillis());
        if (bDLocation.hasSpeed()) {
            setSpeed(bDLocation.getSpeed());
        } else {
            removeSpeed();
        }
        setCountry(bDLocation.getCountry());
        setCountryCode(bDLocation.getCountryCode());
        if (this.countryCode == null || "".equals(this.countryCode) || "0".equals(this.countryCode)) {
            setisAbroad(false);
        } else {
            setisAbroad(true);
        }
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public void setisAbroad(boolean z) {
        this.isAbroad = z;
    }

    public boolean getIsAbroad() {
        return this.isAbroad;
    }
}
