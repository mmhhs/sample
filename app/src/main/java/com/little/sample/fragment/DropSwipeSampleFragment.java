package com.little.sample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.little.drop.listener.IOnRefreshListener;
import com.little.drop.ultimate.UltimateRecyclerView;
import com.little.sample.R;
import com.little.sample.adapter.DropSampleAdapter;
import com.little.sample.model.VisitSampleDataEntity;
import com.little.sample.model.VisitSampleResult;
import com.little.visit.TaskConstant;
import com.little.visit.listener.IOnResultListener;
import com.little.visit.listener.IOnRetryListener;
import com.little.visit.task.PageVisitTask;
import com.little.visit.task.VisitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DropSwipeSampleFragment extends Fragment {
    public View rootView;
    @InjectView(R.id.fragment_drop_swipe_recyclerview)
    UltimateRecyclerView fragmentDropSwipeRecyclerview;
    @InjectView(R.id.visit_link_container)
    LinearLayout visitLinkContainer;
    @InjectView(R.id.visit_link_loading_layout)
    LinearLayout visitLinkLoadingLayout;
    private LinearLayoutManager linearLayoutManager;
    private DropSampleAdapter adapter;
    private List<VisitSampleDataEntity> resultList;
    private List<VisitSampleDataEntity> list = new ArrayList<VisitSampleDataEntity>();
    int pageNumber = 1;
    int PAGER_COUNT = 15;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_drop_swipe_sample, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rootView.setLayoutParams(params);
            ButterKnife.inject(this, rootView);
            init();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ButterKnife.inject(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void init() {
        initUltimate();
        refresh(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void initUltimate() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentDropSwipeRecyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DropSampleAdapter(getActivity(), list);
        fragmentDropSwipeRecyclerview.setAdapter(adapter);
        fragmentDropSwipeRecyclerview.enableLoadmore(true);
        fragmentDropSwipeRecyclerview.enableSwipeRefresh(true);
        fragmentDropSwipeRecyclerview.setEnd(true);
//        fragmentDropSwipeRecyclerview.addItemDividerDecoration(getActivity());
        fragmentDropSwipeRecyclerview.setOnRefreshListener(new IOnRefreshListener() {
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
        String url = "https://bfda-app.ifoton.com.cn/serviceProvider";
        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("page", ""+pageNumber);
        argMap.put("pageSize", ""+PAGER_COUNT);
        PageVisitTask pageVisitTask = new PageVisitTask(getActivity(),"",visitLinkContainer,visitLinkLoadingLayout,"",showLoad,onRetryListener,url,argMap,TaskConstant.POST);
        pageVisitTask.setParseClass(VisitSampleResult.class);
        pageVisitTask.setiOnResultListener(new IOnResultListener() {
            @Override
            public void onSuccess(VisitTask task) {
                if (task.getResultEntity() instanceof VisitSampleResult) {
                    VisitSampleResult res = (VisitSampleResult) task.getResultEntity();
                    resultList = res.data;
                }
                updateInfo((PageVisitTask) task);
            }

            @Override
            public void onError(VisitTask task) {

            }

            @Override
            public void onDone(VisitTask task) {
                try {
                    fragmentDropSwipeRecyclerview.refreshFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pageVisitTask.execute();
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

    void updateInfo(PageVisitTask task) {
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
                    fragmentDropSwipeRecyclerview.setEnd(true);
                } else {
                    fragmentDropSwipeRecyclerview.setEnd(false);
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
