package com.liyuu.strategy.presenter.mine;

import android.Manifest;
import android.text.TextUtils;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.mine.FeedbackContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.FeedbackImageBean;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;


public class FeedbackPresenter extends RxPresenter<FeedbackContract.View> implements FeedbackContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public FeedbackPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void upLoadPhoto(File image) {
        String url = UrlConfig.User_feedbackImage;
        post(mDataManager.uploadImage(url, image), new CommonSubscriber<FeedbackImageBean>(mView, url) {
                    @Override
                    public void onNext(FeedbackImageBean data) {
//                        LogUtil.i("http response:" + "上传图片：" + data.toString());
                        if(data == null) return;
                        ToastUtil.showMsg("上传成功");
                        mView.uploadSuccess(data);
                    }
                });
    }

    @Override
    public void feedback(String content, String contact, String picture) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        if(!TextUtils.isEmpty(contact)) {
            params.put("contact", contact);
        }
        if(!TextUtils.isEmpty(picture)) {
            params.put("picture", picture);
        }
        String url = UrlConfig.User_feedback;
        post(mDataManager.feedback(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object data) {
                        ToastUtil.showMsg("提交成功");
                        mView.finishUI();
                    }
                });
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        addSubscribe(false, rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            mView.takePhoto();
                        } else {
                            mView.showErrorMsg("拍照需要权限哦~");
                        }
                    }
                })
        );
    }

    @Override
    public void checkSelectPermissions(RxPermissions rxPermissions) {
        addSubscribe(false, rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            mView.selectPhoto();
                        } else {
                            mView.showErrorMsg("相册需要权限哦~");
                        }
                    }
                })
        );
    }
}
