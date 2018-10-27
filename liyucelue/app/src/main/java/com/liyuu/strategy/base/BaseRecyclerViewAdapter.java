package com.liyuu.strategy.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-07-21.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder>
        implements AbsListView.OnItemClickListener {

    private List<T> mData;

    public void setData(T... data) {
        mData = new ArrayList<>();
        if (data != null) {
            for (T t : data) mData.add(t);
        }
        notifyDataSetChanged();
    }

    public void setData(Collection<T> data) {
        mData = new ArrayList<>();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void appendData(T... data) {
        if (data == null) return;
        if (mData == null) mData = new ArrayList<>();
        for (T t : data) {
            mData.add(t);
        }
        notifyDataSetChanged();
    }

    public void appendData(Collection<T> data) {
        if (data == null) return;
        if (mData == null) mData = new ArrayList<>();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void insertData(int position, T... data) {
        if (data == null) return;
        if (mData == null) mData = new ArrayList<>();
        for (T t : data) {
            mData.add(position++, t);
        }
        notifyDataSetChanged();
    }

    public void insertData(int position, Collection<T> data) {
        if (data == null) return;
        if (mData == null) mData = new ArrayList<>();
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        if (mData == null) mData = new ArrayList<>();
        return mData;
    }

    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = onCreateView(viewGroup, viewType);
        return new RecyclerViewHolder(view).setOnItemClickListener(this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        onBindViewHolder(recyclerViewHolder, getItem(position), position);
    }

    public abstract View onCreateView(ViewGroup group, int viewType);

    public abstract void onBindViewHolder(RecyclerViewHolder holder, T item, int position);

    private OnItemClick<T> onItemClick;

    public void setOnItemClick(OnItemClick<T> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClick != null)
            onItemClick.onClick(position, getItem(position));
    }
}
