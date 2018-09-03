package com.example.agingmonkeytestv2.testitem;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.service.AutoRunService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-10.
 ************************************************************************/


public abstract class AbstractBaseTestItem implements TestItem{
    private static final int DURATION_TIMES = 1;
    public static final long MAX_DELAY_TIME = 1000;
    protected String TAG = "lxn-AbstractBaseTestItem";
    protected long lastBackPressedTime = -1;
    protected Activity mActivity;
    protected Context mContext;
    //protected LocalBroadcastManager mLocalBroadcastManager;
    private int mResLayoutId = -1;
    private boolean mResult = false;
    protected boolean mStopTest = false;

    public abstract boolean execTest(Handler handler);

    protected abstract void initView(View view);

    public AbstractBaseTestItem(int resLayoutId) {
        this.mResLayoutId = resLayoutId;
    }

    public final void test(final Handler handler) {
    	Log.d(TAG, "--test--");
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



    public final void testByTimes(final Handler handler, final int times) {
    	Log.d(TAG, "--testByTimes--times=="+times);
        AutoRunService.runTest(new Runnable() {
        	
            public void run() {
            	handler.sendEmptyMessage(BaseTestActivity.MSG_START);
                for (int i = 0; i < times; i++) {
                    if (AbstractBaseTestItem.this.mStopTest) {
                        Log.d(TAG, "Test Item stopped by user");
                        break;
                    }
                    AbstractBaseTestItem.this.mResult = AbstractBaseTestItem.this.execTest(handler);
                    Log.d(TAG, "test item run current times: " + i + ", total: " + times);
                }
                handler.sendEmptyMessage(BaseTestActivity.MSG_COMPLETED);
                if (AbstractBaseTestItem.this.mStopTest) {
                	Log.d(TAG, "AbstractBaseTestItem.this.mStopTest----");
                    AbstractBaseTestItem.this.mActivity.finish();
                    //AbstractBaseTestItem.this.mLocalBroadcastManager.sendBroadcast(new Intent(AutoRunService.ACTION_FACTORY_TEST_STOP));
                    mContext.sendBroadcast(new Intent("action_factory_test_stop"));
                    AbstractBaseTestItem.this.tearDown();
                    return;
                }
                Log.d(TAG, "call test activity finish");
                AbstractBaseTestItem.this.mActivity.finish();
                //AbstractBaseTestItem.this.mLocalBroadcastManager.sendBroadcast(new Intent(AutoRunService.ACTION_FACTORY_TEST_NEXT));
                mContext.sendBroadcast(new Intent("action_factory_test_next"));
            }
        });
    }


    public final void testByDuration(Handler handler) {
    	Log.d(TAG, "--testByDuration--");
        testByTimes(handler, 1);
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
        //TODO this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this.mContext);
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
        Toast.makeText(this.mActivity, this.mContext.getString(R.string.double_back_exit), Toast.LENGTH_SHORT).show();
        return false;
    }
}
