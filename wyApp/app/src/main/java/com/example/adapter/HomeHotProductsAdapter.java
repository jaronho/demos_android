package com.example.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.ProductBriefBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.R;
import com.example.util.DoubleUtils;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/2/24.
 * 首页热门商品
 */

public class HomeHotProductsAdapter extends BaseQuickAdapter<ProductBriefBean.DataBean, BaseViewHolder> {
    private MainActivity mActivity;

    public HomeHotProductsAdapter(List<ProductBriefBean.DataBean> data, MainActivity activity) {
        super(R.layout.frist_optimize_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBriefBean.DataBean item) {
        String name = "[" + item.getPro_Name() + "]" + item.getTotal_Content() + item.getCommon_Name() + item.getDosageform();
        String price;
        if (item.getPrice() == 0) {
            price = item.getShow_Price();
        } else {
            price = DoubleUtils.format2decimals(item.getPrice());
        }
        MyGlideUtils.loadImage(mActivity, item.getPic_Url(), (ImageView) helper.getView(R.id.img));

        helper.setText(R.id.pro_name, name)
                .setText(R.id.spec, item.getSpec())
                .setText(R.id.price, price)
                .setText(R.id.unil, "元/" + item.getUnit())
                .setVisible(R.id.unil, item.getPrice() != 0);
    }
}
