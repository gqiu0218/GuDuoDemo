package com.guduodemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.guduodemo.R;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
