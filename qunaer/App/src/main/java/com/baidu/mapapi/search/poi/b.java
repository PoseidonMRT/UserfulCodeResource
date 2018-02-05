package com.baidu.mapapi.search.poi;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator<PoiIndoorResult> {
    b() {
    }

    public PoiIndoorResult a(Parcel parcel) {
        return new PoiIndoorResult(parcel);
    }

    public PoiIndoorResult[] a(int i) {
        return new PoiIndoorResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
