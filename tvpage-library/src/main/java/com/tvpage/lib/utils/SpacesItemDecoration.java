package com.tvpage.lib.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MTPC-110 on 8/24/2017.
 */
//list space item decorations
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
       /* if(parent.getChildAdapterPosition(view) == state.getItemCount()-1){
            outRect.bottom = space;
            outRect.top = 0; //don't forget about recycling...
        }*/
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
        }
    }
}