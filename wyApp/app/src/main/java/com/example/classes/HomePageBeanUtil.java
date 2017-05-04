package com.example.classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageBeanUtil {
    @Nullable
    public static HomePageList getHomePageBeanList(String response) {
        HomePageList homePageList = new HomePageList();
        if (response == null || response.equals("null")) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);

            homePageList.setResult(jsonObject.getBoolean("Result"));
            homePageList.setMessage(jsonObject.getString("Message"));
            JSONObject data = jsonObject.getJSONObject("Data");

            HomePageList.DataBean dataBean = new HomePageList.DataBean();

            dataBean.setLunboList(getList(data.getJSONArray("轮播")));
            dataBean.setChucaoList(getList(data.getJSONArray("除草剂")));
            dataBean.setShachongList(getList(data.getJSONArray("杀虫剂")));
            dataBean.setShajunList(getList(data.getJSONArray("杀菌剂")));
            dataBean.setQitaList(getList(data.getJSONArray("调节剂及其他")));
            dataBean.setCoupons(getList(data.getJSONArray("优惠券领取区")));
            dataBean.setZhongbudaohang(getList(data.getJSONArray("中部导航")));
            dataBean.setZhongbuList(getList(data.getJSONArray("中部通栏")));

            homePageList.setData(dataBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homePageList;
    }

    @NonNull
    private static List<HomePageBean> getList(JSONArray jsonArray) throws JSONException {
        Gson gson = new Gson();
        List<HomePageBean> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            HomePageBean homePageBean = gson.fromJson(jsonArray.getString(i), HomePageBean.class);
            list.add(homePageBean);
        }
        return list;
    }

}
