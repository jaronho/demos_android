package com.example.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nyapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NY on 2017/2/21.
 *
 */

public class MyMsgDialog extends Dialog {

    @BindView(R.id.ivIcon)
    ImageView mIvIcon;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvMsg)
    TextView mTvMsg;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.view_linear)
    View mViewLinear;
    private boolean mIsShowIcon;
    private String mTitle;
    private String mMessage;

    public MyMsgDialog(@NonNull Context context, String title, String message, ConfirmListener confirmListener, CancelListener cancelListener) {
        super(context, R.style.CustomDialog);
        this.mTitle = title;
        this.mMessage = message;
        this.mConfirmListener = confirmListener;
        this.mCancelListener = cancelListener;
    }
    public MyMsgDialog(@NonNull Context context,boolean isShowIcon, String title, String message, ConfirmListener confirmListener, CancelListener cancelListener) {
        super(context, R.style.CustomDialog);
        this.mIsShowIcon = isShowIcon;
        this.mTitle = title;
        this.mMessage = message;
        this.mConfirmListener = confirmListener;
        this.mCancelListener = cancelListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);
        ButterKnife.bind(this);
        if (mIsShowIcon) {
            mIvIcon.setVisibility(View.VISIBLE);
        } else {
            mIvIcon.setVisibility(View.GONE);
        }
        mTvTitle.setText(mTitle);
        mTvMsg.setText(mMessage);

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
