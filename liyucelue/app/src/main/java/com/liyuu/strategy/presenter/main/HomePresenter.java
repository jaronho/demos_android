package com.liyuu.strategy.presenter.main;

import android.content.Context;
import android.text.TextUtils;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.main.HomeContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.BannerBean;
import com.liyuu.strategy.model.bean.HomeMenuBean;
import com.liyuu.strategy.model.bean.HomePayMessageBean;
import com.liyuu.strategy.model.bean.MessageIndexBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.ui.mine.WebViewActivity;
import com.liyuu.strategy.ui.transaction.activity.NewsWelfareActivity;
import com.liyuu.strategy.util.CommonUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    HomePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
    }

    @Override
    public List<OptionalTopStockBean> getThreeStockPlateDefaultData() {
        List<OptionalTopStockBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OptionalTopStockBean bean = new OptionalTopStockBean();
            bean.setLastPx(Float.NaN);
            bean.setPxChange(Float.NaN);
            bean.setPxChangeRate(Float.NaN);
            String name = "";
            switch (i) {
                case 0:
                    name = "上证指数";
                    break;
                case 1:
                    name = "深证成指";
                    break;
                case 2:
                    name = "创业板指";
                    break;
            }
            bean.setStockName(name);
            list.add(bean);
        }
        return list;
    }

    @Override
    public void getThreeStockPlateData() {
        String url = UrlConfig.Stock_stockIndex;
        post(false, mDataManager.fetchTopStockInfo(url),
                new CommonSubscriber<List<OptionalTopStockBean>>(mView, url) {
                    @Override
                    public void onNext(List<OptionalTopStockBean> data) {
                        mView.setTopStocks(data);
                    }
                });
    }

    @Override
    public void getBanner() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", "slider");
        map.put("type", "1");
        String url = UrlConfig.index_slider;
        post(false, mDataManager.fetchBanner(url, map),
                new CommonSubscriber<List<BannerBean>>(mView, url) {
                    @Override
                    public void onNext(List<BannerBean> bannerBeans) {
                        super.onNext(bannerBeans);
                        mView.showBanner(bannerBeans);
                    }
                });
    }

    @Override
    public void getUserMessage() {
        String url = UrlConfig.User_announceIndex;
        post(false, mDataManager.fetchUserMessage(url),
                new CommonSubscriber<List<MessageIndexBean>>(mView, url) {
                    @Override
                    public void onNext(List<MessageIndexBean> messageIndexBeans) {
                        super.onNext(messageIndexBeans);
                        if (messageIndexBeans == null)
                            return;

                        int messageCount = 0;
                        for (MessageIndexBean bean : messageIndexBeans) {
                            messageCount += bean.getTypeUnread();
                        }

                        mView.showMessageHot(messageCount);
                    }
                });
    }

    @Override
    public void getUserPayMessage() {
        Map<String, Object> params = new HashMap<>();
        String url = UrlConfig.Index_lastbuylist;
        post(false, mDataManager.fetchUserPayMessage(url, params),
                new CommonSubscriber<List<HomePayMessageBean>>(mView, url) {
                    @Override
                    public void onNext(List<HomePayMessageBean> datas) {
                        super.onNext(datas);
                        mView.showUserPayMessage(datas);
                    }
                });
    }

    @Override
    public void getMenus() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("key", "INDEX_MENU");
        String url = UrlConfig.Index_indexMenu;
        post(mDataManager.fetchHomeMenuList(url, maps),
                new CommonSubscriber<HomeMenuBean>(mView, url) {
                    @Override
                    public void onNext(HomeMenuBean datas) {
                        super.onNext(datas);

                        //审核状态隐藏
                        boolean isShowHomeMenus = !CommonUtil.isReview();//核审状态不显示
                        mView.showHomeMenus(isShowHomeMenus, datas.getMenu());

                        boolean isShow =
                                datas.getIndexIntroduction() != null &&
                                        !TextUtils.isEmpty(datas.getIndexIntroduction().getImgurl());
                        if (!isShowHomeMenus)
                            isShow = false;

                        mView.showBottomIntroImage(isShow, isShow ? datas.getIndexIntroduction().getImgurl() : "");
                    }
                });
    }

    @Override
    public void bannerOnclick(Context context, BannerBean bean) {
        if (context == null)
            return;
        if (!TextUtils.isEmpty(bean.getAimUrl())) {
            WebViewActivity.start(context, bean.getAimUrl());
        } else if ("NOVICE_WELFARE".equals(bean.getEventKey())) {
            //新手福利活动
            NewsWelfareActivity.start(context);
        }
    }

    @Override
    public void checkStatus() {
        boolean isChecking = CommonUtil.isReview();
        mView.checkViewChange(isChecking);
    }
}