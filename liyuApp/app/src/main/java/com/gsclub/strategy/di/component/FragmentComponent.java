package com.gsclub.strategy.di.component;

import android.app.Activity;

import com.gsclub.strategy.di.module.FragmentModule;
import com.gsclub.strategy.di.scope.FragmentScope;
import com.gsclub.strategy.ui.HomeFragment;
import com.gsclub.strategy.ui.MineFragment;
import com.gsclub.strategy.ui.TradingUnLogFragment;
import com.gsclub.strategy.ui.TransactionFragment;
import com.gsclub.strategy.ui.home.fragment.IncomeListFragment;
import com.gsclub.strategy.ui.home.fragment.PersonalRecord5DYieldFragment;
import com.gsclub.strategy.ui.home.fragment.PersonalRecordListFragment;
import com.gsclub.strategy.ui.home.fragment.TradingSimulatedPositionListFragment;
import com.gsclub.strategy.ui.home.fragment.TradingSimulatedSettlementListFragment;
import com.gsclub.strategy.ui.optional.OptionalFragment;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealCommissionListFragment;
import com.gsclub.strategy.ui.stock.fragment.FiveDayFragment;
import com.gsclub.strategy.ui.stock.fragment.KLineFragment;
import com.gsclub.strategy.ui.stock.fragment.MinuteFragment;
import com.gsclub.strategy.ui.transaction.fragment.BillListFragment;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealPositionListFragment;
import com.gsclub.strategy.ui.transaction.fragment.TradingRealSettlementListFragment;

import dagger.Component;

/**
 * FragmentComponent
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(HomeFragment homeFragment);

    void inject(TransactionFragment transactionFragment);

    void inject(TradingUnLogFragment tradingUnLogFragment);

    void inject(MineFragment mineFragment);

    void inject(IncomeListFragment incomeListFragment);

    void inject(PersonalRecordListFragment personalRecordListFragment);

    void inject(PersonalRecord5DYieldFragment personalRecord5DYieldFragment);

    void inject(MinuteFragment minuteFragment);

    void inject(FiveDayFragment fiveDayFragment);

    void inject(KLineFragment kLineFragment);

    void inject(BillListFragment billListFragment);

    void inject(TradingRealCommissionListFragment tradingRealCommissionListFragment);

    void inject(TradingRealPositionListFragment tradingRealPositionListFragment);

    void inject(TradingRealSettlementListFragment tradingRealSettlementListFragment);

    void inject(TradingSimulatedPositionListFragment tradingSimulatedPositionListFragment);

    void inject(TradingSimulatedSettlementListFragment tradingSimulatedSettlementListFragment);

    void inject(OptionalFragment optionalFragment);
}
