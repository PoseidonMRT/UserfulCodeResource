package com.baidu.mapapi.map;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class h implements Creator<MapStatus> {
    h() {
    }

    public MapStatus a(Parcel parcel) {
        return new MapStatus(parcel);
    }

    public MapStatus[] a(int i) {
        return new MapStatus[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
