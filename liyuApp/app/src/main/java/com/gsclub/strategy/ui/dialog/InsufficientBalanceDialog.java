package com.gsclub.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gsclub.strategy.R;

import butterknife.ButterKnife;

/**
 * 余额不足弹窗
 */
public class InsufficientBalanceDialog extends BaseDialog implements View.OnClickListener {
    private final static String PAYMENT_MONEY_FLOAT = "payment_money_float";

    public static InsufficientBalanceDialog newInstance() {
        InsufficientBalanceDialog dialog = new InsufficientBalanceDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_insufficient_balance, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_sure).setOnClickListener(this);
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
                    baseImpl.dialogSure(InsufficientBalanceDialog.class.getSimpleName(), this);
                break;

            case R.id.tv_cancel:
                if (baseImpl != null)
                    baseImpl.dialogCancle(InsufficientBalanceDialog.class.getSimpleName(), this);
                dismiss();
                break;
        }
    }
}
