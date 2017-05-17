package com.little.sample.daemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.little.sample.base.BaseApplication;
import com.little.sample.base.BaseConstant;
import com.little.visit.util.LogUtil;

public class WakeUpReceiver extends BroadcastReceiver {

    /**
     * 向 WakeUpReceiver 发送带有此 Action 的广播, 即可在不需要服务运行的时候取消 Job / Alarm / Subscription.
     */
    protected static final String ACTION_CANCEL_JOB_ALARM_SUB = "com.little.sample.CANCEL_JOB_ALARM_SUB";

    /**
     * 监听 8 种系统广播 :
     * CONNECTIVITY\_CHANGE, USER\_PRESENT, ACTION\_POWER\_CONNECTED, ACTION\_POWER\_DISCONNECTED,
     * BOOT\_COMPLETED, MEDIA\_MOUNTED, PACKAGE\_ADDED, PACKAGE\_REMOVED.
     * 在网络连接改变, 用户屏幕解锁, 电源连接 / 断开, 系统启动完成, 挂载 SD 卡, 安装 / 卸载软件包时拉起 Service.
     * Service 内部做了判断，若 Service 已在运行，不会重复启动.
     * 运行在:watch子进程中.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            LogUtil.e("--------------WakeUpReceiver--------------");
            if (intent==null){
                return;
            }
            String action = intent.getAction();
            if (ACTION_CANCEL_JOB_ALARM_SUB.equals(action)) {
                KeepWatchService.cancelJobAlarmSub();
                return;
            }
            if (!DaemonHelper.sInitialized) {
                return;
            }

            //启动保活服务
            context.startService(new Intent(context, DaemonHelper.sServiceClass));

            if (action.equals(Intent.ACTION_SCREEN_OFF)){
                LogUtil.e("--------------startKeepLiveActivity--------------");
                KeepLiveManager.getInstance().startKeepLiveActivity();
            }else if (action.equals(Intent.ACTION_USER_PRESENT)||action.equals(Intent.ACTION_SCREEN_ON)){
                LogUtil.e("--------------finishKeepLiveActivity--------------");
                KeepLiveManager.getInstance().finishKeepLiveActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class WakeUpAutoStartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogUtil.e("--------------WakeUpAutoStartReceiver--------------");
                if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                    LogUtil.e("--------------ACTION_BOOT_COMPLETED--------------");
                    if (BaseConstant.IS_KEEP_LIVE){
                        DaemonHelper.initialize(BaseApplication.self(), KeepWorkService.class, DaemonHelper.DEFAULT_WAKE_UP_INTERVAL);
                    }
                }
                if (!DaemonHelper.sInitialized) {
                    return;
                }
                //启动保活服务
                context.startService(new Intent(context, DaemonHelper.sServiceClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
