package com.liyuu.strategy.ui.transaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.bean.TradeIndexBean;
import com.liyuu.strategy.ui.helper.ItemTouchHelperAdapter;

public class BuyingAdapter extends BaseRecyclerViewAdapter<TradeIndexBean.DepositListBean> implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private Context context;

    public BuyingAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_buying, group, false);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final TradeIndexBean.DepositListBean item, int position) {
        TextView tvPrice = holder.getTextView(R.id.tv_stock_pay_price);
        tvPrice.setText(item.getName());
        tvPrice.setBackgroundResource(item.isSelect() ? R.drawable.btn_stock_pay_select : R.drawable.btn_stock_pay_normal);
        tvPrice.setTextColor(context.getResources().getColor(item.isSelect() ? R.color.text_orange_ff8400 : R.color.text_black_333333));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        selectItem(position);
        notifyDataSetChanged();
    }

    public void selectItem(int position) {
        for (int i = 0; i < getData().size(); i++) {
            getData().get(i).setSelect(i == position);
        }
    }
}
