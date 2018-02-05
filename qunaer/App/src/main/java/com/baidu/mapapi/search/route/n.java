package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class n implements Creator<SuggestAddrInfo> {
    n() {
    }

    public SuggestAddrInfo a(Parcel parcel) {
        return new SuggestAddrInfo(parcel);
    }

    public SuggestAddrInfo[] a(int i) {
        return new SuggestAddrInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
