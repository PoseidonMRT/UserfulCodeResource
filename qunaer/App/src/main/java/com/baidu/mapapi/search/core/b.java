package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator<CityInfo> {
    b() {
    }

    public CityInfo a(Parcel parcel) {
        return new CityInfo(parcel);
    }

    public CityInfo[] a(int i) {
        return new CityInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
