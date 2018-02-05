package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class o implements Creator<VehicleInfo> {
    o() {
    }

    public VehicleInfo a(Parcel parcel) {
        return new VehicleInfo(parcel);
    }

    public VehicleInfo[] a(int i) {
        return new VehicleInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
