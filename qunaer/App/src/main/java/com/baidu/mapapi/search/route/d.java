package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class d implements Creator<DrivingRouteLine> {
    d() {
    }

    public DrivingRouteLine a(Parcel parcel) {
        return new DrivingRouteLine(parcel);
    }

    public DrivingRouteLine[] a(int i) {
        return new DrivingRouteLine[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
