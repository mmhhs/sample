package com.little.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnItemListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SampleActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.activity_sample_component)
    public void onClick() {
        PopupDialog popupDialog = new PopupDialog.Builder(this).dismissBackKey(true).dismissOutside(true).onItemListener(new IOnItemListener() {
            @Override
            public void onItem(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        }).build();
        popupDialog.showListDialog(activitySampleComponent,componentList);
    }
}
