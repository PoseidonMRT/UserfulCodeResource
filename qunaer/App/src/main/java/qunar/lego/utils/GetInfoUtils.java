package qunar.lego.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.iflytek.aiui.AIUIConstant;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

class GetInfoUtils {
    static String DEFAULT_VALUE = "";
    private static final String GLOBALENV_CLASS_STR = "com.mqunar.atomenv.GlobalEnv";
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static String batteryData = DEFAULT_VALUE;
    private static BatteryReceiver batteryReceiver = new BatteryReceiver();
    private static SensorEventListener gyroscopeListener = null;
    private static String gyroscopeSensorData = DEFAULT_VALUE;
    private static SensorEventListener lightListener = null;
    private static String lightSensorData = DEFAULT_VALUE;
    private static Object mGlobalEnv = null;
    private static SensorEventListener orientationListener = null;
    private static String orientationSensorData = DEFAULT_VALUE;
    private static GetInfoUtils sGetInfoUtils = null;
    private static SensorManager sSensorManager;

    final class AnonymousClass2 implements SensorEventListener {
        final /* synthetic */ SensorManager val$sensorManager;

        AnonymousClass2(SensorManager sensorManager) {
            this.val$sensorManager = sensorManager;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            GetInfoUtils.gyroscopeSensorData = sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + "," + System.currentTimeMillis();
            if (GetInfoUtils.gyroscopeListener != null) {
                this.val$sensorManager.unregisterListener(GetInfoUtils.gyroscopeListener);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    final class AnonymousClass3 implements SensorEventListener {
        final /* synthetic */ SensorManager val$sensorManager;

        AnonymousClass3(SensorManager sensorManager) {
            this.val$sensorManager = sensorManager;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            GetInfoUtils.lightSensorData = String.valueOf(sensorEvent.values[0] + "," + System.currentTimeMillis());
            if (GetInfoUtils.lightListener != null) {
                this.val$sensorManager.unregisterListener(GetInfoUtils.lightListener);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    final class AnonymousClass4 implements SensorEventListener {
        final /* synthetic */ SensorManager val$sensorManager;

        AnonymousClass4(SensorManager sensorManager) {
            this.val$sensorManager = sensorManager;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            GetInfoUtils.orientationSensorData = sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + "," + System.currentTimeMillis();
            if (GetInfoUtils.orientationListener != null) {
                this.val$sensorManager.unregisterListener(GetInfoUtils.orientationListener);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    class BatteryReceiver extends BroadcastReceiver {
        private BatteryReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int i = intent.getExtras().getInt("level");
            GetInfoUtils.batteryData = String.valueOf((i * 100) / intent.getExtras().getInt("scale")) + "," + System.currentTimeMillis();
            context.getApplicationContext().unregisterReceiver(GetInfoUtils.batteryReceiver);
        }
    }

    GetInfoUtils() {
    }

    static GetInfoUtils getInstance() {
        if (sGetInfoUtils == null) {
            synchronized (GetInfoUtils.class) {
                if (sGetInfoUtils == null) {
                    sGetInfoUtils = new GetInfoUtils();
                }
            }
        }
        return sGetInfoUtils;
    }

    @TargetApi(4)
    String getWh(Context context) {
        String str = DEFAULT_VALUE;
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels + MapViewConstants.ATTR_X + displayMetrics.heightPixels + MapViewConstants.ATTR_X + displayMetrics.densityDpi;
        } catch (Throwable th) {
            return str;
        }
    }

    String getGid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getGid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getSid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getSid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getPid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getPid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getVid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getVid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getCid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getRCid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getUid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getUid", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getUSid() {
        try {
            getGlobalRnvInstance();
            String str = (String) invokeMethod("getUserId", mGlobalEnv, new Class[0], new Object[0]);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    @TargetApi(3)
    String getADID(Context context) {
        String str = DEFAULT_VALUE;
        try {
            str = Secure.getString(context.getContentResolver(), "android_id");
            if ("9774d56d682e549c".equalsIgnoreCase(str) || TextUtils.isEmpty(str)) {
                str = getUid();
            }
        } catch (Throwable th) {
        }
        return str;
    }

    String getSsn(Context context) {
        try {
            return ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getSimSerialNumber();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    JSONObject getBuild() {
        int i = 0;
        try {
            JSONObject jSONObject = new JSONObject();
            for (Field field : Build.class.getDeclaredFields()) {
                field.setAccessible(true);
                jSONObject.put(field.getName(), field.get(null).toString());
            }
            Field[] declaredFields = VERSION.class.getDeclaredFields();
            int length = declaredFields.length;
            while (i < length) {
                Field field2 = declaredFields[i];
                field2.setAccessible(true);
                jSONObject.put(field2.getName(), field2.get(null).toString());
                i++;
            }
            return jSONObject;
        } catch (Throwable th) {
            return null;
        }
    }

    String getApn(Context context) {
        String str;
        String str2 = DEFAULT_VALUE;
        try {
            Cursor query = context.getApplicationContext().getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, null, null, null);
            if (query != null) {
                query.moveToFirst();
                if (query.getCount() == 0 || query.isAfterLast()) {
                    str = str2;
                } else {
                    str = query.getString(query.getColumnIndex("apn"));
                }
                query.close();
            } else {
                query = context.getApplicationContext().getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                if (query != null) {
                    query.moveToFirst();
                    str = query.getString(query.getColumnIndex(AIUIConstant.USER));
                    query.close();
                } else {
                    str = str2;
                }
            }
        } catch (Exception e) {
            try {
                str = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo();
            } catch (Exception e2) {
                str = DEFAULT_VALUE;
            }
        }
        if (str == null) {
            return DEFAULT_VALUE;
        }
        return str.replace("\"", "");
    }

    String getImsi(Context context) {
        try {
            return ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getSubscriberId();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    String getIp() {
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    Object obj2;
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (inetAddress.isLoopbackAddress() || (inetAddress instanceof Inet6Address)) {
                        obj2 = obj;
                    } else {
                        if (obj == null) {
                            stringBuilder.append(',');
                        }
                        stringBuilder.append(inetAddress.getHostAddress());
                        obj2 = null;
                    }
                    obj = obj2;
                }
            }
            return stringBuilder.toString();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    String getLocation() {
        try {
            Location location = (Location) invokeStaticMethod("qunar.sdk.location.LocationFacade", "getNewestCacheLocation", new Class[0], new Object[0]);
            if (location != null) {
                return location.getLongitude() + "," + location.getLatitude();
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    @TargetApi(9)
    String getMacAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            NetworkInterface byName = NetworkInterface.getByName("eth1");
            if (byName == null) {
                byName = NetworkInterface.getByName("wlan0");
            }
            if (byName == null) {
                return DEFAULT_VALUE;
            }
            int length = byName.getHardwareAddress().length;
            for (int i = 0; i < length; i++) {
                stringBuilder.append(String.format("%02X:", new Object[]{Byte.valueOf(r1[i])}));
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return stringBuilder.toString();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    String getMno(Context context) {
        try {
            return ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getSimOperator();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    String getModel() {
        return Build.MODEL;
    }

    String getNetwork(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return activeNetworkInfo.getTypeName();
            }
        } catch (Throwable th) {
        }
        return DEFAULT_VALUE;
    }

    String getOsVersion() {
        return VERSION.RELEASE;
    }

    String getVolume(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService("audio");
            return String.valueOf(audioManager.getStreamVolume(4) + (((audioManager.getStreamVolume(0) + audioManager.getStreamVolume(1)) + audioManager.getStreamVolume(2)) + audioManager.getStreamVolume(3)));
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    JSONObject getLinkedWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (hasPermission(context, "android.permission.ACCESS_WIFI_STATE") && wifiManager.getWifiState() == 3) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("ssid", connectionInfo.getSSID().replace("\"", ""));
                jSONObject.put("bssid", connectionInfo.getBSSID());
                return jSONObject;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    String getBaseStationId(Context context) {
        try {
            return String.valueOf(((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getCellLocation());
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    String getCpuName() {
        Throwable th;
        String str = null;
        Closeable bufferedReader;
        try {
            String readLine;
            bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            do {
                try {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } while (!readLine.contains("Hardware"));
            str = readLine.split(":")[1];
            safeCloseIO(bufferedReader);
        } catch (Throwable th3) {
            Throwable th4 = th3;
            bufferedReader = null;
            th = th4;
            safeCloseIO(bufferedReader);
            throw th;
        }
        if (str == null) {
        }
    }

    String getImageCount(Context context) {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            Cursor query = context.getApplicationContext().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (query != null) {
                try {
                    String valueOf = String.valueOf(query.getCount());
                    if (query == null) {
                        return valueOf;
                    }
                    query.close();
                    return valueOf;
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return DEFAULT_VALUE;
        } catch (Throwable th3) {
            th = th3;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    String getTel(Context context) {
        try {
            return ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getLine1Number();
        } catch (Throwable th) {
            return DEFAULT_VALUE;
        }
    }

    JSONArray getWifiList(Context context, int i) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (hasPermission(context, "android.permission.ACCESS_WIFI_STATE") && wifiManager.getWifiState() == 3) {
                List scanResults = wifiManager.getScanResults();
                Collections.sort(scanResults, new Comparator<ScanResult>() {
                    public int compare(ScanResult scanResult, ScanResult scanResult2) {
                        return scanResult2.level - scanResult.level;
                    }
                });
                JSONArray jSONArray = new JSONArray();
                int i2 = 0;
                while (i2 < scanResults.size() && i2 < i) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("ssid", ((ScanResult) scanResults.get(i2)).SSID);
                    jSONObject.put("bssid", ((ScanResult) scanResults.get(i2)).BSSID.replace("2", "1").replace("a", "b"));
                    jSONObject.put("level", WifiManager.calculateSignalLevel(((ScanResult) scanResults.get(i2)).level, 1001));
                    jSONArray.put(jSONObject);
                    i2++;
                }
                return jSONArray;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    String getGyroscopeSensorData(Context context) {
        try {
            registerGyroscopeSensor(context);
        } catch (Throwable th) {
        }
        return gyroscopeSensorData;
    }

    String getLightSensorData(Context context) {
        try {
            registerLightSensor(context);
        } catch (Throwable th) {
        }
        return lightSensorData;
    }

    String getOrientationSensorData(Context context) {
        try {
            registerOrientationSensor(context);
        } catch (Throwable th) {
        }
        return orientationSensorData;
    }

    String getBattery(Context context) {
        try {
            registerBattery(context);
        } catch (Throwable th) {
        }
        return batteryData;
    }

    private static SensorManager getSensorManager(Context context) {
        if (sSensorManager == null) {
            sSensorManager = (SensorManager) context.getApplicationContext().getSystemService("sensor");
        }
        return sSensorManager;
    }

    @TargetApi(3)
    private static synchronized void registerGyroscopeSensor(Context context) {
        synchronized (GetInfoUtils.class) {
            SensorManager sensorManager = getSensorManager(context);
            Sensor defaultSensor = sensorManager.getDefaultSensor(4);
            if (gyroscopeListener != null) {
                sensorManager.unregisterListener(gyroscopeListener);
            }
            gyroscopeListener = new AnonymousClass2(sensorManager);
            sensorManager.registerListener(gyroscopeListener, defaultSensor, 3);
        }
    }

    @TargetApi(3)
    private static synchronized void registerLightSensor(Context context) {
        synchronized (GetInfoUtils.class) {
            SensorManager sensorManager = getSensorManager(context);
            Sensor defaultSensor = sensorManager.getDefaultSensor(5);
            if (lightListener != null) {
                sensorManager.unregisterListener(lightListener);
            }
            lightListener = new AnonymousClass3(sensorManager);
            sensorManager.registerListener(lightListener, defaultSensor, 3);
        }
    }

    @TargetApi(3)
    private static synchronized void registerOrientationSensor(Context context) {
        synchronized (GetInfoUtils.class) {
            SensorManager sensorManager = getSensorManager(context);
            Sensor defaultSensor = sensorManager.getDefaultSensor(3);
            if (orientationListener != null) {
                sensorManager.unregisterListener(orientationListener);
            }
            orientationListener = new AnonymousClass4(sensorManager);
            sensorManager.registerListener(orientationListener, defaultSensor, 3);
        }
    }

    private static synchronized void registerBattery(Context context) {
        synchronized (GetInfoUtils.class) {
            context.getApplicationContext().registerReceiver(batteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
    }

    private static GetInfoUtils getGlobalRnvInstance() {
        if (mGlobalEnv == null) {
            synchronized (GetInfoUtils.class) {
                if (mGlobalEnv == null) {
                    mGlobalEnv = invokeStaticMethod(GLOBALENV_CLASS_STR, "getInstance", new Class[0], new Object[0]);
                }
            }
        }
        return sGetInfoUtils;
    }

    static Object invokeStaticMethod(String str, String str2, Class<?>[] clsArr, Object[] objArr) {
        Object obj = null;
        try {
            obj = getMethod(Class.forName(str), str2, clsArr).invoke(null, objArr);
        } catch (Exception e) {
        }
        return obj;
    }

    static Object invokeMethod(String str, Object obj, Class<?>[] clsArr, Object[] objArr) {
        try {
            return getMethod(obj.getClass(), str, clsArr).invoke(obj, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        Method method = null;
        while (cls != null) {
            try {
                method = cls.getDeclaredMethod(str, clsArr);
            } catch (Exception e) {
            }
            if (method != null) {
                method.setAccessible(true);
                break;
            }
            cls = cls.getSuperclass();
        }
        return method;
    }

    private static void safeCloseIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    private static boolean hasPermission(Context context, String str) {
        return context.getApplicationContext().getPackageManager().checkPermission(str, context.getApplicationContext().getPackageName()) == 0;
    }
}
