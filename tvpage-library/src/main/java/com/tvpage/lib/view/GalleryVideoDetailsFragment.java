package com.tvpage.lib.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tvpage.lib.R;
import com.tvpage.lib.TvPageGalleryActivity;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.fragments.BaseFragment;
import com.tvpage.lib.model.ProductModel;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;
import com.tvpage.lib.utils.MyPreferences;
import com.tvpage.lib.utils.TvPageInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tvpage.lib.utils.CommonUtils.NUMBER_OF_RESULT_TO_RETURN;
import static com.tvpage.lib.utils.CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY;
import static com.tvpage.lib.utils.CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY;
import static com.tvpage.lib.utils.TvPageUtils.NORMAL_TVPAGE_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_VIDEO_TYPE;

/**
 */

public class GalleryVideoDetailsFragment extends BaseFragment implements View.OnClickListener, ItemClickListener {
    private RecyclerView horizontalRecyclerView;
    private HorizontalAdapter adapter;
    private ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
    private TextView txtVideoTitle;
    private TextView tvNoProduct;


    private TvPagePlayer tvPagePlayer;
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

    public ImageView ib_drawer;
    RelativeLayout ib_back;

    ImageView image_pin;

    TextView tvDescription;
    TextView tvDuration;
    TextView tvPublishedDate;

    ImageView ib_drawers_temp;

    ImageView imgHeaderLogo;

    TvPageVideoModel tvPageVideoModelFromIntent;

    String jsonObjectToPass = "";

    JSONObject jsonControlsToPass = null;

    RecyclerView recyclerVideos;

    int pageNumberFroPagination = 0;

    TextView tvLoadmore;
    RelativeLayout relLoadMore;

    //ProgressDialog progressDialogBottomList;
    List<TvPageVideoModel> list = new ArrayList<TvPageVideoModel>();
    VideoAdapter videoAdapter;

    String sourceDeterminationIntent = "";

    RelativeLayout relBottomVideoListNew;
    String videoDetailsScreen = "";


