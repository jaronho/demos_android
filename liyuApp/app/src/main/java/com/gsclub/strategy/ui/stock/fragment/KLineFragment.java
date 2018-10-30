package com.gsclub.strategy.ui.stock.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.RGBColor;
import com.gsclub.strategy.base.BaseFragment;
import com.gsclub.strategy.contract.stock.KLineStockContract;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.presenter.stock.KLinePresenter;
import com.gsclub.strategy.ui.home.adapter.StockUtils;
import com.gsclub.strategy.ui.stock.other.VolFormatter;
import com.gsclub.strategy.ui.stock.other.bean.DataParse;
import com.gsclub.strategy.ui.stock.other.bean.StockKLineBean;
import com.gsclub.strategy.ui.stock.other.mychart.CoupleChartGestureListener;
import com.gsclub.strategy.util.TimeUtil;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class KLineFragment extends BaseFragment<KLinePresenter> implements KLineStockContract.View, View.OnTouchListener {

    private static final String KLINETYPE_INT = "klinetype_int";
    private static final String STOCK_SYMBOL_STRING = "stock_symbol_string";
    private final static int PER_LOAD_NUMBER = 200;//每次加载的k线数据
    private static boolean isLoadingState = false;//是否正在加载
    @BindView(R.id.combinedchart)
    CombinedChart combinedchart;
    @BindView(R.id.barchart)
    BarChart barChart;
    @BindView(R.id.tv_c1_top)
    TextView tvC1Top;
    @BindView(R.id.tv_c1_bottom)
    TextView tvC1Bottom;
    @BindView(R.id.tv_bar_top)
    TextView tvBarTop;
    @BindView(R.id.tv_chart_load_more)
    TextView tvChartLoadMore;
    @BindView(R.id.tv_bar_load_more)
    TextView tvBarLoadMore;
    @BindView(R.id.tv_load_failed_notify)
    TextView tvLoadFailed;
    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
    @BindView(R.id.tv_time_center)
    TextView tvTimeCenter;
    @BindView(R.id.tv_time_right)
    TextView tvTimeRight;
    @BindView(R.id.tv_ma5)
    TextView tvMa5;
    @BindView(R.id.tv_ma10)
    TextView tvMa10;
    @BindView(R.id.tv_ma20)
    TextView tvMa20;
    XAxis xAxisBar, xAxisK;
    YAxis axisLeftBar, axisLeftK;
    YAxis axisRightBar, axisRightK;
    BarDataSet barDataSet;
    float sum = 0;
    private DataParse mData;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private boolean isCanLoadMore = true;//是否能加载更多
    private int moveLoadNumber = PER_LOAD_NUMBER;//加载完成，移动k线的个数
    private int viewMixWidthNum = 36;//图像显示的柱状体在界面中的最少时个数（用于显示图像比例）
    private int maxPosition = -1;
    private PlayHandler handler;

    private String stockSymbol;

    public static KLineFragment newInstance(int klineType, String stockSymbol) {
        KLineFragment fragmentOne = new KLineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KLINETYPE_INT, klineType);
        bundle.putString(STOCK_SYMBOL_STRING, stockSymbol);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_kline;
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() == null)
            return;
        handler = new PlayHandler(this);
        int klineType = getArguments().getInt(KLINETYPE_INT);
        initChart();
        combinedchart.setOnTouchListener(this);
        mData = new DataParse();

        stockSymbol = getArguments().getString(STOCK_SYMBOL_STRING);
        mPresenter.queryKLine(stockSymbol, klineType, moveLoadNumber);
    }

    /**
     * 移动图表
     */
    private void moveView() {
        if (getActivity() == null) return;
        int size = mData.getKLineDatas().size();
        if (size > moveLoadNumber)
            size = moveLoadNumber;
        combinedchart.moveViewToX(size);
        barChart.moveViewToX(size);
        isLoadingState = false;

        if (mData.getKLineDatas().size() <= moveLoadNumber)
            showOtherData();
    }

    @OnClick({R.id.tv_load_failed_notify})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_load_failed_notify:
//                if (NoDoubleClickUtils.isDoubleClick()) return;
                tvLoadFailed.setVisibility(View.GONE);
