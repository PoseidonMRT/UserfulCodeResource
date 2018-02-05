package com.mapquest.android.maps;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceResponse {
    private JSONHelper helper;
    public Info info;
    public JSONObject serviceResponse;

    public class Info {
        public Copyright copyright;
        public List<String> messages = new ArrayList();
        public int statusCode = -1;

        public class Copyright {
            public String imageAltText = "";
            public String imageUrl = "";
            public String text = "";

            Copyright(JSONObject jSONObject) {
                this.text = ServiceResponse.this.helper.getString("text", jSONObject);
                this.imageUrl = ServiceResponse.this.helper.getString("imageUrl", jSONObject);
                this.imageAltText = ServiceResponse.this.helper.getString("imageAltText", jSONObject);
            }
        }

        Info(JSONObject jSONObject) {
            this.copyright = new Copyright(ServiceResponse.this.helper.getJSONObject("copyright", jSONObject));
            this.statusCode = ServiceResponse.this.helper.getInt("statuscode", jSONObject);
            JSONArray jSONArray = ServiceResponse.this.helper.getJSONArray("messages", jSONObject);
            for (int i = 0; i < jSONArray.length(); i++) {
                this.messages.add(ServiceResponse.this.helper.getString(i, jSONArray));
            }
        }
    }

    public ServiceResponse() {
        this.helper = new JSONHelper();
        this.info = new Info();
    }

    public ServiceResponse(JSONObject jSONObject) {
        this.helper = new JSONHelper();
        this.serviceResponse = jSONObject;
        this.info = new Info(this.helper.getJSONObject("info", jSONObject));
    }

    JSONHelper getHelper() {
        return this.helper;
    }
}
