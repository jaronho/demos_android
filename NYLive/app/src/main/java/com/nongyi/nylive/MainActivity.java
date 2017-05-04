package com.nongyi.nylive;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaronho.sdk.utils.ViewUtil;
import com.nongyi.nylive.utils.NetHelper;
import com.nongyi.nylive.utils.NetHelper.Callback;
import com.nongyi.nylive.view.GuestLiveActivity;
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
    private Dialog mCreateRoomDialog = null;
    private Dialog mEnterRoomDialog = null;

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

        initCreateRoomDialog();
        initEnterRoomDialog();
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
                    Log.d("NYLive", "live login success");
                    ViewUtil.showToast(MainActivity.this, "登陆TSL成功");
                }
                @Override
                public void onError(String module, int errCode, String errMsg) {
                    Log.d("NYLive", "live login error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                    ViewUtil.showToast(MainActivity.this, "登陆TSL失败: " + module + " " + errCode + " " + errMsg);
                }
            });
        }
    };

    OnClickListener onClickBtnCreateRoom = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mCreateRoomDialog.isShowing()) {
                mCreateRoomDialog.show();
            }
        }
    };

    OnClickListener onClickBtnEnterRoom = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mEnterRoomDialog.isShowing()) {
                mEnterRoomDialog.show();
            }
        }
    };

    // 初始创建房间对话框
    private void initCreateRoomDialog() {
        mCreateRoomDialog = new Dialog(this, R.style.dialog);
        mCreateRoomDialog.setContentView(R.layout.dialog_create_room);
        final EditText editTextRoomId = (EditText)mCreateRoomDialog.findViewById(R.id.edittext_room_id);
        TextView tvSure = (TextView)mCreateRoomDialog.findViewById(R.id.btn_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomIdStr = editTextRoomId.getText().toString();
                if ("".equals(roomIdStr)) {
                    return;
                }
                int roomId = Integer.parseInt(roomIdStr);
                Bundle bundle = new Bundle();
                bundle.putInt("room_id", roomId);    // 房间id
                Intent intent = new Intent(MainActivity.this, HostLiveActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                mCreateRoomDialog.dismiss();
            }
        });
        TextView tvCancel = (TextView)mCreateRoomDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateRoomDialog.dismiss();
            }
        });
    }

    // 初始进入房间对话框
    private void initEnterRoomDialog() {
        mEnterRoomDialog = new Dialog(this, R.style.dialog);
        mEnterRoomDialog.setContentView(R.layout.dialog_enter_room);
        final EditText editTextHostId = (EditText)mEnterRoomDialog.findViewById(R.id.edittext_host_id);
        final EditText editTextRoomId = (EditText)mEnterRoomDialog.findViewById(R.id.edittext_room_id);
        TextView tvSure = (TextView)mEnterRoomDialog.findViewById(R.id.btn_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostId = editTextHostId.getText().toString();
                String roomIdStr = editTextRoomId.getText().toString();
                if ("".equals(hostId) || "".equals(roomIdStr)) {
                    return;
                }
                int roomId = Integer.parseInt(roomIdStr);
                Bundle bundle = new Bundle();
                bundle.putString("host_id", hostId);    // 主播id
                bundle.putInt("room_id", roomId);    // 房间id
                Intent intent = new Intent(MainActivity.this, GuestLiveActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                mEnterRoomDialog.dismiss();
            }
        });
        TextView tvCancel = (TextView)mEnterRoomDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEnterRoomDialog.dismiss();
            }
        });
    }
}
