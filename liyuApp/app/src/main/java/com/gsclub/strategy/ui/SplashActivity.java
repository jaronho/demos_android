package com.gsclub.strategy.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.home.SplashContract;
import com.gsclub.strategy.presenter.transaction.SplashPresenter;
import com.gsclub.strategy.tpush.MyNotificationOpenedReceiver;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.SystemUtil;
import com.gsclub.strategy.util.UserInfoUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017-07-05.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.notify_msg)
    TextView mNotifyView;
    @BindView(R.id.tv_count_time)
    TextView mCountTime;
    @BindView(R.id.ad_logo)
    ImageView mAdLogo;

    private volatile boolean adFinish, loginFinish, isGo;
    private Disposable mCountTimeDisposable;
    private boolean isAdClicked;


    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initUI() {
        //修复小米手机按home键切换到桌面，重新点击app图标进入会重新启动app的bug（目前出现机型 小米max）
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        setStatusTransparent(this, true, false);
    }

    @Override
    protected void initEventAndData() {
        loadAd();
        mPresenter.activityControl();
        setImei();
        // TODO: 2018/10/12  暂无广告/直接跳转
        Observable.timer(1000, TimeUnit.MILLISECONDS) //等待1秒
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        adFinish = true;
                        goNext();
//                        showAd();
                    }
                });
    }

    @OnClick({R.id.tv_count_time, R.id.ad_logo})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_logo:
                adFinish = true;
                dispose();
                isAdClicked = true;
                goNext();
                break;
            case R.id.tv_count_time:
                adFinish = true;
                dispose();
                goNext();
                break;
        }
    }

    private void setImei() {
        String imei = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID);
        if (TextUtils.isEmpty(imei))
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID, SystemUtil.getUniquePsuedoID(this));
    }

    private void loadAd() {
        // 展示引导页的时候，不展示广告图
        int welcomeVersion = PreferenceUtils.getInt(SPKeys.FILE_COMMON, SPKeys.GUIDE_VERSION);
        if (welcomeVersion < GuideActivity.GUIDE_VERSION) {
            return;
        }

//        HttpClient.post(UrlPath.index_slider, new SilenceCallback<List<BannerBean>>(){
//            @Override
//            public void onResponse(List<BannerBean> response) {
//                if(response == null || response.size() == 0) {
//                    PreferenceUtils.cleanAd();
//                    return;
//                }
//                BannerBean data = response.get(0);
//                String title = data.getTitle();
//                String photo = data.getImgurl();
//                String url = data.getAim_url();
//                String aid = data.getAid();
//                String jump_type = data.getJump_type();
//                PreferenceUtils.putString(Keys.FILE_AD,
//                        Pair.create(Keys.AD_AID, aid),
//                        Pair.create(Keys.AD_TITLE, title),
//                        Pair.create(Keys.AD_PHOTO, photo),
//                        Pair.create(Keys.AD_URL, url),
//                        Pair.create(Keys.AD_JUMP_TYPE, jump_type));
//            }
//        }, new Pair<String, Object>("key", "START_PAGE"));
    }

    private void runLogin() {
        String open_id = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID);
        if (TextUtils.isEmpty(open_id)) {
            loginFinish();
        } else {
            mPresenter.loginByOpenId(open_id, PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER));
        }
    }

    private void showAd() {
//        final String ad = PreferenceUtils.getString(SPKeys.FILE_AD, SPKeys.AD_PHOTO);
//        if(TextUtils.isEmpty(ad)) {
//            adFinish = true;
//            goNext();
//        } else {
//            mCountTime.setText("3S 跳过");
//            mCountTime.setVisibility(View.VISIBLE);
//            mAdLogo.setVisibility(View.VISIBLE);
//
//            GlideUtils.loadAd(this, ad, mAdLogo);
//            mCountTimeDisposable =
//                    Observable.intervalRange(1, 3,1, 1, TimeUnit.SECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//                            int time = (int) (3 - aLong);
//                            if(time <= 0) {
//                                adFinish = true;
//                                goNext();
//                            } else {
//                                mCountTime.setText(String.format("%dS 跳过", time));
//                            }
//                        }
//                    });
//        }
    }

    private void goNext() {
        if (adFinish && loginFinish) {
            if (isGo) return;// 避免MainActivity跳转两次
            isGo = true;
//            int welcomeVersion = PreferenceUtils.getInt(SPKeys.FILE_COMMON, SPKeys.GUIDE_VERSION);
//            if (welcomeVersion < GuideActivity.GUIDE_VERSION) {
//                GuideActivity.start(this);
//                finish();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(!CommonUtil.isReview() && hasJumpUrl()) {
                    intent.putExtra("is_show_url", true);
                }
                startActivity(intent);
                finish();
                doPush();// 发条消息以执行通知被点击的动作
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
        }
    }

    @Override
    public void doActivityControl() {
        runLogin();
    }

    private void doPush() {
        boolean is_notification_clicked = PreferenceUtils.getBoolean(SPKeys.FILE_COMMON, SPKeys.COMMON_IS_NOTIFICATION_CLICKED);
        if (is_notification_clicked) {
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_IS_NOTIFICATION_CLICKED, false);
            sendBroadcast(new Intent(MyNotificationOpenedReceiver.ACTION_SPLASH_STARTED));
        }
    }

    private void dispose() {
        if (mCountTimeDisposable != null) {
            mCountTimeDisposable.dispose();
            mCountTimeDisposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void loginFinish() {
        loginFinish = true;
        goNext();
    }

    private boolean hasJumpUrl() {
        String jump_url = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.JUMP_URL);
        if(TextUtils.isEmpty(jump_url)) return false;
        return true;
    }
}
