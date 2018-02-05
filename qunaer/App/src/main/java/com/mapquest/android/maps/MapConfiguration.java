package com.mapquest.android.maps;

import com.iflytek.aiui.AIUIConstant;
import com.mapquest.android.maps.TileCacher.CacheType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapConfiguration {
    private static final String LOG_TAG = MapConfiguration.class.getSimpleName();
    private static final String TILE_VERSION_FILENAME = "mqTileVersion.json";
    private static final String TILE_VERSION_SERVICE_URL = "http://mapconfig.mapquest.com/mapconfig?version=3&format=json";
    static boolean versionUpdated;
    private String configStr = null;
    private Map<String, MapConfig> mapConfigList;
    private MapView mapView;

    class MapConfig {
        String coverage;
        List<String> formats = new ArrayList();
        String hostRangeHigh;
        String hostRangeLow;
        String type;
        String urlPattern;

        public boolean equals(MapConfig mapConfig) {
            return this.type.equals(mapConfig.type) && this.hostRangeHigh.equals(mapConfig.hostRangeHigh) && this.hostRangeLow.equals(mapConfig.hostRangeLow) && this.urlPattern.equals(mapConfig.urlPattern) && this.coverage.equals(mapConfig.coverage) && formatString().equals(mapConfig.formatString());
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("type: ").append(this.type).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("hostRangeHigh: ").append(this.hostRangeHigh).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("hostRangeLow: ").append(this.hostRangeLow).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("urlPattern: ").append(this.urlPattern).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("coverage: ").append(this.coverage).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("formats: ").append(formatString()).append(IOUtils.LINE_SEPARATOR_UNIX);
            return stringBuilder.toString();
        }

        private String formatString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.formats.size() > 0) {
                stringBuilder.append("formats: ");
                for (String append : this.formats) {
                    stringBuilder.append(append).append(",");
                }
            }
            return stringBuilder.toString();
        }
    }

    class TileVersionUpdater extends Thread {
        private final String LOG_TAG = TileVersionUpdater.class.getSimpleName();

        public void run() {
            try {
                updateFile();
            } catch (Exception e) {
            }
        }

        private void updateFile() {
            InputStream executeAsStream;
            FileOutputStream fileOutputStream;
            Object obj;
            String access$200;
            Throwable th;
            Throwable th2;
            Object obj2;
            InputStream inputStream = null;
            File file = new File(MapConfiguration.this.getRootDirectory(), MapConfiguration.TILE_VERSION_FILENAME);
            FileOutputStream fileOutputStream2;
            try {
                Map hashMap = new HashMap();
                hashMap.put("Referer", "android://" + MapConfiguration.this.mapView.getContext().getPackageName());
                hashMap.put("Accept", "*/*");
                executeAsStream = HttpUtil.executeAsStream("http://mapconfig.mapquest.com/mapconfig?version=3&format=json&sdk=" + Util.getApiVersion(), hashMap);
                if (executeAsStream != null) {
                    try {
                        fileOutputStream2 = new FileOutputStream(file);
                    } catch (Exception e) {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Exception e2) {
                            }
                        }
                        if (executeAsStream != null) {
                            try {
                                executeAsStream.close();
                            } catch (Exception e3) {
                                obj = null;
                            }
                        }
                        obj = null;
                        if (obj == null) {
                            access$200 = MapConfiguration.this.getConfigurationFromDisk();
                            MapConfiguration.this.mapView.clearTilesInMemory();
                            MapConfiguration.this.mapView.getTileCacher().getCache(CacheType.DB).clear();
                            EventDispatcher.sendEmptyMessage(23);
                            MapConfiguration.this.configStr = access$200;
                            MapConfiguration.this.mapConfigList = MapConfiguration.this.parseConfiguration(MapConfiguration.this.configStr);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream2 = null;
                        inputStream = executeAsStream;
                        th2 = th;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (Exception e4) {
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th2;
                    }
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = executeAsStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream2.write(bArr, 0, read);
                        }
                        fileOutputStream2.flush();
                        obj2 = 1;
                    } catch (Exception e6) {
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (executeAsStream != null) {
                            executeAsStream.close();
                        }
                        obj = null;
                        if (obj == null) {
                            access$200 = MapConfiguration.this.getConfigurationFromDisk();
                            MapConfiguration.this.mapView.clearTilesInMemory();
                            MapConfiguration.this.mapView.getTileCacher().getCache(CacheType.DB).clear();
                            EventDispatcher.sendEmptyMessage(23);
                            MapConfiguration.this.configStr = access$200;
                            MapConfiguration.this.mapConfigList = MapConfiguration.this.parseConfiguration(MapConfiguration.this.configStr);
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        inputStream = executeAsStream;
                        th2 = th;
                        if (fileOutputStream2 != null) {
                            fileOutputStream2.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th2;
                    }
                }
                fileOutputStream2 = null;
                obj2 = null;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (Exception e7) {
                    }
                }
                if (executeAsStream != null) {
                    try {
                        executeAsStream.close();
                    } catch (Exception e8) {
                        obj = obj2;
                    }
                }
                obj = obj2;
            } catch (Exception e9) {
                executeAsStream = null;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (executeAsStream != null) {
                    executeAsStream.close();
                }
                obj = null;
                if (obj == null) {
                    access$200 = MapConfiguration.this.getConfigurationFromDisk();
                    MapConfiguration.this.mapView.clearTilesInMemory();
                    MapConfiguration.this.mapView.getTileCacher().getCache(CacheType.DB).clear();
                    EventDispatcher.sendEmptyMessage(23);
                    MapConfiguration.this.configStr = access$200;
                    MapConfiguration.this.mapConfigList = MapConfiguration.this.parseConfiguration(MapConfiguration.this.configStr);
                }
            } catch (Throwable th5) {
                th2 = th5;
                fileOutputStream2 = null;
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th2;
            }
            if (obj == null) {
                access$200 = MapConfiguration.this.getConfigurationFromDisk();
                if (!(access$200 == null || access$200.equals(MapConfiguration.this.configStr))) {
                    MapConfiguration.this.mapView.clearTilesInMemory();
                    MapConfiguration.this.mapView.getTileCacher().getCache(CacheType.DB).clear();
                    EventDispatcher.sendEmptyMessage(23);
                }
                MapConfiguration.this.configStr = access$200;
                MapConfiguration.this.mapConfigList = MapConfiguration.this.parseConfiguration(MapConfiguration.this.configStr);
            }
        }
    }

    public MapConfiguration(boolean z, MapView mapView) {
        this.mapView = mapView;
        this.configStr = getConfigurationFromDisk();
        if (this.configStr == null) {
            this.configStr = getDefaultConfiguration();
        }
        this.mapConfigList = parseConfiguration(this.configStr);
        if (z) {
            updateMapConfigFromServer();
        }
    }

    public Map<String, MapConfig> getMapConfigsList() {
        return this.mapConfigList;
    }

    public MapConfig getMapConfigByType(String str) {
        return this.mapConfigList.containsKey(str) ? (MapConfig) this.mapConfigList.get(str) : null;
    }

    public String getUrlPattern(String str, String str2) {
        String createKey = createKey(str, str2);
        if (this.mapConfigList == null || !this.mapConfigList.containsKey(createKey)) {
            return null;
        }
        MapConfig mapConfig = (MapConfig) this.mapConfigList.get(createKey);
        return mapConfig.urlPattern.replace("{$hostrange}", mapConfig.hostRangeLow);
    }

    public void updateMapConfigFromServer() {
        if (!versionUpdated) {
            versionUpdated = true;
            new TileVersionUpdater().start();
        }
    }

    private File getRootDirectory() {
        return Util.getAppFileDir(this.mapView.getContext(), "mapquest");
    }

    private String getConfigurationFromDisk() {
        File file = new File(getRootDirectory(), TILE_VERSION_FILENAME);
        String str = null;
        if (file.exists()) {
            try {
                str = Util.convertStreamToString(new FileInputStream(file));
            } catch (Exception e) {
            }
        }
        return str;
    }

    private String getDefaultConfiguration() {
        String str = null;
        try {
            str = Util.convertStreamToString(getClass().getResourceAsStream("/com/mapquest/android/maps/defaultMapConfig.json")).replaceAll("\\n", "").replaceAll("\\t", "");
        } catch (IOException e) {
        }
        return str;
    }

    private Map<String, MapConfig> parseConfiguration(String str) {
        Map hashMap = new HashMap();
        if (str != null && str.length() > 0) {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONObject("mapconfig").getJSONObject("services").getJSONObject("layers").getJSONArray("layer");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                    MapConfig mapConfig = new MapConfig();
                    mapConfig.urlPattern = jSONObject.getString("urlpattern");
                    String string = jSONObject.getString(AIUIConstant.KEY_NAME);
                    mapConfig.type = string;
                    String string2 = jSONObject.getString("coverage");
                    mapConfig.coverage = jSONObject.getString("coverage");
                    JSONObject jSONObject2 = jSONObject.getJSONObject("hostrange");
                    mapConfig.hostRangeHigh = jSONObject2.getString("hi");
                    mapConfig.hostRangeLow = jSONObject2.getString("lo");
                    JSONArray jSONArray2 = jSONObject.getJSONObject("formats").getJSONArray("ext");
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        mapConfig.formats.add(jSONArray2.get(i2).toString());
                    }
                    hashMap.put(createKey(string2, string), mapConfig);
                }
            } catch (Exception e) {
            }
        }
        return hashMap;
    }

    private String createKey(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str).append("-").append(str2);
        return stringBuilder.toString();
    }

    public boolean equals(MapConfiguration mapConfiguration) {
        Map mapConfigsList = mapConfiguration.getMapConfigsList();
        for (String str : mapConfigsList.keySet()) {
            boolean equals = ((MapConfig) this.mapConfigList.get(str)).equals((MapConfig) mapConfigsList.get(str));
            if (!equals) {
                return equals;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.mapConfigList != null) {
            for (String str : this.mapConfigList.keySet()) {
                if (this.mapConfigList.containsKey(str)) {
                    stringBuilder.append(((MapConfig) this.mapConfigList.get(str)).toString());
                }
            }
        }
        return stringBuilder.toString();
    }

    public void destroy() {
        this.mapView = null;
        this.mapConfigList = null;
        this.configStr = null;
    }
}
