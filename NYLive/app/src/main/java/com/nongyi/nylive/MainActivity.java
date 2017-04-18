package com.nongyi.nylive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.Model.DataChannel;
import com.nongyi.nylive.Model.ILiveHelper;
import com.nongyi.nylive.Model.NetHelper;
import com.nongyi.nylive.Model.NetHelper.Callback;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

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
            NetHelper.reqGetSig(mUserid, new Callback<String>() {
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
            NetHelper.reqGetLiveList(status, pageIndex, pageSize, new Callback<DataChannel>() {
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
            NetHelper.reqGetChatroomList(status, pageIndex, pageSize, new Callback<DataChannel>() {
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
            NetHelper.reqGetChannelView(channelId, new Callback<DataChannel>() {
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
            NetHelper.reqUpdateChannelUrl(channelId, newUrl, new Callback<Boolean>() {
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
            NetHelper.reqInteractiveAdd(channelId, userId, type, new Callback<Boolean>() {
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
            NetHelper.reqChannelUserAdd(channelId, userId, new Callback<Boolean>() {
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
            startActivity(new Intent(MainActivity.this, LiveListActivity.class));
        }
    };

    OnClickListener onClickMyLive = new OnClickListener() {
        int roomId = 1;
        String role = "Host";
        @Override
        public void onClick(View v) {
            // 创建房间配置项
            ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                    controlRole(role)// 角色设置
                    .imsupport(false)
                    .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)// 支持后台模式
                    .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)// 权限设置
                    .cameraId(ILiveConstants.FRONT_CAMERA)// 摄像头前置后置
                    .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);// 是否开始半自动接收
            //创建房间
            ILVLiveManager.getInstance().createRoom(roomId, hostOption, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    startActivity(new Intent(MainActivity.this, AnchorLiveActivity.class));
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ViewUtil.showToast(MainActivity.this, module + "|create fail " + errMsg + " " + errMsg);
                }
            });
        }
    };
}
