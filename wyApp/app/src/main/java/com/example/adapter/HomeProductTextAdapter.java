package com.example.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.HomePageBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.R;

import java.util.List;

/**
 * Created by NY on 2017/2/24.
 * 首页分类文字item
 */

public class HomeProductTextAdapter extends BaseQuickAdapter<HomePageBean, BaseViewHolder> {
    private MainActivity mActivity;

    public HomeProductTextAdapter(List<HomePageBean> data, MainActivity activity) {
        super(R.layout.view_home_products_text_item, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageBean item) {
        if (item != null) {
            helper.setText(R.id.tv_product_name, item.getName());
        }
    }
}
