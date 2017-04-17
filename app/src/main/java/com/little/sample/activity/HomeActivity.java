package com.little.sample.activity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.little.picture.util.ToastUtil;
import com.little.sample.R;
import com.little.sample.base.BaseConstant;
import com.little.sample.base.BaseFragmentActivity;
import com.little.sample.daemon.CompatIntentWrapper;
import com.little.sample.fragment.DropArrowSampleFragment;
import com.little.sample.fragment.DropCustomSampleFragment;
import com.little.sample.fragment.DropSwipeSampleFragment;
import com.little.sample.listener.IOnPermissionListener;
import com.little.sample.util.PermissionUtil;

import butterknife.InjectView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;


public class HomeActivity extends BaseFragmentActivity implements IOnPermissionListener {
    @InjectView(android.R.id.tabhost)
    public FragmentTabHost mTabHost;
    @InjectView(R.id.activity_home_tab_text0)
    public TextView naviText0;
    @InjectView(R.id.activity_home_tab_text1)
    public TextView naviText1;
    @InjectView(R.id.activity_home_tab_text2)
    public TextView naviText2;
    public int tabFlag = 0;
    private ReceiveBroadcast receiveBroadcast;

    private final Class[] fragments = { DropSwipeSampleFragment.class, DropCustomSampleFragment.class,
            DropArrowSampleFragment.class };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finishSelf();
            return;
        }
        setTaskTag(getClass().getSimpleName());
        setContentView(R.layout.activity_home);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy(){
        try {
            unregisterReceiver(receiveBroadcast);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    public void init(){
        mTabHost.setup(this, getSupportFragmentManager(), R.id.activity_home_frameLayout);
        int count = fragments.length;
        for (int i = 0; i < count; i++) {
			/* 为每一个Tab按钮设置图标、文字和内容 */
            TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			/* 将Tab按钮添加进Tab选项卡中 */
            mTabHost.addTab(tabSpec, fragments[i], null);
        }
        setCurrentScreen(0);
        registerBroadcast();
//        VersionCheckUtil versionCheckUtils = new VersionCheckUtil(this,naviText0,taskTag);
//        versionCheckUtils.checkVersion(false);

        /**
         * 轨迹跟踪服务的持续运行
         */
        if (BaseConstant.IS_KEEP_LIVE){
            CompatIntentWrapper.whiteListMatters(this, "轨迹跟踪服务的持续运行");
        }

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        tabFlag = intent.getIntExtra(BaseConstant.INTENT_TYPE,0);
        setCurrentScreen(tabFlag);
    }

    @OnClick({R.id.activity_home_tab_text0,R.id.activity_home_tab_text1,R.id.activity_home_tab_text2})
    public void onNaviSelected(View view){
        switch (view.getId()){
            case R.id.activity_home_tab_text0:
                setCurrentScreen(0);
                tabFlag = 0;
                break;
            case R.id.activity_home_tab_text1:
                setCurrentScreen(1);
                tabFlag = 1;
                break;
            case R.id.activity_home_tab_text2:
                setCurrentScreen(2);
                tabFlag = 2;
                break;
            default:
                break;
        }
    }

    private void setCurrentScreen(int index){
        mTabHost.setCurrentTab(index);
        naviText0.setSelected(false);
        naviText1.setSelected(false);
        naviText2.setSelected(false);
        switch (index){
            case 0:
                naviText0.setSelected(true);
                break;
            case 1:
                naviText1.setSelected(true);
                break;
            case 2:
                naviText2.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void registerBroadcast(){
        if (receiveBroadcast ==null){
            receiveBroadcast = new ReceiveBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BaseConstant.ACTION_HOME);
            registerReceiver(receiveBroadcast, filter);
        }
    }

    public class ReceiveBroadcast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            try {
                String action = intent.getAction();
                if (action.equals(BaseConstant.ACTION_HOME)){
                    tabFlag = intent.getExtras().getInt(BaseConstant.INTENT_TYPE);
                    new Handler().postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            setCurrentScreen(tabFlag);
                        }
                    }, 50);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void doACacheNeedsPermission() {

    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void ACacheShowRationale(PermissionRequest request) {
        request.proceed(); // 提示用户权限使用的对话框
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void ACacheOnPermissionDenied() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                PermissionUtil.showPermissionDialog(HomeActivity.this, naviText0);
            }
        }, 1000);
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void ACacheOnNeverAskAgain() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                PermissionUtil.showPermissionDialog(HomeActivity.this, naviText0);
            }
        }, 1000);
    }

    /**
     * 权限请求回调，提示用户之后，用户点击“允许”或者“拒绝”之后调用此方法
     * @param requestCode  定义的权限编码
     * @param permissions 权限名称
     * @param grantResults 允许/拒绝
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    long firstTime = 0;

    @Override
    public void onBackPressed() {
        try {
            //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
//            if (BaseConstant.IS_KEEP_LIVE){
//                CompatIntentWrapper.onBackPressed(this);
//            }else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    ToastUtil.addToast(this, getString(R.string.quit));
                    firstTime = secondTime;
                } else {
                    finishSelf();
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
