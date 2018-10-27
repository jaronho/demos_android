package com.liyuu.strategy.ui.stock.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.contract.stock.FiveDayContract;
import com.liyuu.strategy.presenter.stock.FiveDayPresenter;
import com.liyuu.strategy.ui.stock.other.ChartUtil;
import com.liyuu.strategy.ui.stock.other.bean.StockGsonBean;
import com.liyuu.strategy.util.ScreenUtil;
import com.liyuu.strategy.util.TimeUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 五日分时图
 */
public class FiveDayFragment extends BaseFragment<FiveDayPresenter> implements FiveDayContract.View {
    private static final String STOCK_SYMBOL_STRING = "stock_symbol_string";

    @BindView(R.id.linechart)
    LineChart lineChart;
    @BindView(R.id.barchart)
    BarChart barChart;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_load_failed_notify)
    TextView tvLoadFailed;
    private List<List<StockGsonBean>> fiveStocks = new ArrayList<>();
    private PlayHandler handler = null;

    public static Fragment newInstance(String stockSymbol) {
        FiveDayFragment fragment = new FiveDayFragment();
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
        return R.layout.fragment_five_day;
    }

    @Override
    public void initUI() {
        super.initUI();
        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(false);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.transparent));
        barChart.setViewPortOffsets(0, 0, 0, 0);
        barChart.setDescription("");


        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //bar x y轴
        XAxis xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);

        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.transparent));
        xAxisBar.setAxisLineColor(getResources().getColor(R.color.transparent));

        YAxis axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.transparent));


        YAxis axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        lineChart.setViewPortOffsets(0, 0, 0, 0);
        // 是否在折线图上添加边框
        lineChart.setDrawBorders(false);
        // 曲线描述 -标题
        lineChart.setDescription("");
        // 标题字体大小
//        lineChart.setDescriptionTextSize(16f);
        // 标题字体颜色
