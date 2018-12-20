package com.tvpageapp2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tvpage.lib.utils.Enums;
import com.tvpage.lib.view.TvPageSideBarVideoWidget;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SideBarWidgetTests {


    public TvPageSideBarVideoWidget mView;


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tvpageapp2", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<TvPageSidebarWidgetActivity> mActivityRule = new ActivityTestRule<>(
            TvPageSidebarWidgetActivity.class);

    @Before
    public void setUp() {
        mView = mActivityRule.getActivity().getTvPageLatestVideo();
    }

    @Test
    public void setVideoOptions() {
        mView.setAutoVideoNext(true);
        mView.setAutoVideoPlay(true);
    }


    @Test
    public void setItemPlayButtonStyle() {
        mView.setItemPlayButtonBackgroundBorderColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setItemPlayButtonBackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.gray));
        mView.setItemPlayButtonBorderSize(3);
        mView.setItemPlayButtonIconColor(mActivityRule.getActivity().getResources().getColor(R.color.colorPrimary));
        mView.setItemPlayButtonRadius(70);
//        set Image Overlay
        mView.setItemImageOverLayColor(255, 0, 0, 30);
    }

    @Test
    public void setProductImageStyle() {
        mView.setProductImageBorderColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductImageBorderSize(300f);
        mView.setProductImageBorderWidth(1f);
//        Set Product Image Over Lay
        mView.setProductImageOverLayColor(255, 0, 0, 30);

    }


    @Test
    public void setCTATextBackgroundStyle() {
        mView.setProductCTABackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductCTABorderColor(mActivityRule.getActivity().getResources().getColor(R.color.black));
        mView.setProductCTABorderWidth(11f);
        mView.setProductCTABorderRadius(50f);
    }


    @Test
    public void setCTATextStyle() {
        mView.setProductCTATextColor(mActivityRule.getActivity().getResources().getColor(R.color.black));
        mView.setProductCTATextSize(15f);
        mView.setProductCTAText("SEE DETAILS");
        mView.setProductCTAGravity(Enums.GRAVITY.CENTER);
        mView.setProductCTAStyle(Enums.FONT_STYLE.ITALIC);
//        mView.setProductCTATypeFace("fonts/Dosis-Bold.ttf");
        mView.setProductCTATextTransformation(Enums.TRANSFORMATION.LOWER_CASE);
    }


    @Test
    public void setProductReviewTextStyle() {
        mView.setProductReviewTextColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductReviewTextSize(4f);
        mView.setProductReviewGravity(Enums.GRAVITY.CENTER);
        mView.setProductReviewStyle(Enums.FONT_STYLE.BOLD);
//        mView.setProductReviewTypeFace("fonts/Dosis-Bold.ttf");
    }


    @Test
    public void setProductPriceTextStyle() {
        mView.setProductPriceTextColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductPriceTextSize(4f);
        mView.setProductPriceGravity(Enums.GRAVITY.CENTER);
        mView.setProductPriceStyle(Enums.FONT_STYLE.BOLD);
//        mView.setProductPriceTypeFace("fonts/Dosis-Bold.ttf");
    }


    @Test
    public void setProductPopUpStyle() {
        mView.setProductPopupBackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductPopupBorderColor(mActivityRule.getActivity().getResources().getColor(R.color.cardview_dark_background));
        mView.setProductPopupBorderWidth(11f);
        mView.setProductPopupPadding(5f);
        mView.setProductPopupBorderRadius(50f);
        mView.setProductShadowColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setProductShadowColor(255, 0, 0, 80);
        mView.setProductShadowVisibility(Enums.VISIBILITY.VISIBLE);
        mView.setProductPopupWidth(450f);

    }


    @Test
    public void setCloseIconStyle() {
        mView.setCloseIconColor((mActivityRule.getActivity().getResources().getColor(R.color.price_text_color)));
        mView.setCloseIconHeight(12.5f);
        mView.setCloseIconPadding(30f);
        mView.setCloseIconWidth(12.5f);
    }


    @Test
    public void setModalProductTitleTextStyle() {
        mView.setProductTitleTextColor((mActivityRule.getActivity().getResources().getColor(R.color.price_text_color)));
        mView.setProductTitleTextSize(12.5f);
        mView.setProductTitleGravity(Enums.GRAVITY.RIGHT);
        mView.setProductTitleStyle(Enums.FONT_STYLE.BOLD);
//        mView.setProductTitleTypeFace("fonts/Dosis-Bold.ttf");
    }

    @Test
    public void setModalTitleTextStyle() {
        mView.setModalTitleTextColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setModalTitleTextSize(10.5f);
        mView.setModalTitleTextPaddingTop(10f);
        mView.setModalTitleTextPaddingLeft(10f);
        mView.setModalTitleTextPaddingRight(10f);
        mView.setModalTitleTextPaddingBottom(10f);
        mView.setModalTitleGravity(Enums.GRAVITY.RIGHT);
        mView.setModalTitleStyle(Enums.FONT_STYLE.ITALIC);
//        mView.setModalTitleTypeFace("fonts/Dosis-Bold.ttf");
    }


    @Test
    public void setModalBackGroundStyle() {
        mView.setModalBackGroundColor(mActivityRule.getActivity().getResources().getColor(R.color.price_text_color));
        mView.setModalBodyPadding(0);
        mView.setModalBorderColor(mActivityRule.getActivity().getResources().getColor(R.color.white));
        mView.setModalBorderWidth(0);
        mView.setModalShadowColor(mActivityRule.getActivity().getResources().getColor(R.color.black));
        mView.setModalShadowRGBColor(255, 0, 0, 30);
    }


    @Test
    public void setItemsTextStyle() {
        mView.setItemsTextColor(mActivityRule.getActivity().getResources().getColor(R.color.gray));
        mView.setItemsTextStyle(Enums.FONT_STYLE.ITALIC);
        mView.setItemsTextGravity(Enums.GRAVITY.RIGHT);
        mView.setItemsTextBackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.black));
        mView.setItemsTextSize(12f);
        mView.setItemsTextPadding(10f);
//        mView.setItemsTextTypeFace("fonts/Dosis-Bold.ttf");
    }


    @Test
    public void setItemsPerPageAndRow() {
        mView.setItemsPerPage(6);
        mView.setItemsPerRow(3);
    }


    @Test
    public void setLoadMoreButtonStyle() {
        mView.setLoadMoreBackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.gray));
        mView.setLoadMoreBorderColor(mActivityRule.getActivity().getResources().getColor(R.color.black));
        mView.setLoadMoreBorderRadius(0);
        mView.setLoadMoreBorderWidth(0);
        mView.setLoadMoreText("564132564");
        mView.setLoadMoreTextColor(mActivityRule.getActivity().getResources().getColor(R.color.colorPrimary));
    }


    @Test
    public void setUpTitleFontTextStyle() {
        mView.setTitleTextBackgroundColor(mActivityRule.getActivity().getResources().getColor(R.color.colorPrimary));
        mView.setTitleTextColor(mActivityRule.getActivity().getResources().getColor(R.color.white));
        mView.setTitleTextGravity(Enums.GRAVITY.LEFT);
        mView.setTitleTextStyle(Enums.FONT_STYLE.BOLD);
        mView.setTitleTextPadding(50f);
        mView.setTitleTextSize(10f);
//        mView.setTitleTextTypeFace("fonts/Dosis-Bold.ttf");
        mView.setTitleTextString("WEL COME");
    }
}
