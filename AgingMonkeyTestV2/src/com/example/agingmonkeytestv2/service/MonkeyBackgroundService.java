package com.example.agingmonkeytestv2.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.agingmonkeytestv2.CompletedActivity;
import com.example.agingmonkeytestv2.MonkeyEntryActivity;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.util.PreconditionsUtil;
import com.example.agingmonkeytestv2.util.ShellUtils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/

public class MonkeyBackgroundService extends Service{
	
	public static final String ACTION_KILL_MONKEY_TASK = "action_kill_monkey_task";
    public static final long FILE_WATCHER_TIMEOUT = 300;
    public static final int ID_NOF = 35;
    public static final String KILL_MONKEY_BY_ME = "killMonkeyByMe";
    public static final String MONKEY_ALVIE_COMMAND = "ps | grep com.android.commands.monkey";
    public static final String MONKEY_ALVIE_MARKER = "com.android.commands.monkey";
    public static final String MONKEY_PACKAGE_NAME = "com.android.commands.monkey";
    public static final String SERVICE_ALIVE_COMMAND = "dumpsys activity services | grep MonkeyBackgroundService";
    public static final String SERVICE_ALIVE_MARKER = "com.example.agingmonkeytestv2/.service.MonkeyBackgroundService";
    public static final String TAG = "lxn-monkey-MonkeyBackgroundService";
    private long DELAY = 3600000;
    private long PERIOD = this.DELAY;
    private ActivityManager mActivityManager;
    private String mAppVersionFileName = "app_version.txt";
    private long mDurationTimeMills;
    private boolean mFileWatcherStarted;
    private boolean mHasReStarted = false;
    private boolean mHasStarted = false;
    private boolean mHasStopped = false;
    private boolean mIsNeedRestart = false;
    
    private boolean mIsReceiverStop = false;

    private String mMonkeyCommand;
    private Thread mMonkeyRunThread;
    private String mMonkeyRunThreadName = "MonkeyBackgroundThread";
    private NotificationManager mNotificationManager;
    private long mStartTimeMills;
    private String mThreadNameForSerach = "MonkeyBack";
    private Timer mTimer;
    private Timer mTimerSettings;
    private TimerTask mTimerTask;
    
