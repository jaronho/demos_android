package com.nongyi.nylive;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nongyi.nylive.QuickRecyclerView.MultiItemType;
import com.nongyi.nylive.QuickRecyclerView.QuickViewHolder;
import com.nongyi.nylive.WrapRecyclerView.WrapViewAdapter;
import com.nongyi.nylive.RefreshRecyclerView.Creator;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private RefreshRecyclerView mRecylerView = null;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecylerView = (RefreshRecyclerView)view.findViewById(R.id.recyclerview_list);
        mRecylerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setAdapter(new WrapViewAdapter<VideoData>(getContext(), mDatas, mMultiItemType) {
            @Override
            public void convert(QuickViewHolder holder, VideoData data) {
                updateItemData(holder, data);
            }
        });
        mRecylerView.setHeadViewCreator(mRefreshViewCreator);
        mRecylerView.setFootViewCreator(mLoadViewCreator);
        mRecylerView.addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.item_space)));
        return view;
    }

    // 多数据项类型
    private MultiItemType<VideoData> mMultiItemType = new MultiItemType<VideoData>() {
        @Override
        public int getLayoutId(int position, VideoData data) {
            if (VideoData.TYPE_LIVE == data.type) {
                return R.layout.item_live;
            } else if (VideoData.TYPE_VIDEO == data.type) {
                return R.layout.item_video;
            }
            return 0;
        }
    };

    // 刷新视图构造器
    private Creator mRefreshViewCreator = new Creator() {
        @Override
        public View getView(Context context, RefreshRecyclerView parent) {
            return LayoutInflater.from(getContext()).inflate(R.layout.header_refresh, parent, false);
        }

        @Override
        public void onPull(int viewLength, int dragDistance, int status) {
        }

        @Override
        public void onRefreshing() {
            mRecylerView.stopHeadRefresh();

            VideoData vd = new VideoData();
            vd.type = VideoData.TYPE_LIVE;
            vd.date = "2017-04-14 9:00-21:00";
            vd.describe = "标题";
            vd.people = 2016;
            vd.status = VideoData.STATUS_NOTSTART;
            vd.url = "";
            mRecylerView.addItem(0, vd);
        }

        @Override
        public void onStopRefresh() {
        }
    };

    // 加载视图构造器
    private Creator mLoadViewCreator = new Creator() {
        private View mLoadView = null;

        @Override
        public View getView(Context context, RefreshRecyclerView parent) {
            mLoadView = LayoutInflater.from(getContext()).inflate(R.layout.footer_load, parent, false);
            return mLoadView;
        }

        @Override
        public void onPull(int viewLength, int dragDistance, int status) {
        }

        @Override
        public void onRefreshing() {
            mRecylerView.stopFootRefresh();
        }

        @Override
        public void onStopRefresh() {
        }
    };

    // 更新数据项
    private void updateItemData(QuickViewHolder holder, final VideoData data) {
        OnClickListener onClickItem = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), data.describe, Toast.LENGTH_SHORT).show();
            }
        };
        holder.setText(R.id.textview_describe, data.describe);
        holder.setText(R.id.textview_date, data.date);
        holder.setText(R.id.textview_num, String.valueOf(data.people));
        holder.setOnClickListener(onClickItem);
    }

    // 选项间距类
    private class SpaceItemDecoration extends ItemDecoration{
        private int mSpace = 0;

        public SpaceItemDecoration(int space) {
            mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (0 != parent.getChildAdapterPosition(view)) {
                outRect.top = mSpace;
            }
        }
    }
}
