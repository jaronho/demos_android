package com.nongyi.nylive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nongyi.nylive.QuickRecyclerView.MultiItemType;
import com.nongyi.nylive.QuickRecyclerView.QuickViewHolder;
import com.nongyi.nylive.WrapRecyclerView.WrapViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private WrapRecyclerView mRecylerView = null;
    private List<VideoData> mDatas = new ArrayList<>();

    public ListFragment() {
        VideoData vd1 = new VideoData();
        vd1.type = VideoData.TYPE_VIDEO;
        vd1.date = "2017-03-10 8:00-20:00";
        vd1.describe = "标题1";
        vd1.people = 1023;
        vd1.status = VideoData.STATUS_NOTSTART;
        vd1.url = "";
        mDatas.add(vd1);

        VideoData vd2 = new VideoData();
        vd2.type = VideoData.TYPE_LIVE;
        vd2.date = "2017-04-06 8:00-22:00";
        vd2.describe = "标题2";
        vd2.people = 956;
        vd2.status = VideoData.STATUS_NOTSTART;
        vd2.url = "";
        mDatas.add(vd2);

        VideoData vd3 = new VideoData();
        vd3.type = VideoData.TYPE_VIDEO;
        vd3.date = "2017-04-10 9:00-20:00";
        vd3.describe = "标题3";
        vd3.people = 3014;
        vd3.status = VideoData.STATUS_NOTSTART;
        vd3.url = "";
        mDatas.add(vd3);

        VideoData vd4 = new VideoData();
        vd4.type = VideoData.TYPE_LIVE;
        vd4.date = "2017-04-14 9:00-21:00";
        vd4.describe = "标题4";
        vd4.people = 2016;
        vd4.status = VideoData.STATUS_NOTSTART;
        vd4.url = "";
        mDatas.add(vd4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecylerView = (WrapRecyclerView)view.findViewById(R.id.recyclerview_list);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecylerView.setAdapter(new WrapViewAdapter<VideoData>(view.getContext(), mDatas, new MultiItemType<VideoData>() {
            @Override
            public int getLayoutId(int position, VideoData data) {
                if (VideoData.TYPE_LIVE == data.type) {
                    return R.layout.item_live;
                }
                return R.layout.item_video;
            }
        }) {
            @Override
            public void convert(QuickViewHolder holder, VideoData data) {
                holder.setText(R.id.textview_describe, data.describe);
                holder.setText(R.id.textview_date, data.date);
                holder.setText(R.id.textview_num, String.valueOf(data.people));
            }
        });
        mRecylerView.addHeaderView(inflater.inflate(R.layout.header_refresh, container, false));
        mRecylerView.addFooterView(inflater.inflate(R.layout.footer_load, container, false));
        return view;
    }
}
