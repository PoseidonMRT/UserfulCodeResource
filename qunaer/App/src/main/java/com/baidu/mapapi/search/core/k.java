package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class k extends TransitBaseInfo {
    public static final Creator<k> a = new l();
    private double b;
    private String c;

    protected k(Parcel parcel) {
        super(parcel);
        this.b = parcel.readDouble();
        this.c = parcel.readString();
    }

    public void a(double d) {
        this.b = d;
    }

    public void a(String str) {
        this.c = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeDouble(this.b);
        parcel.writeString(this.c);
    }
}
