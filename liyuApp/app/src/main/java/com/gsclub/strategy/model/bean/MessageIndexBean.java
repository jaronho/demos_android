package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageIndexBean implements Serializable {


    /**
     * type : 1
     * type_name : 系统消息
     * type_unread : 6
     * new_title : 系统公告测试
     * new_id : 22
     */

    private int type;
    @SerializedName("type_name")
    private String typeName;
    @SerializedName("type_unread")
    private int typeUnread;
    @SerializedName("new_title")
    private String newTitle;
    @SerializedName("new_id")
    private int newId;
    @SerializedName("type_icon")
    private String typeIcon;
    @SerializedName("c_time")
    private String cTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeUnread() {
        return typeUnread;
    }

    public void setTypeUnread(int typeUnread) {
        this.typeUnread = typeUnread;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public String getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(String typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
}
