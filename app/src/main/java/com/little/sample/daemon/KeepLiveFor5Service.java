package com.little.sample.daemon;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;

import com.little.visit.util.LogUtil;


public class KeepLiveFor5Service extends JobService {
    private static Service mKeepLiveService;
    private KeepLiveReceiver keepLiveReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("--------------KeepLiveFor5Service onStartCommand--------------");
        mKeepLiveService = this;
        KeepLiveManager.getInstance().setForeground(this);
        KeepLiveManager.getInstance().addAccount();
        KeepLiveManager.getInstance().startJobScheduler();
        try {
            startService(new Intent(this, KeepInnerService.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

//    public static class InnerService extends Service{
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            KeepLiveManager.getInstance().setForeground(mKeepLiveService,this);
//            return super.onStartCommand(intent,flags,startId);
//        }
//
//
//    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(keepLiveReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void registerBroadcast(){
        try {
            if (keepLiveReceiver == null){
                keepLiveReceiver = new KeepLiveReceiver();
                IntentFilter receiverFilter=new IntentFilter();
                receiverFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                receiverFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
                receiverFilter.addAction(Intent.ACTION_POWER_CONNECTED);
                receiverFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
                receiverFilter.addAction(Intent.ACTION_USER_PRESENT);
                receiverFilter.addAction(Intent.ACTION_SCREEN_ON);
                receiverFilter.addAction(Intent.ACTION_SCREEN_OFF);
                registerReceiver(keepLiveReceiver, receiverFilter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
