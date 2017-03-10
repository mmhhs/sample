package com.little.sample.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by mmh on 2017/3/10.
 */
public class ProcessUtil {
    /**
     * 获取进程名字
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
