package com.example.nyapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.adapter.TextAdapter;
import com.example.classes.BaseBean;
import com.example.classes.User;
import com.example.classes.UserInfoBean;
import com.example.util.CustomDialog;
import com.example.util.GsonUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.example.view.DividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ASimpleCache.org.afinal.simplecache.ACache;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 个人资料
 */
public class UserInfoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.btn_personal_edit)
    Button mBtnPersonalEdit;
    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.rg_sex)
    RadioGroup mRgSex;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    @BindView(R.id.rb_man)
    RadioButton mRbMan;
    @BindView(R.id.rb_woman)
    RadioButton mRbWoman;
    private User mUser;
    private UserInfoBean.DataBean mUserInfoData;
    private List<String> mJobList;
    private boolean mIsEdit;
    private CustomDialog mJobDialog;
    private Uri tempUri;
    private File mPicFile;
    private String mPicPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        ACache cache = ACache.get(this);
        mUser = (User) cache.getAsObject("user");
        initView();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_GET_USER_INFO, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            UserInfoBean userInfoBean = GsonUtils.getInstance().fromJson(response, UserInfoBean.class);
                            if (userInfoBean.isResult()) {
                                mUserInfoData = userInfoBean.getData();
                                if (mUserInfoData != null) {
                                    setUserInfoData();
                                }
                            }
                        }
                    }
                });
    }

    private void setUserInfoData() {
        mUser = mUserInfoData.getUserDetail();
        mJobList = mUserInfoData.getJobList();

        MyGlideUtils.loadCircleImage(this, mUser.getHead_Img(), mIvHeadPortrait);
        mTvUserName.setText(mUser.getUser_Name());
        mEtNickname.setText(mUser.getNick_Name());
        mTvPhone.setText(mUser.getMobile());
        mEtEmail.setText(mUser.getEmail());
        mTvJob.setText(mUserInfoData.getJobName());
        int sex = mUser.getSex();
        mRbMan.setChecked(sex == 1);
        mRbWoman.setChecked(sex != 1);
        mTvSex.setText(sex == 1 ? "男" : "女");
        mTvAddress.setText(mUserInfoData.getCountyName());
    }

    @Override
    public void initView() {
        mTvUserName.setEnabled(mIsEdit);
        mEtNickname.setEnabled(mIsEdit);
        mTvPhone.setEnabled(mIsEdit);
        mEtEmail.setEnabled(mIsEdit);
        mTvJob.setEnabled(mIsEdit);
        mTvSex.setEnabled(mIsEdit);
        mTvAddress.setEnabled(mIsEdit);
        mIvHeadPortrait.setEnabled(mIsEdit);
    }

    @OnClick({R.id.layout_back, R.id.btn_personal_edit, R.id.iv_head_portrait, R.id.tv_job, R.id.rb_man, R.id.rb_woman, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_personal_edit:
                mIsEdit = true;
                setDataState();
                break;
            case R.id.iv_head_portrait:
                initPermissions();//动态权限申请
                break;
            case R.id.tv_job:
                if (mJobList != null) {
                    showJobDialog();
                }
                break;
            case R.id.rb_man:
                mRbMan.setChecked(true);
                mRbWoman.setChecked(false);
                mTvSex.setText("男");
                break;
            case R.id.rb_woman:
                mRbMan.setChecked(false);
                mRbWoman.setChecked(true);
                mTvSex.setText("女");
                break;
            case R.id.btn_save:
                int l = mEtNickname.getText().toString().toCharArray().length;
                if (l >= 2 && l <= 10) {
                    if (isEmail(mEtEmail.getText().toString())) {
                        mIsEdit = false;
                        setDataState();
                        saveUserInfo();
                    } else {
                        MyToastUtil.showShortMessage("Email格式填写错误,请重新填写!");
                    }
                } else {
                    MyToastUtil.showShortMessage("昵称长度必须为2-10个字符！");
                }
                break;
        }
    }

    //显示职业列表弹窗
    private void showJobDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_job_item, null);
        RecyclerView rcy_job = (RecyclerView) view.findViewById(R.id.rcy_job);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.divider_bg);

        rcy_job.setLayoutManager(new LinearLayoutManager(this));
        rcy_job.addItemDecoration(dividerItemDecoration);
        rcy_job.setAdapter(new TextAdapter(mJobList));
        rcy_job.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTvJob.setText(mJobList.get(position));
                mJobDialog.dismiss();
            }
        });

        mJobDialog = new CustomDialog(this, "请选择您的职业", view, null, null);
        mJobDialog.setCancelable(false);
        mJobDialog.show();
    }

    //保存用户信息
    private void saveUserInfo() {
        int position = 0;
        for (int i = 0; i < mJobList.size(); i++) {
            if (mTvJob.getText().toString().equals(mJobList.get(i))) {
                position = i;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("loginKey", mUser.getUser_Name());
        map.put("deviceId", MyApplication.sUdid);
        map.put("Nick_Name", mEtNickname.getText().toString());
        map.put("Email", mEtEmail.getText().toString());
        map.put("Job", String.valueOf(position));
        map.put("Sex", mRbMan.isChecked() ? "1" : "2");
        map.put("Headimg", mPicPath == null ? "" : mPicPath);

        MyOkHttpUtils
                .postData(UrlContact.URL_SAVE_USER_INFO, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            BaseBean baseBean = GsonUtils.getInstance().fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                MyToastUtil.showShortMessage(baseBean.getMessage());
                            }
                        }
                    }
                });
    }

    //设置数据可编辑状态
    private void setDataState() {
        if (mIsEdit && mEtNickname.getText().toString().equals("")) {
            mEtNickname.setEnabled(mIsEdit);
        } else {
            mEtNickname.setEnabled(false);
        }
        mIvHeadPortrait.setEnabled(mIsEdit);
        mEtEmail.setEnabled(mIsEdit);
        mTvJob.setEnabled(mIsEdit);
        mBtnSave.setVisibility(mIsEdit ? View.VISIBLE : View.GONE);
        mRgSex.setVisibility(mIsEdit ? View.VISIBLE : View.GONE);
        mTvSex.setVisibility(!mIsEdit ? View.VISIBLE : View.GONE);
        mBtnPersonalEdit.setVisibility(!mIsEdit ? View.VISIBLE : View.GONE);

    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        if (email == null || email.equals("")) {
            return true;
        } else {
            String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(email);
            return m.matches();
        }
    }

    //显示选择图片弹窗
    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"拍照", "选择本地照片"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPicFile = new File(Environment.getExternalStorageDirectory(), mUser.getUser_Name() + "headImage.jpg");
                if (!mPicFile.exists() && !mPicFile.isDirectory()) {
                    System.out.println("目录或文件不存在！");
                    try {
                        mPicFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                switch (which) {
                    case 0: // 拍照
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            tempUri = Uri.fromFile(mPicFile);
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                            startActivityForResult(openCameraIntent, 0);
                        } else {
                            tempUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.example.nyapp.fileprovider", mPicFile);
                            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//将拍取的照片保存到指定URI
                            startActivityForResult(openCameraIntent, 0);
                        }
                        break;
                    case 1: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
//                        openAlbumIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, 1);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                break;
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                }
                break;
            case 2:
                setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                break;
        }
    }

    //裁剪图片方法实现
    protected void startPhotoZoom(Uri uri) {
        //有些损毁的图片，会导致闪退
        if (uri.toString().equals("file:///")) {
            MyToastUtil.showShortMessage("此文件不可用");
            return;
        }
        MyProgressDialog.show(this, true, true);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //这里做了判断  如果图片大于 1024KB 就进行压缩
        if (bitmap.getRowBytes() * bitmap.getHeight() > 1024 * 1024) {
            bitmap = compressImage(bitmap);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        }
        MyProgressDialog.cancel();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 160);
        intent.putExtra("outputY", 160);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 2);
    }

    //保存裁剪之后的图片数据
    protected void setImageToView(Intent data) {
        if (data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                MyGlideUtils.loadCircleBitmap(this, photo, mIvHeadPortrait);
                uploadPic(photo);
            } else {
                MyToastUtil.showShortMessage("图片保存失败!");
            }
        }
    }

    //质量压缩方法
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    private void uploadPic(Bitmap bitmap) {
        if (!mPicFile.exists() && !mPicFile.isDirectory()) {
            System.out.println("目录或文件不存在！");
            try {
                mPicFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mPicFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpUtils
                .post()
                .url(UrlContact.URL_UPLOAD_AVATAR)
                .addFile("Filedata", mPicFile.getName(), mPicFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            BaseBean baseBean = GsonUtils.getInstance().fromJson(response, BaseBean.class);
                            if (baseBean.isResult()) {
                                mPicPath = baseBean.getMessage();
                            }
                        }
                    }
                });
    }

    //动态权限申请
    private void initPermissions() {
        //所要申请的权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            showChoosePicDialog();
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "上传头像必须的权限", 0, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        MyToastUtil.showShortMessage("权限申请成功");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        MyToastUtil.showShortMessage("上传头像需要权限,请手动申请权限!");
    }

}
