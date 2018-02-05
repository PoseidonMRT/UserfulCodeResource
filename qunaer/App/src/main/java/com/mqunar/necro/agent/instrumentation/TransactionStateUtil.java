package com.mqunar.necro.agent.instrumentation;

import android.text.TextUtils;
import com.mqunar.necro.agent.Agent;
import com.mqunar.necro.agent.bean.BaseData;
import com.mqunar.necro.agent.bean.NetworkData;
import com.mqunar.necro.agent.instrumentation.httpclient.HttpRequestEntityImpl;
import com.mqunar.necro.agent.instrumentation.httpclient.HttpResponseEntityImpl;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.util.Config;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;

public class TransactionStateUtil {
    public static final String APP_DATA_HEADER = "X-NewNecro-App-Data";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CROSS_PROCESS_ID_HEADER = "X-NewNecro-ID";
    private static final String REQUEST_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String REQUEST_HEADER_CONNECTION = "Connection";
    private static final String REQUEST_HEADER_CONTENT_LENGTH = "Content-Length";
    private static final String REQUEST_HEADER_CONTENT_TYPE = "Content-Type";
    private static final String REQUEST_HEADER_COOKIE = "Cookie";
    public static final String REQUEST_HEADER_HOST = "Host";
    public static final String REQUEST_HEADER_PITCHER_TYPE = "Pitcher-Type";
    private static final String REQUEST_HEADER_PITCHER_URL = "Pitcher-Url";
    private static final String REQUEST_HEADER_X_CLIENTENCODING = "X-ClientEncoding";
    private static final AgentLog log = AgentLogManager.getAgentLog();

    private static void inspectAndInstrument(TransactionState transactionState, String str, String str2) {
        log.debug("inspectAndInstrument url " + str);
        transactionState.setUrl(str);
        transactionState.setHttpMethod(str2);
        transactionState.setCarrier(Agent.getActiveNetworkCarrier());
        transactionState.setWanType(Agent.getActiveNetworkWanType());
    }

    public static void inspectAndInstrument(TransactionState transactionState, HttpURLConnection httpURLConnection) {
        inspectAndInstrument(transactionState, httpURLConnection.getURL().toString(), httpURLConnection.getRequestMethod());
    }

    private static void inspectAndInstrumentResponse(TransactionState transactionState, String str, int i, int i2) {
        if (!(str == null || "".equals(str))) {
            transactionState.setAppData(str);
        }
        if (i >= 0) {
            transactionState.setBytesReceived((long) i);
        }
        transactionState.setStatusCode(i2);
    }

    public static void inspectAndInstrumentResponse(TransactionState transactionState, HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField(APP_DATA_HEADER);
        int contentLength = httpURLConnection.getContentLength();
        int i = 0;
        try {
            i = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            log.debug("Failed to retrieve response code due to an I/O exception: " + e.getMessage());
        } catch (Throwable e2) {
            log.error("Failed to retrieve response code due to underlying (Harmony?) NPE", e2);
        }
        inspectAndInstrumentResponse(transactionState, headerField, contentLength, i);
    }

    public static HttpRequest inspectAndInstrument(TransactionState transactionState, HttpHost httpHost, HttpRequest httpRequest) {
        String str = null;
        RequestLine requestLine = httpRequest.getRequestLine();
        if (requestLine != null) {
            String uri = requestLine.getUri();
            int i = (uri == null || uri.length() < 10 || uri.substring(0, 10).indexOf("://") < 0) ? 0 : 1;
            if (i == 0 && uri != null && httpHost != null) {
                str = httpHost.toURI().toString();
                StringBuilder append = new StringBuilder().append(str);
                str = (str.endsWith("/") || uri.startsWith("/")) ? "" : "/";
                str = append.append(str).append(uri).toString();
            } else if (i != 0) {
                str = uri;
            }
        }
        log.debug("inspectAndInstrument url " + str);
        if (transactionState.getUrl() == null || transactionState.getHttpMethod() == null) {
            try {
                throw new Exception("TransactionData constructor was not provided with a valid URL, host or HTTP method");
            } catch (Throwable e) {
                AgentLogManager.getAgentLog().error(MessageFormat.format("TransactionStateUtil.inspectAndInstrument(...) for {0} could not determine request URL or HTTP method [host={1}, requestLine={2}]", new Object[]{httpRequest.getClass().getCanonicalName(), httpHost, requestLine}), e);
            }
        } else {
            inspectAndInstrument(transactionState, str, requestLine.getMethod());
            parseRequestHeader(transactionState, httpRequest);
            wrapRequestEntity(transactionState, httpRequest);
            return httpRequest;
        }
    }

    public static HttpUriRequest inspectAndInstrument(TransactionState transactionState, HttpUriRequest httpUriRequest) {
        inspectAndInstrument(transactionState, httpUriRequest.getURI().toString(), httpUriRequest.getMethod());
        parseRequestHeader(transactionState, httpUriRequest);
        wrapRequestEntity(transactionState, httpUriRequest);
        return httpUriRequest;
    }

