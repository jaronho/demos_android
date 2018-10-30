package com.gsclub.strategy.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.gsclub.strategy.ui.transaction.activity.SettlementDetailActivity;
import com.gsclub.strategy.util.StringUtils;

public class PersonalRecordListAdapter extends BaseRefreshAdapter<RankTradeBean> {

    private LayoutInflater inflater;
    private Context context;

    public PersonalRecordListAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_personal_record_list, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final RankTradeBean item, int position) {
        holder.getTextView(R.id.tv_settle_time).setText(item.getSettleTime());
        holder.getTextView(R.id.tv_stock_name).setText(item.getSname());
        holder.getTextView(R.id.tv_symbol).setText(item.getSymbol());

        boolean status = "1".equals(item.getStatus());
        TextView tvStatus = holder.getTextView(R.id.tv_status);
        int textColor2 = context.getResources().getColor( status? R.color.stock_red_d4453d : R.color.stock_green_8ab56a);
        tvStatus.setText(status ? "盈利" : "亏损");
        tvStatus.setBackgroundResource(
                status ?
                        R.drawable.shape_personal_record_profit :
                        R.drawable.shape_personal_record_loss);
        tvStatus.setTextColor(textColor2);

        holder.getTextView(R.id.tv_total_deposit).setText(String.format("保证金:   %s", item.getTotalDeposit()));
        holder.getTextView(R.id.tv_stock_number).setText(String.format("股数:   %s", item.getRealTradeNum()));
        holder.getTextView(R.id.tv_pay_price).setText(String.format("买入价:   %s", item.getRealBuyPrice()));
        holder.getTextView(R.id.tv_loss_price).setText(String.format("止损价:   %s", item.getLossPrice()));
        holder.getTextView(R.id.tv_period_day).setText(String.format("持有天数:   %s", item.getPeriod()));
        holder.getTextView(R.id.tv_loss_deduction).setText(String.format("亏损抵扣:   %s", item.getLossDeduction()));

        String textColor_s = TextUtils.isEmpty(item.getTextColor())?"#666666":item.getTextColor();
        int textColor = Color.parseColor(textColor_s);
        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_trading_profit_or_loss),
                String.format("交易盈亏：  %s", item.getProfitLoss()),
                item.getProfitLoss(),
                textColor);

        StringUtils.setColorFulText(
                holder.getTextView(R.id.tv_sell_price),
                String.format("卖出价：  %s", item.getRealSalePrice()),
                item.getRealSalePrice(),
                textColor);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        SettlementDetailActivity.start(context, String.valueOf(getItem(position).getOid()));
    }

}
