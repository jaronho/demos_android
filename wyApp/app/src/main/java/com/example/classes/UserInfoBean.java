package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/3/14.
 * 我的基本信息
 */

public class UserInfoBean {

    /**
     * Data : {"UserDetail":{"Id":156113,"User_Name":"18317008906","User_Pwd":"E10ADC3949BA59ABBE56E057F20F883E","Sex":1,"State":1,"Type":0,"Grade":0,"Mobile":"18317008906","Money":0,"Email":null,"Add_Date":"2016-10-18T17:02:43","Last_Login_Date":null,"Ext_Id":105078,"Nick_Name":"li","Tel":null,"Head_Img":null,"Edit_Date":"2017-03-14T10:14:23","Job":0,"Good_At":null,"Birthday":null,"Is_Public_Birthday":false,"Pub_Count":null,"Good_Count":null,"Integral":null,"Good_At_String":null,"Description":null,"Is_Recommend":false,"Permit_Type":2,"Province_Id":10,"City_Id":107,"County_Id":1171,"Town_Id":664193,"Village_Id":664194,"Area_Name":"嘉定区/菊园新区/嘉富社区","CardType":null,"Account_Bank":null,"Account_Card":null,"Account_Name":null,"Bind_Type":1,"Purchasing_State":1,"Purch_Apply_Date":"2016-10-18T17:02:43","Purch_Approval_Date":"2016-10-18T17:02:43","StationMaster_Id":null,"Source":"Android","Can_Into_Money":false,"Pro_Visible_Set":0,"Account_Province":null,"Account_City":null,"Account_County":null,"Account_Modify_Set":null,"Account_Sub_Bank":null,"Remain_Money":225514.09,"Pay_Pwd":"E10ADC3949BA59ABBE56E057F20F883E","Province_Name":"上海市","City_Name":"市辖区","County_Name":"嘉定区","Town_Name":"菊园新区","Village_Name":"嘉富社区","WorkStationId":0,"ReceiveName":null},"ProvinceName":null,"CityName":null,"CountyName":"嘉定区/菊园新区/嘉富社区","Birthday":"","JobName":"无","JobList":["无","业务代表","产品经理","技术推广","区域经理","人力资源","企业后勤","产品研发","生产技术","企业管理","合作社","种植大户","普通农户","农资零售","农资批发","农资连锁","农资登记","农资外贸","在校学生","科研教育","政府机关","农艺师","媒体网站","包材设备","平面设计","农资会展"],"BeGoodAtList":null}
     * Result : true
     * Message :
     */

    private DataBean Data;
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

        private User UserDetail;
        private String CountyName;
        private String JobName;
        private List<String> JobList;

        public User getUserDetail() {
            return UserDetail;
        }

        public void setUserDetail(User UserDetail) {
            this.UserDetail = UserDetail;
        }

        public String getCountyName() {
            return CountyName;
        }

        public void setCountyName(String CountyName) {
            this.CountyName = CountyName;
        }

        public String getJobName() {
            return JobName;
        }

        public void setJobName(String JobName) {
            this.JobName = JobName;
        }

        public List<String> getJobList() {
            return JobList;
        }

        public void setJobList(List<String> JobList) {
            this.JobList = JobList;
        }

    }
}
