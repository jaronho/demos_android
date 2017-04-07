package com.nongyi.nylive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyLiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live);

        ImageView imageviewBack = (ImageView)findViewById(R.id.imageview_back);
        imageviewBack.setOnClickListener(onClickImageviewBack);
    }

    View.OnClickListener onClickImageviewBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
