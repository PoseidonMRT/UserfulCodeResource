package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteLine.TYPE;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.RouteStep;
import java.util.ArrayList;
import java.util.List;

public class DrivingRouteLine extends RouteLine<DrivingStep> implements Parcelable {
    public static final Creator<DrivingRouteLine> CREATOR = new d();
    private boolean b;
    private List<RouteNode> c;
    private int d;
    private int e;

    public class DrivingStep extends RouteStep implements Parcelable {
        public static final Creator<DrivingStep> CREATOR = new e();
        List<LatLng> c;
        int[] d;
        private int e;
        private RouteNode f;
        private RouteNode g;
        private String h;
        private String i;
        private String j;
        private String k;
        private int l;

        protected DrivingStep(Parcel parcel) {
            super(parcel);
            this.e = parcel.readInt();
            this.f = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            this.g = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            this.h = parcel.readString();
            this.i = parcel.readString();
            this.j = parcel.readString();
            this.k = parcel.readString();
            this.l = parcel.readInt();
            this.c = parcel.createTypedArrayList(LatLng.CREATOR);
            this.d = parcel.createIntArray();
        }

        public int describeContents() {
            return 0;
        }

        public int getDirection() {
            return this.e;
        }

        public RouteNode getEntrance() {
            return this.f;
        }

        public String getEntranceInstructions() {
            return this.i;
        }

        public RouteNode getExit() {
            return this.g;
        }

        public String getExitInstructions() {
            return this.j;
        }

        public String getInstructions() {
            return this.k;
        }

        public int getNumTurns() {
            return this.l;
        }

        public int[] getTrafficList() {
            return this.d;
        }

        public List<LatLng> getWayPoints() {
            if (this.mWayPoints == null) {
                this.mWayPoints = CoordUtil.decodeLocationList(this.h);
            }
            return this.c;
        }

        public void setDirection(int i) {
            this.e = i;
        }

        public void setEntrance(RouteNode routeNode) {
            this.f = routeNode;
        }

        public void setEntranceInstructions(String str) {
            this.i = str;
        }

        public void setExit(RouteNode routeNode) {
            this.g = routeNode;
        }

        public void setExitInstructions(String str) {
            this.j = str;
        }

        public void setInstructions(String str) {
            this.k = str;
        }

        public void setNumTurns(int i) {
            this.l = i;
        }

        public void setPathList(List<LatLng> list) {
            this.c = list;
        }

        public void setPathString(String str) {
            this.h = str;
        }

        public void setTrafficList(int[] iArr) {
            this.d = iArr;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.e);
            parcel.writeParcelable(this.f, 1);
            parcel.writeParcelable(this.g, 1);
            parcel.writeString(this.h);
            parcel.writeString(this.i);
            parcel.writeString(this.j);
            parcel.writeString(this.k);
            parcel.writeInt(this.l);
            parcel.writeTypedList(this.c);
            parcel.writeIntArray(this.d);
        }
    }

    protected DrivingRouteLine(Parcel parcel) {
        super(parcel);
        this.b = parcel.readByte() != (byte) 0;
        this.c = new ArrayList();
        parcel.readList(this.c, RouteNode.class.getClassLoader());
        this.d = parcel.readInt();
        this.e = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public int getCongestionDistance() {
        return this.d;
    }

    public int getLightNum() {
        return this.e;
    }

    public List<RouteNode> getWayPoints() {
        return this.c;
    }

    @Deprecated
    public boolean isSupportTraffic() {
        return this.b;
    }

    public void setCongestionDistance(int i) {
        this.d = i;
    }

    public void setLightNum(int i) {
        this.e = i;
    }

    public void setSupportTraffic(boolean z) {
        this.b = z;
    }

    public void setWayPoints(List<RouteNode> list) {
        this.c = list;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.setType(TYPE.DRIVESTEP);
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.b ? (byte) 1 : (byte) 0);
        parcel.writeList(this.c);
        parcel.writeInt(this.d);
        parcel.writeInt(this.e);
    }
}
