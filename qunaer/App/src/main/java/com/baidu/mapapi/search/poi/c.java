package com.baidu.mapapi.search.poi;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class c implements Creator<PoiResult> {
    c() {
    }

    public PoiResult a(Parcel parcel) {
        return new PoiResult(parcel);
    }

    public PoiResult[] a(int i) {
        return new PoiResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
