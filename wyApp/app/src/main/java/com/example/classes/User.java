package com.example.classes;

import java.io.Serializable;
import java.util.List;


public class User implements Serializable {
    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", User_Name='" + User_Name + '\'' +
                ", Sex=" + Sex +
                ", Nick_Name='" + Nick_Name + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", Permit_Type='" + Permit_Type + '\'' +
                ", Purchasing_State='" + Purchasing_State + '\'' +
                ", CountyName='" + CountyName + '\'' +
                ", Area_Name='" + Area_Name + '\'' +
                ", Email='" + Email + '\'' +
                ", jobList=" + jobList +
                ", Province_Id=" + Province_Id +
                ", County_Id=" + County_Id +
                ", City_Id=" + City_Id +
                ", Town_Id=" + Town_Id +
                ", Vaillage_Id=" + Vaillage_Id +
                '}';
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int Id;
    private String User_Name;
    private int Sex;
    private String Nick_Name;
    private String Mobile;
    private String Permit_Type;//身份（1：普通用户 2：代购人 3：乡镇服务专员 4:签约代购员）
    private String Purchasing_State;//状态（0：申请中 1：已通过 2：已拒绝）
    private String Head_Img;
    private String CountyName;
    private String Area_Name;
    private String Email;
    private List<String> jobList;
    private int Province_Id;
    private int County_Id;
    private int City_Id;
    private int Town_Id;
    private int Vaillage_Id;

    public int getVaillage_Id() {
        return Vaillage_Id;
    }

    public void setVaillage_Id(int vaillage_Id) {
        Vaillage_Id = vaillage_Id;
    }

    public int getTown_Id() {
        return Town_Id;
    }

    public void setTown_Id(int town_Id) {
        Town_Id = town_Id;
    }

    public int getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int city_Id) {
        City_Id = city_Id;
    }

    public String getArea_Name() {
        return Area_Name;
    }

    public void setArea_Name(String area_Name) {
        Area_Name = area_Name;
    }

    public int getProvince_Id() {
        return Province_Id;
    }

    public void setProvince_Id(int province_Id) {
        Province_Id = province_Id;
    }

    public int getCounty_Id() {
        return County_Id;
    }

    public void setCounty_Id(int county_Id) {
        County_Id = county_Id;
    }

    public String getPermit_Type() {
        return Permit_Type;
    }

    public void setPermit_Type(String permit_Type) {
        Permit_Type = permit_Type;
    }

    public String getPurchasing_State() {
        return Purchasing_State;
    }

    public void setPurchasing_State(String purchasing_State) {
        Purchasing_State = purchasing_State;
    }

    public String getNick_Name() {
        return Nick_Name;
    }

    public void setNick_Name(String nick_Name) {
        Nick_Name = nick_Name;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String countyName) {
        CountyName = countyName;
    }

    public List<String> getJobList() {
        return jobList;
    }

    public void setJobList(List<String> jobList) {
        this.jobList = jobList;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHead_Img() {
        return Head_Img;
    }

    public void setHead_Img(String head_Img) {
        Head_Img = head_Img;
    }
}
