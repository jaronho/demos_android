package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.WithdrawConfirmContract;
import com.gsclub.strategy.presenter.transaction.WithdrawConfirmPresenter;
import com.gsclub.strategy.util.SimpleTextWatcher;
import com.gsclub.strategy.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawConfirmActivity extends BaseActivity<WithdrawConfirmPresenter> implements WithdrawConfirmContract.View {

    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.et_trading_password)
    EditText etTradingPassword;
    private String money;

    public static void start(Context context, String money) {
        context.startActivity(new Intent(context, WithdrawConfirmActivity.class).putExtra("money", money));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_confirm_withdraw;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.confirm_withdraw);
        etTradingPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    tvSure.setEnabled(false);
                    return;
                }
                if(!tvSure.isEnabled()) {
                    tvSure.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void initEventAndData() {
        money = getIntent().getStringExtra("money");
    }

    @OnClick({R.id.tv_sure, R.id.tv_forget_trading_pwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                String trading_password = etTradingPassword.getText().toString();
                if(trading_password.length() < 8) {
                    ToastUtil.showMsg("输入的交易密码少于8位");
                    return;
                }
                mPresenter.confirmCashOrder(money, trading_password);
                break;
            case R.id.tv_forget_trading_pwd:
                SetTradingPasswordActivity.start(this);
                break;
        }
    }

    @Override
    public void doSuccess() {
        RxBus.get().send(RxBus.Code.TRADING_SUCCESS);
        RechargeResultActivity.start(this, false, true, null);
        finishUI();
    }

    @Override
    public void doFail(String message) {
        RechargeResultActivity.start(this, false, false, message);
    }
}
