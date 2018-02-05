package com.mqunar.atomenv.pc.model;

import com.mqunar.atomenv.pc.AbsCallStat;

public class TTSOrderConfirmCall extends AbsCallStat {
    public static final int CALLSTAT_TTS_ORDER_CONFIRM = 7;
    String calltime = "";
    String orderid = "";
    String phone = "";

    public TTSOrderConfirmCall(String str, String str2, String str3) {
        this.orderid = str;
        this.phone = str2;
        this.calltime = str3;
    }

    public String toCallString() {
        return "" + type() + "|#|" + this.orderid + "|#|" + this.phone + "|#|" + this.calltime + "|#|" + count();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.orderid == null ? 0 : this.orderid.hashCode()) + 31) * 31;
        if (this.phone != null) {
            i = this.phone.hashCode();
        }
        return hashCode + i;
    }

    public int type() {
        return 7;
    }

    public String key() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.orderid);
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
        TTSOrderConfirmCall tTSOrderConfirmCall = (TTSOrderConfirmCall) obj;
        if (this.orderid == null) {
            if (tTSOrderConfirmCall.orderid != null) {
                return false;
            }
        } else if (!this.orderid.equals(tTSOrderConfirmCall.orderid)) {
            return false;
        }
        if (this.phone == null) {
            if (tTSOrderConfirmCall.phone != null) {
                return false;
            }
            return true;
        } else if (this.phone.equals(tTSOrderConfirmCall.phone)) {
            return true;
        } else {
            return false;
        }
    }
}
