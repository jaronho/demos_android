package com.example.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nyapp.R;
import com.example.util.MyGlideUtils;

/**
 * Created by NY on 2016/11/8.
 *
 */

public class MyAdDialog {

    private static final String TAG = "MyAdDialog";
    private static Dialog sDialog;
    public static int isShowAd = 0;


    private MyAdDialog() {

    }

    public static MyAdDialog getInstance(final Context context, String url, final int type, String clickUrl) {
        View view =  LayoutInflater.from(context).inflate(R.layout.view_ad_dialog, null);
        View clean = view.findViewById(R.id.clean);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.cancel();
            }
        });
        ImageView iv_ad = (ImageView) view.findViewById(R.id.iv_ad);
        //0:单品页;2:外部网页;3:无链接;4:优惠券领取;6:搜索页8:APP页面
        iv_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 6:
                        break;
                    case 8:
                        break;
                }
            }
        });
        Log.d("TAG", "getResult2: "+url);
        MyGlideUtils.loadNativeImage(context,url,iv_ad);
        sDialog = new Dialog(context,R.style.CustomDialog);
        sDialog.setContentView(view);

        sDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        sDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode==KeyEvent.KEYCODE_BACK) {
//                    Log.d(TAG, "onBackPressed: " );
//                    Intent i = new Intent(Intent.ACTION_MAIN);
//
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    i.addCategory(Intent.CATEGORY_HOME);
//
//                    context.startActivity(i);
//                }
//                return true;
//            }
//        });
        return new MyAdDialog();
    }

    public static void showAdDialog() {

        sDialog.show();
    }

    public  static void cancel() {
        sDialog.dismiss();
    }

//    public boolean isShowAdDialog() {
//        return isShowAdDialog;
//    }
}
