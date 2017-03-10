package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnItemListener;
import com.little.sample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SampleActivity extends Activity {

    @InjectView(R.id.activity_sample_component)
    TextView activitySampleComponent;
    private List<String> componentList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.inject(this);
        componentList.add("网络访问");
        componentList.add("弹窗");
        componentList.add("下拉刷新");
        componentList.add("滑动界面");
        componentList.add("选图");
        componentList.add("滑动返回");
        componentList.add("时间选择");
    }


    @OnClick(R.id.activity_sample_component)
    public void onClick() {
        PopupDialog popupDialog = new PopupDialog.Builder(this).dismissBackKey(true).dismissOutside(true).onItemListener(new IOnItemListener() {
            @Override
            public void onItem(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(SampleActivity.this, VisitSampleActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SampleActivity.this, PopupSampleActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(SampleActivity.this, DropSampleActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(SampleActivity.this, TitleSampleActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(SampleActivity.this, PictureSampleActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(SampleActivity.this, SwipeSampleActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(SampleActivity.this, PickerSampleActivity.class));
                        break;
                }
            }
        }).build();
        popupDialog.showListDialog(activitySampleComponent,componentList);
    }
}
