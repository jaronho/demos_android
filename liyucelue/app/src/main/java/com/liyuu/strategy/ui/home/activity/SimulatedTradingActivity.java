package com.liyuu.strategy.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.home.SimulatedTradingContract;
import com.liyuu.strategy.model.bean.SimulatedInfoBean;
import com.liyuu.strategy.presenter.home.SimulatedTradingPresenter;
import com.liyuu.strategy.ui.home.fragment.TradingSimulatedPositionListFragment;
import com.liyuu.strategy.ui.home.fragment.TradingSimulatedSettlementListFragment;
import com.liyuu.strategy.ui.stock.activity.SearchStockActivity;
import com.liyuu.strategy.ui.view.CustomTabLayout;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimulatedTradingActivity extends BaseActivity<SimulatedTradingPresenter>
        implements SimulatedTradingContract.View, ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SimulatedTradingActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.getUserSimulatedInfo();
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_simulated_trading;
    }

    @Override
    public void initUI() {
        super.initUI();
        initTabLayout();
        setTitle("我的模拟交易");
    }

    @Override
    protected void initEventAndData() {
        //进入模拟交易，将交易状态置为模拟交易状态
        PreferenceUtils.put(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT, 2);

        mPresenter.getUserSimulatedInfo();
    }

    @OnClick({R.id.tv_go_trading})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_trading:
                SearchStockActivity.start(this);
        }
    }

    private void initTabLayout() {
        viewPager.setHorizontalScrollBarEnabled(true);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);

        CustomTabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //模拟交易页面被结束，即模拟交易状态被关闭，状态改为初始值
        PreferenceUtils.put(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT, 1);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                mPresenter.getUserSimulatedInfo();
                break;
        }
    }

    @Override
    public void showUserSimulatedInfo(SimulatedInfoBean data) {
        getTextView(R.id.tv_user_all_money).setText(data.getTotalMoney());
        getTextView(R.id.tv_user_balance).setText(data.getBalanceMoney());
        getTextView(R.id.tv_user_margin_money).setText(data.getFreezeMoney());
        getTextView(R.id.tv_user_market_value).setText(data.getMarketPrice());
        getTextView(R.id.tv_user_profit_loss).setText(data.getProfitLoss());
        int color = getResources().getColor(StringUtils.parseDouble(data.getProfitLoss()) >= 0 ? R.color.stock_money_up_red : R.color.stock_money_down_green);
        getTextView(R.id.tv_user_profit_loss).setTextColor(color);
    }

    private class PageAdapter extends FragmentPagerAdapter {

        String[] titles = new String[]{"持仓", "结算"};

        private PageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return TradingSimulatedPositionListFragment.newInstance();
            } else {
                return TradingSimulatedSettlementListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
