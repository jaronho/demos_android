package com.tencent.qcloud.xiaozhibo.mainui.list;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 小视频列表的Adapter
 * 列表项布局格式: R.layout.listview_ugc_item
 * 列表项数据格式: TCLiveInfo
 */

public class TCUGCVideoListAdapter extends ArrayAdapter<TCLiveInfo> {
    private int resourceId;
    private Activity mActivity;
    private class ViewHolder{
        TextView tvHost;
        ImageView ivCover;
        ImageView ivAvatar;
        TextView tvCreateTime;
    }

    public TCUGCVideoListAdapter(Activity activity, ArrayList<TCLiveInfo> objects) {
        super(activity, R.layout.listview_ugc_item, objects);
        resourceId = R.layout.listview_ugc_item;
        mActivity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TCUGCVideoListAdapter.ViewHolder holder;

        if (convertView != null) {
            holder = (TCUGCVideoListAdapter.ViewHolder)convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder = new TCUGCVideoListAdapter.ViewHolder();
            holder.ivCover = (ImageView) convertView.findViewById(R.id.cover);
            holder.tvHost = (TextView) convertView.findViewById(R.id.host_name);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.tvCreateTime = (TextView) convertView.findViewById(R.id.create_time);

            ViewGroup.LayoutParams params = holder.ivCover.getLayoutParams();
            params.width = (parent.getWidth() - 6) / 2;
            params.height = params.width * 16 / 9;
            holder.ivCover.setLayoutParams(params);
            convertView.setTag(holder);
        }

        TCLiveInfo data = getItem(position);

        //UGC预览图
        String cover = data.userinfo.frontcover;
        if (TextUtils.isEmpty(cover)){
            holder.ivCover.setImageResource(R.drawable.bg_ugc);
        }else{
            RequestManager req = Glide.with(mActivity);
            req.load(cover).placeholder(R.drawable.bg_ugc).into(holder.ivCover);
        }

        //主播头像
        TCUtils.showPicWithUrl(mActivity,holder.ivAvatar,data.userinfo.headpic,R.drawable.face);
        //主播昵称
        if (TextUtils.isEmpty(data.userinfo.nickname)){
            holder.tvHost.setText(TCUtils.getLimitString(data.userid, 10));
        }else{
            holder.tvHost.setText(TCUtils.getLimitString(data.userinfo.nickname, 10));
        }
        //小视频创建时间（发布时间）
        holder.tvCreateTime.setText(generateTimeStr(data.timestamp));

        return convertView;
    }

    public String generateTimeStr(long timestamp){
        String result = "刚刚";
        long timeDistanceinSec = System.currentTimeMillis()/1000 - timestamp;
        if (timeDistanceinSec >= 60 && timeDistanceinSec < 3600) {
            result = String.format(Locale.CHINA, "%d", (timeDistanceinSec) / 60) + "分钟前";
        } else if (timeDistanceinSec >= 3600 && timeDistanceinSec < 60*60*24) {
            result = String.format(Locale.CHINA, "%d", (timeDistanceinSec) / 3600)+ "小时前";
        } else if (timeDistanceinSec >= 3600 * 24){
            result = String.format(Locale.CHINA, "%d", (timeDistanceinSec) / (3600 * 24))+ "天前";
        }
        return result;
    }
}


