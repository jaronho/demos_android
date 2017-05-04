package com.example.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classes.VideoInfo;
import com.example.nyapp.R;
import com.example.view.SpaceItemDecoration;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.MultiLayout;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.QuickViewHolder;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_LiveList extends Fragment {
    private static final String TAG = Fragment_LiveList.class.getSimpleName();
    private RefreshView mRefresh = null;
    private List<VideoInfo> mDatas = new ArrayList<>();

    public Fragment_LiveList() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_list, container, false);

        mRefresh = (RefreshView)view.findViewById(R.id.layout_refresh);
        mRefresh.setHorizontal(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefresh.getView().setLayoutManager(linearLayoutManager);
        mRefresh.getView().setHasFixedSize(true);
        mRefresh.getView().setAdapter(new WrapRecyclerViewAdapter<VideoInfo>(getContext(), mDatas, mMultiLayout) {
            @Override
            public void onBindViewHolder(QuickViewHolder holder, VideoInfo data) {
                updateItemData(holder, data);
            }
        });
        mRefresh.setHeaderWrapper(mRefreshWrapper);
        mRefresh.setFooterWrapper(mLoadWrapper);
        mRefresh.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.video_item_space)));
        return view;
    }

    // 视图数据项
    private MultiLayout<VideoInfo> mMultiLayout = new MultiLayout<VideoInfo>() {
        @Override
        public int getLayoutId(int position, VideoInfo data) {
            if (VideoInfo.TYPE_LIVE == data.type) {
                return R.layout.item_live;
            } else if (VideoInfo.TYPE_VIDEO == data.type) {
                return R.layout.item_video;
            }
            return 0;
        }
    };

    // 刷新包装器
    private RefreshView.Wrapper mRefreshWrapper = new RefreshView.Wrapper() {
        private LinearLayout mHeadView;
        private ImageView mArrowImageView;
        private ProgressBar mProgressBar;
        private TextView mTipsTextView;
        private TextView mLastUpdatedTextView;
        @Override
        public View createView(Context context, RelativeLayout parent) {
            mHeadView = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.head, parent, false);
            mArrowImageView = (ImageView)mHeadView.findViewById(R.id.head_arrowImageView);
            mArrowImageView.setMinimumWidth(70);
            mArrowImageView.setMinimumHeight(50);
            mProgressBar = (ProgressBar)mHeadView.findViewById(R.id.head_progressBar);
            mTipsTextView = (TextView)mHeadView.findViewById(R.id.head_tipsTextView);
            mLastUpdatedTextView = (TextView)mHeadView.findViewById(R.id.head_lastUpdatedTextView);
            return mHeadView;
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

    // 加载包装器
    private RefreshView.Wrapper mLoadWrapper = new RefreshView.Wrapper() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return LayoutInflater.from(getContext()).inflate(R.layout.list_footer_more, parent, false);
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
    private void updateItemData(QuickViewHolder holder, final VideoInfo data) {
        // TODO:点击数据项
        OnClickListener onClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
        holder.setOnClickListener(onClick);
    }
}
