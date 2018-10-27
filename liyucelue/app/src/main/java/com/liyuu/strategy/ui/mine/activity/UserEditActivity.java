package com.liyuu.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.mine.UserEditContract;
import com.liyuu.strategy.presenter.mine.UserEditPresenter;
import com.liyuu.strategy.ui.dialog.EditHeaderDialog;
import com.liyuu.strategy.ui.login.NicknameActivity;
import com.liyuu.strategy.util.PhotoUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.StorageUtil;
import com.liyuu.strategy.util.UserInfoUtil;
import com.liyuu.strategy.widget.CircleImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户资料详情/编辑界面
 */

public class UserEditActivity extends BaseActivity<UserEditPresenter> implements UserEditContract.View {
    @BindView(R.id.iv_head)
    CircleImageView imgHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;

    private Uri uriCamera;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {// 防止因内存不足此Activity被回收，导致UriCamera被清空
            uriCamera = savedInstanceState.getParcelable("uriCamera");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("uriCamera", uriCamera);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_user_edit;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.edit_info);
        showUserMessage();
    }

    @Override
    protected void initEventAndData() {

    }

    private void showUserMessage() {
        ImageLoader.loadHead(App.getInstance(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL), imgHeader);
        tvName.setText(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME));
        tvTel.setText(UserInfoUtil.invertTel(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER)));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    private EditHeaderDialog editHeaderDialog;
    private static final int SELECT_PHOTO = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int RESULT_CROP = 5;

    @OnClick({R.id.layout_head, R.id.layout_name, R.id.layout_tel, R.id.layout_pwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if (editHeaderDialog == null) {
                    editHeaderDialog = new EditHeaderDialog(this);
                    editHeaderDialog.setOnSelectPicture(new EditHeaderDialog.OnSelectPicture() {
                        @Override
                        public void onSelect() {
                            mPresenter.checkSelectPermissions(new RxPermissions(UserEditActivity.this));
                        }
                    });
                    editHeaderDialog.setOnTakePicture(new EditHeaderDialog.OnTakePicture() {
                        @Override
                        public void onTakePicture() {
                            mPresenter.checkPermissions(new RxPermissions(UserEditActivity.this));
                        }
                    });
                }
                editHeaderDialog.show();
                break;
            case R.id.layout_name:
                NicknameActivity.start(this);
                break;
            case R.id.layout_tel:
                EditPhoneActivity.start(this);
                break;
            case R.id.layout_pwd:
                EditPwdActivity.start(this);
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_TEL_REFRESH:
                String tel = UserInfoUtil.invertTel(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER));
                tvTel.setText(tel);
                break;
            case RxBus.Code.EDIT_NICKNAME_SUCCESS:
                tvName.setText(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case SELECT_PHOTO:
                if(data == null) return;
                Uri uri =  data.getData();
                if(uri == null) return;
                Log.d("Tianma", "Uri = " + uri);
                startPhotoZoom(uri);
                break;
            case REQUEST_CAMERA:
                if(uriCamera == null) return;
                startPhotoZoom(uriCamera);
                break;
            case RESULT_CROP:
                mPresenter.updataHeader(new File(StorageUtil.getImageDir(), "temp.jpg"));
                break;
        }
    }

    @Override
    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    public void takePhoto() {
        File file = new File(StorageUtil.getImageDir(), "camera.jpg");
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriCamera = PhotoUtil.fromFile(this, i, file);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
        startActivityForResult(i, REQUEST_CAMERA);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(StorageUtil.getImageDir(), "temp.jpg")));
        startActivityForResult(intent, RESULT_CROP);
    }

    @Override
    public void refreshHeader() {
        ImageLoader.loadHead(App.getInstance(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL), imgHeader);
    }
}
