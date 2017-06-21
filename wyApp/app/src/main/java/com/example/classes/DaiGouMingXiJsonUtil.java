package com.example.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaiGouMingXiJsonUtil {
    public static DaiGouMingXi getDaiGouMingXi(String string) {
        DaiGouMingXi daiGouMingXi;

        daiGouMingXi = new DaiGouMingXi();
        if (string == null || string.equals("null")) {
            return daiGouMingXi;
        }

        JSONObject jSONObject1;
        try {
            jSONObject1 = new JSONObject(string);
            JSONObject jSONObject2 = new JSONObject(jSONObject1.getString("Data"));
            daiGouMingXi.setTitle(jSONObject2.getString("title"));
            daiGouMingXi.setBtnCount(jSONObject2.getInt("btnCount"));
            daiGouMingXi.setIsBindMobile(jSONObject2.getBoolean("IsBindMobile"));
            daiGouMingXi.setIsFillAccount(jSONObject2.getBoolean("IsFillAccount"));
            daiGouMingXi.setCanIntoMoney(jSONObject2.getBoolean("CanIntoMoney"));
            daiGouMingXi.setPurchasingState(jSONObject2.getString("PurchasingState"));
            List<Daigou> strings = new ArrayList<>();
            List<Statistics> strings2 = new ArrayList<Statistics>();
            if (jSONObject2.getString("Items").equals("null")) {
                strings = null;
            } else {
                JSONArray jsonArray = new JSONArray(jSONObject2.getString("Items"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    Daigou daigou = new Daigou();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    daigou.setInvaitedUser(jsonObject.getString("InvaitedUser"));
                    daigou.setMoney(jsonObject.getDouble("Money"));
                    daigou.setProMoney(jsonObject.getDouble("ProMoney"));
                    daigou.setOrderNo(jsonObject.getString("OrderNo"));
                    daigou.setState(jsonObject.getString("State"));
                    daigou.setType(jsonObject.getInt("Type"));

                    List<String> list = new ArrayList<String>();
                    String products = jsonObject.getString("Products");
                    List<Products1> products1 = new ArrayList<Products1>();
                    if (!products.equals("null")) {
                        JSONArray jsonArray2 = new JSONArray(jsonObject.getString("Products"));
//						JSONObject jsonObject3 = new JSONObject(jsonObject.getString("Products"));

//						Products products2 = new Products();
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            list.add(jsonArray2.getJSONObject(j).getString("ImageUrl"));
                            Products1 products2 = new Products1();
                            products2.setLucreMoney(jsonArray2.getJSONObject(j).getString("LucreMoney"));
                            products2.setCount(jsonArray2.getJSONObject(j).getString("Count"));
                            products2.setPrice(jsonArray2.getJSONObject(j).getString("Price"));
                            products2.setMoney(jsonArray2.getJSONObject(j).getString("Money"));
                            products2.setUrlImage(jsonArray2.getJSONObject(j).getString("ImageUrl"));
                            products2.setId(jsonArray2.getJSONObject(j).getInt("Id"));
                            products2.setSpec(jsonArray2.getJSONObject(j).getString("Spec"));
                            products2.setScale(jsonArray2.getJSONObject(j).getString("Scale"));
                            products2.setName(jsonArray2.getJSONObject(j).getString("Name"));
                            products1.add(products2);
                        }
                        daigou.setUrList(list);
//						daigou.setProducts(products2);

                        daigou.setProducts1(products1);
                    }
                    strings.add(daigou);
                }
                daiGouMingXi.setItems(strings);
            }
            if (jSONObject2.getString("Statistics").equals("null")) {
                strings2 = null;
            } else {
                JSONObject jsonArray2 = new JSONObject(jSONObject2.getString("Statistics"));
                Statistics statistics = new Statistics();
//					JSONObject jsonObject0 = jsonArray2.getJSONObject();
                statistics.setAllMoney(jsonArray2.getDouble("allMoney"));
                statistics.setAllRevenue(jsonArray2.getDouble("allRevenue"));
                statistics.setCanCashout(jsonArray2.getDouble("canCashout"));
                statistics.setCashedOut(jsonArray2.getDouble("cashedOut"));
                statistics.setCashingOut(jsonArray2.getDouble("cashingOut"));
                statistics.setFreeze(jsonArray2.getDouble("freeze"));
                statistics.setTodayRevenue(jsonArray2.getDouble("todayRevenue"));
                statistics.setCashedEnter(jsonArray2.getDouble("cashedEnter"));
                strings2.add(statistics);
                daiGouMingXi.setStatistics(strings2);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return daiGouMingXi;
    }
}
