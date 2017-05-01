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

        ILVLiveConfig liveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(liveConfig);
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
        String role = "LiveHost";
        @Override
        public void onClick(View v) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            int roomId = sp.getInt("room_id", 0);
            if (0 == roomId) {
                roomId = 1000;
            } else {
                roomId += 1;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("room_id", roomId);
            editor.commit();
            // 创建房间配置项
            ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                    controlRole(role)// 角色设置
                    .autoFocus(true)
                    .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)// 支持后台模式
                    .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)// 权限设置
                    .cameraId(ILiveConstants.FRONT_CAMERA)// 摄像头前置后置
                    .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);// 是否开始半自动接收
            //创建房间
            ILVLiveManager.getInstance().createRoom(roomId, hostOption, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    startActivity(new Intent(MainActivity.this, HostLiveActivity.class));
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ViewUtil.showToast(MainActivity.this, module + "|create fail " + errMsg + " " + errMsg);
                }
            });
        }
    };

    OnClickListener onClickBtnEnterRoom = new OnClickListener() {

        @Override
        public void onClick(View v) {
//            startActivity(new Intent(MainActivity.this, LiveListActivity.class));
            startActivity(new Intent(MainActivity.this, HostLiveActivity.class));
        }
    };
}
