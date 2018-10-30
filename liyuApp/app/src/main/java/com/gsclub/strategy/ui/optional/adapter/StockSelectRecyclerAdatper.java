package com.gsclub.strategy.ui.optional.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.CustomSelectStockBean;
import com.gsclub.strategy.ui.home.adapter.StockUtils;
import com.gsclub.strategy.ui.optional.activity.SelectStockActivity;

public class StockSelectRecyclerAdatper extends BaseRecyclerViewAdapter<CustomSelectStockBean> {

    private LayoutInflater inflater;
    private Context context;

    public StockSelectRecyclerAdatper(Context ctx) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_stock_select, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final CustomSelectStockBean item, int position) {
        holder.getTextView(R.id.tv_stock_name).setText(item.getSname());
        holder.getTextView(R.id.tv_stock_code).setText(item.getSymbol());

        StockUtils.setStockShow(
                holder.getTextView(R.id.tv_stock_now_price),
                item.getLastPrice(),
                context,
                false,
                false
        );

        StockUtils.setStockShow(
                holder.getTextView(R.id.tv_stock_float_precent),
                item.getPxChangeRate(),
                context,
                true,
                true
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        SelectStockActivity.start(context, getData().get(position).getSymbol(), getData().get(position).getSname());
    }
}
