package com.example.pieview2.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bcarlson on 6/22/14.
 */
public class PieSlice extends View {
    private int sliceId;
    private float mStartAngle;
    private float mEndAngle;

    private Triangle mBounds;
    private Paint mPaintSlice;

    public PieSlice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PieSlice(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieSlice(Context context) {
        super(context);
    }


    public class Triangle
    {
        private PointF p0;
        private PointF p1;
        private PointF p2;

        private float area;
        private int sign;

        public Triangle(PointF p0, PointF p1, PointF p2) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;

            area = 0.5f * (-p1.y*p2.x + p0.y*(-p1.x + p2.x) + p0.x*(p1.y - p2.y) + p1.x+p2.y);
            sign = area < 0 ? -1 : 1;
        }

        public boolean isInside(PointF pCheck) {
            float s = ( p0.y*p2.x - p0.x*p2.y + (p2.y-p0.y)*pCheck.x + (p0.x - p2.x)*pCheck.y) * (float)sign;
            float t = ( p0.x*p1.y - p0.y*p1.x + (p0.y-p1.y)*pCheck.x + (p1.x - p0.x)*pCheck.y) * (float)sign;

            return (s > 0 && t > 0 && (s + t) < (2 * area * sign));
        }

    }

    public void initSlice(int id, int count)
    {
        sliceId = id;

        float angleSpan = 360.0f / count;
        mStartAngle = id * angleSpan;
        mEndAngle = mStartAngle + angleSpan;

        mPaintSlice = new Paint();

        float radius = this.getMeasuredWidth()/2;
        PointF p0 = new PointF(radius, radius);
        PointF p1 = new PointF( radius + (float)(radius * Math.cos(Math.toRadians(mStartAngle))),
                                radius - (float)(radius * Math.sin(Math.toRadians(mStartAngle))));
        PointF p2 = new PointF( radius + (float)(radius * Math.cos(Math.toRadians(mEndAngle))),
                                radius - (float)(radius * Math.sin(Math.toRadians(mEndAngle))));
        mBounds = new Triangle(p0, p1, p2);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.v("PieSlice", "OnMeasure - w: " + widthSize + " h: " + heightSize);
        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        if (mBounds.isInside(new PointF(eventX, eventY))) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("PieSlice", "OnDraw Called");

        int padding = 10;
        RectF rect = new RectF(padding, padding, this.getMeasuredHeight() - padding, this.getMeasuredWidth() - padding);

        mPaintSlice.setColor(Color.GREEN);
        mPaintSlice.setStrokeWidth(20);
        mPaintSlice.setAntiAlias(true);
        mPaintSlice.setStrokeCap(Paint.Cap.ROUND);
        mPaintSlice.setStyle(Paint.Style.STROKE);

        //canvas.drawOval(rect, mPaintSlice);
        Log.v("Draw", "Drawing: " + mStartAngle + " - " + mEndAngle);

        canvas.drawArc(rect, mStartAngle, mStartAngle - mEndAngle, true, mPaintSlice);

    }
}
