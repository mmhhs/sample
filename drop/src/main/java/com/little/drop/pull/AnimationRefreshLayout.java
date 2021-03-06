package com.little.drop.pull;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.little.drop.R;
import com.little.drop.listener.IOnRefreshListener;
import com.little.drop.listener.Pullable;
import com.little.drop.util.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class AnimationRefreshLayout extends RelativeLayout
{
	public static final String TAG = "AnimationRefreshLayout";
	// 初始状态
	public static final int INIT = 0;
	// 释放刷新
	public static final int RELEASE_TO_REFRESH = 1;
	// 正在刷新
	public static final int REFRESHING = 2;
	// 释放加载
	public static final int RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int LOADING = 4;
	// 操作完毕
	public static final int DONE = 5;
	// 当前状态
	private int state = INIT;

	// 刷新回调接口
	private IOnRefreshListener mListener;
	// 刷新成功
	public static final int SUCCEED = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY,lastX;

	// 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;

	// 释放刷新的距离
	private float refreshDist = 200;
	// 释放加载的距离
	private float loadMoreDist = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isFirstLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 1;

	// 下拉箭头的转180°动画
	private RotateAnimation rotateAnimation;
	// 均匀旋转动画
	private RotateAnimation refreshingAnimation;

	// 下拉头
	private View refreshHeaderView;
	// 下拉的箭头
	private ImageView pullView;
	// 下拉的动画视图
	private ImageView refreshImageView;
	// 正在刷新的图标
	private View refreshingView;
	// 刷新结果图标
	private View refreshStateImageView;
	// 刷新结果：成功或失败
	private TextView refreshStateTextView;

	// 上拉头
	private View loadMoreFooterView;
	// 上拉的箭头
	private View pullUpView;
	// 正在加载的图标
	private View loadingView;
	// 加载结果图标
	private View loadStateImageView;
	// 加载结果：成功或失败
	private TextView loadStateTextView;

	// 实现了Pullable接口的View
	private View pullAbleView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	//第二种头部刷新显示方式
	private boolean isSecond = true;
	//刷新动画
	private AnimationDrawable animationDrawableHeader;
	//是否可以下拉刷新
	private boolean canRefresh = true;
	//是否可以上拉加载
	private boolean canLoad = true;
	//是否显示结果
	private boolean showResultTip = false;
	//上下文
	private Context context;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch)
			{
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist)
				{
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadMoreDist)
				{
					pullUpY = -loadMoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0)
			{
				// 已完成回弹
				pullDownY = 0;
				clearPullAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			if (pullUpY > 0)
			{
				// 已完成回弹
				pullUpY = 0;
				clearPullAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
		}

	};

	public void setOnRefreshListener(IOnRefreshListener listener)
	{
		mListener = listener;
	}

	public AnimationRefreshLayout(Context context)
	{
		super(context);
		initView(context);
	}

	public AnimationRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public AnimationRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context)
	{
		this.context = context;
		timer = new MyTimer(updateHandler);
		refreshDist = DensityUtil.dip2px(context, 70);
		loadMoreDist = DensityUtil.dip2px(context,70);

		//翻转和刷新动画
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.drop_anim_reverse);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.drop_anim_rotate);
		//添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	private void hide()
	{
		timer.schedule(5);
	}

	/**
	 * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
	 */
	/**
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void refreshFinish(int refreshResult)
	{
		finishPullAnimation();
		if (showResultTip){
			switch (refreshResult)
			{
				case SUCCEED:
					// 刷新成功
					refreshStateImageView.setVisibility(View.VISIBLE);
					refreshStateTextView.setText(R.string.drop_refresh_success);
					refreshStateImageView.setBackgroundResource(R.mipmap.drop_pull_load_success);
					break;
				case FAIL:
				default:
					// 刷新失败
					refreshStateImageView.setVisibility(View.VISIBLE);
					refreshStateTextView.setText(R.string.drop_refresh_fail);
					refreshStateImageView.setBackgroundResource(R.mipmap.drop_pull_load_fail);
					break;
			}
			// 刷新结果停留1秒
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		}else {
			changeState(DONE);
			hide();
		}
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 *
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void loadMoreFinish(int refreshResult)
	{
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		if (showResultTip){
			switch (refreshResult)
			{
				case SUCCEED:
					// 加载成功
					loadStateImageView.setVisibility(View.VISIBLE);
					loadStateTextView.setText(R.string.drop_load_success);
					loadStateImageView.setBackgroundResource(R.mipmap.drop_pull_load_success);
					break;
				case FAIL:
				default:
					// 加载失败
					loadStateImageView.setVisibility(View.VISIBLE);
					loadStateTextView.setText(R.string.drop_load_fail);
					loadStateImageView.setBackgroundResource(R.mipmap.drop_pull_load_fail);
					break;
			}
			// 刷新结果停留1秒
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		}else {
			changeState(DONE);
			hide();
		}

	}

	private void changeState(int to)
	{
		state = to;
		switch (state)
		{
			case INIT:
				// 下拉布局初始状态
				refreshStateImageView.setVisibility(View.GONE);
				refreshStateTextView.setText(R.string.drop_pull_to_refresh);
				clearPullAnimation();
				if (isSecond){
					refreshImageView.setVisibility(View.VISIBLE);
				}else {
					pullView.setVisibility(View.VISIBLE);
				}
				// 上拉布局初始状态
				loadStateImageView.setVisibility(View.GONE);
				loadStateTextView.setText(R.string.drop_pull_up_to_load);
				pullUpView.clearAnimation();
				pullUpView.setVisibility(View.VISIBLE);
				break;
			case RELEASE_TO_REFRESH:
				// 释放刷新状态
				refreshStateTextView.setText(R.string.drop_release_to_refresh);
				releasePullAnimation();
				break;
			case REFRESHING:
				// 正在刷新状态
				refreshPullAnimation();
				break;
			case RELEASE_TO_LOAD:
				// 释放加载状态
				loadStateTextView.setText(R.string.drop_release_to_load);
				pullUpView.startAnimation(rotateAnimation);
				break;
			case LOADING:
				// 正在加载状态
				pullUpView.clearAnimation();
				loadingView.setVisibility(View.VISIBLE);
				pullUpView.setVisibility(View.INVISIBLE);
				loadingView.startAnimation(refreshingAnimation);
				loadStateTextView.setText(R.string.drop_loading);
				break;
			case DONE:
				// 刷新或加载完毕，啥都不做
				break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull()
	{
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
				downY = ev.getY();
				lastY = downY;
				timer.cancel();
				mEvents = 0;
				releasePull();
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_POINTER_UP:
				// 过滤多点触碰
				mEvents = -1;
				break;
			case MotionEvent.ACTION_MOVE:
				float distanceX = Math.abs(ev.getX() - lastX);
				float distanceY = Math.abs(ev.getY() - lastY);
				if (distanceY>distanceX){
					if (mEvents == 0) {
						if (((Pullable) pullAbleView).canPullDown() && canPullDown
								&& state != LOADING && canRefresh) {
							// 可以下拉，正在加载时不能下拉
							// 对实际滑动距离做缩小，造成用力拉的感觉
							pullDownY = pullDownY + (ev.getY() - lastY) / radio;
							if (pullDownY < 0)
							{
								pullDownY = 0;
								canPullDown = false;
								canPullUp = true;
							}
							if (pullDownY > getMeasuredHeight())
								pullDownY = getMeasuredHeight();
							if (state == REFRESHING)
							{
								// 正在刷新的时候触摸移动
								isTouch = true;
								if (isSecond){
									startHeaderAnimation();
								}
							}
						} else if (((Pullable) pullAbleView).canPullUp() && canPullUp
								&& state != REFRESHING && canLoad) {
							// 可以上拉，正在刷新时不能上拉
							pullUpY = pullUpY + (ev.getY() - lastY) / radio;
							if (pullUpY > 0)
							{
								pullUpY = 0;
								canPullDown = true;
								canPullUp = false;
							}
							if (pullUpY < -getMeasuredHeight())
								pullUpY = -getMeasuredHeight();
							if (state == LOADING)
							{
								// 正在加载的时候触摸移动
								isTouch = true;
							}
						} else
							releasePull();
					} else{
						mEvents = 0;
					}
				}


				lastY = ev.getY();
				lastX = ev.getX();
				int distance = (int)((refreshDist-80)/3);
				// 根据下拉距离改变比例
				radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
						* (pullDownY + Math.abs(pullUpY))));
				requestLayout();
				if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH)
				{
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(INIT);
				}
				if(isSecond){
					if (state != REFRESHING){
						if (pullDownY<=80+distance){
							refreshImageView.setImageResource(R.mipmap.drop_refresh_0);
						}else if (pullDownY<=80+2*distance&&pullDownY>80+distance){
							refreshImageView.setImageResource(R.mipmap.drop_refresh_1);
						}else if (pullDownY<=80+3*distance&&pullDownY>80+2*distance){
							refreshImageView.setImageResource(R.mipmap.drop_refresh_2);
						}else {
							refreshImageView.setImageResource(R.mipmap.drop_refresh_3);
						}
					}
				}

				if (pullDownY >= refreshDist && state == INIT)
				{
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(RELEASE_TO_REFRESH);
				}
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadMoreDist && state == RELEASE_TO_LOAD)
				{
					changeState(INIT);
				}
				if (-pullUpY >= loadMoreDist && state == INIT)
				{
					changeState(RELEASE_TO_LOAD);
				}
				// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
				// Math.abs(pullUpY))就可以不对当前状态作区分了
				if ((pullDownY + Math.abs(pullUpY)) > 8)
				{
					// 防止下拉过程中误触发长按事件和点击事件
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (pullDownY > refreshDist || -pullUpY > loadMoreDist)
					// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
					isTouch = false;
				if (state == RELEASE_TO_REFRESH)
				{
					changeState(REFRESHING);
					// 刷新操作
					if (mListener != null)
						mListener.onRefresh();
				} else if (state == RELEASE_TO_LOAD)
				{
					changeState(LOADING);
					// 加载操作
					if (mListener != null)
						mListener.onLoadMore();
				}
				hide();
			default:
				break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	private void initView()
	{
		// 初始化下拉布局
		pullView = (ImageView) refreshHeaderView.findViewById(R.id.drop_pull_refresh_head_arrow);
		refreshImageView = (ImageView) refreshHeaderView.findViewById(R.id.drop_pull_refresh_head_anim);
//		setPullViewImageResource();
		refreshStateTextView = (TextView) refreshHeaderView.findViewById(R.id.drop_pull_refresh_head_state_text);
		refreshingView = refreshHeaderView.findViewById(R.id.drop_pull_refresh_head_loading);
		refreshStateImageView = refreshHeaderView.findViewById(R.id.drop_pull_refresh_head_state_image);
		// 初始化上拉布局
		pullUpView = loadMoreFooterView.findViewById(R.id.drop_pull_load_more_arrow);
		loadStateTextView = (TextView) loadMoreFooterView.findViewById(R.id.drop_pull_load_more_state_text);
		loadingView = loadMoreFooterView.findViewById(R.id.drop_pull_load_more_loading);
		loadStateImageView = loadMoreFooterView.findViewById(R.id.drop_pull_load_more_state_image);
		if (isSecond){
			refreshingView.setVisibility(GONE);
			pullView.setVisibility(GONE);
			refreshImageView.setVisibility(VISIBLE);
		}else {
			refreshImageView.setVisibility(GONE);
			pullView.setVisibility(VISIBLE);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if (!isFirstLayout)
		{
			// 这里是第一次进来的时候做一些初始化
			refreshHeaderView = getChildAt(0);
			pullAbleView = getChildAt(1);
			loadMoreFooterView = getChildAt(2);
			isFirstLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshHeaderView).getChildAt(0)
					.getMeasuredHeight();
			loadMoreDist = ((ViewGroup) loadMoreFooterView).getChildAt(0)
					.getMeasuredHeight();
		}
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshHeaderView.layout(0,
				(int) (pullDownY + pullUpY) - refreshHeaderView.getMeasuredHeight(),
				refreshHeaderView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullAbleView.layout(0, (int) (pullDownY + pullUpY),
				pullAbleView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pullAbleView.getMeasuredHeight());
		loadMoreFooterView.layout(0,
				(int) (pullDownY + pullUpY) + pullAbleView.getMeasuredHeight(),
				loadMoreFooterView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullAbleView.getMeasuredHeight()
						+ loadMoreFooterView.getMeasuredHeight());
	}

	class MyTimer
	{
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period)
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask
		{
			private Handler handler;

			public MyTask(Handler handler)
			{
				this.handler = handler;
			}

			@Override
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}

		}
	}


	//change pullView
	public void setPullViewImageResource() {
		if (isSecond) {
			refreshImageView.setImageResource(R.anim.drop_anim_loading);
			animationDrawableHeader = null;
			animationDrawableHeader = (AnimationDrawable) refreshImageView.getDrawable();
		}
	}

	public void clearPullAnimation() {
		if (isSecond) {
			stopHeaderAnimation();
		}else {
			pullView.clearAnimation();
		}
	}

	public void releasePullAnimation() {
		if (isSecond) {

		}else {
			pullView.startAnimation(rotateAnimation);
		}
	}

	public void refreshPullAnimation() {
		if (isSecond) {
			refreshingView.setVisibility(View.GONE);
			refreshImageView.setVisibility(View.VISIBLE);
			refreshStateTextView.setText(R.string.drop_refreshing);
			startHeaderAnimation();
		}else {
			pullView.clearAnimation();
			refreshingView.setVisibility(View.VISIBLE);
			pullView.setVisibility(View.GONE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.drop_refreshing);
		}
	}

	public void finishPullAnimation() {
		if (isSecond) {
			stopHeaderAnimation();
			if (showResultTip){
				refreshImageView.setVisibility(View.GONE);
			}
		}else {
			refreshingView.clearAnimation();
			refreshingView.setVisibility(View.GONE);
		}
	}

	/**
	 * 开始头部动画
	 */
	private void startHeaderAnimation(){
		try {
			setPullViewImageResource();
			if (animationDrawableHeader.isRunning()) {
				animationDrawableHeader.stop();
				animationDrawableHeader.start();
			}else {
				animationDrawableHeader.start();
			}
//			refreshImageView.startAnimation(anim);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * 停止头部动画
	 */
	private void stopHeaderAnimation(){
		try {
			if (animationDrawableHeader!=null&&animationDrawableHeader.isRunning()) {
				animationDrawableHeader.stop();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public boolean isSecond() {
		return isSecond;
	}

	public void setIsSecond(boolean isSecond) {
		this.isSecond = isSecond;
	}

	public boolean isCanRefresh() {
		return canRefresh;
	}

	public void setCanRefresh(boolean canRefresh) {
		this.canRefresh = canRefresh;
	}

	public boolean isCanLoad() {
		return canLoad;
	}

	public void setCanLoad(boolean canLoad) {
		this.canLoad = canLoad;
	}

	public boolean isShowResultTip() {
		return showResultTip;
	}

	public void setShowResultTip(boolean showResultTip) {
		this.showResultTip = showResultTip;
	}
}
