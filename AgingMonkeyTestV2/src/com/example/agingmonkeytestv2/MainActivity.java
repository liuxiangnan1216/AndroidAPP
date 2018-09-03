package com.example.agingmonkeytestv2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.service.BatteryTestService;
/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/

public class MainActivity extends Activity {

	public static final long MAX_DELAY_TIME = 1000;
    public static final String TAG = "lxn-MainActivity";
    protected long lastBackPressedTime = -1;
    private Button mFactory;
    private Button mHelp;
    private Button mMonkey;
    private Button mReset;
    private Button mBtnReboot;
    private Intent intent = new Intent();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mMonkey = (Button) findViewById(R.id.monkey);
        this.mFactory = (Button) findViewById(R.id.factory);
        this.mReset = (Button) findViewById(R.id.reset);
        this.mHelp = (Button) findViewById(R.id.tips);
        this.mBtnReboot = (Button) findViewById(R.id.reboot);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        //monkey
        this.mMonkey.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Log.d(TAG, "mMonkey------------------");
				intent.setClass(MainActivity.this, MonkeyEntryActivity.class);
                startActivity(intent);
			}
		});
        
        //factory
        this.mFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "mFactory------------------");
                intent.setClass(MainActivity.this, FactoryEntryActivity.class);
                startActivity(intent);
            }
        });
        
        //reboot
        this.mBtnReboot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "mReboot-----------------");
				intent.setClass(MainActivity.this, RebootTestActivity.class);
				startActivity(intent);
			}
		});

        //reset
        this.mReset.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View view){
        		resetAlertDialog();
        	}
           
        });

        //help
        this.mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "mHelp------------------");
                intent.setClass(MainActivity.this, TipsActivity.class);
                startActivity(intent);

                
            }
        });
    }
    
    protected void onResume() {
        super.onResume();
        //PermissionUtil.checkAndRequestPermissions(this);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        if (System.currentTimeMillis() - this.lastBackPressedTime > 1000) {
            //Toast.makeText(this, getString(R.string.double_back_exit), Toast.LENGTH_SHORT).show();
            this.lastBackPressedTime = System.currentTimeMillis();
        } else {
            if (BatteryTestService.checkSelfAlive()) {
                Intent mServiceIntent = new Intent();
                mServiceIntent.setClass(this, BatteryTestService.class);
                stopService(mServiceIntent);
            }
            finish();
        }
        return true;
    }


    private void resetAlertDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	builder.setTitle(R.string.reset_confirm_message);
    	builder.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
    		public void onClick(android.content.DialogInterface dialog, int which) {
    			Intent intent = new Intent("com.android.internal.os.storage.FORMAT_AND_FACTORY_RESET");
    			intent.setComponent(new android.content.ComponentName("android", "com.android.internal.os.storage.ExternalStorageFormatter"));
    			MainActivity.this.startService(intent);
				Log.d(TAG, "resetAlertDialog--sendBroadcast");
    			MainActivity.this.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
    		}
    	});
    	builder.setNegativeButton(android.R.string.cancel, null);
    	builder.create().show();
    }

}
