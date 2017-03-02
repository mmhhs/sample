package com.little.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.little.popup.adapter.PopupListAdapter;
import com.little.popup.listener.IOnDialogListener;
import com.little.popup.listener.IOnDismissListener;
import com.little.popup.listener.IOnItemListener;
import com.little.popup.util.AlphaUtil;
import com.little.popup.util.StringUtil;

import java.util.List;

public class PopupDialog {
    private Context context;
    private Activity activity;

    private float showAlpha = 0.5f;//显示对话框时界面半透明度
    private float hideAlpha = 1f;//对话框关闭后界面恢复
    /**
     * 弹框半透明两种实现，0采用设置界面透明度，1采用半透明背景色
     * 设为 1时，activity不能为null
     */
    private int alphaType = 1;

    private boolean dismissOutside = false;//点击界面关闭
    private boolean dismissBackKey = false;//点击返回键关闭

    private int animStyle = 0;//动画资源ID
    private String dialogTitle = "";//对话框标题

    /**
     * 提示对话框操作数量 优先级：确定>取消>其他
     * 0不显示，1显示确定，2显示确定、取消，3显示确定、取消、其他
     */
    private int optionCount = 2;
    private String confirmStr = "";//提示对话框确定按钮名称
    private String cancelStr = "";//提示对话框取消按钮名称
    private String otherStr = "";//提示对话框其他按钮名称

    private IOnItemListener onItemListener;//列表对话框监听
    private IOnDialogListener onDialogListener;//提示对话框监听
    private IOnDismissListener onDismissListener;//对话框消失监听

    public PopupDialog(Context context) {
        this.context = context;
    }

    public static class Builder{

        //Required
        private Context context;
        public Builder(Context context){
            this.context = context;
        }

        //Option
        private Activity activity;
        private int alphaType = 1;//弹框半透明两种实现，0采用设置界面透明度，1采用半透明背景色
        private boolean dismissOutside = false;//点击界面关闭
        private boolean dismissBackKey = false;//点击返回键关闭
        private int optionCount = 2;//提示对话框操作数量 优先级：确定>取消>其他
        private String confirmStr = "确定";//提示对话框确定按钮名称
        private String cancelStr = "取消";//提示对话框取消按钮名称
        private String otherStr = "忽略";//提示对话框其他按钮名称
        private IOnItemListener onItemListener;//列表对话框监听
        private IOnDialogListener onDialogListener;//提示对话框监听
        private IOnDismissListener onDismissListener;//对话框消失监听

        public Builder setAlphaType(int alphaType,Activity activity){
            this.alphaType = alphaType;
            this.activity = activity;
            return this;
        }

        public Builder dismissOutside(boolean dismissOutside){
            this.dismissOutside = dismissOutside;
            return this;
        }

        public Builder dismissBackKey(boolean dismissBackKey){
            this.dismissBackKey = dismissBackKey;
            return this;
        }

        public Builder confirmStr(String confirmStr){
            this.confirmStr = confirmStr;
            return this;
        }

        public Builder cancelStr(String cancelStr){
            this.cancelStr = cancelStr;
            return this;
        }

        public Builder otherStr(String otherStr){
            this.otherStr = otherStr;
            return this;
        }

        public Builder optionCount(int optionCount){
            this.optionCount = optionCount;
            return this;
        }

        public Builder onItemListener(IOnItemListener onItemListener){
            this.onItemListener = onItemListener;
            return this;
        }

        public Builder onDialogListener(IOnDialogListener onDialogListener){
            this.onDialogListener = onDialogListener;
            return this;
        }

        public Builder onDismissListener(IOnDismissListener onDismissListener){
            this.onDismissListener = onDismissListener;
            return this;
        }

        public PopupDialog build(){
            return new PopupDialog(this);
        }
    }

    public PopupDialog(Builder builder){
        this.context = builder.context;
        this.alphaType = builder.alphaType;
        this.activity = builder.activity;
        this.dismissOutside = builder.dismissOutside;
        this.dismissBackKey = builder.dismissBackKey;
        this.confirmStr = builder.confirmStr;
        this.cancelStr = builder.cancelStr;
        this.otherStr = builder.otherStr;
        this.optionCount = builder.optionCount;
        this.onItemListener = builder.onItemListener;
        this.onDialogListener = builder.onDialogListener;
        this.onDismissListener = builder.onDismissListener;
    }

    /**
     * 设置半透明
     * @param containerView
     */
    private void showAlpha(View containerView){
        if (alphaType==0){
            AlphaUtil.setBackgroundAlpha(activity, showAlpha);
        }else {
            containerView.setBackgroundResource(R.color.popup_transparent);
        }
    }

    /**
     * 取消半透明
     */
    private void hideAlpha(){
        if (alphaType==0){
            AlphaUtil.setBackgroundAlpha(activity, hideAlpha);
        }
    }

