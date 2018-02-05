package com.mqunar.necro.agent.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.util.NecroCompressUtils;
import com.mqunar.necro.agent.util.NecroFileUtils;
import com.mqunar.necro.agent.util.SafeUtils;
import java.io.File;
import java.util.ArrayList;
import org.apache.http.HttpStatus;
import qunar.lego.utils.FormPart;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;
import qunar.lego.utils.PitcherResponse;

public class NecroSenderProxy implements IHttpSender {
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private String jsonData = null;
    private String mCParam = "";
    private String mHostUrl = "";
    private String mPitcherUrl = "";
    private String mRequestId = "";

    public NecroSenderProxy(String str, String str2, String str3, String str4) {
        this.mHostUrl = str;
        this.mPitcherUrl = str2;
        this.mCParam = str3;
        this.mRequestId = str4;
    }

    @TargetApi(8)
    public void send(Context context, String str) {
        if (str != null) {
            this.jsonData = NecroFileUtils.file2Str(str);
            if (this.jsonData != null) {
                if (SafeUtils.canEncryption()) {
                    this.jsonData = SafeUtils.da(this.jsonData);
                }
                String str2 = str + "gz";
                log.info("发送 JSON数据：" + this.jsonData);
                NecroCompressUtils.doCompressString(this.jsonData, str2);
                this.jsonData = null;
                log.info("send necro file : " + str2);
                HttpHeader httpHeader = new HttpHeader();
                if (!TextUtils.isEmpty(this.mRequestId)) {
                    httpHeader.addHeader("qrid", this.mRequestId);
                }
                Pitcher pitcher = new Pitcher(context, this.mHostUrl, getFormParts(str2), httpHeader);
                if (!TextUtils.isEmpty(this.mPitcherUrl)) {
                    pitcher.setProxyUrl(this.mPitcherUrl);
                }
                PitcherResponse request = pitcher.request();
                if (request.e != null) {
                    log.info("send necro file error : " + str2 + " error : " + request.e);
                    deleteFile(new File(str2));
                } else if (request.respcode > HttpStatus.SC_BAD_REQUEST) {
                    log.info("send necro file failed : " + str2 + "  resCode is: " + request.respcode);
                    deleteFile(new File(str2));
                } else {
                    log.info("send necro file success : " + str2);
                    deleteFile(new File(str2));
                    deleteFile(new File(str));
                }
            }
        }
    }

    private ArrayList<FormPart> getFormParts(String str) {
        ArrayList<FormPart> arrayList = new ArrayList();
        FormPart formPart = new FormPart("c", this.mCParam);
        formPart.addHeader("X-ClientEncoding", "none");
        arrayList.add(formPart);
        formPart = new FormPart("monitor", str, "application/octet-stream");
        formPart.addHeader("X-ClientEncoding", "gzip");
        arrayList.add(formPart);
        return arrayList;
    }

    private void deleteFile(File file) {
        if (file != null && file.exists() && !file.delete()) {
            log.info("delete file failed :" + file.getName());
        }
    }
}
