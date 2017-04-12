package com.nongyi.nylive;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author:  jaron.ho
 * Date:    2017-04-12
 * Brief:   QuickRecyclerViewAdapter
 */

public abstract class QuickRecyclerViewAdapter<T> extends Adapter<QuickRecyclerViewAdapter.QuickViewHolder> {
    protected Context mContext = null;
    protected List<T> mDatas = null;
    private int mLayoutId = 0;
    private MultiLayout<T> mMultiLayout = null;

    public QuickRecyclerViewAdapter(Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
    }

    public QuickRecyclerViewAdapter(Context context, List<T> datas, MultiLayout<T> multiLayout) {
        this(context, datas, 0);
        mMultiLayout = multiLayout;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mMultiLayout) {
            return mMultiLayout.getLayoutId(position, mDatas.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public QuickViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (null != mMultiLayout) {
            mLayoutId = viewType;
        }
        if (mLayoutId <= 0) {
            return null;
        }
        return new QuickViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(QuickViewHolder holder, int position) {
        onBindViewHolder(holder, mDatas.get(position));
    }

    public abstract void onBindViewHolder(QuickViewHolder holder, T data);

    public static abstract class MultiLayout<T> {
        public abstract int getLayoutId(int position, T data);
    }

    public static class QuickViewHolder extends ViewHolder {
        private SparseArray<View> mViews = new SparseArray<>();

        public QuickViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (null == view) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T)view;
        }

        public void setOnClickListener(OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        public void setOnDragListener(OnDragListener listener) {
            itemView.setOnDragListener(listener);
        }

        public void setOnLongClickListener(OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }

        public void setOnTouchListener(OnTouchListener listener) {
            itemView.setOnTouchListener(listener);
        }
    }
}
