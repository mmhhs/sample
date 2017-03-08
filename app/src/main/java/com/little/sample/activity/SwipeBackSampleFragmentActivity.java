package com.little.sample.activity;

import android.os.Bundle;

import com.little.sample.R;
import com.little.swipe.SwipeBackFragmentActivity;

public class SwipeBackSampleFragmentActivity extends SwipeBackFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back_sample_fragment);
        init();
    }

    private void init(){
        getSwipeBackLayout().setEdgeTrackingEnabled(2);
    }


}
