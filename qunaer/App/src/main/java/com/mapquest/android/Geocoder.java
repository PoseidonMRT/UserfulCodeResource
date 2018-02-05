package com.mapquest.android;

import android.content.Context;
import android.location.Address;
import android.net.http.Headers;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

@Instrumented
public final class Geocoder {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private static final String LOG_TAG = Geocoder.class.getName();
    private static final int READ_TIMEOUT = 3000;
    BaseGeocoder geocoder;

    abstract class BaseGeocoder {
        Context context;
        HashMap<String, String> headers;
        Locale locale;

        public abstract List<Address> getFromLocation(double d, double d2, int i);

        public abstract List<Address> getFromLocationName(String str, int i);

        public abstract List<Address> getFromLocationName(String str, int i, double d, double d2, double d3, double d4);

        public BaseGeocoder(Context context) {
            this.context = context;
            this.headers = new HashMap();
            this.headers.put("Referer", getReferer());
        }

        public BaseGeocoder(Geocoder geocoder, Context context, Locale locale) {
            this(context);
            this.locale = locale;
        }

        public final String encode(String str) {
            try {
                str = URLEncoder.encode(str, "UTF8");
            } catch (UnsupportedEncodingException e) {
            }
            return str;
        }

        public final StringBuilder appendEncodedKeyValue(StringBuilder stringBuilder, String str, String str2) {
            return stringBuilder.append(str).append("=").append(encode(str2)).append("&");
        }

        public final StringBuilder appendKeyValue(StringBuilder stringBuilder, String str, String str2) {
            return stringBuilder.append(str).append("=").append(str2).append("&");
        }

        public final boolean isEmpty(JSONObject jSONObject, String str) {
            return jSONObject.isNull(str) || String.valueOf(jSONObject.get(str)).length() == 0;
        }

        public String getReferer() {
            return "android://" + this.context.getPackageName();
        }
    }

    class MapQuestGeocoder extends BaseGeocoder {
        private static final String BASE_URL = "http://www.mapquestapi.com/geocoding/v1/";
        private static final String GEOCODE_URL = "http://www.mapquestapi.com/geocoding/v1/address?";
        private static final String INPUT_FORMAT = "kvp";
        private static final String OUTPUT_FORMAT = "json";
        private static final String REVERSE_GEOCODE_URL = "http://www.mapquestapi.com/geocoding/v1/reverse?";
        String apiKey;

        public MapQuestGeocoder(Context context, Locale locale, String str) {
            super(Geocoder.this, context, locale);
            this.apiKey = str;
        }

        public List<Address> getFromLocation(double d, double d2, int i) {
            StringBuilder stringBuilder = new StringBuilder(REVERSE_GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "lat", "" + d);
            appendEncodedKeyValue(stringBuilder, "lng", "" + d2);
            return invokeApi(stringBuilder.toString());
        }

        public List<Address> getFromLocationName(String str, int i, double d, double d2, double d3, double d4) {
            StringBuilder stringBuilder = new StringBuilder(GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "thumbMaps", "false");
            appendEncodedKeyValue(stringBuilder, Headers.LOCATION, str);
            appendEncodedKeyValue(stringBuilder, "maxResults", "" + i);
            appendEncodedKeyValue(stringBuilder, "boundingBox", d3 + "," + d2 + "," + d + "," + d4);
            return invokeApi(stringBuilder.toString());
        }

        public List<Address> getFromLocationName(String str, int i) {
            StringBuilder stringBuilder = new StringBuilder(GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "thumbMaps", "false");
            appendEncodedKeyValue(stringBuilder, Headers.LOCATION, str);
            appendEncodedKeyValue(stringBuilder, "maxResults", "" + i);
            return invokeApi(stringBuilder.toString());
        }

