package com.example.nyapp;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.AddressPovic;
import com.example.classes.AddressPovicJsonUtil;
import com.example.classes.BaseBean;
import com.example.classes.BaseEventBean;
import com.example.classes.LogisticsDetial;
import com.example.classes.MyStockOutPro;
import com.example.classes.MyStockOutProJsonUtil;
import com.example.classes.ShoppingCartBean;
import com.example.classes.User;
import com.example.classes.UserAddress;
import com.example.classes.UserAddressJsonUtil;
import com.example.fragments.Fragment_ShoppingCart;
import com.example.util.GsonUtils;
import com.example.util.MyDateUtils;
import com.example.util.MyListUtil;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.TianjiaCart;
import com.example.util.UrlContact;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.util.EncodingUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

import static android.app.AlertDialog.Builder;
import static com.example.util.MyOkHttpUtils.getData;

public class DingdanActivity extends BaseActivity {
    private LinearLayout peisong, peisong_linear, peisong_xiaoshi;
    private TextView text_peisong, text_fapiao;
    private int logistaisc_id;
    private LinearLayout shouhuoren_linear, shouhuoren_xiaoshi, shouhuoren;
    private TextView text_name, text_address;
    //	private Map<String, FreePriceJson> map;
    private ListView listView2;
    private LinearLayout newaddress_linear, newaddress_xiaoshi;
    private Button btn_newaddress;
    private TextView text_back;
    private TextView edit_povic, edit_city, edit_area, edit_towns, edit_vaillage;
    private PopupWindow popupwindow;
    private List<AddressPovic> addresses, address1s, address2s, address3s, address4s;
    private boolean isAddress = false;
    private MyStockOutPro myStockOutPro;
    private List<LogisticsDetial> logisticsDetials;
    private String myStockOutProMessage;
    private boolean myStockOutProBoolea;
    Map<String, String> paramsStockOut = new HashMap<String, String>();
    private Button btn_baocun;
    private DisplayImageOptions options = null;
    private TextView edit_newiphone, edit_newname, edit_newaddress;
    private ListView listView1;
    private JSONArray jsonArray;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private Button btn_tijiao;
    private List<ShoppingCartBean.DataBean.ProductBean> listCarts;
    private double zhongjia;
    private String _freeprice;
    private User user;
    private UserAddress userAddresses;
    private List<UserAddress> lUserAddresses;
    private int Province_Id;
    private int City_Id;
    private int Area_Id;
    private int towns_Id;
    private int vaillage_Id;
    private int newAddressid;
    private int Id_addres;
    private TextView text_zongjia, product_zongjia;
    private TextView textFreePrice;
    private RelativeLayout freelayout;
    private LinearLayout layout_back;
    String stringUrl = UrlContact.URL_STRING + "/api/order/GetOffsetRule";
    DecimalFormat ddf1 = new DecimalFormat("#.00");
    private JSONArray jsonArray2 = new JSONArray();
    private ViewHolder hold;
    private Boolean or_idAddres = false;
    private Boolean or_idAddres_preserve = false;
    private ListView logistics_list;//配送方式列表
    private TextView product_peisong;//配送费用
    private LinearLayout mLl_peisong_time;
    private TextView mTv_peisong_time;
    private int year;
    private int month;
    private int day;
    private List<Boolean> mIs_presell_list;
    private List<String> mDateList;
    private DatePickerDialog mDatePickerDialog;
    private LinearLayout ll_returnShoppingCar;
    private String mString;
    private MyMsgDialog mCompoundDialog;
    private MyMsgDialog mApplyDialog;
    private MyMsgDialog mAddressDialog;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dingdan);
        zhongjia = getIntent().getDoubleExtra("zongjia", 0);
        _freeprice = getIntent().getStringExtra("_freePrice");
        mString = getIntent().getStringExtra("from");
        initView();
        initData();
        setPeiSongtime();//获取配送时间
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("loginKey", user.getUser_Name());
        params.put("deviceId", MyApplication.sUdid);

        MyOkHttpUtils
                .postData(UrlContact.URL_STRING + "/api/Order/CheckOut", params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // 获取服务端的用户收货地址
                            userAddresses = UserAddressJsonUtil.getUserAddress(response);
                            if (jsonObject.getBoolean("Result")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("Data");
                                Boolean isApplying = jsonObject1.getBoolean("IsApplying");
                                String message = jsonObject1.getString("applyMessage");
                                String invoiceMessage = jsonObject1.getString("invoiceMessage");
                                text_fapiao.setText(invoiceMessage);
                                if (userAddresses.getId() == 0) {
                                    newaddress_linear.setVisibility(View.VISIBLE);

                                } else {
                                    newAddressid = userAddresses.getId();
                                    text_name.setText(userAddresses.getReceive_Name());
//								text_phone.setText(userAddresses.getReceive_Phone());
                                    if (userAddresses.getTown_Name().equals("null") && userAddresses.getTown_Name() == null) {
                                        text_address.setText(userAddresses.getReceive_Phone() + " " + userAddresses.getProvince_Name() + userAddresses.getCity_Name()
                                                + userAddresses.getCounty_Name() + userAddresses.getReceive_Address());
                                    } else {
                                        text_address.setText(userAddresses.getReceive_Phone() + " " + userAddresses.getProvince_Name() + userAddresses.getCity_Name()
                                                + userAddresses.getCounty_Name() + userAddresses.getTown_Name()
                                                + userAddresses.getVaillage_Name() + userAddresses.getReceive_Address());
                                    }
                                    user.setProvince_Id(userAddresses.getProvince_Id());
//                                user.setProvinceName(userAddresses.getProvince_Name());
                                    user.setCity_Id(userAddresses.getCity_Id());
//                                user.setCityName(userAddresses.getCity_Name());
                                    user.setCounty_Id(userAddresses.getCounty_Id());
                                    user.setCountyName(userAddresses.getCounty_Name());

                                    // 判断是否有四级五级地址（有：显示 没有：弹框提示添加）
                                    if (userAddresses.getState() == 2) {
                                        // 弹窗提示要修改收货地址
                                        Province_Id = userAddresses.getProvince_Id();
                                        City_Id = userAddresses.getCity_Id();
                                        Area_Id = userAddresses.getCounty_Id();
                                        Id_addres = userAddresses.getId();
                                        showDialog_address("请完善收货地址！", userAddresses.getReceive_Name(),
                                                userAddresses.getReceive_Phone(), userAddresses.getProvince_Name(),
                                                userAddresses.getCity_Name(), userAddresses.getCounty_Name(),
                                                userAddresses.getReceive_Address());
                                    } else {
                                        user.setTown_Id(userAddresses.getTown_Id());
//                                    user.setTown_Name(userAddresses.getTown_Name());
                                        user.setVaillage_Id(userAddresses.getVaillage_Id());
//                                    user.setVaillage_Name(userAddresses.getVaillage_Name());
                                    }
                                    // 获取到收货人信息 并缓存
                                    mCache.put("user", user);
                                    paramsStockOut.put("CartItem", jsonArray2.toString());
                                    paramsStockOut.put("couId", user.getCounty_Id() + "");
                                    paramsStockOut.put("townId", user.getTown_Id() + "");
                                    paramsStockOut.put("provinceId", user.getProvince_Id() + "");
                                    paramsStockOut.put("loginKey", user.getUser_Name());
                                    paramsStockOut.put("deviceId", MyApplication.sUdid);
                                    paramsStockOut.put("villageId", user.getVaillage_Id() + "");
                                    getStockOut1(paramsStockOut, listCarts);
                                }
                                if (isApplying) {
                                    showDialog(message);
                                }

                            } else {
                                if (!new JSONObject(response).getBoolean("IsLogin")) {
                                    Intent intent = new Intent(DingdanActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    int currentYear = year;
    int currentMonth = month;
    int currentDay = day;

    private void setPeiSongtime() {
        //初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.add(Calendar.DATE, 7);
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH) + 1;//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

        mLl_peisong_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = minDate();
                //显示当前的年月日后的第7天
                mDatePickerDialog = new DatePickerDialog(DingdanActivity.this, R.style.DatePicker_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int newYear, int newMonth, int newDay) {
                        String date = "";
                        if (newMonth + 1 < 10) {
                            date = newYear + "-0" + (newMonth + 1); //显示当前的年月日后的第7天
                        } else {
                            date = newYear + "-" + (newMonth + 1);
                        }
                        if (newDay < 10) {
                            date = date + "-0" + newDay;
                        } else {
                            date = date + "-" + newDay;
                        }
                        mTv_peisong_time.setText(date);
                        currentYear = newYear;
                        currentMonth = newMonth + 1;
                        currentDay = newDay;
                    }
                }, currentYear, currentMonth - 1, currentDay);
                mDatePickerDialog.getDatePicker().setMinDate(1000 * MyDateUtils.getString2Date(year + "-" + month + "-" + day));
                mDatePickerDialog.getDatePicker().setMaxDate(1000 * MyDateUtils.getString2Date(date));
                mDatePickerDialog.show();
            }
        });
    }

    private String minDate() {
        String minDate = "0";
        if (mIs_presell_list.size() > 0) {
            minDate = mDateList.get(0);

            for (int i = 0; i < mDateList.size(); i++) {

                if (!MyDateUtils.judgeTime2Time2(minDate, mDateList.get(i))) {
                    minDate = mDateList.get(i);
                }
            }

            return minDate;
        }
        return minDate;
    }

    private JSONArray getArray(List<ShoppingCartBean.DataBean.ProductBean> list) {

        for (int i = 0; i < list.size(); i++) {
            JSONObject yosonJsonObject = new JSONObject();
            try {
                yosonJsonObject.put("Id", list.get(i).getPro_Id());

                yosonJsonObject.put("Count", list.get(i).getCount());
                yosonJsonObject.put("type", list.get(i).getMarketing_Type());
                jsonArray2.put(yosonJsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonArray2;
    }

    private JSONArray getArray2(List<ShoppingCartBean.DataBean.ProductBean> list) {

        for (int i = 0; i < list.size(); i++) {
            JSONObject yosonJsonObject = new JSONObject();
            try {
                yosonJsonObject.put("Id", list.get(i).getPro_Id());

                yosonJsonObject.put("Count", list.get(i).getCount());
                yosonJsonObject.put("type", list.get(i).getMarketing_Type());
                jsonArray2.put(yosonJsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonArray2;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.example.nyapp.BaseActivity#initView() 对话框显示是否通过审核
     */
    //显示申请结果
    private void showDialog(String message) {
        mApplyDialog = new MyMsgDialog(this, "温馨提示", message,
                new MyMsgDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mApplyDialog.dismiss();
                    }
                }, null);
        mApplyDialog.show();
    }

    // 修改收货地址信息
    private void showDialog_address(String message, final String receive_Name, final String receive_Phone,
                                    final String province_Name, final String city_Name, final String county_Name, final String receive_Addres) {
        mAddressDialog = new MyMsgDialog(this, "温馨提示", message,
                new MyMsgDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mAddressDialog.dismiss();
                        newaddress_linear.setVisibility(View.VISIBLE);
                        edit_newname.setText(receive_Name);
                        edit_newiphone.setText(receive_Phone);
                        edit_povic.setText(province_Name);
                        edit_city.setText(city_Name);

                        edit_towns.setHint("请选择（乡镇）");
                        edit_towns.setText("");
                        edit_vaillage.setHint("请选择（村/街道）");
                        edit_vaillage.setText("");
                        edit_newaddress.setText(receive_Addres);
                        if (county_Name.equals("市辖区")) {
                            edit_area.setHint("请选择（县）");
                            edit_area.setText("");
                        } else {
                            edit_area.setText(county_Name);
                        }
                        or_idAddres_preserve = true;
                        or_idAddres = true;
                    }
                }, null);
        mAddressDialog.show();
    }

    @Override
    public void initView() {
        text_zongjia = (TextView) findViewById(R.id.text_zongjia);
        product_zongjia = (TextView) findViewById(R.id.product_zongjia);
        ll_returnShoppingCar = (LinearLayout) findViewById(R.id.ll_returnShoppingCar);
        freelayout = (RelativeLayout) findViewById(R.id.freelayout);
        textFreePrice = (TextView) findViewById(R.id.textFreePrice);
        text_fapiao = (TextView) findViewById(R.id.text_fapiao);
        logistics_list = (ListView) findViewById(R.id.logistics_list);
        product_peisong = (TextView) findViewById(R.id.product_peisong);
        mLl_peisong_time = (LinearLayout) findViewById(R.id.ll_peisong_time);
        mTv_peisong_time = (TextView) findViewById(R.id.tv_peisong_time);

        if (_freeprice.equals(".00")) {
            textFreePrice.setText("0.00元");
            freelayout.setVisibility(View.GONE);
        } else {
            freelayout.setVisibility(View.VISIBLE);
            if (Double.valueOf(_freeprice) >= 1) {
                textFreePrice.setText("-" + _freeprice + "元");

            } else {
                textFreePrice.setText("-0" + ddf1.format(Double.valueOf(_freeprice)) + "元");
            }
        }
        ll_returnShoppingCar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        if (user == null || user.getUser_Name() == null) {
            Intent intent = new Intent(DingdanActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        jsonArray = mCache.getAsJSONArray("testJsonArray");

        if (jsonArray == null) {
        } else {

        }
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        btn_tijiao = (Button) findViewById(R.id.btn_tijiao);
        btn_tijiao.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {

                final Map<String, String> params = new HashMap<String, String>();
                params.put("type", 20 + "");
                params.put("loginKey", user.getUser_Name());
                params.put("deviceId", MyApplication.sUdid);
                params.put("CartItem", jsonArray2.toString());
                params.put("deliveryType", logistaisc_id + "");
                params.put("TimeStamp", mTv_peisong_time.getText().toString().trim());
//                params.put("TimeStamp", "");
//                if (mDateList.size() > 0) {
//                    if (mTv_peisong_time.getText().toString().trim().isEmpty()) {
//                        Toast.makeText(MyApplication.getContextObject(), "您的订单有预售商品，请选择配送时间", Toast.LENGTH_SHORT).show();
////                        mLl_peisong_time.performClick();
//                        return;
//                    }
//
//                }
                if (myStockOutPro == null) {
                    Toast.makeText(getApplicationContext(), "收货人信息不能为空", Toast.LENGTH_LONG).show();

                } else {
                    if (myStockOutProBoolea && myStockOutPro.isHasStockOut()) {
                        btn_tijiao.setBackgroundColor(0x7fcccccc);
                        btn_tijiao.setEnabled(false);
                        showDialogGoods(myStockOutProMessage, myStockOutPro.getStockOutPro());
                        listView1.setAdapter(new MyListAdper(listCarts));
                    } else {
                        if (userAddresses != null) {
                            params.put("receiveId", newAddressid + "");
                            btn_tijiao.setBackgroundColor(0xffff4b00);
                            btn_tijiao.setEnabled(true);

                            listView1.setAdapter(new MyListAdper(listCarts));
                            SubmitOrder(params);

                            // ---------------------end
                        } else {
                            Toast.makeText(getApplicationContext(), "请选择正确的收货地址", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            }

        });
        listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(DingdanActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", listCarts.get(arg2).getPro_Id());
                intent.putExtra("name", listCarts.get(arg2).getPro_Name());
                intent.putExtra("type", listCarts.get(arg2).getType());
                startActivity(intent);
            }
        });


        edit_povic = (TextView) findViewById(R.id.edit_povic);
        edit_city = (TextView) findViewById(R.id.edit_city);

        edit_area = (TextView) findViewById(R.id.edit_area);
        edit_towns = (TextView) findViewById(R.id.edit_towns);
        edit_vaillage = (TextView) findViewById(R.id.edit_vaillage);
        new GetNewsTask().execute("0");
        options = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();


        if (mString.equals("first")) {
//            listCarts = Fragment_Shopping_Cart.items2;
//            getArray(listCarts);

        } else if (mString.equals("fragment")) {
            listCarts = Fragment_ShoppingCart.items2;
            getArray2(listCarts);
        } else if (mString.equals("ProductDetailActivity")) {
            listCarts = ProductDetailActivity.items2;
            getArray(listCarts);
        } else {
            listCarts = ShoppingCarActivity.items2;
            if (listCarts.size() != 0) {
                getArray(listCarts);
            }
        }

        peisong = (LinearLayout) findViewById(R.id.peisong);
        peisong_linear = (LinearLayout) findViewById(R.id.peisong_linear);
        peisong_linear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });

        peisong_xiaoshi = (LinearLayout) findViewById(R.id.peisong_xiaoshi);
        text_peisong = (TextView) findViewById(R.id.text_peisong);

        peisong.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                peisong_linear.setVisibility(View.VISIBLE);
            }
        });
        /**
         * 2015年9月17日屏蔽掉物流费后付
         */
        peisong_xiaoshi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                peisong_linear.setVisibility(View.GONE);

            }

        });
        shouhuoren = (LinearLayout) findViewById(R.id.shouhuoren);
        shouhuoren_linear = (LinearLayout) findViewById(R.id.shouhuoren_linear);
        shouhuoren_xiaoshi = (LinearLayout) findViewById(R.id.shouhuoren_xiaoshi);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                isAddress = true;
                shouhuoren_linear.setVisibility(View.GONE);
                text_name.setText(lUserAddresses.get(arg2).getReceive_Name());
