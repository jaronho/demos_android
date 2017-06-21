package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/3/2.
 * 购物车
 */

public class ShoppingCartBean {

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
        private List<ProductBean> Product;
        private List<ProductBean> InvalidProduct;

        public List<ProductBean> getProduct() {
            return Product;
        }

        public void setProduct(List<ProductBean> Product) {
            this.Product = Product;
        }

        public List<ProductBean> getInvalidProduct() {
            return InvalidProduct;
        }

        public void setInvalidProduct(List<ProductBean> InvalidProduct) {
            this.InvalidProduct = InvalidProduct;
        }

        public static class ProductBean {

            private int Pro_Id;
            private String Pro_Name;
            private String Common_Name;
            private String Total_Content;
            private String Spec;
            private double Price;
            private int Count;
            private String Manuf_Name;
            private String Pic_Url;
            private String Dosageform;
            private boolean Is_Presell;
            private int Marketing_Type;
            private String Type;
            private double Free_Price;
            private String TimeStamp;
            private List<PriceListBean> priceList;
            private boolean isCheck = true;//是否选中

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getPro_Id() {
                return Pro_Id;
            }

            public void setPro_Id(int Pro_Id) {
                this.Pro_Id = Pro_Id;
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

            public double getPrice() {
                return Price;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public int getCount() {
                return Count;
            }

            public void setCount(int Count) {
                this.Count = Count;
            }

            public String getManuf_Name() {
                return Manuf_Name;
            }

            public void setManuf_Name(String Manuf_Name) {
                this.Manuf_Name = Manuf_Name;
            }

            public String getPic_Url() {
                return Pic_Url;
            }

            public void setPic_Url(String Pic_Url) {
                this.Pic_Url = Pic_Url;
            }

            public String getDosageform() {
                return Dosageform;
            }

            public void setDosageform(String Dosageform) {
                this.Dosageform = Dosageform;
            }

            public boolean isIs_Presell() {
                return Is_Presell;
            }

            public void setIs_Presell(boolean Is_Presell) {
                this.Is_Presell = Is_Presell;
            }

            public int getMarketing_Type() {
                return Marketing_Type;
            }

            public void setMarketing_Type(int Marketing_Type) {
                this.Marketing_Type = Marketing_Type;
            }

            public String getType() {
                return Type;
            }

            public void setType(String Type) {
                this.Type = Type;
            }

            public double getFree_Price() {
                return Free_Price;
            }

            public void setFree_Price(double Free_Price) {
                this.Free_Price = Free_Price;
            }

            public String getTimeStamp() {
                return TimeStamp;
            }

            public void setTimeStamp(String TimeStamp) {
                this.TimeStamp = TimeStamp;
            }

            public List<PriceListBean> getPriceList() {
                return priceList;
            }

            public void setPriceList(List<PriceListBean> priceList) {
                this.priceList = priceList;
            }

            public static class PriceListBean {

                private double Price;
                private int Minimum;
                private int Maximum;

                public double getPrice() {
                    return Price;
                }

                public void setPrice(double Price) {
                    this.Price = Price;
                }

                public int getMinimum() {
                    return Minimum;
                }

                public void setMinimum(int Minimum) {
                    this.Minimum = Minimum;
                }

                public int getMaximum() {
                    return Maximum;
                }

                public void setMaximum(int Maximum) {
                    this.Maximum = Maximum;
                }
            }
        }
    }
}
