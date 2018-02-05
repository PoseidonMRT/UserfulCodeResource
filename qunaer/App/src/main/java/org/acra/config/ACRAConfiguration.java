package org.acra.config;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import java.io.Serializable;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.builder.EndingPrimer;
import org.acra.builder.ReportPrimer;
import org.acra.collections.b;
import org.acra.collections.c;
import org.acra.collections.d;
import org.acra.dialog.a;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.acra.sender.ReportSenderFactory;

public final class ACRAConfiguration implements Serializable {
    private final d<String> additionalDropBoxTags;
    private final d<String> additionalSharedPreferences;
    private final boolean alsoReportToAndroidFramework;
    private final String applicationLogFile;
    private final int applicationLogFileLines;
    private final Class buildConfigClass;
    private final String certificatePath;
    private final String certificateType;
    private final int connectionTimeout;
    private final boolean deleteOldUnsentReportsOnApplicationStart;
    private final boolean deleteUnapprovedReportsOnApplicationStart;
    private final int dropboxCollectionMinutes;
    private final Class<? extends EndingPrimer> endingPrimerClass;
    private final d<String> excludeMatchingSettingsKeys;
    private final d<String> excludeMatchingSharedPreferencesKeys;
    private final String formUri;
    private final String formUriBasicAuthLogin;
    private final String formUriBasicAuthPassword;
    private final c<String, String> httpHeaders;
    private final Method httpMethod;
    private final boolean includeDropBoxSystemTags;
    private final Class<? extends org.acra.security.c> keyStoreFactoryClass;
    private final b<String> logcatArguments;
    private final boolean logcatFilterByPid;
    private final String mailTo;
    private final boolean monitorANR;
    private final boolean monitorAllProcess;
    private final boolean monitorNativeCrash;
    private final String[] monitorProcess;
    private final d<ReportField> reportContent;
    private final Class<? extends a> reportDialogClass;
    private final Class<? extends ReportPrimer> reportPrimerClass;
    private final d<Class<? extends ReportSenderFactory>> reportSenderFactoryClasses;
    private final Type reportType;
    private final ReportingInteractionMode reportingInteractionMode;
    @RawRes
    private final int resCertificate;
    @StringRes
    private final int resDialogCommentPrompt;
    @StringRes
    private final int resDialogEmailPrompt;
    @DrawableRes
    private final int resDialogIcon;
    @StringRes
    private final int resDialogNegativeButtonText;
    @StringRes
    private final int resDialogOkToast;
    @StringRes
    private final int resDialogPositiveButtonText;
    @StringRes
    private final int resDialogText;
    @StyleRes
    private final int resDialogTheme;
    @StringRes
    private final int resDialogTitle;
    @DrawableRes
    private final int resNotifIcon;
    @StringRes
    private final int resNotifText;
    @StringRes
    private final int resNotifTickerText;
    @StringRes
    private final int resNotifTitle;
    @StringRes
    private final int resToastText;
    private final Class<? extends d> retryPolicyClass;
    private final boolean sendFailedDump;
    private final boolean sendReportsInDevMode;
    private final int sharedPreferencesMode;
    private final String sharedPreferencesName;
    private final int socketTimeout;

    ACRAConfiguration(@NonNull b bVar) {
        this.additionalDropBoxTags = new d(bVar.b());
        this.additionalSharedPreferences = new d(bVar.c());
        this.connectionTimeout = bVar.d();
        this.reportContent = new d(bVar.e());
        this.deleteUnapprovedReportsOnApplicationStart = bVar.f();
        this.deleteOldUnsentReportsOnApplicationStart = bVar.g();
        this.dropboxCollectionMinutes = bVar.h();
        this.alsoReportToAndroidFramework = bVar.i();
        this.formUri = bVar.j();
        this.formUriBasicAuthLogin = bVar.p();
        this.formUriBasicAuthPassword = bVar.q();
        this.sendFailedDump = bVar.k();
        this.monitorNativeCrash = bVar.l();
        this.monitorANR = bVar.m();
        this.monitorAllProcess = bVar.n();
        this.monitorProcess = bVar.o();
        this.includeDropBoxSystemTags = bVar.r();
        this.logcatArguments = new b(bVar.s());
        this.mailTo = bVar.t();
        this.reportingInteractionMode = bVar.u();
        this.resDialogIcon = bVar.z();
        this.resDialogPositiveButtonText = bVar.v();
        this.resDialogNegativeButtonText = bVar.w();
        this.resDialogCommentPrompt = bVar.x();
        this.resDialogEmailPrompt = bVar.y();
        this.resDialogOkToast = bVar.A();
        this.resDialogText = bVar.B();
        this.resDialogTitle = bVar.C();
        this.resDialogTheme = bVar.D();
        this.resNotifIcon = bVar.E();
        this.resNotifText = bVar.F();
        this.resNotifTickerText = bVar.G();
        this.resNotifTitle = bVar.H();
        this.resToastText = bVar.I();
        this.sharedPreferencesMode = bVar.J();
        this.sharedPreferencesName = bVar.K();
        this.socketTimeout = bVar.L();
        this.logcatFilterByPid = bVar.M();
        this.sendReportsInDevMode = bVar.N();
        this.excludeMatchingSharedPreferencesKeys = new d(bVar.O());
        this.excludeMatchingSettingsKeys = new d(bVar.P());
        this.buildConfigClass = bVar.Q();
        this.applicationLogFile = bVar.R();
        this.applicationLogFileLines = bVar.S();
        this.reportDialogClass = bVar.T();
        this.reportPrimerClass = bVar.U();
        this.endingPrimerClass = bVar.V();
        this.httpMethod = bVar.W();
        this.httpHeaders = new c(bVar.ad());
        this.reportType = bVar.X();
        this.reportSenderFactoryClasses = new d(bVar.Y());
        this.keyStoreFactoryClass = bVar.Z();
        this.resCertificate = bVar.aa();
        this.certificatePath = bVar.ab();
        this.certificateType = bVar.ac();
        this.retryPolicyClass = bVar.ae();
    }

