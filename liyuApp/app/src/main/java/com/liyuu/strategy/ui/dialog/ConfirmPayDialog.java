package com.liyuu.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.util.CalculationUtil;

import butterknife.ButterKnife;

/**
 * 确认支付弹窗
 */
public class ConfirmPayDialog extends BaseDialog implements View.OnClickListener {
    private final static String PAYMENT_MONEY_FLOAT = "payment_money_float";

    public static ConfirmPayDialog newInstance(float paymentMoney) {
        ConfirmPayDialog dialog = new ConfirmPayDialog();
        Bundle bundle = new Bundle();
        bundle.putFloat(PAYMENT_MONEY_FLOAT, paymentMoney);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setPaymentMoneyFloat(float paymentMoney) {
        getArguments().putFloat(PAYMENT_MONEY_FLOAT, paymentMoney);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_confirm_pay, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_sure).setOnClickListener(this);

        float paymentMoney = getArguments().getFloat(PAYMENT_MONEY_FLOAT);
        TextView tvPaymentMoney = ButterKnife.findById(view, R.id.tv_payment_money);
        tvPaymentMoney.setText(String.format("￥ %s元", CalculationUtil.roundRuturnString(paymentMoney, 2)));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                dismiss();
                break;
            case R.id.tv_sure:
                if (baseImpl != null)
                    baseImpl.dialogSure(ConfirmPayDialog.class.getSimpleName(), this);
                break;
            case R.id.tv_cancel:
                if (baseImpl != null)
                    baseImpl.dialogCancle(ConfirmPayDialog.class.getSimpleName(), this);
                dismiss();
                break;
        }
    }
}
