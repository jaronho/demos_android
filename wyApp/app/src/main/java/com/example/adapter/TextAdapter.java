package com.example.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.nyapp.R;

import java.util.List;

/**
 * Created by NY on 2017/3/14.
 * 纯文字Adapter
 */

public class TextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TextAdapter(List<String> data) {
        super(R.layout.rcy_text, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}


