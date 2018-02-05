package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class f implements Creator<DrivingRouteResult> {
    f() {
    }

    public DrivingRouteResult a(Parcel parcel) {
        return new DrivingRouteResult(parcel);
    }

    public DrivingRouteResult[] a(int i) {
        return new DrivingRouteResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
