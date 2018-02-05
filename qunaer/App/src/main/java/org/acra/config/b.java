package org.acra.config;

import android.app.Application;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.builder.EndingPrimer;
import org.acra.builder.ReportPrimer;
import org.acra.dialog.CrashReportDialog;
import org.acra.dialog.a;
import org.acra.security.c;
import org.acra.security.e;
import org.acra.sender.DefaultReportSenderFactory;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.acra.sender.ReportSenderFactory;
import qunar.sdk.mapapi.utils.MapConstant.QUNAR_GPS_FIELD;

public final class b {
    @StringRes
    private Integer A;
    @StringRes
    private Integer B;
    @StringRes
    private Integer C;
    @DrawableRes
    private Integer D;
    @StringRes
    private Integer E;
    @StringRes
    private Integer F;
    @StringRes
    private Integer G;
    @StyleRes
    private Integer H;
    @DrawableRes
    private Integer I;
    @StringRes
    private Integer J;
    @StringRes
    private Integer K;
    @StringRes
    private Integer L;
    @StringRes
    private Integer M;
    private Integer N;
    private String O;
    private Integer P;
    private Boolean Q;
    private Boolean R;
    private String[] S;
    private String[] T;
    private Class U;
    private String V;
    private Integer W;
    private Method X;
    private Type Y;
    private final Map<String, String> Z = new HashMap();
    @Nullable
    private final Class<? extends Annotation> a;
    private Class<? extends c> aa;
    private Class<? extends ReportSenderFactory>[] ab;
    @RawRes
    private Integer ac;
    private String ad;
    private String ae;
    private Class<? extends d> af;
    private String[] b;
    private String[] c;
    private Integer d;
    private ReportField[] e;
    private final Map<ReportField, Boolean> f = new EnumMap(ReportField.class);
    private Boolean g;
    private Boolean h;
    private Integer i;
    private Boolean j;
    private String k;
    private String l;
    private String m;
    private Boolean n;
    private Boolean o;
    private Boolean p;
    private Boolean q;
    private Boolean r;
    private String[] s;
    private String[] t;
    private String u;
    private ReportingInteractionMode v;
    private Class<? extends a> w;
    private Class<? extends ReportPrimer> x;
    private Class<? extends EndingPrimer> y;
    @StringRes
    private Integer z;

    public b(@NonNull Application application) {
        ReportsCrashes reportsCrashes = (ReportsCrashes) application.getClass().getAnnotation(ReportsCrashes.class);
        if (reportsCrashes != null) {
            this.a = reportsCrashes.annotationType();
            this.b = reportsCrashes.additionalDropBoxTags();
            this.c = reportsCrashes.additionalSharedPreferences();
            this.d = Integer.valueOf(reportsCrashes.connectionTimeout());
            this.e = reportsCrashes.customReportContent();
            this.g = Boolean.valueOf(reportsCrashes.deleteUnapprovedReportsOnApplicationStart());
            this.h = Boolean.valueOf(reportsCrashes.deleteOldUnsentReportsOnApplicationStart());
            this.i = Integer.valueOf(reportsCrashes.dropboxCollectionMinutes());
            this.j = Boolean.valueOf(reportsCrashes.alsoReportToAndroidFramework());
            this.k = reportsCrashes.formUri();
            this.l = reportsCrashes.formUriBasicAuthLogin();
            this.m = reportsCrashes.formUriBasicAuthPassword();
            this.n = Boolean.valueOf(reportsCrashes.includeDropBoxSystemTags());
            this.t = reportsCrashes.logcatArguments();
            this.u = reportsCrashes.mailTo();
            this.v = reportsCrashes.mode();
            this.D = Integer.valueOf(reportsCrashes.resDialogIcon());
            this.z = Integer.valueOf(reportsCrashes.resDialogPositiveButtonText());
            this.A = Integer.valueOf(reportsCrashes.resDialogNegativeButtonText());
            this.B = Integer.valueOf(reportsCrashes.resDialogCommentPrompt());
            this.C = Integer.valueOf(reportsCrashes.resDialogEmailPrompt());
            this.E = Integer.valueOf(reportsCrashes.resDialogOkToast());
            this.F = Integer.valueOf(reportsCrashes.resDialogText());
            this.G = Integer.valueOf(reportsCrashes.resDialogTitle());
            this.H = Integer.valueOf(reportsCrashes.resDialogTheme());
            this.I = Integer.valueOf(reportsCrashes.resNotifIcon());
            this.J = Integer.valueOf(reportsCrashes.resNotifText());
            this.K = Integer.valueOf(reportsCrashes.resNotifTickerText());
            this.L = Integer.valueOf(reportsCrashes.resNotifTitle());
            this.M = Integer.valueOf(reportsCrashes.resToastText());
            this.N = Integer.valueOf(reportsCrashes.sharedPreferencesMode());
            this.O = reportsCrashes.sharedPreferencesName();
            this.P = Integer.valueOf(reportsCrashes.socketTimeout());
            this.Q = Boolean.valueOf(reportsCrashes.logcatFilterByPid());
            this.R = Boolean.valueOf(reportsCrashes.sendReportsInDevMode());
            this.S = reportsCrashes.excludeMatchingSharedPreferencesKeys();
            this.T = reportsCrashes.excludeMatchingSettingsKeys();
            this.U = reportsCrashes.buildConfigClass();
            this.V = reportsCrashes.applicationLogFile();
            this.W = Integer.valueOf(reportsCrashes.applicationLogFileLines());
            this.w = reportsCrashes.reportDialogClass();
            this.x = reportsCrashes.reportPrimerClass();
            this.y = reportsCrashes.endingPrimerClass();
            this.X = reportsCrashes.httpMethod();
            this.Y = reportsCrashes.reportType();
            this.ab = reportsCrashes.reportSenderFactoryClasses();
            this.aa = reportsCrashes.keyStoreFactoryClass();
            this.ac = Integer.valueOf(reportsCrashes.resCertificate());
            this.ad = reportsCrashes.certificatePath();
            this.ae = reportsCrashes.certificateType();
            this.af = reportsCrashes.retryPolicyClass();
            this.o = Boolean.valueOf(reportsCrashes.sendFailedDump());
            this.p = Boolean.valueOf(reportsCrashes.monitorNativeCrash());
            this.q = Boolean.valueOf(reportsCrashes.monitorANR());
            this.r = Boolean.valueOf(reportsCrashes.monitorAllProcess());
            this.s = reportsCrashes.monitorProcess();
            return;
        }
        this.a = null;
    }

