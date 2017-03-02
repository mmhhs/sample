package com.little.popup.util;

import android.app.Activity;
import android.view.WindowManager;

public class AlphaUtil {

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha   //0.0-1.0
     */
    public static void setBackgroundAlpha(Activity activity,float bgAlpha)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

}