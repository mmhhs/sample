package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;
import com.little.sample.model.VisitSampleDataEntity;
import com.little.sample.model.VisitSampleResult;
import com.little.visit.TaskConstant;
import com.little.visit.listener.IOnResultListener;
import com.little.visit.task.PopupVisitTask;
import com.little.visit.task.VisitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class VisitSampleActivity extends Activity {

    @InjectView(R.id.activity_visit_sample_a)
    TextView activityVisitSampleA;
    @InjectView(R.id.activity_visit_sample_b)
    TextView activityVisitSampleB;
    String tagStr = "VisitSampleActivity";//task唯一标识
    private List<VisitSampleDataEntity> resultList;

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
                visit(true);
                break;
            case R.id.activity_visit_sample_b:
                intent2Page(0);
                break;
        }
    }

    private void intent2Page(int index){
        Intent intent = new Intent(this,TitleAliquotSampleActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }

    void updateInfo(PopupVisitTask task) {
        try {
            if (resultList != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹窗类访问网络线程
     * @param showDialog
     */
    private void visit(boolean showDialog){
        String url = "http://192.168.0.107:8080/serviceProvider/getBrandInfo.action";
        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("brandType", "3");
        PopupVisitTask visitTask = new PopupVisitTask(this,tagStr,activityVisitSampleA,"",showDialog,url,argMap, TaskConstant.POST);
        visitTask.setParseClass(VisitSampleResult.class);
        visitTask.setiOnResultListener(new IOnResultListener() {
            @Override
            public void onSuccess(VisitTask task) {
                if (task.getResultEntity() instanceof VisitSampleResult) {
                    VisitSampleResult res = (VisitSampleResult) task.getResultEntity();
                    resultList = res.data;
                }
                updateInfo((PopupVisitTask) task);
            }

            @Override
            public void onError(VisitTask task) {

            }

            @Override
            public void onDone(VisitTask task) {

            }
        });
        visitTask.execute();
    }

}
