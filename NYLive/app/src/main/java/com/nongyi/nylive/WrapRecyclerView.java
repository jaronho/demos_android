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
 * Brief:   WrapRecyclerView(支持多头部和多底部)
 */

public class WrapRecyclerView extends QuickRecyclerView {
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
    public View getHeaderView(int key) {
        if (null != mAdapter) {
            return mAdapter.getHeaderView(key);
        }
        return null;
    }

    // 获取头部
    public View getHeaderViewAt(int index) {
        if (null != mAdapter) {
            return mAdapter.getHeaderViewAt(index);
        }
        return null;
    }

    // 获取底部
    public View getFooterView(int key) {
        if (null != mAdapter) {
            return mAdapter.getFooterView(key);
        }
        return null;
    }

    // 获取底部
    public View getFooterViewAt(int index) {
        if (null != mAdapter) {
            return mAdapter.getFooterViewAt(index);
        }
        return null;
    }

    // 添加头部
    public int addHeaderView(View view) {
        if (null != mAdapter) {
            return mAdapter.addHeaderView(view);
        }
        return 0;
    }

    // 添加头部(key越小,越在上面)
    public void addHeaderView(int key, View view) {
        if (null != mAdapter) {
            mAdapter.addHeaderView(key, view);
        }
    }

    // 添加底部
    public int addFooterView(View view) {
        if (null != mAdapter) {
            return mAdapter.addFooterView(view);
        }
        return 0;
    }

    // 添加底部(key越大,越在下面)
    public void addFooterView(int key, View view) {
        if (null != mAdapter) {
            mAdapter.addFooterView(key, view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if (null != mAdapter) {
            mAdapter.removeHeaderView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(int key) {
        if (null != mAdapter) {
            mAdapter.removeHeaderView(key);
        }
    }

    // 移除头部
    public void removeHeaderViewAt(int index) {
        if (null != mAdapter) {
            mAdapter.removeHeaderViewAt(index);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (null != mAdapter) {
            mAdapter.removeFooterView(view);
        }
    }

    // 移除底部
    public void removeFooterView(int key) {
        if (null != mAdapter) {
            mAdapter.removeFooterView(key);
        }
    }

    // 移除底部
    public void removeFooterViewAt(int index) {
        if (null != mAdapter) {
            mAdapter.removeFooterViewAt(index);
        }
    }

    public static abstract class WrapViewAdapter<T> extends QuickViewAdapter<T> {
        private SparseArray<View> mHeaderViews;
        private SparseArray<View> mFooterViews;
        private static int mBaseHeaderKey = 0;
        private static int mBaseFooterKey = 0;

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

        public View getHeaderView(int key) {
            if (mHeaderViews.indexOfKey(key) >= 0) {
                return mHeaderViews.get(key);
            }
            return null;
        }

        public View getHeaderViewAt(int index) {
            if (index >= 0 && index < mHeaderViews.size()) {
                return mHeaderViews.valueAt(index);
            }
            return null;
        }

        public View getFooterView(int key) {
            if (mFooterViews.indexOfKey(key) >= 0) {
                return mFooterViews.get(key);
            }
            return null;
        }

        public View getFooterViewAt(int index) {
            if (index >= 0 && index < mFooterViews.size()) {
                return mFooterViews.valueAt(index);
            }
            return null;
        }

        public int addHeaderView(View view) {
            int key = 0;
            if (null != view && mHeaderViews.indexOfValue(view) < 0) {
                key = mBaseHeaderKey++;
                mHeaderViews.put(key, view);
            }
            return key;
        }

        public void addHeaderView(int key, View view) {
            if (null != view && mHeaderViews.indexOfKey(key) < 0) {
                mHeaderViews.put(key, view);
            }
        }

        public int addFooterView(View view) {
            int key = 0;
            if (null != view && mFooterViews.indexOfValue(view) < 0) {
                key = mBaseFooterKey++;
                mFooterViews.put(key, view);
            }
            return key;
        }

        public void addFooterView(int key, View view) {
            if (null != view && mFooterViews.indexOfKey(key) < 0) {
                mFooterViews.put(key, view);
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

        public void removeHeaderView(int key) {
            if (mHeaderViews.indexOfKey(key) >= 0) {
                mHeaderViews.remove(key);
            }
        }

        public void removeHeaderViewAt(int index) {
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

        public void removeFooterView(int key) {
            if (mFooterViews.indexOfKey(key) >= 0) {
                mFooterViews.remove(key);
            }
        }

        public void removeFooterViewAt(int index) {
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
