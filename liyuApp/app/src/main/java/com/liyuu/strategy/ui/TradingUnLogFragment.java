package com.liyuu.strategy.ui;

import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.contract.transaction.TradingUnLogContract;
import com.liyuu.strategy.model.bean.ActivityImagesBean;
import com.liyuu.strategy.presenter.transaction.TradingUnLogPresenter;
import com.liyuu.strategy.ui.home.adapter.NewsWelfareAdapter;
import com.liyuu.strategy.ui.login.LoginActivity;
import com.liyuu.strategy.ui.stock.activity.SearchStockActivity;
import com.liyuu.strategy.ui.transaction.activity.NewsWelfareActivity;
import com.liyuu.strategy.util.UserInfoUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TradingUnLogFragment extends BaseFragment<TradingUnLogPresenter> implements TradingUnLogContract.View {
    @BindView(R.id.layout_news_welfare)
    View layoutNewsWelfare;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.nsv_trading)
    NestedScrollView nsvTrading;
    private NewsWelfareAdapter adapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trading_unlog;
    }

    @Override
    public void initUI() {
        super.initUI();
        rcvContent.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rcvContent.setHasFixedSize(true);
        rcvContent.setNestedScrollingEnabled(false);
        adapter = new NewsWelfareAdapter(getActivity());
        rcvContent.setAdapter(adapter);
        mPresenter.getActivityImages();
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.tv_go_trading, R.id.tv_go_login, R.id.iv_banner, R.id.tv_register_get})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_trading:
                if (UserInfoUtil.isWithLogin(getActivity()))
                    SearchStockActivity.start(getActivity());
                break;
            case R.id.tv_go_login:
                LoginActivity.start(getActivity());
                break;
            case R.id.iv_banner:
            case R.id.tv_register_get:
                NewsWelfareActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void showActivity(ActivityImagesBean bean) {
        if (bean == null) return;
        List<ActivityImagesBean.ListBean> list = bean.getList();
        if (list == null || list.size() == 0) return;
        layoutNewsWelfare.setVisibility(View.VISIBLE);
        adapter.setData(list);
        ImageLoader.load(getActivity(), bean.getModel(), ivBanner);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                nsvTrading.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });
    }
}
