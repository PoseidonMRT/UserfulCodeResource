package com.mqunar.hy.res.libtask;

class NetMsgObj {
    public long arg1;
    public int arg2;
    public int id;
    public Object obj;

    public NetMsgObj(int i, long j, int i2, Object obj) {
        this.id = i;
        this.arg1 = j;
        this.arg2 = i2;
        this.obj = obj;
    }
}
