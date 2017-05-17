package com.tvpage.lib.utils;

import android.content.Context;

/**
 * Created by MTPC-110 on 4/14/2017.
 */

public final class TvPageInstance {
    private Context context;


    private static volatile TvPageInstance instance = null;

    private TvPageInstance(Context context) {
        this.context = context;
    }

    public static TvPageInstance getInstance(Context context) {
        if (instance == null) {
            synchronized (TvPageInstance.class) {
                if (instance == null) {
                    instance = new TvPageInstance(context);
                }
            }
        }
        return instance;
    }

    public void setApiKey(String apiKey) {
        MyPreferences.setPref(context, MyPreferences.USER_ID_PREF_KEY,apiKey);
    }

    public String getApiKey() {
        return MyPreferences.getPref(context, MyPreferences.USER_ID_PREF_KEY);
    }
}