    public static GalleryVideoDetailsFragment getInstance(Bundle bundle) {
        GalleryVideoDetailsFragment videoDetailFragment1 = new GalleryVideoDetailsFragment();
        videoDetailFragment1.setArguments(bundle);
        return videoDetailFragment1;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_detail, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        rootView = view;
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("ABC", "onItemClick: CALL");
        //get ineteht data
        Bundle b = getArguments();
        if (b != null) {

            if (b.containsKey(CommonUtils.PARCABLE_VIDEO_MODEL_KEY)) {
                tvPageVideoModelFromIntent = b.getParcelable(CommonUtils.PARCABLE_VIDEO_MODEL_KEY);
            }

            if (b.containsKey("channel_id")) {
                idChannelForVideosIntent = b.getString("channel_id");
            }

            if (b.containsKey(VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY)) {
                sourceDeterminationIntent = b.getString(VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY);
            }

            if (b.containsKey(VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY)) {
                sourceDeterminationIntent = b.getString(VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY);
            }

            if (b.containsKey(CommonUtils.VIDEO_DETAIL_OPEN_SCREEN)) {
                videoDetailsScreen = b.getString(CommonUtils.VIDEO_DETAIL_OPEN_SCREEN);
            }


         /*   if (b.containsKey("title")) {
                titleIntent = b.getString("title");
                CommonUtils.sout("t " + titleIntent);
            }
            if (b.containsKey("id")) {
                idIntent = b.getString("id");
                CommonUtils.sout("id " + idIntent);
            }
            if (b.containsKey("type")) {
                typeIntent = b.getString("type");
                CommonUtils.sout("typ " + typeIntent);
            }
            if (b.containsKey("video_id")) {
                video_idIntent = b.getString("video_id");
                CommonUtils.sout("vv " + video_idIntent);
            }
            if (b.containsKey("video_desc")) {
                video_descIntent = b.getString("video_desc");

            }
            if (b.containsKey("video_duration")) {
                video_durationIntent = b.getString("video_duration");

            }
            if (b.containsKey("video_date")) {
                video_dateIntent = b.getString("video_date");

            }
            if (b.containsKey("entity_id_parent")) {
                video_entityIdAsChannelIdIntent = b.getString("entity_id_parent");

            }
            if (b.containsKey("quality_urls")) {
                qualityListIntent = (ArrayList<HashMap<String, String>>) b.getSerializable("quality_urls");

            }

            if (b.containsKey("videoModel_paracalable")) {
                tvPageVideoModelFromIntent = b.getParcelable("videoModel_paracalable");
            }*/
        }
        init(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        //handle back button press for child fragment (Video Details)
        //pop detail screen from stack
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    FragmentManager fragmentManager = GalleryVideoDetailsFragment.this.getChildFragmentManager();
//                    if (fragmentManager != null && fragmentManager.getBackStackEntryCount() >= 1) {
//                        fragmentManager.popBackStack();
//                        return true;
//                    }
//
//
//                }
//                return false;
//            }
//        });
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
    }

    /**
     * Init.
     */

    void init(boolean isFromBottomList) {

        //get data from parcable objects

        if (tvPageVideoModelFromIntent != null) {
            try {
                Gson gson = new Gson();
                jsonObjectToPass = gson.toJson(tvPageVideoModelFromIntent);

                if (tvPageVideoModelFromIntent.getTitle() != null) {
                    titleIntent = tvPageVideoModelFromIntent.getTitle();

                }

                if (tvPageVideoModelFromIntent.getId() != null) {
                    idIntent = tvPageVideoModelFromIntent.getId();
                }

                if (tvPageVideoModelFromIntent.getDate_created() != null) {
                    video_dateIntent = tvPageVideoModelFromIntent.getDate_created();
                }

                if (tvPageVideoModelFromIntent.getDescription() != null) {
                    video_descIntent = tvPageVideoModelFromIntent.getDescription();
                }

                if (tvPageVideoModelFromIntent.getEntityIdParent() != null) {
                    video_entityIdAsChannelIdIntent = tvPageVideoModelFromIntent.getEntityIdParent();
                }

                TvPageVideoModel.Asset asset = tvPageVideoModelFromIntent.getAsset();


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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        horizontalRecyclerView = (RecyclerView) rootView.findViewById(R.id.horizontal_recyclerview);
        txtVideoTitle = (TextView) rootView.findViewById(R.id.txtVideoTitle);

        relBottomVideoListNew = (RelativeLayout) rootView.findViewById(R.id.relBottomVideoListNew);

        tvLoadmore = (TextView) rootView.findViewById(R.id.tvLoadmore);
        relLoadMore = (RelativeLayout) rootView.findViewById(R.id.relLoadMore);


        tvPagePlayer = (TvPagePlayer) rootView.findViewById(R.id.tvPagePlayer);
        tvNoProduct = (TextView) rootView.findViewById(R.id.tvNoProduct);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        tvDuration = (TextView) rootView.findViewById(R.id.tvDuration);
        tvPublishedDate = (TextView) rootView.findViewById(R.id.tvPublishedDate);

        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);
        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);
        image_pin = (ImageView) rootView.findViewById(R.id.image_pin);
        ib_drawers_temp = (ImageView) rootView.findViewById(R.id.ib_drawers_temp);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);
        recyclerVideos = (RecyclerView) rootView.findViewById(R.id.recyclerVideos);

        ib_drawer.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        image_pin.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);
        tvLoadmore.setOnClickListener(this);

        LinearLayoutManager vertLinearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(vertLinearLayoutManager);

        setTitle();


