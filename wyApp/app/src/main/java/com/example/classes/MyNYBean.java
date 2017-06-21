package com.example.classes;

/**
 * Created by NY on 2016/12/30.
 * 我的农一
 */

public class MyNYBean {

    private DataBean Data;
    private boolean Result;
    private String Message;
    private boolean IsLogin;

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

    public boolean isIsLogin() {
        return IsLogin;
    }

    public void setIsLogin(boolean IsLogin) {
        this.IsLogin = IsLogin;
    }

    public static class DataBean {
        private double Money;
        private double FrozenMoney;
        private String HeadImage;
        private int ToPayTotal;
        private int ToConfirmTotal;
        private String Mobile;
        private double RemainMoney;
        private String identity;
        private String btnText;
        private int CanSetPayPwd;
        private String PermitType;
        private String PurchasState;

        public double getMoney() {
            return Money;
        }

        public void setMoney(double Money) {
            this.Money = Money;
        }

        public double getFrozenMoney() {
            return FrozenMoney;
        }

        public void setFrozenMoney(double frozenMoney) {
            FrozenMoney = frozenMoney;
        }

        public String getHeadImage() {
            return HeadImage;
        }

        public void setHeadImage(String HeadImage) {
            this.HeadImage = HeadImage;
        }

        public int getToPayTotal() {
            return ToPayTotal;
        }

        public void setToPayTotal(int ToPayTotal) {
            this.ToPayTotal = ToPayTotal;
        }

        public int getToConfirmTotal() {
            return ToConfirmTotal;
        }

        public void setToConfirmTotal(int ToConfirmTotal) {
            this.ToConfirmTotal = ToConfirmTotal;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public double getRemainMoney() {
            return RemainMoney;
        }

        public void setRemainMoney(double RemainMoney) {
            this.RemainMoney = RemainMoney;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getBtnText() {
            return btnText;
        }

        public void setBtnText(String btnText) {
            this.btnText = btnText;
        }

        public int getCanSetPayPwd() {
            return CanSetPayPwd;
        }

        public void setCanSetPayPwd(int CanSetPayPwd) {
            this.CanSetPayPwd = CanSetPayPwd;
        }

        public String getPermitType() {
            return PermitType;
        }

        public void setPermitType(String PermitType) {
            this.PermitType = PermitType;
        }

        public String getPurchasState() {
            return PurchasState;
        }

        public void setPurchasState(String PurchasState) {
            this.PurchasState = PurchasState;
        }
    }
}
