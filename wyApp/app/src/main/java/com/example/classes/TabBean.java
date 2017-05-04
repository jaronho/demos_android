package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/2/5.
 * 底部导航
 */

public class TabBean {

    /**
     * Data : [{"Url":null,"Type":3,"Pic_Path":"http://192.168.80.99:8092/14/2017/02/102888d358624d01a445bde68bcace34.png"},{"Url":null,"Type":3,"Pic_Path":"http://192.168.80.99:8092/14/2017/02/f7e7b81e459e41d48c9274791085a5ca.png"},{"Url":null,"Type":3,"Pic_Path":"http://192.168.80.99:8092/14/2017/02/89631d9487d6483da449d2cd35f72ffa.png"},{"Url":null,"Type":3,"Pic_Path":"http://192.168.80.99:8092/14/2017/02/2aeb0e1fbdca42219b6f49d8c159ec81.png"}]
     * Result : true
     * Message : 成功
     */

    private boolean Result;
    private String Message;
    /**
     * Url : null
     * Type : 3
     * Pic_Path : http://192.168.80.99:8092/14/2017/02/102888d358624d01a445bde68bcace34.png
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
