package com.example.lxn.agingtestnew.testItem;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class LcdTestItem extends AbstractBaseTestItem{

    private final static String TAG = "lxn-LcdTestItem";

    private ImageView imageView;
    private TextView mTv_lcd;
    private int mBrightnessMode;
    private int count = 0;
    private int testTimes = 0;

    public LcdTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        if (count <= testTimes * 5) {
            SystemClock.sleep(600);
            changeBlackGround(count % 5);
            mTv_lcd.setText("" + (count / 5 + 1));
            count++;
            return false;
        } else {
//            onStop();
            mTv_lcd.setText("" + (count / 5 + 1) + "  测试结束！");
            if (testTimes != 0) {
                CompletedActivity.lcdStatus = "PASS";
            }
            onTestEnd();
            return true;
        }
    }

    public void tearDown(){
        Log.d(TAG, "tearDown");
        super.tearDown();
    }

    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    public void setUp() {
        super.setUp();
        Log.d(TAG, "setUp");
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        imageView = (ImageView) view.findViewById(R.id.imageView_lcd);
        mTv_lcd = (TextView) view.findViewById(R.id.tv_lcd);
        this.mBrightnessMode = Settings.System.getInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, -1);
        if (this.mBrightnessMode == 1) {
            Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
        }
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        imageView.setBackgroundColor(Color.RED);
        mTv_lcd.setBackgroundColor(Color.RED);
        testTimes = SettingsActivity.lcd_times;
        Log.d(TAG, "initView test times====" + testTimes);

    }

    private void changeBlackGround(int caseID) {
        Log.d(TAG, "changeBlackGround");
        switch (caseID) {
            case 1:
                imageView.setBackgroundColor(Color.RED);
                mTv_lcd.setBackgroundColor(Color.RED);
                break;
            case 2:
                imageView.setBackgroundColor(Color.GREEN);
                mTv_lcd.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                imageView.setBackgroundColor(Color.BLUE);
                mTv_lcd.setBackgroundColor(Color.BLUE);
                break;
            case 4:
                imageView.setBackgroundColor(Color.BLACK);
                mTv_lcd.setBackgroundColor(Color.BLACK);
                break;
            case 0:
                imageView.setBackgroundColor(Color.WHITE);
                mTv_lcd.setBackgroundColor(Color.WHITE);
                break;
        }
    }

}
