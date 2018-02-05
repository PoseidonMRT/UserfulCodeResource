package com.baidu.mapapi.search.share;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<ShareUrlResult> {
    a() {
    }

    public ShareUrlResult a(Parcel parcel) {
        return new ShareUrlResult(parcel);
    }

    public ShareUrlResult[] a(int i) {
        return new ShareUrlResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
