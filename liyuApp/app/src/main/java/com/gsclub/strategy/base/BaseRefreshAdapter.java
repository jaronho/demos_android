package com.gsclub.strategy.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-07-21.
 */

public abstract class BaseRefreshAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder>
        implements AbsListView.OnItemClickListener {

    private List<T> mData;
    private int mPage = 1;
    private int mPageSize = 10;

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void doRefresh(List<T> response, View noDataLayout, RecyclerView recyclerView, SmartRefreshLayout refreshLayout) {
        if(response == null || response.size() == 0) {
            refreshLayout.setEnableLoadMore(false);
            if(mPage == 1) {
                recyclerView.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
            return;
        }
        if(response.size() < mPageSize) {
            refreshLayout.setEnableLoadMore(false);
        }else {
            refreshLayout.setEnableLoadMore(true);
        }
        if (mPage == 1) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            if (getData() != null && getData().size() > 0) {
                getData().clear();
            }
        }
        appendData(response);
        mPage++;

//        if (mPage == 1 && getData() != null && getData().size() > 0) {
//            //刷新数据时，自动滚到顶部，防止刷新未滚动到第一条，造成视觉异常
//            recyclerView.scrollToPosition(0);
//        }

//        if (response.size() > 0)
//            mPage++;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }
}
