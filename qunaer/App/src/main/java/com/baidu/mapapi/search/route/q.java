package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class q implements Creator<TransitRouteResult> {
    q() {
    }

    public TransitRouteResult a(Parcel parcel) {
        return new TransitRouteResult(parcel);
    }

    public TransitRouteResult[] a(int i) {
        return new TransitRouteResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
