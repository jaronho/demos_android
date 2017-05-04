package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2016/12/27.
 * 热卖商品
 */

public class ProductBriefBean {

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
        private int Id;
        private String Pro_Name;
        private String Common_Name;
        private String Total_Content;
        private String Spec;
        private String Dosageform;
        private String Pic_Url;
        private double Price;
        private int Marketing_Type;

        public int getMarketing_Type() {
            return Marketing_Type;
        }

        public void setMarketing_Type(int marketing_Type) {
            Marketing_Type = marketing_Type;
        }

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
    }
}
