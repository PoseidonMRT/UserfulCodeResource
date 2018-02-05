package com.mqunar.cock.model;

import java.io.Serializable;

public class TcpRequest implements Serializable {
    public byte[] bytes;
    public int reqid;
    public RequestHeader requestHeader;
}