//				text_phone.setText();
                if (!lUserAddresses.get(arg2).getTown_Name().equals("null")) {
                    text_address.setText(lUserAddresses.get(arg2).getReceive_Phone() + " " + lUserAddresses.get(arg2).getProvince_Name()
                            + lUserAddresses.get(arg2).getCity_Name() + lUserAddresses.get(arg2).getCounty_Name()
                            + lUserAddresses.get(arg2).getTown_Name() + lUserAddresses.get(arg2).getVaillage_Name()
                            + lUserAddresses.get(arg2).getReceive_Address());
                    user.setTown_Id(lUserAddresses.get(arg2).getTown_Id());
//                    user.setTown_Name(lUserAddresses.get(arg2).getTown_Name());
                    user.setVaillage_Id(lUserAddresses.get(arg2).getVaillage_Id());
//                    user.setVaillage_Name(lUserAddresses.get(arg2).getVaillage_Name());

                } else {
                    text_address.setText(lUserAddresses.get(arg2).getReceive_Phone() + " " + lUserAddresses.get(arg2).getProvince_Name()
                            + lUserAddresses.get(arg2).getCity_Name() + lUserAddresses.get(arg2).getCounty_Name()
                            + lUserAddresses.get(arg2).getReceive_Address());
                }
                newAddressid = lUserAddresses.get(arg2).getId();
                user.setProvince_Id(lUserAddresses.get(arg2).getProvince_Id());
