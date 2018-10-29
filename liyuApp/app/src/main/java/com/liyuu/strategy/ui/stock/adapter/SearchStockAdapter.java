package com.liyuu.strategy.ui.stock.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.OnItemClick;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.bean.SearchStockBean;
import com.liyuu.strategy.ui.optional.activity.SelectStockActivity;
import com.liyuu.strategy.ui.stock.activity.StockActivity;
import com.liyuu.strategy.util.CommonUtil;

/**
 * Created by hlw on 2017/12/7.
 * 股票搜索界面股票列表适配器
 */

public class SearchStockAdapter extends BaseRecyclerViewAdapter<SearchStockBean> {

    private LayoutInflater inflater;
    private Activity context;
    private OnItemClick<SearchStockBean> onItemClick;

    public SearchStockAdapter(Activity ctx) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    public void setOnItemClick(OnItemClick<SearchStockBean> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_stock_search, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final SearchStockBean item, int position) {

        holder.getTextView(R.id.tv_name).setText(item.getSname());

        holder.getTextView(R.id.tv_stock_number).setText(item.getSymbol());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if (CommonUtil.isReview()){
            SelectStockActivity.start(context, getData().get(position).getSymbol(), getData().get(position).getSname());
        }else {
            StockActivity.start(context, getData().get(position).getSymbol(), getData().get(position).getSname());
        }

        getItem(position).setAddTime(System.currentTimeMillis());

        if (onItemClick != null)
            onItemClick.onClick(position, getItem(position));

    }

}
