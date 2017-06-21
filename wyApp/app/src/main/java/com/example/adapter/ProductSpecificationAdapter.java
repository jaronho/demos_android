package com.example.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.ProductDetailBean;
import com.example.nyapp.R;

import java.util.List;

/**
 * Created by NY on 2017/2/6.
 * 产品规格
 */

public class ProductSpecificationAdapter extends BaseQuickAdapter<ProductDetailBean.DataBean.ProductSpecListBean, BaseViewHolder> {
    private int clickPosition = 0;

    public ProductSpecificationAdapter(List<ProductDetailBean.DataBean.ProductSpecListBean> data) {
        super(R.layout.view_product_specification, data);
    }

    public void setSelectedPosition(int position) {
        clickPosition = position;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductDetailBean.DataBean.ProductSpecListBean item) {

        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_specification,item.getProSpec())
                .addOnClickListener(R.id.tv_specification);

        TextView tv_specification = helper.getView(R.id.tv_specification);
        if (position == clickPosition) {
            tv_specification.setSelected(true);
        } else {
            tv_specification.setSelected(false);
        }

    }
}
