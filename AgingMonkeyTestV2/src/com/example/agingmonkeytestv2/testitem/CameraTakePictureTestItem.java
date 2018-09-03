package com.example.agingmonkeytestv2.testitem;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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

public class CameraTakePictureTestItem extends AbstractBaseTestItem{

	private long TEST_DURATION = 400;
    private long WAIT_PREVIEW = 3000;
    private int mAudioClickId = -1;
    private CameraFragment mCameraMainFragment;
    private CameraFragment mCameraSubFragment;
    private SoundPool mSoundPool;
    private boolean mTestCameraMain = true;
    private int mTimes = 0;
    private String mTimesString;
    private TextView mTimesTv;
    private String TAG = "lxn-CameraTakePictureTestItem";
    
    public CameraTakePictureTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
    
    
    public boolean execTest(Handler handler){
    	this.mTimes++;
    	handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
    	SystemClock.sleep(this.WAIT_PREVIEW);
    	for (int i = 0; i < 5 && !this.mStopTest; i++) {
    		if (this.mTestCameraMain){
    			this.mCameraMainFragment.takePicture();
                this.mSoundPool.play(this.mAudioClickId, 1.0f, 1.0f, 0, 0, 1.0f);
    		}
    		SystemClock.sleep(2000);
    	}
    	if (!this.mStopTest) {
    		SystemClock.sleep(this.TEST_DURATION);
    	}
    	return false;
    }
    
    public void setUp() {
    	this.mTimesTv.setText(this.mTimesString + (this.mTimes / 2));
    	doCameraSwitch();
    }
    
    public void onTestCompleted() {
    	super.onTestCompleted();
    	if (this.mSoundPool != null) {
    		this.mSoundPool.release();
    		this.mSoundPool = null;
    	}
    }
    
    protected void initView(View view) {
    	this.mTimesTv = (TextView) view.findViewById(R.id.times);
    	this.mTimesString = view.getContext().getResources().getString(R.string.test_times);
    	mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 10);
    	mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// TODO Auto-generated method stub
				Log.d(CameraTakePictureTestItem.this.TAG, "Load Completed " + sampleId);
			}
		});
    	this.mAudioClickId = this.mSoundPool.load(view.getContext(), R.raw.camera_click, 1);
    	
    	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    	if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ) {
    		this.mCameraMainFragment = CameraFragment.newInstance(CameraInfo.CAMERA_FACING_BACK);
    	} else {
    		this.mCameraMainFragment = CameraFragment.newInstance(CameraInfo.CAMERA_FACING_FRONT);
    	}
    }
    
    private void doCameraSwitch() {
    	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    	Log.d(TAG, "(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK )====="+(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ));
    	if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK ) {
    		Log.d(TAG, "CameraInfo.CAMERA_FACING_BACK");
    		this.mActivity.getFragmentManager().beginTransaction().replace(R.id.container, this.mCameraMainFragment).commit();
    	} else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT){
    		Log.d(TAG, "CameraInfo.CAMERA_FACING_FRONT");
    		this.mActivity.getFragmentManager().beginTransaction().replace(R.id.container, this.mCameraSubFragment).commit();
    	} else {
    		throw new RuntimeException("some exception happened in camera preview test");
    	}
    	
    }
    
}
