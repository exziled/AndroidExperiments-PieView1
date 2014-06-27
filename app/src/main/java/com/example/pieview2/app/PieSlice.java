package com.example.pieview2.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieSlice extends View {
    private int mLeft;
    private int mTop;

    public PieSlice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PieSlice(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieSlice(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.v("PieSlice", "Left: " + left + " Top: " + top + " Right: " + right + "Bottom: " + bottom);
        mLeft = left;
        mTop = top;

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("PieSlice", "OnDraw Called");

        Path path = new Path();
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setPathEffect(null);

        path.moveTo(mLeft, mTop);
        path.lineTo(5, 5);
        path.moveTo(5, 5);
        path.close();

        canvas.drawPath(path, paint);

        canvas.drawCircle(0, 0, 10, paint);

        //canvas.drawPath(new Path(), );
    }
}
