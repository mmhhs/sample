package com.little.sample.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.little.picture.glide.GlideUtil;
import com.little.sample.R;
import com.little.sample.base.BaseConstant;
import com.little.sample.base.BaseFragmentActivity;
import com.little.sample.fragment.WelcomeFragment;
import com.little.sample.listener.IOnClickListener;
import com.little.sample.util.SharedPreferencesUtil;
import com.little.sample.util.StringUtil;
import com.little.sample.util.SystemUtil;
import com.little.sample.view.RoundProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseFragmentActivity {
	@BindView(R.id.activity_welcome_imageView)
	public ImageView imageView ;
	@BindView(R.id.activity_welcome_draweeView)
	public ImageView adView ;
	@BindView(R.id.activity_welcome_progress_bg)
	public ImageView progressBg;
	@BindView(R.id.activity_welcome_progress)
	public RoundProgressBar progressBar;
	@BindView(R.id.activity_welcome_fragment)
	public LinearLayout proLinear;
	private Handler handler;
	private Timer timer;

	private static FragmentManager fMgr;
	private final String tabTag = "WelcomeFragment";
	private boolean showHelp = true;//显示帮助页面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		setTaskTag(getClass().getSimpleName());
		setContentView(R.layout.activity_welcome);
	}

	@Override
	protected void onDestroy(){
		stop();
		super.onDestroy();
	}

	@Override
	public void init(){
		fMgr = getSupportFragmentManager();
		Boolean isFirst = SharedPreferencesUtil.getHelpStatus(getApplicationContext());
		int versionHelp = SharedPreferencesUtil.getHelpCode(getApplicationContext());
		int versionCurrent = 1;
		try {
			versionCurrent = SystemUtil.getVersionCode(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isFirst){
			SharedPreferencesUtil.saveHelpStatus(getApplicationContext(), false, versionCurrent);
			initFragment();
		}else{
			if(versionHelp<versionCurrent&&showHelp){
				SharedPreferencesUtil.saveHelpStatus(getApplicationContext(), false, versionCurrent);
				initFragment();
			}else{
				if (!StringUtil.isEmpty(SharedPreferencesUtil.getAdImage(this))){
					progressBar.startCountdown();
					progressBar.setVisibility(View.VISIBLE);
					progressBg.setVisibility(View.VISIBLE);
					GlideUtil.getInstance().display(WelcomeActivity.this, SharedPreferencesUtil.getAdImage(this), adView, BaseConstant.SCALE_WIDTH, BaseConstant.SCALE_HEIGHT);
				}else {
					progressBar.setVisibility(View.GONE);
					progressBg.setVisibility(View.GONE);
				}
				initTimerTask();
			}
		}

	}

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		welcomeFragment.setOnClickListener(new IOnClickListener() {
			@Override
			public void onClick() {
				setIntent();
			}
		});
		ft.add(R.id.activity_welcome_fragment, welcomeFragment, tabTag);
		ft.commit();
	}

	/**
	 * 定时跳转
	 */
	private void initTimerTask(){
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}

		};
		timer = new Timer(true);
		timer.schedule(task, 3000);
		handler = new Handler(){
			public void handleMessage(Message msg) {
				//activity
				try {
					if(timer!=null){
						timer.cancel();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				setIntent();
			}
		};
	}

	@OnClick(R.id.activity_welcome_progress)
	public void setIntent(){
		try {
			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, HomeActivity.class);
			startActivity(intent);
			finishSelf();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	boolean isStop = false;

	private void stop(){
		try {
			if (!isStop){
				if(timer!=null){
					timer.cancel();
				}
				progressBar.stopCountdown();
				isStop = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//点击返回按钮
	@Override
	public void onBackPressed() {
		finishSelf();
	}

}