//                user.setProvinceName(lUserAddresses.get(arg2).getProvince_Name());
                user.setCity_Id(lUserAddresses.get(arg2).getCity_Id());
//                user.setCityName(lUserAddresses.get(arg2).getCity_Name());
                user.setCounty_Id(lUserAddresses.get(arg2).getCounty_Id());
                user.setCountyName(lUserAddresses.get(arg2).getCounty_Name());
                mCache.put("user", user);
                if (lUserAddresses.get(arg2).getState() == 2) {
                    // 跳转修改地址信息
                    Province_Id = lUserAddresses.get(arg2).getProvince_Id();
                    City_Id = lUserAddresses.get(arg2).getCity_Id();
                    Area_Id = lUserAddresses.get(arg2).getCounty_Id();
                    Id_addres = lUserAddresses.get(arg2).getId();
                    showDialog_address("请完善地址！", lUserAddresses.get(arg2).getReceive_Name(),
                            lUserAddresses.get(arg2).getReceive_Phone(), lUserAddresses.get(arg2).getProvince_Name(),
                            lUserAddresses.get(arg2).getCity_Name(), lUserAddresses.get(arg2).getCounty_Name(),
                            lUserAddresses.get(arg2).getReceive_Address());
                } else {
                    Map<String, String> pram = new HashMap<String, String>();
                    pram.put("loginKey", user.getUser_Name());
                    pram.put("deviceId", MyApplication.sUdid);
                    pram.put("receiveId", lUserAddresses.get(arg2).getId() + "");
                    getUINewAddress(pram);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("CartItem", jsonArray2.toString());
                    params.put("couId", lUserAddresses.get(arg2).getCounty_Id() + "");
                    params.put("townId", lUserAddresses.get(arg2).getTown_Id() + "");
                    params.put("provinceId", lUserAddresses.get(arg2).getProvince_Id() + "");
                    params.put("loginKey", user.getUser_Name());
                    params.put("deviceId", MyApplication.sUdid);
                    params.put("villageId", lUserAddresses.get(arg2).getVaillage_Id() + "");
                    getStockOut(params);
                }
            }
        });
        shouhuoren.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getShouhuoren();
            }
        });
        shouhuoren_xiaoshi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shouhuoren_linear.setVisibility(View.GONE);
            }
        });
        text_back = (TextView) findViewById(R.id.text_back);
        newaddress_linear = (LinearLayout) findViewById(R.id.newaddress_linear);
        newaddress_xiaoshi = (LinearLayout) findViewById(R.id.newaddress_xiaoshi);
        btn_newaddress = (Button) findViewById(R.id.btn_newaddress);
        btn_baocun = (Button) findViewById(R.id.btn_baocun);
        btn_newaddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newaddress_linear.setVisibility(View.VISIBLE);
                shouhuoren_linear.setVisibility(View.GONE);
                edit_newname.setText("");
                edit_newiphone.setText("");
                edit_povic.setText("");
                edit_povic.setHint("请选择（省）");
                edit_city.setHint("请选择（市）");
                edit_city.setText("");
                edit_area.setHint("请选择（县）");
                edit_area.setText("");
                edit_towns.setHint("请选择（乡镇）");
                edit_towns.setText("");
                edit_vaillage.setHint("请选择（村/街道）");
                edit_vaillage.setText("");
                edit_newaddress.setHint("详细地址");
                edit_newaddress.setText("");
            }
        });
        newaddress_xiaoshi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                newaddress_linear.setVisibility(View.GONE);
                shouhuoren_linear.setVisibility(View.GONE);
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    if (or_idAddres == true) {
                        or_idAddres = false;
                    }
                    if (or_idAddres_preserve == true) {
                        or_idAddres_preserve = false;
                    }
                }

            }
        });
        text_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    if (or_idAddres == true) {
                        or_idAddres = false;
                    }
                    if (or_idAddres_preserve == true) {
                        or_idAddres_preserve = false;
                    }
                }

                if (userAddresses == null) {
                    newaddress_linear.setVisibility(View.GONE);
                    shouhuoren_linear.setVisibility(View.GONE);
                } else {
                    newaddress_linear.setVisibility(View.GONE);
                    shouhuoren_linear.setVisibility(View.VISIBLE);
                }

            }
        });
        text_name = (TextView) findViewById(R.id.text_name);
