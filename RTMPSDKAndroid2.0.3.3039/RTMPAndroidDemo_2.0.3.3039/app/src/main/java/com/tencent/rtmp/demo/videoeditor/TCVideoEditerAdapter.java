package com.tencent.rtmp.demo.videoeditor;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.rtmp.demo.R;
import com.tencent.rtmp.demo.common.utils.TCConstants;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yuejiaoli on 2017/4/30.
 */

public class TCVideoEditerAdapter extends RecyclerView.Adapter<TCVideoEditerAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<String> data = new ArrayList<String>();

    public TCVideoEditerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int itemCount = TCConstants.THUMB_COUNT;
        int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        final int itemWidth = (screenWidth - 2 * padding) / itemCount;
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.ugc_item_thumb_height);
        ImageView view = new ImageView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, height));
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new TCVideoEditerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = data.get(position);
        Glide.with(mContext).load(Uri.fromFile(new File(path))).into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(int position, String filePath) {
        data.add(filePath);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView;
        }
    }

    public void addAll(ArrayList<String> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }
}
