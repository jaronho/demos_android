package com.gsclub.strategy.http;

import android.os.Build;
import android.util.Log;

import com.gsclub.strategy.BuildConfig;
import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.AppConfig;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.http.api.StrategyApis;
import com.gsclub.strategy.http.response.StrategyHttpResponse;
import com.gsclub.strategy.model.SimulatedTradingSettlementBean;
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
import com.gsclub.strategy.model.bean.StockBean;
import com.gsclub.strategy.model.bean.SimulatedTradingPositionBean;
import com.gsclub.strategy.model.bean.StockSettleDetailBean;
import com.gsclub.strategy.model.bean.TradeIndexBean;
import com.gsclub.strategy.model.bean.UserBankBean;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.model.bean.UserOrderListBean;
import com.gsclub.strategy.model.bean.UserRegisterBean;
import com.gsclub.strategy.model.bean.UserTradeInfoBean;
import com.gsclub.strategy.model.bean.WebSetBean;
import com.gsclub.strategy.util.AES;
import com.gsclub.strategy.util.DateUtil;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * http 请求实现类
 */
public class RetrofitHelper implements HttpHelper {

    private StrategyApis strategyApiService;

    @Inject
    public RetrofitHelper(StrategyApis strategyApiService) {
        this.strategyApiService = strategyApiService;
    }

    private List<MultipartBody.Part> getParts(String url, File image) {
        Map<String, Object> params = urlToParam(url);
        String oriParam = transformers(addParams(params));
        String en_data = AES.encrypt(oriParam, AppConfig.AES_KEY);
        LogUtil.d(LogUtil.TAG, AppConfig.API_IP + " : " + url + " : " + oriParam);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("version", BuildConfig.APP_VERSION)
                .addFormDataPart("en_key", AppConfig.EN_KEY)
                .addFormDataPart("device_type", AppConfig.DEVICE_TYPE)
                .addFormDataPart("en_data", en_data)
                .addFormDataPart("opact", url)
                .addFormDataPart("image", image.getName(),
                        RequestBody.create(MediaType.parse("image/png"), image));
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    protected final Map<String, Object> addParams(Map<String, Object> pair) {
        if (pair == null) pair = new HashMap<>();
        pair.put("tms", DateUtil.getNowTime());
        pair.put("imei", PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID));
        pair.put("platform", AppConfig.PLATFORM);
        pair.put("source", App.getInstance().getAppChannel());
        String appModel = Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
        pair.put("app_model", appModel);
//        pair.put("xgpush_device", CommonUtils.getPushToken());
        return pair;
    }

