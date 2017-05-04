package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/2/20.
 * 首页优惠券列表
 */

public class HomeCouponBean {

    /**
     * Data : [{"Show_Price":null,"Unit":"箱","SubUnit_Price":62.5,"SubUnit":"瓶","Id":306,"Pro_Name":"[全地通]41%草甘膦异丙胺盐水剂","State":3,"Common_Name":"草甘膦异丙胺盐","Common_Name_En":null,"Purchasing_Percent":0,"Total_Content":"41%","Spec":"100+900克*8瓶/箱","Dosageform":"水剂","Manuf_Id":135,"Manuf_Name":"辉丰股份","Stock":8,"Toxicity":1,"Group_Id":306,"Type":"除草剂","Is_Delete":false,"Add_Date":"2015-12-14T17:37:55","Pic_Url":"http://192.168.80.99:8092/Product/m/2015/12/cf0052b7df254a539979f312708565e4.jpg","Price":500,"Supply_Price":500,"Supply_Count":1,"Is_Supply":true,"deal":84,"Is_Commend":true,"Is_Presell":true,"Is_Purchase":true,"Is_Stockout":false,"Weight":8,"Ower":1,"Kind":1,"Store_CateId":null,"Cate_Name":"瓶","Cate_Id":"10,1010,101010,101011,101012,101013,101014,101015,101016,101017,101018,101019,1011,101110,101111,101112,101113","Marketing_Type":1,"Begin_Time":null,"End_Time":null,"Plat_Percent":null,"WorkStation_Percent":null,"StationMaster_Percent":null,"Is_PurchasesPlan":false,"Pack_Level":1,"Recommend_Sort":0,"Is_MainProduct":1,"ProductPriceRule":"739,500.00,1,20,0,0.08,0,0.05|","Visible_Set":0},{"Show_Price":null,"Unit":"箱","SubUnit_Price":500,"SubUnit":"桶","Id":307,"Pro_Name":"[全地通]41%草甘膦异丙胺盐水剂","State":3,"Common_Name":"草甘膦异丙胺盐","Common_Name_En":null,"Purchasing_Percent":0,"Total_Content":"41%","Spec":"5千克*4桶/箱","Dosageform":"水剂","Manuf_Id":135,"Manuf_Name":"辉丰股份","Stock":4,"Toxicity":1,"Group_Id":306,"Type":"除草剂","Is_Delete":false,"Add_Date":"2015-12-14T17:37:55","Pic_Url":"http://192.168.80.99:8092/Product/m/2015/12/cf0052b7df254a539979f312708565e4.jpg","Price":2000,"Supply_Price":2000,"Supply_Count":1,"Is_Supply":true,"deal":54,"Is_Commend":false,"Is_Presell":false,"Is_Purchase":false,"Is_Stockout":false,"Weight":20,"Ower":1,"Kind":1,"Store_CateId":null,"Cate_Name":"桶","Cate_Id":"10,1010,101010,101011,101012,101013,101014,101015,101016,101017,101018,101019,1011,101110,101111,101112,101113","Marketing_Type":1,"Begin_Time":null,"End_Time":null,"Plat_Percent":null,"WorkStation_Percent":null,"StationMaster_Percent":null,"Is_PurchasesPlan":false,"Pack_Level":1,"Recommend_Sort":0,"Is_MainProduct":1,"ProductPriceRule":"722,2000.00,1,9999,0,0,0,0.03|","Visible_Set":0},{"Show_Price":null,"Unit":"箱","SubUnit_Price":0.785,"SubUnit":"瓶","Id":219,"Pro_Name":"[花果富]0.01%芸苔素内酯可溶粉剂","State":3,"Common_Name":"芸苔素内酯","Common_Name_En":null,"Purchasing_Percent":0,"Total_Content":"0.01%","Spec":"6克*400瓶/箱","Dosageform":"可溶粉剂","Manuf_Id":126,"Manuf_Name":"辉丰农化","Stock":400,"Toxicity":1,"Group_Id":219,"Type":"杀虫剂","Is_Delete":false,"Add_Date":"2014-10-25T14:54:04","Pic_Url":"http://192.168.80.99:8092/Product/m/2014/10/3142e31f63b7457fb70296255315d5e1.jpg","Price":314,"Supply_Price":314,"Supply_Count":1,"Is_Supply":true,"deal":37,"Is_Commend":false,"Is_Presell":false,"Is_Purchase":false,"Is_Stockout":false,"Weight":7,"Ower":1,"Kind":1,"Store_CateId":null,"Cate_Name":"瓶","Cate_Id":"10,1010,101010,101011,101012,101013,101014,101015,101016,101017,101018,101019,1011,101110,101111,101112,101113","Marketing_Type":1,"Begin_Time":null,"End_Time":null,"Plat_Percent":null,"WorkStation_Percent":null,"StationMaster_Percent":null,"Is_PurchasesPlan":false,"Pack_Level":1,"Recommend_Sort":0,"Is_MainProduct":0,"ProductPriceRule":"729,314.00,1,10,0.05,0.06,0,0.05|","Visible_Set":0},{"Show_Price":null,"Unit":"箱","SubUnit_Price":50,"SubUnit":"瓶","Id":179,"Pro_Name":"[辉丰使百克]13.7%咪鲜胺水剂","State":3,"Common_Name":"咪鲜胺","Common_Name_En":null,"Purchasing_Percent":0,"Total_Content":"13.7%","Spec":"400毫升*10瓶/箱","Dosageform":"水剂","Manuf_Id":133,"Manuf_Name":"海利尔","Stock":10,"Toxicity":2,"Group_Id":178,"Type":"杀菌剂","Is_Delete":false,"Add_Date":"2014-08-19T11:02:24","Pic_Url":"http://192.168.80.99:8092/Product/m/2014/08/8ffbda3e3cba45039234f5668494f516.JPG","Price":500,"Supply_Price":500,"Supply_Count":1,"Is_Supply":true,"deal":11,"Is_Commend":false,"Is_Presell":false,"Is_Purchase":false,"Is_Stockout":false,"Weight":20,"Ower":1,"Kind":1,"Store_CateId":null,"Cate_Name":"瓶","Cate_Id":"10,1010,101012,13,1316,131610","Marketing_Type":3,"Begin_Time":null,"End_Time":null,"Plat_Percent":null,"WorkStation_Percent":null,"StationMaster_Percent":null,"Is_PurchasesPlan":false,"Pack_Level":1,"Recommend_Sort":0,"Is_MainProduct":0,"ProductPriceRule":"732,500.00,1,5,0,0.12,0,0.05|","Visible_Set":0}]
     * Result : true
     * Message :
     */

