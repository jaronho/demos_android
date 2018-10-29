package com.liyuu.strategy.ui.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.BaseRefreshAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.model.bean.MessageIndexBean;
import com.liyuu.strategy.ui.mine.activity.MessageSonActivity;

public class MessageAdapter extends BaseRefreshAdapter<MessageIndexBean> {

    private LayoutInflater inflater;
    private Context context;

    public MessageAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_message, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final MessageIndexBean item, int position) {
        holder.getTextView(R.id.tv_message_type).setText(item.getTypeName());
        TextView tvUnread = holder.getTextView(R.id.tv_unread_number);
        ImageLoader.load(App.getInstance(), item.getTypeIcon(), holder.getImageView(R.id.img_message_type));
        if (item.getTypeUnread() > 0) {
            tvUnread.setVisibility(View.VISIBLE);
            tvUnread.setText(String.valueOf(item.getTypeUnread()));
        } else {
            tvUnread.setVisibility(View.GONE);
        }

        holder.getTextView(R.id.tv_message_sketch).setText(item.getNewTitle());
        holder.getTextView(R.id.tv_show_time).setText(item.getcTime());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        MessageIndexBean item = getItem(position);
        item.setTypeUnread(0);
        notifyDataSetChanged();
        MessageSonActivity.start(context, item.getType(), item.getTypeName());
    }

}
