package com.gsclub.strategy.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseFragment;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.home.TradingSimulatedPositionListContract;
import com.gsclub.strategy.contract.home.TradingSimulatedSettlementListContract;
import com.gsclub.strategy.model.SimulatedTradingSettlementBean;
import com.gsclub.strategy.model.bean.SimulatedTradingPositionBean;
import com.gsclub.strategy.presenter.home.TradingSimulatedPositionListPresenter;
import com.gsclub.strategy.presenter.home.TradingSimulatedSettlementListPresenter;
import com.gsclub.strategy.ui.home.adapter.TradingSimulatedPositionListAdapter;
import com.gsclub.strategy.ui.home.adapter.TradingSimulatedSettlementListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TradingSimulatedSettlementListFragment
        extends BaseFragment<TradingSimulatedSettlementListPresenter>
        implements TradingSimulatedSettlementListContract.View,
        OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;

    private TradingSimulatedSettlementListAdapter adapter;

    public static TradingSimulatedSettlementListFragment newInstance() {
        TradingSimulatedSettlementListFragment fragment = new TradingSimulatedSettlementListFragment();
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

        adapter = new TradingSimulatedSettlementListAdapter(getActivity());
        adapter.setData(new ArrayList<SimulatedTradingSettlementBean.ListBean>());
        rcvContent.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        mPresenter.getTradingSimulatedSettlementData(true);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
                mPresenter.getTradingSimulatedSettlementData(true);
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingSimulatedSettlementData(false);
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingSimulatedSettlementData(true);
        refreshLayout.finishRefresh(3000);
    }

    @Override
    public void showTradingRealSettlementList(boolean isRefresh, List<SimulatedTradingSettlementBean.ListBean> datas) {
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
}
