package com.nongyi.nylive.Model;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.AnchorLiveActivity;
import com.nongyi.nylive.MainActivity;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

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
