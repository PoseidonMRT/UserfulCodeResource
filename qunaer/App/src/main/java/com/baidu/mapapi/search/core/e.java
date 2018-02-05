package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class e implements Creator<PoiInfo> {
    e() {
    }

    public PoiInfo a(Parcel parcel) {
        return new PoiInfo(parcel);
    }

    public PoiInfo[] a(int i) {
        return new PoiInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
