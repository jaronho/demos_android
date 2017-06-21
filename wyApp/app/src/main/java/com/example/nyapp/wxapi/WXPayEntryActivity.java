package com.example.nyapp.wxapi;






import com.example.nyapp.MainActivity;
import com.example.nyapp.MyDingDanActivity;
import com.example.nyapp.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sourceforge.simcpux.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
//	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	private LinearLayout layout_back;
	private ImageButton btn_jixugouwu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_success);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        Intent intent = new Intent(WXPayEntryActivity.this, MyDingDanActivity.class);
        intent.putExtra("state", 0);
		intent.putExtra("title", "我的订单");
        startActivity(intent);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		 String msg = "";
	        if(resp.errCode == 0)
	        {
	            msg = "支付成功";
	            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
	        }
	        else if(resp.errCode == -1)
	        {
	            msg = "支付失败";
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);

	        }
	        else if(resp.errCode == -2)
	        {
	            msg = "已取消支付";
	            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
	        }
	        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle(R.string.app_tip);
//				builder.setMessage(msg);
//				builder.setPositiveButton("确定", null);
//				builder.show();
	        	Toast.makeText(WXPayEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
	        }
	}
}