package qunar.sdk.mapapi.utils;

public interface MapConstant {
    public static final int GPS_CACHE_COUNT = 5;

    public interface ANIMATION_TIME {
        public static final int DEFAULT_TIME = 300;
    }

    public interface OPERATORS_TYPE {
        public static final int OPERATORS_TYPE_MOBILE = 0;
        public static final int OPERATORS_TYPE_TELECOMU = 2;
        public static final int OPERATORS_TYPE_UNICOM = 1;
        public static final int OPERATORS_TYPE_UNKONW = -1;
    }

    public interface QUNAR_GPS_FIELD {
        public static final String CACHE_NAME = "cachgps";
        public static final String DIRECTION_FIELD = "qunardir";
        public static final String LAT_FIELD = "qunarlat";
        public static final String LON_FIELD = "qunarlon";
        public static final String RADIUS_FIELD = "qunarradius";
        public static final String SHARE_NAME = "qunargpsshare";
        public static final String TIME_FIELD = "time";
    }

    public interface TEMPTOKEN {
        public static final String MAP_DESTROY_TOKEN = "the_guy_want_to_cry";
    }

    public interface TIMEOUT {
        public static final int LOCATION_TIMEOUT_DURUATION = 30000;
        public static final int ROUTE_TIMEOUT_DURUATION = 15000;
        public static final int TIMEOUT_WHAT = 10010;
    }
}
