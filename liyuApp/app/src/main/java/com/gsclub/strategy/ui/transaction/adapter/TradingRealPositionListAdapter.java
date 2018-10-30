package com.gsclub.strategy.ui.transaction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.RealTradingPositionBean;
import com.gsclub.strategy.ui.transaction.activity.PositionDetailActivity;
import com.gsclub.strategy.util.StringUtils;

public class TradingRealPositionListAdapter extends BaseRecyclerViewAdapter<RealTradingPositionBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;
    private OnSellOrder onSellOrder;

    public TradingRealPositionListAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    public void setOnSellOrder(OnSellOrder onSellOrder) {
        this.onSellOrder = onSellOrder;
    }

    public interface OnSellOrder {
        void sellOrder(RealTradingPositionBean.ListBean item);
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_order_real_position, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, @SuppressLint("RecyclerView") final RealTradingPositionBean.ListBean item, int position) {
        int textColor = Color.parseColor(item.getTextColor());
        holder.getTextView(R.id.tv_stock_name).setText(item.getSname());
        holder.getTextView(R.id.tv_symbol).setText(String.format("(%s)", item.getSymbol()));

        holder.getTextView(R.id.tv_time).setText(String.format("买入：  %s", item.getOptTime()));

        holder.getTextView(R.id.tv_deposit_money).setText(String.format("保证金：  %s", item.getDeposit()));
        holder.getTextView(R.id.tv_stock_buy_price).setText(String.format("买入价：  %s", item.getRealBuyPrice()));
//        holder.getTextView(R.id.tv_stock_now_price).setText(String.format("现价：  %s", item.getCurrentPrice()));
//        holder.getTextView(R.id.tv_stock_float_precent).setText(String.format("涨跌幅：  %s", item.getProfitLossPer()));
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_now_price),
                String.format("现价：  %s", item.getCurrentPrice()),
                String.valueOf(item.getCurrentPrice()),
                textColor);

        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_float_precent),
                String.format("涨跌幅：  %s", item.getProfitLossPer()),
                String.valueOf(item.getProfitLossPer()),
                textColor);

        holder.getTextView(R.id.tv_stop_loss_money).setText(String.format("止损价：  %s", item.getLossPrice()));
        holder.getTextView(R.id.tv_stock_number).setText(String.format("股数：  %s", item.getRealTradeNum()));
//        holder.getTextView(R.id.tv_stock_market_money).setText(String.format("市值：  %s", item.getMarketPrice()));
//        holder.getTextView(R.id.tv_stock_price_float).setText(String.format("浮动盈亏：  %s", item.getProfitLoss()));
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_market_money),
                String.format("市值：  %s", item.getMarketPrice()),
                String.valueOf(item.getMarketPrice()),
                textColor);
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_price_float),
                String.format("浮动盈亏：  %s", item.getProfitLoss()),
                String.valueOf(item.getProfitLoss()),
                textColor);

        TextView tvStatus = holder.getTextView(R.id.tv_status);
        tvStatus.setEnabled("1".equals(item.getSaleStatus()));//2可卖出 1新购
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setTextColor(context.getResources().getColor(R.color.text_white_ffffff));
        tvStatus.setText("1".equals(item.getSaleStatus()) ? "卖出" : "新购");
        tvStatus.setBackgroundResource(R.drawable.selector_btn_sale);

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSellOrder != null)
                    onSellOrder.sellOrder(item);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        PositionDetailActivity.start(context, getItem(position).getOid());
    }

}
