package com.baidu.platform.comjni.map.commonmemcache;

public class a {
    private long a;
    private JNICommonMemCache b;

    public a() {
        this.a = 0;
        this.b = null;
        this.b = new JNICommonMemCache();
    }

    public long a() {
        this.a = this.b.Create();
        return this.a;
    }

    public void b() {
        if (this.a != 0) {
            this.b.Init(this.a);
        }
    }
}
