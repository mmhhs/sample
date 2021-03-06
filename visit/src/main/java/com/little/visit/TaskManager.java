package com.little.visit;


import com.little.visit.task.AsycnTask;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
	public String tag = "TaskManager";
	public List<TaskModel> taskList = new ArrayList<TaskModel>();

	private static TaskManager instance;

	private TaskManager() {
	}

	public static synchronized TaskManager getTaskManagerInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	/**
	 * 添加线程
	 */
	public void addTask(String tagString,AsycnTask task){
		try {
			TaskModel taskModel = new TaskModel();
			taskModel.tagString = tagString;
			taskModel.task = task;
			taskModel.creatTime = System.currentTimeMillis();
			taskList.add(taskModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭所有还在运行的线程
	 */
	public void cancelAllTasks(){
		for(int i=0;i<taskList.size();i++){
			try {
				TaskModel taskModel = taskList.get(i);
				if(taskModel.task!=null){
					taskModel.task.cancel(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		taskList.clear();
	}

	/**
	 * 关闭tagString所标识的Activity或者Fragment中所有还在运行的线程
	 * @param tagString
	 */
	public void cancelLimitTasks(String tagString){
		List<TaskModel> taskModels = new ArrayList<TaskModel>();
		for(int i=0;i<taskList.size();i++){
			try {
				TaskModel taskModel = taskList.get(i);
				if(taskModel.tagString.equals(tagString)&&taskModel.task!=null){
					long time = System.currentTimeMillis()-taskModel.getCreatTime();
					if (time>500){
						taskModel.task.cancel(true);
						taskModels.add(taskModel);
//						LogUtil.e("******cancelLimitTasks*******"+ System.currentTimeMillis());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i=0;i<taskModels.size();i++){
			try {
				taskList.remove(taskModels.get(i));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭一个还在运行的线程
	 */
	public void cancelOneTasks(AsycnTask task){
		for(int i=0;i<taskList.size();i++){
			try {
				TaskModel taskModel = taskList.get(i);
				if(taskModel.task!=null&&taskModel.task==task){
					taskModel.task.cancel(true);
					taskList.remove(taskModel);
//					LogUtil.e("******cancelOneTasks*******"+ System.currentTimeMillis());
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 移除task
	 * @param task
	 */
	public void removeTask(AsycnTask task){
		for(int i=0;i<taskList.size();i++){
			try {
				TaskModel taskModel = taskList.get(i);
				if(taskModel.task!=null&&taskModel.task==task){
					taskList.remove(taskModel);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public class TaskModel{
		long creatTime;
		String tagString;
		AsycnTask task;

		public long getCreatTime() {
			return creatTime;
		}

		public void setCreatTime(long creatTime) {
			this.creatTime = creatTime;
		}

		public String getTagString() {
			return tagString;
		}

		public void setTagString(String tagString) {
			this.tagString = tagString;
		}

		public AsycnTask getTask() {
			return task;
		}

		public void setTask(AsycnTask task) {
			this.task = task;
		}
	}


}