package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.adapter.MyOrderAdapter;
import com.example.classes.BaseBean;
import com.example.classes.MyOrderBean;
import com.example.classes.User;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyDingDanActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.layout_back_top)
    LinearLayout mLayoutBackTop;
    @BindView(R.id.text_title)
    TextView mTextTitle;

    private String url;
    private User user;
    private List<MyOrderBean.DataBean.OrdersBean> groupOrders;
    DecimalFormat ddf1 = new DecimalFormat("#.00");
    private int loading = 0;
    private int type;
    private MyOrderAdapter mOrderAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mState;
    private int mIndex;
    private int mPageSize;
    private int mPosition = 0;
    private MyMsgDialog mOrderBtnDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mydingdan);
        ButterKnife.bind(this);

        ACache cache = ACache.get(this);
        user = (User) cache.getAsObject("user");

        type = getIntent().getIntExtra("type", 0);
        String title = getIntent().getStringExtra("title");
        mTextTitle.setText(title);

        mState = getIntent().getIntExtra("state", 0);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSwipeToLoad();
    }

    @OnClick({R.id.layout_back, R.id.img_back_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                mSwipeTarget.smoothScrollToPosition(0);
                startMainActivity();
                break;
            case R.id.img_back_top:
                mSwipeTarget.smoothScrollToPosition(0);
                mLayoutBackTop.setVisibility(View.GONE);
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(MyDingDanActivity.this, MainActivity.class);
        startActivity(intent);
//		new MainActivity().showFragment(3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startMainActivity();
        }
        return true;
    }

    @Override
    public void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(MyDingDanActivity.this);
        mSwipeToLoadLayout.setOnLoadMoreListener(MyDingDanActivity.this);

        mSwipeTarget.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mLayoutManager.findFirstVisibleItemPosition() > 1) {
                    mLayoutBackTop.setVisibility(View.VISIBLE);
                } else {
                    mLayoutBackTop.setVisibility(View.GONE);
                }
            }
        });

        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });

    }

    private void getOrderUrl() {

    }

    @Override
    public void onRefresh() {
        mPosition = 0;
        loading = 0;
        mIndex = 1;
        mPageSize = 100;
        initData();
    }

    @Override
    public void onLoadMore() {
        if (groupOrders == null || groupOrders.size() == 0) {
            stopSwipeToLoad();
            Toast.makeText(MyDingDanActivity.this, "已经到底了", Toast.LENGTH_LONG).show();
        } else {
            mPosition = groupOrders.size() - 1;
            loading = 1;
            mIndex = mIndex + 1;
            mPageSize = 20;
            initData();
        }
    }

    //订单button弹窗
    public void showDialog(String text, MyOrderBean.DataBean.OrdersBean.OrderItemsBean orderItemsBean) {
        final String state = orderItemsBean.getState();
        final int id = orderItemsBean.getId();
        mOrderBtnDialog = new MyMsgDialog(this, false, "系统提示", text,
                new MyMsgDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mOrderBtnDialog.dismiss();
                        int action;
                        if (state.equals("待改运费") || state.equals("待支付")) {
                            action = 2;
                        } else {
                            if (state.equals("已发货")) {
                                action = 1;
                            } else {
                                action = 3;
                            }
                        }
                        getOrderProcessingResults(id, action);
                    }
                }, new MyMsgDialog.CancelListener() {
            @Override
            public void onClick() {
                mOrderBtnDialog.dismiss();
            }
        });
        mOrderBtnDialog.setCancelable(false);
        mOrderBtnDialog.show();
    }

    //订单处理结果
    private void getOrderProcessingResults(int id, int action) {
        MyProgressDialog.show(MyDingDanActivity.this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", user.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("orderId", String.valueOf(id));
        map.put("orderAction", String.valueOf(action));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ACTION, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        MyToastUtil.showShortMessage(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        if (response != null) {
                            Gson gson = new Gson();
                            BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                            if (baseBean != null) {
                                if (baseBean.isResult()) {
                                    MyToastUtil.showShortMessage(baseBean.getMessage());
                                    mSwipeToLoadLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSwipeToLoadLayout.setRefreshing(true);
                                        }
                                    });
                                } else {
                                    MyToastUtil.showLongMessage(baseBean.getMessage());
                                }
                            }
                        }
                    }
                });
    }

    private void initData() {
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", user.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("PageSize", String.valueOf(mPageSize));
        map.put("type", String.valueOf(type));
        map.put("PageIndex", String.valueOf(mIndex));
        if (mState != 0) {
            map.put("State", String.valueOf(mState));
        }
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        stopSwipeToLoad();
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        stopSwipeToLoad();
                        Gson gson = new Gson();
                        try {
                            MyOrderBean myOrderBean = gson.fromJson(response, MyOrderBean.class);
                            if (myOrderBean.isResult()) {
                                MyOrderBean.DataBean dataBean = myOrderBean.getData();
                                List<MyOrderBean.DataBean.OrdersBean> orders = dataBean.getOrders();

                                if (loading == 0) {
                                    groupOrders = orders;
                                    if (mOrderAdapter == null) {
                                        mOrderAdapter = new MyOrderAdapter(MyDingDanActivity.this, type, user, groupOrders);
                                        mLayoutManager = new LinearLayoutManager(MyDingDanActivity.this);
                                        mSwipeTarget.setLayoutManager(mLayoutManager);
                                        mSwipeTarget.setAdapter(mOrderAdapter);

                                        View view = LayoutInflater.from(MyDingDanActivity.this).inflate(R.layout.view_empty, null);
                                        mOrderAdapter.setEmptyView(view);
                                    } else {
                                        mOrderAdapter.setNewData(groupOrders);
                                    }

                                } else {
                                    if (orders == null || orders.size() == 0) {
                                        mIndex = mIndex - 1;
                                        Toast.makeText(MyDingDanActivity.this, "已经到底了", Toast.LENGTH_LONG).show();
                                    } else {
                                        mOrderAdapter.addData(mPosition, orders);
                                        mOrderAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (orders != null && orders.size() > 0) {
                                    mSwipeTarget.smoothScrollToPosition(mPosition);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
