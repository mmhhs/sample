package com.little.sample.base;


import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.little.picture.util.fresco.FrescoUtils;


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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        attach(base);
    }

    private void init(){
        initAppForMainProcess();
//        processName = OptionUtil.getProcessName(this, android.os.Process.myPid());
//        if (processName != null) {
//            boolean defaultProcess = processName.equals(getPackageName());
//            if (defaultProcess) {
//                initAppForMainProcess();
//            } else if (processName.contains(":live")) {
//                initAppForLiveProcess();
//            }
//        }
    }

    private void initAppForMainProcess(){
        FrescoUtils.init(this);
    }

    private void initAppForLiveProcess(){

    }


    private void attach(Context base){
//        processName = OptionUtil.getProcessName(this, android.os.Process.myPid());
//        if (processName != null) {
//            boolean defaultProcess = processName.equals(getPackageName());
//            if (defaultProcess) {
//                MultiDex.install(base);
//                startKeepLiveService();
//            } else if (processName.contains(":live")) {
//
//            }
//        }
    }

    public static BaseApplication self(){
        return baseApplication;
    }

//    public void startKeepLiveService(){
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                startService(new Intent(this, KeepLiveFor5Service.class));
//            }else {
//                startService(new Intent(this, KeepLiveService.class));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public void startKeepLiveActivity(){
//        try {
//            Intent intent = new Intent(this, KeepLiveActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }






}