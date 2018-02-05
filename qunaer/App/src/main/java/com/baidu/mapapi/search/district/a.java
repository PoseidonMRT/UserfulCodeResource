package com.baidu.mapapi.search.district;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<DistrictResult> {
    a() {
    }

    public DistrictResult a(Parcel parcel) {
        return new DistrictResult(parcel);
    }

    public DistrictResult[] a(int i) {
        return new DistrictResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
