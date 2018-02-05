package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteLine.TYPE;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.speech.VoiceWakeuperAidl;
import java.util.ArrayList;
import java.util.List;

public class BikingRouteLine extends RouteLine<BikingStep> implements Parcelable {
    public static final Creator<BikingRouteLine> CREATOR = new a();

    public class BikingStep extends RouteStep implements Parcelable {
        public static final Creator<BikingStep> CREATOR = new b();
        private int c;
        private RouteNode d;
        private RouteNode e;
        private String f;
        private String g;
        private String h;
        private String i;

        protected BikingStep(Parcel parcel) {
            super(parcel);
            this.c = parcel.readInt();
            this.d = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            this.e = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            this.f = parcel.readString();
            this.g = parcel.readString();
            this.h = parcel.readString();
            this.i = parcel.readString();
        }

        private List<LatLng> a(String str) {
            if (str == null || str.length() == 0) {
                return null;
            }
            List<LatLng> arrayList = new ArrayList();
            String[] split = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            if (split == null || split.length == 0) {
                return null;
            }
            for (String split2 : split) {
                String[] split3 = split2.split(",");
                if (split3 != null && split3.length >= 2) {
                    Object latLng = new LatLng(Double.valueOf(split3[1]).doubleValue(), Double.valueOf(split3[0]).doubleValue());
                    if (latLng != null && SDKInitializer.getCoordType() == CoordType.GCJ02) {
                        latLng = CoordTrans.baiduToGcj(latLng);
                    }
                    arrayList.add(latLng);
                }
            }
            return arrayList;
        }

        public int describeContents() {
            return 0;
        }

        public int getDirection() {
            return this.c;
        }

        public RouteNode getEntrance() {
            return this.d;
        }

        public String getEntranceInstructions() {
            return this.g;
        }

        public RouteNode getExit() {
            return this.e;
        }

        public String getExitInstructions() {
            return this.h;
        }

        public String getInstructions() {
            return this.i;
        }

        public List<LatLng> getWayPoints() {
            if (this.mWayPoints == null) {
                this.mWayPoints = a(this.f);
            }
            return this.mWayPoints;
        }

        public void setDirection(int i) {
            this.c = i;
        }

        public void setEntrance(RouteNode routeNode) {
            this.d = routeNode;
        }

        public void setEntranceInstructions(String str) {
            this.g = str;
        }

        public void setExit(RouteNode routeNode) {
            this.e = routeNode;
        }

        public void setExitInstructions(String str) {
            this.h = str;
        }

        public void setInstructions(String str) {
            this.i = str;
        }

        public void setPathString(String str) {
            this.f = str;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, 1);
            parcel.writeInt(this.c);
            parcel.writeParcelable(this.d, 1);
            parcel.writeParcelable(this.e, 1);
            parcel.writeString(this.f);
            parcel.writeString(this.g);
            parcel.writeString(this.h);
            parcel.writeString(this.i);
        }
    }

    protected BikingRouteLine(Parcel parcel) {
        super(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public List<BikingStep> getAllStep() {
        return super.getAllStep();
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.setType(TYPE.BIKINGSTEP);
        super.writeToParcel(parcel, 1);
    }
}
