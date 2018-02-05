package org.acra.annotation;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.builder.EndingPrimer;
import org.acra.builder.ReportPrimer;
import org.acra.builder.a;
import org.acra.builder.b;
import org.acra.config.d;
import org.acra.dialog.CrashReportDialog;
import org.acra.security.c;
import org.acra.security.e;
import org.acra.sender.DefaultReportSenderFactory;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.acra.sender.ReportSenderFactory;

@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportsCrashes {
    @NonNull
    String[] additionalDropBoxTags() default {};

    @NonNull
    String[] additionalSharedPreferences() default {};

    boolean alsoReportToAndroidFramework() default false;

    @NonNull
    String applicationLogFile() default "";

    int applicationLogFileLines() default 100;

    @NonNull
    Class buildConfigClass() default Object.class;

    @NonNull
    String certificatePath() default "";

    @NonNull
    String certificateType() default "X.509";

    int connectionTimeout() default 5000;

    @NonNull
    ReportField[] customReportContent() default {};

    boolean deleteOldUnsentReportsOnApplicationStart() default true;

    boolean deleteUnapprovedReportsOnApplicationStart() default true;

    int dropboxCollectionMinutes() default 5;

    @NonNull
    Class<? extends EndingPrimer> endingPrimerClass() default a.class;

    @NonNull
    String[] excludeMatchingSettingsKeys() default {};

    @NonNull
    String[] excludeMatchingSharedPreferencesKeys() default {};

    @NonNull
    String formUri() default "";

    @NonNull
    String formUriBasicAuthLogin() default "ACRA-NULL-STRING";

    @NonNull
    String formUriBasicAuthPassword() default "ACRA-NULL-STRING";

    @NonNull
    Method httpMethod() default Method.POST;

    boolean includeDropBoxSystemTags() default false;

    @NonNull
    Class<? extends c> keyStoreFactoryClass() default e.class;

    @NonNull
    String[] logcatArguments() default {"-t", "100", "-v", "time"};

    boolean logcatFilterByPid() default false;

    @NonNull
    String mailTo() default "";

    @NonNull
    ReportingInteractionMode mode() default ReportingInteractionMode.SILENT;

    boolean monitorANR() default false;

    boolean monitorAllProcess() default false;

    boolean monitorNativeCrash() default false;

    String[] monitorProcess() default {};

    @NonNull
    Class<? extends org.acra.dialog.a> reportDialogClass() default CrashReportDialog.class;

    @NonNull
    Class<? extends ReportPrimer> reportPrimerClass() default b.class;

    @NonNull
    Class<? extends ReportSenderFactory>[] reportSenderFactoryClasses() default {DefaultReportSenderFactory.class};

    @NonNull
    Type reportType() default Type.FORM;

    @RawRes
    int resCertificate() default 0;

    @StringRes
    int resDialogCommentPrompt() default 0;

    @StringRes
    int resDialogEmailPrompt() default 0;

    @DrawableRes
    int resDialogIcon() default 17301543;

    @StringRes
    int resDialogNegativeButtonText() default 17039360;

    @StringRes
    int resDialogOkToast() default 0;

    @StringRes
    int resDialogPositiveButtonText() default 17039370;

    @StringRes
    int resDialogText() default 0;

    @StyleRes
    int resDialogTheme() default 0;

    @StringRes
    int resDialogTitle() default 0;

    @DrawableRes
    int resNotifIcon() default 17301624;

    @StringRes
    int resNotifText() default 0;

    @StringRes
    int resNotifTickerText() default 0;

    @StringRes
    int resNotifTitle() default 0;

    @StringRes
    int resToastText() default 0;

    @NonNull
    Class<? extends d> retryPolicyClass() default org.acra.config.c.class;

    boolean sendFailedDump() default true;

    boolean sendReportsAtShutdown() default true;

    boolean sendReportsInDevMode() default true;

    int sharedPreferencesMode() default 0;

    @NonNull
    String sharedPreferencesName() default "data_acra";

    int socketTimeout() default 20000;
}
