package com.liyuu.strategy.util;

import com.liyuu.strategy.app.ThreadPoolManager;
import com.liyuu.strategy.base.ThreadFinishImpl;
import com.liyuu.strategy.model.bean.StockMinuteBean;
import com.liyuu.strategy.widget.LineView;

import java.util.ArrayList;
import java.util.List;

public class ChartLineUtil {

    public static void initChartLine(final LineView mChart, StockMinuteBean bean) {

        try {

            List<List<String>> list = bean.getData().getTrend().getStockCode();
            if (list == null || list.size() <= 0) return;

            ArrayList<Float> prices = new ArrayList<>();
            //最新价：last_px； 均价：avg_px； 成交量：business_amount； 成交额：business_balance；
            int count = list.size();
            for (int i = 0; i < count; i++) {
                float price = Float.parseFloat(list.get(i).get(1));
                prices.add(price);
            }

            mChart.setData(prices);

//            parseMinutes(list, new ThreadFinishImpl<ArrayList<Float>>() {
//                @Override
//                public void finish(ArrayList<Float> datas, Runnable runnable) {
//                    if (datas == null || datas.size() == 0)
//                        return;
//                    mChart.setData(datas);
//                    ThreadPoolManager.getInstance().remove(runnable);
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void parseMinutes(final List<List<String>> listCode, final ThreadFinishImpl<ArrayList<Float>> calculate) {
        if (listCode == null || listCode.size() == 0) {
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Float> prices = new ArrayList<>();
                    //最新价：last_px； 均价：avg_px； 成交量：business_amount； 成交额：business_balance；
                    int count = listCode.size();
                    for (int i = 0; i < count; i++) {
                        float price = Float.parseFloat(listCode.get(i).get(1));
                        prices.add(price);
                    }
                    calculate.finish(prices, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    ThreadPoolManager.getInstance().remove(this);
                }
            }
        };

        ThreadPoolManager.getInstance().execute(runnable);

    }

}