    private String mStartMonkeyTime = "";
    private String mStopMonkeyTime = "";
    
 
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MonkeyBackground Service onCreate at " + SystemClock.elapsedRealtime());
        this.mTimerSettings = new Timer();
        this.mTimerTask = new TimerTask() {
            public void run() {
                PreconditionsUtil.usbMtp(MonkeyBackgroundService.this);
                PreconditionsUtil.developerSetting(MonkeyBackgroundService.this);
                PreconditionsUtil.usbDebug(MonkeyBackgroundService.this);
            }
        };
        this.mTimerSettings.schedule(this.mTimerTask, this.DELAY, this.PERIOD);
        collectDeviceAppsVersion();
    }

    private void init() {
        SharedPreferences preferences = getSharedPreferences(MonkeyEntryActivity.MONKEY_PREFERENCES_NAME, 1);
        this.mStartTimeMills = preferences.getLong(MonkeyEntryActivity.KEY_MONKEY_START_TIMEMILLS, -1);
        this.mDurationTimeMills = preferences.getLong(MonkeyEntryActivity.KEY_MONKEY_DURATION, -1);
        this.mMonkeyCommand = preferences.getString(MonkeyEntryActivity.KEY_MONKEY_COMMAND, "");
        this.mHasStopped = preferences.getBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_STOPPED, false);
        this.mHasStarted = preferences.getBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_STARTED, false);
        this.mHasReStarted = preferences.getBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_RESTARTED, false);
        this.mIsReceiverStop = false;
        Log.d(TAG, 
        		"init values: \n{\n\t mStartTimeMills : " + this.mStartTimeMills 
        		+ "\n\t mDurationTimeMills : " + this.mDurationTimeMills 
        		+ "\n\t mMonkeyCommand : " + this.mMonkeyCommand 
        		+ "\n\t mHasStopped : " + this.mHasStopped 
        		+ "\n\t mHasStarted : " + this.mHasStarted 
        		+ "\n\t mHasReStarted : " + this.mHasReStarted + "\n\n}");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + this.mHasStarted + " isStopped: " + this.mHasStopped + " isAlive: " + checkMonkeyAlive());
        init();
        this.mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        this.mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        registerReceiver(this.mKillTaskReceiver, new IntentFilter(ACTION_KILL_MONKEY_TASK));
        startForeground(1216, createNotification());
        start(getLeftTimeMills());
        return START_STICKY;
    }


    private void runMonkeyCommand() {
        Log.d(TAG, "monkey start command :  " + this.mMonkeyCommand);
        Process process = null;
        
        try {

        	process = Runtime.getRuntime().exec("sh");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(this.mMonkeyCommand);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
            File outLog = new File(getSdCardPath() + "monkey.txt");
            File errorLog = new File(getSdCardPath() + "monkeyerror.txt");
            Log.d(TAG, "getSdCardPath()==="+getSdCardPath());
            if (!outLog.exists()) {
                outLog.createNewFile();
            }
            if (!errorLog.exists()) {
                errorLog.createNewFile();
            }
            FileLoggerThread outLogThread = new FileLoggerThread(outLog, process.getInputStream());
            FileLoggerThread errorLogThread = new FileLoggerThread(errorLog, process.getErrorStream());
            outLogThread.start();
            errorLogThread.start();
            startFileWatcher(outLog.getPath());
            Log.d(TAG, "runMonkeyCommand----lxn");
            while (true) {
                Log.d(TAG, "this.mIsReceiverStop=="+this.mIsReceiverStop+"\n\t this.mHasStopped=="+this.mHasStopped);
                if (/*this.mIsNeedRestart &&*/ this.mHasStopped && this.mIsReceiverStop) {
                    break;
                }
                SystemClock.sleep(5000);
            }
            outLogThread.abort();
            errorLogThread.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (process != null) {
            process.destroy();
        }
        Log.d(TAG, "monkey run end");
    }


    private void collectDeviceAppsVersion() {
        PackageManager pm = getPackageManager();
        HashMap<String, String> versionInfos = new HashMap<String, String>();
        for (PackageInfo info : pm.getInstalledPackages(PackageManager.GET_ACTIVITIES)) {
            versionInfos.put(info.packageName, info.versionName);
        }
        File file = new File(getSdCardPath() + this.mAppVersionFileName);
        //BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, String> entry : versionInfos.entrySet()) {
                writer.write(((String) entry.getKey()) + "\t" + ((String) entry.getValue()));
                writer.newLine();
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
        e.printStackTrace();
    }
    }


    public void start(long timeMills) {
    	Log.d(TAG, "start-------------");
        if (!this.mHasStopped) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer = null;
            }
            if (timeMills < 0) {
                Log.d(TAG, "left time mills < 0, monkey will not run");
                return;
            }
            mStartMonkeyTime = new Date().toString();
            Log.d(TAG , "createKillTimerTask()====");
            Log.d(TAG , "timeMills()===="+timeMills);
            this.mTimer = new Timer();
            this.mTimer.schedule(createKillTimerTask(), timeMills);
            SharedPreferences.Editor editor = getSharedPreferences(MonkeyEntryActivity.MONKEY_PREFERENCES_NAME, 0).edit();
            editor.putBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_STARTED, true);
            editor.apply();
            forceStopMonkey(this.mActivityManager);
            this.mIsNeedRestart = false;
            this.mMonkeyRunThread = new Thread(this.mMonkeyRunThreadName) {
                public void run() {
                    super.run();
                    Log.d(TAG, "star-----run");
                    MonkeyBackgroundService.this.runMonkeyCommand();
                }
            };
            this.mMonkeyRunThread.start();
            Notification notification = createNotification();
            if (notification != null) {
                startForeground(ID_NOF, notification);
            }
        }
    }

    private void startFileWatcher(final String filePath) {
        this.mFileWatcherStarted = true;
        new Thread(new Runnable() {
            public void run() {
                File file = new File(filePath);
                if (file.exists()) {
                    long lastModified = -1;
                    do {
                        SystemClock.sleep(300000);
                        Log.d(MonkeyBackgroundService.TAG, "FileWatcher  file last modified at " + new Date(file.lastModified()).toString());
                        
                        if (lastModified == file.lastModified()) {
                            Log.d(MonkeyBackgroundService.TAG, "FileWatcher: log file not modified, after 300s");
                            Log.d(MonkeyBackgroundService.TAG, "FileWatcher: so i kill monkey and restart it at" + new Date().toString());
                            MonkeyBackgroundService.this.restart();
                            return;
                        } else if (file.lastModified() > 0) {
                            lastModified = file.lastModified();
                        }
                    } while (MonkeyBackgroundService.this.mFileWatcherStarted);
                    return;
                }
                throw new RuntimeException("cannot start watch on a file which not exists");
            }
        }, "FileWatcherThread").start();
        Log.d(TAG, "start file watcher");
    }

    private String getSdCardPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/";
        File rootPath = new File(path);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        return path;
    }
    
    private String getTimeTag() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d-%02d %02d%02d%02d", new Object[]{
                Integer.valueOf(calendar.get(Calendar.MONTH) + 1),
                Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                Integer.valueOf(calendar.get(Calendar.MINUTE)),
                Integer.valueOf(calendar.get(Calendar.SECOND))});
    }



    private void restart() {
        this.mIsNeedRestart = true;
        //String threadId = getThreadId(getProcessId(getPackageName()), this.mThreadNameForSerach);
        this.mMonkeyRunThread = null;
        forceStopMonkey(this.mActivityManager);
        SharedPreferences.Editor editor = getSharedPreferences(MonkeyEntryActivity.MONKEY_PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_RESTARTED, true);
        editor.apply();
        init();
        start(getLeftTimeMills());
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MonkeyBackground Service destroyed at " + new Date().toString());
        unregisterReceiver(this.mKillTaskReceiver);
        this.mNotificationManager.cancel(ID_NOF);
        if (this.mTimerSettings != null) {
            this.mTimerSettings.cancel();
            this.mTimerSettings = null;
        }
        if (this.mTimer != null && this.mHasStarted) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        stopFileWatcher();
        stopForeground(true);
    }

    
    
    private BroadcastReceiver mKillTaskReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(MonkeyBackgroundService.TAG, "Receive kill task action at: " + new Date().toString());
            if (MonkeyBackgroundService.ACTION_KILL_MONKEY_TASK.equals(intent.getAction())) {
                if (intent.getBooleanExtra(MonkeyBackgroundService.KILL_MONKEY_BY_ME, false)) {
                    Log.d(MonkeyBackgroundService.TAG, "kill background monkey, because time is up");
                } else {
                    Log.d(MonkeyBackgroundService.TAG, "kill background monkey, because power key has press down");
                }
                Log.d(MonkeyBackgroundService.TAG, "Service alive " + MonkeyBackgroundService.checkServiceAlive());
                Log.d(MonkeyBackgroundService.TAG, "monkey alvie" + MonkeyBackgroundService.checkMonkeyAlive());
                MonkeyBackgroundService.this.forceStopMonkey(MonkeyBackgroundService.this.mActivityManager);
                Log.d(MonkeyBackgroundService.TAG, "monkey alvie" + MonkeyBackgroundService.checkMonkeyAlive());
                if (MonkeyBackgroundService.this.mTimer != null && MonkeyBackgroundService.this.mHasStarted) {
                    MonkeyBackgroundService.this.mTimer.cancel();
                    MonkeyBackgroundService.this.mTimer = null;
                }
                mStopMonkeyTime = new Date().toString();
                MonkeyBackgroundService.this.stopSelf();
                MonkeyBackgroundService.this.mNotificationManager.cancel(ID_NOF);
                MonkeyBackgroundService.this.mMonkeyRunThread = null;
                SharedPreferences preferences = MonkeyBackgroundService.this.getSharedPreferences(MonkeyEntryActivity.MONKEY_PREFERENCES_NAME, MODE_PRIVATE);
                MonkeyBackgroundService.this.mHasStarted = preferences.getBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_STARTED, false);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(MonkeyEntryActivity.KEY_MONKEY_STOP_TIMEMILLS, SystemClock.elapsedRealtime());
                editor.putBoolean(MonkeyEntryActivity.KEY_MONKEY_HAS_STOPPED, true);
                editor.apply();
                if (MonkeyBackgroundService.this.mHasStarted) {
                    Intent activityCompleted = new Intent(context, CompletedActivity.class);
                    activityCompleted.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activityCompleted.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putString(CompletedActivity.COMPLETE_MESSAGE, MonkeyBackgroundService.this.generateCompleteMessage());
                    activityCompleted.putExtras(bundle);
                    MonkeyBackgroundService.this.startActivity(activityCompleted);
                    Log.d(MonkeyBackgroundService.TAG, "start completed activity at " + new Date().toGMTString());
                }
                MonkeyBackgroundService.this.mHasStarted = false;
                MonkeyBackgroundService.this.mHasStopped = true;
                MonkeyBackgroundService.this.mIsReceiverStop = true;
                stopForeground(true);
            }
        }
    };
    
    
    private class FileLoggerThread extends Thread {
        private boolean mQuit = false;
        private BufferedReader mReader;
        private BufferedWriter mWriter;

        FileLoggerThread(File file, InputStream in)throws IOException {
            if (file == null) {
                throw new IOException("output file cannot be null");
            }
            Log.d(MonkeyBackgroundService.TAG, file.getAbsolutePath());
            file.createNewFile();
            if (in == null) {
                throw new RuntimeException("FileLoggerThread: input stream  cannot be null");
            }
            this.mWriter = new BufferedWriter(new FileWriter(file));
            this.mReader = new BufferedReader(new InputStreamReader(in));
        }

        public void abort() {
            this.mQuit = true;
        }

        public void run() {
            super.run();
            String line = null;
            while(true) {
            	try {
            		if (!this.mQuit) {
            			if (this.mReader != null) {
	            			line = this.mReader.readLine();
		            		if(line == null){
		            			break;
		            		}
            			} else {
							break;
						}
            		}
            		this.mWriter.write(MonkeyBackgroundService.this.getTimeTag() + "\t" + line + "\n");
            		Log.d(TAG, "this.mWriter.write");
            	} catch (IOException e){
            		e.printStackTrace();
            		return;
            	} finally {
            		try {
	            		if (this.mWriter != null){
	            			this.mWriter.close();
	            			this.mWriter = null;
	            		}
	            		if (this.mReader != null) {
	            			this.mReader.close();
	            			this.mReader = null;
	            		}
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            	}
            }
        }
    }

    private void stopFileWatcher() {
        this.mFileWatcherStarted = false;
    }

    public static boolean checkServiceAlive() {
        return checkAlive(SERVICE_ALIVE_COMMAND, SERVICE_ALIVE_MARKER);
    }

    public static boolean checkMonkeyAlive() {
        return checkAlive(MONKEY_ALVIE_COMMAND, "com.android.commands.monkey");
    }

    public static boolean checkAlive(String command, String aliveMarker) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(command, false, true);
        String msg = commandResult.successMsg;
        if (commandResult.result < 0 || msg == null) {
            return false;
        }
        return msg.contains(aliveMarker);
    }

    public IBinder onBind(Intent intent) {
        throw new RuntimeException("MonkeyBackground Service not support bind yet");
    }

    private long getLeftTimeMills() {
        Log.d(TAG, "getLeftTimeMills: " + ((this.mDurationTimeMills - SystemClock.elapsedRealtime()) + this.mStartTimeMills));
        return (this.mDurationTimeMills - SystemClock.elapsedRealtime()) + this.mStartTimeMills;
    }

    private void forceStopMonkey(ActivityManager activityManager) {
        forceStopPackage(activityManager, "com.android.commands.monkey");
        forceStopPackage("com.android.commands.monkey");
    }


    private void forceStopPackage(ActivityManager activityManager, String packageName) {
        Log.d(TAG, "forceStopPackage: force stop " + packageName);
        try {

            activityManager.getClass().getMethod("forceStopPackage", new Class[]{String.class}).invoke(activityManager, new Object[]{packageName});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return;
    }

    private void forceStopPackage(String packageName) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("ps | grep " + packageName, false, true);
        if (commandResult.result >= 0 && commandResult.successMsg != null) {
            String[] list = commandResult.successMsg.split("\\s+");
            if (list.length > 2) {
                String processId = list[1];
                Log.d(TAG, "forceStopPackage: PackageName " + packageName + "  Process Id  " + processId);
                Log.d(TAG, "forceStopPackage: result " + ShellUtils.execCommand("kill " + processId, false).result);
            }
        }
    }

    private String getTimeMillToString(long timeMills) {
        int days = (int) (timeMills / (24 * 60 * 60 * 1000));
        int hours = (int) ((((timeMills - ((long) ((((days * 24) * 60) * 60) * 1000))) / 60) / 60) / 1000);
        int minutes = (int) (((( timeMills - ((((days * 24) *60) * 60) * 1000) - (((hours * 60) * 60) * 1000)) / 60) / 1000));
        int seconds = (int) ((timeMills - ((long) ((((((days * 24) * 60) * 60) + ((hours * 60) * 60)) + (((int) (((timeMills - ((long) (((((days * 24) * 60) + (hours * 60)) * 60) * 1000))) / 60) / 1000)) * 60)) * 1000))) / 1000);
        return String.format(getString(R.string.time_string_format), new Object[]{
                Integer.valueOf(days),
                Integer.valueOf(hours),
                Integer.valueOf(minutes),
                Integer.valueOf(seconds)});
    }

    private String generateCompleteMessage() {
        String string = getString(R.string.monkey_complete_string_format);
        Object[] objArr = new Object[6];
        objArr[0] = this.mStartMonkeyTime;//getTimeMillToString(this.mStartTimeMills);
        objArr[1] = this.mStopMonkeyTime;//getTimeMillToString(SystemClock.elapsedRealtime());
        objArr[2] = getTimeMillToString(SystemClock.elapsedRealtime() - this.mStartTimeMills);
        objArr[3] = getTimeMillToString(this.mDurationTimeMills);
        objArr[4] = getTimeMillToString(this.mStartTimeMills + this.mDurationTimeMills);
        objArr[5] = this.mStartTimeMills + this.mDurationTimeMills >= SystemClock.elapsedRealtime() ? getString(R.string.result_true) : getString(R.string.result_false);

        Log.d(TAG, "generateCompleteMessage==:"
        				+ "\n\t objArr[0]====" + objArr[0]
        				+ "\n\t objArr[1]====" + objArr[1]
        				+ "\n\t objArr[2]====" + objArr[2]
        				+ "\n\t objArr[3]====" + objArr[3]
        				+ "\n\t objArr[4]====" + objArr[4]
        				+ "\n\t objArr[5]====" + objArr[5]
        				);
        
        return String.format(string, objArr);
    }

    private String getProcessId(String packageName) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("ps | grep " + packageName, false, true);
        String processId = null;
        if (commandResult.result >= 0 && commandResult.successMsg != null) {
            String[] list = commandResult.successMsg.split("\\s+");
            if (list.length > 2) {
                processId = list[1];
            }
        }
        Log.d(TAG, "get " + packageName + " processId " + processId);
        return processId;
    }

    private boolean checkPackageExists(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return true;
        }
        return false;
    }


    private String getThreadId(String processId, String threadName) {
        if (processId == null) {
            return null;
        }
        String threadId = null;
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("ps -p " + processId + " -t | grep " + threadName, false, true);
        if (commandResult.result >= 0 && commandResult.successMsg != null) {
            String[] list = commandResult.successMsg.split("\\s+");
            if (list.length > 2) {
                threadId = list[1];
            }
        }
        Log.d(TAG, "get " + threadName + " ThreadId " + threadId);
        return threadId;
    }

    private TimerTask createKillTimerTask() {
    	Log.d(TAG, "createKillTimerTask----run");
        return new TimerTask() {
            public void run() {
                Log.d(MonkeyBackgroundService.TAG, "Timer task run at: " + new Date().toString());
                Intent broadcast = new Intent(MonkeyBackgroundService.ACTION_KILL_MONKEY_TASK);
                broadcast.putExtra(MonkeyBackgroundService.KILL_MONKEY_BY_ME, true);
                MonkeyBackgroundService.this.sendBroadcast(broadcast);
            }
        };
    }

    private Notification createNotification() {
        //try {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentTitle(getString(R.string.app_name_monkey));
            builder.setContentText("monkey run on start at "+new Date().toString());
            Log.d(TAG, "createNotification  setContentText==="+new Date().toString());
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            return notification;
        //} catch (Exception e) {
        //    return null;
        //}
    }
    
