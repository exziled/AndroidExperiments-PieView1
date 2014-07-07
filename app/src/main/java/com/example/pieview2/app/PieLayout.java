package com.example.pieview2.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieLayout extends ViewGroup {
    private int mBgColor = Color.BLUE;

    public PieLayout(Context context) {
    super(context);
}

    public PieLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieLayout);
        final int N = a.getIndexCount();

        for(int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch(attr)
            {
                case R.styleable.PieLayout_pieBgColor:
                    mBgColor = a.getColor(attr, 0);
            }
        }

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

            child.initSlice(i, count, 10);

            // Get the width and height requested by the child
            child.measure(MeasureSpec.makeMeasureSpec(radius * 2, MeasureSpec.UNSPECIFIED),
                          MeasureSpec.makeMeasureSpec(radius * 2, MeasureSpec.UNSPECIFIED));


            // Layout the child view but give them the entire pie's area.  If we don't do this, there isn't enough room
            // to draw the slice shape of each pie slice.
            child.layout(viewCenterX - radius, viewCenterY - radius, viewCenterX + radius, viewCenterY + radius);
        }

    }

    public int getBackgroundColor() {
        return mBgColor;
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }
}

