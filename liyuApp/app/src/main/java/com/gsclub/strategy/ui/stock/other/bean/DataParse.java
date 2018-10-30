package com.gsclub.strategy.ui.stock.other.bean;

import android.text.TextUtils;
import android.util.SparseArray;

import com.gsclub.strategy.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataParse implements Serializable {
    private ArrayList<MinutesBean> datas = new ArrayList<>();
    private ArrayList<KLineBean> kDatas = new ArrayList<>();
    private float baseValue;
    private float permaxmin;
    private float volmax;
    private SparseArray<String> xValuesLabel = new SparseArray<>();

    /**
     * 添加分时图的数据
     *
     * @param listCode
     */
    public void parseMinutes(List<List<String>> listCode) {
        if (listCode == null || listCode.size() == 0) {
            return;
        }

        if (datas != null)
            datas.clear();

        if (kDatas != null)
            kDatas.clear();

        if (xValuesLabel != null)
            xValuesLabel.clear();
        baseValue = getAv(listCode);

        /*数据解析依照自己需求来定，如果服务器直接返回百分比数据，则不需要客户端进行计算*/
//        baseValue = (float) object.optJSONObject("data").optJSONObject(code).optJSONObject("qt").optJSONArray(code).optDouble(4);

        //最新价：last_px； 均价：avg_px； 成交量：business_amount； 成交额：business_balance；
        int count = listCode.size();
        try {
            for (int i = 0; i < count; i++) {
                MinutesBean minutesData = new MinutesBean();
                int len = listCode.get(i).get(0).length();
                minutesData.time = listCode.get(i).get(0).substring(len - 4, len - 2) + ":" + listCode.get(i).get(0).substring(len - 2, len);
                minutesData.cjprice = Float.parseFloat(listCode.get(i).get(1));
                if (i != 0) {
                    minutesData.cjnum = (long) StringUtils.parseDouble(listCode.get(i).get(3)) - (long) StringUtils.parseDouble(listCode.get(i - 1).get(3));
                    minutesData.total = minutesData.cjnum * minutesData.cjprice + datas.get(i - 1).total;
                    minutesData.avprice = (minutesData.total) / (long) StringUtils.parseDouble(listCode.get(i).get(3));
                } else {
                    minutesData.cjnum = (long) StringUtils.parseDouble(listCode.get(i).get(3));
                    minutesData.avprice = minutesData.cjprice;
                    minutesData.total = minutesData.cjnum * minutesData.cjprice;
                }
                minutesData.cha = minutesData.cjprice - baseValue;
                minutesData.per = (minutesData.cha / baseValue);
                double cha = minutesData.cjprice - baseValue;
                if (Math.abs(cha) > permaxmin) {
                    permaxmin = (float) Math.abs(cha);
                }
                volmax = Math.max(minutesData.cjnum, volmax);
                datas.add(minutesData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }


    /**
     * k线数据加载
     *
     * @param list
     */
    public void parseKLine(List<List<String>> list) {
        ArrayList<KLineBean> kLineBeans = new ArrayList<>();
        if (list != null) {
            int count = list.size();
            for (int i = 0; i < count; i++) {
                KLineBean kLineData = new KLineBean();
                kLineBeans.add(kLineData);
                String date = new BigDecimal(list.get(i).get(0)).toPlainString();
                if (date != null && date.length() > 8) {
                    //返回数据格式为20180111/2018012332 两种（前面的为日k返回，后为分钟k返回）
                    kLineData.date = cutString(date, 0, 8);
                    kLineData.minute = cutString(date, 8, 12);
                } else {
                    kLineData.date = date;
                }
                kLineData.open = Float.parseFloat(list.get(i).get(1));
                kLineData.high = Float.parseFloat(list.get(i).get(2));
                kLineData.low = Float.parseFloat(list.get(i).get(3));
                kLineData.close = Float.parseFloat(list.get(i).get(4));
                kLineData.vol = Float.parseFloat(list.get(i).get(5));
                volmax = Math.max(kLineData.vol, volmax);
                xValuesLabel.put(i, kLineData.date);
            }
        }
        kDatas.addAll(0, kLineBeans);
    }

    private float getAv(List<List<String>> listCode) {
        float result = 0;
        int size = listCode.size();
        for (int i = 0; i < size; i++) {
            result += Float.parseFloat(listCode.get(i).get(1));
        }
        result = result / (size * 1.0f);
        return result;
    }

    /**
     * 买卖5档数据处理
     *
     * @param data 五档数据
     * @param type 2卖/1买
     * @return
     */
    public List<StockTradeBean> getTradeList(String data, int type) {
        if (TextUtils.isEmpty(data))
            return null;
        String[] strings = data.split(",");
        if (strings.length % 3 != 0) return null;
        List<StockTradeBean> list = new ArrayList<>();
        int size = strings.length / 3;
        try {
            for (int i = 0; i < size; i++) {
                StockTradeBean bean = new StockTradeBean();
                bean.setNumber(Float.parseFloat(strings[(size - 1 - i) * 3 + 1]));
                bean.setPrice(Float.parseFloat(strings[(size - 1 - i) * 3]));
                String tradeLevel = ((type == 1) ? ("买" + (i + 1)) : ("卖" + (size - i)));
                bean.setTradeLevel(tradeLevel);
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public float getMin() {
        return baseValue - permaxmin;
    }

    public float getMax() {
        return baseValue + permaxmin;
    }

    public float getPercentMax() {
        return permaxmin / baseValue;
    }

    public float getPercentMin() {
        return -getPercentMax();
    }


    public float getVolmax() {
        return volmax;
    }


    public ArrayList<MinutesBean> getDatas() {
        return datas;
    }

    public ArrayList<KLineBean> getKLineDatas() {
        return kDatas;
    }

    public SparseArray<String> getXValuesLabel() {
        return xValuesLabel;
    }

    public static String cutString(String body, int start, int end) {
        StringBuilder sb = new StringBuilder(body);//构造一个StringBuilder对象
        sb.delete(end, body.length());
        sb.delete(0, start);
        return sb.toString();
    }
}