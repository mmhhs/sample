package com.little.sample.base;

import android.content.Context;

import com.little.visit.util.LogUtil;

import java.io.File;

/**
 * 常量类
 */
public class BaseConstant {
    public static final boolean IS_DEBUG = true;//调试模式
    public static final boolean IS_KEEP_LIVE = true;//开启保活
    public static String IMAGE_SAVE_PATH = "";//图片存储路径
    public static final int SCALE_WIDTH = 360;//压缩图片的目标宽度
    public static final int SCALE_HEIGHT = 640;//压缩图片的目标高度
    public static final int PAGER_START =  1;//分页加载时，默认开始页数
    public static final int PAGER_SIZE =  16;//分页加载时，每页返回数量

    public static void init(Context context){
        IMAGE_SAVE_PATH = context.getExternalFilesDir("") + "/cache/image/";
        File saveFile = new File(IMAGE_SAVE_PATH);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        LogUtil.setIsDebug(IS_DEBUG);//设置打印日志开关
    }

    /**
     * 访问主域名
     */
    public static final String SERVICE_HOST_IP = "http://app.supertoys.com.cn:8080";
    /**
     * 版本更新
     */
    public static final String VERSION = SERVICE_HOST_IP+"/version/getVersion";
}
