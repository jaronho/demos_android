package com.example.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.HomePageBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.R;
import com.example.util.DoubleUtils;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/2/24.
 * 首页分类图文item
 */

public class HomeProductPicAdapter extends BaseQuickAdapter<HomePageBean, BaseViewHolder> {
    private MainActivity mActivity;

    public HomeProductPicAdapter(List<HomePageBean> data, MainActivity activity) {
        super(R.layout.view_home_products_item, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageBean item) {
        if (item != null) {
            double price;
            if (item.getPrice() == 0) {
                price = item.getH_Price();
            } else {
                price = item.getPrice();
            }
            helper.setText(R.id.tv_shopping_name, item.getPro_Name())
                    .setText(R.id.tv_shopping_spec, item.getPro_Spec())
                    .setText(R.id.tv_shopping_price, DoubleUtils.format2decimals(price))
                    .setText(R.id.tv_unit, item.getPriceCompany());

            MyGlideUtils.loadImage(mActivity, item.getPro_img(), (ImageView) helper.getView(R.id.iv_shopping_pic));
        }

    }
}
