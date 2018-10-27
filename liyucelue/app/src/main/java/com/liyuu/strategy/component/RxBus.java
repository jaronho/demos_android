package com.liyuu.strategy.component;

import android.support.annotation.IntDef;

import com.liyuu.strategy.util.RxUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * rxbus
 */
public class RxBus {
    // 主题
    private final FlowableProcessor<Object> bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者
    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus get() {
        return RxBusHolder.sInstance;
    }

    // 提供了一个新的事件
    public void send(@Code int code) {
        send(code, null);
    }

    public void send(@Code int code, Object obj) {
        bus.onNext(new EventType(code, obj));
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    // 封装默认订阅
    public <T> Disposable toDefaultFlowable(Class<T> eventType, Consumer<T> act) {
        return bus.ofType(eventType).compose(RxUtil.<T>rxSchedulerHelper()).subscribe(act);
    }

    public Disposable subscribe(@NonNull final OnEventListener listener) {
        return toFlowable(EventType.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventType>() {
                    @Override
                    public void accept(EventType eventType) throws Exception {
                        listener.onEventAccept(eventType.code, eventType.object);
                    }
                });
    }

    public void release(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public interface OnEventListener {
        void onEventAccept(@Code int code, Object object);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN, Code.USER_LOGIN_OUT, Code.MINUTE_HEART_BREAK, Code.HEART_BREAK,
            Code.MAIN_ACTIVITY_SELECT_ITEM_WITH_FRAGMENT_NAME, Code.USER_TEL_REFRESH, Code.USER_HEADER_REFRESH,
            Code.EDIT_NICKNAME_SUCCESS, Code.USER_BANK_REFRESH, Code.TRADING_SUCCESS,
            Code.USER_LOGIN_SUCCESS_FROM_NEWS_WELFARE, Code.TRADE_FRAGMENT_SELECT_WITH_PAGE_INT,
            Code.NEWS_WELFARE_BUYING_SUCCESS, Code.NEWS_WELFARE_BUYING,
            Code.USER_LOGIN_SUCCESS_WITH_ACTIVITY_DIALOG_INFO, Code.FORGET_PWD_RESET_SUCCESS,
            Code.MINE_OPTOPNAL_STOCK_DATA_CHANGED})
    public @interface Code {

        /**
         * 用户登录成功 并携带UserIndexBean
         */
        int USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN = 1;

        /**
         * 用户退出登录/执行退出清除/界面刷新等操作
         */
        int USER_LOGIN_OUT = 2;

        /**
         * 分钟改变心跳
         */
        int MINUTE_HEART_BREAK = 3;

        /**
         * 心跳
         */
        int HEART_BREAK = 4;

        /**
         * 首页选择了哪个item
         */
        int MAIN_ACTIVITY_SELECT_ITEM_WITH_FRAGMENT_NAME = 7;

        /**
         * 用户电话号码刷新
         */
        int USER_TEL_REFRESH = 8;

        /**
         * 刷新我的页面头像
         */
        int USER_HEADER_REFRESH = 10;

        /**
         * 昵称添加/修改成功
         */
        int EDIT_NICKNAME_SUCCESS = 12;

        /**
         * 绑卡之后刷新账户管理页面的银行卡信息
         */
        int USER_BANK_REFRESH = 14;

        /**
         * 充值、提现成功后刷新交易页面数据
         */
        int TRADING_SUCCESS = 15;

        /**
         * 用户从新手福利到注册完，自动登录成功，自动买入
         */
        int USER_LOGIN_SUCCESS_FROM_NEWS_WELFARE = 16;

        /**
         * 选中交易的某个界面
         */
        int TRADE_FRAGMENT_SELECT_WITH_PAGE_INT = 17;

        /**
         * 新手福利买入成功
         */
        int NEWS_WELFARE_BUYING_SUCCESS = 18;

        /**
         * 新手福利通知买入
         */
        int NEWS_WELFARE_BUYING = 19;

        /**
         * 用户登陆成功，并且匹配到了活动，需要弹窗
         */
        int USER_LOGIN_SUCCESS_WITH_ACTIVITY_DIALOG_INFO = 20;

        /**
         * 忘记密码重置密码成功
         */
        int FORGET_PWD_RESET_SUCCESS = 21;

        /**
         * 我的自选股数据发生变化
         */
        int MINE_OPTOPNAL_STOCK_DATA_CHANGED = 22;
    }

    private static class EventType {
        private int code;
        private Object object;

        EventType(@Code int c, Object obj) {
            this.code = c;
            this.object = obj;
        }
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }
}
