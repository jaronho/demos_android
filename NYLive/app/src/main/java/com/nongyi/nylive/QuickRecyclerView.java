package com.nongyi.nylive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Author:  jaron.ho
 * Date:    2017-04-10
 * Brief:   QuickRecyclerView(支持多选项布局类型)
 */

public class QuickRecyclerView<T> extends RecyclerView {
    private QuickViewAdapter mAdapter = null;
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (null != mAdapter) {
                mAdapter.notifyItemRemoved(positionStart);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (null != mAdapter) {
                mAdapter.notifyItemMoved(fromPosition, toPosition);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (null != mAdapter) {
                mAdapter.notifyItemChanged(positionStart);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (null != mAdapter) {
                mAdapter.notifyItemChanged(positionStart, payload);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (null != mAdapter) {
                mAdapter.notifyItemInserted(positionStart);
            }
        }
    };

    public QuickRecyclerView(Context context) {
        super(context);
    }

    public QuickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (null != mAdapter) {
            throw new AssertionError("exist adapter");
        }
        if (!(adapter instanceof QuickViewAdapter)) {
            throw new AssertionError("adapter must instance of QuickViewAdapter");
        }
        super.setAdapter(adapter);
        mAdapter = (QuickViewAdapter)adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    public void setItem(int index, T data) {
        if (null != mAdapter) {
            mAdapter.setItem(index, data);
        }
    }

    public void addItem(T data) {
        if (null != mAdapter) {
            mAdapter.addItem(data);
        }
    }

    public void addItem(int index, T data) {
        if (null != mAdapter) {
            mAdapter.addItem(index, data);
        }
    }

    public void removeItem(T data) {
        if (null != mAdapter) {
            mAdapter.removeItem(data);
        }
    }

    public void removeItem(int index) {
        if (null != mAdapter) {
            mAdapter.removeItem(index);
        }
    }

    public void clearItems() {
        if (null != mAdapter) {
            mAdapter.clearItems();
        }
    }

    public static abstract class MultiItemType<T> {
        public abstract int getLayoutId(int position, T data);
    }

    public static abstract class QuickViewAdapter<T> extends Adapter<QuickViewHolder> {
        private Context mContext = null;
        private List<T> mDatas = null;
        private int mLayoutId = 0;
        private MultiItemType<T> mMultiItemType = null;

        public QuickViewAdapter(Context context, List<T> datas, int layoutId) {
            mContext = context;
            mDatas = datas;
            mLayoutId = layoutId;
        }

        public QuickViewAdapter(Context context, List<T> datas, MultiItemType<T> multiItemType) {
            mContext = context;
            mDatas = datas;
            mMultiItemType = multiItemType;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (null != mMultiItemType) {
                return mMultiItemType.getLayoutId(position, mDatas.get(position));
            }
            return super.getItemViewType(position);
        }

        @Override
        public QuickViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            if (null != mMultiItemType) {
                mLayoutId = viewType;
            }
            View itemView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            return new QuickViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(QuickViewHolder holder, int position) {
            convert(holder, mDatas.get(position));
        }

        // 设置数据项
        public void setItem(int index, T data) {
            if (index >= 0 && index < mDatas.size() && null != data && !mDatas.contains(data)) {
                mDatas.set(index, data);
            }
        }

        // 添加数据项
        public void addItem(T data) {
            if (null != data && !mDatas.contains(data)) {
                mDatas.add(data);
            }
        }

        // 添加数据项
        public void addItem(int index, T data) {
            if (index >= 0 && index < mDatas.size() && null != data && !mDatas.contains(data)) {
                mDatas.add(index, data);
            }
        }

        // 移除数据项
        public void removeItem(T data) {
            if (null != data && mDatas.contains(data)) {
                mDatas.remove(data);
            }
        }

        // 移除数据项
        public void removeItem(int index) {
            if (index >= 0 && index < mDatas.size()) {
                mDatas.remove(index);
            }
        }

        // 清空数据项
        public void clearItems() {
            mDatas.clear();
        }

        public abstract void convert(QuickViewHolder holder, T data);
    }

    public static class QuickViewHolder extends ViewHolder {
        private SparseArray<View> mViews;

        public QuickViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (null == view) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setTag(int viewId, Object tag) {
            getView(viewId).setTag(tag);
        }

        public void setTag(int viewId, int key, Object tag) {
            getView(viewId).setTag(key, tag);
        }

        public void setAlpha(int viewId, float value) {
            getView(viewId).setAlpha(value);
        }

        public void setVisible(int viewId, int visible) {
            getView(viewId).setVisibility(visible);
        }

        public void setBackgroundColor(int viewId, int color) {
            getView(viewId).setBackgroundColor(color);
        }

        public void setBackgroundResource(int viewId, int backgroundRes) {
            getView(viewId).setBackgroundResource(backgroundRes);
        }

        public void setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setTextColor(int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
        }

        public void setTextColorRes(int viewId, int textColorRes, Context context) {
            TextView view = getView(viewId);
            view.setTextColor(context.getResources().getColor(textColorRes));
        }

        public void linkify(int viewId) {
            TextView view = getView(viewId);
            Linkify.addLinks(view, Linkify.ALL);
        }

        public void setTypeface(Typeface typeface, int... viewIds) {
            for (int viewId : viewIds) {
                TextView view = getView(viewId);
                view.setTypeface(typeface);
                view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }

        public void setImageResource(int viewId, int resId) {
            ImageView view = getView(viewId);
            view.setImageResource(resId);
        }

        public void setImageBitmap(int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
        }

        public void setImageDrawable(int viewId, Drawable drawable) {
            ImageView view = getView(viewId);
            view.setImageDrawable(drawable);
        }

        public void setProgress(int viewId, int max, int progress) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            view.setProgress(progress);
        }

        public void setRating(int viewId, int max, float rating) {
            RatingBar view = getView(viewId);
            view.setMax(max);
            view.setRating(rating);
        }

        public void setChecked(int viewId, boolean checked) {
            Checkable view = getView(viewId);
            view.setChecked(checked);
        }

        public void setOnTouchListener(OnTouchListener listener) {
            itemView.setOnTouchListener(listener);
        }

        public void setOnTouchListener(int viewId, OnTouchListener listener) {
            getView(viewId).setOnTouchListener(listener);
        }

        public void setOnClickListener(OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        public void setOnClickListener(int viewId, OnClickListener listener) {
            getView(viewId).setOnClickListener(listener);
        }

        public void setOnLongClickListener(OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }

        public void setOnLongClickListener(int viewId, OnLongClickListener listener) {
            getView(viewId).setOnLongClickListener(listener);
        }

        public void setOnDragListener(OnDragListener listener) {
            itemView.setOnDragListener(listener);
        }

        public void setOnDragListener(int viewId, OnDragListener listener) {
            getView(viewId).setOnDragListener(listener);
        }
    }
}