        if (sourceDeterminationIntent.equalsIgnoreCase(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_DETAIL_VALUE)
                || sourceDeterminationIntent.equalsIgnoreCase(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_LIST_VALUE)) {
            //need to show list
            relBottomVideoListNew.setVisibility(View.VISIBLE);

            if (!isFromBottomList) {
                callChannelVideoListApi(false);
            }

        } else {
            //hide list layout & dont' call api
            relBottomVideoListNew.setVisibility(View.GONE);
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
                    }
                }).
                setOnVideoViewError(new TvPageInterfaces.OnVideoViewError() {
                    @Override
                    public void OnVideoViewError(String error) {
                        //TvPagePlayer Error call back
                    }
                }).
                setOnMediaError(new TvPageInterfaces.OnMediaError() {
                    @Override
                    public void OnMediaError(String error) {
                        //TvPagePlayer media Error call back
                    }
                })
                .setOnMediaReady(new TvPageInterfaces.OnMediaReady() {
                    @Override
                    public void OnMediaReady(boolean isMediaReady) {
                        //TvPagePlayer video ready call back
                    }
                }).setOnMediaComplete(new TvPageInterfaces.OnMediaComplete() {
                    @Override
                    public void OnMediaComplete(boolean isMediaCompleted) {
                        //TvPagePlayer video completed call back

                    }
                }).setOnVideoEnded(new TvPageInterfaces.OnVideoEnded() {
                    @Override
                    public void OnVideoEnded(boolean isVideoEnded) {
                        //TvPagePlayer video end call back
                    }
                }).setOnVideoPlaying(new TvPageInterfaces.OnVideoPlaying() {
                    @Override
                    public void OnVideoPlaying(boolean isVideoPlaying) {
                        //TvPagePlayer video playing call back
                    }
                }).setOnVideoPaused(new TvPageInterfaces.OnVideoPaused() {
                    @Override
                    public void OnVideoPaused(boolean isVideoPaused) {
                        //TvPagePlayer video paused call back
                    }
                }).setOnVideoBuffering(new TvPageInterfaces.OnVideoBuffering() {
                    @Override
                    public void OnVideoBuffering(boolean isVideoBuffering) {
                        //TvPagePlayer video buffering call back
                    }
                }).setOnMediaPlayBackQualityChanged(new TvPageInterfaces.OnMediaPlayBackQualityChanged() {
                    @Override
                    public void OnMediaPlayBackQualityChanged(String selectedQuality) {
                        //TvPagePlayer video quality changed call back
                    }
                }).setOnSeek(new TvPageInterfaces.OnSeek() {
                    @Override
                    public void OnSeek(String currentVideoTime) {
                        //TvPagePlayer seek call back
                    }
                }).setOnVideoLoad(new TvPageInterfaces.OnVideoLoad() {
                    @Override
                    public void OnVideoLoad(boolean isVideoLoaded) {
                        //TvPagePlayer Video loaded
                    }
                }).setOnVideoCued(new TvPageInterfaces.OnVideoCued() {
                    @Override
                    public void OnVideoCued(boolean isVideoLoaded) {
                        //TvPagePlayer Video cued
                    }
                }).setOnReady(new TvPageInterfaces.OnReady() {
                    @Override
                    public void OnPlayerReady() {
                        //TvPagePlayer Ready call back
                    }
                }).setOnStateChanged(new TvPageInterfaces.OnStateChanged() {
                    @Override
                    public void OnStateChanged() {
                        //TvPagePlayer state changed call back
                    }
                })
                .setOnError(new TvPageInterfaces.OnError() {
                    @Override
                    public void OnError() {
                        //TvPagePlayer error callback

                    }
                })
                .controls(jsonControlsToPass)
                .size(0, 0).initialise();


        horizontalRecyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);


        //call api for product
        //idIntent mean its video id
        if (CommonUtils.isInternetConnected(getActivity())) {
            tvPagePlayer.tvPageGetVideoProductExtractor(idIntent, new OnTvPageResponseApiListener() {
                @Override
                public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                    productList.clear();

                    if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                        try {
                            JSONArray jsonArray = tvPageResponseModel.getJsonArray();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                ProductModel productModel = new ProductModel();

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


                                productList.add(productModel);


                                //call api for Product Impression
                                if (tvPagePlayer != null) {
                                    if (product_id != null && !TextUtils.isEmpty(product_id)
                                            && channel_id != null && !TextUtils.isEmpty(channel_id)
                                            && video_id != null && !TextUtils.isEmpty(video_id)
                                            ) {
                                        tvPagePlayer.analyticsProductImpressionApi(Integer.parseInt(channel_id),
                                                Integer.parseInt(video_id), Integer.parseInt(product_id));
                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    setDataForProduct();

                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }


        //set title
        txtVideoTitle.setText(titleIntent);


        //set desc
        tvDescription.setText(video_descIntent);

        //set date
        if (video_dateIntent.trim().length() > 0) {
            String datefromTimestamp = CommonUtils.getDateFromTimestamp(Integer.parseInt(video_dateIntent) * 1000L, "MMMM dd, yyyy");
            if (datefromTimestamp != null) {
                tvPublishedDate.setText(datefromTimestamp);
            }
        } else {
            tvPublishedDate.setText(" - ");
        }


        //set duration
        tvDuration.setText(video_durationIntent);


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

    void showLoadmore(boolean isNedToShowLoadMore) {
        if (isNedToShowLoadMore) {
            relLoadMore.setVisibility(View.VISIBLE);
        } else {
            relLoadMore.setVisibility(View.INVISIBLE);
        }
    }

    /*
     {
          active: [true/false],
          seekbar :
               {       progressColor: 'HEX COLOR'
               },
          analytics:
              {
            tvpa: [true/false]
            }
     }

    */
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

    void setTitle() {

        //enabled coach options


        //unlock drawer
//        mainTvPageActivity.enableDisableDrawer(false);
        ib_back.setVisibility(View.VISIBLE);
        ib_drawers_temp.setVisibility(View.GONE);
        ib_drawer.setVisibility(View.GONE);


    }


    void setDataForProduct() {
        if (productList != null && productList.size() > 0) {
            //hide error text and visible recyclerview

            //CommonUtils.sout("Productss > " + productList.toString());

            tvNoProduct.setVisibility(View.INVISIBLE);
            horizontalRecyclerView.setVisibility(View.VISIBLE);

            adapter = new HorizontalAdapter(productList);
            horizontalRecyclerView.setAdapter(adapter);
        } else {
            //display error text
            tvNoProduct.setVisibility(View.VISIBLE);
            horizontalRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    void manageErrorText(TextView textView, RecyclerView recyclerView, int size) {

    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        switch (tag) {
            case "back":
                getActivity().onBackPressed();
                break;
            case "img_pin":
                if (productList.size() > 0) {

                    if (tvPagePlayer != null) {
                        tvPagePlayer.pause();
                    }

                    Bundle b = new Bundle();
                    b.putSerializable("product_list", productList);
                    b.putString(CommonUtils.VIDEO_DETAIL_OPEN_SCREEN, videoDetailsScreen);
                    GalleryVideoDetailsFragment fragment = GalleryVideoDetailsFragment.getInstance(b);

//                    pushFragment(fragment, GalleryVideoDetailsFragment.this,
//                            true, false, true, false);
                    ((TvPageGalleryActivity) getActivity()).pushFragment(fragment,
                            true, false, true, false);

                } else {
                    CommonUtils.makeToast("No Product Found", getActivity());
                }
                break;
            case "load_more":
                pageNumberFroPagination++;
                callChannelVideoListApi(true);
                break;
           /* case R.id.ib_back:
                mainTvPageActivity.onBackPressed();
                break;
            case R.id.image_pin:
                if (productList.size() > 0) {

                    if (tvPagePlayer != null) {
                        tvPagePlayer.pause();
                    }

                    Bundle b = new Bundle();
                    b.putSerializable("product_list", productList);
                    mainTvPageActivity.pushFragments(new VideoProductDetailsFragments(), true, true, false
                            , true, VideoProductDetailsFragments.class.getSimpleName(), b);
                } else {
                    CommonUtils.makeToast("No Product Found", getActivity());
                }
                break;
            case R.id.imgHeaderLogo:
                mainTvPageActivity.clearBackStackExceptHome();
                break;
            case R.id.tvLoadmore:
                pageNumberFroPagination++;
                callChannelVideoListApi(true);
                break;
            default:
                break;*/
        }
    }


    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private ArrayList<ProductModel> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image_related;

            public MyViewHolder(View view) {
                super(view);
                image_related = (ImageView) view.findViewById(R.id.image_related);

            }
        }

        public HorizontalAdapter(ArrayList<ProductModel> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_horizonal, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, final int position) {

            //set glide images
            CommonUtils.setImageGlideProduct(getActivity(), horizontalList.get(position).getImg_product(), holder.image_related);

            holder.image_related.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tvPagePlayer != null) {
                        tvPagePlayer.pause();
                    }
                    String product_id = "";
                    String channel_id = "";
                    String video_id = "";


                    product_id = horizontalList.get(position).getProduct_id();
                    channel_id = horizontalList.get(position).getProduct_channel_id();
                    video_id = horizontalList.get(position).getProduct_video_id();

                    //call api for Product Click Api
                    if (tvPagePlayer != null) {
                        if (product_id != null && !TextUtils.isEmpty(product_id)
                                && channel_id != null && !TextUtils.isEmpty(channel_id)
                                && video_id != null && !TextUtils.isEmpty(video_id)
                                ) {
                            tvPagePlayer.analyticsProductClickApi(Integer.parseInt(channel_id),
                                    Integer.parseInt(video_id), Integer.parseInt(product_id));
                        }
                    }

                    Bundle b = new Bundle();
                    b.putInt("position", position);
                    b.putSerializable("product_list", productList);
//                    mainTvPageActivity.pushFragments(new VideoProductDetailsFragments(), true, true, false
//                            , true, VideoProductDetailsFragments.class.getSimpleName(), b);
                    b.putString(CommonUtils.VIDEO_DETAIL_OPEN_SCREEN, videoDetailsScreen);
                    GalleryVideoDetailsFragment fragment = GalleryVideoDetailsFragment.getInstance(b);

//                    pushFragment(fragment, GalleryVideoDetailsFragment.this,
//                            true, false, true, false);

                    ((TvPageGalleryActivity) getActivity()).pushFragment(fragment,
                            true, false, true, false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }


    public void saveInstanceOfVideoView() {
        try {
            if (tvPagePlayer != null) {
                tvPagePlayer.onSaveInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreInsatnceOfVideoView() {
        try {
            if (tvPagePlayer != null) {
                tvPagePlayer.onRestoreInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Video bottom part video list
    public void callChannelVideoListApi(final boolean isLoadmOre) {
        try {


            if (CommonUtils.isInternetConnected(getActivity())) {

                String channelIdToPass = "";

                if (!isLoadmOre) {
                  /*  progressDialogBottomList = new ProgressDialog(getActivity());
                    progressDialogBottomList.setMessage("Please wait...");
                    progressDialogBottomList.setCancelable(false);
                    progressDialogBottomList.show();*/
                }

                if (sourceDeterminationIntent.equalsIgnoreCase(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_DETAIL_VALUE)) {
                    //pput idChannelForVuideosIntent
                    channelIdToPass = idChannelForVideosIntent;
                } else if (sourceDeterminationIntent.equalsIgnoreCase(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_LIST_VALUE)) {
                    //save Channel id from pref..(Main channel id)
                    channelIdToPass = MyPreferences.getPref(getActivity(), MyPreferences.CHANNEL_ID_PREF_KEY);
                } else {

                }

                //call api of list of videos
                tvPagePlayer.tvPageChannelsVideosExtractor(channelIdToPass, pageNumberFroPagination, NUMBER_OF_RESULT_TO_RETURN,
                        "", new OnTvPageResponseApiListener() {
                            @Override
                            public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                                dismissProgressDialog();


                                if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                                    try {

                                        if (!isLoadmOre) {
                                            list.clear();
                                        }


                                        JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            TvPageVideoModel tvPageChannelVideoModel = new TvPageVideoModel();


                                  /*  if (!jsonObject.isNull("category")) {
                                        String category = jsonObject.getString("category");
                                        //add category list
                                        //System.out.println(i +" Category productsss channel>>  "+category);
                                        if (category != null &&
                                                !TextUtils.isEmpty(category) &&
                                                category.trim().length() > 0) {
                                            arrayListProductCategory.add(category);
                                        }
                                    }*/

                                            if (!jsonObject.isNull("title")) {
                                                String title = jsonObject.getString("title");
                                                //add title in tvpage model
                                                tvPageChannelVideoModel.setTitle(title);
                                            }
                                            if (!jsonObject.isNull("id")) {
                                                String id = jsonObject.getString("id");
                                                //add id in tvpage model
                                                tvPageChannelVideoModel.setId(id);
                                            }

                                            if (!jsonObject.isNull("description")) {
                                                String description = jsonObject.getString("description");
                                                //add id in tvpage model
                                                tvPageChannelVideoModel.setDescription(description);
                                            }

                                            if (!jsonObject.isNull("date_created")) {
                                                String date_created = jsonObject.getString("date_created");
                                                //add date in tvpage model
                                                tvPageChannelVideoModel.setDate_created(date_created);
                                            }

                                            if (!jsonObject.isNull("entityIdParent")) {
                                                String entityIdParent = jsonObject.getString("entityIdParent");
                                                //add date in tvpage model
                                                tvPageChannelVideoModel.setEntityIdParent(entityIdParent);
                                            }

                                            if (!jsonObject.isNull("asset")) {
                                                JSONObject jsonObjectAsset = jsonObject.getJSONObject("asset");

                                                TvPageVideoModel.Asset assets = new TvPageVideoModel.Asset();
                                                //get dash url & hls urls
                                                if (!jsonObjectAsset.isNull("dashUrl")) {
                                                    String dashUrl = jsonObjectAsset.getString("dashUrl");
                                                    assets.setDashUrl(dashUrl);
                                                }

                                                if (!jsonObjectAsset.isNull("hlsUrl")) {
                                                    String hlsUrl = jsonObjectAsset.getString("hlsUrl");
                                                    assets.setHlsUrl(hlsUrl);
                                                }

                                                if (!jsonObjectAsset.isNull("sources")) {

                                                    JSONArray jsonArray1 = jsonObjectAsset.getJSONArray("sources");
                                                    List<TvPageVideoModel.Sources> sourceList = new ArrayList<TvPageVideoModel.Sources>();
                                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                                        TvPageVideoModel.Sources sourceToInsert = new TvPageVideoModel.Sources();
                                                        if (!jsonObject1.isNull("file")) {
                                                            String file = jsonObject1.getString("file");
                                                            sourceToInsert.setFile(file);
                                                        }
                                                        if (!jsonObject1.isNull("quality")) {
                                                            String quality = jsonObject1.getString("quality");
                                                            sourceToInsert.setQuality(quality);
                                                        }


                                                        sourceList.add(sourceToInsert);

                                                    }
                                                    //add source list
                                                    assets.setSources(sourceList);
                                                }


                                                if (!jsonObjectAsset.isNull("type")) {
                                                    String type = jsonObjectAsset.getString("type");
                                                    //add type list
                                                    assets.setType(type);
                                                }

                                                if (!jsonObjectAsset.isNull("thumbnailUrl")) {
                                                    String thumbnailUrl = jsonObjectAsset.getString("thumbnailUrl");
                                                    //add type list
                                                    assets.setThumbnailUrl(thumbnailUrl);
                                                }
                                                if (!jsonObjectAsset.isNull("videoId")) {
                                                    String videoId = jsonObjectAsset.getString("videoId");
                                                    //add type list
                                                    assets.setVideoId(videoId);
                                                }
                                                if (!jsonObjectAsset.isNull("prettyDuration")) {
                                                    String prettyDuration = jsonObjectAsset.getString("prettyDuration");
                                                    assets.setPrettyDuration(prettyDuration);
                                                }


                                                //add assets in tvpage model
                                                tvPageChannelVideoModel.setAsset(assets);
                                            }


                                            list.add(tvPageChannelVideoModel);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                //set data
                                setDataToList(isLoadmOre);


                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                dismissProgressDialog();
                                throwable.printStackTrace();
                            }
                        });
            } else {

                dismissProgressDialog();
                CommonUtils.makeToast("No Internet Connection", getActivity());
            }
        } catch (Exception e) {
            dismissProgressDialog();
            e.printStackTrace();
        }
    }

    void dismissProgressDialog() {
        //setProgressLoadMore(false);
       /* if (progressDialogBottomList != null && progressDialogBottomList.isShowing()) {
            progressDialogBottomList.dismiss();
        }*/
    }

    void setDataToList(boolean isLoadMore) {


        if (list != null && list.size() > 0) {
            if (!isLoadMore) {
                videoAdapter = new VideoAdapter(getActivity(), list);
                videoAdapter.setClickListener(this);
                recyclerVideos.setAdapter(videoAdapter);
            } else {
                videoAdapter.notifyDataSetChanged();
            }
        } else {
            showLoadmore(false);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.size() > 0) {
            try {
                if (tvPagePlayer != null) {
                    tvPagePlayer.pause();
                    tvPagePlayer.seek(1);
                    tvPagePlayer.stop();
                    tvPagePlayer = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            tvPageVideoModelFromIntent = list.get(position);
            init(true);

        }


    }


    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

        private List<TvPageVideoModel> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public VideoAdapter(Context context, List<TvPageVideoModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_videos_gallery_list, parent, false);
            VideoAdapter.ViewHolder viewHolder = new VideoAdapter.ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
            TvPageVideoModel item = mData.get(position);
            if (item.getTitle() != null && item.getTitle().trim().length() > 0) {
                holder.txtVideo.setText(item.getTitle());
            }

            TvPageVideoModel.Asset asset = item.getAsset();
            if (asset != null && asset.getThumbnailUrl() != null) {
                CommonUtils.setImageGlide(getActivity(), asset.getThumbnailUrl(), holder.imageVideo);

            }
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView txtVideo;
            public ImageView imageVideo;
            public LinearLayout linearParents;

            public ViewHolder(View itemView) {
                super(itemView);
                txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
                imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
                linearParents = (LinearLayout) itemView.findViewById(R.id.linearParents);
                linearParents.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // convenience method for getting data at click position
        /*public VideoBean getItem(int id) {
            return mData.get(id);
        }*/

        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }


    }
}
