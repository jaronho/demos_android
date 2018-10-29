package com.liyuu.strategy.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.home.TradingSimulatedPositionListContract;
import com.liyuu.strategy.model.bean.SimulatedTradingPositionBean;
import com.liyuu.strategy.presenter.home.TradingSimulatedPositionListPresenter;
import com.liyuu.strategy.ui.dialog.BaseDialog;
import com.liyuu.strategy.ui.dialog.BaseDialogImpl;
import com.liyuu.strategy.ui.dialog.SellStockDialog;
import com.liyuu.strategy.ui.home.adapter.TradingSimulatedPositionListAdapter;
import com.liyuu.strategy.util.CalculationUtil;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.StringUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TradingSimulatedPositionListFragment
        extends BaseFragment<TradingSimulatedPositionListPresenter>
        implements TradingSimulatedPositionListContract.View,
        TradingSimulatedPositionListAdapter.OnSellOrder,
        OnRefreshListener, OnLoadMoreListener,
        BaseDialogImpl, SellStockDialog.SellStockDialogImpl {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;

    private TradingSimulatedPositionListAdapter adapter;
    private SellStockDialog sellStockDialog;

    public static TradingSimulatedPositionListFragment newInstance() {
        TradingSimulatedPositionListFragment fragment = new TradingSimulatedPositionListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_recycler_view_with_empty_page;
    }

    @Override
    public void initUI() {
        super.initUI();
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initEventAndData() {
        rcvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);

        adapter = new TradingSimulatedPositionListAdapter(getActivity());
        adapter.setData(new ArrayList<SimulatedTradingPositionBean.ListBean>());
        adapter.setOnSellOrder(this);
        rcvContent.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        mPresenter.getTradingSimulatedPositionData(true);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
                mPresenter.getTradingSimulatedPositionData(true);
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingSimulatedPositionData(false);
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingSimulatedPositionData(true);
        refreshLayout.finishRefresh(3000);
    }

    @Override
    public void showTradingSimulatedPositionList(boolean isRefresh, List<SimulatedTradingPositionBean.ListBean> datas) {
        rcvContent.setVisibility(View.VISIBLE);
        layoutNoData.setVisibility(View.GONE);

        if (isRefresh) {
            adapter.setData(datas);
        } else {
            adapter.appendData(datas);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyPage() {
        if (adapter != null) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        rcvContent.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void enableLoadMore(boolean isEnableLoadMore) {
        refreshLayout.setEnableLoadMore(isEnableLoadMore);
    }

    @Override
    public void stopRefreshLayoutAnim() {
        if (refreshLayout == null)
            return;
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void saleSuccess() {
        ToastUtil.showMsg(getResources().getString(R.string.sale_success));
        if (sellStockDialog != null) {
            sellStockDialog.dismiss();
        }
        mPresenter.getTradingSimulatedPositionData(true);
    }

    @Override
    public void refreshStockLastPrice(String symbol, double lastPrice) {
        if (sellStockDialog != null && sellStockDialog.getSymbol().equals(symbol)) {
            sellStockDialog.setStockLastPrice(lastPrice).showView();
        }
//
//        if (adapter != null && adapter.getData().size() > 0) {
//            for (int i = adapter.getData().size() - 1; i > 0; i--) {
//                if (symbol.equals(adapter.getData().get(i).getSymbol())) {
//                    String lastPriceString = CalculationUtil.roundRuturnString(lastPrice, 2);
//                    adapter.getData().get(i).setCurrentPrice(lastPriceString);
//                    adapter.notifyItemChanged(i);
//                }
//            }
//        }
    }

    @Override
    public void sellOrder(SimulatedTradingPositionBean.ListBean item) {
        double userBuyPrice = 0, lastPrice = 0;
        try {
            userBuyPrice = StringUtils.parseDouble(item.getRealBuyPrice());
            lastPrice = StringUtils.parseDouble(item.getCurrentPrice());
        } catch (NumberFormatException e) {
            LogUtil.e(e.getMessage());
        }

        if (sellStockDialog == null) {
            sellStockDialog = SellStockDialog.newInstance(item.getOid(), item.getSymbol(), item.getRealTradeNum(), userBuyPrice, lastPrice);
            sellStockDialog.setBaseImpl(this);
        } else {
            sellStockDialog
                    .setOid(item.getOid())
                    .setSymbol(item.getSymbol())
                    .setStockLastPrice(lastPrice)
                    .setUserBuyPrice(userBuyPrice)
                    .setStockNumber(item.getRealTradeNum());
        }

        sellStockDialog.setDialogImpl(this);
        sellStockDialog.show(getFragmentManager());
    }

    @Override
    public void dialogCancle(String dialogName, BaseDialog dialog) {

    }

    @Override
    public void dialogSure(String dialogName, BaseDialog dialog) {
        if (dialogName.equals(SellStockDialog.class.getSimpleName()) && sellStockDialog != null) {
            mPresenter.sellOrder(sellStockDialog.getOid(), sellStockDialog.getSymbol());
        }
    }

    @Override
    public void sellStockDialogRefreshStockLastPrice(String symbol) {
        mPresenter.refreshStockLastPrice(symbol);
    }
}
