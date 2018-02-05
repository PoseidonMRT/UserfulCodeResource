package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class j implements Creator<TaxiInfo> {
    j() {
    }

    public TaxiInfo a(Parcel parcel) {
        return new TaxiInfo(parcel);
    }

    public TaxiInfo[] a(int i) {
        return new TaxiInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
