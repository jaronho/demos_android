package com.liyuu.strategy.di.component;

import android.app.Activity;
import com.liyuu.strategy.di.module.ActivityModule;
import com.liyuu.strategy.di.scope.ActivityScope;
import com.liyuu.strategy.ui.MainActivity;
import com.liyuu.strategy.ui.SplashActivity;
import com.liyuu.strategy.ui.home.activity.IncomeActivity;
import com.liyuu.strategy.ui.home.activity.PersonalRecordActivity;
import com.liyuu.strategy.ui.home.activity.SimulatedTradingActivity;
import com.liyuu.strategy.ui.login.ForgetPwdActivity;
import com.liyuu.strategy.ui.login.LoginActivity;
import com.liyuu.strategy.ui.login.NicknameActivity;
import com.liyuu.strategy.ui.login.RegisterActivity;
import com.liyuu.strategy.ui.mine.WebViewActivity;
import com.liyuu.strategy.ui.mine.activity.AboutUsActivity;
import com.liyuu.strategy.ui.mine.activity.EditPhoneActivity;
import com.liyuu.strategy.ui.mine.activity.EditPwdActivity;
import com.liyuu.strategy.ui.mine.activity.FeedbackActivity;
import com.liyuu.strategy.ui.mine.activity.MessageActivity;
import com.liyuu.strategy.ui.mine.activity.MessageSonActivity;
import com.liyuu.strategy.ui.mine.activity.MineRecordActivity;
import com.liyuu.strategy.ui.mine.activity.SettingActivity;
import com.liyuu.strategy.ui.mine.activity.UserEditActivity;
import com.liyuu.strategy.ui.optional.activity.SelectStockActivity;
import com.liyuu.strategy.ui.optional.activity.StockSelectEditActivity;
import com.liyuu.strategy.ui.stock.activity.SearchStockActivity;
import com.liyuu.strategy.ui.stock.activity.StockActivity;
import com.liyuu.strategy.ui.transaction.activity.BankListActivity;
import com.liyuu.strategy.ui.transaction.activity.BillsActivity;
import com.liyuu.strategy.ui.transaction.activity.BindCardActivity;
import com.liyuu.strategy.ui.transaction.activity.BuyingActivity;
import com.liyuu.strategy.ui.transaction.activity.NewsWelfareActivity;
import com.liyuu.strategy.ui.transaction.activity.PayAccountManageActivity;
import com.liyuu.strategy.ui.transaction.activity.PositionDetailActivity;
import com.liyuu.strategy.ui.transaction.activity.RechargeActivity;
import com.liyuu.strategy.ui.transaction.activity.RechargeResultActivity;
import com.liyuu.strategy.ui.transaction.activity.RecommendStocksActivity;
import com.liyuu.strategy.ui.transaction.activity.SetTradingPasswordActivity;
import com.liyuu.strategy.ui.transaction.activity.SettlementDetailActivity;
import com.liyuu.strategy.ui.transaction.activity.WithDrawActivity;
import com.liyuu.strategy.ui.transaction.activity.WithdrawConfirmActivity;

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
