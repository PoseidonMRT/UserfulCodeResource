package com.baidu.mapapi.search.geocode;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;

final class c implements Creator<AddressComponent> {
    c() {
    }

    public AddressComponent a(Parcel parcel) {
        return new AddressComponent(parcel);
    }

    public AddressComponent[] a(int i) {
        return new AddressComponent[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