        private StringBuilder appendRequiredParameters(StringBuilder stringBuilder) {
            appendKeyValue(stringBuilder, "key", this.apiKey);
            appendEncodedKeyValue(stringBuilder, "inFormat", INPUT_FORMAT);
            appendEncodedKeyValue(stringBuilder, "outFormat", OUTPUT_FORMAT);
            return stringBuilder;
        }

        private List<Address> invokeApi(String str) {
            List arrayList = new ArrayList();
            String access$000 = Geocoder.runRequest(str, this.headers);
            if (access$000 == null) {
                throw new IOException(" Error while reading from network");
            }
            try {
                JSONObject jSONObject = new JSONObject(access$000);
                if (jSONObject.getJSONObject("info").getInt("statuscode") != 0) {
                    jSONObject.getJSONObject("info").getJSONArray("messages").get(0).toString();
                } else {
                    JSONArray jSONArray = jSONObject.getJSONArray("results").getJSONObject(0).getJSONArray("locations");
                    Address address = new Address(this.locale);
                    int length = jSONArray.length();
                    for (int i = 0; i < length; i++) {
                        int i2;
                        int i3;
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        address.setLatitude(jSONObject2.getJSONObject("latLng").getDouble("lat"));
                        address.setLongitude(jSONObject2.getJSONObject("latLng").getDouble("lng"));
                        if (!isEmpty(jSONObject2, "mapUrl")) {
                            address.setUrl(jSONObject2.getString("mapUrl"));
                        }
                        if (isEmpty(jSONObject2, "street")) {
                            i2 = 0;
                        } else {
                            i2 = 1;
                            address.setAddressLine(0, jSONObject2.getString("street"));
                        }
                        if (isEmpty(jSONObject2, "adminArea5")) {
                            i3 = i2;
                        } else {
                            address.setLocality(jSONObject2.getString("adminArea5"));
                            i3 = i2 + 1;
                            address.setAddressLine(i2, jSONObject2.getString("adminArea5"));
                        }
                        if (isEmpty(jSONObject2, "adminArea4")) {
                            i2 = i3;
                        } else {
                            address.setSubAdminArea(jSONObject2.getString("adminArea4"));
                            i2 = i3 + 1;
                            address.setAddressLine(i3, jSONObject2.getString("adminArea4"));
                        }
                        if (isEmpty(jSONObject2, "adminArea3")) {
                            i3 = i2;
                        } else {
                            address.setAdminArea(jSONObject2.getString("adminArea3"));
                            i3 = i2 + 1;
                            address.setAddressLine(i2, jSONObject2.getString("adminArea3"));
                        }
                        if (isEmpty(jSONObject2, "postalCode")) {
                            i2 = i3;
                        } else {
                            address.setPostalCode(jSONObject2.getString("postalCode"));
                            i2 = i3 + 1;
                            address.setAddressLine(i3, jSONObject2.getString("postalCode"));
                        }
                        if (!isEmpty(jSONObject2, "adminArea1")) {
                            address.setCountryName(jSONObject2.getString("adminArea1"));
                            i3 = i2 + 1;
                            address.setAddressLine(i2, jSONObject2.getString("adminArea1"));
                        }
                        arrayList.add(address);
                    }
                }
            } catch (JSONException e) {
            }
            return arrayList;
        }
    }

    class NominatimGeocoder extends BaseGeocoder {
        private static final String BASE_URL = "http://open.mapquestapi.com/nominatim/v1/";
        private static final String GEOCODE_URL = "http://open.mapquestapi.com/nominatim/v1/search?";
        private static final String OUTPUT_FORMAT = "json";
        private static final String REVERSE_GEOCODE_URL = "http://open.mapquestapi.com/nominatim/v1/reverse?";

        public NominatimGeocoder(Context context, Locale locale) {
            super(Geocoder.this, context, locale);
        }

        public List<Address> getFromLocation(double d, double d2, int i) {
            StringBuilder stringBuilder = new StringBuilder(REVERSE_GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "lat", "" + d);
            appendEncodedKeyValue(stringBuilder, "lon", "" + d2);
            return invokeApi(stringBuilder.toString());
        }

