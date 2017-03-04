package com.little.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class VisitSampleActivity extends Activity {

    @InjectView(R.id.activity_visit_sample_a)
    TextView activityVisitSampleA;
    @InjectView(R.id.activity_visit_sample_b)
    TextView activityVisitSampleB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_sample);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.activity_visit_sample_a, R.id.activity_visit_sample_b})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_visit_sample_a:
                break;
            case R.id.activity_visit_sample_b:
                break;
        }
    }
}
