package com.liyuu.strategy.ui.stock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.ui.helper.ItemTouchHelperAdapter;
import com.liyuu.strategy.ui.home.adapter.StockUtils;
import com.liyuu.strategy.ui.stock.other.bean.SelectStockBean;
import com.liyuu.strategy.util.StringUtils;

public class SelectStockRecyclerAdapter extends BaseRecyclerViewAdapter<SelectStockBean> implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private Context context;
    private float symbol_price;

    public SelectStockRecyclerAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    public void setSymbol_price(float symbol_price) {
        this.symbol_price = symbol_price;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_select_stock, group, false);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final SelectStockBean item, int position) {
        holder.getTextView(R.id.tv_item_name).setText(item.getItemName());
        String s = "";
        if (StringUtils.stringIsEquals(item.getItemName(), "成交量"))
            s = "手";
        else if (StringUtils.stringIsEquals(item.getItemName(), "换手率")
                || StringUtils.stringIsEquals(item.getItemName(), "振幅"))
            s = "%";
        TextView tv_number = holder.getTextView(R.id.tv_number);
        tv_number.setText(String.format("%s%s", StringUtils.changeFloatTwo(item.getNumber()), s));

        if (StringUtils.stringIsEquals(item.getItemName(), "换手率")) {
            tv_number.setTextColor(context.getResources().getColor(R.color.stock_red_f16262));
        } else if (StringUtils.stringIsEquals(item.getItemName(), "今开") ||
                StringUtils.stringIsEquals(item.getItemName(), "昨收")) {
            StockUtils.setStockShow(
                    tv_number,
                    item.getNumber() * symbol_price, context,
                    false,
                    false);
            tv_number.setTextColor(context.getResources().getColor(R.color.stock_red_f16262));
        } else {
            tv_number.setTextColor(context.getResources().getColor(R.color.text_black_333333));
        }
    }
}
