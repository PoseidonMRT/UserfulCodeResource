package com.mqunar.cock.model;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

public class PushMessage implements Serializable {
    public JSONObject data;
    public String msgId;
    public String pid;
    public String sendNo;
    public int type;

    public class PushData implements Serializable {
        public String extras;
        public String msg;
        public String title;
        public String url;
    }
}
