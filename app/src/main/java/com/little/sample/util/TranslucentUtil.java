package com.little.sample.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.little.sample.R;
import com.little.sample.manager.SystemBarTintManager;

/**
 * Created by mmh on 2017/3/10.
 */
public class TranslucentUtil {
    private Activity activity;

    public TranslucentUtil(Activity activity) {
        this.activity = activity;
    }

    private static TranslucentUtil instance;

    public static synchronized TranslucentUtil getInstance(Activity activity) {
        if (instance == null) {
            instance = new TranslucentUtil(activity);
        }
        return instance;
    }

    /**
     * 设置状态栏颜色
     */
    public void setKitKatTranslucency() {
        applyKitKatTranslucency(R.color.title_color);
    }

    /**
     * 设置状态栏颜色
     */
    public void setKitKatTranslucency(int colorId) {
        applyKitKatTranslucency(colorId);
    }

    /**
     * Apply KitKat specific translucency.
     */
    public void applyKitKatTranslucency(int colorId) {
        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(activity);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            mTintManager.setTintResource(colorId);
            mTintManager.setStatusBarTintResource(colorId);
            mTintManager.setNavigationBarTintResource(colorId);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
