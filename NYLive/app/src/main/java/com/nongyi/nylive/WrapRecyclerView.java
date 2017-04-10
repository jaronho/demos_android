package com.nongyi.nylive;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author:  jaron.ho
 * Date:    2017-04-10
 * Brief:   WrapRecyclerView(可以添加头部和底部)
 */

public class WrapRecyclerView<T> extends QuickRecyclerView<T> {
    private WrapViewAdapter mAdapter = null;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (null != mAdapter) {
            throw new AssertionError("exist adapter");
        }
        if (!(adapter instanceof WrapViewAdapter)) {
            throw new AssertionError("adapter must instance of WrapViewAdapter");
        }
        super.setAdapter(adapter);
        mAdapter = (WrapViewAdapter)adapter;
        mAdapter.adjustSpanSize(this);  // 解决GridLayout添加头部和底部也要占据一行
    }

    // 获取头部
    public View getHeaderView(int index) {
        if (null != mAdapter) {
            return mAdapter.getHeaderView(index);
        }
        return null;
    }

    // 获取底部
    public View getFooterView(int index) {
        if (null != mAdapter) {
            return mAdapter.getFooterView(index);
        }
        return null;
    }

    // 添加头部
    public void addHeaderView(View view) {
        if (null != mAdapter) {
            mAdapter.addHeaderView(view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        if (null != mAdapter) {
            mAdapter.addFooterView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if (null != mAdapter) {
            mAdapter.removeHeaderView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(int index) {
        if (null != mAdapter) {
            mAdapter.removeHeaderView(index);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (null != mAdapter) {
            mAdapter.removeFooterView(view);
        }
    }

    // 移除底部
    public void removeFooterView(int index) {
        if (null != mAdapter) {
            mAdapter.removeFooterView(index);
        }
    }

    public static abstract class WrapViewAdapter<T> extends QuickViewAdapter<T> {
        private static int BASE_ITEM_TYPE_HEADER = 10000000;    // 基本的头部类型开始位置
        private static int BASE_ITEM_TYPE_FOOTER = 20000000;    // 基本的底部类型开始位置
        private SparseArray<View> mHeaderViews;
        private SparseArray<View> mFooterViews;

        public WrapViewAdapter(Context context, List<T> datas, int layoutId) {
            super(context, datas, layoutId);
            mHeaderViews = new SparseArray<>();
            mFooterViews = new SparseArray<>();
        }

        public WrapViewAdapter(Context context, List<T> datas, MultiItemType<T> multiItemType) {
            super(context, datas, multiItemType);
            mHeaderViews = new SparseArray<>();
            mFooterViews = new SparseArray<>();
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + mHeaderViews.size() + mFooterViews.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderPosition(position)) {
                return mHeaderViews.keyAt(position);
            }
            if (isFooterPosition(position)) {
                position = position - mHeaderViews.size() - super.getItemCount();
                return mFooterViews.keyAt(position);
            }
            position = position - mHeaderViews.size();
            return super.getItemViewType(position);
        }

        @Override
        public QuickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (isHeaderViewType(viewType)) {
                return new QuickViewHolder(mHeaderViews.get(viewType));
            }
            if (isFooterViewType(viewType)) {
                return new QuickViewHolder(mFooterViews.get(viewType));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(QuickViewHolder holder, int position) {
            if (isHeaderPosition(position) || isFooterPosition(position)) {
                return;
            }
            position = position - mHeaderViews.size();
            super.onBindViewHolder(holder, position);
        }

        private boolean isHeaderViewType(int viewType) {
            return mHeaderViews.indexOfKey(viewType) >= 0;
        }

        private boolean isFooterViewType(int viewType) {
            return mFooterViews.indexOfKey(viewType) >= 0;
        }

        private boolean isHeaderPosition(int position) {
            return position < mHeaderViews.size();
        }

        private boolean isFooterPosition(int position) {
            return position >= (mHeaderViews.size() + super.getItemCount());
        }

        public View getHeaderView(int index) {
            if (index >= 0 && index < mHeaderViews.size()) {
                return mHeaderViews.valueAt(index);
            }
            return null;
        }

        public View getFooterView(int index) {
            if (index >= 0 && index < mFooterViews.size()) {
                return mFooterViews.valueAt(index);
            }
            return null;
        }

        public void addHeaderView(View view) {
            if (null != view && mHeaderViews.indexOfValue(view) < 0) {
                mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
            }
        }

        public void addFooterView(View view) {
            if (null != view && mFooterViews.indexOfValue(view) < 0) {
                mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
            }
        }

        public void removeHeaderView(View view) {
            if (null != view) {
                int index = mHeaderViews.indexOfValue(view);
                if (index >= 0) {
                    mHeaderViews.removeAt(index);
                }
            }
        }

        public void removeHeaderView(int index) {
            if (index >= 0 && index < mHeaderViews.size()) {
                mHeaderViews.removeAt(index);
            }
        }

        public void removeFooterView(View view) {
            if (null != view) {
                int index = mFooterViews.indexOfValue(view);
                if (index >= 0) {
                    mFooterViews.removeAt(index);
                }
            }
        }

        public void removeFooterView(int index) {
            if (index >= 0 && index < mFooterViews.size()) {
                mFooterViews.removeAt(index);
            }
        }

        // 解决GridLayoutManager添加头部和底部不占用一行的问题
        public void adjustSpanSize(RecyclerView recycler) {
            if (recycler.getLayoutManager() instanceof GridLayoutManager) {
                final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }
}
