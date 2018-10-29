package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IncomeTypesBean implements Serializable {


    /**
     * type_id : 2
     * name : 盈利牛人
     * rule : ["盈利牛人：近五个交易日累计交易盈利前十名牛人"]
     */

    @SerializedName("type_id")
    private int typeId;
    private String name;
    private List<String> rule;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRule() {
        return rule;
    }

    public void setRule(List<String> rule) {
        this.rule = rule;
    }
}
