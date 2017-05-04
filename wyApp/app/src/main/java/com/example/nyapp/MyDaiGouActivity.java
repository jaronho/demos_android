package com.example.nyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.BaseBean;
import com.example.classes.DaiGouMingXi;
import com.example.classes.DaiGouMingXiJsonUtil;
import com.example.classes.Daigou;
import com.example.classes.Products1;
import com.example.classes.User;
import com.example.lanuchertest.wegdit.PanelView;
import com.example.util.MyListUtil;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class MyDaiGouActivity extends BaseActivity {
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private Button btn_bangding, btn_daigou, btn_back;
    private TextView text_tishi;
    private DaiGouMingXi daiGouMingXi;
    private TextView text_daigou_price, text_daigou_Revenue, text_daigou_todayrevenue, text_freeze, text_cancashout, text_cashingout, text_cashedOut, empty12;
    private LinearLayout xiaoshi_layout, daigou_libiao, daigou_money, mydaigou;
    private ListView listView1;
    //	private Button btn_chakan2;
    private DisplayImageOptions options = null;
    private LinearLayout layout_back;
    DecimalFormat ddf1 = new DecimalFormat("#.00");
    private PanelView panelView;
    private Context mContext;
    private TextView tv_cashedEnter;
    private String loginString;
    private String mUser_name;
    private MyMsgDialog mApplyDialog;

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mydaigou);
        mydaigou = (LinearLayout) findViewById(R.id.mydaigou);
        mContext = this;
        options = new DisplayImageOptions.Builder()
                .displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .build();

        daigou_money = (LinearLayout) findViewById(R.id.daigou_money);
        empty12 = (TextView) findViewById(R.id.empty12);


        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");

        btn_back = (Button) findViewById(R.id.btn_back);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
