package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.adapter.ShoppingCartAdapter;
import com.example.classes.BaseEventBean;
import com.example.classes.ShoppingCartBean;
import com.example.classes.User;
import com.example.util.CustomDialog;
import com.example.util.DoubleUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.TianjiaCart;
import com.example.util.UrlContact;
import com.example.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ShoppingCarActivity extends BaseActivity {
    @BindView(R.id.layout_back)
    LinearLayout mLayoutBack;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.btn_edit_over)
    Button mBtnEditOver;
    @BindView(R.id.rcy_shopping_cart)
    RecyclerView mRcyShoppingCart;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.tv_total_prices)
    TextView mTvTotalPrices;
    @BindView(R.id.tv_FreePrice)
    TextView mTvFreePrice;
    @BindView(R.id.rl_balance)
    RelativeLayout mRlBalance;
    @BindView(R.id.btn_To_Settlement)
    Button mBtnToSettlement;
    private ShoppingCarActivity mActivity;
    private ACache mCache;
    private String mUserName;
    private JSONArray mShoppingNumJsonArray;
    private List<ShoppingCartBean.DataBean.ProductBean> mProductBeanList = new ArrayList<>();//所有显示的商品
    private List<ShoppingCartBean.DataBean.ProductBean> mLastProductBeanList = new ArrayList<>();//上一次显示的商品
    private List<ShoppingCartBean.DataBean.ProductBean> mInvalidProduct;//失效商品
    public static List<ShoppingCartBean.DataBean.ProductBean> items2;//勾选上的商品
    private ShoppingCartAdapter mShoppingCartAdapter;
    private View mEmptyView;
    private int mNum;//0：初始化 1：首页 2：商品详情 3：订单提交页
    private MyMsgDialog mDeleteDialog;
    private CustomDialog mNumUpdateDialog;
    private boolean isEdit;//是否处于编辑状态
    private double mTotalMoney;
    private double mTotalFreePrice;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_shopping_cart);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mActivity = this;
        mNum = 0;
        isLogin();
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        mLayoutBack.setVisibility(View.VISIBLE);
        mEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.view_empty, null);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.divider_bg);

        mRcyShoppingCart.addItemDecoration(dividerItemDecoration);
        mRcyShoppingCart.setLayoutManager(new LinearLayoutManager(mActivity));
        mRcyShoppingCart.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                ShoppingCartBean.DataBean.ProductBean productBean = mProductBeanList.get(position);
                switch (view.getId()) {
                    case R.id.cb_Check:
                        mProductBeanList.get(position).setCheck(!mProductBeanList.get(position).isCheck());
                        mShoppingCartAdapter.setNewData(mProductBeanList);
                        getTotalData();
                        break;
                    case R.id.iv_shoppingCart:
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        intent.putExtra("id", mProductBeanList.get(position).getPro_Id());
                        intent.putExtra("name", mProductBeanList.get(position).getPro_Name());
                        intent.putExtra("type", mProductBeanList.get(position).getType());
                        startActivity(intent);
                        break;
                    case R.id.btn_Del:
                        mDeleteDialog = new MyMsgDialog(mActivity, "系统消息", "您确定要删除这个产品吗？",
                                new MyMsgDialog.ConfirmListener() {
                                    @Override
                                    public void onClick() {
                                        mDeleteDialog.dismiss();
                                        deleteShop(position);
                                        initData();
                                    }
                                },
                                new MyMsgDialog.CancelListener() {
                                    @Override
                                    public void onClick() {
                                        mDeleteDialog.dismiss();
                                    }
                                });
                        mDeleteDialog.setCancelable(false);
                        mDeleteDialog.show();
                        break;
                    case R.id.btn_minus_car:
                        int count = productBean.getCount();
                        if (count <= 1) {
                            MyToastUtil.showShortMessage("商品数量最低为1。");
                        } else {
                            count = count - 1;
                            TianjiaCart.addCart2(productBean.getPro_Id(), String.valueOf(count), mActivity);
                            initData();
                        }
                        break;
                    case R.id.tv_shoppingCart_num:
                        showNumUpdateDialog(position);
                        break;
                    case R.id.btn_add_car:
                        count = productBean.getCount();
                        if (count >= 9999) {
                            MyToastUtil.showShortMessage("超过商品数量限制。");
                        } else {
                            count = count + 1;
                            TianjiaCart.addCart2(productBean.getPro_Id(), String.valueOf(count), mActivity);
                            initData();
                        }
                        break;
                }
            }
        });
    }

    private void initData() {
        mShoppingNumJsonArray = mCache.getAsJSONArray("testJsonArray");
        if (mShoppingNumJsonArray != null) {
            getShoppingCart();
        } else {
            mRlBalance.setVisibility(View.GONE);
            if (mShoppingCartAdapter == null) {
                mShoppingCartAdapter = new ShoppingCartAdapter(mProductBeanList, mActivity);
                mShoppingCartAdapter.setEmptyView(mEmptyView);
                mRcyShoppingCart.setAdapter(mShoppingCartAdapter);
            }

        }
    }

    //接受来自其他界面的商品数据修改
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateShoppingCart(BaseEventBean eventBean) {
        int type = eventBean.getType();
        mNum = eventBean.getNum();
        if (type == 2) {
            if (mNum != 0) {
                MyProgressDialog.show(mActivity, true, true);
                initData();
            }
        }
    }

    //获取购物车数据
    private void getShoppingCart() {
        if (isLogin()) {
            mUserName = mUser.getUser_Name();
        } else {
            mUserName = "";
        }
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        map.put("CartItem", mShoppingNumJsonArray.toString());
        MyOkHttpUtils
                .postData(UrlContact.URL_SHOPPING_CART, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (mNum != 0) {
                            MyProgressDialog.cancel();
                        }
                        mRlBalance.setVisibility(View.GONE);
                        if (mShoppingCartAdapter == null) {
                            mShoppingCartAdapter = new ShoppingCartAdapter(mProductBeanList, mActivity);
                            mShoppingCartAdapter.setEmptyView(mEmptyView);
                            mRcyShoppingCart.setAdapter(mShoppingCartAdapter);
                        }
                        MyToastUtil.showShortMessage("购物车请求失败！请稍后重试！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (mNum != 0) {
                            MyProgressDialog.cancel();
                        }
                        if (response != null) {
                            Gson gson = new Gson();
                            ShoppingCartBean shoppingCartBean = gson.fromJson(response, ShoppingCartBean.class);
                            if (shoppingCartBean.isResult()) {
                                ShoppingCartBean.DataBean dataBean = shoppingCartBean.getData();
                                if (dataBean != null) {
                                    mProductBeanList = dataBean.getProduct();
                                    mInvalidProduct = dataBean.getInvalidProduct();
                                    if (mProductBeanList != null) {
                                        for (ShoppingCartBean.DataBean.ProductBean productBean : mProductBeanList) {
                                            TianjiaCart.addCart2(productBean.getPro_Id(), productBean.getCount() + "", mActivity);
                                        }

                                        if (mInvalidProduct != null) {
                                            for (ShoppingCartBean.DataBean.ProductBean productBean : mInvalidProduct) {
                                                TianjiaCart.addCart2(productBean.getPro_Id(), productBean.getCount() + "", mActivity);
                                            }
                                            if (mInvalidProduct.size() > 0) {
                                                for (int i = 0; i < mInvalidProduct.size(); i++) {
                                                    mProductBeanList.add(mInvalidProduct.get(i));
                                                }
                                                mInvalidProduct.clear();
                                            }
                                        }
                                        for (int i = 0; i < mLastProductBeanList.size(); i++) {
                                            ShoppingCartBean.DataBean.ProductBean lastProductBean = mLastProductBeanList.get(i);
                                            for (int j = 0; j < mProductBeanList.size(); j++) {
                                                ShoppingCartBean.DataBean.ProductBean productBean = mProductBeanList.get(j);
                                                if (productBean.getPro_Id() == lastProductBean.getPro_Id()) {
                                                    productBean.setCheck(lastProductBean.isCheck());
                                                }
                                            }
                                        }
                                        mLastProductBeanList = mProductBeanList;
                                        setShoppingCartData();
                                    } else {
                                        mRlBalance.setVisibility(View.GONE);
                                        if (mShoppingCartAdapter == null) {
                                            mShoppingCartAdapter = new ShoppingCartAdapter(mProductBeanList, mActivity);
                                            mShoppingCartAdapter.setEmptyView(mEmptyView);
                                            mRcyShoppingCart.setAdapter(mShoppingCartAdapter);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //设置购物车数据
    private void setShoppingCartData() {
        if (mProductBeanList.size() == 0) {
            mRlBalance.setVisibility(View.GONE);
        } else {
            getTotalData();
            mRlBalance.setVisibility(View.VISIBLE);
        }
        if (mShoppingCartAdapter == null) {
            mShoppingCartAdapter = new ShoppingCartAdapter(mProductBeanList, mActivity);
            mShoppingCartAdapter.setEmptyView(mEmptyView);
            mRcyShoppingCart.setAdapter(mShoppingCartAdapter);
        } else {
            mShoppingCartAdapter.setNewData(mProductBeanList);
        }


    }

    //计算总价格和总优惠金额
    private void getTotalData() {
        mTotalMoney = 0;
        mTotalFreePrice = 0;
        boolean isAllCheck = true;//是否所以商品选中
        boolean isSingleCheck = false;//是否有单个商品选中
        if (items2 == null) {
            items2 = new ArrayList<>();
        } else {
            items2.clear();
        }
        for (ShoppingCartBean.DataBean.ProductBean productBean : mProductBeanList) {
            if (productBean.isCheck()) {
                if (productBean.getPriceList().size() != 0) {//是否为失效商品
                    isSingleCheck = true;
                    items2.add(productBean);
                    mTotalMoney = mTotalMoney + productBean.getPrice() * productBean.getCount();
                    mTotalFreePrice = mTotalFreePrice + productBean.getFree_Price();
                }
            } else {
                isAllCheck = false;
            }
        }
        mTotalMoney = mTotalMoney - mTotalFreePrice;

        mCbAll.setChecked(isAllCheck);
        mBtnToSettlement.setEnabled(isSingleCheck && !isEdit);
        mBtnToSettlement.setBackgroundColor((isSingleCheck && !isEdit) ? 0xffff4b00 : 0x7fcccccc);

        mTvTotalPrices.setText("总共花费" + DoubleUtils.format2decimals(mTotalMoney) + "元");
        if (mTotalFreePrice > 0) {
            mTvFreePrice.setVisibility(View.VISIBLE);
            mTvFreePrice.setText("共优惠" + DoubleUtils.format2decimals(mTotalFreePrice) + "元");
        } else {
            mTvFreePrice.setVisibility(View.GONE);
        }
    }

    //判断是否登录
    private boolean isLogin() {
        mCache = ACache.get(mActivity);
        String loginString = mCache.getAsString("loginString");
        if (loginString == null) {
            loginString = "false";
            mCache.put("loginString", loginString);
            mUserName = "";
            return false;
        } else {
            if (loginString.equals("true")) {
                mUser = (User) mCache.getAsObject("user");
                mUserName = mUser.getUser_Name();
                return true;
            } else {
                mUserName = "";
                return false;
            }
        }
    }

    //显示商品数量更改弹窗
    private void showNumUpdateDialog(int position) {
        final ShoppingCartBean.DataBean.ProductBean productBean = mProductBeanList.get(position);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.view_shopping_num_update, null);

        final EditText et_num = (EditText) contentView.findViewById(R.id.et_num);
        et_num.setText(String.valueOf(productBean.getCount()));

        Button btn_numSubtract = (Button) contentView.findViewById(R.id.btn_numSubtract);
        btn_numSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(et_num.getText().toString());
                if (count <= 1) {
                    MyToastUtil.showShortMessage("商品数量最低为1。");
                } else {
                    count = count - 1;
                    et_num.setText(String.valueOf(count));
                }
            }
        });
        Button btn_numAdd = (Button) contentView.findViewById(R.id.btn_numAdd);
        btn_numAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(et_num.getText().toString());
                if (count >= 9999) {
                    MyToastUtil.showShortMessage("超过商品数量限制。");
                } else {
                    count = count + 1;
                    et_num.setText(String.valueOf(count));
                }
            }
        });

        mNumUpdateDialog = new CustomDialog(mActivity, "修改商品数量", contentView,
                new CustomDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mNumUpdateDialog.dismiss();
                        TianjiaCart.addCart2(productBean.getPro_Id(), et_num.getText().toString(), mActivity);
                        initData();
                    }
                },
                new CustomDialog.CancelListener() {
                    @Override
                    public void onClick() {
                        mNumUpdateDialog.dismiss();
                    }
                });
        mNumUpdateDialog.setCancelable(false);
        mNumUpdateDialog.show();
    }

    //删除商品
    private void deleteShop(int position) {
        JSONArray shoppingJsonArray = mCache.getAsJSONArray("testJsonArray");
        for (int i = 0; i < shoppingJsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = shoppingJsonArray.getJSONObject(i);
                if (mProductBeanList.get(position).getPro_Id() == jsonObject.getInt("Id")) {
                    try {
                        TianjiaCart.delete(i, shoppingJsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mCache.put("testJsonArray", shoppingJsonArray);
    }

    @OnClick({R.id.btn_edit, R.id.btn_edit_over, R.id.cb_all, R.id.btn_To_Settlement, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                mBtnEdit.setVisibility(View.GONE);
                mBtnEditOver.setVisibility(View.VISIBLE);
                isEdit = true;
                mShoppingCartAdapter.setEdit(isEdit);
                getTotalData();
                break;
            case R.id.btn_edit_over:
                mBtnEdit.setVisibility(View.VISIBLE);
                mBtnEditOver.setVisibility(View.GONE);
                isEdit = false;
                mShoppingCartAdapter.setEdit(isEdit);
                getTotalData();
                break;
            case R.id.cb_all:
                for (ShoppingCartBean.DataBean.ProductBean productBean : mProductBeanList) {
                    productBean.setCheck(mCbAll.isChecked());
                }
                mShoppingCartAdapter.setNewData(mProductBeanList);
                getTotalData();
                break;
            case R.id.btn_To_Settlement:
                if (isLogin()) {
                    Intent intent = new Intent(mActivity, DingdanActivity.class);
                    intent.putExtra("from", "activity");
                    intent.putExtra("zongjia", mTotalMoney);
                    intent.putExtra("_freePrice", mTotalFreePrice != 0 ? String.valueOf(mTotalFreePrice) : ".00");
                    mActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.layout_back:
                finish();
                break;
        }
    }

}