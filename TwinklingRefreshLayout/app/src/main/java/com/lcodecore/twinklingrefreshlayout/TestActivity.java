package com.lcodecore.twinklingrefreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lcodecore.twinklingrefreshlayout.adapter.ScienceAdapter;
import com.lcodecore.twinklingrefreshlayout.utils.ToastUtil;

/**
 * Created by lcodecore on 2017/3/27.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private ScienceAdapter adapter;
    private TwinklingRefreshLayout refreshLayout;

    private TestButton toggle_enableLoadmore, toggle_pureScrollMode_on, toggle_overScrollTopShow, toggle_osFooterShow, toggle_enableOverScroll, toggle_enableKeepIView,
            toggle_showRefreshingWhenOverScroll, toggle_showLoadingWhenOverScroll, toggle_floatRefresh, toggle_autoLoadMore,toggle_enableRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupGridView((GridView) findViewById(R.id.gridView));

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toggle_enableLoadmore = new TestButton(R.id.toggle_enableLoadmore, "enableLoadmore", true);
        toggle_pureScrollMode_on = new TestButton(R.id.toggle_pureScrollMode_on, "pureScrollMode_on", false);
        toggle_overScrollTopShow = new TestButton(R.id.toggle_overScrollTopShow, "overScrollTopShow", true);
        toggle_osFooterShow = new TestButton(R.id.toggle_osFooterShow, "osFooterShow", true);
        toggle_enableOverScroll = new TestButton(R.id.toggle_enableOverScroll, "enableOverScroll", true);
        toggle_enableKeepIView = new TestButton(R.id.toggle_enableKeepIView, "enableKeepIView", true);
        toggle_showRefreshingWhenOverScroll = new TestButton(R.id.toggle_showRefreshingWhenOverScroll, "showRefreshingWhenOS", true);
        toggle_showLoadingWhenOverScroll = new TestButton(R.id.toggle_showLoadingWhenOverScroll, "showLoadingWhenOS", true);
        toggle_floatRefresh = new TestButton(R.id.toggle_floatRefresh, "floatRefresh", false);
        toggle_autoLoadMore = new TestButton(R.id.toggle_autoLoadMore, "autoLoadMore", false);
        toggle_enableRefresh = new TestButton(R.id.toggle_enableRefresh,"enableRefresh",true);
    }

    private void setupGridView(GridView gridView) {
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh);
        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
//        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
        refreshLayout.setHeaderView(headerView);

        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);

        adapter = new ScienceAdapter();
        gridView.setAdapter(adapter);
        adapter.refreshCard();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtil.show("item clicked!");
            }
        });

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refreshCard();
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreCard();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_enableLoadmore:
                toggle_enableLoadmore.toggle();
                refreshLayout.setEnableLoadmore(toggle_enableLoadmore.flag);
                break;
            case R.id.toggle_pureScrollMode_on:
                toggle_pureScrollMode_on.toggle();
                refreshLayout.setPureScrollModeOn();
                break;
            case R.id.toggle_overScrollTopShow:
                toggle_overScrollTopShow.toggle();
                refreshLayout.setOverScrollTopShow(toggle_overScrollTopShow.flag);
                break;
            case R.id.toggle_osFooterShow:
                toggle_osFooterShow.toggle();
                refreshLayout.setOverScrollBottomShow(toggle_osFooterShow.flag);
                break;
            case R.id.toggle_enableOverScroll:
                toggle_enableOverScroll.toggle();
                refreshLayout.setEnableOverScroll(toggle_enableOverScroll.flag);
                break;
            case R.id.toggle_enableKeepIView:
                toggle_enableKeepIView.toggle();
                refreshLayout.setEnableKeepIView(toggle_enableKeepIView.flag);
                break;
            case R.id.toggle_showRefreshingWhenOverScroll:
                toggle_showRefreshingWhenOverScroll.toggle();
                refreshLayout.showRefreshingWhenOverScroll(toggle_showRefreshingWhenOverScroll.flag);
                break;
            case R.id.toggle_showLoadingWhenOverScroll:
                toggle_showLoadingWhenOverScroll.toggle();
                refreshLayout.showLoadingWhenOverScroll(toggle_showLoadingWhenOverScroll.flag);
                break;
            case R.id.toggle_floatRefresh:
                toggle_floatRefresh.toggle();
                refreshLayout.setFloatRefresh(toggle_floatRefresh.flag);
                break;
            case R.id.toggle_autoLoadMore:
                toggle_autoLoadMore.toggle();
                refreshLayout.setAutoLoadMore(toggle_autoLoadMore.flag);
                break;
            case R.id.toggle_enableRefresh:
                toggle_enableRefresh.toggle();
                refreshLayout.setEnableRefresh(toggle_enableRefresh.flag);
        }
    }

    class TestButton {
        private Button button;
        private boolean flag;
        private String text;

        public TestButton(int id, String text, boolean flag) {
            button = (Button) findViewById(id);
            this.text = text;
            this.flag = flag;
            button.setOnClickListener(TestActivity.this);
            button.setText(text + "->" + flag);
        }

        public void toggle() {
            flag = !flag;
            button.setText(text + "->" + flag);
        }
    }
}
