package com.little.title;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.little.title.listener.IOnChangeListener;
import com.little.title.model.TitleItem;
import com.little.title.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动界面标题控件
 */
public class TitlePagerLayout extends LinearLayout {
    private Context context;//上下文
    private TITLE_MODE mode;//三种模式：居中，等分，等分加分割线
    private ViewPager viewPager;//滑动视图
    private List<TextView> textViewList = new ArrayList<TextView>();//标题视图集合
    private List<TitleItem> titleItemList;//标题集合
    private boolean enableMove = true;//是否可滑动
    private IOnChangeListener onChangeListener;//页面变化监听
    private final PageListener pageListener = new PageListener();
    private LayoutParams defaultLayoutParams,lineLayoutParams;

    public TitlePagerLayout(Context context) {
        super(context);
        this.context = context;
    }

    public TitlePagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TitlePagerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void init(){
        defaultLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultLayoutParams.gravity = Gravity.CENTER;
        if (mode == TITLE_MODE.WEIGHT||mode == TITLE_MODE.WEIGHT_LINE){
            defaultLayoutParams.weight = 1;
        }
        lineLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 初始化数据
     * @param mode
     * @param titleItemList
     * @param pager
     * @param index
     */
    public void initData(TITLE_MODE mode,List<TitleItem> titleItemList, ViewPager pager, int index){
        this.mode = mode;
        this.titleItemList = titleItemList;
        this.viewPager = pager;
        init();
        setViewPager(pager);
        addTextViewList();
        setItemStatus(index);
        setViewPagerPosition(index);
    }

    /**
     * 添加标题控件
     */
    private void addTextViewList() {
        if (titleItemList != null && titleItemList.size() > 0) {
            for (int i = 0; i < titleItemList.size(); i++) {
                addItem(i);
            }
        }
    }

    private void addItem(final int position) {
        TitleItem titleItem = titleItemList.get(position);
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        if (mode == TITLE_MODE.WEIGHT_LINE){
            textView.setLineSpacing(DensityUtil.dip2px(context, 2),1);
        }
        if (mode == TITLE_MODE.CENTER){
            setGravity(Gravity.CENTER);
            if(titleItem.getWidth()>0){
                int w = DensityUtil.dip2px(context, titleItem.getWidth());
//                textView.setWidth(w);
                defaultLayoutParams.width = w;
            }
            if(titleItem.getHeight()>0){
                int h = DensityUtil.dip2px(context, titleItem.getHeight());
//                textView.setHeight(h);
                defaultLayoutParams.height = h;
            }
        }
        textView.setLayoutParams(defaultLayoutParams);
        textView.setText(titleItem.getName());
        textView.setBackgroundResource(titleItem.getBackgroundResourceId());
        textView.setTextAppearance(context, titleItem.getTextStyle());
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (enableMove) {
                    setItemStatus(position);
                    setViewPagerPosition(position);
                }
            }

        });
        textViewList.add(textView);
        addView(textView);
        Log.e("TitlePagerLayout","getWidth= "+textView.getLayoutParams().width);
        if (mode == TITLE_MODE.WEIGHT_LINE){
            if (position!=(titleItemList.size()-1)){
                View lineView = LinearLayout.inflate(context, R.layout.title_include_line,null);
                addView(lineView,lineLayoutParams);
            }
        }

    }

    /**
     * 设置标题名称
     * @param list
     */
    public void setTitleName(List<TitleItem> list){
        for (int i=0;i<textViewList.size();i++){
            textViewList.get(i).setText(list.get(i).getName());
        }
    }

    /**
     * 设置标题状态
     * @param position
     */
    private void setItemStatus(int position) {
        if (textViewList != null && position < textViewList.size()) {
            for (int i = 0; i < textViewList.size(); i++) {
                textViewList.get(i).setSelected(false);
            }
            textViewList.get(position).setSelected(true);
        }
    }

    /**
     * 设置ViewPager显示位置
     * @param position
     */
    private void setViewPagerPosition(int position) {
        if (this.viewPager != null && this.viewPager.getAdapter() != null
                && position < this.viewPager.getAdapter().getCount()) {
            this.viewPager.setCurrentItem(position, true);
        }
    }

    /**
     * 设置ViewPager
     * @param pager
     */
    private void setViewPager(ViewPager pager) {
        this.viewPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(pageListener);
    }


    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            if (enableMove) {
                setItemStatus(position);
                if(onChangeListener!=null){
                    onChangeListener.onChange(position);
                }
            }
        }

    }

    public enum TITLE_MODE{
        CENTER,
        WEIGHT,
        WEIGHT_LINE
    }

    public TITLE_MODE getMode() {
        return mode;
    }

    public void setMode(TITLE_MODE mode) {
        this.mode = mode;
    }

    public IOnChangeListener getOnChangeListener() {
        return onChangeListener;
    }

    public void setOnChangeListener(IOnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }
}
