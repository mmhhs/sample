package com.little.sample.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by mmh on 2017/3/14.
 */
public class SystemUtil {

    /**
     * 安装
     *
     * @param context 接收外部传进来的context
     */
    public static void install(Context context, String storePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(storePath)),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地apk文件的版本号
     *
     * @param context
     * @param storePath
     * @return
     */
    public static int getApkVersionCode(Context context, String storePath) {
        int versionCode = 1;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(storePath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            versionCode = info.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取本机号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        String result = "";
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            result = mTelephonyMgr.getLine1Number();
            if (result.startsWith("+86")) {
                result = result.replace("+86", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static int getSystemVersion() {
        int sysVersion = Build.VERSION.SDK_INT;
        return sysVersion;
    }


    /**
     * 获取设备Id和手机品牌以“_”分隔
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String result = "" + tm.getDeviceId() + "_" + Build.BRAND;
        return result;
    }


    /**
     * 获取版本号
     *
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
     *
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

    /**
     * webView基本设置
     *
     * @param webView
     */
    public static void setBaseWebSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点
        webView.requestFocusFromTouch();
        webView.requestFocus();
        //打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true);//关键点 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//		webSettings.setBlockNetworkImage(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        //其他
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//支持内容重新布局
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //webview中缓存
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.supportMultipleWindows();  //多窗口
        // 开启 DOM storage API 功能?
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能?
        webSettings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDefaultTextEncodingName("UTF-8");// 设置默认编码
//		webSettings.setUserAgentString(Application.getUserAgent());
//		String cacheDirPath = BaseConstant.IMAGETAMPPATH;
        //设置? Application Caches 缓存目录?
//		webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能?
        webSettings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webSettings, true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置webview 不支持缩放
     *
     * @param webView
     */
    public static void setWebSettingNoZoom(WebView webView) {
        setBaseWebSetting(webView);
        WebSettings webSettings = webView.getSettings();
        //页面支持缩放
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setSupportZoom(false); // 支持缩放

    }

    /**
     * 设置webview 支持缩放
     *
     * @param webView
     */
    public static void setWebSettingWithZoom(final WebView webView) {
        setBaseWebSetting(webView);
        WebSettings webSettings = webView.getSettings();
        //页面支持缩放
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setDisplayZoomControls(false);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                webView.getContext().startActivity(intent);
            }
        });
    }
}
