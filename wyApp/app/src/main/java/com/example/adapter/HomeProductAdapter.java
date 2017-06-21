package com.example.adapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.classes.HomeClickBean;
import com.example.classes.HomePageBean;
import com.example.classes.HomeProductItemBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.ProductListActivity;
import com.example.nyapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NY on 2017/2/24.
 * 首页分类item
 */

public class HomeProductAdapter extends BaseQuickAdapter<HomeProductItemBean, BaseViewHolder> {
    private MainActivity mActivity;

    public HomeProductAdapter(List<HomeProductItemBean> data, MainActivity activity) {
        super(R.layout.view_home_product_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeProductItemBean item) {
        if (item!=null) {
            final int position = helper.getAdapterPosition();
            helper.setText(R.id.tv_product_title, item.getTitle());

            RecyclerView rcy_home_product_pic = helper.getView(R.id.rcy_home_product_pic);
            RecyclerView rcy_home_product_text = helper.getView(R.id.rcy_home_product_text);

            final TextView tv_product_more = helper.getView(R.id.tv_product_more);
            final TextView tv_regulator = helper.getView(R.id.tv_regulator);
            final TextView tv_water_soluble_fertilizer = helper.getView(R.id.tv_water_soluble_fertilizer);

            tv_product_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(mActivity, ProductListActivity.class);
                            intent.putExtra("type", "除草剂");
                            intent.putExtra("from", "first");
                            mActivity.startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(mActivity, ProductListActivity.class);
                            intent.putExtra("type", "杀虫剂");
                            intent.putExtra("from", "first");
                            mActivity.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(mActivity, ProductListActivity.class);
                            intent.putExtra("type", "杀菌剂");
                            intent.putExtra("from", "first");
                            mActivity.startActivity(intent);
                            break;
                        case 3:
                            tv_product_more.setVisibility(View.GONE);
                            tv_regulator.setVisibility(View.VISIBLE);
                            tv_water_soluble_fertilizer.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });

            tv_regulator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ProductListActivity.class);
                    intent.putExtra("type", "调节剂");
                    intent.putExtra("from", "first");
                    mActivity.startActivity(intent);
                }
            });

            tv_water_soluble_fertilizer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ProductListActivity.class);
                    intent.putExtra("type", "水溶肥");
                    intent.putExtra("from", "first");
                    mActivity.startActivity(intent);
                }
            });

            List<HomePageBean> pageBeanList = item.getPageBeanList();

            final List<HomePageBean> pageBeanPicList = new ArrayList<>();
            final List<HomePageBean> pageBeanTextList = new ArrayList<>();
            if (pageBeanList.size() > 0) {
                for (HomePageBean homePageBean : pageBeanList) {
                    if (homePageBean.getShow_Type() == 1) {
                        pageBeanPicList.add(homePageBean);
                    } else {
                        pageBeanTextList.add(homePageBean);
                    }
                }
            }

            rcy_home_product_pic.setLayoutManager(new GridLayoutManager(mActivity, 2));
            rcy_home_product_pic.setAdapter(new HomeProductPicAdapter(pageBeanPicList, mActivity));
            rcy_home_product_pic.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    EventBus.getDefault().post(new HomeClickBean(0,String.valueOf(pageBeanPicList.get(position).getPro_id()),""));
                }
            });

            rcy_home_product_text.setLayoutManager(new GridLayoutManager(mActivity, 3));
            rcy_home_product_text.setAdapter(new HomeProductTextAdapter(pageBeanTextList, mActivity));
            rcy_home_product_text.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    HomePageBean homePageBean = pageBeanTextList.get(position);
                    EventBus.getDefault().post(new HomeClickBean(homePageBean.getType(), homePageBean.getUrl(),homePageBean.getOwner()));
                }
            });
        }

    }
}
