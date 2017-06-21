package com.example.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.adapter.NYItemAdapter;
import com.example.classes.BaseBean;
import com.example.classes.MyNYBean;
import com.example.classes.NyItemBean;
import com.example.classes.User;
import com.example.classes.UserInfoBean;
import com.example.live.LiveHelper;
import com.example.live.LiveListActivity;
import com.example.live.LiveListChatRoomActivity;
import com.example.nyapp.LoginActivity;
import com.example.nyapp.MainActivity;
import com.example.nyapp.MyApplication;
import com.example.nyapp.MyBangdingPhoneActivity;
import com.example.nyapp.MyDaiGouActivity;
import com.example.nyapp.MyDaiJinQuanActivity;
import com.example.nyapp.MyDingDanActivity;
import com.example.nyapp.MyReceiptActivity;
import com.example.nyapp.MyScoreActivity;
import com.example.nyapp.MyTiXianActivity;
import com.example.nyapp.R;
import com.example.nyapp.TuanGouActivity;
import com.example.nyapp.UserInfoActivity;
import com.example.nyapp.XiugaiMimaActivity;
import com.example.util.ConnectionWork;
import com.example.util.DoubleUtils;
import com.example.util.GsonUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.jaronho.sdk.library.Listener;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by NY on 2017/3/9.
 * 我的农一
 */

public class Fragment_NY extends Fragment {

