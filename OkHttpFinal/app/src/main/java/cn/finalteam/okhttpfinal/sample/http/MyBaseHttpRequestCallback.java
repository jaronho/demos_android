package cn.finalteam.okhttpfinal.sample.http;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.sample.http.model.BaseApiResponse;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:06
 */
public class MyBaseHttpRequestCallback<T extends BaseApiResponse> extends BaseHttpRequestCallback<T>{

    @Override
    protected void onSuccess(T t) {
        int code = t.getCode();
        if ( code == 1 ) {
            onLogicSuccess(t);
        } else {
            onLogicFailure(t);
        }
    }

    public void onLogicSuccess(T t) {

    }

    public void onLogicFailure(T t) {

    }
}
