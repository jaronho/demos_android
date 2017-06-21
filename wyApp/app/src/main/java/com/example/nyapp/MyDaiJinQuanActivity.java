package com.example.nyapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.adapter.MyCouponAdapter;
import com.example.classes.CouponListBean;
import com.example.classes.User;
import com.example.classes.VoucherBean;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyDaiJinQuanActivity extends BaseActivity {
    @BindView(R.id.tab_coupon)
    TabLayout mTabCoupon;
    @BindView(R.id.rcy_my_coupon)
    RecyclerView mRcyMyCoupon;
    private User mUser;
    private List<CouponListBean> mCouponList;
    private View mEmptyView;
    private MyCouponAdapter mMyCouponAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_coupon);
        ButterKnife.bind(this);
        ACache cache = ACache.get(this);
        mUser = (User) cache.getAsObject("user");
        initView();
    }

    @Override
    public void initView() {
        mTabCoupon.addTab(mTabCoupon.newTab().setText("未使用"));
        mTabCoupon.addTab(mTabCoupon.newTab().setText("已使用"));
        mTabCoupon.addTab(mTabCoupon.newTab().setText("已过期"));

        mTabCoupon.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                getCouponData(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (mCouponList == null) {
            mCouponList = new ArrayList<>();
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
        }

        mRcyMyCoupon.setLayoutManager(new LinearLayoutManager(this));
        mMyCouponAdapter = new MyCouponAdapter(mCouponList,0);
        mMyCouponAdapter.setEmptyView(mEmptyView);
        mRcyMyCoupon.setAdapter(mMyCouponAdapter);
        mRcyMyCoupon.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_couponDetail:
                        mCouponList.get(position).setShowRemark(!mCouponList.get(position).isShowRemark());
                        mMyCouponAdapter.setNewData(mCouponList);
                        break;
                }
            }
        });

        getCouponData(0);
    }

    //获取代金券数据
    private void getCouponData(final int position) {
        MyProgressDialog.show(this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("tab", String.valueOf(position + 1));
        MyOkHttpUtils
                .getData(UrlContact.URL_MY_COUPON, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        if (response != null) {
                            Gson gson = new Gson();
                            VoucherBean voucherBean = gson.fromJson(response, VoucherBean.class);
                            if (voucherBean.isResult()) {
                                VoucherBean.DataBean voucherBeanData = voucherBean.getData();
                                if (voucherBeanData != null) {
                                    mCouponList = voucherBeanData.getCouponList();
                                    if (mCouponList != null) {
                                        mMyCouponAdapter.setCouponState(position);
                                        setCouponData();
                                    }
                                }
                            } else {
                                MyToastUtil.showShortMessage(voucherBean.getMessage());
                            }
                        }
                    }
                });
    }

    //设置代金券数据
    private void setCouponData() {
        mMyCouponAdapter.setNewData(mCouponList);
        if (mCouponList.size() > 0) {
            mRcyMyCoupon.smoothScrollToPosition(0);
        }
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
