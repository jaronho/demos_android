package com.gsclub.strategy.ui.optional.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.optional.StockSelectEditContract;
import com.gsclub.strategy.model.bean.CustomSelectStockBean;
import com.gsclub.strategy.presenter.optional.StockSelectEditPresenter;
import com.gsclub.strategy.ui.helper.OnStartDragListener;
import com.gsclub.strategy.ui.helper.SimpleItemTouchHelperCallback;
import com.gsclub.strategy.ui.optional.adapter.StockSelectEditRecyclerAdatper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockSelectEditActivity extends BaseActivity<StockSelectEditPresenter>
        implements StockSelectEditContract.View, OnStartDragListener,
        StockSelectEditRecyclerAdatper.OnSelectChange {
    @BindView(R.id.rcv)
    RecyclerView rcvContent;
    @BindView(R.id.tv_delete_count)
    TextView tvDeleteCount;
    @BindView(R.id.img_all_select)
    ImageView imgSelectAll;
    @BindView(R.id.tv_select_notify)
    TextView tvSelectNotify;


    private ItemTouchHelper mItemTouchHelper;

    private StockSelectEditRecyclerAdatper adapter;

    public static void start(Context context) {
        if (context == null)
            return;
        context.startActivity(new Intent(context, StockSelectEditActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_stock_select_edit;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getResources().getString(R.string.mine_optiona));
        getToolbar().setNavigationIcon(null);
        TextView tvLeft = getToolbar().findViewById(R.id.tv_left);
        tvLeft.setText(getResources().getString(R.string.finish));
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getData() != null) {
                    if (adapter.getData().size() != 0)
                        mPresenter.reSortUserLocalOptionalStockDatas(adapter);
                    finish();
                }
            }
        });
    }

    @Override
    protected void initEventAndData() {
        adapter = new StockSelectEditRecyclerAdatper(StockSelectEditActivity.this, this);
        adapter.setData(new ArrayList<CustomSelectStockBean>());
        adapter.setSelectChange(this);

        rcvContent.setLayoutManager(new LinearLayoutManager(this));
        rcvContent.setHasFixedSize(true);
        rcvContent.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rcvContent);

        mPresenter.getUserLocalOptionalStockDatas();

    }

    @OnClick({R.id.rl_all_select, R.id.tv_delete_count})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_all_select:
                if (getSelectCount(adapter.getData()) != adapter.getData().size())
                    changeAllSelect(1, adapter.getData());
                else
                    changeAllSelect(0, adapter.getData());
                adapter.notifyDataSetChanged();
                changeSelectCount();
                break;
            case R.id.tv_delete_count:
                if (adapter != null)
                    mPresenter.deleteSomeUserLocalOptionalStockDatas(adapter);
                break;
        }
    }

    /**
     * 设置全选，全不选
     *
     * @param changeType 1：全选 2：全不选
     */
    private void changeAllSelect(int changeType, List<CustomSelectStockBean> list) {
        boolean b = false;
        if (changeType == 0) b = false;
        else if (changeType == 1) b = true;
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(b);
        }
    }

    private int getSelectCount(List<CustomSelectStockBean> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) count++;
        }
        return count;
    }


    private void changeSelectCount() {
        tvDeleteCount.setText(String.valueOf(getResources().getString(R.string.delete) + "(" + getSelectCount(adapter.getData()) + ")"));
        if (adapter.getData() == null) return;
        int bg = 0;
        if (adapter.getData().size() == 0 || adapter.getData().size() != getSelectCount(adapter.getData())) {
            bg = R.mipmap.optional_edit_select;
            tvSelectNotify.setText(getResources().getString(R.string.all_item_select));
        } else {
            bg = R.mipmap.optional_edit_select_pre;
            tvSelectNotify.setText(getResources().getString(R.string.cancle_all_select));
        }
        imgSelectAll.setBackgroundResource(bg);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void showUserLocalOptionalStockDatas(List<CustomSelectStockBean> datas) {
        if (adapter == null)
            return;
        adapter.getData().clear();
        adapter.setData(datas);
        adapter.notifyDataSetChanged();
        changeSelectCount();
    }

    @Override
    public void onChange() {
        changeSelectCount();
    }
}
