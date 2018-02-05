package com.mqunar.libtask;

import com.mqunar.tools.CheckUtils;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class HttpHeader implements Iterable<SimpleEntry<String, String>> {
    private final List<SimpleEntry<String, String>> a = new ArrayList();
    private final Map<String, List<String>> b = new HashMap();

    public HttpHeader(HttpHeader httpHeader) {
        addHeaders(httpHeader);
    }

    public HttpHeader(qunar.lego.utils.HttpHeader httpHeader) {
        addHeaders(httpHeader);
    }

    public HttpHeader(Map<String, ?> map) {
        addHeaders((Map) map);
    }

    public void addHeader(String str, String str2) {
        if (str != null && str2 != null) {
            String toLowerCase = str.toLowerCase(Locale.US);
            List list = (List) this.b.get(toLowerCase);
            if (list == null) {
                list = new LinkedList();
                list.add(str2);
                this.b.put(toLowerCase, list);
            } else {
                list.add(str2);
            }
            this.a.add(new SimpleEntry(str, str2));
        }
    }

    public void addHeaders(HttpHeader httpHeader) {
        if (httpHeader != null) {
            Iterator it = httpHeader.iterator();
            while (it.hasNext()) {
                SimpleEntry simpleEntry = (SimpleEntry) it.next();
                addHeader((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
            }
        }
    }

    public void addHeaders(qunar.lego.utils.HttpHeader httpHeader) {
        if (httpHeader != null) {
            Iterator it = httpHeader.iterator();
            while (it.hasNext()) {
                SimpleEntry simpleEntry = (SimpleEntry) it.next();
                addHeader((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
            }
        }
    }

    public void addHeaders(Map<String, ?> map) {
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    if (value.getClass() == String.class) {
                        addHeader((String) entry.getKey(), (String) value);
                    } else if (value instanceof List) {
                        for (Object next : (List) value) {
                            if (next != null) {
                                addHeader((String) entry.getKey(), next.toString());
                            }
                        }
                    }
                }
            }
        }
    }

    public List<SimpleEntry<String, String>> getHeaders() {
        return new ArrayList(this.a);
    }

    public Map<String, Object> getHeadersMap() {
        Map<String, Object> hashMap = new HashMap(this.b.size());
        Iterator it = this.b.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            List list = (List) entry.getValue();
            if (list == null) {
                it.remove();
            } else if (list.size() == 1) {
                hashMap.put(entry.getKey(), list.get(0));
            } else {
                hashMap.put(entry.getKey(), list);
            }
        }
        return hashMap;
    }

    public String getHeader(String str) {
        if (str == null) {
            return null;
        }
        List list = (List) this.b.get(str.toLowerCase(Locale.US));
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    public List<String> getHeaders(String str) {
        if (str == null) {
            return null;
        }
        List list = (List) this.b.get(str.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList(list);
    }

    public boolean hasHeader(String str) {
        if (CheckUtils.isEmpty(str)) {
            return false;
        }
        return this.b.containsKey(str.toLowerCase(Locale.US));
    }

    public int removeHeader(String str) {
        if (str == null) {
            return 0;
        }
        List list = (List) this.b.remove(str.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            if (((String) ((SimpleEntry) it.next()).getKey()).equalsIgnoreCase(str)) {
                it.remove();
            }
        }
        return list.size();
    }

    public void setHeader(String str, String str2) {
        if (str != null) {
            String toLowerCase = str.toLowerCase(Locale.US);
            List list = (List) this.b.get(toLowerCase);
            if (list == null || list.isEmpty()) {
                addHeader(str, str2);
                return;
            }
            list.clear();
            Iterator it = this.a.iterator();
            while (it.hasNext()) {
                if (((String) ((SimpleEntry) it.next()).getKey()).equalsIgnoreCase(str)) {
                    it.remove();
                }
            }
            if (str2 == null) {
                this.b.remove(toLowerCase);
                return;
            }
            list.add(str2);
            this.a.add(new SimpleEntry(str, str2));
        }
    }

    public Iterator<SimpleEntry<String, String>> iterator() {
        return Collections.unmodifiableList(this.a).iterator();
    }

    public String toString() {
        return this.a.toString();
    }
}
