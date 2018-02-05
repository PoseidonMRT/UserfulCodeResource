package com.baidu.mapapi.map;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class e implements Creator<BaiduMapOptions> {
    e() {
    }

    public BaiduMapOptions a(Parcel parcel) {
        return new BaiduMapOptions(parcel);
    }

    public BaiduMapOptions[] a(int i) {
        return new BaiduMapOptions[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
