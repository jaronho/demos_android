package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.PositionDetailContract;
import com.gsclub.strategy.model.bean.PositionDetailBean;
import com.gsclub.strategy.presenter.transaction.PositionDetailPresenter;
import com.gsclub.strategy.ui.dialog.EditStopLossDialog;
import com.gsclub.strategy.util.CalculationUtil;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PositionDetailActivity extends BaseActivity<PositionDetailPresenter>
        implements PositionDetailContract.View, EditStopLossDialog.OnChangeStockStopLossPrice {
    private static final String ORDER_OID_STR = "oid";
    private static final String USER_OPEN_ID_STR = "open_id";
    @BindView(R.id.tv_edit_stop_loss)
    TextView tvEditStopLoss;
    @BindView(R.id.layout_period)
    View layoutPeriod;
    @BindView(R.id.line_period)
    View linePeriod;
    private String oid = "";

    private PositionDetailBean positionDetailBean;
    private EditStopLossDialog editStopLossDialog;//修改止损价弹窗

    public static void start(Context context, String oid) {
        Intent intent = new Intent(context, PositionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_OID_STR, oid);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_position_detail;
    }

    @Override
    protected void initEventAndData() {
        if (getIntent().getExtras() == null) {
            finishUI();
            return;
        }

        String openid = getIntent().getExtras().getString(USER_OPEN_ID_STR);
        String localUid = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID);
        if (!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(localUid) && !openid.equals(localUid)) {
            finishUI();
            return;
        }

        oid = getIntent().getExtras().getString(ORDER_OID_STR);
        mPresenter.getPositionDetail(oid);
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.position_detail);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.TRADING_SUCCESS:
                //用户余额不足充值成功时需重新刷新数据
                mPresenter.getPositionDetail(oid);
                break;
        }
    }

    @Override
    public void showPositionDetail(PositionDetailBean data) {
        this.positionDetailBean = data;
        getTextView(R.id.tv_stock_name).setText(data.getSname());
        getTextView(R.id.tv_stock_period).setText(String.format("T+%s", data.getPeriod()));
        getTextView(R.id.tv_stock_symbol).setText(data.getSymbol());
        getTextView(R.id.tv_deposit).setText(data.getTotalDeposit());
        getTextView(R.id.tv_stock_number).setText(data.getRealTradeNum());
        getTextView(R.id.tv_market_price).setText(data.getMarketPrice());
        getTextView(R.id.tv_buy_time).setText(data.getBuyTime());
        getTextView(R.id.tv_buy_price).setText(data.getRealBuyPrice());
        getTextView(R.id.tv_now_price).setText(data.getCurrentPrice());

        int color = getResources().getColor(StringUtils.parseDouble(data.getProfitLoss()) >= 0.f ? R.color.stock_red_color : R.color.stock_green_color);
        String numberSymbol = StringUtils.parseDouble(data.getProfitLoss()) >= 0.f ? "+" : "";
        getTextView(R.id.tv_price_float).setTextColor(color);
        getTextView(R.id.tv_price_float).setText(String.format("(%s%s%s) %s%s", numberSymbol,
                data.getProfitLossPer(), "%", numberSymbol, data.getProfitLoss()));

        getTextView(R.id.tv_stop_loss_price).setText(String.valueOf(data.getLastLossPrice()));
//        float precent = (float) ((data.getTotalDeposit() * data.getMaxStopRatio()) / (data.getRealBuyPrice() * data.getRealTradeNum()));
        double precent = ((StringUtils.parseDouble(data.getRealBuyPrice()) - StringUtils.parseDouble(data.getLastLossPrice()))
                * 100.f / (StringUtils.parseDouble(data.getRealBuyPrice())));
        getTextView(R.id.tv_stop_loss_message).setText(String.format("亏损 -%s (跌至 -%s%s 发起平仓)",
                CalculationUtil.roundRuturnString(
                        StringUtils.parseDouble(data.getTotalDeposit()) *
                                StringUtils.parseDouble(data.getMaxStopRatio()) / 100, 2),
                CalculationUtil.roundRuturnString(precent, 2), "%"));

        if ("1".equals(data.getIsActivity())) {
            //活动购买的股票无法修改止损价（不允许）
            tvEditStopLoss.setVisibility(View.GONE);
            layoutPeriod.setVisibility(View.GONE);
            linePeriod.setVisibility(View.GONE);
        } else {
            tvEditStopLoss.setVisibility("1".equals(data.getIsCanAddDeposit()) ? View.VISIBLE : View.GONE);
            layoutPeriod.setVisibility(View.VISIBLE);
            linePeriod.setVisibility(View.VISIBLE);
        }

        getTextView(R.id.tv_period).setText(String.format("T+%s", data.getPeriod()));
        getTextView(R.id.tv_period_day).setText(String.format("已持有%s个交易日", data.getTotalPeriod()));

        boolean isOpenContinue = "1".equals(data.getPeriodAutoStatus());
        getTextView(R.id.tv_period_auto_status).setText(isOpenContinue ? "递延开启" : "递延关闭");
        ImageView img = ButterKnife.findById(this, R.id.img_period_auto_status);
        img.setBackgroundResource(isOpenContinue ? R.mipmap.icon_deffer_on : R.mipmap.icon_deffer_off);
        if (!isOpenContinue) {
            getTextView(R.id.tv_end_time).setText(data.getUnDeferDesc());
        } else {
            getTextView(R.id.tv_end_time).setText(data.getEnDeferDesc());
        }

    }

    @Override
    public void changeStockStopLossPriceSuccess(String result) {
        if (editStopLossDialog != null)
            editStopLossDialog.dismiss();
        mPresenter.getPositionDetail(oid);
    }

    @Override
    public void changeStockAutoDeferredSuccess(String result) {
        mPresenter.getPositionDetail(oid);
    }

    @OnClick({R.id.tv_edit_stop_loss, R.id.img_period_auto_status})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_stop_loss:
                if (editStopLossDialog == null) {
                    editStopLossDialog = EditStopLossDialog.newInstance(positionDetailBean);
                    editStopLossDialog.setOnChangeStockStopLossPrice(this);
                } else {
                    editStopLossDialog.setBean(positionDetailBean);
                }
                editStopLossDialog.show(getSupportFragmentManager());
                break;
            case R.id.img_period_auto_status:
                if (positionDetailBean == null)
                    break;
                //点击切换递延状态
                mPresenter.changeStockAutoDeferred(oid, !"1".equals(positionDetailBean.getPeriodAutoStatus()));
                break;
        }
    }

    @Override
    public void changeStockStopLossPrice(double changeStopLossPrice) {
        mPresenter.changeStockStopLossPrice(oid, changeStopLossPrice);
    }
}
