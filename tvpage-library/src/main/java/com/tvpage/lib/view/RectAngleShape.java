package com.tvpage.lib.view;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by MTPC-133 on 12/1/2017.
 */

public class RectAngleShape extends GradientDrawable {
    public RectAngleShape(int bgColor,
                          int pStrokeWidth, int pStrokeColor, float cornerRadius) {
        super((Orientation.BOTTOM_TOP), new int[]{bgColor, bgColor, bgColor});
        setCornerRadius(cornerRadius);
        setShape(GradientDrawable.RECTANGLE);
        setStroke(pStrokeWidth, pStrokeColor);
    }
}