    @NonNull
    public ACRAConfiguration a() {
        switch (u()) {
            case TOAST:
                if (I() == 0) {
                    throw new a("TOAST mode: you have to define the resToastText parameter in your application @ReportsCrashes() annotation.");
                }
                break;
            case NOTIFICATION:
                if (G() == 0 || H() == 0 || F() == 0) {
                    throw new a("NOTIFICATION mode: you have to define at least the resNotifTickerText, resNotifTitle, resNotifText parameters in your application @ReportsCrashes() annotation.");
                } else if (CrashReportDialog.class.equals(T()) && B() == 0) {
                    throw new a("NOTIFICATION mode: using the (default) CrashReportDialog requires you have to define the resDialogText parameter in your application @ReportsCrashes() annotation.");
                }
                break;
            case DIALOG:
                if (CrashReportDialog.class.equals(T()) && B() == 0) {
                    throw new a("DIALOG mode: using the (default) CrashReportDialog requires you to define the resDialogText parameter in your application @ReportsCrashes() annotation.");
                }
        }
        return new ACRAConfiguration(this);
    }

    @NonNull
    String[] b() {
        if (this.b != null) {
            return this.b;
        }
        return new String[0];
    }

    @NonNull
    String[] c() {
        if (this.c != null) {
            return this.c;
        }
        return new String[0];
    }

    int d() {
        if (this.d != null) {
            return this.d.intValue();
        }
        return 5000;
    }

    @NonNull
    Set<ReportField> e() {
        Set<ReportField> hashSet = new HashSet();
        if (this.e != null && this.e.length != 0) {
            ACRA.f.b(ACRA.e, "Using custom Report Fields");
            hashSet.addAll(Arrays.asList(this.e));
        } else if (this.u == null || "".equals(this.u)) {
            ACRA.f.b(ACRA.e, "Using default Report Fields");
            hashSet.addAll(Arrays.asList(org.acra.a.c));
        } else {
            ACRA.f.b(ACRA.e, "Using default Mail Report Fields");
            hashSet.addAll(Arrays.asList(org.acra.a.b));
        }
        for (Entry entry : this.f.entrySet()) {
            if (((Boolean) entry.getValue()).booleanValue()) {
                hashSet.add(entry.getKey());
            } else {
                hashSet.remove(entry.getKey());
            }
        }
        return hashSet;
    }

    boolean f() {
        if (this.g != null) {
            return this.g.booleanValue();
        }
        return true;
    }

    boolean g() {
        if (this.h != null) {
            return this.h.booleanValue();
        }
        return true;
    }

    int h() {
        if (this.i != null) {
            return this.i.intValue();
        }
        return 5;
    }

    boolean i() {
        if (this.j != null) {
            return this.j.booleanValue();
        }
        return false;
    }

    @NonNull
    String j() {
        if (this.k != null) {
            return this.k;
        }
        return "";
    }

    boolean k() {
        if (this.o != null) {
            return this.o.booleanValue();
        }
        return true;
    }

    boolean l() {
        if (this.p != null) {
            return this.p.booleanValue();
        }
        return false;
    }

    boolean m() {
        if (this.q != null) {
            return this.q.booleanValue();
        }
        return false;
    }

    boolean n() {
        if (this.r != null) {
            return this.r.booleanValue();
        }
        return false;
    }

    String[] o() {
        return this.s;
    }

    @NonNull
    String p() {
        if (this.l != null) {
            return this.l;
        }
        return "ACRA-NULL-STRING";
    }

    @NonNull
    String q() {
        if (this.m != null) {
            return this.m;
        }
        return "ACRA-NULL-STRING";
    }

