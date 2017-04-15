package com.nongyi.nylive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jaronho.sdk.library.MD5;
import com.jaronho.sdk.third.okhttpwrap.HttpInfo;
import com.jaronho.sdk.third.okhttpwrap.OkHttpUtil;
import com.jaronho.sdk.third.okhttpwrap.callback.CallbackOk;
import com.tencent.ilivesdk.ILiveSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            NetLogic.reqGetSig("he1", new CallbackOk() {
                @Override
                public void onResponse(HttpInfo httpInfo) throws IOException {
                    try {
                        JSONObject obj = new JSONObject(httpInfo.getRetDetail());
                        JSONObject data = obj.getJSONObject("Data");
                        String sig = data.getString("sig");
                        Log.d("", sig);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
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
