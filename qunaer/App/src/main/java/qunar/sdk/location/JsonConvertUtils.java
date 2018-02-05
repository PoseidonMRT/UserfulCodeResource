package qunar.sdk.location;

import com.alibaba.fastjson.JSON;
import java.util.List;

public class JsonConvertUtils {
    public static String changeArrayDateToJson(List<QLocation> list) {
        String str = null;
        if (!(list == null || list.size() == 0)) {
            try {
                JSON.toJSONString(list);
                str = JSON.toJSONString(list);
            } catch (Exception e) {
            }
        }
        return str;
    }

    public static List<QLocation> changeJsonToArray(String str) {
        try {
            return JSON.parseArray(str, QLocation.class);
        } catch (Exception e) {
            return null;
        }
    }
}
