package com.nongyi.nylive.Model;

import com.jaronho.sdk.library.Listener;

/**
 * Author:  Administrator
 * Date:    2017/4/16
 * Brief:   与服务器接口对接回调
 */

public abstract class NetCallback<T> {
    public abstract void onData(T data);
}
