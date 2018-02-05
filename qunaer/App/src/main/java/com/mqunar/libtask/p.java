package com.mqunar.libtask;

import android.os.Handler.Callback;
import android.os.Message;
import com.mqunar.network.NetMsgObj;
import com.mqunar.network.NetRequest;

class p implements Callback {
    o a;
    NetRequest b;

    p(o oVar) {
        this.a = oVar;
    }

    public void a(NetRequest netRequest) {
        this.b = netRequest;
    }

    public boolean handleMessage(Message message) {
        if (this.a.getStatus().equals(TaskCode.TASK_CANCEL)) {
            return true;
        }
        NetMsgObj netMsgObj;
        if (message.what == 1) {
            netMsgObj = (NetMsgObj) message.obj;
            if (netMsgObj.id == this.b.id) {
                this.a.buildResult((byte[]) netMsgObj.obj, netMsgObj.arg1, netMsgObj.arg2);
                this.a.msgd.onMessage(TaskCode.TASK_RESULT, this.a);
            }
        } else if (message.what == 2) {
            netMsgObj = (NetMsgObj) message.obj;
            if (netMsgObj.id == this.b.id) {
                this.a.requestTotal = netMsgObj.arg1;
                this.a.msgd.onMessage(TaskCode.TASK_REQUEST, this.a);
            }
        } else if (message.what == 3) {
        }
        return false;
    }
}
