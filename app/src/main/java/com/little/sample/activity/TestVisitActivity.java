package com.little.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;
import com.little.sample.base.BaseApplication;
import com.little.sample.base.BaseConstant;
import com.little.sample.util.OkHttpUtil;
import com.little.visit.TaskConstant;
import com.little.visit.listener.IOnResultListener;
import com.little.visit.listener.IOnVisitResultListener;
import com.little.visit.task.PopupVisitTask;
import com.little.visit.task.VisitTask;
import com.little.visit.task.VolleyTask;
import com.little.visit.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TestVisitActivity extends AppCompatActivity {

    String url = "https://bfda-app.ifoton.com.cn/serviceProvider/getVersion.action";
    String apk = "http://bfda-app.ifoton.com.cn:8012/serviceProvider/version/serverapp.apk";
    String params = "{\"limit\":{\"auth\":\"239BA6B4594179AAB3B7827E5A8A0D0BFC5139C7BBC78AB2540618361BC318A7D66552E1\",\"uid\":\"64766\",\"userType\":\"40\"},\"param\":{\"versionCode\":\"54\",\"deviceType\":\"1\",\"version\":\"3.0.1\"}}";
    @InjectView(R.id.activity_title_sample_a)
    TextView activityTitleSampleA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_visit);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.activity_title_sample_a, R.id.activity_title_sample_b, R.id.activity_title_sample_c})
    public void onViewClicked(View view) {
        Map<String, String> argMap = new HashMap<String, String>();
        argMap.put("jsonParame",params);
        switch (view.getId()) {
            case R.id.activity_title_sample_a:
//                VolleyUtil.getInstance(TestVisitActivity.this).visit(Request.Method.POST, url, "test", argMap);
                VolleyTask volleyTask = new VolleyTask(BaseApplication.self(), "test", activityTitleSampleA, true, VolleyTask.DOWNLOAD_FILE_VISIT,apk,  ""+ BaseConstant.APK_PATH, null);
                volleyTask.setOnVisitResultListener(new IOnVisitResultListener<String>() {
                    @Override
                    public void onSuccess(String res) {
                        LogUtil.e("resultEntity= "+res);
                    }

                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                volleyTask.execute();
                break;
            case R.id.activity_title_sample_b:
                OkHttpUtil.getInstance(TestVisitActivity.this).visit(url);
                break;
            case R.id.activity_title_sample_c:
                getVersion(true);
                break;
        }
    }

    void getVersion(final boolean showDialog) {
        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("jsonParame",params);
        PopupVisitTask visitTask = new PopupVisitTask(this, "test", activityTitleSampleA, "", showDialog, url, argMap, TaskConstant.POST);
        visitTask.setiOnResultListener(new IOnResultListener() {
            @Override
            public void onSuccess(VisitTask task) {

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
