package com.gsclub.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.contract.mine.MineRecordContract;
import com.gsclub.strategy.model.bean.RankTradeBean;
import com.gsclub.strategy.model.bean.RankUserBean;
import com.gsclub.strategy.presenter.mine.MineRecordPresenter;
import com.gsclub.strategy.ui.home.adapter.PersonalRecordListAdapter;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 我的战绩界面
 */
public class MineRecordActivity extends BaseActivity<MineRecordPresenter> implements MineRecordContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_five_rate)
    TextView tvFiveRate;
    @BindView(R.id.tv_count_rate)
    TextView tvCountRate;
    @BindView(R.id.tv_count_win_rate)
    TextView tvCountWinRate;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    PersonalRecordListAdapter adapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MineRecordActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_mine_record;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.mine_record);
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);
        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);

        adapter = new PersonalRecordListAdapter(this);
        rcvContent.setAdapter(adapter);

        String nickname = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME);
        tvNickname.setText(nickname);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getRecord();
        getHistory();
    }

    private void getHistory() {
        mPresenter.getHistory(adapter.getPage());
    }

    @Override
    public void showRecord(RankUserBean data) {
        if (data == null) return;
        RankUserBean.UserDataBean bean = data.getUser_data();
        if (bean == null) return;
//        tvNickname.setText(bean.getNickname());
        tvFiveRate.setText(String.format("%s%s", bean.getFiveRate(), "%"));
        tvCountRate.setText(String.format("%s%s", bean.getCountRate(), "%"));
        tvCountWinRate.setText(String.format("%s%s", bean.getCountWinRate(), "%"));
        ImageLoader.loadHead(App.getInstance(), bean.getHeadimg(), ivHead);
    }

    @Override
    public void showHistory(List<RankTradeBean> data) {
        adapter.doRefresh(data, layoutNoData, rcvContent, mSmartRefreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getHistory();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.setPage(1);
        getHistory();
        refreshLayout.finishRefresh();
    }
}
