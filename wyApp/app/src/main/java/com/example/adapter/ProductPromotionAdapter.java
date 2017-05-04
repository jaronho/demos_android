package com.example.adapter;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.ProductCouponBean;
import com.example.nyapp.R;

import java.util.List;

/**
 * Created by NY on 2017/2/6.
 * 产品优惠
 */

public class ProductPromotionAdapter extends BaseQuickAdapter<ProductCouponBean.DataBean, BaseViewHolder>{
    private boolean isShow;

    public ProductPromotionAdapter(List<ProductCouponBean.DataBean> data) {
        super(R.layout.view_product_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductCouponBean.DataBean item) {
        helper.setText(R.id.tv_coupon_name, item.getName());

        TextView tv_coupon_content = helper.getView(R.id.tv_coupon_content);
        tv_coupon_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_coupon_content.setText(Html.fromHtml(item.getContent()));

        final LinearLayout ll_coupon_content = helper.getView(R.id.ll_coupon_content);
        String ruler = "";
        switch (item.getFlag()) {
            case 1:
                ruler = "查看使用规则";
                break;
            case 2:
                ruler = "查看发放规则";
                break;
        }
        helper.setText(R.id.tv_ruler, ruler);

        helper.getView(R.id.tv_ruler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                if (isShow) {
                    ll_coupon_content.setVisibility(View.VISIBLE);
                } else {
                    ll_coupon_content.setVisibility(View.GONE);
                }
            }
        });

        helper.getView(R.id.tv_coupon_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                if (isShow) {
                    ll_coupon_content.setVisibility(View.VISIBLE);
                } else {
                    ll_coupon_content.setVisibility(View.GONE);
                }
            }
        });
    }
}
