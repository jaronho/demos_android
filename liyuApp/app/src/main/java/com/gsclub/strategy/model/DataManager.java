package com.gsclub.strategy.model;

import android.support.annotation.NonNull;

import com.gsclub.strategy.http.HttpHelper;
import com.gsclub.strategy.http.response.StrategyHttpResponse;
import com.gsclub.strategy.model.bean.ActivityImagesBean;
import com.gsclub.strategy.model.bean.ActivityShareBean;
import com.gsclub.strategy.model.bean.AppUpdateBean;
import com.gsclub.strategy.model.bean.BankBean;
import com.gsclub.strategy.model.bean.BannerBean;
import com.gsclub.strategy.model.bean.BillListBean;
import com.gsclub.strategy.model.bean.CashIndexBean;
import com.gsclub.strategy.model.bean.CreateSimulatedOrderBean;
import com.gsclub.strategy.model.bean.FeedbackImageBean;
import com.gsclub.strategy.model.bean.HomeMenuBean;
import com.gsclub.strategy.model.bean.HomePayMessageBean;
import com.gsclub.strategy.model.bean.IncomeListBean;
import com.gsclub.strategy.model.bean.MessageIndexBean;
import com.gsclub.strategy.model.bean.MessageSonBean;
import com.gsclub.strategy.model.bean.NewIndexBean;
import com.gsclub.strategy.model.bean.OptionalTopStockBean;
import com.gsclub.strategy.model.bean.PayInfoBean;
import com.gsclub.strategy.model.bean.PositionDetailBean;
import com.gsclub.strategy.model.bean.ProtocolBean;
import com.gsclub.strategy.model.bean.RankTradeBean;
import com.gsclub.strategy.model.bean.RankUserBean;
import com.gsclub.strategy.model.bean.RealTradingCommissionBean;
import com.gsclub.strategy.model.bean.RealTradingPositionBean;
import com.gsclub.strategy.model.bean.RealTradingSettlementBean;
import com.gsclub.strategy.model.bean.RechargeIndexBean;
import com.gsclub.strategy.model.bean.SaleStockOrderBean;
import com.gsclub.strategy.model.bean.SearchStockBean;
import com.gsclub.strategy.model.bean.SimulatedInfoBean;
import com.gsclub.strategy.model.bean.SimulatedTradingPositionBean;
import com.gsclub.strategy.model.bean.StockBean;
import com.gsclub.strategy.model.bean.StockSettleDetailBean;
import com.gsclub.strategy.model.bean.TradeIndexBean;
import com.gsclub.strategy.model.bean.UserBankBean;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.model.bean.UserOrderListBean;
import com.gsclub.strategy.model.bean.UserRegisterBean;
import com.gsclub.strategy.model.bean.UserTradeInfoBean;
import com.gsclub.strategy.model.bean.WebSetBean;
import com.gsclub.strategy.model.db.DBHelper;
import com.gsclub.strategy.prefs.PreferencesHelper;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.realm.RealmObject;
import io.realm.Sort;

/**
 * http db sharepreference 实现类
 */
public class DataManager implements HttpHelper, DBHelper, PreferencesHelper {

    private HttpHelper mHttpHelper;
    private DBHelper mDbHelper;
    private PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper, DBHelper dbHelper, PreferencesHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public String getImei() {
        return mPreferencesHelper.getImei();
    }

    @Override
    public void setImei(String imei) {
        mPreferencesHelper.setImei(imei);
    }

    @Override
    public String getOpenId() {
        return mPreferencesHelper.getOpenId();
    }

    @Override
    public void setUserInfo(UserIndexBean bean) {
        mPreferencesHelper.setUserInfo(bean);
    }

