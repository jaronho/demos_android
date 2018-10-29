package com.liyuu.strategy.http;

import com.liyuu.strategy.http.response.StrategyHttpResponse;
import com.liyuu.strategy.model.SimulatedTradingSettlementBean;
import com.liyuu.strategy.model.bean.ActivityImagesBean;
import com.liyuu.strategy.model.bean.ActivityShareBean;
import com.liyuu.strategy.model.bean.AppUpdateBean;
import com.liyuu.strategy.model.bean.BankBean;
import com.liyuu.strategy.model.bean.BannerBean;
import com.liyuu.strategy.model.bean.BillListBean;
import com.liyuu.strategy.model.bean.CashIndexBean;
import com.liyuu.strategy.model.bean.CreateSimulatedOrderBean;
import com.liyuu.strategy.model.bean.FeedbackImageBean;
import com.liyuu.strategy.model.bean.HomeMenuBean;
import com.liyuu.strategy.model.bean.HomePayMessageBean;
import com.liyuu.strategy.model.bean.IncomeListBean;
import com.liyuu.strategy.model.bean.MessageIndexBean;
import com.liyuu.strategy.model.bean.MessageSonBean;
import com.liyuu.strategy.model.bean.NewIndexBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.model.bean.PayInfoBean;
import com.liyuu.strategy.model.bean.PositionDetailBean;
import com.liyuu.strategy.model.bean.ProtocolBean;
import com.liyuu.strategy.model.bean.RankTradeBean;
import com.liyuu.strategy.model.bean.RankUserBean;
import com.liyuu.strategy.model.bean.RealTradingCommissionBean;
import com.liyuu.strategy.model.bean.RealTradingPositionBean;
import com.liyuu.strategy.model.bean.RealTradingSettlementBean;
import com.liyuu.strategy.model.bean.RechargeIndexBean;
import com.liyuu.strategy.model.bean.SaleStockOrderBean;
import com.liyuu.strategy.model.bean.SearchStockBean;
import com.liyuu.strategy.model.bean.SimulatedInfoBean;
import com.liyuu.strategy.model.bean.SimulatedTradingPositionBean;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.model.bean.StockSettleDetailBean;
import com.liyuu.strategy.model.bean.TradeIndexBean;
import com.liyuu.strategy.model.bean.UserBankBean;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.model.bean.UserOrderListBean;
import com.liyuu.strategy.model.bean.UserRegisterBean;
import com.liyuu.strategy.model.bean.UserTradeInfoBean;
import com.liyuu.strategy.model.bean.WebSetBean;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * http 请求接口
 */
public interface HttpHelper {

    Flowable<StrategyHttpResponse<WebSetBean>> getInfo(String url);

    Flowable<StrategyHttpResponse<List<OptionalTopStockBean>>> fetchTopStockInfo(String url);

    Flowable<StrategyHttpResponse<UserIndexBean>> fetchLoginInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> logout(String url);

    Flowable<StrategyHttpResponse<ProtocolBean>> fetchProtocolInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> fetchVerificationCodeInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<UserRegisterBean>> login(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<UserIndexBean>> register(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> editPassword(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<IncomeListBean>> getIncomeList(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RankUserBean>> fetchIncomeTypesInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<SearchStockBean>>> fetchSearchStockList(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> getStockReal(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> getStockMinute(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> getStockKLine(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> getStockFiveDay(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> editUserInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<BannerBean>>> fetchBanner(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchUserMessage(String url);

    Flowable<StrategyHttpResponse<List<MessageIndexBean>>> fetchAnnounceIndexInfo(String url);

    Flowable<StrategyHttpResponse<MessageSonBean>> fetchAnnounceListInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<FeedbackImageBean>> uploadImage(String url, File image);

    Flowable<StrategyHttpResponse<Object>> uploadHeader(String url, File image);

    Flowable<StrategyHttpResponse<Object>> feedback(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> sendMsgCode(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<RankTradeBean>>> fetchRankTradeInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<HomeMenuBean>> fetchHomeMenuList(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<TradeIndexBean>> fetchTradeIndex(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> fetchStockReal(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<UserTradeInfoBean>> fetchUserTradeInfo(String url);

    Flowable<StrategyHttpResponse<BillListBean>> fetchBillsInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RankUserBean>> getMyRecord(String url);

    Flowable<StrategyHttpResponse<List<RankTradeBean>>> getMyHistory(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<SimulatedInfoBean>> fetchUserSimulatedInfo(String url);

    Flowable<StrategyHttpResponse<UserOrderListBean>> fetchUserOrderListInfo(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<CreateSimulatedOrderBean>> createSimulatedOrder(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<SaleStockOrderBean>> saleStockOrder(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> changeTradePass(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<PositionDetailBean>> fetchPositionDetail(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<BankBean>>> getBankList(String url);

    Flowable<StrategyHttpResponse<UserBankBean>> getBankCardInfo(String url);

    Flowable<StrategyHttpResponse<Object>> bindBankCard(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RechargeIndexBean>> getRechargeIndex(String url);

    Flowable<StrategyHttpResponse<PayInfoBean>> getRechargeOrder(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<CashIndexBean>> getCashIndex(String url);

    Flowable<StrategyHttpResponse<Object>> fetchData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<StockSettleDetailBean>> fetchStockSettleDetail(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<HomePayMessageBean>>> fetchUserPayMessage(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> getCashOrder(String url);

    Flowable<StrategyHttpResponse<Object>> getCashOrder(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<Object>> customPost(String url);

    Flowable<StrategyHttpResponse<Object>> customPost(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<StockBean>> getStockSingle(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<List<StockBean>>> getStockList(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RealTradingCommissionBean>> getRealTradingCommissionData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RealTradingPositionBean>> getRealTradingPositionData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<RealTradingSettlementBean>> getRealTradingSettlementData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<SimulatedTradingPositionBean>> getSimulatedTradingPositionData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<SimulatedTradingSettlementBean>> getSimulatedTradingSettlementData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<NewIndexBean>> getNewIndex(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<AppUpdateBean>> checkUpdata(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<ActivityShareBean>> fetchActivityShareData(String url, Map<String, Object> params);

    Flowable<StrategyHttpResponse<ActivityImagesBean>> getActivityImages(String url);
}
