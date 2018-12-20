package com.tvpage.lib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvpage.lib.R;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;

import java.util.List;

/**
 * Created by MTPC-110 on 11/29/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private static final String TAG = VideoAdapter.class.getSimpleName();

    private List<TvPageVideoModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private boolean isFromLatestVideoSidebar;


    //default Items Text Color
    private int mItemsTextColor = Color.BLACK;

    //default Items Text Background Color
    private int mItemsTextBackgroundColor = Color.WHITE;

    //default Items Text Size
    private float mItemsTextSize;

    //default Items Text Padding
    private float mItemsTextPadding;

    //default Items Text Padding
    //default Gravity is LEFT
    private String mItemsTextGravity = "2";

    //default Items Text Style
    //default TextStyle is NORMAL
    private String mItemsTextStyle = "1";

    //default Items Type Face
    private String mItemsTextTypeFace = "fonts/helvetica.ttf";

    private static final String DEFAULT_ITEMS_TITLE_TYPE_FACE = "fonts/helvetica.ttf";

    /* Item Play Button Style Variables Start*/

    //default Item Play Button Background Color
    private int mItemPlayButtonBackgroundColor = Color.WHITE;

    //default Item Play Button Background Border Color
    private int mItemPlayButtonBackgroundBorderColor = Color.BLACK;

    //default Item Play Button Background Color
    private int mItemPlayButtonIconColor = Color.BLACK;

    //default Item Play Button Background Color
    private float mItemPlayButtonRadius;

    //default Item Play Button Background Color
    private float mItemPlayButtonBorderSize;

    /*Item Play Button Style Variables End*/

    //default image overlay color
    private int mItemImageOverLayRed;
    private int mItemImageOverLayGreen;
    private int mItemImageOverLayBlue;
    private int mItemImageOverLayAlpha;

    public enum WIDGET_TYPE {
        SIDE_BAR, CAROUSEL, SOLO
    }

    private WIDGET_TYPE mType;


    // data is passed into the constructor
    public VideoAdapter(Context context, List<TvPageVideoModel> data, WIDGET_TYPE type) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        this.mType = type;
        mItemsTextColor = ContextCompat.getColor(context, R.color.default_title_text_color);
        mItemsTextSize = context.getResources().getDimensionPixelSize(R.dimen.default_items_title_text_size);
        mItemsTextPadding = context.getResources().getDimensionPixelSize(R.dimen.default_items_title_padding);
        this.isFromLatestVideoSidebar = isFromLatestVideoSidebar;
    }

    // inflates the cell layout from xml when needed
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (mType) {
            case SOLO:
                view = mInflater.inflate(R.layout.item_solo_video, parent, false);
                break;
            case CAROUSEL:
                view = mInflater.inflate(R.layout.item_latest_videos_carousel, parent, false);
                break;
            case SIDE_BAR:
                view = mInflater.inflate(R.layout.item_latest_videos_sidebar, parent, false);
                break;
            default:
                view = mInflater.inflate(R.layout.item_latest_videos_sidebar, parent, false);
                break;
        }
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

        holder.txtVideo.setTextColor(mItemsTextColor);
        holder.txtVideo.setBackgroundColor(mItemsTextBackgroundColor);
        int padding = (int) mItemsTextPadding;
        holder.txtVideo.setPadding(padding, padding, padding, padding);
        holder.txtVideo.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext, mItemsTextTypeFace, DEFAULT_ITEMS_TITLE_TYPE_FACE),
                CommonUtils.getTextStyle(mItemsTextStyle));
        Log.d(TAG, "onBindViewHolder: " + mItemsTextSize);
        holder.txtVideo.setTextSize(mItemsTextSize);
        holder.txtVideo.setGravity(CommonUtils.getGravity(mItemsTextGravity));

        TvPageVideoModel.Asset asset = item.getAsset();
        if (asset != null && asset.getThumbnailUrl() != null) {
            //TvPageUtils.sout(asset.getThumbnailUrl() == null ? "Null" : asset.getThumbnailUrl());
            CommonUtils.setImageGlide(mContext, asset.getThumbnailUrl(), holder.imageVideo);

        }

        /*Set Play Button Style Start*/
        GradientDrawable playButtonShape = new GradientDrawable();
        playButtonShape.setShape(GradientDrawable.OVAL);
        playButtonShape.setColor(mItemPlayButtonBackgroundColor);
        playButtonShape.setSize((int) mItemPlayButtonRadius, (int) mItemPlayButtonRadius);
        playButtonShape.setStroke((int) mItemPlayButtonBorderSize, mItemPlayButtonBackgroundBorderColor);

        holder.imagePlay.setBackground(playButtonShape);

        holder.imagePlay.setPadding(20, 20, 20, 20);
        Drawable getIcon = CommonUtils.getDrawables(mContext, R.drawable.ic_play_arrow);
        Drawable customizeIcon = CommonUtils.changeDrawableColor(getIcon, mItemPlayButtonIconColor);
        holder.imagePlay.setImageDrawable(customizeIcon);
         /*Set Play Button Style Start*/

         /*Set Image Overlay Style Start*/
        holder.viewOverLay.setBackgroundColor(Color.argb(mItemImageOverLayAlpha, mItemImageOverLayRed,
                mItemImageOverLayGreen, mItemImageOverLayBlue));
        /*Set Image Overlay Style End*/

    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (mType == WIDGET_TYPE.SOLO) {
            return 1;
        } else {
            return mData.size();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtVideo;
        public ImageView imageVideo, imagePlay;
        public View viewOverLay;

        public ViewHolder(View itemView) {
            super(itemView);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
            imagePlay = (ImageView) itemView.findViewById(R.id.imagePlay);
            viewOverLay = itemView.findViewById(R.id.viewImageOverLay);
            itemView.setOnClickListener(this);
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

    public void setItemsTextColor(int mItemsTextColor) {
        this.mItemsTextColor = mItemsTextColor;
        notifyDataSetChanged();
    }

    public void setItemsTextBackgroundColor(int mItemsTextBackgroundColor) {
        this.mItemsTextBackgroundColor = mItemsTextBackgroundColor;
        notifyDataSetChanged();
    }

    public void setItemsTextSize(float mItemsTextSize) {
        this.mItemsTextSize = mItemsTextSize;
        notifyDataSetChanged();
    }

    public void setItemsTextPadding(float mItemsTextPadding) {
        this.mItemsTextPadding = mItemsTextPadding;
        notifyDataSetChanged();
    }

    public void setItemsTextGravity(String mItemsTextGravity) {
        this.mItemsTextGravity = mItemsTextGravity;
        notifyDataSetChanged();
    }

    public void setItemsTextStyle(String mItemsTextStyle) {
        this.mItemsTextStyle = mItemsTextStyle;
        notifyDataSetChanged();
    }

    public void setItemsTextTypeFace(String mItemsTextTypeFace) {
        this.mItemsTextTypeFace = mItemsTextTypeFace;
        notifyDataSetChanged();
    }


    public void setItemPlayButtonBackgroundColor(int mItemPlayButtonBackgroundColor) {
        this.mItemPlayButtonBackgroundColor = mItemPlayButtonBackgroundColor;
    }

    public void setItemPlayButtonBackgroundBorderColor(int mItemPlayButtonBackgroundBorderColor) {
        this.mItemPlayButtonBackgroundBorderColor = mItemPlayButtonBackgroundBorderColor;
        notifyDataSetChanged();
    }

    public void setItemPlayButtonIconColor(int mItemPlayButtonIconColor) {
        this.mItemPlayButtonIconColor = mItemPlayButtonIconColor;
        notifyDataSetChanged();
    }

    public void setItemPlayButtonRadius(float mItemPlayButtonRadius) {
        this.mItemPlayButtonRadius = mItemPlayButtonRadius;
        notifyDataSetChanged();
    }

    public void setItemPlayButtonBorderSize(float mItemPlayButtonBorderSize) {
        this.mItemPlayButtonBorderSize = mItemPlayButtonBorderSize;
        notifyDataSetChanged();
    }

    public void setItemImageOverLayColo(int red, int green, int blue, int alpha) {
        mItemImageOverLayRed = red;
        mItemImageOverLayGreen = green;
        mItemImageOverLayBlue = blue;
        mItemImageOverLayAlpha = alpha;
        notifyDataSetChanged();
    }
}
