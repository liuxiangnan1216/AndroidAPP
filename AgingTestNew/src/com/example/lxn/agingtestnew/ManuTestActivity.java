package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.lxn.agingtestnew.testItem.AudioTestItem;
import com.example.lxn.agingtestnew.testItem.BackLightTestItem;
import com.example.lxn.agingtestnew.testItem.BatteryTestItem;
import com.example.lxn.agingtestnew.testItem.BluetoothTestItem;
import com.example.lxn.agingtestnew.testItem.CameraTestItem;
import com.example.lxn.agingtestnew.testItem.EarpieceTestitem;
import com.example.lxn.agingtestnew.testItem.EmmcTestItem;
import com.example.lxn.agingtestnew.testItem.GpsTestItem;
import com.example.lxn.agingtestnew.testItem.LcdTestItem;
import com.example.lxn.agingtestnew.testItem.MemoryTestItem;
import com.example.lxn.agingtestnew.testItem.RebootTestItem;
import com.example.lxn.agingtestnew.testItem.SensorTestItem;
import com.example.lxn.agingtestnew.testItem.TestItemHolder;
import com.example.lxn.agingtestnew.testItem.VibratorTestItem;
import com.example.lxn.agingtestnew.testItem.VideoTestItem;
import com.example.lxn.agingtestnew.testItem.WifiTestItem;

import java.util.ArrayList;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 19-1-5.
 ************************************************************************/




public class ManuTestActivity extends Activity {
    private final static String TAG = "ManuTestActivity";
    private ListView mListView;
    private ArrayList<TestItemHolder> mItemHolderList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        ListView listView = (ListView) findViewById(R.id.list_recycler);
        listView.setItemsCanFocus(true);

        Adapter adapter = new Adapter(this, mItemHolderList, R.layout.listview_adapter);
        listView.setAdapter(adapter);

        this.mListView = (ListView) findViewById(R.id.list_recycler);
        initTestItem();
    }


    private void initTestItem() {
        this.mItemHolderList.clear();
        Log.d(TAG, "initTestItem-----------");
        TestItemHolder reboot = new TestItemHolder("Reboot", R.layout.layout_testitem_reboot, RebootTestItem.class.getName(), false, true);
        TestItemHolder memory = new TestItemHolder("Memory", R.layout.layout_testitem_memory, MemoryTestItem.class.getName(), false, true);
        TestItemHolder emmc = new TestItemHolder("Emmc", R.layout.layout_testitem_emmc, EmmcTestItem.class.getName(), false, true);
        TestItemHolder battery = new TestItemHolder("Battery", R.layout.layout_testitem_battery, BatteryTestItem.class.getName(), false, true);
        TestItemHolder audio = new TestItemHolder("Audio", R.layout.layout_testitem_audio, AudioTestItem.class.getName(), false, true);
        TestItemHolder vibrator = new TestItemHolder("Vibrator", R.layout.layout_testitem_vibrator, VibratorTestItem.class.getName(), false, true);
        TestItemHolder camera = new TestItemHolder("Camera", R.layout.layout_testitem_camera, CameraTestItem.class.getName(), false, true);
        TestItemHolder video = new TestItemHolder("Video", R.layout.layout_testitem_video, VideoTestItem.class.getName(), false, true);
        TestItemHolder lcd = new TestItemHolder("LCD", R.layout.layout_testitem_lcd, LcdTestItem.class.getName(), true, true);
        TestItemHolder gps = new TestItemHolder("GPS", R.layout.layout_testitem_gps, GpsTestItem.class.getName(), false, true);
        TestItemHolder bluetooth = new TestItemHolder("Bluetooth", R.layout.layout_testitem_bluetooth, BluetoothTestItem.class.getName(), false, true);
        TestItemHolder wifi = new TestItemHolder("Wi-Fi", R.layout.layout_testitem_wifi, WifiTestItem.class.getName(), false, true);
//        TestItemHolder ligth = new TestItemHolder("Ligth", R.layout.layout_testitem_ligth, LightTestItem.class.getName(), false, true);
//        TestItemHolder pro = new TestItemHolder("Proximity", R.layout.layout_testitem_pro, ProTestItem.class.getName(), false, true);
//        TestItemHolder gra = new TestItemHolder("Gravity", R.layout.layout_testitem_gra, GraTestItem.class.getName(), false, true);
        TestItemHolder backlight = new TestItemHolder("BlackLight", R.layout.layout_testitem_backlight, BackLightTestItem.class.getName(), true, true);
//        TestItemHolder qc = new TestItemHolder("QuickCharge", R.layout.layout_testitem_qc, QcTestItem.class.getName(), false, true);
//        TestItemHolder headphone = new TestItemHolder("Headphone", R.layout.layout_testitem_headphone, HeadphoneTestItem.class.getName(), false, true);
        TestItemHolder sensor = new TestItemHolder("Sensor", R.layout.layout_testitem_sensor, SensorTestItem.class.getName(), false, true);
//        TestItemHolder charging = new TestItemHolder("Charging", R.layout.layout_testitem_charging, ChargingTestItem.class.getName(), false, true);
        TestItemHolder earpiece = new TestItemHolder("Earpiece", R.layout.layout_testitem_earpiece, EarpieceTestitem.class.getName(), false, true);



        this.mItemHolderList.add(reboot);
        this.mItemHolderList.add(memory);
        this.mItemHolderList.add(emmc);
        this.mItemHolderList.add(battery);
        this.mItemHolderList.add(audio);
        this.mItemHolderList.add(vibrator);
        this.mItemHolderList.add(camera);
        this.mItemHolderList.add(video);
        this.mItemHolderList.add(lcd);
        this.mItemHolderList.add(gps);
        this.mItemHolderList.add(bluetooth);
        this.mItemHolderList.add(wifi);
        this.mItemHolderList.add(sensor);
//        this.mItemHolderList.add(ligth);
//        this.mItemHolderList.add(gra);
//        this.mItemHolderList.add(gra);
        this.mItemHolderList.add(backlight);
//        this.mItemHolderList.add(qc);
//        this.mItemHolderList.add(headphone);
//        this.mItemHolderList.add(charging);
        this.mItemHolderList.add(earpiece);


    }



    class Adapter extends BaseAdapter {
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
            ViewHolder viewHolder = new ViewHolder();
            if (view == null) {
                view = LayoutInflater.from(ManuTestActivity.this).inflate(R.layout.listview_adapter, null);
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
            Log.d(TAG, "TestItemOnClickListener=onClick");
            startTest();
        }

        private void startTest() {
            Log.d(TAG, "------------startTest--------------");
            Intent activityIntent = new Intent();
            activityIntent.putExtras(this.mHolder.createBundle());
            activityIntent.setClass(ManuTestActivity.this, BaseTestActivity.class);
            ManuTestActivity.this.startActivity(activityIntent);
        }
    }
}
