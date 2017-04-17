package com.little.sample.base;


import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.little.picture.util.fresco.FrescoUtils;
import com.little.sample.daemon.DaemonHelper;
import com.little.sample.daemon.KeepLiveActivity;
import com.little.sample.daemon.KeepLiveManager;
import com.little.sample.daemon.KeepWorkService;
import com.little.sample.util.ProcessUtil;
import com.little.sample.util.StringUtil;
import com.little.visit.util.LogUtil;


public class BaseApplication extends MultiDexApplication {
    private static BaseApplication baseApplication;
    private String processName = "";
    public static String PACKAGE_NAME;
    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE_NAME = getPackageName();
        baseApplication = this;
        init();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        attach(context);
    }

    private void init(){
        if (BaseConstant.IS_KEEP_LIVE){
            LogUtil.e("--------------DaemonHelper.initialize--------------");
            DaemonHelper.initialize(this, KeepWorkService.class, DaemonHelper.DEFAULT_WAKE_UP_INTERVAL);
        }
        BaseConstant.init(baseApplication);
        processName = ProcessUtil.getProcessName(this, android.os.Process.myPid());
        if (!StringUtil.isEmpty(processName)) {
            boolean defaultProcess = processName.equals(getPackageName());
            LogUtil.e("defaultProcess= "+defaultProcess);
            if (defaultProcess) {
                initAppForMainProcess();
            } else if (processName.contains(":watch")) {
                initAppForLiveProcess();
            }
        }
    }

    /**
     * 主进程执行初始化
     */
    private void initAppForMainProcess(){
        FrescoUtils.init(this);
        KeepLiveManager.getInstance().startKeepLiveService();
        KeepLiveManager.getInstance().addAccount();
    }

    private void initAppForLiveProcess(){

    }


    private void attach(Context base){
        processName = ProcessUtil.getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(getPackageName());
            if (defaultProcess) {
                MultiDex.install(base);
//                startKeepLiveService();
            } else if (processName.contains(":watch")) {

            }
        }
    }

    public static BaseApplication self(){
        return baseApplication;
    }

    /**
     * 启动保活service
     */
    public void startKeepLiveService(){
        try {
            if (BaseConstant.IS_KEEP_LIVE){
//                LogUtil.e("--------------DaemonHelper.initialize--------------");
//                DaemonHelper.initialize(this, KeepWorkService.class, DaemonHelper.DEFAULT_WAKE_UP_INTERVAL);
                startService(new Intent(this, KeepWorkService.class));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 启动一像素activity
     */
    public void startKeepLiveActivity(){
        try {
            Intent intent = new Intent(this, KeepLiveActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}