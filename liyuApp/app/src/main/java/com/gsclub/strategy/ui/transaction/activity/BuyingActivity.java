package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.base.OnItemClick;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.BuyingContract;
import com.gsclub.strategy.model.bean.TradeIndexBean;
import com.gsclub.strategy.model.bean.start.BuyingActivityStartBean;
import com.gsclub.strategy.presenter.transaction.BuyingPresenter;
import com.gsclub.strategy.ui.MainActivity;
import com.gsclub.strategy.ui.TransactionFragment;
import com.gsclub.strategy.ui.dialog.BaseDialog;
import com.gsclub.strategy.ui.dialog.BaseDialogImpl;
import com.gsclub.strategy.ui.dialog.ConfirmPayDialog;
import com.gsclub.strategy.ui.dialog.InsufficientBalanceDialog;
import com.gsclub.strategy.ui.dialog.StockMinuteDialog;
import com.gsclub.strategy.ui.home.activity.SimulatedTradingActivity;
import com.gsclub.strategy.ui.transaction.adapter.BuyingAdapter;
import com.gsclub.strategy.util.CalculationUtil;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.ScreenUtil;
import com.gsclub.strategy.util.SimpleTextWatcher;
import com.gsclub.strategy.util.StringUtils;
import com.gsclub.strategy.util.TextViewUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.UserInfoUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 股票购买/下单界面
 */
