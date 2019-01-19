package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class GpsTestItem extends AbstractBaseTestItem{
    private static final String TAG = "lxn-GpsTestItem";

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv_timer;
    private LocationManager locationManager;
    private Context context;
    private long startOnTime;
    private long currentOnTime;
    private long startOffTime;
    private long currentOffTime;
    private int testTimes = 4;
    private int whileTimes = 60;
    private Location location;
    private Criteria criteria = new Criteria();
    private Handler mHandler = new Handler();
    private long timer = 0;
    private String timeStr = "";
    private boolean isStopCount = false;



    public GpsTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        Log.d(TAG, "execTest timer===" + timer);
        if (isStopCount) {
            onTestEnd();
        }
        if (!isStopCount) {
            updateLocation(location);
            return false;
        } else {
            if (location == null){
                tv3.setText("未搜到有效的星");
                CompletedActivity.gpsStatus = "FAIL";
            } else {
                tv3.setText("测试OK！");
                CompletedActivity.gpsStatus = "PASS";
            }
            onTestEnd();
            disableGPS();
            return true;
        }

    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }


    public void onTestEnd() {
        super.onTestEnd();
        Log.d(TAG, "onTestEnd");
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        context = view.getContext();
        tv1 = (TextView) view.findViewById(R.id.tv_gps1);
        tv2 = (TextView) view.findViewById(R.id.tv_gps2);
        tv3 = (TextView) view.findViewById(R.id.tv_gps3);
        tv_timer = (TextView) view.findViewById(R.id.tv_timer);

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(false);

        countTimer();


        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);

    }

    @SuppressLint("MissingPermission")
    public void onResume() {
        super.onResume();
        while (testTimes >= 0) {
            Log.d(TAG, "initview testTimes==" + testTimes);
            testTimes--;
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                disableGPS();
            } else {
                enableGPS();
            }
        }
        SystemClock.sleep(500);
        enableGPS();
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        updateLocation(location);
    }

    @SuppressLint("MissingPermission")
    public void updateLocation(Location location) {

        if (location != null) {
//            tv3.setText(String.format("经度:%s\n纬度:%s\n海拔:%s", location.getLongitude(), location.getLatitude(), location.getAltitude()) + "\n测试OK！");
            tv3.setText("测试OK！");
        } else {
            tv3.setText("正在搜星...");
        }
    }


    @SuppressLint("MissingPermission")
    public void disableGPS() {
        Log.d(TAG, "disableGPS");
        startOnTime = SystemClock.currentThreadTimeMillis();
        currentOnTime = startOnTime;
        Settings.Secure.setLocationProviderEnabled(context.getContentResolver(), LocationManager.GPS_PROVIDER, false);//关闭GPS

        while ((currentOnTime - startOnTime) < 30 *1000) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100,0, locationListener);
                tv1.setText("GPS 关闭OK！");
                break;
            }
            SystemClock.sleep(500);
            currentOnTime = SystemClock.currentThreadTimeMillis();
        }

    }


    @SuppressLint("MissingPermission")
    public void enableGPS() {
        Log.d(TAG, "enableGPS");
        startOffTime = SystemClock.currentThreadTimeMillis();
        currentOffTime = startOffTime;
        Settings.Secure.setLocationProviderEnabled(context.getContentResolver(), LocationManager.GPS_PROVIDER, true);

        while ((currentOffTime - startOffTime) < 30 * 1000) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100,0, locationListener);
                tv2.setText("GPS 开启OK！");
                break;
            }
            SystemClock.sleep(500);
            currentOffTime = SystemClock.currentThreadTimeMillis();
        }

    }

    private LocationListener locationListener = new LocationListener() {
        /**
         * 位置信息变化时触发:当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         */
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged");
//            tv3.setText(String.format("时间:%d\n经度:%s\n纬度:%s\n海拔:%s", location.getTime(), location.getLongitude(), location.getLatitude(), location.getAltitude()));
        }

        /**
         * GPS状态变化时触发:Provider被disable时触发此函数，比如GPS被关闭
        */
         @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
             Log.d(TAG, "onStatusChanged");

        }

        /**
         * 方法描述：GPS开启时触发
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
//            tv2.setText("GPS 开启OK！");
        }

        /**
         * 方法描述： GPS禁用时触发
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
//            tv1.setText("GPS 关闭OK！");
        }
    };

    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isStopCount) {
                timer += 1000;
                timeStr = getFormatHMS(timer);
                tv_timer.setText(timeStr);
                if (timer == (30 *1000)) {
                    isStopCount = true;
                }
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//时
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
