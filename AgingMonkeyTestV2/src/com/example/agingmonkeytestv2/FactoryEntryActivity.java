package com.example.agingmonkeytestv2;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.agingmonkeytestv2.broadcast.CommandField;
import com.example.agingmonkeytestv2.fragment.DatePickerFragment;
import com.example.agingmonkeytestv2.service.AutoRunService;
import com.example.agingmonkeytestv2.testitem.AudioTestItem;
import com.example.agingmonkeytestv2.testitem.BatteryTestItem;
import com.example.agingmonkeytestv2.testitem.BluetoothTestItem;
import com.example.agingmonkeytestv2.testitem.CameraPreviewTestItem;
import com.example.agingmonkeytestv2.testitem.CameraSubTestItem;
import com.example.agingmonkeytestv2.testitem.CameraSubVideoTestItem;
import com.example.agingmonkeytestv2.testitem.CameraTakePictureTestItem;
import com.example.agingmonkeytestv2.testitem.CameraTestItem;
import com.example.agingmonkeytestv2.testitem.CameraVideoTestItem;
import com.example.agingmonkeytestv2.testitem.FlashLightTestItem;
import com.example.agingmonkeytestv2.testitem.FocusMotorTestItem;
import com.example.agingmonkeytestv2.testitem.LcdTestItem;
import com.example.agingmonkeytestv2.testitem.SoundRecordTestItem;
import com.example.agingmonkeytestv2.testitem.TestItemHolder;
import com.example.agingmonkeytestv2.testitem.WiFiTestItem;
import com.example.agingmonkeytestv2.util.ObjectWriterReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com
 *   > DATE: Date on 18-8-7.
 ************************************************************************/


public class FactoryEntryActivity extends Activity{
    private static final int DAY_MAX = 10;
    public static final String TAG = "lxn-FactoryEntryActivity";
    private boolean isAutoMode = false;
    private boolean isNeedReboot = false;
    private boolean isTestByTimeDuration = true;
    private LinearLayout mAutoLayout;
    private FrameLayout mContainerParentLayout;
    private DatePickerFragment mFragment;
    private MenuItem mItemAuto;
    private ArrayList<TestItemHolder> mItemHolderList = new ArrayList();
    private ArrayList<TestItemHolder> mAutoItemHolderList = new ArrayList();
    private MenuItem mItemManually;
    //private MenuItem mItemRebootSettings;
    
    
    //private MenuItem mItemSettings;
    private Button btnSettings;
    //private Button btnReboot;
    
    private RadioGroup mRadioGroup;
    private int mRebootTimes = -1;
    private ListView mListView;
    private Button mStartBt;

