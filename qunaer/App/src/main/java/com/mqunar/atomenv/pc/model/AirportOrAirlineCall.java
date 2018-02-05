package com.mqunar.atomenv.pc.model;

import com.mqunar.atomenv.pc.AbsCallStat;

public class AirportOrAirlineCall extends AbsCallStat {
    public static final int CALLSTAT_FLIGHT_AIRPORT_AIRLINE = 10;
    String airportname = "";
    String calltime = "";
    String phonenum = "";
    String phonetype = "";

    public AirportOrAirlineCall(String str, String str2, String str3, String str4) {
        this.airportname = str;
        this.phonetype = str2;
        this.phonenum = str3;
        this.calltime = str4;
    }

    public String toCallString() {
        return "" + type() + "|#|" + this.airportname + "|#|" + this.phonetype + "|#|" + this.phonenum + "|#|" + this.calltime + "|#|" + count();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.airportname == null ? 0 : this.airportname.hashCode()) + 31) * 31;
        if (this.phonenum != null) {
            i = this.phonenum.hashCode();
        }
        return hashCode + i;
    }

    public int type() {
        return 10;
    }

    public String key() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.airportname);
        stringBuilder.append(this.phonenum);
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
        AirportOrAirlineCall airportOrAirlineCall = (AirportOrAirlineCall) obj;
        if (this.airportname == null) {
            if (airportOrAirlineCall.airportname != null) {
                return false;
            }
        } else if (!this.airportname.equals(airportOrAirlineCall.airportname)) {
            return false;
        }
        if (this.phonenum == null) {
            if (airportOrAirlineCall.phonenum != null) {
                return false;
            }
            return true;
        } else if (this.phonenum.equals(airportOrAirlineCall.phonenum)) {
            return true;
        } else {
            return false;
        }
    }
}
