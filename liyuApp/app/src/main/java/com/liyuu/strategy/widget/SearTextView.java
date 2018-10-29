package com.liyuu.strategy.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部分文字标色
 */
@SuppressLint("AppCompatCustomView")
public class SearTextView extends TextView {

    public SearTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSpecifiedTextsColor(String text, String specifiedTexts, int color) {
        List<Integer> sTextsStartList = new ArrayList<>();
        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(specifiedTexts);
            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
//                if (text.toUpperCase().equals(text)) {
//                }
            }
        } while (start != -1);
        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(styledText);
    }

    public void setSpecifiedTextsColor(String text, List<String> texts, int color) {
        if (!TextUtils.isEmpty(text) && texts != null && texts.size() > 0) {
            List<TextPositionBean> sTextsStartList = new ArrayList<>();
            for (String specifiedTexts : texts) {
                int sTextLength = specifiedTexts.length();
                String temp = text;
                int lengthFront = 0;//记录被找出后前面的字段的长度
                int start = -1;
                do {
                    start = temp.indexOf(specifiedTexts);
                    if (start != -1) {
                        start = start + lengthFront;
                        TextPositionBean bean = new TextPositionBean();
                        bean.setStart(start);
                        bean.setEnd(start + sTextLength);
                        sTextsStartList.add(bean);
                        lengthFront = start + sTextLength;
                        temp = text.substring(lengthFront);
                    }
                } while (start != -1);
            }

            SpannableStringBuilder styledText = new SpannableStringBuilder(text);
            for (TextPositionBean i : sTextsStartList) {
                styledText.setSpan(
                        new ForegroundColorSpan(color),
                        i.start,
                        i.end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(styledText);
        } else {
            setText(TextUtils.isEmpty(text) ? "" : text);
        }
    }

    private class TextPositionBean implements Serializable {
        private int start;
        private int end;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

}