        public List<Address> getFromLocationName(String str, int i, double d, double d2, double d3, double d4) {
            StringBuilder stringBuilder = new StringBuilder(GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "q", str);
            appendKeyValue(stringBuilder, "limit", "" + i);
            appendEncodedKeyValue(stringBuilder, "viewbox", d2 + "," + d3 + "," + d4 + "," + d);
            return invokeApi(stringBuilder.toString());
        }

        public List<Address> getFromLocationName(String str, int i) {
            StringBuilder stringBuilder = new StringBuilder(GEOCODE_URL);
            appendRequiredParameters(stringBuilder);
            appendEncodedKeyValue(stringBuilder, "q", str);
            appendKeyValue(stringBuilder, "limit", "" + i);
            return invokeApi(stringBuilder.toString());
        }

        private StringBuilder appendRequiredParameters(StringBuilder stringBuilder) {
            appendKeyValue(stringBuilder, "format", OUTPUT_FORMAT);
            appendKeyValue(stringBuilder, "addressdetails", "1");
            return stringBuilder;
        }

        private List<Address> invokeApi(String str) {
            List arrayList = new ArrayList();
            String access$000 = Geocoder.runRequest(str, this.headers);
            if (access$000 == null) {
                throw new IOException(" Error while reading from network");
            }
            try {
                Object nextValue = new JSONTokener(access$000).nextValue();
                if (nextValue instanceof JSONArray) {
                    JSONArray jSONArray = (JSONArray) nextValue;
                    int length = jSONArray.length();
                    for (int i = 0; i < length; i++) {
                        arrayList.add(parseAddress(jSONArray.getJSONObject(i)));
                    }
                } else {
                    arrayList.add(parseAddress((JSONObject) nextValue));
                }
            } catch (JSONException e) {
            }
            return arrayList;
        }

