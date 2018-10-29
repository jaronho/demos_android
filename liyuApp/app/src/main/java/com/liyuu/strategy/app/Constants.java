package com.liyuu.strategy.app;

import android.os.Environment;

import java.io.File;

public class Constants {

    //================= TYPE ====================


    //================= KEY ====================


    //================= PATH ====================

    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "codeest" + File.separator + "GeekNews";

    //================= INTENT ====================
    public static final String KEY_CLASSNAME = "classname";
    public static final String KEY_PARAMS = "params";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_INTENT_TO_NICKANME_STR = "key_intent_to_nickanme_str";
    public static final String KEY_BANK_INFO = "bank_info";
    public static final String KEY_API_LOGIN = "is_login_again";//接口返回1099
    public static final String KEY_API_SINGLE_SIGN_ON = "is_single_sign_on";////接口返回1098

    //================= INTENT REQUEST_CODE ====================
    public static final int REQUEST_BANK_LIST = 11;

    //================= 连连支付 ====================
    public static final int BASE_ID = 0;
    public static final int RQF_PAY = BASE_ID + 1;
    public static final int RQF_INSTALL_CHECK = RQF_PAY + 1;
    public static final String RET_CODE_SUCCESS = "0000";// 0000 交易成功
    public static final String RET_CODE_PROCESS = "2008";// 2008 支付处理中
    public static final String RESULT_PAY_SUCCESS = "SUCCESS";
    public static final String RESULT_PAY_PROCESSING = "PROCESSING";
}
