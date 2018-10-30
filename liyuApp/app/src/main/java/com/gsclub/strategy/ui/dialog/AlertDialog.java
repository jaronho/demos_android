package com.gsclub.strategy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gsclub.strategy.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-08-13.
 */

public class AlertDialog extends Dialog {
    private Context context;

    public AlertDialog(@NonNull Context context) {
        super(context, R.style.ThemeDeviceAlertDialogStyle); // Theme_DeviceDefault_Dialog_NoActionBar
        this.context = context;
    }

    private boolean isEditText, singleButton;
    private CharSequence editText, editHint;

    private CharSequence textCancel, textSure;
    private OnClickListener cancelListener, sureListener;

    private EditText mEditText;
    private InputFilter[] filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);

        mEditText = ButterKnife.findById(this, R.id.text);
        mEditText.setText(editText);
        mEditText.setHint(editHint);
        if (filters != null) mEditText.setFilters(filters);
        if (!isEditText) {
            mEditText.setEnabled(false);
        }

        final TextView cancel = ButterKnife.findById(this, R.id.btn_cancel);
        if (textCancel != null) cancel.setText(textCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(AlertDialog.this, Dialog.BUTTON_NEGATIVE);
                }
//                DialogActivity.dismissAndFinish(AlertDialog.this, context);
            }
        });
        TextView sure = ButterKnife.findById(this, R.id.btn_sure);
        if (textSure != null) sure.setText(textSure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureListener != null) {
                    sureListener.onClick(AlertDialog.this, Dialog.BUTTON_POSITIVE);
                }
//                DialogActivity.dismissAndFinish(AlertDialog.this, context);
            }
        });

        if (singleButton) {
            cancel.setVisibility(View.GONE);
            sure.setBackgroundResource(R.drawable.blue_btn_background_bottom_corner);
        }
    }

    public AlertDialog setTextView(CharSequence text) {
        isEditText = false;
        editText = text;
        if (mEditText != null) {
            mEditText.setText(editText);
            mEditText.setEnabled(isEditText);
        }
        return this;
    }

    public AlertDialog setEditText(CharSequence text, CharSequence hint) {
        isEditText = true;
        editText = text;
        editHint = hint;
        if (mEditText != null) {
            mEditText.setText(editText);
            mEditText.setHint(editHint);
            mEditText.setEnabled(isEditText);
        }
        return this;
    }

    public AlertDialog setInputFilters(InputFilter... filters) {
        if (filters == null) filters = new InputFilter[]{};
        if (mEditText != null) {
            mEditText.setFilters(filters);
        }
        this.filters = filters;
        return this;
    }

    public AlertDialog clearInputFilters() {
        return setInputFilters();
    }

    public AlertDialog setCancelButton(CharSequence text, OnClickListener listener) {
        this.textCancel = text;
        this.cancelListener = listener;
        return this;
    }

    public AlertDialog setSureButton(CharSequence text, OnClickListener listener) {
        singleButton = false;
        this.textSure = text;
        this.sureListener = listener;
        return this;
    }

    public AlertDialog setSingleButton(CharSequence text, OnClickListener listener) {
        singleButton = true;
        this.textSure = text;
        this.sureListener = listener;
        return this;
    }

    public String getText() {
        return mEditText.getText().toString();
    }

}
