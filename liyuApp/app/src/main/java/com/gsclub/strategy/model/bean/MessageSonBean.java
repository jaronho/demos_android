package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MessageSonBean implements Serializable {


    /**
     * page : 1.0
     * all_page : 8.0
     * list : [{"id":22,"title":"系统公告测试","content":"<p>系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试<\/p>","thumb":""},{"id":21,"title":"系统公告消息测试","content":"<p>系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试系统公告消息测试<\/p>","thumb":""},{"id":16,"title":"asas","content":"<p>asas<\/p>","thumb":""},{"id":12,"title":"测试未登录消息红心显示222","content":"<p>测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222测试未登录消息红心显示222<\/p>","thumb":""},{"id":11,"title":"测试未登录消息红心显示","content":"<p>测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示测试未登录消息红心显示<\/p>","thumb":""},{"id":8,"title":"系统公告推送，请忽略","content":"<p>系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略系统公告推送，请忽略<\/p>","thumb":""},{"id":7,"title":"系统消息推送","content":"<p>系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送系统消息推送<\/p>","thumb":""},{"id":4,"title":"金牛选股APP更新啦","content":"<p>金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦金牛选股APP更新啦<\/p>","thumb":""}]
     */

    private int page;
    private int all_page;
    private List<ListBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAll_page() {
        return all_page;
    }

    public void setAll_page(int all_page) {
        this.all_page = all_page;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 22.0
         * title : 系统公告测试
         * content : <p>系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试系统公告测试</p>
         * thumb :
         */

        private int id;
        private String title;
        private String content;
        private String thumb;
        @SerializedName("c_time")
        private String time;
        @SerializedName("detail_url")
        private String detailUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }
    }
}