//		text_phone = (TextView) findViewById(R.id.text_phone);
        edit_newaddress = (TextView) findViewById(R.id.edit_newaddress);
        edit_newiphone = (TextView) findViewById(R.id.edit_newiphone);
        edit_newname = (TextView) findViewById(R.id.edit_newname);
        text_address = (TextView) findViewById(R.id.text_address);

        btn_baocun.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isAddress = true;
                if (isMobile(edit_newiphone.getText().toString())) {
                    if (!edit_newname.getText().toString().equals("") && !edit_newiphone.getText().toString().equals("")
                            && !edit_povic.getText().toString().equals("") && !edit_city.getText().toString().equals("")
                            && !edit_area.getText().toString().equals("")
                            && !edit_newaddress.getText().toString().equals("")
                            && !edit_towns.getText().toString().equals("")
                            && !edit_vaillage.getText().toString().equals("")) {
                        text_name.setText(edit_newname.getText().toString());
//						text_phone.setText(edit_newiphone.getText().toString());
                        text_address.setText(edit_newiphone.getText().toString() + " " + edit_povic.getText().toString() + edit_city.getText().toString()
                                + edit_area.getText().toString() + edit_towns.getText().toString()
                                + edit_vaillage.getText().toString() + edit_newaddress.getText().toString());
                        newaddress_linear.setVisibility(View.GONE);
                        shouhuoren_linear.setVisibility(View.GONE);
                        user.setProvince_Id(Province_Id);
//                        user.setProvinceName(edit_povic.getText().toString());
                        user.setCity_Id(City_Id);
//                        user.setCityName(edit_city.getText().toString());
                        user.setCounty_Id(Area_Id);
                        user.setCountyName(edit_area.getText().toString());
                        user.setTown_Id(towns_Id);
//                        user.setTown_Name(edit_towns.getText().toString());
                        user.setVaillage_Id(vaillage_Id);
//                        user.setVaillage_Name(edit_vaillage.getText().toString());
                        mCache.put("user", user);
                        // ---

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("loginKey", user.getUser_Name());
                        params.put("deviceId", MyApplication.sUdid);
                        params.put("Province_Id", Province_Id + "");
                        params.put("Province_Name", edit_povic.getText().toString());
                        params.put("City_Id", City_Id + "");
                        params.put("City_Name", edit_city.getText().toString());
                        params.put("County_Id", Area_Id + "");
                        params.put("County_Name", edit_area.getText().toString());
                        params.put("Receive_Address", edit_newaddress.getText().toString());
                        params.put("Receive_Name", edit_newname.getText().toString());
                        params.put("Receive_Phone", edit_newiphone.getText().toString());
                        params.put("Town_Id", towns_Id + "");
                        params.put("Town_Name", edit_towns.getText().toString());
                        params.put("Village_Id", vaillage_Id + "");
                        params.put("Village_Name", edit_vaillage.getText().toString());
                        if (or_idAddres_preserve) {
                            params.put("Id", Id_addres + "");
                            or_idAddres_preserve = false;
                        } else {
                            params.put("Id", "");
                        }

                        MyOkHttpUtils
                                .postData(UrlContact.URL_STRING + "/api/Order/AddAddress", params)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        MyToastUtil.showShortMessage("服务器连接失败!");
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("Result")) {
                                                newAddressid = jsonObject.getInt("Data");
                                            } else {
                                                MyToastUtil.showShortMessage(jsonObject.getString("Message"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Map<String, String> params1 = new HashMap<String, String>();
                        params1.put("CartItem", jsonArray2.toString());
                        params1.put("couId", Area_Id + "");
                        params1.put("townId", towns_Id + "");
                        params1.put("provinceId", Province_Id + "");
                        params1.put("loginKey", user.getUser_Name());
                        params1.put("deviceId", MyApplication.sUdid);
                        params1.put("villageId", vaillage_Id + "");
                        getStockOut(params1);
                    } else {
                        Toast.makeText(getApplicationContext(), "请全部填写！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "手机格式不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //提交订单
    private void SubmitOrder(Map<String, String> params) {
        MyProgressDialog.show(DingdanActivity.this, true, true);
        MyOkHttpUtils
                .postData(UrlContact.URL_SUBMIT_ORDER, params)
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
                        if (response != null) {
                            BaseBean baseBean = GsonUtils.getInstance().fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                for (int i = 0; i < listCarts.size(); i++) {
                                    if (jsonArray == null)
                                        continue;

                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject1;
                                        try {
                                            jsonObject1 = jsonArray.getJSONObject(j);
                                            if (listCarts.get(i).getPro_Id() == jsonObject1.getInt("Id")) {
                                                TianjiaCart.delete(j, jsonArray);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    mCache.put("testJsonArray", jsonArray);
                                }
                                EventBus.getDefault().post(new BaseEventBean(2, 3));//刷新购物车
                                if (baseBean.getMessage().equals("复合肥")) {
                                    showCompoundDialog();
                                } else {
                                    Intent intent = new Intent(DingdanActivity.this, PayActivity.class);
                                    intent.putExtra("dingdan", baseBean.getData());//订单ID
                                    intent.putExtra("_freePrice", _freeprice);
                                    intent.putExtra("isSecKill", mString);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                MyToastUtil.showShortMessage(baseBean.getMessage());
                            }
                        }
                    }
                });
    }

    //显示复合肥下单说明
    private void showCompoundDialog() {
        String message = "您的订单含有需要单独核算运费的商品，尚不能支付，请联系农一网客服400-11-16899为您核算运费。";
        mCompoundDialog = new MyMsgDialog(this, false, "系统提示", message, new MyMsgDialog.ConfirmListener() {
            @Override
            public void onClick() {
                mCompoundDialog.dismiss();
                Intent intent = new Intent(DingdanActivity.this, MyDingDanActivity.class);
                intent.putExtra("State", 0);
                intent.putExtra("title", "我的订单");
                startActivity(intent);
                finish();
            }
        }, null);
        mCompoundDialog.setCancelable(false);
        mCompoundDialog.show();
    }

    /**
     * 重写onTouchEvent方法
     *
     * @author nongyi-lenovo
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupwindow != null && popupwindow.isShowing()) {
            popupwindow.dismiss();
            // Toast.makeText(getApplicationContext(), "touch",
            // Toast.LENGTH_SHORT).show();
            popupwindow = null;
        }
        return super.onTouchEvent(event);
    }

    private class MyAdapter extends BaseAdapter {
        List<UserAddress> list;

        public MyAdapter(List<UserAddress> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int arg0, View convertView, ViewGroup arg2) {
            Viewholder viewholder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.shouhuoren, null);
                viewholder = new Viewholder();
                viewholder.text_names = (TextView) convertView.findViewById(R.id.text_names);
                viewholder.text_or = (TextView) convertView.findViewById(R.id.text_or);
                viewholder.text_phones = (TextView) convertView.findViewById(R.id.text_phones);
                viewholder.text_addresss = (TextView) convertView.findViewById(R.id.text_addresss);
                viewholder.delete_addres = (TextView) convertView.findViewById(R.id.delete_addres);
                viewholder.editor = (TextView) convertView.findViewById(R.id.editor);
                convertView.setTag(viewholder);
            } else {
                viewholder = (Viewholder) convertView.getTag();
            }
            viewholder.text_names.setText(list.get(arg0).getReceive_Name());
            viewholder.text_phones.setText(list.get(arg0).getReceive_Phone());
            // 添加（乡镇 村/街道）
            if (!list.get(arg0).getTown_Name().equals("null")) {
                viewholder.text_or.setVisibility(View.GONE);
                if (!list.get(arg0).getVaillage_Name().equals("null")) {
                    viewholder.text_addresss.setText(list.get(arg0).getProvince_Name() + list.get(arg0).getCity_Name()
                            + list.get(arg0).getCounty_Name() + list.get(arg0).getTown_Name()
                            + list.get(arg0).getVaillage_Name() + list.get(arg0).getReceive_Address());
                } else {
                    viewholder.text_addresss.setText(list.get(arg0).getProvince_Name() + list.get(arg0).getCity_Name()
                            + list.get(arg0).getCounty_Name() + list.get(arg0).getTown_Name()
                            + list.get(arg0).getReceive_Address());
                }
            } else {
                viewholder.text_or.setVisibility(View.VISIBLE);
                viewholder.text_addresss.setText(list.get(arg0).getProvince_Name() + list.get(arg0).getCity_Name()
                        + list.get(arg0).getCounty_Name() + list.get(arg0).getReceive_Address());
            }
            viewholder.editor.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 编辑所选中的地址
                    shouhuoren_linear.setVisibility(View.GONE);
                    newaddress_linear.setVisibility(View.VISIBLE);
                    Id_addres = list.get(arg0).getId();
                    edit_newname.setText(list.get(arg0).getReceive_Name());
                    edit_newiphone.setText(list.get(arg0).getReceive_Phone());
                    edit_povic.setText(list.get(arg0).getProvince_Name());
                    edit_city.setText(list.get(arg0).getCity_Name());
                    edit_newaddress.setText(list.get(arg0).getReceive_Address());
                    Province_Id = list.get(arg0).getProvince_Id();
                    City_Id = list.get(arg0).getCity_Id();
                    if (list.get(arg0).getCounty_Name().equals("市辖区")) {
                        edit_area.setHint("请选择（县）");
                        edit_area.setText("");
                        edit_towns.setHint("请选择（乡镇）");
                        edit_towns.setText("");
                        edit_vaillage.setHint("请选择（村/街道）");
                        edit_vaillage.setText("");
                    } else {
                        Area_Id = list.get(arg0).getCounty_Id();
                        edit_area.setText(list.get(arg0).getCounty_Name());

                        if (!list.get(arg0).getTown_Name().equals("null")) {
                            towns_Id = list.get(arg0).getTown_Id();
                            edit_towns.setText(list.get(arg0).getTown_Name());
                            edit_vaillage.setHint("请选择（村/街道）");
                            edit_vaillage.setText("");
                            if (!list.get(arg0).getVaillage_Name().equals("null")) {
                                vaillage_Id = list.get(arg0).getVaillage_Id();
                                edit_vaillage.setText(list.get(arg0).getVaillage_Name());
                            } else {
                                edit_vaillage.setHint("请选择（村/街道）");
                                edit_vaillage.setText("");
                            }
                        } else {
                            edit_towns.setHint("请选择（乡镇）");
                            edit_towns.setText("");
                            edit_vaillage.setHint("请选择（村/街道）");
                            edit_vaillage.setText("");
                        }
                    }
                    or_idAddres = true;
                    or_idAddres_preserve = true;
                }
            });
            viewholder.delete_addres.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 传id删除
                    Map<String, String> map = new TreeMap<>();
                    map.put("loginKey", user.getUser_Name());
                    map.put("deviceId", MyApplication.sUdid);
                    map.put("addrId", String.valueOf(list.get(arg0).getId()));
                    String url = UrlContact.URL_STRING + "/api/Order/DelAddress?loginKey=" + user.getUser_Name()
                            + "&deviceId=" + MyApplication.sUdid + "&addrId=" + list.get(arg0).getId();
                    deleteAddres(map);

                }
            });
            return convertView;
        }

        class Viewholder {
            TextView text_names, text_or;
            TextView text_phones;
            TextView text_addresss;
            TextView delete_addres, editor;
        }
    }

    // 删除地址信息
    private void deleteAddres(Map<String, String> params) {
        getData(UrlContact.URL_STRING + "/api/Order/DelAddress", params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            String message = jsonObject.getString("Message");
                            if (Result) {
                                getShouhuoren();
                                Toast.makeText(DingdanActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initmPopupWindowView() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 430, ViewGroup.LayoutParams.FILL_PARENT);
        ListView listView = (ListView) customView.findViewById(R.id.list_povic);
        MyAdapter1 adapter = new MyAdapter1();
        listView.setAdapter(adapter);

    }

    public void initmPopupWindowView2() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 430, ViewGroup.LayoutParams.FILL_PARENT);
        ListView listView = (ListView) customView.findViewById(R.id.list_povic);
        MyAdapter2 adapter = new MyAdapter2();
        listView.setAdapter(adapter);

    }

    public void initmPopupWindowView3() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 430, ViewGroup.LayoutParams.FILL_PARENT);
        ListView listView = (ListView) customView.findViewById(R.id.list_povic);
        MyAdapter3 adapter = new MyAdapter3();
        listView.setAdapter(adapter);
    }

    public void initmPopupWindowView4() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 430, ViewGroup.LayoutParams.FILL_PARENT);
        ListView listView = (ListView) customView.findViewById(R.id.list_povic);
        MyAdapter4 adapter = new MyAdapter4();
        listView.setAdapter(adapter);
    }

    public void initmPopupWindowView5() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 430, ViewGroup.LayoutParams.FILL_PARENT);
        ListView listView = (ListView) customView.findViewById(R.id.list_povic);
        MyAdapter5 adapter = new MyAdapter5();
        listView.setAdapter(adapter);
    }

    class MyAdapter5 extends BaseAdapter {

        @Override
        public int getCount() {
            return address4s.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.provic_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(address4s.get(position).getName());
            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupwindow.dismiss();
                    vaillage_Id = address4s.get(position).getId();
                    edit_vaillage.setText(address4s.get(position).getName());
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }

    class MyAdapter4 extends BaseAdapter {

        @Override
        public int getCount() {
            return address3s.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.provic_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(address3s.get(position).getName());
            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupwindow.dismiss();
                    towns_Id = address3s.get(position).getId();
                    edit_towns.setText(address3s.get(position).getName());
                    edit_vaillage.setHint("请选择（村/街道）");
                    edit_vaillage.setText("");
                    // String string = UrlContact.URL_STRING +
                    // "/api/user/GetAreaByParentId?parentId=" + towns_Id;
                    // getAddress4s(string);
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }

    // 获取村/街道信息
    private void getAddress4s(int id) {
        Map<String, String> params2 = new TreeMap<>();
        params2.put("parentId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ADDRESS, params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            if (Result) {
                                address4s = AddressPovicJsonUtil.getAddressPovic(response);
                                if (address4s != null) {
                                    if (popupwindow != null && popupwindow.isShowing()) {

                                    } else {
                                        initmPopupWindowView5();
                                        popupwindow.showAsDropDown(edit_vaillage, 0, 5);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class MyAdapter3 extends BaseAdapter {

        @Override
        public int getCount() {
            return address2s.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.provic_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(address2s.get(position).getName());
            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupwindow.dismiss();
                    Area_Id = address2s.get(position).getId();
                    edit_area.setText(address2s.get(position).getName());
                    edit_towns.setHint("请选择（乡镇）");
                    edit_towns.setText("");
                    edit_vaillage.setHint("请选择（村/街道）");
                    edit_vaillage.setText("");
                    // String string = UrlContact.URL_STRING +
                    // "/api/user/GetAreaByParentId?parentId=" + Area_Id;
                    // getAddress3s(string);
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }

    // 获取乡镇的信息
    private void getAddress3s(int id) {
        Map<String, String> params2 = new TreeMap<>();
        params2.put("parentId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ADDRESS, params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            if (Result) {
                                address3s = AddressPovicJsonUtil.getAddressPovic(response);
                                if (address3s != null) {
                                    if (popupwindow != null && popupwindow.isShowing()) {

                                    } else {
                                        initmPopupWindowView4();
                                        popupwindow.showAsDropDown(edit_towns, 0, 5);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class MyAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return address1s.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.provic_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(address1s.get(position).getName());
            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupwindow.dismiss();
                    edit_area.setHint("请选择（县）");
                    edit_area.setText("");
                    edit_towns.setHint("请选择（乡镇）");
                    edit_towns.setText("");
                    edit_vaillage.setHint("请选择（村/街道）");
                    edit_vaillage.setText("");
                    edit_city.setText(address1s.get(position).getName());
                    City_Id = address1s.get(position).getId();
                    // String string = UrlContact.URL_STRING +
                    // "/api/user/GetAreaByParentId?parentId=" + City_Id;
                    // getAddress2s(string);
                    // address2s = address1s.get(position).getList();
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }

    // 获取每个县的信息
    private void getAddress2s(int id) {
        Map<String, String> params2 = new TreeMap<>();
        params2.put("parentId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ADDRESS, params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            if (Result) {
                                address2s = AddressPovicJsonUtil.getAddressPovic(response);
                                if (address2s != null) {
                                    if (popupwindow != null && popupwindow.isShowing()) {

                                    } else {
                                        initmPopupWindowView3();
                                        popupwindow.showAsDropDown(edit_area, 0, 5);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class MyAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return addresses.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.provic_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(addresses.get(position).getName());
            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupwindow.dismiss();
                    edit_povic.setText(addresses.get(position).getName());
                    Province_Id = addresses.get(position).getId();
//					String urlString1 = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + Province_Id;
//					getAddress1s(urlString1);
                    edit_city.setHint("请选择（市）");
                    edit_city.setText("");
                    edit_area.setHint("请选择（县）");
                    edit_area.setText("");
                    edit_towns.setHint("请选择（乡镇）");
                    edit_towns.setText("");
                    edit_vaillage.setHint("请选择（村/街道）");
                    edit_vaillage.setText("");
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }

    // 获取每个市的信息
    private void getAddress1s(int id) {
        Map<String, String> params2 = new TreeMap<>();
        params2.put("parentId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ADDRESS, params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            if (Result) {
                                address1s = AddressPovicJsonUtil.getAddressPovic(response);
                                if (address1s != null) {
                                    if (popupwindow != null && popupwindow.isShowing()) {

                                    } else {
                                        initmPopupWindowView2();
                                        popupwindow.showAsDropDown(edit_city, 0, 5);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    class GetNewsTask extends AsyncTask<String, Void, Integer> {
        private String string;

        @Override
        protected Integer doInBackground(String... params) {

            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            edit_povic.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (addresses != null) {
                        if (popupwindow != null && popupwindow.isShowing()) {
                        } else {
                            initmPopupWindowView();
                            popupwindow.showAsDropDown(arg0, 0, 5);
                        }
                    }
                }
            });
            edit_city.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    getAddress1s(Province_Id);
                }
            });
            edit_area.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String string = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + City_Id;
                    getAddress2s(City_Id);
                }
            });
            edit_towns.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String string = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + Area_Id;
                    getAddress3s(Area_Id);
                }
            });
            edit_vaillage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String string = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + towns_Id;
                    getAddress4s(towns_Id);
                }
            });
        }
    }

    /**
     * 获取线上的地址信息
     *
     * @author nongyi-lenovo
     */

    class MyListAdper extends BaseAdapter {

        LayoutInflater inflater;
        public List<ShoppingCartBean.DataBean.ProductBean> items;

        public MyListAdper(List<ShoppingCartBean.DataBean.ProductBean> items) {
            this.items = items;

            inflater = LayoutInflater.from(DingdanActivity.this);

        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            ShoppingCartBean.DataBean.ProductBean item = items.get(position);
            boolean is_presell = item.isIs_Presell();
            if (mIs_presell_list == null) {
                mIs_presell_list = new ArrayList<>();
            }
            if (mDateList == null) {
                mDateList = new ArrayList<>();
            }
            if (is_presell) {
                mLl_peisong_time.setVisibility(View.GONE);
                mIs_presell_list.add(is_presell);
                String timeStamp = item.getTimeStamp();
                String[] date_time = timeStamp.split(" ");
                String date = date_time[0];
                mDateList.add(date);
            }
            if (convertView == null) {
                Log.e("MainActivity", "position1 = " + position);
                convertView = inflater.inflate(R.layout.listviewitem2, null);

                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                holder.text_price = (TextView) convertView.findViewById(R.id.text_price);
                holder.text_count = (TextView) convertView.findViewById(R.id.text_count);
                holder.text_spec = (TextView) convertView.findViewById(R.id.text_spec);
                holder.text_zhongshu = (TextView) convertView.findViewById(R.id.text_zhongshu);
                holder.is_stock = (TextView) convertView.findViewById(R.id.is_stock);
                holder.image_product = (ImageView) convertView.findViewById(R.id.image_product);
                holder.text_freeprice = (TextView) convertView.findViewById(R.id.text_freeprice);
                holder.text_freetext = (TextView) convertView.findViewById(R.id.text_freetext);
                hold = holder;
                convertView.setTag(holder);

            } else {
                Log.e("MainActivity", "position2 = " + position);
                holder = (ViewHolder) convertView.getTag();

            }
            for (int i = 0; i < myStockOutPro.getStockOutPro().size(); i++) {
                if (item.getPro_Id() == Integer.valueOf(myStockOutPro.getStockOutPro().get(i))) {
                    holder.is_stock.setVisibility(View.VISIBLE);
                }
            }

            holder.tvName.setText("[" + item.getPro_Name() + "]" + item.getTotal_Content() + item.getCommon_Name()
                    + item.getDosageform());

            holder.text_spec.setText(item.getSpec());
            holder.text_price.setText("" + item.getPrice() + "元");
            holder.text_count.setText("" + item.getCount() + "");
            holder.image_product.setImageResource(R.drawable.img_non_shop);
            sImageLoader.displayImage(item.getPic_Url(), holder.image_product, options);
            if (item.getPrice() * item.getCount() - item.getFree_Price() >= 1) {

                holder.text_zhongshu.setText("" + ddf1.format(item.getPrice() * item.getCount() - item.getFree_Price()) + "元");
            } else {
                holder.text_zhongshu.setText("0" + ddf1.format(item.getPrice() * item.getCount() - item.getFree_Price()) + "元");

            }

            if (item.getMarketing_Type() == 3) {
                holder.text_freeprice.setVisibility(View.VISIBLE);
                holder.text_freeprice.setText("" + ddf1.format(item.getFree_Price()) + "元");
                holder.text_freetext.setVisibility(View.VISIBLE);
                textFreePrice.setText(ddf1.format(_freeprice));
            }
            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvName;
        public TextView text_price;
        public TextView text_spec;
        public TextView text_count;
        public TextView text_zhongshu;
        public TextView text_freeprice;
        public TextView text_freetext;
        public ImageView image_product;
        public TextView is_stock;

    }

    class MyLogistaicsAdpter extends BaseAdapter {

        private List<LogisticsDetial> detials;

        public MyLogistaicsAdpter(List<LogisticsDetial> detials) {
            this.detials = detials;
        }

        @Override
        public int getCount() {
            return detials.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHold holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DingdanActivity.this).inflate(R.layout.logistaics_list_item, null);
                holder = new ViewHold();
                holder.logistaisc_list = (LinearLayout) convertView.findViewById(R.id.logistaisc_list);
                holder.logistaisc_title = (TextView) convertView.findViewById(R.id.logistisc_title);
                holder.logistaisc_content = (TextView) convertView.findViewById(R.id.logistisc_content);
                convertView.setTag(holder);

            } else {
                holder = (ViewHold) convertView.getTag();
            }

            holder.logistaisc_title.setText(detials.get(position).getTitle());
            holder.logistaisc_content.setText(detials.get(position).getContent());
            holder.logistaisc_list.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    peisong_linear.setVisibility(View.GONE);
                    text_peisong.setText(detials.get(position).getTitle());
                    logistaisc_id = detials.get(position).getId();
                    if (Double.valueOf(detials.get(position).getLgistics()) >= 1) {
                        product_peisong.setText(ddf1.format(Double.valueOf(detials.get(position).getLgistics())) + "元");
                    } else {
                        product_peisong.setText("0" + ddf1.format(Double.valueOf(detials.get(position).getLgistics())) + "元");
                    }
                    if (zhongjia >= 1) {
                        text_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(detials.get(position).getLgistics())) + "");
                    } else {
                        if (zhongjia + Double.valueOf(detials.get(position).getLgistics()) >= 1) {
                            text_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(detials.get(position).getLgistics())) + "");
                        } else {
                            text_zongjia.setText("0" + ddf1.format(zhongjia + Double.valueOf(detials.get(position).getLgistics())) + "");
                        }
                    }
                }
            });
            return convertView;

        }

    }

    static class ViewHold {
        public TextView logistaisc_title, logistaisc_content;
        private LinearLayout logistaisc_list;


    }

    public static boolean isMobile(String mobiles) {
        String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9])|(16[0-9]))\\d{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(mobiles);

        return m.find();// boolean
    }

    public List<AddressPovic> getAddress(int id) {
        Map<String, String> params2 = new TreeMap<>();
        params2.put("parentId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_ORDER_ADDRESS, params2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean Result = jsonObject.getBoolean("Result");
                            if (Result) {
                                addresses = AddressPovicJsonUtil.getAddressPovic(response);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return addresses;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress(1);

    }

    private void showDialogGoods(String message, final List<String> list) {

        final AlertDialog alertDialog = new Builder(this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.showdailoggoods);
            TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
            tv_title.setText("温馨提示");
            TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
            tv_message.setText(message);
            Button cancer = (Button) window.findViewById(R.id.cancer);
            cancer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    alertDialog.dismiss();
                    getShouhuoren();
                }
            });
            Button sure = (Button) window.findViewById(R.id.sure);
            if (list == null) {
                sure.setText("继续下单");
            } else {
                sure.setText("返回购物车");
            }
            sure.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (list == null) {
                        alertDialog.dismiss();
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    public List<UserAddress> getShouhuoren() {

        shouhuoren_linear.setVisibility(View.VISIBLE);
        Map<String, String> params = new TreeMap<>();
        params.put("loginKey", user.getUser_Name());
        params.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .postData(UrlContact.URL_STRING + "/api/Order/GetAddrList", params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if (new JSONObject(response).getBoolean("Result")) {
                                // 获取收货人列表信息
                                lUserAddresses = UserAddressJsonUtil.getUserAddressList(response);
                                listView2.setAdapter(new MyAdapter(lUserAddresses));
                            } else {
                                Toast.makeText(getApplicationContext(), new JSONObject(response).getString("Message"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return lUserAddresses;
    }

    private MyStockOutPro getStockOut1(Map<String, String> paramsStock, final List<ShoppingCartBean.DataBean.ProductBean> list) {

        MyProgressDialog.show(DingdanActivity.this, false, true);
        MyOkHttpUtils
                .postData(UrlContact.URL_STRING + "/api/product/CheckStock", paramsStock)
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
                            if (Result) {
                                myStockOutPro = MyStockOutProJsonUtil.getStockOutPro(response);
                                logisticsDetials = myStockOutPro.getLogistics();
                                if (logisticsDetials.size() > 0) {
                                    logistaisc_id = logisticsDetials.get(0).getId();
                                    text_peisong.setText(logisticsDetials.get(0).getTitle());
                                    if (Double.valueOf(logisticsDetials.get(0).getLgistics()) >= 1) {
                                        product_peisong.setText("" + ddf1.format(Double.valueOf(logisticsDetials.get(0).getLgistics())) + "元");
                                    } else {
                                        product_peisong.setText("0" + ddf1.format(Double.valueOf(logisticsDetials.get(0).getLgistics())) + "元");
                                    }
                                    if (zhongjia >= 1) {
                                        text_zongjia.setText("" + ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        product_zongjia.setText("" + ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");
                                    } else {
                                        if (zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics()) >= 1) {
                                            text_zongjia.setText("" + ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        } else {

                                            text_zongjia.setText("0" + ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        }
                                        if (zhongjia + Double.valueOf(_freeprice) >= 1) {
                                            product_zongjia.setText("" + ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");

                                        } else {

                                            product_zongjia.setText("0" + ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");
                                        }
                                    }

                                    MyLogistaicsAdpter logistaicsAdpter = new MyLogistaicsAdpter(logisticsDetials);
                                    logistics_list.setAdapter(logistaicsAdpter);
                                }
                                myStockOutProBoolea = jsonObject.getBoolean("Result");
                                myStockOutProMessage = jsonObject.getString("Message");
                                listView1.setFocusable(false);
                                listView1.setAdapter(new MyListAdper(list));
                                MyListUtil.setListViewHeightBasedOnChildren(listView1);
                                // ----进入订单判断start

                                if (myStockOutProBoolea && myStockOutPro.isHasStockOut()) {
                                    showDialogGoods(myStockOutProMessage, myStockOutPro.getStockOutPro());
                                    btn_tijiao.setBackgroundColor(0x7fcccccc);
                                    btn_tijiao.setEnabled(false);
                                } else {
                                    if (myStockOutPro.getAlertInviteTip()) {
                                        showDialogGoods(myStockOutProMessage, null);
                                    } else {
                                        if (isAddress) {
                                            hold.is_stock.setVisibility(View.GONE);
                                        }
                                        btn_tijiao.setBackgroundColor(0xffff4b00);
                                        btn_tijiao.setEnabled(true);
                                    }
                                }
                            }
                            // ------进入订单判断end
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return myStockOutPro;
    }

    private MyStockOutPro getStockOut(Map<String, String> paramsStock) {

        MyProgressDialog.show(DingdanActivity.this, false, true);
        MyOkHttpUtils
                .postData(UrlContact.URL_STRING + "/api/product/CheckStock", paramsStock)
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
                            if (Result) {
                                myStockOutPro = MyStockOutProJsonUtil.getStockOutPro(response);
                                logisticsDetials = myStockOutPro.getLogistics();
                                if (logisticsDetials.size() > 0) {

                                    logistaisc_id = logisticsDetials.get(0).getId();
                                    text_peisong.setText(logisticsDetials.get(0).getTitle());
                                    if (Double.valueOf(logisticsDetials.get(0).getLgistics()) >= 1) {
                                        product_peisong.setText(ddf1.format(Double.valueOf(logisticsDetials.get(0).getLgistics())) + "元");
                                    } else {
                                        product_peisong.setText("0" + ddf1.format(Double.valueOf(logisticsDetials.get(0).getLgistics())) + "元");
                                    }
                                    if (zhongjia >= 1) {
                                        text_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        product_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");
                                    } else {
                                        if (zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics()) >= 1) {
                                            text_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        } else {
                                            text_zongjia.setText("0" + ddf1.format(zhongjia + Double.valueOf(logisticsDetials.get(0).getLgistics())) + "");
                                        }
                                        if (zhongjia + Double.valueOf(_freeprice) >= 1) {

                                            product_zongjia.setText(ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");
                                        } else {
                                            product_zongjia.setText("0" + ddf1.format(zhongjia + Double.valueOf(_freeprice)) + "元");

                                        }
                                    }
                                    MyLogistaicsAdpter logistaicsAdpter = new MyLogistaicsAdpter(logisticsDetials);
                                    logistics_list.setAdapter(logistaicsAdpter);
                                }
                                myStockOutProBoolea = jsonObject.getBoolean("Result");
                                myStockOutProMessage = jsonObject.getString("Message");
                                if (myStockOutProBoolea && myStockOutPro.isHasStockOut()) {
                                    showDialogGoods(myStockOutProMessage, myStockOutPro.getStockOutPro());
                                    // hold.is_stock.setVisibility(View.VISIBLE);
                                    btn_tijiao.setBackgroundColor(0x7fcccccc);
                                    btn_tijiao.setEnabled(false);
                                    listView1.setAdapter(new MyListAdper(listCarts));
                                } else {
                                    if (myStockOutPro.getAlertInviteTip()) {
                                        showDialogGoods(myStockOutProMessage, null);
                                    }
                                    btn_tijiao.setBackgroundColor(0xffff4b00);
                                    btn_tijiao.setEnabled(true);
                                    listView1.setAdapter(new MyListAdper(listCarts));

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return myStockOutPro;
    }

    public void getUINewAddress(Map<String, String> paramsStock) {
        MyOkHttpUtils
                .postData(UrlContact.URL_STRING + "/api/order/SetDefaultAddr", paramsStock)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
}
