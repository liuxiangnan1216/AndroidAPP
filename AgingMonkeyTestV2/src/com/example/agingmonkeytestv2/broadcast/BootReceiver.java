package com.example.agingmonkeytestv2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/


public class BootReceiver extends BroadcastReceiver {
    String TAG = "lxn-BootReceiver";
    public void onReceive(Context context, Intent intent) {
    	
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
        	
        	int num=Settings.Global.getInt(context.getContentResolver(), "rebootaccount", 0);
        	int rebootFlag=Settings.Global.getInt(context.getContentResolver(), "rebootflag", 0);
        	android.util.Log.d("jiangqiao","BootReceivernum"+num);
        	 if(num>1){
        	     num--;
        	    Settings.Global.putInt(context.getContentResolver(), "rebootaccount",num);
        	    if(num != 0 && rebootFlag == 1)
        	    {
                    Log.d(this.TAG, "enter BOOT_COMPLETED if");
                    Intent reboot = new Intent("android.intent.action.REBOOT");
                    reboot.putExtra("android.intent.extra.KEY_CONFIRM", false);
                    reboot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(reboot);	
        	    }
        	}else{
        	 Settings.Global.putInt(context.getContentResolver(), "rebootaccount",0);
        	 Settings.Global.putInt(context.getContentResolver(), "rebootflag", 0);
        	}
        		
        }
    }
}
