package com.example.agingmonkeytestv2.testitem;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.fragment.CameraVideoFragment;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class CameraPreviewTestItem extends AbstractBaseTestItem{

	private long TEST_DURATION = 3000;
    private long WAIT_PREVIEW = 3000;
    private CameraVideoFragment mCameraVideoMainFragment;
    private CameraVideoFragment mCameraVideoSubFragment;
    private boolean mTestCameraMain = true;
    private int mTimes = 0;
    private String mTimesString;
    private TextView mTimesTv;
    private final String TAG = "lxn-CameraPreviewTestItem";
	
	public CameraPreviewTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
    public boolean execTest(Handler handler) {
        this.mTimes++;
        handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
        SystemClock.sleep(this.WAIT_PREVIEW);
        SystemClock.sleep(this.TEST_DURATION);
        return false;
    }
    
    public void setUp() {
        this.mTimesTv.setText(this.mTimesString + (this.mTimes / 2));
        doCameraSwitch();
    }
    
    public void tearDown() {
        super.tearDown();
        if (!(this.mCameraVideoMainFragment == null || this.mTestCameraMain)) {
            this.mCameraVideoMainFragment = null;
        }
        if (this.mCameraVideoSubFragment != null && this.mTestCameraMain) {
            this.mCameraVideoSubFragment = null;
        }
    }
    
    protected void initView(View view) {
    	int i = 0;
        this.mTimesTv = (TextView) view.findViewById(R.id.times);
        this.mTimesString = view.getContext().getResources().getString(R.string.test_times);
        
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    	if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ) {
    		this.mCameraVideoMainFragment = CameraVideoFragment.newInstance(CameraInfo.CAMERA_FACING_BACK);
    	} else {
    		this.mCameraVideoMainFragment = CameraVideoFragment.newInstance(CameraInfo.CAMERA_FACING_FRONT);
    	}
    }
    
    private void doCameraSwitch() {
    	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    	Log.e(this.TAG, "(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK )====="+(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ));
    	if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ) {
    		Log.e(this.TAG, "CameraInfo.CAMERA_FACING_BACK");
    		this.mActivity.getFragmentManager().beginTransaction().replace(R.id.container, this.mCameraVideoMainFragment).commit();
    	} else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT){
    		Log.e(this.TAG, "CameraInfo.CAMERA_FACING_FRONT");
    		this.mActivity.getFragmentManager().beginTransaction().replace(R.id.container, this.mCameraVideoSubFragment).commit();
    	} else {
    		throw new RuntimeException("some exception happened in camera preview test");
    	}
    	
    }

}
