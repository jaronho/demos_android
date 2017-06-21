package com.example.adapter;

import android.graphics.Color;
import android.text.Html;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.CouponListBean;
import com.example.nyapp.R;
import com.example.util.DoubleUtils;

import java.util.List;


/**
 * Created by NY on 2017/3/11.
 * 代金券
 */

public class MyCouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {
    private int mState;//代金券状态  0:未使用  1:已使用  2:已过期
    private int mType;//来自界面 0:我的代金券  1:支付选择代金券

    public MyCouponAdapter(List<CouponListBean> data, int type) {
        super(R.layout.rcy_my_coupon_item, data);
        mType = type;
    }

    public void setCouponState(int state) {
        mState = state;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        double money = 0;
        double needMoney = 0;
        if (mType == 0) {
            money = item.getMoney();
            needMoney = item.getNeed_Money();
            if (item.getNeed_CouponMoney() != 0) {
                needMoney = item.getNeed_CouponMoney() * money + 0.01;
            }
        } else if (mType == 1) {
            money = item.getCoupon_Money();
            needMoney = item.getLow_Money();
        }

        helper.setText(R.id.tv_couponPrice, "¥" + DoubleUtils.format2decimals(money))
                .setText(R.id.tv_needPrice, "满" + DoubleUtils.format2decimals(needMoney) + "元可用")
                .setText(R.id.tv_couponNum, "编号：" + item.getCode())
                .setText(R.id.tv_couponTime, "使用期限：" + item.getBegin_Date().split("T")[0] + "至" + item.getEnd_Date().split("T")[0])
                .setText(R.id.tv_remark, Html.fromHtml(item.getRemark()))
                .setBackgroundRes(R.id.rl_couponPrice, mState == 0 ? R.color.colorPrimary : R.color.all_A)
                .addOnClickListener(R.id.tv_couponDetail)
                .addOnClickListener(R.id.tv_couponCheck)
                .setVisible(R.id.tv_couponCheck, mType == 1)
                .setVisible(R.id.iv_couponIsUsed, mState != 0)
                .setVisible(R.id.ll_remark, item.isShowRemark());

        TextView tv_couponDetail = helper.getView(R.id.tv_couponDetail);
        tv_couponDetail.setTextColor(mState == 0 ? Color.parseColor("#ffff4b00") : Color.parseColor("#ffaaaaaa"));
        tv_couponDetail.setEnabled(mState == 0);
        if (mState != 0) {
            helper.setImageResource(R.id.iv_couponIsUsed, mState == 1 ? R.drawable.coupon_used : R.drawable.coupon_expired);
            tv_couponDetail.setText(mState == 1 ? "订单号：" + item.getOrder_No() : "");
        } else {
            tv_couponDetail.setText("查看使用规则");
        }
    }
}
