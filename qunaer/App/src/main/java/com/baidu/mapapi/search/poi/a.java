package com.baidu.mapapi.search.poi;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<PoiDetailResult> {
    a() {
    }

    public PoiDetailResult a(Parcel parcel) {
        return new PoiDetailResult(parcel);
    }

    public PoiDetailResult[] a(int i) {
        return new PoiDetailResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
