package com.singingkungfu.sing.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.singingkungfu.sing.R;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
