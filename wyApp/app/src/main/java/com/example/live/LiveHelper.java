package com.example.live;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.example.classes.Share;
import com.jaronho.sdk.library.Listener;
import com.jaronho.sdk.library.MD5;
import com.jaronho.sdk.third.okhttpwrap.HttpInfo;
import com.jaronho.sdk.third.okhttpwrap.HttpInfo.Builder;
import com.jaronho.sdk.third.okhttpwrap.OkHttpUtil;
import com.jaronho.sdk.third.okhttpwrap.annotation.CacheLevel;
import com.jaronho.sdk.third.okhttpwrap.annotation.CacheType;
import com.jaronho.sdk.third.okhttpwrap.callback.CallbackOk;
import com.jaronho.sdk.third.okhttpwrap.cookie.PersistentCookieJar;
import com.jaronho.sdk.third.okhttpwrap.cookie.cache.SetCookieCache;
import com.jaronho.sdk.third.okhttpwrap.cookie.persistence.SharedPrefsCookiePersistor;
import com.jaronho.sdk.utils.ActivityTracker;
import com.jaronho.sdk.utils.UtilView;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.live.ConstantsLive.AVIMCMD_PRAISE;

/**
 * Author:  jaron.ho
 * Date:    2017-04-15
 * Brief:   与服务器接口对接
 */

public class LiveHelper {
    public static abstract class Callback<T> {
        public abstract void onData(T data);
        public abstract void onDataList(List<T> dataList);
    }

    private static Context mContext = null;
    private static String mUserId = "";
    private static String mUserAvatar = "";
    private static String mNickname = "";
    private static int mTempPages = 0;
    private static int mTempRecords = 0;
    private static boolean mIsLogin = false;

    // 获取时间戳
    private static String getTimeStamp() {
        return String.valueOf((new Date().getTime())/1000);
    }

    // 获取签名
    private static String getSignature() {
        // Signature算法:
        // 1.将AppKey，AppSecret和TimeStamp拼接成一个字符串signatureText
        // 2.将signatureText转换为小写
        // 3.为signatureText生成MD5,并转化为大写
        String signatureText = (ConstantsLive.APP_KEY + ConstantsLive.APP_SECRET + getTimeStamp()).toLowerCase();
        return MD5.get(signatureText).toUpperCase();
    }

    // 获取builder
    private static Builder getBuilder(String url) {
        Builder builder = new Builder();
        builder.setUrl(url);
        builder.addHead("AppKey", ConstantsLive.APP_KEY);
        builder.addHead("TimeStamp", getTimeStamp());
        builder.addHead("Signature", getSignature());
        return builder;
    }

