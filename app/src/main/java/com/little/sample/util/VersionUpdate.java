package com.little.sample.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnDialogListener;
import com.little.popup.listener.IOnDismissListener;
import com.little.sample.base.BaseConstant;
import com.little.sample.manager.ScreenManager;
import com.little.sample.model.VersionDataEntity;
import com.little.sample.model.VersionResult;
import com.little.visit.TaskConstant;
import com.little.visit.listener.IOnProgressListener;
import com.little.visit.listener.IOnResultListener;
import com.little.visit.task.DownloadVisitTask;
import com.little.visit.task.PopupVisitTask;
import com.little.visit.task.VisitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 版本更新
 */
public class VersionUpdate {
    private Context context;//上下文
    private View contentView;//承载视图
    private String taskTag;//线程标识
    private VersionDataEntity dataEntity;
    private boolean showToast = false;//是否显示无需更新提示
    private boolean showIgnoreTip = false;//忽略版本是否显示更新提示
    private ScreenManager screenManager;
    private IOnResultListener onResultListener;
    private boolean needDownLoad = false;//是否需要下载APK

    public VersionUpdate(Activity context, View contentView, String taskTag) {
        this.context = context;
        this.contentView = contentView;
        this.taskTag = taskTag;
        screenManager = ScreenManager.getScreenManagerInstance();
    }

    public void checkVersion(boolean showDialog, final boolean needUpdate) {
        String httpUrl = BaseConstant.VERSION;
        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("version", SystemUtil.getVersionName(context));
        argMap.put("versionCode", "" + SystemUtil.getVersionCode(context));
        argMap.put("deviceType", "1");//设备类型
        PopupVisitTask visitTask = new PopupVisitTask(context, taskTag, contentView, "", showDialog, httpUrl, argMap, TaskConstant.POST);
        visitTask.setParseClass(VersionResult.class);
        visitTask.setiOnResultListener(new IOnResultListener() {
            @Override
            public void onSuccess(VisitTask task) {
                if (task.getResultEntity() instanceof VersionResult) {
                    VersionResult res = (VersionResult) task.getResultEntity();
                    dataEntity = res.data;
                }
                if (needUpdate) {
                    updateVersion();
                }
                if (onResultListener != null) {
                    onResultListener.onSuccess(task);
                }
            }

            @Override
            public void onError(VisitTask task) {
                if (onResultListener != null) {
                    onResultListener.onError(task);
                }
            }

            @Override
            public void onDone(VisitTask task) {
                if (onResultListener != null) {
                    onResultListener.onDone(task);
                }
            }
        });
        visitTask.execute();
    }

    private void updateVersion() {
        try {
            if (dataEntity != null) {
                int appVersion = Integer.parseInt(dataEntity.version);
                if (appVersion > SystemUtil.getVersionCode(context)) {
                    boolean showDialog = true;
                    if (dataEntity.version.equals(SharedPreferencesUtil.getIgnoreVersion(context))) {
                        showDialog = false;
                    }
                    if (showIgnoreTip) {
                        showDialog = true;
                    }
                    if (showDialog) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                PopupDialog popupDialog = new PopupDialog(context);
                                popupDialog.setDialogTitle("有新版本啦，马上更新!");
                                popupDialog.showTipDialog(contentView, "");
                                popupDialog.setOptionCount(3);
                                popupDialog.setOnDialogListener(new IOnDialogListener() {
                                    @Override
                                    public void onConfirm() {
                                        File apkFile = new File(BaseConstant.APK_PATH);
                                        needDownLoad = true;
                                        if (apkFile.exists()) {
                                            int versionCode = SystemUtil.getApkVersionCode(context, BaseConstant.APK_PATH);
                                            if (dataEntity.version.equals("" + versionCode)) {
                                                needDownLoad = false;
                                            }
                                        }
                                        if (needDownLoad) {
                                            downLoadApk(dataEntity.download, BaseConstant.APK_PATH);
                                        } else {
                                            SystemUtil.install(context, BaseConstant.APK_PATH);
                                        }
                                    }

                                    @Override
                                    public void onCancel() {

                                    }

                                    @Override
                                    public void onOther() {
                                        if (dataEntity.forceUpdate == 1) {
                                            screenManager.closeAll();
                                        } else {
                                            SharedPreferencesUtil.saveIgnoreVersion(context, "" + dataEntity.version);
                                        }
                                    }
                                });
                                popupDialog.setOnDismissListener(new IOnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        if (dataEntity.forceUpdate == 1 && !needDownLoad) {
                                            screenManager.closeAll();
                                        }
                                    }
                                });
                            }
                        }, 500);
                    }
                } else {
                    if (showToast) {
                        ToastUtil.addToast(context, "已经是最新的版本");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoadApk(String updateUrl, String storePath) {
        DownloadVisitTask downloadVisitTask = new DownloadVisitTask(context, contentView, "下载更新", false, false, updateUrl, storePath);
        downloadVisitTask.setOnProgressListener(new IOnProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onTransferred(String transferedBytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String fileStorePath) {
                SystemUtil.install(context, fileStorePath);
            }

            @Override
            public void onError(String tip) {
                ToastUtil.addToast(context, tip);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDone() {

            }
        });
        downloadVisitTask.execute();
    }

    public boolean isShowToast() {
        return showToast;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public boolean isShowIgnoreTip() {
        return showIgnoreTip;
    }

    public void setShowIgnoreTip(boolean showIgnoreTip) {
        this.showIgnoreTip = showIgnoreTip;
    }

    public IOnResultListener getOnResultListener() {
        return onResultListener;
    }

    public void setOnResultListener(IOnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}