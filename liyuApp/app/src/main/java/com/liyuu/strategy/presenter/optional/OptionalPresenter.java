package com.liyuu.strategy.presenter.optional;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.StockField;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.optional.OptionalContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.http.response.StrategyHttpResponse;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CustomSelectStockBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.ui.optional.adapter.StockSelectRecyclerAdatper;
import com.liyuu.strategy.util.RxUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.Sort;

public class OptionalPresenter extends RxPresenter<OptionalContract.View> implements OptionalContract.Presenter {
    private DataManager mDataManager;

    private int sortMode = 1;//1 数据库添加顺序  2.涨幅升序 3.涨幅降序

    @Inject
    OptionalPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public List<OptionalTopStockBean> getThreeStockPlateDefaultData() {
        List<OptionalTopStockBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OptionalTopStockBean bean = new OptionalTopStockBean();
            bean.setLastPx(Float.NaN);
            bean.setPxChange(Float.NaN);
            bean.setPxChangeRate(Float.NaN);
            String name = "";
            switch (i) {
                case 0:
                    name = "上证指数";
                    break;
                case 1:
                    name = "深证成指";
                    break;
                case 2:
                    name = "创业板指";
                    break;
            }
            bean.setStockName(name);
            list.add(bean);
        }
        return list;
    }

    @Override
    public void getThreeStockPlateData() {
        String url = UrlConfig.Stock_stockIndex;
        post(false, mDataManager.fetchTopStockInfo(url),
                new CommonSubscriber<List<OptionalTopStockBean>>(mView, url) {
                    @Override
                    public void onNext(List<OptionalTopStockBean> data) {
                        mView.setTopStocks(data);
                    }
                });
    }

    @Override
    public void getUserLocalOptionalStockDatas() {
        List<CustomSelectStockBean> list =
                (List<CustomSelectStockBean>) mDataManager.dbQueryList(CustomSelectStockBean.class, "addTime", Sort.ASCENDING);
        mView.showUserLocalOptionalStockDatas(list);
    }

    @Override
    public void getStocksRealDataAndRefreshList(final StockSelectRecyclerAdatper adatper) {
        if (adatper == null || adatper.getData() == null || adatper.getData().size() == 0)
            return;
        StringBuilder stocks = new StringBuilder();

        for (int i = 0, size = adatper.getData().size(); i < size; i++) {
            stocks.append(adatper.getData().get(i).getSymbol());
            if (i != size - 1)
                stocks.append(",");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("en_prod_code", stocks.toString());
        map.put("fields", "last_px,px_change,px_change_rate,prod_name");
        String url = UrlConfig.stock_real;

        addSubscribe(false, mDataManager.fetchStockReal(url, map)
                .debounce(5000, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<StrategyHttpResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleHPResult())
                .subscribeWith(new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object s) {
                        super.onNext(s);

                        if (adatper == null || adatper.getData() == null || adatper.getData().size() == 0)
                            return;

                        String data = GsonUtil.toString(s);
                        if (TextUtils.isEmpty(data))
                            return;
                        JsonObject snapshot = GsonUtil.getJsonObject(data, "data", "snapshot");
                        if (snapshot == null) return;

                        for (int i = 0, size = adatper.getData().size(); i < size; i++) {
                            String symbol = adatper.getData().get(i).getSymbol();
                            if (!snapshot.has(symbol))
                                continue;
                            JsonArray array = snapshot.get(symbol).getAsJsonArray();
                            int position = GsonUtil.getStockFieldsPosition(snapshot, StockField.LAST_PX);
                            double lastPx = GsonUtil.arrayGetDouble(array, position);
                            adatper.getData().get(i).setLastPrice(lastPx);

                            position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE_RATE);
                            double pxChangeRate = GsonUtil.arrayGetDouble(array, position);
                            adatper.getData().get(i).setPxChangeRate(pxChangeRate);

                            position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE);
                            double pxChange = GsonUtil.arrayGetDouble(array, position);
                            adatper.getData().get(i).setPxChange(pxChange);
                        }

//                        adatper.notifyDataSetChanged();
                        //更新股票实时信息后，根据排序规则对数据进行重新排序
                        sortStockListData(adatper, sortMode);
                    }
                }));
    }

    @Override
    public void changeSortMode(StockSelectRecyclerAdatper adatper) {
        if (adatper == null)
            return;

        if (sortMode == 1) {
            //根据数据库顺序排序
            sortMode = 2;
        } else if (sortMode == 2) {
            //根据涨幅升序排序
            sortMode = 3;
        } else {
            //根据涨幅降序排序
            sortMode = 1;
        }
        sortStockListData(adatper, sortMode);
    }

    /**
     * 重新排列股票列表顺序
     */
    private void sortStockListData(StockSelectRecyclerAdatper adatper, int sortMode) {
        if (adatper == null)
            return;
        int resource = -1;
        List<CustomSelectStockBean> datas = new ArrayList<>(adatper.getData());

        if (sortMode == 2) {
            //根据涨幅升序排序
            Collections.sort(datas, new Comparator<CustomSelectStockBean>() {
                @Override
                public int compare(CustomSelectStockBean o1, CustomSelectStockBean o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    return (int) ((o1.getPxChangeRate() - o2.getPxChangeRate()) * 100);
                }
            });
            resource = R.mipmap.icon_plate_rose_up;
        } else if (sortMode == 3) {
            //根据涨幅降序排序
            Collections.sort(datas, new Comparator<CustomSelectStockBean>() {
                @Override
                public int compare(CustomSelectStockBean o1, CustomSelectStockBean o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    return (int) ((o2.getPxChangeRate() - o1.getPxChangeRate()) * 100);
                }
            });
            resource = R.mipmap.icon_plate_rose_dowm;
        } else {
            //根据数据库顺序排序,根据addTime排序
            Collections.sort(datas, new Comparator<CustomSelectStockBean>() {
                @Override
                public int compare(CustomSelectStockBean o1, CustomSelectStockBean o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    return (int) (o1.getAddTime() - o2.getAddTime());
                }
            });
            resource = R.mipmap.icon_plate_rose_normal;
        }

        mView.changeSortIcon(resource);
        adatper.getData().clear();
        adatper.setData(datas);
        adatper.notifyDataSetChanged();
    }
}
