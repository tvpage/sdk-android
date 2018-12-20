package com.tvpageapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tvpage.lib.TvPageGalleryActivity;

/**
 * Created by MTPC-133 on 12/11/2017.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void openCarouselWidget(View view) {
        openActivity(TvPageCarousellWidgetActivity.class);
    }

    public void openSideBarWidget(View view) {
        openActivity(TvPageSidebarWidgetActivity.class);
    }

    public void openSoloWidget(View view) {
        openActivity(TvPageSoloWidgetActivity.class);
    }

    public void openGalleryWidget(View view) {
                TvPageGalleryActivity.getInstance()
//                .setChannelId(this,"ENTER CHANNEL ID")
                .startActivity(this);

    }

    private void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
