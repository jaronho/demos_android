package com.gsclub.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.mine.MessageContract;
import com.gsclub.strategy.model.bean.MessageIndexBean;
import com.gsclub.strategy.presenter.mine.MessagePresenter;
import com.gsclub.strategy.ui.mine.adapter.MessageAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;
import butterknife.BindView;

/**
 * 消息列表界面（总消息列表）：公告消息/系统消息
 */
public class MessageActivity extends BaseActivity<MessagePresenter>
        implements MessageContract.View, OnRefreshListener{
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;

    private MessageAdapter adapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getString(R.string.message));
        setTitleColor(R.color.text_black_333333);
        refreshLayout.setOnRefreshListener(this);
        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setFocusable(false);
        adapter = new MessageAdapter(this);
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getMessageIndex();
    }

    @Override
    public void loadMessageTypes(List<MessageIndexBean> data) {
        refreshLayout.finishRefresh();
        adapter.doRefresh(data, layoutNoData, rcvContent, refreshLayout);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getMessageIndex();
    }

}
