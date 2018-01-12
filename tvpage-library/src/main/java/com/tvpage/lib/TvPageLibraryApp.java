package com.tvpage.lib;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by MTPC-133 on 11/30/2017.
 */

public class TvPageLibraryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register Caligraphy for font
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Dosis-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }
}
