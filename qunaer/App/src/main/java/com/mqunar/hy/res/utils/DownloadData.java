package com.mqunar.hy.res.utils;

import android.net.http.Headers;
import android.os.Handler;
import com.mqunar.hy.res.HyResInitializer;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.http.client.utils.URLEncodedUtils;

public class DownloadData {
    public static final String DOWNLOAD_END = "download_end";
    public static final String DOWNLOAD_NONE = "download_none";
    public static final String DOWNLOAD_START = "download_start";
    private static final OkHttpClient client = new OkHttpClient();
    private Call call;
    private String downloadState;
    private String filename;
    private long filesize;
    public Handler handler;
    private HybridInfo hybridInfo;
    public String hybridid;
    public String nversion;
    public File savefile;
    private double time = 0.0d;
    public int type;
    public String url;

    public DownloadData(String str, String str2, String str3, int i, Handler handler) {
        String str4 = null;
        this.call = str4;
        this.downloadState = DOWNLOAD_NONE;
        this.hybridid = str;
        this.type = i;
        this.handler = handler;
        this.nversion = str3;
        this.url = str2;
        this.hybridInfo = HybridManager.getInstance().getHybridInfoById(str);
        this.filename = "";
        String str5 = "";
        if (str2 != null && str2.length() > 0 && str2.contains("/")) {
            this.filename = str2.substring(str2.lastIndexOf(47) + 1);
            try {
                str5 = this.filename.substring(this.filename.lastIndexOf("."));
            } catch (Exception e) {
            }
            this.filename = this.filename.substring(0, this.filename.lastIndexOf("."));
        }
        this.filename += "{" + str3 + "}";
        try {
            str4 = new File(QunarUtils.getAppDir(HyResInitializer.getContext()), "files/caches").getCanonicalPath();
        } catch (Throwable e2) {
            Timber.e(e2, new Object[0]);
        }
        this.savefile = new File(str4, this.filename + str5);
        if (!this.savefile.getParentFile().exists()) {
            this.savefile.getParentFile().mkdirs();
        }
        if (this.savefile.exists()) {
            this.savefile.delete();
        }
    }

    public String getDownloadState() {
        return this.downloadState;
    }

    public int hashCode() {
        int i = 0;
        if (this.url != null) {
            i = 0 + this.url.hashCode();
        }
        if (this.nversion != null) {
            return i + this.nversion.hashCode();
        }
        return i;
    }

    public void run() {
        this.downloadState = DOWNLOAD_START;
        this.time = (double) System.currentTimeMillis();
        Builder builder = new Builder();
        builder.addHeader(Headers.CONN_DIRECTIVE, "keep-alive");
        builder.addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        builder.url(this.url);
        this.call = client.newCall(builder.build());
        this.call.enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                DownloadData.this.downloadFail();
            }

            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    byte[] bArr = new byte[1024];
                    InputStream byteStream = response.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(DownloadData.this.savefile);
                    while (true) {
                        int read = byteStream.read(bArr);
                        if (read != -1) {
                            fileOutputStream.write(bArr, 0, read);
                        } else {
                            fileOutputStream.close();
                            DownloadData.this.handler.sendMessage(DownloadData.this.handler.obtainMessage(1001, DownloadData.this));
                            DownloadManager.getInstance().remove(DownloadData.this.url);
                            DownloadData.this.time = ((double) System.currentTimeMillis()) - DownloadData.this.time;
                            DownloadData.this.statistics(DownloadData.this.time, true);
                            DownloadData.this.downloadState = DownloadData.DOWNLOAD_END;
                            return;
                        }
                    }
                }
                DownloadData.this.downloadFail();
            }
        });
    }

    private void downloadFail() {
        if (this.savefile != null && this.savefile.exists()) {
            this.savefile.delete();
        }
        DownloadManager.getInstance().remove(this.url);
        this.time = ((double) System.currentTimeMillis()) - this.time;
        statistics(this.time, false);
        this.handler.sendMessage(this.handler.obtainMessage(1002, this));
        this.downloadState = DOWNLOAD_END;
    }

    public void cancel() {
        if (this.call != null) {
            this.call.cancel();
        }
    }

    private void statistics(double d, boolean z) {
        Object obj = 1;
        if (this.hybridInfo == null) {
            this.hybridInfo = new HybridInfo();
            obj = null;
        }
        if (obj != null) {
            if (z) {
                StatisticsUtil.qpPatchSuccess(this.hybridid, this.hybridInfo.getMd5(), this.hybridInfo.version + "", this.nversion);
            } else {
                StatisticsUtil.qpPatchError(this.hybridid, this.hybridInfo.getMd5(), this.hybridInfo.version + "", this.nversion);
            }
            StatisticsUtil.qpPatchDownloadTime(this.hybridid, d);
            return;
        }
        if (z) {
            StatisticsUtil.qpFullSuccess(this.hybridid, this.hybridInfo.getMd5(), this.hybridInfo.version + "", this.nversion);
        } else {
            StatisticsUtil.qpFullError(this.hybridid, this.hybridInfo.getMd5(), this.hybridInfo.version + "", this.nversion);
        }
        StatisticsUtil.qpFullDownloadTime(this.hybridid, d);
    }
}
