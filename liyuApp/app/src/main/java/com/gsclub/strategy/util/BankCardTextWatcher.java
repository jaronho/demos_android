package com.gsclub.strategy.util;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class BankCardTextWatcher implements TextWatcher {
    int beforeTextLength = 0;
    int onTextLength = 0;
    boolean isChanged = false;

    int location = 0;// 记录光标的位置
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    int konggeNumberB = 0;
    private EditText editText;

    public BankCardTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        onTextLength = s.length();
        buffer.append(s.toString());
        if (onTextLength == beforeTextLength || onTextLength <= 3
                || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        konggeNumberB = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                konggeNumberB++;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (isChanged) {
            location = editText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int konggeNumberC = 0;
            while (index < buffer.length()) {
                if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                    buffer.insert(index, ' ');
                    konggeNumberC++;
                }
                index++;
            }

            if (konggeNumberC > konggeNumberB) {
                location += (konggeNumberC - konggeNumberB);
            }

            tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }
            editText.setText(str);
            Editable etable = editText.getText();
            Selection.setSelection(etable, location);
            isChanged = false;
        }
    }
}
