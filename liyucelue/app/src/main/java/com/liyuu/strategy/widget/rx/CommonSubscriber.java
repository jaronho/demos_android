package com.liyuu.strategy.widget.rx;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.liyuu.strategy.BuildConfig;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.http.exception.ApiException;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.UserInfoUtil;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private BaseView mView;
    private String mErrorMsg;
    private boolean isShowErrorState = true;
    private String mUrl;

    protected CommonSubscriber(BaseView view) {
        this.mView = view;
    }

    protected CommonSubscriber(BaseView view, String url) {
        this.mView = view;
        this.mUrl = url;
    }

    protected CommonSubscriber(BaseView view, boolean isShowErrorState) {
        this.mView = view;
        this.isShowErrorState = isShowErrorState;
    }

    protected CommonSubscriber(BaseView view, String errorMsg, boolean isShowErrorState) {
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    public void onComplete() {
        if (mView == null) {
            return;
        }
        mView.hideLoading();
    }

    @Override
    public void onNext(T t) {
        if (mView != null && isShowErrorState)
            mView.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (isShowErrorState)
            mView.hideLoading();
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showErrorMsg(mErrorMsg);
        } else if (e instanceof ApiException) {
            onMsg(((ApiException) e).getCode(), ((ApiException) e).getMsg());
        } else if (e instanceof HttpException) {
            mView.showErrorMsg("网络请求出现异常");
            if(!BuildConfig.SHOW_LOG) return;
            print((HttpException) e);
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            mView.showErrorMsg("网络数据解析错误");
        } else {
//            mView.showErrorMsg("未知错误"+e.toString());
            LogUtil.i("未知错误" + e.toString());
        }
        e.printStackTrace();
    }

    public void onMsg(int code, String msg) {
        if(code != 1098 && code != 1099) {
            mView.showErrorMsg(msg);
        }
    }

    /**
     * 打印500错误
     * @param exception
     */
    private void print(HttpException exception) {
        try {
            okhttp3.ResponseBody body = exception.response().errorBody();
            StringBuffer sb = new StringBuffer();
            sb.append(LogUtil.TAG).append(" : ").append(mUrl).append(" : ");
            sb.append(exception.code());
            sb.append(" ");
            sb.append(body.string());
            System.out.print(sb.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
