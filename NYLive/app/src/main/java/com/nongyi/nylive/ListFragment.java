package com.nongyi.nylive;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nongyi.nylive.QuickRecyclerViewAdapter.MultiLayout;
import com.nongyi.nylive.QuickRecyclerViewAdapter.QuickViewHolder;
import com.nongyi.nylive.WrapRecyclerView.Creator;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private WrapRecyclerView mRecylerView = null;
    private List<VideoData> mDatas = new ArrayList<>();

    public ListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecylerView = (WrapRecyclerView)view.findViewById(R.id.recyclerview_list);
        mRecylerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setAdapter(new WrapRecyclerViewAdapter<VideoData>(getContext(), mDatas, mMultiLayout) {
            @Override
            public void onBindViewHolder(QuickViewHolder holder, VideoData data) {
                updateItemData(holder, data);
            }
        });
        mRecylerView.setHeadViewCreator(mRefreshViewCreator);
        mRecylerView.setFootViewCreator(mLoadViewCreator);
        ((WrapRecyclerViewAdapter)mRecylerView.getAdapter()).removeFooterView(WrapRecyclerView.KEY_FOOT_VIEW);
        mRecylerView.addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.item_space)));
        return view;
    }

    // 视图数据项
    private MultiLayout<VideoData> mMultiLayout = new MultiLayout<VideoData>() {
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
        private int mIndex = 0;

        @Override
        public View getView(Context context, WrapRecyclerView parent) {
            return LayoutInflater.from(getContext()).inflate(R.layout.header_refresh, parent, false);
        }

        @Override
        public void onPull(int viewLength, int dragDistance, int status) {
            // TODO: 下拉刷新数据
        }

        @Override
        public void onPullAbort() {
        }

        @Override
        public void onRefreshing() {
            // TODO: 刷新数据
            mRecylerView.stopHeadRefresh();
            VideoData vd = new VideoData();
            vd.type = VideoData.TYPE_LIVE;
            vd.date = "2017-04-14 9:00-21:00";
            vd.describe = "标题: " + (++mIndex);
            vd.people = 2016;
            vd.status = VideoData.STATUS_NOTSTART;
            vd.url = "";
            mDatas.add(0, vd);
        }

        @Override
        public void onStopRefresh() {
            mRecylerView.getAdapter().notifyDataSetChanged();
            // TODO: 数据刷新完成
        }
    };

    // 加载视图构造器
    private Creator mLoadViewCreator = new Creator() {
        private View mLoadView = null;

        @Override
        public View getView(Context context, WrapRecyclerView parent) {
            mLoadView = LayoutInflater.from(getContext()).inflate(R.layout.footer_load, parent, false);
            return mLoadView;
        }

        @Override
        public void onPull(int viewLength, int dragDistance, int status) {
            ((WrapRecyclerViewAdapter)mRecylerView.getAdapter()).addFooterView(WrapRecyclerView.KEY_FOOT_VIEW, mLoadView);
            // TODO: 上拉加载数据
        }

        @Override
        public void onPullAbort() {
            mRecylerView.stopFootRefresh();
        }

        @Override
        public void onRefreshing() {
            // TODO: 加载数据数据
            mRecylerView.stopFootRefresh();
        }

        @Override
        public void onStopRefresh() {
            ((WrapRecyclerViewAdapter)mRecylerView.getAdapter()).removeFooterView(WrapRecyclerView.KEY_FOOT_VIEW);
            mRecylerView.getAdapter().notifyDataSetChanged();
            // TODO: 加载数据完成
        }
    };

    // 更新数据项
    private void updateItemData(QuickViewHolder holder, final VideoData data) {
        // TODO:点击数据项
        OnClickListener onClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), data.describe, Toast.LENGTH_SHORT).show();
            }
        };
        ((TextView)holder.getView(R.id.textview_describe)).setText(data.describe);
        ((TextView)holder.getView(R.id.textview_date)).setText(data.date);
        ((TextView)holder.getView(R.id.textview_num)).setText(String.valueOf(data.people));
        holder.setOnClickListener(onClick);
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
