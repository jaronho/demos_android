package com.example.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.classes.MyOrderBean;
import com.example.classes.Share;
import com.example.classes.User;
import com.example.nyapp.DingDanDetialActivity;
import com.example.nyapp.MyDingDanActivity;
import com.example.nyapp.PayActivity;
import com.example.nyapp.ProductDetailActivity;
import com.example.nyapp.R;
import com.example.util.MyDateUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyMsgDialog;
import com.example.util.ShareUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by NY on 2017/1/21.
 * 我的订单
 */

public class MyOrderAdapter extends BaseQuickAdapter<MyOrderBean.DataBean.OrdersBean, BaseViewHolder> {

    private MyDingDanActivity mActivity;
    private int type;
    private User user;
    private DecimalFormat ddf1 = new DecimalFormat("#.00");
    private MyMsgDialog mFreightDialog;

    public MyOrderAdapter(MyDingDanActivity activity, int type, User user, List<MyOrderBean.DataBean.OrdersBean> data) {
        super(R.layout.dingdan_stock_item, data);
        mActivity = activity;
        this.type = type;
        this.user = user;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyOrderBean.DataBean.OrdersBean ordersBean) {
        final int position = baseViewHolder.getAdapterPosition();
        final MyOrderBean.DataBean.OrdersBean.OrderGroupBean orderGroup = ordersBean.getOrderGroup();
        final List<MyOrderBean.DataBean.OrdersBean.OrderItemsBean> orderItems = ordersBean.getOrderItems();

        if (type == 2) {
            baseViewHolder
                    .setVisible(R.id.order_layout_number, false)
                    .setVisible(R.id.order_layout_number, true)
                    .setText(R.id.text_order_username, orderGroup.getUser_Name());
        } else {
            baseViewHolder
                    .setVisible(R.id.order_layout_number, true)
                    .setVisible(R.id.order_layout_number, false);
        }

        baseViewHolder
                .setText(R.id.text_dingdanzuhao, orderGroup.getGroup_No())
                .setText(R.id.text_time, orderGroup.getAdd_Date())
                .setText(R.id.text_ordertote, String.valueOf(orderGroup.getICount()));

        boolean is_hebingfukuan = true;
        for (int i = 0; i < orderItems.size(); i++) {
            List<Integer> btnInts = orderItems.get(i).getButtons();
            boolean isContain = false;
            for (int j = 0; j < btnInts.size(); j++) {
                int btn = btnInts.get(j);
                if (btn == 4) {
                    isContain = true;
                }
            }
            if (!isContain) {
                is_hebingfukuan = false;
            }
        }
        Button btn_hebingfukuan = baseViewHolder.getView(R.id.btn_hebingfukuan);
        btn_hebingfukuan.setVisibility(is_hebingfukuan ? View.VISIBLE : View.GONE);
        btn_hebingfukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isShowFreightDialog = false;
                for (MyOrderBean.DataBean.OrdersBean.OrderItemsBean orderItem : orderItems) {
                    if (orderItem.getState().equals("待改运费")) {
                        isShowFreightDialog = true;
                    }
                }
                if (isShowFreightDialog) {
                    showFreightDialog();
                } else {
                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("dingdan", orderGroup.getId() + "");
                    mActivity.startActivity(intent);
                }
            }
        });

        LinearLayout list_dingdan = baseViewHolder.getView(R.id.list_dingdan);
        list_dingdan.removeAllViews();

        for (int j = 0; j < orderItems.size(); j++) {
            final int number = j;
            View dingdan_item = LayoutInflater.from(mActivity).inflate(R.layout.dingdan_item, null);
            TextView text_dingdanhao = (TextView) dingdan_item.findViewById(R.id.text_dingdanhao);
            TextView text_zhuangtai = (TextView) dingdan_item.findViewById(R.id.text_zhuangtai);
            TextView text_money = (TextView) dingdan_item.findViewById(R.id.text_money);
            LinearLayout product_layout = (LinearLayout) dingdan_item.findViewById(R.id.product_layout);
            Button btn_order_shape = (Button) dingdan_item.findViewById(R.id.btn_order_shape);
            Button btn_queren = (Button) dingdan_item.findViewById(R.id.btn_queren);
            Button btn_quxiao = (Button) dingdan_item.findViewById(R.id.btn_quxiao);
            Button btn_delete = (Button) dingdan_item.findViewById(R.id.btn_delete);
            Button btn_pay = (Button) dingdan_item.findViewById(R.id.btn_pay);
            Button btn_pay_dingjin = (Button) dingdan_item.findViewById(R.id.btn_pay_dingjin);
            Button btn_pay_weikuan = (Button) dingdan_item.findViewById(R.id.btn_pay_weikuan);
            Button btn_chakan = (Button) dingdan_item.findViewById(R.id.btn_chakan);
            Button btn_tuihuo = (Button) dingdan_item.findViewById(R.id.btn_tuihuo);
            // 开始循环
            text_dingdanhao.setText(orderItems.get(j).getSend_Name());

            product_layout.removeAllViews();

            for (int i = 0; i < orderItems.get(j).getSubOrders().size(); i++) {
                final int a = i;
                View view = LayoutInflater.from(mActivity).inflate(R.layout.dingdan_item2, null);
                TextView tvName = (TextView) view.findViewById(R.id.tvName);
                TextView text_price = (TextView) view.findViewById(R.id.text_price);
                TextView text_spec = (TextView) view.findViewById(R.id.text_spec);
                TextView text_count = (TextView) view.findViewById(R.id.text_count);
                ImageView image_product = (ImageView) view.findViewById(R.id.image_product);
                tvName.setText(orderItems.get(j).getSubOrders().get(i).getName());
                text_count.setText("" + orderItems.get(j).getSubOrders().get(i).getCount() + "箱");
                text_spec.setText(orderItems.get(j).getSubOrders().get(i).getSpec());
                text_price.setText("" + orderItems.get(j).getSubOrders().get(i).getPrice() + "元");

                MyGlideUtils.loadNativeImage(mActivity, orderItems.get(j).getSubOrders().get(i).getImageUrl(), image_product);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        intent.putExtra("id",
                                orderItems.get(number).getSubOrders().get(a).getId());
                        intent.putExtra("name", "");
                        intent.putExtra("type", "");
                        mActivity.startActivity(intent);
                    }
                });
                product_layout.addView(view);
            }
            // --------------------start
            btn_queren.setVisibility(View.GONE);
            btn_quxiao.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_pay_dingjin.setVisibility(View.GONE);
            btn_pay_weikuan.setVisibility(View.GONE);
            btn_chakan.setVisibility(View.GONE);
            btn_tuihuo.setVisibility(View.GONE);

            int buttons = orderItems.get(j).getButtons().size();
            for (int i = 0; i < buttons; i++) {
                int button = orderItems.get(j).getButtons().get(i);

                switch (button) {
                    case 1:
                        // 确认收货
                        btn_queren.setVisibility(View.VISIBLE);
                        btn_queren.setText("确认收货");
                        break;
                    case 2:
                        // 取消订单
                        btn_quxiao.setVisibility(View.VISIBLE);
                        btn_quxiao.setText("取消订单");
                        break;
                    case 3:
                        // 删除订单
                        btn_delete.setVisibility(View.VISIBLE);
                        btn_delete.setText("删除订单");
                        break;
                    case 4:
                        // 付款
                        // btn_pay.setVisibility(View.VISIBLE);
                        // btn_pay.setText("付款");
                        break;
                    case 5:
                        // 支付定金
                        btn_pay_dingjin.setVisibility(View.VISIBLE);
                        btn_pay_dingjin.setText("支付定金");
                        break;
                    case 6:
                        // 支付尾款
                        btn_pay_weikuan.setVisibility(View.VISIBLE);
                        btn_pay_weikuan.setText("支付尾款");
                        break;
                    case 7:
                        // 查看详情
                        btn_chakan.setVisibility(View.VISIBLE);
                        btn_chakan.setText("查看详情");
                        break;
                    case 8:
                        // 申请退货
                        btn_tuihuo.setVisibility(View.VISIBLE);
                        btn_tuihuo.setText("申请退货");
                        break;
                }
            }
            btn_order_shape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Share.DataBean dataBean = new Share.DataBean();
                    String shareUser = "";
                    switch (user.getPermit_Type()) {
                        case "1":
                            shareUser = "会员";
                            break;
                        case "2":
                            shareUser = "代购员";
                            break;
                        case "4":
                            shareUser = "签约代购员";
                            break;
                    }

                    String time = "";
                    String orderTime = orderGroup.getAdd_Date().replace("T", " ");//获取订单时间
                    long orderDate = MyDateUtils.getStringToDate(orderTime);

                    long contentDate = MyDateUtils.getGMTime2();//获取当前时间

                    long startDate = MyDateUtils.getStringToDate("2017-03-03 00:00:00");//春耕节开始时间
                    long endDate = MyDateUtils.getStringToDate("2017-03-03 23:59:59");//春耕节结束时间

                    if (orderDate >= startDate && orderDate <= endDate) {
                        time = "春耕节";
                    } else if (orderDate + 3600 >= contentDate) {
                        time = "刚刚";
                    } else {
                        time = "";
                    }

                    dataBean.setSharedTitle("我是农一网 " + shareUser + "，" + time + "我买到了好东西，你也看看~");
                    dataBean.setSharedContent("买好农药就上农一网!");
                    dataBean.setSharedImage(orderItems.get(number).getSubOrders().get(0).getImageUrl());
                    dataBean.setSharedUrl(orderItems.get(number).getShareLink());
                    ShareUtil.initShareDate(mActivity, dataBean);
                }
            });

            btn_queren.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String text = "";
                    if (orderItems.get(number).getState().equals("已发货")) {
                        text = "是否确认收货";
                    }
                    mActivity.showDialog(text, orderItems.get(number));
                }
            });
            btn_quxiao.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String text = "";

                    if (orderItems.get(number).getState().equals("待改运费")
                            || orderItems.get(number).getState().equals("待支付")) {
                        text = "是否取消订单";
                    }
                    mActivity.showDialog(text, orderItems.get(number));

                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String text = "是否删除订单";
                    mActivity.showDialog(text, orderItems.get(number));
                }
            });

            btn_chakan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mActivity, DingDanDetialActivity.class);
                    intent.putExtra("id", orderItems.get(number).getId());
                    mActivity.startActivity(intent);

                }
            });
            text_zhuangtai.setText(orderItems.get(j).getState());
            String state = text_zhuangtai.getText().toString();
            if (state.equals("已支付") || state.equals("已发货") || state.equals("交易成功")) {
                btn_order_shape.setVisibility(View.VISIBLE);
            }
            if (orderItems.get(j).getPrice() >= 1) {

                text_money.setText("" + ddf1.format(orderItems.get(j).getPrice()) + "元");
            } else {
                text_money.setText(
                        "0" + ddf1.format(orderItems.get(j).getPrice()) + "元");

            }
            list_dingdan.addView(dingdan_item);

        }
    }

    //待改运费提示弹窗
    private void showFreightDialog() {
        mFreightDialog = new MyMsgDialog(mActivity, "系统提示", "此订单需要更改运费,请联系客服!", null, new MyMsgDialog.CancelListener() {
            @Override
            public void onClick() {
                mFreightDialog.dismiss();
            }
        });
        mFreightDialog.setCancelable(false);
        mFreightDialog.show();
    }

}