//
//    private String getContentString(long timeMills) {
//    	Log.d(TAG,  "getContentString(timeMills)=="+timeMills);
//        int days = (int) (timeMills / (24 * 60 * 60 * 1000));
//        int hours = (int) ((((timeMills - ((long) ((((days * 24) * 60) * 60) * 1000))) / 60) /60 ) / 1000);
//        int minutes = (int) (((( timeMills - ((((days * 24) *60) * 60) * 1000) - (((hours * 60) * 60) * 1000)) / 60) / 1000));
//        int seconds = (int) ((timeMills - ((long) ((((((days * 24) * 60) * 60) + ((hours * 60) * 60)) + (((int) (((timeMills - ((long) (((((days * 24) * 60) + (hours * 60)) * 60) * 1000))) / 60) / 1000)) * 60)) * 1000))) / 1000);
//        Log.d(TAG, "days==" + days
//        		+"\n\t hours=="+hours
//        		+"\n\t minutes==" +minutes
//        		+"\n\t seconds==" +seconds);
//        Log.d(TAG, 
//        		"getContentString: " + String.format(getString(R.string.monkey_start_string_format), 
//        				new Object[]{
//        					Integer.valueOf(days), 
//        					Integer.valueOf(hours), 
//        					Integer.valueOf(minutes), 
//        					Integer.valueOf(seconds)
//        				}));
//        return String.format(getString(R.string.monkey_start_string_format), new Date().toString() );
//    }

}
