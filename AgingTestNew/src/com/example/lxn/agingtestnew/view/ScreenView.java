package com.example.lxn.agingtestnew.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 19-1-5.
 ************************************************************************/




public class ScreenView extends View {

    private static final String TAG = "lxn-ScreenView";

    private Context mContext;
    private Paint mPaint;
    private String mText = "UNKNOW";
    private float x = (int)Math.random() * 100;
    private float y = (int)Math.random() * 100;
    private float widht;
    private float height;
    private boolean isPlusX = true;
    private boolean isPlusY = true;
    private float speed = 10;
    public int mTextSize = 150;

    public ScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        widht = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = mText;
        Log.d(TAG, "text=========" + text);
        mPaint.setAntiAlias(true);
        if (text.equals("PASS")) {
            mPaint.setColor(Color.GREEN);
        } else {
            mPaint.setColor(Color.RED);
        }
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(50);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawText(text, x, y, mPaint);
    }

    public void setText(String str){
        this.mText = str;
        this.invalidate();
    }

    public void setXY(float x, float y) {
        Log.d(TAG, "setXY x=====" + x + "\n y ====" + y);
        this.x = x;
        this.y = y;
        this.invalidate();
    }
}
