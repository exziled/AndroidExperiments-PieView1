package com.example.pieview2.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieView extends ViewGroup {
    private int mRadius = 200;

    public PieView(Context context) {
    super(context);
}

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int curWidth, curHeight;
        final int childLeft = this.getPaddingLeft();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childTop = this.getPaddingTop();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        // Number of degrees between each child view
        final float degreeSpacing = 360.0f / count;

        // As the PieView spirals out from the center, find the center of our view
        final int viewCenterX = this.getMeasuredWidth() / 2;
        final int viewCenterY = this.getMeasuredHeight() / 2;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            // Get the width and height requested by the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                          MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));

            // Use this in the child.layout() call to set the width (right - left)
            // and height (bottom-top) of the child
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            // Determines the cx and cy positions of the child view
            // based on angle and radius.  Angle is determined by child number
            // and angle spacing (360 / number of children)
            final double rad = Math.toRadians(degreeSpacing * i);
            int viewX = viewCenterX + mRadius * (int)Math.cos(rad);
            int viewY = viewCenterY + mRadius * (int)Math.sin(rad);

            // Call to layout child view
            child.layout(viewX, viewY, viewX + curWidth, viewY + curHeight);
        }
    }
}
