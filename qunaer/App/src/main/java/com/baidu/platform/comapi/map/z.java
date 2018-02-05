package com.baidu.platform.comapi.map;

import android.os.Message;
import com.baidu.mapapi.UIMsg.m_AppUI;

class z {
    private static final String a = z.class.getSimpleName();
    private y b;

    z() {
    }

    void a(Message message) {
        if (message.what == m_AppUI.V_WM_VDATAENGINE) {
            switch (message.arg1) {
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 12:
                case 101:
                case 102:
                    if (this.b != null) {
                        this.b.a(message.arg1, message.arg2);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    void a(y yVar) {
        this.b = yVar;
    }

    void b(y yVar) {
        this.b = null;
    }
}
