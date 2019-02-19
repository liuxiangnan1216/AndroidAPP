package com.example.lxn.sagerealmicloop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-2-19.
 ************************************************************************/



public class AudioLoopback extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "lxn-AudioLoopback";

    private AudioManager mAudioManager;
    final static String openMic1 = "SET_LOOPBACK_TYPE=1,1";//主MIC和EARPHONE
    //    final static String openMic2 = "SET_LOOPBACK_TYPE=1,1";//主MIC和RECEIVER
//    final static String openMic3 = "SET_LOOPBACK_TYPE=1,3";//主MIC和SPEAKE
    final static String closeMic = "SET_LOOPBACK_TYPE=0";

    private final static int OPEN_MIC1 = 1;
    private final static int OPEN_MIC2 = 2;
    private final static int OPEN_MIC3 = 3;
    private final static int NEXT_BUTTON_ENABLE = 4;

    private IntentFilter intentFilter;

    private Button mBtnStart;
    private Button mBtnStop;
    private TextView tv;
    public Chronometer timer;

    private String timeStr = "";
    private boolean isStopCount = false;

    private boolean isStart = false;
    public long timer1 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_test);

        tv = (TextView) findViewById(R.id.tv);
        timer = (Chronometer) findViewById(R.id.chronometerTime);

        mBtnStart = (Button) findViewById(R.id.btnstart);
        mBtnStart.setOnClickListener(this);

        mBtnStop = (Button) findViewById(R.id.btnstop);
        mBtnStop.setOnClickListener(this);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnstart:
                Log.d(TAG, "btn start");
                if (!isStart) {
                    startMicSpeaker();
                    isStart = true;
                }

                break;
            case R.id.btnstop:
                Log.d(TAG, "btn stop");
                mAudioManager.setParameters(closeMic);
                mHandler.removeMessages(1);
                isStopCount = true;
                isStart = false;
                timer.stop();
                timer1 = SystemClock.elapsedRealtime() - timer.getBase();//计时时间：当前时刻时间减去暂停时刻的时间，继续计时
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "msg ==" + msg.what);
            switch (msg.what) {
                case OPEN_MIC1:
                    mAudioManager.setParameters(openMic1);
                    break;
//                case OPEN_MIC0:
//                    mAudioManager.setParameters(openMic0);
//                    break;
            }
        }
    };

//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            synchronized(this){
//                if (mAudioManager.isWiredHeadsetOn()) {
//                    if (!flag1) {
//                        flag1 = true;
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //}
//                        Log.d(TAG, "openMic==" + openMic1);
//                        Message msg = Message.obtain(mHandler, OPEN_MIC1);
//                        mHandler.sendMessage(msg);
//                    }
//                } else {
//
//                }
//            }
//        }
//    };



    class HeadsetBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            System.out.println("action = " + action);
            if (!Intent.ACTION_HEADSET_PLUG.equals(action)) {
//                new Thread(runnable).start();
            }
        }

    }


    private void startMicSpeaker() {
        Log.d(TAG, "startMicSpeaker");
        isStopCount = false;
        Timer timerMic = new Timer();
        timerMic.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain(mHandler, OPEN_MIC1);
                mHandler.sendMessage(msg);
            }
        }, 500);

        timer.setBase(SystemClock.elapsedRealtime() - timer1);
        timer.setFormat("00:%s");
        timer.start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        super.onKeyDown(keyCode, keyEvent);
        return true;
    }


    protected void onDestory() {
        super.onDestroy();
        mAudioManager.setParameters(closeMic);
        mHandler.removeMessages(1);
        isStopCount = true;
        isStart = false;

    }


    protected void onPause() {
        super.onPause();
        mAudioManager.setParameters(closeMic);
        mHandler.removeMessages(1);
        isStopCount = true;
        isStart = false;
    }
}
