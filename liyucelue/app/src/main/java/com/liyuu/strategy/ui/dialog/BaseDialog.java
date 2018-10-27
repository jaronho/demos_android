package com.liyuu.strategy.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.liyuu.strategy.component.RxBus;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class BaseDialog extends DialogFragment
        implements RxBus.OnEventListener, DialogInterface.OnKeyListener {

    protected float scale = 0.80f;

    protected boolean isCancelable = false;

    protected BaseDialogImpl baseImpl;

    private Disposable mDisposable;


    public void setBaseImpl(BaseDialogImpl baseImpl) {
        this.baseImpl = baseImpl;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setOnKeyListener(this);
        mDisposable = RxBus.get().subscribe(this);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int) ((dm.widthPixels * scale * 100.f) / 100);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        if (mDisposable != null)
            RxBus.get().release(mDisposable);
        super.onDestroyView();
    }

    public void show(FragmentManager manager) {
        show(manager, this.getTag());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            Fragment prev = manager.findFragmentByTag(tag);
            if (prev != null)
                manager.beginTransaction().remove(prev).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    protected TextView getTextView(View parent, int id) {
        return ButterKnife.findById(parent, id);
    }

    protected EditText getEdittext(View parent, int id) {
        return ButterKnife.findById(parent, id);
    }

    protected View getView(View parent, int id) {
        return ButterKnife.findById(parent, id);
    }

    @Override
    public void onEventAccept(int code, Object object) {

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }
    }
}
