package com.liyuu.strategy.ui.stock.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.base.OnItemClick;
import com.liyuu.strategy.contract.stock.SearchStockContract;
import com.liyuu.strategy.model.bean.SearchStockBean;
import com.liyuu.strategy.presenter.stock.SearchStockPresenter;
import com.liyuu.strategy.ui.stock.adapter.SearchStockAdapter;
import com.liyuu.strategy.ui.view.MyEditText;
import com.ziyeyouhu.library.KeyboardTouchListener;
import com.ziyeyouhu.library.KeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchStockActivity extends BaseActivity<SearchStockPresenter> implements SearchStockContract.View, TextWatcher, OnItemClick<SearchStockBean> {
    @BindView(R.id.rcv_content)
    RecyclerView rcvContent;
    @BindView(R.id.et_search)
    MyEditText edtSearch;
    @BindView(R.id.ll_no_message)
    LinearLayout llNo;
    @BindView(R.id.tv_notify)
    TextView tvNotify;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.sv_content)
    NestedScrollView svContent;
    @BindView(R.id.tv_latest_search)
    TextView tvLatestSearch;
    @BindView(R.id.tv_clear_search_record)
    TextView tvClearSearchRecord;

    private SearchStockAdapter adatper;
    private KeyboardUtil keyboardUtil;

    public static void start(Context context) {
        if (context == null)
            return;
        context.startActivity(new Intent(context, SearchStockActivity.class));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search_stock;
    }

    @Override
    public void initUI() {
        super.initUI();
        initMoveKeyBoard();
        edtSearch.addTextChangedListener(this);
        tvNotify.setText(R.string.please_searching_for_relevant_content);
        rcvContent.setHasFixedSize(true);
        rcvContent.setLayoutManager(new LinearLayoutManager(SearchStockActivity.this));
        adatper = new SearchStockAdapter(this);
        adatper.setOnItemClick(this);
        adatper.setData(new ArrayList<SearchStockBean>());
        rcvContent.setAdapter(adatper);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getLocalSearchStockHistory();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    public void getSearchData(String keyword) {
        if (keyword == null || "".equals(keyword.trim())) {
            return;
        }
        mPresenter.searchStocks(keyword);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (keyboardUtil.isShow) {
                keyboardUtil.hideSystemKeyBoard();
                keyboardUtil.hideAllKeyBoard();
                keyboardUtil.hideKeyboardLayout();
            } else {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(this, llContent, svContent);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        keyboardUtil.setInputOverListener(new InputOverListener());
        edtSearch.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
//        TextView textView = new TextView(this);
//        textView.setText("123");
//        textView.setTextColor(getResources().getColor(R.color.text_grey_999999));
//        llContent.addView(textView);
    }

    @Override
    public void onClick(int postion, SearchStockBean bean) {
        mPresenter.insertData(bean);
    }

    @OnClick({R.id.img_back, R.id.tv_clear_search_record})
    void onCLick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finishUI();
                break;
            case R.id.tv_clear_search_record:
                mPresenter.clearLocalSearchHistory();
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        getSearchData(s.toString());
    }

    @Override
    public void setStocksData(List<SearchStockBean> stocks) {
        tvLatestSearch.setVisibility(View.GONE);
        tvClearSearchRecord.setVisibility(View.GONE);
        rcvContent.setVisibility(View.VISIBLE);
        adatper.getData().clear();
        adatper.getData().addAll(stocks);
        adatper.notifyDataSetChanged();

        if (adatper.getData() != null && adatper.getData().size() > 0)
            llNo.setVisibility(View.GONE);
        else {
            tvNotify.setText(R.string.not_searching_for_relevant_content);
            llNo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSearchStockHistory(List<SearchStockBean> datas) {
        tvLatestSearch.setVisibility(View.VISIBLE);
        tvClearSearchRecord.setVisibility(View.VISIBLE);
        adatper.getData().clear();
        adatper.getData().addAll(datas);
        adatper.notifyDataSetChanged();

    }

    @Override
    public void hideRecyclerView() {
        rcvContent.setVisibility(View.GONE);
        adatper.getData().clear();
        adatper.notifyDataSetChanged();
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {

        }
    }

    class InputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {

        }
    }
}
