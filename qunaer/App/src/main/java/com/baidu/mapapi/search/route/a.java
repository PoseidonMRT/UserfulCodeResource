package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<BikingRouteLine> {
    a() {
    }

    public BikingRouteLine a(Parcel parcel) {
        return new BikingRouteLine(parcel);
    }

    public BikingRouteLine[] a(int i) {
        return new BikingRouteLine[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
