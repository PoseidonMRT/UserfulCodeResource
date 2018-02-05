package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class l implements Creator<MassTransitRouteResult> {
    l() {
    }

    public MassTransitRouteResult a(Parcel parcel) {
        return new MassTransitRouteResult(parcel);
    }

    public MassTransitRouteResult[] a(int i) {
        return new MassTransitRouteResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
