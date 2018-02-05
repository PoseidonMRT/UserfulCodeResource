package com.baidu.mapapi.search.poi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import java.util.List;

public class PoiResult extends SearchResult implements Parcelable {
    public static final Creator<PoiResult> CREATOR = new c();
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int d = 0;
    private List<PoiInfo> e;
    private List<CityInfo> f;
    private List<PoiAddrInfo> g;
    private boolean h = false;

    PoiResult(Parcel parcel) {
        this.a = parcel.readInt();
        this.b = parcel.readInt();
        this.c = parcel.readInt();
        this.d = parcel.readInt();
        this.e = parcel.readArrayList(PoiInfo.class.getClassLoader());
        this.f = parcel.readArrayList(CityInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public List<PoiAddrInfo> getAllAddr() {
        return this.g;
    }

    public List<PoiInfo> getAllPoi() {
        return this.e;
    }

    public int getCurrentPageCapacity() {
        return this.c;
    }

    public int getCurrentPageNum() {
        return this.a;
    }

    public List<CityInfo> getSuggestCityList() {
        return this.f;
    }

    public int getTotalPageNum() {
        return this.b;
    }

    public int getTotalPoiNum() {
        return this.d;
    }

    public boolean isHasAddrInfo() {
        return this.h;
    }

    public void setAddrInfo(List<PoiAddrInfo> list) {
        this.g = list;
    }

    public void setCurrentPageCapacity(int i) {
        this.c = i;
    }

    public void setCurrentPageNum(int i) {
        this.a = i;
    }

    public void setHasAddrInfo(boolean z) {
        this.h = z;
    }

    public void setPoiInfo(List<PoiInfo> list) {
        this.e = list;
    }

    public void setSuggestCityList(List<CityInfo> list) {
        this.f = list;
    }

    public void setTotalPageNum(int i) {
        this.b = i;
    }

    public void setTotalPoiNum(int i) {
        this.d = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.a);
        parcel.writeInt(this.b);
        parcel.writeInt(this.c);
        parcel.writeInt(this.d);
        parcel.writeList(this.e);
        parcel.writeList(this.f);
    }
}