    private RadioButton mTestByDuration;
    private RadioButton mTestByTime;
    private ArrayList<Boolean> mTestItemCheckeds = new ArrayList();
    private ArrayList<String> mTestItemNames = new ArrayList();
    private EditText mTimeInputEt;
   

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_entry);
        //PermissionUtil.checkAndRequestPermissions(this);
        ListView listView = (ListView)findViewById(R.id.list_recycler);
        //listView.setVerticalScrollBarEnabled(false);
        //listView.setFastScrollEnabled(false);
        listView.setItemsCanFocus(true);

        
        Adapter adapter = new Adapter(this, mItemHolderList, R.layout.listview_adapter);
        listView.setAdapter(adapter);
        initView();
        initTestItem();
        //bind();
        transView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_factory_entry, menu);
        this.mItemAuto = menu.findItem(R.id.menu_auto);
        this.mItemManually = menu.findItem(R.id.menu_manually);
        //this.mItemSettings = menu.findItem(R.id.menu_settings);
        //this.mItemRebootSettings = menu.findItem(R.id.menu_reboot_settings);
        if (this.isAutoMode) {
            this.mItemAuto.setVisible(false);
            this.mItemManually.setVisible(true);
            //this.mItemSettings.setVisible(true);
            //this.mItemRebootSettings.setVisible(true);
        } else {
            this.mItemAuto.setVisible(true);
            this.mItemManually.setVisible(false);
            //this.mItemSettings.setVisible(false);
            //this.mItemRebootSettings.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    private void initView() {
        this.mListView = (ListView) findViewById(R.id.list_recycler);
        //this.mListView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        this.mAutoLayout = (LinearLayout) findViewById(R.id.layout_auto);
        this.mTestByTime = (RadioButton) findViewById(R.id.radiobutton_time);
        this.mTestByDuration = (RadioButton) findViewById(R.id.radiobutton_duration);
        this.mContainerParentLayout = (FrameLayout) findViewById(R.id.container_parent);
        this.mTimeInputEt = (EditText) findViewById(R.id.time_input);
        this.mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        this.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "checkedId==="+checkedId);
                if (R.id.radiobutton_time == checkedId) {
                    FactoryEntryActivity.this.isTestByTimeDuration = true;
                }
                if (R.id.radiobutton_duration == checkedId) {
                    FactoryEntryActivity.this.isTestByTimeDuration = false;
                }
                FactoryEntryActivity.this.transView();
            }
        });
        this.mFragment = DatePickerFragment.newInstance(10);
        
        this.btnSettings = (Button) findViewById(R.id.btn_settings);
        //this.btnReboot = (Button) findViewById(R.id.btn_reboot);
        
        this.mStartBt = (Button) findViewById(R.id.auto_start);
        Log.d(TAG, "FactoryEntryActivity.this.isTestByTimeDuration==="+FactoryEntryActivity.this.isTestByTimeDuration);
        this.mStartBt.setOnClickListener(this.mStartOnClickListener);
        
        this.btnSettings.setOnClickListener(this.mSettinsOnClickListener);
        //this.btnReboot.setOnClickListener(this.mRebootOnClickListener);
    }
    
    private View.OnClickListener mSettinsOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(FactoryEntryActivity.this,"Settings", Toast.LENGTH_SHORT).show();
			autoTestItemConfig();
		}
	};

