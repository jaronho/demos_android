package com.tencent.qcloud.xiaozhibo.common.activity.videochoose;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.common.utils.TCVideoFileInfo;

import java.io.File;
import java.util.ArrayList;

public class TCVideoEditerListAdapter extends RecyclerView.Adapter<TCVideoEditerListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TCVideoFileInfo> data = new ArrayList<TCVideoFileInfo>();
    private int mLastSelected = -1;
    private boolean mMultiplePick;

    public TCVideoEditerListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_ugc_video, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TCVideoFileInfo fileInfo = data.get(position);
        holder.ivSelected.setVisibility(fileInfo.isSelected() ? View.VISIBLE : View.GONE);
        holder.duration.setText(TCUtils.formattedTime(fileInfo.getDuration() / 1000));
        Glide.with(mContext).load(Uri.fromFile(new File(fileInfo.getFilePath()))).into(holder.thumb);
        holder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMultiplePick)
                    changeMultiSelection(position);
                else {
                    changeSingleSelection(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setMultiplePick(boolean multiplePick) {
        mMultiplePick = multiplePick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumb;
        private final TextView duration;
        private final ImageView ivSelected;

        public ViewHolder(final View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            duration = (TextView) itemView.findViewById(R.id.tv_duration);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    public ArrayList<TCVideoFileInfo> getMultiSelected() {
        ArrayList<TCVideoFileInfo> infos = new ArrayList<TCVideoFileInfo>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected()) {
                infos.add(data.get(i));
            }
        }
        return infos;
    }

    public TCVideoFileInfo getSingleSelected() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected()) {
                return data.get(i);
            }
        }
        return null;
    }

    public void addAll(ArrayList<TCVideoFileInfo> files) {
        try {
            this.data.clear();
            this.data.addAll(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void changeSingleSelection(int position) {
        if (mLastSelected != -1) {
            data.get(mLastSelected).setSelected(false);
        }
        notifyItemChanged(mLastSelected);

        TCVideoFileInfo info = data.get(position);
        info.setSelected(true);
        notifyItemChanged(position);

        mLastSelected = position;
    }

    public void changeMultiSelection(int position) {
        if (data.get(position).isSelected()) {
            data.get(position).setSelected(false);
        } else {
            data.get(position).setSelected(true);
        }
        notifyItemChanged(position);
    }

}
