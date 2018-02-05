package com.mqunar.core.basectx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import java.lang.reflect.Method;

public abstract class SchemeDispatcher {
    public static final String RESULT_BROADCAST_MESSAGE_ACTION = "_RESULT_BROADCAST_MESSAGE_ACTION_";
    public static final String SPIDER_REQUESTCODE = "_SPIDER_REQUESTCODE_";
    public static final String TARGET_SCHEME_CLEAR_TOP = "_SPIDER_FLAG_CLEAR_TOP_";
    public static final String TARGET_SCHEME_URI = "QUNAR_SPIDER_TARGET_SCHEME_URL";
    private static String homeScheme = null;

    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Fragment val$context;
        final /* synthetic */ Intent val$intent;

        AnonymousClass1(Fragment fragment, Intent intent) {
            this.val$context = fragment;
            this.val$intent = intent;
        }

        public void run() {
            this.val$context.startActivity(this.val$intent);
        }
    }

    final class AnonymousClass2 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Intent val$intent;

        AnonymousClass2(Context context, Intent intent) {
            this.val$context = context;
            this.val$intent = intent;
        }

        public void run() {
            this.val$context.startActivity(this.val$intent);
        }
    }

    final class AnonymousClass3 implements Runnable {
        final /* synthetic */ Fragment val$activity;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass3(Fragment fragment, Intent intent, int i) {
            this.val$activity = fragment;
            this.val$intent = intent;
            this.val$requestCode = i;
        }

        public void run() {
            this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }

    final class AnonymousClass4 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass4(Activity activity, Intent intent, int i) {
            this.val$activity = activity;
            this.val$intent = intent;
            this.val$requestCode = i;
        }

        public void run() {
            this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }

    final class AnonymousClass5 implements Runnable {
        final /* synthetic */ Fragment val$context;
        final /* synthetic */ Intent val$intent;

        AnonymousClass5(Fragment fragment, Intent intent) {
            this.val$context = fragment;
            this.val$intent = intent;
        }

        public void run() {
            this.val$context.startActivity(this.val$intent);
        }
    }

    final class AnonymousClass6 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Intent val$intent;

        AnonymousClass6(Context context, Intent intent) {
            this.val$context = context;
            this.val$intent = intent;
        }

        public void run() {
            this.val$context.startActivity(this.val$intent);
        }
    }

    final class AnonymousClass7 implements Runnable {
        final /* synthetic */ Fragment val$activity;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass7(Fragment fragment, Intent intent, int i) {
            this.val$activity = fragment;
            this.val$intent = intent;
            this.val$requestCode = i;
        }

        public void run() {
            this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }

    final class AnonymousClass8 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$requestCode;

        AnonymousClass8(Activity activity, Intent intent, int i) {
            this.val$activity = activity;
            this.val$intent = intent;
            this.val$requestCode = i;
        }

        public void run() {
            this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }

    public static String getHomeScheme(Context context) {
        if (TextUtils.isEmpty(homeScheme)) {
            homeScheme = getMetaData(context.getApplicationContext(), "MAIN_SCHEME");
        }
        return homeScheme;
    }

    public static String getMetaData(Context context, String str) {
        AssetManager assetManager;
        Throwable th;
        AssetManager assetManager2 = null;
        Bundle bundle = context.getApplicationInfo().metaData;
        if (bundle != null && !bundle.isEmpty()) {
            return bundle.getString(str);
        }
        XmlResourceParser openXmlResourceParser;
        try {
            AssetManager assetManager3 = (AssetManager) AssetManager.class.newInstance();
            try {
                Method declaredMethod = AssetManager.class.getDeclaredMethod("addAssetPath", new Class[]{String.class});
                declaredMethod.setAccessible(true);
                int intValue = ((Integer) declaredMethod.invoke(assetManager3, new Object[]{context.getApplicationInfo().sourceDir})).intValue();
                if (intValue != 0) {
                    String str2 = "http://schemas.android.com/apk/res/android";
                    openXmlResourceParser = assetManager3.openXmlResourceParser(intValue, "AndroidManifest.xml");
                    try {
                        Object obj = null;
                        for (int eventType = openXmlResourceParser.getEventType(); eventType != 1; eventType = openXmlResourceParser.next()) {
                            switch (eventType) {
                                case 2:
                                    if (!"meta-data".equals(openXmlResourceParser.getName())) {
                                        if (obj == null) {
                                            break;
                                        }
                                        if (openXmlResourceParser != null) {
                                            openXmlResourceParser.close();
                                        }
                                        if (assetManager3 != null) {
                                            assetManager3.close();
                                        }
                                        return null;
                                    } else if (!openXmlResourceParser.getAttributeValue(str2, AIUIConstant.KEY_NAME).equals(str)) {
                                        intValue = 1;
                                        break;
                                    } else {
                                        String attributeValue = openXmlResourceParser.getAttributeValue(str2, "value");
                                        if (openXmlResourceParser != null) {
                                            openXmlResourceParser.close();
                                        }
                                        if (assetManager3 != null) {
                                            assetManager3.close();
                                        }
                                        return attributeValue;
                                    }
                                default:
                                    break;
                            }
                        }
                    } catch (Throwable th2) {
                        assetManager2 = assetManager3;
                        th = th2;
                        if (openXmlResourceParser != null) {
                            openXmlResourceParser.close();
                        }
                        if (assetManager2 != null) {
                            assetManager2.close();
                        }
                        throw th;
                    }
                }
                openXmlResourceParser = null;
                if (openXmlResourceParser != null) {
                    openXmlResourceParser.close();
                }
                if (assetManager3 != null) {
                    assetManager3.close();
                }
            } catch (Throwable th22) {
                openXmlResourceParser = null;
                assetManager2 = assetManager3;
                th = th22;
                if (openXmlResourceParser != null) {
                    openXmlResourceParser.close();
                }
                if (assetManager2 != null) {
                    assetManager2.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            openXmlResourceParser = null;
            if (openXmlResourceParser != null) {
                openXmlResourceParser.close();
            }
            if (assetManager2 != null) {
                assetManager2.close();
            }
            throw th;
        }
        return null;
    }

    public static void sendSchemeAndClearStack(Fragment fragment, String str) {
        sendSchemeAndClearStack(fragment, getHomeScheme(fragment.getActivity()), str);
    }

    public static void sendSchemeAndClearStack(Context context, String str) {
        sendSchemeAndClearStack(context, getHomeScheme(context), str);
    }

    public static void sendSchemeAndClearStack(Fragment fragment, String str, Bundle bundle) {
        sendSchemeAndClearStack(fragment, getHomeScheme(fragment.getActivity()), str, bundle);
    }

    public static void sendSchemeAndClearStack(Context context, String str, Bundle bundle) {
        sendSchemeAndClearStack(context, getHomeScheme(context), str, bundle);
    }

    public static void sendSchemeAndClearStack(Fragment fragment, String str, String str2) {
        sendSchemeAndClearStack(fragment, str, str2, null);
    }

    public static void sendSchemeAndClearStack(Context context, String str, String str2) {
        sendSchemeAndClearStack(context, str, str2, null);
    }

    public static void sendSchemeAndClearStack(Fragment fragment, String str, String str2, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(fragment.getActivity().getPackageName());
        intent.putExtra(TARGET_SCHEME_URI, str2);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        sendIntent(fragment, intent, new AnonymousClass1(fragment, intent));
    }

    public static void sendSchemeAndClearStack(Context context, String str, String str2, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(context.getPackageName());
        intent.putExtra(TARGET_SCHEME_URI, str2);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        sendIntent(context, intent, new AnonymousClass2(context, intent));
    }

    public static void sendSchemeForResultAndClearStack(Fragment fragment, String str, int i) {
        sendSchemeForResultAndClearStack(fragment, getHomeScheme(fragment.getActivity()), str, i);
    }

    public static void sendSchemeForResultAndClearStack(Activity activity, String str, int i) {
        sendSchemeForResultAndClearStack(activity, getHomeScheme(activity), str, i);
    }

    public static void sendSchemeForResultAndClearStack(Fragment fragment, String str, String str2, int i) {
        sendSchemeForResultAndClearStack(fragment, str, str2, i, null);
    }

    public static void sendSchemeForResultAndClearStack(Activity activity, String str, String str2, int i) {
        sendSchemeForResultAndClearStack(activity, str, str2, i, null);
    }

    public static void sendSchemeForResultAndClearStack(Fragment fragment, String str, String str2, int i, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(fragment.getActivity().getPackageName());
        intent.putExtra(TARGET_SCHEME_URI, str2);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(SPIDER_REQUESTCODE, i);
        sendIntent(fragment, intent, new AnonymousClass3(fragment, intent, i));
    }

    public static void sendSchemeForResultAndClearStack(Activity activity, String str, String str2, int i, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(activity.getPackageName());
        intent.putExtra(TARGET_SCHEME_URI, str2);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(SPIDER_REQUESTCODE, i);
        sendIntent(activity, intent, new AnonymousClass4(activity, intent, i));
    }

    public static void sendScheme(Fragment fragment, String str) {
        sendScheme(fragment, str, null, false, 0);
    }

    public static void sendScheme(Context context, String str) {
        sendScheme(context, str, null, false, 0);
    }

    public static void sendScheme(Fragment fragment, String str, Bundle bundle) {
        sendScheme(fragment, str, bundle, false, 0);
    }

    public static void sendScheme(Context context, String str, Bundle bundle) {
        sendScheme(context, str, bundle, false, 0);
    }

    public static void sendScheme(Fragment fragment, String str, boolean z) {
        sendScheme(fragment, str, null, z, 0);
    }

    public static void sendScheme(Context context, String str, boolean z) {
        sendScheme(context, str, null, z, 0);
    }

    public static void sendScheme(Fragment fragment, String str, int i) {
        sendScheme(fragment, str, null, false, i);
    }

    public static void sendScheme(Context context, String str, int i) {
        sendScheme(context, str, null, false, i);
    }

    public static void sendScheme(Fragment fragment, String str, Bundle bundle, boolean z, int i) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(fragment.getActivity().getPackageName());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (z) {
            intent.putExtra(TARGET_SCHEME_CLEAR_TOP, true);
        }
        if (i != 0) {
            intent.setFlags(i);
        }
        sendIntent(fragment, intent, new AnonymousClass5(fragment, intent));
    }

    public static void sendScheme(Context context, String str, Bundle bundle, boolean z, int i) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(context.getPackageName());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (z) {
            intent.putExtra(TARGET_SCHEME_CLEAR_TOP, true);
        }
        if (i != 0) {
            intent.setFlags(i);
        }
        sendIntent(context, intent, new AnonymousClass6(context, intent));
    }

    public static void sendSchemeForResult(Fragment fragment, String str, int i) {
        sendSchemeForResult(fragment, str, i, null);
    }

    public static void sendSchemeForResult(Activity activity, String str, int i) {
        sendSchemeForResult(activity, str, i, null);
    }

    public static void sendSchemeForResult(Fragment fragment, String str, int i, int i2) {
        sendSchemeForResult(fragment, str, i, null, i2);
    }

    public static void sendSchemeForResult(Activity activity, String str, int i, int i2) {
        sendSchemeForResult(activity, str, i, null, i2);
    }

    public static void sendSchemeForResult(Fragment fragment, String str, int i, Bundle bundle) {
        sendSchemeForResult(fragment, str, i, bundle, 0);
    }

    public static void sendSchemeForResult(Activity activity, String str, int i, Bundle bundle) {
        sendSchemeForResult(activity, str, i, bundle, 0);
    }

    public static void sendSchemeForResult(Fragment fragment, String str, int i, Bundle bundle, int i2) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(fragment.getActivity().getPackageName());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (i2 != 0) {
            intent.setFlags(i2);
        }
        intent.putExtra(SPIDER_REQUESTCODE, i);
        sendIntent(fragment, intent, new AnonymousClass7(fragment, intent, i));
    }

    public static void sendSchemeForResult(Activity activity, String str, int i, Bundle bundle, int i2) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setPackage(activity.getPackageName());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (i2 != 0) {
            intent.setFlags(i2);
        }
        intent.putExtra(SPIDER_REQUESTCODE, i);
        sendIntent(activity, intent, new AnonymousClass8(activity, intent, i));
    }

    private static void sendIntent(Object obj, Intent intent, Runnable runnable) {
        try {
            Method declaredMethod = Class.forName("com.mqunar.dispatcher.DispatcherLogic").getDeclaredMethod("processIntent", new Class[]{Object.class, Intent.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(null, new Object[]{obj, intent});
        } catch (Throwable th) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}
