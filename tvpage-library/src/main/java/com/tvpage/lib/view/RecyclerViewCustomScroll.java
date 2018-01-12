package com.tvpage.lib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by MTPC-110 on 12/4/2017.
 */

public class RecyclerViewCustomScroll extends RecyclerView {
    public RecyclerViewCustomScroll(Context context) {
        super(context);
    }

    public RecyclerViewCustomScroll(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCustomScroll(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
    }

}
