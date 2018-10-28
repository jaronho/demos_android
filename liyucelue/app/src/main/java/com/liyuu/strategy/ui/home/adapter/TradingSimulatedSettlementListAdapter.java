package com.liyuu.strategy.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.SimulatedTradingSettlementBean;
import com.liyuu.strategy.ui.transaction.activity.PositionDetailActivity;
import com.liyuu.strategy.util.StringUtils;

public class TradingSimulatedSettlementListAdapter extends BaseRecyclerViewAdapter<SimulatedTradingSettlementBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;

    public TradingSimulatedSettlementListAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_order_simulated_settlement, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final SimulatedTradingSettlementBean.ListBean item, int position) {
        int textColor = Color.parseColor(item.getTextColor());
        holder.getTextView(R.id.tv_stock_name).setText(item.getSname());
        holder.getTextView(R.id.tv_symbol).setText(String.format("(%s)", item.getSymbol()));

        holder.getTextView(R.id.tv_time).setText(String.format("结算：  %s", item.getOptTime()));

        holder.getTextView(R.id.tv_deposit_money).setText(String.format("保证金：  %s", item.getTotalDeposit()));
        holder.getTextView(R.id.tv_stock_number).setText(String.format("股数：  %s", item.getRealTradeNum()));
        holder.getTextView(R.id.tv_stock_buy_price).setText(String.format("买入价：  %s", item.getRealBuyPrice()));
//        holder.getTextView(R.id.tv_stock_trade_float).setText(String.format("交易盈亏：  %s", item.getProfitLoss()));
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_trade_float),
                String.format("交易盈亏：  %s", item.getProfitLoss()),
                String.valueOf(item.getProfitLoss()),
                textColor);

        holder.getTextView(R.id.tv_stop_loss_money).setText(String.format("止损价：  %s", item.getLossPrice()));
        holder.getTextView(R.id.tv_keep_days).setText(String.format("持有天数：  %s", item.getHoldDay()));
//        holder.getTextView(R.id.tv_stock_sale_price).setText(String.format("卖出价：  %s", item.getRealSalePrice()));
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_stock_sale_price),
                String.format("卖出价：  %s", item.getRealSalePrice()),
                String.valueOf(item.getRealSalePrice()),
                textColor);

        holder.getTextView(R.id.tv_stock_loss_price).setText(String.format("亏损抵扣：  %s", item.getLossMoney()));

        TextView tvStatus = holder.getTextView(R.id.tv_status);
        int color = context.getResources().getColor("1".equals(item.getStatus()) ? R.color.stock_red_color : R.color.stock_green_color);
        int bg = "1".equals(item.getStatus()) ? R.drawable.shape_personal_record_profit : R.drawable.shape_personal_record_loss;
        String text = "1".equals(item.getStatus()) ? "盈利" : "亏损";
        tvStatus.setBackgroundResource(bg);
        tvStatus.setText(text);
        tvStatus.setTextColor(color);
    }
}
