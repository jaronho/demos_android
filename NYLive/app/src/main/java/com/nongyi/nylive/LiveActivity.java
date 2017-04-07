package com.nongyi.nylive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        ImageView imageviewBack = (ImageView)findViewById(R.id.imageview_back);
        imageviewBack.setOnClickListener(onClickImageviewBack);

        TextView textviewMine = (TextView)findViewById(R.id.textview_mine);
        textviewMine.setOnClickListener(onClickTextviewMine);
    }

    OnClickListener onClickImageviewBack = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener onClickTextviewMine = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LiveActivity.this, MyLiveActivity.class));
        }
    };
}