//                addKLine();
                break;
        }
    }

    private void initChart() {
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.transparent));
        barChart.setDescription("");
        barChart.setDragEnabled(true);
        barChart.setViewPortOffsets(0, 0, 0, 0);
        barChart.setScaleYEnabled(false);

        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setEnabled(false);
        xAxisBar.setDrawLabels(true);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setTextColor(getResources().getColor(R.color.transparent));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.transparent));

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setEnabled(false);
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.transparent));
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setSpaceTop(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);

        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(getResources().getColor(R.color.transparent));
        combinedchart.setDescription("");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setViewPortOffsets(0, 0, 0, 0);

        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setEnabled(false);
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(getResources().getColor(R.color.transparent));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(getResources().getColor(R.color.transparent));

        //k线图Y轴数字
        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setEnabled(false);
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setTextColor(getResources().getColor(R.color.transparent));
        axisLeftK.setGridColor(getResources().getColor(R.color.transparent));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        axisRightK = combinedchart.getAxisRight();
        axisRightK.setEnabled(false);
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(true);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(getResources().getColor(R.color.transparent));
        combinedchart.setDragDecelerationEnabled(true);
        barChart.setDragDecelerationEnabled(true);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);
        barChart.setDragDecelerationFrictionCoef(0.2f);


        // 将K线控的滑动事件传递给交易量控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));
        // 将交易量控件的滑动事件传递给K线控件
        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{combinedchart}));
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                combinedchart.highlightValues(new Highlight[]{h});
                showMa(h.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
            }
        });
        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                barChart.highlightValues(new Highlight[]{h});
                showMa(h.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
            }
        });
    }

    private float getSum(Integer a, Integer b) {

        for (int i = a; i <= b; i++) {
            sum += mData.getKLineDatas().get(i).close;
        }
        return sum;
    }

    private void setData(DataParse mData) {
        if (getActivity() == null) return;
        int size = mData.getKLineDatas().size();   //点的个数
        // axisLeftBar.setAxisMaxValue(mData.getVolmax());
        String unit = StockUtils.getVolUnit(mData.getVolmax() / 100.f);
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        // axisRightBar.setAxisMaxValue(mData.getVolmax());

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line20Entries = new ArrayList<>();
        for (int i = 0; i < mData.getKLineDatas().size(); i++) {
            int chooseColor = 0;
            if (i > 1) {
                float f = mData.getKLineDatas().get(i).open - mData.getKLineDatas().get(i).close;
                if (f > 0)
                    chooseColor = 0;//对应RGBColor第1个颜色
                else if (f < 0)
                    chooseColor = 1;//对应RGBColor第2个颜色
                else
                    chooseColor = (int) barEntries.get(i - 1).getData();
                //open与close值一致时，显示红色(与ios保持一致)
//                    chooseColor = 1;
            }
            xVals.add(mData.getKLineDatas().get(i).date + "");
            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i, chooseColor));
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close, chooseColor));
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry(getSum(i - 4, i) / 5, i));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry(getSum(i - 9, i) / 10, i));
            }
            if (i >= 19) {
                sum = 0;
                line20Entries.add(new Entry(getSum(i - 19, i) / 20, i));
            }
        }
        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setColors(RGBColor.STOCKS_COLORS);
        barDataSet.setBarSpacePercent(20); //bar空隙
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(xVals, barDataSet);
        barChart.setData(barData);

//        final ViewPortHandler viewPortHandlerBar = barChart.getViewPortHandler();
//        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size()));
//        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
//        final float xscale = 3;
//        touchmatrix.postScale(xscale, 1f);


        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setValueTextColor(Color.BLACK);
        candleDataSet.setDrawValues(true);
        candleDataSet.setColors(RGBColor.STOCKS_COLORS);
//        candleDataSet.setColor(getResources().getColor(R.color.kline_red_color));
        candleDataSet.setDecreasingColor(getResources().getColor(R.color.stock_green_color));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);

        candleDataSet.setIncreasingColor(getResources().getColor(R.color.stock_red_color));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);

//        candleDataSet.setNeutralColor(getResources().getColor(R.color.kline_red_color));
//        candleDataSet.setShadowColor(getResources().getColor(R.color.kline_red_color));

        candleDataSet.setShadowWidth(1f);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);


        CandleData candleData = new CandleData(xVals, candleDataSet);


        ArrayList<ILineDataSet> sets = new ArrayList<>();

        //此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题
        if (size >= 20) {
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
            sets.add(setMaLine(20, xVals, line20Entries));
        } else if (size >= 10) {
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
        } else if (size >= 5) {
            sets.add(setMaLine(5, xVals, line5Entries));
        }


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);

