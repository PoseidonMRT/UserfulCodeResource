package com.mqunar.atomenv.pc.model;

import com.mqunar.atomenv.pc.AbsCallStat;

public class FlightAgentCall extends AbsCallStat {
    public static final int CALLSTAT_FLIGHT_AGENT = 1;
    String agentid = "";
    String calltime = "";
    String code = "";
    String date = "";
    String phone = "";

    public FlightAgentCall(String str, String str2, String str3, String str4, String str5) {
        this.code = str;
        this.phone = str2;
        this.date = str3;
        this.calltime = str4;
        this.agentid = str5;
    }

    public String toCallString() {
        return "" + type() + "|#|" + this.code + "|#|" + this.date + "|#|" + this.agentid + "|#|" + this.phone + "|#|" + this.calltime + "|#|" + count();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.code == null ? 0 : this.code.hashCode()) + (((this.agentid == null ? 0 : this.agentid.hashCode()) + 31) * 31)) * 31;
        if (this.phone != null) {
            i = this.phone.hashCode();
        }
        return hashCode + i;
    }

    public int type() {
        return 1;
    }

    public String key() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.agentid);
        stringBuilder.append(this.code);
        stringBuilder.append(this.phone);
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
        FlightAgentCall flightAgentCall = (FlightAgentCall) obj;
        if (this.agentid == null) {
            if (flightAgentCall.agentid != null) {
                return false;
            }
        } else if (!this.agentid.equals(flightAgentCall.agentid)) {
            return false;
        }
        if (this.code == null) {
            if (flightAgentCall.code != null) {
                return false;
            }
        } else if (!this.code.equals(flightAgentCall.code)) {
            return false;
        }
        if (this.phone == null) {
            if (flightAgentCall.phone != null) {
                return false;
            }
            return true;
        } else if (this.phone.equals(flightAgentCall.phone)) {
            return true;
        } else {
            return false;
        }
    }
}