//		btn_chakan2 = (Button) findViewById(R.id.btn_chakan2);
        btn_bangding = (Button) findViewById(R.id.btn_bangding);
        btn_daigou = (Button) findViewById(R.id.btn_daigou);
        text_tishi = (TextView) findViewById(R.id.text_tishi);

        xiaoshi_layout = (LinearLayout) findViewById(R.id.xiaoshi_layout);
        daigou_libiao = (LinearLayout) findViewById(R.id.daigou_libiao);
        btn_bangding.setVisibility(View.GONE);
        listView1 = (ListView) findViewById(R.id.listView1);

        btn_daigou.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MyDaiGouActivity.this,
                        TuanGouActivity.class);
                intent.putExtra("payurl", UrlContact.URL_STRING
                        + "/UserCenterView/PurchasingRules");
                intent.putExtra("title", "代购规则");
                startActivity(intent);
            }
        });
        btn_bangding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!daiGouMingXi.isIsBindMobile()) {
                    Intent intent = new Intent(MyDaiGouActivity.this,
                            MyBangdingPhoneActivity.class);
                    intent.putExtra("bangding", 1);
                    startActivity(intent);
                } else {
                    MyProgressDialog.show(MyDaiGouActivity.this, true, true);
                    Map<String, String> params2 = new TreeMap<>();
                    params2.put("loginKey", user.getUser_Name());
                    params2.put("deviceId", MyApplication.sUdid);
                    MyOkHttpUtils
                            .getData(UrlContact.URL_STRING + "/api/UserCenter/DoPurchasingApply", params2)
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
                                        JSONObject jsonObject = new JSONObject(
                                                response);
                                        Boolean Result = jsonObject.getBoolean("Result");
                                        if (Result) {
                                            text_tishi.setText("你已成功成为代购！从现在开始，您购买农一网上的品牌农药产品就可以享受代购收益啦。您还可以生成代购推广链接或二维码，发展其他人注册成为代购，他们成功购买时，您同样也可以享受代购收益");
                                            btn_bangding.setVisibility(View.GONE);
                                            layout_back.setVisibility(View.GONE);
                                            btn_back.setVisibility(View.VISIBLE);
                                            btn_back.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View arg0) {
                                                    MyProgressDialog.show(MyDaiGouActivity.this, true, true);
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    user.setPurchasing_State(1 + "");
                                                    user.setPermit_Type(2 + "");
                                                    mCache.put("user", user);
                                                    startActivity(intent);
                                                    MyProgressDialog.cancel();
                                                }
                                            });
                                        } else {
                                            String message = jsonObject.getString("Message");
                                            text_tishi.setText(message);
                                            btn_bangding.setVisibility(View.GONE);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                if (daiGouMingXi.getPurchasingState().equals("2")) {
                    Intent intent = new Intent(MyDaiGouActivity.this,
                            RegisterActivity1.class);
                    intent.putExtra("flag", 5);
                    startActivity(intent);
                }

            }
        });

        /**
         * 1、我的明细中的头信息
         * 2、总代购金额、总代购收益、今日新增、冻结、未提现、已提现、体现中
         */
        text_daigou_price = (TextView) findViewById(R.id.text_daigou_price);
        text_daigou_Revenue = (TextView) findViewById(R.id.text_daigou_Revenue);
        text_daigou_todayrevenue = (TextView) findViewById(R.id.text_daigou_todayrevenue);
        text_freeze = (TextView) findViewById(R.id.text_freeze);
        text_cancashout = (TextView) findViewById(R.id.text_cancashout);
        text_cashingout = (TextView) findViewById(R.id.text_cashingout);
        text_cashedOut = (TextView) findViewById(R.id.text_cashedOut);
        tv_cashedEnter = (TextView) findViewById(R.id.tv_cashedEnter);


    }

    class MyAdapters extends BaseAdapter {
        private List<Daigou> daigous;

        public MyAdapters(List<Daigou> daigous) {
            this.daigous = daigous;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return daigous.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(final int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub


            ViewHolders viewHolders = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MyDaiGouActivity.this)
                        .inflate(R.layout.daigou_item, null);
                viewHolders = new ViewHolders();
                viewHolders.text_no = (TextView) convertView
                        .findViewById(R.id.text_no);
                viewHolders.text_state = (TextView) convertView
                        .findViewById(R.id.text_state);
                viewHolders.text_jine = (TextView) convertView
                        .findViewById(R.id.text_jine);
                viewHolders.text_shouyi = (TextView) convertView
                        .findViewById(R.id.text_shouyi);
                viewHolders.mGallery = (Gallery) convertView
                        .findViewById(R.id.gallery);
//				弹框的信息 start
                viewHolders.content = (LinearLayout) convertView.findViewById(R.id.content);
                viewHolders.product_name = (TextView) convertView.findViewById(R.id.text_prodctname);
                viewHolders.text_spec = (TextView) convertView.findViewById(R.id.text_spec);
                viewHolders.text_price = (TextView) convertView.findViewById(R.id.text_price);
                viewHolders.text_count = (TextView) convertView.findViewById(R.id.text_count);
                viewHolders.text_shouyibili = (TextView) convertView.findViewById(R.id.text_shouyibili);
                viewHolders.text_daigou_price1 = (TextView) convertView.findViewById(R.id.text_daigou_price);
                viewHolders.text_daigoushouyi = (TextView) convertView.findViewById(R.id.text_daigoushouyi);


//				弹窗end


                convertView.setTag(viewHolders);
            } else {
                viewHolders = (ViewHolders) convertView.getTag();
            }
            /*
             * 进入我的订单后图片的点击事件（弹出一个浮窗）
			 */
            final ViewHolders hoders = viewHolders;
            viewHolders.mGallery.setOnItemClickListener(new OnItemClickListener() {

                @SuppressLint("NewApi")
                @Override
                public void onItemClick(AdapterView<?> arg, View arg1,
                                        int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 >= 0) {
                        Products1 products = daiGouMingXi.getItems().get(arg0).getProducts1().get(arg2);
//						panelView = new PanelView(MyDaiGouActivity.this,products);
                        if (products.getCount().equals("")) {
                            products.setCount("---");
                        }
                        if (products.getLucreMoney().equals("")) {
                            products.setLucreMoney("0.00");
                        }
                        if (products.getMoney().equals("")) {
                            products.setMoney("0.00");
                        }
                        if (products.getName().equals("")) {
                            products.setName("---");
                        }
                        if (products.getPrice().equals("")) {
                            products.setPrice("0.00");
                        }
                        if (products.getScale().equals("")) {
                            products.setScale("---");
                        }
                        if (products.getSpec().equals("")) {
                            products.setSpec("---");
                        }
                        Intent intent = new Intent(MyDaiGouActivity.this, MydaigouWindousDialogActivity.class);
                        intent.putExtra("Product", (Serializable) products);
                        startActivity(intent);
                    }
                }
            });


            viewHolders.text_no.setText(daigous.get(arg0).getOrderNo());
            viewHolders.text_state.setText(daigous.get(arg0).getState());

            if (daigous.get(arg0).getMoney() < 1) {
                viewHolders.text_jine.setText("0" + ddf1.format(daigous.get(arg0)
                        .getMoney()) + "元");
            } else {
                viewHolders.text_jine.setText(ddf1.format(daigous.get(arg0)
                        .getMoney()) + "元");
            }

            if (daigous.get(arg0).getProMoney() < 1) {
                viewHolders.text_shouyi.setText("0" + ddf1.format(daigous.get(arg0)
                        .getProMoney()) + "元");

            } else {

                viewHolders.text_shouyi.setText(ddf1.format(daigous.get(arg0)
                        .getProMoney()) + "元");
            }
            ImageShowAdapter adapter = new ImageShowAdapter(
                    getApplicationContext(), daigous.get(arg0).getUrList());
            viewHolders.mGallery.setAdapter(adapter);
            return convertView;
        }

        class ViewHolders {
            TextView text_no;
            TextView text_state;
            TextView text_jine;
            TextView text_shouyi;
            Gallery mGallery;
            private TextView product_name, text_spec, text_price, text_count,
                    text_shouyibili, text_daigou_price1, text_daigoushouyi;
            private LinearLayout content;

        }
    }

    public class ImageShowAdapter extends BaseAdapter {
        private List<String> list;
        private Context mContext;

        public ImageShowAdapter(Context c, List<String> list) {
            mContext = c;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ImageView i = new ImageView(mContext);
            sImageLoader.displayImage(list.get(position), i, options);
            i.setLayoutParams(new Gallery.LayoutParams(270, 190));
            return i;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user.getPermit_Type().equals("1")) {
            getCanApply();
        } else {
            initData();
        }

    }

    //判断是否能够申请代购
    private void getCanApply() {
        if (isLogin()) {
            Map<String, String> map = new TreeMap<>();
            map.put("loginKey", mUser_name);
            map.put("deviceId", MyApplication.sUdid);
            MyOkHttpUtils
                    .getData(UrlContact.URL_CAN_APPLY, map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response != null) {
                                Gson gson = new Gson();
                                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                                if (baseBean.isResult()) {
                                    initData();
                                } else {
                                    mApplyDialog = new MyMsgDialog(MyDaiGouActivity.this, "系统提示", "您的注册地区已签约，如需申请签约，请联系当地工作站或农一网客服。",
                                            new MyMsgDialog.ConfirmListener() {
                                                @Override
                                                public void onClick() {
                                                    MyDaiGouActivity.this.finish();
                                                }
                                            },
                                            null);
                                    mApplyDialog.setCancelable(false);
                                    mApplyDialog.show();
                                }
                            }
                        }
                    });
        }
    }

    private void initData() {
        MyProgressDialog.show(MyDaiGouActivity.this, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", user.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("pageIndex", "1");
        map.put("pageSize", "200");
        MyOkHttpUtils
                .getData(UrlContact.URL_PURCHASING_LIST, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("网络断开连接！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        try {
                            if (!response.equals("") && !response.equals("error")) {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean Result = jsonObject
                                        .getBoolean("Result");
                                if (Result) {
                                    daiGouMingXi = DaiGouMingXiJsonUtil.getDaiGouMingXi(response);
                                    if (!daiGouMingXi.isIsBindMobile()) {
                                        text_tishi.setText("                  您还没有绑定手机，您可以：");
                                        btn_bangding.setText("绑定手机");
                                        btn_bangding.setVisibility(View.VISIBLE);

                                    } else {
                                        if (daiGouMingXi.getPurchasingState().equals("null")
                                                || daiGouMingXi.getPurchasingState().equals("")) {
                                            text_tishi.setText("您还不是农一网代购,您可以:");
                                            btn_bangding.setText("申请代购");
                                            btn_bangding.setVisibility(View.VISIBLE);
                                        }
                                        if (daiGouMingXi.getPurchasingState().equals("1")) {
                                            text_tishi.setVisibility(View.GONE);
                                            xiaoshi_layout.setVisibility(View.GONE);

                                            //状态是“1”的时候才显示
                                            if (daiGouMingXi.getStatistics().get(0).getAllMoney() < 1) {
                                                text_daigou_price.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getAllMoney()) + "");

                                            } else {
                                                text_daigou_price.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getAllMoney()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getAllRevenue() < 1) {
                                                text_daigou_Revenue.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getAllRevenue()) + "");

                                            } else {
                                                text_daigou_Revenue.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getAllRevenue()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getTodayRevenue() < 1) {
                                                text_daigou_todayrevenue.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getTodayRevenue()) + "");

                                            } else {
                                                text_daigou_todayrevenue.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getTodayRevenue()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getFreeze() < 1) {
                                                text_freeze.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getFreeze()) + "");

                                            } else {
                                                text_freeze.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getFreeze()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getCanCashout() < 1) {
                                                text_cancashout.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getCanCashout()) + "");

                                            } else {
                                                text_cancashout.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getCanCashout()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getCashingOut() < 1) {
                                                text_cashingout.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getCashingOut()) + "");

                                            } else {
                                                text_cashingout.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getCashingOut()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getCashedOut() < 1) {
                                                text_cashedOut.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getCashedOut()) + "");

                                            } else {
                                                text_cashedOut.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getCashedOut()) + "");

                                            }
                                            if (daiGouMingXi.getStatistics().get(0).getCashedEnter() < 1) {
                                                tv_cashedEnter.setText("0" + ddf1.format(daiGouMingXi.getStatistics().get(0).getCashedEnter()) + "");

                                            } else {
                                                tv_cashedEnter.setText(ddf1.format(daiGouMingXi.getStatistics().get(0).getCashedEnter()) + "");

                                            }

                                            daigou_libiao.setVisibility(View.VISIBLE);
                                            MyAdapters adapters = new MyAdapters(daiGouMingXi.getItems());
                                            listView1.setAdapter(adapters);
                                            MyListUtil.setListViewHeightBasedOnChildren(listView1);
                                            listView1.setEmptyView(findViewById(R.id.empty12));
                                        } else if (daiGouMingXi.getPurchasingState().equals("2")) {
                                            text_tishi.setText("对不起，您的申请被拒绝，或您已被取消代购身份。您可以拨打电话400-819-0345咨询原因。");
                                            btn_bangding.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    String message = jsonObject.getString("Message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean isLogin() {
        mCache = ACache.get(this);
        loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);
            return false;
        } else {
            if (loginString.equals("true")) {
                user = (User) mCache.getAsObject("user");
                mUser_name = user.getUser_Name();
                return true;
            } else {
                return false;
            }
        }
    }
}
