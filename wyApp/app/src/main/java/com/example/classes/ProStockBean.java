package com.example.classes;

/**
 * Created by NY on 2017/2/7.
 * 产品物流与库存
 */

public class ProStockBean {

    /**
     * ProId : 219
     * StockLevel : 1
     * StockName : 农一网无锡库
     * IsStock : true
     * Remark : 到县自提，免物流费。工作站配送到村，订单总产品金额达1500.00元以上，免物流费；订单总产品金额不足1500.00元，每箱（桶）收取10元物流费。
     * RStock : 0
     * MRStock : 479
     * YSStock : 0
     */

    private DataBean Data;
    /**
     * Data : {"ProId":219,"StockLevel":1,"StockName":"农一网无锡库","IsStock":true,"Remark":"到县自提，免物流费。工作站配送到村，订单总产品金额达1500.00元以上，免物流费；订单总产品金额不足1500.00元，每箱（桶）收取10元物流费。","RStock":0,"MRStock":479,"YSStock":0}
     * Result : true
     * Message : null
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
        private int ProId;
        private int StockLevel;
        private String StockName;
        private boolean IsStock;
        private String Remark;
        private int RStock;
        private int MRStock;
        private int YSStock;

        public int getProId() {
            return ProId;
        }

        public void setProId(int ProId) {
            this.ProId = ProId;
        }

        public int getStockLevel() {
            return StockLevel;
        }

        public void setStockLevel(int StockLevel) {
            this.StockLevel = StockLevel;
        }

        public String getStockName() {
            return StockName;
        }

        public void setStockName(String StockName) {
            this.StockName = StockName;
        }

        public boolean isIsStock() {
            return IsStock;
        }

        public void setIsStock(boolean IsStock) {
            this.IsStock = IsStock;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public int getRStock() {
            return RStock;
        }

        public void setRStock(int RStock) {
            this.RStock = RStock;
        }

        public int getMRStock() {
            return MRStock;
        }

        public void setMRStock(int MRStock) {
            this.MRStock = MRStock;
        }

        public int getYSStock() {
            return YSStock;
        }

        public void setYSStock(int YSStock) {
            this.YSStock = YSStock;
        }
    }
}
