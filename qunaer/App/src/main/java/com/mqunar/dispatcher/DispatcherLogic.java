package com.mqunar.dispatcher;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.basectx.QLogic;
import com.mqunar.core.basectx.SchemeDispatcher;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.json.JsonUtils;
import com.mqunar.libtask.ProgressType;
import com.mqunar.module.MInfo;
import com.mqunar.module.ModuleInfo;
import com.mqunar.module.ModuleInfoController;
import com.mqunar.qav.uelog.QAVLog;
import com.mqunar.tools.log.QLog;
import com.mqunar.tools.log.UELog;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.cookie.ClientCookie;

public class DispatcherLogic {
    private static Handler a = new Handler(Looper.getMainLooper());

    public static void updateMapping(byte[] bArr) {
    }

    public static HashMap<String, String> splitParams(Uri uri) {
        if (uri == null) {
            return new HashMap();
        }
        Set<String> queryParameterNames = getQueryParameterNames(uri);
        HashMap<String, String> hashMap = new HashMap(queryParameterNames.size());
        for (String str : queryParameterNames) {
            hashMap.put(str, uri.getQueryParameter(str));
        }
        return hashMap;
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery == null) {
            return Collections.emptySet();
        }
        Set linkedHashSet = new LinkedHashSet();
        int i = 0;
        do {
            int indexOf = encodedQuery.indexOf(38, i);
            if (indexOf == -1) {
                indexOf = encodedQuery.length();
            }
            int indexOf2 = encodedQuery.indexOf(61, i);
            if (indexOf2 > indexOf || indexOf2 == -1) {
                indexOf2 = indexOf;
            }
            linkedHashSet.add(Uri.decode(encodedQuery.substring(i, indexOf2)));
            i = indexOf + 1;
        } while (i < encodedQuery.length());
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public static String gv() {
        Map mapCopy = ModuleInfoController.modules.mapCopy();
        JSONArray jSONArray = new JSONArray();
        for (ModuleInfo moduleInfo : mapCopy.values()) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(ClientCookie.VERSION_ATTR, Integer.valueOf(moduleInfo.localVersion));
            jSONObject.put("laucherClassName", moduleInfo.launcherClassName);
            jSONObject.put("packageName", moduleInfo.name);
            jSONObject.put("fileName", moduleInfo.fileName);
            jSONArray.add(jSONObject);
        }
        return jSONArray.toJSONString();
    }

    public static void processIntent(Object obj, Intent intent) {
        new UELog(c(obj)).logIntent(obj.getClass().getSimpleName(), intent.getDataString());
        new QAVLog(c(obj)).logIntent(obj.getClass().getSimpleName(), intent.getDataString());
        processIntentNotLog(obj, intent);
    }

    public static void processIntentNotLog(Object obj, Intent intent) {
        a.post(new e(intent, obj));
    }

    public static void processIntentInner(Object obj, Intent intent) {
        if (intent == null) {
            d(obj);
            return;
        }
        Uri data = intent.getData();
        if (data == null) {
            d(obj);
            return;
        }
        try {
            Object mappingComponent = SchemeManager.getMappingComponent(c(obj), intent);
            if (TextUtils.isEmpty(mappingComponent) || mappingComponent.contains("://")) {
                Uri uri;
                if (TextUtils.isEmpty(mappingComponent)) {
                    uri = data;
                } else {
                    uri = Uri.parse(mappingComponent);
                }
                Bundle bundle = new Bundle();
                if (a(obj, intent, bundle)) {
                    if (intent.hasExtra(SchemeDispatcher.RESULT_BROADCAST_MESSAGE_ACTION)) {
                        Intent intent2 = new Intent(intent.getStringExtra(SchemeDispatcher.RESULT_BROADCAST_MESSAGE_ACTION));
                        intent2.putExtras(bundle);
                        LocalBroadcastManager.getInstance(c(obj)).sendBroadcast(intent2);
                    } else {
                        Intent intent3 = new Intent();
                        intent3.putExtras(bundle);
                        a(obj, -1, intent3);
                    }
                    d(obj);
                    return;
                }
                String mapping = SchemeManager.mapping(c(obj), intent);
                if (TextUtils.isEmpty(mapping)) {
                    openWebView(obj, intent, uri, true);
                    return;
                } else if (((ModuleInfo) ModuleInfoController.modules.mapCopy().get(mapping)) == null) {
                    QLog.e("spider", "not found package " + mapping, new Object[0]);
                    List<MInfo> mInfoList = ModuleInfoController.getMInfoList();
                    Intent intent4 = new Intent("com.mqunar.spider.MESSAGE_INSTRUCTION_MINFO_SERVER");
                    Bundle extras;
                    if (mInfoList == null || mInfoList.isEmpty()) {
                        bundle = new Bundle();
                        bundle.putString("scheme", uri.toString());
                        bundle.putString("packageName", mapping);
                        extras = intent.getExtras();
                        if (extras != null) {
                            bundle.putBundle("bundle", extras);
                        }
                        intent4.putExtras(bundle);
                        LocalBroadcastManager.getInstance(c(obj)).sendBroadcast(intent4);
                        return;
                    }
                    for (MInfo mInfo : mInfoList) {
                        if (mapping.equals(mInfo.packageName)) {
                            break;
                        }
                    }
                    MInfo mInfo2 = null;
                    if (mInfo2 != null) {
                        mInfo2.downloadFlag = (byte) -1;
                        List arrayList = new ArrayList(1);
                        arrayList.add(mInfo2);
                        String toJsonString = JsonUtils.toJsonString(arrayList);
                        extras = new Bundle();
                        extras.putString("command", toJsonString);
                        extras.putString("scheme", uri.toString());
                        bundle = intent.getExtras();
                        if (bundle != null) {
                            extras.putBundle("bundle", bundle);
                        }
                        intent4.putExtras(extras);
                        LocalBroadcastManager.getInstance(c(obj)).sendBroadcast(intent4);
                        return;
                    }
                    openWebView(obj, intent, uri, true);
                    return;
                } else {
                    openWebView(obj, intent, uri, true);
                    return;
                }
            }
            b(obj, mappingComponent, intent, data);
        } catch (Throwable th) {
            QLog.e(th);
            RuntimeException runtimeException = new RuntimeException(th);
        }
    }

    public static void openWebView(Object obj, Intent intent, Uri uri, boolean z) {
        Uri parse;
        if (z) {
            try {
                parse = Uri.parse("http://touch.qunar.com/appredirect?url=" + URLEncoder.encode(uri.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                parse = uri;
            }
        } else {
            parse = uri;
        }
        QLog.d("spider", "openWebView url is " + uri, new Object[0]);
        Intent intent2 = new Intent();
        intent2.putExtras(intent);
        intent2.putExtra("url", parse.toString());
        intent2.setClass(c(obj), WebActivity.class);
        intent2.addFlags(268435456);
        a(obj, intent2);
        new Thread(new f(uri)).start();
        d(obj);
    }

    private static void b(Object obj, String str, Intent intent, Uri uri) {
        ModuleInfo matchModule = ModuleInfoController.matchModule(str);
        MInfo mInfo = ModuleInfoController.getMInfo(matchModule);
        if (mInfo != null) {
            switch (mInfo.type) {
                case (byte) 2:
                    if (!TextUtils.isEmpty(mInfo.touch)) {
                        uri = Uri.parse(mInfo.touch);
                    }
                    openWebView(obj, intent, uri, false);
                    return;
                case (byte) 3:
                    Toast.makeText(c(obj), "模块已下线", 1).show();
                    d(obj);
                    return;
            }
        }
        if (matchModule != null && !TextUtils.isEmpty(matchModule.fileName)) {
            Intent intent2 = new Intent();
            intent2.setClassName(c(obj), str);
            intent2.putExtras(intent);
            intent2.addFlags(intent.getFlags());
            intent2.setData(uri);
            a(obj, intent2, matchModule);
        } else if (mInfo == null || TextUtils.isEmpty(mInfo.touch)) {
            openWebView(obj, intent, uri, true);
        } else {
            openWebView(obj, intent, Uri.parse(mInfo.touch), false);
        }
    }

    private static void a(Object obj, Intent intent, ModuleInfo moduleInfo) {
        Class loadFromDexs = QunarApkLoader.loadFromDexs(null, intent.getComponent().getClassName());
        Intent intent2;
        if (Activity.class.isAssignableFrom(loadFromDexs)) {
            if (intent.getBooleanExtra(SchemeDispatcher.TARGET_SCHEME_CLEAR_TOP, false)) {
                intent.addFlags(603979776);
            }
            Object stringExtra = intent.getStringExtra(SchemeDispatcher.TARGET_SCHEME_URI);
            if (TextUtils.isEmpty(stringExtra)) {
                int intExtra = intent.getIntExtra(SchemeDispatcher.SPIDER_REQUESTCODE, -1);
                if (intExtra != -1) {
                    a(obj, intent, intExtra);
                } else {
                    a(obj, intent);
                }
            } else {
                Intent intent3 = new Intent();
                intent3.setClass(c(obj), loadFromDexs);
                intent3.setPackage(c(obj).getPackageName());
                intent3.addFlags(603979776);
                a(obj, intent3);
                intent.removeExtra(SchemeDispatcher.TARGET_SCHEME_URI);
                intent2 = new Intent();
                if (intent.getExtras() != null) {
                    intent2.putExtras(intent.getExtras());
                }
                intent2.setData(Uri.parse(stringExtra));
                intent2.setPackage(c(obj).getPackageName());
                a.post(new g(obj, intent2));
            }
            d(obj);
        } else if (Service.class.isAssignableFrom(loadFromDexs)) {
            c(obj).startService(intent);
            d(obj);
        } else if (BroadcastReceiver.class.isAssignableFrom(loadFromDexs)) {
            c(obj).sendBroadcast(intent);
            d(obj);
        } else if (QLogic.class.isAssignableFrom(loadFromDexs)) {
            if (moduleInfo.application != null && (moduleInfo.application instanceof QLogic)) {
                intent2 = ((QLogic) moduleInfo.application).processScheme(intent);
                if (intent2 != null) {
                    a(obj, -1, intent2);
                } else {
                    a(obj, -1);
                }
            }
            d(obj);
        }
    }

    private static boolean a(Object obj, Intent intent, Bundle bundle) {
        Uri data = intent.getData();
        if (data == null) {
            return false;
        }
        String host = data.getHost();
        if (host == null) {
            return false;
        }
        int indexOf = host.indexOf(".");
        if (indexOf == -1) {
            return false;
        }
        if (!"scrimmage".equals(host.substring(0, indexOf))) {
            return false;
        }
        host = data.getLastPathSegment();
        HashMap splitParams = splitParams(data);
        if ("gv".equals(host)) {
            host = gv();
            if (!(bundle == null || host == null)) {
                bundle.putString("command", host);
            }
            return true;
        } else if ("uv".equals(host)) {
            if (bundle == null) {
                return true;
            }
            if (intent != null) {
                host = intent.getStringExtra("command");
            } else {
                host = null;
            }
            Object updateModuleInfo = ModuleInfoController.updateModuleInfo(host);
            if (TextUtils.isEmpty(updateModuleInfo)) {
                bundle.putString("command", "");
            } else {
                bundle.putString("command", updateModuleInfo);
            }
            return true;
        } else if ("us".equals(host)) {
            return true;
        } else {
            if ("rm".equals(host)) {
                host = (String) splitParams.get("ap");
                return true;
            } else if ("lm".equals(host)) {
                host = (String) splitParams.get("pn");
                QLog.d("spider", "packagename is " + host, new Object[0]);
                Map mapCopy = ModuleInfoController.modules.mapCopy();
                ModuleInfo moduleInfo = (ModuleInfo) mapCopy.get(host);
                a(mapCopy);
                if (moduleInfo == null) {
                    QLog.e("spider", "not found package " + host, new Object[0]);
                    List<MInfo> mInfoList = ModuleInfoController.getMInfoList();
                    Intent intent2 = new Intent("com.mqunar.spider.MESSAGE_INSTRUCTION_MINFO_SERVER");
                    Bundle bundle2;
                    Bundle extras;
                    if (mInfoList == null || mInfoList.isEmpty()) {
                        bundle2 = new Bundle();
                        bundle2.putString("scheme", data.toString());
                        bundle2.putString("packageName", host);
                        extras = intent.getExtras();
                        if (extras != null) {
                            bundle2.putBundle("bundle", extras);
                        }
                        intent2.putExtras(bundle2);
                        LocalBroadcastManager.getInstance(c(obj)).sendBroadcast(intent2);
                    } else {
                        for (MInfo mInfo : mInfoList) {
                            if (host.equals(mInfo.packageName)) {
                                break;
                            }
                        }
                        MInfo mInfo2 = null;
                        if (mInfo2 != null) {
                            mInfo2.downloadFlag = (byte) -1;
                            List arrayList = new ArrayList(1);
                            arrayList.add(mInfo2);
                            host = JsonUtils.toJsonString(arrayList);
                            bundle2 = new Bundle();
                            bundle2.putString("command", host);
                            bundle2.putString("scheme", data.toString());
                            extras = intent.getExtras();
                            if (extras != null) {
                                bundle2.putBundle("bundle", extras);
                            }
                            intent2.putExtras(bundle2);
                            LocalBroadcastManager.getInstance(c(obj)).sendBroadcast(intent2);
                        } else {
                            openWebView(obj, intent, data, true);
                        }
                    }
                } else if (TextUtils.isEmpty(moduleInfo.launcherClassName)) {
                    MInfo mInfo3 = ModuleInfoController.getMInfo(host);
                    if (mInfo3 == null || TextUtils.isEmpty(mInfo3.touch)) {
                        openWebView(obj, intent, data, true);
                    } else {
                        openWebView(obj, intent, Uri.parse(mInfo3.touch), false);
                    }
                } else {
                    b(obj, moduleInfo.launcherClassName, intent, data);
                    QLog.d("spider", "found launcher " + moduleInfo.launcherClassName, new Object[0]);
                }
                return true;
            } else if (!"bs".equals(host)) {
                return false;
            } else {
                Uri parse = Uri.parse((String) splitParams.get("url"));
                Intent intent3 = new Intent();
                intent3.setData(parse);
                intent3.addFlags(intent.getFlags());
                intent3.putExtras(intent);
                List a = SchemeManager.a(c(obj), intent3, ProgressType.PRO_END);
                for (indexOf = 0; indexOf < a.size(); indexOf++) {
                    a.post(new h(obj, (String) a.get(indexOf), intent3, parse));
                }
                return true;
            }
        }
    }

    static void a(Map<String, ModuleInfo> map) {
        if (map != null && map.size() > 0) {
            for (Entry entry : map.entrySet()) {
                QLog.d("spider", ((String) entry.getKey()) + " launcher " + ((ModuleInfo) entry.getValue()).launcherClassName, new Object[0]);
            }
        }
    }

    private static Context c(Object obj) {
        if (obj == null) {
            return QApplication.getContext();
        }
        if (!(obj instanceof Fragment)) {
            return (Context) obj;
        }
        obj = ((Fragment) obj).getActivity();
        if (obj != null) {
            return obj;
        }
        return QApplication.getContext();
    }

    private static void d(Object obj) {
        if (obj != null && obj.getClass() == DispatcherActivity.class) {
            ((Activity) obj).finish();
        }
    }

    private static void a(Object obj, int i, Intent intent) {
        if (obj != null && obj.getClass() == DispatcherActivity.class) {
            ((Activity) obj).setResult(i, intent);
        }
    }

    private static void a(Object obj, int i) {
        if (obj != null && obj.getClass() == DispatcherActivity.class) {
            ((Activity) obj).setResult(i);
        }
    }

    private static void a(Object obj, Intent intent) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Fragment) {
            ((Fragment) obj).startActivity(intent);
        } else if (obj instanceof Activity) {
            ((Context) obj).startActivity(intent);
        } else {
            ((Context) obj).startActivity(intent);
        }
    }

    private static void a(Object obj, Intent intent, int i) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Fragment) {
            if ((intent.getFlags() & 33554432) == 33554432) {
                ((Fragment) obj).startActivity(intent);
            } else {
                ((Fragment) obj).startActivityForResult(intent, i);
            }
        } else if (!(obj instanceof Activity)) {
        } else {
            if ((intent.getFlags() & 33554432) == 33554432) {
                ((Activity) obj).startActivity(intent);
            } else {
                ((Activity) obj).startActivityForResult(intent, i);
            }
        }
    }
}