    private boolean Result;
    private String Message;
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

        private String Show_Price;
        private String Unit;
        private double SubUnit_Price;
        private String SubUnit;
        private int Id;
        private String Pro_Name;
        private String Common_Name;
        private String Total_Content;
        private String Spec;
        private String Dosageform;
        private int Stock;
        private String Pic_Url;
        private double Price;
        private String Cate_Name;

        public String getShow_Price() {
            return Show_Price;
        }

        public void setShow_Price(String Show_Price) {
            this.Show_Price = Show_Price;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public double getSubUnit_Price() {
            return SubUnit_Price;
        }

        public void setSubUnit_Price(double SubUnit_Price) {
            this.SubUnit_Price = SubUnit_Price;
        }

        public String getSubUnit() {
            return SubUnit;
        }

        public void setSubUnit(String SubUnit) {
            this.SubUnit = SubUnit;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getPro_Name() {
            return Pro_Name;
        }

        public void setPro_Name(String Pro_Name) {
            this.Pro_Name = Pro_Name;
        }

        public String getCommon_Name() {
            return Common_Name;
        }

        public void setCommon_Name(String Common_Name) {
            this.Common_Name = Common_Name;
        }

        public String getTotal_Content() {
            return Total_Content;
        }

        public void setTotal_Content(String Total_Content) {
            this.Total_Content = Total_Content;
        }

        public String getSpec() {
            return Spec;
        }

        public void setSpec(String Spec) {
            this.Spec = Spec;
        }

        public String getDosageform() {
            return Dosageform;
        }

        public void setDosageform(String Dosageform) {
            this.Dosageform = Dosageform;
        }

        public int getStock() {
            return Stock;
        }

        public void setStock(int Stock) {
            this.Stock = Stock;
        }

        public String getPic_Url() {
            return Pic_Url;
        }

        public void setPic_Url(String Pic_Url) {
            this.Pic_Url = Pic_Url;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public String getCate_Name() {
            return Cate_Name;
        }

        public void setCate_Name(String Cate_Name) {
            this.Cate_Name = Cate_Name;
        }
    }
}
