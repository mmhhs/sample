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

/**
 * 标题组件示例
 * 三种模式：居中，等分，等分加分割线
 */
public class TitleSampleActivity extends Activity {

    @BindView(R.id.activity_title_sample_a)
    TextView activityTitleSampleA;
    @BindView(R.id.activity_title_sample_b)
    TextView activityTitleSampleB;
    @BindView(R.id.activity_title_sample_c)
    TextView activityTitleSampleC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_sample);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.activity_title_sample_a, R.id.activity_title_sample_b, R.id.activity_title_sample_c})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_title_sample_a:
                startActivity(new Intent(TitleSampleActivity.this, TitleCenterSampleActivity.class));
                break;
            case R.id.activity_title_sample_b:
                startActivity(new Intent(TitleSampleActivity.this, TitleAliquotSampleActivity.class));
                break;
            case R.id.activity_title_sample_c:
                startActivity(new Intent(TitleSampleActivity.this, TitleAliquotLineSampleActivity.class));
                break;
        }
    }
}
