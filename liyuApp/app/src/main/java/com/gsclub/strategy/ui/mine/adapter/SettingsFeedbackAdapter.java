package com.gsclub.strategy.ui.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.util.ScreenUtil;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class SettingsFeedbackAdapter extends BaseRecyclerViewAdapter<String> {
    private LayoutInflater mInflater;
    private Context mContext;
    private OnDeleteListener mOnDeleteListener;
    private OnAddListener mOnAddListener;

    public SettingsFeedbackAdapter(Context ctx) {
        this.mInflater = LayoutInflater.from(ctx);
        mContext = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        View view = mInflater.inflate(R.layout.item_settings_feedback, group, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = ScreenUtil.dp2px(mContext, 75);
        lp.width = width;
        lp.height = width;
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        ImageView deleteIv = holder.getImageView(R.id.iv_delete);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(position);
                }
            }
        });
        int size = this.getItemCount();
        String url = getItem(position);
        ImageView feedbackIv = holder.getImageView(R.id.iv_feedback);
        if(position == size - 1 && TextUtils.isEmpty(url)) {
            deleteIv.setVisibility(View.GONE);
            Glide.with(mContext).load(R.mipmap.ic_add_image).into(feedbackIv);
        }else {
            deleteIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(url).into(feedbackIv);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, String item, final int position) {
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        int size = getItemCount();
        if(position != size-1) return;
        if(mOnAddListener != null) {
            mOnAddListener.onAdd(position);
        }
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }

    public void setOnAddListener(OnAddListener onAddListener) {
        this.mOnAddListener = onAddListener;
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public interface OnAddListener {
        void onAdd(int position);
    }
}
