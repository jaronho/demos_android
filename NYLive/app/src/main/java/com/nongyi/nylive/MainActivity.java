package com.nongyi.nylive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.utils.NetHelper;
import com.nongyi.nylive.utils.NetHelper.Callback;
import com.nongyi.nylive.view.HostLiveActivity;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

public class MainActivity extends AppCompatActivity {
    private String mUserid = "he2";
    private String mSig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetSig = (Button)findViewById(R.id.btn_get_sig);
        btnGetSig.setOnClickListener(onClickBtnGetSig);

        Button btnLoginTSL = (Button)findViewById(R.id.btn_login_tsl);
        btnLoginTSL.setOnClickListener(onClickBtnLoginTSL);

        Button btnCreateRoom = (Button)findViewById(R.id.btn_create_room);
        btnCreateRoom.setOnClickListener(onClickBtnCreateRoom);

        Button btnEnterRoom = (Button)findViewById(R.id.btn_enter_room);
        btnEnterRoom.setOnClickListener(onClickBtnEnterRoom);
    }

    OnClickListener onClickBtnGetSig = new OnClickListener() {
        @Override
        public void onClick(View v) {
            NetHelper.reqGetSig(mUserid, new Callback<String>() {
                @Override
                public void onData(String data) {
                    mSig = data;
                    ViewUtil.showToast(MainActivity.this, "获取Sig成功");
                }
            });
        }
    };

    OnClickListener onClickBtnLoginTSL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ILiveLoginManager.getInstance().iLiveLogin(mUserid, mSig, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    ViewUtil.showToast(MainActivity.this, "登陆TSL成功");
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ViewUtil.showToast(MainActivity.this, "登陆TSL失败: " + module + " " + errCode + " " + errMsg);
                }
            });
        }
    };

    OnClickListener onClickBtnCreateRoom = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, HostLiveActivity.class));
        }
    };

    OnClickListener onClickBtnEnterRoom = new OnClickListener() {

        @Override
        public void onClick(View v) {
//            startActivity(new Intent(MainActivity.this, LiveListActivity.class));
//            startActivity(new Intent(MainActivity.this, HostLiveActivity.class));
        }
    };
}
