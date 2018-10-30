package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.ImageLoader;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.WithdrawContract;
import com.gsclub.strategy.model.bean.CashIndexBean;
import com.gsclub.strategy.presenter.transaction.WithdrawPresenter;
import com.gsclub.strategy.util.SimpleTextWatcher;
import com.gsclub.strategy.util.StringUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class WithDrawActivity extends BaseActivity<WithdrawPresenter> implements WithdrawContract.View {
    public static final int DIGITS_INT = 7;
    @BindView(R.id.iv_bank_logo)
    ImageView iv_bank_logo;
    @BindView(R.id.tv_can_withdraw_money)
    TextView tv_can_withdraw_money;
    @BindView(R.id.tv_fee)
    TextView tv_fee;
    @BindView(R.id.tv_fee_tip)
    TextView tv_fee_tip;
    @BindView(R.id.tv_bank_info)
    TextView tv_bank_info;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.et_money)
    EditText et_money;
    private String enable_cash_money, cash_fee;

    public static void start(Context context) {
        context.startActivity(new Intent(context, WithDrawActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.withdraw);
        et_money.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() == 1 && before == 0) || (s.length() == 2 && "0.".equals(s.toString()))) {
                    et_money.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    setSureTextViewEnabled(true);
                } else if (s.length() == 0 && before == 1) {
                    et_money.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    setSureTextViewEnabled(false);
                }
                setFeeView(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 小数点前面的数字被删除时，加上0
                if (s.toString().startsWith(".")) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("0");
                    sb.append(s.toString());
                    et_money.setText(sb.toString());
                    et_money.setSelection(sb.length());
                }
                // 当小数点被删除且剩余位数大于7时，将后面几位省略
                if (!s.toString().contains(".") && s.length() > DIGITS_INT) {
                    et_money.setText(s.subSequence(0, DIGITS_INT));
                    et_money.setSelection(DIGITS_INT);
                }
                // 当第一位被删，以0开头，将无效的零去掉
                if (s.toString().startsWith("0") && !s.toString().startsWith("0.") && s.length() != 1) {
                    int index = StringUtils.getNonZeroIndex(s.toString());
                    if (index != -1) {
                        String money = s.toString().substring(index, s.length());
                        et_money.setText(money);
                        et_money.setSelection(money.length());// 若采用自动连续删零的方式，这里会导致闪退
                        return;
                    }
                    et_money.setText("");
                    et_money.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
            }
        });
        et_money.setFilters(new InputFilter[]{new MoneyValueFilter()});
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getCashIndex();
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.TRADING_SUCCESS:
                finishUI();
                break;
        }
    }

    @OnClick({R.id.tv_withdraw_all, R.id.tv_sure})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_withdraw_all:
                if (!TextUtils.isEmpty(enable_cash_money)) {
                    et_money.setText(enable_cash_money);
                    et_money.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    et_money.setSelection(enable_cash_money.length());
                    setFeeView(enable_cash_money);
                }
                break;
            case R.id.tv_sure:
                mPresenter.getCashOrder(et_money.getText().toString());
                break;
        }
    }

    @Override
    public void loadData(CashIndexBean data) {
        ImageLoader.load(App.getInstance(), data.getLogo(), iv_bank_logo, R.mipmap.ic_img_def_grey_small);
        tv_bank_info.setText(String.format("%s(尾号%s)", data.getBankName(), data.getBankNo()));
        int color = getResources().getColor(R.color.text_orange_ff8400);
        enable_cash_money = data.getEnableCashMoney();
        StringUtils.setColorFulText(tv_can_withdraw_money, String.format("可提现%s元", enable_cash_money), enable_cash_money, color);
        cash_fee = data.getCashFee();
        StringUtils.setColorFulText(tv_fee, String.format("手续费：%s元", cash_fee), cash_fee, color);
        tv_fee_tip.setText(String.format("(提现手续费%s元/笔，由支付通道收取)", cash_fee));
        String desc = data.getDesc().replace("\\n", "\n\n");
        tv_desc.setText(desc);
    }

    @Override
    public void doNext(String money) {
        WithdrawConfirmActivity.start(this, money);
    }

    private void setFeeView(String money) {
        int color = getResources().getColor(R.color.text_orange_ff8400);
        if (TextUtils.isEmpty(money)) {
            StringUtils.setColorFulText(tv_fee, String.format("手续费：%s元", cash_fee), cash_fee, color);
            return;
        }
        double money_d = StringUtils.parseDouble(money);
        double cash_fee_d = StringUtils.parseDouble(cash_fee);
        if (money_d <= cash_fee_d) {
            StringUtils.setColorFulText(tv_fee, String.format("手续费：%s元", cash_fee), cash_fee, color);
            setSureTextViewEnabled(false);
            return;
        }
        String real_cash = new DecimalFormat("0.00").format(money_d - cash_fee_d);
        StringUtils.setColorFulTexts(tv_fee, color, String.format("手续费：%s元，实际到账%s元", cash_fee, real_cash), cash_fee, real_cash);
        setSureTextViewEnabled(true);
    }

    private void setSureTextViewEnabled(boolean isEnabled) {
        if (isEnabled) {
            if (!tv_sure.isEnabled()) {
                tv_sure.setEnabled(true);
            }
            return;
        }
        if (tv_sure.isEnabled()) {
            tv_sure.setEnabled(false);
        }
    }

    /**
     * 描述   ：金额输入过滤器，限制小数点后输入位数
     * <p>
     * 默认限制小数点2位
     * 默认第一位输入小数点时，转换为0.
     * 如果起始位置为0,且第二位跟的不是".",则无法后续输入
     * <p>
     * 作者   ：Created by DuanRui on 2017/9/28.
     */

    public class MoneyValueFilter extends DigitsKeyListener {

        private static final String TAG = "MoneyValueFilter";
        private int digits = 2;

        public MoneyValueFilter() {
            super(false, true);
        }

        public MoneyValueFilter setDigits(int d) {
            digits = d;
            return this;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置

            // if changed, replace the source
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int len = end - start;

            // if deleting, source is empty
            // and deleting can't break anything
            if (len == 0) {
                return source;
            }

            //以点开始的时候，自动在前面添加0
            if (source.toString().equals(".") && dstart == 0) {
                return "0.";
            }
            //如果起始位置为0,且第二位跟的不是".",则无法后续输入
            if (!source.toString().equals(".") && dest.toString().equals("0")) {
                return "";
            }
            int dlen = dest.length();
            // 限制在第一位输入0
            if (dlen >= 1 && source.toString().equals("0") && dstart == 0) {
                return "";
            }
            // 限制以"0."开头，在0与.中间插入0
            if (dest.toString().startsWith("0.") && dstart == 1) return "";

            // Find the position of the decimal .
            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.') {
                    // being here means, that a number has
                    // been inserted after the dot
                    // check if the amount of digits is right
                    return (dlen - (i + 1) + len > digits) ?
                            "" :
                            new SpannableStringBuilder(source, start, end);
                }
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    // being here means, dot has been inserted
                    // check if the amount of digits is right
                    if ((dlen - dend) + (end - (i + 1)) > digits)
                        return "";
                    else
                        break;  // return new SpannableStringBuilder(source, start, end);
                }
            }

            // 限制小数点前面的位数
            for (int i = 0; i < dlen; i++) {
                if (dest.charAt(i) == '.') {
                    // being here means, that a number has
                    // been inserted after the dot
                    // check if the amount of digits is right
                    return (i + len > DIGITS_INT) ?
                            "" :
                            new SpannableStringBuilder(source, start, end);
                }
            }

            // if the dot is after the inserted part,
            // nothing can break
            return new SpannableStringBuilder(source, start, end);
        }
    }
}
