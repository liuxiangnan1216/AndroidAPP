package com.example.lxn.agingtestnew.testItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.lxn.agingtestnew.BaseTestActivity;
import com.example.lxn.agingtestnew.service.AutoRunService;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/



public abstract class AbstractBaseTestItem implements TestItem {
    private static final int DURATION_TIMES = 1;
    public static final long MAX_DELAY_TIME = 1000;
    protected String TAG = "lxn-AbstractBaseTestItem";
    protected long lastBackPressedTime = -1;
    protected Activity mActivity;
    protected Context mContext;
    protected LocalBroadcastManager mLocalBroadcastManager;
    private int mResLayoutId = -1;
    private boolean mResult = false;
    protected boolean mStopTest = false;
    public boolean isTestEnd = false;



    public abstract boolean execTest(Handler handler);

    protected abstract void initView(View view);

    public AbstractBaseTestItem(int resLayoutId) {
        this.mResLayoutId = resLayoutId;
    }

    public final void test(final Handler handler) {
        Log.d(TAG, "--test--" + AbstractBaseTestItem.this.mStopTest);
        new Thread(new Runnable() {
            public void run() {
                handler.sendEmptyMessage(BaseTestActivity.MSG_START);
                while (!AbstractBaseTestItem.this.mStopTest) {
                    AbstractBaseTestItem.this.mResult = AbstractBaseTestItem.this.execTest(handler);
                    Log.d(TAG, "--mResult--"+AbstractBaseTestItem.this.mResult);

                }
                Log.d(TAG, "Test Item stopped by user");
                AbstractBaseTestItem.this.tearDown();
                handler.sendEmptyMessage(BaseTestActivity.MSG_COMPLETED);
            }
        }).start();
    }


    public final void testAutoRun(final Handler handler) {
        AutoRunService.runTest(new Runnable() {
            @Override
            public void run() {
               handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
               while (true) {
                   if (AbstractBaseTestItem.this.isTestEnd) {
                       Log.d(TAG, "Test Item stopped by user");
                       break;
                   }
                   AbstractBaseTestItem.this.mResult = AbstractBaseTestItem.this.execTest(handler);
                   Log.d(TAG, "test item run auto: ");
               }
//               handler.sendEmptyMessage(BaseTestActivity.MSG_COMPLETED);
//               if (AbstractBaseTestItem.this.mStopTest) {
//                   Log.d(TAG, "AbstractBaseTestItem.this.mStopTest----");
//                   AbstractBaseTestItem.this.mActivity.finish();
//                   AbstractBaseTestItem.this.mLocalBroadcastManager.sendBroadcast(new Intent(AutoRunService.ACTION_AGING_TEST_STOP));
//                   AbstractBaseTestItem.this.tearDown();
//                   return;
//               }
               Log.d(TAG, "call test activity finish");
               AbstractBaseTestItem.this.mActivity.finish();
               AbstractBaseTestItem.this.mLocalBroadcastManager.sendBroadcast(new Intent(AutoRunService.ACTION_AGING_TEST_NEXT));


            }
        });
    }



    public void setUp() {
        this.mResult = false;
        Log.d(TAG, "setUp: " + getClass().getSimpleName());
    }

    public void tearDown() {
        Log.d(TAG, "tearDown");
    }

    public View getView(Activity activity) {
        if (this.mResLayoutId == -1) {
            return null;
        }
        View v = LayoutInflater.from(activity).inflate(this.mResLayoutId, null);
        this.mContext = activity;
        this.mActivity = activity;
        Log.d(TAG, "---getView---");
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this.mContext);
        initView(v);
        return v;
    }

    public void onTestStart() {
        Log.d(TAG, "---onTestStart---");
    }

    public void onTestCompleted() {
        Log.d(TAG , " onTestCompleted");
    }

    public void onCreate() {
        Log.d(TAG , " onCreate");
    }

    public void onResume() {
        Log.d(TAG , " onResume");
        this.mStopTest = false;
    }

    public void onPause() {
        Log.d(TAG , " onPause");
        this.mStopTest = true;
    }

    public void onStop() {
        Log.d(TAG , " onStop");
        this.mStopTest = true;
    }

    public void onTestEnd() {
        Log.d(TAG, "isTestEnd");
        this.isTestEnd = true;
    }

    public void onDestroy() {
        Log.d(TAG , " onDestroy");
    }

    public void onRestart() {
        Log.d(TAG , " onRestart");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG , " onTestCompleted");
        if (keyCode != 4 || isDoubleClick()) {
            return false;
        }
        return true;
    }

    protected boolean isDoubleClick() {
        long current = System.currentTimeMillis();
        if (current - this.lastBackPressedTime < 1000 && this.lastBackPressedTime != -1) {
            return true;
        }
        this.lastBackPressedTime = current;
        Toast.makeText(this.mActivity, "Double click back key to exit!", Toast.LENGTH_SHORT).show();
        return false;
    }
}

