package com.liyuu.strategy.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.di.component.DaggerFragmentComponent;
import com.liyuu.strategy.di.component.FragmentComponent;
import com.liyuu.strategy.di.module.FragmentModule;
import com.liyuu.strategy.ui.view.LoadingDialog;
import com.liyuu.strategy.util.ToastUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * MVP Fragment基类
 */
public abstract class BaseFragment<T extends BasePresenter> extends SimpleFragment implements BaseView, RxBus.OnEventListener {

    @Inject
    protected T mPresenter;
    protected boolean isFragmentCanShow = false;//是否对用户可见
    private LoadingDialog loadingDialog;
    private Disposable mDisposable;

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        mDisposable = RxBus.get().subscribe(this);
        mPresenter.attachView(this);
        loadingDialog = new LoadingDialog(getActivity());
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        RxBus.get().release(mDisposable);
        super.onDestroyView();
    }

    @Override
    public void onEventAccept(int code, Object object) {

    }

    protected TextView getTextView(int id) {
        return ButterKnife.findById(getActivity(), id);
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), msg);
        ToastUtil.showMsg(msg);
    }

    @Override
    public void showLoading() {
        if (loadingDialog != null)
            loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public void finishUI() {
        if (getActivity() != null)
            getActivity().finish();
    }

    protected abstract void initInject();

    @Override
    public void initUI() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        } else if (!isVisibleToUser) {
            onPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            isFragmentCanShow = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint())
            isFragmentCanShow = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint())
            isFragmentCanShow = false;
    }
}