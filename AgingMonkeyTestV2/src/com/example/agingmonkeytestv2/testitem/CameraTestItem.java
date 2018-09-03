package com.example.agingmonkeytestv2.testitem;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.fragment.CameraFragment;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class CameraTestItem extends AbstractBaseTestItem{

    private long TEST_DURATION = 5000;
    private long WAIT_PREVIEW = 5000;
    private CameraFragment mFragment;
    private int mTimes = 0;
    private String mTimesString;
    private TextView mTimesTv;
    
    private final static String TAG = "lxn-CameraTestItem";
    
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    
	public CameraTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
    public boolean execTest(Handler handler) {
        SystemClock.sleep(this.WAIT_PREVIEW);
        this.mFragment.takePicture();
        this.mTimes++;
        handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
        SystemClock.sleep(this.TEST_DURATION);
        return false;
    }
    
    public void setUp() {
        super.setUp();
        this.mTimesTv.setText(this.mTimesString + this.mTimes);
    }
    
    protected void initView(View view) {
        this.mTimesTv = (TextView) view.findViewById(R.id.times);
        this.mTimesString = view.getContext().getResources().getString(R.string.test_times);
        
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ) {
    		this.mFragment = CameraFragment.newInstance(CameraInfo.CAMERA_FACING_BACK);
    		this.mActivity.getFragmentManager().beginTransaction().replace(R.id.container, this.mFragment).commit();
    	} 
    }

	
}
