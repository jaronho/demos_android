package com.gsclub.strategy.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.Constants;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.base.SimpleFragment;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.main.MainContract;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.presenter.main.MainPresenter;
import com.gsclub.strategy.service.HeartBeatService;
import com.gsclub.strategy.tpush.MessageConstants;
import com.gsclub.strategy.ui.login.LoginActivity;
import com.gsclub.strategy.ui.mine.WebViewActivity;
import com.gsclub.strategy.ui.optional.OptionalFragment;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.util.UserInfoUtil;
import com.gsclub.strategy.util.ViewSimulationUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    public final static String GO_TO_TRANSACTION = "go_to_transaction";
    @BindView(R.id.r_btn_select)
    RadioButton rBtnSelect;
    @BindView(R.id.r_btn_transaction)
    RadioButton rBtnTransation;

    private int mSelectedId = R.id.r_btn_home;
    private int mLastSelectedId = R.id.r_btn_home;
    private long lastTime;
    private Intent heartBeatServiceIntent;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserInfoUtil.resetUserOperating(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(GO_TO_TRANSACTION, false)) {
            ViewSimulationUtil.viewOnclick(ButterKnife.findById(this, R.id.r_btn_transaction), this);
            if (intent.getExtras() == null) return;
            String actionType = intent.getExtras().getString(MessageConstants.KEY_PARAMS_ACTION_TYPE, "");
            int select = TransactionFragment.getSelectItem(actionType);
            PreferenceUtils.put(SPKeys.FILE_OTHER, SPKeys.OTHER_TRANSACTION_SELECT_INT, select);
            RxBus.get().send(RxBus.Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT, select);
        } else if (intent.getBooleanExtra(Constants.KEY_API_LOGIN, false)) {
            resetHome();
            LoginActivity.start(this);
        } else if (intent.getBooleanExtra(Constants.KEY_API_SINGLE_SIGN_ON, false)) {
            resetHome();
            String msg = intent.getStringExtra("msg");
            DialogActivity.start(DialogActivity.DIALOG_EXIT, msg);
        } else if(intent.getBooleanExtra("is_show_url", false)) {
            String url = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.JUMP_URL);
            if(TextUtils.isEmpty(url)) return;
            WebViewActivity.start(this, url);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        ButterKnife.findById(this, R.id.r_btn_home).setSelected(true);
        switchFragment(HomeFragment.class);

        if (CommonUtil.isReview()) {
            rBtnTransation.setVisibility(View.GONE);
            rBtnSelect.setVisibility(View.VISIBLE);
        } else {
            rBtnSelect.setVisibility(View.GONE);
            rBtnTransation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEventAndData() {
        heartBeatServiceIntent = new Intent(MainActivity.this, HeartBeatService.class);
        startService(heartBeatServiceIntent);
        mPresenter.getWebSets();
        mPresenter.initXGPush(this);
        mPresenter.checkUpdata();

        if(getIntent().getBooleanExtra("is_show_url", false)) {
            String url = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.JUMP_URL);
            if(TextUtils.isEmpty(url)) return;
            WebViewActivity.start(this, url);
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.r_btn_home, R.id.r_btn_transaction, R.id.r_btn_mine, R.id.r_btn_select})
    void onCLick(View view) {
        viewClick(view);
    }

    private void viewClick(View view) {
        switch (view.getId()) {
            case R.id.r_btn_home:
                setSelectedView(R.id.r_btn_home);
                switchFragment(HomeFragment.class);
                break;
            case R.id.r_btn_transaction:
                doSwitchTrading();
                break;
            case R.id.r_btn_mine:
                setSelectedView(R.id.r_btn_mine);
                switchFragment(MineFragment.class);
                break;
            case R.id.r_btn_select:
                setSelectedView(R.id.r_btn_select);
                switchFragment(OptionalFragment.class);
                break;
        }
    }

    private void setSelectedView(int btnId) {
        mSelectedId = btnId;
        if (mSelectedId == mLastSelectedId) return;
        ButterKnife.findById(this, mLastSelectedId).setSelected(false);
        ButterKnife.findById(this, mSelectedId).setSelected(true);
        mLastSelectedId = mSelectedId;
    }

    private void resetHome() {
        if (mLastSelectedId == R.id.r_btn_home) return;
        setSelectedView(R.id.r_btn_home);
        switchFragment(HomeFragment.class);
    }

    private void switchFragment(Class<? extends SimpleFragment> fragment) {
        RxBus.get().send(RxBus.Code.MAIN_ACTIVITY_SELECT_ITEM_WITH_FRAGMENT_NAME, fragment.getSimpleName());
        String tag = fragment.getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        SimpleFragment baseFragment = (SimpleFragment) manager.findFragmentByTag(tag);
        @SuppressLint("RestrictedApi") List<Fragment> list = manager.getFragments();
        if (list != null) {
            for (Fragment f : list) {
                if (f == null) continue;
                if (f == baseFragment) {
                    transaction.show(f);
                } else {
                    transaction.hide(f);
                }
            }
        }
        if (baseFragment == null) {
            try {
                baseFragment = fragment.newInstance();
                transaction.add(R.id.main_content, baseFragment, tag);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        transaction.commitAllowingStateLoss();
    }

    private void remove(Class<? extends SimpleFragment> fragment) {
        String tag = fragment.getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        @SuppressLint("CommitTransaction")
        FragmentTransaction transaction = manager.beginTransaction();
        SimpleFragment baseFragment = (SimpleFragment) manager.findFragmentByTag(tag);
        if (baseFragment != null) {
            transaction.remove(baseFragment);
        }
    }

    private void doSwitchTrading() {
        if (!UserInfoUtil.isLogin()) {
            setSelectedView(R.id.r_btn_transaction);
            switchFragment(TradingUnLogFragment.class);
            remove(TransactionFragment.class);
            return;
        }
        setSelectedView(R.id.r_btn_transaction);
        switchFragment(TransactionFragment.class);
        remove(TradingUnLogFragment.class);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_ACTIVITY_DIALOG_INFO:
                if (object == null) break;
                UserIndexBean.ActivityBean activityInfo = (UserIndexBean.ActivityBean) object;
                DialogActivity.start(DialogActivity.DIALOG_H5_ACTIVITY, activityInfo);
                break;
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
            case RxBus.Code.USER_LOGIN_OUT:
                if (mSelectedId == R.id.r_btn_transaction) {
                    doSwitchTrading();
                }
                break;
            case RxBus.Code.NEWS_WELFARE_BUYING_SUCCESS:
                if (mSelectedId == R.id.r_btn_transaction) {
                    return;
                }
                ViewSimulationUtil.viewOnclick(ButterKnife.findById(this, R.id.r_btn_transaction), this);
                PreferenceUtils.put(SPKeys.FILE_OTHER, SPKeys.OTHER_TRANSACTION_SELECT_INT, 1);
                RxBus.get().send(RxBus.Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT, 1);
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        long time = System.currentTimeMillis();
        if (time - lastTime > 2000) {
            ToastUtil.showMsg("再按一次返回键退出应用");
            lastTime = time;
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (heartBeatServiceIntent != null)
            stopService(heartBeatServiceIntent);
    }
}
