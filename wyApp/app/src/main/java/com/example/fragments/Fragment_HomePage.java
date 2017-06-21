package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.adapter.HomeCouponAdapter;
import com.example.adapter.HomeHotProductsAdapter;
import com.example.adapter.HomeNavigateAdapter;
import com.example.adapter.HomeProductAdapter;
import com.example.adapter.MyVpAdapter;
import com.example.classes.CouponDataBean;
import com.example.classes.CouponPic;
import com.example.classes.HomeClickBean;
import com.example.classes.HomeCouponBean;
import com.example.classes.HomePageBean;
import com.example.classes.HomePageBeanUtil;
import com.example.classes.HomePageList;
import com.example.classes.HomeProductItemBean;
import com.example.classes.ProductBriefBean;
import com.example.classes.User;
import com.example.nyapp.LoginActivity;
import com.example.nyapp.LotteryActivity;
import com.example.nyapp.MainActivity;
import com.example.nyapp.MyApplication;
import com.example.nyapp.MyBangdingPhoneActivity;
import com.example.nyapp.MyCouponActivity;
import com.example.nyapp.MyDaiGouActivity;
import com.example.nyapp.MyDaiJinQuanActivity;
import com.example.nyapp.MyDingDanActivity;
import com.example.nyapp.MyTiXianActivity;
import com.example.nyapp.ProductDetailActivity;
import com.example.nyapp.ProductListActivity;
import com.example.nyapp.R;
import com.example.nyapp.SearchActivity;
import com.example.nyapp.TuanGouActivity;
import com.example.nyapp.UserInfoActivity;
import com.example.nyapp.ZhuanTiActivity;
import com.example.util.MyGlideUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.TianjiaCart;
import com.example.util.UrlContact;
import com.example.view.DividerItemDecoration;
import com.example.view.MyAdDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.util.MyOkHttpUtils.getData;


