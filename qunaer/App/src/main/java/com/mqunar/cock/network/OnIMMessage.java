package com.mqunar.cock.network;

import android.os.Message;
import com.mqunar.cock.network.tcpmodel.rec.BindAck;
import com.mqunar.cock.network.tcpmodel.rec.ClosePushMessageAck;
import com.mqunar.cock.network.tcpmodel.rec.FetchDeleteMsgAck;
import com.mqunar.cock.network.tcpmodel.rec.FriendRequestPassed;
import com.mqunar.cock.network.tcpmodel.rec.GroupAdmChangedNotify;
import com.mqunar.cock.network.tcpmodel.rec.GroupBuildNotify;
import com.mqunar.cock.network.tcpmodel.rec.GroupChangedInfo;
import com.mqunar.cock.network.tcpmodel.rec.MessageAck;
import com.mqunar.cock.network.tcpmodel.rec.NewFriendRequest;
import com.mqunar.cock.network.tcpmodel.rec.ReportMessageAck;
import com.mqunar.cock.network.tcpmodel.rec.SessionEndNotify;
import com.mqunar.cock.network.tcpmodel.rec.TypingInfoAck;
import com.mqunar.cock.network.tcpmodel.rec.TypingStatus;

public interface OnIMMessage {
    void onBindReceipt(BindAck bindAck);

    void onBindTimeout();

    void onClosePushMessageAck(ClosePushMessageAck closePushMessageAck);

    void onFetchDeleteMsg(FetchDeleteMsgAck fetchDeleteMsgAck);

    void onFriendRequestPassed(FriendRequestPassed friendRequestPassed);

    void onGroupAdmChanged(GroupAdmChangedNotify groupAdmChangedNotify);

    void onGroupBuildNotify(GroupBuildNotify groupBuildNotify);

    void onGroupBuildSuccess(Message message);

    void onGroupInfoChanged(GroupChangedInfo groupChangedInfo);

    void onMessageArrived(Message message);

    void onMessageReceipt(MessageAck messageAck);

    void onMessageReported(ReportMessageAck reportMessageAck);

    void onMsgTimeOut(Message message);

    void onNetEnd(boolean z);

    void onNewFriendRequest(NewFriendRequest newFriendRequest);

    void onSessionEnd(SessionEndNotify sessionEndNotify);

    void onTargets();

    void onTypingAck(TypingInfoAck typingInfoAck);

    void onTypingStatus(TypingStatus typingStatus);
}
