package com.nongyi.nylive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.Model.DataChannel;
import com.nongyi.nylive.Model.NetCallback;
import com.nongyi.nylive.Model.NetLogic;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

public class MainActivity extends AppCompatActivity {
    private String mUserid = "he1";
    private String mSig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetSig = (Button)findViewById(R.id.btn_get_sig);
        btnGetSig.setOnClickListener(onClickBtnGetSig);

        Button btnGetLiveList = (Button)findViewById(R.id.btn_get_live_list);
        btnGetLiveList.setOnClickListener(onClickBtnGetLiveList);

        Button btnGetChatroomList = (Button)findViewById(R.id.btn_get_chatroom_list);
        btnGetChatroomList.setOnClickListener(onClickBtnGetChatroomList);

        Button btnGetChannelView = (Button)findViewById(R.id.btn_get_channel_view);
        btnGetChannelView.setOnClickListener(onClickBtnGetChannelView);

        Button btnUpdateCHannelUrl = (Button)findViewById(R.id.btn_update_channel_url);
        btnUpdateCHannelUrl.setOnClickListener(onClickBtnUpdateChannelUrl);

        Button btnAddInteractive = (Button)findViewById(R.id.btn_add_interactive);
        btnAddInteractive.setOnClickListener(onClickBtnAddInteractive);

        Button btnAddChannelUser = (Button)findViewById(R.id.btn_add_channel_user);
        btnAddChannelUser.setOnClickListener(onClickBtnAddChannelUser);

        Button btnLoginTSL = (Button)findViewById(R.id.btn_login_tsl);
        btnLoginTSL.setOnClickListener(onClickBtnLoginTSL);

        Button btnLive = (Button)findViewById(R.id.btn_live);
        btnLive.setOnClickListener(onClickBtnLive);

        Button btnMyLive = (Button)findViewById(R.id.btn_my_live);
        btnMyLive.setOnClickListener(onClickMyLive);
    }

    OnClickListener onClickBtnGetSig = new OnClickListener() {
        @Override
        public void onClick(View v) {
            NetLogic.reqGetSig(mUserid, new NetCallback<String>() {
                @Override
                public void onData(String data) {
                    mSig = data;
                }
            });
        }
    };

    OnClickListener onClickBtnGetLiveList = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int status = 0;
            int pageIndex = 1;
            int pageSize = 10;
            NetLogic.reqGetLiveList(status, pageIndex, pageSize, new NetCallback<DataChannel>() {
                @Override
                public void onData(DataChannel data) {

                }
            });
        }
    };

    OnClickListener onClickBtnGetChatroomList = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int status = 0;
            int pageIndex = 1;
            int pageSize = 10;
            NetLogic.reqGetChatroomList(status, pageIndex, pageSize, new NetCallback<DataChannel>() {
                @Override
                public void onData(DataChannel data) {

                }
            });
        }
    };

    OnClickListener onClickBtnGetChannelView = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int channelId = 1;
            NetLogic.reqGetChannelView(channelId, new NetCallback<DataChannel>() {
                @Override
                public void onData(DataChannel data) {

                }
            });
        }
    };

    OnClickListener onClickBtnUpdateChannelUrl = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int channelId = 1;
            String newUrl = "";
            NetLogic.reqUpdateChannelUrl(channelId, newUrl, new NetCallback<Boolean>() {
                @Override
                public void onData(Boolean data) {

                }
            });
        }
    };

    OnClickListener onClickBtnAddInteractive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int channelId = 1;
            int userId = 1;
            int type = 0;
            NetLogic.reqInteractiveAdd(channelId, userId, type, new NetCallback<Boolean>() {
                @Override
                public void onData(Boolean data) {

                }
            });
        }
    };

    OnClickListener onClickBtnAddChannelUser = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int channelId = 1;
            int userId = 1;
            NetLogic.reqChannelUserAdd(channelId, userId, new NetCallback<Boolean>() {
                @Override
                public void onData(Boolean data) {

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

    OnClickListener onClickBtnLive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, LiveActivity.class));
        }
    };

    OnClickListener onClickMyLive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, MyLiveActivity.class));
        }
    };
}
