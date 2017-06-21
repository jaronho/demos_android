package com.example.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.example.classes.DaiGouMingXi;
import com.example.classes.DaiGouMingXiJsonUtil;
import com.example.classes.Daigou;
import com.example.classes.Products1;
import com.example.classes.User;
import com.example.lanuchertest.wegdit.PanelView;
import com.example.nyapp.LoginActivity;
import com.example.nyapp.MainActivity;
import com.example.nyapp.MyApplication;
import com.example.nyapp.MyBangdingPhoneActivity;
import com.example.nyapp.MydaigouWindousDialogActivity;
import com.example.nyapp.R;
import com.example.nyapp.RegisterActivity1;
import com.example.nyapp.TuanGouActivity;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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

public class Fragment_Daigou extends Fragment {
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private Button btn_bangding, btn_daigou;
    private TextView text_tishi;
    private DaiGouMingXi daiGouMingXi;
    private LinearLayout xiaoshi_layout, daigou_libiao;
    private ListView listView1;
    private Button btn_chakan2, btn_regist, btn_login, btn_back;
    private String loginString;
    private ImageLoader imageLoader = null;
    private LinearLayout weidenglu;
    private LinearLayout layout_back;
    private DisplayImageOptions options = null;
    private TextView text_daigou_price, text_daigou_Revenue, text_daigou_todayrevenue, text_freeze, text_cancashout,
            text_cashingout, text_cashedOut;
    DecimalFormat ddf1 = new DecimalFormat("#.00");
    private PanelView panelView;
    private String mUserName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mydaigou, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .build();
        mCache = ACache.get(getActivity());
        user = (User) mCache.getAsObject("user");
        mUserName = user.getUser_Name();
        if (user == null) {
            mUserName = "";
        }
        layout_back = (LinearLayout) getView().findViewById(R.id.layout_back);
        layout_back.setVisibility(View.GONE);
        weidenglu = (LinearLayout) getView().findViewById(R.id.weidenglu);
        btn_login = (Button) getView().findViewById(R.id.btn_login);
        btn_regist = (Button) getView().findViewById(R.id.btn_regist);
        // btn_chakan2 = (Button) getView().findViewById(R.id.btn_chakan2);
        btn_bangding = (Button) getView().findViewById(R.id.btn_bangding);
        btn_daigou = (Button) getView().findViewById(R.id.btn_daigou);
        text_tishi = (TextView) getView().findViewById(R.id.text_tishi);
        xiaoshi_layout = (LinearLayout) getView().findViewById(R.id.xiaoshi_layout);
        daigou_libiao = (LinearLayout) getView().findViewById(R.id.daigou_libiao);
        btn_bangding.setVisibility(View.GONE);
        listView1 = (ListView) getView().findViewById(R.id.listView1);
        btn_back = (Button) getView().findViewById(R.id.btn_back);
        /**
         * 1、我的明细中的头信息 2、总代购金额、总代购收益、今日新增、冻结、未提现、已提现、体现中
         */
        text_daigou_price = (TextView) getView().findViewById(R.id.text_daigou_price);
        text_daigou_Revenue = (TextView) getView().findViewById(R.id.text_daigou_Revenue);
        text_daigou_todayrevenue = (TextView) getView().findViewById(R.id.text_daigou_todayrevenue);
        text_freeze = (TextView) getView().findViewById(R.id.text_freeze);
        text_cancashout = (TextView) getView().findViewById(R.id.text_cancashout);
        text_cashingout = (TextView) getView().findViewById(R.id.text_cashingout);
        text_cashedOut = (TextView) getView().findViewById(R.id.text_cashedOut);

