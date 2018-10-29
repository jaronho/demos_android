package com.liyuu.strategy.ui.transaction.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.Constants;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.transaction.BankListContract;
import com.liyuu.strategy.model.bean.BankBean;
import com.liyuu.strategy.presenter.transaction.BankListPresenter;
import com.liyuu.strategy.ui.transaction.adapter.BankListAdapter;
import java.util.List;
import butterknife.BindView;

public class BankListActivity extends BaseActivity<BankListPresenter>
        implements BankListContract.View {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    private BankListAdapter adapter;

    public static void start(Activity activity) {
        activity.startActivityForResult(new Intent(activity, BankListActivity.class), Constants.REQUEST_BANK_LIST);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bank_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.choose_bank);
        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);

        adapter = new BankListAdapter(this);
        rcvContent.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getBankList();
    }

    @Override
    public void loadData(List<BankBean> data) {
        adapter.setData(data);
    }
}
