package com.focustech.electronicbrand.biz;

/**
 * <基础业务类>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public abstract class BasePresenter<V extends IMvpView> implements Presenter<V> {
    protected V mvpView;

    public void attachView(V view) {
        mvpView = view;
    }

    @Override
    public void detachView(V view) {
        mvpView = null;
    }

    @Override
    public String getName() {
        return mvpView.getClass().getSimpleName();
    }
}
