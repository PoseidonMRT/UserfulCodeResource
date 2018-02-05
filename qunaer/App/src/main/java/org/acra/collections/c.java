package org.acra.collections;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class c<K, V> implements Serializable, Map<K, V> {
    private final Map<K, V> a;

    public class a<K, V> implements Entry<K, V> {
        private final Entry<K, V> a;

        a(Entry<K, V> entry) {
            this.a = entry;
        }

        public K getKey() {
            return this.a.getKey();
        }

        public V getValue() {
            return this.a.getValue();
        }

        public V setValue(Object obj) {
            throw new UnsupportedOperationException();
        }
    }

    public c(Map<K, V> map) {
        this.a = new HashMap(map);
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object obj) {
        return this.a.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.a.containsValue(obj);
    }

    @NonNull
    public Set<Entry<K, V>> entrySet() {
        Set<Entry> entrySet = this.a.entrySet();
        org.acra.collections.d.a aVar = new org.acra.collections.d.a();
        for (Entry aVar2 : entrySet) {
            aVar.a(new a(aVar2));
        }
        return aVar.a();
    }

    public V get(Object obj) {
        return this.a.get(obj);
    }

    public boolean isEmpty() {
        return this.a.isEmpty();
    }

    @NonNull
    public Set<K> keySet() {
        return new d(this.a.keySet());
    }

    public V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public void putAll(@NonNull Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.a.size();
    }

    @NonNull
    public Collection<V> values() {
        return new b(this.a.values());
    }
}
