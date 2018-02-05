package com.huawei.hwid.openapi.quicklogin.datatype;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator {
    b() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }

    public DeviceInfo a(Parcel parcel) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.b = parcel.readString();
        deviceInfo.d = parcel.readString();
        deviceInfo.a = parcel.readString();
        deviceInfo.c = parcel.readString();
        return deviceInfo;
    }

    public DeviceInfo[] a(int i) {
        return new DeviceInfo[i];
    }
}
