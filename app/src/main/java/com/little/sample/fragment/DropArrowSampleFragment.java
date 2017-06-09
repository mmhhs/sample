package com.little.sample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.little.drop.listener.IOnRefreshListener;
import com.little.drop.ultimate.AnimationRecyclerView;
import com.little.sample.R;
import com.little.sample.adapter.DropSampleAdapter;
import com.little.sample.base.BaseApplication;
import com.little.sample.model.VisitSampleDataEntity;
import com.little.sample.model.VisitSampleResult;
import com.little.visit.listener.IOnRetryListener;
import com.little.visit.listener.IOnVisitResultListener;
import com.little.visit.okhttp.OkHttpUtil;
import com.little.visit.task.OKHttpTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DropArrowSampleFragment extends Fragment {
    public View rootView;
    private Unbinder unbinder;
    @BindView(R.id.fragment_drop_custom_recyclerview)
    AnimationRecyclerView animationRecyclerView;
    @BindView(R.id.visit_link_container)
    LinearLayout visitLinkContainer;
    @BindView(R.id.visit_link_loading_layout)
    LinearLayout visitLinkLoadingLayout;
    private LinearLayoutManager linearLayoutManager;
    private DropSampleAdapter adapter;
    private List<VisitSampleDataEntity> resultList;
    private List<VisitSampleDataEntity> list = new ArrayList<VisitSampleDataEntity>();
    int pageNumber = 1;
    int PAGER_COUNT = 15;
    String tagStr = "VisitSampleActivity";//task唯一标识

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_drop_custom_sample, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rootView.setLayoutParams(params);
            unbinder = ButterKnife.bind(this, rootView);
            init();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            unbinder = ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void init() {
        initUltimate();
        VisitSampleDataEntity dataEntity = new VisitSampleDataEntity();
        for (int i=0;i<10;i++){
            list.add(dataEntity);
        }
        adapter.notifyDataSetChanged();
//        refresh(true);
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

    private void initUltimate() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        animationRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DropSampleAdapter(getActivity(), list);
        animationRecyclerView.setAdapter(adapter);
        animationRecyclerView.enableLoadmore(true);
        animationRecyclerView.enableRefresh(true);
        animationRecyclerView.setEnd(false);
        animationRecyclerView.setRefreshMode(false);
        animationRecyclerView.setShowResultTip(true);
//        fragmentDropSwipeRecyclerview.addItemDividerDecoration(getActivity());
        animationRecyclerView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }

            @Override
            public void onLoadMore() {
                getList(false);
            }
        });
    }

    void refresh(boolean showLoad) {
        pageNumber = 1;
        getList(showLoad);
    }

    void getList(final boolean showLoad) {
        String url = "https://bfda-app.ifoton.com.cn/serviceProvider/getBrandInfo.action";
        Map<String, String> argMap = new HashMap<String, String>();
        argMap.put("page", ""+pageNumber);
        argMap.put("pageSize", ""+PAGER_COUNT);
        argMap.put("brandType", "3");
        final OKHttpTask okHttpTask = new OKHttpTask(BaseApplication.self(), tagStr, visitLinkContainer, visitLinkLoadingLayout,showLoad, url, argMap, OkHttpUtil.POST,VisitSampleResult.class,onRetryListener);
        okHttpTask.setOnVisitResultListener(new IOnVisitResultListener<VisitSampleResult>() {
            @Override
            public void onSuccess(VisitSampleResult res) {
                resultList = res.data;
                updateInfo(okHttpTask);
            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onFinish() {
                try {
                    animationRecyclerView.refreshFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytes, long contentLength) {

            }
        });
        okHttpTask.execute();
    }

    IOnRetryListener onRetryListener = new IOnRetryListener() {
        @Override
        public void onRetry() {
            refresh(true);
        }

        @Override
        public void onOption() {

        }
    };

    void updateInfo(OKHttpTask task) {
        try {
            if (resultList != null) {
                if (pageNumber == 1) {
                    list.clear();
                }
                pageNumber++;
                for (VisitSampleDataEntity visitSampleDataEntity : resultList) {
                    list.add(visitSampleDataEntity);
                }
                if (resultList.size() < PAGER_COUNT) {
                    animationRecyclerView.setEnd(true);
                } else {
                    animationRecyclerView.setEnd(false);
                }
                if (list.size() == 0 && task != null) {
                    task.addEmptyView("", "", R.mipmap.visit_retry);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
