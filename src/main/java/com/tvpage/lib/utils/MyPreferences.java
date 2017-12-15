package com.tvpage.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MTPC on 10/4/2016.
 */

public class MyPreferences {

    /*SharedPreferences prefs = context.getSharedPreferences(name, mode);
    SharedPreferences.Editor editor = prefs.edit();
    editor.remove(your_key)
            editor.commit();*/


    public static String prefId = "prefIdTvPage";


    //pref name for login_user id
    public static final String USER_ID_PREF_KEY = "USER_ID_PREF_KEY";
    public static final String CHANNEL_ID_PREF_KEY = "CHANNEL_ID_PREF_KEY";



    public static String getPref(Context mContext, String prefkey) {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE);
            return preferences.getString(prefkey, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setPref(Context mContext, String prefKey, String prefValue) {
        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

            if (prefValue != null) {
                editor.remove(prefKey);
                editor.putString(prefKey, prefValue);
            } else {
                editor.remove(prefKey);
                editor.putString(prefKey, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
