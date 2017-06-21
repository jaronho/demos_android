package com.example.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.NyItemBean;
import com.example.nyapp.R;

import java.util.List;

/**
 * Created by NY on 2017/3/10.
 * 我的农一item
 */

public class NYItemAdapter extends BaseQuickAdapter<NyItemBean,BaseViewHolder>{

    public NYItemAdapter(List<NyItemBean> data) {
        super(R.layout.rcy_ny_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NyItemBean item) {
        helper.setText(R.id.tv_ny_item, item.getTitle())
                .setImageResource(R.id.iv_ny_item, item.getIcon());
    }
}
