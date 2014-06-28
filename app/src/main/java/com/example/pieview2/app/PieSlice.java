package com.example.pieview2.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieSlice extends View {
    private Paint mPaintSlice;

    private int mId;
    private float mStartAngle;
    private int mCount;

    private int mCx;
    private int mCy;

    private Canvas mCanvas;

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

        super.onLayout(changed, left, top, right, bottom);
    }

    public void initSlice(int id, int count, int cx, int cy)
    {
        mId = id;
        mCount = count;
        mStartAngle = id * (360.0f / count);
        mPaintSlice = new Paint();

        mCx = cx;
        mCy = cy;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.v("PieSlice", "OnMeasure - w: " + widthSize + " h: " + heightSize);
        setMeasuredDimension(widthSize, heightSize);

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        int radius = this.getMeasuredHeight()/2;

        float cy = radius;
        float cx = radius;

        float cornerX1 = cx + (float)(radius * Math.cos(Math.toRadians(mStartAngle)));
        float cornerY1 = cy - (float)(radius * Math.sin(Math.toRadians(mStartAngle)));

        float cornerX2 = cx + (float)(radius * Math.cos(Math.toRadians(mStartAngle + (360.0f / mCount))));
        float cornerY2 = cy - (float)(radius * Math.sin(Math.toRadians(mStartAngle + (360.0f / mCount))));


        //Log.v("PieSlice", String.format("Click X: %f, Y: %f", eventX, eventY));
        //Log.v("PieSlice", String.format("Slice: %d - X0: %f Y0: %f X1: %f Y1: %f X2: %f Y2: %f", mId, cx, cy, cornerX1, cornerY1, cornerX2, cornerY2));

        float temp = (-1.0f*cornerY1*cornerX2 + cy*(-1.0f*cornerX1 + cornerX2) + cx*(cornerY1 - cornerY2) + cornerX1*cornerY2);
        float area = 0.5f*temp;

        float sign = area < 0 ? -1.0f : 1.0f;
        float s = ((cy*cornerX2 - cx*cornerY2 + (cornerY2 - cy)*eventX + (cx - cornerX2)*eventY)) * sign;
        float t = ((cx*cornerY1 - cy*cornerX1 + (cy - cornerY1)*eventX + (cornerX1 - cx)*eventY)) * sign;

        //Log.v("PieSlice", "S: " + s + " T: " + t + " Area: " + area);

        if (s > 0 && t > 0 && (s+t) < (2 * area * sign)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.v("PieSlice", "ActionDown");
                    this.performClick();
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                default:
                    return true;
            }
        } else {
            return false;
        }
    }


//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        Log.v("PieSlice", "Setting OnClick listener for" + mId);
//        super.setOnClickListener(l);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("PieSlice", "OnDraw Called");

        mCanvas = canvas;

        RectF rect = new RectF(0, 0, 650, 650);

        mPaintSlice.setColor(Color.GREEN);
        mPaintSlice.setStrokeWidth(20);
        mPaintSlice.setAntiAlias(true);
        mPaintSlice.setStrokeCap(Paint.Cap.ROUND);
        mPaintSlice.setStyle(Paint.Style.STROKE);

        //canvas.drawOval(rect, mPaintSlice);
        float start = mStartAngle;
        float end = 360.0f / mCount;
        Log.v("Draw", "Drawing: " + start + " - " + end);
        canvas.drawArc(rect, start, end, true, mPaintSlice);

    }
}
