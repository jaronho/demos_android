package com.liyuu.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.util.CalculationUtil;

/**
 * 出售股票弹窗（需要时时刷新股票价格）
 */
public class SellStockDialog extends BaseDialog implements View.OnClickListener {
    private static final String STOCK_NUMBER_STRING = "stock_number_string";
    private static final String STOCK_USER_BUY_PRICE_DOUBLE = "stock_user_buy_price_double";
    private static final String STOCK_LAST_PRICE_DOUBLE = "stock_last_price_double";
    private static final String STOCK_OID_STRING = "stock_oid_string";
    private static final String STOCK_SYMBOL_STRING = "stock_symbol_string";

    private SellStockDialogImpl dialogImpl;
    private TextView tvStockNumber, tvStockLastPrice;
    private ImageView imgRefresh;
    private RotateAnimation rotateAnimation;

    public static SellStockDialog newInstance(String oid, String symbol, String stockNumber, double userPayPrice, double lastPrice) {
        SellStockDialog sellStockDialog = new SellStockDialog();
        Bundle bundle = new Bundle();
        bundle.putString(STOCK_OID_STRING, oid);
        bundle.putString(STOCK_SYMBOL_STRING, symbol);
        bundle.putString(STOCK_NUMBER_STRING, stockNumber);
        bundle.putDouble(STOCK_LAST_PRICE_DOUBLE, lastPrice);
        bundle.putDouble(STOCK_USER_BUY_PRICE_DOUBLE, userPayPrice);
        sellStockDialog.setArguments(bundle);
        return sellStockDialog;
    }

    public void setDialogImpl(SellStockDialogImpl dialogImpl) {
        this.dialogImpl = null;
        this.dialogImpl = dialogImpl;
    }

    public SellStockDialog setOid(String oid) {
        if (getArguments() == null)
            return this;
        getArguments().putString(STOCK_OID_STRING, oid);
        return this;
    }

    public String getOid() {
        if (getArguments() == null)
            return "";
        return getArguments().getString(STOCK_OID_STRING, "");
    }

    public SellStockDialog setSymbol(String symbol) {
        if (getArguments() == null)
            return this;
        getArguments().putString(STOCK_SYMBOL_STRING, symbol);
        return this;
    }

    public String getSymbol() {
        if (getArguments() == null)
            return "";
        return getArguments().getString(STOCK_SYMBOL_STRING, "");
    }

    public SellStockDialog setStockNumber(String stockNumber) {
        if (getArguments() == null)
            return this;
        getArguments().putString(STOCK_NUMBER_STRING, stockNumber);
        return this;
    }

    public SellStockDialog setUserBuyPrice(double userBuyPrice) {
        if (getArguments() == null)
            return this;
        getArguments().putDouble(STOCK_USER_BUY_PRICE_DOUBLE, userBuyPrice);
        return this;
    }

    public SellStockDialog setStockLastPrice(double lastPrice) {
        if (getArguments() == null)
            return this;
        getArguments().putDouble(STOCK_LAST_PRICE_DOUBLE, lastPrice);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_sell_stock, container);
        tvStockNumber = view.findViewById(R.id.tv_stock_number);
        tvStockLastPrice = view.findViewById(R.id.tv_stock_last_price);
        imgRefresh = view.findViewById(R.id.img_refresh);
        imgRefresh.setOnClickListener(this);
        view.findViewById(R.id.v_close).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_sure).setOnClickListener(this);
        showView();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                dismiss();
                break;
            case R.id.img_refresh:
                refreshStockLastPrice();
                break;
            case R.id.tv_sure:
                if (baseImpl != null)
                    baseImpl.dialogSure(SellStockDialog.class.getSimpleName(), this);
                break;
            case R.id.tv_cancel:
                if (baseImpl != null)
                    baseImpl.dialogCancle(SellStockDialog.class.getSimpleName(), this);
                dismiss();
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                refreshStockLastPrice();
                break;
        }
    }

    public void showView() {
        if (getArguments() != null) {
            tvStockNumber.setText(String.format("%s股", getArguments().getString(STOCK_NUMBER_STRING)));
            double lastPrice = getArguments().getDouble(STOCK_LAST_PRICE_DOUBLE);
            double userPayPrice = getArguments().getDouble(STOCK_USER_BUY_PRICE_DOUBLE);
            int color = getContext().getResources().getColor(R.color.text_grey_666666);//默认灰色
            if (lastPrice > userPayPrice) {
                color = getContext().getResources().getColor(R.color.stock_red_color);
            } else if (lastPrice < userPayPrice) {
                color = getContext().getResources().getColor(R.color.stock_green_color);
            }
            tvStockLastPrice.setTextColor(color);
            tvStockLastPrice.setText(CalculationUtil.roundRuturnString(lastPrice, 2));
        }
    }

    /**
     * 刷新股票最新价格
     */
    private void refreshStockLastPrice() {
        if (rotateAnimation == null) {
            rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            imgRefresh.setAnimation(rotateAnimation);
            rotateAnimation.setDuration(1200);
        }

        if (!isHidden() && dialogImpl != null) {
            if (rotateAnimation != null && rotateAnimation.isInitialized() && rotateAnimation.hasEnded())
                imgRefresh.startAnimation(rotateAnimation);
            dialogImpl.sellStockDialogRefreshStockLastPrice(getSymbol());
        }
    }

    public interface SellStockDialogImpl {
        void sellStockDialogRefreshStockLastPrice(String symbol);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            rotateAnimation = null;
        }
    }
}
