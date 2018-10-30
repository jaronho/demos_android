package com.gsclub.strategy.http.api;

import com.gsclub.strategy.app.AppConfig;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StrategyApis {

    String HOST = AppConfig.API_IP;

    @POST("/")
    Flowable<StrategyHttpResponse<WebSetBean>> getInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<OptionalTopStockBean>>> fetchTopStockInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<UserIndexBean>> fetchLoginInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> logout(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<ProtocolBean>> fetchProtocolInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> fetchVerificationCodeInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<UserRegisterBean>> login(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<UserIndexBean>> register(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> editPassword(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<IncomeListBean>> getIncomeList(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<RankUserBean>> fetchIncomeTypesInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<SearchStockBean>>> fetchSearchStockList(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> getStockReal(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> getStockMinute(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> getStockKLine(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> getStockFiveDay(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> editUserInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<BannerBean>>> fetchBanner(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchUserMessage(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchAnnounceIndexInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<MessageSonBean>> fetchAnnounceListInfo(@Body Map<String, Object> params);

    @Multipart
    @POST("/")
    Flowable<StrategyHttpResponse<FeedbackImageBean>> uploadImage(@Part List<MultipartBody.Part> parts);

    @Multipart
    @POST("/")
    Flowable<StrategyHttpResponse<Object>> uploadHeader(@Part List<MultipartBody.Part> parts);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> feedback(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> sendMsgCode(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<RankTradeBean>>> fetchRankTradeInfo(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<HomeMenuBean>> fetchHomeMenuList(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<TradeIndexBean>> fetchTradeIndex(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> fetchStockReal(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<UserTradeInfoBean>> fetchUserTradeInfo(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<BillListBean>> fetchBillsInfo(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<RankUserBean>> getMyRecord(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<RankTradeBean>>> getMyHistory(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<SimulatedInfoBean>> fetchUserSimulatedInfo(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<UserOrderListBean>> fetchUserOrderListInfo(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<CreateSimulatedOrderBean>> createSimulatedOrder(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<SaleStockOrderBean>> saleStockOrder(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> changeTradePass(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<PositionDetailBean>> fetchPositionDetail(@Body Map<String, Object> stringObjectMap);

    @POST("/")
    Flowable<StrategyHttpResponse<List<BankBean>>> getBankList(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<UserBankBean>> getBankCardInfo(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> bindBankCard(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<RechargeIndexBean>> getRechargeIndex(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<PayInfoBean>> getRechargeOrder(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<CashIndexBean>> getCashIndex(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> fetchData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<StockSettleDetailBean>> fetchStockSettleDetail(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<HomePayMessageBean>>> fetchUserPayMessage(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> getCashOrder(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<Object>> customPost(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<StockBean>> getStockSingle(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<List<StockBean>>> getStockList(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<RealTradingCommissionBean>> getRealTradingCommissionData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<RealTradingPositionBean>> getRealTradingPositionData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<RealTradingSettlementBean>> getRealTradingSettlementData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<SimulatedTradingPositionBean>> getSimulatedTradingPositionData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<SimulatedTradingSettlementBean>> getSimulatedTradingSettlementData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<NewIndexBean>> getNewIndex(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<AppUpdateBean>> checkUpdata(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<ActivityShareBean>> fetchActivityShareData(@Body Map<String, Object> params);

    @POST("/")
    Flowable<StrategyHttpResponse<ActivityImagesBean>> getActivityImages(@Body Map<String, Object> params);
}
