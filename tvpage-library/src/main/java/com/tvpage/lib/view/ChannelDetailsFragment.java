package com.tvpage.lib.view;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.tvpage.lib.R;
import com.tvpage.lib.TvPageGalleryActivity;
import com.tvpage.lib.adapter.SpinnerAdapterProductCateg;
import com.tvpage.lib.adapter.SpinnerAdapterVideotype;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.fragments.BaseFragment;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tvpage.lib.utils.CommonUtils.NUMBER_OF_RESULT_TO_RETURN;
import static com.tvpage.lib.utils.CommonUtils.PARCABLE_VIDEO_MODEL_KEY;


/**
 */

public class ChannelDetailsFragment extends BaseFragment implements View.OnClickListener, ItemClickListener {
    ImageView img_gallery_channel;
    TextView tvProductTitle;
    TextView tvProductVideosNumber;
    TextView tvSubscribe;
    TextView tvReset;
    Spinner spinnerProductCategory;
    Spinner spinnerVideoType;
    RecyclerView recyclerVideos;
    ArrayList<String> arrayListProductCategory = new ArrayList<>();
    ArrayList<String> arrayListvideoType = new ArrayList<>();

    TvPagePlayer tvPagePlayer;
    ProgressDialog progressDialog;
    List<TvPageVideoModel> list = new ArrayList<TvPageVideoModel>();
    VideoAdapter videoAdapter;

    String imageIntent = "";
    String idIntent = "";
    String titleIntent = "";


    public ImageView ib_drawer;
    RelativeLayout ib_back;

    ImageView ib_drawers_temp;

    ImageView imgHeaderLogo;
    //ProgressBar progressVideosLoadMore;

    int pageNumberFroPagination = 0;

    TextView tvLoadmore;
    RelativeLayout relLoadMore;


