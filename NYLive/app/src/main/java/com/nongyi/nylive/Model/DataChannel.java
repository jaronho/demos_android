package com.nongyi.nylive.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/4/16
 * Brief:   频道数据
 */

public class DataChannel {
    public int Id;                      // 编号
    public String Title;                // 标题
    public String MainMap;              // 主图
    public String Url;                  // 地址
    public long StartTime;              // 开始时间
    public long EndTime;                // 结束时间
    public String Status;               // 状态
    public String Pwd;                  // 频道密码
    public String Likes;                // 点赞量
    public String Collect;              // 收藏量
    public String Share;                // 分享量
    public String Details;              // 详细介绍
    public String PublisherAvatar;      // 发布人头像
    public String PublisherName;        // 发布人昵称
    public String PublisherID;          // 发布人ID(主播ID)
    public String VideoSource;          // 视频源
    public int Type;                    // 类型
    public long CreatedDate;            // 创建时间

    public DataChannel(JSONObject obj) throws JSONException {
        Id = obj.getInt("Id");
        Title = obj.getString("Title");
        MainMap = obj.getString("MainMap");
        Url = obj.getString("Url");
        StartTime = obj.getLong("StartTime");
        EndTime = obj.getLong("EndTime");
        Status = obj.getString("Status");
        Pwd = obj.getString("Pwd");
        Likes = obj.getString("Likes");
        Collect = obj.getString("Collect");
        Share = obj.getString("Share");
        Details = obj.getString("Details");
        PublisherAvatar = obj.getString("PublisherAvatar");
        PublisherName = obj.getString("PublisherName");
        PublisherID = obj.getString("PublisherID");
        VideoSource = obj.getString("VideoSource");
        Type = obj.getInt("Type");
        CreatedDate = obj.getLong("CreatedDate");
    }
}
