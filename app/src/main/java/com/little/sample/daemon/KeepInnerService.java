package com.little.sample.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.little.visit.util.LogUtil;


/**
 * Created by mmh on 2017/3/6.
 */
public class KeepInnerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("--------------KeepInnerService onStartCommand--------------");
        KeepLiveManager.getInstance().setInnerService(this);
        return super.onStartCommand(intent,flags,startId);
    }
}
