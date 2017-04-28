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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.MultiLayout;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.QuickViewHolder;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private RefreshView mRefresh = null;
    private List<VideoData> mDatas = new ArrayList<>();

    public ListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRefresh = (RefreshView)view.findViewById(R.id.layout_refresh);
        mRefresh.getView().setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRefresh.getView().setLayoutManager(linearLayoutManager);
        mRefresh.getView().setAdapter(new WrapRecyclerViewAdapter<VideoData>(getContext(), mDatas, mMultiLayout) {
            @Override
            public void onBindViewHolder(QuickViewHolder holder, VideoData data) {
                updateItemData(holder, data);
            }
        });
        for (int i = 0; i < 4; ++i) {
            VideoData vd = new VideoData();
            vd.type = VideoData.TYPE_LIVE;
            vd.date = "2017-04-14 9:00-21:00";
            vd.describe = "标题: " + (i + 1);
            vd.people = 2016;
            vd.status = VideoData.STATUS_NOTSTART;
            vd.url = "";
            mDatas.add(0, vd);
        }
        mRefresh.getView().getAdapter().notifyDataSetChanged();
        mRefresh.setHeaderCreator(mRefreshCreator);
        mRefresh.setFooterCreator(mLoadCreator);
        mRefresh.getView().addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.item_space)));
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
    private RefreshView.Creator mRefreshCreator = new RefreshView.Creator() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return LayoutInflater.from(getContext()).inflate(R.layout.header_refresh, parent, false);
        }

        @Override
        public void onPull(float offset, boolean isReady) {
            Log.d("NYLive", "下拉刷新 == 下拉中: " + (isReady ? "可刷新" : "不可刷新"));
        }

        @Override
        public void onPullAbort() {
            Log.d("NYLive", "下拉刷新 == 取消刷新");
        }

        @Override
        public void onRefreshing() {
            Log.d("NYLive", "下拉刷新 == 刷新中");
            mRefresh.restore();
        }
    };

    // 加载视图构造器
    private RefreshView.Creator mLoadCreator = new RefreshView.Creator() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return LayoutInflater.from(getContext()).inflate(R.layout.footer_load, parent, false);
        }

        @Override
        public void onPull(float offset, boolean isReady) {
            Log.d("NYLive", "加载更多 == 上拉中: " + (isReady ? "可加载" : "不可加载"));
        }

        @Override
        public void onPullAbort() {
            Log.d("NYLive", "加载更多 == 取消加载");
        }

        @Override
        public void onRefreshing() {
            Log.d("NYLive", "加载更多 == 加载中");
            mRefresh.restore();
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
