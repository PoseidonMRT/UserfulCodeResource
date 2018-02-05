package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PriceInfo implements Parcelable {
    public static final Creator<PriceInfo> CREATOR = new f();
    private int a;
    private double b;

    protected PriceInfo(Parcel parcel) {
        this.a = parcel.readInt();
        this.b = parcel.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public double getTicketPrice() {
        return this.b;
    }

    public int getTicketType() {
        return this.a;
    }

    public void setTicketPrice(double d) {
        this.b = d;
    }

    public void setTicketType(int i) {
        this.a = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.a);
        parcel.writeDouble(this.b);
    }
}
