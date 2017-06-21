package com.example.nyapp;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.adapter.MyVpAdapter;
import com.example.adapter.ProductPromotionAdapter;
import com.example.adapter.ProductSpecificationAdapter;
import com.example.classes.BaseBean;
import com.example.classes.BaseEventBean;
import com.example.classes.ConfigureBean;
import com.example.classes.MyGPS;
import com.example.classes.ProAddressBean;
import com.example.classes.ProStockBean;
import com.example.classes.ProductCouponBean;
import com.example.classes.ProductDetailBean;
import com.example.classes.Share;
import com.example.classes.ShoppingCartBean;
import com.example.classes.User;
import com.example.util.ConnectionWork;
import com.example.util.DialogUtil;
import com.example.util.GsonUtils;
import com.example.util.MyDateUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.ShareUtil;
import com.example.util.TianjiaCart;
import com.example.util.UrlContact;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by NY on 2017/2/5.
 * 商品详情页
 */

public class ProductDetailActivity extends BaseActivity {
    @BindView(R.id.vp_product_detail)
    ViewPager mVpProductDetail;
    @BindView(R.id.ll_point_group)
    LinearLayout mLlPointGroup;
    @BindView(R.id.tv_product_type)
    TextView mTvProductType;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;
    @BindView(R.id.rcy_specification)
    RecyclerView mRcySpecification;
    @BindView(R.id.rcy_promotion)
    RecyclerView mRcyPromotion;
    @BindView(R.id.tv_promotion_more)
    TextView mTvPromotionMore;
    @BindView(R.id.rl_promotion)
    RelativeLayout mRlPromotion;
    @BindView(R.id.ll_promotion)
    LinearLayout mLlPromotion;
    @BindView(R.id.tv_coupon_price)
    TextView mTvCouponPrice;
    @BindView(R.id.tv_factory_price)
    TextView mTvFactoryPrice;
    @BindView(R.id.tv_retail_price)
    TextView mTvRetailPrice;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_is_available)
    TextView mTvIsAvailable;
    @BindView(R.id.tv_delivery_place)
    TextView mTvDeliveryPlace;
    @BindView(R.id.ll_delivery_place)
    LinearLayout mLlDeliveryPlace;
    @BindView(R.id.ll_is_available)
    LinearLayout mLlIsAvailable;
    @BindView(R.id.ll_delivery)
    LinearLayout mLlDelivery;
    @BindView(R.id.btn_minus)
    Button mBtnMinus;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.tv_logistics)
    TextView mTvLogistics;
    @BindView(R.id.ll_logistics)
    LinearLayout mLlLogistics;
    @BindView(R.id.btn_call)
    Button mBtnCall;
    @BindView(R.id.btn_call_other)
    Button mBtnCallOther;
    @BindView(R.id.btn_parameter)
    Button mBtnParameter;
    @BindView(R.id.btn_introduce)
    Button mBtnIntroduce;
    @BindView(R.id.ll_parameter)
    LinearLayout mLlParameter;
    @BindView(R.id.ll_introduce)
    LinearLayout mLlIntroduce;
    @BindView(R.id.tv_composition)
    TextView mTvComposition;//有效成分
    @BindView(R.id.tv_content)
    TextView mTvContent;//本品含量
    @BindView(R.id.tv_manufactureName)
    TextView mTvManufactureName;//供货企业
    @BindView(R.id.tv_certificateNum)
    TextView mTvCertificateNum;//登记证号
    @BindView(R.id.tv_approvalNum)
    TextView mTvApprovalNum;//批准证号
    @BindView(R.id.tv_standardNum)
    TextView mTvStandardNum;//标准证号
    @BindView(R.id.tv_dosageForm)
    TextView mTvDosageForm;//剂型
    @BindView(R.id.tv_toxicity)
    TextView mTvToxicity;//毒性
    @BindView(R.id.wb_introduce)
    WebView mWeIntroduce;
    @BindView(R.id.tv_top_dot)
    TextView mTvTopDot;
    @BindView(R.id.edit_povic1)
    TextView mEditPovic1;
    @BindView(R.id.edit_city1)
    TextView mEditCity1;
    @BindView(R.id.edit_area1)
    TextView mEditArea1;
    @BindView(R.id.edit_towns)
    TextView mEditTowns;
    @BindView(R.id.delivery_address)
    LinearLayout mDeliveryAddress;
    @BindView(R.id.tv_bottom_dot)
    TextView mTvBottomDot;
    @BindView(R.id.ll_product_bottom)
    LinearLayout mLlProductBottom;
    @BindView(R.id.tv_time_H)
    TextView mTvTimeH;
    @BindView(R.id.tv_time_type)
    TextView mTvTimeType;
    @BindView(R.id.btn_count_down)
    Button mBtnCountDown;
    @BindView(R.id.ll_product_bottom_sec_kill)
    LinearLayout mLlProductBottomSecKill;
    @BindView(R.id.rl_snap_up_rules)
    RelativeLayout mRlSnapUpRules;
    @BindView(R.id.tv_snap_up_rules)
    TextView mTvSnapUpRules;
    private int id;
    private String name;
    private String type;
    private ACache mCache;
    private String gpsString;
    private String loginString;
    private User mUser;
    private ArrayList<View> mVpPicList;
    private ImageView[] mPointImgs;
    private MyVpAdapter mVpAdapter;
    private ProductDetailBean.DataBean mProDetailData;
    private String mUserName;
    private ProductDetailBean.DataBean.ProductInfoBean mProductInfo;
    private List<ProductDetailBean.DataBean.ProductSpecListBean> mProductSpecList;
    private ProductSpecificationAdapter mProductSpecificationAdapter;
    private double mFactory_price;
    private int mProductTypes;
    private String mTextDescription;
    private String mTextDescription_url;
    private String mTextDescription_name;
    private ProductDetailBean.DataBean.ProductSpecListBean mProductSpecListBean;
    private ProductPromotionAdapter mProductPromotionAdapter;
    private List<ProductCouponBean.DataBean> mCouponDataBeen;
    private List<ProductCouponBean.DataBean> mShowCouponData;
    private MyGPS mGps;
    private int mProvince_Id;
    private int mCity_Id;
    private int mCounty_Id;
    private int mTown_Id;
    private int mAddress_level = 1;
    private List<ProAddressBean.DataBean> mProvinceAddressList;
    private List<ProAddressBean.DataBean> mCityAddressList;
    private List<ProAddressBean.DataBean> mCountyAddressList;
    private List<ProAddressBean.DataBean> mTownAddressList;
    private AddressAdapter mAddressAdapter;
    private int mMarketing_type;
    private long mCountDownTime;
    private AlertDialog mLoadingDialog;
    private int mMrStock;
    private ListView mLv_popupWindow;
    private Share mShare;
    private ProStockBean.DataBean mProStockBeanData;
    private int mShoppingNumm;
    private ProductDetailActivity mActivity;
    public static List<ShoppingCartBean.DataBean.ProductBean> items2;
    private int mIsSecKill;
    private long mTotalTime;
    private long mEstimateTime;
    private long mBeginTime;
    private long mEndTime;
    private long mServersTime;
    private MyMsgDialog mNoNumDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;
    private AlertDialog mAddressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        mActivity = this;
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");
        mIsSecKill = intent.getIntExtra("isSecKill", 0);
        mCache = ACache.get(ProductDetailActivity.this);

        gpsString = mCache.getAsString("gpsString");
        loginString = mCache.getAsString("loginString");
        if (loginString.equals("true")) {
            mUser = (User) mCache.getAsObject("user");
        }
        initEvent();
        initAddress();
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    protected void onResume() {
        super.onResume();
        if (mServersType == 1) {
            getServersTime();
        }
        getShoppingCartNum();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    private void initEvent() {
        mEtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mProductSpecList != null) {
                    if (!s.toString().equals("")) {
                        Integer num = Integer.valueOf(s.toString());
                        if (num == 0) {
                            mEtNum.setText("1");
                        }
                    } else {
                        mEtNum.setText("1");
                    }
                    getProStock();
                }
            }
        });
    }

    private void initAddress() {
        if (gpsString == null) {
            mCache.put("gpsString", "false");
            gpsString = mCache.getAsString("gpsString");
            mGps = new MyGPS();
        } else {
            mGps = (MyGPS) mCache.getAsObject("gps");
            if (gpsString.equals("false")) {
                mGps = new MyGPS();
                mLlDelivery.setVisibility(View.VISIBLE);
            }
        }

        // 获取缓存信息
        if (gpsString.equals("true")) {
            if (mGps.getTownName() != null && mGps.getProvinceName() != null && mGps.getCityName() != null && mGps.getCountyName() != null) {
                mTvAddress.setText(mGps.getProvinceName() + "/" + mGps.getCityName() + "/" + mGps.getCountyName() + "/" + mGps.getTownName());
                mProvince_Id = mGps.getProId();
                mCounty_Id = mGps.getCouId();
                mTown_Id = mGps.getTownId();
            } else {
                if (mGps.getProvinceName() != null && mGps.getCityName() != null && mGps.getCountyName() != null) {
                    mTvAddress.setText(mGps.getProvinceName() + "/" + mGps.getCityName() + "/" + mGps.getCountyName());
                    mProvince_Id = mGps.getProId();
                    mCounty_Id = mGps.getCouId();
                    mTown_Id = 0;
                } else {
                    mTvAddress.setText("请选择地址");
                    mLlIsAvailable.setVisibility(View.GONE);

                }
            }
        } else {
            if (loginString == null) {
                loginString = "false";
                mCache.put("loginString", loginString);
            } else {
                if (loginString.equals("true")) {
                    mUser = (User) mCache.getAsObject("mUser");
                    if (mUser.getProvince_Id() != 0 && mUser.getCounty_Id() != 0) {
                        // 在这里显示根据IP地址获取的地址
                        try {
                            String addres = mUser.getArea_Name();
                            addres = URLDecoder.decode(addres, "UTF-8");
                            String add[] = addres.split(",");
                            if (add.length > 3) {
                                mTvAddress.setText(add[0] + "/" + add[1] + "/" + add[2] + "/" + add[3]);
                            } else {
                                String text_addres = "";
                                for (String anAdd : add) {
                                    text_addres = anAdd + "/";
                                }
                                mTvAddress.setText(text_addres.substring(0, text_addres.length() - 1));
                            }
                            mProvince_Id = mUser.getProvince_Id();
                            mCounty_Id = mUser.getCounty_Id();
                            mTown_Id = mUser.getTown_Id();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mTvAddress.setText("请选择地址");
                        mLlIsAvailable.setVisibility(View.GONE);
                    }
                } else {
                    // 在这里显示根据IP获取的地址
                    mTvAddress.setText("请选择地址");
                    mLlIsAvailable.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initData() {
        if (mUser != null) {
            mUserName = mUser.getUser_Name();
        } else {
            mUserName = "";
        }
        getProductDetailData();
        getPromotionData();
        getAddress(1);
    }

    //获取库存与物流
    private void getProStock() {
        Map<String, String> map = new TreeMap<>();
        map.put("proId", String.valueOf(id));
        map.put("count", mEtNum.getText().toString());
        map.put("provId", String.valueOf(mProvince_Id));
        map.put("couId", String.valueOf(mCounty_Id));
        map.put("townId", String.valueOf(mTown_Id));
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_STOCK, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ProStockBean proStockBean = gson.fromJson(response, ProStockBean.class);
                            if (proStockBean.isResult()) {
                                mProStockBeanData = proStockBean.getData();
                                if (mProStockBeanData != null) {
                                    if (mProStockBeanData.isIsStock()) {
                                        mTvIsAvailable.setText("有货");
                                        mLlDeliveryPlace.setVisibility(View.VISIBLE);
                                    } else {
                                        mTvIsAvailable.setText("无货");
                                        mLlDeliveryPlace.setVisibility(View.GONE);
                                    }
                                    mTvDeliveryPlace.setText(mProStockBeanData.getStockName());
                                    mTvLogistics.setText(mProStockBeanData.getRemark());
                                    mMrStock = mProStockBeanData.getMRStock();
                                }
                            }
                        }
                    }
                });
    }

    //获取产品地址
    private void getAddress(int address_Id) {
        Map<String, String> map = new TreeMap<>();
        map.put("parentId", String.valueOf(address_Id));
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_ADDRESS, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ProAddressBean proAddressBean = gson.fromJson(response, ProAddressBean.class);
                            if (proAddressBean.isResult()) {
                                List<ProAddressBean.DataBean> dataBeanList = proAddressBean.getData();
                                if (dataBeanList != null && dataBeanList.size() > 0) {
                                    switch (mAddress_level) {
                                        case 1:
                                            mProvinceAddressList = dataBeanList;
                                            break;
                                        case 2:
                                            mCityAddressList = dataBeanList;
                                            break;
                                        case 3:
                                            mCountyAddressList = dataBeanList;
                                            break;
                                        case 4:
                                            mTownAddressList = dataBeanList;
                                            break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //获取产品促销数据
    private void getPromotionData() {
        Map<String, String> map = new TreeMap<>();
        map.put("productId", String.valueOf(id));
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_COUPON_INFO, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败，请稍后重试！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ProductCouponBean productCouponBean = gson.fromJson(response, ProductCouponBean.class);
                            if (productCouponBean.isResult()) {
                                mCouponDataBeen = productCouponBean.getData();
                                if (mCouponDataBeen != null && mCouponDataBeen.size() > 0) {
                                    mTvPromotionMore.setVisibility(View.VISIBLE);
                                    mRlPromotion.setVisibility(View.VISIBLE);
                                    setCouponData();
                                } else {
                                    mLlPromotion.setVisibility(View.GONE);
                                }
                            } else {
                                mLlPromotion.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    //获取产品详情数据
    private void getProductDetailData() {
        String url;
        //判断是否为秒杀商品 2：秒杀商品
        if (mIsSecKill == 2) {
            url = UrlContact.URL_SEC_KILL_PRODUCT_DETAIL;
            mLlProductBottom.setVisibility(View.GONE);
            mLlProductBottomSecKill.setVisibility(View.VISIBLE);
            mRlSnapUpRules.setVisibility(View.VISIBLE);
            getSecKillRules();
        } else {
            url = UrlContact.URL_PRODUCT_DETAIL;
            mLlProductBottom.setVisibility(View.VISIBLE);
            mLlProductBottomSecKill.setVisibility(View.GONE);
            mRlSnapUpRules.setVisibility(View.GONE);
        }

        MyProgressDialog.show(mActivity, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("id", String.valueOf(id));
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(url, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        MyToastUtil.showShortMessage("服务器连接失败，请稍后重试！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        if (response != null) {
                            Gson gson = new Gson();
                            ProductDetailBean productDetailBean = gson.fromJson(response, ProductDetailBean.class);
                            if (productDetailBean.isResult()) {
                                mProDetailData = productDetailBean.getData();
                                if (mProDetailData != null) {

                                    setViewPagerData();

                                    setProductData();
                                } else {
                                    finish();
                                    MyToastUtil.showShortMessage(productDetailBean.getMessage());
                                }
                            } else {
                                finish();
                                MyToastUtil.showShortMessage(productDetailBean.getMessage());
                            }
                        }
                    }
                });
    }

    //获取秒杀产品规则
    private void getSecKillRules() {
        Map<String, String> map = new TreeMap<>();
        map.put("key", "SeckillPrompt");
        MyOkHttpUtils
                .getData(UrlContact.URL_CONFIGURE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ConfigureBean configureBean = gson.fromJson(response, ConfigureBean.class);
                            if (configureBean.isFlag()) {
                                String message = configureBean.getMessage();
                                if (message != null && !message.equals("")) {
                                    mTvSnapUpRules.setText(message);
                                }
                            }
                        }
                    }
                });
    }

    //设置促销数据
    private void setCouponData() {
        if (mShowCouponData == null) {
            mShowCouponData = new ArrayList<>();
        }
        mShowCouponData.add(mCouponDataBeen.get(0));
        mProductPromotionAdapter = new ProductPromotionAdapter(mShowCouponData);
        mRcyPromotion.setLayoutManager(new LinearLayoutManager(this));
        mRcyPromotion.setAdapter(mProductPromotionAdapter);
    }

    //设置产品数据
    private void setProductData() {

        //产品标题
        String typeInstruction = mProDetailData.getTypeInstruction();
        mProductInfo = mProDetailData.getProductInfo();
        int type_bg = R.color.shop_type_0;
        mTvProductType.setText(typeInstruction);
        switch (typeInstruction) {
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
        mTvProductType.setBackgroundResource(type_bg);
        if (mProductInfo != null) {
            mTvProductName.setText("[" + mProductInfo.getPro_Name() + "]"
                    + mProductInfo.getTotal_Content()
                    + mProductInfo.getCommon_Name()
                    + mProductInfo.getDosageform());
        }

        //产品规格
        mProductSpecList = mProDetailData.getProductSpecList();
        if (mProductSpecList != null && mProductSpecList.size() > 0) {
            mProductSpecificationAdapter = new ProductSpecificationAdapter(mProductSpecList);
            mRcySpecification.setLayoutManager(new GridLayoutManager(this, 2));
            mRcySpecification.setAdapter(mProductSpecificationAdapter);

            // 初始化选中的规格
           for (int i = 0; i < mProductSpecList.size(); i++) {
                if (mProductSpecList.get(i).getProductId()==id) {
                    selectProductSpec(i);
                }
            }
            mRcySpecification.addOnItemTouchListener(new OnItemChildClickListener() {
                @Override
                public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.tv_specification:
                            selectProductSpec(position);
                            break;
                    }
                }
            });

        }

        //物流
        mTvLogistics.setText(mProDetailData.getProFreightRule());
        if (mProvince_Id != 0 && mCounty_Id != 0) {
            getProStock();
        }

        //底部栏
        if (mProductInfo != null) {
            mMarketing_type = mProductInfo.getMarketing_Type();
            if (mMarketing_type == 2) {
                mEtNum.setEnabled(false);

                mLlProductBottom.setVisibility(View.GONE);
                mLlProductBottomSecKill.setVisibility(View.VISIBLE);
                mRlSnapUpRules.setVisibility(View.VISIBLE);
                if (mProductInfo.getStock() > 0) {
                    setCountDown();
                } else {
                    mBtnCountDown.setEnabled(false);
                    mTvTimeType.setText("商品已售罄");
                    setSoldOutShop("-1");
                }
            } else {
                mLlProductBottom.setVisibility(View.VISIBLE);
                mLlProductBottomSecKill.setVisibility(View.GONE);
                mRlSnapUpRules.setVisibility(View.GONE);
            }
        }

        mBtnParameter.setSelected(true);
        mBtnIntroduce.setSelected(false);
        mLlIntroduce.setVisibility(View.GONE);

        //产品参数
        if (mProductInfo != null) {
            String[] s = mProductInfo.getPercentage().split(":");
            mTvComposition.setText(s[0].trim());
            mTvContent.setText(s[1].trim());
            mTvManufactureName.setText(mProductInfo.getManuf_Name().trim());
            mTvCertificateNum.setText(mProDetailData.getRegistrationNumber().trim());
            mTvApprovalNum.setText(mProDetailData.getApprovalNumber().trim());
            mTvStandardNum.setText(mProDetailData.getStandardNumber().trim());
            mTvDosageForm.setText(mProductInfo.getDosageform().trim());
            mTvToxicity.setText(mProDetailData.getToxicityInstruction().trim());
        }

        //产品介绍
        WebSettings settings = mWeIntroduce.getSettings();
//        mWeIntroduce.requestFocus();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小

        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(true); //隐藏原生的缩放控件

        if (ConnectionWork.isConnect(getApplication())) {
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWeIntroduce.loadUrl(UrlContact.URL_PRODUCT_INTRODUCE + id);
    }

    //选中的规格
    private void selectProductSpec(int position) {
        setProductPrice(position);
        id = mProductSpecList.get(position).getProductId();
        mProductSpecificationAdapter.setSelectedPosition(position);
        mProductSpecificationAdapter.notifyDataSetChanged();

        mWeIntroduce.loadUrl(UrlContact.URL_PRODUCT_INTRODUCE + id);
        getProStock();
    }

    //设置售罄弹窗
    private void setSoldOutShop(String message) {
        AlertDialog soldOutDialog = DialogUtil.Instance().createSoldOutDialog(this, message);
        soldOutDialog.show();
    }

    //设置秒杀倒计时
    private void setCountDown() {
        getProgramsTime();
        getServersTime();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mServersTime = mServersTime + 1;
            setCountDownTime();
            mHandler.postDelayed(this, 1000);
        }
    };

    private int mServersType = -1;

    private void getServersTime() {
        Map<String, String> map = new TreeMap<>();
        map.put("key", "time");
        MyOkHttpUtils
                .getData(UrlContact.URL_CONFIGURE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ConfigureBean configureBean = gson.fromJson(response, ConfigureBean.class);
                            if (configureBean.isFlag()) {
                                String message = configureBean.getMessage();
                                if (message != null && !message.equals("")) {
                                    mServersType = 1;
                                    mServersTime = MyDateUtils.getStringToDate2(message);
                                    setCountDownTime();
                                    //秒杀倒计时
                                    mHandler.postDelayed(mRunnable, 1000);
                                }
                            }
                        }
                    }
                });
    }

    //设置秒杀时间
    private void setCountDownTime() {
        if (mServersTime - mBeginTime < 0) {
            mTvTimeH.setText(MyDateUtils.formatTime((mBeginTime - mServersTime) * 1000));
            mTvTimeType.setText("后开始活动");
            mBtnCountDown.setEnabled(false);
        } else if (mServersTime - mEndTime < 0) {
            mTvTimeH.setText(MyDateUtils.formatTime((mEndTime - mServersTime) * 1000));
            mTvTimeType.setText("后结束活动");
            mBtnCountDown.setEnabled(true);
        } else {
            mTvTimeH.setText("");
            mTvTimeType.setText("活动已结束");
            mBtnCountDown.setEnabled(false);
        }
    }

    Handler mHandler = new Handler();
