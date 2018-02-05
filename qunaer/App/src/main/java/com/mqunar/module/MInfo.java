package com.mqunar.module;

public class MInfo {
    public byte downloadFlag;
    public String fileName;
    public String md5;
    public String packageName;
    public String patchUrl;
    public byte restart;
    public String touch;
    public byte type;
    public String url;
    public int version;

    public MInfo() {
        this.type = (byte) 1;
        this.downloadFlag = (byte) 0;
        this.type = (byte) 1;
    }
}
