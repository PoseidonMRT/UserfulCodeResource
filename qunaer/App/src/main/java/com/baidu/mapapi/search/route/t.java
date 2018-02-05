package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class t implements Creator<WalkingRouteResult> {
    t() {
    }

    public WalkingRouteResult a(Parcel parcel) {
        return new WalkingRouteResult(parcel);
    }

    public WalkingRouteResult[] a(int i) {
        return new WalkingRouteResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
