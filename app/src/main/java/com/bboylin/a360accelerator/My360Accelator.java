package com.bboylin.a360accelerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class My360Accelator extends View {
    private int mWidth;
    private int mHeight;

    private int mLineY = 600;

    private boolean isDrawBack = false;

    private int mBitmapX;
    private int mBitmapY;

    private int mWindowHeight;
    private int mFlyPercent = 100;
    //x坐标为线段中点
    Point supPoint = new Point(450, mLineY);
    private int mBackPercent;

    public My360Accelator(Context context) {
        this(context, null);
    }

    public My360Accelator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public My360Accelator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowHeight = wm.getDefaultDisplay().getHeight();


    }

    private int endX;
    private int endY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 200;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 200;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmapNormal = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        Bitmap bitmapUp = BitmapFactory.decodeResource(getResources(), R.drawable.up);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        opt.inJustDecodeBounds = false;
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg, opt);

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        Path path = new Path();

        //坐标写死微调。。。别打我。left:偏移左边的位置，top： 偏移顶部的位置
        canvas.drawBitmap(backgroundBitmap, 100, mLineY - backgroundBitmap.getHeight() + 100, p);

        Point endPoint = new Point(800, mLineY);

        if (isDrawBack) {

            p.setColor(Color.YELLOW);
            path.moveTo(100, mLineY);
            path.quadTo(supPoint.x, mLineY + (supPoint.y - mLineY) * mFlyPercent / 100, endPoint.x, endPoint.y);
            canvas.drawPath(path, p);

            if (mFlyPercent > 0) {
                canvas.drawBitmap(bitmapUp, mBitmapX, mBitmapY * mFlyPercent / 100, p);
                mFlyPercent -= 5;
                postInvalidateDelayed(10);
            } else {
                mFlyPercent = 100;
                isDrawBack = false;
            }

        } else {

            p.setColor(Color.YELLOW);
            path.moveTo(100, mLineY);
            path.quadTo(supPoint.x, supPoint.y, endPoint.x, endPoint.y);
            canvas.drawPath(path, p);
            mBitmapX = supPoint.x - bitmapNormal.getWidth() / 2;
            mBitmapY = (supPoint.y - mLineY) / 2 + mLineY - bitmapNormal.getHeight();
            canvas.drawBitmap(bitmapNormal, mBitmapX, mBitmapY, p);

        }

        p.setColor(Color.GRAY);
        canvas.drawCircle(100, endPoint.y, 10, p);
        canvas.drawCircle(endPoint.x, endPoint.y, 10, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() > mLineY)
                    supPoint.y = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = (int) event.getX();
                endY = (int) event.getY();
                isDrawBack = true;
                invalidate();
                break;
        }
        return true;
    }
}
