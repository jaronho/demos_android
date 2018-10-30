package com.gsclub.strategy.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/10/30.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<WeakReference<View>> mViews;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int id) {
        WeakReference<View> v = mViews.get(id);
        View tt = v == null ? null : v.get();
        if (tt == null) {
            tt = itemView.findViewById(id);
            mViews.put(id, new WeakReference<>(tt));
        }
        return (T) tt;
    }

    public RecyclerViewHolder setOnItemClickListener(final AdapterView.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, v, getPosition(), v.getId());
            }
        });
        return this;
    }

    public RecyclerViewHolder setOnItemLongClickListener(final AdapterView.OnItemLongClickListener listener) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(null, v, getPosition(), v.getId());
                return true;
            }
        });
        return this;
    }

    public TextView getTextView(int id) {
        return getView(id);
    }

    public ViewStub getViewStub(int id) {
        return getView(id);
    }

    public ImageView getImageView(int id) {
        return getView(id);
    }

    public Button getButton(int id) {
        return getView(id);
    }
}
