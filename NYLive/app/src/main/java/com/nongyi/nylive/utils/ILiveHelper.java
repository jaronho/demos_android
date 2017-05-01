package com.nongyi.nylive.utils;

/**
 * Author:  Administrator
 * Date:    2017/4/19
 * Brief:   ILiveHelper
 */

public class ILiveHelper {
    private static ILiveHelper mInstance = null;

    public static ILiveHelper getInstance() {
        if (null == mInstance) {
            synchronized (ILiveHelper.class) {
                mInstance = new ILiveHelper();
            }
        }
        return mInstance;
    }
}
