package com.tencent.qcloud.xiaozhibo.mainui.list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.linkmic.TCLinkMicLivePlayActivity;
import com.tencent.qcloud.xiaozhibo.play.TCLivePlayerActivity;

import java.util.ArrayList;


/**
 * 直播列表页面，展示当前直播、回放、UGC视频
 * 界面展示使用：GridView+SwipeRefreshLayout
 * 列表数据Adapter：TCLiveListAdapter, TCUGCVideoListAdapter
 * 数据获取接口： TCLiveListMgr
 */
public class TCLiveListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    public static final int START_LIVE_PLAY = 100;
    private static final String TAG = "TCLiveListFragment";
    private GridView mVideoListView;
    private TCLiveListAdapter mVideoListViewAdapter;
    private TCUGCVideoListAdapter mUGCListViewAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //避免连击
    private long mLastClickTime = 0;

    private TextView mLiveText;
    private TextView mVodText;
    private TextView mUGCText;
    private ImageView mLiveImage;
    private ImageView mVodImage;
    private ImageView mUGCImage;
    private int mDataType = TCLiveListMgr.LIST_TYPE_VOD;
    private boolean mLiveListFetched = false;
    private boolean mUGCListFetched = false;
    View mEmptyView;

    public TCLiveListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videolist, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mVideoListView = (GridView) view.findViewById(R.id.live_list);
        mVideoListViewAdapter = new TCLiveListAdapter(getActivity(), (ArrayList<TCLiveInfo>) TCLiveListMgr.getInstance().getDataList(mDataType).clone());
        mVideoListView.setAdapter(mVideoListViewAdapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i >= mVideoListViewAdapter.getCount()) {
                        return;
                    }
                    if (0 == mLastClickTime || System.currentTimeMillis() - mLastClickTime > 1000) {
                        TCLiveInfo item;
                        if (mDataType == TCLiveListMgr.LIST_TYPE_UGC) {
                            item = mUGCListViewAdapter.getItem(i);
                        } else {
                            item = mVideoListViewAdapter.getItem(i);

                        }
                        if (item == null) {
                            Log.e(TAG, "live list item is null at position:" + i);
                            return;
                        }

                        startLivePlay(item);
                    }
                    mLastClickTime = System.currentTimeMillis();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mEmptyView = view.findViewById(R.id.tv_listview_empty);
        mEmptyView.setVisibility(mVideoListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);

        mLiveText = (TextView) view.findViewById(R.id.text_live);
        mVodText = (TextView) view.findViewById(R.id.text_vod);
        mUGCText = (TextView) view.findViewById(R.id.text_ugc);

        mLiveImage = (ImageView) view.findViewById(R.id.image_live);
        mVodImage = (ImageView) view.findViewById(R.id.image_vod);
        mUGCImage = (ImageView) view.findViewById(R.id.image_ugc);

        mLiveText.setOnClickListener(this);
        mVodText.setOnClickListener(this);
        mUGCText.setOnClickListener(this);

        refreshListView();

        return view;
    }

    @Override
    public void onRefresh() {
        refreshListView();
    }

    /**
     * 刷新直播列表
     */
    private void refreshListView() {
        if (reloadLiveList()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        try {
            if (START_LIVE_PLAY == requestCode) {
                if (0 != resultCode) {
                    //观看直播返回错误信息后，刷新列表，但是不显示动画
                    reloadLiveList();
                } else {
                    if (data == null) {
                        return;
                    }
                    //更新列表项的观看人数和点赞数
                    String userId = data.getStringExtra(TCConstants.PUSHER_ID);
                    for (int i = 0; i < mVideoListViewAdapter.getCount(); i++) {
                        TCLiveInfo info = mVideoListViewAdapter.getItem(i);
                        if (info != null && info.userid.equalsIgnoreCase(userId)) {
                            info.viewercount = (int) data.getLongExtra(TCConstants.MEMBER_COUNT, info.viewercount);
                            info.likecount = (int) data.getLongExtra(TCConstants.HEART_COUNT, info.likecount);
                            mVideoListViewAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载直播列表
     */
    private boolean reloadLiveList() {
        switch (mDataType) {
            case TCLiveListMgr.LIST_TYPE_LIVE:
                return TCLiveListMgr.getInstance().reloadLiveList(mDataType, mLiveListener);
            case TCLiveListMgr.LIST_TYPE_VOD:
                return TCLiveListMgr.getInstance().reloadLiveList(mDataType, mVodListener);
            case TCLiveListMgr.LIST_TYPE_UGC:
                return TCLiveListMgr.getInstance().reloadLiveList(mDataType, mUGCListener);
        }
        return false;
    }

    void onGetListData(int retCode, ArrayList<TCLiveInfo> result, boolean refresh) {
        if (retCode == 0 ) {
            mVideoListViewAdapter.clear();
            if (result != null) {
                mVideoListViewAdapter.addAll((ArrayList<TCLiveInfo>)result.clone());
            }
            if (refresh) {
                mVideoListViewAdapter.notifyDataSetChanged();
            }
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "刷新列表失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    TCLiveListMgr.Listener mLiveListener = new TCLiveListMgr.Listener() {

        @Override
        public void onLiveList(int retCode, ArrayList<TCLiveInfo> result, boolean refresh) {
            if (mDataType == TCLiveListMgr.LIST_TYPE_LIVE) {
                onGetListData(retCode, result, refresh);
                mEmptyView.setVisibility(mVideoListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    TCLiveListMgr.Listener mVodListener = new TCLiveListMgr.Listener() {

        @Override
        public void onLiveList(int retCode, ArrayList<TCLiveInfo> result, boolean refresh) {
            if (mDataType == TCLiveListMgr.LIST_TYPE_VOD) {
                onGetListData(retCode, result, refresh);
                mEmptyView.setVisibility(mVideoListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    TCLiveListMgr.Listener mUGCListener = new TCLiveListMgr.Listener() {

        @Override
        public void onLiveList(int retCode, ArrayList<TCLiveInfo> result, boolean refresh) {
            if (mDataType == TCLiveListMgr.LIST_TYPE_UGC) {
                if (retCode == 0) {

                    mUGCListViewAdapter.clear();
                    if (result != null) {
                        mUGCListViewAdapter.addAll((ArrayList<TCLiveInfo>) result.clone());
                    }
                    if (refresh) {
                        mUGCListViewAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "刷新列表失败", Toast.LENGTH_LONG).show();
                    }
                }
                mEmptyView.setVisibility(mUGCListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    /**
     * 开始播放视频
     * @param item 视频数据
     */
    private void startLivePlay(final TCLiveInfo item) {
        Intent intent;
        if (TCConstants.TX_ENABLE_LINK_MIC) {
            intent = new Intent(getActivity(), TCLinkMicLivePlayActivity.class);
        }
        else {
            intent = new Intent(getActivity(), TCLivePlayerActivity.class);
        }
        intent.putExtra(TCConstants.PUSHER_ID, item.userid);
        intent.putExtra(TCConstants.PLAY_URL, item.type == 1? item.hls_play_url: item.playurl);
        intent.putExtra(TCConstants.PUSHER_NAME, item.userinfo.nickname == null ? item.userid : item.userinfo.nickname);
        intent.putExtra(TCConstants.PUSHER_AVATAR, item.userinfo.headpic);
        intent.putExtra(TCConstants.HEART_COUNT, "" + item.likecount);
        intent.putExtra(TCConstants.MEMBER_COUNT, "" + item.viewercount);
        intent.putExtra(TCConstants.GROUP_ID, item.groupid);
        intent.putExtra(TCConstants.PLAY_TYPE, item.type);
        intent.putExtra(TCConstants.FILE_ID, item.fileid);
        intent.putExtra(TCConstants.COVER_PIC, item.userinfo.frontcover);
        intent.putExtra(TCConstants.TIMESTAMP, item.timestamp);
        intent.putExtra(TCConstants.ROOM_TITLE, item.title);
        startActivityForResult(intent,START_LIVE_PLAY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_live:
                mDataType = TCLiveListMgr.LIST_TYPE_LIVE;
                break;
            case R.id.text_vod:
                mDataType = TCLiveListMgr.LIST_TYPE_VOD;
                break;
            case R.id.text_ugc:
                mDataType = TCLiveListMgr.LIST_TYPE_UGC;
                break;
        }
        setStatus(mDataType);
    }

    private void setStatus(int dataType) {
        if (dataType == TCLiveListMgr.LIST_TYPE_LIVE) {
            mLiveText.setTextColor(Color.rgb(0, 0, 0));
            mVodText.setTextColor(Color.rgb(119, 119, 119));
            mUGCText.setTextColor(Color.rgb(119, 119, 119));
            mLiveImage.setVisibility(View.VISIBLE);
            mVodImage.setVisibility(View.INVISIBLE);
            mUGCImage.setVisibility(View.INVISIBLE);
            if (!mLiveListFetched) {
                refreshListView();
                mLiveListFetched = true;
            }
            mVideoListViewAdapter.clear();
            mVideoListViewAdapter.addAll((ArrayList<TCLiveInfo>)TCLiveListMgr.getInstance().getDataList(mDataType).clone());
            mVideoListViewAdapter.notifyDataSetChanged();
            mVideoListView.setNumColumns(1);
            mVideoListView.setHorizontalSpacing(0);
            mVideoListView.setVerticalSpacing(0);
            mVideoListView.setAdapter(mVideoListViewAdapter);
            mEmptyView.setVisibility(mVideoListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
        } else if (dataType == TCLiveListMgr.LIST_TYPE_VOD) {
            mLiveText.setTextColor(Color.rgb(119, 119, 119));
            mVodText.setTextColor(Color.rgb(0, 0, 0));
            mUGCText.setTextColor(Color.rgb(119, 119, 119));
            mLiveImage.setVisibility(View.INVISIBLE);
            mVodImage.setVisibility(View.VISIBLE);
            mUGCImage.setVisibility(View.INVISIBLE);
            mVideoListViewAdapter.clear();
            mVideoListViewAdapter.addAll((ArrayList<TCLiveInfo>)TCLiveListMgr.getInstance().getDataList(mDataType).clone());
            mVideoListViewAdapter.notifyDataSetChanged();
            mVideoListView.setNumColumns(1);
            mVideoListView.setHorizontalSpacing(0);
            mVideoListView.setVerticalSpacing(0);
            mVideoListView.setAdapter(mVideoListViewAdapter);
            mEmptyView.setVisibility(mVideoListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
        } else if (dataType == TCLiveListMgr.LIST_TYPE_UGC) {
            mLiveText.setTextColor(Color.rgb(119, 119, 119));
            mVodText.setTextColor(Color.rgb(119, 119, 119));
            mUGCText.setTextColor(Color.rgb(0, 0, 0));
            mLiveImage.setVisibility(View.INVISIBLE);
            mVodImage.setVisibility(View.INVISIBLE);
            mUGCImage.setVisibility(View.VISIBLE);
            if (mUGCListViewAdapter == null) {
                mUGCListViewAdapter = new TCUGCVideoListAdapter(getActivity(), (ArrayList<TCLiveInfo>) TCLiveListMgr.getInstance().getDataList(mDataType).clone());
            }
            mUGCListViewAdapter.clear();
            mUGCListViewAdapter.addAll((ArrayList<TCLiveInfo>)TCLiveListMgr.getInstance().getDataList(mDataType).clone());
            mUGCListViewAdapter.notifyDataSetChanged();
            if (!mUGCListFetched) {
                refreshListView();
                mUGCListFetched = true;
            }
            mVideoListView.setNumColumns(2);
            mVideoListView.setHorizontalSpacing(6);
            mVideoListView.setVerticalSpacing(6);
            mVideoListView.setAdapter(mUGCListViewAdapter);
            mEmptyView.setVisibility(mUGCListViewAdapter.getCount() == 0? View.VISIBLE: View.GONE);
        }
    }
}