//	private View.OnClickListener mRebootOnClickListener = new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Toast.makeText(FactoryEntryActivity.this,"Reboot", Toast.LENGTH_SHORT).show();
//			rebootSettings();
//		}
//	};

    //onclick start button
    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(FactoryEntryActivity.this,"mStartOnClickListener", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(FactoryEntryActivity.this, AutoRunService.class);
            Bundle bundle = new Bundle();
            Log.d(TAG, "FactoryEntryActivity.this.isTestByTimeDuration)===: " + FactoryEntryActivity.this.isTestByTimeDuration);
            if (FactoryEntryActivity.this.isTestByTimeDuration) {
                String timesText = FactoryEntryActivity.this.mTimeInputEt.getText().toString();
                Log.d(TAG, "times===: " + timesText);
                if (!TextUtils.isEmpty(timesText) && TextUtils.isDigitsOnly(timesText)) {
                    int times = Integer.parseInt(timesText);
                    if (times > 0) {
                        bundle.putInt("TestType", 3);
                        bundle.putInt("TestTimes", times);
                    } else {
                        return;
                    }
                }
                //return;
            } else {
                bundle.putInt("TestType", 4);
                bundle.putLong("TestDuration", FactoryEntryActivity.this.mFragment.getTimeMills());
            }
            ArrayList<TestItemHolder> tempList = new ArrayList();
            Log.d(TAG, "FactoryEntryActivity.this.mAutoItemHolderList.size()===="+FactoryEntryActivity.this.mAutoItemHolderList.size());
            Log.d(TAG, "tempList.size====="+tempList.size());
            for (int i = 0; i < FactoryEntryActivity.this.mAutoItemHolderList.size(); i++) {
                if (((Boolean) FactoryEntryActivity.this.mTestItemCheckeds.get(i)).booleanValue()) {
                    tempList.add(FactoryEntryActivity.this.mAutoItemHolderList.get(i));
                }
            }
            bundle.putParcelableArrayList("ItemHolderList", tempList);
            bundle.putBoolean("TestNeedReboot", FactoryEntryActivity.this.isNeedReboot);
            bundle.putInt("RebootTimes", FactoryEntryActivity.this.mRebootTimes);
            intent.putExtras(bundle);
            if (FactoryEntryActivity.this.isNeedReboot) {
                ObjectWriterReader.write(bundle);
            }
            Log.d(TAG, "intent==="+intent);
            FactoryEntryActivity.this.startService(intent);
        }
    };


    private void transView() {
        if (this.isAutoMode) {//auto
            Toast.makeText(FactoryEntryActivity.this,"is auto mode", Toast.LENGTH_SHORT).show();
            this.mListView.setVisibility(View.GONE);
            this.mAutoLayout.setVisibility(View.VISIBLE);
            if (this.isTestByTimeDuration) {
                this.mContainerParentLayout.setVisibility(View.GONE);
                this.mTimeInputEt.setVisibility(View.VISIBLE);
                this.btnSettings.setVisibility(View.GONE);
                //this.btnReboot.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().remove(this.mFragment).commit();
            } else {
                this.mContainerParentLayout.setVisibility(View.VISIBLE);
                this.mTimeInputEt.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.container, this.mFragment).commit();
                this.btnSettings.setVisibility(View.VISIBLE);
                //this.btnReboot.setVisibility(View.VISIBLE);
            }

        } else {//MANUALLY
            Toast.makeText(FactoryEntryActivity.this, "is manually mode", Toast.LENGTH_SHORT).show();
            this.mListView.setVisibility(View.VISIBLE);
            this.mAutoLayout.setVisibility(View.GONE);

        }
        if (this.mItemAuto != null && this.mItemManually != null /*&& this.mItemSettings != null && this.mItemRebootSettings != null*/) {
            if (this.isAutoMode) {
                this.mItemAuto.setVisible(false);
                this.mItemManually.setVisible(true);
                //this.mItemSettings.setVisible(true);
//                if (this.isTestByTimeDuration) {
//                    //this.mItemRebootSettings.setVisible(true);
//                    return;
//                }
                return;
            }
            this.mItemAuto.setVisible(true);
            this.mItemManually.setVisible(false);
            //this.mItemSettings.setVisible(false);
            //this.mItemRebootSettings.setVisible(false);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_auto:
                this.isAutoMode = true;
                transView();
                break;
            case R.id.menu_manually:
                this.isAutoMode = false;
                transView();
                break;
//            case R.id.menu_reboot_settings:
//                rebootSettings();
//                break;
//            case R.id.menu_settings:
//                autoTestItemConfig();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void rebootSettings() {
        if (this.isAutoMode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.title_reboot_settings));
            builder.setIcon((int) R.drawable.ic_launcher);
            View v = LayoutInflater.from(this).inflate(R.layout.dialog_reboot_settings, null, false);
            final EditText editText = (EditText) v.findViewById(R.id.reboot_times);
            builder.setView(v);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: " + editText.getText().toString());
                    try {
                        FactoryEntryActivity.this.mRebootTimes = Integer.parseInt(editText.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    FactoryEntryActivity.this.isNeedReboot = FactoryEntryActivity.this.mRebootTimes > 0;
                }
            });
            builder.create().show();
        }
    }

    private void autoTestItemConfig() {
        if (this.isAutoMode) {

            Log.d(TAG, "autoTestItemConfig");
            Log.d(TAG, "autoTestItemConfig--mTestItemCheckeds.size()=="+mTestItemCheckeds.size());
            boolean[] checked = new boolean[this.mTestItemCheckeds.size()];
            for (int i = 0; i < this.mTestItemCheckeds.size(); i++) {
                checked[i] = ((Boolean) this.mTestItemCheckeds.get(i)).booleanValue();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon((int) R.drawable.ic_launcher);
            builder.setTitle(getString(R.string.title_test_item_settings));
            builder.setMultiChoiceItems((CharSequence[]) this.mTestItemNames.toArray(new String[0]), checked, new DialogInterface.OnMultiChoiceClickListener() {
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    FactoryEntryActivity.this.mTestItemCheckeds.set(which, Boolean.valueOf(!((Boolean) FactoryEntryActivity.this.mTestItemCheckeds.get(which)).booleanValue()));
                }
            });
            builder.setNegativeButton("OK", null);
            builder.create().show();
        }
    }
    


    private void initTestItem() {
        this.mItemHolderList.clear();
        Log.d(TAG, "initTestItem-----------");
        TestItemHolder lcd = new TestItemHolder(getString(R.string.test_name_lcd), R.layout.layout_testitem_lcd, LcdTestItem.class.getName(), true, false);
        TestItemHolder wifi = new TestItemHolder(getString(R.string.test_name_wifi), R.layout.layout_testitem_wifi, WiFiTestItem.class.getName(), false, true);
        TestItemHolder bluetooth = new TestItemHolder(getString(R.string.test_name_bluetooth), R.layout.layout_testitem_bluetooth, BluetoothTestItem.class.getName(), false, true);
        TestItemHolder flashlight = new TestItemHolder(getString(R.string.test_name_flashlight), R.layout.layout_testitem_flashlight, FlashLightTestItem.class.getName(), false, false);
        TestItemHolder audio = new TestItemHolder(getString(R.string.test_name_audio), R.layout.layout_testitem_audio, AudioTestItem.class.getName(), false, false);
        TestItemHolder battery = new TestItemHolder(getString(R.string.test_name_battery), R.layout.layout_testitem_battery, BatteryTestItem.class.getName(), false, false);
        TestItemHolder focusMotor = new TestItemHolder(getString(R.string.test_name_camera_focus_motor), R.layout.layout_testitem_focus_motor, FocusMotorTestItem.class.getName(), true, false);
        TestItemHolder camera = new TestItemHolder(getString(R.string.test_name_camera_main), R.layout.layout_testitem_camera, CameraTestItem.class.getName(), false, false);
        TestItemHolder cameraSub = new TestItemHolder(getString(R.string.test_name_camera_sub), R.layout.layout_testitem_camera, CameraSubTestItem.class.getName(), false, false);
        TestItemHolder cameraVideo = new TestItemHolder(getString(R.string.test_name_video_main), R.layout.layout_testitem_camera, CameraVideoTestItem.class.getName(), false, false);
        TestItemHolder cameraSubVideo = new TestItemHolder(getString(R.string.test_name_video_sub), R.layout.layout_testitem_camera, CameraSubVideoTestItem.class.getName(), false, false);
        TestItemHolder cameraPreview = new TestItemHolder(getString(R.string.test_name_camera_preview), R.layout.layout_testitem_camera, CameraPreviewTestItem.class.getName(), false, false);
        TestItemHolder cameraTakePicture = new TestItemHolder(getString(R.string.test_name_camera_take_picture), R.layout.layout_testitem_camera, CameraTakePictureTestItem.class.getName(), false, false);
        TestItemHolder soundRecord = new TestItemHolder(getString(R.string.test_name_sound_record), R.layout.layout_testitem_soundrecord, SoundRecordTestItem.class.getName(), false, false);
        
        this.mItemHolderList.add(lcd);
        this.mItemHolderList.add(wifi);
        this.mItemHolderList.add(bluetooth);
        this.mItemHolderList.add(flashlight);
        this.mItemHolderList.add(focusMotor);
        if (FactoryApplication.isNeedAudioTestItem(this)) {
            this.mItemHolderList.add(audio);
        }
        //this.mItemHolderList.add(battery);
        this.mItemHolderList.add(camera);
        //this.mItemHolderList.add(cameraSub);
        this.mItemHolderList.add(cameraVideo);
        //this.mItemHolderList.add(cameraSubVideo);
        this.mItemHolderList.add(cameraPreview);
        this.mItemHolderList.add(cameraTakePicture);
        this.mItemHolderList.add(soundRecord);
        this.mAutoItemHolderList.add(lcd);
        this.mAutoItemHolderList.add(wifi);
        this.mAutoItemHolderList.add(bluetooth);
        this.mAutoItemHolderList.add(flashlight);
        this.mAutoItemHolderList.add(focusMotor);
        if (FactoryApplication.isNeedAudioTestItem(this)) {
            this.mAutoItemHolderList.add(audio);
        }
        this.mAutoItemHolderList.add(camera);
        //this.mAutoItemHolderList.add(cameraSub);
        this.mAutoItemHolderList.add(cameraVideo);
        //this.mAutoItemHolderList.add(cameraSubVideo);
        this.mAutoItemHolderList.add(cameraPreview);
        this.mAutoItemHolderList.add(cameraTakePicture);
        this.mAutoItemHolderList.add(soundRecord);
        Iterator it = this.mAutoItemHolderList.iterator();
        while (it.hasNext()) {
            this.mTestItemNames.add(((TestItemHolder) it.next()).getItemName());
            this.mTestItemCheckeds.add(Boolean.valueOf(true));
        }

    }



    public static void commandFactoryTest(Context context, String testItem) {
        Log.d(TAG, "commandFactoryTest-----------");
        Intent intent = new Intent();
        intent.setClass(context, AutoRunService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("TestType", 4);
        bundle.putLong("TestDuration", getTimeMills(3, 0, 0, 0));
        ArrayList<TestItemHolder> tempList = new ArrayList();
        TestItemHolder lcd = new TestItemHolder(context.getString(R.string.test_name_lcd), R.layout.layout_testitem_lcd, LcdTestItem.class.getName(), true, false);
        TestItemHolder wifi = new TestItemHolder(context.getString(R.string.test_name_wifi), R.layout.layout_testitem_wifi, WiFiTestItem.class.getName(), false, true);
        TestItemHolder bluetooth = new TestItemHolder(context.getString(R.string.test_name_bluetooth), R.layout.layout_testitem_bluetooth, BluetoothTestItem.class.getName(), false, true);
        TestItemHolder flashlight = new TestItemHolder(context.getString(R.string.test_name_flashlight), R.layout.layout_testitem_flashlight, FlashLightTestItem.class.getName(), false, false);
        TestItemHolder camera = new TestItemHolder(context.getString(R.string.test_name_camera_main), R.layout.layout_testitem_camera, CameraTestItem.class.getName(), false, false);
        TestItemHolder cameraSub = new TestItemHolder(context.getString(R.string.test_name_camera_sub), R.layout.layout_testitem_camera, CameraSubTestItem.class.getName(), false, false);
        TestItemHolder cameraVideo = new TestItemHolder(context.getString(R.string.test_name_video_main), R.layout.layout_testitem_camera, CameraVideoTestItem.class.getName(), false, false);
        TestItemHolder cameraSubVideo = new TestItemHolder(context.getString(R.string.test_name_video_sub), R.layout.layout_testitem_camera, CameraSubVideoTestItem.class.getName(), false, false);
        TestItemHolder cameraPreview = new TestItemHolder(context.getString(R.string.test_name_camera_preview), R.layout.layout_testitem_camera, CameraPreviewTestItem.class.getName(), false, false);
        TestItemHolder cameraTakePicture = new TestItemHolder(context.getString(R.string.test_name_camera_take_picture), R.layout.layout_testitem_camera, CameraTakePictureTestItem.class.getName(), false, false);
        TestItemHolder soundRecord = new TestItemHolder(context.getString(R.string.test_name_sound_record), R.layout.layout_testitem_soundrecord, SoundRecordTestItem.class.getName(), false, false);
        TestItemHolder battery = new TestItemHolder(context.getString(R.string.test_name_battery), R.layout.layout_testitem_battery, BatteryTestItem.class.getName(), false, false);
        
        if (testItem.equals(CommandField.KEY_START_FACTORY)) {
            tempList.add(lcd);
            tempList.add(wifi);
            tempList.add(bluetooth);
            tempList.add(flashlight);
            tempList.add(camera);
            tempList.add(cameraSub);
            tempList.add(cameraVideo);
            tempList.add(cameraSubVideo);
            tempList.add(cameraPreview);
            tempList.add(cameraTakePicture);
            tempList.add(soundRecord);
            tempList.add(battery);
        } else {
            if (testItem.equals(CommandField.KEY_START_LCD)) {
                tempList.add(lcd);
            } else {
                if (testItem.equals(CommandField.KEY_START_WIFI)) {
                    tempList.add(wifi);
                } else {
                    if (testItem.equals(CommandField.KEY_START_BLUETOOTH)) {
                        tempList.add(bluetooth);
                    } else {
                        if (testItem.equals(CommandField.KEY_START_FLASHLIGHT)) {
                            tempList.add(flashlight);
                        } else {
                            if (testItem.equals(CommandField.KEY_START_REARCAMERA_PREVIEW)) {
                                tempList.add(camera);
                            } else {
                                if (testItem.equals(CommandField.KEY_START_FRONTCAMERA_PREVIEW)) {
                                    tempList.add(cameraSub);
                                } else {
                                    if (testItem.equals(CommandField.KEY_START_REAR_CAMERA_VIDEO)) {
                                        tempList.add(cameraVideo);
                                    } else {
                                        if (testItem.equals(CommandField.KEY_START_FRONT_CAMERA_VIDEO)) {
                                            tempList.add(cameraSubVideo);
                                        } else {
                                            if (testItem.equals(CommandField.KEY_START_CAMERA_PREVIEW)) {
                                                tempList.add(cameraPreview);
                                            } else {
                                                if (testItem.equals(CommandField.KEY_START_CAMERA_PICTURE)) {
                                                    tempList.add(cameraTakePicture);
                                                } else {
                                                    if (testItem.equals(CommandField.KEY_START_SOUNDRECORD)) {
                                                        tempList.add(soundRecord);
                                                    } else {
                                                    	if (testItem.equals(CommandField.KEY_START_BATTERY)) {
                                                    		tempList.add(battery);
                                                    	} else {
                                                        Log.d("lxn----FacEditTexttoryEntryActivity", "Factory Test item is null");
                                                    
                                                    	}
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        bundle.putParcelableArrayList("ItemHolderList", tempList);
        intent.putExtras(bundle);
        Log.d(TAG,"startService=intent==="+intent);
        context.startService(intent);
    }

    private static long getTimeMills(int day, int hour, int minute, int second) {
        return (long) (((((((day * 24) * 60) * 60) * 1000) + (((hour * 60) * 60) * 1000)) + ((minute * 60) * 1000)) + (second * 1000));
    }

    class Adapter extends BaseAdapter{
        private ArrayList<TestItemHolder> mItemHolderList;
        public Adapter(Context context, ArrayList<TestItemHolder> list, int resource) {
            this.mItemHolderList = list;
        }

        public int getCount() {
            return mItemHolderList.size();
        }

        public Object getItem(int i) {
            return mItemHolderList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        
        public void setChecked(int id, boolean checked){
        	TestItemHolder testItemHolder = mItemHolderList.get(id);
        	if (testItemHolder != null) {
        		testItemHolder.setChecked(checked);
        	}
        	
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //ViewHolder viewHolder;
            ViewHolder viewHolder = new ViewHolder();
            if (view == null) {
                view = LayoutInflater.from(FactoryEntryActivity.this).inflate(R.layout.listview_adapter, null);
                viewHolder.button = (Button) view.findViewById(R.id.list_adapter);
                
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)view.getTag();
            }

            TestItemHolder itemHolder = (TestItemHolder) this.mItemHolderList.get(i);
            viewHolder.button.setText(itemHolder.getItemName());
            viewHolder.button.setOnClickListener(new TestItemOnClickListener(itemHolder));
            return view;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Log.d(TAG, "onBindViewHolder=====");
            TestItemHolder itemHolder = (TestItemHolder) this.mItemHolderList.get(position);
            viewHolder.button.setText(itemHolder.getItemName());
            viewHolder.button.setOnClickListener(new TestItemOnClickListener(itemHolder));
        }
    }

    class ViewHolder{
        public Button button;
        
    }


    class TestItemOnClickListener implements View.OnClickListener {
        private TestItemHolder mHolder;

        public TestItemOnClickListener(TestItemHolder holder) {
            this.mHolder = holder;
        }

        public void onClick(View view) {
            Log.d(TAG, "TestItemOnClickListener===============");
//            if (this.mHolder.getClassName().contains("Camera")) {
//                Intent intent = new Intent();
//                intent.setAction("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
//                    FactoryEntryActivity.this.startActivityForResult(intent, -1, null);
//                    new Timer().schedule(new TimerTask() {
//                        public void run() {
//                            ShellUtils.execCommand("input keyevent 4", false);
//                            SystemClock.sleep(200);
//                            TestItemOnClickListener.this.startTest();
//                        }
//                    }, 2000);
//                    return;
//                }
//                return;
//            }
            Log.d(TAG, "TestItemOnClickListener----call----startTest----");
            startTest();
        }

        private void startTest() {
            Log.d(TAG, "------------startTest--------------");
            Intent activityIntent = new Intent();
            Log.d(TAG, "this.mHolder.createBundle()--------"+this.mHolder.createBundle());
            activityIntent.putExtras(this.mHolder.createBundle());
            activityIntent.setClass(FactoryEntryActivity.this, BaseTestActivity.class);
            FactoryEntryActivity.this.startActivity(activityIntent);
        }
    }
}

