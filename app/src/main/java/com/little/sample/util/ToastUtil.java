package com.little.sample.util;

import android.widget.Toast;

import com.little.sample.base.BaseApplication;

public class ToastUtil {

//    public static void addToast(Context context,String value){
//        try {
//            Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void addToast(String value){
        try {
            Toast.makeText(BaseApplication.self(), value, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}