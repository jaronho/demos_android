package com.gsclub.strategy.ui.transaction.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRefreshAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.BillListBean;

public class BillListAdapter extends BaseRefreshAdapter<BillListBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;

    public BillListAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_bills, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final BillListBean.ListBean item, int position) {
        Resources res = context.getResources();
        TextView tv_value = holder.getTextView(R.id.tv_value);
        if ("2".equals(item.getStatus())) {
            int color = res.getColor(R.color.text_gold_bea85c);
            tv_value.setTextColor(color);
        } else {
            int color = res.getColor(R.color.text_black_333333);
            tv_value.setTextColor(color);
        }

        holder.getTextView(R.id.tv_user_operate).setText(item.getMark());
        holder.getTextView(R.id.tv_time).setText(item.getcTime());
        holder.getTextView(R.id.tv_value).setText(item.getMoney());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
//        UserInfoActivity.start(context, getData().get(position).getUid(), cowManTyte);
    }
}
