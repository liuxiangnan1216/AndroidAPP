package com.example.mycoolweather.receiver;

import com.example.mycoolweather.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	Intent i = new Intent(context , AutoUpdateService.class);//接收到广播再次启动AutoUpdateReceiver服务
	context.startService(i); 
    }

}
