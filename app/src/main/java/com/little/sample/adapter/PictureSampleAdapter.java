package com.little.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.little.picture.glide.GlideUtil;
import com.little.picture.listener.IOnItemClickListener;
import com.little.picture.util.ImageUtil;
import com.little.sample.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PictureSampleAdapter extends BaseAdapter{
    public Context context;
    private List<String> list;
    private int itemWidth = 0;
    private IOnItemClickListener onItemClickListener;
    private int maxSize;

    public PictureSampleAdapter(Context context, List<String> list, int screenWidth, int maxSize) {
        this.context = context;
        this.list = list;
        this.itemWidth = (screenWidth- 2* ImageUtil.dip2px(context, 1))/3;
        this.maxSize = maxSize;
    }

    @Override
    public int getCount() {
        if (list.size()<maxSize){
            return list.size()+1;
        }else {
            return list.size();
    }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView,final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_picture_sample, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.containerLayout.getLayoutParams().width = itemWidth;
        viewHolder.containerLayout.getLayoutParams().height = itemWidth;
        if (position==(list.size())){
            viewHolder.addText.setVisibility(View.VISIBLE);
            viewHolder.contentImage.setVisibility(View.GONE);
        }else {
            viewHolder.addText.setVisibility(View.GONE);
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            GlideUtil.getInstance().display(context, ImageUtil.completeImagePath(list.get(position)), viewHolder.contentImage);
        }
        final int p = position;
        viewHolder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener !=null){
                    onItemClickListener.onItemClick(p);
                }
            }
        });
        return convertView;
    }

    public final static class ViewHolder {
        @InjectView(R.id.adapter_picture_sample_imageview)
        public ImageView contentImage;
        @InjectView(R.id.adapter_picture_sample_add)
        public TextView addText;
        @InjectView(R.id.adapter_picture_sample_layout)
        public RelativeLayout containerLayout;

        public ViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }

    public IOnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}