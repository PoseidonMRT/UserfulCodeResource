package com.mqunar.core;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.mqunar.BuildConfig;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.core.res.ResourcesInfo;
import com.mqunar.core.res.ResourcesInfo.QResources;
import com.mqunar.core.res.compat.WebViewCompat;
import com.mqunar.dispatcher.DispatcherLogic;
import com.mqunar.exception.AtomFileNotFoundException;
import com.mqunar.module.ModuleInfo;
import com.mqunar.module.ModuleInfoController;
import com.mqunar.spider.ModuleMonitor;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.acra.ACRA;

public class QunarApkLoader {
    public static final boolean SIGNATURE_DEBUG = BuildConfig.SIGNATURE_DEBUG.booleanValue();
    private static Context a;
    private static ClassLoader b;
    private static ClassLoader c;
    private static Field d;
    private static Method e;
    private static Method f;
    private static Loaders g = new Loaders();
    private static Set<String> h;
    private static Object i = new Object();

    public static String getSourceDir(PackageManager packageManager, String str) {
        String str2 = null;
        try {
            return packageManager.getPackageInfo(str, 8192).applicationInfo.sourceDir;
        } catch (Throwable th) {
            return str2;
        }
    }

    public static String getLoaderLogStr() {
        Map mapCopy = g.mapCopy();
        List arrayList = new ArrayList(mapCopy.size());
        for (ModuleInfo moduleInfo : mapCopy.keySet()) {
            arrayList.add("[" + moduleInfo.fileName + "," + moduleInfo.application + "]");
        }
        return arrayList.toString();
    }

    public static boolean addWebViewRes(AssetManager assetManager, Method method, Set<String> set) {
        if (set.isEmpty()) {
            return false;
        }
        for (String a : set) {
            a(assetManager, method, a);
        }
        return true;
    }

