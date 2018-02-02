package com.tvpage.lib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tvpage.lib.R;
import com.tvpage.lib.adapter.ProductAdapter;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.fragments.BaseFragment;
import com.tvpage.lib.model.TvPageProductModel;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;
import com.tvpage.lib.utils.Constants;
import com.tvpage.lib.utils.TvPageInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static com.tvpage.lib.utils.TvPageUtils.NORMAL_TVPAGE_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_VIDEO_TYPE;

/**
 */

public class VideoDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = VideoDetailFragment.class.getSimpleName();

    private static Context mContext;
    private TextView mTxtVideoTitle;
    private ImageView mImgClose;
    private RecyclerView mRvProducts;
    private ProgressBar mPbLoaderProducts;
    private TvPagePlayer tvPagePlayer;
    private ProductAdapter mProductAdapter;
    private View viewShadow;
    TvPageVideoModel tvPageVideoModel;

    private LinearLayout linear_parent_detail;
    private List<TvPageProductModel> mProductList = new ArrayList<>();


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

    private String screenDeterminationValueIntent = "";

    ArrayList<HashMap<String, String>> qualityListIntent = new ArrayList<HashMap<String, String>>();


    private JSONObject jsonControlsToPass = null;
    private String jsonObjectToPass = "";
    private LinearLayout llModalBackground;
    private CardView cvModalBackGround;

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

    //default Modal shadow visibility
    private String mModalShadowVisibility = "0";

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

    public static VideoDetailFragment getInstance(Bundle bundle, Context context,
                                                  List<TvPageVideoModel> dataList) {
        VideoDetailFragment videoDetailFragment1 = new VideoDetailFragment();
        videoDetailFragment1.setArguments(bundle);
        list = new ArrayList<>();
        list.addAll(dataList);
        mContext = context;
        return videoDetailFragment1;
    }

    public static VideoDetailFragment getInstance(Bundle bundle, Context context) {
        VideoDetailFragment videoDetailFragment1 = new VideoDetailFragment();
        videoDetailFragment1.setArguments(bundle);
        mContext = context;
        return videoDetailFragment1;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest_video_details, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        setDefaultValue();

    }

    private void setDefaultValue() {
        //Modal Background Style
        mModalBackGroundColor = getResources().getColor(R.color.white);
        mModalShadowColor = getResources().getColor(R.color.default_title_text_color);
        mModalBorderColor = getResources().getColor(R.color.default_title_text_color);
        mModalBodyPadding = getResources().getDimension(R.dimen.default_items_title_padding);
        mModalBorderWidth = getResources().getDimension(R.dimen.default_items_title_padding);

        //Title Text Style
        mModalTitleTextColor = getResources().getColor(R.color.default_modal_title_text_color);
        mModalTitleTextSize = getResources().getDimension(R.dimen.default_modal_title_text_size);
        mModalTitleTextPaddingTop = getResources().getDimension(R.dimen.default_modal_title_padding_top);
        mModalTitleTextPaddingLeft = getResources().getDimension(R.dimen.default_modal_title_padding_right_left);
        mModalTitleTextPaddingRight = getResources().getDimension(R.dimen.default_modal_title_padding_right);
        mModalTitleTextPaddingBottom = getResources().getDimension(R.dimen.common_0_dp);

        //Product Title Text Size
        mProductTitleTextColor = getResources().getColor(R.color.default_modal_product_title_text_color);
        mProductTitleTextSize = getResources().getDimension(R.dimen.default_modal_product_title_size);

        // Close Icon Style
        mCloseIconColor = getResources().getColor(R.color.default_modal_title_text_color);
        mCloseIconHeight = getResources().getDimension(R.dimen.default_modal_close_icon_width_height);
        mCloseIconWidth = getResources().getDimension(R.dimen.default_modal_close_icon_width_height);
        mCloseIconPadding = getResources().getDimension(R.dimen.default_modal_close_icon_padding);

        //Product Pop Up Style
        mProductPopupBackgroundColor = getResources().getColor(R.color.default_popup_bg_color);
        mProductPopupBorderColor = getResources().getColor(R.color.default_popup_border_color);
        mProductPopupBorderRadius = getResources().getDimension(R.dimen.common_0_dp);
        mProductPopupBorderWidth = getResources().getDimension(R.dimen.default_product_popup_border_width);
        mProductPopupPadding = getResources().getDimension(R.dimen.default_product_popup_border_padding);
        mProductShadowColor = getResources().getColor(R.color.white);
        mProductPopupWidth = getResources().getDimension(R.dimen.default_product_popup_width);

        //Product Popup Price
        mProductPriceTextColor = getResources().getColor(R.color.default_modal_product_price_text_color);
        mProductPriceTextSize = getResources().getDimension(R.dimen.default_modal_product_price_size);

        //Product Review
        mProductReviewTextColor = getResources().getColor(R.color.default_modal_product_review_text_color);
        mProductReviewTextSize = getResources().getDimension(R.dimen.default_modal_product_review_size);

        //Product CTA Text Style
        mProductCTATextColor = getResources().getColor(R.color.default_modal_product_cta_text_color);
        mProductCTATextSize = getResources().getDimension(R.dimen.default_modal_product_cta_text_size);

        //Product Popup Style
        mProductCTABackgroundColor = getResources().getColor(R.color.default_modal_product_cta_bg_color);
        mProductCTABorderColor = getResources().getColor(R.color.default_modal_product_cta_border_color);
        mProductCTABorderRadius = getResources().getDimension(R.dimen.common_0_dp);
        mProductCTABorderWidth = getResources().getDimension(R.dimen.common_0_dp);

        //Product Image Style
        mProductImageBorderColor = getResources().getColor(R.color.default_modal_product_cta_bg_color);
        mProductImageBorderWidth = getResources().getDimension(R.dimen.common_0_dp);
        mProductImageBorderSize = getResources().getDimension(R.dimen.common_0_dp);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            this.mContext = getActivity();
        }

        //get Bundle Data
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey(Constants.BUNDLE_KEY_VIDEO_DETAIL_DATA_KEY)) {
                tvPageVideoModel = b.getParcelable(Constants.BUNDLE_KEY_VIDEO_DETAIL_DATA_KEY);
            }
            if (b.containsKey(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_DETERMINATION_KEY)) {
                screenDeterminationValueIntent = b.getString(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_DETERMINATION_KEY);
            }

