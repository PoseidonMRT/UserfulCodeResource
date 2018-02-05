package com.mqunar.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.mqunar.core.DexPathList;
import com.mqunar.core.ModuleParser;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.dependency.AtomNode;
import com.mqunar.core.dependency.ComponentInfo;
import com.mqunar.dispatcher.DispatcherLogic;
import com.mqunar.json.JsonUtils;
import com.mqunar.module.ModuleInfo.DexInfo;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class a extends BroadcastReceiver {
    private List<ModuleInfo> a = new LinkedList();

    a() {
    }

    public void onReceive(Context context, Intent intent) {
        new b(this, context, intent).start();
    }

    private void a(Context context, Intent intent) {
        Object stringExtra = intent.getStringExtra("filePath");
        Object stringExtra2 = intent.getStringExtra("preFilepPath");
        if (ModuleInfoController.f != null) {
            ModuleInfo moduleInfo;
            String toJsonString;
            Object obj;
            ModuleInfo moduleInfo2;
            String stringExtra3 = intent.getStringExtra("scheme");
            Map mapCopy = ModuleInfoController.modules.mapCopy();
            Object obj2 = TextUtils.isEmpty(stringExtra) ? stringExtra2 : stringExtra;
            ModuleInfo moduleInfo3 = null;
            for (String toJsonString2 : mapCopy.keySet()) {
                moduleInfo = (ModuleInfo) mapCopy.get(toJsonString2);
                if (moduleInfo.fileName.equals(obj2)) {
                    moduleInfo.dependency.checkFlag = 1;
                    moduleInfo3 = moduleInfo;
                    break;
                }
            }
            List list = null;
            if (moduleInfo3 == null) {
                Object obj3;
                File[] listFiles = new File(ModuleInfoController.d).listFiles();
                if (listFiles != null) {
                    for (int i = 0; i < listFiles.length; i++) {
                        if (listFiles[i].isFile()) {
                            String absolutePath = listFiles[i].getAbsolutePath();
                            for (ModuleInfo moduleInfo4 : this.a) {
                                if (absolutePath.equals(moduleInfo4.fileName)) {
                                    obj3 = 1;
                                    break;
                                }
                            }
                            obj3 = null;
                            if (obj3 == null) {
                                for (String toJsonString22 : mapCopy.keySet()) {
                                    moduleInfo4 = (ModuleInfo) mapCopy.get(toJsonString22);
                                    if (absolutePath.equals(moduleInfo4.fileName)) {
                                        break;
                                    }
                                }
                                moduleInfo4 = null;
                                if (moduleInfo4 == null) {
                                    try {
                                        moduleInfo4 = ModuleParser.parseModuleDependency(absolutePath);
                                    } catch (Throwable th) {
                                        QLog.crash(th, "parse error in onReceive");
                                        return;
                                    }
                                    if (moduleInfo4 != null) {
                                        moduleInfo4.isDataFile = true;
                                        this.a.add(moduleInfo4);
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
                List arrayList = new ArrayList();
                List<ModuleInfo> arrayList2 = new ArrayList();
                for (ModuleInfo moduleInfo42 : this.a) {
                    arrayList2.add(moduleInfo42);
                    if (moduleInfo42.dependency.checkFlag != -22) {
                        moduleInfo42.dependency.checkFlag = 0;
                    }
                }
                for (ModuleInfo moduleInfo422 : mapCopy.values()) {
                    moduleInfo422.dependency.checkFlag = 1;
                    if (moduleInfo422.isDataFile) {
                        arrayList2.add(moduleInfo422);
                    } else {
                        arrayList.add(moduleInfo422);
                    }
                }
                List<AtomNode> a = ModuleInfoController.b((List) arrayList2);
                List<ModuleInfo> linkedList = new LinkedList();
                do {
                    obj = 1;
                    for (AtomNode atomNode : a) {
                        if (atomNode.needCheck()) {
                            obj = null;
                            atomNode.getCanLoadDependency(ModuleInfoController.f, arrayList, a, null);
                        }
                        obj = obj;
                    }
                } while (obj == null);
                for (AtomNode atomNode2 : a) {
                    moduleInfo422 = atomNode2.getCanLoadDependency(ModuleInfoController.f, arrayList, a, null);
                    if (moduleInfo422 != null) {
                        linkedList.add(moduleInfo422);
                    }
                }
                obj = null;
                for (ModuleInfo moduleInfo4222 : linkedList) {
                    this.a.remove(moduleInfo4222);
                    try {
                        if (ModuleInfoController.b(moduleInfo4222)) {
                            obj = 1;
                            ModuleInfoController.modules.put(moduleInfo4222);
                        }
                        obj3 = obj;
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        obj3 = obj;
                        QLog.crash(th3, "update error");
                    }
                    obj = obj3;
                }
                if (obj != null) {
                    QunarApkLoader.onRegisterOk();
                }
                if (!linkedList.isEmpty()) {
                    List arrayList3 = new ArrayList();
                    String dexCachePath = QunarApkLoader.getDexCachePath(context);
                    Map mapCopy2 = ModuleInfoController.modules.mapCopy();
                    for (String toJsonString222 : mapCopy2.keySet()) {
                        moduleInfo4222 = (ModuleInfo) mapCopy2.get(toJsonString222);
                        File file = new File(dexCachePath);
                        arrayList3.add(DexPathList.optimizedPathFor(new File(moduleInfo4222.fileName), file));
                        if (moduleInfo4222.dexList != null) {
                            for (DexInfo dexInfo : moduleInfo4222.dexList) {
                                arrayList3.add(DexPathList.optimizedPathFor(dexInfo.dexOutPath, file));
                            }
                        }
                    }
                    File[] listFiles2 = new File(dexCachePath).listFiles();
                    if (listFiles2 != null && listFiles2.length > 0) {
                        for (File file2 : listFiles2) {
                            if (!arrayList3.contains(file2.getAbsolutePath())) {
                                file2.delete();
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(stringExtra3)) {
                    for (ModuleInfo moduleInfo42222 : arrayList2) {
                        if (moduleInfo42222.fileName.equals(obj2)) {
                            moduleInfo2 = moduleInfo42222;
                            break;
                        }
                    }
                }
                moduleInfo2 = moduleInfo3;
                for (ModuleInfo moduleInfo422222 : arrayList2) {
                    if (moduleInfo422222.dependency.checkFlag == -22) {
                        Intent intent2 = new Intent("com.mqunar.spider.MESSAGE_NEED_KILL_PROCESS");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
                        list = arrayList2;
                        break;
                    }
                }
                list = arrayList2;
            } else {
                moduleInfo2 = moduleInfo3;
            }
            if (!TextUtils.isEmpty(stringExtra3)) {
                Bundle bundleExtra = intent.getBundleExtra("bundle");
                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(stringExtra3));
                if (bundleExtra != null) {
                    intent3.putExtras(bundleExtra);
                }
                intent3.addFlags(268435456);
                if (moduleInfo2 == null) {
                    DispatcherLogic.openWebView(context, intent3, intent3.getData(), true);
                    return;
                }
                intent3.setPackage(context.getPackageName());
                if (moduleInfo2.dependency.checkFlag == 1 || moduleInfo2.dependency.checkFlag == -22) {
                    DispatcherLogic.processIntentNotLog(context, intent3);
                } else if (moduleInfo2.dependency.checkFlag == -10 || moduleInfo2.dependency.checkFlag == -11) {
                    DispatcherLogic.openWebView(context, intent3, intent3.getData(), true);
                } else {
                    Set hashSet = new HashSet(1);
                    List<Object> mInfoList = ModuleInfoController.getMInfoList();
                    for (ComponentInfo componentInfo : moduleInfo2.dependency.getFailAtomList(r1)) {
                        for (ModuleInfo moduleInfo22 : this.a) {
                            if (moduleInfo22.dependency.packageName.equals(componentInfo.packageName)) {
                                obj = 1;
                                break;
                            }
                        }
                        obj = null;
                        if (obj == null && !ModuleInfoController.modules.hasItem(componentInfo.packageName)) {
                            for (Object obj4 : mInfoList) {
                                if (obj4.packageName.equals(componentInfo.packageName)) {
                                    break;
                                }
                            }
                            obj4 = null;
                            if (obj4 != null) {
                                hashSet.add(obj4);
                            }
                        }
                    }
                    if (hashSet.isEmpty()) {
                        DispatcherLogic.openWebView(context, intent3, intent3.getData(), true);
                        return;
                    }
                    Intent intent4 = new Intent("com.mqunar.spider.MESSAGE_INSTRUCTION_MINFO_SERVER");
                    toJsonString222 = JsonUtils.toJsonString(hashSet);
                    Bundle bundle = new Bundle();
                    bundle.putString("command", toJsonString222);
                    bundle.putString("scheme", stringExtra3);
                    if (TextUtils.isEmpty(stringExtra2)) {
                        bundle.putString("preFilepPath", stringExtra);
                    } else {
                        bundle.putString("preFilepPath", stringExtra2);
                    }
                    if (bundleExtra != null) {
                        bundle.putBundle("bundle", bundleExtra);
                    }
                    intent4.putExtras(bundle);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent4);
                }
            }
        }
    }
}
