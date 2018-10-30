package com.gsclub.strategy.ui.optional.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.model.bean.CustomSelectStockBean;
import com.gsclub.strategy.ui.helper.ItemTouchHelperAdapter;
import com.gsclub.strategy.ui.helper.OnStartDragListener;


public class StockSelectEditRecyclerAdatper extends BaseRecyclerViewAdapter<CustomSelectStockBean>
        implements ItemTouchHelperAdapter {

    private final OnStartDragListener mDragStartListener;
    private LayoutInflater inflater;
    private OnSelectChange selectChange;

    public StockSelectEditRecyclerAdatper(Context ctx, OnStartDragListener dragStartListener) {
        this.inflater = LayoutInflater.from(ctx);
        this.mDragStartListener = dragStartListener;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_stock_select_edit, group, false);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, @SuppressLint("RecyclerView") final CustomSelectStockBean item, int position) {
        holder.getTextView(R.id.tv_stock).setText(item.getSname());

        holder.getTextView(R.id.tv_stock_code).setText(item.getSymbol());

        holder.getImageView(R.id.img_select).setBackgroundResource(
                (item.isChecked()) ? R.mipmap.optional_edit_choice_selected : R.mipmap.optional_edit_choice
        );

        holder.getImageView(R.id.img_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setChecked(!item.isChecked());
                selectChange.onChange();
                notifyDataSetChanged();
            }
        });

        holder.getView(R.id.rl_drag).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        super.onItemDismiss(position);
    }

    public void setSelectChange(OnSelectChange selectChange) {
        this.selectChange = selectChange;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        getData().get(position).setChecked(!getData().get(position).isChecked());
        selectChange.onChange();
        notifyDataSetChanged();
    }

    public interface OnSelectChange {
        void onChange();
    }
}