//            if (b.containsKey(Constants.BUNDLE_KEY_VIDEO_LIST)) {
//                list = b.getParcelableArrayList(Constants.BUNDLE_KEY_VIDEO_LIST);
//            }

            if (b.containsKey(Constants.BUNDLE_KEY_VIDEO_POSITION)) {
                position = b.getInt(Constants.BUNDLE_KEY_VIDEO_POSITION);
            }
        }

        init();
    }


    /**
     * init view
     */
    private void init() {
        mTxtVideoTitle = (TextView) rootView.findViewById(R.id.txtVideoTitle);
        mImgClose = (ImageView) rootView.findViewById(R.id.imgClose);
        mRvProducts = (RecyclerView) rootView.findViewById(R.id.rvProducts);
        mPbLoaderProducts = (ProgressBar) rootView.findViewById(R.id.pbLoaderProducts);
        tvPagePlayer = (TvPagePlayer) rootView.findViewById(R.id.tvPagePlayer);

        linear_parent_detail = (LinearLayout) rootView.findViewById(R.id.linear_parent_detail);
        llModalBackground = rootView.findViewById(R.id.llModalBackground);
        viewShadow = rootView.findViewById(R.id.viewShadow);
//        cvModalBackGround = rootView.findViewById(R.id.cvModalBackGround);


        //set layout manager to recycler list
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(HORIZONTAL);
        mRvProducts.setLayoutManager(manager);

        //set click listener
        linear_parent_detail.setOnClickListener(this);
        mImgClose.setOnClickListener(this);


        initVideos();

        //Set Modal Background Style
        setModalBackGroundStyle();

        //Set Modal Title Style
        setModalTitleStyle();

        //Set Close Icon Style
        setCloseIconStyle();
    }


    /**
     * Init.
     */
    void initVideos() {

        //get data from parcable objects
        //tvPagePlayer.clearPrefOfVideoUrlsAndVideoPostion();
        tvPagePlayer.onResetPlayer();

        // load loop for auto next change video
        if (list != null && list.size() > 0
                && list.size() >= (position + 1)
                && (mCounterVideoStart == 0 || isAutoVideoNext)) {

            try {
                Gson gson = new Gson();
                jsonObjectToPass = gson.toJson(list.get(position));

                if (list.get(position).getTitle() != null) {
//                    titleIntent = tvPageVideoModel.getTitle();
                    titleIntent = list.get(position).getTitle();

                }

                if (list.get(position).getId() != null) {
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
                }


                //set title
                mTxtVideoTitle.setText(titleIntent);

//                TvPageVideoModel.Asset asset = tvPageVideoModel.getAsset();
                TvPageVideoModel.Asset asset = list.get(position).getAsset();


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
                }

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
                callAPIProductListing();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * load video in queue
     */
    private void setAutoQueVideo() {
        try {
            //set urls..load urls
            if (typeIntent.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
                //load youtube urls
                tvPagePlayer.cueVideo(YOUTUBE_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
                //load vimeo urls
                tvPagePlayer.cueVideo(VIMEO_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
                //load normal urls
                tvPagePlayer.cueVideo("", jsonObjectToPass);
            } else {
                //load normal urls
                tvPagePlayer.cueVideo("", jsonObjectToPass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * play video
     */
    private void setAutoPlayVideo() {
        try {
            //set urls..load urls
            if (typeIntent.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
                //load youtube urls
                tvPagePlayer.loadVideo(YOUTUBE_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
                //load vimeo urls
                tvPagePlayer.loadVideo(VIMEO_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
                //load normal urls
                tvPagePlayer.loadVideo("", jsonObjectToPass);
            } else {
                //load normal urls
                tvPagePlayer.loadVideo("", jsonObjectToPass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //get json control for player seekbar color

    private JSONObject getJsonForControls() {
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

    @Override
    public void onClick(View v) {
        String tag = null;
        try {
            // Get TAG from view
            tag = (String) v.getTag();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (tag) {
            case "close":
                closeIconClick();
                break;
            case "linear_parent_detail":
                break;
            default:
                break;
        }
    }

    //perform close button click
    private void closeIconClick() {
        if (screenDeterminationValueIntent.equalsIgnoreCase(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_SIDEBAR_VALUE)) {
            TvPageSideBarVideoWidget parentFragSideBarLatestVideoWidget = ((TvPageSideBarVideoWidget) VideoDetailFragment.this.getParentFragment());
            parentFragSideBarLatestVideoWidget.doPopBackStack();
        } else if (screenDeterminationValueIntent.equalsIgnoreCase(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_CAROUSEL_VALUE)) {
            TvPageCarouselVideoWidget parentFragCarouselLatestVideoWidget = ((TvPageCarouselVideoWidget) VideoDetailFragment.this.getParentFragment());
            parentFragCarouselLatestVideoWidget.doPopBackStack();
        } else if (screenDeterminationValueIntent.equalsIgnoreCase(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_SOLO_VALUE)) {
            TvPageSoloWidget parentFragCarouselLatestVideoWidget = ((TvPageSoloWidget) VideoDetailFragment.this.getParentFragment());
            parentFragCarouselLatestVideoWidget.doPopBackStack();
        } else if (screenDeterminationValueIntent.equalsIgnoreCase(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_VIDEO_GALLERY)) {
            TvPageChannelListFragment parentFragCarouselLatestVideoWidget = ((TvPageChannelListFragment) VideoDetailFragment.this.getParentFragment());
            parentFragCarouselLatestVideoWidget.doPopBackStack();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (tvPagePlayer != null) {
            tvPagePlayer.onSaveInstance();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tvPagePlayer != null) {
            tvPagePlayer.onRestoreInstance();
        }
    }


    // Api to fetch product list
    private void callAPIProductListing() {
        //call api for product
        //idIntent mean its video id
        if (CommonUtils.isInternetConnected(getActivity())) {
            tvPagePlayer.tvPageGetVideoProductExtractor(idIntent, new OnTvPageResponseApiListener() {
                @Override
                public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                    mProductList.clear();

                    if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                        try {
                            JSONArray jsonArray = tvPageResponseModel.getJsonArray();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                TvPageProductModel productModel = new TvPageProductModel();

                                //reset variables for
                                String product_id = "";
                                String channel_id = "";
                                String video_id = "";

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //TvPageVideoProductModel tvPageModel = new TvPageVideoProductModel();
                                if (!jsonObject.isNull("title")) {
                                    String title = jsonObject.getString("title");
                                    productModel.setTitle_product(title);
                                }
                                if (!jsonObject.isNull("imageUrl")) {
                                    String imageUrl = jsonObject.getString("imageUrl");
                                    productModel.setImg_product(imageUrl);
                                }
                                if (!jsonObject.isNull("price")) {
                                    String price_sale = jsonObject.getString("price");
                                    productModel.setPrice_product(price_sale);
                                }
                                if (!jsonObject.isNull("linkUrl")) {
                                    String linkUrl = jsonObject.getString("linkUrl");
                                    productModel.setProduct_info_url(linkUrl);
                                }
                                if (!jsonObject.isNull("id")) {
                                    product_id = jsonObject.getString("id");
                                    productModel.setProduct_id(product_id);
                                }
                                if (!jsonObject.isNull("entityIdParent")) {
                                    channel_id = jsonObject.getString("entityIdParent");
                                    productModel.setProduct_channel_id(channel_id);
                                }
                                if (!jsonObject.isNull("entityIdChild")) {
                                    video_id = jsonObject.getString("entityIdChild");
                                    productModel.setProduct_video_id(video_id);
                                }


                                /*"id":"83102610",                      = Product ID
                                "entityIdParent":"83094490", = Channel Id
                                "entityIdChild":"83102610",   = Video Id*/


                                mProductList.add(productModel);


//                            //call api for Product Impression
//                            if (tvPagePlayer != null) {
//                                if (product_id != null && !TextUtils.isEmpty(product_id)
//                                        && channel_id != null && !TextUtils.isEmpty(channel_id)
//                                        && video_id != null && !TextUtils.isEmpty(video_id)
//                                        ) {
//                                    tvPagePlayer.analyticsProductImpressionApi(Integer.parseInt(channel_id),
//                                            Integer.parseInt(video_id), Integer.parseInt(product_id));
//                                }
//                            }

                            }
                            setDataInToView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    private void setDataInToView() {
        Log.d(TAG, "setDataInToView: " + mProductList.size());
        mProductAdapter = new ProductAdapter(mContext, mProductList);
        mProductAdapter.setProductTitleGravity(mProductTitleGravity);
        mProductAdapter.setProductTitleStyle(mProductTitleStyle);
        mProductAdapter.setProductTitleTextColor(mProductTitleTextColor);
        mProductAdapter.setProductTitleTextSize(mProductTitleTextSize);
        mProductAdapter.setProductTitleTypeFace(mProductTitleTypeFace);

        mProductAdapter.setProductPopupBackgroundColor(mProductPopupBackgroundColor);
        mProductAdapter.setProductPopupBorderColor(mProductPopupBorderColor);
        mProductAdapter.setProductPopupBorderWidth(mProductPopupBorderWidth);
        mProductAdapter.setProductPopupPadding(mProductPopupPadding);
        mProductAdapter.setProductPopupBorderRadius(mProductPopupBorderRadius);
        mProductAdapter.setProductShadowColor(mProductShadowColor);
        mProductAdapter.setProductShadowVisiblity(mProductShadowVisibility);

        mProductAdapter.setProductPriceGravity(mProductPriceGravity);
        mProductAdapter.setProductPriceStyle(mProductPriceStyle);
        mProductAdapter.setProductPriceTextColor(mProductPriceTextColor);
        mProductAdapter.setProductPriceTextSize(mProductPriceTextSize);
        mProductAdapter.setProductPriceTypeFace(mProductPriceTypeFace);

        mProductAdapter.setProductReviewTextColor(mProductReviewTextColor);
        mProductAdapter.setProductReviewTextSize(mProductReviewTextSize);
        mProductAdapter.setProductReviewGravity(mProductReviewGravity);
        mProductAdapter.setProductReviewStyle(mProductReviewStyle);
        mProductAdapter.setProductReviewTypeFace(mProductReviewTypeFace);

        mProductAdapter.setProductCTATextColor(mProductCTATextColor);
        mProductAdapter.setProductCTATextSize(mProductCTATextSize);
        mProductAdapter.setProductCTAGravity(mProductCTAGravity);
        mProductAdapter.setProductCTAStyle(mProductCTAStyle);
        mProductAdapter.setProductCTATypeFace(mProductCTATypeFace);
        mProductAdapter.setProductCTATextTransformation(mProductCTATextTransformation);
        mProductAdapter.setProductCTAText(mProductCTAText);

        mProductAdapter.setProductCTABackgroundColor(mProductCTABackgroundColor);
        mProductAdapter.setProductCTABorderColor(mProductCTABorderColor);
        mProductAdapter.setProductCTABorderWidth(mProductCTABorderWidth);
        mProductAdapter.setProductCTABorderRadius(mProductCTABorderRadius);

        mProductAdapter.setProductImageBorderColor(mProductImageBorderColor);
        mProductAdapter.setProductImageBorderSize(mProductImageBorderSize);
        mProductAdapter.setProductImageBorderWidth(mProductImageBorderWidth);

        mProductAdapter.setProductImageOverLayColo(mProductImageOverLayRed, mProductImageOverLayGreen,
                mProductImageOverLayBlue, mProductImageOverLayAlpha);

        mProductAdapter.setProductPopupWidth(mProductPopupWidth);

        mRvProducts.setAdapter(mProductAdapter);
        mPbLoaderProducts.setVisibility(View.GONE);
    }

    /**
     * Set Modal Back Ground Style
     */
    private void setModalBackGroundStyle() {
        LayerDrawable layerDrawable = (LayerDrawable) mContext.getResources()
                .getDrawable(R.drawable.bg_modal);
        //Shape Customization
        GradientDrawable topDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.topShape);
        topDrawable.setColor(mModalBackGroundColor);
        topDrawable.setStroke((int) mModalBorderWidth, mModalBorderColor);

        if (viewShadow != null) {
            GradientDrawable gdShadow = (GradientDrawable) viewShadow.getBackground();
            gdShadow.setColors(new int[]{
                    mContext.getResources().getColor(android.R.color.transparent), mModalShadowColor});
            viewShadow.setBackground(gdShadow);
            viewShadow.setVisibility(CommonUtils.getVisibility(mModalShadowVisibility));
        }

        if (llModalBackground != null) {
            int padding = (int) mModalBodyPadding;
            llModalBackground.setPadding(padding, padding, padding, padding);
            llModalBackground.setBackground(layerDrawable);
        }

    }

    /**
     * Set Modal Title Style
     */
    private void setModalTitleStyle() {
        if (mTxtVideoTitle != null) {
            mTxtVideoTitle.setTextSize(mModalTitleTextSize);

            mTxtVideoTitle.setTextColor(mModalTitleTextColor);

            mTxtVideoTitle.setPadding((int) mModalTitleTextPaddingLeft,
                    (int) mModalTitleTextPaddingTop,
                    (int) mModalTitleTextPaddingRight,
                    (int) mModalTitleTextPaddingBottom);

            mTxtVideoTitle.setTypeface(
                    CommonUtils.getTypeFaceFromFontPath(mContext,
                            mModalTitleTypeFace,
                            DEFAULT_ITEMS_TITLE_TYPE_FACE));

            mTxtVideoTitle.setTypeface(mTxtVideoTitle.getTypeface(),
                    CommonUtils.getTextStyle(mModalTitleStyle));

            mTxtVideoTitle.setGravity(CommonUtils.getGravity(mModalTitleGravity));
        }
    }

    private void setCloseIconStyle() {
        int padding = (int) mCloseIconPadding;
        if (mImgClose != null) {
            mImgClose.setPadding(padding, padding, padding, padding);
            mImgClose.setBackground(CommonUtils.changeDrawableColor(mImgClose.getBackground(), mCloseIconColor));
        }
    }


    public void setModalBackGroundColor(int mModalBackGroundColor) {
        this.mModalBackGroundColor = mModalBackGroundColor;
        setModalBackGroundStyle();
    }

    public void setModalShadowColor(int mModalShadowColor) {
        this.mModalShadowColor = mModalShadowColor;
        setModalBackGroundStyle();
    }

    public void setModalBorderColor(int mModalBorderColor) {
        this.mModalBorderColor = mModalBorderColor;
        setModalBackGroundStyle();
    }

    public void setModalBorderWidth(float mModalBorderWidth) {
        this.mModalBorderWidth = mModalBorderWidth;
        setModalBackGroundStyle();
    }

    public void setModalBodyPadding(float mModalBodyPadding) {
        this.mModalBodyPadding = mModalBodyPadding;
        setModalBackGroundStyle();
    }


    public void setModalTitleTextColor(int mModalTitleTextColor) {
        this.mModalTitleTextColor = mModalTitleTextColor;
        setModalTitleStyle();
    }

    public void setModalTitleTextSize(float mModalTitleTextSize) {
        this.mModalTitleTextSize = mModalTitleTextSize;
        setModalTitleStyle();
    }

    public void setModalTitleTextPaddingTop(float mModalTitleTextPaddingTop) {
        this.mModalTitleTextPaddingTop = mModalTitleTextPaddingTop;
        setModalTitleStyle();
    }

    public void setModalTitleTextPaddingLeft(float mModalTitleTextPaddingLeft) {
        this.mModalTitleTextPaddingLeft = mModalTitleTextPaddingLeft;
        setModalTitleStyle();
    }

    public void setModalTitleTextPaddingRight(float mModalTitleTextPaddingRight) {
        this.mModalTitleTextPaddingRight = mModalTitleTextPaddingRight;
        setModalTitleStyle();
    }

    public void setModalTitleTextPaddingBottom(float mModalTitleTextPaddingBottom) {
        this.mModalTitleTextPaddingBottom = mModalTitleTextPaddingBottom;
        setModalTitleStyle();
    }

    public void setModalTitleGravity(String mModalTitleGravity) {
        this.mModalTitleGravity = mModalTitleGravity;
        setModalTitleStyle();
    }

    public void setModalTitleStyle(String mModalTitleStyle) {
        this.mModalTitleStyle = mModalTitleStyle;
        setModalTitleStyle();
    }

    public void setModalTitleTypeFace(String mModalTitleTypeFace) {
        this.mModalTitleTypeFace = mModalTitleTypeFace;
        setModalTitleStyle();
    }

    public void setProductTitleTextColor(int mProductTitleTextColor) {
        this.mProductTitleTextColor = mProductTitleTextColor;
        if (mProductAdapter != null) {
            mProductAdapter.setProductTitleTextColor(mProductTitleTextColor);
        }
    }

    public void setProductTitleTextSize(float mProductTitleTextSize) {
        this.mProductTitleTextSize = mProductTitleTextSize;
        if (mProductAdapter != null) {
            mProductAdapter.setProductTitleTextColor(mProductTitleTextColor);
        }
    }

    public void setProductTitleGravity(String mProductTitleGravity) {
        this.mProductTitleGravity = mProductTitleGravity;
        if (mProductAdapter != null) {
            mProductAdapter.setProductTitleGravity(mProductTitleGravity);
        }
    }

    public void setProductTitleStyle(String mProductTitleStyle) {
        this.mProductTitleStyle = mProductTitleStyle;
        if (mProductAdapter != null) {
            mProductAdapter.setProductTitleStyle(mProductTitleStyle);
        }
    }

    public void setProductTitleTypeFace(String mProductTitleTypeFace) {
        this.mProductTitleTypeFace = mProductTitleTypeFace;
        if (mProductAdapter != null) {
            mProductAdapter.setProductTitleTypeFace(mProductTitleTypeFace);
        }
    }

    public void setCloseIconColor(int mCloseIconColor) {
        this.mCloseIconColor = mCloseIconColor;
        setCloseIconStyle();
    }

    public void setCloseIconWidth(float mCloseIconWidth) {
        this.mCloseIconWidth = mCloseIconWidth;
    }

    public void setCloseIconHeight(float mCloseIconHeight) {
        this.mCloseIconHeight = mCloseIconHeight;
    }

    public void setCloseIconPadding(float mCloseIconPadding) {
        this.mCloseIconPadding = mCloseIconPadding;
        setCloseIconStyle();
    }

    public void setProductPopupBackgroundColor(int mProductPopupBackgroundColor) {
        this.mProductPopupBackgroundColor = mProductPopupBackgroundColor;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPopupBackgroundColor(mProductPopupBackgroundColor);
        }
    }

    public void setProductPopupBorderColor(int mProductPopupBorderColor) {
        this.mProductPopupBorderColor = mProductPopupBorderColor;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPopupBorderColor(mProductPopupBorderColor);
        }
    }

    public void setProductPopupBorderWidth(float mProductPopupBorderWidth) {
        this.mProductPopupBorderWidth = mProductPopupBorderWidth;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPopupBorderWidth(mProductPopupBorderWidth);
        }
    }

    public void setProductPopupPadding(float mProductPopupPadding) {
        this.mProductPopupPadding = mProductPopupPadding;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPopupPadding(mProductPopupPadding);
        }
    }

    public void setProductPopupBorderRadius(float mProductPopupBorderRadius) {
        this.mProductPopupBorderRadius = mProductPopupBorderRadius;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPopupBorderRadius(mProductPopupBorderRadius);
        }
    }

    public void setProductPriceTextColor(int mProductPriceTextColor) {
        this.mProductPriceTextColor = mProductPriceTextColor;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPriceTextColor(mProductPriceTextColor);
        }
    }

    public void setProductPriceTextSize(float mProductPriceTextSize) {
        this.mProductPriceTextSize = mProductPriceTextSize;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPriceTextSize(mProductPriceTextSize);
        }
    }

    public void setProductPriceGravity(String mProductPriceGravity) {
        this.mProductPriceGravity = mProductPriceGravity;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPriceGravity(mProductPriceGravity);
        }
    }

    public void setProductPriceStyle(String mProductPriceStyle) {
        this.mProductPriceStyle = mProductPriceStyle;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPriceStyle(mProductPriceStyle);
        }
    }

    public void setProductPriceTypeFace(String mProductPriceTypeFace) {
        this.mProductPriceTypeFace = mProductPriceTypeFace;
        if (mProductAdapter != null) {
            mProductAdapter.setProductPriceTypeFace(mProductPriceTypeFace);
        }
    }


    public void setProductReviewTextColor(int mProductReviewTextColor) {
        this.mProductReviewTextColor = mProductReviewTextColor;
    }

    public void setProductReviewTextSize(float mProductReviewTextSize) {
        this.mProductReviewTextSize = mProductReviewTextSize;
    }

    public void setProductReviewGravity(String mProductReviewGravity) {
        this.mProductReviewGravity = mProductReviewGravity;
    }

    public void setProductReviewStyle(String mProductReviewStyle) {
        this.mProductReviewStyle = mProductReviewStyle;
    }

    public void setProductReviewTypeFace(String mProductReviewTypeFace) {
        this.mProductReviewTypeFace = mProductReviewTypeFace;
    }

    public void setProductCTATextColor(int mProductCTATextColor) {
        this.mProductCTATextColor = mProductCTATextColor;
    }

    public void setProductCTATextSize(float mProductCTATextSize) {
        this.mProductCTATextSize = mProductCTATextSize;
    }

    public void setProductCTAGravity(String mProductCTAGravity) {
        this.mProductCTAGravity = mProductCTAGravity;
    }

    public void setProductCTAStyle(String mProductCTAStyle) {
        this.mProductCTAStyle = mProductCTAStyle;
    }

    public void setProductCTATypeFace(String mProductCTATypeFace) {
        this.mProductCTATypeFace = mProductCTATypeFace;
    }

    public void setProductCTAText(String mProductCTAText) {
        this.mProductCTAText = mProductCTAText;
    }

    public void setProductCTATextTransformation(String mProductCTATextTransformation) {
        this.mProductCTATextTransformation = mProductCTATextTransformation;
    }

    public void setProductCTABackgroundColor(int mProductCTABackgroundColor) {
        this.mProductCTABackgroundColor = mProductCTABackgroundColor;
    }

    public void setProductCTABorderColor(int mProductCTABorderColor) {
        this.mProductCTABorderColor = mProductCTABorderColor;
    }

    public void setProductCTABorderWidth(float mProductCTABorderWidth) {
        this.mProductCTABorderWidth = mProductCTABorderWidth;
    }

    public void setProductCTABorderRadius(float mProductCTABorderRadius) {
        this.mProductCTABorderRadius = mProductCTABorderRadius;
    }

    public void setProductImageBorderColor(int mProductImageBorderColor) {
        this.mProductImageBorderColor = mProductImageBorderColor;
    }

    public void setProductImageBorderWidth(float mProductImageBorderWidth) {
        this.mProductImageBorderWidth = mProductImageBorderWidth;
    }

    public void setProductImageBorderSize(float mProductImageBorderSize) {
        this.mProductImageBorderSize = mProductImageBorderSize;
    }


    public void setProductImageOverLayColor(int red, int green, int blue, int alpha) {
        mProductImageOverLayRed = red;
        mProductImageOverLayGreen = green;
        mProductImageOverLayBlue = blue;
        mProductImageOverLayAlpha = alpha;
    }

    public void setModalShadowVisibility(String visibility) {
        this.mModalShadowVisibility = visibility;
    }

    public void setProductShadowVisibility(String mProductShadowVisibility) {
        this.mProductShadowVisibility = mProductShadowVisibility;
    }

    public void setProductShadowColor(int mProductShadowColor) {
        this.mProductShadowColor = mProductShadowColor;
    }


    public void setAutoVideoPlay(boolean autoVideoPlay) {
        isAutoVideoPlay = autoVideoPlay;
    }

    public void setAutoVideoNext(boolean autoVideoNext) {
        isAutoVideoNext = autoVideoNext;
    }

    public void setProductPopupWidth(float mProductPopupWidth) {
        this.mProductPopupWidth = mProductPopupWidth;
    }
}
