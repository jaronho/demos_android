package com.gsclub.strategy.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.model.bean.ActivityImagesBean;
import com.gsclub.strategy.model.bean.UserBankBean;

public class NewsWelfareAdapter extends BaseRecyclerViewAdapter<ActivityImagesBean.ListBean> {
    private LayoutInflater inflater;
    private Context context;

    public NewsWelfareAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_news_welfare, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, ActivityImagesBean.ListBean item, int position) {
        ImageLoader.load(context, item.getImage(), holder.getImageView(R.id.iv_activity_img));
        holder.getTextView(R.id.tv_title).setText(item.getTitle());
        holder.getTextView(R.id.tv_desc).setText(item.getDesc());
    }
}
