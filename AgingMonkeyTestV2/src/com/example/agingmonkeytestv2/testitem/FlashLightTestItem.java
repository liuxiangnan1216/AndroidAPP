package com.example.agingmonkeytestv2.testitem;

import java.util.ArrayList;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class FlashLightTestItem extends AbstractBaseTestItem{

	
    private long TEST_DURATION = 3000;
    private long WAIT_TIME = 2000;
    private ArrayList<String> mPendingCameraIds = new ArrayList();
    private TextView flashTextView;
    private Camera mCamera;
    Camera.Parameters mParameters;
    
    
	public FlashLightTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
	protected void initView(View view){
		this.flashTextView = (TextView) view.findViewById(R.id.flash_test);
		this.flashTextView.setText("Flash Light Testing ...");
		
	}
	
    public void onTestCompleted() {
        super.onTestCompleted();
        
    }
    
    public boolean execTest(Handler handler) {
    	Log.d("lxn--FlashLightTestItem", "execTest: camera which support flash light size = ");
    	
    	transFlashDisEnable(handler);
    	return true;
    }
    
    
    public void tearDown() {
        super.tearDown();
    }
    
    private void transFlashDisEnable(Handler handler) {
    	handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
    	onFlashLight();
    	SystemClock.sleep(this.TEST_DURATION);
    	
    	offFlashLight();
    	handler.sendEmptyMessage(BaseTestActivity.MSG_SHOW_DISMISS_DIALOG);
        handler.sendEmptyMessage(BaseTestActivity.MSG_TEAR_DOWN);  	
    }
    

    
    private void onFlashLight(){
    	if (mCamera == null) {
    		mCamera = Camera.open();
    	}
    	SystemClock.sleep(1000);
    	mParameters = mCamera.getParameters();
    	mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
    	mCamera.setParameters(mParameters);
    	mCamera.startPreview();
    }
    
    private void offFlashLight(){
    	mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
    	mCamera.setParameters(mParameters);
    	mCamera.stopPreview();
    	mCamera.release();
    	
    	mCamera = null;
    	
    }
	

}
