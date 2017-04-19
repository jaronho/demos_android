package com.lcodecore.twinklingrefreshlayout;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int setInflateId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.bt_music).setOnClickListener(this);
        findViewById(R.id.bt_food).setOnClickListener(this);
        findViewById(R.id.bt_science).setOnClickListener(this);
        findViewById(R.id.bt_photo).setOnClickListener(this);
        findViewById(R.id.bt_story).setOnClickListener(this);
        findViewById(R.id.bt_enjoy).setOnClickListener(this);
        findViewById(R.id.bt_coordinate).setOnClickListener(this);
        findViewById(R.id.bt_test).setOnClickListener(this);
        findViewById(R.id.bt_normalView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_music:
                startActivity(new Intent(MainActivity.this, MusicActivity.class));
                break;
            case R.id.bt_food:
                startActivity(new Intent(MainActivity.this, FoodActivity.class));
                break;
            case R.id.bt_science:
                startActivity(new Intent(MainActivity.this, ScienceActivity.class));
                break;
            case R.id.bt_photo:
                startActivity(new Intent(MainActivity.this, PhotoActivity.class));
                break;
            case R.id.bt_story:
                startActivity(new Intent(MainActivity.this, StoryActivity.class));
                break;
            case R.id.bt_enjoy:
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                break;
            case R.id.bt_coordinate:
                startActivity(new Intent(MainActivity.this,CoordinateActivity.class));
                break;
            case R.id.bt_normalView:
                startActivity(new Intent(MainActivity.this,NormalViewActivity.class));
                break;
            case R.id.bt_test:
                startActivity(new Intent(MainActivity.this,TestActivity.class));
                break;
        }
    }
}
