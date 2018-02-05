package com.mqunar.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.core.dependency.Dependency;
import com.mqunar.dispatcher.SchemeManager;
import com.mqunar.module.ModuleInfo;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.log.QLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModuleParser {
    public static final String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
    private static final String a = ModuleParser.class.getSimpleName();
    private static String b = "_([0-9a-fA-F]{32})\\.";
    private static Pattern c = Pattern.compile(b);

    public static boolean registerModule(ModuleInfo moduleInfo) {
        ApkInfo parseApk = parseApk(moduleInfo.fileName);
        moduleInfo.name = parseApk.packageName;
        moduleInfo.localVersion = parseApk.versionCode;
        moduleInfo.applicationClassName = parseApk.applicationClassName;
        moduleInfo.launcherClassName = parseApk.launcherClassName;
        if (!(ArrayUtils.isEmpty(parseApk.components) && parseApk.logicIntentFilters == null)) {
            if (parseApk.logicIntentFilters != null) {
                List arrayList = new ArrayList();
                arrayList.addAll(parseApk.logicIntentFilters);
                parseApk.addComponents(parseApk.applicationClassName, arrayList);
            }
            SchemeManager.addScheme(parseApk.packageName, parseApk.versionCode, parseApk.components);
        }
        return true;
    }

    public static Dependency parseDependencyFromAsset() {
        try {
            InputStream open = QunarApkLoader.getAppContext().getAssets().open("dependencies.dps");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[4096];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    Dependency dependency = (Dependency) JSON.parseObject(new String(byteArrayOutputStream.toByteArray(), "UTF-8"), Dependency.class);
                    open.close();
                    return dependency;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("文件格式不正确,asset下缺少dependencies.dps文件或者解析失败!", e);
        }
    }

    public static ModuleInfo parseModuleDependency(String str) {
        ZipFile zipFile;
        ModuleInfo moduleInfo = null;
        if (isAtom(str)) {
            ZipFile zipFile2;
            Throwable th;
            try {
                zipFile2 = new ZipFile(str);
                try {
                    ModuleInfo moduleInfo2;
                    InputStream inputStream = zipFile2.getInputStream(zipFile2.getEntry("dependencies.dps"));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    Dependency dependency = (Dependency) JSON.parseObject(new String(byteArrayOutputStream.toByteArray(), "UTF-8"), Dependency.class);
                    if (dependency != null) {
                        ModuleInfo moduleInfo3 = new ModuleInfo();
                        moduleInfo3.name = dependency.packageName;
                        moduleInfo3.fileName = str;
                        moduleInfo3.dependency = dependency;
                        Object obj = zipFile2.getEntry("classes2.dex") != null ? 1 : null;
                        moduleInfo3.zipFile = zipFile2;
                        if (obj != null) {
                            moduleInfo3.dexList = new ArrayList(1);
                        }
                        moduleInfo2 = moduleInfo3;
                    } else {
                        moduleInfo2 = null;
                    }
                    ModuleInfo moduleInfo4 = moduleInfo2;
                    th = null;
                    moduleInfo = moduleInfo4;
                } catch (Throwable th2) {
                    th = th2;
                    zipFile = zipFile2;
                    QLog.e(th);
                    zipFile2 = zipFile;
                    if (moduleInfo == null) {
                        if (zipFile2 != null) {
                            try {
                                zipFile2.close();
                            } catch (Exception e) {
                            }
                        }
                        if (th == null) {
                            throw new RuntimeException("解析dependencies.dps文件时异常!filename is : " + str + ",file.length is : " + new File(str).length() + ",file md5 is : " + getMd5(str), th);
                        }
                        throw new RuntimeException("解析dependencies.dps时，parseObject return null!filename is : " + str);
                    }
                    return moduleInfo;
                }
            } catch (Throwable th3) {
                th = th3;
                Object obj2 = null;
                QLog.e(th);
                zipFile2 = zipFile;
                if (moduleInfo == null) {
                    if (zipFile2 != null) {
                        zipFile2.close();
                    }
                    if (th == null) {
                        throw new RuntimeException("解析dependencies.dps时，parseObject return null!filename is : " + str);
                    }
                    throw new RuntimeException("解析dependencies.dps文件时异常!filename is : " + str + ",file.length is : " + new File(str).length() + ",file md5 is : " + getMd5(str), th);
                }
                return moduleInfo;
            }
            if (moduleInfo == null) {
                if (zipFile2 != null) {
                    zipFile2.close();
                }
                if (th == null) {
                    throw new RuntimeException("解析dependencies.dps时，parseObject return null!filename is : " + str);
                }
                throw new RuntimeException("解析dependencies.dps文件时异常!filename is : " + str + ",file.length is : " + new File(str).length() + ",file md5 is : " + getMd5(str), th);
            }
        }
        return moduleInfo;
    }

    public static String getMd5(String str) {
        return getMd5(new File(str));
    }

    public static String getMd5(File file) {
        try {
            return getMd5(new FileInputStream(file));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMd5(InputStream inputStream) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            StringBuilder stringBuilder = new StringBuilder(32);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                instance.update(bArr, 0, read);
            }
            byte[] digest = instance.digest();
            for (byte b : digest) {
                stringBuilder.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            String stringBuilder2 = stringBuilder.toString();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            return stringBuilder2;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    public static boolean isAtom(String str) {
        String str2 = null;
        if (str != null) {
            int lastIndexOf = str.lastIndexOf("/");
            if (lastIndexOf != -1) {
                str2 = str.substring(lastIndexOf + 1);
            }
        }
        return str2 != null && str2.startsWith("libq_");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.mqunar.core.ApkInfo parseApk(java.lang.String r13) {
        /*
        r12 = 1;
        r2 = 0;
        r0 = android.content.res.AssetManager.class;
        r0 = r0.newInstance();	 Catch:{ Throwable -> 0x0354 }
        r0 = (android.content.res.AssetManager) r0;	 Catch:{ Throwable -> 0x0354 }
        r1 = android.content.res.AssetManager.class;
        r3 = "addAssetPath";
        r4 = 1;
        r4 = new java.lang.Class[r4];	 Catch:{ Throwable -> 0x0067 }
        r5 = 0;
        r6 = java.lang.String.class;
        r4[r5] = r6;	 Catch:{ Throwable -> 0x0067 }
        r1 = r1.getDeclaredMethod(r3, r4);	 Catch:{ Throwable -> 0x0067 }
        r3 = 1;
        r1.setAccessible(r3);	 Catch:{ Throwable -> 0x0067 }
        r3 = 1;
        r3 = new java.lang.Object[r3];	 Catch:{ Throwable -> 0x0067 }
        r4 = 0;
        r3[r4] = r13;	 Catch:{ Throwable -> 0x0067 }
        r1 = r1.invoke(r0, r3);	 Catch:{ Throwable -> 0x0067 }
        r1 = (java.lang.Integer) r1;	 Catch:{ Throwable -> 0x0067 }
        r1 = r1.intValue();	 Catch:{ Throwable -> 0x0067 }
        if (r1 == 0) goto L_0x004e;
    L_0x0030:
        r3 = "AndroidManifest.xml";
        r1 = r0.openXmlResourceParser(r1, r3);	 Catch:{ Throwable -> 0x0067 }
        r6 = r0;
        r7 = r1;
    L_0x0038:
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x01aa }
        r0 = r7.getEventType();	 Catch:{ Throwable -> 0x01aa }
        r1 = r2;
        r3 = r2;
        r4 = r2;
        r5 = r2;
    L_0x0044:
        if (r0 == r12) goto L_0x0319;
    L_0x0046:
        switch(r0) {
            case 0: goto L_0x0049;
            case 1: goto L_0x0049;
            case 2: goto L_0x0181;
            case 3: goto L_0x02b9;
            default: goto L_0x0049;
        };	 Catch:{ Throwable -> 0x01aa }
    L_0x0049:
        r0 = r7.next();	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0044;
    L_0x004e:
        r1 = new java.lang.RuntimeException;	 Catch:{ Throwable -> 0x0067 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0067 }
        r3.<init>();	 Catch:{ Throwable -> 0x0067 }
        r4 = "Failed adding asset path:";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0067 }
        r3 = r3.append(r13);	 Catch:{ Throwable -> 0x0067 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0067 }
        r1.<init>(r3);	 Catch:{ Throwable -> 0x0067 }
        throw r1;	 Catch:{ Throwable -> 0x0067 }
    L_0x0067:
        r4 = move-exception;
    L_0x0068:
        if (r0 == 0) goto L_0x006d;
    L_0x006a:
        r0.close();	 Catch:{ Throwable -> 0x034e }
    L_0x006d:
        r5 = new java.io.File;
        r5.<init>(r13);
        r3 = new java.util.zip.ZipFile;	 Catch:{ Throwable -> 0x016b }
        r1 = 1;
        r3.<init>(r5, r1);	 Catch:{ Throwable -> 0x016b }
        r1 = "AndroidManifest.xml";
        r6 = r3.getEntry(r1);	 Catch:{ Throwable -> 0x016b }
        r1 = new com.mqunar.core.android.content.res.AXmlResourceParser;	 Catch:{ Throwable -> 0x016b }
        r1.<init>();	 Catch:{ Throwable -> 0x016b }
        r3 = r3.getInputStream(r6);	 Catch:{ Throwable -> 0x016b }
        r1.open(r3);	 Catch:{ Throwable -> 0x016b }
        r3 = r2;
    L_0x008b:
        if (r1 != 0) goto L_0x035b;
    L_0x008d:
        r0 = "";
        r6 = r5.getName();
        r0 = new java.util.zip.ZipFile;	 Catch:{ Throwable -> 0x0170 }
        r1 = com.mqunar.core.QunarApkLoader.getAppContext();	 Catch:{ Throwable -> 0x0170 }
        r1 = r1.getApplicationInfo();	 Catch:{ Throwable -> 0x0170 }
        r1 = r1.sourceDir;	 Catch:{ Throwable -> 0x0170 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0170 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0351 }
        r1.<init>();	 Catch:{ Throwable -> 0x0351 }
        r2 = "lib/armeabi/";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0351 }
        r1 = r1.append(r6);	 Catch:{ Throwable -> 0x0351 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0351 }
        r1 = r0.getEntry(r1);	 Catch:{ Throwable -> 0x0351 }
        r1 = r0.getInputStream(r1);	 Catch:{ Throwable -> 0x0351 }
        r1 = getMd5(r1);	 Catch:{ Throwable -> 0x0351 }
    L_0x00c1:
        r2 = com.mqunar.core.QunarApkLoader.getAppContext();
        r2 = r2.getApplicationInfo();
        r2 = r2.sourceDir;
        r7 = getMd5(r2);
        r2 = "";
        if (r0 == 0) goto L_0x0358;
    L_0x00d3:
        r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0179 }
        r2.<init>();	 Catch:{ Throwable -> 0x0179 }
        r8 = "lib/armeabi/";
        r2 = r2.append(r8);	 Catch:{ Throwable -> 0x0179 }
        r2 = r2.append(r6);	 Catch:{ Throwable -> 0x0179 }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x0179 }
        r2 = r0.getEntry(r2);	 Catch:{ Throwable -> 0x0179 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0179 }
        r6.<init>();	 Catch:{ Throwable -> 0x0179 }
        r0 = checkEquals(r0, r2, r5);	 Catch:{ Throwable -> 0x0179 }
        r0 = r6.append(r0);	 Catch:{ Throwable -> 0x0179 }
        r2 = "";
        r0 = r0.append(r2);	 Catch:{ Throwable -> 0x0179 }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x0179 }
    L_0x0101:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r6 = "parse file error: ";
        r2 = r2.append(r6);
        r2 = r2.append(r13);
        r6 = ",len: ";
        r2 = r2.append(r6);
        r5 = r5.length();
        r2 = r2.append(r5);
        r5 = ",md5: ";
        r2 = r2.append(r5);
        r5 = getMd5(r13);
        r2 = r2.append(r5);
        r5 = ",md5InSource : ";
        r2 = r2.append(r5);
        r1 = r2.append(r1);
        r2 = ",checkEquals:";
        r1 = r1.append(r2);
        r0 = r1.append(r0);
        r1 = ",sourceDir: ";
        r0 = r0.append(r1);
        r1 = com.mqunar.core.QunarApkLoader.getAppContext();
        r1 = r1.getApplicationInfo();
        r1 = r1.sourceDir;
        r0 = r0.append(r1);
        r1 = ",md5:";
        r0 = r0.append(r1);
        r0 = r0.append(r7);
        r1 = r0.toString();
        r2 = new java.lang.RuntimeException;
        if (r3 == 0) goto L_0x017f;
    L_0x0166:
        r0 = r3;
    L_0x0167:
        r2.<init>(r1, r0);
        throw r2;
    L_0x016b:
        r1 = move-exception;
        r3 = r1;
        r1 = r2;
        goto L_0x008b;
    L_0x0170:
        r0 = move-exception;
        r1 = r0;
        r0 = r2;
    L_0x0173:
        r1 = r1.getLocalizedMessage();
        goto L_0x00c1;
    L_0x0179:
        r0 = move-exception;
        r0 = r0.getLocalizedMessage();
        goto L_0x0101;
    L_0x017f:
        r0 = r4;
        goto L_0x0167;
    L_0x0181:
        r0 = r7.getName();	 Catch:{ Throwable -> 0x01aa }
        r10 = "manifest";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x01b8;
    L_0x018d:
        r5 = new com.mqunar.core.ApkInfo;	 Catch:{ Throwable -> 0x01aa }
        r5.<init>();	 Catch:{ Throwable -> 0x01aa }
        r5.fileName = r13;	 Catch:{ Throwable -> 0x01aa }
        r0 = 0;
        r10 = "package";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        r5.packageName = r0;	 Catch:{ Throwable -> 0x01aa }
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "versionCode";
        r11 = 0;
        r0 = r7.getAttributeIntValue(r0, r10, r11);	 Catch:{ Throwable -> 0x01aa }
        r5.versionCode = r0;	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x01aa:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x01ac }
    L_0x01ac:
        r0 = move-exception;
        if (r7 == 0) goto L_0x01b2;
    L_0x01af:
        r7.close();
    L_0x01b2:
        if (r6 == 0) goto L_0x01b7;
    L_0x01b4:
        r6.close();
    L_0x01b7:
        throw r0;
    L_0x01b8:
        r10 = "application";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x01d2;
    L_0x01c0:
        r0 = "http://schemas.android.com/apk/res/android";
        r4 = "name";
        r0 = r7.getAttributeValue(r0, r4);	 Catch:{ Throwable -> 0x01aa }
        r5.applicationClassName = r0;	 Catch:{ Throwable -> 0x01aa }
        r4 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x01aa }
        r0 = 0;
        r4.<init>(r0);	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x01d2:
        r10 = "activity";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 != 0) goto L_0x01ea;
    L_0x01da:
        r10 = "service";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 != 0) goto L_0x01ea;
    L_0x01e2:
        r10 = "receiver";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x0216;
    L_0x01ea:
        r0 = "http://schemas.android.com/apk/res/android";
        r3 = "name";
        r0 = r7.getAttributeValue(r0, r3);	 Catch:{ Throwable -> 0x01aa }
        r3 = ".";
        r3 = r0.startsWith(r3);	 Catch:{ Throwable -> 0x01aa }
        if (r3 == 0) goto L_0x020d;
    L_0x01fa:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x01aa }
        r3.<init>();	 Catch:{ Throwable -> 0x01aa }
        r4 = r5.packageName;	 Catch:{ Throwable -> 0x01aa }
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x01aa }
        r0 = r3.append(r0);	 Catch:{ Throwable -> 0x01aa }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x01aa }
    L_0x020d:
        r4 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x01aa }
        r3 = 0;
        r4.<init>(r3);	 Catch:{ Throwable -> 0x01aa }
        r3 = r0;
        goto L_0x0049;
    L_0x0216:
        r10 = "intent-filter";
        r10 = r0.equals(r10);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x0225;
    L_0x021e:
        r1 = new android.content.IntentFilter;	 Catch:{ Throwable -> 0x01aa }
        r1.<init>();	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x0225:
        r10 = "action";
        r10 = r0.equals(r10);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x023a;
    L_0x022d:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "name";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        r1.addAction(r0);	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x023a:
        r10 = "category";
        r10 = r0.equals(r10);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x024f;
    L_0x0242:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "name";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        r1.addCategory(r0);	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x024f:
        r10 = "data";
        r0 = r0.equals(r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x0049;
    L_0x0257:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "scheme";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        r10 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 != 0) goto L_0x0049;
    L_0x0265:
        r1.addDataScheme(r0);	 Catch:{ Throwable -> 0x01aa }
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "mimeType";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x0275;
    L_0x0272:
        r1.addDataType(r0);	 Catch:{ Throwable -> 0x01aa }
    L_0x0275:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "host";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x028a;
    L_0x027f:
        r10 = "http://schemas.android.com/apk/res/android";
        r11 = "port";
        r10 = r7.getAttributeValue(r10, r11);	 Catch:{ Throwable -> 0x01aa }
        r1.addDataAuthority(r0, r10);	 Catch:{ Throwable -> 0x01aa }
    L_0x028a:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "path";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x0298;
    L_0x0294:
        r10 = 0;
        r1.addDataPath(r0, r10);	 Catch:{ Throwable -> 0x01aa }
    L_0x0298:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "pathPrefix";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x02a6;
    L_0x02a2:
        r10 = 1;
        r1.addDataPath(r0, r10);	 Catch:{ Throwable -> 0x01aa }
    L_0x02a6:
        r0 = "http://schemas.android.com/apk/res/android";
        r10 = "pathPattern";
        r0 = r7.getAttributeValue(r0, r10);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x02b4;
    L_0x02b0:
        r10 = 2;
        r1.addDataPath(r0, r10);	 Catch:{ Throwable -> 0x01aa }
    L_0x02b4:
        r4.add(r1);	 Catch:{ Throwable -> 0x01aa }
        goto L_0x0049;
    L_0x02b9:
        r0 = r7.getName();	 Catch:{ Throwable -> 0x01aa }
        r10 = "activity";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 != 0) goto L_0x02d5;
    L_0x02c5:
        r10 = "service";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 != 0) goto L_0x02d5;
    L_0x02cd:
        r10 = "receiver";
        r10 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r10 == 0) goto L_0x02e3;
    L_0x02d5:
        if (r4 == 0) goto L_0x02e0;
    L_0x02d7:
        r0 = r4.isEmpty();	 Catch:{ Throwable -> 0x01aa }
        if (r0 != 0) goto L_0x02e0;
    L_0x02dd:
        r5.addComponents(r3, r4);	 Catch:{ Throwable -> 0x01aa }
    L_0x02e0:
        r3 = r2;
        goto L_0x0049;
    L_0x02e3:
        r10 = "intent-filter";
        r0 = r10.equals(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x0049;
    L_0x02eb:
        if (r3 == 0) goto L_0x0312;
    L_0x02ed:
        r0 = "android.intent.action.MAIN";
        r0 = r1.matchAction(r0);	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x030f;
    L_0x02f5:
        r0 = r1.categoriesIterator();	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x030f;
    L_0x02fb:
        r1 = r0.hasNext();	 Catch:{ Throwable -> 0x01aa }
        if (r1 == 0) goto L_0x030f;
    L_0x0301:
        r1 = "android.intent.category.LAUNCHER";
        r10 = r0.next();	 Catch:{ Throwable -> 0x01aa }
        r1 = r1.equals(r10);	 Catch:{ Throwable -> 0x01aa }
        if (r1 == 0) goto L_0x02fb;
    L_0x030d:
        r5.launcherClassName = r3;	 Catch:{ Throwable -> 0x01aa }
    L_0x030f:
        r1 = r2;
        goto L_0x0049;
    L_0x0312:
        r0 = r5.applicationClassName;	 Catch:{ Throwable -> 0x01aa }
        if (r0 == 0) goto L_0x030f;
    L_0x0316:
        r5.logicIntentFilters = r4;	 Catch:{ Throwable -> 0x01aa }
        goto L_0x030f;
    L_0x0319:
        r0 = "fuck";
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x01aa }
        r1.<init>();	 Catch:{ Throwable -> 0x01aa }
        r2 = "time = ";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x01aa }
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x01aa }
        r2 = r2 - r8;
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x01aa }
        r2 = ",path = ";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x01aa }
        r1 = r1.append(r13);	 Catch:{ Throwable -> 0x01aa }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x01aa }
        r2 = 0;
        r2 = new java.lang.Object[r2];	 Catch:{ Throwable -> 0x01aa }
        com.mqunar.tools.log.QLog.i(r0, r1, r2);	 Catch:{ Throwable -> 0x01aa }
        if (r7 == 0) goto L_0x0348;
    L_0x0345:
        r7.close();
    L_0x0348:
        if (r6 == 0) goto L_0x034d;
    L_0x034a:
        r6.close();
    L_0x034d:
        return r5;
    L_0x034e:
        r1 = move-exception;
        goto L_0x006d;
    L_0x0351:
        r1 = move-exception;
        goto L_0x0173;
    L_0x0354:
        r4 = move-exception;
        r0 = r2;
        goto L_0x0068;
    L_0x0358:
        r0 = r2;
        goto L_0x0101;
    L_0x035b:
        r6 = r0;
        r7 = r1;
        goto L_0x0038;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.core.ModuleParser.parseApk(java.lang.String):com.mqunar.core.ApkInfo");
    }

    public static boolean checkEquals(ZipFile zipFile, ZipEntry zipEntry, File file) {
        try {
            Object md5 = getMd5(zipFile.getInputStream(zipEntry));
            if (TextUtils.isEmpty(md5)) {
                throw new RuntimeException("文件未读完!");
            }
            try {
                return md5.equals(getMd5(file));
            } catch (Throwable th) {
                return false;
            }
        } catch (Throwable th2) {
            RuntimeException runtimeException = new RuntimeException(th2);
        }
    }

    public static String getMetaData(Context context, String str) {
        XmlResourceParser openXmlResourceParser;
        AssetManager assetManager;
        Throwable th;
        AssetManager assetManager2 = null;
        Bundle bundle = context.getApplicationInfo().metaData;
        if (bundle != null && bundle.containsKey(str)) {
            return bundle.getString(str);
        }
        try {
            AssetManager assetManager3 = (AssetManager) AssetManager.class.newInstance();
            try {
                Method declaredMethod = AssetManager.class.getDeclaredMethod("addAssetPath", new Class[]{String.class});
                declaredMethod.setAccessible(true);
                int intValue = ((Integer) declaredMethod.invoke(assetManager3, new Object[]{context.getApplicationInfo().sourceDir})).intValue();
                if (intValue != 0) {
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
                                    } else if (!openXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", AIUIConstant.KEY_NAME).equals(str)) {
                                        intValue = 1;
                                        break;
                                    } else {
                                        String attributeValue = openXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "value");
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
}
