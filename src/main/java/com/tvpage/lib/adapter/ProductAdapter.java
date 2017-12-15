package com.tvpage.lib.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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

import com.tvpage.lib.R;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.model.TvPageProductModel;
import com.tvpage.lib.utils.CommonUtils;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by MTPC-110 on 11/29/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private static final String TAG = ProductAdapter.class.getSimpleName();
    private List<TvPageProductModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

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


    /* Product Popup Style Variables Start*/

    //default popup background color
    private int mProductPopupBackgroundColor = Color.WHITE;

    //default popup border color
    private int mProductPopupBorderColor = Color.BLACK;

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

    //default popup  Width
    private float mProductPopupWidth;


    // data is passed into the constructor
    public ProductAdapter(Context context, List<TvPageProductModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_products, parent, false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        holder.onBind(position);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtProductTitle, txtProductReview, txtProductPrice, txtViewDetail;
        private ImageView imgProduct;
        private MaterialRatingBar mRbReviews;
        private LinearLayout mRlProducts;
        private View mViewOverLay;
        private View mViewShadow;


        public ViewHolder(View itemView) {
            super(itemView);
            txtProductTitle = (TextView) itemView.findViewById(R.id.txtProductTitle);
            txtProductReview = (TextView) itemView.findViewById(R.id.txtProductReviews);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductCost);
            txtViewDetail = (TextView) itemView.findViewById(R.id.txtViewDetails);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            mRbReviews = (MaterialRatingBar) itemView.findViewById(R.id.rbReview);
            mViewShadow = itemView.findViewById(R.id.viewProductShadow);
            mRlProducts = itemView.findViewById(R.id.rlProductItems);
            mViewOverLay = itemView.findViewById(R.id.viewOverLay);
//            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void onBind(int position) {

            /*Set Product Title Style Start*/
            txtProductTitle.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext,
                    mProductTitleTypeFace, DEFAULT_PRODUCT_TITLE_TYPE_FACE));
            txtProductTitle.setTypeface(txtProductTitle.getTypeface(),
                    CommonUtils.getTextStyle(mProductTitleStyle));
            txtProductTitle.setGravity(CommonUtils.getGravity(mProductTitleGravity));
            txtProductTitle.setTextColor(mProductTitleTextColor);
            txtProductTitle.setTextSize(mProductTitleTextSize);
            /*Set Product Title Style End*/

            /*Set Product Popup Background Style Start*/

            GradientDrawable drawable = (GradientDrawable) mRlProducts.getBackground();
            drawable.setCornerRadius(mProductPopupBorderRadius);
            drawable.setColor(mProductPopupBackgroundColor);
            drawable.setStroke((int) mProductPopupBorderWidth, mProductPopupBorderColor);
            int padding = (int) mProductPopupPadding;
            mRlProducts.setPadding(padding, padding, padding, padding);
            mRlProducts.setBackground(drawable);

            /*Set Product Popup Background Style End*/

             /*Set Product Price Style Start*/

            txtProductPrice.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext,
                    mProductPriceTypeFace, DEFAULT_PRODUCT_TITLE_TYPE_FACE));
            txtProductPrice.setTypeface(txtProductPrice.getTypeface(),
                    CommonUtils.getTextStyle(mProductPriceStyle));
            txtProductPrice.setGravity(CommonUtils.getGravity(mProductPriceGravity));
            txtProductPrice.setTextColor(mProductPriceTextColor);
            txtProductPrice.setTextSize(mProductPriceTextSize);

            /*Set Product Price Style End*/


            /*Set Product Price Style Start*/

            txtProductReview.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext,
                    mProductReviewTypeFace, DEFAULT_PRODUCT_TITLE_TYPE_FACE));
            txtProductReview.setTypeface(txtProductReview.getTypeface(),
                    CommonUtils.getTextStyle(mProductReviewStyle));
            txtProductReview.setGravity(CommonUtils.getGravity(mProductReviewGravity));
            txtProductReview.setTextColor(mProductReviewTextColor);
            txtProductReview.setTextSize(mProductReviewTextSize);

            /*Set Product Price Style End*/

            /*Set Product CTA Text Style Start*/

            txtViewDetail.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext,
                    mProductCTATypeFace, DEFAULT_PRODUCT_TITLE_TYPE_FACE));
            txtViewDetail.setTypeface(txtViewDetail.getTypeface(),
                    CommonUtils.getTextStyle(mProductCTAStyle));
            txtViewDetail.setGravity(CommonUtils.getGravity(mProductCTAGravity));
            txtViewDetail.setTextColor(mProductCTATextColor);
            txtViewDetail.setTextSize(mProductCTATextSize);
            Log.d(TAG, "onBind: " + mProductCTATextTransformation);
            if (mProductCTATextTransformation.equalsIgnoreCase("0")) {
                txtViewDetail.setText(mProductCTAText.toUpperCase());
            } else {
                txtViewDetail.setText(mProductCTAText.toLowerCase());
            }

            /*Set Product CTA Text Style End*/

            /*Set Product CTA Background Style Start*/

            GradientDrawable ctaDrawable = (GradientDrawable) txtViewDetail.getBackground();
            ctaDrawable.setCornerRadius(mProductCTABorderRadius);
            ctaDrawable.setColor(mProductCTABackgroundColor);
            ctaDrawable.setStroke((int) mProductCTABorderWidth, mProductCTABorderColor);
            txtViewDetail.setBackground(ctaDrawable);

            /*Set Product CTA Background Style End*/

            TvPageProductModel data = mData.get(position);
            if (CommonUtils.isValidString(data.getTitle_product())) {
                txtProductTitle.setText(data.getTitle_product());
            } else {
                txtProductTitle.setText("");
            }

            if (CommonUtils.isValidString(data.getPrice_product())) {
                txtProductPrice.setText(data.getPrice_product());
            } else {
                txtProductPrice.setText("");
            }

