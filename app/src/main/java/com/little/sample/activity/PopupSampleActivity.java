package com.little.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnDialogListener;
import com.little.popup.listener.IOnItemListener;
import com.little.sample.R;
import com.little.sample.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 弹窗示例
 */
public class PopupSampleActivity extends Activity {
    private List<String> testList = new ArrayList<String>();
    private PopupDialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_sample);
        ButterKnife.bind(this);
        for(int i=0;i<20;i++){
            testList.add("第"+i+"条");
        }

    }


    @OnClick({R.id.activity_popup_sample_list, R.id.activity_popup_sample_dialog1, R.id.activity_popup_sample_dialog2, R.id.activity_popup_sample_dialog3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_popup_sample_list:
                popupDialog = new PopupDialog.Builder(this).dismissBackKey(true).dismissOutside(true).onItemListener(new IOnItemListener() {
                    @Override
                    public void onItem(int position) {
                        ToastUtil.addToast("您选择了："+testList.get(position));
                    }
                }).build();
                popupDialog.showListDialog(view,testList);
                break;
            case R.id.activity_popup_sample_dialog1:
                popupDialog = new PopupDialog.Builder(this)
                        .dismissBackKey(true)
                        .dismissOutside(true)
                        .optionCount(1)
                        .dialogTitle("标题--友情提示")
                        .confirmStr("知道了")
                        .onDialogListener(new IOnDialogListener() {
                            @Override
                            public void onConfirm() {
                                ToastUtil.addToast("您点击了确定按钮");
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onOther() {

                            }
                        }).build();
                popupDialog.showTipDialog(view, "您的手机已欠费！");
                break;
            case R.id.activity_popup_sample_dialog2:
                popupDialog = new PopupDialog.Builder(this)
                        .setAlphaType(0, PopupSampleActivity.this)
                        .dismissBackKey(true)
                        .dismissOutside(true)
                        .optionCount(2)
                        .confirmStr("确定")
                        .cancelStr("取消")
                        .onDialogListener(new IOnDialogListener() {
                            @Override
                            public void onConfirm() {
                                ToastUtil.addToast("您点击了确定按钮");
                            }

                            @Override
                            public void onCancel() {
                                ToastUtil.addToast("您点击了取消按钮");
                            }

                            @Override
                            public void onOther() {

                            }
                        }).build();
                popupDialog.showTipDialog(view, "您是否确定删除？（不显示标题，换第一种半透明方式）");
                break;
            case R.id.activity_popup_sample_dialog3:
                popupDialog = new PopupDialog.Builder(this)
                        .dismissBackKey(true)
                        .dismissOutside(true)
                        .optionCount(3)
                        .dialogTitle("友情提示")
                        .confirmStr("确定")
                        .cancelStr("取消")
                        .otherStr("忽略")
                        .onDialogListener(new IOnDialogListener() {
                            @Override
                            public void onConfirm() {
                                ToastUtil.addToast("您点击了确定按钮");
                            }

                            @Override
                            public void onCancel() {
                                ToastUtil.addToast("您点击了取消按钮");
                            }

                            @Override
                            public void onOther() {
                                ToastUtil.addToast("您点击了忽略按钮");
                            }
                        }).build();
                popupDialog.showTipDialog(view, "当前有新版本，是否马上更新？");
                break;
        }
    }
}
