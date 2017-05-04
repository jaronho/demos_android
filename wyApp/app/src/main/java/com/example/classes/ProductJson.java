package com.example.classes;

public class ProductJson {
    private int Id;
    private String Pro_Name; //商品名称
    private String Common_Name;//化学名
    private String Type;//代表类别
    private String Total_Content;//总含量
    private String Dosageform;//剂型
    private double Price;//价格
    private String Pic_Url;//图片
//    private int Total_Count;//总数
//    private String Percentage;//有效成分
//    private int Toxicity;//毒性
    private String Spec;//规格
//    private String Certificate_No;//证书
//    private String Instruction;//详情
//    private boolean Is_Onsale;//是否上架
//    private boolean Is_Recommend;//是否放到首页
//    private boolean Is_Promotion;//是否特价
    private boolean Is_Stockout;
//    private boolean State;//是否删除
//    private String Add_Date;//添加时间
    private String Manuf_Name;
//    private int Manuf_Id;
    private int deal;
//    private Boolean ShowMarket;
    private String Show_Price;//列表页显示的价格
//    private int Marketing_Type;
//    private int Stock;
//    private String Begin_Time;
//    private String End_Time;

    public String getShow_Price() {
        return Show_Price;
    }

    public void setShow_Price(String show_Price) {
        Show_Price = show_Price;
    }

    public boolean isIs_Stockout() {
        return Is_Stockout;
    }

    public void setIs_Stockout(boolean is_Stockout) {
        Is_Stockout = is_Stockout;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public String getManuf_Name() {
        return Manuf_Name;
    }

    public void setManuf_Name(String manuf_Name) {
        Manuf_Name = manuf_Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPro_Name() {
        return Pro_Name;
    }

    public void setPro_Name(String pro_Name) {
        Pro_Name = pro_Name;
    }

    public String getCommon_Name() {
        return Common_Name;
    }

    public void setCommon_Name(String common_Name) {
        Common_Name = common_Name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTotal_Content() {
        return Total_Content;
    }

    public void setTotal_Content(String total_Content) {
        Total_Content = total_Content;
    }

    public String getDosageform() {
        return Dosageform;
    }

    public void setDosageform(String dosageform) {
        Dosageform = dosageform;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url = pic_Url;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }



}
