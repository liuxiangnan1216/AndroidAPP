package com.example.agingmonkeytestv2.broadcast;

import java.util.Date;

import com.example.agingmonkeytestv2.service.AutoRunService;
import com.example.agingmonkeytestv2.util.ObjectWriterReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/

public class RebootCompletedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Log.d("lxn-RebootCompletedReceiver", "a reboot occurs and completed at" + new Date().toString());
			Bundle bundle = ObjectWriterReader.read();
			if (bundle != null) {
				Intent serviceIntent = new Intent();
				serviceIntent.setClass(context, AutoRunService.class);
				serviceIntent.putExtras(bundle);
				context.startService(serviceIntent);
				return;	
			}
			Log.d("lxn-RebootCompletedReceiver", "reboot not caused by factory test");
            Log.d("lxn-RebootCompletedReceiver", "if you are running the factory test, please pay attention to this reboot");
		}
		
	}
	

}
