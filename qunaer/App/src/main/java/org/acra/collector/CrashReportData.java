package org.acra.collector;

import android.support.annotation.NonNull;
import java.util.EnumMap;
import org.acra.ReportField;
import org.acra.util.i;
import org.json.JSONObject;

public final class CrashReportData extends EnumMap<ReportField, String> {
    private static final long serialVersionUID = 4112578634029874840L;

    public CrashReportData() {
        super(ReportField.class);
    }

    public String getProperty(@NonNull ReportField reportField) {
        return (String) super.get(reportField);
    }

    @NonNull
    public JSONObject toJSON() {
        return i.a(this);
    }
}
