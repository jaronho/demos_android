package com.liyuu.strategy.presenter.stock;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.stock.FiveDayContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.http.response.StrategyHttpResponse;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.StockMinuteBean;
import com.liyuu.strategy.ui.stock.other.bean.StockGsonBean;
import com.liyuu.strategy.util.RxUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FiveDayPresenter extends RxPresenter<FiveDayContract.View> implements FiveDayContract.Presenter {

    private final int FIVE_DAY_LINE_ALL_NUMBER = 61 * 5;//五日分时线总数，以后台返回结果为依据
    private DataManager mDataManager;

    @Inject
    FiveDayPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getFiveDayLine(final String symbolName) {
        Map<String, Object> params = new HashMap<>();
        params.put("prod_code", symbolName);
        params.put("fields", "last_px,avg_px,business_amount,business_balance");
        String url = UrlConfig.Stock_trend5day;

        addSubscribe(false, mDataManager.getStockFiveDay(url, params)
                .subscribeOn(Schedulers.io())
                .compose(RxUtil.<StrategyHttpResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleHPResult())
                .map(new Function<Object, ReturnBean>() {
                    @Override
                    public ReturnBean apply(Object data) throws Exception {
                        String all = GsonUtil.getInstance().toJson(data);
                        String result = all.replace(symbolName, "stockCode");
                        StockMinuteBean bean = GsonUtil.gsonToBean(result, StockMinuteBean.class);
                        List<List<String>> listAll = bean.getData().getTrend().getStockCode();
                        if (listAll == null || listAll.size() <= 0)
                            return null;
                        List<String> xDataList = new ArrayList<>();
                        List<List<StockGsonBean>> lists = dealData(listAll, xDataList);
                        ReturnBean bean1 = new ReturnBean();
                        bean1.setxDataList(xDataList);
                        bean1.setLists(lists);
                        return bean1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<ReturnBean>(mView, url) {
                    @Override
                    public void onNext(ReturnBean data) {
                        super.onNext(data);

                        mView.loadBarAndLineChartView(data.getxDataList(), data.getLists());
                    }
                }));

    }

    private List<List<StockGsonBean>> dealData(List<List<String>> listAll, List<String> xDataList) {
        int count = 0;
        List<List<StockGsonBean>> fiveStocks = new ArrayList<>();
        List<StockGsonBean> stockGsonBeanList = new ArrayList<>();

        String time8 = "";
        int size = listAll.size();
        for (int i = 0; i < size; i++) {
            xDataList.add(String.valueOf(count));
            count++;

            String nowTime = new BigDecimal(listAll.get(i).get(0)).toPlainString().substring(0, 8);

            if ("".equals(time8)) {
                time8 = nowTime;
            }

            if (!time8.equals(nowTime)) {
                time8 = nowTime;
                fiveStocks.add(stockGsonBeanList);
                stockGsonBeanList = new ArrayList<>();
            }

            float price = Float.parseFloat(listAll.get(i).get(1));

            StockGsonBean stockGsonBean = new StockGsonBean();
            stockGsonBean.setTime(nowTime);
            stockGsonBean.setLast_px(listAll.get(i).get(1));
            stockGsonBean.setAvg_px(listAll.get(i).get(2));
            stockGsonBean.setBusiness_amount(listAll.get(i).get(3));
            stockGsonBean.setBusiness_balance(listAll.get(i).get(4));
            stockGsonBeanList.add(stockGsonBean);

            if ((i + 1) == size) {
                fiveStocks.add(stockGsonBeanList);
            }
        }

        if (xDataList.size() < FIVE_DAY_LINE_ALL_NUMBER) {
            for (int i = xDataList.size(); i < FIVE_DAY_LINE_ALL_NUMBER; i++) {
                xDataList.add(String.valueOf(i));
            }
        }
        return fiveStocks;
    }

    private class ReturnBean implements Serializable {
        List<String> xDataList;
        List<List<StockGsonBean>> lists;

        public List<String> getxDataList() {
            return xDataList;
        }

        public void setxDataList(List<String> xDataList) {
            this.xDataList = xDataList;
        }

        public List<List<StockGsonBean>> getLists() {
            return lists;
        }

        public void setLists(List<List<StockGsonBean>> lists) {
            this.lists = lists;
        }
    }
}
