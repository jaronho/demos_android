package com.example.nyapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.bottom.erised.Erised;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.example.classes.BaseEventBean;
import com.example.classes.CouponPic;
import com.example.classes.GpsLocation;
import com.example.classes.MyGPS;
import com.example.classes.MyGPSUtil;
import com.example.classes.MyGPSloction;
import com.example.classes.TabBean;
import com.example.fragments.Fragment_Classification;
import com.example.fragments.Fragment_HomePage;
import com.example.fragments.Fragment_NY;
import com.example.fragments.Fragment_ShoppingCart;
import com.example.util.GsonUtils;
import com.example.util.LogUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.TianjiaCart;
import com.example.util.UrlContact;
import com.example.view.MyAdDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_Home)
    ImageView mIvHome;
    @BindView(R.id.iv_Classification)
    ImageView mIvClassification;
    @BindView(R.id.iv_ShoppingCart)
    ImageView mIvShoppingCart;
    @BindView(R.id.iv_MyNY)
    ImageView mIvMyNY;
    @BindView(R.id.rl_HomePageFragment)
    RelativeLayout mRlHomePageFragment;
    @BindView(R.id.rl_ClassificationFragment)
    RelativeLayout mRlClassificationFragment;
    @BindView(R.id.rl_ShoppingCartFragment)
    RelativeLayout mRlShoppingCartFragment;
    @BindView(R.id.rl_MyNYFragment)
    RelativeLayout mRlMyNYFragment;
    @BindView(R.id.tv_dot2)
    TextView mTvDot2;

    private RelativeLayout[] mTabs;
    private Fragment_HomePage mHomePageFragment;
    private Fragment[] fragments;
    private MyGPS gps;
    private int index;
    private ACache mCache;
    private String gpsString;
    private String isflag;
    // 当前fragment的index
    private int currentTabIndex;
    private LocationService locationService;
    private MyGPSloction gpsLocation = GpsLocation.getgps();
    private long startTime = 0;
    private int mShoppingNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Erised.init(this, "afcaa5d0bd");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mCache = ACache.get(MainActivity.this);
        isflag = mCache.getAsString("isflag");
        gpsString = mCache.getAsString("gpsString");

        initGps();
        initTab();
        initAD();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showShoppingNum();
    }

    private void initGps() {
        try {

            if (gpsString == null) {
                mCache.put("gpsString", "false");
                locationService = ((MyApplication) getApplication()).locationService;
                locationService.registerListener(mListener);
                // 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                // 注册监听
                if (isflag == null) {
                    mCache.put("isflag", "true");

                    int type = getIntent().getIntExtra("from", 0);
                    if (type == 0) {
                        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                        locationService.start();
                    } else if (type == 1) {
                        locationService.setLocationOption(locationService.getOption());
                    }
                }

            } else if (gpsString.equals("true")) {
                gps = (MyGPS) mCache.getAsObject("gps");
            }
        } catch (Exception e) {
            // 定位错误（）
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            String a = stackTraceElement.getFileName() + "\r\n" + stackTraceElement.getMethodName() + "\r\n"
                    + stackTraceElement.getLineNumber() + "\r\n" + e.toString();
            postLog(a);
        }
    }

    private void initData() {
        mTabs = new RelativeLayout[5];
        mTabs[0] = mRlHomePageFragment;
        mTabs[1] = mRlClassificationFragment;
        mTabs[2] = mRlShoppingCartFragment;
        mTabs[3] = mRlMyNYFragment;
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        mHomePageFragment = new Fragment_HomePage();
        Fragment_Classification fragmentClassification = new Fragment_Classification();
        Fragment_ShoppingCart fragmentShoppingCart = new Fragment_ShoppingCart();
        Fragment_NY fragmentNy = new Fragment_NY();
        fragments = new Fragment[]{mHomePageFragment, fragmentClassification, fragmentShoppingCart, fragmentNy};
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragmentNy)
                .hide(fragmentNy)
                .add(R.id.fragment_container, fragmentShoppingCart)
                .hide(fragmentShoppingCart)
                .add(R.id.fragment_container, mHomePageFragment)
                .show(mHomePageFragment)
                .commit();
    }

    //初始化底部导航
    private void initTab() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "3");
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
                            TabBean tabBean = GsonUtils.getInstance().fromJson(response, TabBean.class);
                            if (tabBean.isResult()) {
                                List<TabBean.DataBean> data = tabBean.getData();
                                if (data != null && data.size() == 4) {
                                    MyGlideUtils.loadNativeImage(MainActivity.this, data.get(0).getPic_Path(), mIvHome);
                                    MyGlideUtils.loadNativeImage(MainActivity.this, data.get(1).getPic_Path(), mIvClassification);
                                    MyGlideUtils.loadNativeImage(MainActivity.this, data.get(2).getPic_Path(), mIvShoppingCart);
                                    MyGlideUtils.loadNativeImage(MainActivity.this, data.get(3).getPic_Path(), mIvMyNY);
                                }
                            }
                        }
                    }
                });
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            locationService.unregisterListener(mListener); // 注销掉监听
            locationService.stop(); // 停止定位服务
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == BDLocation.TypeGpsLocation
                        || location.getLocType() == BDLocation.TypeNetWorkLocation
                        || location.getLocType() == BDLocation.TypeOffLineLocation) {
                    if (gpsLocation.getCityName() == null && gpsLocation.getCountyName() == null
                            && gpsLocation.getProvinceName() == null) {
                        gpsLocation.setProvinceName(location.getProvince());
                        gpsLocation.setCityName(location.getCity());
                        gpsLocation.setCountyName(location.getDistrict());
                        try {
                            String proName = URLEncoder.encode(location.getProvince(), "UTF-8");
                            String cityName = URLEncoder.encode(location.getCity(), "UTF-8");
                            String countyName = URLEncoder.encode(location.getDistrict(), "UTF-8");

                            getGps(proName + "," + cityName + "," + countyName);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }

    };

    //获取GPS
    public void getGps(String address) {
        Map<String, String> params = new HashMap<>();
        params.put("addr", address);
        MyOkHttpUtils
                .getData(UrlContact.URL_GPS, params)
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
                                gps = MyGPSUtil.getGps(response);
                                mCache = ACache.get(MyApplication.sContext);
                                if (gps == null) {
                                    mCache.put("gpsString", "false");
                                } else {
                                    mCache.put("gpsString", "true");

                                }
                                mCache.put("gps", gps);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * button点击事件
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_HomePageFragment:
                mHomePageFragment.showShoppingNum();
                index = 0;
                break;
            case R.id.rl_ClassificationFragment:
                index = 1;
                break;
            case R.id.rl_ShoppingCartFragment:
                EventBus.getDefault().post(new BaseEventBean(2, 1));
                index = 2;
                break;
            case R.id.rl_MyNYFragment:
                index = 3;
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;

        showShoppingNum();
    }

    private void showShoppingNum() {
        getShoppingCartNum();
        if (index == 2) {
            mTvDot2.setVisibility(View.GONE);
        } else {
            if (mShoppingNum != 0) {
                mTvDot2.setVisibility(View.VISIBLE);
                mTvDot2.setText(String.valueOf(mShoppingNum));
            } else {
                mTvDot2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initView() {

    }

    public void showFragment(int newPosition) {
        index = newPosition;
        mTabs[newPosition].performClick();
    }

    // 发送错误信息log
    public void postLog(String message) {
        LogUtils.e(message);
        Map<String, String> params = new HashMap<>();
        params.put("Message", message);
        MyOkHttpUtils
                .postData(UrlContact.URL_RECORD_LOG, params)
                .build()
                .connTimeOut(10000)      // 设置当前请求的连接超时时间
                .readTimeOut(10000)      // 设置当前请求的读取超时时间
                .writeTimeOut(10000)     // 设置当前请求的写入超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mTabs[0].isSelected()) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) >= 2000) {
                    Toast.makeText(MainActivity.this, "再按一次退出农一网", Toast.LENGTH_SHORT).show();
                    startTime = currentTime;
                } else {
                    finish();
                }
            } else {
                mTabs[currentTabIndex].setSelected(false);
                mTabs[0].setSelected(true);
                getSupportFragmentManager().beginTransaction().hide(fragments[currentTabIndex]).show(mHomePageFragment).commit();
                currentTabIndex = 0;
            }
        }
        return true;
    }

    public void getShoppingCartNum() {
        mShoppingNum = TianjiaCart.getShoppingNum(this);
    }

    //获取广告数据
    private void initAD() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "0");
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
                            CouponPic couponPic = GsonUtils.getInstance().fromJson(response, CouponPic.class);
                            if (couponPic.isResult()) {
                                List<CouponPic.DataBean> dataBeen = couponPic.getData();
                                if (dataBeen != null && dataBeen.size() > 0) {
                                    if (MyAdDialog.isShowAd == 0) {
                                        MyAdDialog.isShowAd = 1;
                                        int type = dataBeen.get(0).getType();
                                        String url = dataBeen.get(0).getUrl();
                                        String mPicUrl = dataBeen.get(0).getPic_Path();
                                        MyAdDialog.getInstance(MainActivity.this, mPicUrl, type, url);//广告弹窗
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
