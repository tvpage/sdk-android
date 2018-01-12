package com.tvpageapp2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTestINg {
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
//        TvPageVideoWidgetActivity mActivity = new TvPageVideoWidgetActivity();
        mActivityRule.getActivity().addString("str");
//        mView = mActivity.getWidget();
//        mView = mActivityRule.getActivity().getWidget();
    }

    @Test
    public void setVideoOption() {
//        mView.setAutoVideoNext(true);
//        mView.setAutoVideoPlay(true);
    }
}
