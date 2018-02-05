package com.facebook.imagepipeline.backends.okhttp3;

import android.os.Looper;
import android.os.SystemClock;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.NetworkFetcher.Callback;
import com.facebook.imagepipeline.producers.ProducerContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Call.Factory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;

public class OkHttpNetworkFetcher extends BaseNetworkFetcher<OkHttpNetworkFetchState> {
    private static final String FETCH_TIME = "fetch_time";
    private static final String IMAGE_SIZE = "image_size";
    private static final String QUEUE_TIME = "queue_time";
    private static final String TAG = "OkHttpNetworkFetchProducer";
    private static final String TOTAL_TIME = "total_time";
    private final Factory mCallFactory;
    private Executor mCancellationExecutor;

    public class OkHttpNetworkFetchState extends FetchState {
        public long fetchCompleteTime;
        public long responseTime;
        public long submitTime;

        public OkHttpNetworkFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer, producerContext);
        }
    }

    public OkHttpNetworkFetcher(OkHttpClient okHttpClient) {
        this(okHttpClient, okHttpClient.dispatcher().executorService());
    }

    public OkHttpNetworkFetcher(Factory factory, Executor executor) {
        this.mCallFactory = factory;
        this.mCancellationExecutor = executor;
    }

    public OkHttpNetworkFetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        return new OkHttpNetworkFetchState(consumer, producerContext);
    }

    public void fetch(OkHttpNetworkFetchState okHttpNetworkFetchState, Callback callback) {
        okHttpNetworkFetchState.submitTime = SystemClock.elapsedRealtime();
        try {
            fetchWithRequest(okHttpNetworkFetchState, callback, new Builder().cacheControl(new CacheControl.Builder().noStore().build()).url(okHttpNetworkFetchState.getUri().toString()).get().build());
        } catch (Throwable e) {
            callback.onFailure(e);
        }
    }

    public void onFetchCompletion(OkHttpNetworkFetchState okHttpNetworkFetchState, int i) {
        okHttpNetworkFetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
    }

    public Map<String, String> getExtraMap(OkHttpNetworkFetchState okHttpNetworkFetchState, int i) {
        Map<String, String> hashMap = new HashMap(4);
        hashMap.put(QUEUE_TIME, Long.toString(okHttpNetworkFetchState.responseTime - okHttpNetworkFetchState.submitTime));
        hashMap.put(FETCH_TIME, Long.toString(okHttpNetworkFetchState.fetchCompleteTime - okHttpNetworkFetchState.responseTime));
        hashMap.put(TOTAL_TIME, Long.toString(okHttpNetworkFetchState.fetchCompleteTime - okHttpNetworkFetchState.submitTime));
        hashMap.put(IMAGE_SIZE, Integer.toString(i));
        return hashMap;
    }

    protected void fetchWithRequest(final OkHttpNetworkFetchState okHttpNetworkFetchState, final Callback callback, Request request) {
        final Call newCall = this.mCallFactory.newCall(request);
        okHttpNetworkFetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() {
            public void onCancellationRequested() {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    newCall.cancel();
                } else {
                    OkHttpNetworkFetcher.this.mCancellationExecutor.execute(new Runnable() {
                        public void run() {
                            newCall.cancel();
                        }
                    });
                }
            }
        });
        newCall.enqueue(new okhttp3.Callback() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onResponse(okhttp3.Call r7, okhttp3.Response r8) {
                /*
                r6 = this;
                r0 = 0;
                r2 = r4;
                r3 = android.os.SystemClock.elapsedRealtime();
                r2.responseTime = r3;
                r4 = r8.body();
                r2 = r8.isSuccessful();	 Catch:{ Exception -> 0x005f }
                if (r2 != 0) goto L_0x0040;
            L_0x0014:
                r0 = com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher.this;	 Catch:{ Exception -> 0x005f }
                r1 = new java.io.IOException;	 Catch:{ Exception -> 0x005f }
                r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005f }
                r2.<init>();	 Catch:{ Exception -> 0x005f }
                r3 = "Unexpected HTTP code ";
                r2 = r2.append(r3);	 Catch:{ Exception -> 0x005f }
                r2 = r2.append(r8);	 Catch:{ Exception -> 0x005f }
                r2 = r2.toString();	 Catch:{ Exception -> 0x005f }
                r1.<init>(r2);	 Catch:{ Exception -> 0x005f }
                r2 = r5;	 Catch:{ Exception -> 0x005f }
                r0.handleException(r7, r1, r2);	 Catch:{ Exception -> 0x005f }
                r4.close();	 Catch:{ Exception -> 0x0037 }
            L_0x0036:
                return;
            L_0x0037:
                r0 = move-exception;
                r1 = "OkHttpNetworkFetchProducer";
                r2 = "Exception when closing response body";
                com.facebook.common.logging.FLog.w(r1, r2, r0);
                goto L_0x0036;
            L_0x0040:
                r2 = r4.contentLength();	 Catch:{ Exception -> 0x005f }
                r5 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
                if (r5 >= 0) goto L_0x0082;
            L_0x0048:
                r2 = r5;	 Catch:{ Exception -> 0x005f }
                r3 = r4.byteStream();	 Catch:{ Exception -> 0x005f }
                r0 = (int) r0;	 Catch:{ Exception -> 0x005f }
                r2.onResponse(r3, r0);	 Catch:{ Exception -> 0x005f }
                r4.close();	 Catch:{ Exception -> 0x0056 }
                goto L_0x0036;
            L_0x0056:
                r0 = move-exception;
                r1 = "OkHttpNetworkFetchProducer";
                r2 = "Exception when closing response body";
                com.facebook.common.logging.FLog.w(r1, r2, r0);
                goto L_0x0036;
            L_0x005f:
                r0 = move-exception;
                r1 = com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher.this;	 Catch:{ all -> 0x0074 }
                r2 = r5;	 Catch:{ all -> 0x0074 }
                r1.handleException(r7, r0, r2);	 Catch:{ all -> 0x0074 }
                r4.close();	 Catch:{ Exception -> 0x006b }
                goto L_0x0036;
            L_0x006b:
                r0 = move-exception;
                r1 = "OkHttpNetworkFetchProducer";
                r2 = "Exception when closing response body";
                com.facebook.common.logging.FLog.w(r1, r2, r0);
                goto L_0x0036;
            L_0x0074:
                r0 = move-exception;
                r4.close();	 Catch:{ Exception -> 0x0079 }
            L_0x0078:
                throw r0;
            L_0x0079:
                r1 = move-exception;
                r2 = "OkHttpNetworkFetchProducer";
                r3 = "Exception when closing response body";
                com.facebook.common.logging.FLog.w(r2, r3, r1);
                goto L_0x0078;
            L_0x0082:
                r0 = r2;
                goto L_0x0048;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher.2.onResponse(okhttp3.Call, okhttp3.Response):void");
            }

            public void onFailure(Call call, IOException iOException) {
                OkHttpNetworkFetcher.this.handleException(call, iOException, callback);
            }
        });
    }

    private void handleException(Call call, Exception exception, Callback callback) {
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(exception);
        }
    }
}
