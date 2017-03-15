package com.little.sample.daemon;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.little.visit.util.LogUtil;


public class KeepLiveService extends Service {
    private static Service mKeepLiveService;
    private KeepLiveReceiver keepLiveReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("--------------KeepLiveService onStartCommand--------------");
        mKeepLiveService = this;
        KeepLiveManager.getInstance().setForeground(this);
        KeepLiveManager.getInstance().addAccount();
        try {
            startService(new Intent(this, KeepInnerService.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Service.START_STICKY;
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
