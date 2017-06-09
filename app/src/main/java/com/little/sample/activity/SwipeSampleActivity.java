package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwipeSampleActivity extends Activity {

    @BindView(R.id.activity_swipe_sample_a)
    TextView activitySwipeSampleA;
    @BindView(R.id.activity_swipe_sample_b)
    TextView activitySwipeSampleB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_sample);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.activity_swipe_sample_a, R.id.activity_swipe_sample_b})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_swipe_sample_a:
                startActivity(new Intent(SwipeSampleActivity.this,SwipeBackSampleActivity.class));
                break;
            case R.id.activity_swipe_sample_b:
                startActivity(new Intent(SwipeSampleActivity.this,SwipeBackSampleFragmentActivity.class));
                break;
        }
    }
}
