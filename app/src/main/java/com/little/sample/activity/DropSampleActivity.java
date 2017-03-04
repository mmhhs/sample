package com.little.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DropSampleActivity extends Activity {

    @InjectView(R.id.activity_drop_sample_a)
    TextView activityDropSampleA;
    @InjectView(R.id.activity_drop_sample_b)
    TextView activityDropSampleB;
    @InjectView(R.id.activity_drop_sample_c)
    TextView activityDropSampleC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_sample);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.activity_drop_sample_a, R.id.activity_drop_sample_b, R.id.activity_drop_sample_c})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_drop_sample_a:
                break;
            case R.id.activity_drop_sample_b:
                break;
            case R.id.activity_drop_sample_c:
                break;
        }
    }
}
