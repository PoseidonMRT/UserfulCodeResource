package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class g implements Creator<RouteNode> {
    g() {
    }

    public RouteNode a(Parcel parcel) {
        return new RouteNode(parcel);
    }

    public RouteNode[] a(int i) {
        return new RouteNode[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
