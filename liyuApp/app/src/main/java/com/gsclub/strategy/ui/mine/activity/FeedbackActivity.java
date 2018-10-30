package com.gsclub.strategy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.mine.FeedbackContract;
import com.gsclub.strategy.model.bean.FeedbackImageBean;
import com.gsclub.strategy.presenter.mine.FeedbackPresenter;
import com.gsclub.strategy.ui.dialog.EditHeaderDialog;
import com.gsclub.strategy.ui.mine.adapter.SettingsFeedbackAdapter;
import com.gsclub.strategy.util.PhotoUtil;
import com.gsclub.strategy.util.StorageUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.SpaceItemDecoration;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity<FeedbackPresenter> implements FeedbackContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_content)
    EditText mContentEditText;
    @BindView(R.id.edit_contact)
    EditText mContactEditText;
    @BindView(R.id.tv_num)
    TextView mNumTextView;
    @BindView(R.id.tv_commit)
    TextView mCommitTv;

    private SettingsFeedbackAdapter mFeedbackAdapter;
    private List<String> mUrlList;// 显示的图片Url
    private List<String> mUploadUrlList;// 上传的图片Url
    public static final int MAX_IMAGES_COUNT = 3;
    private int mPosition;
    private static final int COMPRESS_START = 12;
    private static final int COMPRESS_SUCCESS = 13;
    private static final int COMPRESS_ERROR = 14;

    private FeedHandler mHandler;

    private static class FeedHandler extends Handler {
        private WeakReference<FeedbackActivity> mActivity;

        FeedHandler(FeedbackActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FeedbackActivity activity = mActivity.get();

            switch (msg.what) {
                case COMPRESS_START:
                    activity.showLoading();
                    break;
                case COMPRESS_SUCCESS:
                    activity.mPresenter.upLoadPhoto((File) msg.obj);
                    break;
                case COMPRESS_ERROR:
                    ToastUtil.showMsg("压缩图片失败");
                    break;
            }
        }
    }

    private EditHeaderDialog editHeaderDialog;
    private static final int SELECT_PHOTO = 1;
    private static final int REQUEST_CAMERA = 2;

    public static void start(Context context) {
        context.startActivity(new Intent(context, FeedbackActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getString(R.string.feedback));
    }

    @Override
    protected void initEventAndData() {
        mHandler = new FeedHandler(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setNestedScrollingEnabled(false);
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.line_space), 4));
        mRecyclerView.setAdapter(mFeedbackAdapter = new SettingsFeedbackAdapter(this));

        mUrlList = new ArrayList<>();
        mUrlList.add("");
        mFeedbackAdapter.setData(mUrlList);

        mFeedbackAdapter.setOnDeleteListener(new SettingsFeedbackAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                deleteData(position);
                if(mUploadUrlList != null && mUploadUrlList.size() != 0) {
                    mUploadUrlList.remove(position);
                }
            }
        });

        mFeedbackAdapter.setOnAddListener(new SettingsFeedbackAdapter.OnAddListener() {
            @Override
            public void onAdd(int position) {
                if (isMax()) return;
                // 打开相册
                showPhotoDialog();
                mPosition = position;
            }
        });

        mUploadUrlList = new ArrayList<>();

        mContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
                int len = s.toString().length();
                mNumTextView.setText(String.format("%s/1000", len));
            }
        });

//        getToken(true);
    }

    public void insertData(int position, String item) {
        if (item == null) return;
        if (mUrlList == null) mUrlList = new ArrayList<>();
        mUrlList.add(position, item);
        int size = mUrlList.size();
        //当添加图片数量达最大值时，将添加的item删除，保持最多3张图片
        if (size == MAX_IMAGES_COUNT + 1) {
            mUrlList.remove(size - 1);
        }
    }

    private void deleteData(int position) {
        //当添加图片数量达最大值时
        if (isMax()) {
            mUrlList.remove(position);
            mUrlList.add("");
            mFeedbackAdapter.setData(mUrlList);
        } else {
            mUrlList.remove(position);
            mFeedbackAdapter.setData(mUrlList);
        }
    }
    /**
     * @return 添加图片数量是否已达最大值
     */
    private boolean isMax() {
        int size = mUrlList.size();
        return size == MAX_IMAGES_COUNT && !TextUtils.isEmpty(mUrlList.get(size - 1));
    }


    @OnClick({R.id.tv_commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                mPresenter.feedback(mContentEditText.getText().toString(), mContactEditText.getText().toString(), getPicture());
                break;
        }
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(mContentEditText.getText())) {
            mCommitTv.setEnabled(false);
            return;
        }
        mCommitTv.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case SELECT_PHOTO:
                if (data == null) return;
                Uri uri = data.getData();
                if (uri == null) return;
//                Log.d("Tianma", "Uri = " + uri);
                String url = PhotoUtil.getRealPathFromUri(this, uri);
                compressOne(new File(url));
                break;
            case REQUEST_CAMERA:
                compressOne(new File(StorageUtil.getImageDir(), "camera.jpg"));
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
        Uri uri = PhotoUtil.fromFile(this, i, file);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(i, REQUEST_CAMERA);
    }

    @Override
    public void uploadSuccess(FeedbackImageBean bean) {
        insertData(mPosition, bean.getUrl());
        mFeedbackAdapter.setData(mUrlList);
        mUploadUrlList.add(bean.getSave_path());
    }

    private void showPhotoDialog() {
        if (editHeaderDialog == null) {
            editHeaderDialog = new EditHeaderDialog(this, "上传图片");
            editHeaderDialog.setOnSelectPicture(new EditHeaderDialog.OnSelectPicture() {
                @Override
                public void onSelect() {
                    mPresenter.checkSelectPermissions(new RxPermissions(FeedbackActivity.this));
                }
            });
            editHeaderDialog.setOnTakePicture(new EditHeaderDialog.OnTakePicture() {
                @Override
                public void onTakePicture() {
                    mPresenter.checkPermissions(new RxPermissions(FeedbackActivity.this));
                }
            });
        }
        editHeaderDialog.show();
    }

    private String getPicture() {
        if (mUploadUrlList == null || mUploadUrlList.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mUploadUrlList.size(); i++) {
            sb.append(mUploadUrlList.get(i));
            if (i != mUploadUrlList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private void compressOne(File file) {
        Luban.compress(this, file)
                .putGear(Luban.CUSTOM_GEAR)
                .setMaxHeight(1000)
                .setMaxWidth(1000)
                .setMaxSize(500)//kB
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        mHandler.sendEmptyMessage(COMPRESS_START);
                    }

                    @Override
                    public void onSuccess(File file) {
                        Message msg = new Message();
                        msg.what = COMPRESS_SUCCESS;
                        msg.obj = file;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHandler.sendEmptyMessage(COMPRESS_ERROR);
                    }
                });
    }
}
