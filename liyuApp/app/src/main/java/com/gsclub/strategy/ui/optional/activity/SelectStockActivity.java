package com.gsclub.strategy.ui.optional.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.ScreenCode;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.optional.SelectStockContract;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.presenter.optional.SelectStockPresenter;
import com.gsclub.strategy.ui.home.adapter.StockUtils;
import com.gsclub.strategy.ui.stock.adapter.SelectStockRecyclerAdapter;
import com.gsclub.strategy.ui.stock.fragment.FiveDayFragment;
import com.gsclub.strategy.ui.stock.fragment.KLineFragment;
import com.gsclub.strategy.ui.stock.fragment.MinuteFragment;
import com.gsclub.strategy.ui.stock.other.bean.SelectStockBean;
import com.gsclub.strategy.ui.view.NoScrollViewPager;
import com.gsclub.strategy.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 股票详情界面（仅用于用户自选）
 */
public class SelectStockActivity extends BaseActivity<SelectStockPresenter>
        implements SelectStockContract.View {
    private static final String STOCK_NAME_STR = "stock_name_str";
    private static final String STOCK_CODE_STR = "stock_code_str";
    private final String[] strings = new String[]{"分时", "五日", "日K", "周K", "月K", "5分", "15分", "30分", "60分"};
    @BindView(R.id.rcv_one)
    RecyclerView rcvOne;
    @BindView(R.id.rcv_two)
    RecyclerView rcvTwo;
    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @BindView(R.id.tl_meun)
    TabLayout tabLayout;
    @BindView(R.id.tv_price)
    TextView tvLastPX;
    @BindView(R.id.tv_float_price)
    TextView tvPxChange;
    @BindView(R.id.tv_float_precent)
    TextView tvPxChangeRate;
    @BindView(R.id.tv_pay_state)
    TextView tvPayState;
    @BindView(R.id.tv_stock_name)
    TextView tvStockName;
    @BindView(R.id.tv_stock_code)
    TextView tvStockCode;
    @BindView(R.id.tv_add_stock)
    TextView tvAddStock;//添加自选按钮
    private SelectStockRecyclerAdapter oneAdapter;
    private SelectStockRecyclerAdapter twoAdapter;
    private List<SelectStockBean> listOne = new ArrayList<>();
    private List<SelectStockBean> listTwo = new ArrayList<>();
    private String stockSymbol;//股票代码
    private String stockSymbolName;//股票名称

    /**
     * 界面跳转
     *
     * @param stockSymbol     股票代码
     * @param stockSymbolName 股票名称
     */
    public static void start(Context context, @NonNull String stockSymbol, String stockSymbolName) {
        if (TextUtils.isEmpty(stockSymbol)) return;
        Intent intent = new Intent(context, SelectStockActivity.class);
        if (!TextUtils.isEmpty(stockSymbol))
            intent.putExtra(STOCK_CODE_STR, stockSymbol);
        if (!TextUtils.isEmpty(stockSymbolName))
            intent.putExtra(STOCK_NAME_STR, stockSymbolName);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_stock;
    }

    @Override
    protected void initEventAndData() {
        stockSymbol = getIntent().getStringExtra(STOCK_CODE_STR);
        tvStockCode.setText(stockSymbol);
        if (TextUtils.isEmpty(stockSymbolName))
            stockSymbolName = "";

        mPresenter.getDefaultData(this);
        mPresenter.queryThisStockIsSelect(stockSymbol);
    }

    private void initInfoView() {
        oneAdapter = new SelectStockRecyclerAdapter(this);
        oneAdapter.setData(listOne);
        rcvOne.setVisibility(View.VISIBLE);
        rcvOne.setLayoutManager(new GridLayoutManager(this, 2));
        ViewGroup.LayoutParams params = rcvOne.getLayoutParams();
        params.width = (ScreenUtil.getScreen(this, ScreenCode.WIDTH) - ScreenUtil.dp2px(this, 20)) / 2;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        rcvOne.setLayoutParams(params);
        rcvOne.setHasFixedSize(true);
        rcvOne.setAdapter(oneAdapter);

        twoAdapter = new SelectStockRecyclerAdapter(this);
        twoAdapter.setData(listTwo);
        rcvTwo.setLayoutManager(new GridLayoutManager(this, 4));
        rcvTwo.setHasFixedSize(true);
        rcvTwo.setAdapter(twoAdapter);
    }

    @Override
    public void initUI() {
        super.initUI();
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(mViewPagerAdapter);
        vpContent.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(vpContent);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @OnClick({R.id.img_right_arrow, R.id.tv_add_stock})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_right_arrow:
                finishUI();
                break;
            case R.id.tv_add_stock:
                if (!TextUtils.isEmpty(stockSymbol) && !TextUtils.isEmpty(stockSymbolName))
                    mPresenter.changeAddStock(stockSymbol, stockSymbolName);
                break;
        }
    }

    @Override
    public void showDefaultData(List<SelectStockBean> one, List<SelectStockBean> two) {
        listOne = one;
        listTwo = two;
        initInfoView();// 初始化股市行情view
        mPresenter.getStockReal(stockSymbol);
    }

    @Override
    public void showStockRealData(String data) {
        if (TextUtils.isEmpty(data)) return;
        JsonObject snapshot = GsonUtil.getJsonObject(data, "data", "snapshot");
        if (snapshot == null) return;
        if (!snapshot.has(stockSymbol)) return;
        JsonArray array = snapshot.get(stockSymbol).getAsJsonArray();

        float symbol_price = (GsonUtil.arrayGetFloat(array, 3) >= 0) ? 1.0f : -1.0f;
        //设置字体颜色
        StockUtils.setStockShow(
                tvLastPX,
                GsonUtil.arrayGetFloat(array, 2) * symbol_price,
                SelectStockActivity.this,
                false,
                false);

        //价格涨幅/涨幅百分比自带单位，无需通过symbol_price设置颜色
        StockUtils.setStockShow(
                tvPxChange,
                GsonUtil.arrayGetFloat(array, 3),
                SelectStockActivity.this,
                true,
                false);

        StockUtils.setStockShow(
                tvPxChangeRate,
                GsonUtil.arrayGetFloat(array, 4),
                SelectStockActivity.this,
                true,
                true);

        String tradeStatus = GsonUtil.arrayGetString(array, 5);//股票交易状态

        for (int i = 0; i < listOne.size(); i++) {
            listOne.get(i).setNumber(GsonUtil.arrayGetFloat(array, i + 6));
        }

        for (int i = 0; i < listTwo.size(); i++) {
            listTwo.get(i).setNumber(GsonUtil.arrayGetFloat(array, i + 10));
        }

        tvStockName.setText(GsonUtil.arrayGetString(array, 18, stockSymbolName));
        if (TextUtils.isEmpty(stockSymbolName))
            stockSymbolName = tvStockName.getText().toString();

        if (!TextUtils.isEmpty(tradeStatus))
            tvPayState.setText(String.format("%s %s", StockUtils.getPayState(tradeStatus), GsonUtil.arrayGetString(array, 21)));

        oneAdapter.setSymbol_price(symbol_price);
        oneAdapter.notifyDataSetChanged();
        twoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddStockView(boolean isHadAdd) {
        tvAddStock.setVisibility(View.VISIBLE);
        if (isHadAdd) {
            tvAddStock.setTextColor(getResources().getColor(R.color.text_color_dark_blue));
            tvAddStock.setBackgroundResource(R.drawable.bg_grey_small_arc_with_transparent);
            tvAddStock.setText("自选");
            Drawable drawable = SelectStockActivity.this.getResources().getDrawable(R.mipmap.ic_stock_optional_delete);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            tvAddStock.setCompoundDrawables(drawable, null, null, null);
        } else {
            tvAddStock.setTextColor(getResources().getColor(R.color.stock_red_color));
            tvAddStock.setBackgroundResource(R.drawable.bg_red_small_arc_with_transparent);
            tvAddStock.setText("+ 自选");
            tvAddStock.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.HEART_BREAK:
                if (isUserCanSee)
                    mPresenter.getStockReal(stockSymbol);
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //"分时", "五日", "日K", "周K", "月K", "5分", "15分", "30分", "60分"
            if (position == 0)
                return MinuteFragment.newInstance(stockSymbol);
            else if (position == 1)
                return FiveDayFragment.newInstance(stockSymbol);
            //取值可以是数字1-9，表示含义如下： 1：1分钟K线 2：5分钟K线 3：15分钟K线 4：30分钟K线 5：60分钟K线 6：日K线 7：周K线 8：月K线 9：年K线
            int klineType = mPresenter.getKlineType(position);
            return KLineFragment.newInstance(klineType, stockSymbol);
        }

        @Override
        public int getCount() {
            return strings.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //切换时销毁之前的fragment
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            if (manager == null) return;
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }
    }
}
