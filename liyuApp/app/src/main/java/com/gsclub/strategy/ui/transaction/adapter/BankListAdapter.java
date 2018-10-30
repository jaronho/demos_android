package com.gsclub.strategy.ui.transaction.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.Constants;
import com.gsclub.strategy.base.BaseRecyclerViewAdapter;
import com.gsclub.strategy.base.RecyclerViewHolder;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.model.bean.BankBean;

public class BankListAdapter extends BaseRecyclerViewAdapter<BankBean> {

    private LayoutInflater inflater;
    private Context context;

    public BankListAdapter(Activity ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        return inflater.inflate(R.layout.item_bank_list, group, false);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final BankBean item, int position) {
        holder.getTextView(R.id.tv_bank_name).setText(item.bank_name);
//        holder.getTextView(R.id.tv_bank_limit).setText(item.bank_limit);
        ImageView logoIv = holder.getImageView(R.id.iv_bank_logo);
        ImageLoader.load(App.getInstance(), item.logo, logoIv, R.mipmap.ic_img_def_grey_small);
        ImageView recommendImage = holder.getImageView(R.id.img_recommend);
        if ("1".equals(item.hot)) {
            recommendImage.setVisibility(View.VISIBLE);
        } else {
            recommendImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        BankBean item = getItem(position);
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_BANK_INFO, item);
        ((Activity)context).setResult(Activity.RESULT_OK, intent);
        ((Activity)context).finish();
    }
}
