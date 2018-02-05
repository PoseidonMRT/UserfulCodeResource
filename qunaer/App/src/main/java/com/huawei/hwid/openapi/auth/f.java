package com.huawei.hwid.openapi.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.out.IHwIDCallBack;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import com.huawei.hwid.openapi.quicklogin.d.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class f {
    private static List f = new ArrayList();
    Toast a = null;
    private AlertDialog b = null;
    private IHwIDCallBack c;
    private boolean d = false;
    private Activity e;
    private int g = 1;

    public f(IHwIDCallBack iHwIDCallBack, Activity activity) {
        this.c = iHwIDCallBack;
        this.e = activity;
        a(new j());
    }

    public void a(boolean z) {
        this.d = z;
    }

    public void a() {
        d.a("AuthHelper", "unregisterBroadcast");
        if (f != null && !f.isEmpty()) {
            try {
                for (j unregisterReceiver : f) {
                    this.e.getApplicationContext().unregisterReceiver(unregisterReceiver);
                }
            } catch (Throwable e) {
                d.b("AuthHelper", e.getMessage(), e);
            }
            f.clear();
        }
    }

    public void a(j jVar) {
        d.a("AuthHelper", "registerBroadcast");
        a();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huawei.hwid.opensdk.game.request.result");
        this.e.getApplicationContext().registerReceiver(jVar, intentFilter);
        f.add(jVar);
    }

    public void a(int i) {
        if (this.a == null) {
            this.a = Toast.makeText(this.e, i, 0);
        } else {
            this.a.setText(i);
        }
        this.a.show();
    }

    private void a(HashMap hashMap) {
        d.b("AuthHelper", "==onUserInfoCallBack==");
        if (this.c != null) {
            this.c.onUserInfo(hashMap);
        } else {
            d.b("AuthHelper", "onUserInfoCallBack, mCallBack is null");
        }
    }

    public void a(Exception exception) {
        if (exception == null) {
            exception = new NullPointerException("unknown error!!");
        }
        d.b("AuthHelper", exception.getMessage(), exception);
        OpenHwID.logout();
        if (this.c != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("loginStatus", "3");
            a(hashMap);
        }
    }

    public void b() {
        d.b("AuthHelper", "enter onUserCancel()");
        OpenHwID.logout();
        if (this.c != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("loginStatus", "0");
            a(hashMap);
        }
    }

    public void a(int i, Bundle bundle) {
        d.b("AuthHelper", "net work err!!");
        a(this.e.getString(b.a(this.e, "ql_network_abnormally")), i, true);
    }

    public void a(String str, int i, boolean z) {
        OnClickListener iVar = new i(this, i);
        OnClickListener hVar = new h();
        if (z) {
            this.b = new Builder(this.e).setMessage(str).setNegativeButton(b.a(this.e, "ql_cacel"), hVar).setPositiveButton(b.a(this.e, "ql_retry"), iVar).show();
            this.b.setCancelable(false);
            return;
        }
        this.b = new Builder(this.e).setMessage(str).setNegativeButton(b.a(this.e, "ql_cacel"), hVar).show();
        this.b.setCancelable(false);
    }

    private int a(Bundle bundle, String str, int i) {
        if (bundle != null) {
            try {
                String valueOf = String.valueOf(bundle.get(str));
                if (!"null".equalsIgnoreCase(valueOf)) {
                    i = Integer.parseInt(valueOf);
                }
            } catch (Throwable th) {
                d.b("AuthHelper", th.getMessage(), th);
            }
        }
        return i;
    }

    public void a(Bundle bundle, int i, Bundle bundle2) {
        d.b("AuthHelper", "come into errDepose");
        String nspstatus = OutReturn.getNSPSTATUS(bundle);
        int retCode = OutReturn.getRetCode(bundle);
        int a = a(bundle, ParamStr.RET_RES_ERROR, 0);
        if ("6".equals(nspstatus) || "102".equals(nspstatus)) {
            if (this.g < 3) {
                this.g++;
                OpenHwID.logout();
                OpenHwID.login(null);
                return;
            }
            a(retCode, bundle, i, bundle2);
        } else if (2 == retCode || 1107 == a) {
            b();
        } else {
            a(retCode, bundle, i, bundle2);
        }
    }

    public void a(int i, Bundle bundle, int i2, Bundle bundle2) {
        d.d("AuthHelper", "isErrCallback =" + this.d + ",rettCode =" + i);
        this.g = 0;
        if (this.d) {
            this.c.onUserInfo(b(i, bundle));
        } else if (100 == i) {
            c(i2, bundle2);
        } else if (b(i)) {
            a(i, i2, bundle2);
        } else if (c(i)) {
            b(i, i2, bundle2);
        } else if (102 == i) {
            a(i2, bundle2);
        } else if (Integer.parseInt("9") == i) {
            this.c.onUserInfo(b(i, bundle));
        }
    }

    private boolean b(int i) {
        return Ret_code.ERR_OPENGW_SSL_FAILED == i || Ret_code.ERR_OPENGW_TIMEOUT_FAILED == i || 1003 == i || 1002 == i || 1001 == i || Ret_code.ERR_OPENGW_ST_TIMEOUT_FAILED == i || Ret_code.ERR_OPENGW_AT_TIMEOUT_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_TIMEOUT_FAILED == i || Ret_code.ERR_OPENGW_TMPST_SSL_FAILED == i || Ret_code.ERR_OPENGW_AT_SSL_HANGSHAKE_FAILED == i || Ret_code.ERR_OPENGW_AT_SSL_TIME_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_SSL_HANGSHAKE_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_SSL_TIME_FAILED == i;
    }

    private boolean c(int i) {
        return d(i) || Ret_code.ERR_OPENGW_AT_RSP_1101_FAILED == i || Ret_code.ERR_OPENGW_AT_RSP_1102_FAILED == i || Ret_code.ERR_OPENGW_AT_RSP_1107_FAILED == i || Ret_code.ERR_OPENGW_AT_RSP_1202_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_RSP_1101_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_RSP_105_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_RSP_1107_FAILED == i || Ret_code.ERR_OPENGW_USERINFO_RSP_102_FAILED == i || 1000 == i || 2000 == i;
    }

    private boolean d(int i) {
        return Ret_code.ERR_OPENGW_FAILED_1201 == i || Ret_code.ERR_OPENGW_FAILED_1401 == i || Ret_code.ERR_OPENGW_TMPST_400 == i || Ret_code.ERR_OPENGW_AT_400 == i || Ret_code.ERR_OPENGW_USERINFO_400 == i;
    }

    private HashMap b(int i, Bundle bundle) {
        HashMap hashMap = new HashMap();
        if (6 == i) {
            hashMap.put("loginStatus", "6");
            hashMap.put("loginResult", "ServiceToken invalid,need re-auth");
        } else if (4 == i) {
            hashMap.put("loginStatus", "4");
            hashMap.put("loginResult", "The account of HwID has not login, cannot login using AIDL");
        } else if (5 == i) {
            hashMap.put("loginStatus", "5");
            hashMap.put("loginResult", "Your app's signature is invalid, contact the admin");
        } else if (100 == i) {
            hashMap.put("loginStatus", "8");
            hashMap.put("loginResult", "System Error");
        } else if (102 == i) {
            hashMap.put("loginStatus", "7");
            hashMap.put("loginResult", "Network Error");
        } else {
            hashMap.put("loginStatus", Integer.valueOf(i));
            hashMap.put("loginResult", bundle.getString(ParamStr.Err_Info, ""));
        }
        return hashMap;
    }

    private void c(int i, Bundle bundle) {
        d.b("AuthHelper", "onSysErr err!!");
        a(this.e.getString(b.a(this.e, "CS_ERR_for_system_error")), i, true);
    }

    private void a(int i, int i2, Bundle bundle) {
        d.b("AuthHelper", "onServerTimeout err!!");
        a(this.e.getString(b.a(this.e, "CS_ERR_for_unable_connect_server")) + "(" + String.valueOf(i).replaceFirst("907114", "") + ")", i2, true);
    }

    private void b(int i, int i2, Bundle bundle) {
        d.b("AuthHelper", "onReqServerFailed err!!");
        a(this.e.getString(b.a(this.e, "CS_ERR_for_unable_get_data")) + "(" + String.valueOf(i).replaceFirst("907114", "") + ")", i2, false);
    }
}
