package com.liyuu.strategy.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.contract.home.IncomeListContract;
import com.liyuu.strategy.model.bean.IncomeListBean;
import com.liyuu.strategy.presenter.home.IncomeListPresenter;
import com.liyuu.strategy.ui.home.adapter.IncomeListAdapter;

import butterknife.BindView;

public class IncomeListFragment extends BaseFragment<IncomeListPresenter> implements IncomeListContract.View {
    private static final String INCOME_TYPE = "income_type";
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.view_no_data)
    View viewNoData;
    @BindView(R.id.tv_income_title)
    TextView tvIncomeTitle;
    @BindView(R.id.tv_my_income_name)
    TextView tvMyIncomeName;
    @BindView(R.id.tv_time_interval)
    TextView tvTimeInterval;
    @BindView(R.id.tv_user_rank)
    TextView tvUserRank;
    @BindView(R.id.tv_user_profit)
    TextView tvUserProfit;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    IncomeListAdapter adapter;
    private int incomeType;

    public static Fragment newInstance(int incomeType) {
        IncomeListFragment fragment = new IncomeListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INCOME_TYPE, incomeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income;
    }

    @Override
    public void initUI() {
        super.initUI();
        rcvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);

        adapter = new IncomeListAdapter(getActivity());
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        incomeType = getArguments().getInt(INCOME_TYPE);
        adapter.setIncomeType(incomeType);
        mPresenter.getIncome(incomeType);
        tvIncomeTitle.setText(incomeType == 1?R.string.five_days_profit:R.string.five_days_yield);
        tvMyIncomeName.setText(incomeType == 1?R.string.my_profit:R.string.my_yield);
    }

    @Override
    public void loadIncomeListInfo(IncomeListBean data) {
        //设置顶部个人信息（排行/收益等等）
        if (data != null && data.getMydata() != null) {
            tvTimeInterval.setText(String.format("%s  -  %s", data.getMydata().getStartDayTime(), data.getMydata().getEndDayTime()));
            tvUserRank.setText(String.format("%s名", data.getMydata().getRank()));
            String unit, profit;
            if(incomeType == 1) {
                profit = data.getMydata().getProfit();
                unit = "元";
            }else {
                profit = data.getMydata().getRate();
                unit = "%";
            }
            tvUserProfit.setText(String.format("%s%s", profit, unit));
        }

        if (data == null || data.getList().size() == 0) {
            rcvContent.setVisibility(View.GONE);
            viewNoData.setVisibility(View.VISIBLE);
        } else {
            rcvContent.setVisibility(View.VISIBLE);
            viewNoData.setVisibility(View.GONE);
            adapter.setData(data.getList());
            adapter.notifyDataSetChanged();
        }

        if(data != null) {
            tvTip.setText(data.getTip());
        }
    }
}
