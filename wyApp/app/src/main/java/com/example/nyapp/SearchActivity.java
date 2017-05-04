package com.example.nyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.ConfigureBean;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.example.view.MyFlowLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String SP_SEARCH_HISTORY = "sp_search_history";
    private static final String TAG = "SearchActivity.class";
    private LinearLayout mBtn_search_back;
    private EditText mEt_searchWord;
    private TextView mTv_search;
    private TextView mTv_clearSearchHistory;

    private LinearLayout mLl_searchWord;
    private SharedPreferences mSharePreference;
    private InputMethodManager mImm;
    private MyFlowLayout fl_keyword;
    private MyFlowLayout fl_Search_History;
    private LinearLayout.LayoutParams mLlp;
    private LinearLayout mLl_view_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initKeyword();
        initSearchHistory();

    }


    @Override
    public void initView() {
        mLlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlp.setMargins(15, 10, 15, 10);

        fl_keyword = (MyFlowLayout) findViewById(R.id.fl_keyword);
        fl_Search_History = (MyFlowLayout) findViewById(R.id.fl_Search_History);

        mBtn_search_back = (LinearLayout) findViewById(R.id.btn_search_back);

        mLl_view_search = (LinearLayout) findViewById(R.id.ll_view_search);
        mLl_searchWord = (LinearLayout) mLl_view_search.findViewById(R.id.ll_searchWord);
        mEt_searchWord = (EditText) mLl_view_search.findViewById(R.id.et_searchWord);
        mTv_search = (TextView) findViewById(R.id.tv_search);
        mTv_clearSearchHistory = (TextView) findViewById(R.id.tv_clearSearchHistory);


        mBtn_search_back.setOnClickListener(this);
        mTv_search.setOnClickListener(this);
        mTv_clearSearchHistory.setOnClickListener(this);
        mEt_searchWord.setOnClickListener(this);
        //1.得到InputMethodManager对象
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEt_searchWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    startSearchActivity();
                    return true;
                }
                return false;
            }

        });


    }

    //获取热门关键字
    private void initKeyword() {
        fl_keyword.removeAllViews();
        Map<String, String> map = new TreeMap<>();
        map.put("key", "HotSearchTerms");
        MyOkHttpUtils
                .getData(UrlContact.URL_CONFIGURE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("网络断开连接！请检查网络是否开启！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ConfigureBean configureBean = gson.fromJson(response, ConfigureBean.class);
                            if (configureBean != null) {
                                if (configureBean.isFlag()) {
                                    String keywordStr = configureBean.getMessage();
                                    if (keywordStr != null && !keywordStr.isEmpty()) {
                                        String[] s = keywordStr.split(",");

                                        for (String key : s) {
                                            final TextView view = new TextView(SearchActivity.this);
                                            view.setText(key);
                                            view.setBackgroundResource(R.drawable.keyword_bg);
                                            view.setTextColor(Color.BLACK);

                                            fl_keyword.addView(view, mLlp);
                                            view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    setEditText(view.getText().toString());
                                                    startSearchActivity();
                                                }
                                            });

                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void setEditText(String text) {
        Log.d(TAG, "setEditText: " + text + ":" + mEt_searchWord);
        mEt_searchWord.setText(text);
    }

    //获取搜索历史
    private void initSearchHistory() {
        fl_Search_History.removeAllViews();
        mTv_clearSearchHistory.setVisibility(View.GONE);
        mSharePreference = getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE);
        String history = mSharePreference.getString("history_keyWord", "").trim();
        if (!TextUtils.isEmpty(history)) {
            String[] str_SearchHistory = history.split(",");
            if (str_SearchHistory.length > 0) {

                for (String historyWord : str_SearchHistory) {
                    final TextView view = new TextView(SearchActivity.this);
                    view.setText(historyWord);
                    view.setTextColor(Color.parseColor("#666666"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setEditText(view.getText().toString());
                            startSearchActivity();
                        }
                    });
                    fl_Search_History.addView(view, mLlp);

                }
                mTv_clearSearchHistory.setVisibility(View.VISIBLE);
            }

        }

    }

    //保存搜索历史
    public void save() {
        String text = mEt_searchWord.getText().toString().trim();
        String oldText = mSharePreference.getString("history_keyWord", "");
        if (!TextUtils.isEmpty(text) && !oldText.contains(text)) {
            SharedPreferences.Editor mEditor = mSharePreference.edit();
            mEditor.putString("history_keyWord", text + "," + oldText);
            mEditor.apply();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_search_back:
                finish();
                break;
            case R.id.et_searchWord:
                mEt_searchWord.requestFocus();
                //2.调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
                mImm.showSoftInput(mEt_searchWord, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.tv_search:
                //2.调用hideSoftInputFromWindow方法隐藏软键盘
                startSearchActivity();
                break;
            case R.id.tv_clearSearchHistory:
                mSharePreference.edit().clear().apply();
                mTv_clearSearchHistory.setVisibility(View.GONE);
                initSearchHistory();
                break;
        }
    }

    private void startSearchActivity() {
        mImm.hideSoftInputFromWindow(mEt_searchWord.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); //强制隐藏键盘
        save();
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("type", mEt_searchWord.getText().toString().trim());
        intent.putExtra("from", "search");
        startActivity(intent);
        mEt_searchWord.setText("");
    }

}
