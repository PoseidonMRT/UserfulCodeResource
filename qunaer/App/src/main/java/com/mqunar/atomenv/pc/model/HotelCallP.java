package com.mqunar.atomenv.pc.model;

import com.mqunar.atomenv.pc.AbsCallStat;

public class HotelCallP extends AbsCallStat {
    public static final int CALLSTAT_HOTEL_P = 4;
    String calltime = "";
    String phone = "";
    String seqid = "";

    public HotelCallP(String str, String str2, String str3) {
        this.seqid = str;
        this.phone = str2;
        this.calltime = str3;
    }

    public String toCallString() {
        return "" + type() + "|#|" + this.seqid + "|#|" + this.phone + "|#|" + this.calltime + "|#|" + count();
    }

    public int type() {
        return 4;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.phone == null ? 0 : this.phone.hashCode()) + 31) * 31;
        if (this.seqid != null) {
            i = this.seqid.hashCode();
        }
        return hashCode + i;
    }

    public String key() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.phone);
        stringBuilder.append(this.seqid);
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HotelCall hotelCall = (HotelCall) obj;
        if (this.phone == null) {
            if (hotelCall.phone != null) {
                return false;
            }
        } else if (!this.phone.equals(hotelCall.phone)) {
            return false;
        }
        if (this.seqid == null) {
            if (hotelCall.seqid != null) {
                return false;
            }
            return true;
        } else if (this.seqid.equals(hotelCall.seqid)) {
            return true;
        } else {
            return false;
        }
    }
}
