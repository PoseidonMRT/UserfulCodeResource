package qunar.sdk.location;

import com.baidu.location.BDLocation;
import com.baidu.platform.comapi.location.CoordinateType;

public enum BDGPSCoorType {
    GCJTYPE,
    MKTTYPE,
    JWDTYPE;

    public static String convert2BDCoorType(BDGPSCoorType bDGPSCoorType) {
        switch (bDGPSCoorType) {
            case GCJTYPE:
                return CoordinateType.GCJ02;
            case MKTTYPE:
                return BDLocation.BDLOCATION_GCJ02_TO_BD09;
            case JWDTYPE:
                return "bd09ll";
            default:
                return CoordinateType.GCJ02;
        }
    }
}
