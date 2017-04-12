package com.little.sample.daemon;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.little.sample.R;
import com.little.sample.base.BaseApplication;
import com.little.sample.manager.ScreenManager;
import com.little.visit.util.LogUtil;


public class KeepLiveManager{
    private static KeepLiveManager instance;
    private final String ACCOUNT_TYPE = BaseApplication.PACKAGE_NAME;
    private final String CONTENT_AUTHORITY = BaseApplication.PACKAGE_NAME+"provider";
    private final long SYNC_FREQUENCY = 30;
    private Account account = null;
    private Service innerService;
    public final int foregroundPushId = 1;

    private KeepLiveManager() {
    }

    public static synchronized KeepLiveManager getInstance() {
        if (instance == null) {
            instance = new KeepLiveManager();
        }
        return instance;
    }

    public void startKeepLiveActivity(){
        BaseApplication.self().startKeepLiveActivity();
    }

    public void finishKeepLiveActivity(){
        ScreenManager.getScreenManagerInstance().closeAppoin(KeepLiveActivity.class);
    }

    /**
     * 启动业务服务
     */
    public void startKeepLiveService(){
        BaseApplication.self().startKeepLiveService();
    }

    /**
     * 停止业务服务
     */
    public void stopKeepLiveService(){
        TraceServiceImpl.stopService();
    }

    /**
     * 启动JobScheduler拉活
     * 适用范围：android5.0以上
     */
    public void startJobScheduler(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                JobInfo.Builder builder = new JobInfo.Builder(WatchDogService.HASH_CODE, new ComponentName(DaemonEnv.sApp, JobSchedulerService.class));
                builder.setPeriodic(DaemonEnv.getWakeUpInterval());
                //Android 7.0+ 增加了一项针对 JobScheduler 的新限制，最小间隔只能是下面设定的数字
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
                builder.setPersisted(true);
                JobScheduler scheduler = (JobScheduler) BaseApplication.self().getSystemService(WatchDogService.JOB_SCHEDULER_SERVICE);
                scheduler.schedule(builder.build());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 启动闹钟定时拉活
     * @return
     */
    public PendingIntent startAlarm(){
        try {
            PendingIntent sPendingIntent = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //Android 4.4- 使用 AlarmManager
                AlarmManager am = (AlarmManager) BaseApplication.self().getSystemService(WatchDogService.ALARM_SERVICE);
                Intent i = new Intent(DaemonEnv.sApp, DaemonEnv.sServiceClass);
                sPendingIntent = PendingIntent.getService(DaemonEnv.sApp, WatchDogService.HASH_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DaemonEnv.getWakeUpInterval(), DaemonEnv.getWakeUpInterval(), sPendingIntent);
            }
            return sPendingIntent;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于在不需要服务运行的时候取消 Job / Alarm / Subscription.
     */
    public void cancelJobAlarmSub() {
        try {
            if (!DaemonEnv.sInitialized) return;
            DaemonEnv.sApp.sendBroadcast(new Intent(WakeUpReceiver.ACTION_CANCEL_JOB_ALARM_SUB));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加账户，并启用账户同步功能
     * 适用范围：所有版本
     */
    public void addAccount(){
        try {
            AccountManager accountManager = (AccountManager)BaseApplication.self().getSystemService(Context.ACCOUNT_SERVICE);
            Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
            if (accounts!=null&&accounts.length>0){
                account = accounts[0];
            }else {
                account = new Account(BaseApplication.self().getString(R.string.account),ACCOUNT_TYPE);
            }
            if (accountManager.addAccountExplicitly(account,null,null)){
                //开启同步，并设置周期
                ContentResolver.setIsSyncable(account,CONTENT_AUTHORITY,1);
                ContentResolver.setSyncAutomatically(account,CONTENT_AUTHORITY,true);
                ContentResolver.addPeriodicSync(account,CONTENT_AUTHORITY,new Bundle(),SYNC_FREQUENCY);
                LogUtil.e("--------------开启同步，并设置周期--------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动更新
     */
    public void triggerRefresh() {
        try {
            if (account==null){
                return;
            }
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(
                    account,
                    CONTENT_AUTHORITY,
                    b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}