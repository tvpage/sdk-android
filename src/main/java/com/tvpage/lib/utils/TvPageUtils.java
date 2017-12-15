package com.tvpage.lib.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tvpage.lib.R;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by MTPC-110 on 3/8/2017.
 */

public class TvPageUtils {


    public static String YOUTUBE_VIDEO_TYPE = "youtube";
    public static String VIMEO_VIDEO_TYPE = "vimeo";
    public static String NORMAL_TVPAGE_VIDEO_TYPE = "mp4";

    public static String YOUTUBE_PRE_URLS = "https://www.youtube.com/watch?v=";
    public static String VIMEO_PRE_URLS = "https://vimeo.com/";

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static String milliSecondsToSeconds(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time

        int seconds = (int) (milliseconds / 1000.0);


        // Prepending 0 to seconds if it is one digit
        /*if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }*/

        secondsString = "" + seconds;

        finalTimerString = secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, long totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }


    public static void sout(String object) {
        // System.out.println("" + object);
    }

    public static void loadImageUsingGlide(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.bg_black)
                    .error(R.drawable.bg_black)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static OkHttpClient getOkHttpClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
