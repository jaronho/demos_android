package com.liyuu.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.transaction.RecommendStocksContract;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.presenter.transaction.RecommendStocksPresenter;
import com.liyuu.strategy.ui.transaction.adapter.RecommendStocksAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import butterknife.BindView;

public class RecommendStocksActivity extends BaseActivity<RecommendStocksPresenter> implements RecommendStocksContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout mSmartRefreshLayout;

    private RecommendStocksAdapter adapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, RecommendStocksActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_recommend_stocks;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.recommend_stocks);

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);
        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);
        adapter = new RecommendStocksAdapter(this);
        adapter.setPageSize(50);
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getStockList(adapter);
    }

    @Override
    public void loadData(List<StockBean> data) {
        adapter.doRefresh(data, layoutNoData, rcvContent, mSmartRefreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getStockList(adapter);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.setPage(1);
        mPresenter.getStockList(adapter);
    }

    @Override
    public void doRefreshFinish() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }
}
