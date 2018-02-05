package com.huawei.hwid.openapi.c;

import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.e.b;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.ResReqHandler;
import com.huawei.hwid.openapi.quicklogin.a;

class d extends ResReqHandler {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void onComplete(Bundle bundle) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(a.a, "userInfoRequest onComplete " + this.a.b.h);
        try {
            if (this.a.b.h) {
                this.a.b.h = false;
                this.a.b.j();
                if (bundle == null) {
                    this.a.a();
                    this.a.b.a(null, "");
                    this.a.b.g.a(new NullPointerException("null return"));
                } else if (OutReturn.isRequestSuccess(bundle)) {
                    com.huawei.hwid.openapi.quicklogin.d.b.d.b(a.a, "getUserInfo onComplete success!");
                    String retContent = OutReturn.getRetContent(bundle);
                    this.a.e = h.a(retContent);
                    String valueOf = String.valueOf(this.a.e.get("userName"));
                    String b = b.b(this.a.b.e, "loginUserName", "");
                    if (TextUtils.isEmpty(b)) {
                        b = valueOf;
                    }
                    b = retContent.substring(0, retContent.length() - 1) + ",\"" + "loginUserName" + "\":\"" + b + "\"}";
                    this.a.e = h.a(b);
                    this.a.b.a(b);
                    if (this.a.d) {
                        b.a(this.a.b.e, "userInfo_Default", b);
                    }
                    this.a.e.put("accesstoken", OpenHwID.getAccessToken(this.a.b.e.getApplicationContext(), a.a().b(), null, a.a().c()));
                    com.huawei.hwid.openapi.b.d.a(this.a.b.e, a.a().b(), "expire", this.a.a);
                    this.a.b.a(this.a.e);
                    this.a.b.a(bundle, valueOf);
                } else if (a.l < a.m) {
                    a.h();
                    com.huawei.hwid.openapi.b.d.b(this.a.b.e, a.a().b(), null, null);
                    this.a.b.a(a.a().c());
                } else {
                    this.a.a();
                    this.a.b.g.a(bundle, 1007, null);
                    this.a.b.a(bundle, "");
                }
            }
        } catch (Exception e) {
            a(e);
        } catch (Exception e2) {
            a(e2);
        } catch (Exception e22) {
            a(e22);
        } catch (Exception e222) {
            a(e222);
        } catch (Exception e2222) {
            a(e2222);
        } catch (Exception e22222) {
            a(e22222);
        } catch (Exception e222222) {
            a(e222222);
        }
    }

    private void a(Exception exception) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(a.a, exception.getMessage(), exception);
        this.a.a();
        this.a.b.j();
        this.a.b.g.a(exception);
        this.a.b.a(null, "");
    }
}
