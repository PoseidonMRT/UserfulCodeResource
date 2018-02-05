package com.baidu.platform.comapi.map;

import android.content.Context;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.NetworkUtil;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.platform.comapi.NativeLoader;
import com.baidu.platform.comapi.commonutils.SysUpdateUtil;
import com.baidu.platform.comapi.commonutils.a;
import com.baidu.platform.comapi.util.SysUpdateObservable;
import com.baidu.platform.comjni.engine.AppEngine;
import com.baidu.vi.VMsg;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class C0009i {
    private static int a;
    private static Context b = BMapManager.getContext();

    static {
        if (VersionInfo.getApiVersion().equals(VersionInfo.getApiVersion())) {
            NativeLoader.getInstance().loadLibrary(VersionInfo.getKitName());
            AppEngine.InitClass();
            C0009i.a(BMapManager.getContext());
            SysUpdateObservable.getInstance().addObserver(new SysUpdateUtil());
            SysUpdateObservable.getInstance().init();
            return;
        }
        throw new BaiduMapSDKException("the version of map is not match with base");
    }

    public static void a() {
        if (a == 0) {
            if (b == null) {
                throw new IllegalStateException("you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
            }
            VMsg.init();
            AppEngine.InitEngine(b);
            AppEngine.StartSocketProc();
            NetworkUtil.updateNetworkProxy(b);
        }
        a++;
    }

    private static void a(Context context) {
        int i = 0;
        if (context != null) {
            try {
                File file = new File(SysOSUtil.getModuleFileName());
                if (!file.exists()) {
                    file.mkdirs();
                }
                context.getAssets();
                String[] strArr = new String[]{"cfg/a/mode_1/map.sdkrs", "cfg/a/mode_1/reduct.sdkrs", "cfg/a/mode_1/traffic.sdkrs", "cfg/a/mode_1/bus.sty", "cfg/a/mode_1/car.sty", "cfg/a/mode_1/cycle.sty", "cfg/a/mode_1/map.sty", "cfg/a/mode_1/reduct.sty", "cfg/a/mode_1/traffic.sty", "cfg/idrres/ResPackIndoorMap.sdkrs", "cfg/idrres/DVIndoor.cfg", "cfg/idrres/baseindoormap.sty", "cfg/a/DVDirectory.cfg", "cfg/a/DVHotcity.cfg", "cfg/a/DVHotMap.cfg", "cfg/a/DVSDirectory.cfg", "cfg/a/DVVersion.cfg", "cfg/a/CustomIndex"};
                String[] strArr2 = new String[]{"cfg/a/CustomIndex"};
                String[] strArr3 = new String[]{"cfg/a/mode_1/map.rs", "cfg/a/mode_1/reduct.rs", "cfg/a/mode_1/traffic.rs", "cfg/a/mode_1/bus.sty", "cfg/a/mode_1/car.sty", "cfg/a/mode_1/cycle.sty", "cfg/a/mode_1/map.sty", "cfg/a/mode_1/reduct.sty", "cfg/a/mode_1/traffic.sty", "cfg/idrres/ResPackIndoorMap.rs", "cfg/idrres/DVIndoor.cfg", "cfg/idrres/baseindoormap.sty", "cfg/a/DVDirectory.cfg", "cfg/a/DVHotcity.cfg", "cfg/a/DVHotMap.cfg", "cfg/a/DVSDirectory.cfg", "cfg/a/DVVersion.cfg", "cfg/a/CustomIndex"};
                String[] strArr4 = new String[]{"cfg/a/CustomIndex"};
                try {
                    int i2;
                    FileOutputStream fileOutputStream;
                    File file2;
                    int i3;
                    File file3 = new File(SysOSUtil.getModuleFileName() + "/ver.dat");
                    byte[] bArr = new byte[]{(byte) 4, (byte) 0, (byte) 3, (byte) 0, (byte) 0, (byte) 0};
                    if (file3.exists()) {
                        FileInputStream fileInputStream = new FileInputStream(file3);
                        byte[] bArr2 = new byte[fileInputStream.available()];
                        fileInputStream.read(bArr2);
                        fileInputStream.close();
                        if (Arrays.equals(bArr2, bArr)) {
                            File file4 = new File(SysOSUtil.getModuleFileName() + "/cfg/a/mode_1/map.sty");
                            if (file4.exists() && file4.length() > 0) {
                                i2 = 0;
                                if (i2 != 0) {
                                    if (file3.exists()) {
                                        file3.delete();
                                    }
                                    file3.createNewFile();
                                    fileOutputStream = new FileOutputStream(file3);
                                    fileOutputStream.write(bArr);
                                    fileOutputStream.close();
                                    file2 = new File(SysOSUtil.getModuleFileName() + "/cfg/a/mode_1");
                                    if (!file2.exists()) {
                                        file2.mkdirs();
                                    }
                                    file2 = new File(SysOSUtil.getModuleFileName() + "/cfg/idrres");
                                    if (!file2.exists()) {
                                        file2.mkdirs();
                                    }
                                }
                                for (i3 = 0; i3 < strArr4.length; i3++) {
                                    a.a(strArr2[i3], strArr4[i3], context);
                                }
                                if (i2 != 0) {
                                    while (i < strArr3.length) {
                                        a.a(strArr[i], strArr3[i], context);
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                    i2 = 1;
                    if (i2 != 0) {
                        if (file3.exists()) {
                            file3.delete();
                        }
                        file3.createNewFile();
                        fileOutputStream = new FileOutputStream(file3);
                        fileOutputStream.write(bArr);
                        fileOutputStream.close();
                        file2 = new File(SysOSUtil.getModuleFileName() + "/cfg/a/mode_1");
                        if (file2.exists()) {
                            file2.mkdirs();
                        }
                        file2 = new File(SysOSUtil.getModuleFileName() + "/cfg/idrres");
                        if (file2.exists()) {
                            file2.mkdirs();
                        }
                    }
                    for (i3 = 0; i3 < strArr4.length; i3++) {
                        a.a(strArr2[i3], strArr4[i3], context);
                    }
                    if (i2 != 0) {
                        while (i < strArr3.length) {
                            a.a(strArr[i], strArr3[i], context);
                            i++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void a(boolean z) {
        C0005e.j(z);
    }

    public static void b() {
        a--;
        if (a == 0) {
            AppEngine.UnInitEngine();
            VMsg.destroy();
        }
    }
}
