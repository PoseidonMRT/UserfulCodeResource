package com.mqunar.cock.network;

import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.UIMsg.d_ResultType;
import com.iflytek.cloud.SpeechError;
import com.mqunar.cock.model.PushAck;
import com.mqunar.cock.model.PushMessage;
import com.mqunar.cock.network.tcpmodel.BaseMessage;
import com.mqunar.cock.network.tcpmodel.rec.BindAck;
import com.mqunar.cock.network.tcpmodel.rec.ClosePushMessageAck;
import com.mqunar.cock.network.tcpmodel.rec.FetchDeleteMsgAck;
import com.mqunar.cock.network.tcpmodel.rec.FriendRequestPassed;
import com.mqunar.cock.network.tcpmodel.rec.GroupAdmChangedNotify;
import com.mqunar.cock.network.tcpmodel.rec.GroupBuildNotify;
import com.mqunar.cock.network.tcpmodel.rec.GroupChangedInfo;
import com.mqunar.cock.network.tcpmodel.rec.MessageAck;
import com.mqunar.cock.network.tcpmodel.rec.NewFriendRequest;
import com.mqunar.cock.network.tcpmodel.rec.QuitChatRoomMessageAck;
import com.mqunar.cock.network.tcpmodel.rec.ReportMessageAck;
import com.mqunar.cock.network.tcpmodel.rec.SessionEndNotify;
import com.mqunar.cock.network.tcpmodel.rec.TypingInfoAck;
import com.mqunar.cock.network.tcpmodel.rec.TypingStatus;
import com.mqunar.cock.utils.JsonProcessorBasedFastJson;
import com.mqunar.imp.Imp;
import com.mqunar.tools.log.QLog;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Timer;

class b implements Runnable {
    final /* synthetic */ byte[] a;
    final /* synthetic */ byte[] b;
    final /* synthetic */ a c;

    b(a aVar, byte[] bArr, byte[] bArr2) {
        this.c = aVar;
        this.a = bArr;
        this.b = bArr2;
    }

