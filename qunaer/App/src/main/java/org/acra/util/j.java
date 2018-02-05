package org.acra.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import org.acra.ACRA;

public final class j {
    public static long a() {
        long blockSizeLong;
        long availableBlocksLong;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        if (VERSION.SDK_INT >= 18) {
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();
        } else {
            blockSizeLong = (long) statFs.getBlockSize();
            availableBlocksLong = (long) statFs.getAvailableBlocks();
        }
        return availableBlocksLong * blockSizeLong;
    }

    public static long b() {
        long blockSizeLong;
        long blockCountLong;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        if (VERSION.SDK_INT >= 18) {
            blockSizeLong = statFs.getBlockSizeLong();
            blockCountLong = statFs.getBlockCountLong();
        } else {
            blockSizeLong = (long) statFs.getBlockSize();
            blockCountLong = (long) statFs.getBlockCount();
        }
        return blockCountLong * blockSizeLong;
    }

    @Nullable
    public static String a(@NonNull Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "Couldn't retrieve DeviceId for : " + context.getPackageName(), e);
            return null;
        }
    }

    @NonNull
    public static String b(@NonNull Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            return filesDir.getAbsolutePath();
        }
        ACRA.f.d(ACRA.e, "Couldn't retrieve ApplicationFilePath for : " + context.getPackageName());
        return "Couldn't retrieve ApplicationFilePath";
    }

    @NonNull
    public static String c() {
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    Object obj2;
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (inetAddress.isLoopbackAddress()) {
                        obj2 = obj;
                    } else {
                        if (obj == null) {
                            stringBuilder.append('\n');
                        }
                        stringBuilder.append(inetAddress.getHostAddress());
                        obj2 = null;
                    }
                    obj = obj2;
                }
            }
        } catch (SocketException e) {
            ACRA.f.d(ACRA.e, e.toString());
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String a(@NonNull Calendar calendar) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.ENGLISH).format(Long.valueOf(calendar.getTimeInMillis()));
    }
}
