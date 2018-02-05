package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import com.baidu.mapapi.UIMsg.m_AppUI;

class G extends Handler {
    final /* synthetic */ F a;

    G(F f) {
        this.a = f;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (this.a.f != null && this.a.f.g != null && ((Long) message.obj).longValue() == this.a.f.h) {
            if (message.what == m_AppUI.MSG_APP_SAVESCREEN) {
                for (l lVar : this.a.f.f) {
                    Bitmap bitmap = null;
                    if (message.arg2 == 1) {
                        int[] iArr = new int[(F.a * F.b)];
                        int[] iArr2 = new int[(F.a * F.b)];
                        if (this.a.f.g != null) {
                            int[] a = this.a.f.g.a(iArr, F.a, F.b);
                            for (int i = 0; i < F.b; i++) {
                                for (int i2 = 0; i2 < F.a; i2++) {
                                    int i3 = a[(F.a * i) + i2];
                                    iArr2[(((F.b - i) - 1) * F.a) + i2] = ((i3 & -16711936) | ((i3 << 16) & 16711680)) | ((i3 >> 16) & 255);
                                }
                            }
                            bitmap = Bitmap.createBitmap(iArr2, F.a, F.b, Config.ARGB_8888);
                        } else {
                            return;
                        }
                    }
                    lVar.a(bitmap);
                }
            } else if (message.what == 39) {
                if (this.a.f != null) {
                    if (message.arg1 == 100) {
                        this.a.f.A();
                    } else if (message.arg1 == 200) {
                        this.a.f.K();
                    } else if (message.arg1 == 1) {
                        if (this.a.e != null) {
                            this.a.e.a();
                        }
                    } else if (message.arg1 == 0) {
                        if (this.a.e != null) {
                            this.a.e.a();
                        }
                    } else if (message.arg1 == 2) {
                        for (l lVar2 : this.a.f.f) {
                            lVar2.c();
                        }
                    }
                    if (!this.a.f.i && F.b > 0 && F.a > 0 && this.a.f.b(0, 0) != null) {
                        this.a.f.i = true;
                        for (l lVar22 : this.a.f.f) {
                            lVar22.b();
                        }
                    }
                    for (l lVar222 : this.a.f.f) {
                        lVar222.a();
                    }
                }
            } else if (message.what == 41) {
                if (this.a.f == null) {
                    return;
                }
                if (this.a.f.l || this.a.f.m) {
                    for (l lVar2222 : this.a.f.f) {
                        lVar2222.b(this.a.f.D());
                    }
                }
            } else if (message.what == 999) {
                for (l lVar22222 : this.a.f.f) {
                    lVar22222.e();
                }
            } else if (message.what == 50) {
                for (l lVar222222 : this.a.f.f) {
                    if (message.arg1 == 0) {
                        lVar222222.a(false);
                    } else if (message.arg1 == 1) {
                        lVar222222.a(true);
                    }
                }
            }
        }
    }
}