//        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
//        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));
//        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
//        final float xscaleCombin = 3;
//        matrixCombin.postScale(xscaleCombin, 1f);

        moveView();
        combinedchart.setVisibleXRangeMaximum(50);
        combinedchart.setVisibleXRangeMinimum(20);
        barChart.setVisibleXRangeMaximum(50);
        barChart.setVisibleXRangeMinimum(20);

        setOffset();


        // 此处解决方法来源于CombinedChartDemo，k线图y轴显示问题，图表滑动后才能对齐的bug，希望有人给出解决方法
        handler.sendEmptyMessageDelayed(0, 300);

    }

    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(getActivity().getResources().getColor(R.color.stock_ma5));
        } else if (ma == 10) {
            lineDataSetMa.setColor(getActivity().getResources().getColor(R.color.stock_ma10));
        } else {
            lineDataSetMa.setColor(getActivity().getResources().getColor(R.color.stock_ma20));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    /*设置量表对齐*/
    private void setOffset() {
        if (getActivity() == null) return;
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft, transRight;
        /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            combinedchart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
        /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 15, transRight, barBottom);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.combinedchart
                || v.getId() == R.id.barchart) {
            if (combinedchart == null) return false;
            if (combinedchart.getData() == null) return false;
            if (barChart == null || barChart.getData() == null) return false;
            showOtherData();

            int max = combinedchart.getHighestVisibleXIndex();

            if (max != maxPosition) {
                //视图未移动的情况下（光标移动），根据光标位置显示均线值
                maxPosition = max;
                showMa(maxPosition);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (combinedchart.getLowestVisibleXIndex() == 0 && !isLoadingState && isCanLoadMore) {
                        tvChartLoadMore.setVisibility(View.VISIBLE);
                        tvBarLoadMore.setVisibility(View.VISIBLE);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    tvChartLoadMore.setVisibility(View.GONE);
                    tvBarLoadMore.setVisibility(View.GONE);
                    if (combinedchart.getLowestVisibleXIndex() == 0 && !isLoadingState && isCanLoadMore) {
                        isLoadingState = true;
//                        addKLine();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * 显示时间/最大最小值等信息
     */
    private void showOtherData() {
        if (getActivity() == null) return;

        tvC1Top.setText(decimalFormat.format(combinedchart.getYChartMax()));
        tvC1Bottom.setText(decimalFormat.format(combinedchart.getYChartMin()));
        tvBarTop.setText(StockUtils.getVolUnit(barChart.getYChartMax()));

        int higtest = combinedchart.getHighestVisibleXIndex();
        int lowest = combinedchart.getLowestVisibleXIndex();
        viewMixWidthNum = higtest - lowest;
        int center = (viewMixWidthNum / 2) + lowest;

        if (combinedchart.getXAxis() != null) {
            tvTimeLeft.setText(TimeUtil.dateFormat(combinedchart.getXAxis().getValues().get(lowest)));
            tvTimeRight.setText(TimeUtil.dateFormat(combinedchart.getXAxis().getValues().get(higtest)));
            tvTimeCenter.setText(TimeUtil.dateFormat(combinedchart.getXAxis().getValues().get(center)));
        }

    }

    /**
     * 显示均线值
     */
    private void showMa(int dataSetIndex) {
        if (getActivity() == null) return;
        KLineMaUtils.draw(dataSetIndex, combinedchart, tvMa5, tvMa10, tvMa20);
    }


    @Override
    public void loadKLine(String data) {
        try {
            if (data == null || !data.contains(stockSymbol)) return;
            String result = data.replace(stockSymbol, "stockCode");
            StockKLineBean bean = GsonUtil.gsonToBean(result, StockKLineBean.class);
            List<List<String>> list = bean.getData().getCandle().getStockCode();
            if (list == null || list.size() <= 0) return;
            if (list.size() < moveLoadNumber)
                isCanLoadMore = false;
            moveLoadNumber = list.size();
            mData.parseKLine(list);
            setData(mData);
            moveView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshData() {
        if (getActivity() == null) return;
        if (mData == null || barChart == null || combinedchart == null) return;
        if (combinedchart.getData() == null || barChart.getData() == null) return;
        moveView();

        barChart.setAutoScaleMinMaxEnabled(true);
        combinedchart.setAutoScaleMinMaxEnabled(true);

        combinedchart.notifyDataSetChanged();
        barChart.notifyDataSetChanged();

        combinedchart.invalidate();
        barChart.invalidate();
    }

    private static class PlayHandler extends Handler {

        WeakReference<KLineFragment> mKLineFragment;

        PlayHandler(KLineFragment kLineFragment) {
            this.mKLineFragment = new WeakReference<>(kLineFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            KLineFragment fragment = this.mKLineFragment.get();
            fragment.refreshData();
        }
    }
}
