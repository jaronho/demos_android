package com.liyuu.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.transaction.PayAccountManageContract;
import com.liyuu.strategy.model.bean.UserBankBean;
import com.liyuu.strategy.presenter.transaction.PayAccountManagePresenter;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PayAccountManageActivity extends BaseActivity<PayAccountManagePresenter> implements PayAccountManageContract.View {
    @BindView(R.id.layout_bind_card)
    View layoutBindCard;
    @BindView(R.id.layout_bank_card_info)
    View layoutBankCardInfo;
    @BindView(R.id.tv_trade_pwd_opt)
    TextView tvTradePwdOpt;
    @BindView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @BindView(R.id.tv_bank_info)
    TextView tvBankInfo;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PayAccountManageActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_account_manage;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.pay_account_manage);
    }

    @Override
    protected void initEventAndData() {
        setBankView();
    }

    @OnClick({R.id.layout_bind_card, R.id.layout_update_trading_password})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_bind_card:
                BindCardActivity.start(this);
                break;
            case R.id.layout_update_trading_password:
                SetTradingPasswordActivity.start(this);
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_BANK_REFRESH:
                setBankView();
                break;
        }
    }

    @Override
    public void setBankCardInfo(UserBankBean data) {
        if(data == null) return;
//        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_BANK_STATUS, data.getBankStatus());
        ImageLoader.load(App.getInstance(), data.getLogo(), ivBankLogo, R.mipmap.ic_img_def_grey_small);
        tvBankInfo.setText(String.format("%s(尾号%s)", data.getBankName(), data.getBankNoFour()));
        tvTip.setText(data.getTip());
    }

    private void setBankView() {
        // 未绑定银行卡
        if(!UserInfoUtil.hasBindCard()) {
            layoutBindCard.setVisibility(View.VISIBLE);
            layoutBankCardInfo.setVisibility(View.GONE);
            return;
        }
        // 显示银行卡信息，及设置/修改交易密码
        layoutBindCard.setVisibility(View.GONE);
        layoutBankCardInfo.setVisibility(View.VISIBLE);
        mPresenter.getBankCardInfo();
        tvTradePwdOpt.setText(UserInfoUtil.hasPayPwd()?R.string.update_trading_password:R.string.set_trading_password);
    }
}
