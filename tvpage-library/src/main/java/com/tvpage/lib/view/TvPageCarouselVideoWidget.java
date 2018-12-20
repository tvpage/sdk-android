package com.tvpage.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tvpage.lib.R;
import com.tvpage.lib.adapter.VideoAdapter;
import com.tvpage.lib.api_listeners.ItemClickListener;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.fragments.BaseFragment;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.model.TvPageVideoModel;
import com.tvpage.lib.utils.CommonUtils;
import com.tvpage.lib.utils.Constants;
import com.tvpage.lib.utils.EndlessRecyclerOnScrollListener;
import com.tvpage.lib.utils.Enums;
import com.tvpage.lib.utils.MyPreferences;
import com.tvpage.lib.utils.SpacesItemDecoration;
import com.tvpage.lib.utils.TvPageInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 */

public class TvPageCarouselVideoWidget extends BaseFragment implements View.OnClickListener, ItemClickListener {
    private static final String TAG = TvPageSideBarVideoWidget.class.getSimpleName();
    private Context mContext;

    private RecyclerView recyclerLatestVideo;
    private ProgressBar progressRecyclerList;
    private TvPagePlayer tvPagePlayer;

    private ArrayList<TvPageVideoModel> list;
    private VideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int pageNumberLoadMore = 0;
    private TextView mTxtLatestVideo, tvVideoError;

    /* Items Text Style Variables Start*/

    //default Items Text Color
    private int mItemsTextColor = Color.BLACK;

    //default Items Text Background Color
    private int mItemsTextBackgroundColor = Color.WHITE;

    //default Items Text Size
    private float mItemsTextSize;

    //default Items Text Padding
    private float mItemsTextPadding;

    //default Items Text Padding
    //default Gravity is Center
    private String mItemsTextGravity = "2";

    //default Items Text Style
    //default TextStyle is NORMAL
    private String mItemsTextStyle = "1";

    //default Items Type Face
    private String mItemsTextTypeFace = "fonts/helvetica.ttf";

    private static final String DEFAULT_ITEMS_TITLE_TYPE_FACE = "fonts/helvetica.ttf";


    /* Items Text Style Variables End*/

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

  /*Item Image OverLay Style Variables End*/

    //default image overlay color
    private int mItemImageOverLayRed;
    private int mItemImageOverLayGreen;
    private int mItemImageOverLayBlue;
    private int mItemImageOverLayAlpha;

  /*Item Image OverLay Style Variables End*/


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

    /* Modal Title Style Variables End*/

    /* Modal PRODUCT Title Style Variables Start*/

    //default Product Title Color
    private int mProductTitleTextColor;

    //default Product Text Size
    private float mProductTitleTextSize;

    //default Product Title is TOP
    private String mProductTitleGravity = "1";

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

    private static final String DEFAULT_PRODUCT_PRICE_TYPE_FACE = "fonts/helvetica.ttf";

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

    /* Video Title Text Style Variables Start*/
    //default Title Text
    private String mTitleTextString = "Recommended Videos";

    //default Title Text Color
    private int mTitleTextColor = Color.BLACK;

    //default Title Text Background Color
    private int mTitleTextBackgroundColor = Color.WHITE;

    //default Title Text Size
    private float mTitleTextSize;

    //default Title Text Padding
    private float mTitleTextPadding;

    //default Title Text Padding
    //default Gravity is LEFT
    private String mTitleTextGravity = "2";

    //default Title Text Style
    //default TextStyle is NORMAL
    private String mTitleTextStyle = "1";

    //default Title Text Visibility
    //default Text Visibility is VISIBLE
//    private String mTitleTextVisibility = "1";

    //default Title Type Face
    private String mTitleTextTypeFace = "fonts/helvetica.ttf";

    private static final String DEFAULT_VIDEO_TITLE_TYPE_FACE = "fonts/helvetica.ttf";
    /* Video Title Text Style Variables End*/

    //default Items Per Page
    private int mItemsPerPage = 25;

    //default product shadow visibility
    private String mProductShadowVisibility = "0";

    //default product shadow color
    private int mProductShadowColor = Color.WHITE;

    /*Video Play Option Start*/

    //default auto video play
    private boolean isAutoVideoPlay = false;

    //default auto video next
    private boolean isAutoVideoNext = false;

    /*Video Play Option  End*/

    private boolean isInfalteView = false;


    public TvPageCarouselVideoWidget() {
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the
     * arguments we handle.
     */
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvPageVideoWidgetCarousel);

        setItemsTextStyleAttribute(a);
        setItemPlayButtonStyle(a);
        setModalBackGroundStyle(a);
        setModalTitleTextStyle(a);
        setProductTitleTextStyle(a);
        setCloseIconStyle(a);
        setProductPopupStyle(a);
        setProductPriceTextStyle(a);
        setProductReviewTextStyle(a);
        setProductCTATextStyle(a);
        setProductCTABackgroundStyle(a);
        setProductImageStyle(a);
        setTitleTextAttributeSet(a);
        setItemsPerPageAndRowAttributeSet(a);
        setVideoOption(a);
        a.recycle();
    }

    /**
     * Set video option attribute set
     *
     * @param typedArray
     */
    private void setVideoOption(TypedArray typedArray) {
        isAutoVideoNext = typedArray.getBoolean(R.styleable
                .TvPageVideoSoloWidget_tvpw_solo_video_auto_next, true);

        isAutoVideoPlay = typedArray.getBoolean(R.styleable
                .TvPageVideoSoloWidget_tvpw_solo_video_auto_play, false);
    }

    /**
     * Set item per page and row Attribute
     *
     * @param typedArray
     */
    private void setItemsPerPageAndRowAttributeSet(TypedArray typedArray) {
        int itemPerPage = typedArray.getInt(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_per_page,
                mItemsPerPage);
        if (itemPerPage != 0 && itemPerPage != mItemsPerPage) {
            mItemsPerPage = itemPerPage;
        }
    }

    /**
     * Set Title Text Attribute
     *
     * @param typedArray
     */
    private void setTitleTextAttributeSet(TypedArray typedArray) {
        Log.d(TAG, "setTitleTextAttributeSet: ");
        isInfalteView = true;
        String titleText = (String) typedArray.getText(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title);
        if (CommonUtils.isValidString(titleText))
            mTitleTextString = titleText;

        mTitleTextBackgroundColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_background_color,
                getResources().getColor(R.color.white));

        mTitleTextColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_color,
                getResources().getColor(R.color.default_title_text_color));


        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mTitleTextGravity = gravity;

