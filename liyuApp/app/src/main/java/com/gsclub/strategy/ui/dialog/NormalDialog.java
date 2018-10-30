package com.gsclub.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsclub.strategy.R;

import butterknife.ButterKnife;

/**
 * 常用弹窗（头部/中心消息提示/底部确认取消按钮）
 */
public class NormalDialog extends BaseDialog implements View.OnClickListener {
    private final static String DIALOG_HEADER_STRING = "dialog_header_string";
    private final static String DIALOG_NOTIFY_STRING = "dialog_notify_string";
    private boolean isCloseIconEquivalentCloseButton = false;//让顶部关闭键与底部关闭按钮点击效果一致

    public static NormalDialog newInstance(String headerString, String notifyString) {
        return newInstance(headerString, notifyString, true);
    }

    public static NormalDialog newInstance(String headerString, String notifyString, boolean isCancelable) {
        NormalDialog dialog = new NormalDialog();
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_HEADER_STRING, headerString);
        bundle.putString(DIALOG_NOTIFY_STRING, notifyString);
        dialog.setArguments(bundle);
        dialog.setCancelable(isCancelable);
        return dialog;
    }

    public void setCloseEquivalent(boolean closeEquivalent) {
        this.isCloseIconEquivalentCloseButton = closeEquivalent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_normal, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_sure).setOnClickListener(this);

        String headerString = getArguments().getString(DIALOG_HEADER_STRING);
        TextView tvHeader = view.findViewById(R.id.tv_header);
        tvHeader.setText(headerString);

        String notifyString = getArguments().getString(DIALOG_NOTIFY_STRING);
        TextView tvNotify = view.findViewById(R.id.tv_notify);
        tvNotify.setText(notifyString);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                if (isCloseIconEquivalentCloseButton && baseImpl != null)
                    baseImpl.dialogCancle(this.getClass().getSimpleName(), this);
                dismiss();
                break;
            case R.id.tv_sure:
                if (baseImpl != null)
                    baseImpl.dialogSure(this.getClass().getSimpleName(), this);
                break;
            case R.id.tv_cancel:
                if (baseImpl != null)
                    baseImpl.dialogCancle(this.getClass().getSimpleName(), this);
                dismiss();
                break;
        }
    }
}
