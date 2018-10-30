package com.gsclub.strategy.ui.stock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.ui.helper.ItemTouchHelperAdapter;
import com.gsclub.strategy.ui.stock.other.bean.StockTradeBean;
import com.gsclub.strategy.util.CalculationUtil;
import com.gsclub.strategy.util.ScreenUtil;

public class StockTradeRecyclerAdapter extends BaseRecyclerViewAdapter<StockTradeBean> implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private Context context;
    private int priceColor = -1;
    private int type = 1;//界面type 1:股票详情 2.详情弹窗

    public StockTradeRecyclerAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPriceColor(int priceColor) {
        this.priceColor = priceColor;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        View view = inflater.inflate(R.layout.item_stock_trade, group, false);
        view.getLayoutParams().height = ScreenUtil.dp2px(context, 17);
        return view;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final StockTradeBean item, int position) {
        holder.getTextView(R.id.tv_trade_level).setText(String.valueOf(item.getTradeLevel()));
        holder.getTextView(R.id.tv_trade_price).setText(CalculationUtil.roundRuturnString(item.getPrice(), 2));
        holder.getTextView(R.id.tv_trade_price).setTextColor(priceColor);
        holder.getTextView(R.id.tv_trade_number).setText(String.valueOf((int) (item.getNumber() / 100)));

        holder.getTextView(R.id.tv_trade_number).setPadding(0, 0, ScreenUtil.dip2px(context, type == 1 ? 5 : 1), 0);
        holder.getTextView(R.id.tv_trade_price).setPadding(ScreenUtil.dip2px(context, type == 1 ? 13 : 3), 0, 0, 0);
    }
}
