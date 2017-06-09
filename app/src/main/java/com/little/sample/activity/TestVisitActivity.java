package com.little.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.little.sample.R;
import com.little.sample.base.BaseApplication;
import com.little.sample.base.BaseConstant;
import com.little.sample.util.SystemUtil;
import com.little.visit.listener.IOnVisitResultListener;
import com.little.visit.model.ResultEntity;
import com.little.visit.okhttp.OkHttpUtil;
import com.little.visit.task.OKHttpTask;
import com.little.visit.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestVisitActivity extends AppCompatActivity {

    String url = "https://bfda-app.ifoton.com.cn/serviceProvider/getVersion.action";
    String apk = "http://bfda-app.ifoton.com.cn:8012/serviceProvider/version/serverapp.apk";
    String params = "{\"limit\":{\"auth\":\"239BA6B4594179AAB3B7827E5A8A0D0BFC5139C7BBC78AB2540618361BC318A7D66552E1\",\"uid\":\"64766\",\"userType\":\"40\"},\"param\":{\"versionCode\":\"54\",\"deviceType\":\"1\",\"version\":\"3.0.1\"}}";
    @BindView(R.id.activity_title_sample_a)
    TextView activityTitleSampleA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_visit);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.activity_title_sample_a, R.id.activity_title_sample_b, R.id.activity_title_sample_c})
    public void onViewClicked(View view) {
        Map<String, String> argMap = new HashMap<String, String>();
        argMap.put("jsonParame",params);
        switch (view.getId()) {
            case R.id.activity_title_sample_a:
                visit(true);
                break;
            case R.id.activity_title_sample_b:
                download(true);

                break;
            case R.id.activity_title_sample_c:
                download(false);
                break;
        }
    }

    private void visit(boolean showDialog){
        Map<String, String> argMap = new HashMap<String, String>();
        argMap.put("jsonParame",params);
        final OKHttpTask okHttpTask = new OKHttpTask(BaseApplication.self(), "test", activityTitleSampleA, showDialog, url, argMap, OkHttpUtil.POST,ResultEntity.class);
        okHttpTask.setOnVisitResultListener(new IOnVisitResultListener<ResultEntity>() {
            @Override
            public void onSuccess(ResultEntity res) {
                LogUtil.e("onSuccess:" + res.getCode());
            }

            @Override
            public void onError(String msg) {
                LogUtil.e("onError:"+msg);
            }

            @Override
            public void onFinish() {
                LogUtil.e("onFinish:"+System.currentTimeMillis());
            }

            @Override
            public void onProgress(long bytes, long contentLength) {

            }
        });
        okHttpTask.execute();
    }

    private void download(boolean showDialog){
        OKHttpTask okHttpTask = new OKHttpTask(BaseApplication.self(),"test",OKHttpTask.PROGRESSSTYLE,activityTitleSampleA,showDialog,OKHttpTask.DOWNLOAD_FILE_VISIT,apk, BaseConstant.APK_PATH, ResultEntity.class);
        okHttpTask.setOnVisitResultListener(new IOnVisitResultListener<String>() {
            @Override
            public void onSuccess(String res) {
                LogUtil.e("onSuccess:" + res);
                SystemUtil.install(TestVisitActivity.this, res);
            }

            @Override
            public void onError(String msg) {
                LogUtil.e("onError:");
            }

            @Override
            public void onFinish() {
                LogUtil.e("onFinish:");
            }

            @Override
            public void onProgress(long bytes, long contentLength) {
                LogUtil.e("onProgress:" + bytes+"   contentLength="+contentLength);
            }
        });
        okHttpTask.getPopupUtil().setDismissKeyback(true);
        okHttpTask.execute();
    }

}