    @BindView(R.id.ll_unLogin)
    LinearLayout mLlUnLogin;
    @BindView(R.id.iv_login_head)
    ImageView mIvLoginHead;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_identity)
    TextView mTvIdentity;
    @BindView(R.id.ll_Personal_Information)
    LinearLayout mLlPersonalInformation;
    @BindView(R.id.tv_myBalance)
    TextView mTvMyBalance;
    @BindView(R.id.tv_myEarnings)
    TextView mTvMyEarnings;
    @BindView(R.id.tv_frozen)
    TextView mTvFrozen;
    @BindView(R.id.btn_withdraw_cash)
    Button mBtnWithdrawCash;
    @BindView(R.id.ll_login)
    LinearLayout mLlLogin;
    @BindView(R.id.tv_apply_withdraw_cash)
    TextView mTvApplyWithdrawCash;
    @BindView(R.id.et_apply_withdraw_cash)
    EditText mEtApplyWithdrawCash;
    @BindView(R.id.rcy_ny_item)
    RecyclerView mRcyNyItem;
    @BindView(R.id.ll_apply_withdraw_cash)
    LinearLayout mLlApplyWithdrawCash;
    @BindView(R.id.tv_unPayment)
    TextView mTvUnPayment;
    @BindView(R.id.tv_unReceiving)
    TextView mTvUnReceiving;
    @BindView(R.id.btn_SignOut)
    Button mBtnSignOut;
    private MainActivity mActivity;
    private List<NyItemBean> mInitNyItemList = new ArrayList<>();
    private List<NyItemBean> mShowNyItemList;
    private ACache mCache;
    private User mUser;
    private String mUserName;
    private NYItemAdapter mNyItemAdapter;
    private MyNYBean.DataBean mMyNYBeanData;
    private MyMsgDialog mWithdrawDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        initItemData();
    }

    //初始化我的农一的功能Item内容数据
    private void initItemData() {
        mInitNyItemList.clear();
        mInitNyItemList.add(new NyItemBean(0, R.drawable.wodeziliao, "基本信息"));
        mInitNyItemList.add(new NyItemBean(1, R.drawable.daijinjuan, "代金券"));
        mInitNyItemList.add(new NyItemBean(2, R.drawable.pinpaishouquan, "签约授权"));
        mInitNyItemList.add(new NyItemBean(3, R.drawable.shezhi, "账号密码"));

        mInitNyItemList.add(new NyItemBean(4, R.drawable.tixian, "我的提现"));
        mInitNyItemList.add(new NyItemBean(5, R.drawable.fapiao, "我的发票"));
        mInitNyItemList.add(new NyItemBean(6, R.drawable.guize, "代购规则"));
        mInitNyItemList.add(new NyItemBean(7, R.drawable.binding_phone, "绑定手机"));

        mInitNyItemList.add(new NyItemBean(8, R.drawable.integral, "我的积分"));
        mInitNyItemList.add(new NyItemBean(9, R.drawable.shouyi, "我的收益"));//代购员
        mInitNyItemList.add(new NyItemBean(10, R.drawable.authorized_areas_order, "授权区域订单"));//签约代购员
        mInitNyItemList.add(new NyItemBean(11, R.drawable.product_inquiries, "产品查询"));//签约代购员

        mInitNyItemList.add(new NyItemBean(12, R.drawable.mima, "支付密码"));//未设置
        mInitNyItemList.add(new NyItemBean(13, R.drawable.ny_tell, "客服电话"));

        mInitNyItemList.add(new NyItemBean(14, R.drawable.zhibo, "直播"));

        mInitNyItemList.add(new NyItemBean(15, R.drawable.liaotianshi, "聊天室"));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ny, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectionWork.isNetworkAvailable(mActivity)) {
            initData();
        } else {
            MyToastUtil.showShortMessage("网络连接失败,请检查网络!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    private void initData() {
        if (isLogin()) {
            mLlLogin.setVisibility(View.VISIBLE);
            mLlUnLogin.setVisibility(View.GONE);
            mBtnSignOut.setVisibility(View.VISIBLE);
            getUserMessage();
        } else {
            mLlLogin.setVisibility(View.GONE);
            mLlUnLogin.setVisibility(View.VISIBLE);
            mBtnSignOut.setVisibility(View.GONE);
        }
    }

    //获取用户信息
    private void getUserMessage() {
        MyProgressDialog.show(mActivity,true,true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_NY, map)
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
                            Gson gson = new Gson();
                            MyNYBean myNYBean = gson.fromJson(response, MyNYBean.class);
                            if (myNYBean.isResult()) {
                                mMyNYBeanData = myNYBean.getData();
                                if (mMyNYBeanData != null) {
                                    setUserMessage();
                                }

                            } else {
                                MyToastUtil.showShortMessage(myNYBean.getMessage());
                                if (isLogin()) {
                                    setSignOut(1);
                                }
                            }
                        }
                    }
                });
    }

    //设置未登录状态下我的农一可显示的item
    private void setUnLoginNyItem() {
        mShowNyItemList.clear();
        for (int i = 0; i < mInitNyItemList.size(); i++) {
            if (i != 9 && i != 10 && i != 11 && i != 12) {
                mShowNyItemList.add(mInitNyItemList.get(i));
            }
        }
        mNyItemAdapter.setNewData(mShowNyItemList);
    }

    //设置用户信息
    private void setUserMessage() {
        //用户身份状态 1：普通用户 2：代购人 3：乡镇服务专员 4:签约代购员
        LiveHelper.setUserAvatar(mMyNYBeanData.getHeadImage());
        Map<String, String> map = new HashMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_GET_USER_INFO, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            UserInfoBean userInfoBean = GsonUtils.getInstance().fromJson(response, UserInfoBean.class);
                            if (userInfoBean.isResult()) {
                                UserInfoBean.DataBean data = userInfoBean.getData();
                                if (data != null) {
                                    LiveHelper.setNickname(data.getUserDetail().getNick_Name());
                                }
                            }
                        }
                    }
                });

        int permitType = Integer.valueOf(mMyNYBeanData.getPermitType());

        mUser.setPermit_Type(String.valueOf(permitType));
        mUser.setPurchasing_State(mMyNYBeanData.getPurchasState());
        mCache.put("user", mUser);

        try {
            mTvUserName.setText(URLDecoder.decode(mUserName, "UTF-8"));//用户名
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mTvMyBalance.setText(DoubleUtils.format2decimals(mMyNYBeanData.getRemainMoney()) + "元");//我的余额
        mTvMyEarnings.setText(DoubleUtils.format2decimals(mMyNYBeanData.getMoney()) + "元");//我的收益
        mTvFrozen.setText(DoubleUtils.format2decimals(mMyNYBeanData.getFrozenMoney()) + "元");//冻结资金
        MyGlideUtils.loadCircleImage(mActivity, mMyNYBeanData.getHeadImage(), mIvLoginHead);//头像
        mTvIdentity.setText(mMyNYBeanData.getIdentity());//身份
        mBtnWithdrawCash.setText(mMyNYBeanData.getBtnText());//转入余额或收益提现

        mTvUnPayment.setText(String.valueOf(mMyNYBeanData.getToPayTotal()));//待付款订单数
        mTvUnReceiving.setText(String.valueOf(mMyNYBeanData.getToConfirmTotal()));//待收货订单数

        int canSetPayPwd = mMyNYBeanData.getCanSetPayPwd();//支付密码状态

        mShowNyItemList.clear();
        for (int i = 0; i < mInitNyItemList.size(); i++) {
            if (permitType == 4) {//签约代购
                if (canSetPayPwd == 1) {//支付密码未设置
                    mShowNyItemList.add(mInitNyItemList.get(i));
                } else {//支付密码已设置
                    if (i != 12) {
                        mShowNyItemList.add(mInitNyItemList.get(i));
                    }
                }
            } else if (permitType == 2) {//代购
                if (i != 10 && i != 11) {
                    if (canSetPayPwd == 1) {//支付密码未设置
                        mShowNyItemList.add(mInitNyItemList.get(i));
                    } else {//支付密码已设置
                        if (i != 12) {
                            mShowNyItemList.add(mInitNyItemList.get(i));
                        }
                    }
                }
            } else {//会员
                if (i != 9 && i != 10 && i != 11) {
                    if (canSetPayPwd == 1) {//支付密码未设置
                        mShowNyItemList.add(mInitNyItemList.get(i));
                    } else {//支付密码已设置
                        if (i != 12) {
                            mShowNyItemList.add(mInitNyItemList.get(i));
                        }
                    }
                }
            }
        }

        mNyItemAdapter.setNewData(mShowNyItemList);
    }

    private void initView() {
        mShowNyItemList = new ArrayList<>();
        mRcyNyItem.setLayoutManager(new GridLayoutManager(mActivity, 4));
        for (int i = 0; i < mInitNyItemList.size(); i++) {
            if (i != 9 && i != 10 && i != 11 && i != 12) {
                mShowNyItemList.add(mInitNyItemList.get(i));
            }
        }
        mNyItemAdapter = new NYItemAdapter(mShowNyItemList);
        mRcyNyItem.setAdapter(mNyItemAdapter);
        mRcyNyItem.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = mShowNyItemList.get(position).getType();
                if (isLogin()) {
                    switch (type) {
                        case 0://基本信息
                            Intent intent = new Intent(mActivity, UserInfoActivity.class);
                            startActivity(intent);
                            break;
                        case 1://代金券
                            intent = new Intent(mActivity, MyDaiJinQuanActivity.class);
                            startActivity(intent);
                            break;
                        case 2://签约授权
                            intent = new Intent(mActivity, TuanGouActivity.class);
                            intent.putExtra("payurl", UrlContact.WEB_URL_STRING + "/UserCenter/ProtocolDetail.html?loginKey="
                                    + mUserName + "&deviceId=" + MyApplication.sUdid);
                            intent.putExtra("type", 7);
                            intent.putExtra("title", "我的签约授权");
                            intent.putExtra("loginKey", mUserName);
                            intent.putExtra("deviceId", MyApplication.sUdid);
                            startActivity(intent);
                            break;
                        case 3://账号密码
                            intent = new Intent(mActivity, XiugaiMimaActivity.class);
                            startActivity(intent);
                            break;
                        case 4://我的提现
                            intent = new Intent(mActivity, MyTiXianActivity.class);
                            startActivity(intent);
                            break;
                        case 5://我的发票
                            intent = new Intent(mActivity, MyReceiptActivity.class);
                            startActivity(intent);
                            break;
                        case 6://代购规则
                            intent = new Intent(mActivity, TuanGouActivity.class);
                            intent.putExtra("payurl", UrlContact.URL_STRING + "/UserCenterView/PurchasingRules");
                            intent.putExtra("title", "代购规则");
                            startActivity(intent);
                            break;
                        case 7://绑定手机
                            if (mMyNYBeanData.getMobile().equals("null") || mMyNYBeanData.getMobile().equals("")) {
                                startBindPhone(1);
                            } else {
                                startBindPhone(2);
                            }
                            break;
                        case 8://我的积分
                            intent = new Intent(mActivity, MyScoreActivity.class);
                            startActivity(intent);
                            break;
                        case 9://我的收益
                            intent = new Intent(mActivity, MyDaiGouActivity.class);
                            startActivity(intent);
                            break;
                        case 10://授权区域订单
                            intent = new Intent(mActivity, MyDingDanActivity.class);
                            intent.putExtra("state", 0);
                            intent.putExtra("type", 2);
                            intent.putExtra("title", "我的区域授权订单");
                            startActivity(intent);
                            break;
                        case 11://产品查询
                            intent = new Intent(mActivity, TuanGouActivity.class);
                            intent.putExtra("payurl", UrlContact.WEB_URL_STRING + "/UserCenter/ProductQuery.html?loginKey="
                                    + mUserName + "&deviceId=" + MyApplication.sUdid);
                            intent.putExtra("type", 0);
                            intent.putExtra("title", "产品查询");
                            startActivity(intent);
                            break;
                        case 12://支付密码
                            intent = new Intent(mActivity, TuanGouActivity.class);
                            intent.putExtra("payurl", UrlContact.WEB_URL_STRING + "/UserCenter/SetPayPwd.html?loginKey="
                                    + mUserName + "&deviceId=" + MyApplication.sUdid);
                            intent.putExtra("type", 0);
                            intent.putExtra("title", "设置支付密码");
                            startActivity(intent);
                            break;
                        case 13://客服电话
                            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-11-16899"));
                            startActivity(intent);
                            break;
                        case 14://直播
                            LiveHelper.startModule(new Listener() {
                                @Override
                                public void onCallback(int i, Object o) {
                                    startActivity(new Intent(mActivity, LiveListActivity.class));
                                }
                            });
                            break;
                        case 15://聊天室
	                        LiveHelper.startModule(new Listener() {
		                        @Override
		                        public void onCallback(int i, Object o) {
			                        startActivity(new Intent(mActivity, LiveListChatRoomActivity.class));
		                        }
	                        });
                            break;
                    }
                } else {
                    if (type == 13) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-11-16899"));
                        startActivity(intent);
                    } else {
                        startToLogin();
                    }
                }
            }
        });

    }

    //判断是否登录
    private boolean isLogin() {
        mCache = ACache.get(mActivity);
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

    @OnClick({R.id.tv_login, R.id.iv_sign_in, R.id.btn_withdraw_cash, R.id.btn_apply_withdraw_cash, R.id.btn_SignOut,
            R.id.rl_unPayment, R.id.rl_unDelivery, R.id.rl_unReceiving, R.id.rl_allOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login://登录
                startToLogin();
                break;
            case R.id.iv_sign_in://签到
                getSignInMessage();
                break;
            case R.id.btn_withdraw_cash:
                if (isLogin()) {
                    if (mUser.getMobile().equals("null") || mUser.getMobile().equals("")) {
                        startBindPhone(1);
                    } else {
                        showWithdrawDialog();
                    }
                } else {
                    startToLogin();
                }
                break;
            case R.id.btn_apply_withdraw_cash:
                if (mEtApplyWithdrawCash.getText().toString().equals("")) {
                    MyToastUtil.showShortMessage("验证码不能为空！");
                } else {
                    //转入余额或提现
                    toBalance();
                }
                break;
            case R.id.btn_SignOut://退出登录
                setSignOut(0);
                break;
            case R.id.rl_unPayment:
                startToOrder(2, "我的待支付订单");
                break;
            case R.id.rl_unDelivery:
                startToOrder(3, "我的待发货订单");
                break;
            case R.id.rl_unReceiving:
                startToOrder(4, "我的待收货订单");
                break;
            case R.id.rl_allOrder:
                startToOrder(0, "我的订单");
                break;
        }
    }

    //跳转我的订单界面
    private void startToOrder(int state, String title) {
        if (isLogin()) {
            Intent intent = new Intent(mActivity, MyDingDanActivity.class);
            intent.putExtra("state", state);
            intent.putExtra("title", title);
            startActivity(intent);
        } else {
            startToLogin();
        }

    }

    //转入余额或提现
    private void toBalance() {
        timer.cancel();
        mLlApplyWithdrawCash.setVisibility(View.GONE);
        MyProgressDialog.show(mActivity, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        map.put("mobileVCode", mEtApplyWithdrawCash.getText().toString());
        MyOkHttpUtils
                .getData(UrlContact.URL_DO_FINANCE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                        handler.sendEmptyMessage(0);
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        if (response != null) {
                            Gson gson = new Gson();
                            BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                            boolean result = baseBean.isResult();
                            String message = baseBean.getMessage();
                            if (result) {
                                String data = baseBean.getData();
                                mTvMyEarnings.setText(R.string.num_0_00);
                                mEtApplyWithdrawCash.setText("");
                                mBtnWithdrawCash.setEnabled(false);
                                mBtnWithdrawCash.setText(mMyNYBeanData.getBtnText());
                                if (!data.equals("TakeProfit")) {//转余额
                                    mTvMyBalance.setText(DoubleUtils.format2decimals(mMyNYBeanData.getRemainMoney() + mMyNYBeanData.getMoney()) + "元");
                                }

                                MyToastUtil.showShortMessage(message);
                            } else {
                                handler.sendEmptyMessage(0);
                                MyToastUtil.showShortMessage(message);
                            }

                        }
                    }
                });
    }

    //显示收益余额弹窗
    private void showWithdrawDialog() {
        String message;
        switch (mMyNYBeanData.getBtnText()) {
            case "转入余额":
                message = "本次操作将全部代购收益转到帐户余额，即时到帐。帐户余额仅可用于订单支付，不能提现。";
                break;
            case "收益提现":
                message = "收益提现将在申请周期结束后7-10工作日到帐，请您耐心等待。如有疑问请致电客服：400-11-16899。";
                break;
            default:
                message = "";
                break;
        }
        mWithdrawDialog = new MyMsgDialog(mActivity, false, "系统消息", message,
                new MyMsgDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mWithdrawDialog.dismiss();
                        sendWithdrawMessage();
                    }
                },
                new MyMsgDialog.CancelListener() {
                    @Override
                    public void onClick() {
                        mWithdrawDialog.dismiss();
                    }
                });
        mWithdrawDialog.setCancelable(false);
        mWithdrawDialog.show();
    }

    //发送提现请求信息
    private void sendWithdrawMessage() {
        MyProgressDialog.show(mActivity, true, true);
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_SEND_FINANCE, map)
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
                            Gson gson = new Gson();
                            BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                            boolean result = baseBean.isResult();
                            String message = baseBean.getMessage();
                            if (result) {
                                mLlApplyWithdrawCash.setVisibility(View.VISIBLE);
                                mTvApplyWithdrawCash.setText(
                                        "系统已向绑定手机" + mUser.getMobile() + "发送验证码，请先填写收到的验证码。");
                                jishi = 120;
                                mBtnWithdrawCash.setEnabled(false);
                                mBtnWithdrawCash.setBackgroundColor(Color.parseColor("#b5b5b5"));
                                timer = new Timer();
                                timer.schedule(new TimerTask() {

                                    @Override
                                    public void run() {
                                        handler.sendEmptyMessage(jishi--);
                                    }
                                }, 0, 1000);

                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    //去绑定手机界面
    private void startBindPhone(int type) {
        Intent intent = new Intent(mActivity, MyBangdingPhoneActivity.class);
        intent.putExtra("bangding", type);
        intent.putExtra("phone", mMyNYBeanData.getMobile());
        startActivity(intent);
    }

    //去登录界面
    private void startToLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
    }

    //设置退出登录
    private void setSignOut(int type) {
        User user = new User();
        mCache.put("loginString", "false");
        mCache.put("user", user);
        mCache.put("type_login", "1");
        mLlLogin.setVisibility(View.GONE);
        mLlUnLogin.setVisibility(View.VISIBLE);
        mBtnSignOut.setVisibility(View.GONE);
        mTvUnPayment.setText("0");
        mTvUnReceiving.setText("0");
        if (type == 0) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            startActivity(intent);
        }

        if (timer != null) {
            timer.cancel();
            mBtnWithdrawCash.setEnabled(true);
            mBtnWithdrawCash.setText(mMyNYBeanData.getBtnText());
            mBtnWithdrawCash.setBackgroundResource(R.drawable.shape_one);
            mLlApplyWithdrawCash.setVisibility(View.GONE);
        }

        setUnLoginNyItem();
        LiveHelper.setUserId(0);
    }

    //签到返回信息
    private void getSignInMessage() {
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", mUserName);
        map.put("deviceId", MyApplication.sUdid);
        map.put("EnmuTpye", "3");
        MyOkHttpUtils
                .getData(UrlContact.URL_SIGN_IN, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                        if (baseBean.isResult()) {
                            Toast.makeText(mActivity, baseBean.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mActivity, baseBean.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 申请提现倒计时
     */
    private Timer timer;// 计时器
    int jishi = 60;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what <= 0) {
                mEtApplyWithdrawCash.setText("");
                mBtnWithdrawCash.setEnabled(true);
                mBtnWithdrawCash.setText(mMyNYBeanData.getBtnText());
                mBtnWithdrawCash.setBackgroundColor(Color.parseColor("#ff4b00"));
                mBtnWithdrawCash.setBackgroundResource(R.drawable.shape_one);
                timer.cancel();
            } else {
                mBtnWithdrawCash.setText("" + msg.what + "秒后重新获取验证码");
            }
        }
    };
}
