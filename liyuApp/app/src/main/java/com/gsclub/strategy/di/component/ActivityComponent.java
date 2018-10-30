package com.gsclub.strategy.di.component;

import android.app.Activity;
import com.gsclub.strategy.di.module.ActivityModule;
import com.gsclub.strategy.di.scope.ActivityScope;
import com.gsclub.strategy.ui.MainActivity;
import com.gsclub.strategy.ui.SplashActivity;
import com.gsclub.strategy.ui.home.activity.IncomeActivity;
import com.gsclub.strategy.ui.home.activity.PersonalRecordActivity;
import com.gsclub.strategy.ui.home.activity.SimulatedTradingActivity;
import com.gsclub.strategy.ui.login.ForgetPwdActivity;
import com.gsclub.strategy.ui.login.LoginActivity;
import com.gsclub.strategy.ui.login.NicknameActivity;
import com.gsclub.strategy.ui.login.RegisterActivity;
import com.gsclub.strategy.ui.mine.WebViewActivity;
import com.gsclub.strategy.ui.mine.activity.AboutUsActivity;
import com.gsclub.strategy.ui.mine.activity.EditPhoneActivity;
import com.gsclub.strategy.ui.mine.activity.EditPwdActivity;
import com.gsclub.strategy.ui.mine.activity.FeedbackActivity;
import com.gsclub.strategy.ui.mine.activity.MessageActivity;
import com.gsclub.strategy.ui.mine.activity.MessageSonActivity;
import com.gsclub.strategy.ui.mine.activity.MineRecordActivity;
import com.gsclub.strategy.ui.mine.activity.SettingActivity;
import com.gsclub.strategy.ui.mine.activity.UserEditActivity;
import com.gsclub.strategy.ui.optional.activity.SelectStockActivity;
import com.gsclub.strategy.ui.optional.activity.StockSelectEditActivity;
import com.gsclub.strategy.ui.stock.activity.SearchStockActivity;
import com.gsclub.strategy.ui.stock.activity.StockActivity;
import com.gsclub.strategy.ui.transaction.activity.BankListActivity;
import com.gsclub.strategy.ui.transaction.activity.BillsActivity;
import com.gsclub.strategy.ui.transaction.activity.BindCardActivity;
import com.gsclub.strategy.ui.transaction.activity.BuyingActivity;
import com.gsclub.strategy.ui.transaction.activity.NewsWelfareActivity;
import com.gsclub.strategy.ui.transaction.activity.PayAccountManageActivity;
import com.gsclub.strategy.ui.transaction.activity.PositionDetailActivity;
import com.gsclub.strategy.ui.transaction.activity.RechargeActivity;
import com.gsclub.strategy.ui.transaction.activity.RechargeResultActivity;
import com.gsclub.strategy.ui.transaction.activity.RecommendStocksActivity;
import com.gsclub.strategy.ui.transaction.activity.SetTradingPasswordActivity;
import com.gsclub.strategy.ui.transaction.activity.SettlementDetailActivity;
import com.gsclub.strategy.ui.transaction.activity.WithDrawActivity;
import com.gsclub.strategy.ui.transaction.activity.WithdrawConfirmActivity;

import dagger.Component;

/**
 * activity component
 */

//生命周期管理
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    //方法参数中，只能传递被注入对象！要在哪个类中注入，写哪个类，注入到父类没用！
    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

    void inject(NicknameActivity nicknameActivity);

    void inject(ForgetPwdActivity forgetPwdActivity);

    void inject(IncomeActivity incomeActivity);

    void inject(PersonalRecordActivity personalRecordActivity);

    void inject(SimulatedTradingActivity simulatedTradingActivity);

    void inject(PositionDetailActivity positionDetailActivity);

    void inject(SearchStockActivity searchStockActivity);

    void inject(StockActivity stockActivity);

    void inject(SettingActivity settingActivity);

    void inject(MessageActivity messageActivity);

    void inject(MessageSonActivity messageSonActivity);

    void inject(WebViewActivity webViewActivity);

    void inject(MineRecordActivity mineRecordActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(UserEditActivity userEditActivity);

    void inject(EditPhoneActivity editPhoneActivity);

    void inject(EditPwdActivity editPwdActivity);

    void inject(AboutUsActivity aboutUsActivity);

    void inject(SettlementDetailActivity settlementDetailActivity);

    void inject(BillsActivity billsActivity);

    void inject(WithDrawActivity withDrawActivity);

    void inject(RechargeActivity rechargeActivity);

    void inject(PayAccountManageActivity payAccountManageActivity);

    void inject(SetTradingPasswordActivity setTradingPasswordActivity);

    void inject(BindCardActivity bindCardActivity);

    void inject(BuyingActivity buyingActivity);

    void inject(BankListActivity bankListActivity);

    void inject(RechargeResultActivity rechargeResultActivity);

    void inject(WithdrawConfirmActivity withdrawConfirmActivity);

    void inject(NewsWelfareActivity newsWelfareActivity);

    void inject(RecommendStocksActivity recommendStocksActivity);

    void inject(SelectStockActivity selectStockActivity);

    void inject(StockSelectEditActivity stockSelectEditActivity);
}