//            mRbReviews.setVisibility(View.GONE);
//            txtProductReview.setVisibility(View.GONE
//            );
//            if(CommonUtils.isValidString(data.get))

            //TODO INSERT PRICE TEXT
//            if (CommonUtils.isValidString(data())) {
//                txtProductReview.setText(data.getTitle());
//            } else {
//                txtProductReview.setText("");
//            }


            if (CommonUtils.isValidString(data.getImg_product())) {
                CommonUtils.setImageGlideProduct(mContext, data.getImg_product(), imgProduct);
            }

            /*Set Product IMG Background Style Start*/
            //Set Image Over Lay Color
            mViewOverLay.setBackgroundColor(Color.argb(mProductImageOverLayAlpha,
                    mProductImageOverLayRed,
                    mProductImageOverLayGreen,
                    mProductImageOverLayBlue));

//            GradientDrawable imgDrawable = (GradientDrawable) imgProduct.getBackground();
//            imgDrawable.setStroke((int) mProductImageBorderSize, mContext.getResources().getColor(R.color.default_title_text_color));
//            RelativeLayout.LayoutParams params =
//                    new RelativeLayout.LayoutParams((int) mProductImageBorderSize,
//                            (int) mProductImageBorderSize);
//            imgProduct.setLayoutParams(params);
//            imgProduct.setBackground(imgDrawable);

            /*Set Product IMG Background Style End*/

            txtViewDetail.setTag(position);
            txtViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    TvPageProductModel model = mData.get(pos);
                    String url = "";
                    url = model.getProduct_info_url();
                    if (url != null && !TextUtils.isEmpty(url)) {

                        if (!url.startsWith("http:")) {
                            url = "http:" + url;
                        }

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        mContext.startActivity(i);
                    }
                }
            });

            /*Set Shadow Style*/
            GradientDrawable gdShadow = (GradientDrawable) mViewShadow.getBackground();
            gdShadow.setColors(new int[]{
                    mContext.getResources().getColor(android.R.color.transparent), mProductShadowColor});
            mViewShadow.setBackground(gdShadow);
            mViewShadow.setVisibility(CommonUtils.getVisibility(mProductShadowVisibility));


            //set product layout width
            ViewGroup.LayoutParams params = mRlProducts.getLayoutParams();
            params.width = (int) mProductPopupWidth;
            mRlProducts.requestLayout();

            ViewGroup.LayoutParams shadowParam = mViewShadow.getLayoutParams();
            shadowParam.width = (int) mProductPopupWidth;
            mViewShadow.requestLayout();

        }
    }

    // convenience method for getting data at click positionF
        /*public VideoBean getItem(int id) {
            return mData.get(id);
        }*/

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setProductTitleTextColor(int mProductTitleTextColor) {
        this.mProductTitleTextColor = mProductTitleTextColor;
        notifyDataSetChanged();
    }

    public void setProductTitleTextSize(float mProductTitleTextSize) {
        this.mProductTitleTextSize = mProductTitleTextSize;
        notifyDataSetChanged();
    }

    public void setProductTitleGravity(String mProductTitleGravity) {
        this.mProductTitleGravity = mProductTitleGravity;
        notifyDataSetChanged();
    }

    public void setProductTitleStyle(String mProductTitleStyle) {
        this.mProductTitleStyle = mProductTitleStyle;
        notifyDataSetChanged();
    }

    public void setProductTitleTypeFace(String mProductTitleTypeFace) {
        this.mProductTitleTypeFace = mProductTitleTypeFace;
        notifyDataSetChanged();
    }

    public void setProductPopupBackgroundColor(int mProductPopupBackgroundColor) {
        this.mProductPopupBackgroundColor = mProductPopupBackgroundColor;
        notifyDataSetChanged();
    }

    public void setProductPopupBorderColor(int mProductPopupBorderColor) {
        this.mProductPopupBorderColor = mProductPopupBorderColor;
        notifyDataSetChanged();
    }

    public void setProductPopupBorderWidth(float mProductPopupBorderWidth) {
        this.mProductPopupBorderWidth = mProductPopupBorderWidth;
        notifyDataSetChanged();
    }

    public void setProductPopupPadding(float mProductPopupPadding) {
        this.mProductPopupPadding = mProductPopupPadding;
        notifyDataSetChanged();
    }

    public void setProductPopupBorderRadius(float mProductPopupBorderRadius) {
        this.mProductPopupBorderRadius = mProductPopupBorderRadius;
        notifyDataSetChanged();
    }


    public void setProductPriceTextColor(int mProductPriceTextColor) {
        this.mProductPriceTextColor = mProductPriceTextColor;
        notifyDataSetChanged();
    }

    public void setProductPriceTextSize(float mProductPriceTextSize) {
        this.mProductPriceTextSize = mProductPriceTextSize;
        notifyDataSetChanged();
    }

    public void setProductPriceGravity(String mProductPriceGravity) {
        this.mProductPriceGravity = mProductPriceGravity;
        notifyDataSetChanged();
    }

    public void setProductPriceStyle(String mProductPriceStyle) {
        this.mProductPriceStyle = mProductPriceStyle;
        notifyDataSetChanged();
    }

    public void setProductPriceTypeFace(String mProductPriceTypeFace) {
        this.mProductPriceTypeFace = mProductPriceTypeFace;
        notifyDataSetChanged();
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

    public void setProductImageOverLayColo(int red, int green, int blue, int alpha) {
        mProductImageOverLayRed = red;
        mProductImageOverLayGreen = green;
        mProductImageOverLayBlue = blue;
        mProductImageOverLayAlpha = alpha;
    }

    public void setProductShadowVisiblity(String visibility) {
        this.mProductShadowVisibility = visibility;
    }

    public void setProductShadowColor(int mModalShadowColor) {
        this.mProductShadowColor = mModalShadowColor;
    }

    public void setProductPopupWidth(float mProductPopupWidth) {
        this.mProductPopupWidth = mProductPopupWidth;
    }
}