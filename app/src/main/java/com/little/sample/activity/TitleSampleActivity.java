package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TitleSampleActivity extends Activity {

    @InjectView(R.id.activity_title_sample_a)
    TextView activityTitleSampleA;
    @InjectView(R.id.activity_title_sample_b)
    TextView activityTitleSampleB;
    @InjectView(R.id.activity_title_sample_c)
    TextView activityTitleSampleC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_sample);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.activity_title_sample_a, R.id.activity_title_sample_b, R.id.activity_title_sample_c})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_title_sample_a:
                startActivity(new Intent(TitleSampleActivity.this, TitleCenterSampleActivity.class));
                break;
            case R.id.activity_title_sample_b:
                break;
            case R.id.activity_title_sample_c:
                break;
        }
    }
}
