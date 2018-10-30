package com.gsclub.strategy.ui.transaction.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseFragment;
import com.gsclub.strategy.contract.transaction.BillListContract;
import com.gsclub.strategy.model.bean.BillListBean;
import com.gsclub.strategy.presenter.transaction.BillListPresenter;
import com.gsclub.strategy.ui.transaction.adapter.BillListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

public class BillListFragment extends BaseFragment<BillListPresenter> implements BillListContract.View, OnRefreshListener, OnLoadMoreListener {
    private static final String FILTER_TYPE = "filter_type";
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout mSmartRefreshLayout;
    BillListAdapter adapter;
    private int type;//1 全部 2 充值取现 3 收入支出

    public static BillListFragment newInstance(int type) {
        BillListFragment fragment = new BillListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FILTER_TYPE, type);
        //fragment保存参数，传入一个Bundle对象
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_record_list;
    }

    @Override
    public void initUI() {
        super.initUI();
//        tvNoData.setText(R.string.no_data);
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);
        rcvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);

        adapter = new BillListAdapter(getActivity());
        adapter.setPageSize(15);
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() == null)
            return;
        type = getArguments().getInt(FILTER_TYPE);
        mPresenter.getList(type, adapter);
    }

    @Override
    public void loadData(List<BillListBean.ListBean> data) {
        adapter.doRefresh(data, layoutNoData, rcvContent, mSmartRefreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getList(type, adapter);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.setPage(1);
        mPresenter.getList(type, adapter);
    }

    @Override
    public void doRefreshFinish() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }
}
