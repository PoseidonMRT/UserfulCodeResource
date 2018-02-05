package com.mqunar.hy.res.upgrade;

import android.net.http.Headers;
import com.alibaba.fastjson.JSON;
import com.mqunar.hy.res.HyResInitializer;
import com.mqunar.hy.res.TaskResult;
import com.mqunar.hy.res.libtask.ChiefGuard;
import com.mqunar.hy.res.libtask.ProxyConductor.Builder;
import com.mqunar.hy.res.libtask.Ticket.RequestFeature;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.model.HybridParam;
import com.mqunar.hy.res.model.HybridResult;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;

public class HyResUpdateRequestTask extends Task<HybridInfo, HybridResult> {
    private static HyResUpdateRequestTask instance;

    public /* bridge */ /* synthetic */ boolean necessary() {
        return super.necessary();
    }

    public /* bridge */ /* synthetic */ void setStatus(byte b) {
        super.setStatus(b);
    }

    public /* bridge */ /* synthetic */ void setValid(boolean z) {
        super.setValid(z);
    }

    public /* bridge */ /* synthetic */ boolean valid() {
        return super.valid();
    }

    private HyResUpdateRequestTask() {
    }

    public static HyResUpdateRequestTask getInstance() {
        if (instance == null) {
            instance = new HyResUpdateRequestTask();
        }
        if (HyResInitializer.getContext() != null) {
            return instance;
        }
        throw new RuntimeException("Must call HyResInitializer.getInstance first !!!");
    }

    public void run(List<HybridInfo> list, TaskResult<HybridResult> taskResult, HybridInfo... hybridInfoArr) {
        if (hybridInfoArr != null || necessary()) {
            HybridParam hybridParam = new HybridParam(list);
            if (HyResInitializer.isDebug() && !HyResInitializer.isOnline()) {
                Timber.i("HyRes HybridParam = " + HybridParam.toJsonString(list), new Object[0]);
            }
            if (hybridInfoArr == null || hybridInfoArr.length == 0) {
                setStatus((byte) 1);
            } else {
                hybridParam.current = hybridInfoArr[0];
            }
            try {
                sendRequest(hybridParam, taskResult);
            } catch (Throwable e) {
                Timber.e(e, "wt", new Object[0]);
            }
        }
    }

    private void sendRequest(HybridParam hybridParam, TaskResult<HybridResult> taskResult) {
        byte[] bytes;
        UnsupportedEncodingException e;
        HybridUpdateCallback hybridUpdateCallback = new HybridUpdateCallback(HyResInitializer.getContext(), this, hybridParam, taskResult);
        StringBuilder stringBuilder = new StringBuilder();
        if (Thread.currentThread().getContextClassLoader() == null) {
            Thread.currentThread().setContextClassLoader(HyResInitializer.getContext().getClassLoader());
        }
        stringBuilder.append("cparam=").append(URLEncoder.encode(JSON.toJSONString(HyResInitializer.getCParam())));
        if (HyResInitializer.getModules() != null) {
            stringBuilder.append("&module=").append(URLEncoder.encode(JSON.toJSONString(HyResInitializer.getModules())));
        }
        stringBuilder.append("&hlist=").append(URLEncoder.encode(HybridParam.toJsonString(hybridParam.hlist)));
        if (hybridParam.current != null) {
            stringBuilder.append("&current=").append(URLEncoder.encode(hybridParam.current.toJsonString()));
        }
        byte[] bArr = new byte[0];
        try {
            bytes = stringBuilder.toString().getBytes("UTF-8");
            try {
                Timber.i(stringBuilder.toString(), new Object[0]);
                Timber.i("list:" + HybridParam.toJsonString(hybridParam.hlist), new Object[0]);
                if (hybridParam.current != null) {
                    Timber.i("current:" + hybridParam.current.toJsonString(), new Object[0]);
                }
                Timber.i("list.size:" + hybridParam.hlist.size(), new Object[0]);
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                e.printStackTrace();
                ChiefGuard.getInstance().addTask(HyResInitializer.getContext(), new Builder().setUrl(HyResInitializer.getServerUrl()).setContent(bytes).addHeader(Headers.CONN_DIRECTIVE, "keep-alive").addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE).create(hybridUpdateCallback), new RequestFeature[0]);
            }
        } catch (UnsupportedEncodingException e3) {
            UnsupportedEncodingException unsupportedEncodingException = e3;
            bytes = bArr;
            e = unsupportedEncodingException;
            e.printStackTrace();
            ChiefGuard.getInstance().addTask(HyResInitializer.getContext(), new Builder().setUrl(HyResInitializer.getServerUrl()).setContent(bytes).addHeader(Headers.CONN_DIRECTIVE, "keep-alive").addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE).create(hybridUpdateCallback), new RequestFeature[0]);
        }
        ChiefGuard.getInstance().addTask(HyResInitializer.getContext(), new Builder().setUrl(HyResInitializer.getServerUrl()).setContent(bytes).addHeader(Headers.CONN_DIRECTIVE, "keep-alive").addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE).create(hybridUpdateCallback), new RequestFeature[0]);
    }
}
