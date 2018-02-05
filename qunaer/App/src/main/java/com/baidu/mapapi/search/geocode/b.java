package com.baidu.mapapi.search.geocode;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator<ReverseGeoCodeResult> {
    b() {
    }

    public ReverseGeoCodeResult a(Parcel parcel) {
        return new ReverseGeoCodeResult(parcel);
    }

    public ReverseGeoCodeResult[] a(int i) {
        return new ReverseGeoCodeResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
