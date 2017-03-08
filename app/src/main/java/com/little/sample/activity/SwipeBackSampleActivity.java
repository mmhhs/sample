package com.little.sample.activity;

import android.os.Bundle;

import com.little.sample.R;
import com.little.swipe.SwipeBackActivity;

public class SwipeBackSampleActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back_sample);
        init();
    }

    private void init(){
//        getSwipeBackLayout().setEdgeTrackingEnabled(1);
    }


}
