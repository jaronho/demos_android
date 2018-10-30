package com.gsclub.strategy.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.BaseRefreshAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.RankTradeBean;

public class PersonalRecord5DYieldAdapter extends BaseRefreshAdapter<RankTradeBean> {

    private LayoutInflater inflater;
    private Context context;

    public PersonalRecord5DYieldAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_personal_record_5d_yield, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final RankTradeBean item, int position) {
        holder.getTextView(R.id.tv_settle_day).setText(item.getSettleDay());
        holder.getTextView(R.id.tv_total_deposit).setText(item.getTotalDeposit());
        holder.getTextView(R.id.tv_s_name).setText(item.getSname());
        TextView tvProfitLoss = holder.getTextView(R.id.tv_profit_loss);
        String textColor_s = TextUtils.isEmpty(item.getTextColor())?"#666666":item.getTextColor();
        int textColor = Color.parseColor(textColor_s);
        tvProfitLoss.setText(item.getProfitLoss());
        tvProfitLoss.setTextColor(textColor);
        holder.getView(R.id.view_line).setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
    }

}
