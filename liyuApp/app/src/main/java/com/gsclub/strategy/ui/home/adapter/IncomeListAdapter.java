package com.gsclub.strategy.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.model.bean.IncomeListBean;
import com.gsclub.strategy.ui.home.activity.PersonalRecordActivity;

public class IncomeListAdapter extends BaseRecyclerViewAdapter<IncomeListBean.ListBean> {

    private LayoutInflater inflater;
    private Context context;
    private int incomeType;

    private int[] resIds = {R.mipmap.icon_income_first, R.mipmap.icon_income_second, R.mipmap.icon_income_third};

    public IncomeListAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_income_list, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final IncomeListBean.ListBean item, int position) {
        TextView tvPosition = holder.getTextView(R.id.tv_position);
        if (position <= 2) {
            Drawable drawable = context.getResources().getDrawable(resIds[position]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            tvPosition.setCompoundDrawables(drawable, null, null, null);
            tvPosition.setText("");
        } else {
            tvPosition.setCompoundDrawables(null, null, null, null);
            tvPosition.setText(String.valueOf(position + 1));
        }
//        int size = getItemCount();
//        holder.getView(R.id.view_line).setVisibility(position == size - 1 ? View.GONE : View.VISIBLE);

        ImageLoader.loadHead(App.getInstance(), item.getHeadimg(), holder.getImageView(R.id.iv_head));

        holder.getTextView(R.id.tv_nickname).setText(item.getNickname());

        holder.getTextView(R.id.tv_show_one).setText(incomeType == 1 ? "盈利" : "收益率");

        holder.getTextView(R.id.tv_show_two).setText(String.format("%s%s", incomeType == 1 ? item.getProfit() : item.getRate(), incomeType == 1 ? "元" : "%"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        PersonalRecordActivity.start(context, getItem(position).getId());
    }

}
