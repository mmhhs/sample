package com.little.sample.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.little.sample.manager.MFragmentsManager;
import com.little.sample.manager.ScreenManager;
import com.little.visit.TaskManager;
import com.little.visit.task.VisitTask;


public abstract class BaseFragment extends Fragment {
    public String taskTag = "BaseFragment";//当前Fragment的线程标识
    public TaskManager taskManager = TaskManager.getTaskManagerInstance();
    public MFragmentsManager mFragmentsManager = MFragmentsManager.getFragmentManagerInstance();
    public ScreenManager screenManager = ScreenManager.getScreenManagerInstance();
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;
    protected boolean isPrepared;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentsManager.addFragment(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        cancelTasks();
        super.onDestroy();
    }

    /**
     * 初始化视图相关操作
     */
    public abstract void init();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    /**
     * 添加线程到线程管理中
     * @param task
     */
    protected void addTask(VisitTask task){
        try {
            taskManager.addTask(taskTag, task);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 关闭当前Fragment中所有还在运行的线程
     */
    protected void cancelTasks(){
        taskManager.cancelLimitTasks(taskTag);
    }

    /**
     * 获取线程标识
     * @return
     */
    public String getTaskTag() {
        return taskTag;
    }

    /**
     * 设置线程标识
     * @param taskTag
     */
    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }


}