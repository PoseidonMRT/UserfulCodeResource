package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.baidu.mapapi.search.route.BikingRouteLine.BikingStep;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import java.util.List;

public class RouteLine<T extends RouteStep> implements Parcelable {
    TYPE a;
    private RouteNode b;
    private RouteNode c;
    private String d;
    private List<T> e;
    private int f;
    private int g;

    public enum TYPE {
        DRIVESTEP(0),
        TRANSITSTEP(1),
        WALKSTEP(2),
        BIKINGSTEP(3);
        
        private int a;

        private TYPE(int i) {
            this.a = i;
        }

        private int a() {
            return this.a;
        }
    }

    protected RouteLine() {
    }

    protected RouteLine(Parcel parcel) {
        int readInt = parcel.readInt();
        this.b = (RouteNode) parcel.readValue(RouteNode.class.getClassLoader());
        this.c = (RouteNode) parcel.readValue(RouteNode.class.getClassLoader());
        this.d = parcel.readString();
        switch (readInt) {
            case 0:
                this.e = parcel.createTypedArrayList(DrivingStep.CREATOR);
                break;
            case 1:
                this.e = parcel.createTypedArrayList(TransitStep.CREATOR);
                break;
            case 2:
                this.e = parcel.createTypedArrayList(WalkingStep.CREATOR);
                break;
            case 3:
                this.e = parcel.createTypedArrayList(BikingStep.CREATOR);
                break;
        }
        this.f = parcel.readInt();
        this.g = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public List<T> getAllStep() {
        return this.e;
    }

    public int getDistance() {
        return this.f;
    }

    public int getDuration() {
        return this.g;
    }

    public RouteNode getStarting() {
        return this.b;
    }

    public RouteNode getTerminal() {
        return this.c;
    }

    public String getTitle() {
        return this.d;
    }

    protected TYPE getType() {
        return this.a;
    }

    public void setDistance(int i) {
        this.f = i;
    }

    public void setDuration(int i) {
        this.g = i;
    }

    public void setStarting(RouteNode routeNode) {
        this.b = routeNode;
    }

    public void setSteps(List<T> list) {
        this.e = list;
    }

    public void setTerminal(RouteNode routeNode) {
        this.c = routeNode;
    }

    public void setTitle(String str) {
        this.d = str;
    }

    protected void setType(TYPE type) {
        this.a = type;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.a != null) {
            parcel.writeInt(this.a.a());
        } else {
            parcel.writeInt(10);
        }
        parcel.writeValue(this.b);
        parcel.writeValue(this.c);
        parcel.writeString(this.d);
        if (this.a != null) {
            parcel.writeTypedList(this.e);
        }
        parcel.writeInt(this.f);
        parcel.writeInt(this.g);
    }
}
