package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnItemListener;
import com.little.sample.R;
import com.little.sample.base.BaseConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends Activity {

    @BindView(R.id.activity_sample_component)
    TextView activitySampleComponent;
    private List<String> componentList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        componentList.add("网络访问");
        componentList.add("弹窗");
        componentList.add("下拉刷新");
        componentList.add("滑动界面");
        componentList.add("选图");
        componentList.add("滑动返回");
        componentList.add("时间选择");
        componentList.add("网页");
    }


    @OnClick(R.id.activity_sample_component)
    public void onClick() {
        PopupDialog popupDialog = new PopupDialog.Builder(this).dismissBackKey(true).dismissOutside(true).onItemListener(new IOnItemListener() {
            @Override
            public void onItem(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(SampleActivity.this, TestVisitActivity.class));
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
                    case 7:
                        Intent intent = new Intent(SampleActivity.this, ActWebActivity.class);
                        intent.putExtra(BaseConstant.INTENT_TYPE,"百度");
                        intent.putExtra(BaseConstant.INTENT_CONTENT,"http://www.baidu.com");
                        startActivity(intent);
                        break;
                }
            }
        }).build();
        popupDialog.showListDialog(activitySampleComponent,componentList);
    }
}
