package com.liyuu.strategy.ui.transaction.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.transaction.NewsWelfareContract;
import com.liyuu.strategy.model.bean.NewIndexBean;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.presenter.transaction.NewsWelfarePresenter;
import com.liyuu.strategy.ui.dialog.BaseDialog;
import com.liyuu.strategy.ui.dialog.BaseDialogImpl;
import com.liyuu.strategy.ui.dialog.NormalDialog;
import com.liyuu.strategy.ui.login.RegisterActivity;
import com.liyuu.strategy.util.StringUtils;
import com.liyuu.strategy.util.UserInfoUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsWelfareActivity extends BaseActivity<NewsWelfarePresenter>
        implements NewsWelfareContract.View, BaseDialogImpl {
    private final static int REQUEST_CODE = 1000;
    @BindView(R.id.tv_stock_name)
    TextView tvStockName;
    @BindView(R.id.tv_stock_symbol)
    TextView tvStockSymbol;
    @BindView(R.id.tv_stock_price)
    TextView tvStockPrice;
    @BindView(R.id.tv_float_price)
    TextView tvFloatPrice;
    @BindView(R.id.tv_float_percent)
    TextView tvFloatPercent;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_stock_number)
    TextView tvStockNumber;
    @BindView(R.id.tv_stock_number_message)
    TextView tvStockNumberMessage;
    @BindView(R.id.tv_user_turnover)
    TextView tvUserTurnover;
    @BindView(R.id.tv_stock_type_message)
    TextView tvStockTypeMessage;
    @BindView(R.id.tv_stock_stop_loss_price_message)
    TextView tvStockStopLossPriceMessage;
    @BindView(R.id.tv_stop_loss_line_message)
    TextView tvStopLossLineMessage;
    @BindView(R.id.tv_user_pay_money)
    TextView tvUserPayMoney;
    @BindView(R.id.ll_stock_bg)
    View layoutStock;
    private String stockSymbol;
    private String stockPrice;// 股票当前价格
    private double tradeMaxStopLossPercent;//股票交易止损系数
    private DecimalFormat df = new DecimalFormat("0.00");
    private NewIndexBean indexBean;

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewsWelfareActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welfare;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.get_welfare);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getStockSingle();
        mPresenter.checkStatus();
    }

    @OnClick({R.id.tv_go_pay, R.id.tv_stock_change, R.id.iv_add, R.id.iv_minus})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_pay:
                if (!UserInfoUtil.isLogin()) {
                    RegisterActivity.start(this, true);
                    return;
                }
                createOrder();
                break;
            case R.id.tv_stock_change:
                startActivityForResult(new Intent(this, RecommendStocksActivity.class), REQUEST_CODE);
                break;
            case R.id.iv_add:
                if (indexBean == null) {
                    finishUI();
                    return;
                }
                mPresenter.changeStockPayNumber(1, StringUtils.parseDouble(stockPrice), StringUtils.parseDouble(indexBean.getBbin()), tradeMaxStopLossPercent);
                break;
            case R.id.iv_minus:
                if (indexBean == null) {
                    finishUI();
                    return;
                }
                mPresenter.changeStockPayNumber(-1, StringUtils.parseDouble(stockPrice), StringUtils.parseDouble(indexBean.getBbin()), tradeMaxStopLossPercent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) return;
                StockBean bean = (StockBean) data.getSerializableExtra("stockInfo");
                loadData(bean);
            }
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_FROM_NEWS_WELFARE:
                finishUI();
                break;
            case RxBus.Code.NEWS_WELFARE_BUYING:
                createOrder();
                break;
            case RxBus.Code.HEART_BREAK:
                if (TextUtils.isEmpty(stockSymbol)) {
                    finishUI();
                    return;
                }
                mPresenter.getStockReal(stockSymbol);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadData(StockBean data) {
        setStockInfo(data);
        if (TextUtils.isEmpty(stockSymbol)) return;
        mPresenter.getNewIndex(stockSymbol);
    }

    private void setStockInfo(StockBean data) {
        if (data == null) return;
        tvStockName.setText(data.getProdName());
        stockSymbol = data.getSymbol();
        tvStockSymbol.setText(stockSymbol);
        stockPrice = data.getLastPx();
        setRealStockInfo(stockPrice, data.getPxChange(), data.getPxChangeRate());
    }

    private void setRealStockInfo(String stockPrice, String price, String pricePercent) {
        tvStockPrice.setText(stockPrice);
        tvFloatPrice.setText(String.format("%s%s", StringUtils.parseDouble(price) >= 0 ? "+" : "", price));
        tvFloatPercent.setText(String.format("%s%s%s", StringUtils.parseDouble(pricePercent) >= 0 ? "+" : "", pricePercent, "%"));
        int color = getResources().getColor(StringUtils.parseDouble(price) >= 0 ? R.color.stock_red_color : R.color.bg_green_00ba6a);
        layoutStock.setBackgroundColor(color);
    }

    @Override
    public void showIndex(NewIndexBean data) {
        if (data == null) return;
        indexBean = data;
        tvDeposit.setText(String.format("%s元", data.getBbin()));
        calculateAndSetStock(data);
        if (!TextUtils.isEmpty(data.getLevelType())) {
            stitchingStockTypeTextView(StringUtils.parseDouble(data.getGearing()), data.getLevelType());
        }
        tvUserPayMoney.setText(String.format("￥%s", data.getDeposit()));
        getTextView(R.id.tv_hold_day).setText(String.format("%s个交易日", data.getHold_day()));
        getTextView(R.id.tv_desc).setText(data.getDesc());
    }

    @Override
    public void showStockRealData(String stockPrice, String price, String pricePercent) {
        setRealStockInfo(stockPrice, price, pricePercent);
        if (indexBean == null) return;
        mPresenter.changeStockPayNumber(0, StringUtils.parseDouble(stockPrice), StringUtils.parseDouble(indexBean.getBbin()), tradeMaxStopLossPercent);
    }

    private void calculateAndSetStock(NewIndexBean data) {
        if (data == null) return;
        double stockPrice_d = StringUtils.parseDouble(stockPrice);
        double tradeBuyStockMoney = StringUtils.parseDouble(data.getBbin());
        double gearing = StringUtils.parseDouble(data.getGearing());
        tradeMaxStopLossPercent = StringUtils.parseDouble(data.getMaxStopRatio()) / 100;
        mPresenter.userSelectMargin(stockPrice_d, tradeBuyStockMoney, gearing, tradeMaxStopLossPercent);
    }

    @Override
    public void setStockNumberTextView(double stockPrice) {
        //以市价10.1元买入，以实际成交数量为准
        String moneyText = String.format("%s元", stockPrice);
        String stockNumberMessage = String.format("以市价%s买入，以实际成交数量为准", moneyText);
        int color = getResources().getColor(R.color.stock_red_color);
        StringUtils.setColorFulText(tvStockNumberMessage, stockNumberMessage, moneyText, color);
    }

    /**
     * 股票数量
     */
    @Override
    public void setStockNumber(int stockNumber) {
        tvStockNumber.setText(String.valueOf(stockNumber));
    }

    /**
     * 成交金额
     */
    @Override
    public void setUserTurnover(double userTurnoverMoney) {
        getTextView(R.id.tv_user_turnover).setText(df.format(userTurnoverMoney));
    }


    private void stitchingStockTypeTextView(double gearing, String levelType) {
        String redText = String.format("%s倍", gearing);
        String text = String.format("当前所选为%s，可获%s杠杆", levelType, redText);
        int color = getResources().getColor(R.color.stock_red_color);
        StringUtils.setColorFulText(tvStockTypeMessage, text, redText, color);
    }

    @Override
    public void setStopLossTextView(double tradeStopLossMoney, double stopPercent) {
        //tv_stop_loss_line_message  亏损-￥680(跌至-6.8%发起平仓)
        int money = (int) tradeStopLossMoney;
        String moneyText = String.format("-￥%s", money);
        String percentText = String.format("-%s%s", df.format(stopPercent * 100.f), "%");
        String text = String.format("亏损%s(跌至%s发起平仓)", moneyText, percentText);
        int color = getResources().getColor(R.color.stock_red_color);
        StringUtils.setColorFulTexts(tvStopLossLineMessage, color, text, moneyText, percentText);
    }

    @Override
    public void setStockStopLossPriceTextView(double stopLossLossPrice) {
        //预计止损价9.20元
        tvStockStopLossPriceMessage.setText(String.format("预计止损价%s元", df.format(stopLossLossPrice)));
    }

    @Override
    public void showCloseDialog() {
        NormalDialog dialog = NormalDialog.newInstance("新手福利", "您已经使用过了", false);
//        dialog.setCancelable(false);//false：dialog弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setBaseImpl(this);
        dialog.setCloseEquivalent(true);
        dialog.show(getSupportFragmentManager());
    }

    private void createOrder() {
        if (indexBean == null) {
            finishUI();
            return;
        }
        mPresenter.createOrder(
                stockSymbol,
                tvStockName.getText().toString(),
                StringUtils.parseDouble(indexBean.getDeposit()),
                1,
                StringUtils.parseInt(tvStockNumber.getText().toString()));
    }

    @Override
    public void dialogCancle(String dialogName, BaseDialog dialog) {
        if (dialogName.equals(NormalDialog.class.getSimpleName())) {
            finishUI();
        }
    }

    @Override
    public void dialogSure(String dialogName, BaseDialog dialog) {
        if (dialogName.equals(NormalDialog.class.getSimpleName())) {
            finishUI();
        }
    }
}
