package com.tvpage.lib.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvpage.lib.R;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.fragments.BaseFragment;
import com.tvpage.lib.model.TvPageChannelModel;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;
import com.tvpage.lib.utils.Constants;
import com.tvpage.lib.utils.MyPreferences;
import com.tvpage.lib.utils.TvPageInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class TvPageChannelListFragment extends BaseFragment implements ItemClickListener, View.OnClickListener {

    RecyclerView recyclerViewCahnnels;
    RecyclerView recyclerView;
    TextView tvChannelError;
    TextView tvVideoError;

    TvPagePlayer tvPagePlayer;


    private VideoAdapter adapter;
    List<TvPageVideoModel> list;


    private ChannelAdapter channelAdapter;
    List<TvPageChannelModel> listChannel;


    ProgressDialog progressDialog;


    String intentSource = "";
    int pageNumberFroPagination = 0;


    TextView tvLoadmore;
    RelativeLayout relLoadMore;

    // ProgressBar progressVideosLoadMore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tvpage_video_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        //handle back button press for child fragment (Video Details)
        //pop detail screen from stack
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    FragmentManager fragmentManager = TvPageChannelListFragment.this.getChildFragmentManager();
                    if (fragmentManager != null && fragmentManager.getBackStackEntryCount() >= 1) {
                        fragmentManager.popBackStack();
                        return true;
                    }


                }
                return false;
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get bundle data
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey(CommonUtils.VIDEO_GALLERY_SOURCE_KEY)) {
                intentSource = b.getString(CommonUtils.VIDEO_GALLERY_SOURCE_KEY);
            }
        }
        init();
    }

    void init() {
        tvPagePlayer = new TvPagePlayer(getActivity());

        tvLoadmore = (TextView) rootView.findViewById(R.id.tvLoadmore);
        relLoadMore = (RelativeLayout) rootView.findViewById(R.id.relLoadMore);

        // progressVideosLoadMore = (ProgressBar) rootView.findViewById(R.id.progressVideosLoadMore);
        tvChannelError = (TextView) rootView.findViewById(R.id.tvChannelError);
        tvVideoError = (TextView) rootView.findViewById(R.id.tvVideoError);
        recyclerViewCahnnels = (RecyclerView) rootView.findViewById(R.id.recyclerViewCahnnels);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        tvLoadmore.setOnClickListener(this);

        LinearLayoutManager vertLinearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /*int spanCount = 2; // 3 columns
        int spacing = 20; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(vertLinearLayoutManager);

        LinearLayoutManager vertLinearLayoutManagerChannels
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCahnnels.setHasFixedSize(true);
        recyclerViewCahnnels.setLayoutManager(vertLinearLayoutManagerChannels);


        /*sssdsdsdsdsdsrecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(vertLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                //
                //setProgressLoadMore(true);
                pageNumberFroPagination++;
                callVideoListApi(true);
            }
        });*/

        setTitle();
        callChannelListApi();

        //set api key and id of tv page player
        TvPageInstance.getInstance(getActivity()).setApiKey("1758799");//1758799
        //TSRTvPageInit.getInstance(this).setApiKey("1758929")
        MyPreferences.setPref(getActivity(), MyPreferences.CHANNEL_ID_PREF_KEY, "66133905");//66133905


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


    void showLoadmore(boolean isNedToShowLoadMore) {
        if (isNedToShowLoadMore) {
            relLoadMore.setVisibility(View.VISIBLE);
        } else {
            relLoadMore.setVisibility(View.INVISIBLE);
        }
    }

    void setTitle() {

        //enabled coach options

       /* if (intentSource != null && intentSource.equalsIgnoreCase(CommonUtils.VIDEO_GALLERY_SOURCE_VALUE)) {
            //from drawer
            //unlock drawer
            mainTvPageActivity.enableDisableDrawer(true);

            ib_back.setVisibility(View.GONE);
            ib_drawer.setVisibility(View.VISIBLE);
        } else {
            //from back
            //Enable Back
            mainTvPageActivity.enableDisableDrawer(false);

            ib_back.setVisibility(View.VISIBLE);
            ib_drawers_temp.setVisibility(View.VISIBLE);
            ib_drawer.setVisibility(View.GONE);
        }*/


    }

    public void callVideoListApi(final boolean isLoadmOre) {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {


                //call api of list of videos
                String channel_id = MyPreferences.getPref(getActivity(),
                        MyPreferences.CHANNEL_ID_PREF_KEY);
                tvPagePlayer.tvPageChannelsVideosExtractor(channel_id, pageNumberFroPagination, CommonUtils.NUMBER_OF_RESULT_TO_RETURN, "",
                        new OnTvPageResponseApiListener() {
                            @Override
                            public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                //setProgressLoadMore(false);

                                if (!isLoadmOre) {
                                    list = new ArrayList<TvPageVideoModel>();
                                }

                                if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                                    try {
                                        JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            TvPageVideoModel tvPageModel = new TvPageVideoModel();

                                            if (!jsonObject.isNull("title")) {
                                                String title = jsonObject.getString("title");
                                                //add title in tvpage model
                                                tvPageModel.setTitle(title);
                                            }
                                            if (!jsonObject.isNull("id")) {
                                                String id = jsonObject.getString("id");
                                                //add id in tvpage model
                                                tvPageModel.setId(id);
                                            }

                                            if (!jsonObject.isNull("description")) {
                                                String description = jsonObject.getString("description");
                                                //add id in tvpage model
                                                tvPageModel.setDescription(description);
                                            }

                                            if (!jsonObject.isNull("date_created")) {
                                                String date_created = jsonObject.getString("date_created");
                                                //add date in tvpage model
                                                tvPageModel.setDate_created(date_created);
                                            }

                                            if (!jsonObject.isNull("entityIdParent")) {
                                                String entityIdParent = jsonObject.getString("entityIdParent");
                                                //add date in tvpage model
                                                tvPageModel.setEntityIdParent(entityIdParent);
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
                                                tvPageModel.setAsset(assets);
                                            }


                                            list.add(tvPageModel);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                setDataToList(isLoadmOre);

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                //setProgressLoadMore(false);
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                throwable.printStackTrace();
                            }
                        });
            } else {
                //setProgressLoadMore(false);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                CommonUtils.makeToast("No Internet Connection", getActivity());
            }
        } catch (Exception e) {
            //setProgressLoadMore(false);
            e.printStackTrace();
        }
    }

    public void callChannelListApi() {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //call api of list of videos
                tvPagePlayer.tvPageChannelsExtractor(new OnTvPageResponseApiListener() {
                    @Override
                    public void onSuccess(TvPageResponseModel tvPageResponseModel) {


                        callVideoListApi(false);


                        listChannel = new ArrayList<TvPageChannelModel>();

                        if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                            try {
                                JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TvPageChannelModel tvPageChannelModel = new TvPageChannelModel();

                                    if (!jsonObject.isNull("title")) {
                                        String title = jsonObject.getString("title");
                                        //add title in tvpage model
                                        tvPageChannelModel.setTitle(title);
                                    }

                                    if (!jsonObject.isNull("id")) {
                                        String id = jsonObject.getString("id");
                                        //add title in tvpage model
                                        tvPageChannelModel.setId(id);
                                    }

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("settings");
                                    TvPageChannelModel.Settings settings = new TvPageChannelModel.Settings();
                                    if (!jsonObject1.isNull("canvasUrl")) {
                                        String canvasUrl = jsonObject1.getString("canvasUrl");
                                        settings.setCanvasUrl(canvasUrl);
                                    }

                                    //set settings
                                    tvPageChannelModel.setSettings(settings);

                                    listChannel.add(tvPageChannelModel);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        setChannelDataToList();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        callVideoListApi(false);
                        throwable.printStackTrace();
                    }
                });
            } else {

                callVideoListApi(false);
                CommonUtils.makeToast("No Internet Connection", getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDataToList(boolean isLoadMore) {
        if (list != null && list.size() > 0) {

            //System.out.println("Lsit Gallery si>> " + list.size() + " is Load more " + isLoadMore);

            recyclerView.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(View.GONE);

            if (!isLoadMore) {
                adapter = new VideoAdapter(getActivity(), list);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                //just notify adapter
                adapter.notifyDataSetChanged();
            }


        } else {
            //hide load more....
            showLoadmore(false);
            recyclerView.setVisibility(View.GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }
    }

    void setChannelDataToList() {
        if (listChannel != null && listChannel.size() > 0) {
            recyclerViewCahnnels.setVisibility(View.VISIBLE);
            tvChannelError.setVisibility(View.GONE);
            channelAdapter = new ChannelAdapter(getActivity(), listChannel);
            channelAdapter.setClickListener(this);
            recyclerViewCahnnels.setAdapter(channelAdapter);
        } else {
            recyclerViewCahnnels.setVisibility(View.GONE);
            tvChannelError.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onItemClick(View view, int position) {

        String tag = (String) view.getTag();
        if (CommonUtils.isValidString(tag)) {

            if (tag.equalsIgnoreCase("linearParents")) {

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


                    Bundle b = new Bundle();
                    b.putParcelable(Constants.BUNDLE_KEY_VIDEO_DETAIL_DATA_KEY, list.get(position));
                    b.putInt(Constants.BUNDLE_KEY_VIDEO_POSITION, position);
                    b.putString(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_DETERMINATION_KEY,
                            Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_VIDEO_GALLERY);
                    VideoDetailFragment fragment = VideoDetailFragment.getInstance(b, getActivity(), list);
                    pushFragment(fragment, TvPageChannelListFragment.this,
                            true, false, true, false);


                    /*bundle.putString("title", title);
                    bundle.putString("id", id);
                    bundle.putString("type", type);
                    bundle.putString("video_id", video_id);
                    bundle.putString("video_duration", video_duration);
                    bundle.putString("video_desc", video_desc);
                    bundle.putString("video_date", video_date);
                    bundle.putString("entity_id_parent", entity_id_parent);
                    bundle.putSerializable("quality_urls", qualityList);*/

//                    bundle.putParcelable(PARCABLE_VIDEO_MODEL_KEY, list.get(position));
//                    bundle.putString(CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY, CommonUtils.VIDEO_DETAIL_FROM_CHANNEL_LIST_VALUE);


                    //push video Detail acttivity

//                    mainTvPageActivity.pushFragments(new VideoDetailFragment(), true, true, false, true,
//                            VideoDetailFragment.class.getSimpleName(), bundle);
                }


            } else if (tag.equalsIgnoreCase("linearParentsChannel")) {


                if (listChannel.size() > 0) {

                    String title = "";
                    String id = "";
                    String image = "";


                    TvPageChannelModel tvPageChannelModel = listChannel.get(position);

                    if (tvPageChannelModel.getTitle() != null) {
                        title = tvPageChannelModel.getTitle();

                    }

                    if (tvPageChannelModel.getId() != null) {
                        id = tvPageChannelModel.getId();
                    }


                    if (tvPageChannelModel.getSettings() != null && tvPageChannelModel.getSettings().getCanvasUrl() != null) {
                        image = tvPageChannelModel.getSettings().getCanvasUrl();
                    }


//                    Bundle bundle = new Bundle();
//                    bundle.putString("title", title);
//                    bundle.putString("id", id);
//                    bundle.putString("image", image);


                    //push video Detail acttivity
//                    mainTvPageActivity.pushFragments(new ChannelDetailsFragment(), true, true, false, true, ChannelDetailsFragment.class.getSimpleName(), bundle);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (CommonUtils.isValidString(tag)) {
            if (tag.equalsIgnoreCase("loadmore")) {
                pageNumberFroPagination++;
                callVideoListApi(true);
            }
        }
    }


    //adapters


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

    public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

        private List<TvPageChannelModel> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public ChannelAdapter(Context context, List<TvPageChannelModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_channel_video_gallery, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TvPageChannelModel item = mData.get(position);
            if (item.getTitle() != null && item.getTitle().trim().length() > 0) {
                holder.txtVideo.setText(item.getTitle());
            }

            if (item.getSettings() != null && item.getSettings().getCanvasUrl() != null
                    && item.getSettings().getCanvasUrl().trim().length() > 0
                    ) {
                if (!item.getSettings().getCanvasUrl().startsWith("http")) {
                    CommonUtils.setImageGlideProduct(getActivity(), "http:" + item.getSettings().getCanvasUrl(), holder.imageVideo);
                } else {
                    CommonUtils.setImageGlideProduct(getActivity(), item.getSettings().getCanvasUrl(), holder.imageVideo);
                }

            } else {
                //null image
                CommonUtils.setImageGlideProduct(getActivity(), "", holder.imageVideo);
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
            public LinearLayout linearParentsChannel;

            public ViewHolder(View itemView) {
                super(itemView);
                txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
                imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
                linearParentsChannel = (LinearLayout) itemView.findViewById(R.id.linearParentsChannel);
                linearParentsChannel.setOnClickListener(this);

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

    protected void doPopBackStack() {
        FragmentManager fragmentManager = TvPageChannelListFragment.this.getChildFragmentManager();
        if (fragmentManager != null && fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack();
        }
    }

}