//        String visibility = a.getString(R.styleable.TvPageVideoWidget_tvpw_video_title_text_visibility);
//        if (CommonUtils.isValidString(visibility))
//            mTitleTextVisibility = visibility;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_style);
        if (CommonUtils.isValidString(textStyle))
            mTitleTextStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_font_path);
        if (CommonUtils.isValidString(fontTypeFace))
            mTitleTextTypeFace = fontTypeFace;

        mTitleTextPadding = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_padding,
                getResources().getDimension(R.dimen.default_carousel_video_title_padding));

        float textSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_video_title_text_size,
                getResources().getDimension(R.dimen.default_carousel_video_title_text_size));
        mTitleTextSize = CommonUtils.getValidFloat(textSize,
                getResources().getDimension(R.dimen.default_video_title_text_size));
    }

    /**
     * Set product image background attribute set
     *
     * @param typedArray
     */
    private void setProductImageStyle(TypedArray typedArray) {
        mProductImageBorderColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_product_img_border_color,
                getResources().getColor(R.color.default_modal_product_cta_bg_color));

        mProductImageBorderWidth = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_img_border_width,
                getResources().getDimension(R.dimen.common_0_dp));

        mProductImageBorderSize = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_img_border_size,
                getResources().getDimension(R.dimen.common_0_dp));
    }

    /**
     * Set product CTA background attribute set
     *
     * @param typedArray
     */
    private void setProductCTABackgroundStyle(TypedArray typedArray) {
        mProductCTABackgroundColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_product_cta_background_color,
                getResources().getColor(R.color.default_modal_product_cta_bg_color));

        mProductCTABorderColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_product_cta_border_color,
                getResources().getColor(R.color.default_modal_product_cta_border_color));

        mProductCTABorderRadius = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_cta_border_radius,
                getResources().getDimension(R.dimen.common_0_dp));

        mProductCTABorderWidth = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_cta_border_width,
                getResources().getDimension(R.dimen.common_0_dp));
    }

    /**
     * Set product CTA attribute set
     *
     * @param typedArray
     */
    private void setProductCTATextStyle(TypedArray typedArray) {

        String text = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text);
        if (CommonUtils.isValidString(text))
            mProductCTAText = text;

        mProductCTATextColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_color,
                getResources().getColor(R.color.default_modal_product_cta_text_color));

        float mProductCTATextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_size,
                getResources().getDimension(R.dimen.default_modal_product_cta_text_size));

        this.mProductCTATextSize = CommonUtils.getValidFloat(mProductCTATextSize,
                getResources().getDimension(R.dimen.default_modal_product_cta_text_size));

        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mProductCTAGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_style);
        if (CommonUtils.isValidString(textStyle))
            mProductCTAStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_typeface);
        if (CommonUtils.isValidString(fontTypeFace))
            mProductCTATypeFace = fontTypeFace;

        String mProductCTATextTransformation = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_cta_text_transformation);
        if (CommonUtils.isValidString(mProductCTATextTransformation)) {
            this.mProductCTATextTransformation = mProductCTATextTransformation;
        }
    }

    /**
     * Set product review attribute set
     *
     * @param typedArray
     */
    private void setProductReviewTextStyle(TypedArray typedArray) {
        mProductReviewTextColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_review_text_color,
                getResources().getColor(R.color.default_modal_product_review_text_color));

        float mProductReviewTextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_review_text_size,
                getResources().getDimension(R.dimen.default_modal_product_review_size));

        this.mProductReviewTextSize = CommonUtils.getValidFloat(mProductReviewTextSize,
                getResources().getDimension(R.dimen.default_modal_product_review_size));

        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_review_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mProductReviewGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_review_text_style);
        if (CommonUtils.isValidString(textStyle))
            mProductReviewStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_review_text_typeface);
        if (CommonUtils.isValidString(fontTypeFace))
            mProductReviewTypeFace = fontTypeFace;
    }

    /**
     * Set product price attribute set
     *
     * @param typedArray
     */
    private void setProductPriceTextStyle(TypedArray typedArray) {
        mProductPriceTextColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_price_text_color,
                getResources().getColor(R.color.default_modal_product_price_text_color));

        float mProductPriceTextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_price_text_size,
                getResources().getDimension(R.dimen.default_modal_product_price_size));

        this.mProductPriceTextSize = CommonUtils.getValidFloat(mProductPriceTextSize,
                getResources().getDimension(R.dimen.default_modal_product_price_size));
        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_price_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mProductPriceGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_price_text_style);
        if (CommonUtils.isValidString(textStyle))
            mProductPriceStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_price_text_typeface);
        if (CommonUtils.isValidString(fontTypeFace))
            mProductPriceTypeFace = fontTypeFace;
    }

    /**
     * Set product pop up attribute set
     *
     * @param typedArray
     */
    private void setProductPopupStyle(TypedArray typedArray) {
        mProductPopupBackgroundColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_background_color,
                getResources().getColor(R.color.default_popup_bg_color));

        mProductPopupBorderColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_border_color,
                getResources().getColor(R.color.default_popup_border_color));

        mProductPopupBorderRadius = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_border_radius,
                getResources().getDimension(R.dimen.common_0_dp));

        mProductPopupBorderWidth = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_border_width,
                getResources().getDimension(R.dimen.default_product_popup_border_width));

        mProductPopupPadding = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_padding,
                getResources().getDimension(R.dimen.default_product_popup_border_padding));

        mProductShadowColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_shadow_color,
                getResources().getColor(R.color.white));

        String mModalShadowVisibility = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_shadow_visibility);

        if (CommonUtils.isValidString(mModalShadowVisibility)) {
            this.mProductShadowVisibility = mModalShadowVisibility;
        }

        float width = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_product_popup_width,
                getResources().getDimension(R.dimen.default_product_popup_width));
        if (width != 0.0)
            mProductPopupWidth = width;
    }

    /**
     * Set close icon attribute set
     *
     * @param typedArray
     */
    private void setCloseIconStyle(TypedArray typedArray) {
        mCloseIconColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_close_icon_color,
                getResources().getColor(R.color.default_modal_title_text_color));

        mCloseIconHeight = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_close_icon_height,
                getResources().getDimension(R.dimen.default_modal_close_icon_width_height));

        mCloseIconWidth = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_close_icon_width,
                getResources().getDimension(R.dimen.default_modal_close_icon_width_height));

        mCloseIconPadding = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_close_icon_padding,
                getResources().getDimension(R.dimen.default_modal_close_icon_padding));
    }

    /**
     * Set product title attribute set
     *
     * @param typedArray
     */
    private void setProductTitleTextStyle(TypedArray typedArray) {
        mProductTitleTextColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_title_text_color,
                getResources().getColor(R.color.default_modal_product_title_text_color));

        float mProductTitleTextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_title_text_size,
                getResources().getDimension(R.dimen.default_modal_product_title_size));

        this.mProductTitleTextSize = CommonUtils.getValidFloat(mProductTitleTextSize,
                getResources().getDimension(R.dimen.default_modal_product_title_size));


        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_title_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mProductTitleGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_title_text_style);
        if (CommonUtils.isValidString(textStyle))
            mProductTitleStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_product_title_text_typeface);
        if (CommonUtils.isValidString(fontTypeFace))
            mProductTitleTypeFace = fontTypeFace;
    }

    /**
     * Set modal title text style
     */
    private void setModalTitleTextStyle(TypedArray typedArray) {

        mModalTitleTextColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_color,
                getResources().getColor(R.color.default_modal_title_text_color));

        float mModalTitleTextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_size,
                getResources().getDimension(R.dimen.default_modal_title_text_size));

        this.mModalTitleTextSize = CommonUtils.getValidFloat(mModalTitleTextSize,
                getResources().getDimension(R.dimen.default_modal_title_text_size));


        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_position);
        if (CommonUtils.isValidString(gravity))
            mModalTitleGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_style);
        if (CommonUtils.isValidString(textStyle))
            mModalTitleStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_typeface);
        if (CommonUtils.isValidString(fontTypeFace))
            mModalTitleTypeFace = fontTypeFace;

        mModalTitleTextPaddingTop = typedArray.getDimension(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_padding_top,
                getResources().getDimension(R.dimen.default_modal_title_padding_top));

        mModalTitleTextPaddingLeft = typedArray.getDimension(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_padding_Left,
                getResources().getDimension(R.dimen.default_modal_title_padding_right_left));

        mModalTitleTextPaddingRight = typedArray.getDimension(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_padding_Right,
                getResources().getDimension(R.dimen.default_modal_title_padding_right));

        mModalTitleTextPaddingBottom = typedArray.getDimension(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_modal_title_text_padding_bottom,
                getResources().getDimension(R.dimen.common_0_dp));


    }

    /**
     * Set modal background style
     */
    private void setModalBackGroundStyle(TypedArray typedArray) {

        mModalBackGroundColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_background_color,
                getResources().getColor(R.color.white));

        mModalShadowColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_shadow_color,
                getResources().getColor(R.color.default_title_text_color));

        mModalBorderColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_border_color,
                getResources().getColor(R.color.default_title_text_color));

        mModalBodyPadding = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_body_padding,
                getResources().getDimension(R.dimen.default_items_title_padding));

        mModalBorderWidth = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_border_width,
                getResources().getDimension(R.dimen.default_items_title_padding));

        String mModalShadowVisibility = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_modal_shadow_visibility);

        if (CommonUtils.isValidString(mModalShadowVisibility)) {
            this.mModalShadowVisibility = mModalShadowVisibility;
        }

    }

    /**
     * Set play button attribute set
     *
     * @param typedArray
     */
    private void setItemPlayButtonStyle(TypedArray typedArray) {
        mItemPlayButtonBackgroundColor = typedArray.getColor(R.styleable
                        .TvPageVideoWidgetCarousel_tvpw_carousel_item_play_button_background_color,
                getResources().getColor(R.color.default_item_play_button_bg_color));

        mItemPlayButtonBackgroundBorderColor = typedArray.getColor(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_item_play_button_background_border_color,
                getResources().getColor(R.color.default_item_play_button_border_color));

        mItemPlayButtonIconColor = typedArray.getColor(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_item_play_button_icon_color,
                getResources().getColor(R.color.default_item_play_button_color));

        mItemPlayButtonRadius = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_item_play_button_background_radius,
                getResources().getDimension(R.dimen.default_item_play_button_radius));

        mItemPlayButtonBorderSize = typedArray.getDimension(
                R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_item_play_button_border_size,
                getResources().getDimension(R.dimen.default_item_play_button_border));

    }

    /**
     * Set items text style
     */
    private void setItemsTextStyleAttribute(TypedArray typedArray) {

        mItemsTextBackgroundColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_background_color,
                getResources().getColor(R.color.white));

        mItemsTextColor = typedArray.getColor(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_color,
                getResources().getColor(R.color.default_title_text_color));

        String gravity = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_gravity);
        if (CommonUtils.isValidString(gravity))
            mItemsTextGravity = gravity;

        String textStyle = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_style);
        if (CommonUtils.isValidString(textStyle))
            mItemsTextStyle = textStyle;

        String fontTypeFace = typedArray.getString(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_title_font_path);
        if (CommonUtils.isValidString(fontTypeFace))
            mItemsTextTypeFace = fontTypeFace;

        mItemsTextPadding = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_padding,
                getResources().getDimension(R.dimen.default_items_title_padding));

        mItemsTextSize = typedArray.getDimension(R.styleable.TvPageVideoWidgetCarousel_tvpw_carousel_items_text_size,
                getResources().getDimension(R.dimen.default_items_title_text_size));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tvpage_player_carousel_latest_video_widget,
                container, false);
        return view;
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

                    FragmentManager fragmentManager = TvPageCarouselVideoWidget.this.getChildFragmentManager();
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

    /**
     * Remove fragment from parent fragment
     */
    protected void doPopBackStack() {
        FragmentManager fragmentManager = TvPageCarouselVideoWidget.this.getChildFragmentManager();
        if (fragmentManager != null && fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * On activity created
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            this.mContext = getActivity();
        }
        init();
    }


    /**
     * Initialise view
     */
    /*used to initialise views*/
    private void init() {
        Log.d(TAG, "init: ");
        progressRecyclerList = (ProgressBar) rootView.findViewById(R.id.progressRecyclerList);
        recyclerLatestVideo = (RecyclerView) rootView.findViewById(R.id.recyclerLatestVideo);
        tvVideoError = (TextView) rootView.findViewById(R.id.tvVideoError);
        LinearLayout llHeaderView = rootView.findViewById(R.id.llHeaderView);
        mTxtLatestVideo = llHeaderView.findViewById(R.id.txtLatestVideo);
        recyclerLatestVideo.setHasFixedSize(true);
        tvPagePlayer = new TvPagePlayer(mContext);

        if (!isInfalteView) {
            mTitleTextBackgroundColor = getResources().getColor(R.color.white);
            mTitleTextColor = getResources().getColor(R.color.default_title_text_color);
            mTitleTextPadding = getResources()
                    .getDimension(R.dimen.default_carousel_video_title_padding);
            float textSize =
                    getResources().getDimension(R.dimen.default_carousel_video_title_text_size);
            mTitleTextSize = CommonUtils.getValidFloat(textSize,
                    getResources().getDimension(R.dimen.default_video_title_text_size));
        }

        //set Layout Manger to recycler view
        setLayoutManagerToList();

        //Set Style For Video Title Text
        setVideoTitleStyle();

        //set api key and id of tv page player
        TvPageInstance.getInstance(mContext).setApiKey("1758799");//1758799
        //TSRTvPageInit.getInstance(this).setApiKey("1758929")
        MyPreferences.setPref(mContext, MyPreferences.CHANNEL_ID_PREF_KEY, "66133905");//66133905
        recyclerLatestVideo.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                pageNumberLoadMore++;
                callVideoListApi(true);
            }
        });

        callVideoListApi(false);

    }

    /**
     * Set up video title text style
     */
    private void setVideoTitleStyle() {
        Log.d(TAG, "setVideoTitleStyle: ");
        if (mTxtLatestVideo != null) {

            mTxtLatestVideo.setText(mTitleTextString);
            Log.d(TAG, "setVideoTitleStyle: " + mTitleTextGravity);
            mTxtLatestVideo.setGravity(CommonUtils.getGravity(mTitleTextGravity));
            mTxtLatestVideo.setBackgroundColor(mTitleTextBackgroundColor);
            mTxtLatestVideo.setTextColor(mTitleTextColor);
            mTxtLatestVideo.setTextSize(mTitleTextSize);
            int padding = (int) mTitleTextPadding;
            mTxtLatestVideo.setPadding(padding, padding, padding, padding);
            mTxtLatestVideo.setTypeface(CommonUtils.getTypeFaceFromFontPath(mContext, mTitleTextTypeFace,
                    DEFAULT_VIDEO_TITLE_TYPE_FACE));
            mTxtLatestVideo.setTypeface(mTxtLatestVideo.getTypeface(),
                    CommonUtils.getTextStyle(mTitleTextStyle));
//        mTxtLatestVideo.setCompoundDrawablesRelative(null, null, null,
//                getResources().getDrawable(R.drawable.dotted));
//        mTxtLatestVideo.setVisibility(CommonUtils.getVisibility(mTitleTextVisibility));

        } else {
            Log.d(TAG, "Title View is null");
        }

    }

    /**
     * Set layout manager to recycler view
     */
    private void setLayoutManagerToList() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerLatestVideo.setNestedScrollingEnabled(false);
        recyclerLatestVideo.setLayoutManager(layoutManager);
        //15dp as px, value might be obtained e.g. from dimen resources...
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics());
        recyclerLatestVideo.addItemDecoration(new SpacesItemDecoration(space));

    }


    /**
     * Call video listing API
     *
     * @param isLoadMore
     */
    public void callVideoListApi(final boolean isLoadMore) {
        try {
            if (CommonUtils.isInternetConnected(mContext)) {

                if (!isLoadMore) {
                    visibleGoneProgress(true);
                }

                //call api of list of videos
                String channel_id = MyPreferences.getPref(mContext, MyPreferences.CHANNEL_ID_PREF_KEY);
                tvPagePlayer.tvPageChannelsVideosExtractor(channel_id, pageNumberLoadMore, mItemsPerPage, ""
                        , new OnTvPageResponseApiListener() {
                            @Override
                            public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                                visibleGoneProgress(false);
                                if (!isLoadMore) {
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

                                setDataToList(isLoadMore);

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                visibleGoneProgress(false);
                                throwable.printStackTrace();
                            }
                        });
            } else {
                visibleGoneProgress(false);
                CommonUtils.makeToast("No Internet Connection", mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set list to recycler view
     *
     * @param isLoadMore
     */
    private void setDataToList(boolean isLoadMore) {
        if (list != null && list.size() > 0) {
            recyclerLatestVideo.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(GONE);


            if (!isLoadMore) {
                adapter = new VideoAdapter(mContext, list, VideoAdapter.WIDGET_TYPE.CAROUSEL);
                adapter.setItemsTextBackgroundColor(mItemsTextBackgroundColor);
                adapter.setItemsTextColor(mItemsTextColor);
                adapter.setItemsTextGravity(mItemsTextGravity);
                adapter.setItemsTextPadding(mItemsTextPadding);
                adapter.setItemsTextSize(mItemsTextSize);
                adapter.setItemsTextStyle(mItemsTextStyle);
                adapter.setItemsTextTypeFace(mItemsTextTypeFace);

                adapter.setItemPlayButtonBackgroundBorderColor(mItemPlayButtonBackgroundBorderColor);
                adapter.setItemPlayButtonBackgroundColor(mItemPlayButtonBackgroundColor);
                adapter.setItemPlayButtonBorderSize(mItemPlayButtonBorderSize);
                adapter.setItemPlayButtonIconColor(mItemPlayButtonIconColor);
                adapter.setItemPlayButtonRadius(mItemPlayButtonRadius);

                adapter.setItemImageOverLayColo(mItemImageOverLayRed,
                        mItemImageOverLayGreen,
                        mItemImageOverLayBlue,
                        mItemImageOverLayAlpha);

                adapter.setClickListener(this);
                recyclerLatestVideo.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            recyclerLatestVideo.setVisibility(GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }
    }


    /**
     * Show hide progress bar in recycler view
     *
     * @param isVisible
     */
    private void visibleGoneProgress(boolean isVisible) {
        if (isVisible) {
            if (progressRecyclerList != null) {
                progressRecyclerList.setVisibility(View.VISIBLE);
            }
        } else {
            if (progressRecyclerList != null) {
                progressRecyclerList.setVisibility(GONE);
            }
        }
    }


    /**
     * Click Event Load More Button
     *
     * @param v
     */
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
            default:
                break;
        }
    }

    /**
     * Item click event handle
     *
     * @param view
     * @param position
     */
    //item click of list
    @Override
    public void onItemClick(View view, int position) {
        //CommonUtils.sout("Pos: "+position);
        //open video player and its product details

        if (list.size() > 0) {
            Bundle b = new Bundle();
            b.putParcelable(Constants.BUNDLE_KEY_VIDEO_DETAIL_DATA_KEY, list.get(position));
            b.putInt(Constants.BUNDLE_KEY_VIDEO_POSITION, position);
            b.putString(Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_DETERMINATION_KEY, Constants.BUNDLE_KEY_VIDEO_DETAIL_SCREEN_CAROUSEL_VALUE);
            VideoDetailFragment fragment = VideoDetailFragment.getInstance(b, getActivity(), list);
            /*Set Modal Style*/
            fragment.setModalBackGroundColor(mModalBackGroundColor);
            fragment.setModalBodyPadding(mModalBodyPadding);
            fragment.setModalBorderColor(mModalBorderColor);
            fragment.setModalShadowColor(mModalShadowColor);
            fragment.setModalBorderWidth(mModalBorderWidth);

            /*Set Modal Title Style*/
            fragment.setModalTitleTextColor(mModalTitleTextColor);
            fragment.setModalTitleTextSize(mModalTitleTextSize);
            fragment.setModalTitleTextPaddingTop(mModalTitleTextPaddingTop);
            fragment.setModalTitleTextPaddingLeft(mModalTitleTextPaddingLeft);
            fragment.setModalTitleTextPaddingRight(mModalTitleTextPaddingRight);
            fragment.setModalTitleTextPaddingBottom(mModalTitleTextPaddingBottom);
            fragment.setModalTitleGravity(mModalTitleGravity);
            fragment.setModalTitleStyle(mModalTitleStyle);
            fragment.setModalTitleTypeFace(mModalTitleTypeFace);
            fragment.setModalShadowVisibility(mModalShadowVisibility);

            /*Set Modal Product Title Style*/
            fragment.setProductTitleTextColor(mProductTitleTextColor);
            fragment.setProductTitleTextSize(mProductTitleTextSize);
            fragment.setProductTitleGravity(mProductTitleGravity);
            fragment.setProductTitleStyle(mProductTitleStyle);
            fragment.setProductTitleTypeFace(mProductTitleTypeFace);

            /*Set Close Icon Style*/
            fragment.setCloseIconColor(mCloseIconColor);
            fragment.setCloseIconHeight(mCloseIconHeight);
            fragment.setCloseIconPadding(mCloseIconPadding);
            fragment.setCloseIconWidth(mCloseIconWidth);

            /*Set Modal Product Popup Background Style*/
            fragment.setProductPopupBackgroundColor(mProductPopupBackgroundColor);
            fragment.setProductPopupBorderColor(mProductPopupBorderColor);
            fragment.setProductPopupBorderWidth(mProductPopupBorderWidth);
            fragment.setProductPopupPadding(mProductPopupPadding);
            fragment.setProductPopupBorderRadius(mProductPopupBorderRadius);
            fragment.setProductShadowVisibility(mProductShadowVisibility);
            fragment.setProductShadowColor(mProductShadowColor);
            fragment.setProductPopupWidth(mProductPopupWidth);

            /*Set Modal Product Price Style*/
            fragment.setProductPriceTextColor(mProductPriceTextColor);
            fragment.setProductPriceTextSize(mProductPriceTextSize);
            fragment.setProductPriceGravity(mProductPriceGravity);
            fragment.setProductPriceStyle(mProductPriceStyle);
            fragment.setProductPriceTypeFace(mProductPriceTypeFace);

            /*Set Modal Product Review Style*/
            fragment.setProductReviewTextColor(mProductReviewTextColor);
            fragment.setProductReviewTextSize(mProductReviewTextSize);
            fragment.setProductReviewGravity(mProductReviewGravity);
            fragment.setProductReviewStyle(mProductReviewStyle);
            fragment.setProductReviewTypeFace(mProductReviewTypeFace);

            /*Set Modal Product CTA TEXT Style*/
            fragment.setProductCTATextColor(mProductCTATextColor);
            fragment.setProductCTATextSize(mProductCTATextSize);
            fragment.setProductCTAGravity(mProductCTAGravity);
            fragment.setProductCTAStyle(mProductCTAStyle);
            fragment.setProductCTATypeFace(mProductCTATypeFace);
            fragment.setProductCTATextTransformation(mProductCTATextTransformation);
            fragment.setProductCTAText(mProductCTAText);

            /*Set Modal Product CTA Background Style*/
            fragment.setProductCTABackgroundColor(mProductCTABackgroundColor);
            fragment.setProductCTABorderColor(mProductCTABorderColor);
            fragment.setProductCTABorderWidth(mProductCTABorderWidth);
            fragment.setProductCTABorderRadius(mProductCTABorderRadius);

             /*Set Modal Product Image Background Style*/
            fragment.setProductImageBorderColor(mProductImageBorderColor);
            fragment.setProductImageBorderSize(mProductImageBorderSize);
            fragment.setProductImageBorderWidth(mProductImageBorderWidth);
            fragment.setProductImageOverLayColor(mProductImageOverLayRed, mProductImageOverLayGreen,
                    mProductImageOverLayBlue, mProductImageOverLayAlpha);

            /*Set Video Options*/
            fragment.setAutoVideoNext(isAutoVideoNext);
            fragment.setAutoVideoPlay(isAutoVideoPlay);

            pushFragment(fragment, TvPageCarouselVideoWidget.this, true, false, true, false);
        }

    }

    /**
     * Set items text color
     *
     * @param color(resource color)
     */
    public void setItemsTextColor(final int color) {
        this.mItemsTextColor = color;
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemsTextColor(color);
                    }
                });

            }
        }
    }

    /**
     * Set items text background color
     *
     * @param color(ColorID)
     */
    public void setItemsTextBackgroundColor(final int color) {
        this.mItemsTextBackgroundColor = color;
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.setItemsTextBackgroundColor(color);
                    }
                });
            }
        }
    }

    /**
     * Set item text size
     *
     * @param size
     */
    public void setItemsTextSize(final float size) {
        this.mItemsTextSize = size;
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.setItemsTextSize(size);
                    }
                });
            }
        }
    }

    /**
     * Set item text padding
     *
     * @param padding(Pass dimen value)
     */
    public void setItemsTextPadding(final float padding) {
        this.mItemsTextPadding = padding;
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemsTextPadding(padding);
                    }
                });
            }
        }
    }

    /**
     * Set items text gravity
     *
     * @param gravity(Pass Enum value Enums.GRAVITY)
     */
    public void setItemsTextGravity(final Enums.GRAVITY gravity) {
        this.mItemsTextGravity = CommonUtils.getGravity(gravity);
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemsTextGravity(CommonUtils.getGravity(gravity));
                    }
                });
            }
        }
    }

    /**
     * Set items text style
     *
     * @param fontStyle(Pass Enum value Enums.FONT_STYLE)
     */
    public void setItemsTextStyle(final Enums.FONT_STYLE fontStyle) {
        this.mItemsTextStyle = CommonUtils.getTextStyle(fontStyle);
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemsTextStyle(CommonUtils.getTextStyle(fontStyle));
                    }
                });

            }

        }
    }

    /**
     * Set items text type face
     *
     * @param fontPath(pass path for typeface)
     */
    public void setItemsTextTypeFace(final String fontPath) {
        this.mItemsTextTypeFace = fontPath;
        if (adapter != null) {
            //Update UI in Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.setItemsTextTypeFace(fontPath);
                    }
                });
            }
        }
    }

    /**
     * Set item play button background color
     *
     * @param color(pass resource color)
     */
    public void setItemPlayButtonBackgroundColor(int color) {
        this.mItemPlayButtonBackgroundColor = color;
    }

    /**
     * Set item play button background border color
     *
     * @param color(pass resource color)
     */
    public void setItemPlayButtonBackgroundBorderColor(int color) {
        this.mItemPlayButtonBackgroundBorderColor = color;
    }

    /**
     * Set item play button icon color
     *
     * @param color(pass resource color)
     */
    public void setItemPlayButtonIconColor(int color) {
        this.mItemPlayButtonIconColor = color;
    }

    /**
     * Set item play button radius
     *
     * @param radius(pass dimen value)
     */
    public void setItemPlayButtonRadius(float radius) {
        this.mItemPlayButtonRadius = radius;
    }

    /**
     * Set item play button border size
     *
     * @param size(pass dimen value)
     */
    public void setItemPlayButtonBorderSize(float size) {
        this.mItemPlayButtonBorderSize = size;
    }

    /**
     * Set item image overlay color
     *
     * @param red   (pass int value)
     * @param green (pass int value)
     * @param blue  (pass int value)
     * @param alpha (pass int value)
     */
    public void setItemImageOverLayColor(int red, int green, int blue, int alpha) {
        mItemImageOverLayRed = red;
        mItemImageOverLayGreen = green;
        mItemImageOverLayBlue = blue;
        mItemImageOverLayAlpha = alpha;
    }

    /**
     * Set product image overlay color
     *
     * @param red   (pass int value)
     * @param green (pass int value)
     * @param blue  (pass int value)
     * @param alpha (pass int value)
     */
    public void setProductImageOverLayColor(int red, int green, int blue, int alpha) {
        mProductImageOverLayRed = red;
        mProductImageOverLayGreen = green;
        mProductImageOverLayBlue = blue;
        mProductImageOverLayAlpha = alpha;
    }

    /**
     * Set product image border color
     *
     * @param color(pass resource color)
     */
    public void setProductImageBorderColor(int color) {
        this.mProductImageBorderColor = color;
    }

    /**
     * Set product image border width
     *
     * @param width(pass dimen value)
     */
    public void setProductImageBorderWidth(float width) {
        this.mProductImageBorderWidth = width;
    }

    /**
     * Set product image border size
     *
     * @param size(pass dimen value)
     */
    public void setProductImageBorderSize(float size) {
        this.mProductImageBorderSize = size;
    }

    /**
     * Set modal background color
     *
     * @param color(pass resource color)
     */
    public void setModalBackGroundColor(int color) {
        this.mModalBackGroundColor = color;
    }

    /**
     * Set modal shadow color
     *
     * @param color(pass resource color)
     */
    public void setModalShadowColor(int color) {
        this.mModalShadowColor = color;
    }

    /**
     * Set modal border color
     *
     * @param color(pass resource color)
     */
    public void setModalBorderColor(int color) {
        this.mModalBorderColor = color;
    }

    /**
     * Set modal border width
     *
     * @param width(pass resource color)
     */
    public void setModalBorderWidth(float width) {
        this.mModalBorderWidth = width;
    }

    /**
     * Set modal body padding
     *
     * @param padding(pass dimen value)
     */
    public void setModalBodyPadding(float padding) {
        this.mModalBodyPadding = padding;
    }

    /**
     * Set modal title text color
     *
     * @param color(pass resource color)
     */
    public void setModalTitleTextColor(int color) {
        this.mModalTitleTextColor = color;
    }

    /**
     * Set modal title text style
     *
     * @param size(pass dimen value)
     */
    public void setModalTitleTextSize(float size) {
        this.mModalTitleTextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_modal_title_text_size));
    }

    /**
     * Set modal title text padding top
     *
     * @param padding(pass dimen value)
     */
    public void setModalTitleTextPaddingTop(float padding) {
        this.mModalTitleTextPaddingTop = padding;
    }

    /**
     * Set modal title text padding left
     *
     * @param padding(pass dimen value)
     */
    public void setModalTitleTextPaddingLeft(float padding) {
        this.mModalTitleTextPaddingLeft = padding;
    }

    /**
     * Set modal title text padding Right
     *
     * @param padding(pass dimen value)
     */
    public void setModalTitleTextPaddingRight(float padding) {
        this.mModalTitleTextPaddingRight = padding;
    }

    /**
     * Set modal title text padding bottom
     *
     * @param padding(pass dimen value)
     */
    public void setModalTitleTextPaddingBottom(float padding) {
        this.mModalTitleTextPaddingBottom = padding;
    }

    /**
     * Set modal title gravity
     *
     * @param gravity(pass Enums.GRAVITY value)
     */
    public void setModalTitleGravity(Enums.GRAVITY gravity) {
        this.mModalTitleGravity = CommonUtils.getGravity(gravity);
    }

    /**
     * Set modal title style
     *
     * @param fontStyle(pass Enums.FONT_STYLE value)
     */
    public void setModalTitleStyle(Enums.FONT_STYLE fontStyle) {
        this.mModalTitleStyle = CommonUtils.getTextStyle(fontStyle);
    }

    /**
     * Set modal title type face
     *
     * @param fontPath(pass typeface path)
     */
    public void setModalTitleTypeFace(String fontPath) {
        this.mModalTitleTypeFace = fontPath;
    }


    /**
     * Set product title text color
     *
     * @param mProductTitleTextColor(pass resource color)
     */
    public void setProductTitleTextColor(int mProductTitleTextColor) {
        this.mProductTitleTextColor = mProductTitleTextColor;
    }

    /**
     * Set product title text size
     *
     * @param size(pass dimen value)
     */
    public void setProductTitleTextSize(float size) {
        this.mProductTitleTextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_modal_product_title_size));
    }

    /**
     * Set product title gravity
     *
     * @param gravity(pass Enums.GRAVITY value)
     */
    public void setProductTitleGravity(Enums.GRAVITY gravity) {
        this.mProductTitleGravity = CommonUtils.getGravity(gravity);
    }

    /**
     * Set product title style
     *
     * @param fontStyle(Enums.FONT_STYLE value)
     */
    public void setProductTitleStyle(Enums.FONT_STYLE fontStyle) {
        this.mProductTitleStyle = CommonUtils.getTextStyle(fontStyle);
    }

    /**
     * Set product title type face
     *
     * @param fontPath(pass font path)
     */
    public void setProductTitleTypeFace(String fontPath) {
        this.mProductTitleTypeFace = fontPath;
    }

    /**
     * Set close icon color
     *
     * @param color(pass resource color)
     */
    public void setCloseIconColor(int color) {
        this.mCloseIconColor = color;
    }

    /**
     * Set close icon width
     *
     * @param width(pass dimen value)
     */
    public void setCloseIconWidth(float width) {
        this.mCloseIconWidth = width;
    }

    /**
     * Set close icon height
     *
     * @param height(pass dimen value)
     */
    public void setCloseIconHeight(float height) {
        this.mCloseIconHeight = height;
    }

    /**
     * Set close icon padding
     *
     * @param padding(pass dimen value)
     */
    public void setCloseIconPadding(float padding) {
        this.mCloseIconPadding = padding;
    }

    /**
     * Set product popup background color
     *
     * @param color(pass dimen value)
     */
    public void setProductPopupBackgroundColor(int color) {
        this.mProductPopupBackgroundColor = color;
    }

    /**
     * Set product popup border color
     *
     * @param color(pass resource color)
     */
    public void setProductPopupBorderColor(int color) {
        this.mProductPopupBorderColor = color;
    }

    /**
     * Set product popup border width
     *
     * @param width(pass dimen value)
     */
    public void setProductPopupBorderWidth(float width) {
        this.mProductPopupBorderWidth = width;
    }

    /**
     * Set product popup padding
     *
     * @param padding(pass dimen value)
     */
    public void setProductPopupPadding(float padding) {
        this.mProductPopupPadding = padding;
    }

    /**
     * Set product popup border radius
     *
     * @param radius(pass dimen value)
     */
    public void setProductPopupBorderRadius(float radius) {
        this.mProductPopupBorderRadius = radius;
    }

    /**
     * Set product price text color
     *
     * @param color(pass resource color)
     */
    public void setProductPriceTextColor(int color) {
        this.mProductPriceTextColor = color;
    }

    /**
     * Set product price text size
     *
     * @param size(pass dimen value)
     */
    public void setProductPriceTextSize(float size) {
        this.mProductPriceTextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_modal_product_price_size));
    }

    /**
     * Set product price gravity
     *
     * @param gravity(pass Enums.GRAVITY value)
     */
    public void setProductPriceGravity(Enums.GRAVITY gravity) {
        this.mProductPriceGravity = CommonUtils.getGravity(gravity);
    }

    /**
     * Set product price style
     *
     * @param style (pass Enums.FONT_STYLE  style)
     */
    public void setProductPriceStyle(Enums.FONT_STYLE style) {
        this.mProductPriceStyle = CommonUtils.getTextStyle(style);
    }

    /**
     * Set product price typeface
     *
     * @param fontPath(pass font path)
     */
    public void setProductPriceTypeFace(String fontPath) {
        this.mProductPriceTypeFace = fontPath;
    }

    /**
     * Set product review text color
     *
     * @param color(pass resource color)
     */
    public void setProductReviewTextColor(int color) {
        this.mProductReviewTextColor = color;
    }

    /**
     * Set product review text size
     *
     * @param size(pass dimen value)
     */
    public void setProductReviewTextSize(float size) {
        this.mProductReviewTextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_modal_product_review_size));
    }

    /**
     * Set product review gravity
     *
     * @param gravity(pass Enums.GRAVITY value)
     */
    public void setProductReviewGravity(Enums.GRAVITY gravity) {
        this.mProductReviewGravity = CommonUtils.getGravity(gravity);
    }

    /**
     * Set product review style
     *
     * @param style(pass Enums.FONT_STYLE value)
     */
    public void setProductReviewStyle(Enums.FONT_STYLE style) {
        this.mProductReviewStyle = CommonUtils.getTextStyle(style);
    }

    /**
     * Set product review type face
     *
     * @param fontPath(pass font path)
     */
    public void setProductReviewTypeFace(String fontPath) {
        this.mProductReviewTypeFace = fontPath;
    }


    /**
     * Set product CTA text color
     *
     * @param color(pass resource color)
     */
    public void setProductCTATextColor(int color) {
        this.mProductCTATextColor = color;
    }

    /**
     * Set product CTA text size
     *
     * @param size(pass dimen value)
     */
    public void setProductCTATextSize(float size) {
        this.mProductCTATextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_modal_product_cta_text_size));
    }

    /**
     * Set product CTA gravity
     *
     * @param gravity(pass Enums.GRAVITY value)
     */
    public void setProductCTAGravity(Enums.GRAVITY gravity) {
        this.mProductCTAGravity = CommonUtils.getGravity(gravity);
    }

    /**
     * Set product CTA style
     *
     * @param style(pass Enums.FONT_STYLE value)
     */
    public void setProductCTAStyle(Enums.FONT_STYLE style) {
        this.mProductCTAStyle = CommonUtils.getTextStyle(style);
    }

    /**
     * Set product CTA type face
     *
     * @param fontPath (pass font path)
     */
    public void setProductCTATypeFace(String fontPath) {
        this.mProductCTATypeFace = fontPath;
    }

    /**
     * Set product CTA text
     *
     * @param text(pass title text)
     */
    public void setProductCTAText(String text) {
        this.mProductCTAText = text;
    }

    /**
     * Set product CTA text transformation
     *
     * @param transformation(Enums.TRANSFORMATION value)
     */
    public void setProductCTATextTransformation(Enums.TRANSFORMATION transformation) {
        this.mProductCTATextTransformation = CommonUtils.getTransformation(transformation);
    }

    /**
     * Set product CTA background color
     *
     * @param color(pass resource color)
     */
    public void setProductCTABackgroundColor(int color) {
        this.mProductCTABackgroundColor = color;
    }

    /**
     * Set product CTA border color
     *
     * @param color(pass resource color)
     */
    public void setProductCTABorderColor(int color) {
        this.mProductCTABorderColor = color;
    }

    /**
     * Set product CTA border width
     *
     * @param width(pass dimen value)
     */
    public void setProductCTABorderWidth(float width) {
        this.mProductCTABorderWidth = width;
    }

    /**
     * Set product CTA border radius
     *
     * @param radius(pass dimen value)
     */
    public void setProductCTABorderRadius(float radius) {
        this.mProductCTABorderRadius = radius;
    }

    /**
     * Set modal shadow visibility
     *
     * @param visibility(pass Enums.VISIBILITY value)
     */
    public void setModalShadowVisibility(Enums.VISIBILITY visibility) {
        this.mModalShadowVisibility = CommonUtils.getVisibility(visibility);
    }

    /**
     * Set modal shadow RGB color
     *
     * @param red   (pass int value)
     * @param green (pass int value)
     * @param blue  (pass int value)
     * @param alpha (pass int value)
     */
    public void setModalShadowRGBColor(int red, int green, int blue, int alpha) {
        this.mModalShadowColor = Color.argb(alpha, red, green, blue);
    }

    /**
     * Set title text string
     *
     * @param text(pass title text)
     */
    public void setTitleTextString(String text) {
        if (CommonUtils.isValidString(text)) {
            this.mTitleTextString = text;
            setVideoTitleStyle();
        }
    }

    /**
     * Set title text color
     *
     * @param color(pass resource color)
     */
    public void setTitleTextColor(int color) {
        if (color != 0
                && String.valueOf(color).length() >= 6) {
            this.mTitleTextColor = color;
            setVideoTitleStyle();
        }
    }

    /**
     * Set title text background color
     *
     * @param color(pass resource color)
     */
    public void setTitleTextBackgroundColor(int color) {
        if (color != 0
                && String.valueOf(color).length() >= 6) {
            this.mTitleTextBackgroundColor = color;
            setVideoTitleStyle();
        }
    }

    /**
     * Set title text size
     *
     * @param size(pass dimen value)
     */
    public void setTitleTextSize(float size) {
        this.mTitleTextSize = CommonUtils.getValidFloat(size,
                getResources().getDimension(R.dimen.default_video_title_text_size));
        setVideoTitleStyle();
    }

    /**
     * Set title text padding
     *
     * @param padding(pass dimen value)
     */
    public void setTitleTextPadding(float padding) {
        this.mTitleTextPadding = padding;
        setVideoTitleStyle();
    }

    /**
     * Set title font gravity
     *
     * @param(pass Enums.GRAVITY value)
     */
    public void setTitleTextGravity(Enums.GRAVITY gravity) {
        this.mTitleTextGravity = CommonUtils.getGravity(gravity);
        setVideoTitleStyle();
    }

    /**
     * Set title text style
     *
     * @param style(pass Enums.FONT_STYLE value)
     */
    public void setTitleTextStyle(Enums.FONT_STYLE style) {
        this.mTitleTextStyle = CommonUtils.getTextStyle(style);
        setVideoTitleStyle();
    }

