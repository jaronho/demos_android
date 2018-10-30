package com.gsclub.strategy.contract.main;

import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.BannerBean;
import com.gsclub.strategy.model.bean.HomeMenuBean;
import com.gsclub.strategy.model.bean.HomePayMessageBean;
import com.gsclub.strategy.model.bean.OptionalTopStockBean;

import java.util.List;

public interface HomeContract {
    interface View extends BaseView {
        void setTopStocks(List<OptionalTopStockBean> list);

        void showBanner(List<BannerBean> banners);

        void showMessageHot(int messageCount);

        void showUserPayMessage(List<HomePayMessageBean> datas);

        void showHomeMenus(boolean isShow, List<HomeMenuBean.MenuBean> homeMenuBeans);

        void showBottomIntroImage(boolean isShow, String imgUrl);

        void checkViewChange(boolean isChecking);
    }

    interface Presenter extends BasePresenter<View> {
        //获取生成的顶部三大板块空数据
        List<OptionalTopStockBean> getThreeStockPlateDefaultData();

        //获取顶部三大板块数据
        void getThreeStockPlateData();

        void getBanner();

        void getUserMessage();

        //获取大家都在买的数据列表
        void getUserPayMessage();

        //获取首页菜单栏
        void getMenus();

        void bannerOnclick(Context context, BannerBean bean);

        //验证是否为核审状态
        void checkStatus();
    }
}
