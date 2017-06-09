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

public class DropSampleActivity extends Activity {

    @BindView(R.id.activity_drop_sample_a)
    TextView activityDropSampleA;
    @BindView(R.id.activity_drop_sample_b)
    TextView activityDropSampleB;
    @BindView(R.id.activity_drop_sample_c)
    TextView activityDropSampleC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_sample);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.activity_drop_sample_a, R.id.activity_drop_sample_b, R.id.activity_drop_sample_c})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_drop_sample_a:
                intent2Drop(0);
                break;
            case R.id.activity_drop_sample_b:
                intent2Drop(1);
                break;
            case R.id.activity_drop_sample_c:
                intent2Drop(2);
                break;
        }
    }

    private void intent2Drop(int index){
        Intent intent = new Intent(this,TitleAliquotSampleActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}
