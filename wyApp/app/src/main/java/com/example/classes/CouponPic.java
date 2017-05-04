package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2016/12/23.
 * 获取抢优惠券图片url
 */

public class CouponPic {

    /**
     * Data : [{"Url":"31","Type":0,"Pic_Path":"http://img.16899.com/14/2016/12/57eca263782b4ad29a61216540bdc33e.png"}]
     * Result : true
     * Message : 成功
     */

    private boolean Result;
    private String Message;
    /**
     * Url : 31
     * Type : 0
     * Pic_Path : http://img.16899.com/14/2016/12/57eca263782b4ad29a61216540bdc33e.png
     */

    private List<DataBean> Data;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private String Url;
        private int Type;
        private String Pic_Path;

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getPic_Path() {
            return Pic_Path;
        }

        public void setPic_Path(String Pic_Path) {
            this.Pic_Path = Pic_Path;
        }
    }
}
