package com.tencent.qcloud.timchat.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushToken;

/**
 * iid refresh
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final static String TAG = "InstanceID";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if(!TextUtils.isEmpty(refreshedToken)) {
            TIMOfflinePushToken param = new TIMOfflinePushToken();
            param.setToken(refreshedToken);
            param.setBussid(169);
            TIMManager.getInstance().setOfflinePushToken(param);
        }
    }
}
