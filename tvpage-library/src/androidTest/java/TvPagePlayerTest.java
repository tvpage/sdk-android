import android.annotation.SuppressLint;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.LayoutInflater;

import com.tvpage.lib.R;
import com.tvpage.lib.utils.TvPageInterfaces;
import com.tvpage.lib.view.TvPageBuilder;
import com.tvpage.lib.view.TvPagePlayer;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by MTPC-133 on 1/18/2018.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TvPagePlayerTest extends TestCase {

    public TvPagePlayer mPlayer;
    private Context instrumentationCtx;


    @SuppressLint("InflateParams")
    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getContext();
        mPlayer = new TvPagePlayer(instrumentationCtx);

//        mPlayer = (TvPagePlayer) LayoutInflater.from(instrumentationCtx)
//                .inflate(R.layout.tvpage_player, null);


//        https://app.tvpage.com/api/channels/66133905/videos?X-login-id=1758799&p=0&n=25

       /* JSONObject parent = new JSONObject();
        try {


            JSONObject seekbar = new JSONObject();
            seekbar.put("progressColor", "#FFFFFF");


            JSONObject analytics = new JSONObject();
            analytics.put("tvpa", "true");


            parent.put("active", "true");
            parent.put("seekbar", seekbar);
            parent.put("analytics", analytics);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //builder
        TvPageBuilder tvPageBuilder = new TvPageBuilder(mPlayer).
                setOnVideoViewReady(new TvPageInterfaces.OnVideoViewReady() {
                    @Override
                    public void OnVideoViewReady(boolean isVideoLoaded) {
                        //TvPagePlayer Ready call back
//                        Log.d(TAG, "OnVideoViewReady: ");
                    }
                }).
                setOnVideoViewError(new TvPageInterfaces.OnVideoViewError() {
                    @Override
                    public void OnVideoViewError(String error) {
                        //TvPagePlayer Error call back
//                        Log.d(TAG, "OnVideoViewError: ");
                    }
                }).
                setOnMediaError(new TvPageInterfaces.OnMediaError() {
                    @Override
                    public void OnMediaError(String error) {
                        //TvPagePlayer media Error call back
//                        Log.d(TAG, "OnMediaError: ");
                    }
                })
                .setOnMediaReady(new TvPageInterfaces.OnMediaReady() {
                    @Override
                    public void OnMediaReady(boolean isMediaReady) {
                        //TvPagePlayer video ready call back
//                        Log.d(TAG, "OnMediaReady: ");
                    }
                }).setOnMediaComplete(new TvPageInterfaces.OnMediaComplete() {
                    @Override
                    public void OnMediaComplete(boolean isMediaCompleted) {
//                        Log.d(TAG, "OnMediaComplete: ");
                    }
                }).setOnVideoEnded(new TvPageInterfaces.OnVideoEnded() {
                    @Override
                    public void OnVideoEnded(boolean isVideoEnded) {
                        //TvPagePlayer video end call back
//                        Log.d(TAG, "OnVideoEnded: ");
                    }
                }).setOnVideoPlaying(new TvPageInterfaces.OnVideoPlaying() {
                    @Override
                    public void OnVideoPlaying(boolean isVideoPlaying) {
                        //TvPagePlayer video playing call back
//                        Log.d(TAG, "OnVideoPlaying: ");
                    }
                }).setOnVideoPaused(new TvPageInterfaces.OnVideoPaused() {
                    @Override
                    public void OnVideoPaused(boolean isVideoPaused) {
                        //TvPagePlayer video paused call back
//                        Log.d(TAG, "OnVideoPaused: ");
                    }
                }).setOnVideoBuffering(new TvPageInterfaces.OnVideoBuffering() {
                    @Override
                    public void OnVideoBuffering(boolean isVideoBuffering) {
                        //TvPagePlayer video buffering call back
//                        Log.d(TAG, "OnVideoBuffering: ");
                    }
                }).setOnMediaPlayBackQualityChanged(new TvPageInterfaces.OnMediaPlayBackQualityChanged() {
                    @Override
                    public void OnMediaPlayBackQualityChanged(String selectedQuality) {
                        //TvPagePlayer video quality changed call back
//                        Log.d(TAG, "OnMediaPlayBackQualityChanged: ");
                    }
                }).setOnSeek(new TvPageInterfaces.OnSeek() {
                    @Override
                    public void OnSeek(String currentVideoTime) {
                        //TvPagePlayer seek call back
//                        Log.d(TAG, "OnSeek: ");
                    }
                }).setOnVideoLoad(new TvPageInterfaces.OnVideoLoad() {
                    @Override
                    public void OnVideoLoad(boolean isVideoLoaded) {
                        //TvPagePlayer Video loaded
//                        Log.d(TAG, "OnVideoLoad: ");
                    }
                }).setOnVideoCued(new TvPageInterfaces.OnVideoCued() {
                    @Override
                    public void OnVideoCued(boolean isVideoLoaded) {
                        //TvPagePlayer Video cued
//                        Log.d(TAG, "OnVideoCued: ");
                    }
                }).setOnReady(new TvPageInterfaces.OnReady() {
                    @Override
                    public void OnPlayerReady() {
                        //TvPagePlayer Ready call back
//                        Log.d(TAG, "OnPlayerReady: ");
                    }
                }).setOnStateChanged(new TvPageInterfaces.OnStateChanged() {
                    @Override
                    public void OnStateChanged() {
                        //TvPagePlayer state changed call back
//                        Log.d(TAG, "OnStateChanged: ");
                    }
                })
                .setOnError(new TvPageInterfaces.OnError() {
                    @Override
                    public void OnError() {
                        //TvPagePlayer error callback
//                        Log.d(TAG, "OnError: ");

                    }
                })
                .controls(parent)
                .size(0, 0).initialise();*/
    }

    @Test
    public void check() {
//        mPlayer.onSaveInstance();
//        mPlayer.onRestoreInstance();
      /*  mPlayer.play();
        mPlayer.pause();
        mPlayer.stop();
        mPlayer.volume(10);
        mPlayer.mute();
        mPlayer.unmute();
        mPlayer.setQuality(1);*/
    }
}
