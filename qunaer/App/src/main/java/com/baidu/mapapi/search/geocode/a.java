package com.baidu.mapapi.search.geocode;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<GeoCodeResult> {
    a() {
    }

    public GeoCodeResult a(Parcel parcel) {
        return new GeoCodeResult(parcel);
    }

    public GeoCodeResult[] a(int i) {
        return new GeoCodeResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
