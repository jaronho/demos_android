package com.nongyi.nylive.utils;

import android.content.Context;

import com.jaronho.sdk.library.MD5;
import com.jaronho.sdk.third.okhttpwrap.HttpInfo;
import com.jaronho.sdk.third.okhttpwrap.HttpInfo.Builder;
import com.jaronho.sdk.third.okhttpwrap.OkHttpUtil;
import com.jaronho.sdk.third.okhttpwrap.callback.CallbackOk;
import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.bean.DataChannel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Author:  jaron.ho
 * Date:    2017-04-15
 * Brief:   与服务器接口对接
 */

public class NetHelper {
    public static abstract class Callback<T> {
        public abstract void onData(T data);
    }

    private static Context mContext = null;

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
        String signatureText = (Global.APP_KEY + Global.APP_SECRET + getTimeStamp()).toLowerCase();
        return MD5.get(signatureText).toUpperCase();
    }

    // 获取builder
    private static Builder getBuilder(String url) {
        Builder builder = new Builder();
        builder.setUrl(url);
        builder.addHead("AppKey", Global.APP_KEY);
        builder.addHead("TimeStamp", getTimeStamp());
        builder.addHead("Signature", getSignature());
        return builder;
    }

    // 获取数据
    private static JSONObject getData(HttpInfo httpInfo) {
        try {
            JSONObject obj = new JSONObject(httpInfo.getRetDetail());
            int status = obj.getInt("Status");
            if (401 == status || 500 == status) {   // 参数异常,系统异常
                ViewUtil.showToast(mContext, obj.getString("ChatMessage"));
            }
            return obj.getJSONObject("Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 功  能: 初始化
     * 参  数: context - 应用上下文
     * 返回值: 无
     */
    public static void init(Context context) {
        mContext = context;
    }

    /*
     * 功  能: 请求获取Sig
     * 参  数: userid - 用户id
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  sig - String,Sig值
     *                }
     */
    public static void reqGetSig(String userid, final Callback<String> cb) {
        String url = "http://livemanager.16899.com/Rest/TLS/GetSig";
        Builder builder = getBuilder(url);
        builder.addParam("userid", userid);
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getString("sig"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求获取直播室列表
     * 参  数: Status - int,状态:0=未开始,1=已开始,2=已结束,没传(获取全部)
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  Id - int,编号
     *                  Title - String,标题
     *                  MainMap - String,主图
     *                  Url - String,直播地址
     *                  StartTime - long,开始时间
     *                  EndTime - long,结束时间
     *                  Status - String,状态
     *                  Pwd - String,频道密码
     *                  Likes - String,点赞量
     *                  Collect - String,收藏量
     *                  Share - String,分享量
     *                  Details - String,详细介绍
     *                  PublisherAvatar - String,发布人头像
     *                  PublisherName - String,发布人昵称
     *                  PublisherID - String,发布人ID(主播ID)
     *                  VideoSource - String,视频源
     *                  Type - int, 类型
     *                  CreatedDate - long,创建时间
     *                }
     */
    public static void reqGetLiveList(int status, int pageIndex, int pageSize, final Callback<DataChannel> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/GetLiveList";
        Builder builder = getBuilder(url);
        if (0 == status || 1 == status || 2 == status) {
            builder.addParam("Status", String.valueOf(status));
        }
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new DataChannel(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求群聊列表
     * 参  数: Status - int,状态:0=未开始,1=已开始,2=已结束,没传(获取全部)
     *         PageIndex - int,请求第几页,默认第一页
     *         PageSize - int,请求条数,默认10条
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  Id - int,编号
     *                  Title - String,标题
     *                  MainMap - String,主图
     *                  Url - String,地址
     *                  StartTime - long,开始时间
     *                  EndTime - long,结束时间
     *                  Status - String,状态
     *                  Pwd - String,频道密码
     *                  Likes - String,点赞量
     *                  Collect - String,收藏量
     *                  Share - String,分享量
     *                  Details - String,详细介绍
     *                  PublisherAvatar - String,发布人头像
     *                  PublisherName - String,发布人昵称
     *                  PublisherID - String,发布人ID(主播ID)
     *                  VideoSource - String,视频源
     *                  Type - int, 类型
     *                  CreatedDate - long,创建时间
     *                }
     */
    public static void reqGetChatroomList(int status, int pageIndex, int pageSize, final Callback<DataChannel> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/GetChatroomList";
        Builder builder = getBuilder(url);
        if (0 == status || 1 == status || 2 == status) {
            builder.addParam("Status", String.valueOf(status));
        }
        builder.addParam("PageIndex", String.valueOf(pageIndex));
        builder.addParam("PageSize", String.valueOf(pageSize));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new DataChannel(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求频道详细
     * 参  数: id - int,频道ID
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  Id - int,编号
     *                  Title - String,标题
     *                  MainMap - String,主图
     *                  Url - String,直播地址
     *                  StartTime - long,开始时间
     *                  EndTime - long,结束时间
     *                  Status - String,状态
     *                  Pwd - String,频道密码
     *                  Likes - String,点赞量
     *                  Collect - String,收藏量
     *                  Share - String,分享量
     *                  Details - String,详细介绍
     *                  PublisherAvatar - String,发布人头像
     *                  PublisherName - String,发布人昵称
     *                  PublisherID - String,发布人ID(主播ID)
     *                  VideoSource - String,视频源
     *                  Type - int, 类型
     *                  CreatedDate - long,创建时间
     *                }
     */
    public static void reqGetChannelView(int id, final Callback<DataChannel> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/GetChannelView";
        Builder builder = getBuilder(url);
        builder.addParam("id", String.valueOf(id));
        OkHttpUtil.getDefault().doGetAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(new DataChannel(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求更新URL(直播室,视频)
     * 参  数: id - int,直播室ID或视频ID
     *         Url - String,新的Url地址
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  sysMsg - boolean,true:更新成功,false-更新失败
     *                }
     */
    public static void reqUpdateChannelUrl(int id, String newUrl, final Callback<Boolean> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/UpdateChannelUrl";
        Builder builder = getBuilder(url);
        builder.addParam("id", String.valueOf(id));
        builder.addParam("Url", newUrl);
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getBoolean("sysMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求互动记录
     * 参  数: ChannelId - int,直播室ID或视频ID
     *         UserId - int,用户ID
     *         TYPE - int,0=点赞,1=收藏,2=分享
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  sysMsg - boolean,true:更新成功,false-更新失败
     *                }
     */
    public static void reqInteractiveAdd(int channelid, int userid, int type, final Callback<Boolean> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/InteractiveAdd";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelid));
        builder.addParam("UserId", String.valueOf(userid));
        builder.addParam("TYPE", String.valueOf(type));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getBoolean("sysMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 功  能: 请求新增频道用户
     * 参  数: ChannelId - int,直播室ID或视频ID
     *         UserId - int,用户ID
     * 返回值: Status - int,请求处理响应状态,200:成功,401:参数异常,500:系统异常
     *         ChatMessage - String,信息提示
     *         Data - {
     *                  sysMsg - boolean,true:更新成功,false-更新失败
     *                }
     */
    public static void reqChannelUserAdd(int channelid, int userid, final Callback<Boolean> cb) {
        String url = "http://livemanager.16899.com/rest/Channel/ChannelUserAdd";
        Builder builder = getBuilder(url);
        builder.addParam("ChannelId", String.valueOf(channelid));
        builder.addParam("UserId", String.valueOf(userid));
        OkHttpUtil.getDefault().doPostAsync(builder.build(), new CallbackOk() {
            @Override
            public void onResponse(HttpInfo httpInfo) throws IOException {
                try {
                    JSONObject data = getData(httpInfo);
                    if (null != data && null != cb) {
                        cb.onData(data.getBoolean("sysMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
