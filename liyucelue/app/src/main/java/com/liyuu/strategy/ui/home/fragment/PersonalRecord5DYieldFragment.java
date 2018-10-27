package com.liyuu.strategy.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.contract.home.PersonalRecord5DYieldContract;
import com.liyuu.strategy.model.bean.RankTradeBean;
import com.liyuu.strategy.presenter.home.PersonalRecord5DYieldPresenter;
import com.liyuu.strategy.ui.home.adapter.PersonalRecord5DYieldAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

public class PersonalRecord5DYieldFragment extends BaseFragment<PersonalRecord5DYieldPresenter> implements PersonalRecord5DYieldContract.View, OnRefreshListener, OnLoadMoreListener {
    private static final String TYPE_INT = "type";
    private static final String RANK_ID_INT = "rank_id";
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout mSmartRefreshLayout;
    PersonalRecord5DYieldAdapter adapter;
    private int type;
    private int rankId;

    public static PersonalRecord5DYieldFragment newInstance(int type, int rankId) {
        PersonalRecord5DYieldFragment fragment = new PersonalRecord5DYieldFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_INT, type);
        bundle.putInt(RANK_ID_INT, rankId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_record_5d_yield;
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

        adapter = new PersonalRecord5DYieldAdapter(getActivity());
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        type = getArguments().getInt(TYPE_INT);
        rankId = getArguments().getInt(RANK_ID_INT);
        getList();
    }

    private void getList() {
        mPresenter.getRecordList(type, rankId, adapter.getPage());
    }

    @Override
    public void showRecordList(List<RankTradeBean> data) {
        adapter.doRefresh(data, layoutNoData, rcvContent, mSmartRefreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getList();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.setPage(1);
        getList();
        refreshLayout.finishRefresh();
    }
}