//        lineChart.setDescriptionColor(context.getApplicationContext().getResources()
//                .getColor(R.color.black));
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);
        // 如果没有数据的时候，会显示这个，类似文本框的placeholder
        lineChart.setNoDataTextDescription("");
        // 是否显示表格颜色
        lineChart.setDrawGridBackground(false);
        // 禁止绘制图表边框的线
        lineChart.setDrawBorders(false);
        // 表格的的颜色，在这里是是给颜色设置一个透明度
        // lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        // 设置是否启动触摸响应
        lineChart.setTouchEnabled(false);
        // 是否可以拖拽
        lineChart.setDragEnabled(false);
        // 是否可以缩放
        lineChart.setScaleEnabled(false);
        // 如果禁用，可以在x和y轴上分别进行缩放
        lineChart.setPinchZoom(false);

        // 隐藏右侧Y轴
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        // 显示X轴上的刻度值
        xAxis.setDrawLabels(false);
        // 设置X轴的数据显示在报表的下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 轴线
        // xAxis.setDrawAxisLine(false);
        // 设置不从X轴发出纵向直线
        xAxis.setDrawGridLines(false);
    }

    @Override
    protected void initEventAndData() {
        String symbol = getArguments().getString(STOCK_SYMBOL_STRING);
        mPresenter.getFiveDayLine(symbol);
    }

    @Override
    public void loadBarAndLineChartView(List<String> xDataList, List<List<StockGsonBean>> lists) {
        if (getActivity() == null) return;

        fiveStocks.clear();
        fiveStocks.addAll(lists);

        //显示5日分别的日期
        loadDate(fiveStocks);

        lineChart.setVisibleXRangeMaximum(xDataList.size());
        lineChart.setVisibleXRangeMinimum(xDataList.size());

        LineData data = new LineData(xDataList);
        BarData barData = new BarData(xDataList);

        //根据获取的信息绘制5日k线（会出现第5根点数未满/或者未返回第五日数据的情况:需自动填空值占位）
        for (int i = 0, size = fiveStocks.size(); i < size; i++) {
            data.addDataSet(ChartUtil.getLineDataSet(getActivity(), getY(i, fiveStocks),
                    "", getActivity().getResources().getColor(R.color.stock_line_blue), true));
            data.addDataSet(ChartUtil.getLineDataSet(getActivity(), getAVGY(i, fiveStocks),
                    "", getActivity().getResources().getColor(R.color.stock_line_yellow), false));
        }

        barData.addDataSet(ChartUtil.getBarDataSet(getActivity(), getBarY(fiveStocks),
                ""));

        data.setHighlightEnabled(false);
        barData.setHighlightEnabled(false);

        barChart.setData(barData);

        lineChart.setData(data);

        if (handler == null)
            handler = new PlayHandler(lineChart, barChart);
        // 执行的动画,x轴（动画持续时间）
        handler.sendEmptyMessage(0);
    }

    /**
     * 加载五日分时五日的日期
     */
    private void loadDate(List<List<StockGsonBean>> fiveStocks) {
        if (getActivity() == null) return;
        ((ViewGroup) llDate).removeAllViews();//清除所有加载过的view，重新加载
        int size = fiveStocks.size();
        for (int i = 0; i < 5; i++) {
            TextView tv = new TextView(getActivity());
            String date = " ";

            if (i < size) {
                List<StockGsonBean> list = fiveStocks.get(i);
                date = TimeUtil.dateFormat(list.get(0).getTime(), "yyyyMMdd", "yyyy-MM-dd");
            }

            tv.setText(date);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(ScreenUtil.dp2px(getActivity(), 3));
            tv.setTextColor(getActivity().getResources().getColor(R.color.text_grey_979eaf));
            llDate.addView(tv);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.CENTER;
        }

    }

    /**
     * 获取y轴数据
     *
     * @param day   五日的哪一天
     * @param datas 五日数据合集
     */
    private List<Entry> getY(int day, List<List<StockGsonBean>> datas) {
        List<Entry> yDataList = new ArrayList<>();
        int position = 0;
        for (int i = 0, size = datas.size(); i < size; i++) {
            for (int j = 0, sizz = datas.get(i).size(); j < sizz; j++) {
                if (i == day) {
                    yDataList.add(new Entry(Float.parseFloat(datas.get(i).get(j).getLast_px()), position));
                }
                position++;
            }
        }
        return yDataList;
    }

    private List<BarEntry> getBarY(List<List<StockGsonBean>> datas) {
        List<BarEntry> barEntries = new ArrayList<>();
        int position = 0;
        int chooseColor = 0;
        for (int i = 0, size = datas.size(); i < size; i++) {
            for (int j = 0, sizz = datas.get(i).size(); j < sizz; j++) {
                if (j > 1) {
                    float f = Float.parseFloat(datas.get(i).get(j).getLast_px())
                            - Float.parseFloat(datas.get(i).get(j - 1).getLast_px());
                    if (f > 0) chooseColor = 0;
                    else if (f < 0) chooseColor = 1;
                    else chooseColor = (int) barEntries.get(i).getData();
                    barEntries.add(new BarEntry(
                            Float.parseFloat(datas.get(i).get(j).getBusiness_balance()) /*- Float.parseFloat(datas.get(i).get(j - 1).getBusiness_amount())*/
                            , position, chooseColor));

                } else {
                    barEntries.add(new BarEntry(
                            Float.parseFloat(fiveStocks.get(i).get(j).getBusiness_balance())
                            , position, chooseColor));

                }

                position++;
            }
        }
        return barEntries;
    }

    private List<Entry> getAVGY(int add, List<List<StockGsonBean>> datas) {
        List<Entry> yDataList = new ArrayList<>();
        int position = 0;
        for (int i = 0, size = datas.size(); i < size; i++) {
            for (int j = 0, sizz = datas.get(i).size(); j < sizz; j++) {
                if (i == add) {
                    yDataList.add(new Entry(Float.parseFloat(datas.get(i).get(j).getAvg_px()), position));
                }
                position++;
            }
        }
        return yDataList;
    }

    private static class PlayHandler extends Handler {
        WeakReference<LineChart> lineChart;
        WeakReference<BarChart> barChart;

        PlayHandler(LineChart lineChart, BarChart barChart) {
            this.lineChart = new WeakReference<>(lineChart);
            this.barChart = new WeakReference<>(barChart);
        }

        @Override
        public void handleMessage(Message msg) {

            LineChart lineChart = this.lineChart.get();
            BarChart barChart = this.barChart.get();

            lineChart.setAutoScaleMinMaxEnabled(false);
            barChart.setAutoScaleMinMaxEnabled(false);

            lineChart.notifyDataSetChanged();
            barChart.notifyDataSetChanged();

            lineChart.invalidate();
            barChart.invalidate();
        }
    }


}
