package com.tvpage.lib;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.tvpage.lib.view.TvPagePlayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class FullScreenVideoPlayerActivity extends AppCompatActivity {

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private Uri uri = null;
    private long position = 0;
    int controlsVisible = 0;
    private boolean video_completedi = false;
    private boolean video_mutedi = false;
    private String video_statei = "";
    private int video_volumei = 0;
    private int video_quality_leveli = 0;
    private ArrayList<HashMap<String, String>> arrayListQualityi = new ArrayList<HashMap<String, String>>();
    TvPagePlayer tvPagePlayer;
    FrameLayout framesparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        goFullscreen();
        getIntentData();
        init();
        //showVideoPlayer();
    }

    private void getIntentData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("video_url")) {
                uri = Uri.parse(b.getString("video_url"));
            }

            if (b.containsKey("video_position")) {
                position = b.getLong("video_position");
            }

            if (b.containsKey("video_completed")) {
                video_completedi = b.getBoolean("video_completed");
            }

            if (b.containsKey("video_mute")) {
                video_mutedi = b.getBoolean("video_mute");
            }

            if (b.containsKey("video_state")) {
                video_statei = b.getString("video_state");
            }
            if (b.containsKey("video_volume")) {
                video_volumei = b.getInt("video_volume");
            }

            if (b.containsKey("video_quality_data")) {
                arrayListQualityi = (ArrayList<HashMap<String, String>>) b.getSerializable("video_quality_data");
            }

            if (b.containsKey("video_quality_level")) {
                video_quality_leveli = b.getInt("video_quality_level");
            }

        }
    }


    private void init() {

        framesparent = (FrameLayout) findViewById(R.id.framesparent);
        tvPagePlayer = (TvPagePlayer) findViewById(R.id.tvPagePlayer);

        TvPagePlayer.FullscreenExitListener fullScreenExitListener = new TvPagePlayer.FullscreenExitListener() {
            @Override
            public void onFullscreenExit() {
                onBackPressed();
            }

            @Override
            public void onHideControl() {
                goFullscreen();
            }
        };

        Method method = null;
        try {


            //update [refrences for video last position
            updatePrefForPosition((int) position);

            //Access Private Methods using Reflection api.....
            if (arrayListQualityi != null && arrayListQualityi.size() > 0) {

                method = TvPagePlayer.class.getDeclaredMethod("setDataToQualityList", ArrayList.class, int.class, String.class, Context.class);
                method.setAccessible(true);
                method.invoke(tvPagePlayer, arrayListQualityi, video_quality_leveli, video_statei, FullScreenVideoPlayerActivity.this);

            }

            Method methodFlag = TvPagePlayer.class.getDeclaredMethod("setFullscreenFlag", boolean.class, TvPagePlayer.FullscreenExitListener.class);
            methodFlag.setAccessible(true);
            methodFlag.invoke(tvPagePlayer, true, fullScreenExitListener);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDestroy() {

        //update [refrences for video last position
        //Access Private Methods using Reflection api.....

        super.onDestroy();
        exitFullscreen();


    }

    private void goFullscreen() {
        setUiFlags(true);
    }

    private void exitFullscreen() {
        setUiFlags(false);
    }

    /**
     * Applies the correct flags to the windows decor view to enter
     * or exit fullscreen mode
     *
     * @param fullscreen True if entering fullscreen mode
     */
    private void setUiFlags(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(fullscreen ? getFullscreenUiFlags() : View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * Determines the appropriate fullscreen flags based on the
     * systems API version.
     *
     * @return The appropriate decor view flags to enter fullscreen mode when supported
     */
    private int getFullscreenUiFlags() {
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        return flags;
    }

    /**
     * Listens to the system to determine when to show the default controls
     * for the {@link VideoView}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class FullScreenListener implements View.OnSystemUiVisibilityChangeListener {
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                /*Show controls*/
                Log.wtf("FullScreenActivity", "Show controls");
                // llControls.setVisibility(View.VISIBLE);
            }
        }
    }

    void updatePrefForPosition(int position) {
        try {
            SharedPreferences mPref = getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putInt("video_position", position);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



     /*   if (tvPagePlayer != null) {
            Method method = null;
            Method methodCurrentVideoPosition = null;
            int videoCurrentPosToPass = 0;


            try {
                System.out.println("@@@@ ON BACKKKKACTIVTYYYYYYYYYYYYYYYYY@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

             *//*   methodCurrentVideoPosition = TvPagePlayer.class.getDeclaredMethod("getVideoViewCurrentPositions");
                methodCurrentVideoPosition.setAccessible(true);
                long videoCurrentPosition = (long) methodCurrentVideoPosition.invoke(tvPagePlayer);
                videoCurrentPosToPass = (int) videoCurrentPosition;*//*

                long videoCurrentPosition = tvPagePlayer.getVideoViewCurrentPositions();
                videoCurrentPosToPass = (int) videoCurrentPosition;


                method = TvPagePlayer.class.getDeclaredMethod("setBackFromFullscreen", ArrayList.class, int.class);
                method.setAccessible(true);
                method.invoke(tvPagePlayer, arrayListQualityi, videoCurrentPosToPass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

            if (tvPagePlayer != null) {
                tvPagePlayer.onSaveInstance();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (tvPagePlayer != null) {
                tvPagePlayer.onRestoreInstance();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}