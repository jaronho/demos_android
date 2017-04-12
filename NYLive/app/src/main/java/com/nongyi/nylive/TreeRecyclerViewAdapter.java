package com.nongyi.nylive;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  jaron.ho
 * Date:    2017-04-12
 * Brief:   TreeRecyclerViewAdapter
 */

public class TreeRecyclerViewAdapter extends WrapRecyclerViewAdapter {
    public TreeRecyclerViewAdapter(Context context, List<TreeItem> datas) {
        super(context, datas, 0);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        if (0 == viewType) {
            TreeItem item = (TreeItem)mDatas.get(position);
            viewType = item.getLayoutId(position, item.getData());
        }
        return viewType;
    }

    @Override
    public QuickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuickViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (null == viewHolder) {
            viewHolder = new QuickViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuickViewHolder holder, final int position) {
        final TreeItem item = (TreeItem)mDatas.get(position);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse(position, false);
                item.onClick(item.getData());
            }
        });
        onBindViewHolder(holder, item);
    }

    @Override
    public void onBindViewHolder(QuickViewHolder holder, Object item) {
        ((TreeItem)item).onBindViewHolder(holder, ((TreeItem)item).getData());
    };

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        // 判断是否是GridLayoutManager,因为GridLayoutManager才能设置每个条目的行占比
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    TreeItem item = (TreeItem)mDatas.get(position);
                    if (0 == item.getSpanSize()) {  // 如果是默认的大小,则占一行
                        return gridLayoutManager.getSpanCount();
                    }
                    return item.getSpanSize();  // 根据item的SpanSize来决定所占大小
                }
            });
        }
    }

    // 展开/折叠
    public void expandOrCollapse(int position, boolean recursion) {
        TreeItem item = (TreeItem)mDatas.get(position);
        if (item.hasChildren()) {
            if (item.isExpand()) {  // 当前展开,执行折叠
                mDatas.removeAll(item.getAllChildren());
                item.expandOrCollapse(false, true);
            } else {    // 当前折叠,执行展开
                mDatas.addAll(position + 1, recursion ? item.getAllChildren() : item.getChildren());
                item.expandOrCollapse(true, recursion);
            }
            notifyDataSetChanged();
        }
    }

    // 添加选项
    public void addItem(TreeItem item, int index) {
        if (null == item || null != item.getParent() || mDatas.contains(item)) {
            return;
        }
        item.expandOrCollapse(false, true);
        int count = mDatas.size();
        if (index < 0) {
            index = 0;
        } else if (index >= count) {
            index = count;
        }
        mDatas.add(index, item);
        notifyDataSetChanged();
    }

    // 添加选项
    public void addItem(TreeItem item, TreeItem parent, int index) {
        if (null == item || null != item.getParent() || null == parent || mDatas.contains(item)) {
            return;
        }
        item.expandOrCollapse(false, true);
        int count = parent.getChildren().size();
        if (index < 0) {
            index = 0;
        } else if (index >= count) {
            index = count;
        }
        parent.getChildren().add(index, item);
        int position = mDatas.indexOf(parent);
        if (position >= 0 && parent.isExpand()) {
            expandOrCollapse(position, true);
        }
    }

    // 移除选项
    public void removeItem(TreeItem item) {
        if (null != item) {
            item.expandOrCollapse(false, true);
            mDatas.remove(item);
            if (item.hasChildren()) {
                mDatas.removeAll(item.getAllChildren());
            }
            TreeItem parent = item.getParent();
            if (null != parent) {
                parent.getChildren().remove(item);
            }
            item.setParent(null);
            notifyDataSetChanged();
        }
    }

    public static abstract class TreeItem<T> extends MultiLayout<T> {
        private T mData = null;
        private boolean mIsExpand = false;
        private TreeItem mParent = null;
        private List<TreeItem> mChildren = null;

        public TreeItem(T data, TreeItem parent) {
            mData = data;
            mParent = parent;
            mChildren = onInitChildren(data);
            if (null == mChildren) {
                mChildren = new ArrayList<>();
            }
        }

        // 获取数据
        public T getData() {
            return mData;
        }

        // 设置数据
        public void setData(T data) {
            mData = data;
        }

        // 展开或折叠
        public void expandOrCollapse(boolean expand, boolean recursion) {
            mIsExpand = expand;
            if (recursion) {
                for (int i = 0, len = mChildren.size(); i < len; ++i) {
                    mChildren.get(i).expandOrCollapse(expand, true);
                }
            }
        }

        // 是否展开
        public boolean isExpand() {
            return mIsExpand;
        }

        // 是否有子项
        public boolean hasChildren() {
            return mChildren.size() > 0;
        }

        // 获取父节点
        public TreeItem getParent() {
            return mParent;
        }

        // 设置父节点
        public void setParent(TreeItem parent) {
            mParent = parent;
        }

        // 获取子项
        public List<TreeItem> getChildren() {
            return mChildren;
        }

        // 递归遍历所有子项
        public List<TreeItem> getAllChildren() {
            List<TreeItem> allChildren = new ArrayList<>();
            for (int i = 0, len = mChildren.size(); i < len; ++i) {
                TreeItem item = mChildren.get(i);
                allChildren.add(item);
                if (item.hasChildren()) {
                    List children = item.getAllChildren();
                    if (null != children && children.size() > 0) {
                        allChildren.addAll(children);
                    }
                }
            }
            return allChildren;
        }

        // item在每行中的spansize,如果为0则占满一行
        public abstract int getSpanSize();

        // 初始化子项
        public abstract List<TreeItem> onInitChildren(T data);

        // 绑定视图
        public abstract void onBindViewHolder(QuickViewHolder holder, T data);

        // 被点击
        public abstract void onClick(T data);
    }
}
