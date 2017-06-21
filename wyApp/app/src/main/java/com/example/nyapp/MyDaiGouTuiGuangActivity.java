package com.example.nyapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classes.Share;
import com.example.classes.User;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.ShareUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class MyDaiGouTuiGuangActivity extends Activity implements OnClickListener {
    @BindView(R.id.qr_image)
    ImageView mQrImage;
    @BindView(R.id.textView_message)
    TextView mTextViewMessage;
    private User user;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int id;
    private Share mShare;
    private Share.DataBean mShareData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mytuiguang_fenxiang1);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", 0);
        ACache cache = ACache.get(this);
        user = (User) cache.getAsObject("user");
        imageLoader = ImageLoader.getInstance();


        options = new DisplayImageOptions.Builder()
                .displayer(new SimpleBitmapDisplayer())
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(
                        ImageScaleType.IN_SAMPLE_INT)
                .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShareDate();


    }

    /**
     * 加载分享的内容信息
     */
    private void loadShareDate() {
        Map<String, String> map = new TreeMap<>();
        map.put("loginKey", user.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("pro_id", String.valueOf(id));
        MyOkHttpUtils
                .getData(UrlContact.URL_PRODUCT_SHARE, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyProgressDialog.cancel();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyProgressDialog.cancel();
                        Gson gson = new Gson();
                        mShare = gson.fromJson(response, Share.class);
                        if (mShare.isResult()) {
                            mShareData = mShare.getData();
                            mTextViewMessage.setText(mShareData.getMessage());
                            imageLoader.displayImage(mShareData.getQRImage(),
                                    mQrImage,
                                    options);

                        }
                    }
                });
    }

    @OnClick({R.id.layout_back, R.id.button_tuiguang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.button_tuiguang:
                ShareUtil.initShareDate(MyDaiGouTuiGuangActivity.this, mShareData);
                break;
        }
    }
}
