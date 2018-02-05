package com.mqunar.cock.model;

import org.json.JSONObject;

public class PushAck extends BaseHttpMessage {
    public JSONObject jsonObject;
    public String msgId;
    public int type;
}
