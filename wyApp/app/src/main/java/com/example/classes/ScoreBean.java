package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/1/17.
 * 我的积分Bean
 * public enum PointsRules
 * {
 * 订单积分 = 0,
 * 关闭订单,
 * 首次注册,
 * 签到积分,
 * 冻结积分,
 * 过期积分,
 * 晒单积分,
 * 评价积分,
 * 分享积分,
 * 活动积分,
 * 积分兑换,
 * 积分抽奖,
 * 管理员手动更新积分
 * }
 */

public class ScoreBean {

    /**
     * SumPoints : 4
     * PointsRecordModelList : [{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-18T04:26:18"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T09:01:03"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T09:04:09"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T11:50:07"}]
     */

    private DataBean Data;
    /**
     * Data : {"SumPoints":4,"PointsRecordModelList":[{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-18T04:26:18"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T09:01:03"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T09:04:09"},{"PointRules":3,"PointNum":1,"Operation_Platform":"1","CreateTime":"2017-01-19T11:50:07"}]}
     * Result : true
     * Message :
     */

    private boolean Result;
    private String Message;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

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

    public static class DataBean {
        private int SumPoints;
        /**
         * PointRules : 3
         * PointNum : 1
         * Operation_Platform : 1
         * CreateTime : 2017-01-18T04:26:18
         */

        private List<PointsRecordModelListBean> PointsRecordModelList;

        public int getSumPoints() {
            return SumPoints;
        }

        public void setSumPoints(int SumPoints) {
            this.SumPoints = SumPoints;
        }

        public List<PointsRecordModelListBean> getPointsRecordModelList() {
            return PointsRecordModelList;
        }

        public void setPointsRecordModelList(List<PointsRecordModelListBean> PointsRecordModelList) {
            this.PointsRecordModelList = PointsRecordModelList;
        }

        public static class PointsRecordModelListBean {
            private String PointRules;
            private String PointNum;
            private String Operation_Platform;
            private String CreateTime;

            public String getPointRules() {
                return PointRules;
            }

            public void setPointRules(String PointRules) {
                this.PointRules = PointRules;
            }

            public String getPointNum() {
                return PointNum;
            }

            public void setPointNum(String PointNum) {
                this.PointNum = PointNum;
            }

            public String getOperation_Platform() {
                return Operation_Platform;
            }

            public void setOperation_Platform(String Operation_Platform) {
                this.Operation_Platform = Operation_Platform;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }
        }
    }
}
