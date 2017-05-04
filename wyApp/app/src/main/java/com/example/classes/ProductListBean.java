package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/3/21.
 * 商品列表
 */

public class ProductListBean {

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

        private int Total_Count;
        private boolean ShowMarket;
        private List<ManufNamelistBean> Manuf_Namelist;
        private List<DosageformlistBean> Dosageformlist;
        private List<ProductJson> V_SearchProduct;
        private List<TypelistBean> Typelist;

        public int getTotal_Count() {
            return Total_Count;
        }

        public void setTotal_Count(int Total_Count) {
            this.Total_Count = Total_Count;
        }

        public boolean isShowMarket() {
            return ShowMarket;
        }

        public void setShowMarket(boolean ShowMarket) {
            this.ShowMarket = ShowMarket;
        }

        public List<ManufNamelistBean> getManuf_Namelist() {
            return Manuf_Namelist;
        }

        public void setManuf_Namelist(List<ManufNamelistBean> Manuf_Namelist) {
            this.Manuf_Namelist = Manuf_Namelist;
        }

        public List<DosageformlistBean> getDosageformlist() {
            return Dosageformlist;
        }

        public void setDosageformlist(List<DosageformlistBean> Dosageformlist) {
            this.Dosageformlist = Dosageformlist;
        }

        public List<ProductJson> getV_SearchProduct() {
            return V_SearchProduct;
        }

        public void setV_SearchProduct(List<ProductJson> V_SearchProduct) {
            this.V_SearchProduct = V_SearchProduct;
        }

        public List<TypelistBean> getTypelist() {
            return Typelist;
        }

        public void setTypelist(List<TypelistBean> Typelist) {
            this.Typelist = Typelist;
        }

        public static class ManufNamelistBean {
            /**
             * Manuf_Name : 丹麦科麦农
             */

            private String Manuf_Name;

            public String getManuf_Name() {
                return Manuf_Name;
            }

            public void setManuf_Name(String Manuf_Name) {
                this.Manuf_Name = Manuf_Name;
            }
        }

        public static class DosageformlistBean {
            /**
             * Dosageform : 水剂
             */

            private String Dosageform;

            public String getDosageform() {
                return Dosageform;
            }

            public void setDosageform(String Dosageform) {
                this.Dosageform = Dosageform;
            }
        }

        public static class TypelistBean {
            /**
             * Type : 除草剂
             */

            private String Type;

            public String getType() {
                return Type;
            }

            public void setType(String Type) {
                this.Type = Type;
            }
        }
    }
}
