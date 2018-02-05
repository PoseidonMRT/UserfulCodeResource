package com.baidu.mapapi.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<LatLng> {
    a() {
    }

    public LatLng a(Parcel parcel) {
        return new LatLng(parcel);
    }

    public LatLng[] a(int i) {
        return new LatLng[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