        private Address parseAddress(JSONObject jSONObject) {
            int i;
            Address address = new Address(this.locale);
            address.setLatitude(jSONObject.getDouble("lat"));
            address.setLongitude(jSONObject.getDouble("lon"));
            JSONObject jSONObject2 = jSONObject.getJSONObject("address");
            if (isEmpty(jSONObject2, "road")) {
                i = 0;
            } else {
                i = 1;
                address.setAddressLine(0, jSONObject2.getString("road"));
            }
            if (!isEmpty(jSONObject2, "display_name")) {
                address.setFeatureName(jSONObject2.getString("display_name").split(",")[0]);
            }
            if (!isEmpty(jSONObject2, "suburb")) {
                address.setSubLocality(jSONObject2.getString("suburb"));
                int i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("suburb"));
                i = i2;
            }
            if (!isEmpty(jSONObject2, "city")) {
                address.setLocality(jSONObject2.getString("city"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("city"));
                i = i2;
            } else if (!isEmpty(jSONObject2, "town")) {
                address.setLocality(jSONObject2.getString("town"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("town"));
                i = i2;
            } else if (!isEmpty(jSONObject2, "village")) {
                address.setLocality(jSONObject2.getString("village"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("village"));
                i = i2;
            }
            if (!isEmpty(jSONObject2, "county")) {
                address.setSubAdminArea(jSONObject2.getString("county"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("county"));
                i = i2;
            }
            if (!isEmpty(jSONObject2, "state")) {
                address.setAdminArea(jSONObject2.getString("state"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("state"));
                i = i2;
            }
            if (!isEmpty(jSONObject2, "postcode")) {
                address.setPostalCode(jSONObject2.getString("postcode"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("postcode"));
                i = i2;
            }
            if (!isEmpty(jSONObject2, "country")) {
                address.setCountryName(jSONObject2.getString("country"));
                i2 = i + 1;
                address.setAddressLine(i, jSONObject2.getString("country"));
            }
            if (!isEmpty(jSONObject2, "country_code")) {
                address.setCountryCode(jSONObject2.getString("country_code"));
            }
            return address;
        }
    }

    public Geocoder(Context context, Locale locale) {
        this.geocoder = null;
        this.geocoder = new NominatimGeocoder(context, locale);
    }

    public Geocoder(Context context) {
        this(context, DEFAULT_LOCALE);
    }

    public Geocoder(Context context, Locale locale, String str) {
        this.geocoder = null;
        this.geocoder = new MapQuestGeocoder(context, locale, str);
    }

    public Geocoder(Context context, String str) {
        this(context, DEFAULT_LOCALE, str);
    }

    public List<Address> getFromLocation(double d, double d2, int i) {
        if (d < -90.0d || d > 90.0d) {
            throw new IllegalArgumentException("Invalid latitude value " + d);
        } else if (d2 >= -180.0d && d2 <= 180.0d) {
            return this.geocoder.getFromLocation(d, d2, i);
        } else {
            throw new IllegalArgumentException("Invalid longitude value " + d2);
        }
    }

    public List<Address> getFromLocationName(String str, int i, double d, double d2, double d3, double d4) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid location name " + str);
        } else if (d < -90.0d || d > 90.0d) {
            throw new IllegalArgumentException("Invalid lowerLeftLatitude value " + d);
        } else if (d3 < -90.0d || d3 > 90.0d) {
            throw new IllegalArgumentException("Invalid upperRightLatitude value " + d3);
        } else if (d2 < -180.0d || d2 > 180.0d) {
            throw new IllegalArgumentException("Invalid lowerLeftLongitude value " + d2);
        } else if (d4 >= -180.0d && d4 <= 180.0d) {
            return this.geocoder.getFromLocationName(str, i, d, d2, d3, d4);
        } else {
            throw new IllegalArgumentException("Invalid upperRightLongitude value " + d4);
        }
    }

    public List<Address> getFromLocationName(String str, int i) {
        if (str != null && str.length() != 0) {
            return this.geocoder.getFromLocationName(str, i);
        }
        throw new IllegalArgumentException("Invalid location name " + str);
    }

    static boolean isPresent() {
        return true;
    }

    private static String runRequest(String str, Map<String, String> map) {
        String str2;
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "utf-8");
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 5000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 3000);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        HttpUriRequest httpGet = new HttpGet(str);
        if (map != null) {
            try {
                for (String str22 : map.keySet()) {
                    httpGet.addHeader(str22, (String) map.get(str22));
                }
            } catch (Exception e) {
                str22 = null;
                try {
                    httpGet.abort();
                    return str22;
                } finally {
                    if (defaultHttpClient != null) {
                        defaultHttpClient.getConnectionManager().shutdown();
                    }
                }
            } catch (Throwable th) {
                r2.consumeContent();
            }
        }
        HttpResponse execute = !(defaultHttpClient instanceof HttpClient) ? defaultHttpClient.execute(httpGet) : HttpInstrumentation.execute(defaultHttpClient, httpGet);
        HttpEntity entity;
        if (execute.getStatusLine().getStatusCode() == 200) {
            entity = execute.getEntity();
            if (entity != null) {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream(entity.getContentLength() < 0 ? 4096 : (int) entity.getContentLength());
                entity.writeTo(byteArrayOutputStream);
                str22 = byteArrayOutputStream.toString();
                try {
                    entity.consumeContent();
                } catch (Exception e2) {
                    httpGet.abort();
                    return str22;
                }
            }
            str22 = null;
            if (defaultHttpClient == null) {
                return str22;
            }
            defaultHttpClient.getConnectionManager().shutdown();
            return str22;
        }
        if (defaultHttpClient != null) {
            defaultHttpClient.getConnectionManager().shutdown();
        }
        return null;
    }
}
