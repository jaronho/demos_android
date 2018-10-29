package com.liyuu.strategy.ui.transaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.bean.RealTradingCommissionBean;
import com.liyuu.strategy.util.StringUtils;

public class TradingRealCommissionListAdapter extends BaseRecyclerViewAdapter<RealTradingCommissionBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;

    public TradingRealCommissionListAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }


    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_order_real_commission, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final RealTradingCommissionBean.ListBean item, int position) {
        holder.getTextView(R.id.tv_time).setText(String.format("委托：  %s", item.getOptTime()));
        holder.getTextView(R.id.tv_stock_name).setText(item.getSname());
        holder.getTextView(R.id.tv_symbol).setText(String.format("(%s)", item.getSymbol()));

        holder.getTextView(R.id.tv_deposit_money).setText(String.format("保证金：  %s", item.getDeposit()));
        holder.getTextView(R.id.tv_commission_number).setText(String.format("成交/委托：  %s/%s", item.getRealTradeNum(), item.getStockNum()));
//        holder.getTextView(R.id.tv_commission_type).setText(String.format("委托类型:  %s", item.getType()));
        int typeColor = context.getResources().getColor(R.color.text_grey_666666);
        if ("买入".equals(item.getType())) {
            typeColor = context.getResources().getColor(R.color.stock_red_f16262);
        } else if ("卖出".equals(item.getType())) {
            typeColor = context.getResources().getColor(R.color.stock_green_73a848);
        }
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_commission_type),
                String.format("委托类型：  %s", item.getType()),
                String.valueOf(item.getType()),
                typeColor);


        holder.getTextView(R.id.tv_stop_loss_money).setText(String.format("止损价：  %s", item.getLossPrice()));
        holder.getTextView(R.id.tv_commission_money).setText(String.format("委托价：  %s", item.getRealBuyPrice()));
//        holder.getTextView(R.id.tv_order_status).setText(String.format("状态:  %s", item.getMark()));
        int statusColor = context.getResources().getColor(R.color.text_grey_666666);
        if ("买入".equals(item.getType())) {
            statusColor = context.getResources().getColor(R.color.text_grey_666666);
        } else if ("卖出".equals(item.getType())) {
            statusColor = context.getResources().getColor(R.color.stock_green_73a848);
        }

        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_order_status),
                String.format("状态：  %s", item.getMark()),
                String.valueOf(item.getMark()),
                statusColor);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
    }

}
