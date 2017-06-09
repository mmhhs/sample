package com.little.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.little.picture.view.PageIndicatorView;
import com.little.sample.R;
import com.little.sample.adapter.FragmentVPAdapter;
import com.little.sample.fragment.DropArrowSampleFragment;
import com.little.sample.fragment.DropCustomSampleFragment;
import com.little.sample.fragment.DropSwipeSampleFragment;
import com.little.title.TitlePagerLayout;
import com.little.title.listener.IOnChangeListener;
import com.little.title.model.TitleItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleAliquotLineSampleActivity extends FragmentActivity {

    @BindView(R.id.activity_title_center_sample_titlePagerLayout)
    TitlePagerLayout activityTitleCenterSampleTitlePagerLayout;
    @BindView(R.id.activity_title_center_sample_viewPager)
    ViewPager activityTitleCenterSampleViewPager;
    @BindView(R.id.activity_title_center_sample_pageIndicatorView)
    PageIndicatorView pageIndicatorView;

    private FragmentVPAdapter mFragmentPagerAdapter;
    private int screenFlag = 0;//界面标识
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private ArrayList<TitleItem> titleItemList = new ArrayList<TitleItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_center_sample);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onDestroy() {
        try {
            mFragmentPagerAdapter.removeFragments(fragmentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void init() {
        screenFlag = getIntent().getIntExtra("index", 0);
        mFragmentPagerAdapter = new FragmentVPAdapter(getSupportFragmentManager(), fragmentList);
        activityTitleCenterSampleViewPager.setAdapter(mFragmentPagerAdapter);
        initFragment();
        setTitles();
        activityTitleCenterSampleTitlePagerLayout.initData(TitlePagerLayout.TITLE_MODE.WEIGHT_LINE, titleItemList, activityTitleCenterSampleViewPager, screenFlag);
        activityTitleCenterSampleTitlePagerLayout.setOnChangeListener(new IOnChangeListener() {
            @Override
            public void onChange(int position) {
                screenFlag = position;
            }
        });
        pageIndicatorView.setPageTotal(fragmentList.size());
        pageIndicatorView.setPageSelect(0);
        activityTitleCenterSampleViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setPageSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTitles() {
        titleItemList.clear();
        TitleItem titleItem1 = new TitleItem("第一种", R.style.sample_style_text, R.drawable.sample_title_bg);
        TitleItem titleItem2 = new TitleItem("第二种", R.style.sample_style_text, R.drawable.sample_title_bg);
        TitleItem titleItem3 = new TitleItem("第三种", R.style.sample_style_text, R.drawable.sample_title_bg);
        titleItemList.add(titleItem1);
        titleItemList.add(titleItem2);
        titleItemList.add(titleItem3);
    }

    public void initFragment() {
        mFragmentPagerAdapter.removeFragments(fragmentList);
        fragmentList.clear();
        DropSwipeSampleFragment fragment1 = new DropSwipeSampleFragment();
        fragmentList.add(fragment1);
        DropCustomSampleFragment fragment2 = new DropCustomSampleFragment();
        fragmentList.add(fragment2);
        DropArrowSampleFragment fragment3 = new DropArrowSampleFragment();
        fragmentList.add(fragment3);
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

}
