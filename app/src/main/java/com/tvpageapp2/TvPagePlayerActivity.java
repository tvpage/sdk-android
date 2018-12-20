package com.tvpageapp2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.TvPageInterfaces;
import com.tvpage.lib.view.TvPageBuilder;
import com.tvpage.lib.view.TvPageCarouselVideoWidget;
import com.tvpage.lib.view.TvPagePlayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MTPC-133 on 2/2/2018.
 */

public class TvPagePlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TvPagePlayerActivity.class.getSimpleName();
    private TvPagePlayer tvPagePlayer;
    TvPageVideoModel tvPageVideoModel;
    private String screenDeterminationValueIntent = "";

    //intent data
    String titleIntent = "";
    String idIntent = "";
    String idChannelForVideosIntent = "";
    String typeIntent = "";
    String video_idIntent = "";
    String video_descIntent = "";
    String video_durationIntent = "";
    String video_dateIntent = "";
    String video_entityIdAsChannelIdIntent = "";


    ArrayList<HashMap<String, String>> qualityListIntent = new ArrayList<HashMap<String, String>>();


    private JSONObject jsonControlsToPass = null;
    private String jsonObjectToPass = "";

     /* Modal Background Style Variables Start*/

    //default Modal Background Color
    private int mModalBackGroundColor = Color.WHITE;

    //default Modal Shadow Color
    private int mModalShadowColor = Color.WHITE;

    //default Modal border color
    private int mModalBorderColor = Color.WHITE;

    //default Modal border Width
    private float mModalBorderWidth = 0.0f;

    //default Modal body padding
    private float mModalBodyPadding = 0.0f;


    /* Modal Background Style Variables End*/

    /* Modal Title Style Variables Start*/

    //default Modal Title Color
    private int mModalTitleTextColor;

    //default Items Text Size
    private float mModalTitleTextSize;

    //default Modal Title Text Padding TOP
    private float mModalTitleTextPaddingTop;

    //default Modal Title Text Padding LEFT
    private float mModalTitleTextPaddingLeft;

    //default Modal Title Text Padding RIGHT
    private float mModalTitleTextPaddingRight;

    //default Modal Title Text Padding BOTTOM
    private float mModalTitleTextPaddingBottom;

    //default Modal Title is TOP
    private String mModalTitleGravity = "1";

    //default Items Text Style
    //default Modal Title TextStyle is NORMAL
    private String mModalTitleStyle = "1";

    //default Modal Title Type Face
    private String mModalTitleTypeFace = "fonts/helvetica.ttf";

    private static final String DEFAULT_ITEMS_TITLE_TYPE_FACE = "fonts/helvetica.ttf";

    /* Modal Product Title Style Variables End*/

     /* Modal PRODUCT Title Style Variables Start*/

    //default Product Title Color
    private int mProductTitleTextColor;

    //default Product Text Size
    private float mProductTitleTextSize;

    //default Product Title is TOP
    private String mProductTitleGravity = "1";

    //default Items Text Style
    //default Modal Title TextStyle is NORMAL
    private String mProductTitleStyle = "1";

    //default Modal Title Type Face
    private String mProductTitleTypeFace = "fonts/helvetica.ttf";

    private static final String DEFAULT_PRODUCT_TITLE_TYPE_FACE = "fonts/helvetica.ttf";

    /* Modal PRODUCT Title Style Variables End*/

    /* Modal Close Icon Style Variables Start*/

    //default Close Icon Color
    private int mCloseIconColor;

    //default Close Icon Width
    private float mCloseIconWidth;

    //default Close Icon Height
    private float mCloseIconHeight;

    //default Close Icon Padding
    private float mCloseIconPadding;

    /* Modal Close Icon Style Variables End*/


     /* Product Popup Style Variables Start*/

    //default popup background color
    private int mProductPopupBackgroundColor = Color.WHITE;

    //default popup border color
    private int mProductPopupBorderColor = Color.BLACK;

    //default popup  Width
    private float mProductPopupWidth;

    //default popup border Width
    private float mProductPopupBorderWidth;

    //default popup padding
    private float mProductPopupPadding;

    //default popup radius
    private float mProductPopupBorderRadius;

    /* Product Popup Style Variables End*/

    /* Modal PRODUCT Price Style Variables Start*/

    //default Product Price Color
    private int mProductPriceTextColor;

    //default Product Price Size
    private float mProductPriceTextSize;

    //default Product Price is TOP
    private String mProductPriceGravity = "1";

    //default Modal Price TextStyle is NORMAL
    private String mProductPriceStyle = "1";

    //default Modal Price Type Face
    private String mProductPriceTypeFace = "fonts/helvetica.ttf";

    /* Modal PRODUCT Price Style Variables End*/

    /* Modal PRODUCT REVIEW Style Variables Start*/

    //default Product Price Color
    private int mProductReviewTextColor;

    //default Product Price Size
    private float mProductReviewTextSize;

    //default Product Price is TOP
    private String mProductReviewGravity = "1";

    //default Modal Price TextStyle is NORMAL
    private String mProductReviewStyle = "1";

    //default Modal Price Type Face
    private String mProductReviewTypeFace = "fonts/helvetica.ttf";

    /* Modal PRODUCT REVIEW Style Variables End*/

    /* Modal PRODUCT CTA Style Variables Start*/

    //default Product CTA
    private String mProductCTAText = "View Details";

    //default Product CTA Color
    private int mProductCTATextColor;

    //default Product CTA Size
    private float mProductCTATextSize;

    //default Product CTA is TOP
    private String mProductCTAGravity = "1";

    //default Modal CTA TextStyle is NORMAL
    private String mProductCTAStyle = "1";

    //default Modal CTA Type Face
    private String mProductCTATypeFace = "fonts/helvetica.ttf";

    //default Modal CTA Transformation
    //default Uppercase
    private String mProductCTATextTransformation = "0";

    /* Modal PRODUCT CTA Style Variables End*/

     /* Product CTA BG Style Variables Start*/

    //default CTA background color
    private int mProductCTABackgroundColor = Color.WHITE;

    //default CTA border color
    private int mProductCTABorderColor = Color.BLACK;

    //default CTA border Width
    private float mProductCTABorderWidth;

    //default CTA radius
    private float mProductCTABorderRadius;

    /* Product CTA BG Style Variables End*/

    /* Product IMAGE Style Variables Start*/

    //default product image border color
    private int mProductImageBorderColor = Color.TRANSPARENT;

    //default product image border  width
    private float mProductImageBorderWidth = 0.0f;

    //default product image size
    private float mProductImageBorderSize;

    //default image overlay color
    private int mProductImageOverLayRed;
    private int mProductImageOverLayGreen;
    private int mProductImageOverLayBlue;
    private int mProductImageOverLayAlpha;

    /* Product IMAGE Style Variables End*/

    //default product shadow visibility
    private String mProductShadowVisibility = "0";

    //default product shadow color
    private int mProductShadowColor = Color.WHITE;

    private int position;
    private static ArrayList<TvPageVideoModel> list;

    /*Video Play Option Start*/

    //defalut auto video play
    private boolean isAutoVideoPlay = false;

    //default auto video next
    private boolean isAutoVideoNext = false;

    //check video is first time load
    private int mCounterVideoStart = 0;

    /*Video Play Option  End*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_latest_video_details);
        setDefaultValue();
        jsonObjectToPass = getResources().getString(R.string.str);
        init();


    }

    private void setDefaultValue() {
        /*//Modal Background Style
        mModalBackGroundColor = getResources().getColor(com.tvpage.lib.R.color.white);
        mModalShadowColor = getResources().getColor(com.tvpage.lib.R.color.default_title_text_color);
        mModalBorderColor = getResources().getColor(com.tvpage.lib.R.color.default_title_text_color);
        mModalBodyPadding = getResources().getDimension(com.tvpage.lib.R.dimen.default_items_title_padding);
        mModalBorderWidth = getResources().getDimension(com.tvpage.lib.R.dimen.default_items_title_padding);

        //Title Text Style
        mModalTitleTextColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_title_text_color);
        mModalTitleTextSize = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_title_text_size);
        mModalTitleTextPaddingTop = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_title_padding_top);
        mModalTitleTextPaddingLeft = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_title_padding_right_left);
        mModalTitleTextPaddingRight = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_title_padding_right);
        mModalTitleTextPaddingBottom = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);

        //Product Title Text Size
        mProductTitleTextColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_title_text_color);
        mProductTitleTextSize = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_product_title_size);

        // Close Icon Style
        mCloseIconColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_title_text_color);
        mCloseIconHeight = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_close_icon_width_height);
        mCloseIconWidth = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_close_icon_width_height);
        mCloseIconPadding = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_close_icon_padding);

        //Product Pop Up Style
        mProductPopupBackgroundColor = getResources().getColor(com.tvpage.lib.R.color.default_popup_bg_color);
        mProductPopupBorderColor = getResources().getColor(com.tvpage.lib.R.color.default_popup_border_color);
        mProductPopupBorderRadius = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);
        mProductPopupBorderWidth = getResources().getDimension(com.tvpage.lib.R.dimen.default_product_popup_border_width);
        mProductPopupPadding = getResources().getDimension(com.tvpage.lib.R.dimen.default_product_popup_border_padding);
        mProductShadowColor = getResources().getColor(com.tvpage.lib.R.color.white);
        mProductPopupWidth = getResources().getDimension(com.tvpage.lib.R.dimen.default_product_popup_width);

        //Product Popup Price
        mProductPriceTextColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_price_text_color);
        mProductPriceTextSize = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_product_price_size);

        //Product Review
        mProductReviewTextColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_review_text_color);
        mProductReviewTextSize = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_product_review_size);

        //Product CTA Text Style
        mProductCTATextColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_cta_text_color);
        mProductCTATextSize = getResources().getDimension(com.tvpage.lib.R.dimen.default_modal_product_cta_text_size);

        //Product Popup Style
        mProductCTABackgroundColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_cta_bg_color);
        mProductCTABorderColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_cta_border_color);
        mProductCTABorderRadius = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);
        mProductCTABorderWidth = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);

        //Product Image Style
        mProductImageBorderColor = getResources().getColor(com.tvpage.lib.R.color.default_modal_product_cta_bg_color);
        mProductImageBorderWidth = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);
        mProductImageBorderSize = getResources().getDimension(com.tvpage.lib.R.dimen.common_0_dp);*/
    }

    /**
     * init view
     */
    private void init() {
        tvPagePlayer = (TvPagePlayer) findViewById(com.tvpage.lib.R.id.tvPagePlayer);
        initVideos();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Init.
     */
    void initVideos() {

        //get data from parcable objects
//        tvPagePlayer.clearPrefOfVideoUrlsAndVideoPostion();
        tvPagePlayer.onResetPlayer();

        // load loop for auto next change video
        /*if (list != null && list.size() > 0
                && list.size() >= (position + 1)
                && (mCounterVideoStart == 0 || isAutoVideoNext)) {

            try {*/
//        Gson gson = new Gson();
//        jsonObjectToPass = gson.toJson(list.get(position));

//        if (list.get(position).getTitle() != null) {
//                    titleIntent = tvPageVideoModel.getTitle();
//            titleIntent = list.get(position).getTitle();
//
//        }

       /* if (list.get(position).getId() != null) {
//                    idIntent = tvPageVideoModel.getId();
            idIntent = list.get(position).getId();
        }

        if (list.get(position).getDate_created() != null) {
//                    video_dateIntent = tvPageVideoModel.getDate_created();
            video_dateIntent = list.get(position).getDate_created();
        }

        if (list.get(position).getDescription() != null) {
//                    video_descIntent = tvPageVideoModel.getDescription();
            video_descIntent = list.get(position).getDescription();
        }

        if (list.get(position).getEntityIdParent() != null) {
//                    video_entityIdAsChannelIdIntent = tvPageVideoModel.getEntityIdParent();
            video_entityIdAsChannelIdIntent = list.get(position).getEntityIdParent();
        }*/


//                TvPageVideoModel.Asset asset = tvPageVideoModel.getAsset();
       /* TvPageVideoModel.Asset asset = list.get(position).getAsset();


        if (asset != null) {

            if (asset.getPrettyDuration() != null) {
                video_durationIntent = asset.getPrettyDuration();
            }
            if (asset.getType() != null) {
                typeIntent = asset.getType();
            }

            if (asset.getVideoId() != null) {
                video_idIntent = asset.getVideoId();
            }


            //Manage SOurces (File & Quality)

            if (asset.getSources() != null && asset.getSources().size() > 0) {

                for (int j = 0; j < asset.getSources().size(); j++) {
                    if (asset.getSources().get(j).getQuality() != null && asset.getSources().get(j).getFile() != null) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(asset.getSources().get(j).getQuality(), asset.getSources().get(j).getFile());
                        qualityListIntent.add(hashMap);
                    }
                }
            }
        }*/

        JSONObject jsonObjectTemp = null;
        jsonObjectTemp = getJsonForControls();
        if (jsonObjectTemp != null) {
            jsonControlsToPass = jsonObjectTemp;
        }


        //builder
        TvPageBuilder tvPageBuilder = new TvPageBuilder(tvPagePlayer).
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
                        //TvPagePlayer video completed call back
//                        Log.d(TAG, "OnMediaComplete: ");
                        // check is auto next
                        if (isAutoVideoNext) {
                            // increment video count position
                            position = position + 1;
                            // check if last video play than start video again from first position
                            if (position == list.size()) {
                                position = 0;
                            }
                            // load video again
                            initVideos();
                        }
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
                .controls(jsonControlsToPass)
                .size(0, 0).initialise();

        //Update Counter for avoiding video is first time load or not
        mCounterVideoStart = mCounterVideoStart + 1;

        //check is auto play or not
        if (isAutoVideoPlay) {
            setAutoPlayVideo();
        } else {
            setAutoQueVideo();
        }

        //Call Listing API
//                callAPIProductListing();
            /*} catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    //get json control for player seekbar color

    public JSONObject getJsonForControls() {
        JSONObject parent = new JSONObject();
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
            return null;
        }

        return parent;
    }

    /**
     * load video in queue
     */
    public void setAutoQueVideo() {
        try {
            //set urls..load urls
            /*if (typeIntent.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
                //load youtube urls
                tvPagePlayer.cueVideo(YOUTUBE_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
                //load vimeo urls
                tvPagePlayer.cueVideo(VIMEO_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
                //load normal urls*/
            tvPagePlayer.cueVideo("", jsonObjectToPass);
            /*} else {
                //load normal urls
                tvPagePlayer.cueVideo("", jsonObjectToPass);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * play video
     */
    public void setAutoPlayVideo() {
        try {
            //set urls..load urls
            /*if (typeIntent.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
                //load youtube urls
                tvPagePlayer.loadVideo(YOUTUBE_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
                //load vimeo urls
                tvPagePlayer.loadVideo(VIMEO_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
                //load normal urls*/
            tvPagePlayer.loadVideo("", jsonObjectToPass);
           /* } else {
                //load normal urls
                tvPagePlayer.loadVideo("", jsonObjectToPass);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TvPagePlayer getWidget() {
        return (TvPagePlayer) findViewById(R.id.tvPagePlayer);
    }
}
