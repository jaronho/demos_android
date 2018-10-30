package com.gsclub.strategy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.gsclub.strategy.app.ThreadPoolManager;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.util.TimeUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by hlw on 2017/12/18.
 */

public class HeartBeatService extends Service implements RxBus.OnEventListener {
    private boolean isRun = true;//是否运行线程
    private Disposable mDisposable;
    private int sixSecondCount = 0;
    private String minuteStr;
    private Runnable heartRunable = new Runnable() {
        @Override
        public void run() {
            try {
                while (isRun) {
                    Thread.sleep(2000);
                    //检查到分钟改变，发送一个广播
                    if (TextUtils.isEmpty(minuteStr)) {
                        minuteStr = TimeUtil.getNowMinute();
                    }

                    if (!minuteStr.equals(TimeUtil.getNowMinute())) {
                        minuteStr = TimeUtil.getNowMinute();
                        RxBus.get().send(RxBus.Code.MINUTE_HEART_BREAK);
                    }

                    //固定每6秒发送一个广播
                    sixSecondCount++;
                    if (sixSecondCount == 3) {
                        RxBus.get().send(RxBus.Code.HEART_BREAK);
                        sixSecondCount = 0;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDisposable = RxBus.get().subscribe(this);
        ThreadPoolManager.getInstance().execute(heartRunable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;

        if (heartRunable != null)
            ThreadPoolManager.getInstance().remove(heartRunable);

        RxBus.get().release(mDisposable);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        switch (code) {

        }
    }
}
