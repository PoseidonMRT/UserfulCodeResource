package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.BusInfo;
import com.baidu.mapapi.search.core.CoachInfo;
import com.baidu.mapapi.search.core.PlaneInfo;
import com.baidu.mapapi.search.core.PriceInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.k;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.speech.VoiceWakeuperAidl;
import java.util.ArrayList;
import java.util.List;

public final class MassTransitRouteLine extends RouteLine<TransitStep> implements Parcelable {
    public static final Creator<MassTransitRouteLine> CREATOR = new i();
    private String b;
    private double c;
    private List<PriceInfo> d;
    private List<List<TransitStep>> e = null;

    public class TransitStep extends RouteStep implements Parcelable {
        public static final Creator<TransitStep> CREATOR = new j();
        private List<TrafficCondition> c;
        private LatLng d;
        private LatLng e;
        private k f;
        private PlaneInfo g;
        private CoachInfo h;
        private BusInfo i;
        private StepVehicleInfoType j;
        private String k;
        private String l;

        public enum StepVehicleInfoType {
            ESTEP_TRAIN(1),
            ESTEP_PLANE(2),
            ESTEP_BUS(3),
            ESTEP_DRIVING(4),
            ESTEP_WALK(5),
            ESTEP_COACH(6);
            
            private int a;

            private StepVehicleInfoType(int i) {
                this.a = 0;
                this.a = i;
            }

            public int getInt() {
                return this.a;
            }
        }

        public class TrafficCondition implements Parcelable {
            public static final Creator<TrafficCondition> CREATOR = new k();
            private int a;
            private int b;

            protected TrafficCondition(Parcel parcel) {
                this.a = parcel.readInt();
                this.b = parcel.readInt();
            }

            public int describeContents() {
                return 0;
            }

            public int getTrafficGeoCnt() {
                return this.b;
            }

            public int getTrafficStatus() {
                return this.a;
            }

            public void setTrafficGeoCnt(int i) {
                this.b = i;
            }

            public void setTrafficStatus(int i) {
                this.a = i;
            }

            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(this.a);
                parcel.writeInt(this.b);
            }
        }

        protected TransitStep(Parcel parcel) {
            super(parcel);
            this.c = parcel.createTypedArrayList(TrafficCondition.CREATOR);
            this.d = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
            this.e = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
            this.f = (k) parcel.readParcelable(k.class.getClassLoader());
            this.g = (PlaneInfo) parcel.readParcelable(PlaneInfo.class.getClassLoader());
            this.h = (CoachInfo) parcel.readParcelable(CoachInfo.class.getClassLoader());
            this.i = (BusInfo) parcel.readParcelable(BusInfo.class.getClassLoader());
            switch (parcel.readInt()) {
                case 1:
                    this.j = StepVehicleInfoType.ESTEP_TRAIN;
                    break;
                case 2:
                    this.j = StepVehicleInfoType.ESTEP_PLANE;
                    break;
                case 3:
                    this.j = StepVehicleInfoType.ESTEP_BUS;
                    break;
                case 4:
                    this.j = StepVehicleInfoType.ESTEP_DRIVING;
                    break;
                case 5:
                    this.j = StepVehicleInfoType.ESTEP_WALK;
                    break;
                case 6:
                    this.j = StepVehicleInfoType.ESTEP_COACH;
                    break;
            }
            this.k = parcel.readString();
            this.l = parcel.readString();
        }

        private List<LatLng> a(String str) {
            List<LatLng> arrayList = new ArrayList();
            String[] split = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            if (split != null) {
                int i = 0;
                while (i < split.length) {
                    if (!(split[i] == null || split[i] == "")) {
                        String[] split2 = split[i].split(",");
                        if (!(split2 == null || split2[1] == "" || split2[0] == "")) {
                            Object latLng = new LatLng(Double.parseDouble(split2[1]), Double.parseDouble(split2[0]));
                            if (latLng != null && SDKInitializer.getCoordType() == CoordType.GCJ02) {
                                latLng = CoordTrans.baiduToGcj(latLng);
                            }
                            arrayList.add(latLng);
                        }
                    }
                    i++;
                }
            }
            return arrayList;
        }

        public int describeContents() {
            return 0;
        }

        public BusInfo getBusInfo() {
            return this.i;
        }

        public CoachInfo getCoachInfo() {
            return this.h;
        }

        public LatLng getEndLocation() {
            return this.e;
        }

        public String getInstructions() {
            return this.k;
        }

        public PlaneInfo getPlaneInfo() {
            return this.g;
        }

        public LatLng getStartLocation() {
            return this.d;
        }

        public List<TrafficCondition> getTrafficConditions() {
            return this.c;
        }

        public k getTrainInfo() {
            return this.f;
        }

        public StepVehicleInfoType getVehileType() {
            return this.j;
        }

        public List<LatLng> getWayPoints() {
            if (this.mWayPoints == null) {
                this.mWayPoints = a(this.l);
            }
            return this.mWayPoints;
        }

        public void setBusInfo(BusInfo busInfo) {
            this.i = busInfo;
        }

        public void setCoachInfo(CoachInfo coachInfo) {
            this.h = coachInfo;
        }

        public void setEndLocation(LatLng latLng) {
            this.e = latLng;
        }

        public void setInstructions(String str) {
            this.k = str;
        }

        public void setPathString(String str) {
            this.l = str;
        }

        public void setPlaneInfo(PlaneInfo planeInfo) {
            this.g = planeInfo;
        }

        public void setStartLocation(LatLng latLng) {
            this.d = latLng;
        }

        public void setTrafficConditions(List<TrafficCondition> list) {
            this.c = list;
        }

        public void setTrainInfo(k kVar) {
            this.f = kVar;
        }

        public void setVehileType(StepVehicleInfoType stepVehicleInfoType) {
            this.j = stepVehicleInfoType;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeTypedList(this.c);
            parcel.writeParcelable(this.d, i);
            parcel.writeParcelable(this.e, i);
            parcel.writeParcelable(this.f, i);
            parcel.writeParcelable(this.g, i);
            parcel.writeParcelable(this.h, i);
            parcel.writeParcelable(this.i, i);
            parcel.writeInt(this.j.getInt());
            parcel.writeString(this.k);
            parcel.writeString(this.l);
        }
    }

    protected MassTransitRouteLine(Parcel parcel) {
        super(parcel);
        int readInt = parcel.readInt();
        this.b = parcel.readString();
        this.c = parcel.readDouble();
        this.d = parcel.createTypedArrayList(PriceInfo.CREATOR);
        if (readInt > 0) {
            this.e = new ArrayList();
            for (int i = 0; i < readInt; i++) {
                this.e.add(parcel.createTypedArrayList(TransitStep.CREATOR));
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public String getArriveTime() {
        return this.b;
    }

    public List<List<TransitStep>> getNewSteps() {
        return this.e;
    }

    public double getPrice() {
        return this.c;
    }

    public List<PriceInfo> getPriceInfo() {
        return this.d;
    }

    public void setArriveTime(String str) {
        this.b = str;
    }

    public void setNewSteps(List<List<TransitStep>> list) {
        this.e = list;
    }

    public void setPrice(double d) {
        this.c = d;
    }

    public void setPriceInfo(List<PriceInfo> list) {
        this.d = list;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.e == null ? 0 : this.e.size());
        parcel.writeString(this.b);
        parcel.writeDouble(this.c);
        parcel.writeTypedList(this.d);
        for (List writeTypedList : this.e) {
            parcel.writeTypedList(writeTypedList);
        }
    }
}