        btn_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_regist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), RegisterActivity1.class);
                intent.putExtra("daigou", 2);
                startActivity(intent);
            }
        });
        btn_daigou.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), TuanGouActivity.class);
                intent.putExtra("payurl", UrlContact.URL_STRING + "/UserCenterView/PurchasingRules");
                intent.putExtra("title", "代购规则");
                startActivity(intent);
            }
        });
        btn_bangding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!daiGouMingXi.isIsBindMobile()) {
                    Intent intent = new Intent(getActivity(), MyBangdingPhoneActivity.class);
                    intent.putExtra("bangding", 1);

                    startActivity(intent);
                } else {
                    MyProgressDialog.show(getActivity(), true, true);
                    Map<String, String> params2 = new TreeMap<>();
                    params2.put("loginKey", mUserName);
                    params2.put("deviceId", MyApplication.sUdid);
                    MyOkHttpUtils
                            .getData(UrlContact.URL_STRING + "/api/UserCenter/DoPurchasingApply", params2)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    MyToastUtil.showShortMessage("服务器连接失败!");
                                    MyProgressDialog.cancel();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    MyProgressDialog.cancel();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Boolean Result = jsonObject.getBoolean("Result");
                                        String message = jsonObject.getString("Message");
                                        if (Result) {
                                            text_tishi.setText(message);
                                            btn_bangding.setVisibility(View.GONE);
                                            layout_back.setVisibility(View.GONE);
                                            btn_back.setVisibility(View.VISIBLE);
                                            btn_back.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View arg0) {
                                                    // stub
                                                    MyProgressDialog.show(getActivity(), true, true);
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    user.setPurchasing_State(1 + "");
                                                    user.setPermit_Type(2 + "");
                                                    mCache.put("user", user);
                                                    startActivity(intent);
                                                    MyProgressDialog.cancel();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                if (daiGouMingXi.getPurchasingState().equals("2")) {
                    Intent intent = new Intent(getActivity(), RegisterActivity1.class);
                    intent.putExtra("flag", 5);
                    startActivity(intent);
                }
            }
        });

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

        @Override
        public View getView(final int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolders viewHolders = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.daigou_item, null);
                viewHolders = new ViewHolders();
                viewHolders.text_no = (TextView) convertView.findViewById(R.id.text_no);
                viewHolders.text_state = (TextView) convertView.findViewById(R.id.text_state);
                viewHolders.text_jine = (TextView) convertView.findViewById(R.id.text_jine);
                viewHolders.text_shouyi = (TextView) convertView.findViewById(R.id.text_shouyi);
                viewHolders.mGallery = (Gallery) convertView.findViewById(R.id.gallery);
                convertView.setTag(viewHolders);
            } else {
                viewHolders = (ViewHolders) convertView.getTag();
            }

			/*
             * 进入我的订单后图片的点击事件（弹出一个浮窗）
			 */
            viewHolders.mGallery.setOnItemClickListener(new OnItemClickListener() {

                @SuppressLint("NewApi")
                @Override
                public void onItemClick(AdapterView<?> arg, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 >= 0) {
                        // panelView = new
                        // PanelView(getActivity(),daiGouMingXi.getItems().get(arg0).getProducts1().get(arg2));
                        Intent intent = new Intent(getActivity(), MydaigouWindousDialogActivity.class);
                        Products1 products1 = daiGouMingXi.getItems().get(arg0).getProducts1().get(arg2);
                        intent.putExtra("Product", (Serializable) products1);
                        startActivity(intent);
                    }
                }
            });
            viewHolders.text_no.setText(daigous.get(arg0).getOrderNo());
            viewHolders.text_state.setText(daigous.get(arg0).getState());
            if (daigous.get(arg0).getMoney() < 1) {
                viewHolders.text_jine.setText("0" + ddf1.format(daigous.get(arg0).getMoney()) + "元");
            } else {
                viewHolders.text_jine.setText(ddf1.format(daigous.get(arg0).getMoney()) + "元");
            }

            if (daigous.get(arg0).getProMoney() < 1) {
                viewHolders.text_shouyi.setText("0" + ddf1.format(daigous.get(arg0).getProMoney()) + "元");

            } else {

                viewHolders.text_shouyi.setText(ddf1.format(daigous.get(arg0).getProMoney()) + "元");
            }
            ImageShowAdapter adapter = new ImageShowAdapter(getActivity(), daigous.get(arg0).getUrList());
            viewHolders.mGallery.setAdapter(adapter);
            return convertView;
        }

        class ViewHolders {
            TextView text_no;
            TextView text_state;
            TextView text_jine;
            TextView text_shouyi;
            Gallery mGallery;
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
            imageLoader.displayImage(list.get(position), i, options);
            i.setLayoutParams(new Gallery.LayoutParams(270, 190));
            return i;
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);
            weidenglu.setVisibility(View.VISIBLE);
            text_tishi.setVisibility(View.GONE);
            xiaoshi_layout.setVisibility(View.GONE);
        } else {
            if (getActivity() != null) {

                if (loginString.equals("true")) {
                    weidenglu.setVisibility(View.GONE);

                    MyProgressDialog.show(getActivity(), true, true);
                    Map<String, String> params2 = new TreeMap<>();
                    params2.put("loginKey", mUserName);
                    params2.put("deviceId", MyApplication.sUdid);
                    params2.put("pageIndex", "1");
                    params2.put("pageSize", "200");
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
                                        JSONObject jsonObject = new JSONObject(response);
                                        Boolean Result = jsonObject.getBoolean("Result");
                                        if (Result) {
                                            daiGouMingXi = DaiGouMingXiJsonUtil.getDaiGouMingXi(response);
                                            if (daiGouMingXi != null) {
                                                if (!daiGouMingXi.isIsBindMobile()) {
                                                    text_tishi.setText("                  您还没有绑定手机，您可以：");
                                                    btn_bangding.setText("绑定手机");
                                                    btn_bangding.setVisibility(View.VISIBLE);

                                                } else {
                                                    text_tishi.setText(daiGouMingXi.getTitle());
                                                    if (daiGouMingXi.getBtnCount() == 2) {
                                                        btn_bangding.setText("申请代购");
                                                        btn_bangding.setVisibility(View.VISIBLE);

                                                    } else {
                                                        btn_bangding.setVisibility(View.GONE);
                                                    }
                                                /*
												 * if (daiGouMingXi
												 * .getPurchasingState()
												 * .equals("0")) { text_tishi
												 * .setText(
												 * "        您的申请已经提交，请耐心等待审核。只有审核通过后再支付的订单才能享受代购收益，您现在为订单支付还不能享受代购收益，请耐心等待审核。"
												 * ); }
												 */
                                                    if (daiGouMingXi.getPurchasingState().equals("1")) {
                                                        text_tishi.setVisibility(View.GONE);
                                                        xiaoshi_layout.setVisibility(View.GONE);
                                                        if (daiGouMingXi.getItems() == null) {
//														text_tishi.setText(
//																"你已成功成为代购！从现在开始，您购买农一网上的品牌农药产品就可以享受代购收益啦。您还可以生成代购推广链接或二维码，发展其他人注册成为代购，他们成功购买时，您同样也可以享受代购收益");
                                                            layout_back.setVisibility(View.GONE);

                                                            btn_back.setVisibility(View.VISIBLE);
                                                            btn_back.setOnClickListener(new OnClickListener() {

                                                                @Override
                                                                public void onClick(View arg0) {
                                                                    // TODO
                                                                    // Auto-generated
                                                                    // method stub
                                                                    MyProgressDialog.show(getActivity(), true, true);
                                                                    Intent intent = new Intent(getActivity(),
                                                                            MainActivity.class);
                                                                    user.setPurchasing_State(1 + "");
                                                                    user.setPermit_Type(2 + "");
                                                                    mCache.put("user", user);
                                                                    startActivity(intent);
                                                                    MyProgressDialog.cancel();
                                                                }
                                                            });
                                                        }

                                                        // 状态是“1”的时候才显示
                                                        if (daiGouMingXi.getStatistics().get(0).getAllMoney() < 1) {
                                                            text_daigou_price.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getAllMoney())
                                                                    + "");

                                                        } else {
                                                            text_daigou_price.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getAllMoney())
                                                                    + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getAllRevenue() < 1) {
                                                            text_daigou_Revenue.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getAllRevenue())
                                                                    + "");

                                                        } else {
                                                            text_daigou_Revenue.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getAllRevenue())
                                                                    + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getTodayRevenue() < 1) {
                                                            text_daigou_todayrevenue.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getTodayRevenue())
                                                                    + "");

                                                        } else {
                                                            text_daigou_todayrevenue.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getTodayRevenue())
                                                                    + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getFreeze() < 1) {
                                                            text_freeze.setText("0"
                                                                    + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getFreeze())
                                                                    + "");

                                                        } else {
                                                            text_freeze.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getFreeze()) + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getCanCashout() < 1) {
                                                            text_cancashout.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCanCashout())
                                                                    + "");

                                                        } else {
                                                            text_cancashout.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCanCashout())
                                                                    + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getCashingOut() < 1) {
                                                            text_cashingout.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCashingOut())
                                                                    + "");

                                                        } else {
                                                            text_cashingout.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCashingOut())
                                                                    + "");

                                                        }
                                                        if (daiGouMingXi.getStatistics().get(0).getCashedOut() < 1) {
                                                            text_cashedOut.setText("0" + ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCashedOut())
                                                                    + "");

                                                        } else {
                                                            text_cashedOut.setText(ddf1.format(
                                                                    daiGouMingXi.getStatistics().get(0).getCashedOut())
                                                                    + "");

                                                        }

                                                        daigou_libiao.setVisibility(View.VISIBLE);
                                                        MyAdapters adapters = new MyAdapters(daiGouMingXi.getItems());
                                                        listView1.setAdapter(adapters);
                                                        listView1.setEmptyView(getView().findViewById(R.id.empty12));
                                                    } else if (daiGouMingXi.getPurchasingState().equals("2")) {
                                                        text_tishi
                                                                .setText("对不起，您的申请被拒绝，或您已被取消代购身份。您可以拨打电话400-819-0345咨询原因。");
                                                        btn_bangding.setVisibility(View.GONE);
                                                    }
                                                }

                                            }
                                        } else {
                                            String message = jsonObject.getString("Message");
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    weidenglu.setVisibility(View.VISIBLE);
                    text_tishi.setVisibility(View.GONE);
                    xiaoshi_layout.setVisibility(View.GONE);
                    daigou_libiao.setVisibility(View.GONE);
                }
            }

        }

    }
}
