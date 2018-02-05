package com.huawei.hwid.openapi.b;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.mapapi.UIMsg.f_FUN;
import com.huawei.hwid.openapi.d.a;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import com.huawei.hwid.openapi.out.ResReqHandler;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.io.UnsupportedEncodingException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

class b extends Thread {
    private static String d = com.huawei.hwid.openapi.a.b.a;
    Context a = null;
    a b = null;
    ResReqHandler c = null;

    b(Context context, a aVar, ResReqHandler resReqHandler) {
        this.a = context;
        this.b = aVar;
        this.c = resReqHandler;
    }

    public void run() {
        Bundle bundle = null;
        for (int i = 0; i < 3; i++) {
            try {
                String b = this.b.b();
                HttpResponse a = com.huawei.hwid.openapi.e.b.a.a(this.a, this.b.d());
                int statusCode = a.getStatusLine().getStatusCode();
                if (statusCode >= HttpStatus.SC_BAD_REQUEST) {
                    bundle = a(statusCode);
                } else {
                    bundle = this.b.a(a);
                    if (HttpStatus.SC_MOVED_TEMPORARILY == OutReturn.getRetResCode(bundle)) {
                        String a2 = a(bundle);
                        if (!TextUtils.isEmpty(a2)) {
                            if (a2.startsWith("oob#")) {
                                bundle = a(a2);
                                break;
                            } else {
                                c.a(this.a, new com.huawei.hwid.openapi.d.a.a(a2), this.c);
                                return;
                            }
                        }
                    }
                    if (this.b.a(bundle, b)) {
                        bundle.putInt(ParamStr.RET_CODE, 1);
                        break;
                    }
                    b(bundle);
                    d.b(d, "bundle info:" + k.a(bundle.toString(), true));
                    d.b(d, "nsp info:" + OutReturn.getNSPSTATUS(bundle));
                }
            } catch (RuntimeException e) {
                bundle = a(e);
            } catch (Exception e2) {
                bundle = a(e2);
            }
        }
        this.c.finish(bundle);
    }

    private Bundle a(String str) {
        Bundle b = com.huawei.hwid.openapi.quicklogin.b.b.b.a.b(str.replace("oob#", ""));
        if (str.indexOf(ParamStr.ACCESS_TOKEN) != -1) {
            b.putInt(ParamStr.RET_CODE, 1);
        } else if (Integer.parseInt(b.getString(ParamStr.RET_RES_ERROR)) == 1202) {
            b.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_RSP_1202_FAILED);
        } else {
            b.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_TIMEOUT_FAILED);
        }
        return b;
    }

    private String a(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle(ParamStr.RET_RES_HEAD);
        if (bundle2 == null) {
            c.b(d, "bundle.getBundle is null");
        }
        if (bundle2 != null) {
            return bundle2.getString("Location");
        }
        return null;
    }

    private Bundle a(RuntimeException runtimeException) {
        if ((runtimeException instanceof IllegalArgumentException) || (runtimeException instanceof IllegalStateException)) {
            return OutReturn.creatReturn(1001, runtimeException.toString());
        }
        if (this.b instanceof com.huawei.hwid.openapi.d.a.a) {
            return OutReturn.creatReturn(Ret_code.ERR_OPENGW_AT_TIMEOUT_FAILED, runtimeException.toString());
        }
        if (this.b instanceof com.huawei.hwid.openapi.d.a.b) {
            return OutReturn.creatReturn(Ret_code.ERR_OPENGW_USERINFO_TIMEOUT_FAILED, runtimeException.toString());
        }
        return OutReturn.creatReturn(Ret_code.ERR_OPENGW_TIMEOUT_FAILED, runtimeException.toString());
    }

    private void b(Bundle bundle) {
        bundle.putInt(ParamStr.RET_CODE, 2000);
        if (this.b instanceof com.huawei.hwid.openapi.d.a.a) {
            String retContent = OutReturn.getRetContent(bundle);
            if (OutReturn.getErrorCode(retContent).equals(String.valueOf(f_FUN.FUN_ID_SCH_POI))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_RSP_1101_FAILED);
            } else if (OutReturn.getErrorCode(retContent).equals(String.valueOf(f_FUN.FUN_ID_SCH_NAV))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_RSP_1102_FAILED);
            } else if (OutReturn.getErrorCode(retContent).equals(String.valueOf(1107))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_RSP_1107_FAILED);
            } else if (OutReturn.getErrorCode(retContent).equals(String.valueOf(1202))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_AT_RSP_1202_FAILED);
            } else {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_ST_TIMEOUT_FAILED);
            }
        } else if (!(this.b instanceof com.huawei.hwid.openapi.d.a.b)) {
        } else {
            if (OutReturn.getNSPSTATUS(bundle).equals(String.valueOf(102))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_USERINFO_RSP_102_FAILED);
            } else if (OutReturn.getNSPSTATUS(bundle).equals(String.valueOf(105))) {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_USERINFO_RSP_105_FAILED);
            } else {
                bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_USERINFO_TIMEOUT_FAILED);
            }
        }
    }

    private Bundle a(Exception exception) {
        Bundle creatReturn;
        d.b(d, exception.getMessage(), exception);
        if ((exception instanceof UnsupportedEncodingException) || (exception instanceof IllegalArgumentException) || (exception instanceof IllegalStateException)) {
            creatReturn = OutReturn.creatReturn(1001, exception.toString());
        } else if (exception instanceof SSLHandshakeException) {
            if (this.b instanceof com.huawei.hwid.openapi.d.a.a) {
                creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_AT_SSL_HANGSHAKE_FAILED, exception.toString());
            } else if (this.b instanceof com.huawei.hwid.openapi.d.a.b) {
                creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_USERINFO_SSL_HANGSHAKE_FAILED, exception.toString());
            } else {
                creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_SSL_FAILED, exception.toString());
            }
        } else if (this.b instanceof com.huawei.hwid.openapi.d.a.a) {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_AT_TIMEOUT_FAILED, exception.toString());
        } else if (this.b instanceof com.huawei.hwid.openapi.d.a.b) {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_USERINFO_TIMEOUT_FAILED, exception.toString());
        } else {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_TIMEOUT_FAILED, exception.toString());
        }
        d.d(d, "debug:" + k.a(creatReturn));
        return creatReturn;
    }

    private Bundle a(int i) {
        if (this.b instanceof com.huawei.hwid.openapi.d.a.a) {
            return OutReturn.creatReturn(Ret_code.ERR_OPENGW_AT_400, "http error:" + i);
        }
        if (this.b instanceof com.huawei.hwid.openapi.d.a.b) {
            return OutReturn.creatReturn(Ret_code.ERR_OPENGW_USERINFO_400, "http error:" + i);
        }
        return OutReturn.creatReturn(Ret_code.ERR_UP_400_FAILED, "http error:" + i);
    }
}