    private static void a(AssetManager assetManager, Method method, String str) {
        if (VERSION.SDK_INT <= 23) {
            try {
                method.invoke(assetManager, new Object[]{str});
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        try {
            Method declaredMethod = AssetManager.class.getDeclaredMethod("addAssetPathAsSharedLibrary", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(assetManager, new Object[]{str});
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public static void addChromResources(Resources resources, AssetManager assetManager, Method method) {
        if (VERSION.SDK_INT >= 21 && !WebViewCompat.check(a, assetManager, method)) {
            if (h == null) {
                synchronized (i) {
                    if (h == null) {
                        String sourceDir;
                        WebViewCompat.append("<====================>");
                        Set linkedHashSet = new LinkedHashSet();
                        PackageManager packageManager = a.getPackageManager();
                        if (VERSION.SDK_INT >= 24) {
                            try {
                                Class loadClass = a.getClassLoader().loadClass("android.webkit.WebViewFactory");
                                Method declaredMethod = loadClass.getDeclaredMethod("getWebViewContextAndSetProvider", new Class[0]);
                                declaredMethod.setAccessible(true);
                                declaredMethod.invoke(null, new Object[0]);
                                Method declaredMethod2 = loadClass.getDeclaredMethod("getLoadedPackageInfo", new Class[0]);
                                declaredMethod2.setAccessible(true);
                                sourceDir = getSourceDir(packageManager, ((PackageInfo) declaredMethod2.invoke(null, new Object[0])).packageName);
                                linkedHashSet.add(sourceDir);
                                WebViewCompat.append("getWebViewContextAndSetProvider:" + sourceDir);
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                        if (linkedHashSet.size() <= 0) {
                            Object sourceDir2;
                            sourceDir = getSourceDir(packageManager, "com.google.android.webview");
                            if (!TextUtils.isEmpty(sourceDir)) {
                                linkedHashSet.add(sourceDir);
                            }
                            WebViewCompat.append("com.google.android.webview:" + sourceDir);
                            String sourceDir3 = getSourceDir(packageManager, "com.android.webview");
                            WebViewCompat.append("com.android.webview:" + sourceDir3);
                            if (!(TextUtils.isEmpty(sourceDir) || TextUtils.isEmpty(sourceDir3))) {
                                String parent = new File(sourceDir).getParent();
                                String parent2 = new File(sourceDir3).getParent();
                                if (!(parent.startsWith(parent2) || parent2.startsWith(parent))) {
                                    Collection linkedHashSet2;
                                    if (WebViewCompat.isCustomization && sourceDir.contains("WebViewGoogle_42") && sourceDir3.contains("com.google.android.webview")) {
                                        linkedHashSet2 = new LinkedHashSet();
                                        linkedHashSet2.add(sourceDir3);
                                        linkedHashSet2.addAll(linkedHashSet);
                                        linkedHashSet.clear();
                                        linkedHashSet.addAll(linkedHashSet2);
                                        WebViewCompat.append("add dir2");
                                    } else if (WebViewCompat.isC106) {
                                        if (sourceDir.contains("/system/app/WebViewGoogle/WebViewGoogle.apk") && sourceDir3.contains("/system/app/webview/webview.apk")) {
                                        }
                                        if (sourceDir.contains("/data/app/com.google.android.webview-1/base.apk") && sourceDir3.contains("/data/app/com.android.webview-1/base.apk")) {
                                        }
                                    } else if (WebViewCompat.isCustomization2 && sourceDir.contains("com.google.android.webview") && sourceDir3.contains("WebViewGoogle_42")) {
                                        linkedHashSet2 = new LinkedHashSet();
                                        linkedHashSet2.add(sourceDir3);
                                        linkedHashSet2.addAll(linkedHashSet);
                                        linkedHashSet.clear();
                                        linkedHashSet.addAll(linkedHashSet2);
                                        WebViewCompat.append("add dir2");
                                    } else {
                                        linkedHashSet.add(sourceDir3);
                                    }
                                }
                            }
                            int identifier = resources.getIdentifier("android:string/config_webViewPackageName", "string", "android");
                            if (identifier != 0) {
                                sourceDir3 = resources.getString(identifier);
                                WebViewCompat.append("string/config_webViewPackageName:" + sourceDir3);
                                sourceDir2 = getSourceDir(packageManager, sourceDir3);
                                if (TextUtils.isEmpty(sourceDir2)) {
                                    try {
                                        sourceDir2 = a.createPackageContext(sourceDir3, 3).getApplicationInfo().sourceDir;
                                    } catch (Throwable th2) {
                                    }
                                }
                                if (!TextUtils.isEmpty(sourceDir2)) {
                                    linkedHashSet.add(sourceDir2);
                                    WebViewCompat.append("config_webViewPackageName:" + sourceDir2);
                                }
                            }
                            if (VERSION.SDK_INT >= 24) {
                                sourceDir2 = getSourceDir(packageManager, "com.android.chrome");
                                if (!TextUtils.isEmpty(sourceDir2)) {
                                    linkedHashSet.add(sourceDir2);
                                }
                                WebViewCompat.append("com.android.chrome:" + sourceDir2);
                            }
                        }
                        h = linkedHashSet;
                        WebViewCompat.append("webviewRes:" + h);
                    }
                }
            }
            addWebViewRes(assetManager, method, h);
        }
    }

    public static List<String> getAllResPackageName() {
        Map mapCopy = g.mapCopy();
        List<String> arrayList = new ArrayList(mapCopy.size());
        for (ModuleInfo moduleInfo : mapCopy.keySet()) {
            arrayList.add(moduleInfo.name);
        }
        return arrayList;
    }

    public static void addModulesAssetsPath(Resources resources, AssetManager assetManager) {
        try {
            Method declaredMethod = AssetManager.class.getDeclaredMethod("addAssetPath", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            QLog.i("addAssetPath " + a.getApplicationInfo().sourceDir + " return cookie is " + declaredMethod.invoke(assetManager, new Object[]{a.getApplicationInfo().sourceDir}), new Object[0]);
            addChromResources(resources, assetManager, declaredMethod);
            Iterator it = g.mapCopy().keySet().iterator();
            while (it.hasNext()) {
                declaredMethod.invoke(assetManager, new Object[]{((ModuleInfo) it.next()).fileName});
            }
        } catch (Throwable th) {
            QLog.e(th);
            ACRA.getErrorReporter().handleSilentException(th);
        }
    }

    static int a() {
        return g.size();
    }

    public static Context getAppContext() {
        return a;
    }

    public static String getDexCachePath(Context context) {
        return context == null ? null : context.getDir("opt", 0).getAbsolutePath();
    }

    public static String getPackageName(String str) {
        ModuleInfo matchModule = ModuleInfoController.matchModule(str);
        if (matchModule == null || matchModule.type != (byte) 0) {
            return null;
        }
        return matchModule.name;
    }

    public static String getPkgName(String str) {
        ModuleInfo matchModule = ModuleInfoController.matchModule(str);
        return matchModule == null ? null : matchModule.name;
    }

    public static boolean isSpiderClass(String str) {
        try {
            ClassLoader classLoader = Class.forName(str).getClassLoader();
            if (QunarApkLoader.class.getClassLoader() == classLoader || (classLoader instanceof BaseApkLoader)) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Resources checkResources(Resources resources, Context context) {
        if (resources instanceof QResources) {
            return resources;
        }
        loadResourceWithoutBroadcast(context);
        return ResourcesInfo.getCurrentResources();
    }

    public static void loadResourceWithoutBroadcast(Context context) {
        try {
            Context context2 = (Context) ReflectUtils.getField((Object) context, "mBase");
            if (context2 != null) {
                ReflectUtils.setField("mResources", context2, ResourcesInfo.getCurrentResources());
                ReflectUtils.setField("mTheme", context2, null);
            }
        } catch (Throwable th) {
            QLog.e(th);
        }
        try {
            ReflectUtils.setField("mResources", context, ResourcesInfo.getCurrentResources());
            ReflectUtils.setField("mTheme", context, null);
        } catch (Throwable th2) {
            QLog.e(th2);
        }
    }

    public static void loadResource(Context context) {
        try {
            checkClassLoader();
        } catch (Throwable th) {
            QLog.e(th);
        }
        loadResourceWithoutBroadcast(context);
        try {
            Object invokeMethod = ReflectUtils.invokeMethod("needOnCreateSendBroadcast", context, new Class[0], new Object[0]);
            if (invokeMethod != null && (invokeMethod instanceof Boolean) && ((Boolean) invokeMethod).booleanValue()) {
                Intent intent = new Intent();
                intent.setAction("com.mqunar.spider.MESSAGE_ACTIVITY_ONCREATE");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } catch (Throwable th2) {
            QLog.e(th2);
        }
    }

    static void a(Context context) {
        a = context;
    }

    public static void init(Context context) {
        try {
            a(context);
            if (c == null) {
                Class cls = ClassLoader.class;
                e = cls.getDeclaredMethod("findClass", new Class[]{String.class});
                e.setAccessible(true);
                f = cls.getDeclaredMethod("findLoadedClass", new Class[]{String.class});
                f.setAccessible(true);
                c = context.getClassLoader().getParent();
                b = new b(c);
                try {
                    cls = Class.forName("android.app.ActivityThread");
                    Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
                    declaredMethod.setAccessible(true);
                    Object invoke = declaredMethod.invoke(null, new Object[0]);
                    Field declaredField = cls.getDeclaredField("mInstrumentation");
                    declaredField.setAccessible(true);
                    declaredField.set(invoke, new QInstrumentation((Instrumentation) declaredField.get(invoke)));
                } catch (Throwable th) {
                    ACRA.getErrorReporter().handleSilentException(new RuntimeException("error int init Instrumentation!", th));
                }
                d = ClassLoader.class.getDeclaredField("parent");
                d.setAccessible(true);
            }
        } catch (Throwable th2) {
            RuntimeException runtimeException = new RuntimeException(th2);
        }
        checkClassLoader();
    }

    public static void checkClassLoader() {
        a(a.getClassLoader());
        a(ClassLoader.getSystemClassLoader());
        try {
            a((ClassLoader) ReflectUtils.invokeMethod("getClassLoader", ReflectUtils.getField(((Application) a).getBaseContext(), "mPackageInfo"), new Class[0], new Object[0]));
        } catch (Throwable th) {
        }
        Thread.currentThread().setContextClassLoader(b);
    }

    static void a(ClassLoader classLoader) {
        if (classLoader != null && !(classLoader instanceof BaseApkLoader) && classLoader != b && classLoader.getParent() != b) {
            try {
                d.set(classLoader, b);
            } catch (IllegalAccessException e) {
            }
        }
    }

    public static boolean registerModule(ModuleInfo moduleInfo) {
        return ModuleParser.registerModule(moduleInfo);
    }

    public static void onRegisterOk() {
        String gv = DispatcherLogic.gv();
        QApplication.setVersionInfo(gv);
        Storage.newStorage(a, OwnerConstant.STORAGE_OWNER_SYS).putString("sys_atom", gv);
    }

    public static boolean checkModuleIsLoaded(ModuleInfo moduleInfo) {
        return g.hasItem(moduleInfo);
    }

    private static boolean a(String str) {
        try {
            Signature[] apkSignatureByFilePath = getApkSignatureByFilePath(str);
            if (apkSignatureByFilePath == null || apkSignatureByFilePath.length <= 0) {
                return false;
            }
            for (Signature a : apkSignatureByFilePath) {
                if (a(a)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            QLog.crash(th, "");
            return true;
        }
    }

    private static boolean a(Signature signature) {
        byte[] digest = MessageDigest.getInstance("SHA1").digest(signature.toByteArray());
        long[] jArr = new long[0];
        if (SIGNATURE_DEBUG) {
            jArr = new long[]{1957711140, 2602605240L, 1155921428, 3683068326L, 1575755371};
        } else {
            jArr = new long[]{2054110377, 885279029, 389842755, 37852508, 169717025};
        }
        for (int i = 0; i < 5; i++) {
            if (jArr[i] != ((((((((long) (digest[(i * 4) + 0] & 255)) << 24) + (((long) (digest[(i * 4) + 1] & 255)) << 16)) + (((long) (digest[(i * 4) + 2] & 255)) << 8)) + ((long) (digest[(i * 4) + 3] & 255))) ^ -1) & 4294967295L)) {
                return false;
            }
        }
        return true;
    }

    public static Signature[] getApkSignatureByFilePath(String str) {
        JarFile jarFile = new JarFile(str);
        try {
            Certificate[] a;
            int i;
            Signature[] signatureArr;
            byte[] bArr = new byte[8192];
            try {
                a = a(jarFile, jarFile.getJarEntry("classes.dex"), bArr);
            } catch (Throwable th) {
                a = null;
            }
            if (a == null) {
                Enumeration entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (!(jarEntry.isDirectory() || jarEntry.getName().startsWith("META-INF/"))) {
                        Certificate[] a2 = a(jarFile, jarEntry, bArr);
                        if (a2 == null) {
                            jarFile.close();
                            return null;
                        }
                        if (a != null) {
                            int i2 = 0;
                            while (i2 < a.length) {
                                Object obj;
                                i = 0;
                                while (i < a2.length) {
                                    if (a[i2] != null && a[i2].equals(a2[i])) {
                                        obj = 1;
                                        break;
                                    }
                                    i++;
                                }
                                obj = null;
                                if (obj == null || a.length != a2.length) {
                                    jarFile.close();
                                    return null;
                                }
                                i2++;
                            }
                            a2 = a;
                        }
                        a = a2;
                    }
                }
            }
            jarFile.close();
            if (a == null || a.length <= 0) {
                signatureArr = null;
            } else {
                i = a.length;
                signatureArr = new Signature[a.length];
                for (int i3 = 0; i3 < i; i3++) {
                    signatureArr[i3] = new Signature(a[i3].getEncoded());
                }
            }
            return signatureArr;
        } catch (Throwable th2) {
            jarFile.close();
        }
    }

    private static Certificate[] a(JarFile jarFile, JarEntry jarEntry, byte[] bArr) {
        InputStream bufferedInputStream = new BufferedInputStream(jarFile.getInputStream(jarEntry));
        do {
        } while (bufferedInputStream.read(bArr, 0, bArr.length) != -1);
        bufferedInputStream.close();
        return jarEntry != null ? jarEntry.getCertificates() : null;
    }

    public static boolean add(ModuleInfo moduleInfo) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        boolean addApk = addApk(moduleInfo);
        currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
        ModuleMonitor instance = ModuleMonitor.getInstance();
        if (ModuleMonitor.getInstance().readMonitorModule(moduleInfo)) {
            z = false;
        } else {
            z = true;
        }
        instance.monitorModule(moduleInfo, currentTimeMillis, z);
        QLog.d("WASTE", "add (%s) waste (%d)ms", moduleInfo.name, Long.valueOf(currentTimeMillis));
        return addApk;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean addNotRun(com.mqunar.module.ModuleInfo r8) {
        /*
        r1 = 1;
        r0 = r8.fileName;
        r2 = r0.intern();
        monitor-enter(r2);
        r0 = r8.fileName;	 Catch:{ all -> 0x0093 }
        r0 = a(r0);	 Catch:{ all -> 0x0093 }
        if (r0 != 0) goto L_0x0013;
    L_0x0010:
        r0 = 0;
        monitor-exit(r2);	 Catch:{ all -> 0x0093 }
    L_0x0012:
        return r0;
    L_0x0013:
        r0 = g;	 Catch:{ all -> 0x0093 }
        r0 = r0.hasItem(r8);	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x001e;
    L_0x001b:
        monitor-exit(r2);	 Catch:{ all -> 0x0093 }
        r0 = r1;
        goto L_0x0012;
    L_0x001e:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0093 }
        r0.<init>();	 Catch:{ all -> 0x0093 }
        r3 = com.mqunar.module.ModuleInfoController.getQlibPath();	 Catch:{ all -> 0x0093 }
        r0 = r0.append(r3);	 Catch:{ all -> 0x0093 }
        r3 = java.io.File.pathSeparator;	 Catch:{ all -> 0x0093 }
        r0 = r0.append(r3);	 Catch:{ all -> 0x0093 }
        r3 = a;	 Catch:{ all -> 0x0093 }
        r3 = r3.getApplicationInfo();	 Catch:{ all -> 0x0093 }
        r3 = r3.nativeLibraryDir;	 Catch:{ all -> 0x0093 }
        r0 = r0.append(r3);	 Catch:{ all -> 0x0093 }
        r0 = r0.toString();	 Catch:{ all -> 0x0093 }
        r3 = new com.mqunar.core.BaseApkLoader;	 Catch:{ all -> 0x0093 }
        r4 = a;	 Catch:{ all -> 0x0093 }
        r4 = getDexCachePath(r4);	 Catch:{ all -> 0x0093 }
        r5 = c;	 Catch:{ all -> 0x0093 }
        r3.<init>(r8, r4, r0, r5);	 Catch:{ all -> 0x0093 }
        r0 = g;	 Catch:{ all -> 0x0093 }
        r0.put(r8, r3);	 Catch:{ all -> 0x0093 }
        b();	 Catch:{ all -> 0x0093 }
        r0 = g;	 Catch:{ all -> 0x0093 }
        r0 = r0.copyKeys();	 Catch:{ all -> 0x0093 }
        r3 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x0093 }
        r3.<init>();	 Catch:{ all -> 0x0093 }
        r4 = r0.iterator();	 Catch:{ all -> 0x0093 }
    L_0x0065:
        r0 = r4.hasNext();	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0096;
    L_0x006b:
        r0 = r4.next();	 Catch:{ all -> 0x0093 }
        r0 = (com.mqunar.module.ModuleInfo) r0;	 Catch:{ all -> 0x0093 }
        r5 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x0093 }
        r5.<init>();	 Catch:{ all -> 0x0093 }
        r6 = "packageName";
        r7 = r0.name;	 Catch:{ all -> 0x0093 }
        r5.put(r6, r7);	 Catch:{ all -> 0x0093 }
        r6 = "version";
        r7 = r0.localVersion;	 Catch:{ all -> 0x0093 }
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0093 }
        r5.put(r6, r7);	 Catch:{ all -> 0x0093 }
        r6 = "fileName";
        r0 = r0.fileName;	 Catch:{ all -> 0x0093 }
        r5.put(r6, r0);	 Catch:{ all -> 0x0093 }
        r3.add(r5);	 Catch:{ all -> 0x0093 }
        goto L_0x0065;
    L_0x0093:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0093 }
        throw r0;
    L_0x0096:
        r0 = a;	 Catch:{ all -> 0x0093 }
        r4 = "qunar_sys";
        r0 = com.mqunar.storage.Storage.newStorage(r0, r4);	 Catch:{ all -> 0x0093 }
        r4 = "sys_loaded_atom";
        r3 = r3.toJSONString();	 Catch:{ all -> 0x0093 }
        r0.putString(r4, r3);	 Catch:{ all -> 0x0093 }
        monitor-exit(r2);	 Catch:{ all -> 0x0093 }
        r0 = com.mqunar.module.ModuleInfoController.getDownloadJson(r8);
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x00d1;
    L_0x00b2:
        r2 = new android.os.Bundle;
        r2.<init>();
        r3 = "command";
        r2.putString(r3, r0);
        r0 = "com.mqunar.spider.MESSAGE_INSTRUCTION_MINFO_SERVER";
        r3 = new android.content.Intent;
        r3.<init>(r0);
        r3.putExtras(r2);
        r0 = getAppContext();
        r0 = android.support.v4.content.LocalBroadcastManager.getInstance(r0);
        r0.sendBroadcast(r3);
    L_0x00d1:
        r0 = r1;
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.core.QunarApkLoader.addNotRun(com.mqunar.module.ModuleInfo):boolean");
    }

    private static void b() {
        Resources genNewResources = ResourcesInfo.genNewResources();
        Object baseContext = ((Application) a).getBaseContext();
        ReflectUtils.setField("mResources", ReflectUtils.getField(baseContext, "mPackageInfo"), genNewResources);
        ReflectUtils.setField("mResources", baseContext, genNewResources);
        ReflectUtils.setField("mTheme", baseContext, null);
    }

    public static void runApplicationOncreate(ModuleInfo moduleInfo) {
        if (!TextUtils.isEmpty(moduleInfo.applicationClassName) && moduleInfo.application == null) {
            Context baseContext = ((Application) a).getBaseContext();
            Class loadFromDexs = loadFromDexs(null, moduleInfo.applicationClassName);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                long currentTimeMillis = System.currentTimeMillis();
                moduleInfo.application = Instrumentation.newApplication(loadFromDexs, baseContext);
                moduleInfo.application.onCreate();
                QLog.d("WASTE", "application (%s) onCreate waste (%d)", loadFromDexs.getName(), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                return;
            }
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            Runnable cVar = new c(moduleInfo, loadFromDexs, baseContext, atomicBoolean);
            Handler handler = new Handler(Looper.getMainLooper());
            if (!handler.postAtFrontOfQueue(cVar)) {
                handler.post(cVar);
            }
            synchronized (atomicBoolean) {
                if (!atomicBoolean.get()) {
                    atomicBoolean.wait(30000);
                }
            }
        }
    }

    public static boolean addApk(ModuleInfo moduleInfo) {
        boolean addNotRun = addNotRun(moduleInfo);
        QLog.d("fuck", "apkPath is " + moduleInfo.fileName + ",addOk=" + addNotRun, new Object[0]);
        if (!addNotRun) {
            return false;
        }
        runApplicationOncreate(moduleInfo);
        return true;
    }

    private static Class<?> b(String str) {
        try {
            return c.loadClass(str);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    private static Class<?> c(String str) {
        Class<?> cls;
        if (f != null) {
            try {
                cls = (Class) f.invoke(a.getClassLoader(), new Object[]{str});
            } catch (Throwable th) {
                cls = null;
            }
        } else {
            cls = null;
        }
        if (cls != null || e == null) {
            return cls;
        }
        return (Class) e.invoke(a.getClassLoader(), new Object[]{str});
    }

    public static boolean shouldRestartWhenResNotFound(int i) {
        Object obj;
        if (i >= 805306368 && i <= 2130706431) {
            int i2 = i >> 24;
            for (ModuleInfo moduleInfo : g.mapCopy().keySet()) {
                if (i2 == moduleInfo.dependency.packageId) {
                    obj = moduleInfo.fileName;
                    break;
                }
            }
        }
        obj = null;
        if (TextUtils.isEmpty(obj) || new File(obj).exists()) {
            return false;
        }
        return true;
    }

    private static Class<?> a(BaseApkLoader baseApkLoader, String str) {
        Class<?> cls;
        Class<?> cls2;
        ClassNotFoundException classNotFoundException = null;
        Map mapCopy = g.mapCopy();
        ClassNotFoundException classNotFoundException2 = null;
        for (BaseApkLoader baseApkLoader2 : mapCopy.values()) {
            Class cls3;
            if (baseApkLoader2 == null || baseApkLoader2 == baseApkLoader) {
                cls3 = classNotFoundException2;
            } else {
                cls3 = baseApkLoader2.loadClassFromCurrentCache(str);
                if (cls3 != null) {
                    break;
                }
            }
            Object obj = cls3;
        }
        Object obj2 = classNotFoundException2;
        if (r0 == null) {
            cls = r0;
            for (BaseApkLoader baseApkLoader22 : mapCopy.values()) {
                if (!(baseApkLoader22 == null || baseApkLoader22 == baseApkLoader)) {
                    try {
                        cls = baseApkLoader22.findClass(str);
                    } catch (ClassNotFoundException e) {
                    }
                    if (cls != null) {
                        cls2 = cls;
                        break;
                    }
                }
                cls = cls;
            }
            cls2 = cls;
        } else {
            cls2 = r0;
        }
        if (cls2 == null) {
            String optimizedPathFor;
            ModuleInfo moduleInfo;
            int length;
            Map mapCopy2 = ModuleInfoController.modules.mapCopy();
            int i = -1;
            ModuleInfo moduleInfo2 = null;
            for (String optimizedPathFor2 : mapCopy2.keySet()) {
                if (str.startsWith(optimizedPathFor2 + ".")) {
                    moduleInfo = (ModuleInfo) mapCopy2.get(optimizedPathFor2);
                    if (!mapCopy.containsKey(moduleInfo) || moduleInfo.hasUnloadMultiDex()) {
                        length = optimizedPathFor2.length();
                        if (length > i) {
                            i = length;
                            moduleInfo2 = moduleInfo;
                        }
                    }
                }
                length = i;
                moduleInfo = moduleInfo2;
                i = length;
                moduleInfo2 = moduleInfo;
            }
            if (moduleInfo2 == null) {
                int i2 = i;
                ModuleInfo moduleInfo3 = moduleInfo2;
                BaseApkLoader baseApkLoader3 = null;
                for (String optimizedPathFor22 : mapCopy2.keySet()) {
                    if (str.startsWith(optimizedPathFor22 + ".")) {
                        moduleInfo = (ModuleInfo) mapCopy2.get(optimizedPathFor22);
                        if (mapCopy.containsKey(moduleInfo)) {
                            int length2 = optimizedPathFor22.length();
                            if (length2 > i2) {
                                baseApkLoader3 = (BaseApkLoader) mapCopy.get(moduleInfo);
                                moduleInfo3 = moduleInfo;
                                length = length2;
                                i2 = length;
                            }
                        }
                    }
                    length = i2;
                    i2 = length;
                }
                classNotFoundException = ErrorMsg.a(a, baseApkLoader, str, mapCopy2, moduleInfo3, baseApkLoader3);
                if (!(moduleInfo3 == null || new File(moduleInfo3.fileName).exists())) {
                    throw new AtomFileNotFoundException(moduleInfo3.fileName, classNotFoundException);
                }
            } else if (mapCopy.containsKey(moduleInfo2)) {
                try {
                    cls2 = moduleInfo2.loadFromMultiDex(str);
                } catch (Throwable e2) {
                    throw new RuntimeException(e2);
                } catch (ClassNotFoundException e3) {
                    classNotFoundException = e3;
                }
            } else {
                ClassNotFoundException classNotFoundException3;
                try {
                    if (add(moduleInfo2)) {
                        try {
                            cls = moduleInfo2.loader.findClass(str);
                            classNotFoundException3 = null;
                        } catch (ClassNotFoundException e4) {
                            classNotFoundException3 = e4;
                            cls = cls2;
                        }
                        if (cls == null) {
                            if (moduleInfo2.hasUnloadMultiDex()) {
                                cls = moduleInfo2.loadFromMultiDex(str);
                            }
                        }
                    } else {
                        classNotFoundException3 = new ClassNotFoundException(str + " not found,isInstalled is false");
                        cls = cls2;
                    }
                } catch (Throwable e22) {
                    throw new RuntimeException(e22);
                } catch (ClassNotFoundException e5) {
                    classNotFoundException3 = e5;
                } catch (Throwable e222) {
                    Throwable runtimeException = new RuntimeException(e222);
                    optimizedPathFor22 = DexPathList.optimizedPathFor(new File(moduleInfo2.fileName), new File(getDexCachePath(a)));
                    if (!new File(optimizedPathFor22).exists()) {
                        AtomFileNotFoundException atomFileNotFoundException = new AtomFileNotFoundException(optimizedPathFor22, runtimeException);
                    }
                }
                classNotFoundException = classNotFoundException3;
                cls2 = cls;
            }
        }
        if (cls2 != null || classNotFoundException == null) {
            return cls2;
        }
        throw classNotFoundException;
    }

    public static Class<?> loadFromDexs(BaseApkLoader baseApkLoader, String str) {
        Class<?> loadClassFromCurrent;
        Throwable e;
        Class<?> a;
        Throwable th = null;
        if (baseApkLoader != null) {
            try {
                loadClassFromCurrent = baseApkLoader.loadClassFromCurrent(str);
            } catch (Throwable e2) {
                loadClassFromCurrent = null;
                th = e2;
            }
        } else {
            loadClassFromCurrent = null;
        }
        if (loadClassFromCurrent == null) {
            try {
                loadClassFromCurrent = b(str);
            } catch (Throwable th2) {
                th = th2;
            }
        }
        if (loadClassFromCurrent == null) {
            try {
                loadClassFromCurrent = c(str);
            } catch (Throwable th3) {
                th = th3;
            }
        }
        if (loadClassFromCurrent == null) {
            boolean z;
            boolean startsWith = str.startsWith("com.mqunar.");
            if (startsWith) {
                z = startsWith;
            } else {
                int i = 0;
                z = startsWith;
                for (ModuleInfo moduleInfo : ModuleInfoController.modules.mapCopy().values()) {
                    if (!(moduleInfo == null || moduleInfo.dependency == null || moduleInfo.dependency.atomPackages == null || moduleInfo.dependency.atomPackages.isEmpty())) {
                        for (String str2 : moduleInfo.dependency.atomPackages) {
                            if (!TextUtils.isEmpty(str2) && str.startsWith(str2 + ".")) {
                                i = 1;
                                z = true;
                                break;
                            }
                        }
                        if (i != 0) {
                            break;
                        }
                    }
                    z = z;
                    i = i;
                }
            }
            if (z) {
                try {
                    a = a(baseApkLoader, str);
                    e2 = th;
                } catch (ClassNotFoundException e3) {
                    e2 = e3;
                    a = loadClassFromCurrent;
                }
                if (a == null) {
                    QLog.i("after loadFromSo not search className = " + str, new Object[0]);
                }
                if (a == null || e2 == null) {
                    return a;
                }
                throw new ClassNotFoundException("class not found : " + str, e2);
            }
        }
        e2 = th;
        a = loadClassFromCurrent;
        if (a == null) {
        }
        return a;
    }
}
