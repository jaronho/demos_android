package com.liyuu.strategy.ui.transaction.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.BaseRefreshAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.util.StringUtils;

public class RecommendStocksAdapter extends BaseRefreshAdapter<StockBean> {

    private LayoutInflater inflater;
    private Activity context;

    public RecommendStocksAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_recommend_stocks, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, StockBean item, int position) {
        holder.getTextView(R.id.tv_stock_name).setText(item.getProdName());
        holder.getTextView(R.id.tv_stock_symbol).setText(item.getSymbol());
        TextView tvStockPrice = holder.getTextView(R.id.tv_stock_price);
        TextView tvFloatPercent = holder.getTextView(R.id.tv_float_percent);
        tvStockPrice.setText(item.getLastPx());

        double px_change_rate = StringUtils.parseDouble(item.getPxChangeRate());
        if (px_change_rate > 0.f) {
            tvFloatPercent.setText(String.format("+%s%s", px_change_rate, "%"));
            tvStockPrice.setTextColor(context.getResources().getColor(R.color.stock_red_e5462d));
            tvFloatPercent.setTextColor(context.getResources().getColor(R.color.stock_red_e5462d));
        } else if (px_change_rate < 0.f) {
            //负数自带符号
            tvStockPrice.setTextColor(context.getResources().getColor(R.color.stock_green_73a848));
            tvFloatPercent.setTextColor(context.getResources().getColor(R.color.stock_green_73a848));
        } else if (Double.isNaN(px_change_rate)) {
            tvFloatPercent.setText("--");
        } else {
            tvStockPrice.setTextColor(context.getResources().getColor(R.color.stock_grey_color));
            tvFloatPercent.setTextColor(context.getResources().getColor(R.color.stock_grey_color));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        StockBean bean = getItem(position);
        Intent intent = new Intent();
        intent.putExtra("stockInfo", bean);
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
    }
}
