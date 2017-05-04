package com.example.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classes.ScoreBean;
import com.example.nyapp.MyScoreActivity;
import com.example.nyapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NY on 2017/1/17.
 * 积分记录表
 */

public class MyScoreListAdapter extends RecyclerView.Adapter<MyScoreListAdapter.MyScoreListViewHolder> {
    private List<ScoreBean.DataBean.PointsRecordModelListBean> mList;
    private MyScoreActivity mActivity;

    public MyScoreListAdapter(List<ScoreBean.DataBean.PointsRecordModelListBean> list, MyScoreActivity activity) {
        mList = list;
        mActivity = activity;
    }

    public void addData(List<ScoreBean.DataBean.PointsRecordModelListBean> dataList) {
        mList.addAll(dataList);
    }


    @Override
    public MyScoreListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_score_list_item, parent, false);
        return new MyScoreListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyScoreListViewHolder holder, int position) {
        ScoreBean.DataBean.PointsRecordModelListBean pointsRecordModelListBean = mList.get(position);
        String[] time = pointsRecordModelListBean.getCreateTime().split("T");
        holder.mTvScoreTime.setText(time[0]);
        holder.mTvScoreNum.setText(String.valueOf(pointsRecordModelListBean.getPointNum()));
        holder.mTvScoreType.setText(pointsRecordModelListBean.getPointRules());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyScoreListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_score_time)
        TextView mTvScoreTime;
        @BindView(R.id.tv_score_num)
        TextView mTvScoreNum;
        @BindView(R.id.tv_score_type)
        TextView mTvScoreType;
        MyScoreListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
