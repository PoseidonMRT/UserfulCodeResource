package com.mqunar.atomenv.pc.model;

import com.mqunar.atomenv.pc.AbsCallStat;

public class HotelAgentCall extends AbsCallStat {
    public static final int CALLSTAT_HOTEL_AGENT = 2;
    String agentid = "";
    String calltime = "";
    String date = "";
    String phone = "";
    String seqid = "";

    public HotelAgentCall(String str, String str2, String str3, String str4, String str5) {
        this.seqid = str;
        this.phone = str2;
        this.date = str3;
        this.calltime = str4;
        this.agentid = str5;
    }

    public String toCallString() {
        return "" + type() + "|#|" + this.seqid + "|#|" + this.date + "|#|" + this.agentid + "|#|" + this.phone + "|#|" + this.calltime + "|#|" + count();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.phone == null ? 0 : this.phone.hashCode()) + (((this.agentid == null ? 0 : this.agentid.hashCode()) + 31) * 31)) * 31;
        if (this.seqid != null) {
            i = this.seqid.hashCode();
        }
        return hashCode + i;
    }

    public int type() {
        return 2;
    }

    public String key() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.agentid);
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
        HotelAgentCall hotelAgentCall = (HotelAgentCall) obj;
        if (this.agentid == null) {
            if (hotelAgentCall.agentid != null) {
                return false;
            }
        } else if (!this.agentid.equals(hotelAgentCall.agentid)) {
            return false;
        }
        if (this.phone == null) {
            if (hotelAgentCall.phone != null) {
                return false;
            }
        } else if (!this.phone.equals(hotelAgentCall.phone)) {
            return false;
        }
        if (this.seqid == null) {
            if (hotelAgentCall.seqid != null) {
                return false;
            }
            return true;
        } else if (this.seqid.equals(hotelAgentCall.seqid)) {
            return true;
        } else {
            return false;
        }
    }
}
