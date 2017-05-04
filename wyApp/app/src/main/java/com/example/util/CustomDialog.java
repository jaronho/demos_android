package com.example.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nyapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NY on 2017/3/8.
 * 自定义dialog
 */

public class CustomDialog extends Dialog {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.rl_dialog_content)
    RelativeLayout mRlDialogContent;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    @BindView(R.id.view_linear)
    View mViewLinear;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.view_top_linear)
    View mViewTopLinear;
    private String mTitle;
    private View mView;

    public CustomDialog(Context context, String title, View view, ConfirmListener confirmListener, CancelListener cancelListener) {
        super(context, R.style.CustomDialog);
        mTitle = title;
        mView = view;
        mConfirmListener = confirmListener;
        mCancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_dialog);
        ButterKnife.bind(this);

        mTvTitle.setText(mTitle);
        mRlDialogContent.addView(mView);

        if (mConfirmListener != null) {
            mTvSubmit.setVisibility(View.VISIBLE);
            mTvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConfirmListener.onClick();
                }
            });
        } else {
            mTvSubmit.setVisibility(View.GONE);
            mViewLinear.setVisibility(View.GONE);
        }

        if (mCancelListener != null) {
            mTvCancel.setVisibility(View.VISIBLE);
            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCancelListener.onClick();
                }
            });
        } else {
            mTvCancel.setVisibility(View.GONE);
            mViewLinear.setVisibility(View.GONE);
        }

        if (mConfirmListener == null && mCancelListener == null) {
            mTvSubmit.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mViewLinear.setVisibility(View.GONE);
            mViewTopLinear.setVisibility(View.GONE);
        }
    }

    private ConfirmListener mConfirmListener;

    public interface ConfirmListener {
        void onClick();
    }

    private CancelListener mCancelListener;

    public interface CancelListener {
        void onClick();
    }
}
