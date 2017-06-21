package com.example.nyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.Ordercals;
import com.example.classes.OrdercalsJsonUtil;
import com.example.classes.User;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class PayActivity extends BaseActivity {
    private String dingdanhao;
    private User user;
    private ACache mCache;
    private ListView list_pay;
    private ArrayList<Ordercals> ordercals;
    private MyAdapter adapter;
    private String urlString2 = UrlContact.URL_STRING + "/api/pay/index?orderId=";
    private String liuyanString = "";
    public static List<Integer> pIntegers;
    public static List<Integer> odrIntegers;
    public static List<Double> coMoneyIntegers;
    private String url;
    private LinearLayout layout_back;
    private static List<Integer> positIntegers;
    private static List<String> remark;
    private TextView daijinquan, freeprice;
    DecimalFormat ddf1 = new DecimalFormat("#.00");
    public static String balanceMoney = "0";
    private int UserMoneyFlag = 0;
    private int UseFlag = 0;//0:没有使用余额 1：使用了余额
    private int mLastPosition;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;
    private MyMsgDialog mSecKillDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payitem);
        dingdanhao = getIntent().getStringExtra("dingdan");
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        urlString2 = urlString2 + dingdanhao;
        list_pay = (ListView) findViewById(R.id.list_pay);
        pIntegers = new ArrayList<Integer>();
        odrIntegers = new ArrayList<Integer>();
        coMoneyIntegers = new ArrayList<Double>();
        positIntegers = new ArrayList<Integer>();
        remark = new ArrayList<String>();
        balanceMoney = "0";
        UseFlag = 0;
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        String isSecKill = getIntent().getStringExtra("isSecKill");

        if (isSecKill != null && isSecKill.equals("ProductDetailActivity")) {
            mSecKillDialog = new MyMsgDialog(this, "系统提示", "您已拥有本轮双草一元抢的资格，请立即支付，如果您离开本页面或在下轮活动前未完成付款，将视为您主动放弃本轮抢购资格",
                    new MyMsgDialog.ConfirmListener() {
                        @Override
                        public void onClick() {
                            mSecKillDialog.dismiss();
                        }
                    },
                    null);
            mSecKillDialog.setCancelable(false);
            mSecKillDialog.show();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (balanceMoney.equals("n")) {
            balanceMoney = "0";
        }
        initData();

    }

    private void initData() {
        MyProgressDialog.show(PayActivity.this, true, true);
        Map<String, String> params2 = new TreeMap<>();
        params2.put("orderId", dingdanhao);
        MyOkHttpUtils
                .getData(UrlContact.URL_STRING + "/api/pay/index",params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        try {
                            if (new JSONObject(response).getBoolean("Result")) {
                                ordercals = OrdercalsJsonUtil.getOrdercalss(response);
                                adapter = new MyAdapter(ordercals);
                                list_pay.setAdapter(adapter);
                                list_pay.setEmptyView(findViewById(R.id.empty14));
                            } else {
                                Toast.makeText(getApplicationContext(), new JSONObject(response).getString("Message"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void initView() {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Pay Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<Ordercals> ordercals2;

        public MyAdapter(ArrayList<Ordercals> ordercals) {
            this.ordercals2 = ordercals;
        }

        @Override
        public int getCount() {
            return ordercals2.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            double totalMoney;
            double couponMoney;
            double payMoney;
            double canBalanceMoney;

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PayActivity.this).inflate(R.layout.pay_listitem, null);
                viewHolder = new ViewHolder();
                viewHolder.dingdan_num = (TextView) convertView.findViewById(R.id.dingdan_num);
                viewHolder.textq_qiyename = (TextView) convertView.findViewById(R.id.textq_qiyename);
                viewHolder.text_peisong = (TextView) convertView.findViewById(R.id.text_peisong);
                viewHolder.product_zongjia = (TextView) convertView.findViewById(R.id.product_zongjia);
                // viewHolder.product_peisong = (TextView)
                // convertView.findViewById(R.id.product_peisong);
                viewHolder.daijinquan = (TextView) convertView.findViewById(R.id.daijinquan);
                // viewHolder.freeprice = (TextView)
                // convertView.findViewById(R.id.freeprice);
                viewHolder.ToPay_Price = (TextView) convertView.findViewById(R.id.ToPay_Price);
                viewHolder.shuoming_layout = (LinearLayout) convertView.findViewById(R.id.shuoming_layout);
                viewHolder.daijinquan_price = (TextView) convertView.findViewById(R.id.daijinquan_price);
                viewHolder.edit_liuyan = (EditText) convertView.findViewById(R.id.edit_liuyan);
                viewHolder.text_zhifu = (TextView) convertView.findViewById(R.id.text_zhifu);
                viewHolder.daijin_layout = (RelativeLayout) convertView.findViewById(R.id.daijin_layout);
                viewHolder.text_tishi = (TextView) convertView.findViewById(R.id.text_tishi);
                // viewHolder.freelayout = (RelativeLayout)
                // convertView.findViewById(R.id.freelayout);
                viewHolder.couponlayout = (RelativeLayout) convertView.findViewById(R.id.couponlayout);
                viewHolder.banlance_layout = (RelativeLayout) convertView.findViewById(R.id.banlance_layout);
                viewHolder.edit_liuyan.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        liuyanString = arg0.toString();

                    }
                });
                viewHolder.rl_balance_Price = (RelativeLayout) convertView.findViewById(R.id.rl_balance_Price);
                viewHolder.tv_balance_Price = (TextView) convertView.findViewById(R.id.tv_balance_Price);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.edit_liuyan.setText(ordercals2.get(position).getRemark());
            for (int i = 0; i < positIntegers.size(); i++) {
                if (positIntegers.get(i) == position) {
                    viewHolder.edit_liuyan.setText(remark.get(i));
                }
            }
            if (ordercals2.get(position).isRefreButton()) {
                viewHolder.daijin_layout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.daijin_layout.setVisibility(View.GONE);
            }
            String[] payinfo = ordercals2.get(position).getPayInfo().split("&");
            if (payinfo[0].equals("")) {
                viewHolder.text_peisong.setVisibility(View.GONE);
            } else {
                viewHolder.text_peisong.setVisibility(View.VISIBLE);
                viewHolder.text_peisong.setText(payinfo[0]);
            }
            viewHolder.shuoming_layout = (LinearLayout) convertView.findViewById(R.id.shuoming_layout);
            viewHolder.shuoming_layout.removeAllViews();
            if (payinfo.length > 3) {
                if (payinfo[1].equals("") || payinfo[2].equals("")) {
                    viewHolder.shuoming_layout.setVisibility(View.GONE);
                } else {
                    viewHolder.shuoming_layout.setVisibility(View.VISIBLE);
                    for (int i = 1; i < payinfo.length; i++) {
                        TextView textView = new TextView(PayActivity.this);
                        textView.setText(payinfo[i]);
                        viewHolder.shuoming_layout.addView(textView);
                    }
                }
            }
            viewHolder.daijinquan_price.setText("元");
            if (ordercals2.get(position).getCoupon_Price() == 0) {
                couponMoney = 0;
                viewHolder.couponlayout.setVisibility(View.GONE);

            } else {
                couponMoney = ordercals2.get(position).getCoupon_Price();
                viewHolder.couponlayout.setVisibility(View.VISIBLE);
            }

//			判断是否有余额
            if (Double.valueOf(balanceMoney) > 0) {
                viewHolder.rl_balance_Price.setVisibility(View.VISIBLE);

                UserMoneyFlag = 1;
                UseFlag = 1;
                if (Double.valueOf(balanceMoney) > ordercals2.get(position).getToPay_Price()) {
                    payMoney = 0;
                } else {
                    if (ordercals2.get(position).getToPay_Price() - Double.valueOf(balanceMoney) >= 1) {
                        payMoney = ordercals2.get(position).getToPay_Price() - Double.valueOf(balanceMoney);
                    } else {
                        payMoney = ordercals2.get(position).getToPay_Price() - Double.valueOf(balanceMoney);
                    }
                }
            } else {
                viewHolder.rl_balance_Price.setVisibility(View.GONE);
                UserMoneyFlag = 0;
                if (ordercals2.get(position).getToPay_Price() >= 1) {
                    payMoney = ordercals2.get(position).getToPay_Price();
                } else {
                    payMoney = ordercals2.get(position).getToPay_Price();
                }
            }


            for (int i = 0; i < pIntegers.size(); i++) {
                if (pIntegers.get(i) == position) {
                    couponMoney = coMoneyIntegers.get(i);

                    viewHolder.couponlayout.setVisibility(View.VISIBLE);
                    if (Double.valueOf(balanceMoney) > 0) {
                        UserMoneyFlag = 1;
                        UseFlag = 1;
                        if (Double.valueOf(balanceMoney) > ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i)) {
                            payMoney = 0;
                        } else {
                            if (ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i) >= 1) {
                                payMoney = ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i) - Double.valueOf(balanceMoney);
                            } else {
                                payMoney = ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i) - Double.valueOf(balanceMoney);

                            }
                        }
                    } else {
                        UserMoneyFlag = 0;
                        if (ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i) >= 1) {
                            payMoney = ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i);
                        } else {
                            payMoney = ordercals2.get(position).getToPay_Price() - coMoneyIntegers.get(i);

                        }
                    }

                }
            }

            viewHolder.banlance_layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mLastPosition = position;
                    isShowBalanceDialog = 4;
                    Intent intent = new Intent(PayActivity.this, TuanGouActivity.class);
                    intent.putExtra("payurl",
                            UrlContact.WEB_URL_STRING + "/Pay/Moneyindex?orderId=" + ordercals2.get(position).getOrder_Id()
                                    + "&Type=4" + "&loginkey=" + user.getUser_Name() + "&deviceId="
                                    + MyApplication.sUdid + "&UseFlag=" + UseFlag);
                    intent.putExtra("type", 1);
                    intent.putExtra("title", "余额支付");
                    startActivity(intent);
                }
            });
            viewHolder.dingdan_num.setText(ordercals2.get(position).getOrder_No());
            viewHolder.textq_qiyename.setText(ordercals2.get(position).getManuf_Name());
            totalMoney = ordercals2.get(position).getTotal_price()
                    + Double.parseDouble(ordercals2.get(position).getWorkStation_Freight())
                    + ordercals2.get(position).getFreight()
                    - Double.parseDouble(ordercals2.get(position).getFree_Price());
            if (totalMoney >= 1) {

                viewHolder.product_zongjia.setText("" + ddf1.format(totalMoney) + "元");
            } else {
                viewHolder.product_zongjia.setText("0" + ddf1.format(totalMoney) + "元");

            }

            viewHolder.daijinquan.setText("" + ddf1.format(couponMoney) + getString(R.string.yuan));

            if (payMoney == 0) {
                viewHolder.ToPay_Price.setText(getString(R.string.num_0_00));
            } else if (payMoney < 1) {
                viewHolder.ToPay_Price.setText("0" + ddf1.format(payMoney) + "元");
            } else {
                viewHolder.ToPay_Price.setText("" + ddf1.format(payMoney) + "元");
            }

            if (Double.valueOf(balanceMoney) < 0) {
                viewHolder.tv_balance_Price.setText("");
            }
            canBalanceMoney = totalMoney - couponMoney - payMoney;
            viewHolder.tv_balance_Price.setText("" + ddf1.format(canBalanceMoney) + "元");

            if (ordercals2.get(position).getReminder().equals("null")) {
                viewHolder.text_tishi.setText("");
                viewHolder.text_tishi.setVisibility(View.GONE);
            } else {
                viewHolder.text_tishi.setVisibility(View.VISIBLE);

                viewHolder.text_tishi.setText(ordercals2.get(position).getReminder());

            }
            viewHolder.daijin_layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    positIntegers.add(position);
                    remark.add(liuyanString);
                    Intent intent = new Intent(PayActivity.this, PayCouponActivity.class);
                    intent.putExtra("order", (ordercals2.get(position).getOrder_Id()));
                    intent.putExtra("position", position);
                    startActivity(intent);

                }
            });
            if (ordercals2.get(position).isNextButton()) {
                viewHolder.text_zhifu.setVisibility(View.VISIBLE);
            } else {
                viewHolder.text_zhifu.setVisibility(View.GONE);
            }
            viewHolder.text_zhifu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (isShowBalanceDialog == 2) {
                        isShowBalanceDialog = 4;
                        showBalanceDialog();
                    } else {

                        goPay(position);
                    }

                }
            });


            if (position == ordercals2.size() - 1) {
                if (canBalanceMoney > 0) {
                    if (isShowBalanceDialog == 4) {
                        showBalanceDialog();
                    }
                }
            }

            return convertView;
        }


        class ViewHolder {
            TextView dingdan_num;
            TextView textq_qiyename;
            TextView text_peisong;
            LinearLayout shuoming_layout;
            EditText edit_liuyan;
            TextView product_zongjia;
            // TextView product_peisong;
            TextView daijinquan;
            // TextView freeprice;
            TextView ToPay_Price;
            TextView daijinquan_price;
            TextView text_zhifu;
            RelativeLayout daijin_layout, banlance_layout;
            // RelativeLayout freelayout;
            RelativeLayout rl_balance_Price;
            RelativeLayout couponlayout;
            TextView tv_balance_Price;
            TextView text_tishi;
        }
    }

    private void goPay(final int position) {
        String couponI = "";
        balanceMoney = "0";
        for (int i = 0; i < pIntegers.size(); i++) {
            if (pIntegers.get(i) == position) {
                couponI = odrIntegers.get(i) + "";
            }
        }
        try {
            if (!liuyanString.equals("")) {
                liuyanString = URLEncoder.encode(liuyanString, "UTF-8");

            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        for (int i = 0; i < pIntegers.size(); i++) {
            if (pIntegers.get(i) == position) {
                pIntegers.remove(i);
                odrIntegers.remove(i);
            }
        }
        MyProgressDialog.show(PayActivity.this, true, true);
        Map<String, String> params2 = new TreeMap<>();
        params2.put("loginkey", user.getUser_Name());
        params2.put("deviceId", MyApplication.sUdid);
        params2.put("couponId", couponI);
        params2.put("orderId", String.valueOf(ordercals.get(position).getOrder_Id()));
        params2.put("remark", liuyanString);
        params2.put("UserMoneyFlag", String.valueOf(UserMoneyFlag));
        MyOkHttpUtils
                .getData(UrlContact.URL_STRING + "/api/pay/paystate",params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("Result")) {
                                Intent intent = new Intent(PayActivity.this, PayActivity2.class);
                                intent.putExtra("payurl", object.getString("Message"));
                                intent.putExtra("productId", (ordercals.get(position).getOrder_Id()) + "");
                                startActivity(intent);
                            } else {
                                initData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static int isShowBalanceDialog = -1;//余额支付弹窗  -1：初始  0：弹出  1：确认支付 2：取消支付 3：退出余额支付 4：进入余额支付

    private void showBalanceDialog() {
        final Dialog dialogT = new AlertDialog.Builder(this)
                .setTitle("系统提醒")
                .setMessage("是否确认使用余额支付")
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                isShowBalanceDialog = 1;
                                goPay(mLastPosition);

                            }

                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isShowBalanceDialog = 2;
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();// 创建

        dialogT.show();
        isShowBalanceDialog = 0;
    }

}
