package com.little.sample.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by mmh on 2017/3/14.
 */
public class SystemUtil {
    /**
     * 获取系统版本
     * @return
     */
    public static int getSystemVersion(){
        int sysVersion = Build.VERSION.SDK_INT;
        return sysVersion;
    }


    /**
     * 获取设备Id和手机品牌以“_”分隔
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String result = ""+tm.getDeviceId()+"_"+ Build.BRAND;
        return result;
    }


    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int code = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取版本名称
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