//    {          // handle
//                    getServersTime();
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    mCountDownTime--;
//                    if (mCountDownTime == 0) {//活动开始
//                        if (countDownType == 1) {
//
//                            mCountDownTime = mTotalTime;
//                            countDownType = 2;
//                            mBtnCountDown.setEnabled(true);
//                            mTvTimeType.setText("后结束活动");
//                        } else if (countDownType == 2) {
//                            mCountDownTime = 0;
//                            mBtnCountDown.setEnabled(false);
//                            mTvTimeType.setText("活动已结束");
//                        }
//                    }
//                    mTvTimeH.setText(MyDateUtils.formatTime(mCountDownTime * 1000));
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    private int countDownType;

    //设置秒杀活动状态
    private void getProgramsTime() {
        //秒杀状态时间
        mEstimateTime = mProDetailData.getEstimateTime();
//        mEstimateTime = 10;

        //开始时间
        String BTime = mProductInfo.getBegin_Time().replace("T", " ");
        if (BTime.contains("+")) {//End_Time=2017-02-19T13:00:00+08:00
            BTime = BTime.split("\\+")[0];
        }
        mBeginTime = MyDateUtils.getStringToDate(BTime);

        //结束时间
        String eTime = mProductInfo.getEnd_Time().replace("T", " ");
        if (eTime.contains("+")) {//End_Time=2017-02-19T13:00:00+08:00
            eTime = eTime.split("\\+")[0];
        }
        mEndTime = MyDateUtils.getStringToDate(eTime);

        //活动总时间
        mTotalTime = mEndTime - mBeginTime;

//        if (mEstimateTime > 0) {//活动还未开始
//
//            //活动开始剩余时间
//            mCountDownTime = mEstimateTime;
//            countDownType = 1;
//            mBtnCountDown.setEnabled(false);
//            mTvTimeType.setText("后开始活动");
//        } else {
//
//            //活动结束剩余时间
//            mCountDownTime = mTotalTime + mEstimateTime;
//
//            if (mCountDownTime <= 0) {//活动结束
//                mCountDownTime = 0;
//                mBtnCountDown.setEnabled(false);
//                mTvTimeType.setText("活动已结束");
//                mTvTimeH.setText("");
//            } else {//活动正在进行
//                countDownType = 2;
//                mBtnCountDown.setEnabled(true);
//                mTvTimeType.setText("后结束活动");
//            }
//        }
//        mTvTimeH.setText(MyDateUtils.formatTime(mCountDownTime * 1000));
    }

    //设置价格
    public void setProductPrice(int position) {
        mProductSpecListBean = mProductSpecList.get(position);
        mFactory_price = mProductSpecListBean.getStrSinglesDay();
        mProductTypes = mProductSpecListBean.getProductTypes();
        mTextDescription = mProductSpecListBean.getTextDescription();
        String[] s = mTextDescription.split(",");
        if (s.length == 2) {
            mTextDescription_url = s[1];
        }
        mTextDescription_name = s[0];
        id = mProductSpecListBean.getProductId();

        switch (mProductTypes) {
            case 0:
                mTvCouponPrice.setText(mProductSpecListBean.getBoxPriceRange());
                mTvFactoryPrice.setVisibility(View.GONE);
                mIvPic.setVisibility(View.GONE);
                break;
            case 1:
                mTvCouponPrice.setText(mProductSpecListBean.getBoxPriceRange());
                mTvCouponPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mTvCouponPrice.setTextColor(getResources().getColor(R.color.gray1));

                mTvFactoryPrice.setVisibility(View.VISIBLE);
                mTvFactoryPrice.setText("" + mTextDescription_name + String.valueOf(mFactory_price));

                if (mTextDescription_url.equals("")) {
                    mIvPic.setVisibility(View.GONE);
                } else {
                    MyGlideUtils.loadNativeImage(this, mTextDescription_url, mIvPic);
                    mIvPic.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                mTvCouponPrice.setText(mProductSpecListBean.getBoxPriceRange());
                mTvCouponPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mTvCouponPrice.setTextColor(getResources().getColor(R.color.gray1));

                mTvFactoryPrice.setVisibility(View.VISIBLE);
                mTvFactoryPrice.setText("" + mTextDescription_name + String.valueOf(mFactory_price));

                mIvPic.setVisibility(View.GONE);
                break;
        }
        mTvRetailPrice.setText(mProductSpecListBean.getBottlePriceRange());
    }

    //设置轮播图
    private void setViewPagerData() {
        List<String> proImgList = mProDetailData.getProImgList();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
        params.setMargins(5, 0, 5, 0);
        if (proImgList != null) {
            mVpPicList = new ArrayList<>();
            mPointImgs = new ImageView[proImgList.size()];

            for (int i = 0; i < proImgList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.fragment_7, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_fragment7);
                MyGlideUtils.loadNativeImage(this, proImgList.get(i), imageView);
                mVpPicList.add(view);

                ImageView pointImg = new ImageView(this);
                mPointImgs[i] = pointImg;
                if (i == 0) {
                    mPointImgs[i].setImageResource(R.drawable.vp_point_pressed_bg);
                } else {
                    mPointImgs[i].setImageResource(R.drawable.vp_point_normal_bg);
                }
                mLlPointGroup.addView(mPointImgs[i], params);
            }

            mVpAdapter = new MyVpAdapter(mVpPicList);
            mVpProductDetail.setAdapter(mVpAdapter);

            mVpProductDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < mPointImgs.length; i++) {
                        if (position == i) {
                            mPointImgs[i].setImageResource(R.drawable.vp_point_pressed_bg);
                        } else {
                            mPointImgs[i].setImageResource(R.drawable.vp_point_normal_bg);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.tv_detail, R.id.img_back, R.id.btn_share, R.id.rl_top_shoppingcar,
            R.id.btn_minus, R.id.btn_add, R.id.tv_promotion_more, R.id.btn_address, R.id.deliveryaddress_xiaoshi,
            R.id.btn_call, R.id.btn_call_other, R.id.btn_parameter, R.id.btn_introduce, R.id.ll_delivery,
            R.id.rl_bottom_shoppingcar, R.id.btn_shopingcar, R.id.btn_goumai, R.id.text_back, R.id.btn_count_down,
            R.id.edit_povic1, R.id.edit_city1, R.id.edit_area1, R.id.edit_towns})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back://返回
                mActivity.finish();
                break;
            case R.id.btn_share://分享
                mActivity.loadShareDate();
                break;
            case R.id.rl_top_shoppingcar://购物车
                Intent intent = new Intent(mActivity, ShoppingCarActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_detail://查看详情
                String json = GsonUtils.getInstance().toJson(mProDetailData);
                intent = new Intent(mActivity, ProCheckDetailActivity.class);
                intent.putExtra("ProDetailData", json);
                intent.putExtra("Id", id);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_promotion_more://更多促销信息
                mTvPromotionMore.setVisibility(View.GONE);
                mProductPromotionAdapter = new ProductPromotionAdapter(mCouponDataBeen);
                mRcyPromotion.setLayoutManager(new LinearLayoutManager(this));
                mRcyPromotion.setAdapter(mProductPromotionAdapter);
                break;
            case R.id.btn_minus://减
                updateShoppingNum(-1);
                break;
            case R.id.btn_add://加
                updateShoppingNum(1);
                break;
            case R.id.btn_call:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mProDetailData.getManufPhone()));
                startActivity(intent);
                break;
            case R.id.btn_call_other:
                if (mProductInfo != null) {
                    intent = new Intent(ProductDetailActivity.this, QiYeZiliaoActivity.class);
                    intent.putExtra("ManufId", mProductInfo.getManuf_Id());
                    startActivity(intent);
                }
                break;
            case R.id.btn_parameter://参数
                mLlParameter.setVisibility(View.VISIBLE);
                mLlIntroduce.setVisibility(View.GONE);
                mBtnParameter.setSelected(true);
                mBtnIntroduce.setSelected(false);
                break;
            case R.id.btn_introduce://产品介绍
                mLlParameter.setVisibility(View.GONE);
                mLlIntroduce.setVisibility(View.VISIBLE);
                mBtnParameter.setSelected(false);
                mBtnIntroduce.setSelected(true);
                break;
            case R.id.rl_bottom_shoppingcar://进入购物车
                intent = new Intent(ProductDetailActivity.this, ShoppingCarActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_shopingcar://加入购物车
                String price = mTvCouponPrice.getText().toString();
                if (price.equals("暂无价格")) {
                    MyToastUtil.showLongMessage("该商品不能加入购物车");
                } else {
                    if (mProductSpecList != null) {
                        if (mProStockBeanData != null) {
                            if (mProStockBeanData.isIsStock()) {
                                TianjiaCart.addCart(id, mEtNum.getText().toString(), getApplicationContext());
                                EventBus.getDefault().post(new BaseEventBean(2, 2));
                                MyToastUtil.showShortMessage("添加成功");
                            } else {
                                mNoNumDialog = new MyMsgDialog(mActivity, "系统提示", "该产品库存不足，无法购买。您可以尝试修改配送地址或者购买数量。",
                                        new MyMsgDialog.ConfirmListener() {
                                            @Override
                                            public void onClick() {
                                                mNoNumDialog.dismiss();
                                            }
                                        }, null);
                                mNoNumDialog.show();
                            }
                        } else {
                            MyToastUtil.showLongMessage("该商品无法加入购物车");
                        }
                    }
                    getShoppingCartNum();
                }
                break;
            case R.id.btn_goumai://购买商品
                price = mTvCouponPrice.getText().toString();
                if (price.equals("暂无价格")) {
                    MyToastUtil.showLongMessage("该商品不能加入购物车");
                } else {
                    if (mProductSpecList != null) {
                        if (mProStockBeanData != null) {
                            if (mProStockBeanData.isIsStock()) {

                                TianjiaCart.addCart(id, mEtNum.getText().toString(), getApplicationContext());
                                intent = new Intent(ProductDetailActivity.this, ShoppingCarActivity.class);
                                startActivity(intent);
                            } else {
                                mNoNumDialog = new MyMsgDialog(mActivity, "系统提示", "该产品库存不足，无法购买。您可以尝试修改配送地址或者购买数量。",
                                        new MyMsgDialog.ConfirmListener() {
                                            @Override
                                            public void onClick() {
                                                mNoNumDialog.dismiss();
                                            }
                                        }, null);
                                mNoNumDialog.show();
                            }
                        } else {
                            MyToastUtil.showLongMessage("该商品无法加入购物车");
                        }
                    }
                }
                break;
            case R.id.btn_address://确认修改地址
                setAddress();
                break;
            case R.id.ll_delivery://显示地址选择框
                mEditPovic1.setText("");
                mEditCity1.setText("");
                mEditArea1.setText("");
                mEditTowns.setText("");
                mEditCity1.setEnabled(false);
                mEditArea1.setEnabled(false);
                mEditTowns.setEnabled(false);
                mDeliveryAddress.setVisibility(View.VISIBLE);
                break;
            case R.id.text_back://隐藏地址选择框

                mDeliveryAddress.setVisibility(View.GONE);
                break;
            case R.id.edit_povic1://省
                mAddress_level = 1;
                mEditCity1.setEnabled(false);
                mEditArea1.setEnabled(false);
                mEditTowns.setEnabled(false);
                initPopupWindowView(view, mAddress_level);
                break;
            case R.id.edit_city1://市
                mAddress_level = 2;
                mEditArea1.setEnabled(false);
                mEditTowns.setEnabled(false);
                initPopupWindowView(view, mAddress_level);
                break;
            case R.id.edit_area1://县
                mAddress_level = 3;
                mEditTowns.setEnabled(false);
                initPopupWindowView(view, mAddress_level);
                break;
            case R.id.edit_towns://乡镇
                mAddress_level = 4;
                initPopupWindowView(view, mAddress_level);
                break;
            case R.id.btn_count_down://秒杀商品
                mBtnCountDown.setEnabled(false);
                goToOrder();
                break;
            case R.id.deliveryaddress_xiaoshi://取消地址选择框
                mDeliveryAddress.setVisibility(View.GONE);
                break;
        }
    }

    //获取购物车商品数量
    public void getShoppingCartNum() {
        mShoppingNumm = TianjiaCart.getShoppingNum(this);
        if (mShoppingNumm != 0) {
            mTvTopDot.setVisibility(View.VISIBLE);
            mTvTopDot.setText(String.valueOf(mShoppingNumm));
            mTvBottomDot.setVisibility(View.VISIBLE);
            mTvBottomDot.setText(String.valueOf(mShoppingNumm));
        } else {
            mTvTopDot.setVisibility(View.GONE);
            mTvBottomDot.setVisibility(View.GONE);
        }
    }

    //加载分享数据
    private void loadShareDate() {
        /**
         * 加载分享的内容信息
         * @return
         */

        if (mUser != null) {
            mUserName = mUser.getUser_Name();
        }
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        map.put("pro_id", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_SHARE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                    }

                    @Override
                    public void onResponse(String response, int mid) {
                        MyProgressDialog.cancel();
                        Gson gson = new Gson();
                        mShare = gson.fromJson(response, Share.class);
                        if (mShare.isResult()) {
                            Share.DataBean mShareData = mShare.getData();
                            if (mShareData != null) {
                                ShareUtil.initShareDate(ProductDetailActivity.this, mShareData);
                            }
                        } else {
                            MyToastUtil.showShortMessage(mShare.getMessage());
                        }
                    }
                });

    }

    //更新商品数量
    private void updateShoppingNum(int i) {
        int shoppingNum = Integer.valueOf(mEtNum.getText().toString());
        if (mMarketing_type == 2) {
            MyToastUtil.showShortMessage("该商品为秒杀商品，不能更改数量！请选择立即购买！");
        } else {
            switch (i) {
                case -1:
                    if (shoppingNum == 1) {
                        mNoNumDialog = new MyMsgDialog(mActivity, "系统提示", "商品数量不能为0！",
                                new MyMsgDialog.ConfirmListener() {
                                    @Override
                                    public void onClick() {
                                        mNoNumDialog.dismiss();
                                    }
                                }, null);
                        mNoNumDialog.show();
                    } else {
                        shoppingNum = shoppingNum - 1;
                    }
                    break;
                case 1:
                    if (shoppingNum >= mMrStock) {
                        mNoNumDialog = new MyMsgDialog(mActivity, "系统提示", "已超出该商品库存数量！如需要，可尝试修改配送地址！",
                                new MyMsgDialog.ConfirmListener() {
                                    @Override
                                    public void onClick() {
                                        mNoNumDialog.dismiss();
                                    }
                                }, null);
                        mNoNumDialog.show();
                    } else {
                        shoppingNum = shoppingNum + 1;
                    }
                    break;
            }
            mEtNum.setText(String.valueOf(shoppingNum));
        }
    }

    //立即购买秒杀商品
    private void goToOrder() {
        loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            if (loginString.equals("true")) {
                mLoadingDialog = DialogUtil.Instance().createLoadingDialog(this);
                mLoadingDialog.show();
                goToOrderData();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }

    }

    //判断是否可以秒杀
    private void goToOrderData() {
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("ProId", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_RUSH_SHOP, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoadingDialog.dismiss();
                        MyToastUtil.showLongMessage("发生错误，请稍后重试！");
                    }

                    @Override
                    public void onResponse(String response, int mid) {
                        if (response != null) {
                            mBtnCountDown.setEnabled(false);
                            Gson gson = new Gson();
                            BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                //message 0：可以秒杀
                                getListCarts(id, Integer.valueOf(mEtNum.getText().toString()));

                            } else {
                                //message -1:已售罄 -2：没资格
                                setSoldOutShop(baseBean.getMessage());
                                mLoadingDialog.dismiss();
                            }
                        }
                    }
                });
    }

    //秒杀成功传递数据到订单页
    private void getListCarts(int id, int count) {
        JSONArray cartItemJsonArray = new JSONArray();
        JSONObject cartItemJsonObject = new JSONObject();
        try {
            cartItemJsonObject.put("Id", id);
            cartItemJsonObject.put("Count", count);
            cartItemJsonArray.put(0, cartItemJsonObject);

            Map<String, String> map = new TreeMap<>();
            map.put("loginKey", mUser.getUser_Name());
            map.put("deviceId", MyApplication.sUdid);
            map.put("CartItem", cartItemJsonArray.toString());
            MyOkHttpUtils
                    .postData(UrlContact.URL_SHOPPING_CART, map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            mLoadingDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mLoadingDialog.dismiss();
                            if (response != null) {
                                Gson gson = new Gson();
                                ShoppingCartBean shoppingCartBean = gson.fromJson(response, ShoppingCartBean.class);
                                if (shoppingCartBean != null && shoppingCartBean.isResult()) {
                                    ShoppingCartBean.DataBean data = shoppingCartBean.getData();
                                    if (data != null) {
                                        items2 = data.getProduct();
                                        String price = mTvCouponPrice.getText().toString();
                                        String[] split = price.split("：");
                                        String s = split[1];

                                        Intent intent = new Intent(mActivity, DingdanActivity.class);

                                        intent.putExtra("from", "ProductDetailActivity");
                                        intent.putExtra("zongjia", Double.valueOf(s));
                                        intent.putExtra("_freePrice", ".00");
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    //设置地址
    private void setAddress() {
        if (mEditPovic1.getText().toString().equals("") || mEditCity1.getText().toString().equals("")
                || mEditArea1.getText().toString().equals("") || mEditTowns.getText().toString().equals("")) {
            Toast.makeText(ProductDetailActivity.this, "请全部填写！", Toast.LENGTH_SHORT).show();
        } else {
            mLlIsAvailable.setVisibility(View.VISIBLE);
            mTvAddress.setText(mEditPovic1.getText() + "/" + mEditCity1.getText() + "/" + mEditArea1.getText()
                    + "/" + mEditTowns.getText());
            // 重新缓存地址信息
            mGps.setProvinceName(mEditPovic1.getText() + "");
            mGps.setCityName(mEditCity1.getText() + "");
            mGps.setCountyName(mEditArea1.getText() + "");
            mGps.setTownName(mEditTowns.getText() + "");
            mGps.setProId(mProvince_Id);
            mGps.setCityId(mCity_Id);
            mGps.setCouId(mCounty_Id);
            mGps.setTownId(mTown_Id);
            mCache.put("gpsString", "true");
            mCache.put("gps", mGps);
            mDeliveryAddress.setVisibility(View.GONE);

            getProStock();
        }
    }

    //弹出地址选择框
    private void initPopupWindowView(View view, int i) {
        // 获取自定义布局文件的视图
        View customView = LayoutInflater.from(this).inflate(R.layout.popview_item, null);
        mLv_popupWindow = (ListView) customView.findViewById(R.id.list_povic);

        switch (i) {
            case 1:
                mAddressAdapter = new AddressAdapter(mProvinceAddressList);
                break;
            case 2:
                mAddressAdapter = new AddressAdapter(mCityAddressList);
                break;
            case 3:
                mAddressAdapter = new AddressAdapter(mCountyAddressList);
                break;
            case 4:
                mAddressAdapter = new AddressAdapter(mTownAddressList);
                break;
        }
        mLv_popupWindow.setAdapter(mAddressAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(customView);
        mAddressDialog = builder.create();
        mAddressDialog.setCancelable(false);
        mAddressDialog.show();
//        mPopupWindow.showAsDropDown(view, 0, 5);


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ProductDetail Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    //地址选择适配器
    class AddressAdapter extends BaseAdapter {
        private List<ProAddressBean.DataBean> mList;

        AddressAdapter(List<ProAddressBean.DataBean> addressList) {
            this.mList = addressList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AddressViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.provic_item, null);
                holder = new AddressViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.text_povices);
                convertView.setTag(holder);
            } else {
                holder = (AddressViewHolder) convertView.getTag();
            }
            holder.textView.setText(mList.get(position).getName());
            holder.layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mAddressDialog.dismiss();
                    switch (mAddress_level) {
                        case 1:
                            mEditPovic1.setText(mList.get(position).getName());
                            mProvince_Id = mList.get(position).getId();
                            mAddress_level = 2;
                            getAddress(mProvince_Id);

                            mEditCity1.setEnabled(true);
                            mEditCity1.setText("");
                            mEditArea1.setText("");
                            mEditTowns.setText("");
                            break;
                        case 2:
                            mEditCity1.setText(mList.get(position).getName());
                            mCity_Id = mList.get(position).getId();
                            mAddress_level = 3;
                            getAddress(mCity_Id);

                            mEditArea1.setEnabled(true);
                            mEditArea1.setText("");
                            mEditTowns.setText("");
                            break;
                        case 3:
                            mEditArea1.setText(mList.get(position).getName());
                            mCounty_Id = mList.get(position).getId();
                            mAddress_level = 4;
                            getAddress(mCounty_Id);

                            mEditTowns.setEnabled(true);
                            mEditTowns.setText("");
                            break;
                        case 4:
                            mEditTowns.setText(mList.get(position).getName());
                            mTown_Id = mList.get(position).getId();
                            mAddress_level = 5;

                            break;
                    }
                }
            });

            return convertView;
        }

        class AddressViewHolder {
            LinearLayout layout;
            TextView textView;
        }
    }


}
