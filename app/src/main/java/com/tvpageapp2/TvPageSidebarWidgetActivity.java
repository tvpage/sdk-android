package com.tvpageapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tvpage.lib.view.TvPageSideBarVideoWidget;


public class TvPageSidebarWidgetActivity extends AppCompatActivity {

    TvPageSideBarVideoWidget tvPageLatestVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    void initComponents() {
        tvPageLatestVideo = (TvPageSideBarVideoWidget) getSupportFragmentManager()
                .findFragmentById(R.id.tvPageLatestVideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpTitleFontTextStyle();
        setLoadMoreButtonStyle();
        setItemsPerPageAndRow();
        setItemsTextStyle();
        setModalBackGroundStyle();
        setModalTitleTextStyle();
        setModalProductTitleTextStyle();
        setCloseIconStyle();
        setProductPopUpStyle();
        setProductPriceTextStyle();
        setProductReviewTextStyle();
        setCTATextStyle();
        setCTATextBackgroundStyle();
        setProductImageStyle();
        setItemPlayButtonStyle();
        setVideoOption();

    }

    /**
     * set Auto Video Play Option
     */
    private void setVideoOption() {
   /*     tvPageLatestVideo.setAutoVideoNext(true);
        tvPageLatestVideo.setAutoVideoPlay(true);*/
    }

    /**
     * Set Item Play Image Style
     */
    private void setItemPlayButtonStyle() {
//        tvPageLatestVideo.setItemPlayButtonBackgroundBorderColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setItemPlayButtonBackgroundColor(getResources().getColor(R.color.gray));
//        tvPageLatestVideo.setItemPlayButtonBorderSize(3);
//        tvPageLatestVideo.setItemPlayButtonIconColor(getResources().getColor(R.color.colorPrimary));
//        tvPageLatestVideo.setItemPlayButtonRadius(70);
////        set Image Overlay
//        tvPageLatestVideo.setItemImageOverLayColor(255, 0, 0, 30);
    }

    /**
     * Set Product Image Style
     */
    private void setProductImageStyle() {
//        tvPageLatestVideo.setProductImageBorderColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductImageBorderSize(300f);
//        tvPageLatestVideo.setProductImageBorderWidth(1f);
        //Set Product Image Over Lay
//        tvPageLatestVideo.setProductImageOverLayColor(255, 0, 0, 30);

    }

    /**
     * Set CTA Background Style
     */
    private void setCTATextBackgroundStyle() {
//        tvPageLatestVideo.setProductCTABackgroundColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductCTABorderColor(getResources().getColor(R.color.black));
//        tvPageLatestVideo.setProductCTABorderWidth(11f);
//        tvPageLatestVideo.setProductCTABorderRadius(50f);
    }

    /**
     * Set CTA Text Style
     */
    private void setCTATextStyle() {
//        tvPageLatestVideo.setProductCTATextColor(getResources().getColor(R.color.black));
//        tvPageLatestVideo.setProductCTATextSize(15f);
//        tvPageLatestVideo.setProductCTAText("SEE DETAILS");
//        tvPageLatestVideo.setProductCTAGravity(Enums.GRAVITY.CENTER);
//        tvPageLatestVideo.setProductCTAStyle(Enums.FONT_STYLE.ITALIC);
//        tvPageLatestVideo.setProductCTATypeFace("fonts/Dosis-Bold.ttf");
//        tvPageLatestVideo.setProductCTATextTransformation(Enums.TRANSFORMATION.LOWER_CASE);
    }

    /**
     * Set Product Review Text Style
     */
    private void setProductReviewTextStyle() {
//        tvPageLatestVideo.setProductReviewTextColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductReviewTextSize(4f);
//        tvPageLatestVideo.setProductReviewGravity(Enums.GRAVITY.CENTER);
//        tvPageLatestVideo.setProductReviewStyle(Enums.FONT_STYLE.BOLD);
//        tvPageLatestVideo.setProductReviewTypeFace("fonts/Dosis-Bold.ttf");
    }


    /**
     * Set Product Price Text Style
     */
    private void setProductPriceTextStyle() {
//        tvPageLatestVideo.setProductPriceTextColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductPriceTextSize(4f);
//        tvPageLatestVideo.setProductPriceGravity(Enums.GRAVITY.CENTER);
//        tvPageLatestVideo.setProductPriceStyle(Enums.FONT_STYLE.BOLD);
//        tvPageLatestVideo.setProductPriceTypeFace("fonts/Dosis-Bold.ttf");
    }

    /**
     * Set Product Popup Style
     */
    private void setProductPopUpStyle() {
//        tvPageLatestVideo.setProductPopupBackgroundColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductPopupBorderColor(getResources().getColor(R.color.cardview_dark_background));
//        tvPageLatestVideo.setProductPopupBorderWidth(11f);
//        tvPageLatestVideo.setProductPopupPadding(5f);
//        tvPageLatestVideo.setProductPopupBorderRadius(50f);
//        tvPageLatestVideo.setProductShadowColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setProductShadowColor(255,0,0,80);
//        tvPageLatestVideo.setProductShadowVisibility(Enums.VISIBILITY.VISIBLE);
//        tvPageLatestVideo.setProductPopupWidth(450f);

    }

    /**
     * Set Modal Close Icon Style
     */
    private void setCloseIconStyle() {
//        tvPageLatestVideo.setCloseIconColor((getResources().getColor(R.color.price_text_color)));
//        tvPageLatestVideo.setCloseIconHeight(12.5f);
//        tvPageLatestVideo.setCloseIconPadding(30f);
//        tvPageLatestVideo.setCloseIconWidth(12.5f);
    }

    /**
     * Set Modal Product Title Text
     */
    private void setModalProductTitleTextStyle() {
//        tvPageLatestVideo.setProductTitleTextColor((getResources().getColor(R.color.price_text_color)));
//        tvPageLatestVideo.setProductTitleTextSize(12.5f);
//        tvPageLatestVideo.setProductTitleGravity(Enums.GRAVITY.RIGHT);
//        tvPageLatestVideo.setProductTitleStyle(Enums.FONT_STYLE.BOLD);
//        tvPageLatestVideo.setProductTitleTypeFace("fonts/Dosis-Bold.ttf");
    }

    /**
     * Set Modal Title Text Style
     */
    private void setModalTitleTextStyle() {
//        tvPageLatestVideo.setModalTitleTextColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setModalTitleTextSize(10.5f);
//        tvPageLatestVideo.setModalTitleTextPaddingTop(10f);
//        tvPageLatestVideo.setModalTitleTextPaddingLeft(10f);
//        tvPageLatestVideo.setModalTitleTextPaddingRight(10f);
//        tvPageLatestVideo.setModalTitleTextPaddingBottom(10f);
//        tvPageLatestVideo.setModalTitleGravity(Enums.GRAVITY.RIGHT);
//        tvPageLatestVideo.setModalTitleStyle(Enums.FONT_STYLE.ITALIC);
//        tvPageLatestVideo.setModalTitleTypeFace("fonts/Dosis-Bold.ttf");
    }

    /**
     * set Modal Background Style
     */
    private void setModalBackGroundStyle() {
//        tvPageLatestVideo.setModalBackGroundColor(getResources().getColor(R.color.price_text_color));
//        tvPageLatestVideo.setModalBodyPadding(0);
//        tvPageLatestVideo.setModalBorderColor(getResources().getColor(R.color.white));
//        tvPageLatestVideo.setModalBorderWidth(0);
//        tvPageLatestVideo.setModalShadowColor(getResources().getColor(R.color.black));
//        tvPageLatestVideo.setProductShadowVisiblity(Enums.VISIBILITY.GONE);
//        tvPageLatestVideo.setModalShadowRGBColor(255,0,0,30);
    }

    /**
     * set Items Text Style
     */
    private void setItemsTextStyle() {
//        tvPageLatestVideo.setItemsTextColor(getResources().getColor(R.color.gray));
//        tvPageLatestVideo.setItemsTextStyle(Enums.FONT_STYLE.ITALIC);
//        tvPageLatestVideo.setItemsTextGravity(Enums.GRAVITY.RIGHT);
//        tvPageLatestVideo.setItemsTextBackgroundColor(getResources().getColor(R.color.black));
//        tvPageLatestVideo.setItemsTextSize(12f);
//        tvPageLatestVideo.setItemsTextPadding(10f);
//        tvPageLatestVideo.setItemsTextTypeFace("fonts/Dosis-Bold.ttf");
    }

    /**
     * set Item Per Page And Row
     */
    private void setItemsPerPageAndRow() {
//        tvPageLatestVideo.setItemsPerPage(6);
//        tvPageLatestVideo.setItemsPerRow(3);
    }

    /**
     * Set Load More Button Style
     */
    private void setLoadMoreButtonStyle() {
//        tvPageLatestVideo.setLoadMoreBackgroundColor(getResources().getColor(R.color.gray));
//        tvPageLatestVideo.setLoadMoreBorderColor(getResources().getColor(R.color.black));
//        tvPageLatestVideo.setLoadMoreBorderRadius(0);
//        tvPageLatestVideo.setLoadMoreBorderWidth(0);
//        tvPageLatestVideo.setLoadMoreText("564132564");
//        tvPageLatestVideo.setLoadMoreTextColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * Set Title Font Text Style
     */
    private void setUpTitleFontTextStyle() {
//        tvPageLatestVideo.setTitleTextBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        tvPageLatestVideo.setTitleTextColor(getResources().getColor(R.color.white));
//        tvPageLatestVideo.setTitleTextGravity(Enums.GRAVITY.LEFT);
//        tvPageLatestVideo.setTitleTextStyle(Enums.FONT_STYLE.BOLD);
//        tvPageLatestVideo.setTitleTextPadding(50f);
//        tvPageLatestVideo.setTitleTextSize(10f);
//        tvPageLatestVideo.setTitleTextTypeFace("fonts/Dosis-Bold.ttf");
//        tvPageLatestVideo.setTitleTextString("WEL COME");
    }

    public TvPageSideBarVideoWidget getTvPageLatestVideo() {
        return (TvPageSideBarVideoWidget) getSupportFragmentManager()
                .findFragmentById(R.id.tvPageLatestVideo);
    }


    public void addString(String str) {
        Log.d("ABC", "addString: " + str);
    }

}