    @NonNull
    public c<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    @NonNull
    public d<ReportField> getReportFields() {
        return this.reportContent;
    }

    @NonNull
    public d<String> additionalDropBoxTags() {
        return this.additionalDropBoxTags;
    }

    @NonNull
    public d<String> additionalSharedPreferences() {
        return this.additionalSharedPreferences;
    }

    public int connectionTimeout() {
        return this.connectionTimeout;
    }

    public boolean deleteUnapprovedReportsOnApplicationStart() {
        return this.deleteUnapprovedReportsOnApplicationStart;
    }

    public boolean deleteOldUnsentReportsOnApplicationStart() {
        return this.deleteOldUnsentReportsOnApplicationStart;
    }

    public int dropboxCollectionMinutes() {
        return this.dropboxCollectionMinutes;
    }

    public boolean alsoReportToAndroidFramework() {
        return this.alsoReportToAndroidFramework;
    }

    @Nullable
    public String formUri() {
        return this.formUri;
    }

    @Nullable
    public String formUriBasicAuthLogin() {
        return this.formUriBasicAuthLogin;
    }

    @Nullable
    public String formUriBasicAuthPassword() {
        return this.formUriBasicAuthPassword;
    }

    public boolean sendFailedDump() {
        return this.sendFailedDump;
    }

    public boolean monitorNativeCrash() {
        return this.monitorNativeCrash;
    }

    public boolean monitorANR() {
        return this.monitorANR;
    }

    public boolean monitorAllProcess() {
        return this.monitorAllProcess;
    }

    public String[] monitorProcess() {
        return this.monitorProcess;
    }

    public boolean includeDropBoxSystemTags() {
        return this.includeDropBoxSystemTags;
    }

    @NonNull
    public b<String> logcatArguments() {
        return this.logcatArguments;
    }

    @Nullable
    public String mailTo() {
        return this.mailTo;
    }

    @NonNull
    public ReportingInteractionMode mode() {
        return this.reportingInteractionMode;
    }

    @StringRes
    public int resDialogPositiveButtonText() {
        return this.resDialogPositiveButtonText;
    }

    @StringRes
    public int resDialogNegativeButtonText() {
        return this.resDialogNegativeButtonText;
    }

    @StringRes
    public int resDialogCommentPrompt() {
        return this.resDialogCommentPrompt;
    }

    @StringRes
    public int resDialogEmailPrompt() {
        return this.resDialogEmailPrompt;
    }

    @DrawableRes
    public int resDialogIcon() {
        return this.resDialogIcon;
    }

    @StringRes
    public int resDialogOkToast() {
        return this.resDialogOkToast;
    }

    @StringRes
    public int resDialogText() {
        return this.resDialogText;
    }

    @StringRes
    public int resDialogTitle() {
        return this.resDialogTitle;
    }

    @StyleRes
    public int resDialogTheme() {
        return this.resDialogTheme;
    }

    @DrawableRes
    public int resNotifIcon() {
        return this.resNotifIcon;
    }

    @StringRes
    public int resNotifText() {
        return this.resNotifText;
    }

    @StringRes
    public int resNotifTickerText() {
        return this.resNotifTickerText;
    }

    @StringRes
    public int resNotifTitle() {
        return this.resNotifTitle;
    }

    @StringRes
    public int resToastText() {
        return this.resToastText;
    }

    public int sharedPreferencesMode() {
        return this.sharedPreferencesMode;
    }

    @NonNull
    public String sharedPreferencesName() {
        return this.sharedPreferencesName;
    }

    public int socketTimeout() {
        return this.socketTimeout;
    }

    public boolean logcatFilterByPid() {
        return this.logcatFilterByPid;
    }

    public boolean sendReportsInDevMode() {
        return this.sendReportsInDevMode;
    }

    @NonNull
    public d<String> excludeMatchingSharedPreferencesKeys() {
        return this.excludeMatchingSharedPreferencesKeys;
    }

    @NonNull
    public d<String> excludeMatchingSettingsKeys() {
        return this.excludeMatchingSettingsKeys;
    }

    @NonNull
    public Class buildConfigClass() {
        return this.buildConfigClass;
    }

    @NonNull
    public String applicationLogFile() {
        return this.applicationLogFile;
    }

    public int applicationLogFileLines() {
        return this.applicationLogFileLines;
    }

    @NonNull
    public Class<? extends a> reportDialogClass() {
        return this.reportDialogClass;
    }

    @NonNull
    public Class<? extends ReportPrimer> reportPrimerClass() {
        return this.reportPrimerClass;
    }

    @NonNull
    public Class<? extends EndingPrimer> endingPrimerClass() {
        return this.endingPrimerClass;
    }

    @NonNull
    public Method httpMethod() {
        return this.httpMethod;
    }

    @NonNull
    public Type reportType() {
        return this.reportType;
    }

    @NonNull
    public d<Class<? extends ReportSenderFactory>> reportSenderFactoryClasses() {
        return this.reportSenderFactoryClasses;
    }

    @NonNull
    public Class<? extends org.acra.security.c> keyStoreFactoryClass() {
        return this.keyStoreFactoryClass;
    }

    @RawRes
    public int resCertificate() {
        return this.resCertificate;
    }

    @NonNull
    public String certificatePath() {
        return this.certificatePath;
    }

    @NonNull
    public String certificateType() {
        return this.certificateType;
    }

    @NonNull
    public Class<? extends d> retryPolicyClass() {
        return this.retryPolicyClass;
    }
}
