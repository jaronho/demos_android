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
import com.gsclub.strategy.util.StringUtils;

import butterknife.ButterKnife;

/**
 * 微信客服弹窗
 */
public class CustomerWeChatDialog extends BaseDialog implements View.OnClickListener {

    public static CustomerWeChatDialog newInstance(String customer_we_chat) {
        CustomerWeChatDialog dialog = new CustomerWeChatDialog();
        Bundle bundle = new Bundle();
        bundle.putString("customer_we_chat", customer_we_chat);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_customer_wechat, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_sure).setOnClickListener(this);
        String customer_we_chat = getArguments().getString("customer_we_chat");
        TextView tv = ButterKnife.findById(view, R.id.tv_message);
        String wholeText = String.format("已成功复制客服微信  %s\n快去打开微信添加吧", customer_we_chat);
        StringUtils.setColorFulText(tv, wholeText, customer_we_chat, Color.parseColor("#21a4ff"));
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
                    baseImpl.dialogSure(CustomerWeChatDialog.class.getSimpleName(), this);
                break;

            case R.id.tv_cancel:
                if (baseImpl != null)
                    baseImpl.dialogCancle(CustomerWeChatDialog.class.getSimpleName(), this);
                dismiss();
                break;
        }
    }
}
