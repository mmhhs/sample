package com.little.sample.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.little.drop.ultimate.UltimateViewAdapter;
import com.little.sample.R;
import com.little.sample.model.VisitSampleDataEntity;

import java.util.List;

import butterknife.ButterKnife;


public class DropSampleAdapter extends UltimateViewAdapter {
    public Context context;
    public List<VisitSampleDataEntity> list;



    public DropSampleAdapter(Context context, List<VisitSampleDataEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View v = null;
        RecyclerView.ViewHolder holer = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_sample, null);
        v.setLayoutParams(layoutParams);
        holer = new MyViewHolder(v);
        return holer;

    }


    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (position < getItemCount() && (customHeaderView != null ? position <= list.size() :
                    position < list.size()) && (customHeaderView != null ? position > 0 : true)) {
                VisitSampleDataEntity visitSampleDataEntity = list.get(position);
                MyViewHolder viewHolder = (MyViewHolder) holder;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
