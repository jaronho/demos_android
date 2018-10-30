package com.gsclub.strategy.ui.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.BaseRefreshAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.MessageSonBean;
import com.gsclub.strategy.ui.mine.WebViewActivity;

public class MessageSonAdapter extends BaseRefreshAdapter<MessageSonBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;

    public MessageSonAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_message_son, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final MessageSonBean.ListBean item, int position) {
//        holder.getTextView(R.id.tv_system_message_show_time).setText(item.get);
        holder.getTextView(R.id.tv_system_message_title).setText(item.getTitle());
        holder.getTextView(R.id.tv_system_message_sketch).setText(item.getContent());
        holder.getTextView(R.id.tv_system_message_show_time).setText(item.getTime());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        WebViewActivity.start(context, getItem(position).getDetailUrl());
    }
}
