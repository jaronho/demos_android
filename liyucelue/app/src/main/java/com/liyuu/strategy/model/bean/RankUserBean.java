package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RankUserBean implements Serializable {


    /**
     * user_data : {"nickname":"订单","headimg":"","five_rate":"5.00","count_rate":"25.00","count_win_rate":"100.00"}
     */

    private UserDataBean user_data;

    public UserDataBean getUser_data() {
        return user_data;
    }

    public void setUser_data(UserDataBean user_data) {
        this.user_data = user_data;
    }

    public static class UserDataBean implements Serializable {
        /**
         * nickname : 订单
         * headimg :
         * five_rate : 5.00
         * count_rate : 25.00
         * count_win_rate : 100.00
         */

        private String nickname;
        private String headimg;
        @SerializedName("five_rate")
        private String fiveRate;
        @SerializedName("count_rate")
        private String countRate;
        @SerializedName("count_win_rate")
        private String countWinRate;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getFiveRate() {
            return fiveRate;
        }

        public void setFiveRate(String fiveRate) {
            this.fiveRate = fiveRate;
        }

        public String getCountRate() {
            return countRate;
        }

        public void setCountRate(String countRate) {
            this.countRate = countRate;
        }

        public String getCountWinRate() {
            return countWinRate;
        }

        public void setCountWinRate(String countWinRate) {
            this.countWinRate = countWinRate;
        }
    }
}
