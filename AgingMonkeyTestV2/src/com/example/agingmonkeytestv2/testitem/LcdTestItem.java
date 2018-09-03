package com.example.agingmonkeytestv2.testitem;

import java.util.ArrayList;
import java.util.Random;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;

import android.content.res.Resources;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class LcdTestItem extends AbstractBaseTestItem{
	
	private long TEST_DURATION = 2000;
	private ArrayList<Integer> drawableIds = new ArrayList();
	private int mBrightnessMode;
	private TextView mFullScreenView;
	private static final String TAG = "lxn--LcdTestItem";
	

	public LcdTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean execTest(Handler handler){
		Log.d(TAG, "execTest");
        handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
        SystemClock.sleep(this.TEST_DURATION);
        return true;
	}
	
	public void initView(View view){
		Log.d(TAG, "initView");
		this.mFullScreenView = (TextView) view.findViewById(R.id.lcd);
		this.mContext = view.getContext();
		this.mBrightnessMode = Settings.System.getInt(this.mContext.getContentResolver(), "screen_brightness_mode", -1);
		if (this.mBrightnessMode == 1) {
			Settings.System.putInt(this.mContext.getContentResolver(), "screen_brightness_mode", 0);
		}
		initDrawableIds();
	}
	
	private void initDrawableIds() {
		Log.d(TAG, "initDrawableIds");
		this.drawableIds.clear();
		Resources res = this.mContext.getResources();
		for (int start = 97; start < 116; start++) {
			this.drawableIds.add(Integer.valueOf(res.getIdentifier(((char) start) + "", "drawable", this.mContext.getPackageName())));
		}	
	}
	
    public void setUp() {
        Log.d(TAG, "setUp");
        super.setUp();
        changeViewBackground();
        changeScreenBrightness();
    }

    private void changeViewBackground() {
        Log.d(TAG, "changeViewBackground");
        this.mFullScreenView.setBackgroundResource(((Integer) this.drawableIds.get(new Random().nextInt(this.drawableIds.size()))).intValue());
    }

    private void changeScreenBrightness() {
        Log.d(TAG, "changeScreenBrightness");
        Settings.System.putInt(this.mContext.getContentResolver(), "screen_brightness", new Random().nextInt(255));
    }

    public void onTestCompleted() {
        Log.d(TAG, "onTestCompleted");
        super.onTestCompleted();
        Settings.System.putInt(this.mContext.getContentResolver(), "screen_brightness_mode", this.mBrightnessMode);
    }
	

}