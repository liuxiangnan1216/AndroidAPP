package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;



/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class BackLightTestItem extends AbstractBaseTestItem{

    private static final String TAG = "lxn-BackLightTestItem";

    private int mBrightnessMode;
    private TextView textView;
    private int test_count = 0;
    private int test_times = 0;
    private int showCount = 1;

    private CompletedActivity completedActivity = new CompletedActivity();

    public BackLightTestItem(int resLayoutId) {
        super(resLayoutId);
        Log.d(TAG, "BackLightTestItem");
    }

    @Override
    public boolean execTest(Handler handler) {
//        Log.d(TAG, "execTest");
        if (test_count < test_times) {
            test_count++;
            setUp();
            return false;
        } else {
            if (test_times != 0) {
                mHanlder.sendEmptyMessage(2);
            }
            return true;
        }
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");

        this.mBrightnessMode = Settings.System.getInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, -1);
        if (this.mBrightnessMode == 1) {
            Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
        }
        textView = (TextView) view.findViewById(R.id.tv_blacklight);
        test_times = SettingsActivity.backlight_times;
        Log.d(TAG, "initview test_times====" + test_times);

    }

    public void setUp() {
        super.setUp();
        Log.d(TAG, "setUp");
        changeScreenBrightness();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    textView.setText("正在测试第：" + (showCount++) + " 次");
                    break;
                case 2:
                    textView.setText("测试结束");
                    onTestEnd();
                    completedActivity.backlightStatus = "PASS";
                    break;

            }
            super.handleMessage(msg);
        }

    };


    public void changeScreenBrightness() {
        Log.d(TAG, "changeScreenBrightness");

        mHanlder.sendEmptyMessage(1);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 30);
        SystemClock.sleep(500);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 70);
        SystemClock.sleep(500);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 100);
        SystemClock.sleep(500);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 150);
        SystemClock.sleep(500);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 200);
        SystemClock.sleep(500);
        Settings.System.putInt(this.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        SystemClock.sleep(500);
    }
}