    boolean r() {
        if (this.n != null) {
            return this.n.booleanValue();
        }
        return false;
    }

    @NonNull
    String[] s() {
        if (this.t != null) {
            return this.t;
        }
        return new String[]{"-t", Integer.toString(100), "-v", QUNAR_GPS_FIELD.TIME_FIELD};
    }

    @NonNull
    String t() {
        if (this.u != null) {
            return this.u;
        }
        return "";
    }

    @NonNull
    ReportingInteractionMode u() {
        if (this.v != null) {
            return this.v;
        }
        return ReportingInteractionMode.SILENT;
    }

    @StringRes
    public int v() {
        if (this.z != null) {
            return this.z.intValue();
        }
        return 17039370;
    }

    @StringRes
    int w() {
        if (this.A != null) {
            return this.A.intValue();
        }
        return 17039360;
    }

    @StringRes
    int x() {
        if (this.B != null) {
            return this.B.intValue();
        }
        return 0;
    }

    @StringRes
    int y() {
        if (this.C != null) {
            return this.C.intValue();
        }
        return 0;
    }

    @DrawableRes
    int z() {
        if (this.D != null) {
            return this.D.intValue();
        }
        return 17301543;
    }

    @StringRes
    int A() {
        if (this.E != null) {
            return this.E.intValue();
        }
        return 0;
    }

    @StringRes
    int B() {
        if (this.F != null) {
            return this.F.intValue();
        }
        return 0;
    }

    @StringRes
    int C() {
        if (this.G != null) {
            return this.G.intValue();
        }
        return 0;
    }

    @StyleRes
    int D() {
        if (this.H != null) {
            return this.H.intValue();
        }
        return 0;
    }

    @DrawableRes
    int E() {
        if (this.I != null) {
            return this.I.intValue();
        }
        return 17301624;
    }

    @StringRes
    int F() {
        if (this.J != null) {
            return this.J.intValue();
        }
        return 0;
    }

    @StringRes
    int G() {
        if (this.K != null) {
            return this.K.intValue();
        }
        return 0;
    }

    @StringRes
    int H() {
        if (this.L != null) {
            return this.L.intValue();
        }
        return 0;
    }

    @StringRes
    int I() {
        if (this.M != null) {
            return this.M.intValue();
        }
        return 0;
    }

    int J() {
        if (this.N != null) {
            return this.N.intValue();
        }
        return 0;
    }

    @NonNull
    String K() {
        if (this.O != null) {
            return this.O;
        }
        return "";
    }

    int L() {
        if (this.P != null) {
            return this.P.intValue();
        }
        return 20000;
    }

    boolean M() {
        if (this.Q != null) {
            return this.Q.booleanValue();
        }
        return false;
    }

    boolean N() {
        if (this.R != null) {
            return this.R.booleanValue();
        }
        return true;
    }

    @NonNull
    String[] O() {
        if (this.S != null) {
            return this.S;
        }
        return new String[0];
    }

    @NonNull
    String[] P() {
        if (this.T != null) {
            return this.T;
        }
        return new String[0];
    }

    @NonNull
    Class Q() {
        if (this.U != null) {
            return this.U;
        }
        return Object.class;
    }

    @NonNull
    String R() {
        if (this.V != null) {
            return this.V;
        }
        return "";
    }

    int S() {
        if (this.W != null) {
            return this.W.intValue();
        }
        return 100;
    }

    @NonNull
    Class<? extends a> T() {
        if (this.w != null) {
            return this.w;
        }
        return CrashReportDialog.class;
    }

    @NonNull
    Class<? extends ReportPrimer> U() {
        if (this.x != null) {
            return this.x;
        }
        return org.acra.builder.b.class;
    }

    @NonNull
    Class<? extends EndingPrimer> V() {
        if (this.y != null) {
            return this.y;
        }
        return org.acra.builder.a.class;
    }

    @NonNull
    Method W() {
        if (this.X != null) {
            return this.X;
        }
        return Method.POST;
    }

    @NonNull
    Type X() {
        if (this.Y != null) {
            return this.Y;
        }
        return Type.FORM;
    }

    @NonNull
    Class<? extends ReportSenderFactory>[] Y() {
        if (this.ab != null) {
            return this.ab;
        }
        return new Class[]{DefaultReportSenderFactory.class};
    }

    @NonNull
    Class<? extends c> Z() {
        if (this.aa != null) {
            return this.aa;
        }
        return e.class;
    }

    @RawRes
    int aa() {
        if (this.ac != null) {
            return this.ac.intValue();
        }
        return 0;
    }

    @NonNull
    String ab() {
        if (this.ad != null) {
            return this.ad;
        }
        return "";
    }

    @NonNull
    String ac() {
        if (this.ae != null) {
            return this.ae;
        }
        return "X.509";
    }

    @NonNull
    Map<String, String> ad() {
        return this.Z;
    }

    @NonNull
    Class<? extends d> ae() {
        if (this.af != null) {
            return this.af;
        }
        return c.class;
    }
}
