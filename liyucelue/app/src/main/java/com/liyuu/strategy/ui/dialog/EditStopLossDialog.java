package com.liyuu.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.model.bean.PositionDetailBean;
import com.liyuu.strategy.ui.transaction.activity.BindCardActivity;
import com.liyuu.strategy.ui.transaction.activity.RechargeActivity;
import com.liyuu.strategy.util.CalculationUtil;
import com.liyuu.strategy.util.StringUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.util.UserInfoUtil;
import com.liyuu.strategy.widget.edittext.MoneyTextWatcher;

import java.lang.ref.WeakReference;
import java.math.RoundingMode;

import butterknife.ButterKnife;

/**
 * 修改止损价弹窗
 */
public class EditStopLossDialog extends BaseDialog implements View.OnClickListener {
    private final static String POSITION_DETAIL_BEAN = "position_detail_bean";
    private final static int HANDLER_EDITTEXT_CHANGE = 1001;//用户手动填入亏损值
    private double realBuyPrice, lastLossPrice, totalDeposit, maxStopRatio;
    private String zeroPriceString;
    private View view;
    private ImageView imgAdd, imgMinus;
    private TextView tvSure;
    private TextView tvNowCalLossMoney, tvNowCalLossPrecent;
    private EditText edtStopLossPrice;
    private PositionDetailBean bean;
    private double nowStopLoss = 0.00f;
    private double maxStopLossPrice = 0.00f, minStopLossPrice = 0.00f;//股票最高/最低可设置的止损价
    private double userBalance = 0.00f, addMarginCall = 0.00f;//用户余额/用户修改止损价所需花费
    private OnChangeStockStopLossPrice onChangeStockStopLossPrice;
    private PHandler handler = new PHandler();

