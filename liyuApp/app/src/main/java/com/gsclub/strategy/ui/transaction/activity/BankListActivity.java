package com.gsclub.strategy.ui.transaction.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.Constants;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.transaction.BankListContract;
import com.gsclub.strategy.model.bean.BankBean;
import com.gsclub.strategy.presenter.transaction.BankListPresenter;
import com.gsclub.strategy.ui.transaction.adapter.BankListAdapter;
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