public class Fragment_HomePage extends Fragment implements OnRefreshListener {
    public static final String TAG = "Fragment_HomePage.class";
    @BindView(R.id.tv_dot1)
    TextView mTvDot1;
    @BindView(R.id.vp_HomePage)
    ViewPager mVpHomePage;
    @BindView(R.id.ll_VpPoint)
    LinearLayout mLlVpPoint;
    @BindView(R.id.rcy_Navigate)
    RecyclerView mRcyNavigate;
    @BindView(R.id.iv_Banner)
    ImageView mIvBanner;
    @BindView(R.id.rl_Banner)
    RelativeLayout mRlBanner;
    @BindView(R.id.rcy_hot)
    RecyclerView mRcyHot;
    @BindView(R.id.rcy_coupon)
    RecyclerView mRcyCoupon;
    @BindView(R.id.ll_Home_Coupon)
    LinearLayout mLlHomeCoupon;
    @BindView(R.id.rcy_home_product)
    RecyclerView mRcyHomeProduct;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.iv_coupon)
    ImageView mIvCoupon;
    @BindView(R.id.iv_home_coupon)
    ImageView mIvHomeCoupon;
    @BindView(R.id.ll_home_hot)
    LinearLayout mLlHomeHot;
    @BindView(R.id.rl_home_products)
    RelativeLayout mRlHomeProducts;
    private MainActivity mActivity;
    private ACache mCache;
    private String mUserName = "";
    private List<View> mVpImageList;
    private ImageView[] mPointImgList;
    private int mVpPosition;
    private Handler mHandler = new Handler();
    private List<ProductBriefBean.DataBean> mProductBriefBeanData;
    private User mUser;
    private MyMsgDialog mBingPhoneDialog;
    private HomePageList.DataBean mHomePageDataList;
    private String mCouponPicUrl;
    private String mRuleId;
    private int mCouponType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mActivity = (MainActivity) getActivity();
        mCache = ACache.get(mActivity);
        isLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showShoppingNum();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(mRunnable);
    }

    @OnClick({R.id.editText1, R.id.saoyisao, R.id.iv_Banner, R.id.tv_MoreCoupon, R.id.iv_coupon, R.id.iv_home_coupon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText1:
                Intent intent = new Intent(mActivity, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.saoyisao:
                mActivity.showFragment(2);
                break;
            case R.id.iv_Banner:
                HomePageBean bannerBean = mHomePageDataList.getZhongbuList().get(0);
                onHomeClickEvent(new HomeClickBean(bannerBean.getType(), bannerBean.getUrl(), bannerBean.getOwner()));
                break;
            case R.id.tv_MoreCoupon:
                intent = new Intent(mActivity, ProductListActivity.class);
                intent.putExtra("ruleId", "9");
                intent.putExtra("couponType", "1");
                intent.putExtra("from", "first");
                mActivity.startActivity(intent);
                break;
            case R.id.iv_home_coupon:
                List<HomePageBean> coupons = mHomePageDataList.getCoupons();
                HomePageBean couponsBean = coupons.get(0);
                onHomeClickEvent(new HomeClickBean(couponsBean.getType(),
                        "/home/takecoupon.html?ruleid=" + couponsBean.getUrl()
                        , couponsBean.getOwner()));
                break;
            case R.id.iv_coupon:
                if (!isLogin()) {
                    intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (mCouponType == 6) {
                        getTakeLottery();
                    } else {
                        getTakeCoupon();
                    }
                }
                break;
        }
    }

    //判断是否登录
    private boolean isLogin() {
        String loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);
            return false;
        } else {
            if (loginString.equals("true")) {
                mUser = (User) mCache.getAsObject("user");
                mUserName = mUser.getUser_Name();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onRefresh() {
        mVpPosition = 0;
        mHandler.removeCallbacks(mRunnable);
        initData();
    }

    private void initData() {
        getIsCoupon();
        getHomeData();
        getCommendData();
    }

    //隐藏刷新、加载布局
    private void stopSwipeToLoad() {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    //获取首页数据
    private void getHomeData() {
        getData(UrlContact.URL_RESOURCE_LIST,new HashMap<String, String>())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        stopSwipeToLoad();
                        MyToastUtil.showShortMessage("网络断开连接！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        stopSwipeToLoad();
                        if (response != null) {
                            HomePageList homePageList = HomePageBeanUtil.getHomePageBeanList(response);
                            if (homePageList != null) {
                                if (homePageList.isResult()) {
                                    mHomePageDataList = homePageList.getData();
                                    if (mHomePageDataList != null) {
                                        setHomePageData();
                                    }
                                }

                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (MyAdDialog.isShowAd == 1) {
                                    MyAdDialog.showAdDialog();
                                    MyAdDialog.isShowAd = 2;
                                }
                            }
                        }, 1500);
                    }
                });
    }

    //获取热门商品数据
    private void getCommendData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        getData(UrlContact.URL_COMMEND_SHOPPING, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("网络断开连接！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ProductBriefBean productBriefBean = gson.fromJson(response, ProductBriefBean.class);
                            if (productBriefBean.isResult()) {
                                mProductBriefBeanData = productBriefBean.getData();
                                if (mProductBriefBeanData != null && mProductBriefBeanData.size() > 0) {
                                    setProductHotData();
                                }
                            }
                        }
                    }
                });
    }

    //判断是否显示抢优惠券或抽奖图标
    private void getIsCoupon() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "2");
        MyOkHttpUtils
                .getData(UrlContact.URL_AD, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            CouponPic couponPic = gson.fromJson(response, CouponPic.class);
                            if (couponPic.isResult()) {
                                List<CouponPic.DataBean> dataBeen = couponPic.getData();
                                if (dataBeen != null && dataBeen.size() > 0) {
                                    CouponPic.DataBean dataBean = dataBeen.get(0);

                                    mCouponPicUrl = dataBean.getPic_Path();
                                    mRuleId = dataBean.getUrl();
                                    mCouponType = dataBean.getType();
                                    MyGlideUtils.loadImage(mActivity, mCouponPicUrl, mIvCoupon);
                                    mIvCoupon.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
    }

    //跳转抽奖界面
    private void getTakeLottery() {
        Intent intent = new Intent(mActivity, LotteryActivity.class);
        mActivity.startActivity(intent);

    }

    //返回抢优惠券结果
    private void getTakeCoupon() {
        HashMap<String, String> map = new HashMap<>();
        map.put("RuleId", mRuleId);
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        getData(UrlContact.URL_TAKE_COUPON, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            CouponDataBean couponDataBean = gson.fromJson(response, CouponDataBean.class);
                            String message = couponDataBean.getMessage();
                            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //设置热门商品数据
    private void setProductHotData() {
        mLlHomeHot.setVisibility(View.VISIBLE);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.divider_bg);

        mRcyHot.addItemDecoration(dividerItemDecoration);
        mRcyHot.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mRcyHot.setAdapter(new HomeHotProductsAdapter(mProductBriefBeanData, mActivity));
        mRcyHot.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("id", mProductBriefBeanData.get(position).getId());
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                intent.putExtra("isSecKill", mProductBriefBeanData.get(position).getMarketing_Type());
                mActivity.startActivity(intent);
            }
        });
    }

    //设置首页数据
    private void setHomePageData() {
        setViewPagerData(mHomePageDataList.getLunboList());
        setHomeNavigateData(mHomePageDataList.getZhongbudaohang());
        setHomeBannerData(mHomePageDataList.getZhongbuList());
        setHomeCouponData(mHomePageDataList.getCoupons());
        setHomeProductData();
    }

    //设置产品分类数据
    private void setHomeProductData() {
        mRlHomeProducts.setVisibility(View.VISIBLE);

        List<HomeProductItemBean> productItemBeen = new ArrayList<>();
        productItemBeen.add(new HomeProductItemBean("除草剂", mHomePageDataList.getChucaoList()));
        productItemBeen.add(new HomeProductItemBean("杀虫剂", mHomePageDataList.getShachongList()));
        productItemBeen.add(new HomeProductItemBean("杀菌剂", mHomePageDataList.getShajunList()));
        productItemBeen.add(new HomeProductItemBean("植物营养及调节剂", mHomePageDataList.getQitaList()));

        mRcyHomeProduct.setLayoutManager(new LinearLayoutManager(mActivity));
        mRcyHomeProduct.setAdapter(new HomeProductAdapter(productItemBeen, mActivity));
    }

    //设置优惠券专区数据
    private void setHomeCouponData(List<HomePageBean> coupons) {
        if (coupons != null) {
            if (coupons.size() > 0) {
                mLlHomeCoupon.setVisibility(View.VISIBLE);
                MyGlideUtils.loadNativeImage(mActivity, coupons.get(0).getPic_Path(), mIvHomeCoupon);
                getHomeCouponList(coupons.get(0).getUrl());
            } else {
                mLlHomeCoupon.setVisibility(View.GONE);
            }
        }
    }

    //获取优惠券列表
    private void getHomeCouponList(String url) {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        map.put("ruleId", url);
        getData(UrlContact.URL_COUPON_SHOPPING, map)
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
                            HomeCouponBean homeCouponBean = gson.fromJson(response, HomeCouponBean.class);
                            if (homeCouponBean.isResult()) {
                                List<HomeCouponBean.DataBean> homeCouponBeanData = homeCouponBean.getData();
                                if (homeCouponBeanData != null && homeCouponBeanData.size() > 0) {
                                    setHomeCouponListData(homeCouponBeanData);
                                }
                            }
                        }
                    }
                });
    }

    //设置优惠券列表数据
    private void setHomeCouponListData(final List<HomeCouponBean.DataBean> homeCouponBeanData) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.divider_bg);
        mRcyCoupon.addItemDecoration(dividerItemDecoration);
        mRcyCoupon.setLayoutManager(layoutManager);

        mRcyCoupon.setAdapter(new HomeCouponAdapter(homeCouponBeanData, mActivity));
        mRcyCoupon.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(homeCouponBeanData.get(position).getId()));
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                mActivity.startActivity(intent);
            }
        });
    }

    //设置中部通栏数据
    private void setHomeBannerData(List<HomePageBean> zhongbuList) {
        if (zhongbuList != null) {
            if (zhongbuList.size() > 0) {
                mRlBanner.setVisibility(View.VISIBLE);
                MyGlideUtils.loadNativeImage(mActivity, zhongbuList.get(0).getPic_Path(), mIvBanner);
            } else {
                mRlBanner.setVisibility(View.GONE);
            }
        }
    }

    //设置中部导航数据
    private void setHomeNavigateData(final List<HomePageBean> zhongbudaohang) {
        if (zhongbudaohang != null) {
            mRcyNavigate.setLayoutManager(new GridLayoutManager(mActivity, zhongbudaohang.size() / 2));
            mRcyNavigate.setAdapter(new HomeNavigateAdapter(zhongbudaohang, mActivity));
            mRcyNavigate.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    HomePageBean homePageBean = zhongbudaohang.get(position);
                    onHomeClickEvent(new HomeClickBean(homePageBean.getType(), homePageBean.getUrl(), homePageBean.getOwner()));
                }
            });
        }
    }

    //设置轮播数据
    private void setViewPagerData(List<HomePageBean> lunboList) {
        if (lunboList != null) {
            if (mVpImageList == null) {
                mVpImageList = new ArrayList<>();
            } else {
                mVpImageList.clear();
            }
            mPointImgList = new ImageView[lunboList.size()];

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);

            mLlVpPoint.removeAllViews();
            for (int i = 0; i < lunboList.size(); i++) {
                View mView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_6, null);
                ImageView imageView = (ImageView) mView.findViewById(R.id.image_fragment6);
                final HomePageBean VpItemBean = lunboList.get(i);
                MyGlideUtils.loadNativeImage(mActivity, VpItemBean.getPic_Path(), imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHomeClickEvent(new HomeClickBean(VpItemBean.getType(), VpItemBean.getUrl(), VpItemBean.getOwner()));
                    }
                });
                mVpImageList.add(mView);

                ImageView pointImg = new ImageView(mActivity);
                mPointImgList[i] = pointImg;
                if (i == 0) {
                    mPointImgList[i].setBackgroundResource(R.drawable.yuan);
                } else {
                    mPointImgList[i].setBackgroundResource(R.drawable.yuan_w);
                }
                mLlVpPoint.addView(mPointImgList[i], params);
            }

            MyVpAdapter myVpAdapter = new MyVpAdapter(mVpImageList);
            mVpHomePage.setAdapter(myVpAdapter);

            mVpHomePage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mVpPosition = position;
                    for (int i = 0; i < mPointImgList.length; i++) {
                        if (i == position) {
                            mPointImgList[i].setBackgroundResource(R.drawable.yuan);
                        } else {
                            mPointImgList[i].setBackgroundResource(R.drawable.yuan_w);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mVpHomePage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            mHandler.removeCallbacks(mRunnable);
                            break;
                        case MotionEvent.ACTION_UP:
                            mHandler.postDelayed(mRunnable, 5000);
                            break;
                        default:
                            mHandler.postDelayed(mRunnable, 5000);
                            break;
                    }
                    return false;
                }
            });

            mHandler.postDelayed(mRunnable, 5000);
        }
    }

    //轮播定时器
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mVpPosition == mPointImgList.length - 1) {
                mVpPosition = 0;
            } else {
                mVpPosition = mVpPosition + 1;
            }
            mVpHomePage.setCurrentItem(mVpPosition);
            mHandler.postDelayed(this, 5000);
        }
    };

    //获取购物车商品数量
    public void showShoppingNum() {
        int shoppingNum = TianjiaCart.getShoppingNum(mActivity);
        if (shoppingNum != 0) {
            mTvDot1.setVisibility(View.VISIBLE);
            mTvDot1.setText(String.valueOf(shoppingNum));
        } else {
            mTvDot1.setVisibility(View.GONE);
        }
    }

    /**
     * 0-跳转到商品详情页 URL值为商品ID
     * 1-网页链接 URL值为链接地址 ===》不处理
     * 2-网页链接 URL值为链接地址
     * 3-无操作URL值为空
     * 5-跳转到优惠券领取
     * 6-跳转到列表页 URL值为搜索关键字
     * 7-优惠券领取 URL值为优惠券发放规则ID
     * 8-跳转Activity URL值为类名
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onHomeClickEvent(HomeClickBean bean) {
        int type = bean.getType();
        String url = bean.getUrl();
        String title = bean.getTitle();

        switch (type) {
            case 0:
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(url));
                intent.putExtra("name", "");
                intent.putExtra("type", "");
                mActivity.startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                if (url.contains("eqxiu.com")) {
                    intent = new Intent(mActivity, ZhuanTiActivity.class);
                    intent.putExtra("payurl", url);
                    intent.putExtra("title", title);
                    startActivity(intent);
                } else if (url.contains("UserCenter/Protocol.html")) {
                    //签约代购
                    //http://m.16899.com/UserCenter/Protocol.html?loginKey=18317008906&deviceId=de1ba972-8431-32c6-aa74-bb4c08885aca
                    if (isLogin()) {
                        intent = new Intent(mActivity, TuanGouActivity.class);
                        intent.putExtra("payurl", url + "?loginKey=" + mUserName + "&deviceId=" + MyApplication.sUdid);
                        intent.putExtra("title", title);
                        startActivity(intent);
                    } else {
                        intent = new Intent(mActivity, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(mActivity, TuanGouActivity.class);
                    intent.putExtra("payurl", url);
                    intent.putExtra("title", title);
                    if (url.contains("/home/Special")) {
                        intent.putExtra("type", 7);
                    }
                    startActivity(intent);
                }
                break;
            case 3:

                break;
            case 5:
                // 优惠券领取
                intent = new Intent(mActivity, MyCouponActivity.class);
                intent.putExtra("payurl", url);
                intent.putExtra("title", title);
                startActivity(intent);
                break;
            case 6:
                // 6-跳转到列表页 URL值为搜索关键字
                intent = new Intent(mActivity, ProductListActivity.class);
                intent.putExtra("type", url);
                intent.putExtra("from", "search");
                startActivity(intent);
                break;
            case 7:
                if (isLogin()) {
                    if (mUser.getMobile().equals("null") || mUser.getMobile() == null) {
                        mBingPhoneDialog = new MyMsgDialog(mActivity, "温馨提示", "您现在还没有绑定手机号码，现在是否去绑定？",
                                new MyMsgDialog.ConfirmListener() {
                                    @Override
                                    public void onClick() {
                                        mBingPhoneDialog.dismiss();
                                        Intent intent = new Intent(mActivity, MyBangdingPhoneActivity.class);
                                        intent.putExtra("bangding", 1);
                                        mActivity.startActivity(intent);
                                    }
                                },
                                new MyMsgDialog.CancelListener() {
                                    @Override
                                    public void onClick() {
                                        mBingPhoneDialog.dismiss();
                                    }
                                });
                        mBingPhoneDialog.setCancelable(false);
                        mBingPhoneDialog.show();
                    } else {

                        intent = new Intent(mActivity, TuanGouActivity.class);
                        intent.putExtra("payurl", url + "?loginKey=" + mUserName + "&deviceId=" + MyApplication.sUdid);
                        intent.putExtra("title", title);
                        intent.putExtra("type", 7);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case 8:
                if (isLogin()) {
                    switch (url) {
                        case "MyDingDanActivity":
                            intent = new Intent(mActivity, MyDingDanActivity.class);
                            intent.putExtra("State", 0);
                            intent.putExtra("title", title);
                            startActivity(intent);
                            break;
                        case "MyDaiJinQuanActivity":
                            intent = new Intent(mActivity, MyDaiJinQuanActivity.class);
                            startActivity(intent);
                            break;
                        case "MyDaiGouActivity":
                            intent = new Intent(mActivity, MyDaiGouActivity.class);
                            startActivity(intent);
                            break;
                        case "MyTiXianActivity":
                            intent = new Intent(mActivity, MyTiXianActivity.class);
                            startActivity(intent);
                            break;
                        case "MyGeRenActivity":
                            intent = new Intent(mActivity, UserInfoActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            try {
                                Class class1 = Class.forName("com.example.nyapp." + url);
                                intent = new Intent(mActivity, class1);
                                intent.putExtra("State", 0);
                                intent.putExtra("title", title);
                                startActivity(intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}
