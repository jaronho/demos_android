package com.gsclub.strategy.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.AndroidWorkaround;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.di.component.ActivityComponent;
import com.gsclub.strategy.di.component.DaggerActivityComponent;
import com.gsclub.strategy.di.module.ActivityModule;
import com.gsclub.strategy.ui.view.LoadingDialog;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.status.StatusHelper;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView, RxBus.OnEventListener {

    @Inject
    protected T mPresenter;
    protected boolean isUserCanSee = false;//用户是否可见
    private LoadingDialog loadingDialog;
    private Disposable mDisposable;

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        mDisposable = RxBus.get().subscribe(this);
        initStatusBar();
        initInject();
        initUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        if (mPresenter != null)
            mPresenter.attachView(this);
        initToolBar();
        loadingDialog = new LoadingDialog(this);
    }

    protected void initStatusBar() {
        //状态栏适配
        StatusHelper.statusBarLightMode(this);
    }

    @Override
    public void initUI() {
        setStatusTransparent(this, false, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserCanSee = true;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUserCanSee = false;
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        RxBus.get().release(mDisposable);
        super.onDestroy();
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

    protected TextView getTextView(int id) {
        return ButterKnife.findById(this, id);
    }

    @Override
    public void finishUI() {
        hideLoading();
        this.finish();
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
        ToastUtil.showMsg(msg);
    }

    @Override
    public void onEventAccept(int code, Object object) {

    }

    final void initToolBar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar 一直为null
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolbarBack();
                }
            });
        }
    }

    protected void toolbarBack() {
        finish();
    }

    protected final Toolbar getToolbar() {
        return ButterKnife.findById(this, R.id.tool_bar);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleView = ButterKnife.findById(this, R.id.title);
        titleView.setText(title);
    }

    protected abstract void initInject();

    /**
     * 通过设置全屏，设置状态栏透明 导航栏白色
     */
    public void setStatusTransparent(Activity activity, boolean isFullScreen, boolean isNeedUpStateBar) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
//                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                int set = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                if (isFullScreen)
                    set |= (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    set |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                window.getDecorView().setSystemUiVisibility(set);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                int color = (isFullScreen) ? Color.TRANSPARENT : Color.WHITE;
                window.setStatusBarColor(color);
                window.setNavigationBarColor(color);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }

            if (AndroidWorkaround.checkDeviceHasNavigationBar(this))
                AndroidWorkaround.assistActivity(findViewById(android.R.id.content), this, isNeedUpStateBar);
        }
    }

    public void setStatusTransparentFull(Activity activity, boolean isFullScreen, boolean isNeedUpStateBar) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
//                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                int set = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                if (isFullScreen)
                    set |= (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    set |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                window.getDecorView().setSystemUiVisibility(set);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                int color = Color.WHITE;
                window.setStatusBarColor(color);
                window.setNavigationBarColor(color);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }

            if (AndroidWorkaround.checkDeviceHasNavigationBar(this))
                AndroidWorkaround.assistActivity(findViewById(android.R.id.content), this, isNeedUpStateBar);
        }
    }
}