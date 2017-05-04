package com.example.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.ProductJson;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/3/21.
 * 商品列表adapter
 */

public class ProductListAdapter extends BaseQuickAdapter<ProductJson,BaseViewHolder>{

    private Context mContext;

    public ProductListAdapter(List<ProductJson> data, Context context) {
        super(R.layout.rcy_product_list_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductJson item) {
        String price;
        if (item.getPrice() == 0) {
            price = item.getShow_Price();
        } else {
            price = String.valueOf(item.getPrice());
        }

        String type = item.getType();
        int type_bg = R.color.shop_type_0;
        switch (type) {
            case "除草剂":
                type_bg = R.color.shop_type_1;
                break;
            case "杀虫剂":
                type_bg = R.color.shop_type_2;
                break;
            case "杀菌剂":
                type_bg = R.color.shop_type_3;
                break;
            case "调节剂":
                type_bg = R.color.shop_type_4;
                break;
            case "飞防套餐":
                type_bg = R.color.shop_type_5;
                break;
            case "复合肥":
                type_bg = R.color.shop_type_6;
                break;
        }

        MyGlideUtils.loadImage(mContext,item.getPic_Url(), (ImageView) helper.getView(R.id.iv_Preview));

        helper.setText(R.id.tv_Title,"[" + item.getPro_Name() + "]" + item.getTotal_Content() + item.getCommon_Name() + item.getDosageform())
        .setText(R.id.tv_Review, price)
        .setText(R.id.tv_specification, item.getSpec())
        .setText(R.id.text_jixing, type)
        .setBackgroundRes(R.id.text_jixing, type_bg)
        .setText(R.id.tv_deal, String.valueOf(item.getDeal()))
        .setText(R.id.tv_qiye, item.getManuf_Name())
        .setVisible(R.id.tv_review_spec, item.getPrice() != 0);
    }
}
