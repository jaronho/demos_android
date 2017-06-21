package com.example.live;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.live.im.ui.ChatActivity;
import com.example.nyapp.R;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;

import java.util.ArrayList;
import java.util.List;

public class LiveListChatRoomActivity extends AppCompatActivity {
    private RefreshView mRefresh = null;
    private List<ChannelInfo> mDatas = new ArrayList<>();
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
            Log.d("NYLive", "get chat room list, pages: " + mPages + ", records: " + mRecords);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_list_chatroom_activity);

        ImageView imageviewBack = (ImageView)findViewById(R.id.imageview_back);
        imageviewBack.setOnClickListener(onClickImageviewBack);

        mRefresh = (RefreshView)findViewById(R.id.layout_refresh);
        mRefresh.setHorizontal(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefresh.getView().setVerticalScrollBarEnabled(true);
        mRefresh.getView().setLayoutManager(linearLayoutManager);
        mRefresh.getView().setHasFixedSize(true);
        mRefresh.getView().setAdapter(new WrapRecyclerViewAdapter<ChannelInfo>(this, mDatas, R.layout.live_item) {
            @Override
            public void onBindViewHolder(QuickViewHolder holder, ChannelInfo data) {
                updateItemData(holder, data);
            }
        });
        mRefresh.setHeaderWrapper(mRefreshWrapper);
        mRefresh.setFooterWrapper(mLoadWrapper);
        mRefresh.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.live_video_item_space)));
        onInitList();
    }

    // 刷新包装器
    private RefreshView.Wrapper mRefreshWrapper = new RefreshView.Wrapper() {
        private ViewGroup mHeadView;
        private TextView mTipsTextView;
        @Override
        public View createView(Context context, RelativeLayout parent) {
            mHeadView = (ViewGroup) LayoutInflater.from(LiveListChatRoomActivity.this).inflate(R.layout.live_refresh_header, parent, false);
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
    private void updateItemData(QuickRecyclerViewAdapter.QuickViewHolder holder, final ChannelInfo data) {
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
                                    LiveHelper.toast("您没有权限进入聊天室");
                                    return;
                                }
                                Log.d("NYLive", "channel info: " + data.toString());
                                if (LiveHelper.getUserId().equals(data.PublisherID)) {    // 群主
                                    ChatActivity.navToChat(LiveListChatRoomActivity.this, data.VideoSource, TIMConversationType.Group, data);
                                } else {
                                    LiveHelper.reqChannelUserAdd(data.Id, LiveHelper.getUserId());
                                    TIMGroupManager.getInstance().applyJoinGroup(data.VideoSource, "", new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {
                                            Log.d("NYLive", "join group error, i: " + i + ", s: " + s);
                                            if (10013 == i) {
                                                ChatActivity.navToChat(LiveListChatRoomActivity.this, data.VideoSource, TIMConversationType.Group, data);
                                            } else {
                                                LiveHelper.toast("申请加入群失败, " + i + " " + s);
                                            }
                                        }
                                        @Override
                                        public void onSuccess() {
                                            Log.d("NYLive", "join group success");
                                            ChatActivity.navToChat(LiveListChatRoomActivity.this, data.VideoSource, TIMConversationType.Group, data);
                                        }
                                    });
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
        holder.getView(R.id.view_describe_bg).setVisibility(View.INVISIBLE);
        holder.getView(R.id.textview_date).setVisibility(View.INVISIBLE);
        ImageView imageViewSnapshoot = holder.getView(R.id.imageview_snapshoot);
        if (null != data.MainMap && !data.MainMap.isEmpty()) {
            Picasso.with(this).load(data.MainMap).into(imageViewSnapshoot);
        }
        ((TextView)holder.getView(R.id.textview_title)).setText(data.Title);
        ((TextView)holder.getView(R.id.textview_watch)).setText(String.valueOf(data.WatchCount));
	    ((TextView)holder.getView(R.id.textview_type)).setText("进入");
    }

    // 初始列表
    private void onInitList() {
        mDatas.clear();
        mCurrentPage = 1;
        refreshList();
    }

    // 刷新列表
    private void refreshList() {
        int pageSize = 10;
        LiveHelper.reqGetChatroomList(mCurrentPage, pageSize, mCallback);
    }

    OnClickListener onClickImageviewBack = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
