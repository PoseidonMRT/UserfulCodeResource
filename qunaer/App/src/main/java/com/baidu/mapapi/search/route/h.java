package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class h implements Creator<IndoorRouteResult> {
    h() {
    }

    public IndoorRouteResult a(Parcel parcel) {
        return new IndoorRouteResult(parcel);
    }

    public IndoorRouteResult[] a(int i) {
        return new IndoorRouteResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