public class BuyingActivity extends BaseActivity<BuyingPresenter>
        implements BuyingContract.View, OnItemClick<TradeIndexBean.DepositListBean>,
        BaseDialogImpl {

    private static final String SYMBOL_BEAN = "symbol_bean";
    private static final String SYMBOL_QUERY = "prod_name,last_px,px_change,px_change_rate,bid_grp,offer_grp,preclose_px";
    private static final int HANDLER_INPUT_CODE = 1000;

    @BindView(R.id.rcv_buying)
    RecyclerView rcvBuying;
    @BindView(R.id.edt_trade_money)
    EditText edtTradeMoney;
    @BindView(R.id.img_select_t1)
    ImageView imgSelectT1;
    @BindView(R.id.tv_stock_symbol)
    TextView tvStockSymbol;
    @BindView(R.id.tv_stock_name)
    TextView tvStockName;
    @BindView(R.id.tv_stock_price)
    TextView tvStockPrice;
    @BindView(R.id.tv_float_price)
    TextView tvFloatPrice;
    @BindView(R.id.tv_float_precent)
    TextView tvFloatPrecent;
    @BindView(R.id.tv_stock_detail)
    TextView tvStockDetail;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @BindView(R.id.ll_stock_bg)
    LinearLayout llStcokBg;

    private BuyingAdapter buyingAdapter;
    private String symbol;
    private boolean isNotOpenContinue = true;//是否不开启递延
    private double stockPrice;//股票当前价格
    private double tradeMaxStopLossPrecent;//股票交易止损系数
    private boolean isRefreshStockPrice = false;//是否刷新股票价格以及相关联的参数
    private double userTurnoverMoney, userStopPrecent, userStopMoney;//用户成交金额/用户止损率/用户止损价

    private TradeIndexBean tradeIndexBean;

    private ConfirmPayDialog confirmPayDialog;//确认支付弹窗
    private InsufficientBalanceDialog insufficientBalanceDialog;//余额不足弹窗
    private StockMinuteDialog stockMinuteDialog;//股票详情弹窗

    private String stockRealData, stockMinuteData;//股票实时数据，股票分时数据

    private PHandler handler = new PHandler();

    public static void start(Context context, BuyingActivityStartBean bean) {
        Intent intent = new Intent(context, BuyingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SYMBOL_BEAN, bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_buying;
    }

    private void setHeaderStockPlate(String symbol, String stockName, double lastPrice, double pxChange, double pxchangeRate) {
        setHeaderStockPlate(symbol, stockName, String.valueOf(lastPrice), String.valueOf(pxChange), String.valueOf(pxchangeRate));
    }

    private void setHeaderStockPlate(String symbol, String stockName, String lastPrice, String pxChange, String pxchangeRate) {
        double operator = StringUtils.parseDouble(pxChange);
        setHeaderStockPlate(symbol, stockName, lastPrice, pxChange, pxchangeRate, operator);
    }

    private void setHeaderStockPlate(String symbol, String stockName, String lastPrice, String pxChange, String pxchangeRate, double operator) {
        int colorOne = getResources().getColor(
                operator == 0.f ?
                        R.color.text_grey_999999 : R.color.text_white_ffffff);

        String operatorString = "";
        if (operator > 0.f)
            operatorString = "+";

        tvStockSymbol.setText(symbol);
        tvStockSymbol.setTextColor(colorOne);
        tvFloatPrice.setText(String.format("%s%s", operatorString, pxChange));
        tvFloatPrice.setTextColor(colorOne);
        tvFloatPrecent.setText(String.format("%s%s%s", operatorString, pxchangeRate, "%"));
        tvFloatPrecent.setTextColor(colorOne);

        tvStockName.setText(stockName);
        tvStockPrice.setText(lastPrice);
    }

    @Override
    public void initUI() {
        super.initUI();
        int tradeMode = PreferenceUtils.getInt(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT, 1);//股票交易模式 1.正常交易 2.模拟交易
        int titleID = (tradeMode == 1) ? R.string.buying_t_plus_one : R.string.simulate_buying_t_plus_one;
        setTitle(titleID);

        BuyingActivityStartBean startBean = (BuyingActivityStartBean) getIntent().getSerializableExtra(SYMBOL_BEAN);
        symbol = startBean.getSymbol();
        stockPrice = StringUtils.parseDouble(startBean.getLastPrice());

        setHeaderStockPlate(
                symbol,
                startBean.getsName(),
                startBean.getLastPrice(),
                startBean.getPxChange(),
                startBean.getPxChangeRate());

        rcvBuying.setLayoutManager(new GridLayoutManager(this, 3));
        rcvBuying.setNestedScrollingEnabled(false);
        rcvBuying.setFocusable(false);
        rcvBuying.setHasFixedSize(true);
        buyingAdapter = new BuyingAdapter(this);
        buyingAdapter.setData(new ArrayList<TradeIndexBean.DepositListBean>());
        buyingAdapter.setOnItemClick(this);
        rcvBuying.setAdapter(buyingAdapter);

        edtTradeMoney.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                //800毫秒未再次输入文字判定为用户输入结束
                handler.removeMessages(HANDLER_INPUT_CODE);
                handler.sendEmptyMessageDelayed(HANDLER_INPUT_CODE, 800);
            }
        });

    }

    @Override
    protected void initEventAndData() {
        handler.setmBuyingActivity(this);

        //获取订单数据
        mPresenter.getTradeIndex(symbol);

        mPresenter.getMinuteStock(symbol);

        mPresenter.getStockReal(symbol, SYMBOL_QUERY);

        mPresenter.loadContinueModeData(this, isNotOpenContinue);
    }

    @Override
    public void showTradeIndex(TradeIndexBean data) {
        if (data == null) {
            finishUI();
            return;
        }

        tradeIndexBean = data;

        if (data.getRemark() != null && data.getRemark().size() > 0) {
            for (TradeIndexBean.RemarkBean bean : data.getRemark()) {
                TextViewUtil.setRemarkTextView(this, tvDesc,
                        bean.getDesc(), bean.getText(), bean.getUrl(), true);
                tvDesc.append("\n");
            }
        }

        if (data.getDepositList() != null) {
            buyingAdapter.getData().clear();
            buyingAdapter.setData(data.getDepositList());
        }

        if (!TextUtils.isEmpty(data.getLevelType())) {
            stitchingStockTypeTextView(StringUtils.parseDouble(data.getGearing()), data.getLevelType());

            //获取服务器数据后，会将金额第一条选中，将选中的该条金额计算出止损线额度填入view
            tradeMaxStopLossPrecent = StringUtils.parseDouble(data.getMaxStopLine()) / 100;
            double tradeBuyStockMoney = buyingAdapter.getData().get(0).getValue();

            //初始保证金选择完毕，对股票购买数量进行计算
            mPresenter.userSelectMargin(stockPrice, tradeBuyStockMoney,
                    StringUtils.parseDouble(data.getGearing()), tradeMaxStopLossPrecent);
        }

        getTextView(R.id.tv_user_balance).setText(String.format("账户余额：￥%s", tradeIndexBean.getBalanceMoney()));
    }

    @Override
    public void showStockRealData(String originData, String stockName, double stockPrice, double pricedouble, double pricePrecent) {
        this.stockPrice = stockPrice;
        this.stockRealData = originData;
        if (stockMinuteDialog != null)
            stockMinuteDialog.showStockReal(originData, symbol);


        setHeaderStockPlate(
                symbol,
                stockName,
                stockPrice,
                pricedouble,
                pricePrecent
        );

        if (isRefreshStockPrice) {
            isRefreshStockPrice = false;
            mPresenter.changeStockPayNumber(0, stockPrice, getTradeMoney(), tradeMaxStopLossPrecent);
        }

    }

    @Override
    public void onClick(int position, TradeIndexBean.DepositListBean depositListBean) {
        //手动选择，清除输入的金额(反之亦然)
        edtTradeMoney.setText("");
        double tradeBuyStockMoney = depositListBean.getValue();
        String payMoney = CalculationUtil.roundRuturnString(tradeBuyStockMoney, 2);
        setUserPayMoney(String.format("￥%s", payMoney));
        //重新计算最高买入量
        mPresenter.userSelectMargin(stockPrice, tradeBuyStockMoney,
                StringUtils.parseDouble(tradeIndexBean.getGearing()), tradeMaxStopLossPrecent);
    }


    @OnClick({R.id.iv_add, R.id.iv_minus, R.id.img_select_t1, R.id.tv_go_pay, R.id.tv_stock_detail})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                mPresenter.changeStockPayNumber(1, stockPrice, getTradeMoney(), tradeMaxStopLossPrecent);
                break;
            case R.id.iv_minus:
                mPresenter.changeStockPayNumber(-1, stockPrice, getTradeMoney(), tradeMaxStopLossPrecent);
                break;
            case R.id.img_select_t1:
                isNotOpenContinue = !isNotOpenContinue;
                mPresenter.loadContinueModeData(this, isNotOpenContinue);
                break;
            case R.id.tv_go_pay:
                //判断余额是否足够，余额不足先充值
                if (!isEnoughMoney()) {
                    if (insufficientBalanceDialog == null) {
                        insufficientBalanceDialog = InsufficientBalanceDialog.newInstance();
                        insufficientBalanceDialog.setBaseImpl(this);
                    }

                    insufficientBalanceDialog.show(getSupportFragmentManager());

                    return;
                }

                //余额充足，弹出支付弹窗
                if (confirmPayDialog == null) {
                    confirmPayDialog = ConfirmPayDialog.newInstance(getTradeMoney());
                    confirmPayDialog.setBaseImpl(this);
                } else {
                    confirmPayDialog.setPaymentMoneyFloat(getTradeMoney());
                }

                confirmPayDialog.show(getSupportFragmentManager());
                break;
            case R.id.tv_stock_detail:
                if (stockMinuteDialog == null)
                    stockMinuteDialog = new StockMinuteDialog();

                if (!TextUtils.isEmpty(symbol))
                    stockMinuteDialog.setSymbol(symbol);

                if (!TextUtils.isEmpty(stockRealData))
                    stockMinuteDialog.setStockRealData(stockRealData);

                if (!TextUtils.isEmpty(stockMinuteData))
                    stockMinuteDialog.setStockMinuteData(stockMinuteData);

                stockMinuteDialog.show(getSupportFragmentManager());
                break;
        }
    }

    /**
     * 余额是否够支付押金
     */
    private boolean isEnoughMoney() {
        if (tradeIndexBean == null)
            return false;
        int tradeMoney = getTradeMoney();

        return StringUtils.parseDouble(tradeIndexBean.getBalanceMoney()) - tradeMoney >= 0;
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                isRefreshStockPrice = true;
                mPresenter.getStockReal(symbol, SYMBOL_QUERY);
                break;
            case RxBus.Code.MINUTE_HEART_BREAK:
                mPresenter.getMinuteStock(symbol);
                break;
        }
    }

    /**
     * 手动输入保证金处理
     */
    private void inputTradeMoney() {
        handler.removeMessages(HANDLER_INPUT_CODE);

        //该监听器回调金额输入框(输入为整数，最高为50000，为1000倍数)
        String text = edtTradeMoney.getText().toString();
        if (TextUtils.isEmpty(text))
            return;
        int oldMoney = Integer.parseInt(text);
        int money = (int) (Math.ceil(oldMoney / 1000.f) * 1000);
        if (money > 50000)
            money = 50000;

        String payMoney = CalculationUtil.roundRuturnString(money, 2);
        setUserPayMoney(String.format("￥%s", payMoney));
        buyingAdapter.selectItem(-1);
        buyingAdapter.notifyDataSetChanged();

        //两个参数相同，即当前为用户输入完毕并当前值为修正值，不再settext，防止死循环
        if (oldMoney == money)
            return;
        edtTradeMoney.setText(String.valueOf(money));
        edtTradeMoney.setSelection(edtTradeMoney.getText().toString().length());

        if (confirmPayDialog != null)
            confirmPayDialog.setPaymentMoneyFloat(money);

        mPresenter.userSelectMargin(stockPrice, money,
                StringUtils.parseDouble(tradeIndexBean.getGearing()), tradeMaxStopLossPrecent);
    }

    @Override
    public void setStockNumberTextView(double stockPrice) {
        //以市价10.1元买入，以实际成交数量为准
        String moneyText = String.format("%s元", stockPrice);
        String stockNumberMessage = String.format("以市价%s买入，以实际成交数量为准", moneyText);
        int color = getResources().getColor(R.color.stock_red_color);
        SpannableStringBuilder ssb = new SpannableStringBuilder(stockNumberMessage);
        settextColor(ssb, color, moneyText, stockNumberMessage);
        getTextView(R.id.tv_stock_number_message).setText(ssb);
    }

    @Override
    public void setStockNumber(int stockNumber) {
        getTextView(R.id.tv_stock_number).setText(String.valueOf(stockNumber));
    }

    @Override
    public void setUserTurnover(double userTurnoverMoney) {
        this.userTurnoverMoney = userTurnoverMoney;
        String turnover = CalculationUtil.roundRuturnString(userTurnoverMoney, 2);
        getTextView(R.id.tv_user_turnover).setText(String.format("%s元", turnover));
    }


    private void stitchingStockTypeTextView(double gearing, String levelType) {
        String redText = String.format("%s倍", gearing);
        String text = String.format("当前所选为%s，可获%s杠杆", levelType, redText);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        int start = text.indexOf(redText);
        int color = getResources().getColor(R.color.stock_red_color);
        ssb.setSpan(new ForegroundColorSpan(color), start, start + redText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getTextView(R.id.tv_stock_type_message).setText(ssb);
    }

    @Override
    public void setStopLossTextView(double tradeStopLossMoney, double stopPrecent) {
        //tv_stop_loss_line_message  亏损-￥680(跌至-6.8%发起平仓)
        this.userStopMoney = tradeStopLossMoney;
        this.userStopPrecent = stopPrecent;
        int money = (int) tradeStopLossMoney;
        String moneyText = String.format("-￥%s", money);
        String precentText = String.format("-%s%s", CalculationUtil.roundRuturnString(stopPrecent * 100.f, 2), "%");
        String text = String.format("亏损%s(跌至%s发起平仓)", moneyText, precentText);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        int color = getResources().getColor(R.color.stock_red_color);

        settextColor(ssb, color, moneyText, text);
        settextColor(ssb, color, precentText, text);
        getTextView(R.id.tv_stop_loss_line_message).setText(ssb);
    }

    @Override
    public void setStockStopLossPriceTextView(double stopLossLossPrice) {
        //预计止损价9.20元
        getTextView(R.id.tv_stock_stop_loss_price_message)
                .setText(String.format("预计止损价%s元", CalculationUtil.roundRuturnString(stopLossLossPrice, 2)));
    }

    @Override
    public void setUserPayMoney(String text) {
        getTextView(R.id.tv_user_pay_money).setText(text);
    }

    @Override
    public void startToMainActivity() {
        if (confirmPayDialog != null)
            confirmPayDialog.dismiss();

        RxBus.get().send(RxBus.Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT, TransactionFragment.select_commission);

        Context context = App.getInstance();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.GO_TO_TRANSACTION, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void startToSimulatedTradingActivity() {
        ToastUtil.showMsg(getResources().getString(R.string.buy_success));
        if (confirmPayDialog != null)
            confirmPayDialog.dismiss();

        Context context = App.getInstance();
        Intent intent = new Intent(context, SimulatedTradingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void showMinute(String data) {
        this.stockMinuteData = data;
        if (stockMinuteDialog != null)
            stockMinuteDialog.showMinute(data, symbol);
    }

    @Override
    public void showContinueMode(boolean isShowSwitch, int switchResouce, String switchMessage, String continueMessage) {
        getTextView(R.id.tv_switch_message).setText(switchMessage);
        getTextView(R.id.tv_stock_continue_message).setText(continueMessage);
        if (isShowSwitch) {
            imgSelectT1.setVisibility(View.VISIBLE);
            imgSelectT1.setImageResource(switchResouce);
        } else {
            imgSelectT1.setVisibility(View.GONE);
        }
    }


    private void settextColor(SpannableStringBuilder ssb, int color, String startText, String allText) {
        int start = allText.indexOf(startText);
        ssb.setSpan(new ForegroundColorSpan(color), start, start + startText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 获取用户缴纳的保证金
     */
    private int getTradeMoney() {
        if (TextUtils.isEmpty(edtTradeMoney.getText().toString())) {
            for (TradeIndexBean.DepositListBean bean : buyingAdapter.getData()) {
                if (bean.isSelect())
                    return bean.getValue();
            }
        } else {
            return Integer.parseInt(edtTradeMoney.getText().toString());
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLER_INPUT_CODE);
        handler = null;
    }

    @Override
    public void dialogCancle(String dialogName, BaseDialog dialog) {

    }

    @Override
    public void dialogSure(String dialogName, BaseDialog dialog) {
        if (ConfirmPayDialog.class.getSimpleName().equals(dialogName)) {
            goToPay();
        } else if (InsufficientBalanceDialog.class.getSimpleName().equals(dialogName)) {
            if (!UserInfoUtil.hasBindCard()) {
                BindCardActivity.start(this);
                return;
            }
            RechargeActivity.start(this);
        }
    }

    private void goToPay() {
        //购买
        int status = PreferenceUtils.getInt(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT);
        if (status == 1) {
            mPresenter.createOrder(
                    symbol,
                    tvStockName.getText().toString(),
                    getTradeMoney(),
                    isNotOpenContinue ? 2 : 1,
                    Integer.parseInt(getTextView(R.id.tv_stock_number).getText().toString()));
        } else if (status == 2) {
            mPresenter.createSimulatedOrder(
                    isNotOpenContinue ? 2 : 1,
                    userTurnoverMoney,
                    getTradeMoney(),
                    Integer.parseInt(getTextView(R.id.tv_stock_number).getText().toString()),
                    userStopPrecent, userStopMoney, symbol, tvStockName.getText().toString()
            );
        }
    }

    private static class PHandler extends Handler {

        private WeakReference<BuyingActivity> mBuyingActivity;

        void setmBuyingActivity(BuyingActivity mBuyingActivity) {
            this.mBuyingActivity = new WeakReference<>(mBuyingActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BuyingActivity activity = mBuyingActivity.get();
            switch (msg.what) {
                case HANDLER_INPUT_CODE:
                    activity.inputTradeMoney();
                    break;
            }
        }
    }
}
