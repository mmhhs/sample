package com.little.sample.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.little.picture.view.PageIndicatorView;
import com.little.sample.R;
import com.little.sample.adapter.ViewPagerAdapter;
import com.little.sample.base.BaseFragment;
import com.little.sample.listener.IOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class WelcomeFragment extends BaseFragment implements OnPageChangeListener {
    public View rootView;
	private Unbinder unbinder;
    @BindView(R.id.fragment_welcome_viewPager)
    public ViewPager mViewPager;
    @BindView(R.id.fragment_welcome_pageIndicatorView)
    public PageIndicatorView mPageIndicatorView;
	private ViewPagerAdapter adapter;
	private List<Integer> imgList = new ArrayList<Integer>();
	private List<View> viewList = new ArrayList<View>();
	public IOnClickListener onClickListener;
	private boolean clickButton = false;
	
	public WelcomeFragment(){

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		setTaskTag(getClass().getSimpleName());
        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_welcome,null);
			unbinder = ButterKnife.bind(this, rootView);
            init();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

	@Override
	public void onDestroyView() {
		try {
			unbinder.unbind();
		}catch (Exception e){
			e.printStackTrace();
		}
		super.onDestroyView();
	}

    @Override
    public void init(){
		imgList.add(R.color.grey);
		imgList.add(R.color.red);
		imgList.add(R.color.title_color);
		for(int i=0;i<imgList.size();i++){
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_welcome, null);
			ImageView img = (ImageView) view.findViewById(R.id.adapter_welcome_imageView);
			img.setImageResource(imgList.get(i));
			ImageButton imgButton = (ImageButton) view.findViewById(R.id.adapter_welcome_imageButton);
			if(i==(imgList.size()-1)){
				if(clickButton){
					imgButton.setVisibility(View.VISIBLE);
					imgButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							if (onClickListener !=null){
								onClickListener.onClick();
							}
						}
					});
				}else{
					view.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							if (onClickListener !=null){
								onClickListener.onClick();
							}
						}
						
					});
				}
			}else{
				imgButton.setVisibility(View.GONE);
			}
			
			viewList.add(view);
		}
		adapter = new ViewPagerAdapter(viewList);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);
		mPageIndicatorView.setPageTotal(imgList.size());
		mPageIndicatorView.setPageSelect(0);
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible){
			return;
		}
		//TODO 判断是否加载过数据

		//TODO 网络加载

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mPageIndicatorView.setPageSelect(arg0);
	}

	public void setOnClickListener(IOnClickListener iOnClickListener) {
		this.onClickListener = iOnClickListener;
	}
}