    /**
     * 弹框半透明两种实现-采用设置界面透明度
     * @param activity
     */
    public void setScreenAlphaStyle(Activity activity){
        this.activity = activity;
        alphaType = 0;
    }

    /**
     * 显示列表对话框
     * @param view 父视图
     * @param stringList 数据
     */
    public PopupWindow showListDialog(View view, List<String> stringList){
        PopupWindow popupWindow = getListDialog(stringList);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getListDialog(List<String> stringList){
        View view = LayoutInflater.from(context).inflate(R.layout.popup_dialog_list,null, false);
        ListView listView = (ListView) view.findViewById(R.id.popup_dialog_list_listview);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.popup_dialog_list_container_layout);
        PopupListAdapter adapter = new PopupListAdapter(context,stringList);
        listView.setAdapter(adapter);
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (onItemListener !=null){
                    onItemListener.onItem(arg2);
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }

    /**
     * 显示提示对话框
     * @param view 承载视图
     * @param message 提示内容
     */
    public PopupWindow showTipDialog(View view, String message){
        PopupWindow popupWindow = getTipDialog(message);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public PopupWindow getTipDialog(String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_dialog_tip,null, false);
        TextView titleText = (TextView) view.findViewById(R.id.popup_dialog_tip_title);
        TextView messageText = (TextView) view.findViewById(R.id.popup_dialog_tip_content);
        TextView confirmText = (TextView) view.findViewById(R.id.popup_dialog_tip_confirm);
        TextView cancelText = (TextView) view.findViewById(R.id.popup_dialog_tip_cancel);
        TextView otherText = (TextView) view.findViewById(R.id.popup_dialog_tip_other);
        LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.popup_dialog_tip_container_layout);
        if(!StringUtil.isEmpty(dialogTitle)){
            titleText.setText(dialogTitle);
            titleText.setVisibility(View.VISIBLE);
        }else {
            titleText.setVisibility(View.GONE);
        }
        if(!StringUtil.isEmpty(message)){
            messageText.setText(message);
        }
        if (!StringUtil.isEmpty(confirmStr)){
            confirmText.setText(confirmStr);
        }
        if (!StringUtil.isEmpty(cancelStr)){
            cancelText.setText(cancelStr);
        }
        if (!StringUtil.isEmpty(otherStr)){
            otherText.setText(otherStr);
        }
        switch (optionCount){
            case 0:
                confirmText.setVisibility(View.GONE);
                cancelText.setVisibility(View.GONE);
                otherText.setVisibility(View.GONE);
                break;
            case 1:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.GONE);
                otherText.setVisibility(View.GONE);
                break;
            case 2:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.VISIBLE);
                otherText.setVisibility(View.GONE);
                break;
            case 3:
                confirmText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.VISIBLE);
                otherText.setVisibility(View.VISIBLE);
                break;
        }
        showAlpha(containerLayout);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        if(dismissBackKey){
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); //使按返回键能够消失
        }
        if(animStyle>0){
            popupWindow.setAnimationStyle(animStyle);
        }
        confirmText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onConfirm();
                }
                popupWindow.dismiss();
            }

        });
        cancelText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onCancel();
                }
                popupWindow.dismiss();
            }

        });
        otherText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (onDialogListener !=null){
                    onDialogListener.onOther();
                }
                popupWindow.dismiss();
            }

        });
        containerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dismissOutside) {
                    popupWindow.dismiss();
                }
            }

        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAlpha();
                if (onDismissListener !=null){
                    onDismissListener.onDismiss();
                }
            }
        });
        return popupWindow;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getAlphaType() {
        return alphaType;
    }

    public void setAlphaType(int alphaType) {
        this.alphaType = alphaType;
    }

    public boolean isDismissOutside() {
        return dismissOutside;
    }

    public void setDismissOutside(boolean dismissOutside) {
        this.dismissOutside = dismissOutside;
    }

    public boolean isDismissBackKey() {
        return dismissBackKey;
    }

    public void setDismissBackKey(boolean dismissBackKey) {
        this.dismissBackKey = dismissBackKey;
    }

    public int getAnimStyle() {
        return animStyle;
    }

    public void setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }

    public String getConfirmStr() {
        return confirmStr;
    }

    public void setConfirmStr(String confirmStr) {
        this.confirmStr = confirmStr;
    }

    public String getCancelStr() {
        return cancelStr;
    }

    public void setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
    }

    public String getOtherStr() {
        return otherStr;
    }

    public void setOtherStr(String otherStr) {
        this.otherStr = otherStr;
    }

    public IOnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(IOnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public IOnDialogListener getOnDialogListener() {
        return onDialogListener;
    }

    public void setOnDialogListener(IOnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    public IOnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(IOnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}