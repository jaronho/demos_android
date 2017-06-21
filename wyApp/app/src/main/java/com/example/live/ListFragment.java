package com.example.live;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nyapp.R;
import com.jaronho.sdk.third.circledialog.CircleDialog;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.QuickViewHolder;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    public static final int TYPE_LIVE = 0;  // 直播
    public static final int TYPE_VIDEO = 1; // 视频
    public static final int TYPE_MY_JOIN = 2;  // 我参与的
    public static final int TYPE_MY_COLLECT = 3;    // 我收藏的
    public static final int TYPE_MY_START = 4;  // 我发起的
    private Context mContext = null;
    private RefreshView mRefresh = null;
    private List<ChannelInfo> mDatas = new ArrayList<>();
    private int mType = TYPE_LIVE;
    private boolean mIsCreated = false;
    private boolean mCanInitList = false;
    private boolean mIsListInited = false;
    private int mPages = 0;
    private int mRecords = 0;
    private int mCurrentPage = 1;
    private LiveHelper.Callback mCallback = new LiveHelper.Callback<ChannelInfo>() {
        @Override
        public void onData(ChannelInfo data) {
        }
        @Override
        public void onDataList(List<ChannelInfo> dataList) {
            for (int i = 0, len = dataList.size(); i < len; ++i) {
                mDatas.add(dataList.get(i));
            }
            mRefresh.getView().getAdapter().notifyDataSetChanged();
            mPages = LiveHelper.getTempPages();
            mRecords = LiveHelper.getTempRecords();
            Log.d("NYLive", "get list, pages: " + mPages + ", records: " + mRecords);
        }
    };

    public ListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.live_list_fragment, container, false);
        mRefresh = (RefreshView)view.findViewById(R.id.layout_refresh);
        mRefresh.setHorizontal(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefresh.getView().setVerticalScrollBarEnabled(true);
        mRefresh.getView().setLayoutManager(linearLayoutManager);
        mRefresh.getView().setHasFixedSize(true);
        mRefresh.getView().setAdapter(new WrapRecyclerViewAdapter<ChannelInfo>(getContext(), mDatas, R.layout.live_item) {
            @Override
            public void onBindViewHolder(QuickViewHolder holder, ChannelInfo data) {
                updateItemData(holder, data);
            }
        });
        mRefresh.setHeaderWrapper(mRefreshWrapper);
		mRefresh.setFooterWrapper(mLoadWrapper);
        mRefresh.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.live_video_item_space)));
        mIsCreated = true;
        onInitList();
        return view;
    }

    // 刷新包装器
    private RefreshView.Wrapper mRefreshWrapper = new RefreshView.Wrapper() {
        private ViewGroup mHeadView;
        private TextView mTipsTextView;
        @Override
        public View createView(Context context, RelativeLayout parent) {
            mHeadView = (ViewGroup)LayoutInflater.from(getContext()).inflate(R.layout.live_refresh_header, parent, false);
            mTipsTextView = (TextView)mHeadView.findViewById(R.id.textview_tips);
            return mHeadView;
        }
		@Override
		public void onPullReady() {
		}
        @Override
        public void onPull(float maxOffset, float offset, boolean isForward, boolean isReady) {
            if (isReady) {
                mTipsTextView.setText("松开刷新");
            } else {
                mTipsTextView.setText("下拉刷新");
            }
        }
        @Override
        public void onPullAbort() {
        }
        @Override
        public void onRefreshing() {
            mRefresh.restore();
            mDatas.clear();
            mCurrentPage = 1;
            refreshList();
        }
    };
	
	// 加载包装器
    private RefreshView.Wrapper mLoadWrapper = new RefreshView.Wrapper() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return null;
        }
		@Override
		public void onPullReady() {
			if (mCurrentPage < mPages) {
				++mCurrentPage;
				refreshList();
			} else {
				mCurrentPage = mPages;
			}
		}
        @Override
        public void onPull(float maxOffset, float offset, boolean isForward, boolean isReady) {
        }
        @Override
        public void onPullAbort() {
        }
        @Override
        public void onRefreshing() {
            mRefresh.restore();
        }
    };

    // 更新数据项
    private void updateItemData(QuickViewHolder holder, final ChannelInfo data) {
        // 点击数据项
        OnClickListener onClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveHelper.reqGetChannelView(data.Id, new LiveHelper.Callback<ChannelInfo>() {
                    @Override
                    public void onData(final ChannelInfo data) {
                        LiveHelper.reqGetAuthority(LiveHelper.getUserId(), data.Id, true, new LiveHelper.Callback<AuthorityInfo>() {
                            @Override
                            public void onData(AuthorityInfo ai) {
                                if (ai.IsIn) {  // 被踢出,无法进入
                                    LiveHelper.toast("您没有权限进入直播房间");
                                    return;
                                }
                                if (1 == data.TYPE) {   // 如果不是直播,进入观看录像页面
                                    Log.d("NYLive", "不是直播,进入观看录像页面");
                                    Intent intent = new Intent(mContext, VideoActivity.class);
                                    intent.putExtra("channel_info", data);
                                    startActivity(intent);
                                    return;
                                }
                                if (2 == data.STATUS) { // 直播已结束,进入观看录像页面
                                    Log.d("NYLive", "直播已结束,进入观看录像页面");
                                    Intent intent = new Intent(mContext, VideoActivity.class);
                                    intent.putExtra("channel_info", data);
                                    startActivity(intent);
                                    return;
                                }
                                if (LiveHelper.getUserId().equals(data.PublisherID)) {   // 如果自己创建的直播（主播）
                                    if (0 == data.STATUS) { // 未开始，是否现在创建直播
                                        new CircleDialog.Builder(getActivity())
                                                .setText("是否创建直播？")
                                                .setPositive("确定", new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(mContext, HostLiveActivity.class);
                                                        intent.putExtra("channel_info", data);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegative("取消", null)
                                                .setCancelable(true)
                                                .show();
                                    } else if (1 == data.STATUS) {  // 直播中，延续直播
                                        Log.d("NYLive", "直播中，延续直播");
                                        Intent intent = new Intent(mContext, HostLiveActivity.class);
                                        intent.putExtra("channel_info", data);
                                        startActivity(intent);
                                    }
                                } else {    // 非自己创建的直播（观众）
                                    if (0 == data.STATUS) { // 未开始，暂时不能观看
                                        LiveHelper.toast("直播未开始，暂时不能观看");
                                    } else if (1 == data.STATUS) {  // 直播中，进入直播
                                        Log.d("NYLive", "直播中，进入直播");
                                        Intent intent = new Intent(mContext, GuestLiveActivity.class);
                                        intent.putExtra("channel_info", data);
                                        startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onDataList(List<AuthorityInfo> dataList) {
                            }
                        });
                    }
                    @Override
                    public void onDataList(List<ChannelInfo> dataList) {
                    }
                });
            }
        };
        holder.setOnClickListener(onClick);
        // 界面内容
        ImageView imageViewSnapshoot = holder.getView(R.id.imageview_snapshoot);
        if (null != data.MainMap && !data.MainMap.isEmpty()) {
            Picasso.with(getContext()).load(data.MainMap).into(imageViewSnapshoot);
        }
        ((TextView)holder.getView(R.id.textview_title)).setText(data.Title);
        ((TextView)holder.getView(R.id.textview_watch)).setText(String.valueOf(data.WatchCount));
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy年MM月dd日HH:mm");
        if (0 == data.TYPE) {   // 直播
            String startTimeStr = dateFormat.format(data.StartTime);
            String statusStr = "";
            if (0 == data.STATUS) {
                statusStr = "未开始";
            } else if (1 == data.STATUS) {
                statusStr = "已开始";
            } else if (2 == data.STATUS) {
                statusStr = "已结束";
            }
            ((TextView)holder.getView(R.id.textview_date)).setText(startTimeStr + "直播(" + statusStr + ")");
            ((TextView)holder.getView(R.id.textview_type)).setText("进入直播");
        } else {    // 视频
            String createTimeStr = dateFormat.format(data.CreatedDate);
            ((TextView)holder.getView(R.id.textview_date)).setText(createTimeStr);
            ((TextView)holder.getView(R.id.textview_type)).setText("观看视频");
        }
    }

    // 初始列表
    private void onInitList() {
        if (!mCanInitList || !mIsCreated || mIsListInited) {
            return;
        }
        mCanInitList = false;
        mIsListInited = true;
        mDatas.clear();
        mCurrentPage = 1;
        refreshList();
    }

    // 刷新列表
    private void refreshList() {
        int pageSize = 10;
        switch (mType) {
            case TYPE_LIVE:
                LiveHelper.reqGetLiveList(mCurrentPage, pageSize, mCallback);
                break;
            case TYPE_VIDEO:
                LiveHelper.reqGetVideoList(mCurrentPage, pageSize, mCallback);
                break;
            case TYPE_MY_JOIN:
                LiveHelper.reqGetParticipateList(LiveHelper.getUserId(), mCurrentPage, pageSize, mCallback);
                break;
            case TYPE_MY_COLLECT:
                LiveHelper.reqGetCollectionList(LiveHelper.getUserId(), mCurrentPage, pageSize, mCallback);
                break;
            case TYPE_MY_START:
                LiveHelper.reqGetStartedList(LiveHelper.getUserId(), mCurrentPage, pageSize, mCallback);
                break;
        }
    }

    // 设置可以初始列表
    public void setCanInitList() {
        mCanInitList = true;
        onInitList();
    }

    // 创建
    public static ListFragment create(int type) {
        ListFragment lf = new ListFragment();
        lf.mType = type;
        return lf;
    }
}
