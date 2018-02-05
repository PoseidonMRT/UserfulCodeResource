package qunar.lego.utils;

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
    private final Map<String, List<String>> fieldMap = new HashMap();
    private final List<SimpleEntry<String, String>> fields = new ArrayList();

    public HttpHeader(HttpHeader httpHeader) {
        addHeaders(httpHeader);
    }

    public HttpHeader(Map<String, ?> map) {
        addHeaders((Map) map);
    }

    public void addHeader(String str, String str2) {
        if (str != null && str2 != null) {
            String toLowerCase = str.toLowerCase(Locale.US);
            List list = (List) this.fieldMap.get(toLowerCase);
            if (list == null) {
                list = new LinkedList();
                list.add(str2);
                this.fieldMap.put(toLowerCase, list);
            } else {
                list.add(str2);
            }
            this.fields.add(new SimpleEntry(str, str2));
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
        return new ArrayList(this.fields);
    }

    public Map<String, Object> getHeadersMap() {
        Map<String, Object> hashMap = new HashMap(this.fieldMap.size());
        Iterator it = this.fieldMap.entrySet().iterator();
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
        List list = (List) this.fieldMap.get(str.toLowerCase(Locale.US));
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    public List<String> getHeaders(String str) {
        if (str == null) {
            return null;
        }
        List list = (List) this.fieldMap.get(str.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList(list);
    }

    public boolean hasHeader(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return this.fieldMap.containsKey(str.toLowerCase(Locale.US));
    }

    public int removeHeader(String str) {
        if (str == null) {
            return 0;
        }
        List list = (List) this.fieldMap.remove(str.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Iterator it = this.fields.iterator();
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
            List list = (List) this.fieldMap.get(toLowerCase);
            if (list == null || list.isEmpty()) {
                addHeader(str, str2);
                return;
            }
            list.clear();
            Iterator it = this.fields.iterator();
            while (it.hasNext()) {
                if (((String) ((SimpleEntry) it.next()).getKey()).equalsIgnoreCase(str)) {
                    it.remove();
                }
            }
            if (str2 == null) {
                this.fieldMap.remove(toLowerCase);
                return;
            }
            list.add(str2);
            this.fields.add(new SimpleEntry(str, str2));
        }
    }

    public Iterator<SimpleEntry<String, String>> iterator() {
        return Collections.unmodifiableList(this.fields).iterator();
    }

    public String toString() {
        return this.fields.toString();
    }
}
