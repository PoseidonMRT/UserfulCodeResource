package com.mqunar.cock.model;

import java.io.Serializable;

public class TcpResult implements Serializable {
    public byte[] bytes;
    public int resultcode;
    public TcpResponseHeader tcpResponseHeader;
}
