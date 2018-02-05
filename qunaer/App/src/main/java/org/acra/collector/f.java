package org.acra.collector;

import android.content.Context;
import android.os.DropBoxManager;
import android.os.DropBoxManager.Entry;
import android.support.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;

final class f {
    private static final String[] a = new String[]{"system_app_anr", "system_app_wtf", "system_app_crash", "system_server_anr", "system_server_wtf", "system_server_crash", "BATTERY_DISCHARGE_INFO", "SYSTEM_RECOVERY_LOG", "SYSTEM_BOOT", "SYSTEM_LAST_KMSG", "APANIC_CONSOLE", "APANIC_THREADS", "SYSTEM_RESTART", "SYSTEM_TOMBSTONE", "data_app_strictmode"};
    private final SimpleDateFormat b = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());

    f() {
    }

    @NonNull
    public String a(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        try {
            DropBoxManager dropBoxManager = (DropBoxManager) context.getSystemService("dropbox");
            Calendar instance = Calendar.getInstance();
            instance.roll(12, -aCRAConfiguration.dropboxCollectionMinutes());
            long timeInMillis = instance.getTimeInMillis();
            this.b.format(instance.getTime());
            List<String> arrayList = new ArrayList();
            if (aCRAConfiguration.includeDropBoxSystemTags()) {
                arrayList.addAll(Arrays.asList(a));
            }
            Collection additionalDropBoxTags = aCRAConfiguration.additionalDropBoxTags();
            if (!additionalDropBoxTags.isEmpty()) {
                arrayList.addAll(additionalDropBoxTags);
            }
            if (arrayList.isEmpty()) {
                return "No tag configured for collection.";
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : arrayList) {
                stringBuilder.append("Tag: ").append(str).append('\n');
                Entry nextEntry = dropBoxManager.getNextEntry(str, timeInMillis);
                if (nextEntry == null) {
                    stringBuilder.append("Nothing.").append('\n');
                } else {
                    while (nextEntry != null) {
                        long timeMillis = nextEntry.getTimeMillis();
                        instance.setTimeInMillis(timeMillis);
                        stringBuilder.append('@').append(this.b.format(instance.getTime())).append('\n');
                        String text = nextEntry.getText(500);
                        if (text != null) {
                            stringBuilder.append("Text: ").append(text).append('\n');
                        } else {
                            stringBuilder.append("Not Text!").append('\n');
                        }
                        nextEntry.close();
                        nextEntry = dropBoxManager.getNextEntry(str, timeMillis);
                    }
                    continue;
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            ACRA.f.b(ACRA.e, "DropBoxManager not available.");
            return "N/A";
        }
    }
}