    private String transformers(Map<String, ?> params) {
        JSONObject object = new JSONObject();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            try {
                object.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

    private Map<String, Object> urlToParam(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("opact", url);
        return params;
    }

    private Map<String, Object> urlAddParam(String url, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("opact", url);
        return params;
    }

    @Override
    public Flowable<StrategyHttpResponse<WebSetBean>> getInfo(String url) {
        return strategyApiService.getInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<OptionalTopStockBean>>> fetchTopStockInfo(String url) {
        return strategyApiService.fetchTopStockInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserIndexBean>> fetchLoginInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchLoginInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> logout(String url) {
        return strategyApiService.logout(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<ProtocolBean>> fetchProtocolInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchProtocolInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchVerificationCodeInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchVerificationCodeInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserRegisterBean>> login(String url, Map<String, Object> params) {
        return strategyApiService.login(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserIndexBean>> register(String url, Map<String, Object> params) {
        return strategyApiService.register(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> editPassword(String url, Map<String, Object> params) {
        return strategyApiService.editPassword(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<IncomeListBean>> getIncomeList(String url, Map<String, Object> params) {
        return strategyApiService.getIncomeList(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RankUserBean>> fetchIncomeTypesInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchIncomeTypesInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<SearchStockBean>>> fetchSearchStockList(String url, Map<String, Object> params) {
        return strategyApiService.fetchSearchStockList(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockReal(String url, Map<String, Object> params) {
        return strategyApiService.getStockReal(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockMinute(String url, Map<String, Object> params) {
        return strategyApiService.getStockMinute(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockKLine(String url, Map<String, Object> params) {
        return strategyApiService.getStockKLine(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getStockFiveDay(String url, Map<String, Object> params) {
        return strategyApiService.getStockFiveDay(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> editUserInfo(String url, Map<String, Object> params) {
        return strategyApiService.editUserInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<BannerBean>>> fetchBanner(String url, Map<String, Object> params) {
        return strategyApiService.fetchBanner(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchUserMessage(String url) {
        return strategyApiService.fetchUserMessage(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchAnnounceIndexInfo(String url) {
        return strategyApiService.fetchAnnounceIndexInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<MessageSonBean>> fetchAnnounceListInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchAnnounceListInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<RankTradeBean>>> fetchRankTradeInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchRankTradeInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<HomeMenuBean>> fetchHomeMenuList(String url, Map<String, Object> params) {
        return strategyApiService.fetchHomeMenuList(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<TradeIndexBean>> fetchTradeIndex(String url, Map<String, Object> params) {
        return strategyApiService.fetchTradeIndex(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchStockReal(String url, Map<String, Object> params) {
        return strategyApiService.fetchStockReal(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserTradeInfoBean>> fetchUserTradeInfo(String url) {
        return strategyApiService.fetchUserTradeInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<BillListBean>> fetchBillsInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchBillsInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedInfoBean>> fetchUserSimulatedInfo(String url) {
        return strategyApiService.fetchUserSimulatedInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserOrderListBean>> fetchUserOrderListInfo(String url, Map<String, Object> params) {
        return strategyApiService.fetchUserOrderListInfo(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<CreateSimulatedOrderBean>> createSimulatedOrder(String url, Map<String, Object> params) {
        return strategyApiService.createSimulatedOrder(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<SaleStockOrderBean>> saleStockOrder(String url, Map<String, Object> params) {
        return strategyApiService.saleStockOrder(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<FeedbackImageBean>> uploadImage(String url, File image) {
        List<MultipartBody.Part> parts = getParts(url, image);
        return strategyApiService.uploadImage(parts);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> uploadHeader(String url, File image) {
        List<MultipartBody.Part> parts = getParts(url, image);
        return strategyApiService.uploadHeader(parts);
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> feedback(String url, Map<String, Object> params) {
        return strategyApiService.feedback(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> sendMsgCode(String url, Map<String, Object> params) {
        return strategyApiService.sendMsgCode(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RankUserBean>> getMyRecord(String url) {
        return strategyApiService.getMyRecord(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<RankTradeBean>>> getMyHistory(String url, Map<String, Object> params) {
        return strategyApiService.getMyHistory(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> changeTradePass(String url, Map<String, Object> params) {
        return strategyApiService.changeTradePass(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<PositionDetailBean>> fetchPositionDetail(String url, Map<String, Object> params) {
        return strategyApiService.fetchPositionDetail(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<BankBean>>> getBankList(String url) {
        return strategyApiService.getBankList(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<UserBankBean>> getBankCardInfo(String url) {
        return strategyApiService.getBankCardInfo(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> bindBankCard(String url, Map<String, Object> params) {
        return strategyApiService.bindBankCard(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RechargeIndexBean>> getRechargeIndex(String url) {
        return strategyApiService.getRechargeIndex(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<PayInfoBean>> getRechargeOrder(String url, Map<String, Object> params) {
        return strategyApiService.getRechargeOrder(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<CashIndexBean>> getCashIndex(String url) {
        return strategyApiService.getCashIndex(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> fetchData(String url, Map<String, Object> params) {
        return strategyApiService.fetchData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<StockSettleDetailBean>> fetchStockSettleDetail(String url, Map<String, Object> params) {
        return strategyApiService.fetchStockSettleDetail(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<HomePayMessageBean>>> fetchUserPayMessage(String url, Map<String, Object> params) {
        return strategyApiService.fetchUserPayMessage(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getCashOrder(String url) {
        return strategyApiService.getCashOrder(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> getCashOrder(String url, Map<String, Object> params) {
        return strategyApiService.getCashOrder(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> customPost(String url) {
        return strategyApiService.customPost(urlToParam(url));
    }

    @Override
    public Flowable<StrategyHttpResponse<Object>> customPost(String url, Map<String, Object> params) {
        return strategyApiService.customPost(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<StockBean>> getStockSingle(String url, Map<String, Object> params) {
        return strategyApiService.getStockSingle(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<List<StockBean>>> getStockList(String url, Map<String, Object> params) {
        return strategyApiService.getStockList(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingCommissionBean>> getRealTradingCommissionData(String url, Map<String, Object> params) {
        return strategyApiService.getRealTradingCommissionData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingPositionBean>> getRealTradingPositionData(String url, Map<String, Object> params) {
        return strategyApiService.getRealTradingPositionData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<RealTradingSettlementBean>> getRealTradingSettlementData(String url, Map<String, Object> params) {
        return strategyApiService.getRealTradingSettlementData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedTradingPositionBean>> getSimulatedTradingPositionData(String url, Map<String, Object> params) {
        return strategyApiService.getSimulatedTradingPositionData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<SimulatedTradingSettlementBean>> getSimulatedTradingSettlementData(String url, Map<String, Object> params) {
        return strategyApiService.getSimulatedTradingSettlementData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<NewIndexBean>> getNewIndex(String url, Map<String, Object> params) {
        return strategyApiService.getNewIndex(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<AppUpdateBean>> checkUpdata(String url, Map<String, Object> params) {
        return strategyApiService.checkUpdata(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<ActivityShareBean>> fetchActivityShareData(String url, Map<String, Object> params) {
        return strategyApiService.fetchActivityShareData(urlAddParam(url, params));
    }

    @Override
    public Flowable<StrategyHttpResponse<ActivityImagesBean>> getActivityImages(String url) {
        return strategyApiService.getActivityImages(urlToParam(url));
    }
}
