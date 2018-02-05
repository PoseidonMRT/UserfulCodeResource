package com.mqunar.libtask;

import com.mqunar.tools.EnumUtils.ITypeCode;

public class Ticket {
    public static final int ADD_CANCELSAMET = 3;
    public static final int ADD_INSERT2HEAD = 1;
    public static final int ADD_ONORDER = 0;
    public static final int CACHE_DEP = 2;
    public static final int CACHE_DOB = 3;
    public static final int CACHE_FORCE = 1;
    public static final int CACHE_NEVER = 0;
    public final int addType;
    public final int cacheType;
    public final boolean cancelable;
    public final boolean multiTrasnfer;
    public final boolean progress;

    public enum RequestFeature implements ITypeCode {
        CANCELABLE,
        ADD_ONORDER {
            public int getCode() {
                return 0;
            }
        },
        ADD_INSERT2HEAD {
            public int getCode() {
                return 1;
            }
        },
        ADD_CANCELSAMET {
            public int getCode() {
                return 3;
            }
        },
        CACHE_FORCE {
            public int getCode() {
                return 1;
            }
        },
        CACHE_DEP {
            public int getCode() {
                return 2;
            }
        },
        CACHE_NEVER {
            public int getCode() {
                return 0;
            }
        },
        CACHE_DOB {
            public int getCode() {
                return 3;
            }
        },
        MULTI_TRANSFER,
        PROGRESS;

        public int getCode() {
            return -1;
        }
    }

    public Ticket(RequestFeature... requestFeatureArr) {
        boolean z;
        boolean z2;
        int i;
        boolean z3 = false;
        int i2 = 3;
        if (requestFeatureArr == null || requestFeatureArr.length == 0) {
            z = false;
            z2 = false;
            i = 0;
        } else {
            boolean z4 = false;
            boolean z5 = false;
            z = false;
            z2 = false;
            i = 0;
            for (RequestFeature requestFeature : requestFeatureArr) {
                if (requestFeature != null) {
                    switch (u.a[requestFeature.ordinal()]) {
                        case 1:
                            z2 = true;
                            break;
                        case 2:
                        case 3:
                        case 4:
                            if (!z5) {
                                i2 = requestFeature.getCode();
                                z5 = true;
                                break;
                            }
                            throw new IllegalArgumentException("duplicate ADD type is not allowed");
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if (!z4) {
                                i = requestFeature.getCode();
                                z4 = true;
                                break;
                            }
                            throw new IllegalArgumentException("duplicate CACHE type is not allowed");
                        case 9:
                            z = true;
                            break;
                        case 10:
                            z3 = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        this.addType = i2;
        this.cacheType = i;
        this.cancelable = z2;
        this.multiTrasnfer = z;
        this.progress = z3;
    }
}