    public void run() {
        Object str;
        QLog.d("CockHandler", "onmessage", new Object[0]);
        try {
            str = new String(this.a, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = null;
        }
        QLog.d("CockHandler", "appname = " + str, new Object[0]);
        if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase("QuTui")) {
            ByteBuffer wrap = ByteBuffer.wrap(this.b);
            byte[] bArr = new byte[(this.b.length - 2)];
            short s = wrap.getShort();
            wrap.get(bArr);
            QLog.d("CockHandler", "version = " + s, new Object[0]);
            PushMessage pushMessage = (PushMessage) JsonProcessorBasedFastJson.deserializeStatic(bArr, PushMessage.class);
            if (this.c.a.q) {
                if (1 == pushMessage.type && pushMessage != null && pushMessage.data != null) {
                    this.c.a.a("com.mqunar.atom.qutui.ACTION_MESSAGE_RECEIVED", pushMessage.data.toJSONString());
                } else if (!(2 != pushMessage.type || pushMessage == null || pushMessage.data == null)) {
                    this.c.a.a("com.mqunar.atom.qutui.ACTION_MESSAGE_TRANSPARENT", pushMessage.data.toJSONString());
                }
            }
            PushAck pushAck = new PushAck();
            pushAck.type = 1;
            pushAck.msgId = pushMessage.msgId;
            this.c.a.sendRequest(OneKeyCremation.getInstance().serializeWithoutImp(pushAck), "QuTui");
            QLog.d("CockHandler", JSON.toJSONString(pushMessage), new Object[0]);
        } else if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase("OChat")) {
            byte[] d = Imp.d(this.b);
            BaseMessage baseMessage = (BaseMessage) JsonProcessorBasedFastJson.deserializeStatic(d, BaseMessage.class);
            QLog.d("protocal " + baseMessage.t, new Object[0]);
            switch (baseMessage.t) {
                case 2:
                    BindAck bindAck = (BindAck) JsonProcessorBasedFastJson.deserializeStatic(d, BindAck.class);
                    if (this.c.a.s.get(Integer.toString(bindAck.reqid)) != null) {
                        ((Timer) this.c.a.s.get(Integer.toString(bindAck.reqid))).cancel();
                        this.c.a.s.remove(Integer.toString(bindAck.reqid));
                    }
                    if (this.c.a.e != null) {
                        this.c.a.e.onBindReceipt(bindAck);
                        break;
                    }
                    break;
                case 4:
                    if (this.c.a.e != null) {
                        break;
                    }
                    break;
                case 6:
                    MessageAck messageAck = (MessageAck) JsonProcessorBasedFastJson.deserializeStatic(d, MessageAck.class);
                    if (this.c.a.s.get(Integer.toString(messageAck.reqid)) != null) {
                        ((Timer) this.c.a.s.get(Integer.toString(messageAck.reqid))).cancel();
                        this.c.a.s.remove(Integer.toString(messageAck.reqid));
                    }
                    if (this.c.a.e != null) {
                        this.c.a.e.onMessageReceipt(messageAck);
                        break;
                    }
                    break;
                case 7:
                    Message message = (Message) JsonProcessorBasedFastJson.deserializeStatic(d, Message.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onMessageArrived(message);
                        break;
                    }
                    break;
                case 11:
                    GroupChangedInfo groupChangedInfo = (GroupChangedInfo) JsonProcessorBasedFastJson.deserializeStatic(d, GroupChangedInfo.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onGroupInfoChanged(groupChangedInfo);
                        break;
                    }
                    break;
                case 19:
                    GroupBuildNotify groupBuildNotify = (GroupBuildNotify) JsonProcessorBasedFastJson.deserializeStatic(d, GroupBuildNotify.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onGroupBuildNotify(groupBuildNotify);
                        break;
                    }
                    break;
                case 23:
                    NewFriendRequest newFriendRequest = (NewFriendRequest) JsonProcessorBasedFastJson.deserializeStatic(d, NewFriendRequest.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onNewFriendRequest(newFriendRequest);
                        break;
                    }
                    break;
                case d_ResultType.ESPECIAL_QUERY /*26*/:
                    FriendRequestPassed friendRequestPassed = (FriendRequestPassed) JsonProcessorBasedFastJson.deserializeStatic(d, FriendRequestPassed.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onFriendRequestPassed(friendRequestPassed);
                        break;
                    }
                    break;
                case 44:
                    GroupAdmChangedNotify groupAdmChangedNotify = (GroupAdmChangedNotify) JsonProcessorBasedFastJson.deserializeStatic(d, GroupAdmChangedNotify.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onGroupAdmChanged(groupAdmChangedNotify);
                        break;
                    }
                    break;
                case MotionEventCompat.AXIS_GENERIC_16 /*47*/:
                    ClosePushMessageAck closePushMessageAck = (ClosePushMessageAck) JsonProcessorBasedFastJson.deserializeStatic(d, ClosePushMessageAck.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onClosePushMessageAck(closePushMessageAck);
                        break;
                    }
                    break;
                case EventDispatcher.TRAFFIC_ENABLED /*51*/:
                    ReportMessageAck reportMessageAck = (ReportMessageAck) JsonProcessorBasedFastJson.deserializeStatic(d, ReportMessageAck.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onMessageReported(reportMessageAck);
                        break;
                    }
                    break;
                case SpeechError.TIP_ERROR_IVP_EXTRA_RGN_SOPPORT /*56*/:
                    break;
                case SpeechError.TIP_ERROR_IVP_MUCH_NOISE /*58*/:
                    QuitChatRoomMessageAck quitChatRoomMessageAck = (QuitChatRoomMessageAck) JsonProcessorBasedFastJson.deserializeStatic(d, QuitChatRoomMessageAck.class);
                    break;
                case SpeechError.TIP_ERROR_IVP_TOO_LOW /*59*/:
                    SessionEndNotify sessionEndNotify = (SessionEndNotify) JsonProcessorBasedFastJson.deserializeStatic(d, SessionEndNotify.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onSessionEnd(sessionEndNotify);
                        break;
                    }
                    break;
                case 62:
                    TypingInfoAck typingInfoAck = (TypingInfoAck) JsonProcessorBasedFastJson.deserializeStatic(d, TypingInfoAck.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onTypingAck(typingInfoAck);
                        break;
                    }
                    break;
                case 63:
                    TypingStatus typingStatus = (TypingStatus) JsonProcessorBasedFastJson.deserializeStatic(d, TypingStatus.class);
                    if (this.c.a.e != null) {
                        this.c.a.e.onTypingStatus(typingStatus);
                        break;
                    }
                    break;
            }
            FetchDeleteMsgAck fetchDeleteMsgAck = (FetchDeleteMsgAck) JsonProcessorBasedFastJson.deserializeStatic(d, FetchDeleteMsgAck.class);
            if (this.c.a.e != null) {
                this.c.a.e.onFetchDeleteMsg(fetchDeleteMsgAck);
            }
            Iterator it = this.c.a.i.iterator();
            while (it.hasNext()) {
                ((ImListener) it.next()).onMessage(d);
            }
        }
    }
}
