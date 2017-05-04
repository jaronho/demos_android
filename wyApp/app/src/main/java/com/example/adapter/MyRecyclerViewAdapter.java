package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.ProductBriefBean;
import com.example.nyapp.ProductDetailActivity;
import com.example.nyapp.R;
import com.example.util.MyGlideUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by NY on 2016/11/14.
 * 热卖商品列表
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyRecyclerViewHOlder> {
    private List<ProductBriefBean.DataBean> briefs;
    private Context mContext;
    DecimalFormat ddf1 = new DecimalFormat("#.00");

    public MyRecyclerViewAdapter(List<ProductBriefBean.DataBean> briefs, Context context) {
        this.briefs = briefs;
        this.mContext = context;
    }


    @Override
    public MyRecyclerViewHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.frist_optimize_item, parent, false);
        return new MyRecyclerViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHOlder holder, final int position) {
        String name = "[" + briefs.get(position).getPro_Name() + "]"
                + briefs.get(position).getTotal_Content() + briefs.get(position).getCommon_Name()
                + briefs.get(position).getDosageform();
        String spec = briefs.get(position).getSpec();
        if (briefs.get(position).getPrice()==0) {
            holder.price.setText(briefs.get(position).getShow_Price());
            holder.unil.setVisibility(View.GONE);
        } else {
            holder.unil.setVisibility(View.VISIBLE);
            holder.price.setText(String.valueOf(briefs.get(position).getPrice()));
            holder.unil.setText(String.format("元/%s", briefs.get(position).getUnit()));
        }
        holder.pro_name.setText(name);
        holder.Spec.setText(spec);

        MyGlideUtils.loadImage(mContext, briefs.get(position).getPic_Url(), holder.img);
        // unil_price 要通过后台获取

        holder.hot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(briefs.get(position).getId()));
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                intent.putExtra("isSecKill", briefs.get(position).getMarketing_Type());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return briefs.size();
    }

    class MyRecyclerViewHOlder extends RecyclerView.ViewHolder {
        private TextView pro_name, Spec, price, unil;
        private LinearLayout hot;
        private ImageView img;

        MyRecyclerViewHOlder(View itemView) {
            super(itemView);
            pro_name = (TextView) itemView.findViewById(R.id.pro_name);
            Spec = (TextView) itemView.findViewById(R.id.spec);
            price = (TextView) itemView.findViewById(R.id.price);
            unil = (TextView) itemView.findViewById(R.id.unil);
            img = (ImageView) itemView.findViewById(R.id.img);
            hot = (LinearLayout) itemView.findViewById(R.id.hot);
        }
    }
}
