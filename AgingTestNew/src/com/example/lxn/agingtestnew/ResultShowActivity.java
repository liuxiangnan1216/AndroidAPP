package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.lxn.agingtestnew.view.ScreenView;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-12.
 ************************************************************************/


public class ResultShowActivity extends Activity{


    private static final String TAG = "lxn-ResultShowActivity";

    private ScreenView screenView;
    public float widht;
    public float height;
    private Handler handler = new Handler();
    private PowerManager.WakeLock mWakeLock;
    private int count = 0;

    private float x = (int)Math.random() * 100;
    private float y = (int)Math.random() * 100;
    private boolean isPlusX = true;
    private boolean isPlusY = true;
    private float speed = 2;

    public static String resut_test = "NoExecute";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_resultshow);

        screenView = (ScreenView) findViewById(R.id.view);

        widht = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();

        Log.d(TAG, "888888888" + widht);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeLock");
        }

//        screenView.setText(resut_test);

        handler.postDelayed(task, 500);


    }


    public void onResume() {
        super.onResume();
        screenView.setText(resut_test);
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }

    }

    public void onPause() {
        super.onPause();
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }


    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            changeXY();
            Log.d(TAG, "Runnable");
            handler.postDelayed(this, 10);

        }
    };


    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }

    public void changeXY() {
        int r = (int)(Math.random() * 255);
        int g = (int)(Math.random() * 255);
        int b = (int)(Math.random() * 255);
        if (r == 0 && g == 255 && b ==0){
            g = 0;
        }
        if (r == 255 && g == 0 && b ==0){
            r = 0;
        }



        if (isPlusX) {
            x += speed;
            if (x >= widht - screenView.mTextSize * 2) {
                isPlusX = false;
                screenView.setBackgroundColor(Color.rgb(r, g, b));
            }
        } else {
            x-= speed;
            if (x <= 0) {
                isPlusX = true;
                screenView.setBackgroundColor(Color.rgb(r, g, b));
            }
        }

        if (isPlusY) {
            y += speed;
            if (y >= height) {
                isPlusY = false;
                screenView.setBackgroundColor(Color.rgb(r, g, b));
            }
        } else {
            y -= speed;
            if (y <= 0 + screenView.mTextSize / 2) {
                isPlusY = true;
                screenView.setBackgroundColor(Color.rgb(r, g, b));
            }
        }
        screenView.setXY(x, y);
    }
}
