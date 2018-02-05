package com.mqunar.cock.model;

import java.io.Serializable;
import java.util.Map;

public class TcpResponseHeader implements Serializable {
    public int code;
    public Map<String, String> header;
    public String id;
}
