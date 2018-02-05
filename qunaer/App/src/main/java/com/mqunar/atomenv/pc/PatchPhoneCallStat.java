package com.mqunar.atomenv.pc;

import com.mqunar.atomenv.pc.model.AirportOrAirlineCall;
import com.mqunar.atomenv.pc.model.FlightAgentCall;
import com.mqunar.atomenv.pc.model.HotelAgentCall;
import com.mqunar.atomenv.pc.model.HotelCall;
import com.mqunar.atomenv.pc.model.HotelCallP;
import com.mqunar.atomenv.pc.model.TTSOrderConfirmCall;
import com.mqunar.atomenv.pc.model.TTSOrderDetailCall;

@Deprecated
public class PatchPhoneCallStat {
    @Deprecated
    public static void AddFlightAgentCall(String str, String str2, String str3, String str4, String str5) {
        PhoneCallStat.getInstance().add(new FlightAgentCall(str, str2, str3, str4, str5));
    }

    @Deprecated
    public static void AddFlightAirlineCall(String str, String str2, String str3) {
        PhoneCallStat.getInstance().add(new AirportOrAirlineCall(str3, "", str, str2));
    }

    @Deprecated
    public static void AddFlightAirportCall(String str, String str2, String str3, String str4) {
        PhoneCallStat.getInstance().add(new AirportOrAirlineCall(str3, str4, str, str2));
    }

    @Deprecated
    public static void AddHotelAgentCall(String str, String str2, String str3, String str4, String str5) {
        PhoneCallStat.getInstance().add(new HotelAgentCall(str, str2, str3, str4, str5));
    }

    @Deprecated
    public static void AddHotelCall(String str, String str2, String str3) {
        PhoneCallStat.getInstance().add(new HotelCall(str, str2, str3));
    }

    @Deprecated
    public static void AddHotelCallP(String str, String str2, String str3) {
        PhoneCallStat.getInstance().add(new HotelCallP(str, str2, str3));
    }

    @Deprecated
    public static void AddTTSOrderConfirm(String str, String str2, String str3) {
        PhoneCallStat.getInstance().add(new TTSOrderConfirmCall(str, str2, str3));
    }

    @Deprecated
    public static void AddTTSOrderDetailCall(String str, String str2, String str3) {
        PhoneCallStat.getInstance().add(new TTSOrderDetailCall(str, str2, str3));
    }
}
