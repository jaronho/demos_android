package com.gsclub.strategy.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.text.TextWatcher;

@SuppressLint("AppCompatCustomView")
public class DifferSizeEditText extends EditText implements TextWatcher {
    public DifferSizeEditText(Context context) {
        super(context);
    }

    public DifferSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DifferSizeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() == 1 && before == 0) {
            if("0".equals(s.toString())) {
                setText("");
                return;
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }else if(s.length() == 0 && before == 1) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
