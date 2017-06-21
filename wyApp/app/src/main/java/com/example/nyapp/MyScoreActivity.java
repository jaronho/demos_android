package com.example.nyapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.adapter.MyScoreListAdapter;
import com.example.classes.ScoreBean;
import com.example.classes.User;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.example.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyScoreActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "MyScoreActivity";
    @BindView(R.id.tv_total_score)
    TextView mTvTotalScore;
    @BindView(R.id.swipe_target)
    RecyclerView mRcyScore;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private User mUser;
    private MyScoreListAdapter mScoreListAdapter;
    private int mIndex;
    private int mPageSize;
    private int mState;
    private int mPosition = 0;
    private List<ScoreBean.DataBean.PointsRecordModelListBean> mPointsRecordModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSwipeToLoad();
    }

    @Override
    public void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(MyScoreActivity.this);
        mSwipeToLoadLayout.setOnLoadMoreListener(MyScoreActivity.this);
    }

    private void initData() {
        ACache cache = ACache.get(this);
        mUser = (User) cache.getAsObject("user");
        mTvTotalScore.setText(String.valueOf(0));

        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh() {
        mPosition = 0;
        mState = 0;
        mIndex = 1;
        mPageSize = 20;
        getScoreList();
    }

    @Override
    public void onLoadMore() {
        if (mPointsRecordModelList == null || mPointsRecordModelList.size() == 0) {
            stopSwipeToLoad();
            Toast.makeText(MyScoreActivity.this, "已经到底了", Toast.LENGTH_LONG).show();
        } else {
            mPosition = mPointsRecordModelList.size() - 1;
            mState = 1;
            mIndex = mIndex + 1;
            getScoreList();
        }

    }

    /**
     * 获取积分记录表数据
     */
    private void getScoreList() {
        Map<String, String> map = new HashMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("CurrentIndex", String.valueOf(mIndex));
        map.put("PageSize", String.valueOf(mPageSize));

        MyOkHttpUtils.getData(UrlContact.URL_SCORE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                        stopSwipeToLoad();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        stopSwipeToLoad();
                        Gson gson = new Gson();
                        ScoreBean scoreBean = gson.fromJson(response, ScoreBean.class);
                        if (scoreBean.isResult()) {
                            ScoreBean.DataBean data = scoreBean.getData();
                            List<ScoreBean.DataBean.PointsRecordModelListBean> dataList = data.getPointsRecordModelList();
                            mTvTotalScore.setText(String.valueOf(data.getSumPoints()));

                            if (mState == 0) {
                                mPointsRecordModelList = dataList;
                                mScoreListAdapter = new MyScoreListAdapter(mPointsRecordModelList, MyScoreActivity.this);

                                mRcyScore.setLayoutManager(new LinearLayoutManager(MyScoreActivity.this));

                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyScoreActivity.this, DividerItemDecoration.VERTICAL_LIST);
                                dividerItemDecoration.setDivider(R.drawable.divider_bg);
                                mRcyScore.addItemDecoration(dividerItemDecoration);

                                mRcyScore.setAdapter(mScoreListAdapter);
                            } else {
                                if (dataList == null || dataList.size() == 0) {
                                    mIndex = mIndex - 1;
                                    Toast.makeText(MyScoreActivity.this, "已经到底了", Toast.LENGTH_LONG).show();
                                } else {
                                    mScoreListAdapter.addData(dataList);
                                    mScoreListAdapter.notifyDataSetChanged();
                                }
                            }
                            mRcyScore.smoothScrollToPosition(mPosition);
                        }
                    }
                });
    }

    //隐藏刷新、加载布局
    private void stopSwipeToLoad() {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
}
