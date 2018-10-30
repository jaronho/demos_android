package com.gsclub.strategy.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseFragment;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.main.TransactionContract;
import com.gsclub.strategy.model.bean.UserTradeInfoBean;
import com.gsclub.strategy.presenter.main.TransactionPresenter;
import com.gsclub.strategy.ui.stock.activity.SearchStockActivity;
import com.gsclub.strategy.ui.transaction.activity.BillsActivity;
import com.gsclub.strategy.ui.transaction.activity.BindCardActivity;
import com.gsclub.strategy.ui.transaction.activity.PayAccountManageActivity;
import com.gsclub.strategy.ui.transaction.activity.RechargeActivity;
import com.gsclub.strategy.ui.transaction.activity.SetTradingPasswordActivity;
import com.gsclub.strategy.ui.transaction.activity.WithDrawActivity;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealCommissionListFragment;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealPositionListFragment;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealSettlementListFragment;
import com.gsclub.strategy.ui.view.CustomTabLayout;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.StringUtils;
import com.gsclub.strategy.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class TransactionFragment extends BaseFragment<TransactionPresenter>
        implements TransactionContract.View, ViewPager.OnPageChangeListener {
    public final static int select_commission = 0;
    public final static int select_position = 1;
    public final static int select_settlement = 2;
    private final static String[] selectItems = {"commission", "position", "commission"};
    @BindView(R.id.tv_user_all_money)
    TextView tvUserAllMoney;
    @BindView(R.id.tv_user_balance)
    TextView tvUserBalance;
    @BindView(R.id.tv_user_margin_money)
    TextView tvUserMarginMoney;
    @BindView(R.id.tv_user_market_value)
    TextView tvUserMarketValue;
    @BindView(R.id.tv_user_profit_loss)
    TextView tvUserProfitLoss;
    @BindView(R.id.view_pager)
    ViewPager pager;
    @BindView(R.id.tab_layout)
    CustomTabLayout tabLayout;

    public static int getSelectItem(@NonNull String itemsName) {
        for (int i = 0; i < selectItems.length; i++) {
            if (selectItems[i].equals(itemsName))
                return i;
        }
        return 0;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transaction;
    }

    @Override
    public void initUI() {
        super.initUI();
        initTabLayout();
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getUserTradeInfo();
    }

    @OnClick({R.id.tv_right, R.id.tv_go_trading, R.id.tv_withdraw, R.id.tv_recharge, R.id.tv_account_manage})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                BillsActivity.start(getActivity());
                break;
            case R.id.tv_go_trading:
                if (UserInfoUtil.isWithLogin(getActivity()))
                    SearchStockActivity.start(getActivity());
                break;
            case R.id.tv_withdraw:
                if (!UserInfoUtil.hasBindCard()) {
                    BindCardActivity.start(getActivity());
                    return;
                }
                if (!UserInfoUtil.hasPayPwd()) {
                    SetTradingPasswordActivity.start(getActivity());
                    return;
                }
                WithDrawActivity.start(getActivity());
                break;
            case R.id.tv_recharge:
                if (!UserInfoUtil.hasBindCard()) {
                    BindCardActivity.start(getActivity());
                    return;
                }
                RechargeActivity.start(getActivity());
                break;
            case R.id.tv_account_manage:
                PayAccountManageActivity.start(getActivity());
                break;
        }
    }

    private void initTabLayout() {
        pager.setHorizontalScrollBarEnabled(true);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new PageAdapter(getChildFragmentManager()));
        pager.addOnPageChangeListener(this);

        int select = PreferenceUtils.getInt(SPKeys.FILE_OTHER, SPKeys.OTHER_TRANSACTION_SELECT_INT, 0);
        pager.setCurrentItem(select);
        PreferenceUtils.put(SPKeys.FILE_OTHER, SPKeys.OTHER_TRANSACTION_SELECT_INT, 0);

        tabLayout.setupWithViewPager(pager);
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
    public void showUserTradeInfo(UserTradeInfoBean data) throws NumberFormatException {
        tvUserAllMoney.setText(data.getTotalMoney());
        tvUserBalance.setText(data.getBalanceMoney());
        tvUserMarginMoney.setText(data.getFreezeMoney());
        tvUserMarketValue.setText(data.getMarketPrice());
        tvUserProfitLoss.setText(data.getProfitLoss());
        int color = getResources().getColor(StringUtils.parseDouble(data.getProfitLoss()) >= 0 ? R.color.stock_money_up_red : R.color.stock_money_down_green);
        tvUserProfitLoss.setTextColor(color);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.TRADING_SUCCESS:
                if (UserInfoUtil.isLogin())
                    mPresenter.getUserTradeInfo();
                break;
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
                if (UserInfoUtil.isLogin())
                    mPresenter.getUserTradeInfo();
                break;
            case RxBus.Code.HEART_BREAK:
                if (isFragmentCanShow && UserInfoUtil.isLogin())
                    mPresenter.getUserTradeInfo();
                break;
            case RxBus.Code.NEWS_WELFARE_BUYING_SUCCESS:
                if (UserInfoUtil.isLogin())
                    mPresenter.getUserTradeInfo();
                pager.setCurrentItem(1);
                break;
            case RxBus.Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT:
                if (object == null)
                    break;
                int selectPosition = (int) object;
                pager.setCurrentItem(selectPosition);
                break;
        }
    }

    private class PageAdapter extends FragmentPagerAdapter {

        String[] titles = new String[]{"委托", "持仓", "结算"};

        private PageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

//            TradeType tradeType;
            if (position == 0) {
                return TradingRealCommissionListFragment.newInstance();
            } else if (position == 1) {
                return TradingRealPositionListFragment.newInstance();
            } else {
                return TradingRealSettlementListFragment.newInstance();
            }
//            return TradingListFragment.newInstance(tradeType, true);//这里为了保持持仓和结算的type值跟我的交易中一样，加1

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
