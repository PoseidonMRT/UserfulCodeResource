package qunar.sdk.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import qunar.sdk.mapapi.utils.MapConstant.QUNAR_GPS_FIELD;

public class GPSStorageUtils {
    public static void cach5GPS(Context context, QLocation qLocation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(QUNAR_GPS_FIELD.SHARE_NAME, 0);
        List changeJsonToArray = JsonConvertUtils.changeJsonToArray(sharedPreferences.getString(QUNAR_GPS_FIELD.CACHE_NAME, ""));
        changeJsonToArray.add(qLocation);
        String changeArrayDateToJson = JsonConvertUtils.changeArrayDateToJson(changeJsonToArray);
        Editor edit = sharedPreferences.edit();
        edit.putString(QUNAR_GPS_FIELD.CACHE_NAME, changeArrayDateToJson);
        edit.commit();
    }

    public static void cach5GPS(Context context, ArrayList<QLocation> arrayList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(QUNAR_GPS_FIELD.SHARE_NAME, 0);
        List changeJsonToArray = JsonConvertUtils.changeJsonToArray(sharedPreferences.getString(QUNAR_GPS_FIELD.CACHE_NAME, ""));
        if (changeJsonToArray != null && !changeJsonToArray.isEmpty()) {
            changeJsonToArray.addAll(arrayList);
            String changeArrayDateToJson = JsonConvertUtils.changeArrayDateToJson(changeJsonToArray);
            Editor edit = sharedPreferences.edit();
            edit.putString(QUNAR_GPS_FIELD.CACHE_NAME, changeArrayDateToJson);
            edit.commit();
        }
    }

    public static ArrayList<QLocation> getFitGPSS(Context context, long j) {
        ArrayList<QLocation> arrayList = new ArrayList();
        List<QLocation> changeJsonToArray = JsonConvertUtils.changeJsonToArray(context.getSharedPreferences(QUNAR_GPS_FIELD.SHARE_NAME, 0).getString(QUNAR_GPS_FIELD.CACHE_NAME, ""));
        if (!(changeJsonToArray == null || changeJsonToArray.isEmpty())) {
            for (QLocation qLocation : changeJsonToArray) {
                if (new Date().getTime() - qLocation.getTime() < j) {
                    arrayList.add(qLocation);
                }
            }
        }
        return arrayList;
    }

    public static QLocation getFitGPS(Context context, long j) {
        List changeJsonToArray = JsonConvertUtils.changeJsonToArray(context.getSharedPreferences(QUNAR_GPS_FIELD.SHARE_NAME, 0).getString(QUNAR_GPS_FIELD.CACHE_NAME, ""));
        if (!(changeJsonToArray == null || changeJsonToArray.isEmpty())) {
            QLocation qLocation = (QLocation) changeJsonToArray.get(changeJsonToArray.size() - 1);
            if ((new Date().getTime() - qLocation.getTime()) / 1000 < j) {
                return qLocation;
            }
        }
        return null;
    }
}
