package com.example.nyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.adapter.ProductListAdapter;
import com.example.classes.ProductJson;
import com.example.classes.ProductListBean;
import com.example.classes.User;
import com.example.util.GsonUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.example.view.DividerItemDecoration;
import com.example.view.MyFlowLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 产品列表界面
 */
public class ProductListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.et_searchWord)
    EditText mEtSearchWord;
    @BindView(R.id.ll_searchWord)
    LinearLayout mLlSearchWord;
    @BindView(R.id.iv_numSort)
    ImageView mIvNumSort;
    @BindView(R.id.iv_priceSort)
    ImageView mIvPriceSort;
    @BindView(R.id.tv_shoppingNum)
    TextView mTvShoppingNum;
    @BindView(R.id.ll_proListCoupon)
    LinearLayout mLlProListCoupon;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.ll_back_top)
    LinearLayout mLlBackTop;
    @BindView(R.id.fl_business)
    MyFlowLayout mFlBusiness;
    @BindView(R.id.fl_preparation)
    MyFlowLayout mFlPreparation;
    @BindView(R.id.fl_type)
    MyFlowLayout mFlType;
    @BindView(R.id.fl_price)
    MyFlowLayout mFlPrice;
    @BindView(R.id.rl_filter)
    RelativeLayout mRlFilter;
    private static final String SP_SEARCH_HISTORY = "sp_search_history";
    @BindView(R.id.iv_useCoupon)
    ImageButton mIvUseCoupon;
    @BindView(R.id.iv_buyCoupon)
    ImageButton mIvBuyCoupon;
    private SharedPreferences mSharePreference;
    private ProductListActivity mActivity;
    private List<ProductJson> mSearchProductList;//商品条目数据
    private ArrayList<String> mDosageformlist;//剂型
    private ArrayList<String> mManuf_namelist;//企业名
    private ArrayList<String> mTypelist;//类型
    private ArrayList<String> mPricelist;//价格
    private String mDosageform = "";
    private String mManufName = "";
    private String mType = "";
    private String mPrice = "";
    private String mFrom;
    private String mKeyWord;
    private String mState;
    private String mRuleId;
    private String mCouponType;
    private int mIndex = 1;
    private LinearLayoutManager mLayoutManager;
    private String mUser_name;
    private ProductListAdapter mProductListAdapter;
    private int loading = 0;
    private ProductListBean.DataBean mDataBean;
    private ProductListBean.DataBean mInitDataBean;
    private boolean isInitFilter = true;
    private LinearLayout.LayoutParams mLlp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_list);
        ButterKnife.bind(this);
        mActivity = this;
        mSharePreference = getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE);
        mLlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlp.setMargins(12, 10, 12, 10);
        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mState = intent.getStringExtra("type");
        mRuleId = intent.getStringExtra("ruleId");
        mCouponType = intent.getStringExtra("couponType");

        mFrom = mFrom == null ? "" : mFrom;
        mState = mState == null ? "" : mState;
        mRuleId = mRuleId == null ? "" : mRuleId;
        mCouponType = mCouponType == null ? "" : mCouponType;

        mEtSearchWord.setText(mState);
        initView();
    }

    @Override
    public void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mLayoutManager.findFirstVisibleItemPosition() > 1) {
                    mLlBackTop.setVisibility(View.VISIBLE);
                } else {
                    mLlBackTop.setVisibility(View.GONE);
                }
            }
        });
        mSwipeTarget.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("id", mSearchProductList.get(position).getId());
                intent.putExtra("name", mSearchProductList.get(position).getPro_Name());
                intent.putExtra("type", mSearchProductList.get(position).getType());
                mActivity.startActivity(intent);
            }
        });
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }


    private void initData() {
        if (!isLogin()) {
            mUser_name = "";
        }
        if (mSearchProductList == null) {
            mSearchProductList = new ArrayList<>();
        }
        if (mDosageformlist == null) {
            mDosageformlist = new ArrayList<>();
        }
        if (mManuf_namelist == null) {
            mManuf_namelist = new ArrayList<>();
        }
        if (mTypelist == null) {
            mTypelist = new ArrayList<>();
        }
        if (mPricelist == null) {
            mPricelist = new ArrayList<>();
            mPricelist.add("0-100");
            mPricelist.add("100-200");
            mPricelist.add("200-500");
            mPricelist.add("500-1000");
            mPricelist.add("1000以上");
        }
        mKeyWord = mEtSearchWord.getText().toString();
        Map<String, String> params = new TreeMap<>();
        params.put("loginKey", mUser_name);
        params.put("deviceId", MyApplication.sUdid);
        switch (mFrom) {
            case "search":
                params.put("KeyWords", mKeyWord);
                params.put("ruleId", mRuleId);
                params.put("couponType", mCouponType);
                params.put("Manuf_Name", mManufName);
                params.put("Dosageform", mDosageform);
                params.put("Type", mType);
                params.put("Price", mPrice);
                params.put("PageIndex", String.valueOf(mIndex));
                break;
            case "first":
                params.put("Type", mKeyWord);
                params.put("ruleId", mRuleId);
                params.put("couponType", mCouponType);
                params.put("PageIndex", String.valueOf(mIndex));
                break;
            case "class":
                params.put("Cate_Id", mState);
                params.put("KeyWords", mKeyWord);
                params.put("ruleId", mRuleId);
                params.put("couponType", mCouponType);
                params.put("Manuf_Name", mManufName);
                params.put("Dosageform", mDosageform);
                params.put("Type", mType);
                params.put("Price", mPrice);
                params.put("PageIndex", String.valueOf(mIndex));
                break;
        }
        getProductListData(params);
    }

    //获取商品列表数据
    private void getProductListData(Map<String, String> params) {
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_LIST, params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        stopSwipeToLoad();
                        if (response != null) {
                            ProductListBean productListBean = GsonUtils.getInstance().fromJson(response, ProductListBean.class);
                            if (productListBean.isResult()) {
                                mDataBean = productListBean.getData();
                                mLlProListCoupon.setVisibility(mDataBean.isShowMarket() ? View.VISIBLE : View.GONE);
                                mTvShoppingNum.setText("（共" + mDataBean.getTotal_Count() + "个产品）");
                                if (mDataBean != null) {
                                    List<ProductJson> vSearchProduct = mDataBean.getV_SearchProduct();
                                    if (loading == 0) {
                                        mSearchProductList = vSearchProduct;
                                        if (isInitFilter) {
                                            mInitDataBean = mDataBean;
                                            isInitFilter = false;
                                        }
                                        setFilterData(mDataBean);
                                        setProductListData();
                                    } else {
                                        if (vSearchProduct.size() <= 0) {
                                            mIndex = mIndex - 1;
                                            MyToastUtil.showLongMessage("已经到底了!");
                                        } else {
                                            int size = mSearchProductList.size();
                                            mSearchProductList.addAll(vSearchProduct);
                                            mProductListAdapter.setNewData(mSearchProductList);
                                            mSwipeTarget.smoothScrollToPosition(size);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //设置筛选数据
    private void setFilterData(ProductListBean.DataBean dataBean) {
        mDosageformlist.clear();
        mManuf_namelist.clear();
        mTypelist.clear();
        for (ProductListBean.DataBean.DosageformlistBean dosageformlistBean : dataBean.getDosageformlist()) {
            mDosageformlist.add(dosageformlistBean.getDosageform());
        }
        for (ProductListBean.DataBean.ManufNamelistBean manufNamelistBean : dataBean.getManuf_Namelist()) {
            mManuf_namelist.add(manufNamelistBean.getManuf_Name());
        }
        for (ProductListBean.DataBean.TypelistBean typelistBean : dataBean.getTypelist()) {
            mTypelist.add(typelistBean.getType());
        }

        setFilterDate(mManuf_namelist, mFlBusiness, 1);
        setFilterDate(mDosageformlist, mFlPreparation, 2);
        setFilterDate(mTypelist, mFlType, 3);
        setFilterDate(mPricelist, mFlPrice, 4);
    }

    //设置商品列表数据
    private void setProductListData() {
        if (mProductListAdapter == null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
            dividerItemDecoration.setDivider(R.drawable.divider_bg);
            mLayoutManager = new LinearLayoutManager(this);
            mProductListAdapter = new ProductListAdapter(mSearchProductList, this);
            mSwipeTarget.setLayoutManager(mLayoutManager);
            mSwipeTarget.addItemDecoration(dividerItemDecoration);
            mSwipeTarget.setAdapter(mProductListAdapter);

            View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
            mProductListAdapter.setEmptyView(view);
        } else {
            mProductListAdapter.setNewData(mSearchProductList);
        }
    }

    @OnClick({R.id.btn_search_back, R.id.tv_search, R.id.ll_numSort, R.id.ll_priceSort, R.id.btn_filter, R.id.view_exit,
            R.id.iv_useCoupon, R.id.iv_buyCoupon, R.id.iv_back_top, R.id.tv_exitFilter, R.id.btn_reset, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_back:
                finish();
                break;
            case R.id.tv_search:
                save();
                isInitFilter = true;
                mSwipeToLoadLayout.setRefreshing(true);
                break;
            case R.id.ll_numSort:
                sortForNum();
                break;
            case R.id.ll_priceSort:
                sortForPrice();
                break;
            case R.id.btn_filter:
                mRlFilter.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_useCoupon:
                boolean isUseCoupon = mCouponType.equals("1");
                mIvUseCoupon.setSelected(!isUseCoupon);
                mIvBuyCoupon.setSelected(false);
                if (isUseCoupon) {
                    mCouponType = "";
                    mRuleId = "";
                } else {
                    mCouponType = "1";
                    mRuleId = "9";
                }
                mSwipeToLoadLayout.setRefreshing(true);
                break;
            case R.id.iv_buyCoupon:
                boolean isBuyCoupon = mCouponType.equals("2");
                mIvBuyCoupon.setSelected(!isBuyCoupon);
                mIvUseCoupon.setSelected(false);
                if (isBuyCoupon) {
                    mCouponType = "";
                    mRuleId = "";
                } else {
                    mCouponType = "2";
                    mRuleId = "9";
                }
                mSwipeToLoadLayout.setRefreshing(true);
                break;
            case R.id.iv_back_top:
                mSwipeTarget.smoothScrollToPosition(0);
                break;
            case R.id.tv_exitFilter:
                mRlFilter.setVisibility(View.GONE);
                break;
            case R.id.view_exit:
                mRlFilter.setVisibility(View.GONE);
                break;
            case R.id.btn_reset:
                mManufName = "";
                mDosageform = "";
                mType = "";
                mPrice = "";
                setFilterData(mInitDataBean);
                break;
            case R.id.btn_confirm:
                mRlFilter.setVisibility(View.GONE);
                mSwipeToLoadLayout.setRefreshing(true);
                break;
        }
    }

    private boolean isSortPrice = true;

    //价格排序
    private void sortForPrice() {
        if (mSearchProductList != null) {
            mIvPriceSort.setBackgroundResource(isSortPrice ? R.drawable.arrow_down : R.drawable.arrow_top);
            Collections.sort(mSearchProductList, new Comparator<ProductJson>() {
                @Override
                public int compare(ProductJson o1, ProductJson o2) {
                    double deal1 = o1.getPrice();
                    double deal2 = o2.getPrice();
                    if (deal2 > deal1) {
                        return isSortPrice ? 1 : -1;
                    } else if (deal2 == deal1) {
                        return 0;
                    } else {
                        return isSortPrice ? -1 : 1;
                    }
                }
            });
            isSortPrice = !isSortPrice;
            isSortNum = true;
            mProductListAdapter.setNewData(mSearchProductList);
        }
    }

    private boolean isSortNum;

    //销量排序
    private void sortForNum() {
        if (mSearchProductList != null) {
            mIvNumSort.setBackgroundResource(isSortNum ? R.drawable.arrow_down : R.drawable.arrow_top);
            Collections.sort(mSearchProductList, new Comparator<ProductJson>() {
                @Override
                public int compare(ProductJson o1, ProductJson o2) {
                    int deal1 = o1.getDeal();
                    int deal2 = o2.getDeal();
                    if (deal2 > deal1) {
                        return isSortNum ? 1 : -1;
                    } else if (deal2 == deal1) {
                        return 0;
                    } else {
                        return isSortNum ? -1 : 1;
                    }
                }
            });
            isSortNum = !isSortNum;
            isSortPrice = true;
            mProductListAdapter.setNewData(mSearchProductList);
        }
    }

    @Override
    public void onRefresh() {
                mIndex = 1;
                loading = 0;
                initData();
    }

    @Override
    public void onLoadMore() {
                mIndex = mIndex + 1;
                loading = 1;
                initData();
    }

    private int mPressed = -1;

    private void setFilterDate(final ArrayList<String> list, final MyFlowLayout viewGroup, final int fag) {
        viewGroup.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(list.get(i));
            if (i == mPressed) {
                view.setTextColor(this.getResources().getColor(R.color.red));
                view.setBackgroundResource(R.drawable.view_filter_item_pressed_bg);
                mPressed = -1;
            } else {
                view.setTextColor(this.getResources().getColor(R.color.gray_normal));
                view.setBackgroundResource(R.drawable.view_filter_item_normal_bg);
            }
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPressed = finalI;
                    setSearchFilter(finalI, fag);
                    setFilterDate(list, viewGroup, fag);
                }
            });
            viewGroup.addView(view, mLlp);
        }
    }

    public void setSearchFilter(int position, int fag) {
        switch (fag) {
            case 1:
                mManufName = mManuf_namelist.get(position);
                break;
            case 2:
                mDosageform = mDosageformlist.get(position);
                break;
            case 3:
                mType = mTypelist.get(position);
                break;
            case 4:
                mPrice = mPricelist.get(position);
                break;
        }
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

    private void save() {
        String text = mEtSearchWord.getText().toString().trim();
        String oldText = mSharePreference.getString("history_keyWord", "");
        if (!TextUtils.isEmpty(text) && !oldText.contains(text)) {
            SharedPreferences.Editor mEditor = mSharePreference.edit();
            mEditor.putString("history_keyWord", text + "," + oldText);
            mEditor.apply();
        }
    }

    private boolean isLogin() {
        ACache cache = ACache.get(this);
        String loginString = cache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            cache.put("loginString", loginString);
            return false;
        } else {
            if (loginString.equals("true")) {
                User user = (User) cache.getAsObject("user");
                mUser_name = user.getUser_Name();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRlFilter.getVisibility() == View.VISIBLE) {
                mRlFilter.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
        return true;
    }

}