    @Override
    public Flowable<StrategyHttpResponse<WebSetBean>> getInfo(String url) {
        return mHttpHelper.getInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<OptionalTopStockBean>>> fetchTopStockInfo(String url) {
        return mHttpHelper.fetchTopStockInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserIndexBean>> fetchLoginInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchLoginInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> logout(String url) {
        return mHttpHelper.logout(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<ProtocolBean>> fetchProtocolInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchProtocolInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchVerificationCodeInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchVerificationCodeInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserRegisterBean>> login(String url, Map<String, Object> params) {
        return mHttpHelper.login(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserIndexBean>> register(String url, Map<String, Object> params) {
        return mHttpHelper.register(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> editPassword(String url, Map<String, Object> params) {
        return mHttpHelper.editPassword(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<IncomeListBean>> getIncomeList(String url, Map<String, Object> params) {
        return mHttpHelper.getIncomeList(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RankUserBean>> fetchIncomeTypesInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchIncomeTypesInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<SearchStockBean>>> fetchSearchStockList(String url, Map<String, Object> params) {
        return mHttpHelper.fetchSearchStockList(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockReal(String url, Map<String, Object> params) {
        return mHttpHelper.getStockReal(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockMinute(String url, Map<String, Object> params) {
        return mHttpHelper.getStockMinute(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockKLine(String url, Map<String, Object> params) {
        return mHttpHelper.getStockKLine(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockFiveDay(String url, Map<String, Object> params) {
        return mHttpHelper.getStockFiveDay(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> editUserInfo(String url, Map<String, Object> params) {
        return mHttpHelper.editUserInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<BannerBean>>> fetchBanner(String url, Map<String, Object> params) {
        return mHttpHelper.fetchBanner(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchUserMessage(String url) {
        return mHttpHelper.fetchUserMessage(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchAnnounceIndexInfo(String url) {
        return mHttpHelper.fetchAnnounceIndexInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<MessageSonBean>> fetchAnnounceListInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchAnnounceListInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<RankTradeBean>>> fetchRankTradeInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchRankTradeInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<HomeMenuBean>> fetchHomeMenuList(String url, Map<String, Object> params) {
        return mHttpHelper.fetchHomeMenuList(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<TradeIndexBean>> fetchTradeIndex(String url, Map<String, Object> params) {
        return mHttpHelper.fetchTradeIndex(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchStockReal(String url, Map<String, Object> params) {
        return mHttpHelper.fetchStockReal(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserTradeInfoBean>> fetchUserTradeInfo(String url) {
        return mHttpHelper.fetchUserTradeInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<BillListBean>> fetchBillsInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchBillsInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedInfoBean>> fetchUserSimulatedInfo(String url) {
        return mHttpHelper.fetchUserSimulatedInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserOrderListBean>> fetchUserOrderListInfo(String url, Map<String, Object> params) {
        return mHttpHelper.fetchUserOrderListInfo(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<CreateSimulatedOrderBean>> createSimulatedOrder(String url, Map<String, Object> params) {
        return mHttpHelper.createSimulatedOrder(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<SaleStockOrderBean>> saleStockOrder(String url, Map<String, Object> params) {
        return mHttpHelper.saleStockOrder(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<FeedbackImageBean>> uploadImage(String url, File image) {
        return mHttpHelper.uploadImage(url, image);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> uploadHeader(String url, File image) {
        return mHttpHelper.uploadHeader(url, image);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> feedback(String url, Map<String, Object> params) {
        return mHttpHelper.feedback(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> sendMsgCode(String url, Map<String, Object> params) {
        return mHttpHelper.sendMsgCode(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RankUserBean>> getMyRecord(String url) {
        return mHttpHelper.getMyRecord(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<RankTradeBean>>> getMyHistory(String url, Map<String, Object> params) {
        return mHttpHelper.getMyHistory(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> changeTradePass(String url, Map<String, Object> params) {
        return mHttpHelper.changeTradePass(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<PositionDetailBean>> fetchPositionDetail(String url, Map<String, Object> params) {
        return mHttpHelper.fetchPositionDetail(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<BankBean>>> getBankList(String url) {
        return mHttpHelper.getBankList(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<UserBankBean>> getBankCardInfo(String url) {
        return mHttpHelper.getBankCardInfo(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> bindBankCard(String url, Map<String, Object> params) {
        return mHttpHelper.bindBankCard(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RechargeIndexBean>> getRechargeIndex(String url) {
        return mHttpHelper.getRechargeIndex(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<PayInfoBean>> getRechargeOrder(String url, Map<String, Object> params) {
        return mHttpHelper.getRechargeOrder(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<CashIndexBean>> getCashIndex(String url) {
        return mHttpHelper.getCashIndex(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchData(String url, Map<String, Object> params) {
        return mHttpHelper.fetchData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<StockSettleDetailBean>> fetchStockSettleDetail(String url, Map<String, Object> params) {
        return mHttpHelper.fetchStockSettleDetail(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<HomePayMessageBean>>> fetchUserPayMessage(String url, Map<String, Object> params) {
        return mHttpHelper.fetchUserPayMessage(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getCashOrder(String url) {
        return mHttpHelper.getCashOrder(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getCashOrder(String url, Map<String, Object> params) {
        return mHttpHelper.getCashOrder(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> customPost(String url) {
        return mHttpHelper.customPost(url);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> customPost(String url, Map<String, Object> params) {
        return mHttpHelper.customPost(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<StockBean>> getStockSingle(String url, Map<String, Object> params) {
        return mHttpHelper.getStockSingle(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<List<StockBean>>> getStockList(String url, Map<String, Object> params) {
        return mHttpHelper.getStockList(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingCommissionBean>> getRealTradingCommissionData(String url, Map<String, Object> params) {
        return mHttpHelper.getRealTradingCommissionData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingPositionBean>> getRealTradingPositionData(String url, Map<String, Object> params) {
        return mHttpHelper.getRealTradingPositionData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingSettlementBean>> getRealTradingSettlementData(String url, Map<String, Object> params) {
        return mHttpHelper.getRealTradingSettlementData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedTradingPositionBean>> getSimulatedTradingPositionData(String url, Map<String, Object> params) {
        return mHttpHelper.getSimulatedTradingPositionData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedTradingSettlementBean>> getSimulatedTradingSettlementData(String url, Map<String, Object> params) {
        return mHttpHelper.getSimulatedTradingSettlementData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<NewIndexBean>> getNewIndex(String url, Map<String, Object> params) {
        return mHttpHelper.getNewIndex(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<AppUpdateBean>> checkUpdata(String url, Map<String, Object> params) {
        return mHttpHelper.checkUpdata(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<ActivityShareBean>> fetchActivityShareData(String url, Map<String, Object> params) {
        return mHttpHelper.fetchActivityShareData(url, params);
    }

    @Override
    public Flowable<StrategyHttpResponse<ActivityImagesBean>> getActivityImages(String url) {
        return mHttpHelper.getActivityImages(url);
    }

    @Override
    public List<? extends RealmObject> dbQueryList(@NonNull Class<? extends RealmObject> clazz, String queryField, Sort sortType) {
        return mDbHelper.dbQueryList(clazz, queryField, sortType);
    }

    @Override
    public List<? extends RealmObject> dbQueryEqualList(@NonNull Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String equal, Sort sortType) {
        return mDbHelper.dbQueryEqualList(clazz, queryField, equal, sortType);
    }

    @Override
    public void dbDeleteTable(Class<? extends RealmObject> clazz) {
        mDbHelper.dbDeleteTable(clazz);
    }

    @Override
    public void dbDeleteData(Class<? extends RealmObject> clazz, String queryStr, Object o) {
        mDbHelper.dbDeleteData(clazz, queryStr, o);
    }

    @Override
    public void dbInsertData(RealmObject data) {
        mDbHelper.dbInsertData(data);
    }

    @Override
    public RealmObject dbQueryDataByField(Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String queryData) {
        return mDbHelper.dbQueryDataByField(clazz, queryField, queryData);
    }
}
