package com.liyuu.strategy.presenter.mine;

import android.Manifest;
import android.text.TextUtils;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.mine.UserEditContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import javax.inject.Inject;
import io.reactivex.functions.Consumer;

public class UserEditPresenter extends RxPresenter<UserEditContract.View> implements UserEditContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public UserEditPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
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

    @Override
    public void updataHeader(File bitmap) {
        String url = UrlConfig.User_uploadHeadImg;
        post(true, mDataManager.uploadHeader(url, bitmap), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object data) {
                LogUtil.i("http response:" + "上传图片：" + data.toString());
                String imageUrl = data.toString();
                if(TextUtils.isEmpty(imageUrl)) return;
                ToastUtil.showMsg("上传成功");
                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL, imageUrl);
                mView.refreshHeader();
                RxBus.get().send(RxBus.Code.USER_HEADER_REFRESH);
            }
        });
    }

}
