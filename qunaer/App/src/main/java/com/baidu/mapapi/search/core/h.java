package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class h implements Creator<RouteStep> {
    h() {
    }

    public RouteStep a(Parcel parcel) {
        return new RouteStep(parcel);
    }

    public RouteStep[] a(int i) {
        return new RouteStep[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
