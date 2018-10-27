package com.liyuu.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.mine.MessageSonContract;
import com.liyuu.strategy.model.bean.MessageSonBean;
import com.liyuu.strategy.presenter.mine.MessageSonPresenter;
import com.liyuu.strategy.ui.mine.adapter.MessageSonAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 消息列表子列表详情界面
 */
public class MessageSonActivity extends BaseActivity<MessageSonPresenter> implements MessageSonContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;

    private MessageSonAdapter adapter;

    private static final String MESSAGE_TYPE_INT = "message_type_int";
    private static final String TYPE_NAME = "type_name";

    private int messageType;//消息类型（由上层传入）
    private boolean isFirst = true;

    public static void start(Context context, int messageType, String type_name) {
        Intent intent = new Intent(context, MessageSonActivity.class);
        intent.putExtra(MESSAGE_TYPE_INT, messageType);
        intent.putExtra(TYPE_NAME, type_name);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_message_son;
    }

    @Override
    public void initUI() {
        super.initUI();
        String title = getIntent().getStringExtra(TYPE_NAME);
        setTitle(title);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setFocusable(false);
        adapter = new MessageSonAdapter(this);
        adapter.setData(new ArrayList<MessageSonBean.ListBean>());
        adapter.setPageSize(20);
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        messageType =getIntent().getIntExtra(MESSAGE_TYPE_INT, 1);
        mPresenter.getAnnounceList(String.valueOf(messageType), adapter);
    }

    @Override
    public void loadMessage(List<MessageSonBean.ListBean> list) {
        if(isFirst) {
            isFirst = false;
            RxBus.get().send(RxBus.Code.MAIN_ACTIVITY_SELECT_ITEM_WITH_FRAGMENT_NAME);
        }
//        adapter.getData().clear();
//        adapter.appendData(list);
//        adapter.notifyDataSetChanged();
        adapter.doRefresh(list, layoutNoData, rcvContent, refreshLayout);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.setPage(1);
        mPresenter.getAnnounceList(String.valueOf(messageType), adapter);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getAnnounceList(String.valueOf(messageType), adapter);
        refreshLayout.finishLoadMore();
    }
}
