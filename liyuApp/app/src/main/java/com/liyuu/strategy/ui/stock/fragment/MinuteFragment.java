package com.liyuu.strategy.ui.stock.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.RGBColor;
import com.liyuu.strategy.app.StockField;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.stock.MinuteStockContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.bean.StockMinuteBean;
import com.liyuu.strategy.presenter.stock.MinuteStockPresenter;
import com.liyuu.strategy.ui.home.adapter.StockUtils;
import com.liyuu.strategy.ui.stock.adapter.StockTradeRecyclerAdapter;
import com.liyuu.strategy.ui.stock.other.ChartUtil;
import com.liyuu.strategy.ui.stock.other.VolFormatter;
import com.liyuu.strategy.ui.stock.other.bean.DataParse;
import com.liyuu.strategy.ui.stock.other.bean.MinutesBean;
import com.liyuu.strategy.ui.stock.other.bean.StockTradeBean;
import com.liyuu.strategy.ui.stock.other.mychart.MyBarChart;
import com.liyuu.strategy.ui.stock.other.mychart.MyBottomMarkerView;
import com.liyuu.strategy.ui.stock.other.mychart.MyLeftMarkerView;
import com.liyuu.strategy.ui.stock.other.mychart.MyLineChart;
import com.liyuu.strategy.ui.stock.other.mychart.MyRightMarkerView;
import com.liyuu.strategy.ui.stock.other.mychart.MyXAxis;
import com.liyuu.strategy.ui.stock.other.mychart.MyYAxis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MinuteFragment extends BaseFragment<MinuteStockPresenter> implements MinuteStockContract.View {
    private static final String STOCK_SYMBOL_STRING = "stock_symbol_string";
    @BindView(R.id.line_chart)
    MyLineChart lineChart;
    @BindView(R.id.bar_chart)
    MyBarChart barChart;
    @BindView(R.id.rcv_buy)
    RecyclerView rcvBuy;
    @BindView(R.id.rcv_sell)
    RecyclerView rcvSell;
    @BindView(R.id.tv_load_failed_notify)
    TextView tvLoadFailed;
    MyXAxis xAxisLine;
    MyYAxis axisRightLine, axisLeftLine;
    BarDataSet barDataSet;
    MyXAxis xAxisBar;
    MyYAxis axisLeftBar, axisRightBar;
    SparseArray<String> stringSparseArray;
    StockTradeRecyclerAdapter buyAdapter, sellAdapter;
    private DataParse mData;
    private String stockSymbol;
    private float stockPreclosePx = -1.f;//股票昨收价

    public static MinuteFragment newInstance(String stockSymbol) {
        MinuteFragment fragment = new MinuteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STOCK_SYMBOL_STRING, stockSymbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_minute;
    }

    @Override
    protected void initEventAndData() {
        stockSymbol = getArguments().getString(STOCK_SYMBOL_STRING);
        initRecyclerView();
        initChart();
        mData = new DataParse();
        stringSparseArray = mPresenter.getDefaultXLabels();
        mPresenter.setOnChartValueSelectedListener(lineChart, barChart);
        mPresenter.setOnChartValueSelectedListener(barChart, lineChart);

        if (getActivity() != null) {
            mPresenter.getStockReal(stockSymbol, mData);
        }
    }


    @OnClick({R.id.tv_load_failed_notify})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_load_failed_notify:
//                if (NoDoubleClickUtils.isDoubleClick()) return;
                tvLoadFailed.setVisibility(View.GONE);
//                addMinute();
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                if (!TextUtils.isEmpty(stockSymbol))
                    mPresenter.getStockReal(stockSymbol, mData);
                break;
        }
    }

    private void initRecyclerView() {
//        buyAdapter = mPresenter.initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_red_f16262));
//        sellAdapter = mPresenter.initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_green_73a848));

        buyAdapter = mPresenter.initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_red_f16262));
        sellAdapter = mPresenter.initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_red_f16262));

        mPresenter.initRecyclerView(getActivity(), rcvBuy, buyAdapter);
        mPresenter.initRecyclerView(getActivity(), rcvSell, sellAdapter);

        sellAdapter.getData().clear();
        sellAdapter.getData().addAll(mPresenter.getDefaultList());
        buyAdapter.getData().clear();
        buyAdapter.getData().addAll(mPresenter.getDefaultList());

        buyAdapter.notifyDataSetChanged();
        sellAdapter.notifyDataSetChanged();
    }

    private void initChart() {
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(false);
        lineChart.setBorderWidth(1);
        lineChart.setBorderColor(getResources().getColor(R.color.transparent));
        lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setDescription("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(false);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.transparent));
        barChart.setViewPortOffsets(0, 0, 0, 0);
        barChart.setDescription("");


        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);


        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

//        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.transparent));
        xAxisLine.enableGridDashedLine(10f, 5f, 0f);
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.transparent));
        xAxisLine.setTextColor(getResources().getColor(R.color.transparent));
        axisLeftLine.setGridColor(getResources().getColor(R.color.transparent));
        axisLeftLine.setTextColor(getResources().getColor(R.color.transparent));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.transparent));
        axisRightLine.setTextColor(getResources().getColor(R.color.transparent));

        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.transparent));
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.transparent));


        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

    }


    @SuppressLint("ResourceType")
    private void setData(DataParse mData) {
        if (getActivity() == null) return;
        setMarkerView(mData);
        setShowLabels(stringSparseArray);
        if (mData.getDatas().size() == 0) {
            lineChart.setNoDataText(getString(R.string.no_data));
            return;
        }
        //设置y左右两轴最大最小值
        float maxValue, minValue;
        maxValue = stockPreclosePx * 1.11f;
        minValue = stockPreclosePx * 0.91f;
//        axisLeftLine.setAxisMinValue(minValue);
//        axisLeftLine.setAxisMaxValue(maxValue);
        axisRightLine.setAxisMinValue(minValue);
        axisRightLine.setAxisMaxValue(maxValue);


        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = StockUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        axisLeftBar.setShowMaxAndUnit(unit);
        axisLeftBar.setDrawLabels(true);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar.setAxisMaxValue(mData.getVolmax());
        //   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
        //axisRightBar.setShowOnlyMinMax(true);

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getActivity().getResources().getColor(R.color.transparent));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0, j = 0; i < mData.getDatas().size(); i++, j++) {
            MinutesBean t = mData.getDatas().get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }

            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }

            lineCJEntries.add(new Entry(mData.getDatas().get(i).cjprice, i));
            lineJJEntries.add(new Entry(mData.getDatas().get(i).avprice, i));

            int chooseColor = 0;
            if (i > 1) {
                float f = mData.getDatas().get(i).cjprice - mData.getDatas().get(i - 1).cjprice;
                if (f > 0) chooseColor = 0;
                else if (f < 0) chooseColor = 1;
                else chooseColor = (int) barEntries.get(i - 1).getData();
            }
            barEntries.add(new BarEntry(mData.getDatas().get(i).cjnum, i, chooseColor));
            // dateList.add(mData.getDatas().get(i).time);
        }
        LineDataSet d1 = ChartUtil.getLineDataSet(getActivity(), lineCJEntries, "成交价",
                getActivity().getResources().getColor(R.color.stock_line_blue), true);


        LineDataSet d2 = ChartUtil.getLineDataSet(getActivity(), lineJJEntries, "均价",
                getActivity().getResources().getColor(R.color.stock_line_yellow), false);

        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(45); //bar空隙
        barDataSet.setHighLightColor(getActivity().getResources().getColor(R.color.transparent));
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(false);
        barDataSet.setColors(RGBColor.STOCKS_COLORS_CONTRARY);
        //谁为基准