    private static void wrapRequestEntity(TransactionState transactionState, HttpRequest httpRequest) {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest httpEntityEnclosingRequest = (HttpEntityEnclosingRequest) httpRequest;
            if (httpEntityEnclosingRequest.getEntity() != null) {
                httpEntityEnclosingRequest.setEntity(new HttpRequestEntityImpl(httpEntityEnclosingRequest.getEntity(), transactionState));
            }
        }
    }

    private static void parseRequestHeader(TransactionState transactionState, HttpRequest httpRequest) {
        try {
            Header[] allHeaders = httpRequest.getAllHeaders();
            if (allHeaders != null) {
                HashMap hashMap = new HashMap();
                for (Header header : allHeaders) {
                    if (!(header == null || "".equals(header.getValue()))) {
                        String name = header.getName();
                        if (!("Content-Type".equals(name) || REQUEST_HEADER_X_CLIENTENCODING.equals(name) || "Host".equals(name) || "Connection".equals(name) || REQUEST_HEADER_ACCEPT_ENCODING.equals(name) || HTTP.CONTENT_LEN.equals(name) || "Cookie".equals(name))) {
                            hashMap.put(header.getName(), header.getValue());
                        }
                    }
                }
                if (hashMap.size() > 0) {
                    transactionState.setHeaders(hashMap);
                }
            }
        } catch (Throwable th) {
            log.warning("parseRequestHeader Failed parse header: " + th.toString());
        }
    }

    static void parseConnectionHeader(TransactionState transactionState, HttpURLConnection httpURLConnection) {
        try {
            Map requestProperties = httpURLConnection.getRequestProperties();
            if (requestProperties != null) {
                HashMap hashMap = new HashMap();
                for (Entry entry : requestProperties.entrySet()) {
                    if (!(entry == null || "".equals(((List) entry.getValue()).toString()))) {
                        String str = (String) entry.getKey();
                        if (!("Content-Type".equals(str) || REQUEST_HEADER_X_CLIENTENCODING.equals(str) || "Host".equals(str) || "Connection".equals(str) || REQUEST_HEADER_ACCEPT_ENCODING.equals(str) || HTTP.CONTENT_LEN.equals(str) || "Cookie".equals(str))) {
                            hashMap.put(entry.getKey(), ((List) entry.getValue()).toString().replace("[", "").replace("]", ""));
                        }
                    }
                }
                if (hashMap.size() > 0) {
                    transactionState.setHeaders(hashMap);
                }
            }
        } catch (Throwable th) {
            log.warning("parseConnectionHeader Failed parse header: " + th.toString());
        }
    }

    public static HttpResponse inspectAndInstrument(TransactionState transactionState, HttpResponse httpResponse) {
        log.debug("statuscode " + httpResponse.getStatusLine().getStatusCode());
        transactionState.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        Header[] headers = httpResponse.getHeaders(APP_DATA_HEADER);
        if (!(headers == null || headers.length <= 0 || "".equals(headers[0].getValue()))) {
            transactionState.setAppData(headers[0].getValue());
        }
        headers = httpResponse.getHeaders(HTTP.CONTENT_LEN);
        if (headers != null && headers.length > 0) {
            try {
                transactionState.setBytesReceived(Long.parseLong(headers[0].getValue()));
                addTransactionAndErrorData(transactionState, httpResponse);
            } catch (NumberFormatException e) {
                log.warning("Failed to parse content length: " + e.toString());
            }
        } else if (httpResponse.getEntity() != null) {
            httpResponse.setEntity(new HttpResponseEntityImpl(httpResponse.getEntity(), transactionState, -1));
        } else {
            transactionState.setBytesReceived(0);
            addTransactionAndErrorData(transactionState, (HttpResponse) null);
        }
        return httpResponse;
    }

    public static void setErrorCodeFromException(TransactionState transactionState, Exception exception) {
        log.error("TransactionStateUtil: Attempting to convert network exception " + exception.getClass().getName() + " to error code.");
        transactionState.setErrorMsg(exception.getMessage());
    }

    private static void addTransactionAndErrorData(TransactionState transactionState, HttpResponse httpResponse) {
        end(transactionState);
    }

    public static void end(TransactionState transactionState) {
        BaseData end = transactionState.end();
        if (end != null && !excludeData(end)) {
            Agent.dealData(end);
        }
    }

    private static boolean excludeData(NetworkData networkData) {
        if (TextUtils.isEmpty(networkData.reqUrl) || networkData.reqUrl.contains(Config.t)) {
            return true;
        }
        if (!networkData.reqUrl.contains(".jpg") && !networkData.reqUrl.contains(".JPG") && !networkData.reqUrl.contains(".png") && !networkData.reqUrl.contains(".PNG") && !networkData.reqUrl.contains(".jpeg") && !networkData.reqUrl.contains(".JPEG") && !networkData.reqUrl.contains(".webp") && !networkData.reqUrl.contains(".WEBP")) {
            return false;
        }
        if (Integer.parseInt(networkData.reqTime) > 2000) {
            return false;
        }
        return true;
    }
}
