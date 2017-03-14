package com.little.sample.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mmh on 2017/3/14.
 */
public class SharedPreferencesUtil {
    public final static String HELP = "HELP";//引导页
    public final static String IGNOREVERSION = "IGNOREVERSION";//忽略版本
    public final static String ADIMAGEPATH = "ADIMAGEPATH";//广告图片路径

    /**
     * 保存帮助界面显示状态
     * @param context
     * @param status true表示需要显示
     */
    public static void saveHelpStatus(Context context, boolean status,int code)
    {
        SharedPreferences sp = context.getSharedPreferences(HELP, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putBoolean(HELP, status);
        editor.putInt("code", code);
        editor.commit();
    }
    public static boolean getHelpStatus(Context context){

        SharedPreferences sp = context.getSharedPreferences(HELP, 0);
        boolean status = sp.getBoolean(HELP, true);
        return status;
    }
    public static int getHelpCode(Context context){

        SharedPreferences sp = context.getSharedPreferences(HELP, 0);
        int code = sp.getInt("code", 1);
        return code;
    }

    /**
     * 保存忽略版本
     * @param context
     * @param version 忽略版本
     */
    public static void saveIgnoreVersion(Context context, String version)
    {
        SharedPreferences sp = context.getSharedPreferences(IGNOREVERSION, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString(IGNOREVERSION, version);
        editor.commit();
    }
    public static String getIgnoreVersion(Context context){

        SharedPreferences sp = context.getSharedPreferences(IGNOREVERSION, 0);
        String id = sp.getString(IGNOREVERSION, "");
        return id;
    }

    /**
     * 保存广告图片路径
     * @param context
     * @param url
     */
    public static void saveAdImage(Context context, String url)
    {
        SharedPreferences sp = context.getSharedPreferences(ADIMAGEPATH, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString(ADIMAGEPATH, url);
        editor.commit();
    }
    public static String getAdImage(Context context){

        SharedPreferences sp = context.getSharedPreferences(ADIMAGEPATH, 0);
        String id = sp.getString(ADIMAGEPATH, "");
        return id;
    }
}
