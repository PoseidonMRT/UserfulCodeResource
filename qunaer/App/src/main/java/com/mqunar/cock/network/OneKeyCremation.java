package com.mqunar.cock.network;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.mqunar.cock.model.BaseHttpMessage;
import com.mqunar.cock.model.BaseParam;
import com.mqunar.cock.model.RequestHttpMessage;
import com.mqunar.cock.model.TcpRequest;
import com.mqunar.cock.model.TcpResponseHeader;
import com.mqunar.cock.model.TcpResult;
import com.mqunar.cock.model.YaccaListResult;
import com.mqunar.cock.model.YaccaListResult.YaccaInfo;
import com.mqunar.cock.network.callback.TcpMessageCallBack;
import com.mqunar.cock.network.state.ChannelState;
import com.mqunar.cock.network.state.YaccaConnectedState;
import com.mqunar.cock.network.state.YaccaConnectingState;
import com.mqunar.cock.network.state.YaccaDisconnectState;
import com.mqunar.cock.network.state.YaccaListGettingState;
import com.mqunar.cock.network.state.YaccaListUnavailableState;
import com.mqunar.cock.network.state.YaccaListValuebleState;
import com.mqunar.cock.utils.CockConstants;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class OneKeyCremation implements OnCockMessage {
    public static final int MESSAGE_QUEQUE_RETENTION_TIMEOUT = 257;
    public static final int MESSAGE_TCP_REQUEST_TIMEOUT = 259;
    public static final int MESSAGE_YACCA_CONNECT_TIMEOUT = 258;
    public static final int STATUS_AUTHFAIL = 4;
    public static final int STATUS_CONNECTED = 2;
    public static final int STATUS_CONNECTING = 3;
    public static final int STATUS_DISCONNECTED = 1;
    public static final int TCP_RESULT_CODE_RECEIPT = 200;
    public static final int TCP_RESULT_CODE_TIMEOUT = 300;
    private static volatile OneKeyCremation a = new OneKeyCremation();
    private static int d = 0;
    private RemoteSvcProxy b;
    private AtomicBoolean c = new AtomicBoolean(false);
    private int e = 1;
    private ConcurrentLinkedQueue<BaseHttpMessage> f;
    private ConcurrentHashMap<Integer, TcpMessageCallBack> g;
    private List<YaccaInfo> h;
    private Handler i;
    private Handler j;
    private ChannelState k = this.yaccaListUnavailableState;
    public YaccaConnectedState yaccaConnectedState = new YaccaConnectedState();
    public YaccaConnectingState yaccaConnectingState = new YaccaConnectingState();
    public YaccaDisconnectState yaccaDisconnectState = new YaccaDisconnectState();
    public YaccaListGettingState yaccaListGettingState = new YaccaListGettingState();
    public YaccaListUnavailableState yaccaListUnavailableState = new YaccaListUnavailableState();
    public YaccaListValuebleState yaccaListValuebleState = new YaccaListValuebleState();

    public static int getmReqid() {
        return d;
    }

    public ChannelState getState() {
        return this.k;
    }

    public void setState(ChannelState channelState) {
        synchronized (this) {
            this.k = channelState;
        }
    }

    public Handler getHandler() {
        return this.i;
    }

    private OneKeyCremation() {
        HandlerThread handlerThread = new HandlerThread("timeout_thread");
        handlerThread.start();
        this.j = new Handler(handlerThread.getLooper());
        this.b = new RemoteSvcProxy(QApplication.getContext(), this.j);
        this.f = new ConcurrentLinkedQueue();
        this.g = new ConcurrentHashMap();
        CockHandler.getInstance().setOnCockMessageListener(this);
        this.i = new g(this, handlerThread.getLooper());
    }

    public static OneKeyCremation getInstance() {
        if (a == null) {
            synchronized (OneKeyCremation.class) {
                if (a == null) {
                    a = new OneKeyCremation();
                }
            }
        }
        return a;
    }

    public void tryConnect() {
        oneKeyCremation();
    }

    public void sendMessage(BaseHttpMessage baseHttpMessage) {
        if (getInstance().oneKeyCremation()) {
            CockHandler.getInstance().sendRequest(serializeWithoutImp(baseHttpMessage), "QuTui");
        } else {
            a(baseHttpMessage, (long) DateTimeUtils.ONE_MINUTE);
        }
    }

    private void a(BaseHttpMessage baseHttpMessage, long j) {
        if (!this.f.contains(baseHttpMessage)) {
            this.f.offer(baseHttpMessage);
            Message obtain = Message.obtain();
            obtain.what = 257;
            obtain.obj = baseHttpMessage;
            this.i.sendMessageDelayed(obtain, j);
        }
    }

    private synchronized void a(int i) {
        this.e = i;
    }

    public void connect() {
        if (ArrayUtils.isEmpty(this.h)) {
            loadYacaAsync(new BaseParam());
            setState(this.yaccaListGettingState);
            return;
        }
        a(this.h);
    }

    private synchronized void a(List<YaccaInfo> list) {
        if (!ArrayUtils.isEmpty((Collection) list)) {
            a(((YaccaInfo) list.get(0)).ip, ((YaccaInfo) list.get(0)).tPort);
            list.remove(0);
        }
    }

    private void a(String str, int i) {
        try {
            QLog.d("YACCA", "connect yacca (%s,%d)", str, Integer.valueOf(i));
            a(3);
            setState(this.yaccaConnectingState);
            this.i.sendEmptyMessageDelayed(258, 10000);
            CockHandler.getInstance().listen(str, i, CockConstants.getAppname());
        } catch (Exception e) {
            QLog.d("OneKeyCremation connect error", new Object[0]);
        }
    }

    public TcpResult sendRequest(TcpRequest tcpRequest) {
        byte[] bArr;
        TcpResult tcpResult = null;
        d++;
        tcpRequest.reqid = d;
        tcpRequest.requestHeader.id = String.valueOf(d);
        Object toJSONString = JSON.toJSONString(tcpRequest.requestHeader);
        if (TextUtils.isEmpty(toJSONString)) {
            bArr = null;
        } else {
            bArr = toJSONString.getBytes();
        }
        if (bArr != null) {
            ByteBuffer allocate = ByteBuffer.allocate((bArr.length + 4) + tcpRequest.bytes.length);
            allocate.putInt(bArr.length);
            allocate.put(bArr);
            allocate.put(tcpRequest.bytes);
            tcpResult = new TcpResult();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.g.put(Integer.valueOf(d), new h(this, tcpResult, countDownLatch));
            Message obtain = Message.obtain();
            obtain.what = 259;
            obtain.obj = tcpRequest;
            this.i.sendMessageDelayed(obtain, 20000);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return tcpResult;
    }

    public void onConnected() {
        QLog.d("OneKeyCremation connect success", new Object[0]);
        setState(this.yaccaConnectedState);
        this.i.removeMessages(258);
        a(2);
    }

    public void onNetEnd(boolean z) {
        QLog.d("OneKeyCremation server disconnect", new Object[0]);
        if (!z) {
            if (ArrayUtils.isEmpty(this.h)) {
                setState(this.yaccaListUnavailableState);
            } else {
                setState(this.yaccaDisconnectState);
            }
            a(1);
        }
    }

    public void onRequest(int i, RequestHttpMessage requestHttpMessage) {
    }

    public void onMessage(TcpResponseHeader tcpResponseHeader, byte[] bArr) {
        if (tcpResponseHeader != null && this.g.get(Integer.valueOf(Integer.parseInt(tcpResponseHeader.id))) != null) {
            ((TcpMessageCallBack) this.g.get(Integer.valueOf(Integer.parseInt(tcpResponseHeader.id)))).onReceipt(tcpResponseHeader, bArr);
        }
    }

    public void loadYacaAsync(BaseParam baseParam) {
        this.b.sendAsync(ServiceMap.IM_GET_YACCA, baseParam, new i(this, YaccaListResult.class, this.b));
    }

    public boolean oneKeyCremation() {
        return !this.c.get() && this.k.oneKeyCremation();
    }

    public boolean pushCremation() {
        return false;
    }

    public byte[] serializeWithoutImp(Object obj) {
        Object toJSONString = JSON.toJSONString(obj);
        if (TextUtils.isEmpty(toJSONString)) {
            return null;
        }
        return toJSONString.getBytes();
    }

    public void stopPush() {
        CockHandler.getInstance().setPush(false);
    }

    public void startPush() {
        CockHandler.getInstance().setPush(true);
    }
}
