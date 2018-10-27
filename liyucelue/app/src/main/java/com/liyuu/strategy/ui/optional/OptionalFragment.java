package com.liyuu.strategy.ui.optional;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.optional.OptionalContract;
import com.liyuu.strategy.model.bean.CustomSelectStockBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.presenter.optional.OptionalPresenter;
import com.liyuu.strategy.ui.home.adapter.StockTopAdapter;
import com.liyuu.strategy.ui.optional.activity.StockSelectEditActivity;
import com.liyuu.strategy.ui.optional.adapter.StockSelectRecyclerAdatper;
import com.liyuu.strategy.ui.stock.activity.SearchStockActivity;
import com.liyuu.strategy.util.ScreenUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自选fragment
 */

public class OptionalFragment extends BaseFragment<OptionalPresenter>
        implements OptionalContract.View, OnRefreshListener {
    @BindView(R.id.rcv_stock_content)
    RecyclerView rcvContent;
    @BindView(R.id.rcv_stock_top)
    RecyclerView rcvStockTop;
    @BindView(R.id.tv_float_change)
    TextView tvFloatChange;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.v_fill)
    View vFill;

    @BindView(R.id.layout_no_data)
    View layoutNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    private StockTopAdapter topAdapter;
    private StockSelectRecyclerAdatper adatper;
    private boolean isNeedRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_optional;
    }

    @Override
    public void initUI() {
        super.initUI();

        tvNoData.setGravity(Gravity.CENTER);
        tvNoData.setPadding(0, ScreenUtil.dip2px(getActivity(), 96), 0, 0);

        rcvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);
        adatper = new StockSelectRecyclerAdatper(getActivity());
        adatper.setData(new ArrayList<CustomSelectStockBean>());
        rcvContent.setAdapter(adatper);

        rcvStockTop.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rcvStockTop.setHasFixedSize(true);
        rcvStockTop.setNestedScrollingEnabled(false);
        topAdapter = new StockTopAdapter(getActivity());
        topAdapter.setData(mPresenter.getThreeStockPlateDefaultData());
        topAdapter.notifyDataSetChanged();
        rcvStockTop.setAdapter(topAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedRefresh)
            mPresenter.getUserLocalOptionalStockDatas();
        isNeedRefresh = false;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getThreeStockPlateData();
        mPresenter.getUserLocalOptionalStockDatas();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                if (!isFragmentCanShow)
                    break;
                //fragment可见时，需要刷新三大板块的数据
                mPresenter.getThreeStockPlateData();
                mPresenter.getStocksRealDataAndRefreshList(adatper);
                break;
            case RxBus.Code.MINE_OPTOPNAL_STOCK_DATA_CHANGED:
                isNeedRefresh = true;
                break;
        }
    }

    @OnClick({R.id.img_title_left, R.id.tv_float_change, R.id.tv_title_right})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_title_left:
                SearchStockActivity.start(getActivity());
                break;
            case R.id.tv_float_change:
                mPresenter.changeSortMode(adatper);
                break;
            case R.id.tv_title_right:
                StockSelectEditActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void setTopStocks(List<OptionalTopStockBean> list) {
        topAdapter.getData().clear();
        topAdapter.setData(list);
        topAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUserLocalOptionalStockDatas(List<CustomSelectStockBean> datas) {
        if (adatper == null)
            return;

        boolean isHaveData = !(datas == null || datas.size() == 0);
        layoutNoData.setVisibility(isHaveData ? View.GONE : View.VISIBLE);
        rcvContent.setVisibility(isHaveData ? View.VISIBLE : View.GONE);
        adatper.getData().clear();
        adatper.notifyDataSetChanged();

        if (!isHaveData)
            return;

        adatper.getData().addAll(datas);
        adatper.notifyDataSetChanged();

        mPresenter.getStocksRealDataAndRefreshList(adatper);
    }

    @Override
    public void changeSortIcon(int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvFloatChange.setCompoundDrawables(null, null, drawable, null);
    }
}
