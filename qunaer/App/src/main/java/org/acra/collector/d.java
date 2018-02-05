package org.acra.collector;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.support.annotation.NonNull;
import org.acra.ACRA;

final class d {
    @NonNull
    public static String a(@NonNull Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (FeatureInfo featureInfo : context.getPackageManager().getSystemAvailableFeatures()) {
                String str = featureInfo.name;
                if (str != null) {
                    stringBuilder.append(str);
                } else {
                    stringBuilder.append("glEsVersion = ").append(featureInfo.getGlEsVersion());
                }
                stringBuilder.append('\n');
            }
        } catch (Throwable th) {
            ACRA.f.b(ACRA.e, "Couldn't retrieve DeviceFeatures for " + context.getPackageName(), th);
            stringBuilder.append("Could not retrieve data: ");
            stringBuilder.append(th.getMessage());
        }
        return stringBuilder.toString();
    }
}
