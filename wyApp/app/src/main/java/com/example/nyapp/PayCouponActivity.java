package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.adapter.MyCouponAdapter;
import com.example.classes.BaseBean;
import com.example.classes.CouponListBean;
import com.example.classes.PayCouponBean;
import com.example.util.GsonUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PayCouponActivity extends BaseActivity {
    @BindView(R.id.rcy_payCoupon)
    RecyclerView mRcyPayCoupon;
    private List<CouponListBean> mPayCouponList;
    private int mPayPosition;
    private int mOrderId;
    private View mEmptyView;
    private MyCouponAdapter mMyCouponAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_coupon);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPayPosition = intent.getIntExtra("position", 0);
        mOrderId = intent.getIntExtra("order", 0);

        initView();
        initData();
    }

    private void initData() {
        MyProgressDialog.show(this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("orderId", String.valueOf(mOrderId));
        MyOkHttpUtils
                .getData(UrlContact.URL_PAY_COUPON, map)
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
                            PayCouponBean payCouponBean = GsonUtils.getInstance().fromJson(response, PayCouponBean.class);
                            if (payCouponBean.isResult()) {
                                mPayCouponList = payCouponBean.getData();
                                mMyCouponAdapter.setNewData(mPayCouponList);
                            } else {
                                MyToastUtil.showShortMessage(payCouponBean.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void initView() {
        if (mPayCouponList == null) {
            mPayCouponList = new ArrayList<>();
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
        }
        mRcyPayCoupon.setLayoutManager(new LinearLayoutManager(this));
        mMyCouponAdapter = new MyCouponAdapter(mPayCouponList, 1);
        mMyCouponAdapter.setEmptyView(mEmptyView);
        mRcyPayCoupon.setAdapter(mMyCouponAdapter);
        mRcyPayCoupon.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_couponDetail:
                        mPayCouponList.get(position).setShowRemark(!mPayCouponList.get(position).isShowRemark());
                        mMyCouponAdapter.setNewData(mPayCouponList);
                        break;
                    case R.id.tv_couponCheck:
                        checkCoupon(position);
                        break;
                }
            }
        });
    }

    //选中代金券返回数据,回到支付界面
    private void checkCoupon(final int position) {
        MyProgressDialog.show(this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("couponId", String.valueOf(mPayCouponList.get(position).getId()));
        map.put("orderId", String.valueOf(mOrderId));
        MyOkHttpUtils
                .getData(UrlContact.URL_CHECK_COUPON, map)
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
                            BaseBean baseBean = GsonUtils.getInstance().fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                MyToastUtil.showShortMessage(baseBean.getMessage());
                                PayActivity.pIntegers.add(mPayPosition);
                                PayActivity.coMoneyIntegers.add(mPayCouponList.get(position).getCoupon_Money());
                                PayActivity.odrIntegers.add(mPayCouponList.get(position).getId());
                                finish();
                            } else {
                                MyToastUtil.showShortMessage(baseBean.getMessage());
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
