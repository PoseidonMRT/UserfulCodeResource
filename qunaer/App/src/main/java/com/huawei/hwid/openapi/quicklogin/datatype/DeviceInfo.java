package com.huawei.hwid.openapi.quicklogin.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DeviceInfo implements Parcelable {
    public static final Creator CREATOR = new b();
    private String a;
    private String b;
    private String c;
    private String d;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.b);
        parcel.writeString(this.d);
        parcel.writeString(this.a);
        parcel.writeString(this.c);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{'mDeviceAliasName':");
        stringBuilder.append(this.d);
        stringBuilder.append(",'mDeviceID':");
        stringBuilder.append(this.b);
        stringBuilder.append(",'mTerminalType':");
        stringBuilder.append(this.c);
        stringBuilder.append(",'mDeviceType':");
        stringBuilder.append(this.a);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
