package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<BusInfo> {
    a() {
    }

    public BusInfo a(Parcel parcel) {
        return new BusInfo(parcel);
    }

    public BusInfo[] a(int i) {
        return new BusInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