//        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(mPresenter.getMinutesCount(), sets);
        lineChart.setData(cd);
        BarData barData = new BarData(mPresenter.getMinutesCount(), barDataSet);
        barChart.setData(barData);

        setOffset();
        lineChart.invalidate();//刷新图
        barChart.invalidate();

    }


    /*设置量表对齐*/
    private void setOffset() {
        if (getActivity() == null) return;
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft, transRight;
        /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            lineChart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

        /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            lineChart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }

    public void setShowLabels(SparseArray<String> labels) {
        if (getActivity() == null) return;
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }

    private void setMarkerView(DataParse mData) {
        if (getActivity() == null) return;
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(getActivity(), R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(getActivity(), R.layout.mymarkerview);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(getActivity(), R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, bottomMarkerView, mData);
        barChart.setMarker(leftMarkerView, rightMarkerView, bottomMarkerView, mData);
    }

    @Override
    public void showMinute(String response) {
        if (response == null || getActivity() == null)
            return;
        if (!response.contains(stockSymbol))
            return;
        String result = response.replace(stockSymbol, "stockCode");
        StockMinuteBean bean = GsonUtil.gsonToBean(result, StockMinuteBean.class);
        List<List<String>> list = bean.getData().getTrend().getStockCode();
        if (list == null || list.size() <= 0) return;
        mData.parseMinutes(list);
        setData(mData);
    }

    @Override
    public void showFiveLevelData(String data) {
        mPresenter.getMinuteStock(stockSymbol);
        JsonObject snapshot = GsonUtil.getJsonObject(data, "data", "snapshot");
        if (snapshot == null) return;
        if (!snapshot.has(stockSymbol)) return;

        JsonArray array = snapshot.get(stockSymbol).getAsJsonArray();

        int position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE);
        double pxChange = GsonUtil.arrayGetDouble(array, position, 2);
        int color = getResources().getColor(pxChange >= 0 ? R.color.stock_red_color : R.color.stock_green_color);

        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.BID_GRP);
        String buystr = GsonUtil.arrayGetString(array, position);
        List<StockTradeBean> listBuy = mData.getTradeList(buystr, 1);

        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.OFFER_GRP);
        String sellstr = GsonUtil.arrayGetString(array, position);
        List<StockTradeBean> listSell = mData.getTradeList(sellstr, 2);
        showFiveLevelData(buyAdapter, listBuy, color);
        showFiveLevelData(sellAdapter, listSell, color);

        stockPreclosePx = GsonUtil.arrayGetFloat(array, 4);
    }

    private void showFiveLevelData(StockTradeRecyclerAdapter adatper, List<StockTradeBean> list, int color) {
        adatper.getData().clear();
        adatper.getData().addAll(list);
        adatper.setPriceColor(color);
        adatper.notifyDataSetChanged();
    }

}