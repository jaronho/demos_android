package com.liyuu.strategy.ui.dialog;

public interface BaseDialogImpl {
    /**
     * 点击取消按钮/X按钮
     */
    void dialogCancle(String dialogName, BaseDialog dialog);

    /**
     * 点击确定按钮
     */
    void dialogSure(String dialogName, BaseDialog dialog);

}
