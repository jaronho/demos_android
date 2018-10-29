package com.liyuu.strategy.di.component;

import android.app.Activity;

import com.liyuu.strategy.di.module.FragmentModule;
import com.liyuu.strategy.di.scope.FragmentScope;
import com.liyuu.strategy.ui.HomeFragment;
import com.liyuu.strategy.ui.MineFragment;
import com.liyuu.strategy.ui.TradingUnLogFragment;
import com.liyuu.strategy.ui.TransactionFragment;
import com.liyuu.strategy.ui.home.fragment.IncomeListFragment;
import com.liyuu.strategy.ui.home.fragment.PersonalRecord5DYieldFragment;
import com.liyuu.strategy.ui.home.fragment.PersonalRecordListFragment;
import com.liyuu.strategy.ui.home.fragment.TradingSimulatedPositionListFragment;
import com.liyuu.strategy.ui.home.fragment.TradingSimulatedSettlementListFragment;
import com.liyuu.strategy.ui.optional.OptionalFragment;
import com.liyuu.strategy.ui.transaction.fragment.TradingRealCommissionListFragment;
import com.liyuu.strategy.ui.stock.fragment.FiveDayFragment;
import com.liyuu.strategy.ui.stock.fragment.KLineFragment;
import com.liyuu.strategy.ui.stock.fragment.MinuteFragment;
import com.liyuu.strategy.ui.transaction.fragment.BillListFragment;
import com.liyuu.strategy.ui.transaction.fragment.TradingRealPositionListFragment;
import com.liyuu.strategy.ui.transaction.fragment.TradingRealSettlementListFragment;

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
