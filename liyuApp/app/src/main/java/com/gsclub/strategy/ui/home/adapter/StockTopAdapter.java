package com.gsclub.strategy.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.OptionalTopStockBean;
import com.gsclub.strategy.ui.helper.ItemTouchHelperAdapter;

public class StockTopAdapter extends BaseRecyclerViewAdapter<OptionalTopStockBean> implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private Context context;

    public StockTopAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;

    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        if(viewType == 1)
            return inflater.inflate(R.layout.item_stock_top_left, group, false);
        if(viewType == 2)
            return inflater.inflate(R.layout.item_stock_top, group, false);
        return inflater.inflate(R.layout.item_stock_top_right, group, false);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final OptionalTopStockBean item, int position) {
        holder.getTextView(R.id.tv_stock_name).setText(item.getStockName());

        StockUtils.setStockShow(
                holder.getTextView(R.id.tv_stock_float_price),
                item.getPxChange(),
                context,
                true,
                false
        );

        StockUtils.setStockShow(
                holder.getTextView(R.id.tv_stock_float_precent),
                item.getPxChangeRate(),
                context,
                true,
                true
        );

        float symbol_price = (item.getPxChange() >= 0) ? 1.0f : -1.0f;

        StockUtils.setStockShow(
                holder.getTextView(R.id.tv_stock_now_price),
                item.getLastPx() * symbol_price,
                context,
                false,
                false
        );

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 3 == 0) {
            return 1;
        }
        if(position % 3 == 1) {
            return 2;
        }
        return 3;
    }
}
