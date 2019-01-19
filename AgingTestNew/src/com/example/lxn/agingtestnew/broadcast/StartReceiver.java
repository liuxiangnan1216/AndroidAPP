package com.example.lxn.agingtestnew.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-18.
 ************************************************************************/


public class StartReceiver extends BroadcastReceiver {

    private final String SECRET_CODE = "android.provider.Telephony.SECRET_CODE";

    private final Uri chineseUri = Uri.parse("android_secret_code://1234");
    private final Uri englishUri = Uri.parse("android_secret_code://1234");

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SECRET_CODE.equals(action)) {
            Uri uri = intent.getData();
            try {
                Intent AgingIntent = new Intent();
                AgingIntent.setComponent(new android.content.ComponentName("com.example.lxn.agingtestnew", "com.example.lxn.agingtestnew.MainActivity"));
                AgingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (uri.equals(chineseUri)) {
                    AgingIntent.putExtra("language", "C");
                } else if (uri.equals(englishUri)) {
                    AgingIntent.putExtra("language", "E");
                }
                context.startActivity(AgingIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
