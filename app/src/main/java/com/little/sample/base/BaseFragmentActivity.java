package com.little.sample.base;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.little.sample.listener.IOnPermissionListener;
import com.little.sample.manager.MFragmentsManager;
import com.little.sample.manager.ScreenManager;
import com.little.sample.util.TranslucentUtil;
import com.little.visit.TaskManager;
import com.little.visit.task.VisitTask;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends FragmentActivity implements IOnPermissionListener {
	public String taskTag = "BaseFragmentActivity";//当前BaseFragmentActivity的线程标识
	protected ScreenManager screenManager = ScreenManager.getScreenManagerInstance();
	public TaskManager taskManager = TaskManager.getTaskManagerInstance();
	public MFragmentsManager mFragmentsManager = MFragmentsManager.getFragmentManagerInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenManager.pushActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.inject(this);
		TranslucentUtil.getInstance(this).setKitKatTranslucency();
		init();
	}

	@Override
	protected void onPause(){
		super.onPause();
	}

	@Override
	protected void onResume(){
		super.onResume();
	}

	@Override
	protected void onDestroy(){
		cancelTasks();
		super.onDestroy();
	}

	/**
	 * 初始化相关操作
	 */
	public abstract void init();

	/**
	 * 添加线程到线程管理中
	 * @param task
	 */
	protected void addTask(VisitTask task){
		try {
			taskManager.addTask(taskTag, task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭当前FragmentActivity中所有还在运行的线程
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

	/**
	 * 删除所有Fragment
	 */
	public void removeAllFragments(){
		mFragmentsManager.removeAllFragment(getSupportFragmentManager());
	}

	/**
	 * 关闭当前Activity
	 */
	public void finishSelf(){
		try {
			screenManager.closeActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}




}