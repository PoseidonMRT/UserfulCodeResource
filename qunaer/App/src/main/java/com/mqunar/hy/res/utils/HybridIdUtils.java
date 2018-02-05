package com.mqunar.hy.res.utils;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class HybridIdUtils {
    private static final Map<String, String> map = new HashMap();

    public static String getHybrididByUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (String) map.get(UriCodec.getUrlWithOutQueryAndHash(str));
    }

    static {
        map.put("http://touch.qunar.com/hy/flight/bargainflight", "flight_tejia");
        map.put("http://touch.qunar.com/hy/flight/airportservice", "flight_traffic");
        map.put("http://food.qunar.com/app", "group_food");
        map.put("http://hotel.qunar.com/global/mob/index", "hotel_internal");
        map.put("http://m.huichang.qunar.com/app.html", "hotel_mice_user");
        map.put("http://review.qunar.com/mall/touch/indexV2.htm", "hotel_ugc_mall");
        map.put("http://operation.qunar.com/syn/mpHotelVacation", "mob_activity");
        map.put("http://bnb.qunar.com/touch/newbnbtouch.jsp", "mob_bnb");
        map.put("http://searchtouch.qunar.com/queryData/hyIndex.do", "mob_destination");
        map.put("http://hy.touch.qunar.com/mobile-hotel-hybrid/prd/pages/panorama-view.html", "mob_hotel");
        map.put("http://ochat.qunar.com/ochat-hybrid/prd/pages/profile.html", "mob_im");
        map.put("http://order.qunar.com/mordercenter/index", "mob_ordercenter");
        map.put("https://user.qunar.com/mobile/security/securityQuestions.jsp", "mob_uc");
        map.put("https://pay.qunar.com/mobile/h5/personalcenter/myaccount/index.htm", "mob_uc");
        map.put("https://pay.qunar.com/mobile/h5/personalcenter/myaccount/myinfos.htm", "mob_uc");
        map.put("http://user.qunar.com/mobile/feedback/feedback.jsp", "mob_uc");
        map.put("https://pay.qunar.com/mobile/h5/personalcenter/myaccount/mysettings.htm", "mob_uc");
        map.put("https://pay.qunar.com/mobile/h5/personalcenter/myaccount/mysafe.htm", "mob_uc");
        map.put("http://waimai.qunar.com/mobile.jsp", "mob_waimai");
        map.put("https://pay.qunar.com/mobile/h5/asset/myWalletIndex.htm", "mob_wallet");
        map.put("https://pay.qunar.com/m/member/asset/balance/withdraw.html", "mob_wallet");
        map.put("https://pay.qunar.com/m/member/asset/balance/onine.html", "mob_wallet");
        map.put("http://touch.travel.qunar.com/travelorder/", "travel_trade");
        map.put("http://touch.dujia.qunar.com/abroad/index.qunar", "vacation_abroad");
        map.put("http://touch.dujia.qunar.com/abroad/theme_list.qunar", "vacation_abroad");
        map.put("http://touch.dujia.qunar.com/nindex.qunar", "vacation_home");
    }
}
