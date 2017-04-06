package com.focustech.electronicbrand.biz;

/**
 * <基础业务类>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface Presenter<V> {
    void attachView(V view);

    void detachView(V view);

    String getName();
}
