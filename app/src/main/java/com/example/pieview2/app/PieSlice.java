package com.example.pieview2.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieSlice extends View {
    private Paint mPaintSlice;

    private int mId;
    private float mStartAngle;
    private int mCount;

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

    public void initSlice(int id, int count)
    {
        mId = id;
        mCount = count;
        mStartAngle = id * (360.0f / count);
        mPaintSlice = new Paint();
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
    protected void onDraw(Canvas canvas) {
        Log.v("PieSlice", "OnDraw Called");


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
