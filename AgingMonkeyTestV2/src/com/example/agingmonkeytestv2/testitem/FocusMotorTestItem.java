package com.example.agingmonkeytestv2.testitem;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.agingmonkeytestv2.FactoryApplication;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.util.ShellUtils;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class FocusMotorTestItem extends AbstractBaseTestItem{
	
	private static final String COMMAND_CAT_AFI_CODE = "cat /sys/class/af1_code/af1_code/af1_code";
    private TextView mAfiCodeTextView;
    private Camera mCamera;
    private SurfaceView mSurface;
    private String isFacuse = "false ";
    private int testContent = 1;

	public FocusMotorTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
	
    protected void initView(View view) {
        this.mSurface = (SurfaceView) view.findViewById(R.id.preview);
        this.mSurface.getHolder().addCallback(this.mCallBack);
        this.mAfiCodeTextView = (TextView) view.findViewById(R.id.afi_code);
    }
	
    private Camera.AutoFocusCallback mAutoFocusCallBack = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            Log.d("AutoFocus", "Camera.AutoFocusCallback===" + success);
            if (success) {
            	isFacuse = "true";
            }
            if (FocusMotorTestItem.this.mActivity != null) {
                FocusMotorTestItem.this.mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                    	Log.d("AutoFocus", "FocusMotorTestItem.getAfiCode()===" + FocusMotorTestItem.getAfiCode());
                    	if(FocusMotorTestItem.getAfiCode() != -1){
                    		
                    		FocusMotorTestItem.this.mAfiCodeTextView.setText("afi_code:"+FocusMotorTestItem.getAfiCode());
                    	} else {
                    		FocusMotorTestItem.this.mAfiCodeTextView.setText("focus: " + isFacuse + "  test times:"+testContent);
                    		
                    	}
                    }
                });
            }
        }
    };
    
    
    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            FocusMotorTestItem.this.mCamera = Camera.open();
            if (FocusMotorTestItem.this.mCamera != null) {
                Camera.Parameters parameters = FocusMotorTestItem.this.mCamera.getParameters();
                Camera.Size closer = FocusMotorTestItem.getCloselyPreSize(true, FocusMotorTestItem.this.mSurface.getWidth(), FocusMotorTestItem.this.mSurface.getHeight(), parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(closer.width, closer.height);
                FocusMotorTestItem.this.mCamera.setParameters(parameters);
                FocusMotorTestItem.setCameraDisplayOrientation(FocusMotorTestItem.this.mActivity, 0, FocusMotorTestItem.this.mCamera);
                try {
                    FocusMotorTestItem.this.mCamera.setPreviewDisplay(FocusMotorTestItem.this.mSurface.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FocusMotorTestItem.this.mCamera.startPreview();
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };
    
    public boolean execTest(Handler handler) {
        if (this.mCamera != null) {
            this.mCamera.autoFocus(this.mAutoFocusCallBack);
            //-----------------------
        	Log.d("lxn--FocusMotorTestItem","ischangedFocuse==isSmoothZoomSupported()===="+this.mCamera.getParameters().isSmoothZoomSupported());
        	Log.d("lxn--FocusMotorTestItem","ischangedFocuse==isZoomSupported()===="+this.mCamera.getParameters().isZoomSupported());
        	//-----------------------
        }
        testContent++;
        if (testContent > Math.pow(2, 20) ){
        	testContent = 0;
        }
        SystemClock.sleep(1000);
        return false;
    }
    
    private static int getAfiCode() {
        ShellUtils.CommandResult result = ShellUtils.execCommand(COMMAND_CAT_AFI_CODE, false);
        Log.d("lxn--FocusMotorTestItem", "getAfiCode==result=="+result);
        if (result.result == 0) {
            boolean res;
            String str = result.successMsg;
            Log.d("AFI_CODE", str);
            Matcher matcher = Pattern.compile("\\d+").matcher(str);
            if (matcher.find() && matcher.find()) {
                res = true;
            } else {
                res = false;
            }
            String value = "";
            if (res) {
                value = matcher.group(0);
            }
            try {
            	Log.d("lxn--FocusMotorTestItem", "Integer.parseInt(value)---"+Integer.parseInt(value));
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e("lxn--FocusMotorTestItem", "NumberFormatException---");
                return -2;
            }
        }
        Log.e("AFI_CODE", result.errorMsg);
        return -1;
    }
	
    

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        int result;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = 0;
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
        }
        if (info.facing == 1) {
            result = (360 - ((info.orientation + degrees) % 360)) % 360;
        } else {
            result = ((info.orientation - degrees) + 360) % 360;
        }
        Log.d("setDisplayOrientation", result + "");
        camera.setDisplayOrientation(result);
    }

    public static Camera.Size getCloselyPreSize(boolean isPortrait, int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {
        int reqTmpWidth;
        int reqTmpHeight;
        if (isPortrait) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        for (Camera.Size size : preSizeList) {
            if (size.width == reqTmpWidth && size.height == reqTmpHeight) {
                return size;
            }
        }
        float reqRatio = ((float) reqTmpWidth) / ((float) reqTmpHeight);
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size2 : preSizeList) {
            float deltaRatio = Math.abs(reqRatio - (((float) size2.width) / ((float) size2.height)));
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size2;
            }
        }
        return retSize;
    }


    public boolean startCamera() {
        Intent cameraIntent = new Intent();
        if (FactoryApplication.isMediatek()) {
            cameraIntent.setClassName("com.mediatek.camera", "com.android.camera.CameraLauncher");
        } else {
            cameraIntent.setClassName("com.android.camera2", "com.android.camera.CameraLauncher");
        }
        if (this.mContext.getPackageManager().queryIntentActivities(cameraIntent, 0).size() <= 0) {
            return false;
        }
        cameraIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(cameraIntent);
        return true;
    }

    public void onPause() {
        super.onPause();
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

}
