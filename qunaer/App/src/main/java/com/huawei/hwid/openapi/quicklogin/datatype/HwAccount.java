package com.huawei.hwid.openapi.quicklogin.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class HwAccount implements Parcelable {
    public static final Creator CREATOR = new c();
    private String a;
    private String b;
    private String c;
    private String d;
    private int e = 0;
    private String f;
    private String g;
    private String h;

    public String toString() {
        return " accountname =" + this.c + " ;token = " + this.b + " ;tokenType = " + this.a + " ;userId = " + this.d + " ;siteId = " + this.e + " ;cookie = " + this.f + " ;deviceId = " + this.g + " ;deviceType = " + this.h;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeInt(this.e);
        parcel.writeString(this.f);
        parcel.writeString(this.g);
        parcel.writeString(this.h);
    }
}
