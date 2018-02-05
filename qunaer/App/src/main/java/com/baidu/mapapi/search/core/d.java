package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class d implements Creator<PlaneInfo> {
    d() {
    }

    public PlaneInfo a(Parcel parcel) {
        return new PlaneInfo(parcel);
    }

    public PlaneInfo[] a(int i) {
        return new PlaneInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
