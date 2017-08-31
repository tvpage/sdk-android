package com.tvpage.lib.view;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.tvpage.lib.FullScreenVideoPlayerActivity;
import com.tvpage.lib.R;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.utils.SpinnerAdapterQuality;
import com.tvpage.lib.utils.TvPageException;
import com.tvpage.lib.utils.TvPageInstance;
import com.tvpage.lib.utils.TvPageInterfaces;
import com.tvpage.lib.utils.TvPageUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import okhttp3.Request;
import okhttp3.Response;
import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;

import static android.media.MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING;
import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END;
import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START;
import static android.media.MediaPlayer.MEDIA_INFO_METADATA_UPDATE;
import static android.media.MediaPlayer.MEDIA_INFO_NOT_SEEKABLE;
import static android.media.MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT;
import static android.media.MediaPlayer.MEDIA_INFO_UNKNOWN;
import static android.media.MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
import static android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
import static android.media.MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING;
import static com.tvpage.lib.utils.TvPageUtils.NORMAL_TVPAGE_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.getOkHttpClient;

/*import com.devbrackets.android.exomedia.core.EMListenerMux;
import com.devbrackets.android.exomedia.core.exoplayer.EMExoPlayer;
import com.devbrackets.android.exomedia.core.listener.Id3MetadataListener;
import com.devbrackets.android.exomedia.core.listener.InfoListener;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import com.google.android.exoplayer.metadata.id3.Id3Frame;*/

/**
 * Created by MTPC-110 on 3/9/2017.
 */
public class TvPagePlayer extends RelativeLayout implements View.OnClickListener, OnCompletionListener,
        OnPreparedListener, MediaPlayer.OnInfoListener
        , SeekBar.OnSeekBarChangeListener, OnErrorListener {

    //controls
    private boolean isCallAnalyticsApi = true;

    //fullscreen flag
    private boolean isFullScreenEnable = true;

    //analytics
    private String videoIdForAnalytics = "";
    private String channelIdForAnalytics = "";

    private Context mContext;

    private String baseUrlApi = "https://app.tvpage.com/api";
    private final String baseUrlAnalyticsApi = "http://api.tvpage.com/v1/__tvpa.gif";

    private VideoView videoView;
    private FrameLayout controllerAnchor;
    private LinearLayout video_control, llSeekBar;
    private RelativeLayout rlTime, relativesParent, llButtons;

    private RelativeLayout relVideoViewPGesture;
    private ImageView play_video, imgFullScreen, imgQuality;
    /*pause_video*/
    private SeekBarCompat seekbarVideo, seekBarVolume;
    private TextView play_time, tvHd;
    //play_total_time
    /*private TextView tv144;
    private TextView tv240;
    private TextView tv360;
    private TextView tv480;
    private TextView tv720;
    private TextView tv1080;*/
    private ImageView imgVolume, imgPoster;
    /*,stop_video*/
    // private Spinner spinnerQuality;

    /**
     * The Recycler quality.
     */
    RecyclerView recyclerQuality;
    /**
     * The Quality adapter.
     */
    QualityAdapter qualityAdapter;


    /**
     * The M handler.
     */
    Handler mHandler = new Handler();
    /**
     * The M handler three second once.
     */
    Handler mHandlerThreeSecondOnce = new Handler();
    /**
     * The M handler three second repeat.
     */
    Handler mHandlerThreeSecondRepeat = new Handler();

    //private ProgressDialog progressDialog;

    private ProgressBar progressMediaController;

    /**
     * The Utils.
     */
    TvPageUtils utils;

    private int position = 0;
    private String urlToPlayDesiredQuality = "";

    private boolean isVideoCompleted = false;

    private boolean isVideoPlayedUi = false;

    private boolean isFullScreenMode = false;


    private int volume_video = 0;

    private int heightOfView = 0;
    private int widthOfView = 0;

    private long currentTime = 0;
    private long duration = 0;

    private int qualityLevelCurrent = 0;
    // Root view's LayoutParams
    private RelativeLayout.LayoutParams mRootParam;

    /**
     * The constant VIDEO_UNSTARTED.
     */
/*unstarted:  -1,
    ended:      0,
    playing:    1,
    paused:     2,
    buffering:  3,
    cued:       5*/
    public static String VIDEO_UNSTARTED = "-1";
    /**
     * The constant VIDEO_ENDED.
     */
    public static String VIDEO_ENDED = "0";
    /**
     * The constant VIDEO_PLAYED.
     */
    public static String VIDEO_PLAYED = "1";
    /**
     * The constant VIDEO_PAUSED.
     */
    public static String VIDEO_PAUSED = "2";
    /**
     * The constant VIDEO_BUFFERING.
     */
    public static String VIDEO_BUFFERING = "3";
    /**
     * The constant VIDEO_CUED.
     */
    public static String VIDEO_CUED = "5";

    private String murlToPlay = "";
    private String state = VIDEO_UNSTARTED;

    /**
     * The Audio manager.
     */
//audio
    AudioManager audioManager;

    private boolean isMuted = false;

    private int volume = 0;

    private boolean isStopped = false;
    private boolean isQualityVisible = false;

    private int controlsVisible = 0;


    //cued flags
    private boolean isCueVideoFlag = false;
    private boolean isLoadVideoFlag = false;

    /**
     * The constant MIN_WIDTH.
     */
//gesture
    // minimum video view width
    static final int MIN_WIDTH = 100;

    private static final String HLS_DASH_TAG = "8000p";


    // detector to pinch zoom in/out
    private ScaleGestureDetector mScaleGestureDetector;
    /**
     * The Matrix.
     */
    Matrix matrix = new Matrix();

    private boolean isFirstThreeSecondApiCalled = false;

    /**
     * The Dash or hls url from intent.
     */
    String dashOrHlsUrlFromIntent = "";


    /**
     * The List of quality.
     */
    ArrayList<HashMap<String, String>> listOfQuality = new ArrayList<HashMap<String, String>>();


    /**
     * The Check spinner.
     */
    int checkSpinner = 0;

    /**
     * The M settings content observer.
     */
    SettingsContentObserver mSettingsContentObserver;

    /**
     * The On video view ready.
     */
//interface values
    TvPageInterfaces.OnVideoViewReady onVideoViewReady;
    /**
     * The On video view error.
     */
    TvPageInterfaces.OnVideoViewError onVideoViewError;
    /**
     * The On media ready.
     */
    TvPageInterfaces.OnMediaReady onMediaReady;
    /**
     * The On media error.
     */
    TvPageInterfaces.OnMediaError onMediaError;
    /**
     * The On media complete.
     */
    TvPageInterfaces.OnMediaComplete onMediaComplete;
    /**
     * The On video ended.
     */
    TvPageInterfaces.OnVideoEnded onVideoEnded;
    /**
     * The On video playing.
     */
    TvPageInterfaces.OnVideoPlaying onVideoPlaying;
    /**
     * The On video paused.
     */
    TvPageInterfaces.OnVideoPaused onVideoPaused;
    /**
     * The On video buffering.
     */
    TvPageInterfaces.OnVideoBuffering onVideoBuffering;
    /**
     * The On media play back quality changed.
     */
    TvPageInterfaces.OnMediaPlayBackQualityChanged onMediaPlayBackQualityChanged;
    /**
     * The On media provider changed.
     */
    TvPageInterfaces.OnMediaProviderChanged onMediaProviderChanged;
    /**
     * The On seek.
     */
    TvPageInterfaces.OnSeek onSeek;
    /**
     * The On video load.
     */
    TvPageInterfaces.OnVideoLoad onVideoLoad;
    /**
     * The On video cued.
     */
    TvPageInterfaces.OnVideoCued onVideoCued;
    /**
     * The On ready.
     */
    TvPageInterfaces.OnReady onReady;
    /**
     * The On state changed.
     */
    TvPageInterfaces.OnStateChanged onStateChanged;
    /**
     * The On error.
     */
    TvPageInterfaces.OnError onError;



    /**
     * Gets on error.
     *
     * @return the on error
     */
    public TvPageInterfaces.OnError getOnError() {
        return onError;
    }

    /**
     * Sets on error.
     *
     * @param onError the on error
     */
    public void setOnError(TvPageInterfaces.OnError onError) {
        this.onError = onError;
    }


    /**
     * Gets on state changed.
     *
     * @return the on state changed
     */
    public TvPageInterfaces.OnStateChanged getOnStateChanged() {
        return onStateChanged;
    }

    /**
     * Sets on state changed.
     *
     * @param onStateChanged the on state changed
     */
    public void setOnStateChanged(TvPageInterfaces.OnStateChanged onStateChanged) {
        this.onStateChanged = onStateChanged;
    }


    /**
     * Gets on ready.
     *
     * @return the on ready
     */
    public TvPageInterfaces.OnReady getOnReady() {
        return onReady;
    }

    /**
     * Sets on ready.
     *
     * @param onReady the on ready
     */
    public void setOnReady(TvPageInterfaces.OnReady onReady) {
        this.onReady = onReady;
    }


    /**
     * Gets on video view ready.
     *
     * @return the on video view ready
     */
    public TvPageInterfaces.OnVideoViewReady getOnVideoViewReady() {
        return onVideoViewReady;
    }

    /**
     * Sets on video view ready.
     *
     * @param onVideoViewReady the on video view ready
     */
    public void setOnVideoViewReady(TvPageInterfaces.OnVideoViewReady onVideoViewReady) {
        this.onVideoViewReady = onVideoViewReady;
    }

    /**
     * Gets on video view error.
     *
     * @return the on video view error
     */
    public TvPageInterfaces.OnVideoViewError getOnVideoViewError() {
        return onVideoViewError;
    }

    /**
     * Sets on video view error.
     *
     * @param onVideoViewError the on video view error
     */
    public void setOnVideoViewError(TvPageInterfaces.OnVideoViewError onVideoViewError) {
        this.onVideoViewError = onVideoViewError;
    }

    /**
     * Gets on media ready.
     *
     * @return the on media ready
     */
    public TvPageInterfaces.OnMediaReady getOnMediaReady() {
        return onMediaReady;
    }

    /**
     * Sets on media ready.
     *
     * @param onMediaReady the on media ready
     */
    public void setOnMediaReady(TvPageInterfaces.OnMediaReady onMediaReady) {
        this.onMediaReady = onMediaReady;
    }

    /**
     * Gets on media error.
     *
     * @return the on media error
     */
    public TvPageInterfaces.OnMediaError getOnMediaError() {
        return onMediaError;
    }

    /**
     * Sets on media error.
     *
     * @param onMediaError the on media error
     */
    public void setOnMediaError(TvPageInterfaces.OnMediaError onMediaError) {
        this.onMediaError = onMediaError;
    }

    /**
     * Gets on media complete.
     *
     * @return the on media complete
     */
    public TvPageInterfaces.OnMediaComplete getOnMediaComplete() {
        return onMediaComplete;
    }

    /**
     * Sets on media complete.
     *
     * @param onMediaComplete the on media complete
     */
    public void setOnMediaComplete(TvPageInterfaces.OnMediaComplete onMediaComplete) {
        this.onMediaComplete = onMediaComplete;
    }

    /**
     * Gets on video ended.
     *
     * @return the on video ended
     */
    public TvPageInterfaces.OnVideoEnded getOnVideoEnded() {
        return onVideoEnded;
    }

    /**
     * Sets on video ended.
     *
     * @param onVideoEnded the on video ended
     */
    public void setOnVideoEnded(TvPageInterfaces.OnVideoEnded onVideoEnded) {
        this.onVideoEnded = onVideoEnded;
    }

    /**
     * Gets on video playing.
     *
     * @return the on video playing
     */
    public TvPageInterfaces.OnVideoPlaying getOnVideoPlaying() {
        return onVideoPlaying;
    }

    /**
     * Sets on video playing.
     *
     * @param onVideoPlaying the on video playing
     */
    public void setOnVideoPlaying(TvPageInterfaces.OnVideoPlaying onVideoPlaying) {
        this.onVideoPlaying = onVideoPlaying;
    }

    /**
     * Gets on video paused.
     *
     * @return the on video paused
     */
    public TvPageInterfaces.OnVideoPaused getOnVideoPaused() {
        return onVideoPaused;
    }

    /**
     * Sets on video paused.
     *
     * @param onVideoPaused the on video paused
     */
    public void setOnVideoPaused(TvPageInterfaces.OnVideoPaused onVideoPaused) {
        this.onVideoPaused = onVideoPaused;
    }

    /**
     * Gets on video buffering.
     *
     * @return the on video buffering
     */
    public TvPageInterfaces.OnVideoBuffering getOnVideoBuffering() {
        return onVideoBuffering;
    }

    /**
     * Sets on video buffering.
     *
     * @param onVideoBuffering the on video buffering
     */
    public void setOnVideoBuffering(TvPageInterfaces.OnVideoBuffering onVideoBuffering) {
        this.onVideoBuffering = onVideoBuffering;
    }

    /**
     * Gets on media play back quality changed.
     *
     * @return the on media play back quality changed
     */
    public TvPageInterfaces.OnMediaPlayBackQualityChanged getOnMediaPlayBackQualityChanged() {
        return onMediaPlayBackQualityChanged;
    }

    /**
     * Sets on media play back quality changed.
     *
     * @param onMediaPlayBackQualityChanged the on media play back quality changed
     */
    public void setOnMediaPlayBackQualityChanged(TvPageInterfaces.OnMediaPlayBackQualityChanged onMediaPlayBackQualityChanged) {
        this.onMediaPlayBackQualityChanged = onMediaPlayBackQualityChanged;
    }

    /**
     * Gets on media provider changed.
     *
     * @return the on media provider changed
     */
    public TvPageInterfaces.OnMediaProviderChanged getOnMediaProviderChanged() {
        return onMediaProviderChanged;
    }

    /**
     * Sets on media provider changed.
     *
     * @param onMediaProviderChanged the on media provider changed
     */
    public void setOnMediaProviderChanged(TvPageInterfaces.OnMediaProviderChanged onMediaProviderChanged) {
        this.onMediaProviderChanged = onMediaProviderChanged;
    }

    /**
     * Gets on seek.
     *
     * @return the on seek
     */
    public TvPageInterfaces.OnSeek getOnSeek() {
        return onSeek;
    }

    /**
     * Sets on seek.
     *
     * @param onSeek the on seek
     */
    public void setOnSeek(TvPageInterfaces.OnSeek onSeek) {
        this.onSeek = onSeek;
    }

    /**
     * Gets on video load.
     *
     * @return the on video load
     */
    public TvPageInterfaces.OnVideoLoad getOnVideoLoad() {
        return onVideoLoad;
    }

    /**
     * Sets on video load.
     *
     * @param onVideoLoad the on video load
     */
    public void setOnVideoLoad(TvPageInterfaces.OnVideoLoad onVideoLoad) {
        this.onVideoLoad = onVideoLoad;
    }

    /**
     * Gets on video cued.
     *
     * @return the on video cued
     */
    public TvPageInterfaces.OnVideoCued getOnVideoCued() {
        return onVideoCued;
    }

    /**
     * Sets on video cued.
     *
     * @param onVideoCued the on video cued
     */
    public void setOnVideoCued(TvPageInterfaces.OnVideoCued onVideoCued) {
        this.onVideoCued = onVideoCued;
    }


    /**
     * Instantiates a new Tv page player.
     *
     * @param context the context
     */
    public TvPagePlayer(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    /**
     * Instantiates a new Tv page player.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public TvPagePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    /**
     * Instantiates a new Tv page player.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public TvPagePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * Instantiates a new Tv page player.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TvPagePlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
    }

    //set url to play


    /**
     * Gets murl to play.
     *
     * @return the murl to play
     */
    public String getMurlToPlay() {
        return murlToPlay;
    }

    /**
     * Sets murl to play.
     *
     * @param murlToPlay the murl to play
     */
    public void setMurlToPlay(String murlToPlay) {
        this.murlToPlay = murlToPlay;
    }

    /*used to initialise views*/
    private void init() {

        View view = inflate(mContext, R.layout.tvpage_player, null);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        controllerAnchor = (FrameLayout) view.findViewById(R.id.controllerAnchor);
        video_control = (LinearLayout) view.findViewById(R.id.video_control);
        llSeekBar = (LinearLayout) view.findViewById(R.id.llSeekBar);
        llButtons = (RelativeLayout) view.findViewById(R.id.llButtons);


        rlTime = (RelativeLayout) view.findViewById(R.id.rlTime);
        relVideoViewPGesture = (RelativeLayout) view.findViewById(R.id.relVideoViewPGesture);
        relativesParent = (RelativeLayout) view.findViewById(R.id.relativesParent);


        play_video = (ImageView) view.findViewById(R.id.play_video);
        imgFullScreen = (ImageView) view.findViewById(R.id.imgFullScreen);
        imgQuality = (ImageView) view.findViewById(R.id.imgQuality);
        ///ppause_video = (ImageView) view.findViewById(R.id.pause_video);
        seekbarVideo = (SeekBarCompat) view.findViewById(R.id.seekbarVideo);
        seekBarVolume = (SeekBarCompat) view.findViewById(R.id.seekBarVolume);
        play_time = (TextView) view.findViewById(R.id.play_time);
        tvHd = (TextView) view.findViewById(R.id.tvHd);
        //play_total_time = (TextView) view.findViewById(R.id.play_total_time);
       /* tv144 = (TextView) view.findViewById(R.id.tv144);
        tv240 = (TextView) view.findViewById(R.id.tv240);
        tv360 = (TextView) view.findViewById(R.id.tv360);
        tv480 = (TextView) view.findViewById(R.id.tv480);
        tv720 = (TextView) view.findViewById(R.id.tv720);
        tv1080 = (TextView) view.findViewById(R.id.tv1080);*/
        progressMediaController = (ProgressBar) view.findViewById(R.id.progressMediaController);
        imgVolume = (ImageView) view.findViewById(R.id.imgVolume);
        //stop_video = (ImageView) view.findViewById(R.id.stop_video);
        mRootParam = (LayoutParams) ((View) relVideoViewPGesture).getLayoutParams();
        //spinnerQuality = (Spinner) view.findViewById(R.id.spinnerQuality);
        imgPoster = (ImageView) view.findViewById(R.id.imgPoster);

        recyclerQuality = (RecyclerView) view.findViewById(R.id.recyclerQuality);
        LinearLayoutManager vertLinearLayoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        /*int spanCount = 2; // 3 columns
        int spacing = 20; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);*/
        recyclerQuality.setHasFixedSize(true);
        recyclerQuality.setLayoutManager(vertLinearLayoutManager);


        addView(view);

        utils = new TvPageUtils();

        seekBarVolume.setPadding(20, 0, 20, 0);
        seekbarVideo.setPadding(20, 0, 20, 0);

        //set seekbar color


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            seekbarVideo.setSecondaryProgress(ContextCompat.getColor(mContext, R.color.red));
        } else {
            seekbarVideo.setSecondaryProgress(mContext.getResources().getColor(R.color.red));
        }


        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            //set intial voume of seekbar from audio manager
            //TvPageUtils.sout("Max Volume is: " + audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volume_video = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        }

        //set seek bar intial data
        seekbarVideo.setProgress(0);
        seekbarVideo.setMax(100);


        play_video.setOnClickListener(this);
        imgFullScreen.setOnClickListener(this);
        imgQuality.setOnClickListener(this);
        relativesParent.setOnClickListener(this);
        controllerAnchor.setOnClickListener(this);
        //ppause_video.setOnClickListener(this);
        imgVolume.setOnClickListener(this);
        //stop_video.setOnClickListener(this);
        seekbarVideo.setOnSeekBarChangeListener(this);
        seekBarVolume.setOnSeekBarChangeListener(this);
        videoView.setOnPreparedListener(this);
        //videoView.setOnInfoListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);

        videoView.setOnBufferUpdateListener(new OnBufferUpdateListener() {
            @Override
            public void onBufferingUpdate(@IntRange(from = 0L, to = 100L) int percent) {
                //Log.d("percentage", "" + percent);
                if (seekbarVideo != null) {
                    seekbarVideo.setSecondaryProgress(percent);
                }

            }
        });


        //relativesParent.addView(zoomView);


        // set up gesture listeners

        mScaleGestureDetector = new ScaleGestureDetector(mContext, new MyScaleGestureListener());
       /* videoView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });*/
     /*   relativesParent.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (recyclerQuality != null && recyclerQuality.getVisibility() == VISIBLE) {
                    isQualityVisible = false;
                    recyclerQuality.setVisibility(INVISIBLE);
                }
                mScaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });*/
        /*relativesParent.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (recyclerQuality != null && recyclerQuality.getVisibility() == VISIBLE) {
                    isQualityVisible = false;
                    recyclerQuality.setVisibility(INVISIBLE);
                }


                return true;
            }
        });
*/

        //register content observer to detect system's volume change by end user manually
        mSettingsContentObserver = new SettingsContentObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true,
                mSettingsContentObserver);
    }

    @Override
    public boolean onError(Exception e) {
        if (e != null) {
            e.printStackTrace();
            if (e.getCause().getMessage().contains("403")) {
                // Log.e("OnError", urlToPlayDesiredQuality);

               /* if (recyclerQuality != null) {
                    if (recyclerQuality.findViewHolderForAdapterPosition(2) != null) {
                        recyclerQuality.findViewHolderForAdapterPosition(2).itemView.performClick();
                    } else if (recyclerQuality.findViewHolderForAdapterPosition(3) != null) {
                        recyclerQuality.findViewHolderForAdapterPosition(3).itemView.performClick();
                    }
                }*/
            }
        }

        if (onError != null) {
            onError.OnError();
        }

        return true;
    }


    /**
     * The type Settings content observer.
     */
