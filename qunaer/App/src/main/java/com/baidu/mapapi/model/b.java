package com.baidu.mapapi.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator<LatLngBounds> {
    b() {
    }

    public LatLngBounds a(Parcel parcel) {
        return new LatLngBounds(parcel);
    }

    public LatLngBounds[] a(int i) {
        return new LatLngBounds[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
