package com.example.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.HomePageBean;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/2/20.
 * 中部导航
 */

public class HomeNavigateAdapter extends BaseQuickAdapter<HomePageBean,BaseViewHolder> {
    private Context mContext;
    public HomeNavigateAdapter(List<HomePageBean> data, Context context) {
        super(R.layout.view_home_navigate_item, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageBean item) {
        MyGlideUtils.loadNativeImage(mContext,item.getPic_Path(), (ImageView) helper.getView(R.id.iv_home_navigate));
    }
}