    public static EditStopLossDialog newInstance(PositionDetailBean bean) {
        EditStopLossDialog dialog = new EditStopLossDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(POSITION_DETAIL_BEAN, bean);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setBean(PositionDetailBean bean) {
        getArguments().putSerializable(POSITION_DETAIL_BEAN, bean);
    }

    public void setOnChangeStockStopLossPrice(OnChangeStockStopLossPrice onChangeStockStopLossPrice) {
        this.onChangeStockStopLossPrice = onChangeStockStopLossPrice;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (handler != null) {
            handler.setEditStopLossDialog(this);
        }

        zeroPriceString = getContext().getResources().getString(R.string.price_zero_string_value);

        view = inflater.inflate(R.layout.dialog_edit_stop_loss, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);
        tvSure = view.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        bean = (PositionDetailBean) getArguments().getSerializable(POSITION_DETAIL_BEAN);

        if (bean != null) {
            initView();
        }
        return view;
    }

    private void initView() throws NumberFormatException {
        nowStopLoss = StringUtils.parseDouble(bean.getLastLossPrice());
//        maxStopLossPrice = (1 - bean.getMinStopLoss() / 100.f) * bean.getRealBuyPrice();
        maxStopLossPrice = CalculationUtil.round(StringUtils.parseDouble(bean.getMaxStopLossPrice()), 2, RoundingMode.FLOOR);
        getTextView(view, R.id.tv_now_price).setText(String.valueOf(bean.getCurrentPrice()));

        edtStopLossPrice = view.findViewById(R.id.edt_stop_loss_price);
        String edtStopLossPriceString = bean.getLastLossPrice();
        edtStopLossPrice.setText(edtStopLossPriceString);
        if (!TextUtils.isEmpty(edtStopLossPriceString))
            edtStopLossPrice.setSelection(edtStopLossPriceString.length());

        imgAdd = (ImageView) getView(view, R.id.iv_add);
        imgAdd.setOnClickListener(this);
        imgMinus = (ImageView) getView(view, R.id.iv_minus);
        imgMinus.setOnClickListener(this);
        changeAddMinusView(nowStopLoss);

        tvNowCalLossMoney = view.findViewById(R.id.tv_now_cal_loss_money);
        tvNowCalLossPrecent = view.findViewById(R.id.tv_now_cal_loss_precent);

        lastLossPrice = StringUtils.parseDouble(bean.getLastLossPrice());
        realBuyPrice = StringUtils.parseDouble(bean.getRealBuyPrice());
        userBalance = StringUtils.parseDouble(bean.getBalanceMoney());
        totalDeposit = StringUtils.parseDouble(bean.getTotalDeposit());
        maxStopRatio = StringUtils.parseDouble(bean.getMaxStopRatio());
        getTextView(view, R.id.tv_user_balance).setText(String.format("账户余额：￥%s", bean.getBalanceMoney()));
        float precent = (float) ((realBuyPrice - lastLossPrice) * 100.f / (realBuyPrice));

        tvNowCalLossMoney.setText(
                CalculationUtil.roundRuturnString(
                        totalDeposit * maxStopRatio / 100.f, 2));
        tvNowCalLossPrecent.setText(CalculationUtil.roundRuturnString(precent, 2));

        edtStopLossPrice.addTextChangedListener(new MoneyTextWatcher(edtStopLossPrice) {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(HANDLER_EDITTEXT_CHANGE);
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) throws NumberFormatException {
                super.afterTextChanged(s);

                if (handler == null || getContext() == null) {
                    return;
                }

                if (TextUtils.isEmpty(s.toString()) && getContext() != null) {
                    handler.removeMessages(HANDLER_EDITTEXT_CHANGE);
//                    editText.setText(zeroPriceString);
                    return;
                }

                if (zeroPriceString.equals(s.toString())) {
                    return;
                }

                String text = String.valueOf(CalculationUtil.round(nowStopLoss, 2));
                if (!TextUtils.isEmpty(text) && text.equals(edtStopLossPrice.getText().toString()))
                    return;

                String edtTextString = s.toString();
                if (TextUtils.isEmpty(edtTextString)) {
                    handler.removeMessages(HANDLER_EDITTEXT_CHANGE);
                    return;
                }

                double nowStopLoss_ = StringUtils.parseDouble(edtTextString);
                nowStopLoss = nowStopLoss_;
                if (nowStopLoss_ > maxStopLossPrice) {
                    nowStopLoss = maxStopLossPrice;
                    edtStopLossPrice
                            .setText(CalculationUtil.roundRuturnString(maxStopLossPrice, 2));
                    edtStopLossPrice
                            .setSelection(edtStopLossPrice.getText().length());
                    changeMarginCall();
                    return;
                }
                //800毫秒未再次输入文字判定为用户输入结束
                handler.removeMessages(HANDLER_EDITTEXT_CHANGE);
                handler.sendEmptyMessageDelayed(HANDLER_EDITTEXT_CHANGE, 800);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                dismiss();
                break;
            case R.id.tv_sure:
                //余额不足跳转至充值界面
                if (addMarginCall > userBalance && getContext() != null) {
                    if (!UserInfoUtil.hasBindCard()) {
                        //用户未绑定银行卡首先进行绑卡
                        BindCardActivity.start(getActivity());
                        dismiss();
                        break;
                    }
                    RechargeActivity.start(getContext());
                    dismiss();
                    break;
                }

                if (baseImpl != null)
                    baseImpl.dialogSure(EditStopLossDialog.class.getSimpleName(), this);

                if (TextUtils.isEmpty(edtStopLossPrice.getText().toString())) {
                    ToastUtil.showMsg("请输入金额");
                    break;
                }

                if (onChangeStockStopLossPrice != null)
                    onChangeStockStopLossPrice.changeStockStopLossPrice(CalculationUtil.round(nowStopLoss, 2));
                break;
            case R.id.iv_add:
                if (bean == null) {
                    return;
                }

                double newStopLoss = CalculationUtil.add(nowStopLoss, 0.01f);
                if (newStopLoss <= (1 - StringUtils.parseDouble(bean.getMinStopLoss()) / 100.f) * StringUtils.parseDouble(bean.getRealBuyPrice())) {
                    edtStopLossPrice.setText(CalculationUtil.roundRuturnString(newStopLoss, 2));
                    changeMarginCall(newStopLoss);
                }


                break;
            case R.id.iv_minus:
                if (bean == null) {
                    return;
                }

                newStopLoss = CalculationUtil.sub(nowStopLoss, 0.01f);
                if (newStopLoss >= 0) {
                    edtStopLossPrice.setText(CalculationUtil.roundRuturnString(newStopLoss, 2));
                    changeMarginCall(newStopLoss);
                }
                break;
        }
    }

    public void changeMarginCall() {
        try {
            String edtString = edtStopLossPrice.getText().toString();
            if (TextUtils.isEmpty(edtString))
                return;
            double newStopLoss = StringUtils.parseDouble(edtString);
            changeMarginCall(newStopLoss);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 止损线变化相应数据显示改变
     */
    public void changeMarginCall(double newStopLoss) {
        double changeStockStopLossPrice = realBuyPrice - newStopLoss;
        addMarginCall = (changeStockStopLossPrice * StringUtils.parseInt(bean.getRealTradeNum())
                - totalDeposit * maxStopRatio * 0.01f) / (maxStopRatio / 100.f);

        if (addMarginCall < 0 || newStopLoss == lastLossPrice)
            addMarginCall = 0;

        double newTradeMoney = CalculationUtil.round((addMarginCall + totalDeposit) * maxStopRatio * 0.01f, 2);

        double precent = changeStockStopLossPrice * 100.f / realBuyPrice;

        getTextView(view, R.id.tv_margin_call).setText(String.format("%s 元", CalculationUtil.roundRuturnString(addMarginCall, 2)));
        nowStopLoss = newStopLoss;
        edtStopLossPrice.setText(CalculationUtil.roundRuturnString(nowStopLoss, 2));

        tvNowCalLossMoney.setText(
                CalculationUtil.roundRuturnString(newTradeMoney, 2));
        tvNowCalLossPrecent.setText(CalculationUtil.roundRuturnString(precent, 2));

        edtStopLossPrice.setSelection(edtStopLossPrice.getText().length());

        changeAddMinusView(nowStopLoss);
        sureButtonChange();
    }

    /**
     * 确认按钮功能
     * 用户余额足够时，可直接修改止损线（按钮文字：确定）
     * 用户余额不足时，点击跳转至充值界面（按钮文字：去充值）
     */
    private void sureButtonChange() {
        if (getContext() == null)
            return;
        if (addMarginCall > userBalance) {
            tvSure.setText(getContext().getResources().getString(R.string.go_to_payment));
        } else {
            tvSure.setText(getContext().getResources().getString(R.string.sure));
        }
    }

    /**
     * 增加/减少按钮，在当前价格到达相应的极限值时置灰
     */
    private void changeAddMinusView(double nowStopLoss) {
        nowStopLoss = CalculationUtil.round(nowStopLoss, 2);
        boolean isCanAdd = nowStopLoss < maxStopLossPrice && !(nowStopLoss == maxStopLossPrice);
        imgAdd.setImageResource(isCanAdd ? R.mipmap.icon_add : R.mipmap.icon_not_add);


        boolean isCanReduce = nowStopLoss > minStopLossPrice && !(nowStopLoss == minStopLossPrice);
        imgMinus.setImageResource(isCanReduce ? R.mipmap.icon_minus : R.mipmap.icon_not_minus);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (view != null && bean != null)
            edtStopLossPrice.setText(bean.getLastLossPrice());
    }

    public interface OnChangeStockStopLossPrice {
        void changeStockStopLossPrice(double changeStopLossPrice);
    }

    private static class PHandler extends Handler {
        WeakReference<EditStopLossDialog> editStopLossDialogWeakReference;

        void setEditStopLossDialog(EditStopLossDialog dialog) {
            this.editStopLossDialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_EDITTEXT_CHANGE:
                    EditStopLossDialog dialog = editStopLossDialogWeakReference.get();
                    dialog.changeMarginCall();
                    break;
            }
        }
    }
}
