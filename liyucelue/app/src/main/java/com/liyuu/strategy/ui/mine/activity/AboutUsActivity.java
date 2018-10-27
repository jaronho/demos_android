package com.liyuu.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.NullContract;
import com.liyuu.strategy.presenter.NullPresenter;
import com.liyuu.strategy.util.SystemUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class AboutUsActivity extends BaseActivity<NullPresenter> implements NullContract.View {

    public static void start(Context context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.about_us);
        TextView tvVersion = ButterKnife.findById(this, R.id.tv_app_version);
        tvVersion.setText(String.format("v%s", SystemUtil.getVersionName(this)));
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({})
    void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