    // 获取数据
    private static JSONObject getData(HttpInfo httpInfo) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(httpInfo.getRetDetail());
            int status = obj.getInt("Status");
            if (401 == status || 500 == status) {   // 参数异常,系统异常
                toast(obj.getString("Message"));
                return null;
            }
            if (402 == status) {
                toast("积分不足");
                return null;
            }
            return obj.getJSONObject("Data");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("NYLive", (null == obj ? "" : obj.toString()) + "\n" + e.getMessage());
            toast(httpInfo.getRetDetail());
        }
        return null;
    }

    // 获取数据列表
    private static JSONArray getDataList(HttpInfo httpInfo) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(httpInfo.getRetDetail());
            int status = obj.getInt("Status");
            if (401 == status || 500 == status) {   // 参数异常,系统异常
                toast(obj.getString("Message"));
                return new JSONArray();
            }
            if (402 == status) {
                toast("积分不足");
                return null;
            }
            JSONObject pageObj = obj.getJSONObject("Paging");
            mTempPages = pageObj.getInt("Pages");
            mTempRecords = pageObj.getInt("Records");
            return obj.getJSONArray("Data");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("NYLive", (null == obj ? "" : obj.toString()) + "\n" + e.getMessage());
            toast(httpInfo.getRetDetail());
        }
        return new JSONArray();
    }

    /*
     * 功  能: 显示提示
     * 参  数: msg - 提示内容
     * 返回值: 无
     */
    public static void toast(String msg) {
        UtilView.showToast(ActivityTracker.getTopActivity(), msg);
    }

    /*
     * 功  能: 初始化
     * 参  数: context - 应用上下文
     * 返回值: 无
     */
    public static void init(Application app) {
        Log.d("NYLive", "init");
        ActivityTracker.init(app);
        mContext = app.getApplicationContext();
        if ("".equals(mUserId)) {
            SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
            mUserId = sp.getString("user_id", "");
        }
        if ("".equals(mUserAvatar)) {
            SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
            mUserAvatar = sp.getString("user_avatar", "");
        }
        if ("".equals(mNickname)) {
            SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
            mNickname = sp.getString("nickname", "");
        }
        // 初始化okhttp
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/okhttp_download/";
        OkHttpUtil.init(app, "NYLive")
                .setConnectTimeout(30)//连接超时时间
                .setWriteTimeout(30)//写超时时间
                .setReadTimeout(30)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheLevel(CacheLevel.FIRST_LEVEL)//缓存等级
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setShowHttpLog(false)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .addResultInterceptor(null)//请求结果拦截器
                .addExceptionInterceptor(null)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(app)))//持久化cookie
                .build();
        // 初始化ILiveSDK
        ILiveSDK.getInstance().initSdk(mContext, ConstantsLive.APP_ID, ConstantsLive.ACCOUNT_TYPE);
        ILVLiveConfig liveConfig = new ILVLiveConfig();
        liveConfig.setLiveMsgListener(MessageListener.getInstance());
        ILVLiveManager.getInstance().init(liveConfig);
    }

    /*
     * 功  能: 获取userId
     * 参  数: 无
     * 返回值: String
     */
    public static String getUserId() {
        return mUserId;
    }

    /*
     * 功  能: 设置userId
     * 参  数: userId - 用户id
     * 返回值: 无
     */
    public static void setUserId(int userId) {
        Log.d("NYLive", "set user id: " + userId);
        mUserId = userId > 0 ? String.valueOf(userId) : "";
        SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", mUserId);
        editor.apply();
    }

    /*
     * 功  能: 获取头像
     * 参  数: 无
     * 返回值: String
     */
    public static String getUserAvatar() {
        return mUserAvatar;
    }

    /*
     * 功  能: 设置头像
     * 参  数: userAvatar - 头像
     * 返回值: 无
     */
    public static void setUserAvatar(String userAvatar) {
        Log.d("NYLive", "set user avatar: " + userAvatar);
        mUserAvatar = userAvatar;
        SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_avatar", mUserAvatar);
        editor.apply();

        TIMFriendshipManager.getInstance().setFaceUrl(mUserAvatar, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }
            @Override
            public void onSuccess() {
            }
        });
    }

    /*
     * 功  能: 获取昵称
     * 参  数: 无
     * 返回值: String
     */
    public static String getNickname() {
        return mNickname;
    }

    /*
     * 功  能: 设置昵称
     * 参  数: nickname - 昵称
     * 返回值: 无
     */
    public static void setNickname(String nickname) {
        Log.d("NYLive", "set nickname: " + nickname);
        mNickname = nickname;
        SharedPreferences sp = mContext.getSharedPreferences("sp_live", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nickname", mNickname);
        editor.apply();

        TIMFriendshipManager.getInstance().setNickName(mNickname, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }
            @Override
            public void onSuccess() {
            }
        });
    }

    /*
     * 功  能: 获取总页数
     * 参  数: 无
     * 返回值: int
     */
    public static int getTempPages() {
        return mTempPages;
    }

    /*
     * 功  能: 获取总记录数
     * 参  数: 无
     * 返回值: int
     */
    public static int getTempRecords() {
        return mTempRecords;
    }

    /*
     * 功  能: 启动模块
     * 参  数: 无
     * 返回值: 无
     */
    public static void startModule(final Listener listener) {
        Log.d("NYLive", "startModule, userId: " + mUserId);
        if ("".equals(mUserId)) {
            toast("登录后才可观看直播");
            return;
        }
        if (mIsLogin) {
            if (null != listener) {
                listener.onCallback(0, null);
            }
            return;
        }
        reqGetSig(mUserId, new Callback<String>() {
            @Override
            public void onData(String sig) {
                ILiveLoginManager.getInstance().iLiveLogin(mUserId, sig, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d("NYLive", "live login success");
                        mIsLogin = true;
                        WaitDialog.cancel();
                        if (null != listener) {
                            listener.onCallback(0, null);
                        }
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d("NYLive", "live login error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                        WaitDialog.cancel();
                        toast("登陆TSL失败: " + module + " " + errCode + " " + errMsg);
                    }
                });
            }
            @Override
            public void onDataList(List<String> dataList) {
            }
        });
    }

    /*
     * 功  能: 请求获取Sig
     * 参  数: userid - 用户id
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     */
    public static void reqGetSig(String userid, final Callback<String> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/Rest/TLS/GetSig";
        Builder builder = getBuilder(url);
        builder.addParam("userid", userid);
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getString("sig"));
                    } else {
                        WaitDialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取直播室列表
     * 参  数: PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetLiveList(int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetLiveList";
        Builder builder = getBuilder(url);
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取视频列表
     * 参  数: PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetVideoList(int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetVideoList";
        Builder builder = getBuilder(url);
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取我参与的列表
     * 参  数: UserId - 用户id
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetParticipateList(String UserId, int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetParticipateList";
        Builder builder = getBuilder(url);
        builder.addParam("UserId", UserId);
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取我收藏的列表
     * 参  数: UserId - 用户id
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetCollectionList(String UserId, int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetCollectionList";
        Builder builder = getBuilder(url);
        builder.addParam("UserId", UserId);
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取我发起的列表
     * 参  数: UserId - 用户id
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetStartedList(String UserId, int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetStartedList";
        Builder builder = getBuilder(url);
        builder.addParam("UserId", UserId);
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求频道详细
     * 参  数: id - int,频道ID
     * 返回值: json
     */
    public static void reqGetChannelView(int id, final Callback<ChannelInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/GetChannelView";
        Builder builder = getBuilder(url);
        builder.addParam("id", String.valueOf(id));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new ChannelInfo(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求更新频道状态
     * 参  数: id - int,频道ID
     * 返回值: json
     */
    public static void reqUpdateChannelStatus(int id, int status) {
        WaitDialog.show(true);
        String url = "http://livemanager.16899.com/Rest/Channel/UpdateChannelStatus";
        Builder builder = getBuilder(url);
        builder.addParam("Id", String.valueOf(id));
        builder.addParam("Status", String.valueOf(status));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
            }
        });
    }

    /*
     * 功  能: 请求心跳
     * 参  数: id - int,频道ID
     * 返回值: json
     */
    public static void reqHeartbeat(int id) {
        String url = "http://livemanager.16899.com/Rest/Channel/Heartbeat";
        Builder builder = getBuilder(url);
        builder.addParam("Id", String.valueOf(id));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
            }
        });
    }

    /*
     * 功  能: 请求获取用户积分
     * 参  数: userId - int,用户id
     * 返回值: json
     */
    public static void reqGetUserIntegral(String userId, final boolean showWait, final Callback<String> cb) {
        if (showWait) {
            WaitDialog.show(true);
        }
        String url = "http://livemanager.16899.com/Rest/Channel/GetUserIntegral";
        Builder builder = getBuilder(url);
        builder.addParam("Userid", userId);
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                if (showWait) {
                    WaitDialog.cancel();
                }
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getString("sysMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取直播间内容分享
     * 参  数: id - int,频道ID
     * 返回值: json
     */
    public static void reqShareChannel(int id, final Callback<Share.DataBean> cb) {
        WaitDialog.show(true);
        String url = "http://livemanager.16899.com/Rest/Channel/ShareChannel";
        Builder builder = getBuilder(url);
        builder.addParam("Id", String.valueOf(id));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        Share.DataBean db = new Share.DataBean();
                        db.setSharedTitle(data.getString("title"));
                        db.setSharedContent(data.getString("content"));
                        db.setSharedImage(data.getString("coverimg"));
                        db.setSharedUrl(data.getString("shareurl"));
                        cb.onData(db);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求互动
     * 参  数: channelId - int,频道ID
     *         userId - String,userId,自己
     *         type - int,互动类型:0=点赞，1=收藏，2=分享
     * 返回值: json
     */
    public static void reqInteractiveAdd(int channelId, String userId, final int type) {
        String url = "http://livemanager.16899.com/Rest/Channel/InteractiveAdd";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelId));
        builder.addParam("UserId", userId);
        builder.addParam("TYPE", String.valueOf(type));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                if (null != getData(httpInfo)) {
                    if (0 == type) {
                        ILVCustomCmd customCmd = new ILVCustomCmd();
                        customCmd.setCmd(AVIMCMD_PRAISE);
                        customCmd.setParam("");
                        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                                Log.d("NYLive", "send praise success, data: " + data);
                            }
                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                                Log.d("NYLive", "send praise error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                            }
                        });
                    } else if (1 == type) {
                        toast("收藏成功");
                    }
                }
            }
        });
    }

    /*
     * 功  能: 请求获取礼物列表
     * 参  数: 无
     * 返回值: json
     */
    public static void reqGetGivingList(final Callback<GiftInfo> cb) {
        WaitDialog.show(true);
        String url = "http://livemanager.16899.com/Rest/Channel/GetGivingList";
        Builder builder = getBuilder(url);
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<GiftInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new GiftInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求赠送礼物
     * 参  数: userId - String,赠送用户Id
     *         publishId - String,接收用户Id
     *         channelId - int,直播室Id
     *         givingId - int,礼物Id
     *         count - int,礼物数量
     * 返回值: json
     */
    public static void reqSendGiving(String userId, String publishId, int channelId, int givingId, int count, final Callback<Boolean> cb) {
        WaitDialog.show(true);
        String url = "http://livemanager.16899.com/Rest/Channel/SendGiving";
        Builder builder = getBuilder(url);
        builder.addParam("Userid", userId);
        builder.addParam("ChannelUserId", publishId);
        builder.addParam("ChannelId", String.valueOf(channelId));
        builder.addParam("GivingId", String.valueOf(givingId));
        builder.addParam("Count", String.valueOf(count));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
//                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(true);
                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("NYLive", e.toString());
//                }
            }
        });
    }

    /*
     * 功  能: 请求新增频道用户
     * 参  数: ChannelId - int,直播室ID或视频ID
     *         UserId - String,用户ID
     * 返回值: json
     */
    public static void reqChannelUserAdd(int channelid, String userid) {
        String url = "http://livemanager.16899.com/Rest/Channel/ChannelUserAdd";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelid));
        builder.addParam("UserId", userid);
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
            }
        });
    }

    /*
     * 功  能: 请求频道用户离开
     * 参  数: ChannelId - int,直播室ID或视频ID
     *         UserId - String,用户ID
     * 返回值: json
     */
    public static void reqChannelUserLeave(int channelid, String userid) {
        String url = "http://livemanager.16899.com/Rest/Channel/ChannelUserLeave";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelid));
        builder.addParam("UserId", userid);
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
            }
        });
    }

    /*
     * 功  能: 请求直播室人数列表
     * 参  数: roomId - int,房间id
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetChannelUsersList(int roomId, int pageIndex, int pageSize, final Callback<UserInfo> cb) {
        String url = "http://livemanager.16899.com/Rest/Channel/GetChannelUsersList";
        Builder builder = getBuilder(url);
        builder.addParam("Id", String.valueOf(roomId));
//        builder.addParam("PageIndex", String.valueOf(pageIndex));
//        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<UserInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new UserInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求获取用户权限
     * 参  数: userid - 用户id
     *         channelId - 房间id
     *         showWait - 是否显示转圈
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     */
    public static void reqGetAuthority(String userid, int channelId, final boolean showWait, final Callback<AuthorityInfo> cb) {
        if (showWait) {
            WaitDialog.show(false);
        }
        String url = "http://livemanager.16899.com/rest/Channel/GetAuthority";
        Builder builder = getBuilder(url);
        builder.addParam("Userid", userid);
        builder.addParam("ChannelId", String.valueOf(channelId));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                if (showWait) {
                    WaitDialog.cancel();
                }
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new AuthorityInfo(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求修改频道用户状态
     * 参  数: userid - 用户id
     *         channelId - 房间id
     *         isWords - true=禁言
     *         isIn - true=踢出
     *         isManager - true=管理
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     */
    public static void reqChannelUserUpdateStatus(String userid, int channelId, boolean isWords, boolean isIn, boolean isManager, final Callback<AuthorityInfo> cb) {
        WaitDialog.show(false);
        String url = "http://livemanager.16899.com/rest/Channel/ChannelUserUpdateStatus";
        Builder builder = getBuilder(url);
        builder.addParam("Userid", userid);
        builder.addParam("ChannelId", String.valueOf(channelId));
        builder.addParam("IsWords", isWords ? "true" : "false");
        builder.addParam("IsIn", isIn ? "true" : "false");
        builder.addParam("IsManage", isManager ? "true" : "false");
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new AuthorityInfo(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求聊天列表
     * 参  数: channelId - int,频道ID
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetChatList(int channelId, int pageIndex, int pageSize, final Callback<ChatInfo> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/GetChatList";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelId));
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChatInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            JSONObject data = dataList.getJSONObject(i);
                            ChatInfo ci = new ChatInfo();
                            ci.setId(data.getLong("Id"));
                            ci.setGroupId(data.getInt("ChannelId"));
                            ci.setSenderId(data.getString("UserId"));
                            ci.setName(data.getString("Name"));
                            ci.setAvatar(data.getString("ProfilePicture"));
                            ci.setContent(data.getString("Content"));
                            ci.setTime(new Date(data.getLong("CreatedDate")));
                            infoList.add(ci);
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求修改频道用户状态
     * 参  数: userid - 用户id
     *         channelId - 房间id
     *         content - 内容
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     */
    public static void reqChatAdd(final String userid, final int channelId, final String content, final Callback<ChatInfo> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/ChatAdd";
        Builder builder = getBuilder(url);
        builder.addParam("Userid", userid);
        builder.addParam("ChannelId", String.valueOf(channelId));
        builder.addParam("Content", content);
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                JSONObject data = null;
                try {
                    data = getData(httpInfo);
                    if (null != data && null != cb) {
                        if (data.getBoolean("sysMsg")) {
                            ChatInfo ci = new ChatInfo();
                            ci.setGroupId(channelId);
                            ci.setSenderId(userid);
                            ci.setAvatar(LiveHelper.getUserAvatar());
                            ci.setContent(content);
                            cb.onData(ci);
                        } else {
                            toast("消息发送失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == data ? "" : data.toString()) + "\n" + e.toString());
                }
            }
        });
    }

    /*
     * 功  能: 请求群聊列表
     * 参  数: PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: json
     */
    public static void reqGetChatroomList(int pageIndex, int pageSize, final Callback<ChannelInfo> cb) {
        WaitDialog.show(true);
        String url = "http://livemanager.16899.com/rest/Channel/GetChatroomList";
        Builder builder = getBuilder(url);
//        builder.addParam("Status", "0");  // 0=未开始，1=已开始，2=已结束，没传，获取全部
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                WaitDialog.cancel();
                JSONArray dataList = null;
                try {
                    dataList = getDataList(httpInfo);
                    if (dataList.length() > 0 && null != cb) {
                        List<ChannelInfo> infoList = new ArrayList<>();
                        for (int i = 0, len = dataList.length(); i < len; ++i) {
                            infoList.add(new ChannelInfo(dataList.getJSONObject(i)));
                        }
                        cb.onDataList(infoList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NYLive", (null == dataList ? "" : dataList.toString()) + "\n" + e.toString());
                }
            }
        });
    }
}
