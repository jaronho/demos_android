package com.gsclub.strategy.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.RGBColor;
import com.gsclub.strategy.app.StockField;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.model.bean.StockMinuteBean;
import com.gsclub.strategy.ui.home.adapter.StockUtils;
import com.gsclub.strategy.ui.stock.adapter.StockTradeRecyclerAdapter;
import com.gsclub.strategy.ui.stock.other.ChartUtil;
import com.gsclub.strategy.ui.stock.other.VolFormatter;
import com.gsclub.strategy.ui.stock.other.bean.DataParse;
import com.gsclub.strategy.ui.stock.other.bean.MinutesBean;
import com.gsclub.strategy.ui.stock.other.bean.StockTradeBean;
import com.gsclub.strategy.ui.stock.other.mychart.MyBarChart;
import com.gsclub.strategy.ui.stock.other.mychart.MyBottomMarkerView;
import com.gsclub.strategy.ui.stock.other.mychart.MyLeftMarkerView;
import com.gsclub.strategy.ui.stock.other.mychart.MyLineChart;
import com.gsclub.strategy.ui.stock.other.mychart.MyRightMarkerView;
import com.gsclub.strategy.ui.stock.other.mychart.MyXAxis;
import com.gsclub.strategy.ui.stock.other.mychart.MyYAxis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 股票分时详情弹窗
 */
public class StockMinuteDialog extends BaseDialog implements View.OnClickListener {
    MyXAxis xAxisLine;
    MyYAxis axisRightLine, axisLeftLine;
    BarDataSet barDataSet;
    MyXAxis xAxisBar;
    MyYAxis axisLeftBar, axisRightBar;
    SparseArray<String> stringSparseArray;
    StockTradeRecyclerAdapter buyAdapter, sellAdapter;
    private TextView tvStockName;
    private TextView tvStockSymbol;
    private TextView tvStockLastPrice;
    private TextView tvStockFloatPrecent;
    private MyLineChart lineChart;
    private MyBarChart barChart;
    private RecyclerView rcvBuy;
    private RecyclerView rcvSell;
    private DataParse mData;
    private float stockPreclosePx = -1.f;//股票昨收价

    private String symbol, stockRealData, stockMinuteData;
    private DecimalFormat df = new DecimalFormat("#.00");


    public static StockMinuteDialog newInstance() {
        StockMinuteDialog dialog = new StockMinuteDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setStockRealData(String stockRealData) {
        this.stockRealData = stockRealData;
    }

    public void setStockMinuteData(String stockMinuteData) {
        this.stockMinuteData = stockMinuteData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_stock_minute, container);
        ButterKnife.findById(view, R.id.v_close).setOnClickListener(this);

        tvStockName = view.findViewById(R.id.tv_stock_name);
        tvStockSymbol = view.findViewById(R.id.tv_stock_symbol);
        tvStockLastPrice = view.findViewById(R.id.tv_stock_last_price);
        tvStockFloatPrecent = view.findViewById(R.id.tv_stock_float_precent);
        lineChart = view.findViewById(R.id.line_chart);
        barChart = view.findViewById(R.id.bar_chart);
        rcvBuy = view.findViewById(R.id.rcv_buy);
        rcvSell = view.findViewById(R.id.rcv_sell);

        init();

        if (!TextUtils.isEmpty(symbol) && !TextUtils.isEmpty(stockRealData))
            showStockReal(stockRealData, symbol);

        if (!TextUtils.isEmpty(symbol) && !TextUtils.isEmpty(stockMinuteData))
            showMinute(stockMinuteData, symbol);

        return view;
    }

    private void init() {
        initRecyclerView();
        initChart();
        mData = new DataParse();
        stringSparseArray = getDefaultXLabels();
        setOnChartValueSelectedListener(lineChart, barChart);
        setOnChartValueSelectedListener(barChart, lineChart);

    }

