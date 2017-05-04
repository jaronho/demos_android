package com.example.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.HomePageBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.ProductDetailActivity;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;
import com.example.util.MyToastUtil;

import java.util.List;

/**
 * Created by NY on 2016/12/16.
 *
 */

public class MyHomeProductsAdapter extends RecyclerView.Adapter<MyHomeProductsAdapter.OneViewHolder> {
    private List<HomePageBean> mList;
    private MainActivity mActivity;

    public MyHomeProductsAdapter(List<HomePageBean> list, MainActivity activity) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public OneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_home_products_item, parent, false);
        return new OneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OneViewHolder holder, final int position) {

            holder.mTv_shopping_name.setText(mList.get(position).getPro_Name());
            holder.mTv_shopping_spec.setText(mList.get(position).getPro_Spec());
            if (mList.get(position).getPrice() <= 0) {
                holder.mTv_shopping_price.setText(String.valueOf(mList.get(position).getH_Price()));
            } else {

                holder.mTv_shopping_price.setText(String.valueOf(mList.get(position).getPrice()));
            }
            holder.mTv_unit.setText(mList.get(position).getPriceCompany());
            MyGlideUtils.loadImage(mActivity, mList.get(position).getPro_img(), holder.mIv_shoppoing_pic);
            holder.mLl_home_products_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).getPro_id() != -1) {
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        intent.putExtra("id", Integer.valueOf(mList.get(position).getPro_id()));
                        intent.putExtra("name", "");
                        intent.putExtra("type", "");
                        mActivity.startActivity(intent);
                    } else {
                        MyToastUtil.showShortMessage("该商品已下架");
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class OneViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_shoppoing_pic;
        private final TextView mTv_shopping_name;
        private final TextView mTv_shopping_spec;
        private final TextView mTv_shopping_price;
        private final TextView mTv_unit;
        private final LinearLayout mLl_home_products_item;

        OneViewHolder(View itemView) {
            super(itemView);
            mLl_home_products_item = (LinearLayout) itemView.findViewById(R.id.ll_home_products_item);
            mIv_shoppoing_pic = (ImageView) itemView.findViewById(R.id.iv_shopping_pic);
            mTv_shopping_name = (TextView) itemView.findViewById(R.id.tv_shopping_name);
            mTv_shopping_spec = (TextView) itemView.findViewById(R.id.tv_shopping_spec);
            mTv_shopping_price = (TextView) itemView.findViewById(R.id.tv_shopping_price);
            mTv_unit = (TextView) itemView.findViewById(R.id.tv_unit);

        }
    }

}
