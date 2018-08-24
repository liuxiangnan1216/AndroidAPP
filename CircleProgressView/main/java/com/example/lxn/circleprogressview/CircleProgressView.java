package com.example.lxn.circleprogressview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-20.
 ************************************************************************/

public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressView";

    private int mMaxProgress = 100;

    private int mProgress = 50;

    private final int mCircleLineStrokeWidth = 8;
    private final int mCirclePointStrokeWidth = 10;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;

    private String mTxtHint1;

    private String mTxtHint2;

    private float angle;
    private int mRadius;


    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);//设置进度条背景色
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2 + (mCirclePointStrokeWidth - mCircleLineStrokeWidth/2); // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2 + (mCirclePointStrokeWidth - mCircleLineStrokeWidth/2); // 左上角y
        mRectF.right = width - mCircleLineStrokeWidth / 2 - (mCirclePointStrokeWidth - mCircleLineStrokeWidth/2); // 左下角x
        mRectF.bottom = height - mCircleLineStrokeWidth / 2 - (mCirclePointStrokeWidth - mCircleLineStrokeWidth/2); // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, 0, 360, false, mPaint);
        mPaint.setColor(Color.GREEN);//进度条颜色


        angle = ((float)mProgress / mMaxProgress) * 360;

        canvas.drawArc(mRectF, -90, angle, false, mPaint);
        //在圆圈处画个小圆圈

        mRadius = width/2 - mCirclePointStrokeWidth;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        canvas.drawCircle(
                (float) (width/2 + (mRadius * Math.cos((angle - 90)*Math.PI/180))),
                (float) (height/2 + (mRadius * Math.sin((angle - 90)*Math.PI/180))),
                mCirclePointStrokeWidth,mPaint);


        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        String text = mProgress + "℃";
        int textHeight = height / 4;
        mPaint.setTextSize(textHeight);
        mPaint.setColor(Color.rgb(0x66,0x66,0x66));
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

        if (!TextUtils.isEmpty(mTxtHint1)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint1;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            mPaint.setColor(Color.rgb(0x33,0x99,0xff));
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
        }

        if (!TextUtils.isEmpty(mTxtHint2)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint2;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }
}