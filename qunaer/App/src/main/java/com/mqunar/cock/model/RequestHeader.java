package com.mqunar.cock.model;

import java.io.Serializable;
import java.util.Map;

public class RequestHeader implements Serializable {
    public Map<String, String> header;
    public String id;
    public String pitcherType;
    public String pitcherUrl;
}
