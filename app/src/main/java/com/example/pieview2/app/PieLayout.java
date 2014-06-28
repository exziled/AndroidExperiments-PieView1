package com.example.pieview2.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieLayout extends ViewGroup {

    public PieLayout(Context context) {
    super(context);
}

    public PieLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();


        final int radius;
        if (this.getMeasuredWidth() > this.getMeasuredHeight()) {
            radius = this.getMeasuredHeight() / 2;
        } else {
            radius = this.getMeasuredWidth() / 2;
        }

        // As the PieView spirals out from the center, find the center of our view
        final int viewCenterX = this.getMeasuredWidth() / 2;
        final int viewCenterY = this.getMeasuredHeight() / 2;

        for (int i = 0; i < count; i++) {
            PieSlice child = (PieSlice)getChildAt(i);
            //View child = getChildAt(i);

            child.initSlice(i, count);
            // Get the width and height requested by the child
            child.measure(MeasureSpec.makeMeasureSpec(radius * 2, MeasureSpec.UNSPECIFIED),
                          MeasureSpec.makeMeasureSpec(radius *2, MeasureSpec.UNSPECIFIED));


            // Layout the child view but give them the entire pie's area.  If we don't do this, there isn't enough room
            // to draw the slice shape of each pie slice.
            child.layout(viewCenterX - radius, viewCenterY - radius, viewCenterX + radius, viewCenterY + radius);
        }
    }
}
