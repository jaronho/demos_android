package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.AppConfig;
import com.gsclub.strategy.app.Constants;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.RechargeContract;
import com.gsclub.strategy.lianlianpay.BaseHelper;
import com.gsclub.strategy.lianlianpay.MobileSecurePayer;
import com.gsclub.strategy.lianlianpay.PayOrder;
import com.gsclub.strategy.model.bean.PayInfoBean;
import com.gsclub.strategy.model.bean.RechargeIndexBean;
import com.gsclub.strategy.presenter.transaction.RechargePresenter;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.SimpleTextWatcher;
import com.gsclub.strategy.util.StringUtils;
import com.gsclub.strategy.util.ToastUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity<RechargePresenter> implements RechargeContract.View {
    @BindView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @BindView(R.id.tv_bank_info)
    TextView tvBankInfo;
    @BindView(R.id.tv_bank_limit)
    TextView tvBankLimit;
    @BindView(R.id.tv_fee_note)
    TextView tvFeeNote;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private int echo_money;
    private String desc;

    public static void start(Context context) {
        context.startActivity(new Intent(context, RechargeActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.recharge);
        etMoney.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    tvSure.setEnabled(false);
                    return;
                }
                if(!tvSure.isEnabled()) {
                    tvSure.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                // 当第一位被删，以0开头，将无效的零去掉
                if(s.toString().startsWith("0")) {
                    int index = StringUtils.getNonZeroIndex(s.toString());
                    if(index != -1) {
                        String money = s.toString().substring(index, s.length());
                        etMoney.setText(money);
                        etMoney.setSelection(money.length());// 若采用自动删零的方式，这里会导致闪退
                        return;
                    }
                    etMoney.setText("");
                    etMoney.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
            }
        });
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getRechargeIndex();
    }

    @OnClick({R.id.tv_sure})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                String moneyStr = etMoney.getText().toString();
                int money = StringUtils.parseInt(moneyStr);
                if(money < echo_money) {
                    if(TextUtils.isEmpty(desc)) {
                        ToastUtil.showMsg(R.string.input_recharge_amount_hint);
                    }else {
                        ToastUtil.showMsg(desc);
                    }
                    return;
                }
                mPresenter.getRechargeOrder(moneyStr);
                break;
        }
    }

    @Override
    public void loadData(RechargeIndexBean data) {
        if(data == null) return;
        ImageLoader.load(App.getInstance(), data.getLogo(), ivBankLogo, R.mipmap.ic_img_def_grey_small);
        tvBankInfo.setText(String.format("%s(尾号%s)", data.getBankName(), data.getBankNo()));
        tvBankLimit.setText(data.getBankLimit());
        desc = data.getDesc();
        etMoney.setHint(desc);

        int color = getResources().getColor(R.color.text_orange_ff8400);
        String colorText = data.getRechargeFeePer() + "%";
        String totalText = "充值手续费率" + colorText;
        StringUtils.setColorFulText(tvFeeNote, totalText, colorText, color);
        echo_money = data.getEchoMoney();
    }

    @Override
    public void recharge(PayInfoBean data) {
        if(data == null) return;
        LianLianInfoPay(data);
    }

    private void LianLianInfoPay(PayInfoBean data){
        PayOrder order;
        order = constructPreCardPayOrder(data.getBusiPartner(), data.getNameGoods(), data.getNotifyUrl(),
                data.getSignType(), data.getValidOrder(), data.getUserId(), data.getCardId(),
                data.getUserName(), data.getMoneyOrder(), data.getBankNo(),
                data.getNoAgree(), data.getOidPartner(), data.getSign(), data.getNoOrder(),
                data.getDtOrder(), data.getRiskItem(), data.getMoney());
        LogUtil.d("连连支付参数：" + order.toString());
        String content4Pay = BaseHelper
                .toJSONString(order);
        MobileSecurePayer msp = new MobileSecurePayer();
        msp.payAuth(content4Pay, mHandler,
                Constants.RQF_PAY, this, AppConfig.LLENVIROMENT);
    }

    private PayOrder constructPreCardPayOrder(String Busi_partner,
                                              String Name_goods, String Notify_url, String Sign_type,
                                              String valid_order

            , String User_id, String Id_no, String Acct_name,
                                              String Money_order, String Card_no, String No_agree,
                                              String Oid_partner, String Sign, String No_order, String Dt_order,
                                              String Risk_item, String money) {
//        recharge_money = money;
        PayOrder order = new PayOrder();
        order.setBusi_partner(Busi_partner);
        order.setNo_order(No_order);
        order.setDt_order(Dt_order);
        order.setName_goods(Name_goods);
        order.setNotify_url(Notify_url);
        // MD5 签名方式
        order.setSign_type(Sign_type);
        // RSA 签名方式
        // order.setSign_type(PayOrder.SIGN_TYPE_RSA);

        order.setValid_order(valid_order);

        order.setUser_id(User_id);
        order.setId_no(Id_no);

        order.setAcct_name(Acct_name);
        order.setMoney_order(Money_order);

        // 银行卡卡号，该卡首次支付时必填
        order.setCard_no(Card_no);
        // 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
        order.setNo_agree(No_agree);
        // 风险控制参数。
        order.setRisk_item(Risk_item);
        // Log.e("风控参数", Risk_item+"哈");

        String sign = Sign;
        order.setOid_partner(Oid_partner);
        String content = BaseHelper.sortParam(order);
        order.setSign(sign);
        LogUtil.d(LogUtil.TAG + ",order", "order=" + content);
        return order;
    }

    private Handler mHandler = createHandler();
    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_PAY: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        // TODO 先判断状态码，状态码为 成功或处理中 的需要 验签
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_SUCCESS
                                    .equalsIgnoreCase(resulPay)) {
                                mPresenter.checkRechargeOrder(objContent.optString("no_order"));
                            } else {
                                ToastUtil.showMsg(retMsg);
                            }

                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING
                                    .equalsIgnoreCase(resulPay)) {
                                // BaseHelper.showDialog(getActivity(), "提示",
                                // objContent.optString("ret_msg") + "交易状态码："
                                // + retCode,
                                // android.R.drawable.ic_dialog_alert);
                                ToastUtil.showMsg(objContent.optString("ret_msg"));
                            }

                        } else {
                            // BaseHelper.showDialog(getActivity(), "提示", retMsg
                            // + "，交易状态码:" + retCode,
                            // android.R.drawable.ic_dialog_alert);
                            LogUtil.d(LogUtil.TAG + "交易状态码:" + retCode + ",交易状态：" + retMsg);
                            ToastUtil.showMsg(retMsg);
                        }
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void doSuccess() {
        RxBus.get().send(RxBus.Code.TRADING_SUCCESS);
        RechargeResultActivity.start(RechargeActivity.this, true, true, null);
        finishUI();
    }
}
