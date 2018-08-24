package com.example.lxn.circleprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CircleProgressView circleProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
    }
}
