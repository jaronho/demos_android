package com.example.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.HomeCouponBean;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/2/20.
 * 优惠券列表
 */

public class HomeCouponAdapter extends BaseQuickAdapter<HomeCouponBean.DataBean, BaseViewHolder> {
    private Context mContext;

    public HomeCouponAdapter(List<HomeCouponBean.DataBean> data, Context context) {
        super(R.layout.view_home_coupon_item, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeCouponBean.DataBean item) {
        MyGlideUtils.loadNativeImage(mContext, item.getPic_Url(), (ImageView) helper.getView(R.id.iv_coupon_item));

        helper.setText(R.id.tv_home_coupon_name, item.getPro_Name())
                .setText(R.id.tv_coupon_spec, item.getSpec());

        double price = item.getPrice();
        if (price == 0.0) {
            helper.setText(R.id.tv_coupon_price, item.getShow_Price())
                    .setVisible(R.id.tv_coupon_unit, false);
        } else {
            helper.setText(R.id.tv_coupon_price, String.valueOf(price))
                    .setText(R.id.tv_coupon_unit, "元/"+item.getUnit());
        }
    }
}