    public static ChannelDetailsFragment getInstance(Bundle bundle) {
        ChannelDetailsFragment videoDetailFragment1 = new ChannelDetailsFragment();
        videoDetailFragment1.setArguments(bundle);
        return videoDetailFragment1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_channel_videos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get Bundle Data
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("title")) {
                titleIntent = b.getString("title");
                CommonUtils.sout("t " + titleIntent);
            }
            if (b.containsKey("id")) {
                idIntent = b.getString("id");
                CommonUtils.sout("id " + idIntent);
            }
            if (b.containsKey("image")) {
                imageIntent = b.getString("image");
                CommonUtils.sout("image " + imageIntent);
            }

        }

        init();
    }

    void init() {

        tvPagePlayer = new TvPagePlayer(getActivity());

        tvLoadmore = (TextView) rootView.findViewById(R.id.tvLoadmore);
        relLoadMore = (RelativeLayout) rootView.findViewById(R.id.relLoadMore);


        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);

        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);
        //progressVideosLoadMore = (ProgressBar) rootView.findViewById(R.id.progressVideosLoadMore);

        img_gallery_channel = (ImageView) rootView.findViewById(R.id.img_gallery_channel);
        tvProductTitle = (TextView) rootView.findViewById(R.id.tvProductTitle);
        tvProductVideosNumber = (TextView) rootView.findViewById(R.id.tvProductVideosNumber);
        tvSubscribe = (TextView) rootView.findViewById(R.id.tvSubscribe);
        tvReset = (TextView) rootView.findViewById(R.id.tvReset);
        spinnerProductCategory = (Spinner) rootView.findViewById(R.id.spinnerProductCategory);
        spinnerVideoType = (Spinner) rootView.findViewById(R.id.spinnerVideoType);
        recyclerVideos = (RecyclerView) rootView.findViewById(R.id.recyclerVideos);
        ib_drawers_temp = (ImageView) rootView.findViewById(R.id.ib_drawers_temp);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);

        ib_back.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);
        tvLoadmore.setOnClickListener(this);


        LinearLayoutManager vertLinearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /*int spanCount = 2; // 3 columns
        int spacing = 20; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);*/
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(vertLinearLayoutManager);

       /* ssssrecyclerVideos.addOnScrollListener(new EndlessRecyclerOnScrollListener(vertLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                //
                if (CommonUtils.isInternetConnected(getActivity())) {
                    //setProgressLoadMore(true);
                    pageNumberFroPagination++;
                    callChannelVideoListApi(true);
                }
            }
        });*/


        //set title
        setTitle();

        //set Video type data
        arrayListvideoType.clear();

        arrayListvideoType.add("Type of Video");
        arrayListvideoType.add("Youtube");
        arrayListvideoType.add("Vimeo");
        arrayListvideoType.add("Mp4");

        setVideoTypeSpinnerData();

        //set image
        if (!imageIntent.startsWith("http")) {
            CommonUtils.setImageGlideProduct(getActivity(), "http:" + imageIntent, img_gallery_channel);
        } else {
            CommonUtils.setImageGlideProduct(getActivity(), imageIntent, img_gallery_channel);
        }

        //set title
        tvProductTitle.setText(titleIntent);


        callChannelVideoListApi(false);
    }

    void showLoadmore(boolean isNedToShowLoadMore) {
        if (isNedToShowLoadMore) {
            relLoadMore.setVisibility(View.VISIBLE);
        } else {
            relLoadMore.setVisibility(View.INVISIBLE);
        }
    }

   /* void setProgressLoadMore(final boolean isToDisplay) {
        if (progressVideosLoadMore != null) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isToDisplay) {
                            progressVideosLoadMore.setVisibility(View.VISIBLE);
                        } else {
                            progressVideosLoadMore.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }*/

    void setTitle() {

        //enabled coach options


        //from back
        //Enable Back
//        mainTvPageActivity.enableDisableDrawer(false);
        ib_back.setVisibility(View.VISIBLE);
        ib_drawers_temp.setVisibility(View.GONE);
        ib_drawer.setVisibility(View.GONE);


    }


    public void callChannelVideoListApi(final boolean isLoadmOre) {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {

                if (!isLoadmOre) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                //call api of list of videos
                tvPagePlayer.tvPageChannelsVideosExtractor(idIntent, pageNumberFroPagination,
                        NUMBER_OF_RESULT_TO_RETURN, "", new OnTvPageResponseApiListener() {
                            @Override
                            public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                                dismissProgressDialog();


                                if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                                    try {

                                        if (!isLoadmOre) {
                                            list.clear();
                                            arrayListProductCategory.clear();
                                            arrayListProductCategory.add("Product Category");
                                        }


                                        JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            TvPageVideoModel tvPageChannelVideoModel = new TvPageVideoModel();


                                            if (!jsonObject.isNull("category")) {
                                                String category = jsonObject.getString("category");
                                                //add category list
                                                //System.out.println(i +" Category productsss channel>>  "+category);
                                                if (category != null &&
                                                        !TextUtils.isEmpty(category) &&
                                                        category.trim().length() > 0) {
                                                    arrayListProductCategory.add(category);
                                                }
                                            }
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
                                setProductSpinnerData();

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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setProductSpinnerData() {
        if (arrayListProductCategory.size() > 0) {
           /* for (HashMap<String, String> entry : listOfQuality) {
                for (String key : entry.keySet()) {
                    String url = entry.get(key);
                    String quality = key;
                    TvPageUtils.sout(" QualityKey: " + quality + " UrlValue: " + url);
                }
            }*/

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        SpinnerAdapterProductCateg adapterRegion = new SpinnerAdapterProductCateg
                                (getActivity(), R.layout.adapter_spinner_product_cat, arrayListProductCategory);

                        spinnerProductCategory.setAdapter(adapterRegion);
                        spinnerProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String keysToPass = "";


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });

            }
        }


    }

    private void setVideoTypeSpinnerData() {
        if (arrayListvideoType.size() > 0) {
           /* for (HashMap<String, String> entry : listOfQuality) {
                for (String key : entry.keySet()) {
                    String url = entry.get(key);
                    String quality = key;
                    TvPageUtils.sout(" QualityKey: " + quality + " UrlValue: " + url);
                }
            }*/


            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        SpinnerAdapterVideotype adapterRegion = new SpinnerAdapterVideotype
                                (getActivity(), R.layout.adapter_spinner_product_cat, arrayListvideoType);

                        spinnerVideoType.setAdapter(adapterRegion);
                        spinnerVideoType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String keysToPass = "";


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }

        }


    }

    void setDataToList(boolean isLoadMore) {

        //set number of videos
        tvProductVideosNumber.setText("" + list.size() + " VIDEOS");

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
      /*  if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(View.GONE);
            adapter = new VideoAdapter(getActivity(), list);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }*/
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();

        switch (tag) {
            case "back":
                getActivity().onBackPressed();
                break;
            case "logo":
//                mainTvPageActivity.clearBackStackExceptHome();
                break;
            case "loadMore":
                pageNumberFroPagination++;
                callChannelVideoListApi(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.size() > 0) {

            String title = "";
            String id = "";
            String type = "";
            String video_id = "";
            String video_desc = "";
            String video_duration = "";
            String video_date = "";
            String entity_id_parent = "";

            /*ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();

            TvPageVideoModel tvPageVideoModelPos = list.get(position);

            if (tvPageVideoModelPos.getTitle() != null) {
                title = tvPageVideoModelPos.getTitle();

            }

            if (tvPageVideoModelPos.getId() != null) {
                id = tvPageVideoModelPos.getId();
            }

            if (tvPageVideoModelPos.getDate_created() != null) {
                video_date = tvPageVideoModelPos.getDate_created();
            }

            if (tvPageVideoModelPos.getDescription() != null) {
                video_desc = tvPageVideoModelPos.getDescription();
            }

            if (tvPageVideoModelPos.getEntityIdParent() != null) {
                entity_id_parent = tvPageVideoModelPos.getEntityIdParent();
            }

            TvPageVideoModel.Asset asset = tvPageVideoModelPos.getAsset();


            if (asset != null) {

                if (asset.getPrettyDuration() != null) {
                    video_duration = asset.getPrettyDuration();
                }
                if (asset.getType() != null) {
                    type = asset.getType();
                }

                if (asset.getVideoId() != null) {
                    video_id = asset.getVideoId();
                }


                //Manage SOurces (File & Quality)

                if (asset.getSources() != null && asset.getSources().size() > 0) {

                    for (int j = 0; j < asset.getSources().size(); j++) {
                        if (asset.getSources().get(j).getQuality() != null && asset.getSources().get(j).getFile() != null) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(asset.getSources().get(j).getQuality(), asset.getSources().get(j).getFile());
                            qualityList.add(hashMap);
                        }
                    }
                }
            }*/


            Bundle bundle = new Bundle();

            /*bundle.putString("title", title);
            bundle.putString("id", id);
            bundle.putString("type", type);
            bundle.putString("video_id", video_id);
            bundle.putString("video_duration", video_duration);
            bundle.putString("video_desc", video_desc);
            bundle.putString("video_date", video_date);
            bundle.putString("entity_id_parent", entity_id_parent);
            bundle.putSerializable("quality_urls", qualityList);*/
            bundle.putParcelable(PARCABLE_VIDEO_MODEL_KEY, list.get(position));
            bundle.putString("channel_id", idIntent);
            bundle.putString(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY, CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_DETAIL_VALUE);
            bundle.putString(CommonUtils.VIDEO_DETAIL_OPEN_SCREEN, CommonUtils.VIDEO_DETAIL_OPEN_SCREEN_CHANNEL_DETAIL);

            //push video Detail acttivity
//            pushFragment(new VideoDetailFragment(), true, false, true, VideoDetailFragment.class.getSimpleName(), bundle);


            //push video Detail acttivity
            GalleryVideoDetailsFragment fragment1 = GalleryVideoDetailsFragment.getInstance(bundle);
            Log.d("ABC", "onItemClick:asd CALL");
           /* pushFragment(fragment1, ChannelDetailsFragment.this,
                    true, true, true, false);
*/
            ((TvPageGalleryActivity) getActivity()).pushFragment(fragment1,
                    true, true, true, false);

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
