package com.example.lxn.myscanqr;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxn.myscanqr.utils.StatusBarUtils;
import com.google.zxing.CaptureActivity;

import static android.os.SystemClock.sleep;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-5-16.
 ************************************************************************/

public class CustomCaptureActivity extends CaptureActivity {
    private static final String TAG = "lxnSQR-CustomCaptureActivity";

    private boolean isContinuousScan;
    @Override
    public int getLayoutId() {
        return R.layout.custom_capture_activity;
    }

    @Override
    public void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        Log.d(TAG, "CustomCaptureActivity");
        Toolbar toolbar = findViewById(R.id.toolbar);
        StatusBarUtils.immersiveStatusBar(this,toolbar,0.2f);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getIntent().getStringExtra(MainActivity.KEY_TITLE));

        isContinuousScan = getIntent().getBooleanExtra(MainActivity.KEY_IS_CONTINUOUS,false);
        //获取CaptureHelper，里面有扫码相关的配置设置
        getCaptureHelper().playBeep(true)//播放音效
                .vibrate(true)//震动
                .continuousScan(isContinuousScan);//是否连扫
    }

    /**
     * 关闭闪光灯（手电筒）
     */
    private void offFlash(){
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    /**
     * 开启闪光灯（手电筒）
     */
    public void openFlash(){
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }


    /**
     * 扫码结果回调
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onResultCallback(String result) {
        Log.d(TAG, "onResultCallback isContinuousScan: " + isContinuousScan);
        if(isContinuousScan){//连续扫码时，直接弹出结果
            Toast.makeText(CustomCaptureActivity.this, result, Toast.LENGTH_SHORT).show();
            sleep(1 * 1000);
        }

        return super.onResultCallback(result);
    }


    private void clickFlash(View v){
        if(v.isSelected()){
            offFlash();
            v.setSelected(false);
        }else{
            openFlash();
            v.setSelected(true);
        }

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ivFlash:
                clickFlash(v);
                break;
        }
    }
}