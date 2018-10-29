package com.liyuu.strategy.ui.transaction.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.transaction.TradingRealCommissionListContract;
import com.liyuu.strategy.model.bean.RealTradingCommissionBean;
import com.liyuu.strategy.presenter.transaction.TradingRealCommissionListPresenter;
import com.liyuu.strategy.ui.TransactionFragment;
import com.liyuu.strategy.ui.transaction.adapter.TradingRealCommissionListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TradingRealCommissionListFragment
        extends BaseFragment<TradingRealCommissionListPresenter>
        implements TradingRealCommissionListContract.View,
        OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;

    private TradingRealCommissionListAdapter adapter;

    public static TradingRealCommissionListFragment newInstance() {
        TradingRealCommissionListFragment fragment = new TradingRealCommissionListFragment();
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

        adapter = new TradingRealCommissionListAdapter(getActivity());
        adapter.setData(new ArrayList<RealTradingCommissionBean.ListBean>());
        rcvContent.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        mPresenter.getTradingRealCommissionData(true);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
            case RxBus.Code.NEWS_WELFARE_BUYING_SUCCESS:
                mPresenter.getTradingRealCommissionData(true);
                break;
            case RxBus.Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT:
                if (object == null)
                    break;
                int selectPosition = (int) object;
                if (selectPosition == TransactionFragment.select_commission)
                    mPresenter.getTradingRealCommissionData(true);
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingRealCommissionData(false);
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTradingRealCommissionData(true);
        refreshLayout.finishRefresh(3000);
    }

    @Override
    public void showTradingRealCommissionList(boolean isRefresh, List<RealTradingCommissionBean.ListBean> datas) {
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
