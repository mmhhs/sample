package com.little.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;
import com.little.sample.base.BaseApplication;
import com.little.sample.model.VisitSampleDataEntity;
import com.little.sample.model.VisitSampleResult;
import com.little.visit.listener.IOnVisitResultListener;
import com.little.visit.okhttp.OkHttpUtil;
import com.little.visit.task.OKHttpTask;
import com.little.visit.util.LogUtil;

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

    void updateInfo(OKHttpTask task) {
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
        String url = "https://bfda-app.ifoton.com.cn/serviceProvider/getBrandInfo.action";
        Map<String, String> argMap = new HashMap<String, String>();
        argMap.put("brandType", "3");
        final OKHttpTask okHttpTask = new OKHttpTask(BaseApplication.self(), tagStr, activityVisitSampleA, showDialog, url, argMap, OkHttpUtil.POST,VisitSampleResult.class);
        okHttpTask.setOnVisitResultListener(new IOnVisitResultListener<VisitSampleResult>() {
            @Override
            public void onSuccess(VisitSampleResult res) {
                resultList = res.data;
                updateInfo(okHttpTask);
                LogUtil.e("onSuccess:"+res);
            }

            @Override
            public void onError(String msg) {
                LogUtil.e("onError:"+msg);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onProgress(long bytes, long contentLength) {

            }
        });
        okHttpTask.execute();
    }

}