//    public void setTitleTextVisibility(Enums.VISIBILITY visibility) {
//        this.mTitleTextVisibility = CommonUtils.getVisibility(visibility);
//        setVideoTitleStyle();
//    }

    /**
     * Set title text type face
     *
     * @param fontPath(pass font path)
     */
    public void setTitleTextTypeFace(String fontPath) {
        this.mTitleTextTypeFace = fontPath;
        setVideoTitleStyle();
    }

    /**
     * Set items per page
     *
     * @param countPerPage(pass item count)
     */
    public void setItemsPerPage(int countPerPage) {
        if (countPerPage != 0)
            this.mItemsPerPage = countPerPage;
    }


    /**
     * Set product shadow visibility
     *
     * @param visibility (pass Enums.VISIBILITY value)
     */
    public void setProductShadowVisibility(Enums.VISIBILITY visibility) {
        this.mProductShadowVisibility = CommonUtils.getVisibility(visibility);
    }

    /**
     * Set product shadow color
     *
     * @param mProductShadowColor(pass resource color)
     */
    public void setProductShadowColor(int mProductShadowColor) {
        this.mProductShadowColor = mProductShadowColor;
    }

    /**
     * Set product shadow color
     *
     * @param red   (pass int value)
     * @param green (pass int value)
     * @param blue  (pass int value)
     * @param alpha (pass int value)
     */
    public void setProductShadowColor(int red, int green, int blue, int alpha) {
        this.mProductShadowColor = Color.argb(alpha, red, green, blue);
    }


    /**
     * Set auto video play
     *
     * @param autoVideoPlay(pass boolean)
     */
    public void setAutoVideoPlay(boolean autoVideoPlay) {
        isAutoVideoPlay = autoVideoPlay;
    }

    /**
     * Set auto video next
     *
     * @param autoVideoNext(pass boolean)
     */
    public void setAutoVideoNext(boolean autoVideoNext) {
        isAutoVideoNext = autoVideoNext;
    }

    /**
     * Set product popup width
     *
     * @param width(pass dimen value)
     */
    public void setProductPopupWidth(float width) {
        this.mProductPopupWidth = width;
    }
}
