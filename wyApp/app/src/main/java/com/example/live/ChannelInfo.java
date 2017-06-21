package com.example.live;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/4/16
 * Brief:   频道信息
 */

public class ChannelInfo implements Parcelable {
    private JSONObject mJSONObj = null;
    public int Id;                      // 编号
    public String Title;                // 标题
    public String MainMap;              // 主图
    public String Url;                  // 地址
    public long StartTime;              // 开始时间
    public long EndTime;                // 结束时间
    public int STATUS;               // 状态:0.未开始,1.已开始,2.已结束
    public String Pwd;                  // 频道密码
    public String Likes;                // 点赞量
    public String Collect;              // 收藏量
    public String Share;                // 分享量
    public String Details;              // 详细介绍
    public String PublisherAvatar;      // 发布人头像
    public String PublisherName;        // 发布人昵称
    public String PublisherID;          // 发布人ID(主播ID)
    public String VideoSource;          // 视频源
    public int TYPE;                    // 类型:0.直播,1.视频
    public long CreatedDate;            // 创建时间
    public int ChannelId;               // 房间id
    public int Integral;                    // 积分
    public int WatchCount;              // 观看数

    public ChannelInfo(JSONObject obj) throws JSONException {
        mJSONObj = obj;
        Id = obj.getInt("Id");
        Title = obj.getString("Title");
        MainMap = obj.getString("MainMap");
        Url = obj.getString("Url");
        StartTime = obj.getLong("StartTime");
        EndTime = obj.getLong("EndTime");
        STATUS = obj.getInt("STATUS");
        Pwd = obj.getString("Pwd");
        Likes = obj.getString("Likes");
        Collect = obj.getString("Collect");
        Share = obj.getString("Share");
        Details = obj.getString("Details");
        PublisherAvatar = obj.getString("PublisherAvatar");
        PublisherName = obj.getString("PublisherName");
        PublisherID = obj.getString("PublisherID");
        VideoSource = obj.getString("VideoSource");
        TYPE = obj.getInt("TYPE");
        CreatedDate = obj.getLong("CreatedDate");
        if (!obj.isNull("ChannelId")) {
            ChannelId = obj.getInt("ChannelId");
        }
        Integral = obj.getInt("Integral");
        WatchCount = obj.getInt("WatchCount");
    }

    protected ChannelInfo(Parcel in) {
        Id = in.readInt();
        Title = in.readString();
        MainMap = in.readString();
        Url = in.readString();
        StartTime = in.readLong();
        EndTime = in.readLong();
        STATUS = in.readInt();
        Pwd = in.readString();
        Likes = in.readString();
        Collect = in.readString();
        Share = in.readString();
        Details = in.readString();
        PublisherAvatar = in.readString();
        PublisherName = in.readString();
        PublisherID = in.readString();
        VideoSource = in.readString();
        TYPE = in.readInt();
        CreatedDate = in.readLong();
        ChannelId = in.readInt();
        Integral = in.readInt();
        WatchCount = in.readInt();
    }

    public static final Creator<ChannelInfo> CREATOR = new Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel in) {
            return new ChannelInfo(in);
        }
        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Title);
        dest.writeString(MainMap);
        dest.writeString(Url);
        dest.writeLong(StartTime);
        dest.writeLong(EndTime);
        dest.writeInt(STATUS);
        dest.writeString(Pwd);
        dest.writeString(Likes);
        dest.writeString(Collect);
        dest.writeString(Share);
        dest.writeString(Details);
        dest.writeString(PublisherAvatar);
        dest.writeString(PublisherName);
        dest.writeString(PublisherID);
        dest.writeString(VideoSource);
        dest.writeInt(TYPE);
        dest.writeLong(CreatedDate);
        dest.writeInt(ChannelId);
        dest.writeInt(Integral);
        dest.writeInt(WatchCount);
    }

    @Override
    public String toString() {
        if (null != mJSONObj) {
            return mJSONObj.toString();
        }
        return "";
    }
}
