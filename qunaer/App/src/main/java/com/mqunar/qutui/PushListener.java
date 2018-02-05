package com.mqunar.qutui;

import com.alibaba.fastjson.JSONObject;

public interface PushListener {
    void onPush(JSONObject jSONObject);
}
