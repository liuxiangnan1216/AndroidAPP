package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
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


public class VibratorTestItem extends AbstractBaseTestItem {

    private static final String TAG = "lxn-VibratorTestItem";

    private Vibrator vibrator;
    private TextView mTextView;

    private long startTime = 0;
    private long testDuration = 0;

    public VibratorTestItem(int resLayoutId){
        super(resLayoutId);
        Log.d(TAG, "VibratorTestItem");

    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        return true;
    }

    @SuppressLint("ServiceCast")
    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        mTextView = (TextView) view.findViewById(R.id.textView_time);
        testDuration = SettingsActivity.vibrator_time;
        Log.d(TAG, "initView test Duration===" + testDuration);

        vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        mHanlder.postDelayed(task, 0);
        mHanlder.postDelayed(task1, 0);

        startTime = System.currentTimeMillis();

//        initDrawable();

    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @SuppressLint("MissingPermission")
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        vibrator.cancel();
        mHanlder.removeCallbacks(task);
        mHanlder.removeCallbacks(task1);
        mTextView.setText("测试完成！");
        if (testDuration != 0) {
            CompletedActivity.vibratorStatus = "PASS";
        }


    }

    public void tearDown() {
        Log.d(TAG, "tearDown");
        super.tearDown();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHanlder = new Handler() {
        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    vibrator.vibrate(2000);
                    break;
                case 2:
                    String setTime = mTime();
                    mTextView.setText(setTime);
                    break;
                case 3:
                    onTestEnd();
                    onStop();
                    break;

            }
            super.handleMessage(msg);
        }

    };

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            mHanlder.sendEmptyMessage(1);
            mHanlder.postDelayed(this, 3000);//延迟秒,再次执行task本身,实现了亮度变换的效果
        }
    };

    private Runnable task1 = new Runnable() {
        @Override
        public void run() {
            mHanlder.sendEmptyMessage(2);
            mHanlder.postDelayed(this, 100);//延迟秒,再次执行task本身,实现了亮度变换的效果
        }
    };

    private String mTime() {
        Long realTime = System.currentTimeMillis();
        Long diffTime = realTime - startTime;
        Long min = diffTime / (60 * 1000);
        Long sec = (diffTime % (60 * 1000)) / 1000;
        Log.d(TAG, "realTime====" + realTime +
                        "\nstarTime====" + startTime +
                        "\nmin=========" + min +
                        "\n sec========" + sec);
        if (diffTime >= testDuration) {
            mHanlder.sendEmptyMessage(3);
        }

        if (min > 0){
            sec = (((realTime - startTime) / 1000) - (min * 60));
        } else {
            sec = ((realTime - startTime) / 1000);
        }
        String reTime = "已经测试： " + Long.toString(min) + "分" + Long.toString(sec) + "秒";
        return reTime;

    }


}
