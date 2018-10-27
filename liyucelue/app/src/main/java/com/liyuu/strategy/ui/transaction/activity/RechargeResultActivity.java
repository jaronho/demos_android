package com.liyuu.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.NullContract;
import com.liyuu.strategy.presenter.NullPresenter;
import com.liyuu.strategy.util.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeResultActivity extends BaseActivity<NullPresenter> implements NullContract.View {
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    private String message;

    public static void start(Context context, boolean isRecharge, boolean isSuccess, String message) {
        Intent intent = new Intent(context, RechargeResultActivity.class);
        intent.putExtra("isRecharge", isRecharge);
        intent.putExtra("isSuccess", isSuccess);
        if(!TextUtils.isEmpty(message)) {
            intent.putExtra("message", message);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_recharge_result;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.recharge);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("完成");
    }

    @Override
    protected void initEventAndData() {
        Intent intent = getIntent();
        boolean isRecharge = intent.getBooleanExtra("isRecharge", false);
        boolean isSuccess = intent.getBooleanExtra("isSuccess", false);
        message = intent.getStringExtra("message");
        setView(isRecharge, isSuccess);
    }

    private void setView(boolean isRecharge, boolean isSuccess) {
        setTitle(isRecharge?R.string.recharge:R.string.withdraw);
        setTextDrawable(isSuccess);
        if(isRecharge) {
            String type_name = getResources().getString(R.string.recharge);
            String result = getResources().getString(isSuccess?R.string.success:R.string.failure);
            tvResult.setText(type_name + result);
            return;
        }
        String result = isSuccess?"您的提现申请已提交成功":"提现失败";
        tvResult.setText(result);
        if(!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }
    }

    private void setTextDrawable(boolean isSuccess) {
        Drawable drawableTop = getResources().getDrawable(isSuccess?
                R.mipmap.icon_recharge_success:R.mipmap.icon_recharge_fail);
        tvResult.setCompoundDrawablesWithIntrinsicBounds(null,
                drawableTop, null, null);
        tvResult.setCompoundDrawablePadding(ScreenUtil.dp2px(this, 20));
    }

    @OnClick({R.id.tv_right})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                finishUI();
                break;
        }
    }
}
