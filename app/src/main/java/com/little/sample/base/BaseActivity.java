package com.little.sample.base;


import android.app.Activity;
import android.os.Bundle;

import com.little.sample.manager.ScreenManager;
import com.little.sample.util.TranslucentUtil;
import com.little.visit.OKHttpManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends Activity{
	public String taskTag = "BaseActivity";//当前BaseActivity的线程标识
	protected ScreenManager screenManager = ScreenManager.getScreenManagerInstance();
	public OKHttpManager taskManager = OKHttpManager.getOkHttpManager();

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
	 * 关闭当前Activity中所有还在运行的线程
	 */
	protected void cancelTasks(){
		taskManager.cancelTasksByTag(taskTag);
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