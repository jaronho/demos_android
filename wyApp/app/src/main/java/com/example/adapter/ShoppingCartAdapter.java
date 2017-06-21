package com.example.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.ShoppingCartBean;
import com.example.nyapp.R;
import com.example.util.DoubleUtils;
import com.example.util.MyGlideUtils;

import java.util.List;

/**
 * Created by NY on 2017/3/2.
 * 购物车
 */

public class ShoppingCartAdapter extends BaseQuickAdapter<ShoppingCartBean.DataBean.ProductBean, BaseViewHolder> {
    private Context mContext;
    private boolean mIsEdit;//是否处于编辑状态

    public ShoppingCartAdapter(List<ShoppingCartBean.DataBean.ProductBean> data, Context context) {
        super(R.layout.view_shopping_cart_item, data);
        mContext = context;
    }

    public void setEdit(boolean isEdit) {
        mIsEdit = isEdit;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingCartBean.DataBean.ProductBean item) {
        double totalPrice = item.getPrice() * item.getCount() - item.getFree_Price();
        boolean isShowAbate = item.getPriceList().size() == 0;//是否为失效商品

        ImageView iv_shoppingCart = helper.getView(R.id.iv_shoppingCart);
        MyGlideUtils.loadImage(mContext, item.getPic_Url(), iv_shoppingCart);

        helper.setChecked(R.id.cb_Check, !isShowAbate && item.isCheck())
                .addOnClickListener(R.id.cb_Check)
                .addOnClickListener(R.id.iv_shoppingCart)
                .addOnClickListener(R.id.btn_Del)
                .addOnClickListener(R.id.btn_minus_car)
                .addOnClickListener(R.id.tv_shoppingCart_num)
                .addOnClickListener(R.id.btn_add_car)
                .setVisible(R.id.ll_shoppingCart_count, !mIsEdit)
                .setVisible(R.id.ll_shoppingCart_edit, mIsEdit)
                .setVisible(R.id.ll_del, !mIsEdit)
                .setVisible(R.id.iv_abate_shopping, isShowAbate)
                .setVisible(R.id.tv_shoppingCart_Price, !isShowAbate)
                .setVisible(R.id.tv_shoppingCart_spec, !isShowAbate)
                .setVisible(R.id.ll_shoppingCart_number, !isShowAbate)
                .setVisible(R.id.amount_price, !isShowAbate)
                .setVisible(R.id.cb_Check, !isShowAbate)
                .setBackgroundColor(R.id.head, isShowAbate ? 0xffeeeeee : 0xffffffff)
                .setText(R.id.tv_shoppingCart_Price, DoubleUtils.format2decimals(item.getPrice()) + "元")
                .setText(R.id.tv_shoppingCart_spec, item.getSpec())
                .setText(R.id.tv_shoppingCart_count, String.valueOf(item.getCount()))
                .setText(R.id.tv_shoppingCart_num, String.valueOf(item.getCount()))
                .setText(R.id.tv_pay_money, DoubleUtils.format2decimals(totalPrice))
                .setText(R.id.tv_Name, "[" + item.getPro_Name() + "]" + item.getTotal_Content() + item.getCommon_Name()
                        + item.getDosageform());

    }
}
