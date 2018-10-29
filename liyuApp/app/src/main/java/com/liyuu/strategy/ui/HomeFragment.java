package com.liyuu.strategy.ui;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.ScreenCode;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.main.HomeContract;
import com.liyuu.strategy.model.bean.BannerBean;
import com.liyuu.strategy.model.bean.HomeMenuBean;
import com.liyuu.strategy.model.bean.HomePayMessageBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.presenter.main.HomePresenter;
import com.liyuu.strategy.ui.home.activity.IncomeActivity;
import com.liyuu.strategy.ui.home.activity.SimulatedTradingActivity;
import com.liyuu.strategy.ui.home.adapter.StockTopAdapter;
import com.liyuu.strategy.ui.mine.WebViewActivity;
import com.liyuu.strategy.ui.mine.activity.MessageActivity;
import com.liyuu.strategy.ui.stock.activity.SearchStockActivity;
import com.liyuu.strategy.ui.view.banner.Banner;
import com.liyuu.strategy.ui.view.banner.BannerAdapter;
import com.liyuu.strategy.util.ScreenUtil;
import com.liyuu.strategy.util.UserInfoUtil;
import com.liyuu.strategy.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomePresenter>
        implements HomeContract.View, ViewSwitcher.ViewFactory, OnRefreshListener {
    @BindView(R.id.rcv_stock_top)
    RecyclerView rcvStockTop;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.img_notify_hot)
    ImageView imgNotifyHot;
    @BindView(R.id.ts_pay_message)
    TextSwitcher textSwitcher;
    @BindView(R.id.ll_home_menu)
    LinearLayout llHomeMenu;
    @BindView(R.id.srl_content)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.cl_content)
    ConstraintLayout constraintLayout;
    @BindView(R.id.tv_little_title_one)
    TextView tvLittleTitleOne;
    @BindView(R.id.tv_go_to_trade)
    TextView tvGoToTrade;
    @BindView(R.id.img_intro)
    ImageView imgIntro;


    private StockTopAdapter topAdapter;
    private List<HomePayMessageBean> listPayMessage;
    private int numberCount = 0;
    private CountDownTimer countDownTimer = new CountDownTimer(60000, 3000) {
        @SuppressLint("DefaultLocale")
        @Override
        public void onTick(long millisUntilFinished) {
            if (listPayMessage == null || listPayMessage.size() == 0)
                return;

            if (textSwitcher.getNextView() instanceof TextView) {
                TextView tv = (TextView) textSwitcher.getNextView();
                tv.setLineSpacing(4.f, 1.0f);
                tv.setMaxLines(3);
            }

            int position = numberCount % listPayMessage.size();
            textSwitcher.setText(Html.fromHtml(listPayMessage.get(position).getDesc()));
            getTextView(R.id.tv_pay_nickname).setText(listPayMessage.get(position).getNickname());
            getTextView(R.id.tv_pay_time).setText(listPayMessage.get(position).getBuy_time());
            CircleImageView img = ButterKnife.findById(getActivity(), R.id.img_pay_header);
            ImageLoader.loadHead(App.getInstance(), listPayMessage.get(position).getHeadimg(), img);
            numberCount++;
        }

        @Override
        public void onFinish() {
            if (countDownTimer != null)
                countDownTimer.start();
        }
    };

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @OnClick({R.id.tv_search, R.id.rl_notify, R.id.tv_go_to_trade})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                SearchStockActivity.start(getActivity());
                break;
            case R.id.rl_notify:
                MessageActivity.start(getActivity());
                break;
            case R.id.tv_go_to_trade:
                SearchStockActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        refreshLayout.setOnRefreshListener(this);
        rcvStockTop.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rcvStockTop.setHasFixedSize(true);
        rcvStockTop.setNestedScrollingEnabled(false);
        topAdapter = new StockTopAdapter(getActivity());
        topAdapter.setData(mPresenter.getThreeStockPlateDefaultData());
        topAdapter.notifyDataSetChanged();
        rcvStockTop.setAdapter(topAdapter);

    }

    @Override
    protected void initEventAndData() {
        mPresenter.checkStatus();
        mPresenter.getMenus();
        mPresenter.getBanner();
        mPresenter.getUserMessage();
        mPresenter.getUserPayMessage();
        mPresenter.getThreeStockPlateData();
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.MAIN_ACTIVITY_SELECT_ITEM_WITH_FRAGMENT_NAME:
                mPresenter.getUserMessage();
                break;
            case RxBus.Code.HEART_BREAK:
                if (!isFragmentCanShow)
                    return;
                mPresenter.getThreeStockPlateData();
                break;
            case RxBus.Code.MINUTE_HEART_BREAK:
                mPresenter.getUserPayMessage();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    @Override
    public void setTopStocks(List<OptionalTopStockBean> list) {
        topAdapter.getData().clear();
        topAdapter.setData(list);
        topAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBanner(final List<BannerBean> banners) {

        if (banners == null || banners.size() == 0) {
            banner.setVisibility(View.GONE);
            return;
        }

        banner.setVisibility(View.VISIBLE);

        BannerAdapter adapter = new BannerAdapter<BannerBean>(banners) {
            @Override
            protected void bindTips(TextView tv, BannerBean bannerModel) {
                tv.setText(bannerModel.getTitle());
            }

            @Override
            public void bindImage(ImageView imageView, BannerBean bannerModel) {
                ImageLoader.loadBanner(App.getInstance(), bannerModel.getImgUrl(), 10, imageView);
            }
        };

        banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.bannerOnclick(getActivity(), banners.get(position));
            }
        });
        banner.getLayoutParams().height = (int) ((ScreenUtil.getScreen(getActivity(), ScreenCode.WIDTH)-ScreenUtil.dp2px(getActivity(), 30)) *3/7);
        banner.setBannerAdapter(adapter);
        banner.notifyDataHasChanged();
    }

    @Override
    public void showMessageHot(int messageCount) {
        imgNotifyHot.setVisibility(messageCount <= 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showUserPayMessage(List<HomePayMessageBean> datas) {
        if (datas == null || datas.size() == 0) {
            constraintLayout.setVisibility(View.GONE);
            return;
        }
        constraintLayout.setVisibility(View.VISIBLE);
        numberCount = 0;
        listPayMessage = datas;
        textSwitcher.setFactory(this);
        textSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.tv_fade_in));
        textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.tv_fade_out));
        countDownTimer.start();
    }

    @Override
    public void showBottomIntroImage(boolean isShow, String imgUrl) {
        imgIntro.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (isShow)
            ImageLoader.load(App.getInstance(), imgUrl, imgIntro);
    }

    @Override
    public void showHomeMenus(boolean isShow, List<HomeMenuBean.MenuBean> homeMenuBeans) {
        if (!isShow) {
            llHomeMenu.setVisibility(View.GONE);
            return;
        }

        llHomeMenu.setVisibility(View.VISIBLE);

        if (homeMenuBeans == null || homeMenuBeans.size() == 0)
            return;
        llHomeMenu.removeAllViews();
        int width = ScreenUtil.getScreen(getActivity(), ScreenCode.WIDTH) / homeMenuBeans.size();

        for (HomeMenuBean.MenuBean bean :
                homeMenuBeans) {
            ConstraintLayout view =
                    (ConstraintLayout) LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_home_menu_textview, llHomeMenu, false);
            TextView tv = view.findViewById(R.id.tv_home_menu);
            ImageView img = view.findViewById(R.id.img_home_menu);

            tv.setText(bean.getTitle());
            ImageLoader.load(App.getInstance(), bean.getImgUrl(), img);
            llHomeMenu.addView(view);
            view.getLayoutParams().width = width;
            setHomeMenuClick(view, bean);
        }
    }

    @Override
    public void checkViewChange(boolean isChecking) {
        tvLittleTitleOne.setText(getResources().getString(isChecking ? R.string.dynamic_information : R.string.all_buy));
        tvGoToTrade.setVisibility(isChecking ? View.GONE : View.VISIBLE);
    }

    private void setHomeMenuClick(View view, final HomeMenuBean.MenuBean bean) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bean.getType()) {
                    case 1:
                        //收益榜
                        IncomeActivity.start(getActivity());
                        break;
                    case 2:
                        //模拟交易
                        if (!UserInfoUtil.isWithLogin(getActivity())) return;
                        SimulatedTradingActivity.start(getActivity());
                        break;
                    case 3:
                        //新手教程
                        WebViewActivity.start(getActivity(), bean.getAimUrl());
                        break;
                    case 4:
                        //邀请分佣
                        if (!UserInfoUtil.isWithLogin(getActivity())) return;
                        WebViewActivity.start(getActivity(), bean.getAimUrl());
                        break;
                }
            }
        });
    }

    @Override
    public View makeView() {
        TextView tv = new TextView(getActivity());
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        return tv;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getBanner();
        mPresenter.getThreeStockPlateData();
        mPresenter.getUserMessage();
        refreshLayout.finishRefresh(800);
    }
}
