package com.liyuu.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.transaction.SettlementDetailContract;
import com.liyuu.strategy.model.bean.StockSettleDetailBean;
import com.liyuu.strategy.presenter.transaction.SettlementDetailPresenter;
import com.liyuu.strategy.ui.home.adapter.StockUtils;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.StringUtils;

public class SettlementDetailActivity extends BaseActivity<SettlementDetailPresenter>
        implements SettlementDetailContract.View {
    private static final String STOCK_ORDER_ID_STRING = "oid";//stock_order_id_string


    public static void start(Context context, String oid) {
        if (context == null || TextUtils.isEmpty(oid))
            return;
        Intent intent = new Intent(context, SettlementDetailActivity.class);
        intent.putExtra(STOCK_ORDER_ID_STRING, oid);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_settlement_detail;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getStockSettleDetail(getIntent().getStringExtra(STOCK_ORDER_ID_STRING));
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.settlement_detail);
    }

    @Override
    public void showStockSettleDetail(StockSettleDetailBean data) {
        if (data == null)
            return;
        getTextView(R.id.tv_stock_name).setText(data.getSname());
        getTextView(R.id.tv_period).setText(String.format("T+%s", data.getPeriod()));
        getTextView(R.id.tv_stock_code).setText(data.getSymbol());
        getTextView(R.id.tv_order_id).setText(String.format("交易编号：%s", data.getOid()));
        getTextView(R.id.tv_buy_time).setText(data.getBuyTime());
        getTextView(R.id.tv_sale_time).setText(data.getSaleTime());
        getTextView(R.id.tv_hold_period).setText(String.format("%s个交易日", data.getHoldPeriod()));
        getTextView(R.id.tv_stock_buy_price).setText(data.getRealOneStockBuyPrice());
        getTextView(R.id.tv_stock_number).setText(String.format("%s股", data.getStockBuyNumber()));

        int color = getResources().getColor(R.color.text_grey_666666);
        try {
            double cj = StringUtils.parseDouble(data.getRealOneStockSalePrice()) - StringUtils.parseDouble(data.getRealOneStockBuyPrice());
            color = StockUtils.getStockTextColor(this, cj);
        } catch (NumberFormatException e) {
            LogUtil.e(e.getMessage());
        }
        getTextView(R.id.tv_stock_sale_price).setText(data.getRealOneStockSalePrice());
        getTextView(R.id.tv_stock_sale_price).setTextColor(color);
        getTextView(R.id.tv_stock_sale_type).setText(data.getSaleType());

        String symbol = StringUtils.parseDouble(data.getProfitLoss()) >= 0 ? "+" : "";
        color = getResources().getColor(R.color.text_grey_666666);
        try {
            double profitLoss = StringUtils.parseDouble(data.getProfitLoss());
            color = StockUtils.getStockTextColor(this, profitLoss);
        } catch (NumberFormatException e) {
            LogUtil.e(e.getMessage());
        }
        getTextView(R.id.tv_stock_float_precent).setText(
                String.format("(%s%s%s) %s%s", symbol, data.getProfitLossRatio(), "%", symbol, data.getProfitLoss()));
        getTextView(R.id.tv_stock_float_precent).setTextColor(color);

//        getTextView(R.id.tv_deferred_message).setText(String.format("递延%s个交易日", data.getDeferredPeriod()));
        getTextView(R.id.tv_deferred_message).setText(data.getDeferPeriodDesc());
        getTextView(R.id.tv_origin_deposit).setText(data.getDeposit());
        getTextView(R.id.tv_add_deposit).setText(data.getAddDeposit());
        getTextView(R.id.tv_service_fee).setText(data.getOriginCoopFee());
        getTextView(R.id.tv_deferred_service_fee).setText(data.getDeferredCoopFee());
        getTextView(R.id.tv_deduction_deposit).setText(data.getDeductionDeposit());
        getTextView(R.id.tv_return_deposit).setText(String.valueOf(data.getReturnDeposit()));
    }
}
