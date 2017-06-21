package com.example.nyapp.wxapi;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

//    private IWXAPI api;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
//        api.registerApp(Constants.APP_ID);
//        api.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    @Override
//    public void onReq(BaseReq req)
//    {
//    }
//
//    @Override
//    public void onResp(BaseResp resp)
//    {
//        int result = 0;
//
//        switch (resp.errCode)
//        {
//            case BaseResp.ErrCode.ERR_OK:
//                result = R.string.errcode_success;
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = R.string.errcode_cancel;
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = R.string.errcode_deny;
//                break;
//            default:
//                result = R.string.errcode_unknown;
//                break;
//        }
//
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//        finish();
//        overridePendingTransition(R.anim.change_in, R.anim.change_out);
//    }

}