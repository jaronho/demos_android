package com.liyuu.strategy.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.ui.mine.WebViewActivity;

public class TextViewUtil {
    private static void setLinkClickable(final Context context, final SpannableStringBuilder clickableHtmlBuilder,
                                         final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View view) {
                //Do something with URL here.
                String url = urlSpan.getURL();
                System.out.println(url);
                WebViewActivity.start(context, url);
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    public static CharSequence getClickableHtml(Context context, String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(context, clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }

    /**
     * 设置包含超文本连接的textview
     *
     * @param allText      textview中所有的内容
     * @param remarkText   超文本链接文字内容
     * @param url          超文本链接跳转url
     * @param isAppendMode textview 装载模式 true 为append,不清空textview，后续继续添加spanning
     */
    public static void setRemarkTextView(final Context context, TextView textView, @NonNull String allText,
                                         String remarkText, final String url, boolean isAppendMode) {
        if (TextUtils.isEmpty(remarkText)) {
            String tvString = textView.getText().toString();
            boolean isNull = TextUtils.isEmpty(tvString);
            if (isNull)
                textView.setText(allText);
            else
                textView.append(allText);
            return;
        }

        SpannableString ss = new SpannableString(allText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                WebViewActivity.start(context, url);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        int start = allText.indexOf(remarkText);
        ss.setSpan(clickableSpan, start, start + remarkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isAppendMode)
            textView.append(ss);
        else
            textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setLineSpacing(ScreenUtil.dp2px(context, 7), 1);
    }
}
