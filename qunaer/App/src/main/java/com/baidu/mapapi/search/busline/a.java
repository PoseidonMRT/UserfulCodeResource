package com.baidu.mapapi.search.busline;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<BusLineResult> {
    a() {
    }

    public BusLineResult a(Parcel parcel) {
        return new BusLineResult(parcel);
    }

    public BusLineResult[] a(int i) {
        return new BusLineResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
