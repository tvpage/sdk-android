package com.tvpage.lib.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by MTPC-110 on 4/4/2017.
 */

public class TvPageResponseModel {
    JSONObject jsonObject;
    JSONArray jsonArray;


    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }


}
