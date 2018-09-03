package com.example.agingmonkeytestv2.fragment;

import com.example.agingmonkeytestv2.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-9.
 ************************************************************************/


public class DatePickerFragment extends Fragment{
    public static final int ID_DAY = 1;
    public static final int ID_HOUR = 2;
    public static final int ID_MINUTE = 3;
    public static final int ID_SECOND = 4;
    private static final String KEY_MAX_DAY = "key_max_day";
    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private int dayMax = 10;
    private final int dayMin = 0;
    private final int hourMax = 23;
    private final int hourMin = 0;
    private String mDayLabel;
    private TextView mDaySelectorTv;
    private String mHourLabel;
    private TextView mHourSelectorTv;
    private String mMinuteLabel;
    private TextView mMinuteSelectorTv;

    private String mSecondLabel;
    private TextView mSecondSelectorTv;
    private final int minuteMax = 59;
    private final int minuteMin = 0;
    private final int secondMax = 59;
    private final int secondMin = 0;


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int min = 0;
            int max = 60;
            int current = 23;
            int id = -1;
            String title = DatePickerFragment.this.getString(R.string.time_choose_title);
            String label = "";

            switch (view.getId()) {
                case R.id.time_selector_day:
                    min = 0;
                    max = DatePickerFragment.this.dayMax;
                    id = 1;
                    current = Integer.parseInt(DatePickerFragment.this.mDaySelectorTv.getText().toString());
                    label = DatePickerFragment.this.mDayLabel;
                    break;
                case R.id.time_selector_hour:
                    min = 0;
                    max = 23;
                    id = 2;
                    current = Integer.parseInt(DatePickerFragment.this.mDaySelectorTv.getText().toString());
                    label = DatePickerFragment.this.mHourLabel;
                    break;
                case R.id.time_selector_minute:
                    min = 0;
                    max = 59;
                    id = 3;
                    current = Integer.parseInt(DatePickerFragment.this.mMinuteSelectorTv.getText().toString());
                    label = DatePickerFragment.this.mMinuteLabel;
                    break;
                case R.id.time_selector_second:
                    min = 0;
                    max = 59;
                    id = 4;
                    label = DatePickerFragment.this.mSecondLabel;
                    current = Integer.parseInt(DatePickerFragment.this.mSecondSelectorTv.getText().toString());
                    break;

            }
            DatePickerFragment.this.createDialog(DatePickerFragment.this.getActivity(), title, label, id, min, max, current).show();

        }
    };


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.dayMax = getArguments().getInt(KEY_MAX_DAY);
        }
    }

    public void onResume() {
        super.onResume();
        this.mDayLabel = getString(R.string.time_label_day);
        this.mHourLabel = getString(R.string.time_label_hour);
        this.mMinuteLabel = getString(R.string.time_label_minute);
        this.mSecondLabel = getString(R.string.time_label_second);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        this.mDaySelectorTv = (TextView) view.findViewById(R.id.time_selector_day);
        this.mDaySelectorTv.setOnClickListener(this.mOnClickListener);
        this.mHourSelectorTv = (TextView) view.findViewById(R.id.time_selector_hour);
        this.mHourSelectorTv.setOnClickListener(this.mOnClickListener);
        this.mMinuteSelectorTv = (TextView) view.findViewById(R.id.time_selector_minute);
        this.mMinuteSelectorTv.setOnClickListener(this.mOnClickListener);
        this.mSecondSelectorTv = (TextView) view.findViewById(R.id.time_selector_second);
        this.mSecondSelectorTv.setOnClickListener(this.mOnClickListener);
    }

    public static DatePickerFragment newInstance(int maxDay) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_MAX_DAY, maxDay);
        fragment.setArguments(args);
        return fragment;
    }

    private long getTimeMills(int day, int hour, int minute, int second) {
        return (long) (((((((day * 24) * 60) * 60) * 1000) + (((hour * 60) * 60) * 1000)) + ((minute * 60) * 1000)) + (second * 1000));
    }

    public long getTimeMills() {
        int day = Integer.parseInt(this.mDaySelectorTv.getText().toString());
        int hour = Integer.parseInt(this.mHourSelectorTv.getText().toString());
        int minute = Integer.parseInt(this.mMinuteSelectorTv.getText().toString());
        int second = Integer.parseInt(this.mSecondSelectorTv.getText().toString());
        Log.d(TAG, "getTimeMills day: " + day + ", hours: " + hour + ", minutes: " + minute + ", second: " + second);
        return getTimeMills(day, hour, minute, second);
    }

    private AlertDialog createDialog(Context context, String title, String label, final int id, int min, int max, int current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle((CharSequence) title);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null, false);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        ((TextView) v.findViewById(R.id.label)).setText(label);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(current);
        builder.setView(v);
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case 1:
                        DatePickerFragment.this.mDaySelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 2:
                        DatePickerFragment.this.mHourSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 3:
                        DatePickerFragment.this.mMinuteSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 4:
                        DatePickerFragment.this.mSecondSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    default:
                        return;
                }
            }
        });
        return builder.create();
    }

}
