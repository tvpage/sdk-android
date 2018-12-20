package com.tvpageapp2;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tvpage.lib.view.TvPagePlayer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by MTPC-133 on 2/2/2018.
 */

@RunWith(AndroidJUnit4.class)
public class PlayerTestCase {

    public TvPagePlayer mView;

    /*Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tvpageapp2", appContext.getPackageName());
    }
*/
    @Rule
    public ActivityTestRule<TvPagePlayerActivity> mActivityRule = new ActivityTestRule<>(
            TvPagePlayerActivity.class);

    @Before
    public void setUp() {
        mView = mActivityRule.getActivity().getWidget();
    }

    @Test
    public void setVideoOptions() {
        mView.play();
//        mView.setAutoVideoPlay(true);
    }
}
