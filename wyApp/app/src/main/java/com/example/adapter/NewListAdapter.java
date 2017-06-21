package com.example.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.ProductJson;
import com.example.nyapp.ProductDetailActivity;
import com.example.nyapp.ProductListActivity;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NewListAdapter extends BaseAdapter {
    private ProductListActivity mActivity;
    private ProductJson productJson;
    private ViewHolder hold;
    DecimalFormat ddf1 = new DecimalFormat("#.00");

    static class ViewHolder {
        ImageView ivPreview;
        TextView tvTitle;
        TextView tvSpec;
        TextView tvReview;
        TextView text_qiye;
        TextView text_jixing;
        TextView text_deal;
        TextView text_end;
        TextView tvreview_spce;
        //		ImageView text_tianjiagouwuche,text_tianjiagouwuche1;
        LinearLayout relative_product;
    }


    public ArrayList<ProductJson> news = new ArrayList<ProductJson>();

    public NewListAdapter(ProductListActivity mActivity, ArrayList<ProductJson> news, ImageLoader imageLoader) {
        this.mActivity = mActivity;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        productJson = news.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_news, null);
            holder = new ViewHolder();
            holder.ivPreview = (ImageView) convertView.findViewById(R.id.ivPreview);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvSpec = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvReview = (TextView) convertView.findViewById(R.id.tvReview);
            holder.text_deal = (TextView) convertView.findViewById(R.id.text_deal);
            holder.text_qiye = (TextView) convertView.findViewById(R.id.text_qiye);
            holder.text_jixing = (TextView) convertView.findViewById(R.id.text_jixing);
            holder.text_end = (TextView) convertView.findViewById(R.id.text_end);
            holder.tvreview_spce = (TextView) convertView.findViewById(R.id.tvreview_spce);
//			holder.text_tianjiagouwuche = (ImageView) convertView.findViewById(R.id.text_tianjiagouwuche);
//			holder.text_tianjiagouwuche1 = (ImageView) convertView.findViewById(R.id.text_tianjiagouwuche1);
            holder.relative_product = (LinearLayout) convertView.findViewById(R.id.relative_product);
            hold = holder;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyGlideUtils.loadImage(mActivity, news.get(position).getPic_Url(), holder.ivPreview);
//        if (news.size() - 1 == position && (mActivity.listJsons.size() == 0 || mActivity.total_count == news.size())) {
//            holder.text_end.setVisibility(View.VISIBLE);
//            holder.text_end.setText("已经全部加载完毕");
//        } else {
//            if (position == news.size() - 1) {
//                holder.text_end.setVisibility(View.VISIBLE);
//                holder.text_end.setText("正在加载中...");
//            } else {
//                holder.text_end.setVisibility(View.GONE);
//            }
//
//
//        }

        holder.tvTitle.setText("[" + news.get(position).getPro_Name() + "]" + news.get(position).getTotal_Content() + news.get(position).getCommon_Name() + news.get(position).getDosageform());
        holder.tvSpec.setText(news.get(position).getSpec());
        if (news.get(position).getPrice()==0) {
            holder.tvReview.setText(news.get(position).getShow_Price());
            holder.tvreview_spce.setVisibility(View.GONE);
        } else {
            holder.tvreview_spce.setVisibility(View.VISIBLE);
            if (news.get(position).getPrice() < 1) {

                holder.tvReview.setText("0" + ddf1.format(Double.valueOf(news.get(position).getPrice())) + "");
            } else {
                holder.tvReview.setText("" + ddf1.format(Double.valueOf(news.get(position).getPrice())) + "");
            }
        }
        holder.text_deal.setText("" + news.get(position).getDeal() + "");

        final String type = news.get(position).getType();
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
        holder.text_jixing.setBackgroundResource(type_bg);
        holder.text_jixing.setText(type);

        holder.text_qiye.setText(news.get(position).getManuf_Name());
        holder.relative_product.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("id", news.get(position).getId());
                intent.putExtra("name", news.get(position).getPro_Name());
                intent.putExtra("type", type);
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }


    public void addNews(List<ProductJson> addNews) {
        for (ProductJson hm : addNews) {
            news.add(hm);
        }
    }

    @Override
    public Object getItem(int arg0) {
        return news.get(arg0);
    }

    public void setList(ArrayList<ProductJson> plits) {
        if (plits != null) {
            news = (ArrayList<ProductJson>) plits.clone();
            notifyDataSetChanged();
        }
    }

    private Resources getResources() {
        Resources mResources = null;
        mResources = getResources();
        return mResources;
    }

}
