package com.mapquest.android.intent;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import com.mapquest.android.maps.GeoPoint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class MapQuestAppLauncher {
    static final String ACE_MARKET_INTENT = "market://details?id=com.mapquest.android.ace";
    private static final String INTENT_FILE_DIR = "mapquest";
    private static final String INTENT_FILE_NAME = "mqtmp.txt";
    private static final String LOG_TAG = MapQuestAppLauncher.class.getName();

    final class AnonymousClass1 implements OnClickListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$intent;

        AnonymousClass1(Context context, String str) {
            this.val$context = context;
            this.val$intent = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            MapQuestAppLauncher.writeIntentToDevice(this.val$context, this.val$intent);
            this.val$context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MapQuestAppLauncher.ACE_MARKET_INTENT)));
        }
    }

    public enum MapLayer {
        TRAFFIC("traffic");
        
        private String value;

        private MapLayer(String str) {
            this.value = str;
        }

        public String value() {
            return this.value;
        }
    }

    public enum MapType {
        MAP("map"),
        HYB("hyb"),
        SAT("sat");
        
        private String value;

        private MapType(String str) {
            this.value = str;
        }

        public String value() {
            return this.value;
        }
    }

    public static void launchNavigation(Context context, GeoPoint geoPoint, String str) {
        launchNavigation(context, geoPoint, str, true);
    }

    public static void launchNavigation(Context context, GeoPoint geoPoint, String str, boolean z) {
        launchNavigation(context, geoPoint, str, (MapType) null, (MapLayer) null, z, true);
    }

    public static void launchNavigation(Context context, GeoPoint geoPoint, String str, MapType mapType, MapLayer mapLayer) {
        launchNavigation(context, geoPoint, str, mapType, mapLayer, true, true);
    }

    public static void launchNavigation(Context context, GeoPoint geoPoint, String str, MapType mapType, MapLayer mapLayer, boolean z, boolean z2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mapquest://navigate?q=");
        stringBuilder.append(geoPoint.getLatitude()).append(",").append(geoPoint.getLongitude());
        if (str != null && str.length() > 0) {
            stringBuilder.append("(").append(encode(str)).append(")");
        }
        if (mapType != null) {
            stringBuilder.append("&maptype=").append(mapType.value());
        }
        if (mapLayer != null) {
            stringBuilder.append("&layer=").append(mapLayer.value());
        }
        stringBuilder.append("&autostart=").append(Boolean.valueOf(z2).toString());
        handleIntent(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())), context, stringBuilder.toString(), z);
    }

    static void handleIntent(Intent intent, Context context, String str, boolean z) {
        List queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
            writeIntentToDevice(context, str);
            if (z) {
                showDownloadDialog(context, str);
                return;
            } else {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(ACE_MARKET_INTENT)));
                return;
            }
        }
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    static void showDownloadDialog(Context context, String str) {
        Builder builder = new Builder(context);
        builder.setTitle("Download Alert");
        builder.setMessage("You do not have the MapQuest Navigation App installed.  Would you like to download it?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new AnonymousClass1(context, str));
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create();
        builder.show();
    }

    static void writeIntentToDevice(Context context, String str) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + INTENT_FILE_DIR);
        if (file.exists() || !file.mkdirs()) {
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, INTENT_FILE_NAME));
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String encode(String str) {
        String str2 = null;
        try {
            str2 = URLEncoder.encode(str, "UTF8");
        } catch (UnsupportedEncodingException e) {
        }
        return str2;
    }

    static String getAceMarketIntent() {
        return ACE_MARKET_INTENT;
    }
}
