package com.example.pieview2.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

    private int mRadius;
    private int mPadding = 10;
    private int mRadiusCenter = 100;

    private RectF mOuterRect;
    private RectF mInnerRect;
    private Path mArcPath;

    private int mSliceColor = Color.BLACK;
    private int mSliceTapColor = Color.YELLOW;

    private Triangle mBounds;
    private Paint mPaintSlice = new Paint();
    private Paint mPaintText = new Paint();
    private String mDisplayText;


    public PieSlice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieSlice);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch(attr) {
                case R.styleable.PieSlice_sliceBgColor:
                    mSliceColor = a.getColor(i, 0);
                    break;
                case R.styleable.PieSlice_sliceTapColor:
                    mSliceTapColor = a.getColor(i, 0);
                    break;
            }
        }
    }

    public PieSlice(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieSlice(Context context) {
        super(context);
    }

    @Override
    public String toString() {
        return String.format("PieSlice-%d", sliceId);
    }

    /**
     * Generic class which defines a triangle object as used by the PieSlice class.
     */
    public class Triangle
    {
        private PointF p0;
        private PointF p1;
        private PointF p2;

        private float area;
        private int sign;

        /**
         * Initialize a Triangle Object with any three points.  Method will also calculate the area
         * of the defined triangle.
         *
         * @param p0    Any Point
         * @param p1    Any Point
         * @param p2    Any Point
         *
         */
        public Triangle(PointF p0, PointF p1, PointF p2) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;

            area = 0.5f * (-p1.y*p2.x + p0.y*(-p1.x + p2.x) + p0.x*(p1.y - p2.y) + p1.x+p2.y);
            sign = area < 0 ? -1 : 1;
        }

        /**
         * Determine if an xy grid point lies within the defined triangle.  Uses barycentric coordinates
         * for all calculations
         *
         * @param pCheck    Point to check against
         * @return          True if point lies within triangle, false otherwise
         *
         */
        public boolean isInside(PointF pCheck) {
            float s = ( p0.y*p2.x - p0.x*p2.y + (p2.y-p0.y)*pCheck.x + (p0.x - p2.x)*pCheck.y) * (float)sign;
            float t = ( p0.x*p1.y - p0.y*p1.x + (p0.y-p1.y)*pCheck.x + (p1.x - p0.x)*pCheck.y) * (float)sign;

            return (s > 0 && t > 0 && (s + t) < (2 * area * sign));
        }

        public Point getSpan() {
            return new Point((int)p2.x, (int)p2.y);
        }

        public Point getStart() {
            return new Point((int)p1.x, (int)p1.y);
        }

        @Override
        public String toString() {
            return String.format("0:(%f, %f) 1:(%f, %f) 2:(%f, %f)", p0.x, p0.y, p1.x, p1.y, p2.x, p2.y);
        }

    }

    /**
     * Tell a PieSlice about the state of the parent PieLayout
     *
     * @param id        Slice identifier in total pie.  Used to set relative angular position
     * @param count     Total number of slices in pie.  Used to set angular span.
     *
     */
    public void initSlice(int id, int count, int slicePadding)
    {
        sliceId = id;

        //Log.v("PieSlice", String.format("Init Slice %d", id));

        float angleSpan = (360.0f / count);
        mStartAngle = id * angleSpan;
        mEndAngle = mStartAngle + (angleSpan - slicePadding);

        float radius = this.getMeasuredWidth()/2;
        PointF p0 = new PointF(radius, radius);
        PointF p1 = new PointF( radius + (float)(radius * Math.cos(Math.toRadians(mStartAngle))),
                                radius - (float)(radius * Math.sin(Math.toRadians(mStartAngle))));
        PointF p2 = new PointF( radius + (float)(radius * Math.cos(Math.toRadians(mEndAngle))),
                                radius - (float)(radius * Math.sin(Math.toRadians(mEndAngle))));
        mBounds = new Triangle(p0, p1, p2);
        mRadius = this.getMeasuredWidth()/2;


        int innerRadius = mRadius - mRadiusCenter;
        mOuterRect = new RectF(mPadding, mPadding, this.getMeasuredHeight() - mPadding, this.getMeasuredWidth() - mPadding);
        mInnerRect = new RectF(innerRadius, innerRadius, this.getMeasuredHeight() - innerRadius, this.getMeasuredWidth() - innerRadius);

        mArcPath = new Path();

        mPaintSlice.setColor(mSliceColor);
        mPaintSlice.setAntiAlias(true);

        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(20);

        mDisplayText = String.format("%d", sliceId);
    }

    /**
     * Override onMeasure to ensure each PieSlice is given the exact same dimensions, the same
     * dimensions as the parent layout.  This results in multiple layers of overlapping canvas.
     *
     * @param widthMeasureSpec      Parents specified width
     * @param heightMeasureSpec     Parents specified height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Log.v("PieSlice", "OnMeasure - w: " + widthSize + " h: " + heightSize);
        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        // From here, only execute a click operation if the tapped point is within the bounds of
        // this slice.
        if (mBounds.isInside(new PointF(eventX, eventY))) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPaintSlice.setColor(mSliceTapColor);
                    this.invalidate();
                    //return true;
                case MotionEvent.ACTION_MOVE:
                    return true;
                case MotionEvent.ACTION_UP:
                    mPaintSlice.setColor(mSliceColor);
                    this.performClick();
                    this.invalidate();
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
        mArcPath.reset();
        mArcPath.arcTo(mInnerRect, mStartAngle * -1, mStartAngle - mEndAngle, true);
        mArcPath.arcTo(mOuterRect, mEndAngle * -1, (mStartAngle - mEndAngle) * -1);
        mArcPath.close();

        canvas.drawPath(mArcPath, mPaintSlice);
    }
}