    private void initRecyclerView() {
        buyAdapter = initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_red_f16262));
        sellAdapter = initLevelAdapter(getActivity(), getActivity().getResources().getColor(R.color.stock_green_73a848));

        initRecyclerView(getActivity(), rcvBuy, buyAdapter);
        initRecyclerView(getActivity(), rcvSell, sellAdapter);

        sellAdapter.getData().clear();
        sellAdapter.getData().addAll(getDefaultList());
        buyAdapter.getData().clear();
        buyAdapter.getData().addAll(getDefaultList());

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
        LineData cd = new LineData(getMinutesCount(), sets);
        lineChart.setData(cd);
        BarData barData = new BarData(getMinutesCount(), barDataSet);
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

    private void setShowLabels(SparseArray<String> labels) {
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

    public void showMinute(@NonNull String response, @NonNull String stockSymbol) {
        if (!TextUtils.isEmpty(response) && !TextUtils.isEmpty(stockSymbol) && response.contains(stockSymbol)) {
            String result = response.replace(stockSymbol, "stockCode");
            StockMinuteBean bean = GsonUtil.gsonToBean(result, StockMinuteBean.class);
            List<List<String>> list = bean.getData().getTrend().getStockCode();
            if (list == null || list.size() <= 0) return;
            mData.parseMinutes(list);
            setData(mData);
        }
    }

    public void showStockReal(@NonNull String data, @NonNull String stockSymbol) {
        JsonObject snapshot = GsonUtil.getJsonObject(data, "data", "snapshot");
        if (snapshot == null) return;
        if (!snapshot.has(stockSymbol)) return;

        JsonArray array = snapshot.get(stockSymbol).getAsJsonArray();

        float priceFloat = GsonUtil.arrayGetFloat(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE));
        float pricePrecent = GsonUtil.arrayGetFloat(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE_RATE));
        int color = getResources().getColor(StockUtils.getStockColor(priceFloat));

        stockPreclosePx = GsonUtil.arrayGetFloat(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.PRECLOSE_PX));

        tvStockName.setText(GsonUtil.arrayGetString(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.STOCK_NAME)));
        tvStockSymbol.setText(symbol);
        tvStockLastPrice.setText(String.valueOf(GsonUtil.arrayGetFloat(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.LAST_PX))));
        tvStockLastPrice.setTextColor(color);

        String symbol = priceFloat >= 0 ? "+" : "";
        String floatAndPrecent = String.format("%s%s %s%s%s", symbol, priceFloat, symbol, pricePrecent, "%");
        tvStockFloatPrecent.setText(floatAndPrecent);
        tvStockFloatPrecent.setTextColor(color);

        String buystr = GsonUtil.arrayGetString(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.BID_GRP));
        List<StockTradeBean> listBuy = mData.getTradeList(buystr, 1);

        String sellstr = GsonUtil.arrayGetString(array, GsonUtil.getStockFieldsPosition(snapshot, StockField.OFFER_GRP));
        List<StockTradeBean> listSell = mData.getTradeList(sellstr, 2);
        showFiveLevelData(buyAdapter, listBuy, color);
        showFiveLevelData(sellAdapter, listSell, color);
    }

    private void showFiveLevelData(StockTradeRecyclerAdapter adatper, List<StockTradeBean> list, int color) {
        adatper.setType(2);
        adatper.getData().clear();
        adatper.getData().addAll(list);
        adatper.setPriceColor(color);
        adatper.notifyDataSetChanged();
    }


    private void setOnChartValueSelectedListener(Chart one, final Chart two) {
        one.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                two.highlightValue(new Highlight(h.getXIndex(), 0));
            }

            @Override
            public void onNothingSelected() {
                two.highlightValue(null);
            }
        });
    }


    private SparseArray<String> getDefaultXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "");
        xLabels.put(60, "");
        xLabels.put(121, "");
        xLabels.put(182, "");
        xLabels.put(241, "");
        return xLabels;
    }

    private StockTradeRecyclerAdapter initLevelAdapter(Activity activity, int color) {
        StockTradeRecyclerAdapter adatper = new StockTradeRecyclerAdapter(activity);
        adatper.setData(new ArrayList<StockTradeBean>());
        adatper.setPriceColor(color);
        return adatper;
    }

    private void initRecyclerView(Activity activity, RecyclerView recyclerView, StockTradeRecyclerAdapter adatper) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adatper);
    }

    private List<StockTradeBean> getDefaultList() {
        List<StockTradeBean> list = new ArrayList<StockTradeBean>();
        for (int i = 0; i < 5; i++) {
            StockTradeBean bean = new StockTradeBean();
            bean.setTradeLevel("--");
            bean.setPrice(0.f);
            bean.setNumber(0.f);
            list.add(bean);
        }
        return list;
    }

    private String[] getMinutesCount() {
        //分时图上限为242条数据
        return new String[242];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                dismiss();
                break;
        }
    }
}
