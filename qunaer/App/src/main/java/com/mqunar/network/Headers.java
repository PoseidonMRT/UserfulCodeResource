package com.mqunar.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;

public class Headers {
    private final String[] a;

    public final class Builder {
        private final List<String> a = new ArrayList(20);

        public Builder add(String str) {
            int indexOf = str.indexOf(":");
            if (indexOf != -1) {
                return add(str.substring(0, indexOf).trim(), str.substring(indexOf + 1));
            }
            throw new IllegalArgumentException("Unexpected header: " + str);
        }

        public Builder add(String str, String str2) {
            if (str == null) {
                throw new IllegalArgumentException("name == null");
            } else if (str2 == null) {
                throw new IllegalArgumentException("value == null");
            } else if (str.length() != 0 && str.indexOf(0) == -1 && str2.indexOf(0) == -1) {
                return a(str, str2);
            } else {
                throw new IllegalArgumentException("Unexpected header: " + str + ": " + str2);
            }
        }

        private Builder a(String str, String str2) {
            this.a.add(str);
            this.a.add(str2.trim());
            return this;
        }

        public Builder removeAll(String str) {
            int i = 0;
            while (i < this.a.size()) {
                if (str.equalsIgnoreCase((String) this.a.get(i))) {
                    this.a.remove(i);
                    this.a.remove(i);
                    i -= 2;
                }
                i += 2;
            }
            return this;
        }

        public Builder set(String str, String str2) {
            removeAll(str);
            add(str, str2);
            return this;
        }

        public String get(String str) {
            for (int size = this.a.size() - 2; size >= 0; size -= 2) {
                if (str.equalsIgnoreCase((String) this.a.get(size))) {
                    return (String) this.a.get(size + 1);
                }
            }
            return null;
        }

        public Headers build() {
            return new Headers();
        }
    }

    private Headers(Builder builder) {
        this.a = (String[]) builder.a.toArray(new String[builder.a.size()]);
    }

    private Headers(String[] strArr) {
        this.a = strArr;
    }

    public String get(String str) {
        List a = a(this.a, str);
        return a.isEmpty() ? null : (String) a.get(0);
    }

    public String[] getAll(String str) {
        List a = a(this.a, str);
        return (String[]) a.toArray(new String[a.size()]);
    }

    public int size() {
        return this.a.length / 2;
    }

    public String name(int i) {
        int i2 = i * 2;
        if (i2 < 0 || i2 >= this.a.length) {
            return null;
        }
        return this.a[i2];
    }

    public String value(int i) {
        int i2 = (i * 2) + 1;
        if (i2 < 0 || i2 >= this.a.length) {
            return null;
        }
        return this.a[i2];
    }

    public Set<String> names() {
        Set treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            treeSet.add(name(i));
        }
        return Collections.unmodifiableSet(treeSet);
    }

    public List<String> values(String str) {
        int size = size();
        List list = null;
        for (int i = 0; i < size; i++) {
            if (str.equalsIgnoreCase(name(i))) {
                if (list == null) {
                    list = new ArrayList(2);
                }
                list.add(value(i));
            }
        }
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        Collections.addAll(builder.a, this.a);
        return builder;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(name(i)).append(": ").append(value(i)).append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        return stringBuilder.toString();
    }

    private static List<String> a(String[] strArr, String str) {
        List arrayList = new ArrayList(1);
        for (int length = strArr.length - 2; length >= 0; length -= 2) {
            if (str.equalsIgnoreCase(strArr[length])) {
                arrayList.add(strArr[length + 1]);
            }
        }
        return arrayList;
    }

    public static Headers of(String... strArr) {
        if (strArr == null || strArr.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        }
        int i;
        String[] strArr2 = (String[]) strArr.clone();
        for (i = 0; i < strArr2.length; i++) {
            if (strArr2[i] == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            strArr2[i] = strArr2[i].trim();
        }
        i = 0;
        while (i < strArr2.length) {
            String str = strArr2[i];
            String str2 = strArr2[i + 1];
            if (str.length() != 0 && str.indexOf(0) == -1 && str2.indexOf(0) == -1) {
                i += 2;
            } else {
                throw new IllegalArgumentException("Unexpected header: " + str + ": " + str2);
            }
        }
        return new Headers(strArr2);
    }

    public static Headers of(Map<String, String> map) {
        if (map == null) {
            throw new IllegalArgumentException("Expected map with header names and values");
        }
        String[] strArr = new String[(map.size() * 2)];
        int i = 0;
        for (Entry entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            String trim = ((String) entry.getKey()).trim();
            String trim2 = ((String) entry.getValue()).trim();
            if (trim.length() != 0 && trim.indexOf(0) == -1 && trim2.indexOf(0) == -1) {
                strArr[i] = trim;
                strArr[i + 1] = trim2;
                i += 2;
            } else {
                throw new IllegalArgumentException("Unexpected header: " + trim + ": " + trim2);
            }
        }
        return new Headers(strArr);
    }
}
