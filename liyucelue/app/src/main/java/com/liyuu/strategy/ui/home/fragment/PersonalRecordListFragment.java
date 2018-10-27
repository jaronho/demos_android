package com.liyuu.strategy.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.contract.home.PersonalRecordListContract;
import com.liyuu.strategy.model.bean.RankTradeBean;
import com.liyuu.strategy.presenter.home.PersonalRecordListPresenter;
import com.liyuu.strategy.ui.home.adapter.PersonalRecordListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

public class PersonalRecordListFragment extends BaseFragment<PersonalRecordListPresenter> implements PersonalRecordListContract.View, OnRefreshListener, OnLoadMoreListener {
    private static final String TYPE_INT = "type";
    private static final String RANK_ID_INT = "rank_id";
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout mSmartRefreshLayout;
    PersonalRecordListAdapter adapter;
    private int type;
    private int rankId;

    public static PersonalRecordListFragment newInstance(int type, int rankId) {
        PersonalRecordListFragment fragment = new PersonalRecordListFragment();
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

        adapter = new PersonalRecordListAdapter(getActivity());
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        type = getArguments().getInt(TYPE_INT);
        rankId = getArguments().getInt(RANK_ID_INT);
        getList();
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

    private void getList() {
        mPresenter.getRecordList(type, rankId, adapter.getPage());
    }
}
