package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class f implements Creator<PriceInfo> {
    f() {
    }

    public PriceInfo a(Parcel parcel) {
        return new PriceInfo(parcel);
    }

    public PriceInfo[] a(int i) {
        return new PriceInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
