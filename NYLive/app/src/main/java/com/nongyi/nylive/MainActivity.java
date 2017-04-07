package com.nongyi.nylive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetSig = (Button)findViewById(R.id.btn_get_sig);
        btnGetSig.setOnClickListener(onClickBtnGetSig);

        Button btnLoginTSL = (Button)findViewById(R.id.btn_login_tsl);
        btnLoginTSL.setOnClickListener(onClickBtnLoginTSL);

        Button btnLive = (Button)findViewById(R.id.btn_live);
        btnLive.setOnClickListener(onClickBtnLive);

        Button btnMyLive = (Button)findViewById(R.id.btn_my_live);
        btnMyLive.setOnClickListener(onClickMyLive);

        Button btnChatRoom = (Button)findViewById(R.id.btn_chat_room);
        btnChatRoom.setOnClickListener(onClickChatRoom);
    }

    OnClickListener onClickBtnGetSig = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "点击 \"获取Sig\"", Toast.LENGTH_SHORT).show();
        }
    };

    OnClickListener onClickBtnLoginTSL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "点击 \"登陆TSL\"", Toast.LENGTH_SHORT).show();
        }
    };

    OnClickListener onClickBtnLive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "点击 \"直播\"", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LiveActivity.class));
        }
    };

    OnClickListener onClickMyLive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "点击 \"我的直播\"", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, MyLiveActivity.class));
        }
    };

    OnClickListener onClickChatRoom = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "点击 \"聊天室\"", Toast.LENGTH_SHORT).show();
        }
    };
}