//set Volume when system voulme change using device's up and down volume buttons
    public class SettingsContentObserver extends ContentObserver {

        /**
         * Instantiates a new Settings content observer.
         *
         * @param handler the handler
         */
        public SettingsContentObserver(Handler handler) {

            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //Log.v("TvPage Volumesss ", "Volumes  change detected");
            audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                //set intial voume of seekbar from audio manager
                //TvPageUtils.sout("Max Volume is: " + audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                volume_video = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

            }

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mContext != null) {
            mContext.getContentResolver().unregisterContentObserver(mSettingsContentObserver);
        }

        if (mHandlerThreeSecondRepeat != null) {
            mHandlerThreeSecondRepeat.removeCallbacks(runnableThreeSecondRepeat);
        }

        if (mHandler != null) {
            mHandler.removeCallbacks(mUpdateTimeTask);
        }

        if (mHandlerThreeSecondOnce != null) {
            mHandlerThreeSecondOnce.removeCallbacks(runnableThreeSecondOnce);
        }

        super.onDetachedFromWindow();

    }

    /**
     * Sets data to quality list.
     */
    void setDataToQualityList() {
        if (listOfQuality != null && listOfQuality.size() > 0) {

            //check for 0 position only..as auto adds on 0 first's position only using Comparator
            for (String key : listOfQuality.get(0).keySet()) {
                if (key != null && key.trim().length() > 0) {
                    if (key.equalsIgnoreCase(HLS_DASH_TAG)) {
                        //do nothing as there is hls or dash url is available
                    } else {
                        //need to add auto field as no hls or dash url is available...manage as per network bandwidth
                        String connectionType = Connectivity.getConnectionSpeedRange(mContext);
                        if (connectionType != null
                                && !TextUtils.isEmpty(connectionType)) {
                            HashMap<String, String> hashMapAuto = new HashMap<String, String>();
                            if (connectionType.equalsIgnoreCase("wifi")) {
                                //if wifi is there..set 480p in auto
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("480p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //480p is not available ...put check for 360p
                                    hashMapAuto.clear();
                                    hashMapAuto = setQualityLevelToAuto("360p", listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    } else {
                                        //set default entry of last
                                        hashMapAuto.clear();
                                        hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                        if (hashMapAuto != null
                                                && hashMapAuto.size() > 0
                                                ) {
                                            listOfQuality.add(hashMapAuto);
                                        }
                                    }
                                }
                            } else if (connectionType.equalsIgnoreCase("2g")) {
                                //set 144p qulaity in auto...last position
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("144p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //set default entry of last
                                    hashMapAuto.clear();
                                    hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    }
                                }
                            } else if (connectionType.equalsIgnoreCase("3g")) {
                                //set 360p in auto..second last position
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("360p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //set default entry of last
                                    hashMapAuto.clear();
                                    hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    }
                                }

                            } else if (connectionType.equalsIgnoreCase("4g")) {
                                //set 480p in auto
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("480p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //480p is not available ...put check for 360p
                                    hashMapAuto.clear();
                                    hashMapAuto = setQualityLevelToAuto("360p", listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    } else {
                                        //set default entry of last
                                        hashMapAuto.clear();
                                        hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                        if (hashMapAuto != null
                                                && hashMapAuto.size() > 0
                                                ) {
                                            listOfQuality.add(hashMapAuto);
                                        }
                                    }
                                }

                            } else if (connectionType.equalsIgnoreCase("unknown")) {
                                //set last quality..last position
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("144p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //set default entry of last
                                    hashMapAuto.clear();
                                    hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    }
                                }

                            } else {
                                //default is last quality..last position
                                hashMapAuto.clear();
                                hashMapAuto = setQualityLevelToAuto("144p", listOfQuality);
                                if (hashMapAuto != null
                                        && hashMapAuto.size() > 0
                                        ) {
                                    listOfQuality.add(hashMapAuto);
                                } else {
                                    //set default entry of last
                                    hashMapAuto.clear();
                                    hashMapAuto = getDefaultEntryInAuto(listOfQuality);
                                    if (hashMapAuto != null
                                            && hashMapAuto.size() > 0
                                            ) {
                                        listOfQuality.add(hashMapAuto);
                                    }
                                }
                            }


                        }


                        // sort arraylist again using Comparator
                        //Sorting the Quality
                        Collections.sort(listOfQuality, new Comparator<HashMap<String, String>>() {
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                    /*Log.d("Dattaaa",""+o1.keySet().iterator().next().substring(0, o1.keySet().iterator().next().length() - 1));
                    Log.d("Dattaa2",""+o2.keySet().iterator().next().substring(0, o2.keySet().iterator().next().length() - 1));
                    */

                                int val1 = Integer.parseInt(o1.keySet().iterator().next().substring(0, o1.keySet().iterator().next().length() - 1));
                                int val2 = Integer.parseInt(o2.keySet().iterator().next().substring(0, o2.keySet().iterator().next().length() - 1));
                                return val2 - val1;
                            }

                        });
                    }
                }

            }


            //System.out.println("Lsit Gallery si>> " + list.size() + " is Load more " + isLoadMore);
            qualityAdapter = new QualityAdapter(mContext, listOfQuality);
            recyclerQuality.setAdapter(qualityAdapter);
        }
    }

    private HashMap<String, String> getDefaultEntryInAuto(ArrayList<HashMap<String, String>> hashMapArrayList) {
        HashMap<String, String> hashMapAuto = new HashMap<String, String>();
        try {
            if (hashMapArrayList.size() > 0) {
                for (int i = 0; i < hashMapArrayList.size(); i++) {
                    if (i == hashMapArrayList.size() - 1) {
                        for (Map.Entry<String, String> item : hashMapArrayList.get(i).entrySet()) {
                            hashMapAuto.put(HLS_DASH_TAG, item.getValue());
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashMapAuto;
    }

    //set quality
    private HashMap<String, String> setQualityLevelToAuto(String qualityName, ArrayList<HashMap<String, String>> arrayList) {
        HashMap<String, String> hashMapAuto = new HashMap<String, String>();
        try {
            if (arrayList.size() > 0) {
                for (HashMap<String, String> contact : arrayList) {
                    for (Map.Entry<String, String> item : contact.entrySet()) {
                        String key = item.getKey();
                        String value = item.getValue();
                        if (key.equalsIgnoreCase(qualityName)) {
                            hashMapAuto.put(HLS_DASH_TAG, value);
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return hashMapAuto;
    }


    /**
     * Sets data to quality list.
     *
     * @param hashMapArrayList    the hash map array list
     * @param qualityLevelCurrent the quality level current
     * @param video_state         the video state
     * @param mContext            the m context
     */
    void setDataToQualityList(ArrayList<HashMap<String, String>> hashMapArrayList, int qualityLevelCurrent, String video_state, Context mContext) {
        if (hashMapArrayList != null && hashMapArrayList.size() > 0) {

            if (recyclerQuality != null) {
                //check if state play/pause..if play then isLoadVideoFlag = true...or isVideoFlag=false
               /* if (video_state.equalsIgnoreCase(VIDEO_PLAYED)) {
                    isLoadVideoFlag = true;
                } else if (video_state.equalsIgnoreCase(VIDEO_PAUSED)) {
                    isLoadVideoFlag = false;
                } else {
                    isLoadVideoFlag = true;
                }*/

                //System.out.println("Lsit Gallery si>> " + list.size() + " is Load more " + isLoadMore);
                QualityAdapterFullScreen qualityAdapterFullScreen = new QualityAdapterFullScreen(mContext, hashMapArrayList, qualityLevelCurrent);
                recyclerQuality.setAdapter(qualityAdapterFullScreen);
            }
        }
    }

    /**
     * The type Quality adapter.
     */
    public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.ViewHolder> {

        private ArrayList<HashMap<String, String>> mData;
        private LayoutInflater mInflater;
        private boolean isFromItemClick = false;
        private int currentSlectedItem = 0;


        /**
         * Instantiates a new Quality adapter.
         *
         * @param context the context
         * @param data    the data
         */
// data is passed into the constructor
        public QualityAdapter(Context context, ArrayList<HashMap<String, String>> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;

        }

        /**
         * Instantiates a new Quality adapter.
         *
         * @param context                       the context
         * @param data                          the data
         * @param currentPositionFromFullscreen the current position from fullscreen
         * @param isFromItemClick               the is from item click
         */
// data is passed into the constructor
        public QualityAdapter(Context context, ArrayList<HashMap<String, String>> data, int currentPositionFromFullscreen
                , boolean isFromItemClick) {
            this.currentSlectedItem = currentPositionFromFullscreen;
            this.isFromItemClick = isFromItemClick;
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;

        }

        // inflates the cell layout from xml when needed
        @Override
        public QualityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.adapter_spinner_quality, parent, false);
            QualityAdapter.ViewHolder viewHolder = new QualityAdapter.ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(QualityAdapter.ViewHolder holder, final int position) {

            final HashMap<String, String> item = mData.get(position);

            //Log.d("Posss: ", "" + position + " : " + item.size() + " : " + mData.size());

            if (item.size() > 0) {
                for (String key : item.keySet()) {
                    if (key != null && key.trim().length() > 0) {
                        if (key.equalsIgnoreCase(HLS_DASH_TAG)) {
                            holder.list_item.setText("Auto");
                        } else {
                            holder.list_item.setText(key);
                        }
                    }

                }
            }


            //background color detremination
            if (position == currentSlectedItem) {
                //set orange background color
                holder.relativeSpinnerParent.setBackgroundColor(getColor(mContext, R.color.qualityBackground));
            } else {
                //set transperent color
                holder.relativeSpinnerParent.setBackgroundColor(getColor(mContext, R.color.rowBackground));
            }

            if (!isFromItemClick) {
                if (position == mData.size() - 1) {
                    // load more data here.last data has loaded
                    //do perform click of recyclerview
                    if (recyclerQuality.findViewHolderForAdapterPosition(0) != null) {
                        recyclerQuality.findViewHolderForAdapterPosition(0).itemView.performClick();
                    }
                }
            }

            holder.relativeSpinnerParent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String keysToPass = "";


                    //Save Current Quality Levels
                    qualityLevelCurrent = position;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //color selection process
                            isFromItemClick = true;
                            currentSlectedItem = position;
                            notifyDataSetChanged();
                        }
                    }, 500);


                    for (String key : mData.get(position).keySet()) {
                        keysToPass = key;

                        if (keysToPass.contains("1080") || keysToPass.contains("720")) {
                            //show hd icon upper part of setting
                            if (tvHd != null) {
                                tvHd.setVisibility(VISIBLE);
                            }
                        } else {
                            //Hide hd icon upper part of setting
                            if (tvHd != null) {
                                tvHd.setVisibility(INVISIBLE);
                            }
                        }

                        if (mData.get(position).get(key).startsWith("http")) {
                            urlToPlayDesiredQuality = mData.get(position).get(key);
                        } else {
                            urlToPlayDesiredQuality = "http:" + mData.get(position).get(key);
                        }

                    }

                    //set pref of quality key
                    SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString("video_quality_key", keysToPass);
                    editor.putInt("video_quality_level_current", qualityLevelCurrent);
                    editor.apply();


                    isQualityVisible = false;
                    //close/invisible recyclerview
                    recyclerQuality.setVisibility(INVISIBLE);


                    setPosOnPrefForQuality();


                    setVideosOnQualityChanged();
                }
            });
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        /**
         * The type View holder.
         */
// stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * The List item.
             */
            public TextView list_item;

            /**
             * The Relative spinner parent.
             */
            public RelativeLayout relativeSpinnerParent;


            /**
             * Instantiates a new View holder.
             *
             * @param itemView the item view
             */
            public ViewHolder(View itemView) {
                super(itemView);
                list_item = (TextView) itemView.findViewById(R.id.list_item);

                relativeSpinnerParent = (RelativeLayout) itemView.findViewById(R.id.relativeSpinnerParent);

            }


        }

        // convenience method for getting data at click position
        /*public VideoBean getItem(int id) {
            return mData.get(id);
        }*/


    }

    /**
     * The type Quality adapter full screen.
     */
    public class QualityAdapterFullScreen extends RecyclerView.Adapter<QualityAdapterFullScreen.ViewHolder> {

        private ArrayList<HashMap<String, String>> mData;
        private LayoutInflater mInflater;
        private int qualityLevel = 0;
        private boolean isFirstClickOfQuality = true;

        private boolean isFromItemClick = false;
        private int currentSlectedItem = 0;


        /**
         * Instantiates a new Quality adapter full screen.
         *
         * @param context      the context
         * @param data         the data
         * @param qualityLevel the quality level
         */
// data is passed into the constructor
        public QualityAdapterFullScreen(Context context, ArrayList<HashMap<String, String>> data, int qualityLevel) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.qualityLevel = qualityLevel;

        }

        // inflates the cell layout from xml when needed
        @Override
        public QualityAdapterFullScreen.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.adapter_spinner_quality, parent, false);
            QualityAdapterFullScreen.ViewHolder viewHolder = new QualityAdapterFullScreen.ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(QualityAdapterFullScreen.ViewHolder holder, final int position) {

            final HashMap<String, String> item = mData.get(position);

            //Log.d("Posss: ", "" + position + " : " + item.size() + " : " + mData.size());

            if (item.size() > 0) {
                for (String key : item.keySet()) {
                    if (key != null && key.trim().length() > 0) {
                        if (key.equalsIgnoreCase(HLS_DASH_TAG)) {
                            holder.list_item.setText("Auto");
                        } else {
                            holder.list_item.setText(key);
                        }
                    }

                }
            }


            //background color detremination
            if (position == currentSlectedItem) {
                //set orange background color
                holder.relativeSpinnerParent.setBackgroundColor(getColor(mContext, R.color.qualityBackground));
            } else {
                //set transperent color
                holder.relativeSpinnerParent.setBackgroundColor(getColor(mContext, R.color.rowBackground));

            }


            if (!isFromItemClick) {
                if (position == mData.size() - 1) {
                    // load more data here.last data has loaded
                    //do perform click of recyclerview
                    if (recyclerQuality.findViewHolderForAdapterPosition(qualityLevel) != null) {
                        recyclerQuality.findViewHolderForAdapterPosition(qualityLevel).itemView.performClick();
                    }
                }
            }

            holder.relativeSpinnerParent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String keysToPass = "";


                    //Save Current Quality Levels
                    qualityLevelCurrent = position;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //color selection process
                            isFromItemClick = true;
                            currentSlectedItem = position;
                            notifyDataSetChanged();
                        }
                    }, 500);


                    for (String key : mData.get(position).keySet()) {
                        keysToPass = key;

                        if (keysToPass.contains("1080") || keysToPass.contains("720")) {
                            //show hd icon upper part of setting
                            if (tvHd != null) {
                                tvHd.setVisibility(VISIBLE);
                            }
                        } else {
                            //Hide hd icon upper part of setting
                            if (tvHd != null) {
                                tvHd.setVisibility(INVISIBLE);
                            }
                        }

                        if (mData.get(position).get(key).startsWith("http")) {
                            urlToPlayDesiredQuality = mData.get(position).get(key);
                        } else {
                            urlToPlayDesiredQuality = "http:" + mData.get(position).get(key);
                        }


                    }


                    //set pref of quality key
                    SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString("video_quality_key", keysToPass);
                    editor.putInt("video_quality_level_current", qualityLevelCurrent);
                    editor.apply();

                    isQualityVisible = false;
                    //close/invisible recyclerview
                    recyclerQuality.setVisibility(INVISIBLE);

                    if (!isFirstClickOfQuality) {
                        //manage preferences for Video position in full screen mode
                        setPosOnPrefForQuality();
                    } else {
                        //this is first Time Quality's CLick...make flag to false
                        isFirstClickOfQuality = false;
                    }

                    setVideosOnQualityChanged();
                }
            });
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        /**
         * The type View holder.
         */
// stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * The List item.
             */
            public TextView list_item;

            /**
             * The Relative spinner parent.
             */
            public RelativeLayout relativeSpinnerParent;


            /**
             * Instantiates a new View holder.
             *
             * @param itemView the item view
             */
            public ViewHolder(View itemView) {
                super(itemView);
                list_item = (TextView) itemView.findViewById(R.id.list_item);

                relativeSpinnerParent = (RelativeLayout) itemView.findViewById(R.id.relativeSpinnerParent);

            }


        }

        // convenience method for getting data at click position
        /*public VideoBean getItem(int id) {
            return mData.get(id);
        }*/


    }


    /**
     * Cue video.
     *
     * @param murlToPlay       the murl to play
     * @param jsonObjectResult the json object result
     */
    public void cueVideo(String murlToPlay, String jsonObjectResult) {
        String videoType = "";
        if (!isInternetConnected(mContext)) {
            makeToast(getStringText(mContext, R.string.no_internet_connection), mContext);
            return;
        }

        //OnvideoView ready & Ready Listnere
        if (onVideoViewReady != null) {
            onVideoViewReady.OnVideoViewReady(true);
        }

        if (onReady != null) {
            onReady.OnPlayerReady();
        }


        //The video load & play automatically
        ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();
        if (jsonObjectResult != null) {
            try {
                if (!TextUtils.isEmpty(jsonObjectResult)) {


                    //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                    JSONObject jsonObject = new JSONObject(jsonObjectResult);


                   /* if (!jsonObject.isNull("title")) {
                        String title = jsonObject.getString("title");
                        //add title in tvpage model
                    }
                    if (!jsonObject.isNull("description")) {
                        String description = jsonObject.getString("description");
                        //add id in tvpage model

                    }

                    if (!jsonObject.isNull("date_created")) {
                        String date_created = jsonObject.getString("date_created");
                        //add date in tvpage model

                    }*/


                    if (!jsonObject.isNull("id")) {
                        videoIdForAnalytics = jsonObject.getString("id");

                    }


                    if (!jsonObject.isNull("entityIdParent")) {
                        channelIdForAnalytics = jsonObject.getString("entityIdParent");
                        //add date in tvpage model

                    }


                    if (!jsonObject.isNull("asset")) {
                        JSONObject jsonObjectAsset = jsonObject.getJSONObject("asset");

                        //get dash url & hls urls
                        if (!jsonObjectAsset.isNull("hlsUrl")) {
                            String hlsUrl = jsonObjectAsset.getString("hlsUrl");
                            if (hlsUrl != null && !TextUtils.isEmpty(hlsUrl)) {
                                dashOrHlsUrlFromIntent = hlsUrl;
                            }
                        } else if (!jsonObjectAsset.isNull("dashUrl")) {
                            String dashUrl = jsonObjectAsset.getString("dashUrl");
                            if (dashUrl != null && !TextUtils.isEmpty(dashUrl)) {
                                dashOrHlsUrlFromIntent = dashUrl;
                            }
                        } else {

                        }

                        if (!jsonObjectAsset.isNull("sources")) {

                            JSONArray jsonArray1 = jsonObjectAsset.getJSONArray("sources");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                String quality = "";
                                String file = "";


                                if (!jsonObject1.isNull("file") && !jsonObject1.isNull("quality")) {
                                    quality = jsonObject1.getString("quality");
                                    file = jsonObject1.getString("file");
                                    hashMap.put(quality, file);
                                    qualityList.add(hashMap);

                                }


                            }
                            //add source list

                        }

                        if (!jsonObjectAsset.isNull("type")) {
                            videoType = jsonObjectAsset.getString("type");
                            //add type list

                        }

                      /*



                       if (!jsonObjectAsset.isNull("thumbnailUrl")) {
                            String thumbnailUrl = jsonObjectAsset.getString("thumbnailUrl");
                            //add type list

                        }
                        if (!jsonObjectAsset.isNull("videoId")) {
                            String videoId = jsonObjectAsset.getString("videoId");
                            //add type list

                        }
                        if (!jsonObjectAsset.isNull("prettyDuration")) {
                            String prettyDuration = jsonObjectAsset.getString("prettyDuration");

                        }*/


                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //call analytics api of channel impression
        analyticsChannelImpressionApi();


        //The video load only..Does not play automatically
        isCueVideoFlag = true;
        this.murlToPlay = murlToPlay;


        //set state of cued video
        state = VIDEO_CUED;

        //OnvideoView state changed
        if (onStateChanged != null) {
            onStateChanged.OnStateChanged();
        }


        if (videoType.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
            youTubeExtractor();
        } else if (videoType.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
            vimeoExtractor();
        } else if (videoType.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
            //just set quality in spinner
            if (dashOrHlsUrlFromIntent != null &&
                    !TextUtils.isEmpty(dashOrHlsUrlFromIntent)
                    && dashOrHlsUrlFromIntent.toString().trim().length() > 0
                    ) {
                //dash or hls url available...give priority to it


                if (dashOrHlsUrlFromIntent.startsWith("http")) {
                    urlToPlayDesiredQuality = dashOrHlsUrlFromIntent;
                } else {
                    urlToPlayDesiredQuality = "http:" + dashOrHlsUrlFromIntent;
                }

                //add auto field
                HashMap<String, String> hashMapHls = new HashMap<String, String>();
                hashMapHls.put(HLS_DASH_TAG, urlToPlayDesiredQuality);
                qualityList.add(hashMapHls);

            }

            if (qualityList != null && qualityList.size() > 0) {
                listOfQuality.clear();
                this.listOfQuality = qualityList;
                setQualitySpinnerData();
            }


        } else {

        }


    }

    /**
     * Load video.
     *
     * @param murlToPlay       the murl to play
     * @param jsonObjectResult the json object result
     */
    public void loadVideo(String murlToPlay, String jsonObjectResult) {

        String videoType = "";

        if (!isInternetConnected(mContext)) {
            makeToast(getStringText(mContext, R.string.no_internet_connection), mContext);
            return;
        }

        //OnvideoView ready & Ready Listnere
        if (onVideoViewReady != null) {
            onVideoViewReady.OnVideoViewReady(true);
        }

        if (onReady != null) {
            onReady.OnPlayerReady();
        }


        //The video load & play automatically
        ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();
        if (jsonObjectResult != null) {
            try {
                if (!TextUtils.isEmpty(jsonObjectResult)) {


                    //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                    JSONObject jsonObject = new JSONObject(jsonObjectResult);


                   /* if (!jsonObject.isNull("title")) {
                        String title = jsonObject.getString("title");
                        //add title in tvpage model
                    }
                    if (!jsonObject.isNull("description")) {
                        String description = jsonObject.getString("description");
                        //add id in tvpage model

                    }

                    if (!jsonObject.isNull("date_created")) {
                        String date_created = jsonObject.getString("date_created");
                        //add date in tvpage model

                    }*/


                    if (!jsonObject.isNull("id")) {
                        videoIdForAnalytics = jsonObject.getString("id");

                    }


                    if (!jsonObject.isNull("entityIdParent")) {
                        channelIdForAnalytics = jsonObject.getString("entityIdParent");
                        //add date in tvpage model

                    }


                    if (!jsonObject.isNull("asset")) {
                        JSONObject jsonObjectAsset = jsonObject.getJSONObject("asset");

                        //get dash url & hls urls
                        if (!jsonObjectAsset.isNull("hlsUrl")) {
                            String hlsUrl = jsonObjectAsset.getString("hlsUrl");
                            if (hlsUrl != null && !TextUtils.isEmpty(hlsUrl)) {
                                dashOrHlsUrlFromIntent = hlsUrl;
                            }
                        } else if (!jsonObjectAsset.isNull("dashUrl")) {
                            String dashUrl = jsonObjectAsset.getString("dashUrl");
                            if (dashUrl != null && !TextUtils.isEmpty(dashUrl)) {
                                dashOrHlsUrlFromIntent = dashUrl;
                            }
                        } else {

                        }


                        if (!jsonObjectAsset.isNull("sources")) {

                            JSONArray jsonArray1 = jsonObjectAsset.getJSONArray("sources");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                String quality = "";
                                String file = "";


                                if (!jsonObject1.isNull("file") && !jsonObject1.isNull("quality")) {
                                    quality = jsonObject1.getString("quality");
                                    file = jsonObject1.getString("file");
                                    hashMap.put(quality, file);
                                    qualityList.add(hashMap);

                                }


                            }
                            //add source list

                        }

                        if (!jsonObjectAsset.isNull("type")) {
                            //System.out.println("" + jsonObjectAsset.getString("type"));
                            //add type list
                            videoType = jsonObjectAsset.getString("type");
                        }

                       /*

                        if (!jsonObjectAsset.isNull("thumbnailUrl")) {
                            System.out.println("" + jsonObjectAsset.getString("thumbnailUrl"));
                            //add type list

                        }
                        if (!jsonObjectAsset.isNull("videoId")) {
                            System.out.println("" + jsonObjectAsset.getString("videoId"));
                            //add type list

                        }
                        if (!jsonObjectAsset.isNull("prettyDuration")) {
                            System.out.println("" + jsonObjectAsset.getString("prettyDuration"));

                        }*/


                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //call analytics api of channel impression
        analyticsChannelImpressionApi();


        isLoadVideoFlag = true;
        this.murlToPlay = murlToPlay;
        if (videoType.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
            youTubeExtractor();
        } else if (videoType.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
            vimeoExtractor();
        } else if (videoType.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
            //just set quality in spinner
            //just set quality in spinner'
            if (dashOrHlsUrlFromIntent != null &&
                    !TextUtils.isEmpty(dashOrHlsUrlFromIntent)
                    && dashOrHlsUrlFromIntent.toString().trim().length() > 0
                    ) {
                //dash or hls url available...give priority to it


                if (dashOrHlsUrlFromIntent.startsWith("http")) {
                    urlToPlayDesiredQuality = dashOrHlsUrlFromIntent;
                } else {
                    urlToPlayDesiredQuality = "http:" + dashOrHlsUrlFromIntent;
                }

                //add auto field
                HashMap<String, String> hashMapHls = new HashMap<String, String>();
                hashMapHls.put(HLS_DASH_TAG, urlToPlayDesiredQuality);
                qualityList.add(hashMapHls);

            }


            if (qualityList != null && qualityList.size() > 0) {
                this.listOfQuality.clear();
                this.listOfQuality = qualityList;
                setQualitySpinnerData();

            }


        } else {

        }

    }


    /**
     * You tube extractor.
     */
    void youTubeExtractor() {
        // String youtubeLink = "https://www.youtube.com/watch?v=B27zvZRfeSo";
        //String youtubeLink = "https://www.youtube.com/watch?v=1YBl3Zbt80A";
        //String youtubeLink = " https://www.youtube.com/watch?v=g4t2R9RneFM";

        if (!isInternetConnected(mContext)) {
            makeToast(getStringText(mContext, R.string.no_internet_connection), mContext);
            return;
        }

        if (murlToPlay == null || TextUtils.isEmpty(murlToPlay)) {
            return;
        }

        //System.out.println("Youtube Urls#######: " + murlToPlay);

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(mContext) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;

                    listOfQuality.clear();

                    HashMap<Integer, String> hashMapItag = new HashMap<>();
                    for (int i = 0; i < ytFiles.size(); i++) {
                        int key = ytFiles.keyAt(i);
                        // get the object by the key.
                        hashMapItag.put(key, ytFiles.get(key).getUrl());

                    }

                    if (hashMapItag != null && hashMapItag.size() > 0) {
                        if (hashMapItag.containsKey(17)) {
                            HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                            hashMapToAdd.put("144p", hashMapItag.get(17));
                            if (hashMapToAdd.size() > 0) {
                                listOfQuality.add(hashMapToAdd);
                            }
                        }

                        if (hashMapItag.containsKey(36)) {
                            HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                            hashMapToAdd.put("240p", hashMapItag.get(36));
                            if (hashMapToAdd.size() > 0) {
                                listOfQuality.add(hashMapToAdd);
                            }
                        }

                        if (hashMapItag.containsKey(18)) {
                            HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                            hashMapToAdd.put("360p", hashMapItag.get(18));
                            if (hashMapToAdd.size() > 0) {
                                listOfQuality.add(hashMapToAdd);
                            }
                        }

                        if (hashMapItag.containsKey(22)) {
                            HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                            hashMapToAdd.put("720p", hashMapItag.get(22));
                            if (hashMapToAdd.size() > 0) {
                                listOfQuality.add(hashMapToAdd);
                            }
                        }

                        HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                        if (hashMapItag.containsKey(96)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(96));
                        } else if (hashMapItag.containsKey(95)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(95));
                        } else if (hashMapItag.containsKey(94)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(94));
                        } else if (hashMapItag.containsKey(93)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(93));
                        } else if (hashMapItag.containsKey(92)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(92));
                        } else if (hashMapItag.containsKey(91)) {
                            hashMapToAdd.put(HLS_DASH_TAG, hashMapItag.get(91));
                        }
                        if (hashMapToAdd.size() > 0) {
                            listOfQuality.add(hashMapToAdd);
                        }

                        /*if (!hashMapItag.containsKey(91)
                                && !hashMapItag.containsKey(92)
                                && !hashMapItag.containsKey(93)
                                && !hashMapItag.containsKey(94)
                                && !hashMapItag.containsKey(95)
                                && !hashMapItag.containsKey(96)
                                ) {
                            HashMap<String, String> hashMapToAddDash = new HashMap<String, String>();
                            if (hashMapItag.containsKey(133)) {
                                hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(133));
                            } else if (hashMapItag.containsKey(134)) {
                                hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(134));
                            } else if (hashMapItag.containsKey(135)) {
                                hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(135));
                            } else if (hashMapItag.containsKey(136)) {
                                hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(136));
                            } else if (hashMapItag.containsKey(137)) {
                                hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(137));
                                //System.out.println("Dash Video ############: " + hashMapItag.get(137));
                            } *//*else if (hashMapItag.containsKey(140)) {
                                //hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(140));
                                //System.out.println("Dash Audio ############: " + hashMapItag.get(140));
                            } else if (hashMapItag.containsKey(141)) {
                                //hashMapToAddDash.put(HLS_DASH_TAG, hashMapItag.get(141));
                            }*//*
                            if (hashMapToAddDash.size() > 0) {
                                listOfQuality.add(hashMapToAddDash);
                            }
                        }*/

                    }


                 /*   case 17:
                        //144p
                        hashMapToAdd.put("144p", ytFiles.get(key).getUrl());
                        break;
                    case 36:
                        //240p
                        hashMapToAdd.put("240p", ytFiles.get(key).getUrl());
                        break;
                            *//*case 43:
                                //360p

                                hashMapToAdd.put("360p", ytFiles.get(key).getUrl());

                                break;*//*
                    case 18:
                        //360p

                        hashMapToAdd.put("360p", ytFiles.get(key).getUrl());

                        break;
                    case 22:
                        //720p
                        hashMapToAdd.put("720p", ytFiles.get(key).getUrl());
                        break;
                    default:
                        break;*/


                    //urlToPlayDesiredQuality = url240q;
                    setQualitySpinnerData();
                    //setVideos();
                    //String downloadUrl = ytFiles.get(itag).getUrl();
                } else {
                    Log.d("NULll From Youitube", "NULll From Youitube");

                }
            }

            @Override
            protected void onPostExecute(SparseArray<YtFile> ytFiles) {
                //progressDialog.dismiss();
                super.onPostExecute(ytFiles);
                Log.d("On Post Youitube", "OnPost Youitube");


            }

            @Override
            protected void onCancelled(SparseArray<YtFile> ytFileSparseArray) {
                //progressDialog.dismiss();
                super.onCancelled(ytFileSparseArray);
                Log.d("On Cancelled Youitube", "OnCancelled Youitube");

            }


        };

     /*   // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(mContext);
        // set a title for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();
*/

        ytEx.execute(murlToPlay);

    }

    /**
     * Vimeo extractor.
     */
    void vimeoExtractor() {
        // String youtubeLink = "https://www.youtube.com/watch?v=B27zvZRfeSo";
        //String youtubeLink = "https://www.youtube.com/watch?v=1YBl3Zbt80A";
        //String youtubeLink = " https://www.youtube.com/watch?v=g4t2R9RneFM";

        if (!isInternetConnected(mContext)) {
            makeToast(getStringText(mContext, R.string.no_internet_connection), mContext);
            return;
        }

        if (murlToPlay == null || TextUtils.isEmpty(murlToPlay)) {
            return;
        }

      /*  // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(mContext);
        // set a title for the progress bar
        progressDialog.setMessage("Loading...");

        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();
*/

        VimeoExtractor.getInstance().fetchVideoWithURL(murlToPlay, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                // progressDialog.dismiss();
                if (video != null) {

                    listOfQuality.clear();

                    Set set = video.getStreams().entrySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry mentry = (Map.Entry) iterator.next();
                        //TvPageUtils.sout("key is: " + mentry.getKey() + " & Value is: ");
                        //TvPageUtils.sout(mentry.getValue());
                        HashMap<String, String> hashMapToAdd = new HashMap<String, String>();
                        hashMapToAdd.put("" + mentry.getKey(), "" + mentry.getValue());
                        if (hashMapToAdd.size() > 0) {
                            listOfQuality.add(hashMapToAdd);
                        }
                    }


                  /*  if (video.getStreams().get("144p") != null) {
                        url144q = video.getStreams().get("144p");
                        TvPageUtils.sout("URLS.Q144..." + url144q + " ");
                    }

                    if (video.getStreams().get("240p") != null) {
                        url240q = video.getStreams().get("240p");
                        TvPageUtils.sout("URLS.Q240..." + url240q + " ");
                    }

                    if (video.getStreams().get("360p") != null) {
                        url360q = video.getStreams().get("360p");
                        TvPageUtils.sout("URLS.Q360..." + url360q + " ");
                    }

                    if (video.getStreams().get("480p") != null) {
                        url480q = video.getStreams().get("480p");
                        TvPageUtils.sout("URLS.Q480..." + url480q + " ");
                    }
                    if (video.getStreams().get("720p") != null) {
                        url720q = video.getStreams().get("720p");
                        TvPageUtils.sout("URLS.Q720..." + url720q + " ");
                    }
                    if (video.getStreams().get("1080p") != null) {
                        url1080q = video.getStreams().get("1080p");
                        TvPageUtils.sout("URLS.Q1080..." + url1080q + " ");
                    }*/


                    //urlToPlayDesiredQuality = url360q;
                    setQualitySpinnerData();
                    //setVideos();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                // progressDialog.dismiss();
            }
        });


    }


    private void setQualitySpinnerData() {
        if (listOfQuality.size() > 0) {
           /* for (HashMap<String, String> entry : listOfQuality) {
                for (String key : entry.keySet()) {
                    String url = entry.get(key);
                    String quality = key;
                    TvPageUtils.sout(" QualityKey: " + quality + " UrlValue: " + url);
                }
            }*/


            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Sorting the Quality
                    Collections.sort(listOfQuality, new Comparator<HashMap<String, String>>() {
                        @Override
                        public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                    /*Log.d("Dattaaa",""+o1.keySet().iterator().next().substring(0, o1.keySet().iterator().next().length() - 1));
                    Log.d("Dattaa2",""+o2.keySet().iterator().next().substring(0, o2.keySet().iterator().next().length() - 1));
                    */

                            int val1 = Integer.parseInt(o1.keySet().iterator().next().substring(0, o1.keySet().iterator().next().length() - 1));
                            int val2 = Integer.parseInt(o2.keySet().iterator().next().substring(0, o2.keySet().iterator().next().length() - 1));
                            return val2 - val1;
                        }

                    });

                    SpinnerAdapterQuality adapterRegion = new SpinnerAdapterQuality
                            (mContext, R.layout.adapter_spinner_quality, listOfQuality);

                    setDataToQualityList();

                  /*  spinnerQuality.setAdapter(adapterRegion);
                    spinnerQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String keysToPass = "";

                            //Save Current Quality Levels
                            qualityLevelCurrent = position;

                            HashMap<String, String> item = listOfQuality.get(position);
                            for (String key : item.keySet()) {
                                keysToPass = key;
                                if (listOfQuality.get(position).get(key).startsWith("http")) {
                                    urlToPlayDesiredQuality = listOfQuality.get(position).get(key);
                                } else {
                                    urlToPlayDesiredQuality = "http:" + listOfQuality.get(position).get(key);
                                }


                            }


                            setPosOnPrefForQuality();
                            setVideosOnQualityChanged();

                            if (++checkSpinner > 1) {
                                //it means its valid to pass Quality Changed Interface ...
                                // Call listener of Quality Changed
                                //Log.d("tv quality", " VALID ");
                                //set call back for Quality Changed
                                if (onMediaPlayBackQualityChanged != null) {
                                    onMediaPlayBackQualityChanged.OnMediaPlayBackQualityChanged(keysToPass);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });*/
                }
            });


        }


    }

    /**
     * On save instance.
     */
    public void onSaveInstance() {
        SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        long pos = videoView.getCurrentPosition();
        editor.putInt("video_position", (int) pos);
        editor.putString("video_url", urlToPlayDesiredQuality);
        editor.putBoolean("video_completed", isVideoCompleted);
        editor.putBoolean("video_mute", isMuted);
        editor.putString("video_state", state);
        editor.putInt("video_volume", volume_video);
        editor.putInt("video_quality_level", getQuality());
        editor.commit();


        videoView.pause();
    }

    /**
     * On restore instance.
     */
    public void onRestoreInstance() {


        SharedPreferences prefs = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        position = prefs.getInt("video_position", 0);
        urlToPlayDesiredQuality = prefs.getString("video_url", "");
        isVideoCompleted = prefs.getBoolean("video_completed", false);
        isMuted = prefs.getBoolean("video_mute", false);
        state = prefs.getString("video_state", "");
        volume_video = prefs.getInt("video_volume", 0);
        setVideosOnRestore();

    }


    /**
     * Play.
     */
    public void play() {

        if (videoView != null) {

            //call first 3 second handler here
            if (!isFirstThreeSecondApiCalled) {
                isFirstThreeSecondApiCalled = true;
                mHandlerThreeSecondOnce.postDelayed(runnableThreeSecondOnce, 3000);
            }

            //register callback of every 3 second repeat handler
            mHandlerThreeSecondRepeat.removeCallbacksAndMessages(null);
            mHandlerThreeSecondRepeat.postDelayed(runnableThreeSecondRepeat, 3000);


            //manage play/pause button UI
            changePlayPauseImages(true);
            isVideoPlayedUi = true;

            if (isStopped) {
                isStopped = false;
                showProgressBarOfMediaController();
                //videoView.setVideoPath(urlToPlayDesiredQuality);
                videoView.setVideoURI(Uri.parse(urlToPlayDesiredQuality));
            }


            videoView.requestFocus();
            videoView.start();
            //video playing listener
            if (onVideoPlaying != null) {
                onVideoPlaying.OnVideoPlaying(true);
            }

            state = VIDEO_PLAYED;

            //OnvideoView state changed
            if (onStateChanged != null) {
                onStateChanged.OnStateChanged();
            }

        }
    }

    /**
     * Pause.
     */
    public void pause() {
        if (videoView != null && videoView.isPlaying()) {

            //remove call backs on pause..of repat handler
            mHandlerThreeSecondRepeat.removeCallbacks(runnableThreeSecondRepeat);


            //manage play/pause button UI
            changePlayPauseImages(false);
            isVideoPlayedUi = false;

            videoView.pause();

            //register listener for video paused
            if (onVideoPaused != null) {
                onVideoPaused.OnVideoPaused(true);
            }

            state = VIDEO_PAUSED;

            //OnvideoView state changed
            if (onStateChanged != null) {
                onStateChanged.OnStateChanged();
            }
        }
    }

    /**
     * Stop.
     */
    public void stop() {
        if (videoView != null) {
            videoView.stopPlayback();
            isStopped = true;
            clearPrefOfVideoAndPostion();
        }
    }

    /**
     * Volume.
     *
     * @param volume the volume
     */
    public void volume(int volume) {
        volume_video = volumeConvertor(volume);
        seekBarVolume.setProgress(volume_video);
        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }


    private int volumeConvertor(int volumeToConvert) {
        int convertedVolume = 0;
        try {
            convertedVolume = (volumeToConvert * 15) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedVolume;
    }


    /**
     * Mute.
     */
    public void mute() {
        //its not mute..do mute
        setVolumes(0, true);
        changeVolumeImages(true);
    }

    /**
     * Unmute.
     */
    public void unmute() {
        //its muted..do unmute
        setVolumes(100, true);
        changeVolumeImages(false);

    }

    /**
     * Seek.
     *
     * @param seconds the seconds
     */
    public void seek(int seconds) {
        if (videoView != null) {

            if (seconds > 0) {
                int milliseconds = 0;
                milliseconds = seconds * 1000;

                if (milliseconds <= videoView.getDuration()) {
                    //TvPageUtils.sout("milii to set: "+milliseconds);
                    videoView.seekTo(milliseconds);
                    // update timer progress again
                    //updateProgressBar();
                } else {
                    //gives error as second is greater than total video durations

                }
            }
        }
    }

    /**
     * Sets quality.
     *
     * @param level the level
     */
    public void setQuality(int level) {
        //check whether passing index is valid for our array size (Level(Passing Index) < Array Size)

        if (listOfQuality != null && level < listOfQuality.size()) {
            if (recyclerQuality != null) {
                if (recyclerQuality.findViewHolderForAdapterPosition(level) != null) {
                    recyclerQuality.findViewHolderForAdapterPosition(level).itemView.performClick();
                }
            }

        }
    }

    /**
     * Sets quality for back from fullscreen.
     *
     * @param level the level
     */
    void setQualityForBackFromFullscreen(int level) {
        //check whether passing index is valid for our array size (Level(Passing Index) < Array Size)

        if (listOfQuality != null && level < listOfQuality.size()) {
            if (recyclerQuality != null) {
                if (recyclerQuality.findViewHolderForAdapterPosition(level) != null) {
                    recyclerQuality.findViewHolderForAdapterPosition(level).itemView.performClick();
                }
            }
        }

    }

    /**
     * Gets quality levels.
     *
     * @return the quality levels
     */
    public ArrayList<String> getQualityLevels() {
        ArrayList<String> qualityList = new ArrayList<>();
        try {
            if (listOfQuality.size() > 0) {
                for (HashMap<String, String> entry : listOfQuality) {
                    for (String key : entry.keySet()) {
                        qualityList.add(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qualityList;

    }

    /**
     * Gets quality.
     *
     * @return the quality
     */
    public int getQuality() {
        return qualityLevelCurrent;
    }

    /**
     * Enable controls.
     */
    public void enableControls() {
        enableDisableControls(true);
    }

    /**
     * Disable controls.
     */
    public void disableControls() {
        enableDisableControls(false);
    }

    /**
     * Is muted boolean.
     *
     * @return the boolean
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Sets muted.
     *
     * @param muted the muted
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }


    /**
     * Gets volume.
     *
     * @return the volume
     */
    public int getVolume() {
        volume = getConvertedVolumeToReturn(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return volume;
    }

    private int getConvertedVolumeToReturn(int volumeFromAudio) {
        int convertedVolumeToReturn = 0;
        try {
            convertedVolumeToReturn = (volumeFromAudio * 100) / 15;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedVolumeToReturn;
    }

    /**
     * Sets volume.
     *
     * @param volume the volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }


    /**
     * Gets height of view.
     *
     * @return the height of view
     */
    public int getHeightOfView() {
        try {
            heightOfView = relativesParent.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heightOfView;
    }

    /**
     * Sets height of view.
     *
     * @param heightOfView the height of view
     */
    public void setHeightOfView(int heightOfView) {
        this.heightOfView = heightOfView;
    }

    /**
     * Gets width of view.
     *
     * @return the width of view
     */
    public int getWidthOfView() {

        try {
            widthOfView = relativesParent.getMeasuredWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return widthOfView;
    }

    /**
     * Sets width of view.
     *
     * @param widthOfView the width of view
     */
    public void setWidthOfView(int widthOfView) {
        this.widthOfView = widthOfView;
    }

    /**
     * Gets current time.
     *
     * @return the current time
     */
    public long getCurrentTime() {

        if (videoView != null) {
            currentTime = videoView.getCurrentPosition() / 1000;
        }
        return currentTime;
    }

    /**
     * Sets current time.
     *
     * @param currentTime the current time
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public long getDuration() {
        if (videoView != null) {
            duration = videoView.getDuration() / 1000;
        }
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Hide controls.
     */
    public void hideControls() {
        if (controllerAnchor != null) {
            controllerAnchor.animate().translationY(controllerAnchor.getHeight()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    controllerAnchor.setVisibility(GONE);
                    if (fullscreenExitListener != null)
                        fullscreenExitListener.onHideControl();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }
    }

    /**
     * Show controls.
     */
    public void showControls() {
        if (controllerAnchor != null) {

            controllerAnchor.setVisibility(VISIBLE);
            controllerAnchor.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    /**
     * Resize.
     *
     * @param width  the width
     * @param height the height
     */
    public void resize(int width, int height) {
        if (relativesParent != null && width > 0 && height > 0) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativesParent.setLayoutParams(layoutParams);
        }
    }


    /**
     * Sets poster.
     *
     * @param imageurl the imageurl
     */
    public void setPoster(String imageurl) {
        if (imgPoster != null && imageurl != null && !TextUtils.isEmpty(imageurl)) {
            imgPoster.setVisibility(VISIBLE);
            relVideoViewPGesture.setVisibility(INVISIBLE);
            TvPageUtils.loadImageUsingGlide(mContext, imageurl, imgPoster);
        }
    }

    private void enableDisableControls(boolean isEnabled) {
        if (isEnabled) {
            //enable all controls
            play_video.setClickable(true);
            play_video.setEnabled(true);
          /*  spause_video.setClickable(true);
            pause_video.setEnabled(true);*/
            /*stop_video.setClickable(true);
            stop_video.setEnabled(true);*/
            imgVolume.setClickable(true);
            imgVolume.setEnabled(true);
            seekBarVolume.setClickable(true);
            seekBarVolume.setEnabled(true);
            seekbarVideo.setClickable(true);
            seekbarVideo.setEnabled(true);

            imgQuality.setClickable(true);
            imgQuality.setEnabled(true);

            imgFullScreen.setClickable(true);
            imgFullScreen.setEnabled(true);

        } else {
            //disble all controls

            play_video.setClickable(false);
            play_video.setEnabled(false);
           /* spause_video.setClickable(false);
            pause_video.setEnabled(false);*/
           /* stop_video.setClickable(false);
            stop_video.setEnabled(false);*/
            imgVolume.setClickable(false);
            imgVolume.setEnabled(false);
            seekBarVolume.setClickable(false);
            seekBarVolume.setEnabled(false);
            seekbarVideo.setClickable(false);
            seekbarVideo.setEnabled(false);

            imgQuality.setClickable(false);
            imgQuality.setEnabled(false);

            imgFullScreen.setClickable(false);
            imgFullScreen.setEnabled(false);


        }
    }


    /**
     * Sets media providers.
     *
     * @param mediaProvidersCommaSeprated the media providers comma seprated
     */
    public void setMediaProviders(String mediaProvidersCommaSeprated) {
        if (mediaProvidersCommaSeprated != null
                && !TextUtils.isEmpty(mediaProvidersCommaSeprated)
                && mediaProvidersCommaSeprated.toString().trim().length() > 0
                ) {
            //its not null or not contains white spaces
        }
    }

    /**
     * Sets api base url.
     *
     * @param apiBaseUrl the api base url
     */
    public void setApiBaseUrl(String apiBaseUrl) {
        if (apiBaseUrl != null
                && !TextUtils.isEmpty(apiBaseUrl)
                && apiBaseUrl.toString().trim().length() > 0
                ) {
            //its not null or not contains white spaces
            baseUrlApi = apiBaseUrl;
        } else {
            //set Default Base Urls
            baseUrlApi = "https://app.tvpage.com/api";
        }
    }

    /**
     * Sets controls.
     *
     * @param jsonObject the json object
     */
    public void setControls(JSONObject jsonObject) {
        try {
            String activeControlValue = "";
            String progressColorVal = "q";
            String analyticsApiVal = "";

            JSONObject jsonObject1 = jsonObject;
            if (jsonObject1 != null) {

                if (!jsonObject1.isNull("active")) {
                    activeControlValue = jsonObject1.getString("active");
                }
                if (!jsonObject1.isNull("seekbar")) {
                    JSONObject jsonObjectProgressColor = jsonObject1.getJSONObject("seekbar");
                    if (!jsonObjectProgressColor.isNull("progressColor")) {
                        progressColorVal = jsonObjectProgressColor.getString("progressColor");
                    }
                }

                if (!jsonObject1.isNull("analytics")) {
                    JSONObject jsonObjectAnalytics = jsonObject1.getJSONObject("analytics");
                    if (!jsonObjectAnalytics.isNull("tvpa")) {
                        analyticsApiVal = jsonObjectAnalytics.getString("tvpa");
                    }
                }

                //manage value for analytics api
                switch (analyticsApiVal) {
                    case "true":
                        isCallAnalyticsApi = true;
                        break;
                    case "false":
                        isCallAnalyticsApi = false;
                        break;
                    default:
                        isCallAnalyticsApi = true;
                        break;
                }

                //set seekbar color
                if (progressColorVal != null &&
                        !TextUtils.isEmpty(progressColorVal)
                        && progressColorVal.toString().trim().length() > 0
                        && progressColorVal.startsWith("#")
                        ) {
                    if (seekBarVolume != null) {
                        seekBarVolume.setProgressColor(Color.parseColor(progressColorVal));
                        seekBarVolume.setThumbColor(Color.parseColor(progressColorVal));
                    }

                    if (seekbarVideo != null) {
                        seekbarVideo.setProgressColor(Color.parseColor(progressColorVal));
                        seekbarVideo.setThumbColor(Color.parseColor(progressColorVal));
                    }
                }


                //enable/disable cmedia controls
                switch (activeControlValue) {
                    case "true":
                        enableControls();
                        break;
                    case "false":
                        disableControls();
                        break;
                    default:
                        enableControls();
                        break;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //click and Other Listeners

    @Override
    public void onClick(View v) {


        String tag = null;
        try {
            tag = (String) v.getTag();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tag == null) {
            return;
        }
        switch (tag) {
            case "play_video":
                if (urlToPlayDesiredQuality != null
                        && !TextUtils.isEmpty(urlToPlayDesiredQuality)
                        && urlToPlayDesiredQuality.toString().trim().length() > 0) {
                    if (isVideoPlayedUi) {
                        //the video playing...so make pause and change UI
                        //flag will be changed in below function
                        pause();
                    } else {
                        //the video paused ...so make play & chnage Ui
                        //flag will be changed in below function
                        play();
                    }
                }

                break;
          /*  case "pause_video":
                pause();
                break;*/
           /* case "stop_video":
                stop();
                break;*/
            case "imgVolume":
                if (isMuted) {
                    //its muted..do unmute
                    unmute();
                } else {
                    //its not mute..do mute
                    mute();
                }

                break;
            case "imgFullScreen":
                if (!isFullScreenMode) {
                    setFullScreenMode();
                } else {
                    if (fullscreenExitListener != null)
                        fullscreenExitListener.onFullscreenExit();
                }
                break;
            case "imgQuality":

                if (isQualityVisible) {
                    isQualityVisible = false;
                    //close/invisible recyclerview
                    recyclerQuality.setVisibility(INVISIBLE);

                } else {
                      /*Vishal changes 31-08-2017*/
                   /* if (videoView != null && seekbarVideo.getProgress() < 6) {
                        return;
                    }*/
                    isQualityVisible = true;
                    //Visible recyclerview
                    recyclerQuality.setVisibility(VISIBLE);
                }

                break;
            case "relativesParent":
                if (controlsVisible == 0) {
                    controlsVisible = 1;
                    hideControls();
                } else if (controlsVisible == 1) {
                    controlsVisible = 0;
                    showControls();
                }

                if (recyclerQuality != null && recyclerQuality.getVisibility() == VISIBLE) {
                    isQualityVisible = false;
                    recyclerQuality.setVisibility(INVISIBLE);
                }

                break;
            case "controllerAnchor":

                break;
            default:
                break;
        }
    }


    @Override
    public void onCompletion() {

        //stop video view
        stop();


        //it means The media source has completed

        //it means video paused...and change ui to pause(pass false in ui)
        changePlayPauseImages(false);
        isVideoPlayedUi = false;

        //clear pref of position and urls
        clearPrefOfVideoAndPostion();

        //save flag for videos
        isVideoCompleted = true;

        state = VIDEO_ENDED;

        //OnvideoView state changed
        if (onStateChanged != null) {
            onStateChanged.OnStateChanged();
        }

        //pass data into listener
        if (onMediaComplete != null) {
            onMediaComplete.OnMediaComplete(true);
        }
        if (onVideoEnded != null) {
            onVideoEnded.OnVideoEnded(true);
        }

        //just stop video..
        //videoView.stopPlayback();
        mHandlerThreeSecondRepeat.removeCallbacks(runnableThreeSecondRepeat);

        Log.d("Media Completed", "Media Completed");
    }

    private void clearPrefOfVideoAndPostion() {
        //clear pref
        SharedPreferences settings = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        //settings.edit().clear().commit();

        SharedPreferences.Editor editor = settings.edit();
        editor.remove("video_position");
        editor.apply();

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MEDIA_INFO_UNKNOWN:
                break;
            case MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MEDIA_INFO_VIDEO_RENDERING_START:
                hideProgressBarOfMediaController();
                Log.d("Rendering Starts", "Rendering Starts");
                break;
            case MEDIA_INFO_BUFFERING_START:
                showProgressBarOfMediaController();
                if (onVideoBuffering != null) {
                    onVideoBuffering.OnVideoBuffering(true);
                }
                state = VIDEO_BUFFERING;

                //OnvideoView state changed
                if (onStateChanged != null) {
                    onStateChanged.OnStateChanged();
                }

                break;
            case MEDIA_INFO_BUFFERING_END:
                hideProgressBarOfMediaController();
                if (onVideoBuffering != null) {
                    onVideoBuffering.OnVideoBuffering(false);
                }
                state = VIDEO_PLAYED;

                //OnvideoView state changed
                if (onStateChanged != null) {
                    onStateChanged.OnStateChanged();
                }

                break;
            case 703:
                //MEDIA_INFO_NETWORK_BANDWIDTH
                break;
            case MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case MEDIA_INFO_NOT_SEEKABLE:
                break;
            case MEDIA_INFO_METADATA_UPDATE:
                break;
            case MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                break;
            case MEDIA_INFO_SUBTITLE_TIMED_OUT:
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onPrepared() {


// close the progress bar and play the video
       /* if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }*/
        //if we have a position on savedInstanceState, the video playback should start from here
        /*TvPageUtils.sout(" ON PREPAREDD @@@ " + stopPosition);
        if (stopPosition != 0) {
            videoViews.seekTo(stopPosition);
        } else {
        */
        SharedPreferences prefs = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        position = prefs.getInt("video_position", 0);
        TvPageUtils.sout("Pos: " + position);
        Log.d("Media Prepared ", "Media Preapred ");

        videoView.seekTo(position);

        //hide poster if available and visible videoview
        if (imgPoster != null) {
            imgPoster.setVisibility(INVISIBLE);
        }

        //Visible Videoview
        if (relVideoViewPGesture != null) {
            relVideoViewPGesture.setVisibility(VISIBLE);
        }

        if (onMediaReady != null) {
            onMediaReady.OnMediaReady(true);
        }

        hideProgressBarOfMediaController();


        if (isLoadVideoFlag) {
            //reset load videos flag
            isLoadVideoFlag = false;

            //register load video listener call back
            if (onVideoLoad != null) {
                onVideoLoad.OnVideoLoad(true);
            }


            //play videos
            play();
        }

        if (isCueVideoFlag) {
            //reset cue video flag
            isCueVideoFlag = false;


            //hide progress media controller as in cue video does not start automatically
            hideProgressBarOfMediaController();

            //register cue video listener callback
            if (onVideoCued != null) {
                onVideoCued.OnVideoCued(true);
            }

        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String tag = null;
        try {
            tag = (String) seekBar.getTag();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tag == null) {
            return;
        }
        switch (tag) {
            case "seekBarVolume":
                setVolumes(progress, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        String tag = null;
        try {
            tag = (String) seekBar.getTag();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tag == null) {
            return;
        }
        switch (tag) {
            case "seekbarVideo":
                mHandler.removeCallbacks(mUpdateTimeTask);
                break;
            default:
                break;
        }

    }

    private void hideProgressBarOfMediaController() {
        try {
            if (progressMediaController != null && progressMediaController.getVisibility() == VISIBLE) {
                progressMediaController.setVisibility(GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgressBarOfMediaController() {
        try {
            if (progressMediaController != null) {
                progressMediaController.setVisibility(VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        String tag = null;
        try {
            tag = (String) seekBar.getTag();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tag == null) {
            return;
        }
        switch (tag) {
            case "seekbarVideo":
                mHandler.removeCallbacks(mUpdateTimeTask);
                long totalDuration = videoView.getDuration();


                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                videoView.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
                break;
            default:
                break;
        }

    }

    private void setVolumes(int position, boolean isSeekTo) {
        if (audioManager != null) {
            if (position == 0) {
                isMuted = true;
                changeVolumeImages(true);
            } else {
                isMuted = false;
                changeVolumeImages(false);
            }

            if (isSeekTo) {
                seekBarVolume.setProgress(position);
            }

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, position, 0);
            //set updated volume
            volume_video = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
    }

    private void changeVolumeImages(boolean isMute) {
        if (isMute) {
            //its mute
            imgVolume.setImageResource(R.drawable.ic_volume_off);
        } else {
            //its up volume
            imgVolume.setImageResource(R.drawable.ic_volume_up);
        }
    }

    private void changePlayPauseImages(boolean isPlays) {
        if (isPlays) {
            //its mute
            play_video.setImageResource(R.drawable.ic_media_pause);
        } else {
            //its up volume
            play_video.setImageResource(R.drawable.ic_media_play);
        }
    }


    public void setFullscreenEnable(boolean isFullScreenEnable) {
        this.isFullScreenEnable = isFullScreenEnable;
        if (imgFullScreen == null) return;
        if (this.isFullScreenEnable) {
            imgFullScreen.setVisibility(View.VISIBLE);
        } else {
            imgFullScreen.setVisibility(View.GONE);
        }
    }

    public boolean isFullScreenEnabled() {
        return this.isFullScreenEnable;
    }

    private void setVideos() {
        try {


            //set the media controller in the VideoView
            // videoView.setMediaController(null);
            //set the uri of the video to be played
            //videoView.setVideoPath(urlToPlayDesiredQuality);
            videoView.setVideoURI(Uri.parse(urlToPlayDesiredQuality));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        // Updating progress bar
        updateProgressBar();


        if (isLoadVideoFlag) {
            //play videos
            play();
        }



        /*videoViews.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoViews.setOnPreparedListener(this);*/


    }

    /**
     * Sets videos on restore.
     */
    void setVideosOnRestore() {
        try {
            //set the media controller in the VideoView
            // videoView.setMediaController(null);
            //set the uri of the video to be played
            //videoView.setVideoPath(urlToPlayDesiredQuality);
            videoView.setVideoURI(Uri.parse(urlToPlayDesiredQuality));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        // Updating progress bar
        updateProgressBar();

        videoView.seekTo(position);

        try {

            SharedPreferences prefs = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
            String keysToPass = prefs.getString("video_quality_key", "");
            qualityLevelCurrent = prefs.getInt("video_quality_level_current", 0);
            if (keysToPass.contains("1080") || keysToPass.contains("720")) {
                //show hd icon upper part of setting
                if (tvHd != null) {
                    tvHd.setVisibility(VISIBLE);
                }
            } else {
                //Hide hd icon upper part of setting
                if (tvHd != null) {
                    tvHd.setVisibility(INVISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //play videos
        play();

        if (listOfQuality.size() > 0) {
            //just notify adapter
            //pass last param bollean as true....
            qualityAdapter = new QualityAdapter(mContext, listOfQuality, qualityLevelCurrent, true);
            if (qualityAdapter != null && recyclerQuality != null) {
                recyclerQuality.setAdapter(qualityAdapter);
                qualityAdapter.notifyDataSetChanged();
            }
        }


        /*videoViews.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoViews.setOnPreparedListener(this);*/


    }


    /**
     * Sets pos on pref for quality.
     */
    void setPosOnPrefForQuality() {
        try {
            SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            long pos = videoView.getCurrentPosition();
            editor.putInt("video_position", (int) pos);
            editor.putString("video_url", urlToPlayDesiredQuality);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets videos on quality changed.
     */
    void setVideosOnQualityChanged() {
        try {
            //set the media controller in the VideoView
            // videoView.setMediaController(null);
            //set the uri of the video to be played
            //videoView.setVideoPath(urlToPlayDesiredQuality);
            videoView.setVideoURI(Uri.parse(urlToPlayDesiredQuality));

            //update video url quality
            SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString("video_url", urlToPlayDesiredQuality);
            editor.commit();


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        // Updating progress bar
        updateProgressBar();

        SharedPreferences prefs = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        position = prefs.getInt("video_position", 0);

        videoView.seekTo(position);

        if (isLoadVideoFlag) {
            //play videos
            play();
        }

        /*videoViews.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoViews.setOnPreparedListener(this);*/
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if (videoView == null) {
                return;
            }
            long totalDuration = videoView.getDuration();
            long currentDuration = videoView.getCurrentPosition();

            // Displaying Total Duration time
            //play_total_time.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            play_time.setText("" + utils.milliSecondsToTimer(currentDuration));

            //call listener for seek time
            if (onSeek != null) {
                onSeek.OnSeek(utils.milliSecondsToTimer(currentDuration));
            }

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekbarVideo.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

  /*  @Override
    public boolean onError() {
      *//*  switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:

                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:

                break;
            default:
                break;
        }


        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //File or network related operation errors.
                if (onMediaError != null) {
                    onMediaError.OnMediaError("Media IO Error");
                    //call back of onerror occurs
                    if (onError != null) {
                        onError.OnError();
                    }
                }
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //Bitstream is not conforming to the related coding standard or file spec.
                if (onMediaError != null) {
                    onMediaError.OnMediaError("Media MalFormed Error");
                    //call back of onerror occurs
                    if (onError != null) {
                        onError.OnError();
                    }
                }
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //Bitstream is conforming to the related coding standard or file spec, but the media framework does not support the feature.
                if (onMediaError != null) {
                    onMediaError.OnMediaError("Media Unsupported Error");
                    //call back of onerror occurs
                    if (onError != null) {
                        onError.OnError();
                    }
                }
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //Some operation takes too long to complete, usually more than 3-5 seconds.
                if (onMediaError != null) {
                    onMediaError.OnMediaError("Media Timed Out Error");
                    //call back of onerror occurs
                    if (onError != null) {
                        onError.OnError();
                    }
                }
                break;
            case -2147483648:
                //System Error
                if (onMediaError != null) {
                    onMediaError.OnMediaError("Media System Error");
                    //call back of onerror occurs
                    if (onError != null) {
                        onError.OnError();
                    }
                }
                break;
            default:
                break;
        }*//*

        if (onError != null) {
            onError.OnError();
        }

        return true;
    }*/

    //gestures
    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private int mW, mH;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // scale our video view
            mW *= detector.getScaleFactor();
            mH *= detector.getScaleFactor();
            if (mW < MIN_WIDTH) { // limits width
                mW = videoView.getWidth();
                mH = videoView.getHeight();
            }
            //Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
            // videoView.setFixedVideoSize(mW, mH); // important
            mRootParam.width = mW;
            mRootParam.height = mH;


            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mW = videoView.getWidth();
            mH = videoView.getHeight();
            Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.d("onScaleEnd", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
        }

    }

    private class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        /**
         * The Starting span.
         */
        float startingSpan;
        /**
         * The End span.
         */
        float endSpan;
        /**
         * The Start focus x.
         */
        float startFocusX;
        /**
         * The Start focus y.
         */
        float startFocusY;


        public boolean onScaleBegin(ScaleGestureDetector detector) {
            startingSpan = detector.getCurrentSpan();
            startFocusX = detector.getFocusX();
            startFocusY = detector.getFocusY();
            return true;
        }


        public boolean onScale(ScaleGestureDetector detector) {
            //  relVideoViewPGesture.scale(detector.getCurrentSpan() / startingSpan, startFocusX, startFocusY);

            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            //relVideoViewPGesture.restore();
        }
    }


    /**
     * Tv page get video extractor.
     *
     * @param page                        the page
     * @param numberOfResult              the number of result
     * @param orderByDate                 the order by date
     * @param orderDESCorASC              the order des cor asc
     * @param stringToSearch              the string to search
     * @param status                      the status
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
//Videos
    public void tvPageGetVideoExtractor(int page,
                                        int numberOfResult, String orderByDate, String orderDESCorASC
            , String stringToSearch, String status,
                                        final OnTvPageResponseApiListener onTvPageResponseApiListener) {
        String login_user_id = "";
        //sString idFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String idFromPref = TvPageInstance.getInstance(mContext).getApiKey();
        if (idFromPref != null && idFromPref.trim().length() > 0) {
            //set this is id in  pref
            login_user_id = idFromPref;
        } else {
            //set Default id in pref
            login_user_id = "1758929";
        }

        //set Pref of user id
        //sMyPreferences.setPref(mContext, MyPreferences.USER_ID_PREF_KEY, login_user_id);
        TvPageInstance.getInstance(mContext).setApiKey(login_user_id);

        //https://app.tvpage.com/api/videos?X-login-id=1758929&p=0&n=5&o=%22&od=DESC
        //call api
        String urlToPass = baseUrlApi + "/videos?X-login-id=" + login_user_id;

        if (numberOfResult != 0) {
            urlToPass = urlToPass + "&p=" + page + "&n=" + numberOfResult;
        }
        if (orderByDate != null && !TextUtils.isEmpty(orderByDate)) {
            urlToPass = urlToPass + "&o=" + orderByDate;
        }
        if (orderDESCorASC != null && !TextUtils.isEmpty(orderDESCorASC)
                ) {
            urlToPass = urlToPass + "&od=" + orderDESCorASC;
        }

        if (stringToSearch != null && !TextUtils.isEmpty(stringToSearch)) {
            urlToPass = urlToPass + "&s=" + stringToSearch;
        }

        if (status != null && !TextUtils.isEmpty(status)) {
            urlToPass = urlToPass + "&status=" + status;
        }

        //System.out.println("Url to pass>> " + urlToPass);

        new GetVideoTask(urlToPass, onTvPageResponseApiListener).execute();

    }


    /**
     * The type Get video task.
     */
    public class GetVideoTask extends AsyncTask<Void, Void, Void> {

        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get video task.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoTask(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            TvPageUtils.sout("Videos Results>> " + results);


            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        JSONArray jsonArray = new JSONArray(results);
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start with html
                        /*sssMyPreferences.setPref(mContext, MyPreferences.USER_ID_PREF_KEY, "1758929");
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);*/
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }

            }

        }


    }


    private String callTvPageServer(String url) throws TvPageException, IOException {

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = getOkHttpClient().newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else {
                if (response.code() == 403) {
                    throw new TvPageException("Invalid data : " + response);
                } else if (response.code() == 500) {
                    throw new TvPageException("Error on process: " + response);
                } else {
                    throw new TvPageException("Unknown error: " + response);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }


    }

    /**
     * Tv page get video product extractor.
     *
     * @param video_id                    the video id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageGetVideoProductExtractor(String video_id, final OnTvPageResponseApiListener onTvPageResponseApiListener) {

        String login_user_id = "";

        if (video_id != null && video_id.trim().length() > 0) {
            //set this is id in  pref
            //get usaer id from pref
            //slogin_user_id = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
            login_user_id = TvPageInstance.getInstance(mContext).getApiKey();

            //call api
            //https://app.tvpage.com/api/videos/87501580/products?X-login-id=1758929
            new GetVideoProductTask(baseUrlApi + "/videos/" + video_id + "/products?X-login-id=" + login_user_id
                    , onTvPageResponseApiListener).execute();

        }


    }

    /**
     * The type Get video product task.
     */
    public class GetVideoProductTask extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;


        /**
         * Instantiates a new Get video product task.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoProductTask(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                //System.out.println("product Result: " + results);
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();

                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        tvPageResponseModel.setJsonArray(jsonArray);

                        //pass listener
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page get video channle extractor.
     *
     * @param video_id                    the video id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageGetVideoChannleExtractor(String video_id, final OnTvPageResponseApiListener onTvPageResponseApiListener) {

        String login_user_id = "";

        if (video_id != null && video_id.trim().length() > 0) {
            //set this is id in  pref
            //get usaer id from pref
            //slogin_user_id = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
            login_user_id = TvPageInstance.getInstance(mContext).getApiKey();

            //call api
            //https://app.tvpage.com/api/videos/87501580/channels?X-login-id=1758929
            new GetVideoChannelTask(baseUrlApi + "/videos/" + video_id + "/channels?X-login-id=" + login_user_id
                    , onTvPageResponseApiListener).execute();

        }


    }

    /**
     * The type Get video channel task.
     */
    public class GetVideoChannelTask extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;


        /**
         * Instantiates a new Get video channel task.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoChannelTask(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page get single video extractor.
     *
     * @param video_id                    the video id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageGetSingleVideoExtractor(String video_id, final OnTvPageResponseApiListener onTvPageResponseApiListener) {
        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/videos/87501580?X-login-id=1758929
        new GetSingleVideoTask(baseUrlApi + "/videos/" + video_id + "?X-login-id=" + loginUserIdFromPref, onTvPageResponseApiListener).execute();

    }


    /**
     * The type Get single video task.
     */
    public class GetSingleVideoTask extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get single video task.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetSingleVideoTask(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {

                        JSONObject jsonObject = new JSONObject(results);


                        tvPageResponseModel.setJsonObject(jsonObject);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start with html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page transcript video extractor.
     *
     * @param video_id                    the video id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageTranscriptVideoExtractor(String video_id, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/videos/87501580/transcript?X-login-id=1758929
        new GetVideoTranscriptor(baseUrlApi + "/videos/" + video_id + "/transcript?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get video transcriptor.
     */
    public class GetVideoTranscriptor extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get video transcriptor.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoTranscriptor(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();

                    if (!results.startsWith("<html>")) {
                        JSONObject jsonObject = new JSONObject(results);


                     /*   if (!jsonObject.isNull("loginId")) {
                            String loginId = jsonObject.getString("loginId");
                            //add title in tvpage model
                            tvPageVideoTranscriptorModel.setLoginId(loginId);
                        }
                        if (!jsonObject.isNull("id")) {
                            String id = jsonObject.getString("id");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setId(id);
                        }
                        if (!jsonObject.isNull("entityType")) {
                            String entityType = jsonObject.getString("entityType");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setEntityType(entityType);
                        }
                        if (!jsonObject.isNull("transcripts")) {
                            String transcripts = jsonObject.getString("transcripts");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setTranscripts(transcripts);
                        }*/

                        tvPageResponseModel.setJsonObject(jsonObject);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start with html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page video search extractor.
     *
     * @param search_keyword              the search keyword
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageVideoSearchExtractor(String search_keyword, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/videos/search?X-login-id=1758929&s=a
        new GetVideoSearch(baseUrlApi + "/videos/search?X-login-id=" + loginUserIdFromPref + "&s=" + search_keyword
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get video search.
     */
    public class GetVideoSearch extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get video search.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoSearch(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();

                    if (!results.startsWith("<html>")) {
                        JSONArray jsonArray = new JSONArray(results);


                     /*   if (!jsonObject.isNull("loginId")) {
                            String loginId = jsonObject.getString("loginId");
                            //add title in tvpage model
                            tvPageVideoTranscriptorModel.setLoginId(loginId);
                        }
                        if (!jsonObject.isNull("id")) {
                            String id = jsonObject.getString("id");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setId(id);
                        }
                        if (!jsonObject.isNull("entityType")) {
                            String entityType = jsonObject.getString("entityType");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setEntityType(entityType);
                        }
                        if (!jsonObject.isNull("transcripts")) {
                            String transcripts = jsonObject.getString("transcripts");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setTranscripts(transcripts);
                        }*/

                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start with html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }


    /**
     * Tv page video reference id extractor.
     *
     * @param reference_id_comma_seprated the reference id comma seprated
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageVideoReferenceIdExtractor(String reference_id_comma_seprated, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/videos/referenceIds?X-login-id=1758929&ids=001%2C4%2C5
        new GetVideoReferenceId(baseUrlApi + "/videos/referenceIds?X-login-id=" + loginUserIdFromPref
                + "&ids=" + reference_id_comma_seprated
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get video reference id.
     */
    public class GetVideoReferenceId extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get video reference id.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetVideoReferenceId(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();

                    if (!results.startsWith("<html>")) {
                        JSONObject jsonObject = new JSONObject(results);


                     /*   if (!jsonObject.isNull("loginId")) {
                            String loginId = jsonObject.getString("loginId");
                            //add title in tvpage model
                            tvPageVideoTranscriptorModel.setLoginId(loginId);
                        }
                        if (!jsonObject.isNull("id")) {
                            String id = jsonObject.getString("id");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setId(id);
                        }
                        if (!jsonObject.isNull("entityType")) {
                            String entityType = jsonObject.getString("entityType");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setEntityType(entityType);
                        }
                        if (!jsonObject.isNull("transcripts")) {
                            String transcripts = jsonObject.getString("transcripts");
                            //add id in tvpage model
                            tvPageVideoTranscriptorModel.setTranscripts(transcripts);
                        }*/

                        tvPageResponseModel.setJsonObject(jsonObject);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start with html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }


    /**
     * Tv page product extractor.
     *
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
//product
    public void tvPageProductExtractor(OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/products?X-login-id=1758929
        new GetProducts(baseUrlApi + "/products?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get products.
     */
    public class GetProducts extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get products.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetProducts(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);

                       /* for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TvPageProductModel tvPageProductModel = new TvPageProductModel();


                            if (!jsonObject.isNull("title")) {
                                String title = jsonObject.getString("title");
                                tvPageProductModel.setTitle(title);
                            }
                            if (!jsonObject.isNull("description")) {
                                String description = jsonObject.getString("description");
                                tvPageProductModel.setDescription(description);
                            }
                            if (!jsonObject.isNull("referenceId")) {
                                String referenceId = jsonObject.getString("referenceId");
                                tvPageProductModel.setReferenceId(referenceId);
                            }
                            if (!jsonObject.isNull("data")) {

                                String data = jsonObject.getString("data");
                                tvPageProductModel.setData(data);
                            }
                            if (!jsonObject.isNull("date_created")) {
                                String date_created = jsonObject.getString("date_created");
                                tvPageProductModel.setDate_created(date_created);
                            }
                            if (!jsonObject.isNull("date_modified")) {
                                String date_modified = jsonObject.getString("date_modified");
                                tvPageProductModel.setDate_modified(date_modified);
                            }
                            if (!jsonObject.isNull("tags")) {
                                String tags = jsonObject.getString("tags");
                                tvPageProductModel.setTags(tags);
                            }
                            if (!jsonObject.isNull("cartId")) {
                                String cartId = jsonObject.getString("cartId");
                                tvPageProductModel.setCartId(cartId);
                            }
                            if (!jsonObject.isNull("OUT_OF_STOCK")) {
                                String OUT_OF_STOCK = jsonObject.getString("OUT_OF_STOCK");
                                tvPageProductModel.setOUT_OF_STOCK(OUT_OF_STOCK);
                            }
                            if (!jsonObject.isNull("search")) {
                                String search = jsonObject.getString("search");
                                tvPageProductModel.setSearch(search);
                            }
                            if (!jsonObject.isNull("visibility")) {
                                String visibility = jsonObject.getString("visibility");
                                tvPageProductModel.setVisibility(visibility);
                            }
                            if (!jsonObject.isNull("id")) {
                                String id = jsonObject.getString("id");
                                tvPageProductModel.setId(id);
                            }
                            if (!jsonObject.isNull("loginId")) {
                                String loginId = jsonObject.getString("loginId");
                                tvPageProductModel.setLoginId(loginId);
                            }
                            if (!jsonObject.isNull("entityType")) {
                                String entityType = jsonObject.getString("entityType");
                                tvPageProductModel.setEntityType(entityType);
                            }
                            if (!jsonObject.isNull("linkUrl")) {
                                String linkUrl = jsonObject.getString("linkUrl");
                                tvPageProductModel.setLinkUrl(linkUrl);
                            }
                            if (!jsonObject.isNull("actionText")) {
                                String actionText = jsonObject.getString("actionText");
                                tvPageProductModel.setActionText(actionText);
                            }
                            if (!jsonObject.isNull("imageUrl")) {
                                String imageUrl = jsonObject.getString("imageUrl");
                                tvPageProductModel.setImageUrl(imageUrl);
                            }
                            if (!jsonObject.isNull("category")) {
                                String category = jsonObject.getString("category");
                                tvPageProductModel.setCategory(category);
                            }
                            if (!jsonObject.isNull("quantity")) {
                                String quantity = jsonObject.getString("quantity");
                                tvPageProductModel.setQuantity(quantity);
                            }
                            if (!jsonObject.isNull("price")) {
                                String price = jsonObject.getString("price");
                                tvPageProductModel.setPrice(price);
                            }
                            if (!jsonObject.isNull("color")) {
                                String color = jsonObject.getString("color");
                                tvPageProductModel.setColor(color);
                            }
                            if (!jsonObject.isNull("pcondition")) {
                                String pcondition = jsonObject.getString("pcondition");
                                tvPageProductModel.setPcondition(pcondition);
                            }
                            if (!jsonObject.isNull("availability")) {
                                String availability = jsonObject.getString("availability");
                                tvPageProductModel.setAvailability(availability);
                            }
                            if (!jsonObject.isNull("gtin")) {
                                String gtin = jsonObject.getString("gtin");
                                tvPageProductModel.setGtin(gtin);
                            }
                            if (!jsonObject.isNull("mpn")) {
                                String mpn = jsonObject.getString("mpn");
                                tvPageProductModel.setMpn(mpn);
                            }
                            if (!jsonObject.isNull("gender")) {
                                String gender = jsonObject.getString("gender");
                                tvPageProductModel.setGender(gender);
                            }
                            if (!jsonObject.isNull("age_group")) {
                                String age_group = jsonObject.getString("age_group");
                                tvPageProductModel.setAge_group(age_group);
                            }
                            if (!jsonObject.isNull("price_sale")) {
                                String price_sale = jsonObject.getString("price_sale");
                                tvPageProductModel.setPrice_sale(price_sale);
                            }


                            if (!jsonObject.isNull("brand")) {
                                String brand = jsonObject.getString("brand");
                                tvPageProductModel.setBrand(brand);
                            }
                            if (!jsonObject.isNull("size")) {
                                String size = jsonObject.getString("size");
                                tvPageProductModel.setSize(size);
                            }
                            if (!jsonObject.isNull("pattern")) {
                                String pattern = jsonObject.getString("pattern");
                                tvPageProductModel.setPattern(pattern);
                            }
                            if (!jsonObject.isNull("cartName")) {
                                String cartName = jsonObject.getString("cartName");
                                tvPageProductModel.setCartName(cartName);
                            }


                            arrayList.add(tvPageProductModel);
                        }*/

                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page product single extractor.
     *
     * @param product_id                  the product id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageProductSingleExtractor(String product_id, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/products/87515019?X-login-id=1758929
        new GetSingleProduct(baseUrlApi + "/products/" + product_id + "?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get single product.
     */
    public class GetSingleProduct extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get single product.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetSingleProduct(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONObject jsonObject = new JSONObject(results);


                      /*  if (!jsonObject.isNull("title")) {
                            String title = jsonObject.getString("title");
                            tvPageProductSingleModel.setTitle(title);
                        }
                        if (!jsonObject.isNull("description")) {
                            String description = jsonObject.getString("description");
                            tvPageProductSingleModel.setDescription(description);
                        }
                        if (!jsonObject.isNull("referenceId")) {
                            String referenceId = jsonObject.getString("referenceId");
                            tvPageProductSingleModel.setReferenceId(referenceId);
                        }
                        if (!jsonObject.isNull("data")) {

                            String data = jsonObject.getString("data");
                            tvPageProductSingleModel.setData(data);
                        }
                        if (!jsonObject.isNull("date_created")) {
                            String date_created = jsonObject.getString("date_created");
                            tvPageProductSingleModel.setDate_created(date_created);
                        }
                        if (!jsonObject.isNull("date_modified")) {
                            String date_modified = jsonObject.getString("date_modified");
                            tvPageProductSingleModel.setDate_modified(date_modified);
                        }
                        if (!jsonObject.isNull("tags")) {
                            String tags = jsonObject.getString("tags");
                            tvPageProductSingleModel.setTags(tags);
                        }
                        if (!jsonObject.isNull("cartId")) {
                            String cartId = jsonObject.getString("cartId");
                            tvPageProductSingleModel.setCartId(cartId);
                        }
                        if (!jsonObject.isNull("OUT_OF_STOCK")) {
                            String OUT_OF_STOCK = jsonObject.getString("OUT_OF_STOCK");
                            tvPageProductSingleModel.setOUT_OF_STOCK(OUT_OF_STOCK);
                        }
                        if (!jsonObject.isNull("search")) {
                            String search = jsonObject.getString("search");
                            tvPageProductSingleModel.setSearch(search);
                        }
                        if (!jsonObject.isNull("visibility")) {
                            String visibility = jsonObject.getString("visibility");
                            tvPageProductSingleModel.setVisibility(visibility);
                        }
                        if (!jsonObject.isNull("id")) {
                            String id = jsonObject.getString("id");
                            tvPageProductSingleModel.setId(id);
                        }
                        if (!jsonObject.isNull("loginId")) {
                            String loginId = jsonObject.getString("loginId");
                            tvPageProductSingleModel.setLoginId(loginId);
                        }
                        if (!jsonObject.isNull("entityType")) {
                            String entityType = jsonObject.getString("entityType");
                            tvPageProductSingleModel.setEntityType(entityType);
                        }
                        if (!jsonObject.isNull("linkUrl")) {
                            String linkUrl = jsonObject.getString("linkUrl");
                            tvPageProductSingleModel.setLinkUrl(linkUrl);
                        }
                        if (!jsonObject.isNull("actionText")) {
                            String actionText = jsonObject.getString("actionText");
                            tvPageProductSingleModel.setActionText(actionText);
                        }
                        if (!jsonObject.isNull("imageUrl")) {
                            String imageUrl = jsonObject.getString("imageUrl");
                            tvPageProductSingleModel.setImageUrl(imageUrl);
                        }
                        if (!jsonObject.isNull("category")) {
                            String category = jsonObject.getString("category");
                            tvPageProductSingleModel.setCategory(category);
                        }
                        if (!jsonObject.isNull("quantity")) {
                            String quantity = jsonObject.getString("quantity");
                            tvPageProductSingleModel.setQuantity(quantity);
                        }
                        if (!jsonObject.isNull("price")) {
                            String price = jsonObject.getString("price");
                            tvPageProductSingleModel.setPrice(price);
                        }
                        if (!jsonObject.isNull("color")) {
                            String color = jsonObject.getString("color");
                            tvPageProductSingleModel.setColor(color);
                        }
                        if (!jsonObject.isNull("pcondition")) {
                            String pcondition = jsonObject.getString("pcondition");
                            tvPageProductSingleModel.setPcondition(pcondition);
                        }
                        if (!jsonObject.isNull("availability")) {
                            String availability = jsonObject.getString("availability");
                            tvPageProductSingleModel.setAvailability(availability);
                        }
                        if (!jsonObject.isNull("gtin")) {
                            String gtin = jsonObject.getString("gtin");
                            tvPageProductSingleModel.setGtin(gtin);
                        }
                        if (!jsonObject.isNull("mpn")) {
                            String mpn = jsonObject.getString("mpn");
                            tvPageProductSingleModel.setMpn(mpn);
                        }
                        if (!jsonObject.isNull("gender")) {
                            String gender = jsonObject.getString("gender");
                            tvPageProductSingleModel.setGender(gender);
                        }
                        if (!jsonObject.isNull("age_group")) {
                            String age_group = jsonObject.getString("age_group");
                            tvPageProductSingleModel.setAge_group(age_group);
                        }
                        if (!jsonObject.isNull("price_sale")) {
                            String price_sale = jsonObject.getString("price_sale");
                            tvPageProductSingleModel.setPrice_sale(price_sale);
                        }


                        if (!jsonObject.isNull("brand")) {
                            String brand = jsonObject.getString("brand");
                            tvPageProductSingleModel.setBrand(brand);
                        }
                        if (!jsonObject.isNull("size")) {
                            String size = jsonObject.getString("size");
                            tvPageProductSingleModel.setSize(size);
                        }
                        if (!jsonObject.isNull("pattern")) {
                            String pattern = jsonObject.getString("pattern");
                            tvPageProductSingleModel.setPattern(pattern);
                        }
                        if (!jsonObject.isNull("cartName")) {
                            String cartName = jsonObject.getString("cartName");
                            tvPageProductSingleModel.setCartName(cartName);
                        }*/


                        //pass listener
                        tvPageResponseModel.setJsonObject(jsonObject);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page product recommend network extractor.
     *
     * @param product_id                  the product id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageProductRecommendNetworkExtractor(String product_id, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/products/87515019/recommend/network?X-login-id=1758929
        new GetProductRecommendNetwork(baseUrlApi + "/products/" + product_id + "/recommend/network?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get product recommend network.
     */
    public class GetProductRecommendNetwork extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get product recommend network.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetProductRecommendNetwork(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page product videos extractor.
     *
     * @param product_id                  the product id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageProductVideosExtractor(String product_id, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/products/87515019/videos?X-login-id=1758929
        new GetProductVideos(baseUrlApi + "/products/" + product_id + "/videos?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get product videos.
     */
    public class GetProductVideos extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get product videos.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetProductVideos(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            //System.out.println("pro details video URLS> " + url + "Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    //Channels

    /**
     * Tv page channels extractor.
     *
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageChannelsExtractor(OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api
        //https://app.tvpage.com/api/channels?X-login-id=1758929
        new GetChannels(baseUrlApi + "/channels?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get channels.
     */
    public class GetChannels extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get channels.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetChannels(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {

            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page channels info extractor.
     *
     * @param channel_id                  the channel id
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageChannelsInfoExtractor(String channel_id, OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

        //call api

        //https:app.tvpage.com/api/channels/87486516?X-login-id=1758929
        new GetChannelInfo(baseUrlApi + "/channels/" + channel_id + "?X-login-id=" + loginUserIdFromPref
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get channel info.
     */
    public class GetChannelInfo extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get channel info.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetChannelInfo(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            // System.out.println("Results>> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONObject jsonObject = new JSONObject(results);


                        //pass listener
                        tvPageResponseModel.setJsonObject(jsonObject);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    /**
     * Tv page channels videos extractor.
     *
     * @param channel_id                  the channel id
     * @param page                        the page
     * @param numberOfResult              the number of result
     * @param stringToSearch              the string to search
     * @param onTvPageResponseApiListener the on tv page response api listener
     */
    public void tvPageChannelsVideosExtractor(String channel_id, int page, int numberOfResult, String stringToSearch
            , OnTvPageResponseApiListener onTvPageResponseApiListener) {

        //sString loginUserIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginUserIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();


        String urlToPass = baseUrlApi + "/channels/" + channel_id + "/videos?X-login-id=" + loginUserIdFromPref;

        if (numberOfResult != 0) {
            urlToPass = urlToPass + "&p=" + page + "&n=" + numberOfResult;
        }


        if (stringToSearch != null && !TextUtils.isEmpty(stringToSearch)) {
            urlToPass = urlToPass + "&s=" + stringToSearch;
        }


        //call api
        //https://app.tvpage.com/api/channels/87486517/videos?X-login-id=1758929
        new GetChannelVideos(urlToPass
                , onTvPageResponseApiListener).execute();

    }

    /**
     * The type Get channel videos.
     */
    public class GetChannelVideos extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";
        private OnTvPageResponseApiListener onTvPageResponseApiListener;

        /**
         * Instantiates a new Get channel videos.
         *
         * @param url                         the url
         * @param onTvPageResponseApiListener the on tv page response api listener
         */
        public GetChannelVideos(String url, OnTvPageResponseApiListener onTvPageResponseApiListener) {
            this.onTvPageResponseApiListener = onTvPageResponseApiListener;
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);
            try {
                if (!TextUtils.isEmpty(results)) {
                    TvPageResponseModel tvPageResponseModel = new TvPageResponseModel();
                    if (!results.startsWith("<html>")) {
                        //TvPageUtils.sout("URLS: " + url + "Result Product: " + results);
                        JSONArray jsonArray = new JSONArray(results);


                        //pass listener
                        tvPageResponseModel.setJsonArray(jsonArray);
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    } else {
                        //start woith html
                        onTvPageResponseApiListener.onSuccess(tvPageResponseModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onTvPageResponseApiListener.onFailure(e);
                //call back of onerror occurs
                if (onError != null) {
                    onError.OnError();
                }
            }

        }


    }

    //Analytics api
/*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929
        // &pg=87486517&vd=87501580&vvs=D4C2F9A2-EB1E-493A-8274-AC26DC03ED55&vct=1&vdr=144&vt=3*/
    private void analyticsChannelImpressionApi() {

        if (isCallAnalyticsApi) {
            //Log.d("Channel impresion api", "Channel impresion api");

        /*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929&rt=ci*/
            //sString loginIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
            String loginIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();
            new PostChannelImpressionAnalytics(baseUrlAnalyticsApi + "?li=" + loginIdFromPref + "&X-login-id=" + loginIdFromPref + "&rt=ci").execute();
        }
    }

    /**
     * The type Post channel impression analytics.
     */
    class PostChannelImpressionAnalytics extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";

        /**
         * Instantiates a new Post channel impression analytics.
         *
         * @param url the url
         */
        public PostChannelImpressionAnalytics(String url) {
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);

        }
    }

    private void analyticsVideoViewApi() {
        if (isCallAnalyticsApi) {
            //Log.d("Videoview analytics once api", "Videoview analytics once api");

        /*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929&rt=ci*/
            //sString loginIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
            String loginIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();
            new PostVideoViewAnalytics(baseUrlAnalyticsApi + "?li=" + loginIdFromPref + "&X-login-id=" + loginIdFromPref
                    + "&pg=" + channelIdForAnalytics + "&vd=" + videoIdForAnalytics + "&rt=vv").execute();
        }
    }

    /**
     * The type Post video view analytics.
     */
    class PostVideoViewAnalytics extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";

        /**
         * Instantiates a new Post video view analytics.
         *
         * @param url the url
         */
        public PostVideoViewAnalytics(String url) {
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);

        }
    }

    private void analyticsVideoTimeApi() {
        if (isCallAnalyticsApi) {
            // Log.d("view time analytics Repeats api", "view time analytics Repeats api");

            long currentTimeToPass = 0;
            long totalTimeToPass = 0;
            final int viewTimeToPass = 3;

        /*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929&rt=ci*/
            //sString loginIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
            String loginIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();

            @SuppressLint("HardwareIds")
            String unique_id = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID); //unique device id

            if (getCurrentTime() != 0) {
                currentTimeToPass = getCurrentTime();
            }

            if (getDuration() != 0) {
                totalTimeToPass = getDuration();
            }

            new PostViewTimeAnalytics(baseUrlAnalyticsApi + "?li=" + loginIdFromPref + "&X-login-id=" + loginIdFromPref
                    + "&pg=" + channelIdForAnalytics + "&vd=" + videoIdForAnalytics + "&vvs=" + unique_id + "&vct=" + currentTimeToPass
                    + "&vdr=" + totalTimeToPass + "&vt=" + viewTimeToPass + "&rt=vt").execute();

        }

    }

    /**
     * The type Post view time analytics.
     */
    class PostViewTimeAnalytics extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";

        /**
         * Instantiates a new Post view time analytics.
         *
         * @param url the url
         */
        public PostViewTimeAnalytics(String url) {
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);

        }
    }


    /**
     * Analytics product impression api.
     *
     * @param channel_id the channel id
     * @param video_id   the video id
     * @param product_id the product id
     */
    public void analyticsProductImpressionApi(int channel_id, int video_id, int product_id) {
        // Log.d("Product Impression analytics api", "Product Impression analytics api");


        /*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929&rt=ci*/
        //sString loginIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();


        new PostProductImpressionAnalytics(baseUrlAnalyticsApi + "?li=" + loginIdFromPref + "&X-login-id=" + loginIdFromPref
                + "&pg=" + channel_id + "&vd=" + video_id + "&ct=" + product_id + "&rt=pi").execute();

    }

    /**
     * The type Post product impression analytics.
     */
    class PostProductImpressionAnalytics extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";

        /**
         * Instantiates a new Post product impression analytics.
         *
         * @param url the url
         */
        public PostProductImpressionAnalytics(String url) {
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);

        }
    }

    /**
     * Analytics product click api.
     *
     * @param channel_id the channel id
     * @param video_id   the video id
     * @param product_id the product id
     */
    public void analyticsProductClickApi(int channel_id, int video_id, int product_id) {
        //Log.d("Product CLICKS Impression analytics api", "Product CLICKS Impression analytics api");


        /*http://api.tvpage.com/v1/__tvpa.gif?li=1758929&X-login-id=1758929&rt=ci*/
        //sString loginIdFromPref = MyPreferences.getPref(mContext, MyPreferences.USER_ID_PREF_KEY);
        String loginIdFromPref = TvPageInstance.getInstance(mContext).getApiKey();


        new PostProductClickAnalytics(baseUrlAnalyticsApi + "?li=" + loginIdFromPref + "&X-login-id=" + loginIdFromPref
                + "&pg=" + channel_id + "&vd=" + video_id + "&ct=" + product_id + "&rt=pk").execute();

    }

    /**
     * The type Post product click analytics.
     */
    class PostProductClickAnalytics extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private String results = "";
        private String url = "";

        /**
         * Instantiates a new Post product click analytics.
         *
         * @param url the url
         */
        public PostProductClickAnalytics(String url) {
            this.url = url;
        }

        @SuppressWarnings("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                results = callTvPageServer(url);
            } catch (IOException | TvPageException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void avoid) {
            //System.out.println("URL>>" + url + "Results >> " + results);

        }
    }

    /**
     * The Runnable three second once.
     */
    Runnable runnableThreeSecondOnce = new Runnable() {
        @Override
        public void run() {

            analyticsVideoViewApi();
        }
    };

    /**
     * The Runnable three second repeat.
     */
    Runnable runnableThreeSecondRepeat = new Runnable() {
        @Override
        public void run() {
            //call api view time
            analyticsVideoTimeApi();

            //againregister call back
            mHandlerThreeSecondRepeat.postDelayed(runnableThreeSecondRepeat, 3000);

        }
    };

    //set full screen mode
    private void setFullScreenMode() {

        if (videoView != null
                && urlToPlayDesiredQuality != null
                && !TextUtils.isEmpty(urlToPlayDesiredQuality)
                && urlToPlayDesiredQuality.toString().trim().length() > 0
                ) {
            videoView.pause();

            //open full screen activty
            long pos = videoView.getCurrentPosition();
            //Log.d("position", "" + pos);

            Intent intent = new Intent(mContext, FullScreenVideoPlayerActivity.class);
            intent.putExtra("video_position", pos);
            intent.putExtra("video_url", urlToPlayDesiredQuality);
            intent.putExtra("video_completed", isVideoCompleted);
            intent.putExtra("video_mute", isMuted);
            intent.putExtra("video_state", state);
            intent.putExtra("video_volume", volume_video);
            intent.putExtra("video_quality_data", listOfQuality);
            intent.putExtra("video_quality_level", getQuality());
            mContext.startActivity(intent);

        }
    }

    /**
     * Sets back from fullscreen.
     *
     * @param hashMapArrayList    the hash map array list
     * @param video_quality_level the video quality level
     */
    void setBackFromFullscreen(ArrayList<HashMap<String, String>> hashMapArrayList, int video_quality_level) {




        /*SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("video_position", videoCurrentPosition);
        editor.commit();*/




       /* if (videoView != null) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //update pref odf position
                    try {
                        SharedPreferences mPref = mContext.getSharedPreferences("tvpage_pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putInt("video_position", videoCurrentPosition);
                        editor.commit();


                        // Updating progress bar
                        updateProgressBar();

                        videoView.seekTo(position);

                        //play videos
                        play();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);

          *//*  if (hashMapArrayList != null &&
                    hashMapArrayList.size() > 0) {
                listOfQuality.clear();
                listOfQuality = hashMapArrayList;
                //set recycler's perform click
                // setQualityForBackFromFullscreen();
                setDataToQualityList();
            }*//*
        }*/
    }

    private void setFullscreenFlag(boolean flag, FullscreenExitListener fullscreenExitListener) {
        this.isFullScreenMode = true;
        this.fullscreenExitListener = fullscreenExitListener;
    }

    /**
     * Gets video view current positions.
     *
     * @return the video view current positions
     */
    public long getVideoViewCurrentPositions() {
        long videoCurrentPosition = 0;
        if (videoView != null) {
            videoCurrentPosition = videoView.getCurrentPosition();
        }
        return videoCurrentPosition;
    }

    private FullscreenExitListener fullscreenExitListener;

    /**
     * The interface Fullscreen exit listener.
     */
    public interface FullscreenExitListener {
        /**
         * On fullscreen exit.
         */
        void onFullscreenExit();

        /**
         * On hide control.
         */
        void onHideControl();
    }


    private static class Connectivity {

        /**
         * Get the network info
         *
         * @param context the context
         * @return network info
         */
        public static NetworkInfo getNetworkInfo(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }

        /**
         * Check if there is any connectivity
         *
         * @param context the context
         * @return boolean
         */
        public static boolean isConnected(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected());
        }

        /**
         * Check if there is any connectivity to a Wifi network
         *
         * @param context the context
         * @return boolean
         */
        public static boolean isConnectedWifi(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
        }

        /**
         * Check if there is any connectivity to a mobile network
         *
         * @param context the context
         * @return boolean
         */
        public static boolean isConnectedMobile(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
        }

        /**
         * Check if there is fast connectivity
         *
         * @param context the context
         * @return boolean
         */
        public static boolean isConnectedFast(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
        }

        /**
         * Check if the connection is fast
         *
         * @param type    the type
         * @param subType the sub type
         * @return boolean
         */
        public static boolean isConnectionFast(int type, int subType) {
            if (type == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return false; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return true; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return true; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return false; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return true; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return true; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return true; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
			 * to appropriate level to use these
			 */
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                        return true; // ~ 1-2 Mbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        return true; // ~ 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                        return true; // ~ 10-20 Mbps
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        return false; // ~25 kbps
                    case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                        return true; // ~ 10+ Mbps
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return false;
                }
            } else {
                return false;
            }
        }


        /**
         * Gets connection speed range.
         *
         * @param context the context
         * @return the connection speed range
         */
        public static String getConnectionSpeedRange(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info == null || !info.isConnected()) {
                return "error";
            }
            int type = info.getType();
            int subType = info.getSubtype();

            if (type == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager mTelephonyManager = (TelephonyManager)
                        context.getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = mTelephonyManager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4G";
                    default:
                        return "Unknown";
                }
            } else {
                return "unknown";
            }
        }


    }

    private static boolean isInternetConnected(Context context) {
        boolean isConnected = false;
        try {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
            return isConnected;
        }

    }

    private static void makeToast(String message, Context mContext) {
        try {
            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getStringText(Context context, int textToget) {
        String textToReturn = "";
        try {
            textToReturn = context.getResources().getString(textToget);
            return textToReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return textToReturn;
        }

    }

    private static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}